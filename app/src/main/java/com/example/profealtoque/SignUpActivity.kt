package com.example.profealtoque

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class SignUpActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_singup)

        // Inicializa Firebase Auth
        auth = FirebaseAuth.getInstance()

        val btnSignup = findViewById<Button>(R.id.imageView2)
        btnSignup.setOnClickListener {
            // Captura los textos de los EditText al hacer clic
            val username = findViewById<EditText>(R.id.editTextText2).text.toString()
            val email = findViewById<EditText>(R.id.editTextText).text.toString()
            val password = findViewById<EditText>(R.id.editTextText3).text.toString()

            // Verifica que los campos no estén vacíos
            if (username.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
                createUser(username, email, password)
            } else {
                Toast.makeText(this, "Por favor, completa todos los campos.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun createUser(username: String, email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    Toast.makeText(this, "Cuenta creada: ${user?.email}", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Error: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    // Método para ir a la actividad de inicio de sesión
    fun irlogin(view: android.view.View) {
        val intent = Intent(this, SignInActivity::class.java)
        startActivity(intent)
    }
}
