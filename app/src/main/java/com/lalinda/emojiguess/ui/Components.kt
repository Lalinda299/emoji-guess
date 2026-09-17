package com.lalinda.emojiguess.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.viewinterop.AndroidView
import com.lalinda.emojiguess.ads.AdConfig
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import kotlinx.coroutines.launch

@Composable
fun StatChip(
    icon: String,
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    backgroundColor: Color = DarkNavyCard,
    accentColor: Color = GoldAccent
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        color = backgroundColor,
        shadowElevation = 4.dp
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(text = icon, fontSize = 18.sp)
            if (label.isNotEmpty()) {
                Spacer(modifier = Modifier.width(6.dp))
                Column(
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = label,
                        fontSize = 11.sp,
                        color = TextMuted,
                        fontWeight = FontWeight.Medium,
                        maxLines = 1
                    )
                    Text(
                        text = value,
                        fontSize = 13.sp,
                        color = accentColor,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1
                    )
                }
            } else {
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = value,
                    fontSize = 14.sp,
                    color = accentColor,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1
                )
            }
        }
    }
}

@Composable
fun XpProgressBar(
    xp: Int,
    level: Int,
    title: String,
    modifier: Modifier = Modifier
) {
    val currentLevelBaseXp = (level - 1) * 500
    val xpInCurrentLevel = (xp - currentLevelBaseXp).coerceIn(0, 500)
    val progressFraction = xpInCurrentLevel / 500f

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = DarkNavyCard
    ) {
        Column(modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "LVL $level • $title",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Black,
                    color = GoldAccent
                )
                Text(
                    text = "$xpInCurrentLevel / 500 XP",
                    fontSize = 12.sp,
                    color = TextMuted,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(Color.Black.copy(alpha = 0.4f))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(progressFraction)
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(
                            Brush.horizontalGradient(
                                listOf(CyanSecondary, GoldAccent)
                            )
                        )
                )
            }
        }
    }
}

@Composable
fun TopGameHeader(
    score: Int,
    lives: Int,
    maxLives: Int = 6,
    nextLifeTimerSeconds: Long = 0L,
    coins: Int,
    onBackClick: () -> Unit,
    onRefillClick: (() -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
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

        Row(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 6 Hearts Indicator
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = DarkNavyCard,
                modifier = Modifier.clickable { onRefillClick?.invoke() }
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "❤️ $lives/$maxLives", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = RedWrong)
                    if (lives < maxLives && nextLifeTimerSeconds > 0) {
                        val mins = nextLifeTimerSeconds / 60
                        val secs = nextLifeTimerSeconds % 60
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = String.format("(%02d:%02d)", mins, secs),
                            fontSize = 11.sp,
                            color = TextMuted
                        )
                    }
                }
            }

            // Score
            StatChip(icon = "⭐", label = "", value = "$score", accentColor = CyanSecondary)

            // Coins
            StatChip(icon = "🪙", label = "", value = "$coins", accentColor = GoldAccent)
        }
    }
}

