package com.example.post.data.models

import kotlinx.serialization.Serializable

@Serializable
data class Reactions(
    val id: String = "",
    val reaction: String,
    val number: String
) {

}
