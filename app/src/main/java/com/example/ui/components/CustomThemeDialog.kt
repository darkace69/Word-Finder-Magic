package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.model.GradeLevel
import com.example.model.ThemeData
import com.example.ui.theme.WordSearchColors
import java.util.UUID

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CustomThemeDialog(
    initialTheme: ThemeData? = null,
    currentGrade: GradeLevel,
    onDismiss: () -> Unit,
    onSave: (ThemeData) -> Unit
) {
    var themeName by remember { mutableStateOf(initialTheme?.name ?: "") }
    var selectedEmoji by remember { mutableStateOf(initialTheme?.emoji ?: "⭐") }
    var selectedColorIndex by remember {
        val initialIdx = WordSearchColors.CustomThemePalettes.indexOfFirst {
            it.first == (initialTheme?.primaryColorHex ?: 0xFF3B82F6L)
        }
        mutableStateOf(if (initialIdx >= 0) initialIdx else 0)
    }

    var newWordInput by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val wordsList = remember {
        mutableStateListOf<String>().apply {
            if (initialTheme != null && initialTheme.customWords.isNotEmpty()) {
                addAll(initialTheme.customWords)
            } else {
                // Default starter words
                addAll(listOf("HAPPY", "SMILE", "SUN", "STAR", "PLAY"))
            }
        }
    }

    val emojiChoices = listOf("⭐", "🦁", "🚀", "🦄", "⚽", "🍕", "🦖", "👑", "🌈", "🎸", "🍦", "🐬", "🚗", "🎨")

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = Color.White,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
                .testTag("custom_theme_dialog")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (initialTheme == null) "Create Theme" else "Edit Theme",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1E293B)
                        )
                    )
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.testTag("close_theme_dialog")
                    ) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Theme Name Input
                OutlinedTextField(
                    value = themeName,
                    onValueChange = {
                        themeName = it
                        errorMessage = null
                    },
                    label = { Text("Theme Name (e.g. My Spelling List)") },
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("theme_name_input"),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Emoji Picker
                Text(
                    text = "Pick an Icon:",
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF475569)
                    )
                )

                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    emojiChoices.forEach { emoji ->
                        val isSelected = emoji == selectedEmoji
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(if (isSelected) Color(0xFFE0E7FF) else Color(0xFFF1F5F9))
                                .border(
                                    width = if (isSelected) 2.dp else 1.dp,
                                    color = if (isSelected) Color(0xFF4F46E5) else Color(0xFFCBD5E1),
                                    shape = CircleShape
                                )
                                .clickable { selectedEmoji = emoji }
                                .testTag("emoji_option_$emoji"),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = emoji, fontSize = 20.sp)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Color Palette Picker
                Text(
                    text = "Pick Theme Colors:",
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF475569)
                    )
                )

                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    WordSearchColors.CustomThemePalettes.forEachIndexed { index, (pCol, sCol) ->
                        val isSelected = index == selectedColorIndex
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape)
                                .background(
                                    Brush.linearGradient(
                                        listOf(Color(pCol), Color(sCol))
                                    )
                                )
                                .border(
                                    width = if (isSelected) 3.dp else 1.dp,
                                    color = if (isSelected) Color(0xFF1E293B) else Color.White,
                                    shape = CircleShape
                                )
                                .clickable { selectedColorIndex = index }
                                .testTag("color_palette_$index")
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Words Manager
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Words (${wordsList.size}):",
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF475569)
                        )
                    )
                    Text(
                        text = "Need at least 4",
                        style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF94A3B8))
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                // Add word row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = newWordInput,
                        onValueChange = {
                            newWordInput = it.uppercase().filter { ch -> ch in 'A'..'Z' }
                            errorMessage = null
                        },
                        placeholder = { Text("Add word...") },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("new_word_input"),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            val word = newWordInput.trim()
                            if (word.length < currentGrade.minWordLen) {
                                errorMessage = "Words must be at least ${currentGrade.minWordLen} letters"
                            } else if (word.length > currentGrade.gridSize) {
                                errorMessage = "Word cannot exceed grid size (${currentGrade.gridSize})"
                            } else if (wordsList.contains(word)) {
                                errorMessage = "Word already added"
                            } else {
                                wordsList.add(word)
                                newWordInput = ""
                                errorMessage = null
                            }
                        },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4F46E5)),
                        modifier = Modifier.testTag("add_word_button")
                    ) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = "Add Word")
                    }
                }

                errorMessage?.let { err ->
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = err,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Word Chips
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 140.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color(0xFFF8FAFC))
                        .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(14.dp))
                        .padding(8.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        wordsList.forEach { word ->
                            Row(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color.White)
                                    .border(1.dp, Color(0xFFCBD5E1), RoundedCornerShape(8.dp))
                                    .padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = word,
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF1E293B)
                                    )
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Remove word",
                                    tint = Color(0xFF94A3B8),
                                    modifier = Modifier
                                        .size(14.dp)
                                        .clickable { wordsList.remove(word) }
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Action Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(
                        onClick = onDismiss,
                        modifier = Modifier.testTag("cancel_custom_theme")
                    ) {
                        Text("Cancel", color = Color(0xFF64748B))
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Button(
                        onClick = {
                            val finalName = themeName.trim().ifEmpty { "My Custom Theme" }
                            if (wordsList.size < 4) {
                                errorMessage = "Please add at least 4 words"
                                return@Button
                            }
                            val palette = WordSearchColors.CustomThemePalettes[selectedColorIndex]
                            val newTheme = ThemeData(
                                id = initialTheme?.id ?: "custom_${UUID.randomUUID()}",
                                name = finalName,
                                emoji = selectedEmoji,
                                description = "Custom theme with ${wordsList.size} words",
                                primaryColorHex = palette.first,
                                secondaryColorHex = palette.second,
                                isCustom = true,
                                customWords = wordsList.toList()
                            )
                            onSave(newTheme)
                        },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4F46E5)),
                        modifier = Modifier.testTag("save_custom_theme_button")
                    ) {
                        Text("Save Theme", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
