package com.example.trabalho_livro_livre

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DetalhesLivroScreen(
    modifier: Modifier = Modifier,
    isDonoDoAnuncio: Boolean = false, // <- Nova flag: define qual versão da tela vai aparecer
    tituloLivro: String = "A Metamorfose",
    autorLivro: String = "Franz Kafka",
    categoriaLivro: String = "Ficção",
    condicaoLivro: String = "Usado (Ótimo Estado)",
    tipoAnuncio: String = "DOAÇÃO",
    descricaoLivro: String = "A Metamorfose é a mais célebre novela de Franz Kafka e uma das mais importantes da história da literatura. O texto narra a história de Gregor Samsa, um caixeiro-viajante que abandona as suas próprias vontades para sustentar a família.",
    corCapaSimulada: Color = Color(0xFF2C435A),
    onVoltar: () -> Unit = {},
    onEditarAnuncio: () -> Unit = {},
    onExcluirAnuncio: () -> Unit = {},
    onChamarNoWhatsapp: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFFAFAFA))
    ) {
        // 1. Cabeçalho Superior com Botão de Voltar e Título da Tela
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(Color.White, CircleShape)
                    .border(1.dp, Color(0xFFE5E7EB), CircleShape)
                    .clickable { onVoltar() },
                contentAlignment = Alignment.Center
            ) {
                BasicText(
                    text = "❮",
                    style = TextStyle(color = Color(0xFF0F2C3D), fontSize = 16.sp, fontWeight = FontWeight.Bold)
                )
            }

            BasicText(
                text = "Detalhes do Livro",
                style = TextStyle(color = Color(0xFF0F2C3D), fontSize = 18.sp, fontWeight = FontWeight.Bold)
            )

            Spacer(modifier = Modifier.size(40.dp))
        }

        // --- ÁREA DE CONTEÚDO ROLÁVEL ---
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
        ) {

            // 2. Área de Exibição da Capa do Livro
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp)
                    .background(corCapaSimulada, RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.TopStart
            ) {
                Box(
                    modifier = Modifier
                        .padding(12.dp)
                        .background(Color(0xFFE2F5EC), RoundedCornerShape(6.dp))
                        .padding(horizontal = 10.dp, vertical = 5.dp)
                ) {
                    BasicText(
                        text = tipoAnuncio,
                        style = TextStyle(color = Color(0xFF2E7D32), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 3. Bloco de Informações Textuais (Modo Leitura)
            BasicText(
                text = tituloLivro,
                style = TextStyle(color = Color(0xFF0F2C3D), fontSize = 24.sp, fontWeight = FontWeight.Bold)
            )

            Spacer(modifier = Modifier.height(4.dp))

            BasicText(
                text = "por $autorLivro",
                style = TextStyle(color = Color(0xFF6B7280), fontSize = 16.sp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Ficha Técnica Rápida (Categoria e Condição)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                FichaInformativaItem(label = "Categoria", valor = categoriaLivro, modifier = Modifier.weight(1f))
                FichaInformativaItem(label = "Condição", valor = condicaoLivro, modifier = Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Seção: Descrição
            BasicText(
                text = "Descrição",
                style = TextStyle(color = Color(0xFF0F2C3D), fontSize = 16.sp, fontWeight = FontWeight.Bold)
            )

            Spacer(modifier = Modifier.height(6.dp))

            BasicText(
                text = descricaoLivro,
                style = TextStyle(color = Color(0xFF4B5563), fontSize = 14.sp, lineHeight = 20.sp)
            )

            Spacer(modifier = Modifier.height(30.dp))

            // 4. Painel de Ações Condicional (Dono vs Consumidor)
            if (isDonoDoAnuncio) {
                // VISÃO DO DONO: Só exibe botões de Editar e Excluir
                Row(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Botão Editar
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(46.dp)
                            .background(Color.White, RoundedCornerShape(8.dp))
                            .border(1.dp, Color(0xFFE5E7EB), RoundedCornerShape(8.dp))
                            .clickable { onEditarAnuncio() },
                        contentAlignment = Alignment.Center
                    ) {
                        BasicText(
                            text = "✏️  Editar",
                            style = TextStyle(color = Color(0xFF1F2937), fontSize = 14.sp, fontWeight = FontWeight.Medium)
                        )
                    }

                    // Botão Excluir
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(46.dp)
                            .background(Color(0xFFFEE2E2), RoundedCornerShape(8.dp))
                            .border(1.dp, Color(0xFFFCA5A5), RoundedCornerShape(8.dp))
                            .clickable { onExcluirAnuncio() },
                        contentAlignment = Alignment.Center
                    ) {
                        BasicText(
                            text = "🗑️  Excluir",
                            style = TextStyle(color = Color(0xFFDC2626), fontSize = 14.sp, fontWeight = FontWeight.Medium)
                        )
                    }
                }
            } else {
                // VISÃO DO CONSUMIDOR: Só exibe o botão do WhatsApp
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .background(Color(0xFF25D366), RoundedCornerShape(8.dp)) // Verde do WhatsApp
                        .clickable { onChamarNoWhatsapp() }
                        .padding(bottom = 4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    BasicText(
                        text = "Chamar no WhatsApp  💬",
                        style = TextStyle(color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                    )
                }
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}

@Composable
fun FichaInformativaItem(label: String, valor: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .background(Color.White, RoundedCornerShape(8.dp))
            .border(1.dp, Color(0xFFE5E7EB), RoundedCornerShape(8.dp))
            .padding(12.dp)
    ) {
        BasicText(
            text = label,
            style = TextStyle(color = Color(0xFF9CA3AF), fontSize = 11.sp)
        )
        Spacer(modifier = Modifier.height(2.dp))
        BasicText(
            text = valor,
            style = TextStyle(color = Color(0xFF1F2937), fontSize = 13.sp, fontWeight = FontWeight.Bold)
        )
    }
}

// --- PREVIEWS CONFIGURADOS PARA TESTAR AS DUAS VERSÕES NA IDE ---

@Preview(showBackground = true, name = "Visão do Comprador (Consumidor)")
@Composable
fun DetalhesConsumidorPreview() {
    DetalhesLivroScreen(isDonoDoAnuncio = false)
}

@Preview(showBackground = true, name = "Visão do Dono do Anúncio")
@Composable
fun DetalhesDonoPreview() {
    DetalhesLivroScreen(isDonoDoAnuncio = true)
}