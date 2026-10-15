package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.audio.KidsSoundManager
import com.example.data.CurriculumWordRepository
import com.example.data.PreferencesManager
import com.example.data.ThemeRepository
import com.example.engine.WordSearchGenerator
import com.example.model.GameStats
import com.example.model.GradeLevel
import com.example.model.GridPoint
import com.example.model.PlacedWord
import com.example.model.ThemeData
import com.example.model.UserSettings
import com.example.model.WordSearchPuzzle
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

import android.content.Context
import com.example.util.PrintWorksheetHelper

sealed class Screen {
    object Splash : Screen()
    object Home : Screen()
    object Game : Screen()
}

data class GameUiState(
    val currentScreen: Screen = Screen.Splash,
    val selectedGrade: GradeLevel = GradeLevel.KINDERGARTEN,
    val themes: List<ThemeData> = emptyList(),
    val currentTheme: ThemeData? = null,
    val puzzle: WordSearchPuzzle? = null,
    val foundWords: List<PlacedWord> = emptyList(),
    val hintsRemaining: Int = 3,
    val activeHintPoint: GridPoint? = null,
    val elapsedSeconds: Int = 0,
    val isGameCompleted: Boolean = false,
    val starsEarned: Int = 3,
    val stats: GameStats = GameStats(),
    val settings: UserSettings = UserSettings(),
    val puzzlesPlayed: Int = 0,
    val isPremiumUnlocked: Boolean = false,
    val freePuzzlesRemaining: Int = PreferencesManager.MAX_FREE_PUZZLES,
    val showPaywallDialog: Boolean = false,
    val showCustomThemeDialog: Boolean = false,
    val editingTheme: ThemeData? = null,
    val showSettingsDialog: Boolean = false
)

class GameViewModel(application: Application) : AndroidViewModel(application) {

    private val themeRepository = ThemeRepository(application)
    private val preferencesManager = PreferencesManager(application)
    private val soundManager = KidsSoundManager(application)

    private val _uiState = MutableStateFlow(GameUiState())
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    private var timerJob: Job? = null

    init {
        val savedGrade = preferencesManager.getSavedGrade()
        val savedStats = preferencesManager.getStats()
        val savedSettings = preferencesManager.getSettings()
        val allThemes = themeRepository.getAllThemes()
        val played = preferencesManager.getPuzzlesPlayed()
        val isUnlocked = preferencesManager.isPremiumUnlocked()
        val remaining = preferencesManager.getRemainingFreePuzzles()

        _uiState.update {
            it.copy(
                selectedGrade = savedGrade,
                stats = savedStats,
                settings = savedSettings,
                themes = allThemes,
                puzzlesPlayed = played,
                isPremiumUnlocked = isUnlocked,
                freePuzzlesRemaining = remaining
            )
        }
    }

    fun selectGrade(grade: GradeLevel) {
        preferencesManager.saveGrade(grade)
        _uiState.update { it.copy(selectedGrade = grade) }
        // If in game, regenerate puzzle with new grade
        val currentTheme = _uiState.value.currentTheme
        if (_uiState.value.currentScreen is Screen.Game && currentTheme != null) {
            startPuzzle(currentTheme, grade)
        }
    }

