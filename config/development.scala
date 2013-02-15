import com.twitter.conversions.time._
import com.twitter.logging.config._
import com.twitter.ostrich.admin.config._
import com.twitter.dirtybird.profile._

// development mode.
new DirtybirdServiceProfile {

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
}
