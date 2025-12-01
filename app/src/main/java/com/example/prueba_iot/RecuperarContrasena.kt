package com.example.prueba_iot

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import kotlin.random.Random
import android.content.Intent

class RecuperarContrasena : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recuperar_contrasena)
        auth = FirebaseAuth.getInstance()

        val etEmail = findViewById<EditText>(R.id.et_email)
        val btnRecuperar = findViewById<Button>(R.id.btn_recover)
        val btnVolverLogin = findViewById<Button>(R.id.btn_volver_login_recover)

        btnVolverLogin.setOnClickListener {
            finish()
        }

        btnRecuperar.setOnClickListener {
            val email = etEmail.text.toString().trim()

            if (email.isEmpty()) {
                Toast.makeText(this, "Ingresa el correo electrónico.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.sendPasswordResetEmail(email)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        mostrarDialogoExito("Correo Enviado",
                            "Se ha enviado un correo electrónico de recuperación a $email. Sigue las instrucciones para restablecer tu contraseña.")
                    } else {
                        val errorMessage = task.exception?.message ?: "Error desconocido al enviar el correo."
                        mostrarDialogoError("Fallo al Enviar Correo", errorMessage)
                    }
                }
        }
    }

    private fun mostrarDialogoExito(title: String, message: String) {
        AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("Aceptar") { dialog, _ ->
                dialog.dismiss()
                startActivity(Intent(this, IniciarSesion::class.java))
                finish()
            }
            .show()
    }

    private fun mostrarDialogoError(title: String, message: String) {
        AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

}