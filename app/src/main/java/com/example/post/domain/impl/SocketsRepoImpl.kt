package com.example.post.domain.impl

import com.example.post.data.models.Posts
import com.example.post.data.repo.SocketsRepo
import com.example.post.utils.Resource
import io.ktor.client.HttpClient
import io.ktor.client.features.websocket.webSocketSession
import io.ktor.client.request.url
import io.ktor.http.cio.websocket.Frame
import io.ktor.http.cio.websocket.WebSocketSession
import io.ktor.http.cio.websocket.close
import io.ktor.http.cio.websocket.readText
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.isActive
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class SocketsRepoImpl(
    private val client: HttpClient
) : SocketsRepo {
    private var socket: WebSocketSession? = null

    override suspend fun initiateSession(username: String): Resource<Unit> {
        return try {
            socket = client.webSocketSession {
                url("${SocketsRepo.EndPoints.SocketEndPoint.url}?username=$username")
            }
            if (socket?.isActive == true) {
                Resource.Success(Unit)
            } else Resource.Error("Couldn't establish a connection.")
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(e.localizedMessage ?: "Unknown error")
        }
    }

    override suspend fun closeSession() {
        socket?.close()
    }

    override suspend fun observePost(): Flow<Posts> {
        return try {
            socket?.incoming?.receiveAsFlow()?.filter { it is Frame.Text }?.map {
                val json = (it as? Frame.Text)?.readText() ?: ""
                val post = Json.decodeFromString<Posts>(json)
                post
            } ?: flow { }
        } catch (e: Exception) {
            e.printStackTrace()
            flow { }
        }
    }

    override suspend fun sendPost(posts: Posts) {
        //convert this post and send it as a string the server will do the appropriate serialization
        val postToSend = Json.encodeToString(posts)/*todo*//*might create an error*/
        try {
            socket?.send(Frame.Text(postToSend))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}