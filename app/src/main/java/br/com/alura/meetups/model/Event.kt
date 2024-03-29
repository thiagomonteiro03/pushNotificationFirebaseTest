package br.com.alura.meetups.model

data class Event(
    val id: String,
    val titulo: String,
    val descricao: String,
    val imagem: String? = null,
    val inscritos: Int,
    val estaInscrito: Boolean = false
)