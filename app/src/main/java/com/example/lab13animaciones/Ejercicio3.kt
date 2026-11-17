package com.example.lab13animaciones

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun Ejercicio3() {
    // Estado que indica si el cuadro está movido y agrandado
    var expandido by remember { mutableStateOf(false) }

    // Tamaño animado
    val tamano by animateDpAsState(
        targetValue = if (expandido) 150.dp else 80.dp,
        animationSpec = tween(durationMillis = 800),
        label = "tamano"
    )

    // Posición animada en X y en Y
    val desplazamientoX by animateDpAsState(
        targetValue = if (expandido) 150.dp else 0.dp,
        animationSpec = tween(durationMillis = 800),
        label = "desplazamientoX"
    )
    val desplazamientoY by animateDpAsState(
        targetValue = if (expandido) 40.dp else 0.dp,
        animationSpec = tween(durationMillis = 800),
        label = "desplazamientoY"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Button(
            onClick = { expandido = !expandido },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(if (expandido) "Regresar" else "Mover y agrandar")
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Cuadro A: offset ANTES de size y background
        Text("Cuadro A: offset → size → background")
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
        ) {
            Box(
                modifier = Modifier
                    .offset(x = desplazamientoX, y = desplazamientoY)
                    .size(tamano)
                    .background(Color(0xFFE91E63)),
                contentAlignment = Alignment.Center
            ) {
                Text("A", color = Color.White)
            }
        }

        // Cuadro B: offset DESPUÉS de size y background
        Text("Cuadro B: size → background → offset")
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(tamano)
                    .background(Color(0xFF009688))
                    .offset(x = desplazamientoX, y = desplazamientoY),
                contentAlignment = Alignment.Center
            ) {
                Text("B", color = Color.White)
            }
        }
    }
}