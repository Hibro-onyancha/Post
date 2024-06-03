package com.example.post.domain.impl

import com.example.post.data.models.Posts
import com.example.post.data.repo.PostRepo
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class PostRepoImpl(
    private val client: HttpClient
) : PostRepo {
    override suspend fun getAllPosts(): Pair<List<Posts>, String> {
        return try {
            Pair(
                client.get<List<Posts>>(PostRepo.EndPoints.GetAllPosts.url),
                "successful retrieved all posts"
            )

        } catch (e: Exception) {
            Pair(emptyList(), "$e")

        }
    }
}