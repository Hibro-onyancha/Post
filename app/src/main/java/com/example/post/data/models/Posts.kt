package com.example.post.data.models

import kotlinx.serialization.Serializable


@Serializable
data class Posts(
    val text: String,
    val name: String,
    val about: String,
    val id: String = "",
    val reactions: List<Reactions> = listOf()
)
