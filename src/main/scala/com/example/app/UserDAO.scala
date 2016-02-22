package com.example.app

import com.mongodb.casbah.Imports._
import com.novus.salat.annotations._
import com.novus.salat.dao.SalatDAO
import com.example.context._


case class User(@Key("_id") id: Int, username: String, password: String)

object UserDAO extends SalatDAO[User, Int](collection = MongoConnection()("myBlog")("users")) {

  def add(username: String, password: String) = {
    val user = MongoDBObject("username" -> username,
                             "password" -> password)
    collection += user
  }

  def findUser(username: String): Option[MongoDBObject] = {
    for (user <- collection.findOne(MongoDBObject("username" -> username))) yield user
  }

  def validateLogin(username: String, password: String): Option[MongoDBObject] = {
    val user = findUser(username)

    user match {
      case x if x.isEmpty => None
      case x if x.isDefined && password != x.get("password") =>  println("user password is not a match")
                                                                  None
      case _ => user
    }
  }
}
