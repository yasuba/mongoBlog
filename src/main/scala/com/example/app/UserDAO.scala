package com.example.app

import com.mongodb.casbah.Imports._
import com.novus.salat.annotations._
import com.novus.salat.dao.SalatDAO
import com.example.context._


case class User(@Key("_id") id: Int, username: String, password: String)

object UserDAO extends SalatDAO[User, Int](collection = MongoConnection()("blog")("users")) {

  def add(username: String, password: String) = {
    val builder = MongoDBObject.newBuilder
    builder += "username" -> username
    builder += "password" -> password
    collection += builder.result.asDBObject
  }

  def findUser(username: String): Option[MongoDBObject] = {
    for (user <- collection.findOne(MongoDBObject("username" -> username))) yield user
  }

  def validateLogin(username: String, password: String): Option[MongoDBObject] = {
    val user = findUser(username).get
    if (password != user("password")) {
      println("user password is not a match")
      None
    } else Some(user)
  }
}
