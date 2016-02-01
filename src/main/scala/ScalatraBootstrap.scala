import com.example.app._
import com.mongodb.casbah.Imports._
import org.scalatra._
import javax.servlet.ServletContext

class ScalatraBootstrap extends LifeCycle {

  val mongoClient = MongoClient("localhost", 27017)

  override def init(context: ServletContext) {
    val posts = mongoClient("blog")("posts")

    context.mount(new MyScalatraServlet(posts), "/*")
  }

  override def destroy(context: ServletContext) {
    mongoClient.close
  }
}

