package com.example.post.presentations.screens

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.post.data.models.Posts
import com.example.post.data.models.Reactions
import com.example.post.data.repo.PostRepo
import com.example.post.data.repo.SocketsRepo
import com.example.post.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostsViewModel @Inject constructor(
    private val postRepo: PostRepo,
    private val socketsRepo: SocketsRepo
) : ViewModel() {
    private val _uiState = MutableStateFlow(PostsScreenState())
    val uiState = _uiState.asStateFlow()
    fun initiateConnection() {
        viewModelScope.launch {
            getAllMessages()
            val result = socketsRepo.initiateSession("hibro")
            when (result) {
                is Resource.Success -> {
                    socketsRepo.observePost()
                        .onEach { message ->
                            val newPosts = _uiState.value.posts.toMutableList().apply {
                                add(0, message)
                            }
                            _uiState.update {
                                it.copy(
                                    posts = newPosts
                                )
                            }
                        }.launchIn(viewModelScope)
                }

                is Resource.Error -> {
                    println("error initiating the connection")
                }
            }
        }
    }

    fun disconnect() {
        viewModelScope.launch {
            socketsRepo.closeSession()
        }
    }

    fun getAllMessages() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = true
                )
            }
            val (list, string) = postRepo.getAllPosts()
            _uiState.update {
                it.copy(
                    isLoading = false,
                    posts = _uiState.value.posts + list
                )
            }
            Log.e("all-posts", string)

        }
    }

    fun createNewPost() {
        viewModelScope.launch {
            val reactionsList = listOf(
                Reactions(reaction = "Like", number = "5"),
                Reactions(reaction = "Love", number = "10"),
                Reactions(reaction = "Wow", number = "3")
            )

            val post = Posts(
                text = "This is a new post with reactions",
                name = "hibro",
                about = "Example about field",
                reactions = reactionsList
            )
            socketsRepo.sendPost(post)
        }

    }

    override fun onCleared() {
        super.onCleared()
        disconnect()
    }
}