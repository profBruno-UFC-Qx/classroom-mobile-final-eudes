package com.example.trabalho_livro_livre.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.serialization.Serializable
import androidx.compose.runtime.*
import androidx.compose.ui.text.font.FontStyle
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.android.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import kotlinx.coroutines.launch


@Serializable
data class QuoteResponse(val content: String, val author: String)

@Serializable
data class AdviceResponse(val slip: Slip)

@Serializable
data class Slip(val advice: String)

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    // ALTERADO: Agora envia e-mail, senha e um callback para receber a mensagem de erro da ViewModel
    onLoginSucesso: (String, String, (String) -> Unit) -> Unit = { _, _, _ -> },
    onNavegarParaRegistro: () -> Unit = {}
) {
    var email by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }
    var senhaVisivel by remember { mutableStateOf(false) }

    // Estado para controlar mensagens de validação
    var erroMensagem by remember { mutableStateOf("") }

    var quote by remember { mutableStateOf("Carregando frase...") }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        val client = HttpClient(Android) {
            install(ContentNegotiation) { json(Json { ignoreUnknownKeys = true }) }
        }

        try {
            // Nova API (Advice Slip)
            val response: AdviceResponse = client.get("https://api.adviceslip.com/advice").body()
            quote = "\"${response.slip.advice}\""
        } catch (e: Exception) {
            quote = "Erro: ${e.message}" // Mostra o erro real para sabermos o motivo
        } finally {
            client.close()
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFFAFAFA))
            .padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Logo do App
        BasicText(
            text = "📖 LIVRO LIVRE",
            style = TextStyle(
                color = Color(0xFF0F2C3D),
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        BasicText(
            text = "Conecte-se para compartilhar conhecimento",
            style = TextStyle(color = Color(0xFF6B7280), fontSize = 14.sp, textAlign = TextAlign.Center)
        )

        Spacer(modifier = Modifier.height(36.dp))

        // Card do Formulário
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, RoundedCornerShape(16.dp))
                .border(1.dp, Color(0xFFE5E7EB), RoundedCornerShape(16.dp))
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Campo Email
            Column {
                BasicText(
                    text = "E-mail",
                    style = TextStyle(color = Color(0xFF4B5563), fontSize = 13.sp, fontWeight = FontWeight.Medium)
                )
                Spacer(modifier = Modifier.height(6.dp))
                BasicTextField(
                    value = email,
                    onValueChange = {
                        email = it
                        if (it.isNotBlank()) erroMensagem = "" // Limpa o erro ao começar a digitar
                    },
                    textStyle = TextStyle(color = Color.Black, fontSize = 14.sp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp)
                        .background(Color(0xFFF9FAFB), RoundedCornerShape(8.dp))
                        .border(1.dp, Color(0xFFD1D5DB), RoundedCornerShape(8.dp))
                        .padding(horizontal = 12.dp, vertical = 12.dp),
                    decorationBox = { innerTextField ->
                        if (email.isEmpty()) {
                            BasicText(
                                text = "seu.email@provedor.com",
                                style = TextStyle(color = Color(0xFF9CA3AF), fontSize = 14.sp)
                            )
                        }
                        innerTextField()
                    }
                )
            }

            // Campo Senha
            Column {
                BasicText(
                    text = "Senha",
                    style = TextStyle(color = Color(0xFF4B5563), fontSize = 13.sp, fontWeight = FontWeight.Medium)
                )
                Spacer(modifier = Modifier.height(6.dp))
                BasicTextField(
                    value = senha,
                    onValueChange = {
                        senha = it
                        if (it.isNotBlank()) erroMensagem = ""
                    },
                    textStyle = TextStyle(color = Color.Black, fontSize = 14.sp),
                    visualTransformation = if (senhaVisivel) VisualTransformation.None else PasswordVisualTransformation(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp)
                        .background(Color(0xFFF9FAFB), RoundedCornerShape(8.dp))
                        .border(1.dp, Color(0xFFD1D5DB), RoundedCornerShape(8.dp))
                        .padding(horizontal = 12.dp, vertical = 12.dp),
                    decorationBox = { innerTextField ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(modifier = Modifier.weight(1f)) {
                                if (senha.isEmpty()) {
                                    BasicText(
                                        text = "••••••••",
                                        style = TextStyle(color = Color(0xFF9CA3AF), fontSize = 14.sp)
                                    )
                                }
                                innerTextField()
                            }
                            BasicText(
                                text = if (senhaVisivel) "👁️" else "🙈",
                                style = TextStyle(fontSize = 16.sp),
                                modifier = Modifier.clickable { senhaVisivel = !senhaVisivel }
                            )
                        }
                    }
                )
            }

            // Mensagem de Erro Visual (Se houver)
            if (erroMensagem.isNotEmpty()) {
                BasicText(
                    text = erroMensagem,
                    style = TextStyle(color = Color.Red, fontSize = 13.sp, fontWeight = FontWeight.Medium)
                )
            } else {
                Spacer(modifier = Modifier.height(4.dp))
            }

            // Botão de Entrar com validação real e integração com ViewModel
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(46.dp)
                    .background(Color(0xFF43624E), RoundedCornerShape(8.dp))
                    .clickable {
                        // Validação local de campos em branco
                        if (email.isBlank() || senha.isBlank()) {
                            erroMensagem = "Por favor, preencha todos os campos."
                        } else if (!email.contains("@")) {
                            erroMensagem = "Insira um formato de e-mail válido."
                        } else {
                            // Envia as credenciais reais para a MainActivity buscar na ViewModel/DataStore
                            onLoginSucesso(email.trim(), senha.trim()) { textoDoErro ->
                                // Este bloco roda caso o cadastro não coincida ou não exista
                                erroMensagem = textoDoErro
                            }
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                BasicText(
                    text = "Entrar",
                    style = TextStyle(color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Link para Criar Conta
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            BasicText(
                text = "Não tem uma conta? ",
                style = TextStyle(color = Color(0xFF6B7280), fontSize = 14.sp)
            )
            BasicText(
                text = "Cadastre-se",
                style = TextStyle(color = Color(0xFF43624E), fontSize = 14.sp, fontWeight = FontWeight.Bold),
                modifier = Modifier.clickable { onNavegarParaRegistro() }
            )
        }
        Text(
            text = quote,
            modifier = Modifier.padding(16.dp),
            style = TextStyle(fontSize = 14.sp, fontStyle = FontStyle.Italic, color = Color.Gray)
        )
    }
}

@Preview(showBackground = true, device = "id:pixel_5")
@Composable
fun LoginPreview() {
    LoginScreen()
}