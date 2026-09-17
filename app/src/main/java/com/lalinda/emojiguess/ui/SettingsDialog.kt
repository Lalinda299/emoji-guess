package com.lalinda.emojiguess.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
fun SettingsDialog(
    soundEnabled: Boolean,
    bgmEnabled: Boolean,
    vibrationEnabled: Boolean,
    onSoundToggled: (Boolean) -> Unit,
    onBgmToggled: (Boolean) -> Unit,
    onVibrationToggled: (Boolean) -> Unit,
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
                    text = "⚙️ Settings",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextWhite
                )

                Spacer(modifier = Modifier.height(20.dp))

                // BGM Music Switch
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "🎵", fontSize = 20.sp)
                        Text(text = "Music (BGM)", fontSize = 16.sp, color = TextWhite, fontWeight = FontWeight.Medium)
                    }
                    Switch(
                        checked = bgmEnabled,
                        onCheckedChange = onBgmToggled,
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = TextWhite,
                            checkedTrackColor = PurplePrimary
                        )
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Sound FX Switch
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "🔊", fontSize = 20.sp)
                        Text(text = "Sound FX", fontSize = 16.sp, color = TextWhite, fontWeight = FontWeight.Medium)
                    }
                    Switch(
                        checked = soundEnabled,
                        onCheckedChange = onSoundToggled,
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = TextWhite,
                            checkedTrackColor = PurplePrimary
                        )
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Vibration Switch
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "📳", fontSize = 20.sp)
                        Text(text = "Vibration", fontSize = 16.sp, color = TextWhite, fontWeight = FontWeight.Medium)
                    }
                    Switch(
                        checked = vibrationEnabled,
                        onCheckedChange = onVibrationToggled,
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = TextWhite,
                            checkedTrackColor = PurplePrimary
                        )
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // About Info
                Text(
                    text = "Emoji Guess v1.0.0",
                    fontSize = 12.sp,
                    color = TextMuted,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(vertical = 4.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Close Button
                GameButton(
                    text = "CLOSE",
                    onClick = onDismiss,
                    backgroundColor = DarkNavyCard,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}
