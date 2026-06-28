package com.example.trabalho_livro_livre.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun RegistroScreen(
    modifier: Modifier = Modifier,
    // ALTERADO: Agora envia os 4 parâmetros reais para a MainActivity persistir no DataStore
    onRegistroSucesso: (String, String, String, String) -> Unit = { _, _, _, _ -> },
    onVoltarParaLogin: () -> Unit = {}
) {
    var nome by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var whatsapp by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }

    // Estado para controlar mensagens de erro de validação
    var erroMensagem by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFFAFAFA))
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        BasicText(
            text = "Criar Conta",
            style = TextStyle(color = Color(0xFF0F2C3D), fontSize = 28.sp, fontWeight = FontWeight.Bold)
        )

        Spacer(modifier = Modifier.height(6.dp))

        BasicText(
            text = "Entre para a maior comunidade de leitores de Quixadá",
            style = TextStyle(color = Color(0xFF6B7280), fontSize = 14.sp, textAlign = TextAlign.Center)
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Card do Formulário de Cadastro
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, RoundedCornerShape(16.dp))
                .border(1.dp, Color(0xFFE5E7EB), RoundedCornerShape(16.dp))
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Campo: Nome Completo
            Column {
                BasicText(
                    text = "Nome Completo",
                    style = TextStyle(color = Color(0xFF4B5563), fontSize = 13.sp, fontWeight = FontWeight.Medium)
                )
                Spacer(modifier = Modifier.height(6.dp))
                InputRegistro(
                    valor = nome,
                    onValorAlterado = {
                        nome = it
                        if (it.isNotBlank()) erroMensagem = ""
                    },
                    dica = "Ex: João Silva"
                )
            }

            // Campo: E-mail
            Column {
                BasicText(
                    text = "E-mail",
                    style = TextStyle(color = Color(0xFF4B5563), fontSize = 13.sp, fontWeight = FontWeight.Medium)
                )
                Spacer(modifier = Modifier.height(6.dp))
                InputRegistro(
                    valor = email,
                    onValorAlterado = {
                        email = it
                        if (it.isNotBlank()) erroMensagem = ""
                    },
                    dica = "seu.email@provedor.com"
                )
            }

            // Campo: WhatsApp
            Column {
                BasicText(
                    text = "WhatsApp (com DDD)",
                    style = TextStyle(color = Color(0xFF4B5563), fontSize = 13.sp, fontWeight = FontWeight.Medium)
                )
                Spacer(modifier = Modifier.height(6.dp))
                InputRegistro(
                    valor = whatsapp,
                    onValorAlterado = {
                        whatsapp = it
                        if (it.isNotBlank()) erroMensagem = ""
                    },
                    dica = "Ex: 88999999999"
                )
            }

            // Campo: Senha
            Column {
                BasicText(
                    text = "Criar Senha",
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
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp)
                        .background(Color(0xFFF9FAFB), RoundedCornerShape(8.dp))
                        .border(1.dp, Color(0xFFD1D5DB), RoundedCornerShape(8.dp))
                        .padding(horizontal = 12.dp, vertical = 12.dp),
                    decorationBox = { innerTextField ->
                        if (senha.isEmpty()) {
                            BasicText(
                                text = "Mínimo 6 caracteres",
                                style = TextStyle(color = Color(0xFF9CA3AF), fontSize = 14.sp)
                            )
                        }
                        innerTextField()
                    }
                )
            }

            // Exibição da mensagem de erro de validação
            if (erroMensagem.isNotEmpty()) {
                BasicText(
                    text = erroMensagem,
                    style = TextStyle(color = Color.Red, fontSize = 13.sp, fontWeight = FontWeight.Medium)
                )
            } else {
                Spacer(modifier = Modifier.height(4.dp))
            }

            // Botão de Cadastrar com a nova lógica aplicada
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(46.dp)
                    .background(Color(0xFF1C354E), RoundedCornerShape(8.dp))
                    .clickable {
                        // LÓGICA DE VALIDAÇÃO LOCAL:
                        if (nome.isBlank() || email.isBlank() || whatsapp.isBlank() || senha.isBlank()) {
                            erroMensagem = "Por favor, preencha todos os campos."
                        } else if (!email.contains("@")) {
                            erroMensagem = "Insira um formato de e-mail válido."
                        } else if (senha.length < 6) {
                            erroMensagem = "A senha deve conter no mínimo 6 caracteres."
                        } else {
                            // ALTERADO: Envia todos os dados informados limpos para a MainActivity
                            onRegistroSucesso(nome.trim(), email.trim(), whatsapp.trim(), senha.trim())
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                BasicText(
                    text = "Cadastrar",
                    style = TextStyle(color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Link para Voltar
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            BasicText(
                text = "Já possui uma conta? ",
                style = TextStyle(color = Color(0xFF6B7280), fontSize = 14.sp)
            )
            BasicText(
                text = "Faça Login",
                style = TextStyle(color = Color(0xFF1C354E), fontSize = 14.sp, fontWeight = FontWeight.Bold),
                modifier = Modifier.clickable { onVoltarParaLogin() }
            )
        }
    }
}

@Composable
fun InputRegistro(valor: String, onValorAlterado: (String) -> Unit, dica: String) {
    BasicTextField(
        value = valor,
        onValueChange = onValorAlterado,
        textStyle = TextStyle(color = Color.Black, fontSize = 14.sp),
        modifier = Modifier
            .fillMaxWidth()
            .height(44.dp)
            .background(Color(0xFFF9FAFB), RoundedCornerShape(8.dp))
            .border(1.dp, Color(0xFFD1D5DB), RoundedCornerShape(8.dp))
            .padding(horizontal = 12.dp, vertical = 12.dp),
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

@Preview(showBackground = true, device = "id:pixel_5")
@Composable
fun RegistroPreview() {
    RegistroScreen()
}