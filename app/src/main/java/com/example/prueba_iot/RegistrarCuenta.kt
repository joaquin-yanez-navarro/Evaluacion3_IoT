package com.example.prueba_iot

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class RegistrarCuenta : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registrar_cuenta)

        auth = FirebaseAuth.getInstance()

        val etEmail = findViewById<EditText>(R.id.et_email)
        val etPassword = findViewById<EditText>(R.id.et_new_password)
        val btnCrearCuenta = findViewById<Button>(R.id.btn_register)
        val btnVolverLogin = findViewById<Button>(R.id.btn_volver_login)

        btnVolverLogin.setOnClickListener {
            finish()
        }

        btnCrearCuenta.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Completa todos los campos.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        mostrarDialogoExito()
                    } else {
                        val errorMessage = task.exception?.message ?: "Error desconocido al registrar."
                        mostrarDialogoError("Fallo en el Registro", errorMessage)
                    }
                }
        }
    }

    private fun mostrarDialogoExito() {
        AlertDialog.Builder(this)
            .setTitle("Registro Exitoso")
            .setMessage("Tu cuenta ha sido creada. Ahora puedes iniciar sesión.")
            .setPositiveButton("Aceptar") { dialog, _ ->
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