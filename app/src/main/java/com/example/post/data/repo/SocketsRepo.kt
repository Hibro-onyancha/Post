package com.example.post.data.repo

import com.example.post.data.models.Posts
import com.example.post.utils.Resource
import kotlinx.coroutines.flow.Flow

interface SocketsRepo {
    suspend fun initiateSession(username: String): Resource<Unit>
    suspend fun closeSession()
    suspend fun observePost(): Flow<Posts>
    suspend fun sendPost(posts: Posts)

    companion object {
        const val BASE_URL = "ws://192.168.93.157:8080"
    }

    sealed class EndPoints(val url: String) {
        object SocketEndPoint : EndPoints("$BASE_URL/post-sockets")
    }

}