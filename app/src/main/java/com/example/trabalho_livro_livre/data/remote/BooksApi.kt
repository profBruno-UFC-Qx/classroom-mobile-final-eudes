package com.example.trabalho_livro_livre.data.remote

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.android.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

object BooksApi {
    private val client = HttpClient(Android) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
            })
        }
    }

    suspend fun buscarLivro(query: String): VolumeInfo? {
        // A API do Google retorna uma lista, pegamos o primeiro resultado (.firstOrNull)
        val response: GoogleBooksResponse = client.get("https://www.googleapis.com/books/v1/volumes?q=intitle:$query").body()
        return response.items?.firstOrNull()?.volumeInfo
    }
}