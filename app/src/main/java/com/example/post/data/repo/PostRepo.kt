package com.example.post.data.repo

import com.example.post.data.models.Posts

interface PostRepo {

    //here we are only getting all what has been stored in the database
    //in the implementation you will have to get the instance of the database from a http endpoint
    suspend fun getAllPosts(): Pair<List<Posts>, String>

    //get the base url
    companion object {
        const val BASE_URL = "http://192.168.93.157:8080"
    }

    sealed class EndPoints(val url: String) {
        object GetAllPosts : EndPoints("$BASE_URL/posts")
    }


}