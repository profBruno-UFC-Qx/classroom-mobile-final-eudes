package com.example.trabalho_livro_livre.data

import android.content.Context
import androidx.datastore.dataStore
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.trabalho_livro_livre.data.models.LivroPersistido
import com.example.trabalho_livro_livre.data.models.UsuarioSessao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString

// Definição do modelo de transporte para o cadastro local
data class CadastroLocal(val nome: String, val email: String, val whatsapp: String, val senha: String)

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

    // CORRIGIDO: Adicionado 'context.' antes de dataStore
    fun obterCadastroSalvo(): Flow<CadastroLocal> = context.dataStore.data.map { prefs ->
        CadastroLocal(
            nome = prefs[stringPreferencesKey("user_nome")] ?: "",
            email = prefs[stringPreferencesKey("user_email")] ?: "",
            whatsapp = prefs[stringPreferencesKey("user_whatsapp")] ?: "",
            senha = prefs[stringPreferencesKey("user_senha")] ?: ""
        )
    }

    // CORRIGIDO: Adicionado 'context.' antes de dataStore
    suspend fun salvarCadastroCompleto(nome: String, email: String, whatsapp: String, senha: String) {
        context.dataStore.edit { prefs ->
            prefs[stringPreferencesKey("user_nome")] = nome
            prefs[stringPreferencesKey("user_email")] = email
            prefs[stringPreferencesKey("user_whatsapp")] = whatsapp
            prefs[stringPreferencesKey("user_senha")] = senha
            prefs[booleanPreferencesKey("user_logado")] = true
        }
    }

    suspend fun deletarLivro(livro: LivroPersistido) {
        context.dataStore.edit { prefs ->
            // 1. Lê a lista atual do DataStore
            val jsonAtual = prefs[DataStoreKeys.LISTA_LIVROS]
            val listaAtual = if (jsonAtual.isNullOrEmpty()) emptyList()
            else try { Json.decodeFromString<List<LivroPersistido>>(jsonAtual) } catch(e: Exception) { emptyList() }

            // 2. Filtra a lista removendo o livro com o ID correspondente
            val novaLista = listaAtual.filterNot { it.id == livro.id }

            // 3. Salva a nova lista de volta no DataStore
            prefs[DataStoreKeys.LISTA_LIVROS] = Json.encodeToString(novaLista)
        }
    }

    suspend fun salvarListaCompleta(lista: List<LivroPersistido>) {
        context.dataStore.edit { prefs ->
            // Serializa a lista completa e salva no DataStore
            prefs[DataStoreKeys.LISTA_LIVROS] = Json.encodeToString(lista)
        }
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