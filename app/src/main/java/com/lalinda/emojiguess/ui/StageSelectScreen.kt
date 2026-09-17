package com.lalinda.emojiguess.ui

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lalinda.emojiguess.game.GameUiState

data class StageInfo(
    val id: Int,
    val title: String,
    val subtitle: String,
    val icon: String
)

val stageList = listOf(
    StageInfo(1, "Stage 1", "Novice Explorer", "😀"),
    StageInfo(2, "Stage 2", "Pop Culture", "😍"),
    StageInfo(3, "Stage 3", "Brain Teaser", "😡"),
    StageInfo(4, "Stage 4", "Emoji Master", "😈"),
    StageInfo(5, "Stage 5", "Hardcore Blitz", "💀")
)

@Composable
fun StageSelectScreen(
    uiState: GameUiState,
    onStageSelect: (Int) -> Unit,
    onLevelClick: (stage: Int, level: Int) -> Unit,
    onRefillLivesClick: () -> Unit,
    onBackClick: () -> Unit
) {
    val selectedStage = uiState.selectedStage
    val unlockedStage = uiState.unlockedStage

    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseGlow by infiniteTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(PremiumBackgroundGradient)
    ) {
        FloatingBackgroundEmojis()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            // Header Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, bottom = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(DarkNavyCard)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = TextWhite
                    )
                }

                Text(
                    text = "SELECT LEVEL",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Black,
                    color = PurpleGlow,
                    letterSpacing = 2.sp
                )

                // Lives Indicator
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = DarkNavyCard,
                    modifier = Modifier.clickable { onRefillLivesClick() }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "❤️ ${uiState.lives}/${uiState.maxLives}", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = RedWrong)
                    }
                }
            }

            // Stage Tabs Selector
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                items(stageList) { stage ->
                    val isUnlocked = stage.id <= unlockedStage
                    val isSelected = stage.id == selectedStage

                    val bg = when {
                        isSelected -> PurplePrimary
                        isUnlocked -> DarkNavyCard
                        else -> Color(0xFF1B1B2A)
                    }

                    val borderCol = when {
                        isSelected -> GoldAccent
                        isUnlocked -> PurpleGlow.copy(alpha = 0.4f)
                        else -> Color.Transparent
                    }

                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = bg,
                        modifier = Modifier
                            .border(2.dp, borderCol, RoundedCornerShape(16.dp))
                            .clip(RoundedCornerShape(16.dp))
                            .clickable { onStageSelect(stage.id) }
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(text = if (isUnlocked) stage.icon else "🔒", fontSize = 16.sp)
                            Column {
                                Text(
                                    text = stage.title,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Black,
                                    color = if (isUnlocked) TextWhite else TextMuted
                                )
                                Text(
                                    text = stage.subtitle,
                                    fontSize = 10.sp,
                                    color = if (isSelected) CyanSecondary else TextMuted
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Active Stage Info & Grid
            if (selectedStage <= 4) {
                // Stages 1..4: 50 Levels Grid
                Column(modifier = Modifier.fillMaxSize()) {
                    Text(
                        text = "${stageList[selectedStage - 1].icon} STAGE $selectedStage: ${stageList[selectedStage - 1].subtitle.uppercase()} (50 LEVELS)",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = CyanSecondary,
                        modifier = Modifier.padding(bottom = 10.dp)
                    )

                    LazyVerticalGrid(
                        columns = GridCells.Fixed(5),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        contentPadding = PaddingValues(bottom = 24.dp)
                    ) {
                        items(50) { index ->
                            val levelNum = index + 1
                            val key = "s${selectedStage}_l$levelNum"
                            val stars = uiState.levelStarsMap[key] ?: -1

                            val prevKey = "s${selectedStage}_l${levelNum - 1}"
                            val isLevelUnlocked = (levelNum == 1) || (uiState.levelStarsMap.containsKey(key)) || (uiState.levelStarsMap.containsKey(prevKey))

                            LevelGridCard(
                                level = levelNum,
                                stars = if (stars >= 0) stars else 0,
                                isUnlocked = isLevelUnlocked,
                                isCompleted = stars > 0,
                                pulseScale = if (isLevelUnlocked && stars == 0) pulseGlow else 1f,
                                onClick = {
                                    if (isLevelUnlocked) {
                                        onLevelClick(selectedStage, levelNum)
                                    }
                                }
                            )
                        }
                    }
                }
            } else {
                // Stage 5: Hardcore Blitz Mode Card
                val isStage5Unlocked = unlockedStage >= 5

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(vertical = 30.dp, horizontal = 12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .scale(if (isStage5Unlocked) pulseGlow else 1f)
                            .border(3.dp, if (isStage5Unlocked) GoldAccent else Color.Gray, RoundedCornerShape(26.dp)),
                        shape = RoundedCornerShape(26.dp),
                        color = DarkNavySurface
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(28.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(text = if (isStage5Unlocked) "💀 ⚡ 💀" else "🔒 💀 🔒", fontSize = 48.sp)
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "HARDCORE SPEED BLITZ",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Black,
                                color = if (isStage5Unlocked) GoldLight else TextMuted,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = if (isStage5Unlocked) {
                                    "⚡ 10-Second Timer per question!\n💥 Non-stop endless emojis!\n🔥 High Stakes Bonus Coins & XP!"
                                } else {
                                    "🔒 LOCKED!\nComplete all 200 campaign levels (Stages 1-4) to unlock Stage 5 Hardcore Blitz!"
                                },
                                fontSize = 14.sp,
                                color = TextWhite,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(24.dp))
                            GameButton(
                                text = if (isStage5Unlocked) "ENTER HARDCORE MODE!" else "🔒 LOCKED",
                                icon = if (isStage5Unlocked) "⚡" else "🔒",
                                onClick = {
                                    if (isStage5Unlocked) {
                                        onLevelClick(5, 1)
                                    }
                                },
                                enabled = isStage5Unlocked,
                                backgroundColor = if (isStage5Unlocked) RedWrong else Color.Gray,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LevelGridCard(
    level: Int,
    stars: Int,
    isUnlocked: Boolean,
    isCompleted: Boolean,
    pulseScale: Float,
    onClick: () -> Unit
) {
    val cardBg = when {
        !isUnlocked -> Color(0xFF161625)
        isCompleted -> PurplePrimary.copy(alpha = 0.4f)
        else -> DarkNavyCard
    }

    val borderCol = when {
        !isUnlocked -> Color.Transparent
        isCompleted -> GoldAccent
        else -> CyanGlow.copy(alpha = 0.6f)
    }

    Box(
        modifier = Modifier
            .size(62.dp)
            .scale(pulseScale)
            .clip(RoundedCornerShape(16.dp))
            .background(cardBg)
            .border(2.dp, borderCol, RoundedCornerShape(16.dp))
            .clickable(enabled = isUnlocked) { onClick() }
            .padding(4.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (!isUnlocked) {
                Text(text = "🔒", fontSize = 16.sp)
            } else {
                Text(
                    text = "$level",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Black,
                    color = TextWhite
                )

                if (isCompleted && stars > 0) {
                    val starIcons = "⭐".repeat(stars)
                    Text(text = starIcons, fontSize = 9.sp)
                }
            }
        }
    }
}