@Composable
fun RefillLivesDialog(
    coins: Int,
    onWatchAdRefill: () -> Unit,
    onCoinsRefill: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = DarkNavySurface,
        title = {
            Text(
                text = "💔 REFILL LIVES",
                fontSize = 22.sp,
                fontWeight = FontWeight.Black,
                color = RedWrong,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                Text(
                    text = "Need lives to continue guessing? Choose a refill option below:",
                    fontSize = 14.sp,
                    color = TextWhite,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                GameButton(
                    text = "WATCH AD (+2 LIVES)",
                    icon = "🎁",
                    onClick = onWatchAdRefill,
                    backgroundColor = GoldAccent,
                    textColor = DarkNavyBackground,
                    modifier = Modifier.fillMaxWidth()
                )

                GameButton(
                    text = "REFILL ALL FOR 500 🪙",
                    icon = "🪙",
                    onClick = onCoinsRefill,
                    backgroundColor = PurplePrimary,
                    enabled = coins >= 500,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {},
        dismissButton = {
            TextButton(onClick = onDismiss, modifier = Modifier.fillMaxWidth()) {
                Text(text = "Close", color = TextMuted, textAlign = TextAlign.Center)
            }
        }
    )
}

@Composable
fun LevelUpDialog(
    newLevel: Int,
    rewardCoins: Int,
    onClaimReward: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onClaimReward,
        containerColor = DarkNavySurface,
        title = {
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                Text(text = "🎉 LEVEL UP!", fontSize = 28.sp, fontWeight = FontWeight.Black, color = GoldLight)
                Text(text = "YOU ARE NOW LEVEL $newLevel!", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = CyanSecondary)
            }
        },
        text = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(text = "🌟 Level Up Rewards:", fontSize = 15.sp, color = TextWhite, fontWeight = FontWeight.SemiBold)
                Text(text = "🪙 +$rewardCoins Bonus Coins", fontSize = 16.sp, color = GoldAccent, fontWeight = FontWeight.Bold)
                Text(text = "❤️ Lives Fully Restored to 6!", fontSize = 16.sp, color = RedWrong, fontWeight = FontWeight.Bold)
            }
        },
        confirmButton = {
            GameButton(
                text = "CLAIM REWARDS!",
                icon = "✨",
                onClick = onClaimReward,
                backgroundColor = GoldAccent,
                textColor = DarkNavyBackground,
                modifier = Modifier.fillMaxWidth()
            )
        }
    )
}

@Composable
fun ExitAppDialog(
    onConfirmExit: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = DarkNavySurface,
        title = {
            Text(
                text = "👋 EXIT GAME?",
                fontSize = 22.sp,
                fontWeight = FontWeight.Black,
                color = GoldLight,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        },
        text = {
            Text(
                text = "Are you sure you want to exit Emoji Guess?",
                fontSize = 15.sp,
                color = TextWhite,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        },
        confirmButton = {
            GameButton(
                text = "YES, EXIT",
                icon = "🚪",
                onClick = onConfirmExit,
                backgroundColor = RedWrong,
                modifier = Modifier.fillMaxWidth()
            )
        },
        dismissButton = {
            TextButton(onClick = onDismiss, modifier = Modifier.fillMaxWidth()) {
                Text(text = "CANCEL", color = CyanSecondary, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
            }
        }
    )
}

@Composable
fun GameButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    backgroundColor: Color = PurplePrimary,
    textColor: Color = TextWhite,
    icon: String? = null,
    enabled: Boolean = true
) {
    val scale = remember { Animatable(1f) }
    val scope = rememberCoroutineScope()

    Box(
        modifier = modifier
            .scale(scale.value)
            .clip(RoundedCornerShape(20.dp))
            .background(
                if (enabled) {
                    Brush.verticalGradient(
                        colors = listOf(
                            backgroundColor,
                            backgroundColor.copy(alpha = 0.85f)
                        )
                    )
                } else {
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Gray,
                            Color.DarkGray
                        )
                    )
                }
            )
            .clickable(
                enabled = enabled,
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                scope.launch {
                    scale.animateTo(0.94f, animationSpec = tween(60))
                    scale.animateTo(1f, animationSpec = tween(60))
                }
                onClick()
            }
            .padding(vertical = 16.dp, horizontal = 24.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (icon != null) {
                Text(text = icon, fontSize = 20.sp)
            }
            Text(
                text = text,
                color = textColor,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun BannerAdView(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp)
            .background(DarkNavyBackground),
        contentAlignment = Alignment.Center
    ) {
        AndroidView(
            factory = { context ->
                AdView(context).apply {
                    setAdSize(AdSize.BANNER)
                    adUnitId = AdConfig.BANNER_ID
                    loadAd(AdRequest.Builder().build())
                }
            },
            modifier = Modifier.fillMaxWidth()
        )
    }
}
