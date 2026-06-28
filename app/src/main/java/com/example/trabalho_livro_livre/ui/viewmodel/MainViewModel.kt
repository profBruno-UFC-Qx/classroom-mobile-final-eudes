package com.example.trabalho_livro_livre.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trabalho_livro_livre.data.AppRepository
import com.example.trabalho_livro_livre.data.models.LivroPersistido
import com.example.trabalho_livro_livre.data.models.UsuarioSessao
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID

class MainViewModel(private val repository: AppRepository) : ViewModel() {

    val sessaoUsuario: StateFlow<UsuarioSessao> = repository.sessaoUsuarioFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), UsuarioSessao("", "", false))

    val listaLivros: StateFlow<List<LivroPersistido>> = repository.livrosFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Mantido por compatibilidade interna, se necessário
    fun logarOuCadastrar(nome: String, whatsapp: String, onSucesso: () -> Unit) {
        viewModelScope.launch {
            repository.salvarSessao(nome, whatsapp)
            onSucesso()
        }
    }

    // ADICIONADO: Lógica real para registrar um novo usuário com e-mail e senha locais
    fun registrarUsuario(nome: String, email: String, whatsapp: String, senha: String, onSucesso: () -> Unit) {
        viewModelScope.launch {
            // Salva os dados de cadastro e define as credenciais diretamente no DataStore pelo repositório
            repository.salvarCadastroCompleto(nome, email, whatsapp, senha)
            onSucesso()
        }
    }

    // ADICIONADO: Lógica real que valida se os dados digitados coincidem com o cadastro do dispositivo
    fun tentarLogin(emailDigitado: String, senhaDigitada: String, onSucesso: () -> Unit, onErro: (String) -> Unit) {
        viewModelScope.launch {
            // Captura o estado atual das credenciais diretamente do fluxo do repositório
            val cadastro = repository.obterCadastroSalvo().first()

            if (cadastro.email.isBlank()) {
                onErro("Nenhuma conta encontrada neste dispositivo. Cadastre-se primeiro!")
            } else if (cadastro.email == emailDigitado && cadastro.senha == senhaDigitada) {
                // Credenciais batem! Ativa a sessão do usuário como logado
                repository.salvarSessao(cadastro.nome, cadastro.whatsapp)
                onSucesso()
            } else {
                onErro("E-mail ou senha incorretos.")
            }
        }
    }

    fun deslogar(onSucesso: () -> Unit) {
        viewModelScope.launch {
            repository.apagarSessao()
            onSucesso()
        }
    }

    fun adicionarNovoAnuncio(
        titulo: String,
        autor: String,
        preco: String,
        tipo: String,
        condicao: String,
        descricao: String
    ) {
        viewModelScope.launch {
            // Captura o whatsapp ou nome do usuário que está logado atualmente na sessão
            val usuarioAtual = sessaoUsuario.value.whatsapp

            val novo = LivroPersistido(
                id = UUID.randomUUID().toString(),
                titulo = titulo,
                autor = autor,
                preco = preco,
                tipoAnuncio = tipo,
                condicao = condicao,
                descricao = descricao,
                donoAnuncio = usuarioAtual // ADICIONADO: Vincula o livro ao usuário logado
            )
            repository.salvarLivro(novo)
        }
    }

    // MODIFICADO: Função implementada para realizar a exclusão real do anúncio no repositório
    fun deletarAnuncio(idLivro: String, onSucesso: () -> Unit) {
        viewModelScope.launch {
            // Busca o livro específico na lista atual emitida pelo StateFlow através do ID
            val livroParaRemover = listaLivros.value.find { it.id == idLivro }
            if (livroParaRemover != null) {
                repository.deletarLivro(livroParaRemover)
                onSucesso()
            }
        }
    }
}