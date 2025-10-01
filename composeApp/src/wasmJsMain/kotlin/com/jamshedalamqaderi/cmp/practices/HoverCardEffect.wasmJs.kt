package com.jamshedalamqaderi.cmp.practices

import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.AwaitPointerEventScope
import androidx.compose.ui.input.pointer.PointerEvent
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent

@OptIn(ExperimentalComposeUiApi::class)
@Composable
actual fun Modifier.onPointerEvent(
    eventType: PointerEventType,
    onEvent: AwaitPointerEventScope.(event: PointerEvent) -> Unit
): Modifier = onPointerEvent(eventType, PointerEventPass.Main, onEvent)