package com.example.profealtoque

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ChatApi {
    @POST("/chat")
    fun sendMessage(@Body request: ChatRequest): Call<ChatResponse>
}