package com.example.post.presentations.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun PostScreen(
    model: PostsViewModel = hiltViewModel()
) {

    val uiState = model.uiState.collectAsState().value
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(key1 = lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_START) {
                model.initiateConnection()
            } else if (event == Lifecycle.Event.ON_STOP) {
                model.disconnect()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
    model.getAllMessages()

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        if (!uiState.isLoading) {
            Scaffold(
                floatingActionButton = {
                    Button(onClick = {
                        model.createNewPost()
                    }) {
                        Text(text = "new post 😎")
                    }
                }
            ) { _ ->
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    itemsIndexed(uiState.posts) { index, post ->
                        Column(
                            modifier = Modifier
                                .fillMaxWidth(0.95f)
                                .clip(RoundedCornerShape(10.dp))
                                .padding(5.dp)
                                .horizontalScroll(rememberScrollState()),
                            horizontalAlignment = Alignment.Start
                        ) {
                            Text(
                                text = "$index\npost:  ${post.text}\nabout: ${post.about}\nid: ${post.id}   name:    ${post.name} ",
                                fontSize = 20.sp,
                                color = MaterialTheme.colorScheme.onBackground,
                                fontWeight = FontWeight.ExtraBold
                            )
                            Spacer(modifier = Modifier.height(5.dp))
                            post.reactions.forEachIndexed { reactIndex, reaction ->
                                Text(
                                    text = "$reactIndex\nreaction:  ${reaction.reaction}\nnumber: ${reaction.number}\nid: ${reaction.id}",
                                    fontSize = 15.sp,
                                    color = Color.Red,
                                    fontWeight = FontWeight.Light
                                )
                            }
                        }
                    }
                }

            }
        } else {
            Text(text = "loading", fontSize = 30.sp, modifier = Modifier.align(Alignment.Center))
        }
    }
}