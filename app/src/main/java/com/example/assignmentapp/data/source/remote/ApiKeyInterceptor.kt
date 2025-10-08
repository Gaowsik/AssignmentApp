package com.example.assignmentapp.data.source.remote


import okhttp3.Interceptor

class ApiKeyInterceptor(private val apiKey: String) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
        val original = chain.request()
        val originalUrl = original.url

        val url = originalUrl.newBuilder()
            .addQueryParameter("apiKey", apiKey)
            .build()

        val request = original.newBuilder().url(url).build()
        return chain.proceed(request)
    }
}