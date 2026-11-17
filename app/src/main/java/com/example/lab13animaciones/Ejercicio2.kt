package com.example.lab13animaciones

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
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
fun Ejercicio2() {
    // Estado que indica si el cuadro es azul o verde
    var esAzul by remember { mutableStateOf(true) }

    // Color animado con tween (duración fija de 1.5 s)
    val colorTween by animateColorAsState(
        targetValue = if (esAzul) Color.Blue else Color.Green,
        animationSpec = tween(durationMillis = 1500),
        label = "colorTween"
    )

    // Color animado con spring (efecto resorte)
    val colorSpring by animateColorAsState(
        targetValue = if (esAzul) Color.Blue else Color.Green,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessVeryLow
        ),
        label = "colorSpring"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(onClick = { esAzul = !esAzul }) {
            Text(if (esAzul) "Cambiar a verde" else "Cambiar a azul")
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text("Animación con tween (1500 ms)")
        Spacer(modifier = Modifier.height(8.dp))
        Box(
            modifier = Modifier
                .size(150.dp)
                .background(colorTween)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text("Animación con spring")
        Spacer(modifier = Modifier.height(8.dp))
        Box(
            modifier = Modifier
                .size(150.dp)
                .background(colorSpring)
        )
    }
}