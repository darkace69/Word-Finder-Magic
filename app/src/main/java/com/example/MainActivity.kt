package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.PreferencesManager
import com.example.ui.components.CustomThemeDialog
import com.example.ui.components.PaywallDialog
import com.example.ui.components.SettingsDialog
import com.example.ui.screens.GameScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.SplashScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.viewmodel.GameViewModel
import com.example.viewmodel.Screen

class MainActivity : ComponentActivity() {

    private val viewModel: GameViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    WordFinderMagicApp(viewModel = viewModel)
                }
            }
        }
    }
}

@Composable
fun WordFinderMagicApp(viewModel: GameViewModel) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    when (uiState.currentScreen) {
        is Screen.Splash -> {
            SplashScreen(
                onSplashComplete = { viewModel.finishSplash() }
            )
        }

        is Screen.Home -> {
            HomeScreen(
                selectedGrade = uiState.selectedGrade,
                themes = uiState.themes,
                stats = uiState.stats,
                isPremiumUnlocked = uiState.isPremiumUnlocked,
                puzzlesPlayed = uiState.puzzlesPlayed,
                onSelectGrade = { viewModel.selectGrade(it) },
                onSelectTheme = { viewModel.startPuzzle(it) },
                onQuickPlay = {
                    val defaultTheme = uiState.themes.firstOrNull() ?: return@HomeScreen
                    viewModel.startPuzzle(defaultTheme)
                },
                onCreateCustomTheme = { viewModel.openCreateCustomThemeDialog() },
                onEditCustomTheme = { viewModel.openCreateCustomThemeDialog(it) },
                onDeleteCustomTheme = { viewModel.deleteCustomTheme(it) },
                onOpenSettings = { viewModel.openSettingsDialog() },
                onOpenPaywall = { viewModel.openPaywallDialog() }
            )
        }

        is Screen.Game -> {
            val currentTheme = uiState.currentTheme ?: uiState.themes.first()
            GameScreen(
                theme = currentTheme,
                gradeLevel = uiState.selectedGrade,
                puzzle = uiState.puzzle,
                foundWords = uiState.foundWords,
                hintsRemaining = uiState.hintsRemaining,
                activeHintPoint = uiState.activeHintPoint,
                elapsedSeconds = uiState.elapsedSeconds,
                isCompleted = uiState.isGameCompleted,
                starsEarned = uiState.starsEarned,
                settings = uiState.settings,
                onCellsSelected = { viewModel.onCellsSelected(it) },
                onUseHint = { viewModel.useHint() },
                onSpeakWord = { viewModel.speakWord(it) },
                onPlayAgain = { viewModel.playAgain() },
                onNextLevel = { viewModel.nextTheme() },
                onBack = { viewModel.navigateToHome() },
                onOpenSettings = { viewModel.openSettingsDialog() },
                onPrintPuzzle = { includeAnswerKey ->
                    viewModel.printCurrentPuzzle(context, includeAnswerKey)
                }
            )
        }
    }

    // Custom Theme Creation/Editing Modal
    if (uiState.showCustomThemeDialog) {
        CustomThemeDialog(
            initialTheme = uiState.editingTheme,
            currentGrade = uiState.selectedGrade,
            onDismiss = { viewModel.closeCustomThemeDialog() },
            onSave = { viewModel.saveCustomTheme(it) }
        )
    }

    // Settings Modal
    if (uiState.showSettingsDialog) {
        SettingsDialog(
            settings = uiState.settings,
            onUpdateSettings = { viewModel.updateSettings(it) },
            onDismiss = { viewModel.closeSettingsDialog() }
        )
    }

    // Paywall Modal (Triggered after 5 free puzzles or via VIP upgrade button)
    if (uiState.showPaywallDialog) {
        PaywallDialog(
            puzzlesPlayed = uiState.puzzlesPlayed,
            maxFreePuzzles = PreferencesManager.MAX_FREE_PUZZLES,
            onUnlockUnlimited = { viewModel.unlockPremium() },
            onRestorePurchase = { viewModel.restorePurchases() },
            onDismiss = { viewModel.closePaywallDialog() }
        )
    }
}
