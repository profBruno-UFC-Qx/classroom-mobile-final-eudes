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

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.trabalho_livro_livre.R

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
    val condicao: String,       // Adicionado
    val descricao: String,
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
                    livros = livrosCadastrados,
                    usuarioWhatsapp = sessao.whatsapp, // INJETADO: Passa o whatsapp logado para a Home fazer a comparação
                    onNavegarParaAdicionar = { backStack.add(AdicionarLivroKey) },
                    onNavegarParaPerfil = { backStack.add(PerfilKey) },
                    onLivroClicado = { chaveLivro -> backStack.add(chaveLivro) }
                )
            }

            // 4. Tela Detalhes do Livro (ATUALIZADO)
            entry<DetalhesLivroKey>(metadata = ListDetailSceneStrategy.detailPane()) { key ->
                DetalhesLivroScreen(
                    isDonoDoAnuncio = key.isDono,
                    tituloLivro = key.titulo,
                    autorLivro = key.autor,
                    tipoAnuncio = key.tipoAnuncio,
                    condicaoLivro = key.condicao,
                    descricaoLivro = key.descricao,
                    onVoltar = {
                        if (backStack.size > 1) {
                            backStack.removeAt(backStack.lastIndex)
                        }
                    },
                    onExcluirAnuncio = {
                        viewModel.deletarAnuncio(idLivro = key.id) {
                            if (backStack.size > 1) {
                                backStack.removeAt(backStack.lastIndex)
                            }
                        }
                    },
                    // --- ADICIONE ESTA PARTE ---
                    onEditarAnuncio = { novoTitulo, novoAutor, novoPreco, novaDescricao ->
                        viewModel.atualizarAnuncio(
                            idLivro = key.id,
                            novoTitulo = novoTitulo,
                            novoAutor = novoAutor,
                            novoPreco = novoPreco,
                            novaDescricao = novaDescricao
                        )
                    }
                    // ---------------------------
                )
            }

            // 5. Tela Adicionar Livro (Library)
            entry<AdicionarLivroKey>(metadata = ListDetailSceneStrategy.listPane()) {
                AdicionarLivroScreen(
                    livrosAtuais = livrosCadastrados,
                    usuarioWhatsapp = sessao.whatsapp,
                    onSalvarAnuncio = { t, a, p, tipo, cond, desc, ctx ->
                        viewModel.adicionarNovoAnuncio(t, a, p, tipo, cond, desc, ctx)
                    },
                    onLivroClicado = { livro ->
                        backStack.add(
                            DetalhesLivroKey(
                                id = livro.id,
                                titulo = livro.titulo,
                                autor = livro.autor,
                                tipoAnuncio = livro.tipoAnuncio,
                                condicao = livro.condicao, // Agora existe na Key
                                descricao = livro.descricao, // Agora existe na Key
                                isDono = true
                            )
                        )
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

fun exibirNotificacaoLivro(context: Context, tituloLivro: String) {
    val canalId = "canal_livros"
    val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    // 1. Criar canal (Necessário apenas uma vez, mas seguro rodar sempre)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel = NotificationChannel(
            canalId, "Notificações de Livros",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        manager.createNotificationChannel(channel)
    }

    // 2. Construir a notificação
    val notificacao = NotificationCompat.Builder(context, canalId)
        .setSmallIcon(android.R.drawable.ic_dialog_info) // Ícone padrão
        .setContentTitle("Livro Adicionado!")
        .setContentText("O livro '$tituloLivro' foi adicionado com sucesso.")
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .setAutoCancel(true)
        .build()

    // 3. Exibir
    manager.notify(1, notificacao)
}