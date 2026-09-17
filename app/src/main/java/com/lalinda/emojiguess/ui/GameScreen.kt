package com.lalinda.emojiguess.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import com.lalinda.emojiguess.game.GameUiState
import kotlinx.coroutines.launch

@Composable
fun GameScreen(
    uiState: GameUiState,
    onAnswerSelected: (String) -> Unit,
    onHintClick: () -> Unit,
    onRefillLivesClick: () -> Unit,
    onBackClick: () -> Unit
) {
    val currentQ = uiState.currentQuestionState ?: return

    val infiniteTransition = rememberInfiniteTransition(label = "CardBreath")
    val breathScale by infiniteTransition.animateFloat(
        initialValue = 0.99f,
        targetValue = 1.01f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )

    val flamePulse by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "flame"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(PremiumBackgroundGradient)
    ) {
        // Floating ambient background particles
        FloatingBackgroundEmojis()

        // Confetti burst on correct answer!
        ParticleBurst(trigger = uiState.triggerParticleBurst)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Top Stats Header
            TopGameHeader(
                score = uiState.score,
                lives = uiState.lives,
                maxLives = uiState.maxLives,
                nextLifeTimerSeconds = uiState.nextLifeTimerSeconds,
                coins = uiState.coins,
                onBackClick = onBackClick,
                onRefillClick = onRefillLivesClick
            )

            // Speed Bonanza / Flash Bonus Banner Overlay
            if (uiState.isFlashBonusActive && uiState.flashBonusTimeRemainingSeconds > 0) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 4.dp)
                        .scale(flamePulse)
                        .border(2.dp, GoldAccent, RoundedCornerShape(18.dp)),
                    shape = RoundedCornerShape(18.dp),
                    color = GoldAccent.copy(alpha = 0.25f)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "⚡ SPEED BONANZA! (+50 🪙)",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Black,
                            color = GoldLight
                        )
                        Text(
                            text = "⏱️ ${uiState.flashBonusTimeRemainingSeconds}s",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Black,
                            color = RedWrong
                        )
                    }
                }
            }

            // Hardcore Mode Countdown Timer Bar
            if (uiState.isHardcoreMode) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 4.dp)
                        .border(2.dp, RedWrong, RoundedCornerShape(18.dp)),
                    shape = RoundedCornerShape(18.dp),
                    color = RedWrong.copy(alpha = 0.25f)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "🔥 HARDCORE TIMER",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Black,
                            color = TextWhite
                        )
                        Text(
                            text = "⏱️ ${uiState.hardcoreTimeRemainingSeconds}s",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Black,
                            color = GoldLight
                        )
                    }
                }
            }

            // Progress & Level Header Row
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 4.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Level & Stage Badge
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = PurplePrimary.copy(alpha = 0.25f),
                        modifier = Modifier.border(1.dp, PurpleGlow, RoundedCornerShape(14.dp))
                    ) {
                        val stageTitle = when {
                            currentQ.isDailyChallenge -> "🔥 DAILY CHALLENGE"
                            uiState.isHardcoreMode -> "⚡ HARDCORE BLITZ - LVL ${currentQ.level}"
                            else -> "⭐ STAGE ${currentQ.stage} - LVL ${currentQ.level}/50"
                        }
                        Text(
                            text = stageTitle,
                            fontSize = 13.sp,
                            color = TextWhite,
                            fontWeight = FontWeight.Black,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp)
                        )
                    }

                    // Streak Flame indicator with pulse animation
                    if (uiState.streak > 1) {
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = GoldAccent.copy(alpha = 0.2f),
                            modifier = Modifier
                                .scale(flamePulse)
                                .border(1.dp, GoldLight, RoundedCornerShape(14.dp))
                        ) {
                            Text(
                                text = "🔥 ${uiState.streak} STREAK",
                                fontSize = 13.sp,
                                color = GoldLight,
                                fontWeight = FontWeight.Black,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp)
                            )
                        }
                    }
                }
            }

            // Live Companion / Animated Emoji Guide Banner
            LiveGuideBanner(
                uiState = uiState,
                currentQState = currentQ
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Main Emoji Question Card
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .scale(breathScale)
                    .border(
                        2.dp,
                        Brush.horizontalGradient(listOf(PurpleGlow.copy(alpha = 0.6f), CyanGlow.copy(alpha = 0.4f))),
                        RoundedCornerShape(26.dp)
                    ),
                shape = RoundedCornerShape(26.dp),
                color = DarkNavySurface,
                shadowElevation = 12.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 24.dp, horizontal = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Category Badge
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = PurplePrimary.copy(alpha = 0.3f)
                    ) {
                        Text(
                            text = currentQ.question.category.uppercase(),
                            fontSize = 13.sp,
                            color = PurpleGlow,
                            fontWeight = FontWeight.Black,
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 5.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Large Emoji Combination
                    Text(
                        text = currentQ.question.emojiCombination,
                        fontSize = 56.sp,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    if (currentQ.isCorrect == true) {
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = GreenCorrect.copy(alpha = 0.3f),
                            modifier = Modifier.border(1.5.dp, GreenCorrect, RoundedCornerShape(14.dp))
                        ) {
                            Text(
                                text = "🎉 PERFECT! YOU ARE THE BEST! (+${100 + (uiState.streak - 1) * 25} PTS)",
                                fontSize = 13.sp,
                                color = TextWhite,
                                fontWeight = FontWeight.Black,
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                            )
                        }
                    } else if (currentQ.isCorrect == false) {
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = RedWrong.copy(alpha = 0.3f),
                            modifier = Modifier.border(1.5.dp, RedWrong, RoundedCornerShape(14.dp))
                        ) {
                            Text(
                                text = "❌ WRONG ANSWER! STAY FOCUSED!",
                                fontSize = 13.sp,
                                color = TextWhite,
                                fontWeight = FontWeight.Black,
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                            )
                        }
                    } else {
                        Text(
                            text = "Can you guess this emoji combination?",
                            fontSize = 14.sp,
                            color = TextMuted,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Hint Button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                horizontalArrangement = Arrangement.End
            ) {
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = DarkNavyCard,
                    modifier = Modifier
                        .clip(RoundedCornerShape(14.dp))
                        .clickable { onHintClick() }
                        .border(1.dp, GoldAccent.copy(alpha = 0.5f), RoundedCornerShape(14.dp))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(text = "💡 Hint (-100 🪙)", fontSize = 13.sp, color = GoldAccent, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // 4 Answer Option Buttons
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                currentQ.shuffledOptions.forEach { option ->
                    AnswerButton(
                        optionText = option,
                        isSelected = currentQ.selectedOption == option,
                        isCorrectAnswer = currentQ.question.correctAnswer == option,
                        isAnswerSubmitted = currentQ.selectedOption != null,
                        isDisabledByHint = currentQ.disabledOptions.contains(option),
                        onClick = { onAnswerSelected(option) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))
        }
    }
}

@Composable
fun AnswerButton(
    optionText: String,
    isSelected: Boolean,
    isCorrectAnswer: Boolean,
    isAnswerSubmitted: Boolean,
    isDisabledByHint: Boolean,
    onClick: () -> Unit
) {
    val shakeOffset = remember { Animatable(0f) }
    val buttonScale = remember { Animatable(1f) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(isAnswerSubmitted, isSelected) {
        if (isAnswerSubmitted && isSelected) {
            if (isCorrectAnswer) {
                buttonScale.animateTo(1.06f, tween(100, easing = LinearOutSlowInEasing))
                buttonScale.animateTo(1f, tween(100, easing = FastOutLinearInEasing))
            } else {
                shakeOffset.animateTo(
                    targetValue = 0f,
                    animationSpec = keyframes {
                        durationMillis = 400
                        -16f at 50
                        16f at 100
                        -12f at 150
                        12f at 200
                        -6f at 250
                        6f at 300
                        0f at 400
                    }
                )
            }
        }
    }

    val (bgColor, borderColor, iconText) = when {
        isAnswerSubmitted && isCorrectAnswer -> Triple(GreenCorrect, GreenCorrect, " ✓")
        isAnswerSubmitted && isSelected && !isCorrectAnswer -> Triple(RedWrong, RedWrong, " ✕")
        isDisabledByHint -> Triple(Color(0xFF1E1E2E), Color.Transparent, "")
        else -> Triple(DarkNavyCard, PurpleGlow.copy(alpha = 0.2f), "")
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .offset(x = shakeOffset.value.dp)
            .scale(buttonScale.value)
            .clip(RoundedCornerShape(18.dp))
            .background(bgColor)
            .border(2.dp, borderColor, RoundedCornerShape(18.dp))
            .clickable(
                enabled = !isAnswerSubmitted && !isDisabledByHint,
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                scope.launch {
                    buttonScale.animateTo(0.95f, tween(50))
                    buttonScale.animateTo(1f, tween(50))
                }
                onClick()
            }
            .padding(vertical = 16.dp, horizontal = 22.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = optionText,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = if (isDisabledByHint) Color.Gray else TextWhite
            )

            if (iconText.isNotEmpty()) {
                Text(
                    text = iconText,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Black,
                    color = TextWhite
                )
            }
        }
    }
}

@Composable
fun LiveGuideBanner(
    uiState: GameUiState,
    currentQState: com.lalinda.emojiguess.game.CurrentQuestionState,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "LiveGuide")
    val guidePulse by infiniteTransition.animateFloat(
        initialValue = 0.94f,
        targetValue = 1.06f,
        animationSpec = infiniteRepeatable(
            animation = tween(900, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "guidePulse"
    )

    val (avatar, guideMessage, badgeBg, borderColor) = when {
        currentQState.isCorrect == true -> Quadruple(
            "👑",
            "YOU ARE THE BEST! PERFECT ANSWER! 🎉",
            Brush.horizontalGradient(listOf(Color(0xFF059669), Color(0xFF10B981))),
            GoldAccent
        )
        currentQState.isCorrect == false -> Quadruple(
            "💪",
            "Don't give up! You can do it! Stay focused!",
            Brush.horizontalGradient(listOf(Color(0xFFB91C1C), Color(0xFFEF4444))),
            RedWrong
        )
        uiState.streak >= 3 -> Quadruple(
            "🔥",
            "UNSTOPPABLE! ${uiState.streak} STREAK! YOU'RE A LEGEND!",
            Brush.horizontalGradient(listOf(Color(0xFFD97706), Color(0xFFF59E0B))),
            GoldLight
        )
        uiState.streak == 2 -> Quadruple(
            "⚡",
            "DOUBLE COMBO! YOU ARE ON FIRE! 🔥",
            Brush.horizontalGradient(listOf(Color(0xFF6D28D9), Color(0xFF8B5CF6))),
            CyanSecondary
        )
        uiState.isHardcoreMode -> Quadruple(
            "⚡",
            "SPEED BLITZ! YOU ARE THE BEST! THINK FAST!",
            Brush.horizontalGradient(listOf(Color(0xFF991B1B), Color(0xFFDC2626))),
            GoldAccent
        )
        else -> Quadruple(
            "🤖",
            "You are the BEST! Guess the emoji combination below! 👇",
            Brush.horizontalGradient(listOf(DarkNavyCard, Color(0xFF262642))),
            PurpleGlow
        )
    }

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 4.dp)
            .border(2.dp, borderColor, RoundedCornerShape(22.dp)),
        shape = RoundedCornerShape(22.dp),
        color = Color.Transparent,
        shadowElevation = 8.dp
    ) {
        Box(
            modifier = Modifier
                .background(badgeBg)
                .padding(horizontal = 14.dp, vertical = 10.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Live Avatar Icon with pulse animation
                Box(
                    modifier = Modifier
                        .scale(guidePulse)
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(Color.Black.copy(alpha = 0.3f))
                        .border(1.5.dp, GoldLight, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = avatar,
                        fontSize = 24.sp
                    )
                }

                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = GoldAccent.copy(alpha = 0.3f)
                        ) {
                            Text(
                                text = "LIVE GUIDE",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Black,
                                color = GoldLight,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                        Text(
                            text = "• ${uiState.playerTitle}",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextMuted
                        )
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = guideMessage,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Black,
                        color = TextWhite
                    )
                }
            }
        }
    }
}

private data class Quadruple<A, B, C, D>(
    val first: A,
    val second: B,
    val third: C,
    val fourth: D
)
