package com.example.prueba_iot

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class Home : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var rvNoticias: RecyclerView
    private lateinit var btnLogout: Button
    private lateinit var fabAddNoticia: FloatingActionButton


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        rvNoticias = findViewById(R.id.rv_noticias)
        btnLogout = findViewById(R.id.btn_logout)
        fabAddNoticia = findViewById(R.id.fab_add_noticia)

        if (auth.currentUser == null) {
            navegarALogin()
            return
        }

        btnLogout.setOnClickListener {
            cerrarSesion()
        }

        fabAddNoticia.setOnClickListener {
            startActivity(Intent(this, AgregarNoticia::class.java))
        }

        cargarNoticiasDesdeFirestore()
    }

    private fun cerrarSesion() {
        auth.signOut()
        navegarALogin()
    }

    private fun navegarALogin() {
        val intent = Intent(this, IniciarSesion::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    private fun cargarNoticiasDesdeFirestore() {
        db.collection("noticias")
            .orderBy("fecha", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { result ->
                val noticiasList = mutableListOf<Noticia>()
                for (document in result) {
                    val noticia = document.toObject(Noticia::class.java)
                    noticia.documentId = document.id
                    noticiasList.add(noticia)
                }

            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Error al cargar noticias: ${exception.message}", Toast.LENGTH_LONG).show()
            }
    }
}