package com.example.trabalho_livro_livre.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore

// Instancia o singleton do banco de dados no escopo do Context
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "livro_livre_prefs")

object DataStoreKeys {
    val USER_NOME = stringPreferencesKey("user_nome")
    val USER_WHATSAPP = stringPreferencesKey("user_whatsapp")
    val USER_LOGADO = booleanPreferencesKey("user_logado")
    val LISTA_LIVROS = stringPreferencesKey("lista_livros_json")
}