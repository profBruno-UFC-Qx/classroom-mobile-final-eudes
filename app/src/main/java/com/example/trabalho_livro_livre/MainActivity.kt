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
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import kotlinx.serialization.Serializable

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
) {
    val backStack = rememberNavBackStack(LoginKey)

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

            // 1. Tela de Login
            entry<LoginKey>(
                metadata = ListDetailSceneStrategy.listPane()
            ) {
                LoginScreen(
                    onLoginSucesso = {
                        backStack.add(HomeKey)
                    },
                    onNavegarParaRegistro = {
                        backStack.add(RegistroKey)
                    }
                )
            }

            // 2. Tela de Registro
            entry<RegistroKey>(
                metadata = ListDetailSceneStrategy.detailPane()
            ) {
                RegistroScreen(
                    onRegistroSucesso = {
                        backStack.add(HomeKey)
                    },
                    onVoltarParaLogin = {
                        if (backStack.size > 1) {
                            backStack.removeAt(backStack.lastIndex)
                        }
                    }
                )
            }

            // 3. Tela Home
            entry<HomeKey>(
                metadata = ListDetailSceneStrategy.listPane(
                    detailPlaceholder = {
                        Text("Escolha um livro da lista para ver os detalhes")
                    }
                )
            ) {
                HomeScreen(
                    onNavegarParaAdicionar = {
                        backStack.add(AdicionarLivroKey)
                    },
                    onNavegarParaPerfil = {
                        backStack.add(PerfilKey)
                    },
                    onLivroClicado = { chaveLivro ->
                        // ATUALIZADO: Captura a chave enviada no clique do item e adiciona à pilha
                        backStack.add(chaveLivro)
                    }
                )
            }

            // 4. Tela Detalhes do Livro
            entry<DetalhesLivroKey>(
                metadata = ListDetailSceneStrategy.detailPane()
            ) {
                DetalhesLivroScreen(
                    isDonoDoAnuncio = it.isDono,
                    tituloLivro = it.titulo,
                    autorLivro = it.autor,
                    tipoAnuncio = it.tipoAnuncio,
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

            // 5. Tela Adicionar Livro
            entry<AdicionarLivroKey>(
                metadata = ListDetailSceneStrategy.listPane()
            ) {
                AdicionarLivroScreen(
                    onNavegarParaHome = {
                        backStack.add(HomeKey)
                    },
                    onNavegarParaPerfil = {
                        backStack.add(PerfilKey)
                    }
                )
            }

            // 6. Tela Perfil
            entry<PerfilKey>(
                metadata = ListDetailSceneStrategy.listPane()
            ) {
                PerfilScreen(
                    onNavegarParaHome = {
                        backStack.add(HomeKey)
                    },
                    onNavegarParaAdicionar = {
                        backStack.add(AdicionarLivroKey)
                    },
                    onLogout = {
                        backStack.add(LoginKey)
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