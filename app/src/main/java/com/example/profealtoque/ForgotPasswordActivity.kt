package com.example.profealtoque

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class ForgotPasswordActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth // Declaración de FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password) // Asignación del layout

        auth = FirebaseAuth.getInstance() // Inicialización de FirebaseAuth

        val emailEditText = findViewById<EditText>(R.id.emailEditText) // Campo de correo electrónico
        val resetPasswordButton = findViewById<Button>(R.id.resetPasswordButton) // Botón de restablecimiento

        resetPasswordButton.setOnClickListener {
            val email = emailEditText.text.toString().trim() // Obtener el correo electrónico ingresado

            if (email.isNotEmpty()) {
                // Llama a la función para enviar el correo de restablecimiento
                resetPassword(email)
            } else {
                Toast.makeText(this, "Por favor, ingresa tu correo electrónico", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Método para enviar el enlace de restablecimiento de contraseña
    private fun resetPassword(email: String) {
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Muestra un mensaje si el envío fue exitoso
                    Toast.makeText(this, "Correo de restablecimiento enviado a $email", Toast.LENGTH_SHORT).show()
                } else {
                    // Muestra el error si falla el envío
                    Toast.makeText(this, "Error: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }
}
