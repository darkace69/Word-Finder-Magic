package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Print
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.model.GradeLevel
import com.example.model.GridPoint
import com.example.model.PlacedWord
import com.example.model.ThemeData
import com.example.model.UserSettings
import com.example.model.WordSearchPuzzle
import com.example.ui.components.CelebrationDialog
import com.example.ui.components.WordBank
import com.example.ui.components.WordSearchGrid

@Composable
fun GameScreen(
    theme: ThemeData,
    gradeLevel: GradeLevel,
    puzzle: WordSearchPuzzle?,
    foundWords: List<PlacedWord>,
    hintsRemaining: Int,
    activeHintPoint: GridPoint?,
    elapsedSeconds: Int,
    isCompleted: Boolean,
    starsEarned: Int,
    settings: UserSettings,
    onCellsSelected: (List<GridPoint>) -> Unit,
    onUseHint: () -> Unit,
    onSpeakWord: (String) -> Unit,
    onPlayAgain: () -> Unit,
    onNextLevel: () -> Unit,
    onBack: () -> Unit,
    onOpenSettings: () -> Unit,
    onPrintPuzzle: (includeAnswerKey: Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    var showPrintDialog by remember { mutableStateOf(false) }

    // Handle Android system back gesture / button
    BackHandler {
        onBack()
    }

    Surface(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC)),
        color = Color(0xFFF8FAFC)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
        ) {
            // Game Top Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Back Button
                IconButton(
                    onClick = onBack,
                    modifier = Modifier.testTag("game_back_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back to Themes",
                        tint = Color(0xFF334155)
                    )
                }

                // Theme & Grade Center Badge
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = theme.emoji, fontSize = 20.sp)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = theme.name,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF0F172A)
                            )
                        )
                    }
                    Text(
                        text = "${gradeLevel.shortName} • ${gradeLevel.gridSize}×${gradeLevel.gridSize}",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = Color(0xFF6366F1),
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                }

                // Action Buttons (Timer, Print, Hint, Refresh, Settings)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (settings.timerEnabled) {
                        val mins = elapsedSeconds / 60
                        val secs = elapsedSeconds % 60
                        val formattedTime = String.format("%02d:%02d", mins, secs)
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color(0xFFE2E8F0))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = formattedTime,
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF334155)
                                )
                            )
                        }
                        Spacer(modifier = Modifier.width(4.dp))
                    }

                    // Print Worksheet Button
                    IconButton(
                        onClick = { showPrintDialog = true },
                        modifier = Modifier.testTag("game_print_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Print,
                            contentDescription = "Print Worksheet",
                            tint = Color(0xFF0F766E)
                        )
                    }

                    // Hint Button
                    IconButton(
                        onClick = onUseHint,
                        enabled = hintsRemaining > 0 && !isCompleted,
                        modifier = Modifier.testTag("game_hint_button")
                    ) {
                        BadgedBox(
                            badge = {
                                Badge(
                                    containerColor = if (hintsRemaining > 0) Color(0xFFF59E0B) else Color.Gray,
                                    contentColor = Color.White
                                ) {
                                    Text(text = "$hintsRemaining")
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Lightbulb,
                                contentDescription = "Hint",
                                tint = if (hintsRemaining > 0) Color(0xFFF59E0B) else Color.LightGray
                            )
                        }
                    }

                    // Refresh / Regenerate Button
                    IconButton(
                        onClick = onPlayAgain,
                        modifier = Modifier.testTag("game_refresh_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "New Puzzle",
                            tint = Color(0xFF64748B)
                        )
                    }

                    // Settings Button
                    IconButton(
                        onClick = onOpenSettings,
                        modifier = Modifier.testTag("game_settings_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Settings",
                            tint = Color(0xFF64748B)
                        )
                    }
                }
            }

            // Scrollable Game Play Area
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(4.dp))

                // Puzzle Grid
                if (puzzle != null) {
                    WordSearchGrid(
                        grid = puzzle.grid,
                        foundWords = foundWords,
                        isUppercase = settings.isUppercase,
                        hintPoint = activeHintPoint,
                        onWordSelected = onCellsSelected,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Print Worksheet Banner Button
                    OutlinedButton(
                        onClick = { showPrintDialog = true },
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp)
                            .testTag("print_worksheet_banner_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Print,
                            contentDescription = "Print",
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Print Paper Worksheet (PDF)",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Word Bank
                    WordBank(
                        targetWords = puzzle.targetWords,
                        foundWords = foundWords,
                        isUppercase = settings.isUppercase,
                        onSpeakWord = onSpeakWord
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }

        // Print Options Dialog
        if (showPrintDialog) {
            PrintOptionsDialog(
                themeName = theme.name,
                gradeName = gradeLevel.title,
                onDismiss = { showPrintDialog = false },
                onConfirmPrint = { includeAnswerKey ->
                    showPrintDialog = false
                    onPrintPuzzle(includeAnswerKey)
                }
            )
        }

        // Celebration Modal
        if (isCompleted) {
            CelebrationDialog(
                themeName = theme.name,
                themeEmoji = theme.emoji,
                wordsFound = foundWords.size,
                starsEarned = starsEarned,
                elapsedSeconds = elapsedSeconds,
                isTimerEnabled = settings.timerEnabled,
                onPlayAgain = onPlayAgain,
                onNextLevel = onNextLevel,
                onHome = onBack
            )
        }
    }
}

@Composable
fun PrintOptionsDialog(
    themeName: String,
    gradeName: String,
    onDismiss: () -> Unit,
    onConfirmPrint: (includeAnswerKey: Boolean) -> Unit
) {
    var includeAnswerKey by remember { mutableStateOf(true) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = Color.White,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
                .testTag("print_options_dialog")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(22.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Print Worksheet 🖨️",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0F172A)
                        )
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "Generate a printable US Letter worksheet for $themeName ($gradeName). Perfect for classrooms, homework, or offline road trips!",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = Color(0xFF64748B)
                    )
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Checkbox for Answer Key
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFF1F5F9))
                        .clickable { includeAnswerKey = !includeAnswerKey }
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = includeAnswerKey,
                        onCheckedChange = { includeAnswerKey = it },
                        colors = CheckboxDefaults.colors(checkedColor = Color(0xFF0F766E))
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = "Include Teacher Answer Key",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF1E293B)
                            )
                        )
                        Text(
                            text = "Adds Page 2 with highlighted solutions",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = Color(0xFF64748B)
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancel", color = Color(0xFF64748B))
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = { onConfirmPrint(includeAnswerKey) },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0F766E)),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.testTag("confirm_print_button")
                    ) {
                        Icon(imageVector = Icons.Default.Print, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Print / Save PDF", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
