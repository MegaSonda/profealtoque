package com.example.profealtoque

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth // Importa FirebaseAuth para la autenticación

class SignUpActivity : AppCompatActivity() {

    // Declara una instancia de FirebaseAuth
    private lateinit var auth: FirebaseAuth

    // Método que se ejecuta al crear la actividad
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_singup) // Define el layout para esta actividad

        // Inicializa la instancia de FirebaseAuth
        auth = FirebaseAuth.getInstance()

        // Obtiene la referencia del botón de registro y configura su evento de clic
        val btnSignup = findViewById<Button>(R.id.imageView2)
        btnSignup.setOnClickListener {

            // Captura el texto de los EditText para usuario, email y contraseña
            val username = findViewById<EditText>(R.id.editTextText2).text.toString()
            val email = findViewById<EditText>(R.id.editTextText).text.toString()
            val password = findViewById<EditText>(R.id.editTextText3).text.toString()

            // Verifica que todos los campos contengan texto
            if (username.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
                // Llama al método para crear la cuenta si los campos están completos
                createUser(username, email, password)
            } else {
                // Muestra un mensaje indicando que los campos están incompletos
                Toast.makeText(this, "Por favor, completa todos los campos.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Método para crear un usuario en Firebase
    private fun createUser(username: String, email: String, password: String) {

        // Usa FirebaseAuth para crear un usuario con correo electrónico y contraseña

        auth.createUserWithEmailAndPassword(email, password) //instancia y metodo que ocupa firebase
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Si la creación fue exitosa, obtiene el usuario actual de Firebase
                    val user = auth.currentUser
                    // Muestra un mensaje de éxito con el correo del usuario
                    Toast.makeText(this, "Cuenta creada: ${user?.email}", Toast.LENGTH_SHORT).show()
                } else {
                    // Si hubo un error, muestra el mensaje de error
                    Toast.makeText(this, "Error: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    // Método para ir a la actividad de inicio de sesión
    fun irlogin(view: android.view.View) {
        // Crea un Intent para navegar a SignInActivity
        val intent = Intent(this, SignInActivity::class.java)
        startActivity(intent) // Inicia la actividad de inicio de sesión
    }
}
