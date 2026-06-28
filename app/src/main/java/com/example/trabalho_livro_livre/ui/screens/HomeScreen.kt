package com.example.trabalho_livro_livre.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.trabalho_livro_livre.DetalhesLivroKey
import com.example.trabalho_livro_livre.data.models.LivroPersistido

// Modelo de dados unificado para exibição visual na Home
data class LivroHome(
    val id: String,
    val titulo: String,
    val autor: String,
    val tipo: String,
    val condicao: String = "",    // Adicionado para suportar a Key
    val descricao: String = "",
    val corTagBg: Color,
    val corTagTexto: Color,
    val corCapaSimulada: Color,
    val isDono: Boolean
)

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    livros: List<LivroPersistido> = emptyList(), // Lista dinâmica vinda do DataStore
    usuarioWhatsapp: String = "", // ADICIONADO: Usado para validar se o usuário é o dono do anúncio clicado
    onNavegarParaAdicionar: () -> Unit = {},
    onNavegarParaPerfil: () -> Unit = {},
    onLivroClicado: (DetalhesLivroKey) -> Unit = {}
) {
    var busca by remember { mutableStateOf("") }
    val categorias = listOf("Tudo", "Ficção", "Didáticos", "Romance", "Suspenso")

    // Livros mockados/padrão que já iniciam no aplicativo
    val livrosPadrao = listOf(
        LivroHome("1", "A Metamorfose", "Franz Kafka", "DOAÇÃO", "Usado", "Livro clássico", Color(0xFFE2F5EC), Color(0xFF2E7D32), Color(0xFF2C435A), false),
        LivroHome("2", "1984", "George Orwell", "TROCA", "Bom", "Distopia", Color(0xFFE3F2FD), Color(0xFF1565C0), Color(0xFF3B5249), false),
        LivroHome("3", "O Pequeno Príncipe", "Saint-Exupéry", "VENDA", "Novo", "Infantil", Color(0xFFFFF9C4), Color(0xFFF57F17), Color(0xFF5E4B3C), false),
        LivroHome("4", "Dom Casmurro", "Machado de Assis", "DOAÇÃO", "Usado", "Clássico brasileiro", Color(0xFFE2F5EC), Color(0xFF2E7D32), Color(0xFFD9D9D9), false)
    )

    // Mapeia os livros cadastrados dinamicamente no DataStore para o padrão visual da Home
    val livrosDinamicos = livros.map { livro ->
        LivroHome(
            id = livro.id,
            titulo = livro.titulo,
            autor = livro.autor,
            tipo = if (livro.preco == "0.00" || livro.preco.isEmpty()) livro.tipoAnuncio else "R$ ${livro.preco}",
            condicao = livro.condicao,
            descricao = livro.descricao,
            corTagBg = if (livro.preco == "0.00" || livro.preco.isEmpty()) Color(0xFFE2F5EC) else Color(0xFFFFF9C4),
            corTagTexto = if (livro.preco == "0.00" || livro.preco.isEmpty()) Color(0xFF2E7D32) else Color(0xFFF57F17),
            corCapaSimulada = Color(0xFF1C354E),
            // ALTERADO: Agora valida dinamicamente se o WhatsApp de quem criou bate com o logado
            isDono = livro.donoAnuncio == usuarioWhatsapp
        )
    }

    // Une as duas listas para renderização (Os criados recentemente aparecem primeiro)
    val listaCompletaLivros = livrosDinamicos + livrosPadrao

    // Filtra a lista completa baseado no texto digitado no campo de busca
    val listaFiltrada = if (busca.isBlank()) {
        listaCompletaLivros
    } else {
        listaCompletaLivros.filter {
            it.titulo.contains(busca, ignoreCase = true) || it.autor.contains(busca, ignoreCase = true)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFFAFAFA))
    ) {
        // --- ÁREA DE CONTEÚDO ---
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            ) {
                // 1. Cabeçalho
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    BasicText(
                        text = "LIVRO LIVRE",
                        style = TextStyle(color = Color(0xFF0F2C3D), fontSize = 22.sp, fontWeight = FontWeight.Bold)
                    )
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(Color(0xFFD9D9D9), CircleShape)
                            .clickable { onNavegarParaPerfil() },
                        contentAlignment = Alignment.Center
                    ) {
                        BasicText(text = "👨", style = TextStyle(fontSize = 18.sp))
                    }
                }

                // 2. Campo de Busca
                BasicTextField(
                    value = busca,
                    onValueChange = { busca = it },
                    textStyle = TextStyle(color = Color.Black, fontSize = 14.sp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(46.dp)
                        .background(Color(0xFFF3F4F6), RoundedCornerShape(10.dp))
                        .border(1.dp, Color(0xFFE5E7EB), RoundedCornerShape(10.dp))
                        .padding(horizontal = 12.dp, vertical = 12.dp),
                    decorationBox = { innerTextField ->
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            BasicText(text = "🔍  ", style = TextStyle(fontSize = 14.sp))
                            if (busca.isEmpty()) {
                                BasicText(
                                    text = "Buscar por título, autor...",
                                    style = TextStyle(color = Color.Gray, fontSize = 14.sp)
                                )
                            }
                            innerTextField()
                        }
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // 3. Categorias
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(categorias) { cat ->
                        val isSelecionado = cat == "Tudo"
                        Box(
                            modifier = Modifier
                                .background(
                                    color = if (isSelecionado) Color(0xFFE2F5EC) else Color.White,
                                    shape = RoundedCornerShape(20.dp)
                                )
                                .border(
                                    width = 1.dp,
                                    color = if (isSelecionado) Color(0xFFE2F5EC) else Color(0xFFE5E7EB),
                                    shape = RoundedCornerShape(20.dp)
                                )
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                        ) {
                            BasicText(
                                text = cat,
                                style = TextStyle(
                                    color = if (isSelecionado) Color(0xFF2E7D32) else Color(0xFF4B5563),
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // 4. Grid Dinâmico de Livros
                if (listaFiltrada.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        BasicText(
                            text = "Nenhum livro encontrado.",
                            style = TextStyle(color = Color.Gray, fontSize = 14.sp)
                        )
                    }
                } else {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(listaFiltrada) { livro ->
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color.White, RoundedCornerShape(8.dp))
                                    .border(1.dp, Color(0xFFE5E7EB), RoundedCornerShape(8.dp))
                                    .clickable {
                                        // Aciona a rota repassando se REALMENTE é o dono comparado na linha 53
                                        onLivroClicado(
                                            DetalhesLivroKey(
                                                id = livro.id,
                                                titulo = livro.titulo,
                                                autor = livro.autor,
                                                tipoAnuncio = livro.tipo,
                                                condicao = livro.condicao,
                                                descricao = livro.descricao,
                                                isDono = livro.isDono
                                            )
                                        )
                                    }
                            ) {
                                Column {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(150.dp)
                                            .background(livro.corCapaSimulada)
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .padding(8.dp)
                                                .background(livro.corTagBg, RoundedCornerShape(4.dp))
                                                .padding(horizontal = 6.dp, vertical = 3.dp)
                                        ) {
                                            BasicText(
                                                text = livro.tipo,
                                                style = TextStyle(
                                                    color = livro.corTagTexto,
                                                    fontSize = 9.sp,
                                                    fontWeight = FontWeight.Bold
                                                )
                                            )
                                        }
                                    }

                                    Column(modifier = Modifier.padding(8.dp)) {
                                        BasicText(
                                            text = livro.titulo,
                                            style = TextStyle(color = Color(0xFF1F2937), fontSize = 14.sp, fontWeight = FontWeight.Bold),
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                        Spacer(modifier = Modifier.height(2.dp))
                                        BasicText(
                                            text = livro.autor,
                                            style = TextStyle(color = Color(0xFF6B7280), fontSize = 12.sp),
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // 5. Botão Flutuante (+)
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(bottom = 16.dp, end = 16.dp)
                    .size(54.dp)
                    .background(Color(0xFF43624E), CircleShape)
                    .clickable { onNavegarParaAdicionar() },
                contentAlignment = Alignment.Center
            ) {
                BasicText(
                    text = "+",
                    style = TextStyle(color = White, fontSize = 26.sp, fontWeight = FontWeight.Light, textAlign = TextAlign.Center)
                )
            }
        }

        // --- BARRA DE NAVEGAÇÃO DO RODAPÉ ---
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(62.dp)
                .background(Color.White)
                .border(width = 1.dp, color = Color(0xFFE5E7EB)),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Aba 1: Home (Ativa)
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                BasicText(text = "🏠", style = TextStyle(fontSize = 18.sp))
                Spacer(modifier = Modifier.height(2.dp))
                BasicText(text = "Home", style = TextStyle(color = Color(0xFF43624E), fontSize = 11.sp, fontWeight = FontWeight.Bold))
            }

            // Aba 2: Library (Leva para adicionar)
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .clickable { onNavegarParaAdicionar() },
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                BasicText(text = "📖", style = TextStyle(fontSize = 18.sp))
                Spacer(modifier = Modifier.height(2.dp))
                BasicText(text = "Library", style = TextStyle(color = Color.Gray, fontSize = 11.sp))
            }

            // Aba 3: Profile
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .clickable { onNavegarParaPerfil() },
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                BasicText(text = "👤", style = TextStyle(fontSize = 18.sp))
                Spacer(modifier = Modifier.height(2.dp))
                BasicText(text = "Profile", style = TextStyle(color = Color.Gray, fontSize = 11.sp))
            }
        }
    }
}

// Cor branca genérica declarada localmente para manter a independência de imports do Material Theme
val White = Color(0xFFFFFFFF)

@Preview(showBackground = true, device = "id:pixel_5")
@Composable
fun HomePreview() {
    HomeScreen()
}