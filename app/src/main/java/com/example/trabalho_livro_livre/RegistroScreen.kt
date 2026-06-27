package com.example.trabalho_livro_livre

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
    onRegistroSucesso: () -> Unit = {},
    onVoltarParaLogin: () -> Unit = {}
) {
    var nome by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var whatsapp by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }

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
                InputRegistro(valor = nome, onValorAlterado = { nome = it }, dica = "Ex: João Silva")
            }

            // Campo: E-mail
            Column {
                BasicText(
                    text = "E-mail",
                    style = TextStyle(color = Color(0xFF4B5563), fontSize = 13.sp, fontWeight = FontWeight.Medium)
                )
                Spacer(modifier = Modifier.height(6.dp))
                InputRegistro(valor = email, onValorAlterado = { email = it }, dica = "seu.email@provedor.com")
            }

            // Campo: WhatsApp (Crucial para a funcionalidade de contato direto que adicionamos!)
            Column {
                BasicText(
                    text = "WhatsApp (com DDD)",
                    style = TextStyle(color = Color(0xFF4B5563), fontSize = 13.sp, fontWeight = FontWeight.Medium)
                )
                Spacer(modifier = Modifier.height(6.dp))
                InputRegistro(valor = whatsapp, onValorAlterado = { whatsapp = it }, dica = "Ex: 85999999999")
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
                    onValueChange = { senha = it },
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

            Spacer(modifier = Modifier.height(8.dp))

            // Botão de Cadastrar (Azul Escuro para diferenciar do login)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(46.dp)
                    .background(Color(0xFF1C354E), RoundedCornerShape(8.dp))
                    .clickable { onRegistroSucesso() },
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