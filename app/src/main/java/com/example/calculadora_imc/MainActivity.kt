package com.example.calculadora_imc

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import java.util.Locale


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            MaterialTheme {

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    AppNavegacion()

                }
            }
        }
    }
}

@Composable
fun PantallaIngreso(
    onNavegarAResultado: (String, Double) -> Unit
) {

    var nombre by remember { mutableStateOf("") }
    var peso by remember { mutableStateOf("") }
    var altura by remember { mutableStateOf("") }

    var error by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),

        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = "Calculadora IMC",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(24.dp))

        TextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre") }
        )

        Spacer(modifier = Modifier.height(12.dp))

        TextField(
            value = peso,
            onValueChange = { peso = it },
            label = { Text("Peso") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        TextField(
            value = altura,
            onValueChange = { altura = it },
            label = { Text("Altura") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (error) {

            Text(
                text = "Por favor, ingresa valores válidos",
                color = Color.Red
            )

            Spacer(modifier = Modifier.height(16.dp))
        }

        Button(
            onClick = {

                val pesoDouble = peso.toDoubleOrNull()
                val alturaDouble = altura.toDoubleOrNull()

                if (
                    pesoDouble == null ||
                    alturaDouble == null ||
                    pesoDouble <= 0 ||
                    alturaDouble <= 0
                ) {

                    error = true

                } else {

                    error = false

                    val imc =
                        pesoDouble / (alturaDouble * alturaDouble)

                    onNavegarAResultado(
                        nombre,
                        imc
                    )
                }
            }
        ) {

            Text("Calcular")
        }
    }
}

@Composable
fun PantallaResultado(
    nombre: String,
    imc: Double,
    onVolver: () -> Unit
) {

    val categoria = when {

        imc < 18.5 -> "Bajo peso"

        imc < 25.0 -> "Peso normal"

        imc < 30.0 -> "Sobrepeso"

        else -> "Obesidad"
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),

        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = "Resultado",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Hola $nombre, tu resultado es:"
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "IMC: %.1f".format(imc)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = categoria
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = onVolver
        ) {

            Text("Volver")
        }
    }
}

@Composable
fun AppNavegacion() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "ingreso"
    ) {

        composable("ingreso") {

            PantallaIngreso { nombre, imc ->

                navController.navigate(
                    "resultado/$nombre/$imc"
                )
            }
        }

        composable("resultado/{nombre}/{imc}") { backStackEntry ->

            val nombre =
                backStackEntry.arguments?.getString("nombre") ?: ""

            val imc =
                backStackEntry.arguments
                    ?.getString("imc")
                    ?.toDoubleOrNull() ?: 0.0

            PantallaResultado(
                nombre = nombre,
                imc = imc,
                onVolver = {
                    navController.popBackStack()
                }
            )
        }
    }
}