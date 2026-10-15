package com.example.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.model.UserSettings

@Composable
fun SettingsDialog(
    settings: UserSettings,
    onUpdateSettings: (UserSettings) -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = Color.White,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
                .testTag("settings_dialog")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Game Settings ⚙️",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1E293B)
                        )
                    )
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.testTag("close_settings_button")
                    ) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Uppercase Toggle
                SettingSwitchRow(
                    title = "Uppercase Letters",
                    subtitle = if (settings.isUppercase) "Showing ABC (Uppercase)" else "Showing abc (Lowercase)",
                    checked = settings.isUppercase,
                    onCheckedChange = { onUpdateSettings(settings.copy(isUppercase = it)) },
                    testTag = "setting_uppercase_switch"
                )

                HorizontalDivider(color = Color(0xFFF1F5F9), modifier = Modifier.padding(vertical = 8.dp))

                // Word Pronunciation (TTS)
                SettingSwitchRow(
                    title = "Read Words Aloud 🗣️",
                    subtitle = "Tap 🔊 in word bank to hear pronunciation",
                    checked = settings.speechPronunciationEnabled,
                    onCheckedChange = { onUpdateSettings(settings.copy(speechPronunciationEnabled = it)) },
                    testTag = "setting_speech_switch"
                )

                HorizontalDivider(color = Color(0xFFF1F5F9), modifier = Modifier.padding(vertical = 8.dp))

                // Sound Effects
                SettingSwitchRow(
                    title = "Sound Effects 🔔",
                    subtitle = "Chimes when words are found",
                    checked = settings.soundEffectsEnabled,
                    onCheckedChange = { onUpdateSettings(settings.copy(soundEffectsEnabled = it)) },
                    testTag = "setting_sound_switch"
                )

                HorizontalDivider(color = Color(0xFFF1F5F9), modifier = Modifier.padding(vertical = 8.dp))

                // Timer Mode
                SettingSwitchRow(
                    title = "Show Puzzle Timer ⏱️",
                    subtitle = if (settings.timerEnabled) "Clock is counting" else "Relaxed untimed mode",
                    checked = settings.timerEnabled,
                    onCheckedChange = { onUpdateSettings(settings.copy(timerEnabled = it)) },
                    testTag = "setting_timer_switch"
                )

                HorizontalDivider(color = Color(0xFFF1F5F9), modifier = Modifier.padding(vertical = 8.dp))

                // First Letter Hints
                SettingSwitchRow(
                    title = "Letter Hints 💡",
                    subtitle = "Hint button reveals first letter",
                    checked = settings.showFirstLetterHints,
                    onCheckedChange = { onUpdateSettings(settings.copy(showFirstLetterHints = it)) },
                    testTag = "setting_hints_switch"
                )
            }
        }
    }
}

@Composable
private fun SettingSwitchRow(
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    testTag: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1E293B)
                )
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = Color(0xFF64748B),
                    fontSize = 12.sp
                )
            )
        }

        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = Color(0xFF4F46E5)
            ),
            modifier = Modifier.testTag(testTag)
        )
    }
}
