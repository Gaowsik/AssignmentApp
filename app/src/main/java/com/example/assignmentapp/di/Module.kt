package com.example.assignmentapp.di

import com.example.assignmentapp.data.source.remote.network.ApiKeyInterceptor
import com.example.assignmentapp.data.source.remote.NewsApi
import com.example.assignmentapp.data.source.remote.network.NetworkConfig.TIMEOUT
import com.example.assignmentapp.data.source.remote.network.createAppApiClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        val apiKeyInterceptor = ApiKeyInterceptor(BuildConfig.NEWS_API_KEY)

        return OkHttpClient.Builder().connectTimeout(TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT, TimeUnit.SECONDS)
            .addInterceptor(apiKeyInterceptor)
            .addInterceptor(loggingInterceptor)
            .build()
    }


    @Provides
    @Singleton
    fun provideUserApi(okHttpClient: OkHttpClient): NewsApi {
        val baseUrl = BuildConfig.BASE_API_URL
        val wMSRoleApiClient = createAppApiClient(baseUrl, okHttpClient)
        return wMSRoleApiClient.create(NewsApi::class.java)
    }
}