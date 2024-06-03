package com.example.post.presentations.screens

import com.example.post.data.models.Posts

data class PostsScreenState(
    val posts: List<Posts> = listOf(),
    val isLoading: Boolean = false
)
