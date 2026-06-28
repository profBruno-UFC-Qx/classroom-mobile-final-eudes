package com.example.trabalho_livro_livre

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.layout.calculatePaneScaffoldDirective
import androidx.compose.material3.adaptive.navigation3.ListDetailSceneStrategy
import androidx.compose.material3.adaptive.navigation3.rememberListDetailSceneStrategy
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.example.trabalho_livro_livre.ui.viewmodel.MainViewModel
import com.example.trabalho_livro_livre.ui.screens.*
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel

@Serializable
data object LoginKey : NavKey

@Serializable
data object RegistroKey : NavKey

@Serializable
data object HomeKey : NavKey

@Serializable
data object AdicionarLivroKey : NavKey

@Serializable
data object PerfilKey : NavKey

@Serializable
data class DetalhesLivroKey(
    val id: String,
    val titulo: String,
    val autor: String,
    val tipoAnuncio: String,
    val isDono: Boolean
) : NavKey

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                // CORRIGIDO: Agora renderiza a árvore de navegação principal diretamente
                Greeting(
                    modifier = Modifier.padding(innerPadding)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun Greeting(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = koinViewModel() // Injetado automaticamente pelo Koin
) {
    // Coleta o estado síncrono da sessão para decidir a tela inicial de destino
    val sessao by viewModel.sessaoUsuario.collectAsStateWithLifecycle()
    val livrosCadastrados by viewModel.listaLivros.collectAsStateWithLifecycle()

    // Se o usuário já estiver logado no DataStore, abre direto na Home, caso contrário, Login
    val telaInicial = if (sessao.estaLogado) HomeKey else LoginKey
    val backStack = rememberNavBackStack(telaInicial)

    val windowAdaptiveInfo = currentWindowAdaptiveInfo()
    val directive = remember(windowAdaptiveInfo) {
        calculatePaneScaffoldDirective(windowAdaptiveInfo)
            .copy(horizontalPartitionSpacerSize = 0.dp)
    }
    val listDetailStrategy = rememberListDetailSceneStrategy<NavKey>(directive = directive)

    NavDisplay(
        backStack = backStack,
        sceneStrategy = listDetailStrategy,
        entryProvider = entryProvider {

            // 1. Rota de Login
            entry<LoginKey>(metadata = ListDetailSceneStrategy.listPane()) {
                LoginScreen(
                    onLoginSucesso = { emailDigitado, senhaDigitada, onErrorCallback ->
                        viewModel.tentarLogin(
                            emailDigitado = emailDigitado,
                            senhaDigitada = senhaDigitada,
                            onSucesso = { backStack.add(HomeKey) },
                            onErro = { mensagemDeErro -> onErrorCallback(mensagemDeErro) }
                        )
                    },
                    onNavegarParaRegistro = { backStack.add(RegistroKey) }
                )
            }

            // 2. Rota de Registro
            entry<RegistroKey>(metadata = ListDetailSceneStrategy.detailPane()) {
                RegistroScreen(
                    onRegistroSucesso = { nome, email, whatsapp, senha ->
                        viewModel.registrarUsuario(
                            nome = nome,
                            email = email,
                            whatsapp = whatsapp,
                            senha = senha,
                            onSucesso = { backStack.add(HomeKey) }
                        )
                    },
                    onVoltarParaLogin = {
                        if (backStack.size > 1) backStack.removeAt(backStack.lastIndex)
                    }
                )
            }

            // 3. Tela Home
            entry<HomeKey>(
                metadata = ListDetailSceneStrategy.listPane(
                    detailPlaceholder = { Text("Escolha um livro da lista para ver os detalhes") }
                )
            ) {
                HomeScreen(
                    livros = livrosCadastrados, // Repassa a lista dinâmica sem filtros (vê tudo de todos)
                    onNavegarParaAdicionar = { backStack.add(AdicionarLivroKey) },
                    onNavegarParaPerfil = { backStack.add(PerfilKey) },
                    onLivroClicado = { chaveLivro -> backStack.add(chaveLivro) }
                )
            }

            // 4. Tela Detalhes do Livro
            entry<DetalhesLivroKey>(metadata = ListDetailSceneStrategy.detailPane()) { key ->
                DetalhesLivroScreen(
                    isDonoDoAnuncio = key.isDono,
                    tituloLivro = key.titulo,
                    autorLivro = key.autor,
                    tipoAnuncio = key.tipoAnuncio,
                    onVoltar = {
                        if (backStack.size > 1) {
                            backStack.removeAt(backStack.lastIndex)
                        }
                    },
                    onExcluirAnuncio = {
                        if (backStack.size > 1) {
                            backStack.removeAt(backStack.lastIndex)
                        }
                    }
                )
            }

            // 5. Tela Adicionar Livro (Library)
            entry<AdicionarLivroKey>(metadata = ListDetailSceneStrategy.listPane()) {
                AdicionarLivroScreen(
                    livrosAtuais = livrosCadastrados, // Alimenta a listagem inferior com dados reativos
                    usuarioWhatsapp = sessao.whatsapp, // MODIFICADO: Injeta o WhatsApp da sessão ativa para realizar o filtro interno
                    onSalvarAnuncio = { t, a, p, tipo, cond, desc ->
                        viewModel.adicionarNovoAnuncio(t, a, p, tipo, cond, desc)
                    },
                    onNavegarParaHome = { backStack.add(HomeKey) },
                    onNavegarParaPerfil = { backStack.add(PerfilKey) }
                )
            }

            // 6. Tela Perfil
            entry<PerfilKey>(metadata = ListDetailSceneStrategy.listPane()) {
                PerfilScreen(
                    nomeUsuario = sessao.nome, // Injeta nome salvo de forma dinâmica
                    whatsappUsuario = sessao.whatsapp, // Injeta número salvo de forma dinâmica
                    onNavegarParaHome = { backStack.add(HomeKey) },
                    onNavegarParaAdicionar = { backStack.add(AdicionarLivroKey) },
                    onLogout = {
                        viewModel.deslogar {
                            backStack.add(LoginKey)
                        }
                    }
                )
            }
        },
        predictivePopTransitionSpec = {
            slideInVertically(initialOffsetY = { -it }) togetherWith slideOutVertically(
                targetOffsetY = { it })
        },
        modifier = modifier
    )
}