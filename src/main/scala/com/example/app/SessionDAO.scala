package com.example.app

import com.mongodb.casbah.Imports._
import com.novus.salat.annotations._
import com.novus.salat.dao.SalatDAO
import com.example.context._

import scala.util.Random

case class Session(@Key("_id") id: Int, session: String)

object SessionDAO extends SalatDAO[Session, Int](collection = MongoConnection()("myBlog")("sessions")) {

  def startSession(username: String): String = {
    val sessionId = randomString(10)
    val session = MongoDBObject("username" -> username,
                                "sessionId" -> sessionId)
    collection += session
    sessionId
  }

  def randomString(n: Int): String = {
    n match {
      case 1 => Random.nextPrintableChar.toString
      case _ => Random.nextPrintableChar.toString ++ randomString(n-1).toString
    }
  }

    def getUsername(sessionId: String) = {
      val session = getSession(sessionId)
      session.getOrElse("none")
    }

  def getSession(sessionId: String) = {
//    if (sessionId == "None") "Error"
    val id: DBObject = MongoDBObject("sessionId" -> sessionId)
    for (x <- collection.findOne(id)) yield x("username").toString
  }

}


