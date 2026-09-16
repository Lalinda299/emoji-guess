package com.example.emojiguess.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.withTransform
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

data class Particle(
    val x: Float,
    val y: Float,
    val color: Color,
    val radius: Float,
    val angle: Double,
    val speed: Float,
    val rotation: Float
)

@Composable
fun ParticleBurst(
    trigger: Boolean,
    onAnimationEnd: () -> Unit = {}
) {
    if (!trigger) return

    val progress = remember { Animatable(0f) }
    val particles = remember {
        val colors = listOf(
            Color(0xFFFFD700), // Gold
            Color(0xFF10B981), // Emerald
            Color(0xFF06B6D4), // Cyan
            Color(0xFF8B5CF6), // Purple
            Color(0xFFEC4899), // Pink
            Color(0xFFF59E0B)  // Amber
        )
        List(36) {
            val angle = Random.nextDouble(0.0, 2.0 * Math.PI)
            val speed = Random.nextFloat() * 400f + 200f
            Particle(
                x = 0f,
                y = 0f,
                color = colors.random(),
                radius = Random.nextFloat() * 12f + 6f,
                angle = angle,
                speed = speed,
                rotation = Random.nextFloat() * 360f
            )
        }
    }

    LaunchedEffect(trigger) {
        progress.snapTo(0f)
        progress.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 800, easing = LinearEasing)
        )
        onAnimationEnd()
    }

    val currentProgress = progress.value

    Canvas(modifier = Modifier.fillMaxSize()) {
        val centerX = size.width / 2f
        val centerY = size.height * 0.38f

        particles.forEach { p ->
            val dist = p.speed * currentProgress
            val alpha = (1f - currentProgress).coerceIn(0f, 1f)
            val px = centerX + cos(p.angle).toFloat() * dist
            val py = centerY + sin(p.angle).toFloat() * dist + (currentProgress * currentProgress * 180f) // gravity effect

            withTransform({
                rotate(p.rotation + currentProgress * 360f, pivot = Offset(px, py))
            }) {
                drawCircle(
                    color = p.color.copy(alpha = alpha),
                    radius = p.radius * (1f - currentProgress * 0.4f),
                    center = Offset(px, py)
                )
            }
        }
    }
}
