package com.example.app

import java.text.SimpleDateFormat
import com.novus.salat.annotations._
import com.novus.salat.dao._
import com.mongodb.casbah.Imports._
import com.example.context._

case class Post(@Key("_id") id: Int, name: String, post: String, createdAt: SimpleDateFormat)

object PostDAO extends SalatDAO[Post, Int](collection = MongoConnection()("blog")("posts")) {

  def allBlogs():List[Map[String, String]] = {
      val cursor = collection.find().toList
      cursor.map(doc => Map("author" -> doc("author").toString, "post" -> doc("post").toString, "dateAdded" -> doc
      ("dateAdded").toString))
    }

  def add(author: String, post: String) = {
    val format = new java.text.SimpleDateFormat("dd-MM-yyyy")
    val date = format.parse(format.format(new java.util.Date()))
    val builder = MongoDBObject.newBuilder
    builder += "author" -> author
    builder += "post" -> post
    builder += "dateAdded" -> date
    collection += builder.result.asDBObject
  }
}


