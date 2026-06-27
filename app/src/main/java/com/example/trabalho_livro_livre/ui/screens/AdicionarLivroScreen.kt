package com.example.trabalho_livro_livre.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.trabalho_livro_livre.data.models.LivroPersistido

// Modelo estritamente visual usado apenas para renderização da linha
data class LivroAnunciadoVisual(
    val titulo: String,
    val autor: String,
    val infoTag: String,
    val corTagBg: Color,
    val corTagTexto: Color,
    val corCapaSimulada: Color
)

@Composable
fun AdicionarLivroScreen(
    modifier: Modifier = Modifier,
    livrosAtuais: List<LivroPersistido> = emptyList(), // Conectado à lista reativa do DataStore
    onSalvarAnuncio: (String, String, String, String, String, String) -> Unit = { _, _, _, _, _, _ -> }, // Callback da ViewModel
    onNavegarParaHome: () -> Unit = {},
    onNavegarParaPerfil: () -> Unit = {}
) {
    // Estados locais do Formulário
    var titulo by remember { mutableStateOf("") }
    var autor by remember { mutableStateOf("") }
    var preco by remember { mutableStateOf("") }
    var descricao by remember { mutableStateOf("") }

    // Estados fixos simulados para os seletores (podem ser expandidos no futuro)
    val tipoAnuncioSelecionado = "VENDA"
    val condicaoSelecionada = "Novo"

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFFAFAFA))
    ) {
        // --- CONTEÚDO ROLÁVEL (Formulário + Listagem) ---
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

                // 1. Cabeçalho Principal
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

                // 2. Título da Seção
                BasicText(
                    text = "Meus Anúncios",
                    style = TextStyle(color = Color(0xFF0F2C3D), fontSize = 22.sp, fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.height(2.dp))
                BasicText(
                    text = "Gerencie sua biblioteca compartilhada",
                    style = TextStyle(color = Color(0xFF6B7280), fontSize = 14.sp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // 3. Card Branco do Formulário
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White, RoundedCornerShape(12.dp))
                        .border(1.dp, Color(0xFFE5E7EB), RoundedCornerShape(12.dp))
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    // Box de Upload Pontilhado Customizado
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(110.dp)
                            .background(Color(0xFFF9FAFB), RoundedCornerShape(8.dp))
                            .border(width = 1.dp, color = Color(0xFF9CA3AF), shape = RoundedCornerShape(8.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            BasicText("☁️", style = TextStyle(fontSize = 24.sp))
                            Spacer(modifier = Modifier.height(4.dp))
                            BasicText("Upload da Imagem da Capa", style = TextStyle(fontSize = 14.sp, color = Color(0xFF4B5563), fontWeight = FontWeight.Medium))
                            BasicText("PNG, JPG até 5MB", style = TextStyle(fontSize = 12.sp, color = Color(0xFF9CA3AF)))
                        }
                    }

                    // Campo: Título
                    Column {
                        BasicText("Título", style = TextStyle(fontSize = 13.sp, color = Color(0xFF4B5563), fontWeight = FontWeight.Medium))
                        Spacer(modifier = Modifier.height(4.dp))
                        CustomInputNativo(valor = titulo, onValorAlterado = { titulo = it }, dica = "Ex: O Pequeno Príncipe")
                    }

                    // Campo: Autor
                    Column {
                        BasicText("Autor", style = TextStyle(fontSize = 13.sp, color = Color(0xFF4B5563), fontWeight = FontWeight.Medium))
                        Spacer(modifier = Modifier.height(4.dp))
                        CustomInputNativo(valor = autor, onValorAlterado = { autor = it }, dica = "Ex: Antoine de Saint-Exupéry")
                    }

                    // Linha dupla: Preço e Negociação
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            BasicText("Preço (R$)", style = TextStyle(fontSize = 13.sp, color = Color(0xFF4B5563), fontWeight = FontWeight.Medium))
                            Spacer(modifier = Modifier.height(4.dp))
                            CustomInputNativo(valor = preco, onValorAlterado = { preco = it }, dica = "0,00")
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            BasicText("Negociação", style = TextStyle(fontSize = 13.sp, color = Color(0xFF4B5563), fontWeight = FontWeight.Medium))
                            Spacer(modifier = Modifier.height(4.dp))
                            CustomSeletorNativo(textoOpcao = tipoAnuncioSelecionado)
                        }
                    }

                    // Campo: Estado de Conservação
                    Column {
                        BasicText("Estado de Conservação", style = TextStyle(fontSize = 13.sp, color = Color(0xFF4B5563), fontWeight = FontWeight.Medium))
                        Spacer(modifier = Modifier.height(4.dp))
                        CustomSeletorNativo(textoOpcao = condicaoSelecionada)
                    }

                    // Campo: Descrição
                    Column {
                        BasicText("Descrição", style = TextStyle(fontSize = 13.sp, color = Color(0xFF4B5563), fontWeight = FontWeight.Medium))
                        Spacer(modifier = Modifier.height(4.dp))
                        CustomInputNativo(
                            valor = descricao,
                            onValorAlterado = { descricao = it },
                            dica = "Conte um pouco sobre a história desse livro...",
                            modifier = Modifier.height(80.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    // Botão Salvar Anúncio Conectado ao DataStore
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp)
                            .background(Color(0xFF1C354E), RoundedCornerShape(8.dp))
                            .clickable {
                                if (titulo.isNotBlank() && autor.isNotBlank()) {
                                    // Dispara o evento de salvamento real para o DataStore
                                    onSalvarAnuncio(
                                        titulo,
                                        autor,
                                        preco.ifEmpty { "0.00" },
                                        tipoAnuncioSelecionado,
                                        condicaoSelecionada,
                                        descricao
                                    )
                                    // Limpa os campos do formulário para o próximo input
                                    titulo = ""
                                    autor = ""
                                    preco = ""
                                    descricao = ""
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        BasicText(
                            text = "Salvar Anúncio",
                            style = TextStyle(color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // 4. Seção Listados Recentemente Dinâmica
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    BasicText(
                        text = "Listados Recentemente",
                        style = TextStyle(color = Color(0xFF0F2C3D), fontSize = 15.sp, fontWeight = FontWeight.Bold)
                    )
                    BasicText(
                        text = "${livrosAtuais.size} itens",
                        style = TextStyle(color = Color.Gray, fontSize = 13.sp)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Renderização reativa dos livros vindo do DataStore
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(bottom = 24.dp)
                ) {
                    if (livrosAtuais.isEmpty()) {
                        BasicText(
                            text = "Nenhum livro cadastrado ainda.",
                            style = TextStyle(color = Color.Gray, fontSize = 13.sp)
                        )
                    } else {
                        livrosAtuais.forEach { livro ->
                            LinhaLivroAnunciado(
                                livro = LivroAnunciadoVisual(
                                    titulo = livro.titulo,
                                    autor = livro.autor,
                                    infoTag = if (livro.preco == "0.00" || livro.preco.isEmpty()) livro.tipoAnuncio else "R$ ${livro.preco}",
                                    corTagBg = if (livro.preco == "0.00" || livro.preco.isEmpty()) Color(0xFFE2F5EC) else Color(0xFFE3F2FD),
                                    corTagTexto = if (livro.preco == "0.00" || livro.preco.isEmpty()) Color(0xFF2E7D32) else Color(0xFF1565C0),
                                    corCapaSimulada = Color(0xFF2C435A)
                                )
                            )
                        }
                    }
                }
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

            // Aba 2: Library (Ativa)
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
                    BasicText(text = "📖", style = TextStyle(fontSize = 16.sp))
                }
                Spacer(modifier = Modifier.height(2.dp))
                BasicText(text = "Library", style = TextStyle(color = Color(0xFF43624E), fontSize = 11.sp, fontWeight = FontWeight.Bold))
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

// --- COMPONENTES AUXILIARES LOCAIS (NATIVOS) ---

@Composable
fun CustomInputNativo(
    valor: String,
    onValorAlterado: (String) -> Unit,
    dica: String,
    modifier: Modifier = Modifier
) {
    BasicTextField(
        value = valor,
        onValueChange = onValorAlterado,
        textStyle = TextStyle(color = Color.Black, fontSize = 14.sp),
        modifier = modifier
            .fillMaxWidth()
            .height(40.dp)
            .background(Color.White, RoundedCornerShape(6.dp))
            .border(1.dp, Color(0xFFCCCCCC), RoundedCornerShape(6.dp))
            .padding(horizontal = 10.dp, vertical = 10.dp),
        decorationBox = { innerTextField ->
            if (valor.isEmpty()) {
                BasicText(
                    text = dica,
                    style = TextStyle(color = Color(0xFF9CA3AF), fontSize = 14.sp)
                )
            }
            innerTextField()
        }
    )
}

@Composable
fun CustomSeletorNativo(textoOpcao: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp)
            .background(Color.White, RoundedCornerShape(6.dp))
            .border(1.dp, Color(0xFFCCCCCC), RoundedCornerShape(6.dp))
            .padding(horizontal = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        BasicText(text = textoOpcao, style = TextStyle(color = Color.Black, fontSize = 14.sp))
        BasicText(text = "▼", style = TextStyle(color = Color.Gray, fontSize = 10.sp))
    }
}

@Composable
fun LinhaLivroAnunciado(livro: LivroAnunciadoVisual) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(12.dp))
            .border(1.dp, Color(0xFFE5E7EB), RoundedCornerShape(12.dp))
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(width = 46.dp, height = 64.dp)
                .background(livro.corCapaSimulada, RoundedCornerShape(4.dp))
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            BasicText(
                text = livro.titulo,
                style = TextStyle(color = Color(0xFF1F2937), fontSize = 14.sp, fontWeight = FontWeight.Bold)
            )
            BasicText(
                text = livro.autor,
                style = TextStyle(color = Color(0xFF6B7280), fontSize = 12.sp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Box(
                modifier = Modifier
                    .background(livro.corTagBg, RoundedCornerShape(4.dp))
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
                BasicText(
                    text = livro.infoTag,
                    style = TextStyle(color = livro.corTagTexto, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                )
            }
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            BasicText(text = "✏️", modifier = Modifier.clickable { }, style = TextStyle(fontSize = 14.sp))
            BasicText(text = "🗑️", modifier = Modifier.clickable { }, style = TextStyle(fontSize = 14.sp))
        }
    }
}