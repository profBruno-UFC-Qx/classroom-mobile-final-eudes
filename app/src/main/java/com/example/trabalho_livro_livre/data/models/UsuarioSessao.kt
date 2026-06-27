package com.example.trabalho_livro_livre.data.models

import kotlinx.serialization.Serializable

@Serializable
data class UsuarioSessao(
    val nome: String,
    val whatsapp: String,
    val estaLogado: Boolean
)