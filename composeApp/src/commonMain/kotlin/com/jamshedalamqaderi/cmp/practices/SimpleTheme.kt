package com.jamshedalamqaderi.cmp.practices

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp


@Composable
fun SimpleTheme() {
    Card(
        modifier = Modifier.width(300.dp),
        shape = MaterialTheme.shapes.small,
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent,
        )
    ) {
        Column(
            Modifier.background(
                Brush.verticalGradient(
                    listOf(
                        MaterialTheme.colorScheme.tertiaryContainer,
                        MaterialTheme.colorScheme.tertiaryContainer.copy(.7f),
                    )
                ),
            ).padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                "Simple Headline",
                style = MaterialTheme.typography.titleLarge.copy(color = MaterialTheme.colorScheme.onTertiaryContainer)
            )
            Text(
                "Testing simple theme color combination using jetpack compose",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onTertiaryContainer.copy(
                        .7f
                    )
                )
            )
            Spacer(Modifier.height(8.dp))
            Button(
                modifier = Modifier.enableClickAnimation(),
                shape = MaterialTheme.shapes.small,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.tertiary,
                    contentColor = MaterialTheme.colorScheme.onTertiary
                ),
                onClick = {}
            ) {
                Text("Click Me")
            }
        }
    }
}

@Composable
fun Modifier.enableClickAnimation(): Modifier {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        label = "scale"
    )
    return this.scale(scale)
        .pointerInput(Unit) {
            awaitEachGesture {
                val down = awaitFirstDown(requireUnconsumed = false)
                isPressed = true
                down.consume()
                waitForUpOrCancellation()
                isPressed = false
            }
        }
}