package com.example.emojiguess.ui

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.emojiguess.game.GameUiState

import androidx.compose.foundation.clickable

@Composable
fun HomeScreen(
    uiState: GameUiState,
    onSelectStageClick: () -> Unit,
    onHardcoreClick: () -> Unit,
    onDailyChallengeClick: () -> Unit,
    onRefillLivesClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "EmojiFloat")
    val floatOffset by infiniteTransition.animateFloat(
        initialValue = -12f,
        targetValue = 12f,
        animationSpec = infiniteRepeatable(
            animation = tween(1400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "offset"
    )
    val logoScale by infiniteTransition.animateFloat(
        initialValue = 0.97f,
        targetValue = 1.03f,
        animationSpec = infiniteRepeatable(
            animation = tween(1800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )
    val pulseGlow by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 0.8f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(PremiumBackgroundGradient)
    ) {
        // Live Floating Emojis Background
        FloatingBackgroundEmojis()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Top Header: 6 Lives, Coins & Settings
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 6 Hearts Chip
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = DarkNavyCard,
                    modifier = Modifier.clickable { onRefillLivesClick() }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "❤️ ${uiState.lives}/${uiState.maxLives}", fontSize = 14.sp, fontWeight = FontWeight.Black, color = RedWrong)
                        if (uiState.lives < uiState.maxLives && uiState.nextLifeTimerSeconds > 0) {
                            val mins = uiState.nextLifeTimerSeconds / 60
                            val secs = uiState.nextLifeTimerSeconds % 60
                            Text(
                                text = String.format(" (%02d:%02d)", mins, secs),
                                fontSize = 11.sp,
                                color = TextMuted
                            )
                        }
                    }
                }

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    StatChip(icon = "🪙", label = "", value = "${uiState.coins}", accentColor = GoldAccent)

                    IconButton(
                        onClick = onSettingsClick,
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(DarkNavyCard)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Settings",
                            tint = TextWhite
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Player XP Level Bar
            XpProgressBar(
                xp = uiState.playerXp,
                level = uiState.playerLevel,
                title = uiState.playerTitle
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Logo & Animated Hero Emoji
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Surface(
                    modifier = Modifier.scale(logoScale),
                    shape = RoundedCornerShape(28.dp),
                    color = Color.Transparent
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "EMOJI",
                            fontSize = 44.sp,
                            fontWeight = FontWeight.Black,
                            color = PurpleGlow,
                            letterSpacing = 4.sp
                        )
                        Text(
                            text = "GUESS",
                            fontSize = 48.sp,
                            fontWeight = FontWeight.Black,
                            color = CyanSecondary,
                            letterSpacing = 6.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Bouncing Animated Emoji Halo
                Box(
                    modifier = Modifier
                        .offset(y = floatOffset.dp)
                        .size(105.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.radialGradient(
                                colors = listOf(
                                    GoldAccent.copy(alpha = pulseGlow * 0.5f),
                                    PurplePrimary.copy(alpha = 0.2f),
                                    Color.Transparent
                                )
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "🤔",
                        fontSize = 68.sp
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "5 STAGES • 200+ LEVELS",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Black,
                    color = GoldLight,
                    letterSpacing = 1.sp,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Action Buttons Section
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Select Stage & Levels Button
                GameButton(
                    text = "SELECT STAGE & LEVELS",
                    icon = "🗺️",
                    onClick = onSelectStageClick,
                    backgroundColor = PurplePrimary,
                    modifier = Modifier.fillMaxWidth()
                )

                // Stage 5 Hardcore Blitz Button
                GameButton(
                    text = "STAGE 5: HARDCORE BLITZ",
                    icon = "⚡",
                    onClick = onHardcoreClick,
                    backgroundColor = RedWrong,
                    modifier = Modifier.fillMaxWidth()
                )

                // Stats Chips Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    StatChip(
                        icon = "🏆",
                        label = "Best Score",
                        value = "${uiState.bestScore}",
                        accentColor = CyanSecondary,
                        modifier = Modifier.weight(1f)
                    )
                    StatChip(
                        icon = "🔓",
                        label = "Unlocked",
                        value = "Stage ${uiState.unlockedStage}",
                        accentColor = GoldAccent,
                        modifier = Modifier.weight(1f)
                    )
                }

                // Daily Challenge Button
                GameButton(
                    text = if (uiState.isDailyChallengeAvailableToday) "DAILY CHALLENGE" else "DAILY COMPLETED",
                    icon = "📅",
                    onClick = onDailyChallengeClick,
                    backgroundColor = if (uiState.isDailyChallengeAvailableToday) GoldAccent else Color.Gray,
                    textColor = DarkNavyBackground,
                    enabled = uiState.isDailyChallengeAvailableToday,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Adaptive Banner Ad Container
            BannerAdView()

            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

