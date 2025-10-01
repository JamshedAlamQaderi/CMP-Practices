package com.jamshedalamqaderi.cmp.practices

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.VisibilityThreshold
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Code
import androidx.compose.material3.Icon
import androidx.compose.material3.InputChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.AwaitPointerEventScope
import androidx.compose.ui.input.pointer.PointerEvent
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.round
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

val purpleColor = Color(0xff6b57ff)
val greenColor = Color(0xB321D789)

const val INNER_CIRCLE_SIZE = 180
const val VIEW_WIDTH = 400
const val VIEW_HEIGHT = 200

@Composable
fun HoverCardEffect() {
    val scope = rememberCoroutineScope()
    var hovered by remember { mutableStateOf(false) }
    var mousePosition by remember { mutableStateOf(IntOffset.Zero) }
    val circleOffset = remember {
        Animatable(
            IntOffset.Zero,
            IntOffset.VectorConverter,
            IntOffset.VisibilityThreshold
        )
    }
    val circleCornerRoundPercent = remember { Animatable(50f) }
    val circleSize = remember { Animatable(IntSize.Zero, IntSize.VectorConverter, IntSize.VisibilityThreshold) }
    val backgroundShadowOffset by animateDpAsState(if (hovered) 20.dp else 0.dp)
    var mousePressed by remember { mutableStateOf(false) }
    var mouseReleased by remember { mutableStateOf(false) }

    Box(Modifier.wrapContentSize()) {
        if (backgroundShadowOffset.value > 0f) {
            Box(
                Modifier
                    .offset(backgroundShadowOffset, backgroundShadowOffset)
                    .width(VIEW_WIDTH.dp)
                    .height(VIEW_HEIGHT.dp)
                    .background(purpleColor, MaterialTheme.shapes.medium)
            )
        }
        Box(
            Modifier
                .width(VIEW_WIDTH.dp)
                .height(VIEW_HEIGHT.dp)
                .background(MaterialTheme.colorScheme.surface, MaterialTheme.shapes.medium)
                .clip(MaterialTheme.shapes.medium)
                .onPointerEvent(PointerEventType.Press) {
                    mousePressed = true && hovered
                }
                .onPointerEvent(PointerEventType.Release) {
                    mousePressed = false
                    mouseReleased = true && hovered
                }
                .onPointerEvent(PointerEventType.Move) {
                    mousePosition = it.changes.first().position.round()
                }
                .onPointerEvent(PointerEventType.Enter) {
                    scope.launch {
                        hovered = true
                        circleSize.animateTo(IntSize(INNER_CIRCLE_SIZE, INNER_CIRCLE_SIZE))
                    }
                }
                .onPointerEvent(PointerEventType.Exit) {
                    scope.launch {
                        circleSize.animateTo(IntSize.Zero)
                        hovered = false
                    }
                }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Code, contentDescription = null)
                    InputChip(
                        selected = true,
                        onClick = {},
                        label = {
                            Text("Kotlin")
                        }
                    )
                }
                Text("Kotlin Onboarding", style = MaterialTheme.typography.headlineSmall)
                Spacer(Modifier.height(4.dp))
                Text("~4 Hours", style = MaterialTheme.typography.bodyMedium.copy(color = Color.Gray))
                Spacer(Modifier.height(16.dp))
                Text(
                    "Learn Kotlin by working on six projects, including a basic version of Photoshop.",
                    style = MaterialTheme.typography.bodyMedium.copy(color = Color.DarkGray)
                )
            }
            if (hovered) {
                LaunchedEffect(mousePressed, mouseReleased) {
                    if (mousePressed) {
                        listOf(
                            async {
                                circleOffset.animateTo(IntOffset.Zero)
                            },
                            async {
                                circleSize.animateTo(IntSize(VIEW_WIDTH, VIEW_HEIGHT))
                            },
                            async { circleCornerRoundPercent.animateTo(0f) }
                        ).awaitAll()
                        return@LaunchedEffect
                    }

                    if (mouseReleased) {
                        scope.launch {
                            listOf(
                                async { circleSize.animateTo(IntSize(INNER_CIRCLE_SIZE, INNER_CIRCLE_SIZE)) },
                                async { circleCornerRoundPercent.animateTo(50f) }
                            ).awaitAll()
                        }
                    }
                    snapshotFlow {
                        mousePosition to circleSize.value
                    }.collectLatest {
                        circleOffset.animateTo(
                            IntOffset(
                                x = (it.first.x - it.second.width / 2),
                                y = (it.first.y - it.second.height / 2),
                            ),
                            tween(0)
                        )
                    }
                }
                Box(
                    Modifier
                        .offset { circleOffset.value }
                        .size(circleSize.value.width.dp, circleSize.value.height.dp)
                        .background(
                            greenColor,
                            shape = RoundedCornerShape(percent = circleCornerRoundPercent.value.toInt().coerceIn(0, 50))
                        ),
                )
            }
        }
    }
}

@Composable
fun Modifier.onPointerEvent(
    eventType: PointerEventType,
    onEvent: AwaitPointerEventScope.(event: PointerEvent) -> Unit
): Modifier {
    return this.pointerInput(Unit) {
        awaitPointerEventScope {
            while (true) {
                val event = awaitPointerEvent()
                if (event.type == eventType) onEvent(event)
            }
        }
    }
}