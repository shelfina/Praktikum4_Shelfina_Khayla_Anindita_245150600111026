package com.example.pemesanantiket

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

enum class StatusPesanan {
    IDLE,
    LOADING,
    SUCCESS,
    ERROR
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                TiketScreenParent()
            }
        }
    }
}

@Composable
fun TiketScreenParent() {
    var namaPembeli by remember { mutableStateOf("") }
    var jumlahTiket by remember { mutableIntStateOf(1) }
    var statusPesanan by remember { mutableStateOf(StatusPesanan.IDLE) }
    var isProcessingTrigger by remember { mutableStateOf(false) }

    LaunchedEffect(isProcessingTrigger) {
        if (isProcessingTrigger) {
            if (namaPembeli.trim().isEmpty()) {
                statusPesanan = StatusPesanan.ERROR
                isProcessingTrigger = false
            } else {
                statusPesanan = StatusPesanan.LOADING
                delay(2000L)
                statusPesanan = StatusPesanan.SUCCESS
                isProcessingTrigger = false
            }
        }
    }

    TiketScreenContent(
        namaPembeli = namaPembeli,
        onNamaChange = { namaPembeli = it },
        jumlahTiket = jumlahTiket,
        onTambahTiket = { jumlahTiket++ },
        onKurangTiket = { if (jumlahTiket > 1) jumlahTiket-- },
        statusPesanan = statusPesanan,
        onPesanClick = { isProcessingTrigger = true }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TiketScreenContent(
    namaPembeli: String,
    onNamaChange: (String) -> Unit,
    jumlahTiket: Int,
    onTambahTiket: () -> Unit,
    onKurangTiket: () -> Unit,
    statusPesanan: StatusPesanan,
    onPesanClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Pemesanan Tiket",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF185ABC)
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(innerPadding)
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Nama",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = Color.Black
            )
            OutlinedTextField(
                value = namaPembeli,
                onValueChange = onNamaChange,
                placeholder = { Text("Masukkan nama Anda", color = Color.Gray) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                singleLine = true,
                enabled = statusPesanan != StatusPesanan.LOADING,
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = Color(0xFFD0D0D0),
                    focusedBorderColor = Color(0xFFD0D0D0)
                )
            )

            Text(
                text = "Jumlah Tiket",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = Color.Black
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                val isMinusEnabled = statusPesanan != StatusPesanan.LOADING && jumlahTiket > 1
                OutlinedButton(
                    onClick = onKurangTiket,
                    enabled = isMinusEnabled,
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(1.dp, Color(0xFFD0E1F9)),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = Color(0xFFF0F4FC),
                        contentColor = Color.Black,
                        disabledContainerColor = Color(0xFFF0F4FC),
                        disabledContentColor = Color.Black
                    ),
                    contentPadding = PaddingValues(0.dp),
                    modifier = Modifier
                        .width(80.dp)
                        .height(48.dp)
                ) {
                    Text("-", fontSize = 22.sp, fontWeight = FontWeight.Bold)
                }

                Text(
                    text = "$jumlahTiket",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                val isPlusEnabled = statusPesanan != StatusPesanan.LOADING
                OutlinedButton(
                    onClick = onTambahTiket,
                    enabled = isPlusEnabled,
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(1.dp, Color(0xFFD0E1F9)),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = Color(0xFFF0F4FC),
                        contentColor = Color.Black,
                        disabledContainerColor = Color(0xFFF0F4FC),
                        disabledContentColor = Color.Black
                    ),
                    contentPadding = PaddingValues(0.dp),
                    modifier = Modifier
                        .width(80.dp)
                        .height(48.dp)
                ) {
                    Text("+", fontSize = 22.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            Button(
                onClick = onPesanClick,
                enabled = statusPesanan != StatusPesanan.LOADING,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF185ABC),
                    disabledContainerColor = Color(0xFFB0BEC5),
                    disabledContentColor = Color.White
                )
            ) {
                Text(
                    text = "Pesan Tiket",
                    fontSize = 15.sp,
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            StatusCard(statusPesanan = statusPesanan)
        }
    }
}

@Composable
fun StatusCard(statusPesanan: StatusPesanan) {
    val (backgroundColor, textColor, iconColor, borderColor) = when (statusPesanan) {
        StatusPesanan.IDLE -> Quadruple(Color(0xFFF0F4FC), Color(0xFF5F6368), Color(0xFF5F6368), Color(0xFFD0E1F9))
        StatusPesanan.LOADING -> Quadruple(Color(0xFFE8F0FE), Color(0xFF185ABC), Color(0xFF185ABC), Color(0xFFD0E1F9))
        StatusPesanan.SUCCESS -> Quadruple(Color(0xFFE6F4EA), Color(0xFF137333), Color(0xFF34A853), Color(0xFFCEEAD6))
        StatusPesanan.ERROR -> Quadruple(Color(0xFFFCE8E6), Color(0xFFC5221F), Color(0xFFEA4335), Color(0xFFFAD2CF))
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, borderColor),
        colors = CardDefaults.cardColors(containerColor = backgroundColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            when (statusPesanan) {
                StatusPesanan.IDLE -> {
                    Text(
                        text = buildAnnotatedString {
                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                append("Status: ")
                            }
                            append("Silakan pesan tiket")
                        },
                        color = textColor,
                        fontSize = 14.sp
                    )
                }
                StatusPesanan.LOADING -> {
                    CircularProgressIndicator(
                        modifier = Modifier.size(18.dp),
                        color = iconColor,
                        strokeWidth = 2.dp
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = buildAnnotatedString {
                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                append("Status: ")
                            }
                            append("Memproses pesanan...")
                        },
                        color = textColor,
                        fontSize = 14.sp
                    )
                }
                StatusPesanan.SUCCESS -> {
                    Box(
                        modifier = Modifier
                            .size(20.dp)
                            .background(iconColor, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "✓",
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = buildAnnotatedString {
                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                append("Status: ")
                            }
                            append("Tiket berhasil dipesan!")
                        },
                        color = textColor,
                        fontSize = 14.sp
                    )
                }
                StatusPesanan.ERROR -> {
                    Box(
                        modifier = Modifier
                            .size(20.dp)
                            .background(iconColor, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "!",
                            color = Color.White,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = buildAnnotatedString {
                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                append("Status: ")
                            }
                            append("Nama harus diisi")
                        },
                        color = textColor,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}

data class Quadruple<A, B, C, D>(
    val first: A,
    val second: B,
    val third: C,
    val fourth: D
)

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun TiketScreenPreview() {
    MaterialTheme {
        TiketScreenParent()
    }
}