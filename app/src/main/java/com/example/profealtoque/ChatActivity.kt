package com.example.profealtoque

import VirtualProfessor
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import java.util.Locale

class ChatActivity : AppCompatActivity() {

    private lateinit var chatScrollView: ScrollView
    private lateinit var chatInput: EditText
    private lateinit var sendButton: Button
    private lateinit var inputLayout: LinearLayout
    private lateinit var messagesLayout: LinearLayout // El contenedor donde se agregan los mensajes
    private lateinit var virtualProfessor: VirtualProfessor // Instancia de VirtualProfessor

    private var selectedSubject: String? = null // Para almacenar la materia seleccionada

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        // Inicializa los componentes del layout
        chatScrollView = findViewById(R.id.chatScrollView)
        chatInput = findViewById(R.id.chatInput)
        sendButton = findViewById(R.id.sendButton)
        inputLayout = findViewById(R.id.inputLayout)
        messagesLayout = findViewById(R.id.messagesLayout)

        // Crea una instancia de VirtualProfessor
        virtualProfessor = VirtualProfessor(BuildConfig.MY_API_KEY)

        // Recupera la materia seleccionada desde el intent
        selectedSubject = intent.getStringExtra("subject")

        // Agrega un log para verificar el valor recuperado
        Log.d("ChatActivity", "Selected subject: $selectedSubject")

        // Si hay una materia seleccionada, agrega un mensaje de bienvenida
        selectedSubject?.let {
            addMessageToChat("Has seleccionado el profesor de $it.")
        }

        // Configura el botón de enviar
        sendButton.setOnClickListener {
            sendButton.isEnabled = false  // Desactiva el botón
            sendMessage()
        }
    }

    private fun sendMessage() {
        val message = chatInput.text.toString().trim()  // Obtiene el texto del input

        if (message.isNotEmpty()) {
            // Agrega el mensaje del usuario al chat
            addMessageToChat("Tú: $message")

            // Verifica si hay una materia seleccionada
            selectedSubject?.let { subject ->
                // Normaliza la materia seleccionada
                val normalizedSubject = subject.trim().lowercase(Locale.ROOT)

                // Genera una respuesta simulada basada en la materia seleccionada y el mensaje del usuario
                val response = generateSimulatedResponse(message, normalizedSubject)

                // Agrega la respuesta simulada al chat
                addResponseMessage(response)
            } ?: run {
                // Si no hay materia seleccionada, muestra un mensaje de error
                addResponseMessage("Lo siento, no se ha seleccionado un profesor. Por favor, selecciona una materia antes de enviar un mensaje.")
            }

            // Limpia el campo de texto después de enviar el mensaje
            chatInput.text.clear()

            // Hace que el chat se desplace automáticamente hacia abajo para mostrar el último mensaje
            chatScrollView.post { chatScrollView.fullScroll(View.FOCUS_DOWN) }
        }
    }

    // Método para generar una respuesta simulada
    private fun generateSimulatedResponse(input: String, profesorActual: String): String {
        // Limpia el valor de profesorActual
        val profesorNormalizado = profesorActual.trim().lowercase(Locale.ROOT)

        return when (profesorNormalizado) {
            "matematicas", "matemáticas" -> {
                when {
                    input.contains("parábola", ignoreCase = true) -> "Una parábola es la gráfica de una función cuadrática. Tiene una forma curva y simétrica. Su ecuación general es y = ax² + bx + c."
                    input.contains("parabola", ignoreCase = true) -> "Una parábola es la gráfica de una función cuadrática. Tiene una forma curva y simétrica. Su ecuación general es y = ax² + bx + c."
                    input.contains("radio", ignoreCase = true) -> "La fórmula para el radio de un círculo es r = C / (2π), donde C es la circunferencia."
                    input.contains("hexágono", ignoreCase = true) -> "Un hexágono es una figura geométrica de seis lados. Si es regular, sus lados y ángulos son iguales."
                    input.contains("hexagono", ignoreCase = true) -> "Un hexágono es una figura geométrica de seis lados. Si es regular, sus lados y ángulos son iguales."
                    input.contains("reto matemático", ignoreCase = true) -> "¡Claro! ¿Qué tal si calculas cuánto es 15 x 4?"
                    input.contains("reto matematico", ignoreCase = true) -> "¡Claro! ¿Qué tal si calculas cuánto es 15 x 4?"
                    input.contains("hola", ignoreCase = true) -> "¡Hola! ¿Cómo puedo ayudarte hoy?"
                    else -> "Lo siento, solo puedo responder preguntas de matemáticas."
                }
            }
            "lenguaje" -> {
                when {
                    input.contains("significado", ignoreCase = true) -> "Puedo ayudarte con el significado de palabras. ¿Cuál te gustaría saber?"
                    input.contains("aprobar", ignoreCase = true) -> "Dar por bueno o suficiente algo o a alguien: Emitir un juicio positivo o favorable sobre un trabajo, proyecto, examen, etc. Por ejemplo, \"El profesor decidió aprobar mi entrega dos.\""
                    input.contains("hola", ignoreCase = true) -> "¡Hola! ¿Cómo puedo ayudarte hoy?"
                    input.contains("lenguaje", ignoreCase = true) -> "El lenguaje es esencial para la comunicación. ¿Te gustaría practicar algo?"
                    input.contains("ortografía", ignoreCase = true) -> "La ortografía es importante. ¿Sabías que 'había' lleva acento porque es una palabra grave terminada en vocal?"
                    else -> "Lo siento, solo puedo responder preguntas de lenguaje."
                }
            }
            "historia" -> {
                when {
                    input.contains("Napoleón", ignoreCase = true) -> "Napoleón fue un líder militar y político francés."
                    input.contains("hola", ignoreCase = true) -> "¡Hola! ¿Cómo puedo ayudarte hoy?"
                    else -> "Lo siento, solo puedo responder preguntas de historia."
                }
            }
            else -> "Lo siento, no reconozco ese profesor. Por favor selecciona matemáticas, lenguaje o historia."
        }
    }


    // Método para agregar mensajes al chat
    fun addMessageToChat(message: String) {
        val messageTextView = TextView(this)
        messageTextView.text = message
        messagesLayout.addView(messageTextView)
        chatScrollView.post { chatScrollView.fullScroll(View.FOCUS_DOWN) }
    }

    // Método para manejar las respuestas y agregarlas al chat
    fun addResponseMessage(response: String) {
        val responseTextView = TextView(this)
        responseTextView.text = response
        messagesLayout.addView(responseTextView)
        chatScrollView.post { chatScrollView.fullScroll(View.FOCUS_DOWN) }

        // Reactiva el botón una vez recibida la respuesta
        sendButton.isEnabled = true
    }
}
