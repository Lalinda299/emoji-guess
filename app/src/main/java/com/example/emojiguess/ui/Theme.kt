package com.example.emojiguess.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

val DarkNavyBackground = Color(0xFF0F172A)
val DarkNavySurface = Color(0xFF1E1B4B)
val DarkNavyCard = Color(0xFF312E81)
val DarkGlassCard = Color(0x331E1B4B)

val PurplePrimary = Color(0xFF8B5CF6)
val PurpleDark = Color(0xFF6D28D9)
val PurpleGlow = Color(0xFFA78BFA)

val CyanSecondary = Color(0xFF06B6D4)
val CyanGlow = Color(0xFF67E8F9)

val GoldAccent = Color(0xFFF59E0B)
val GoldLight = Color(0xFFFBBF24)

val GreenCorrect = Color(0xFF10B981)
val RedWrong = Color(0xFFEF4444)

val TextWhite = Color(0xFFFFFFFF)
val TextMuted = Color(0xFFCBD5E1)

// Radiant Gradients
val PremiumBackgroundGradient = Brush.verticalGradient(
    colors = listOf(
        Color(0xFF0F172A),
        Color(0xFF1E1B4B),
        Color(0xFF311B92)
    )
)

val CardGlowGradient = Brush.linearGradient(
    colors = listOf(
        Color(0xFF8B5CF6).copy(alpha = 0.3f),
        Color(0xFF06B6D4).copy(alpha = 0.15f)
    )
)

val PrimaryButtonGradient = Brush.horizontalGradient(
    colors = listOf(
        Color(0xFF8B5CF6),
        Color(0xFF6366F1)
    )
)

val GoldButtonGradient = Brush.horizontalGradient(
    colors = listOf(
        Color(0xFFF59E0B),
        Color(0xFFD97706)
    )
)

private val DarkColorScheme = darkColorScheme(
    primary = PurplePrimary,
    onPrimary = TextWhite,
    secondary = CyanSecondary,
    onSecondary = TextWhite,
    tertiary = GoldAccent,
    onTertiary = TextWhite,
    background = DarkNavyBackground,
    onBackground = TextWhite,
    surface = DarkNavySurface,
    onSurface = TextWhite,
    surfaceVariant = DarkNavyCard,
    onSurfaceVariant = TextMuted
)

@Composable
fun EmojiGuessTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        content = content
    )
}