    fun startPuzzle(theme: ThemeData, grade: GradeLevel = _uiState.value.selectedGrade) {
        // Enforce paywall after 5 free puzzles
        if (!_uiState.value.isPremiumUnlocked && _uiState.value.puzzlesPlayed >= PreferencesManager.MAX_FREE_PUZZLES) {
            _uiState.update { it.copy(showPaywallDialog = true) }
            return
        }

        timerJob?.cancel()

        if (!_uiState.value.isPremiumUnlocked) {
            preferencesManager.incrementPuzzlesPlayed()
        }
        val newPlayedCount = preferencesManager.getPuzzlesPlayed()
        val newRemaining = preferencesManager.getRemainingFreePuzzles()

        val candidateWords = if (theme.isCustom && theme.customWords.isNotEmpty()) {
            theme.customWords
        } else {
            CurriculumWordRepository.getWords(theme.id, grade)
        }

        val generatedPuzzle = WordSearchGenerator.generatePuzzle(
            themeId = theme.id,
            words = candidateWords,
            gradeLevel = grade
        )

        _uiState.update {
            it.copy(
                currentScreen = Screen.Game,
                currentTheme = theme,
                puzzle = generatedPuzzle,
                foundWords = emptyList(),
                hintsRemaining = 3,
                activeHintPoint = null,
                elapsedSeconds = 0,
                isGameCompleted = false,
                starsEarned = 3,
                puzzlesPlayed = newPlayedCount,
                freePuzzlesRemaining = newRemaining
            )
        }

        startTimer()
    }

