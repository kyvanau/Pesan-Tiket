package com.example.pesantiket

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Icon
import com.example.pesantiket.ui.theme.PesanTiketTheme
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            PesanTiketTheme {

                var hargaTiket by remember { mutableStateOf(50000) }
                var jumlahTiket by remember { mutableStateOf(1) }
                var namaPembeli by remember { mutableStateOf("") }

                var status by remember {
                    mutableStateOf("Silakan pesan tiket")
                }

                var sedangMemproses by remember {
                    mutableStateOf(false)
                }

                LaunchedEffect(sedangMemproses) {
                    if (sedangMemproses) {

                        status = "Memproses pesanan..."

                        delay(5000)

                        status = "Tiket berhasil dipesan!"
                        sedangMemproses = false
                    }
                }

                Scaffold(
                    topBar = {
                        AppBar()
                    }
                ) { innerPadding ->

                    HalamanPemesananTiket(
                        modifier = Modifier.padding(innerPadding),

                        hargaTiket = hargaTiket,
                        jumlahTiket = jumlahTiket,
                        namaPembeli = namaPembeli,
                        status = status,
                        sedangMemproses = sedangMemproses,

                        onNamaPembeliChange = {
                            namaPembeli = it
                        },

                        onTambahTiket = {
                            jumlahTiket++
                        },

                        onKurangTiket = {
                            if (jumlahTiket > 1) {
                                jumlahTiket--
                            }
                        },

                        onPesanTiket = {
                            if (namaPembeli.isBlank()) {
                                status = "Nama harus diisi"
                            } else {
                                sedangMemproses = true
                            }
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar() {
    TopAppBar(
        title = {
            Text(
                text = "Pemesanan Tiket",
                color = Color.White,
                fontSize = 20.sp
            )
        },
        colors = androidx.compose.material3.TopAppBarDefaults.topAppBarColors(
            containerColor = Color(0xFF1769D1)
        )
    )
}

@Composable
fun HalamanPemesananTiket(
    modifier: Modifier = Modifier,
    hargaTiket: Int,
    jumlahTiket: Int,
    namaPembeli: String,
    status: String,
    sedangMemproses: Boolean,
    onNamaPembeliChange: (String) -> Unit,
    onTambahTiket: () -> Unit,
    onKurangTiket: () -> Unit,
    onPesanTiket: () -> Unit
) {

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top
    ) {

        Text(
            text = "Nama",
            fontSize = 14.sp
        )

        Spacer(modifier = Modifier.height(6.dp))

        OutlinedTextField(
            value = namaPembeli,
            onValueChange = onNamaPembeliChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                Text("Masukkan nama Anda")
            },
            singleLine = true,
            enabled = !sedangMemproses
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Jumlah Tiket",
            fontSize = 14.sp
        )

        Spacer(modifier = Modifier.height(6.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Button(
                onClick = onKurangTiket,
                enabled = jumlahTiket > 1 && !sedangMemproses
            ) {
                Text("-")
            }

            Text(
                text = "$jumlahTiket",
                fontSize = 20.sp
            )

            Button(
                onClick = onTambahTiket,
                enabled = !sedangMemproses
            ) {
                Text("+")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onPesanTiket,
            modifier = Modifier.fillMaxWidth(),
            enabled = !sedangMemproses,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF1769D1)
            )
        ) {
            Text("Pesan Tiket")
        }

        Spacer(modifier = Modifier.height(20.dp))

        StatusPesanan(
            status = status
        )
    }
}

@Composable
fun StatusPesanan(
    status: String
) {

    val sedangProses = status == "Memproses pesanan..."
    val berhasil = status == "Tiket berhasil dipesan!"
    val error = status == "Nama harus diisi"

    val backgroundColor = when {
        berhasil -> Color(0xFFE5F6E9)
        error -> Color(0xFFFFE8E8)
        sedangProses -> Color(0xFFE8F3FF)
        else -> Color(0xFFF2F6FC)
    }

    val textColor = when {
        berhasil -> Color(0xFF247A3D)
        error -> Color(0xFFB3261E)
        sedangProses -> Color(0xFF1769D1)
        else -> Color(0xFF344054)
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor
        )
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            when {
                sedangProses -> {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .padding(end = 12.dp)
                            .height(20.dp),
                        strokeWidth = 2.dp
                    )
                }

                berhasil -> {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = Color(0xFF4CAF50),
                        modifier = Modifier.padding(end = 10.dp)
                    )
                }

                error -> {
                    Icon(
                        imageVector = Icons.Default.Error,
                        contentDescription = null,
                        tint = Color(0xFFE53935),
                        modifier = Modifier.padding(end = 10.dp)
                    )
                }
            }

            Text(
                text = "Status: $status",
                color = textColor,
                fontSize = 14.sp
            )
        }
    }
}