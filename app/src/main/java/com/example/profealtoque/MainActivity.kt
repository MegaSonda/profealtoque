package com.example.profealtoque

import VirtualProfessor
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    private val apiKey: String = BuildConfig.MY_API_KEY
    private lateinit var virtualProfessor: VirtualProfessor
    private lateinit var auth: FirebaseAuth
    private val database = FirebaseDatabase.getInstance()

    private lateinit var cardMatematicas: CardView
    private lateinit var cardLenguaje: CardView
    private lateinit var cardHistoria: CardView
    private lateinit var editTextSearch: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()
        virtualProfessor = VirtualProfessor(apiKey)

        // Inicialización de elementos
        cardMatematicas = findViewById(R.id.cardMatematicas)
        cardLenguaje = findViewById(R.id.cardLenguaje)
        cardHistoria = findViewById(R.id.cardHistoria)
        editTextSearch = findViewById(R.id.editTextText)

        // Filtro de búsqueda en tiempo real
        editTextSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val query = s.toString().toLowerCase()
                filterCards(query)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        // Redirección al hacer clic en las tarjetas
        cardMatematicas.setOnClickListener {
            startChatActivity("Matemáticas")
        }

        cardLenguaje.setOnClickListener {
            startChatActivity("Lenguaje")
        }

        cardHistoria.setOnClickListener {
            startChatActivity("Historia")
        }

        // Cerrar sesión
        val btnLogout = findViewById<Button>(R.id.btnLogout)
        btnLogout.setOnClickListener {
            logLogout()
            auth.signOut()
            startActivity(Intent(this, SignInActivity::class.java))
            finish()
        }
    }

    private fun startChatActivity(subject: String) {
        val intent = Intent(this, ChatActivity::class.java).apply {
            putExtra("subject", subject)
        }
        startActivity(intent)
    }

    private fun filterCards(query: String) {
        // Convertir el texto de búsqueda a minúsculas para hacer la búsqueda insensible a mayúsculas/minúsculas
        cardMatematicas.visibility = if ("matematicas".contains(query)) android.view.View.VISIBLE else android.view.View.GONE
        cardLenguaje.visibility = if ("lenguaje".contains(query)) android.view.View.VISIBLE else android.view.View.GONE
        cardHistoria.visibility = if ("historia".contains(query)) android.view.View.VISIBLE else android.view.View.GONE
    }

    private fun logLogout() {
        val userId = auth.currentUser?.uid
        val currentTime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
        userId?.let {
            database.getReference("logs/$it").child("Ultimo Cierre Sesion").setValue(currentTime)
        }
    }
}