    private fun startTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (isActive) {
                delay(1000)
                if (!_uiState.value.isGameCompleted) {
                    _uiState.update { it.copy(elapsedSeconds = it.elapsedSeconds + 1) }
                }
            }
        }
    }

    fun onCellsSelected(cells: List<GridPoint>) {
        val puzzle = _uiState.value.puzzle ?: return
        if (cells.isEmpty() || _uiState.value.isGameCompleted) return

        // Extract word formed forward and backward
        val forwardWord = cells.map { puzzle.grid[it.row][it.col] }.joinToString("")
        val backwardWord = forwardWord.reversed()

        val targetWords = puzzle.targetWords
        val alreadyFound = _uiState.value.foundWords.map { it.word }

        var matchedWord: String? = null
        if (targetWords.contains(forwardWord) && !alreadyFound.contains(forwardWord)) {
            matchedWord = forwardWord
        } else if (targetWords.contains(backwardWord) && !alreadyFound.contains(backwardWord)) {
            matchedWord = backwardWord
        }

        if (matchedWord != null) {
            val originalPlaced = puzzle.placedWords.firstOrNull { it.word == matchedWord }
            val newPlaced = PlacedWord(
                word = matchedWord,
                start = cells.first(),
                end = cells.last(),
                cells = cells,
                colorIndex = originalPlaced?.colorIndex ?: _uiState.value.foundWords.size
            )

            val updatedFound = _uiState.value.foundWords + newPlaced
            val allFound = updatedFound.size >= puzzle.targetWords.size

            // Audio & Haptic chime
            soundManager.playWordFoundTone(_uiState.value.settings.soundEffectsEnabled)
            if (_uiState.value.settings.speechPronunciationEnabled) {
                soundManager.speakWord(matchedWord, true)
            }

            var calculatedStars = 3
            if (_uiState.value.hintsRemaining < 2) calculatedStars = 2
            if (_uiState.value.hintsRemaining == 0) calculatedStars = 1

            _uiState.update {
                it.copy(
                    foundWords = updatedFound,
                    activeHintPoint = null, // clear hint when word found
                    isGameCompleted = allFound,
                    starsEarned = calculatedStars
                )
            }

            if (allFound) {
                timerJob?.cancel()
                preferencesManager.recordCompletedPuzzle(calculatedStars, updatedFound.size)
                _uiState.update { it.copy(stats = preferencesManager.getStats()) }
                soundManager.playVictoryFanfare(_uiState.value.settings.soundEffectsEnabled)
            }
        }
    }

    fun useHint() {
        val state = _uiState.value
        val puzzle = state.puzzle ?: return
        if (state.hintsRemaining <= 0 || state.isGameCompleted) return

        val foundWordStrings = state.foundWords.map { it.word }
        val remainingPlacedWords = puzzle.placedWords.filter { !foundWordStrings.contains(it.word) }

        if (remainingPlacedWords.isNotEmpty()) {
            val nextWordToFind = remainingPlacedWords.first()
            val hintPoint = nextWordToFind.start

            _uiState.update {
                it.copy(
                    hintsRemaining = it.hintsRemaining - 1,
                    activeHintPoint = hintPoint
                )
            }

            if (state.settings.speechPronunciationEnabled) {
                soundManager.speakWord(nextWordToFind.word, true)
            }
        }
    }

    fun speakWord(word: String) {
        soundManager.speakWord(word, _uiState.value.settings.speechPronunciationEnabled)
    }

    fun finishSplash() {
        _uiState.update { it.copy(currentScreen = Screen.Home) }
    }

    fun printCurrentPuzzle(context: Context, includeAnswerKey: Boolean = false) {
        val puzzle = _uiState.value.puzzle ?: return
        val theme = _uiState.value.currentTheme ?: return
        val grade = _uiState.value.selectedGrade
        PrintWorksheetHelper.printPuzzle(context, puzzle, theme, grade, includeAnswerKey)
    }

    fun navigateToHome() {
        timerJob?.cancel()
        _uiState.update { it.copy(currentScreen = Screen.Home, isGameCompleted = false) }
    }

    fun playAgain() {
        val theme = _uiState.value.currentTheme ?: themeRepository.getAllThemes().first()
        startPuzzle(theme, _uiState.value.selectedGrade)
    }

    fun nextTheme() {
        val allThemes = _uiState.value.themes
        val currentIndex = allThemes.indexOfFirst { it.id == _uiState.value.currentTheme?.id }
        val nextTheme = if (currentIndex in 0 until allThemes.size - 1) {
            allThemes[currentIndex + 1]
        } else {
            allThemes.first()
        }
        startPuzzle(nextTheme, _uiState.value.selectedGrade)
    }

    // Custom Theme Management
    fun openCreateCustomThemeDialog(themeToEdit: ThemeData? = null) {
        _uiState.update {
            it.copy(showCustomThemeDialog = true, editingTheme = themeToEdit)
        }
    }

    fun closeCustomThemeDialog() {
        _uiState.update {
            it.copy(showCustomThemeDialog = false, editingTheme = null)
        }
    }

    fun saveCustomTheme(theme: ThemeData) {
        themeRepository.saveCustomTheme(theme)
        val refreshed = themeRepository.getAllThemes()
        _uiState.update {
            it.copy(
                themes = refreshed,
                showCustomThemeDialog = false,
                editingTheme = null
            )
        }
    }

    fun deleteCustomTheme(themeId: String) {
        themeRepository.deleteCustomTheme(themeId)
        val refreshed = themeRepository.getAllThemes()
        _uiState.update { it.copy(themes = refreshed) }
    }

    // Settings
    fun openSettingsDialog() {
        _uiState.update { it.copy(showSettingsDialog = true) }
    }

    fun closeSettingsDialog() {
        _uiState.update { it.copy(showSettingsDialog = false) }
    }

    fun updateSettings(newSettings: UserSettings) {
        preferencesManager.saveSettings(newSettings)
        _uiState.update { it.copy(settings = newSettings) }
    }

    // Paywall Management
    fun openPaywallDialog() {
        _uiState.update { it.copy(showPaywallDialog = true) }
    }

    fun closePaywallDialog() {
        _uiState.update { it.copy(showPaywallDialog = false) }
    }

    fun unlockPremium() {
        preferencesManager.setPremiumUnlocked(true)
        _uiState.update {
            it.copy(
                isPremiumUnlocked = true,
                freePuzzlesRemaining = Int.MAX_VALUE,
                showPaywallDialog = false
            )
        }
        soundManager.playVictoryFanfare(true)
    }

    fun restorePurchases() {
        preferencesManager.setPremiumUnlocked(true)
        _uiState.update {
            it.copy(
                isPremiumUnlocked = true,
                freePuzzlesRemaining = Int.MAX_VALUE,
                showPaywallDialog = false
            )
        }
        soundManager.playWordFoundTone(true)
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
        soundManager.shutdown()
    }
}
