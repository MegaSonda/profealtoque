package com.example.profealtoque

import VirtualProfessor
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.cardview.widget.CardView
import com.google.firebase.auth.FirebaseAuth // Importa FirebaseAuth para la autenticación
import com.google.firebase.database.FirebaseDatabase // Importa FirebaseDatabase para la base de datos
import java.text.SimpleDateFormat // Para formatear la fecha
import java.util.* // Utilidades de fecha y tiempo

class MainActivity : AppCompatActivity() {
    // Usa BuildConfig para obtener la clave de API
    private val apiKey: String = BuildConfig.MY_API_KEY
    private lateinit var virtualProfessor: VirtualProfessor
    private lateinit var auth: FirebaseAuth // Variable para manejar la autenticación
    private val database = FirebaseDatabase.getInstance() // Instancia de la base de datos en Firebase

    // Definición de las CardViews
    private lateinit var cardMatematicas: CardView
    private lateinit var cardLenguaje: CardView
    private lateinit var cardHistoria: CardView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Inicializa FirebaseAuth
        auth = FirebaseAuth.getInstance()

        //  VirtualProfessor
        virtualProfessor = VirtualProfessor(apiKey)

        // CardViews
        cardMatematicas = findViewById(R.id.cardMatematicas)
        cardLenguaje = findViewById(R.id.cardLenguaje)
        cardHistoria = findViewById(R.id.cardHistoria)

        Log.d("MainActivity", "API Key: $apiKey")

        // Configura los click listeners para las CardViews
        cardMatematicas.setOnClickListener {
            val intent = Intent(this, ChatActivity::class.java)
            intent.putExtra("subject", "Matemáticas")
            startActivity(intent)
        }

        cardLenguaje.setOnClickListener {
            val intent = Intent(this, ChatActivity::class.java)
            intent.putExtra("subject", "Lenguaje")
            startActivity(intent)
        }

        cardHistoria.setOnClickListener {
            val intent = Intent(this, ChatActivity::class.java)
            intent.putExtra("subject", "Historia")
            startActivity(intent)
        }

        // Manejo del cierre de sesión
        val btnLogout = findViewById<Button>(R.id.btnLogout) // Botón de cerrar sesión
        btnLogout.setOnClickListener {
            val userId = auth.currentUser?.uid // Obtiene el ID del usuario actual
            val currentTime = getCurrentTime() // Obtiene la hora actual

            // Registrar la hora de cierre de sesión
            if (userId != null) {
                val logRef = database.getReference("logs/$userId") // Referencia a la base de datos para ese usuario
                logRef.child("lastSignOut").setValue(currentTime) // Actualiza el tiempo de cierre de sesión
            }

            // Cierra sesión en Firebase
            auth.signOut()

            // Redirige a la pantalla de inicio de sesión
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
            finish() // Finaliza la actividad actual para evitar que el usuario vuelva a esta pantalla
        }
    }

    // Función para obtener la hora actual en formato "yyyy-MM-dd HH:mm:ss"
    private fun getCurrentTime(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()) // Formato de fecha y hora
        return dateFormat.format(Date()) // Retorna la fecha y hora actuales formateadas
    }
}
