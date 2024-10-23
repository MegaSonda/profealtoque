package com.example.profealtoque
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.profealtoque.ApiClient
import com.example.profealtoque.ChatRequest
import com.example.profealtoque.ChatResponse
import com.example.profealtoque.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PruebaActivity : AppCompatActivity() {

    private lateinit var messageInput: EditText
    private lateinit var sendButton: Button
    private lateinit var responseTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.prueba_activity)

        // Inicializa las vistas
        messageInput = findViewById(R.id.messageInput)
        sendButton = findViewById(R.id.sendButton)
        responseTextView = findViewById(R.id.responseTextView)

        // Configura el botón para enviar el mensaje
        sendButton.setOnClickListener {
            val message = messageInput.text.toString()
            sendMessage(message)
        }
    }

    private fun sendMessage(message: String) {
        // Crea una instancia de ChatRequest
        val request = ChatRequest(message)

        // Realiza la llamada a la API
        ApiClient.chatApi.sendMessage(request).enqueue(object : Callback<ChatResponse> {
            override fun onResponse(call: Call<ChatResponse>, response: Response<ChatResponse>) {
                if (response.isSuccessful) {
                    // Muestra la respuesta en el TextView
                    responseTextView.text = response.body()?.response ?: "No response"
                } else {
                    // Maneja errores
                    Toast.makeText(this@PruebaActivity, "Error: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ChatResponse>, t: Throwable) {
                // Maneja fallos de la llamada
                Toast.makeText(this@PruebaActivity, "Failed: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
