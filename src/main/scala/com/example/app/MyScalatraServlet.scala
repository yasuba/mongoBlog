package com.example.app

import com.mongodb.casbah.Imports._
import org.scalatra._
import scalate.ScalateSupport
import scala.util.Random

class MyScalatraServlet(posts: MongoCollection) extends MongodbuniversityStack with
ScalateSupport {

  get("/") {
    val blogs:List[Map[String, String]] = PostDAO.allBlogs()
    val cookie = cookies.get("session")
    val username = SessionDAO.getUsername(cookie.getOrElse(""))

    contentType = "text/html"
    ssp("/index", "blogs" -> blogs, "username" -> username)
  }

  get("/login") {
    contentType = "text/html"
    ssp("/login")
  }

  post("/login") {
    val username:String = params("username")
    val password:String = params("password")
    val userRecord = UserDAO.validateLogin(username, password)
    if (userRecord.isDefined) {
      val sessionId = SessionDAO.startSession(username)
      cookies.update("session", sessionId)
      redirect("/welcome")
    } else redirect("/")
  }

  get("/signup") {
    contentType = "text/html"
    ssp("/signup")
  }

  post("/signup") {
    val username: String = params("username")
    val password: String = params("password")
    val verify: String = params("verify")

    UserDAO.add(username, password)

    val sessionId: String = if(validateSignup(username: String, password: String, verify: String)) SessionDAO.startSession(username) else "no session id"
    cookies.update("session", sessionId)

    redirect("/welcome")
  }

  get("/logout") {
    val cookie = cookies.get("session").getOrElse("None")
//    cookies.delete(cookie)
    cookies.update("session", "none")
    redirect("/")
  }

  get("/welcome") {
    val cookie = cookies.get("session").getOrElse("None")
    val username = SessionDAO.getUsername(cookie)

    if (username == "none") {
      println("welcome: can't identify user...redirecting to signup")
      redirect("/signup")
    } else {
      contentType = "text/html"
      ssp("/welcome", "username" -> username)
    }
  }

  post("/addPost") {
    val cookie:String = cookies.get("session").getOrElse("None")
    val author:String = SessionDAO.getUsername(cookie)
    val postBody:String = params("postBody")

    PostDAO.add(author, postBody)
    redirect("/")
  }

  get("/editPost") {
    val postId = params("postId")
    val post = PostDAO.findOne(postId)
    println(post)
    ssp("/editPost", "post" -> post)
  }

  def validateSignup(username: String, password: String, verify: String): Boolean = password == verify

}
