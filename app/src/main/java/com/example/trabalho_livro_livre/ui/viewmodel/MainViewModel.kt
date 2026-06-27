package com.example.trabalho_livro_livre.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trabalho_livro_livre.data.AppRepository
import com.example.trabalho_livro_livre.data.models.LivroPersistido
import com.example.trabalho_livro_livre.data.models.UsuarioSessao
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID

class MainViewModel(private val repository: AppRepository) : ViewModel() {

    val sessaoUsuario: StateFlow<UsuarioSessao> = repository.sessaoUsuarioFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), UsuarioSessao("", "", false))

    val listaLivros: StateFlow<List<LivroPersistido>> = repository.livrosFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun logarOuCadastrar(nome: String, whatsapp: String, onSucesso: () -> Unit) {
        viewModelScope.launch {
            repository.salvarSessao(nome, whatsapp)
            onSucesso()
        }
    }

    fun deslogar(onSucesso: () -> Unit) {
        viewModelScope.launch {
            repository.apagarSessao()
            onSucesso()
        }
    }

    fun adicionarNovoAnuncio(titulo: String, autor: String, preco: String, tipo: String, condicao: String, descricao: String) {
        viewModelScope.launch {
            val novo = LivroPersistido(
                id = UUID.randomUUID().toString(),
                titulo = titulo,
                autor = autor,
                preco = preco,
                tipoAnuncio = tipo,
                condicao = condicao,
                descricao = descricao
            )
            repository.salvarLivro(novo)
        }
    }
}