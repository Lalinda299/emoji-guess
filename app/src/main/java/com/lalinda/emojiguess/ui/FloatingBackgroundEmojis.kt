package com.lalinda.emojiguess.ui

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.random.Random

data class FloatingEmojiData(
    val emoji: String,
    val initialXRatio: Float,
    val initialYRatio: Float,
    val durationMs: Int,
    val fontSize: Int,
    val alpha: Float
)

@Composable
fun FloatingBackgroundEmojis() {
    val floatingEmojis = remember {
        val emojiList = listOf("🤔", "🧩", "🎯", "👑", "🔥", "✨", "🎬", "🍿", "🍕", "🚀", "💡", "🎮")
        List(14) {
            FloatingEmojiData(
                emoji = emojiList.random(),
                initialXRatio = Random.nextFloat(),
                initialYRatio = Random.nextFloat(),
                durationMs = Random.nextInt(4000, 9000),
                fontSize = Random.nextInt(18, 34),
                alpha = Random.nextFloat() * 0.15f + 0.05f
            )
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        floatingEmojis.forEachIndexed { index, data ->
            val transition = rememberInfiniteTransition(label = "Float_$index")
            val offsetY = transition.animateFloat(
                initialValue = 0f,
                targetValue = -120f,
                animationSpec = infiniteRepeatable(
                    animation = tween(data.durationMs, easing = LinearEasing),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "offsetY_$index"
            )

            val offsetX = transition.animateFloat(
                initialValue = -15f,
                targetValue = 15f,
                animationSpec = infiniteRepeatable(
                    animation = tween((data.durationMs * 0.7f).toInt(), easing = LinearEasing),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "offsetX_$index"
            )

            Text(
                text = data.emoji,
                fontSize = data.fontSize.sp,
                modifier = Modifier
                    .offset(
                        x = (data.initialXRatio * 320).dp + offsetX.value.dp,
                        y = (data.initialYRatio * 600).dp + offsetY.value.dp
                    )
                    .alpha(data.alpha)
            )
        }
    }
}
