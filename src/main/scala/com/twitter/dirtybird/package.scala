package com.twitter

import scala.collection.mutable
import scala.collection.immutable.TreeSet

package object dirtybird {
  type DocumentId = Long
  type Term = String

  object PostingList {
    val ordering = new Ordering[DocumentId] {
      def compare(d1: DocumentId, d2: DocumentId) = {
        d1.compare(d2)
      }
    }
  }

  type PostingList  = TreeSet[DocumentId]
  type Index = mutable.HashMap[Term, PostingList]
}

