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

        btnRecuperar.setOnClickListener {
            val email = etEmail.text.toString().trim()

            if (email.isEmpty()) {
                Toast.makeText(this, "Ingresa el correo electrónico.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            btnVolverLogin.setOnClickListener {
                finish()
            }

            auth.signInWithEmailAndPassword(email, "password_ficticia_solo_para_validar")
                .addOnFailureListener { exception ->

                    auth.fetchSignInMethodsForEmail(email)
                        .addOnCompleteListener { fetchTask ->
                            if (fetchTask.isSuccessful && fetchTask.result?.signInMethods?.isNotEmpty() == true) {

                            } else {
                                mostrarDialogoError("Usuario no encontrado", "El correo electrónico no está registrado en el sistema.")
                            }
                        }
                }
        }
    }

    private fun generarNuevaContrasena(): String {
        val charPool : List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')
        return (1..8).map { Random.nextInt(0, charPool.size) }
            .map(charPool::get)
            .joinToString("")
    }

    private fun mostrarNuevaClave(password: String) {
        AlertDialog.Builder(this)
            .setTitle("Clave Recuperada (EV3)")
            .setMessage("Se ha verificado tu cuenta. Tu nueva contraseña temporal es: \n\n**$password**\n\nDebes usarla para iniciar sesión.")
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