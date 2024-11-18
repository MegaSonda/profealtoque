package com.example.ChatGPTAssitant
import android.util.Log
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

class ChatGPTAssistant {

    private val client = OkHttpClient()
    private var lastRequestTime: Long = 0
    private val REQUEST_DELAY_MS = 3000

    fun askQuestion(apiKey: String, prompt: String, callback: (String?) -> Unit) {

        val currentTime = System.currentTimeMillis()
        val elapsedTime = currentTime - lastRequestTime

        if (elapsedTime < REQUEST_DELAY_MS) {
            callback("Estás enviando solicitudes demasiado rápido. Espera un momento.")
            return
        }

        lastRequestTime = currentTime

        val json = JSONObject()
        json.put("model", "gpt-3.5-turbo")

        val messages = JSONArray().apply {
            put(JSONObject().apply {
                put("role", "user")
                put("content", prompt)
            })
        }
        json.put("messages", messages)
        json.put("max_tokens", 150)
        val requestBody = RequestBody.create("application/json".toMediaTypeOrNull(), json.toString())

        val request = Request.Builder()
            .url("https://api.openai.com/v1/chat/completions")
            .addHeader("Authorization", "Bearer $apiKey")
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback("Error al conectar con la API: ${e.message}")
                Log.e("ChatGPTAssistant", "Error: ${e.message}", e)
            }

            override fun onResponse(call: Call, response: Response) {
                if (!response.isSuccessful) {
                    Log.e("ChatGPTAssistant", "Error en la respuesta: ${response.code}")
                    callback("Error en la respuesta: ${response.code}")
                    return
                }

                val jsonResponse = JSONObject(response.body?.string() ?: "{}")
                val responseText = jsonResponse.optJSONArray("choices")
                    ?.optJSONObject(0)
                    ?.optJSONObject("message")
                    ?.optString("content")

                callback(responseText)
            }
        })
    }
}
