package com.example.profealtoque

import com.google.firebase.database.FirebaseDatabase // Importa Firebase Realtime Database
import java.text.SimpleDateFormat // Importa el formato de fecha
import java.util.* // Importa utilidades como 'Date'
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.google.firebase.auth.FirebaseAuth
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class SignInActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth // Inicializa la autenticación de Firebase
    private val database = FirebaseDatabase.getInstance() // Obtiene una instancia de Firebase Realtime Database

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sing_in) // Asigna el layout de inicio de sesión

        auth = FirebaseAuth.getInstance() // Obtiene una instancia de autenticación de Firebase

        val btnSignin = findViewById<Button>(R.id.btnSignin) // Obtiene el botón de inicio de sesión
        btnSignin.setOnClickListener {
            val email = findViewById<EditText>(R.id.editTextText).text.toString().trim() // Obtiene y limpia el texto del campo de email
            val password = findViewById<EditText>(R.id.editTextText2).text.toString().trim() // Obtiene y limpia el texto del campo de contraseña

            if (email.isNotEmpty() && password.isNotEmpty()) {
                // Verifica si los campos no están vacíos antes de intentar el inicio de sesión
                signInUser(email, password) // Llama a la función para iniciar sesión
            } else {
                // Muestra un mensaje si los campos están vacíos
                Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Función para autenticar al usuario usando Firebase Authentication
    private fun signInUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Si el inicio de sesión es exitoso
                    val user = auth.currentUser // Obtiene el usuario autenticado
                    Toast.makeText(this, "Bienvenido, ${user?.email}", Toast.LENGTH_SHORT).show()

                    // Registro de la hora de inicio de sesión en Firebase Realtime Database
                    val userId = user?.uid // Obtiene el ID del usuario actual
                    val currentTime = getCurrentTime() // Obtiene la hora actual formateada

                    if (userId != null) {
                        // Verifica si el ID del usuario no es nulo
                        val logRef = database.getReference("logs/$userId") // Obtiene la referencia en la base de datos
                        logRef.child("lastSignIn").setValue(currentTime) // Guarda la hora de inicio de sesión en Firebase bajo 'lastSignIn'
                    }

                    // Redirige al menú principal después del inicio de sesión exitoso
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish() // Finaliza la actividad actual para que el usuario no pueda volver presionando "atrás"
                } else {
                    // Si falla la autenticación, muestra el mensaje de error
                    Toast.makeText(this, "Error: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    // Función que retorna la hora actual en formato "yyyy-MM-dd HH:mm:ss"
    private fun getCurrentTime(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()) // Formato de fecha y hora
        return dateFormat.format(Date()) // Retorna la fecha y hora actuales formateadas
    }

    // Método para ir a la actividad de registro de usuario
    fun irregistrarcuenta(view: android.view.View) {
        val intent = Intent(this, SignUpActivity::class.java) // Crea un intent para redirigir a la actividad de registro
        startActivity(intent) // Inicia la actividad de registro
    }
}
