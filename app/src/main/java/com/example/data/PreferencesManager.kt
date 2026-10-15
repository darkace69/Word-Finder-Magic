package com.example.data

import android.content.Context
import android.content.SharedPreferences
import com.example.model.GameStats
import com.example.model.GradeLevel
import com.example.model.UserSettings

class PreferencesManager(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("word_search_preferences", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_PUZZLES_SOLVED = "puzzles_solved"
        private const val KEY_TOTAL_STARS = "total_stars"
        private const val KEY_WORDS_FOUND = "words_found"
        private const val KEY_SELECTED_GRADE = "selected_grade"
        private const val KEY_IS_UPPERCASE = "is_uppercase"
        private const val KEY_SOUND_ENABLED = "sound_enabled"
        private const val KEY_SPEECH_ENABLED = "speech_enabled"
        private const val KEY_TIMER_ENABLED = "timer_enabled"
        private const val KEY_HINTS_ENABLED = "hints_enabled"
        private const val KEY_PUZZLES_PLAYED = "puzzles_played"
        private const val KEY_IS_PREMIUM_UNLOCKED = "is_premium_unlocked"
        const val MAX_FREE_PUZZLES = 5
    }

    fun getPuzzlesPlayed(): Int = prefs.getInt(KEY_PUZZLES_PLAYED, 0)

    fun incrementPuzzlesPlayed() {
        prefs.edit().putInt(KEY_PUZZLES_PLAYED, getPuzzlesPlayed() + 1).apply()
    }

    fun isPremiumUnlocked(): Boolean = prefs.getBoolean(KEY_IS_PREMIUM_UNLOCKED, false)

    fun setPremiumUnlocked(unlocked: Boolean) {
        prefs.edit().putBoolean(KEY_IS_PREMIUM_UNLOCKED, unlocked).apply()
    }

    fun getRemainingFreePuzzles(): Int {
        if (isPremiumUnlocked()) return Int.MAX_VALUE
        return maxOf(0, MAX_FREE_PUZZLES - getPuzzlesPlayed())
    }

    fun getStats(): GameStats {
        return GameStats(
            puzzlesSolved = prefs.getInt(KEY_PUZZLES_SOLVED, 0),
            totalStars = prefs.getInt(KEY_TOTAL_STARS, 0),
            wordsFoundCount = prefs.getInt(KEY_WORDS_FOUND, 0)
        )
    }

    fun recordCompletedPuzzle(starsEarned: Int, wordsFoundInPuzzle: Int) {
        val currentStats = getStats()
        prefs.edit()
            .putInt(KEY_PUZZLES_SOLVED, currentStats.puzzlesSolved + 1)
            .putInt(KEY_TOTAL_STARS, currentStats.totalStars + starsEarned)
            .putInt(KEY_WORDS_FOUND, currentStats.wordsFoundCount + wordsFoundInPuzzle)
            .apply()
    }

    fun getSavedGrade(): GradeLevel {
        val gradeName = prefs.getString(KEY_SELECTED_GRADE, GradeLevel.KINDERGARTEN.name)
        return try {
            GradeLevel.valueOf(gradeName ?: GradeLevel.KINDERGARTEN.name)
        } catch (_: Exception) {
            GradeLevel.KINDERGARTEN
        }
    }

    fun saveGrade(grade: GradeLevel) {
        prefs.edit().putString(KEY_SELECTED_GRADE, grade.name).apply()
    }

    fun getSettings(): UserSettings {
        return UserSettings(
            isUppercase = prefs.getBoolean(KEY_IS_UPPERCASE, true),
            soundEffectsEnabled = prefs.getBoolean(KEY_SOUND_ENABLED, true),
            speechPronunciationEnabled = prefs.getBoolean(KEY_SPEECH_ENABLED, true),
            timerEnabled = prefs.getBoolean(KEY_TIMER_ENABLED, false),
            showFirstLetterHints = prefs.getBoolean(KEY_HINTS_ENABLED, true)
        )
    }

    fun saveSettings(settings: UserSettings) {
        prefs.edit()
            .putBoolean(KEY_IS_UPPERCASE, settings.isUppercase)
            .putBoolean(KEY_SOUND_ENABLED, settings.soundEffectsEnabled)
            .putBoolean(KEY_SPEECH_ENABLED, settings.speechPronunciationEnabled)
            .putBoolean(KEY_TIMER_ENABLED, settings.timerEnabled)
            .putBoolean(KEY_HINTS_ENABLED, settings.showFirstLetterHints)
            .apply()
    }
}
