package com.example.prueba_iot

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.auth.FirebaseAuth
import java.util.Date

class AgregarNoticia : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agregar_noticia)

        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        val etTitulo = findViewById<EditText>(R.id.et_titulo)
        val etResumen = findViewById<EditText>(R.id.et_resumen)
        val etContenido = findViewById<EditText>(R.id.et_contenido)
        val etUrlImagen = findViewById<EditText>(R.id.et_url_imagen)
        val btnCrearNoticia = findViewById<Button>(R.id.btn_crear_noticia)

        btnCrearNoticia.setOnClickListener {
            val titulo = etTitulo.text.toString().trim()
            val resumen = etResumen.text.toString().trim()
            val contenido = etContenido.text.toString().trim()
            val urlImagen = etUrlImagen.text.toString().trim()
            val autor = auth.currentUser?.email ?: "Autor Desconocido"

            if (titulo.isEmpty() || resumen.isEmpty() || contenido.isEmpty()) {
                Toast.makeText(this, "Faltan campos obligatorios.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            guardarNoticiaEnFirestore(titulo, resumen, contenido, autor, urlImagen)
        }
    }

    private fun guardarNoticiaEnFirestore(
        titulo: String,
        resumen: String,
        contenido: String,
        autor: String,
        urlImagen: String
    ) {
        val nuevaNoticia = Noticia(
            titulo = titulo,
            resumen = resumen,
            contenido = contenido,
            autor = autor,
            urlImagen = urlImagen,
            fecha = Date()
        )

        db.collection("noticias")
            .add(nuevaNoticia)
            .addOnSuccessListener {
                mostrarDialogoExito("Noticia Creada", "La noticia '$titulo' fue publicada exitosamente.")
            }
            .addOnFailureListener { e ->
                mostrarDialogoError("Fallo al Publicar", "Error: ${e.message}")
            }
    }

    private fun mostrarDialogoExito(title: String, message: String) {
        AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("Aceptar") { dialog, _ ->
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