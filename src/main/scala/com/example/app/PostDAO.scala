package com.example.app

import java.text.SimpleDateFormat
import java.util.Date
import com.novus.salat.annotations._
import com.novus.salat.dao._
import com.mongodb.casbah.Imports._
import com.example.context._
import com.novus.salat._

case class Post(@Key("_id") id: ObjectId = new ObjectId, author: String, postBody: String, createdAt: Date)

object PostDAO extends SalatDAO[Post, ObjectId](collection = MongoConnection()("myBlog")("posts")) {

  def allBlogs():List[Map[String, String]] = {
    val dateFormat = new SimpleDateFormat("dd/MM/yyyy")
    val cursor = collection.find().toList
    cursor.map(doc => Map("_id" -> doc("_id").toString,
                          "author" -> doc("author").toString,
                          "postBody" -> doc("postBody").toString,
                          "createdAt" -> dateFormat.format(doc("createdAt").asInstanceOf[Date])))
    }

  def add(author: String, postBody: String) = {
    val date = new Date()
    val post: Post = Post(new ObjectId, author, postBody, date)
    val dbo = grater[Post].asDBObject(post)
    collection += dbo
  }

  def findOne(id: ObjectId): Option[MongoDBObject] = {
    for (post <- collection.findOne(MongoDBObject("_id" -> id))) yield post
  }
}


