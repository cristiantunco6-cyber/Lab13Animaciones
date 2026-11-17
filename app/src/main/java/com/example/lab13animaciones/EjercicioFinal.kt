package com.example.lab13animaciones

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.sin
import kotlin.random.Random

// Colores del juego
private val Dorado = Color(0xFFFFC857)
private val VerdeNeon = Color(0xFF69F0AE)
private val RojoNeon = Color(0xFFFF5252)
private val AzulNoche = Color(0xFF1A1A40)

private const val DURACION_PARTIDA = 20

enum class FaseJuego { Inicio, Jugando, Fin }

@Composable
fun EjercicioFinal() {
    var fase by remember { mutableStateOf(FaseJuego.Inicio) }
    var puntaje by remember { mutableIntStateOf(0) }
    var record by remember { mutableIntStateOf(0) }
    var nuevoRecord by remember { mutableStateOf(false) }

    // animateColorAsState: el fondo cambia de color según la pantalla
    val colorArriba by animateColorAsState(
        targetValue = when (fase) {
            FaseJuego.Inicio -> AzulNoche
            FaseJuego.Jugando -> Color(0xFF0F2027)
            FaseJuego.Fin -> Color(0xFF3A1C71)
        },
        animationSpec = tween(1000),
        label = "colorArriba"
    )
    val colorAbajo by animateColorAsState(
        targetValue = when (fase) {
            FaseJuego.Inicio -> Color(0xFF7A0BC0)
            FaseJuego.Jugando -> Color(0xFF2C5364)
            FaseJuego.Fin -> Color(0xFFD76D77)
        },
        animationSpec = tween(1000),
        label = "colorAbajo"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(colorArriba, colorAbajo)))
    ) {
        CieloEstrellado()

        // AnimatedContent: transición entre pantallas del juego
        AnimatedContent(
            targetState = fase,
            transitionSpec = {
                (fadeIn(tween(600)) + scaleIn(initialScale = 0.85f, animationSpec = tween(600))) togetherWith
                        (fadeOut(tween(400)) + scaleOut(targetScale = 1.1f, animationSpec = tween(400)))
            },
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding(),
            label = "faseJuego"
        ) { faseActual ->
            when (faseActual) {
                FaseJuego.Inicio -> PantallaInicio(
                    record = record,
                    onJugar = {
                        puntaje = 0
                        fase = FaseJuego.Jugando
                    }
                )

                FaseJuego.Jugando -> PantallaJuego(
                    puntaje = puntaje,
                    onPuntaje = { puntaje++ },
                    onFin = {
                        nuevoRecord = puntaje > record
                        if (nuevoRecord) record = puntaje
                        fase = FaseJuego.Fin
                    }
                )

                FaseJuego.Fin -> PantallaFin(
                    puntaje = puntaje,
                    nuevoRecord = nuevoRecord,
                    onReiniciar = {
                        puntaje = 0
                        fase = FaseJuego.Jugando
                    },
                    onMenu = { fase = FaseJuego.Inicio }
                )
            }
        }
    }
}

// Fondo con estrellas que titilan (animación infinita)
@Composable
fun CieloEstrellado() {
    val estrellas = remember {
        List(70) { Triple(Random.nextFloat(), Random.nextFloat(), Random.nextFloat()) }
    }
    val transicion = rememberInfiniteTransition(label = "cielo")
    val tiempo by transicion.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(3000, easing = LinearEasing)),
        label = "tiempo"
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        estrellas.forEach { (x, y, desfase) ->
            val brillo = 0.15f + 0.85f * abs(sin((tiempo + desfase) * 2f * PI.toFloat()))
            drawCircle(
                color = Color.White.copy(alpha = brillo),
                radius = 1.5f + desfase * 3f,
                center = Offset(x * size.width, y * size.height)
            )
        }
    }
}

