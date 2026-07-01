package com.example.trabalho_livro_livre.ui.screens

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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PerfilScreen(
    modifier: Modifier = Modifier,
    nomeUsuario: String = "Usuário",
    whatsappUsuario: String = "Quixadá, Ceará", // Substitui a localização antiga
    onNavegarParaHome: () -> Unit = {},
    onNavegarParaAdicionar: () -> Unit = {},
    onLogout: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFFAFAFA))
    ) {
        // --- ÁREA DE CONTEÚDO ROLÁVEL ---
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp)
            ) {
                // 1. Cabeçalho Superior padrão (Igual ao das imagens image_78a440.png e image_789fe6.png)
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
                            .background(Color(0xFFD9D9D9), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        BasicText(text = "👨", style = TextStyle(fontSize = 18.sp))
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // 2. Card Principal de Informações do Usuário (Baseado na imagem image_6ebde2.png)
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White, RoundedCornerShape(16.dp))
                        .border(1.dp, Color(0xFFE5E7EB), RoundedCornerShape(16.dp))
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Foto de perfil grande arredondada com sobreposição simulada
                    Box(
                        modifier = Modifier
                            .size(84.dp)
                            .background(Color(0xFFEBF1F5), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        BasicText(text = "👨‍💻", style = TextStyle(fontSize = 42.sp))
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Nome e Localização conforme o print image_6ebde2.png
                    BasicText(
                        text = nomeUsuario,
                        style = TextStyle(color = Color(0xFF0F2C3D), fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(4.dp))

                }

                Spacer(modifier = Modifier.height(16.dp))



                Spacer(modifier = Modifier.height(20.dp))

                // 4. Lista de Opções Estilizadas em Linhas dentro do Card Branco
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White, RoundedCornerShape(12.dp))
                        .border(1.dp, Color(0xFFE5E7EB), RoundedCornerShape(12.dp))
                ) {


                    // Opção de Sair com cor de alerta (Vermelho) conforme o mockup
                    LinhaOpcaoPerfil(
                        icone = "🚪",
                        texto = "Sair",
                        corTexto = Color(0xFFDC2626),
                        corSeta = Color(0xFFDC2626),
                        onClick = { onLogout() }
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }

        // --- BARRA DE NAVEGAÇÃO DO RODAPÉ (Com foco ativo na aba Profile) ---
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(62.dp)
                .background(Color.White)
                .border(width = 1.dp, color = Color(0xFFE5E7EB)),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Aba 1: Home
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .clickable { onNavegarParaHome() },
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                BasicText(text = "🏠", style = TextStyle(fontSize = 18.sp))
                Spacer(modifier = Modifier.height(2.dp))
                BasicText(text = "Home", style = TextStyle(color = Color.Gray, fontSize = 11.sp))
            }

            // Aba 2: Library / Anúncios
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

            // Aba 3: Profile (Ativa e destacada com o fundo verde sutil idêntico à imagem image_6ebde2.png)
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .background(Color(0xFFE2F5EC), RoundedCornerShape(12.dp))
                        .padding(horizontal = 14.dp, vertical = 2.dp),
                    contentAlignment = Alignment.Center
                ) {
                    BasicText(text = "👤", style = TextStyle(fontSize = 16.sp))
                }
                Spacer(modifier = Modifier.height(2.dp))
                BasicText(
                    text = "Profile",
                    style = TextStyle(color = Color(0xFF43624E), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                )
            }
        }
    }
}

// --- COMPONENTES AUXILIARES INTERNOS CORRIGIDOS ---

@Composable
fun ItemMetricaPerfil(valor: String, label: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .background(Color.White, RoundedCornerShape(10.dp))
            .border(1.dp, Color(0xFFE5E7EB), RoundedCornerShape(10.dp))
            .padding(vertical = 12.dp, horizontal = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        BasicText(
            text = valor,
            style = TextStyle(color = Color(0xFF1C354E), fontSize = 16.sp, fontWeight = FontWeight.Bold)
        )
        Spacer(modifier = Modifier.height(2.dp))
        BasicText(
            text = label,
            style = TextStyle(
                color = Color(0xFF6B7280),
                fontSize = 11.sp,
                textAlign = TextAlign.Center // Corrigido: Agora dentro de TextStyle!
            )
        )
    }
}

@Composable
fun LinhaOpcaoPerfil(
    icone: String,
    texto: String,
    corTexto: Color = Color(0xFF374151),
    corSeta: Color = Color(0xFFD1D5DB),
    onClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 14.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            BasicText(text = icone, style = TextStyle(fontSize = 16.sp))
            Spacer(modifier = Modifier.width(12.dp))
            BasicText(
                text = texto,
                style = TextStyle(color = corTexto, fontSize = 14.sp, fontWeight = FontWeight.Medium)
            )
        }
        BasicText(
            text = "❯",
            style = TextStyle(color = corSeta, fontSize = 12.sp)
        )
    }
}

@Composable
fun LinhaDivisoria() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(Color(0xFFF3F4F6))
    )
}

@Preview(showBackground = true, device = "id:pixel_5")
@Composable
fun PerfilPreview() {
    PerfilScreen()
}