package com.example.post.domain.di

import com.example.post.data.repo.PostRepo
import com.example.post.data.repo.SocketsRepo
import com.example.post.domain.impl.PostRepoImpl
import com.example.post.domain.impl.SocketsRepoImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.features.logging.Logging
import io.ktor.client.features.websocket.WebSockets
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object PostsModules {

    @Provides
    @Singleton
    fun provideHttpClient(): HttpClient {
        return HttpClient(CIO) {
            install(Logging)
            install(WebSockets)
            install(JsonFeature) {
                serializer = KotlinxSerializer()
            }
        }
    }

    @Provides
    @Singleton
    fun providePostsRepo(client: HttpClient): PostRepo {
        return PostRepoImpl(client)
    }

    @Provides
    @Singleton
    fun provideSocketsRepo(client: HttpClient): SocketsRepo {
        return SocketsRepoImpl(client)
    }

}