package com.twitter.dirtybird

import com.twitter.util._
import thriftscala.{DirtybirdService => ThriftDirtybirdService, Document, Query, DirtybirdException}
import com.twitter.finagle.stats.StatsReceiver
import com.twitter.logging.Logger

class DirtybirdService(
  statsReceiver: StatsReceiver
) extends ThriftDirtybirdService.FutureIface {
  val log = Logger.get(getClass.getSimpleName)

  val documentIndex       = new DocumentIndex
  val documentIdGenerator = new SimpleDocumentIdGenerator
  val tokenizer           = new SimpleTokenizer(new SimpleTermNormalizer)

  // TODO Implement: Makes "add" idempotent by computing a checksum of every document added
  // and associating the assigned document id with that checksum
  // TODO Make thread safe
  // val documentChecksums = mutable.HashMap[]

  def add(text: String) = {
    val documentId = documentIdGenerator()

    tokenizer(text) foreach {
      documentIndex.add(_, documentId)
    }

    Future.value(true)
  }

  def search(query: Query) = {
    Future.value(
      documentIndex.get(query.term) map { postingList =>
        postingList.toSeq map { Document(_, "") }
      } getOrElse(Nil)
    )
  }

  def dumpIndex() = {
    println(documentIndex)
    Future.Unit
  }
}

trait DocumentIdGenerator {
  def apply(): DocumentId
}

class SimpleDocumentIdGenerator extends DocumentIdGenerator {
  def apply() = {
    // TODO Not threadsafe or partitionable yet (assuming doc ids needs to be globally unique
    Time.now.inNanoseconds
  }
}

trait TermNormalizer {
  def apply(term: Term): Term
}

class SimpleTermNormalizer extends TermNormalizer {
  def apply(term: Term) = {
    term.toLowerCase.replaceAll("""\W""", "")
  }
}

trait Tokenizer {
  def apply(text: String): Seq[Term]
}

class SimpleTokenizer(termNormalizer: TermNormalizer) extends Tokenizer {
  def apply(text: String) = {
    text.split(" ") map { termNormalizer(_) }
  }
}

class DocumentIndex(index: Index = new Index) {
  val postingListFactory = () => new PostingList()(PostingList.ordering)

  def add(term: Term, documentId: DocumentId) = {
    val postingListForTerm = index.getOrElseUpdate(term, postingListFactory())
    val updatedPostingList = term -> (postingListForTerm + documentId)
    index += updatedPostingList
  }

  def get(term: Term) = {
    index.get(term)
  }

  override def toString = {
    "DocumentIndex: index=%s".format(index.toString())
  }
}