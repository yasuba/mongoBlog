package com.example.app

import java.text.SimpleDateFormat
import com.novus.salat.annotations._
import com.novus.salat.dao._
import com.mongodb.casbah.Imports._
import com.example.context._

case class Post(@Key("_id") id: Int, name: String, postBody: String, createdAt: SimpleDateFormat)

object PostDAO extends SalatDAO[Post, Int](collection = MongoConnection()("myBlog")("posts")) {

  def allBlogs():List[Map[String, String]] = {
      val cursor = collection.find().toList
      cursor.map(doc => Map("author" -> doc("author").toString,
                            "postBody" -> doc("postBody").toString,
                            "dateAdded" -> doc("dateAdded").toString))
    }

  def add(author: String, postBody: String) = {
    val format = new java.text.SimpleDateFormat("dd-MM-yyyy")
    val date = format.format(new java.util.Date())
    val post = MongoDBObject("author" -> author,
                             "postBody" -> postBody,
                             "dateAdded" -> date)
    collection += post
  }
}


