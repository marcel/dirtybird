namespace java com.twitter.dirtybird.thrift
namespace rb Dirtybird

/**
 * It's considered good form to declare an exception type for your service.
 * Thrift will serialize and transmit them transparently.
 */
exception DirtybirdException {
  1: string description
}

struct Document {
  1: required i64 id
  2: required string text
}

struct Query {
  1: required string term
}

service DirtybirdService {
//  string get(1: string key) throws(1: DirtybirdException ex)
//
//  void put(1: string key, 2: string value)
//
//  bool exists(1: string key)

  // TODO Return some result struct once things are more fleshed out
  bool add(1: string text) throws(1: DirtybirdException ex)

  list<Document> search(1: Query query)

  void dumpIndex()
}