@Composable
fun PantallaInicio(record: Int, onJugar: () -> Unit) {
    val transicion = rememberInfiniteTransition(label = "inicio")
    val flotar by transicion.animateFloat(
        initialValue = -12f,
        targetValue = 12f,
        animationSpec = infiniteRepeatable(tween(1500, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "flotar"
    )
    val pulso by transicion.animateFloat(
        initialValue = 1f,
        targetValue = 1.08f,
        animationSpec = infiniteRepeatable(tween(900), RepeatMode.Reverse),
        label = "pulso"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("⭐", fontSize = 96.sp, modifier = Modifier.offset(y = flotar.dp))
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            "CAZADOR DE\nESTRELLAS",
            fontSize = 38.sp,
            fontWeight = FontWeight.Black,
            color = Color.White,
            textAlign = TextAlign.Center,
            lineHeight = 44.sp,
            letterSpacing = 2.sp
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            "Atrapa todas las estrellas que puedas en $DURACION_PARTIDA segundos",
            color = Color.White.copy(alpha = 0.75f),
            fontSize = 15.sp,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(48.dp))
        Button(
            onClick = onJugar,
            modifier = Modifier
                .scale(pulso)
                .width(220.dp)
                .height(58.dp),
            shape = RoundedCornerShape(29.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Dorado, contentColor = AzulNoche)
        ) {
            Text("JUGAR", fontSize = 20.sp, fontWeight = FontWeight.Bold, letterSpacing = 3.sp)
        }
        if (record > 0) {
            Spacer(modifier = Modifier.height(24.dp))
            Text("🏆 Récord: $record", color = Dorado, fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
fun PantallaJuego(puntaje: Int, onPuntaje: () -> Unit, onFin: () -> Unit) {
    var tiempoRestante by remember { mutableIntStateOf(DURACION_PARTIDA) }
    var objetivoX by remember { mutableFloatStateOf(0.5f) }
    var objetivoY by remember { mutableFloatStateOf(0.5f) }
    var golpes by remember { mutableIntStateOf(0) }
    var mostrarMasUno by remember { mutableStateOf(false) }
    val onFinActual by rememberUpdatedState(onFin)

    // Cuenta regresiva
    LaunchedEffect(Unit) {
        while (tiempoRestante > 0) {
            delay(1000)
            tiempoRestante--
        }
        onFinActual()
    }

    // Muestra el +1 por medio segundo cada vez que se atrapa una estrella
    LaunchedEffect(golpes) {
        if (golpes > 0) {
            mostrarMasUno = true
            delay(500)
            mostrarMasUno = false
        }
    }

    // animateColorAsState: la barra pasa de verde a rojo al final
    val colorTiempo by animateColorAsState(
        targetValue = if (tiempoRestante <= 5) RojoNeon else VerdeNeon,
        animationSpec = tween(500),
        label = "colorTiempo"
    )
    // animateFloatAsState: la barra se va vaciando
    val progreso by animateFloatAsState(
        targetValue = tiempoRestante / DURACION_PARTIDA.toFloat(),
        animationSpec = tween(1000, easing = LinearEasing),
        label = "progreso"
    )

    // Estrella que gira y late todo el tiempo
    val transicion = rememberInfiniteTransition(label = "estrella")
    val giro by transicion.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(tween(4000, easing = LinearEasing)),
        label = "giro"
    )
    val latido by transicion.animateFloat(
        initialValue = 1f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(tween(500), RepeatMode.Reverse),
        label = "latido"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Marcador
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text("PUNTAJE", color = Color.White.copy(alpha = 0.6f), fontSize = 12.sp, letterSpacing = 2.sp)
                // AnimatedContent: el número sube deslizándose
                AnimatedContent(
                    targetState = puntaje,
                    transitionSpec = {
                        (slideInVertically { it } + fadeIn()) togetherWith
                                (slideOutVertically { -it } + fadeOut())
                    },
                    label = "puntaje"
                ) { valor ->
                    Text("$valor", color = Color.White, fontSize = 36.sp, fontWeight = FontWeight.Bold)
                }
            }
            Column(horizontalAlignment = Alignment.End) {
                Text("TIEMPO", color = Color.White.copy(alpha = 0.6f), fontSize = 12.sp, letterSpacing = 2.sp)
                Text("${tiempoRestante}s", color = colorTiempo, fontSize = 36.sp, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Barra de tiempo
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(Color.White.copy(alpha = 0.15f))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(progreso)
                    .clip(RoundedCornerShape(4.dp))
                    .background(colorTiempo)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Zona de juego
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            val tamanoEstrella = 80.dp
            val limiteX = maxWidth - tamanoEstrella
            val limiteY = maxHeight - tamanoEstrella

            // animateDpAsState + offset: la estrella salta con efecto resorte
            val posX by animateDpAsState(
                targetValue = limiteX * objetivoX,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                ),
                label = "posX"
            )
            val posY by animateDpAsState(
                targetValue = limiteY * objetivoY,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                ),
                label = "posY"
            )

            Box(
                modifier = Modifier
                    .offset(x = posX, y = posY)
                    .size(tamanoEstrella)
                    .scale(latido)
                    .rotate(giro)
                    .clip(CircleShape)
                    .background(
                        Brush.radialGradient(
                            listOf(Color(0xAAFFF59D), Color(0x00FFC857))
                        )
                    )
                    .clickable {
                        objetivoX = Random.nextFloat()
                        objetivoY = Random.nextFloat()
                        golpes++
                        onPuntaje()
                    },
                contentAlignment = Alignment.Center
            ) {
                Text("⭐", fontSize = 46.sp)
            }

            // AnimatedVisibility: el +1 aparece y sube
            androidx.compose.animation.AnimatedVisibility(
                visible = mostrarMasUno,
                enter = slideInVertically { it } + fadeIn(),
                exit = slideOutVertically { -it } + fadeOut(),
                modifier = Modifier.offset(x = posX + 20.dp, y = posY - 50.dp)
            ) {
                Text("+1", color = Dorado, fontSize = 40.sp, fontWeight = FontWeight.Black)
            }
        }
    }
}

@Composable
fun PantallaFin(
    puntaje: Int,
    nuevoRecord: Boolean,
    onReiniciar: () -> Unit,
    onMenu: () -> Unit
) {
    var mostrarTarjeta by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        delay(300)
        mostrarTarjeta = true
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            "¡TIEMPO!",
            fontSize = 42.sp,
            fontWeight = FontWeight.Black,
            color = Color.White,
            letterSpacing = 4.sp
        )

        Spacer(modifier = Modifier.height(24.dp))

        // AnimatedVisibility: la tarjeta entra rebotando
        AnimatedVisibility(
            visible = mostrarTarjeta,
            enter = scaleIn(
                initialScale = 0.3f,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioHighBouncy,
                    stiffness = Spring.StiffnessLow
                )
            ) + fadeIn()
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .background(Color.White.copy(alpha = 0.12f), RoundedCornerShape(28.dp))
                    .padding(horizontal = 48.dp, vertical = 28.dp)
            ) {
                Text(if (nuevoRecord) "🏆" else "⭐", fontSize = 64.sp)
                Text("$puntaje", fontSize = 64.sp, fontWeight = FontWeight.Black, color = Dorado)
                Text("estrellas atrapadas", color = Color.White.copy(alpha = 0.8f), fontSize = 16.sp)
                if (nuevoRecord) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Text("¡NUEVO RÉCORD!", color = VerdeNeon, fontWeight = FontWeight.Bold, letterSpacing = 2.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(40.dp))

        Button(
            onClick = onReiniciar,
            modifier = Modifier
                .width(220.dp)
                .height(56.dp),
            shape = RoundedCornerShape(28.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Dorado, contentColor = AzulNoche)
        ) {
            Text("JUGAR DE NUEVO", fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedButton(
            onClick = onMenu,
            modifier = Modifier
                .width(220.dp)
                .height(56.dp),
            shape = RoundedCornerShape(28.dp),
            border = BorderStroke(1.dp, Color.White.copy(alpha = 0.6f)),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White)
        ) {
            Text("MENÚ", fontWeight = FontWeight.Bold, letterSpacing = 2.sp)
        }
    }
}