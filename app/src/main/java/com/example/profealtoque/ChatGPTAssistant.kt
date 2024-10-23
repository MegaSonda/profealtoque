import okhttp3.*
import org.json.JSONObject
import java.io.IOException

class ChatGPTAssistant {

    private val client = OkHttpClient()
    private var lastRequestTime: Long = 0
    private val REQUEST_DELAY_MS = 3000 // 3 segundos entre solicitudes

    fun askQuestion(apiKey: String, prompt: String, callback: (String?) -> Unit) {
        val currentTime = System.currentTimeMillis()
        val elapsedTime = currentTime - lastRequestTime

        if (elapsedTime < REQUEST_DELAY_MS) {
            // Si no ha pasado suficiente tiempo desde la última solicitud, espera antes de hacer otra
            callback("Estás enviando solicitudes demasiado rápido. Espera un momento.")
            return
        }

        lastRequestTime = currentTime

        val requestBody = FormBody.Builder()
            .add("prompt", prompt)
            .add("max_tokens", "150")
            .build()

        val request = Request.Builder()
            .url("https://api.openai.com/v1/completions")
            .addHeader("Authorization", "Bearer $apiKey")
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback("Error al conectar con la API: ${e.message}")
            }

            override fun onResponse(call: Call, response: Response) {
                if (!response.isSuccessful) {
                    if (response.code == 429) {
                        callback("Has excedido el límite de solicitudes. Intenta nuevamente en unos segundos.")
                    } else {
                        callback("Error en la respuesta: ${response.code}")
                    }
                    return
                }

                val jsonResponse = JSONObject(response.body?.string() ?: "{}")
                val responseText = jsonResponse.optJSONArray("choices")
                    ?.optJSONObject(0)
                    ?.optString("text")

                callback(responseText)
            }
        })
    }
}
