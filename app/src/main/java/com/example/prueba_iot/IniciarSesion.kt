package com.example.prueba_iot

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.EditText
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import android.content.Intent
import com.example.prueba_iot.RecuperarContrasena
import com.example.prueba_iot.RegistrarCuenta
import com.google.firebase.auth.FirebaseAuth
import android.widget.Toast

class IniciarSesion : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_iniciar_sesion)

        auth = FirebaseAuth.getInstance()

        val etUsername = findViewById<EditText>(R.id.et_username)
        val etPassword = findViewById<EditText>(R.id.et_password)
        val btnLogin = findViewById<Button>(R.id.btn_login)
        val btnForgotPassword = findViewById<Button>(R.id.btn_forgot_password)
        val btnRegister = findViewById<Button>(R.id.btn_register)

        btnLogin.setOnClickListener {
            val email = etUsername.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Asumiendo que 'Home' es la actividad principal después del login
                            startActivity(Intent(this, Home::class.java))
                            finish()
                        } else {
                            AlertDialog.Builder(this)
                                .setTitle("Error de Login")
                                .setMessage(task.exception?.message ?: "Error desconocido")
                                .setPositiveButton("OK") { _, _ -> }
                                .show()
                        }
                    }
            } else {
                AlertDialog.Builder(this)
                    .setTitle("Error")
                    .setMessage("Completa todos los campos")
                    .setPositiveButton("OK") { _, _ -> }
                    .show()
            }
        }

        btnForgotPassword.setOnClickListener {
            startActivity(Intent(this, RecuperarContrasena::class.java))
        }

        btnRegister.setOnClickListener {
            startActivity(Intent(this, RegistrarCuenta::class.java))
        }
    }
}
