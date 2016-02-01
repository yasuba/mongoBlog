package com.example.app

import org.scalatest.BeforeAndAfter
import org.scalatest.Matchers
import org.scalatest.WordSpec


class PostSpec extends WordSpec with Matchers with BeforeAndAfter {

  "a post" should {
    "have a name" in {
     val post = Post(1, "new post", "this is a post", "")
    }
  }
}
