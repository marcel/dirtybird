import com.twitter.dirtybird.profile._

//mvn -pl :gizmoduck-server exec:java -Dexec.args="-f gizmoduck/server/config/development.scala"

// development mode.
new DirtybirdServiceProfile //{

//  loggers =
//    new LoggerConfig {
//      level = Level.DEBUG
//      handlers = new FileHandlerConfig {
//        filename = "searchbird.log"
//        roll = Policy.SigHup
//      }
//    } :: new LoggerConfig {
//      node = "stats"
//      level = Level.INFO
//      useParents = false
//      handlers = new FileHandlerConfig {
//        filename = "stats.log"
//        formatter = BareFormatterConfig
//      }
//    }
//}
