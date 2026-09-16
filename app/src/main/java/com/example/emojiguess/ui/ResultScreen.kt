package com.example.emojiguess.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.emojiguess.game.GameUiState

@Composable
fun ResultScreen(
    uiState: GameUiState,
    onPlayAgainClick: () -> Unit,
    onHomeClick: () -> Unit,
    onWatchAdContinueClick: () -> Unit
) {
    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        visible = true
    }

    val accuracy = if (uiState.questionsAnsweredInSession > 0) {
        (uiState.correctAnswersInSession.toFloat() / uiState.questionsAnsweredInSession.toFloat() * 100).toInt()
    } else {
        0
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(PremiumBackgroundGradient)
    ) {
        // Floating live background
        FloatingBackgroundEmojis()

        AnimatedVisibility(
            visible = visible,
            enter = fadeIn(tween(450)) + scaleIn(tween(450))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Spacer(modifier = Modifier.height(24.dp))

                // Title Banner
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = if (uiState.isDailyChallengeMode) "CHALLENGE COMPLETE!" else "GAME OVER",
                        fontSize = 34.sp,
                        fontWeight = FontWeight.Black,
                        color = if (uiState.isDailyChallengeMode) GoldLight else RedWrong,
                        letterSpacing = 2.sp
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Final Score & Level Card
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, PurpleGlow.copy(alpha = 0.4f), RoundedCornerShape(22.dp)),
                        shape = RoundedCornerShape(22.dp),
                        color = DarkNavySurface,
                        shadowElevation = 10.dp
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 22.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            if (uiState.starsEarnedInLevel > 0) {
                                val starsText = "⭐".repeat(uiState.starsEarnedInLevel)
                                Text(text = starsText, fontSize = 32.sp)
                                Spacer(modifier = Modifier.height(8.dp))
                            }

                            Text(
                                text = if (uiState.isHardcoreMode) "BLITZ LEVEL: ${uiState.selectedLevel}" else "STAGE ${uiState.selectedStage} • LEVEL ${uiState.selectedLevel}",
                                fontSize = 15.sp,
                                color = GoldLight,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "${uiState.score}",
                                fontSize = 54.sp,
                                fontWeight = FontWeight.Black,
                                color = CyanSecondary
                            )
                            Text(
                                text = "🏆 Best Score: ${uiState.bestScore}",
                                fontSize = 14.sp,
                                color = TextMuted,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Detailed Statistics Grid
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = DarkNavyCard
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        StatResultRow(icon = "✨", label = "XP Earned", value = "+${uiState.xpEarnedInSession} XP")
                        StatResultRow(icon = "🎯", label = "Correct Answers", value = "${uiState.correctAnswersInSession}")
                        StatResultRow(icon = "❌", label = "Wrong Answers", value = "${uiState.questionsAnsweredInSession - uiState.correctAnswersInSession}")
                        StatResultRow(icon = "📊", label = "Accuracy", value = "$accuracy%")
                        StatResultRow(icon = "🔥", label = "Highest Streak", value = "${uiState.highestStreakInSession}")
                        StatResultRow(icon = "🪙", label = "Coins Earned", value = "+${uiState.coinsEarnedInSession}")
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Action Buttons
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    if (!uiState.isDailyChallengeMode) {
                        GameButton(
                            text = "WATCH AD (+6 LIVES)",
                            icon = "🎁",
                            onClick = onWatchAdContinueClick,
                            backgroundColor = GoldAccent,
                            textColor = DarkNavyBackground,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    GameButton(
                        text = "PLAY AGAIN",
                        icon = "🔄",
                        onClick = onPlayAgainClick,
                        backgroundColor = PurplePrimary,
                        modifier = Modifier.fillMaxWidth()
                    )

                    GameButton(
                        text = "HOME",
                        icon = "🏠",
                        onClick = onHomeClick,
                        backgroundColor = DarkNavyCard,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}


@Composable
fun StatResultRow(
    icon: String,
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = icon, fontSize = 18.sp)
            Text(text = label, fontSize = 15.sp, color = TextWhite, fontWeight = FontWeight.Medium)
        }
        Text(text = value, fontSize = 16.sp, color = CyanSecondary, fontWeight = FontWeight.Bold)
    }
}
