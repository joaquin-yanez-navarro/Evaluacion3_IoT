package com.example.prueba_iot

import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class Noticia(
    var documentId: String = "",
    var titulo: String = "",
    var resumen: String = "",
    var contenido: String = "",
    var autor: String = "",
    var urlImagen: String = "",
    @ServerTimestamp
    var fecha: Date? = null
) {
    constructor() : this("", "", "", "", "", "", null)
}