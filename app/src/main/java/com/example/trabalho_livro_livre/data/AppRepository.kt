package com.example.trabalho_livro_livre.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import com.example.trabalho_livro_livre.data.models.LivroPersistido
import com.example.trabalho_livro_livre.data.models.UsuarioSessao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString

class AppRepository(private val context: Context) {

    // Transforma a leitura assíncrona do DataStore em um fluxo reativo (Flow)
    val sessaoUsuarioFlow: Flow<UsuarioSessao> = context.dataStore.data.map { prefs ->
        UsuarioSessao(
            nome = prefs[DataStoreKeys.USER_NOME] ?: "",
            whatsapp = prefs[DataStoreKeys.USER_WHATSAPP] ?: "",
            estaLogado = prefs[DataStoreKeys.USER_LOGADO] ?: false
        )
    }

    val livrosFlow: Flow<List<LivroPersistido>> = context.dataStore.data.map { prefs ->
        val json = prefs[DataStoreKeys.LISTA_LIVROS]
        if (json.isNullOrEmpty()) emptyList()
        else try { Json.decodeFromString(json) } catch (e: Exception) { emptyList() }
    }

    suspend fun salvarSessao(nome: String, whatsapp: String) {
        context.dataStore.edit { prefs ->
            prefs[DataStoreKeys.USER_NOME] = nome
            prefs[DataStoreKeys.USER_WHATSAPP] = whatsapp
            prefs[DataStoreKeys.USER_LOGADO] = true
        }
    }

    suspend fun apagarSessao() {
        context.dataStore.edit { prefs ->
            prefs[DataStoreKeys.USER_LOGADO] = false
        }
    }

    suspend fun salvarLivro(livro: LivroPersistido) {
        context.dataStore.edit { prefs ->
            val jsonAtual = prefs[DataStoreKeys.LISTA_LIVROS]
            val listaAtual = if (jsonAtual.isNullOrEmpty()) emptyList()
            else try { Json.decodeFromString<List<LivroPersistido>>(jsonAtual) } catch(e: Exception) { emptyList() }

            val novaLista = listaAtual + livro
            prefs[DataStoreKeys.LISTA_LIVROS] = Json.encodeToString(novaLista)
        }
    }
}