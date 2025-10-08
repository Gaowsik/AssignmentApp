package com.example.assignmentapp.data.source.remote.network

import com.example.assignmentapp.data.APIResource
import com.google.gson.Gson
import com.google.gson.JsonObject

fun parseErrors(failure: APIResource.Error): String {
    return when {
        failure.isNetworkError -> "Network Error"

        failure.errorCode == 403 -> {
            "Unauthorized request"
        }

        failure.errorCode == 404 -> {
            ("Resource not found")
        }

        failure.errorCode == 422 -> {
            ("Validation error")
        }

        failure.errorCode == 500 -> {
            try {
                val errorBody = Gson().fromJson(failure.errorBody?.string(), JsonObject::class.java)
                (errorBody.get("message").asString)
            } catch (e: Exception) {
                ("Internal server error")
            }
        }

        failure.errorCode == 504 -> {
            ("Gateway timeout")
        }

        failure.errorCode == 0 -> {
            ("Unknown error")
        }

        else -> {
            try {
                val errorBody = failure.errorBody?.string()
                val jsonObject = Gson().fromJson(errorBody, JsonObject::class.java)
                val errorMessage = jsonObject.getAsJsonObject("error").get("message")?.asString
                errorMessage ?: "Unknown error"
            } catch (e: Exception) {
                // Log the exception if needed
                "Unknown error"
            }
        }
    }
}


