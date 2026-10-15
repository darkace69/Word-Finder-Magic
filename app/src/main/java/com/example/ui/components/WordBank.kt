package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.PlacedWord
import com.example.ui.theme.WordSearchColors

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun WordBank(
    targetWords: List<String>,
    foundWords: List<PlacedWord>,
    isUppercase: Boolean,
    onSpeakWord: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val foundMap = foundWords.associateBy { it.word }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(Color(0xFFF8FAFC))
            .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(20.dp))
            .padding(14.dp)
            .testTag("word_bank")
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Word List (${foundWords.size}/${targetWords.size})",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF334155)
                )
            )

            Text(
                text = "Tap 🔊 to hear",
                style = MaterialTheme.typography.bodySmall.copy(
                    color = Color(0xFF64748B),
                    fontWeight = FontWeight.Medium
                )
            )
        }

        Spacer(modifier = Modifier.size(10.dp))

        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            targetWords.forEach { word ->
                val placed = foundMap[word]
                val isFound = placed != null
                val displayWord = if (isUppercase) word.uppercase() else word.lowercase()

                val chipBg by animateColorAsState(
                    targetValue = if (isFound) {
                        val color = WordSearchColors.HighlightPastels[placed.colorIndex % WordSearchColors.HighlightPastels.size]
                        color.copy(alpha = 0.28f)
                    } else {
                        Color.White
                    },
                    animationSpec = tween(durationMillis = 350),
                    label = "chip_bg"
                )

                val chipBorderColor by animateColorAsState(
                    targetValue = if (isFound) {
                        val color = WordSearchColors.HighlightPastels[placed.colorIndex % WordSearchColors.HighlightPastels.size]
                        color.copy(alpha = 0.85f)
                    } else {
                        Color(0xFFCBD5E1)
                    },
                    animationSpec = tween(durationMillis = 350),
                    label = "chip_border"
                )

                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(chipBg)
                        .border(1.5.dp, chipBorderColor, RoundedCornerShape(12.dp))
                        .clickable { onSpeakWord(word) }
                        .padding(horizontal = 10.dp, vertical = 7.dp)
                        .testTag("word_chip_${word.lowercase()}"),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (isFound) {
                        Box(
                            modifier = Modifier
                                .size(18.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF10B981)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Word found",
                                tint = Color.White,
                                modifier = Modifier.size(13.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(6.dp))
                    } else {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                            contentDescription = "Pronounce word",
                            tint = Color(0xFF64748B),
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                    }

                    Text(
                        text = displayWord,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = if (isFound) FontWeight.SemiBold else FontWeight.Bold,
                            fontSize = 15.sp,
                            textDecoration = if (isFound) TextDecoration.LineThrough else TextDecoration.None,
                            color = if (isFound) Color(0xFF475569) else Color(0xFF0F172A)
                        )
                    )
                }
            }
        }
    }
}
