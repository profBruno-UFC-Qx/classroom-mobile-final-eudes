package com.example.trabalho_livro_livre

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

// Modelo de dados local mantido para os livros
data class LivroHome(
    val titulo: String,
    val autor: String,
    val tipo: String,
    val corTagBg: Color,
    val corTagTexto: Color,
    val corCapaSimulada: Color
)

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onNavegarParaAdicionar: () -> Unit = {},
    onNavegarParaPerfil: () -> Unit = {},
    onLivroClicado: (DetalhesLivroKey) -> Unit = {} // ADICIONADO: Lambda para capturar o clique no livro
) {
    var busca by remember { mutableStateOf("") }
    val categorias = listOf("Tudo", "Ficção", "Didáticos", "Romance", "Suspenso")

    val listaLivros = listOf(
        LivroHome("A Metamorfose", "Franz Kafka", "DOAÇÃO", Color(0xFFE2F5EC), Color(0xFF2E7D32), Color(0xFF2C435A)),
        LivroHome("1984", "George Orwell", "TROCA", Color(0xFFE3F2FD), Color(0xFF1565C0), Color(0xFF3B5249)),
        LivroHome("O Pequeno...", "Saint-Exupéry", "VENDA", Color(0xFFFFF9C4), Color(0xFFF57F17), Color(0xFF5E4B3C)),
        LivroHome("Dom Casmurro", "Machado de Assis", "DOAÇÃO", Color(0xFFE2F5EC), Color(0xFF2E7D32), Color(0xFFD9D9D9)),
        LivroHome("Sapiens", "Yuval Noah Harari", "TROCA", Color(0xFFE3F2FD), Color(0xFF1565C0), Color(0xFF738276)),
        LivroHome("O Alquimista", "Paulo Coelho", "VENDA", Color(0xFFFFF9C4), Color(0xFFF57F17), Color(0xFF1C1C1C))
    )

    // Layout estrutural usando Column para separar a Área de Conteúdo da Barra de Rodapé fixa
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFFAFAFA))
    ) {

        // --- ÁREA DE CONTEÚDO (Ocupa todo o espaço restante) ---
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
                // 1. Cabeçalho (Título e Foto de Perfil)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    BasicText(
                        text = "LIVRO LIVRE",
                        style = TextStyle(
                            color = Color(0xFF0F2C3D),
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(Color(0xFFD9D9D9), CircleShape)
                            .clickable { onNavegarParaPerfil() },
                        contentAlignment = Alignment.Center
                    ) {
                        BasicText(
                            text = "👨",
                            style = TextStyle(fontSize = 18.sp)
                        )
                    }
                }

                // 2. Campo de Busca customizado (BasicTextField)
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
                            BasicText(
                                text = "🔍  ",
                                style = TextStyle(fontSize = 14.sp)
                            )
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

                // 3. Carrossel de Categorias
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

                // 4. Grid de Livros (2 colunas)
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(listaLivros) { livro ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.White, RoundedCornerShape(8.dp))
                                .border(1.dp, Color(0xFFE5E7EB), RoundedCornerShape(8.dp))
                                .clickable {
                                    // ALTERAÇÃO AQUI: Dispara a ação de clique enviando os dados mapeados para a chave do Navigation 3
                                    onLivroClicado(
                                        DetalhesLivroKey(
                                            titulo = livro.titulo,
                                            autor = livro.autor,
                                            tipoAnuncio = livro.tipo,
                                            isDono = false // Pode ser alterado para testar visões de dono do anúncio
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
                                        style = TextStyle(
                                            color = Color(0xFF1F2937),
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Bold
                                        ),
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    BasicText(
                                        text = livro.autor,
                                        style = TextStyle(
                                            color = Color(0xFF6B7280),
                                            fontSize = 12.sp
                                        ),
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // 5. Botão Flutuante de Adição (+) sobreposto no canto inferior direito
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
                    style = TextStyle(
                        color = Color.White,
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Light,
                        textAlign = TextAlign.Center
                    )
                )
            }
        }

        // --- BARRA DE NAVEGAÇÃO DO RODAPÉ (Customizada sem Material) ---
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
                BasicText(
                    text = "🏠",
                    style = TextStyle(fontSize = 18.sp)
                )
                Spacer(modifier = Modifier.height(2.dp))
                BasicText(
                    text = "Home",
                    style = TextStyle(
                        color = Color(0xFF43624E), // Verde destacado da aba ativa
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
            }

            // Aba 2: Library (Morta por enquanto)
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                BasicText(
                    text = "📖",
                    style = TextStyle(fontSize = 18.sp)
                )
                Spacer(modifier = Modifier.height(2.dp))
                BasicText(
                    text = "Library",
                    style = TextStyle(
                        color = Color.Gray,
                        fontSize = 11.sp
                    )
                )
            }

            // Aba 3: Profile (Leva para a tela de Perfil)
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .clickable { onNavegarParaPerfil() },
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                BasicText(
                    text = "👤",
                    style = TextStyle(fontSize = 18.sp)
                )
                Spacer(modifier = Modifier.height(2.dp))
                BasicText(
                    text = "Profile",
                    style = TextStyle(
                        color = Color.Gray,
                        fontSize = 11.sp
                    )
                )
            }
        }
    }
}

@Preview(showBackground = true, device = "id:pixel_5")
@Composable
fun HomePreview() {
    HomeScreen()
}