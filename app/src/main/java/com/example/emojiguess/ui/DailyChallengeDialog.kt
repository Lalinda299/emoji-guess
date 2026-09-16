package com.example.emojiguess.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog

@Composable
fun DailyChallengeDialog(
    isAvailable: Boolean,
    onStartChallenge: () -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = DarkNavySurface,
            shadowElevation = 12.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "📅 DAILY CHALLENGE",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Black,
                    color = GoldAccent
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "🔥 Today's Special Challenge",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextWhite
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = if (isAvailable) {
                        "Test your emoji skills with today's special question!\n\nReward: 🪙 +100 Bonus Coins"
                    } else {
                        "You have already completed today's challenge!\nCome back tomorrow for a new question."
                    },
                    fontSize = 14.sp,
                    color = TextMuted,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(24.dp))

                if (isAvailable) {
                    GameButton(
                        text = "START CHALLENGE",
                        icon = "▶",
                        onClick = {
                            onDismiss()
                            onStartChallenge()
                        },
                        backgroundColor = GoldAccent,
                        textColor = DarkNavyBackground,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(10.dp))
                }

                GameButton(
                    text = "CANCEL",
                    onClick = onDismiss,
                    backgroundColor = DarkNavyCard,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}
