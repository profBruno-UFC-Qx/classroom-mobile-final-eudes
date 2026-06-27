package com.example.trabalho_livro_livre.data.models
import kotlinx.serialization.Serializable

@Serializable
data class LivroPersistido(
    val id: String,
    val titulo: String,
    val autor: String,
    val preco: String,
    val tipoAnuncio: String,
    val condicao: String,
    val descricao: String
)
