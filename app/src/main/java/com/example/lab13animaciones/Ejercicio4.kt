package com.example.lab13animaciones

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// 1. Los tres estados de la pantalla
enum class EstadoPantalla { Cargando, Contenido, Error }

@Composable
fun Ejercicio4() {
    var estado by remember { mutableStateOf(EstadoPantalla.Cargando) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Estado actual: ${estado.name}", fontSize = 16.sp)

        Spacer(modifier = Modifier.height(12.dp))

        // Botón que pasa al siguiente estado
        Button(onClick = {
            estado = when (estado) {
                EstadoPantalla.Cargando -> EstadoPantalla.Contenido
                EstadoPantalla.Contenido -> EstadoPantalla.Error
                EstadoPantalla.Error -> EstadoPantalla.Cargando
            }
        }) {
            Text("Siguiente estado")
        }

        Spacer(modifier = Modifier.height(32.dp))

        // 2 y 3. AnimatedContent con fadeIn (800 ms, retraso 200 ms) y fadeOut (400 ms)
        AnimatedContent(
            targetState = estado,
            transitionSpec = {
                fadeIn(animationSpec = tween(durationMillis = 800, delayMillis = 200)) togetherWith
                        fadeOut(animationSpec = tween(durationMillis = 400))
            },
            label = "estadoPantalla"
        ) { estadoActual ->
            when (estadoActual) {
                EstadoPantalla.Cargando -> {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator()
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Cargando datos...", fontSize = 18.sp)
                    }
                }

                EstadoPantalla.Contenido -> {
                    Box(
                        modifier = Modifier
                            .size(width = 260.dp, height = 150.dp)
                            .background(Color(0xFF4CAF50), RoundedCornerShape(16.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "¡Datos cargados correctamente!",
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                EstadoPantalla.Error -> {
                    Box(
                        modifier = Modifier
                            .size(width = 260.dp, height = 150.dp)
                            .background(Color(0xFFF44336), RoundedCornerShape(16.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "⚠️ Ocurrió un error al cargar",
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}