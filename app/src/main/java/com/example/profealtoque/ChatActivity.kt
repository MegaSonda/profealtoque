package com.example.profealtoque
import VirtualProfessor
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.profealtoque.BuildConfig
import com.example.profealtoque.R

class ChatActivity : AppCompatActivity() {

    private lateinit var chatScrollView: ScrollView
    private lateinit var chatInput: EditText
    private lateinit var sendButton: Button
    private lateinit var messagesLayout: LinearLayout
    private lateinit var virtualProfessor: VirtualProfessor
    private var selectedSubject: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        chatScrollView = findViewById(R.id.chatScrollView)
        chatInput = findViewById(R.id.chatInput)
        sendButton = findViewById(R.id.sendButton)
        messagesLayout = findViewById(R.id.messagesLayout)

        virtualProfessor = VirtualProfessor(BuildConfig.MY_API_KEY)
        selectedSubject = intent.getStringExtra("subject")

        selectedSubject?.let {
            addMessageToChat("Has seleccionado el profesor de $it.")
        }

        sendButton.setOnClickListener {
            sendMessage()
        }
    }

    private fun sendMessage() {
        val message = chatInput.text.toString().trim()
        if (message.isNotEmpty()) {
            addMessageToChat("Tú: $message")
            val callback: (String?) -> Unit = { response -> displayResponse(response) }

            when (selectedSubject) {
                "Matemáticas" -> virtualProfessor.onMathProfessorSelected(message, callback)
                "Lenguaje" -> virtualProfessor.onLanguageProfessorSelected(message, callback)
                "Historia" -> virtualProfessor.onHistoryProfessorSelected(message, callback)
                else -> displayResponse("Asignatura no reconocida.")
            }

            chatInput.text.clear()
        }
    }

    private fun displayResponse(response: String?) {
        runOnUiThread {
            addMessageToChat(response ?: "No pude obtener una respuesta.")
            sendButton.isEnabled = true
        }
    }

    private fun addMessageToChat(message: String) {
        // Crear un LinearLayout para envolver el TextView
        val messageLayout = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL // Cambiar a orientación horizontal para que los mensajes se alineen correctamente
            setPadding(8, 8, 8, 8)
        }

        // Crear el TextView que contiene el mensaje
        val messageTextView = TextView(this).apply {
            text = message
            setPadding(10, 10, 10, 10)
            setTextColor(getColor(android.R.color.black))  // Color de texto
        }

        // Verificar si es un mensaje del usuario o del asistente
        if (message.startsWith("Tú:")) {
            // Establecer fondo para el mensaje del usuario
            messageTextView.setBackgroundResource(R.drawable.user_message_background)
            messageLayout.gravity = Gravity.END  // Alineación a la derecha
            messageTextView.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(0, 0, 16, 0)  // Margen derecho para los mensajes del usuario
            }
        } else {
            // Establecer fondo para la respuesta del asistente
            messageTextView.setBackgroundResource(R.drawable.assistant_message_background)
            messageLayout.gravity = Gravity.START  // Alineación a la izquierda
            messageTextView.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(16, 0, 0, 0)  // Margen izquierdo para las respuestas del asistente
            }
        }

        // Agregar el TextView al LinearLayout
        messageLayout.addView(messageTextView)

        // Agregar el LinearLayout al layout principal
        messagesLayout.addView(messageLayout)

        // Desplazar el ScrollView hacia el final
        chatScrollView.post {
            chatScrollView.fullScroll(View.FOCUS_DOWN)
        }
    }

}
