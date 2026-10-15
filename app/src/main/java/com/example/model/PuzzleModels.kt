package com.example.model

data class GridPoint(
    val row: Int,
    val col: Int
)

data class PlacedWord(
    val word: String,
    val start: GridPoint,
    val end: GridPoint,
    val cells: List<GridPoint>,
    val colorIndex: Int = 0
)

data class WordSearchPuzzle(
    val gridSize: Int,
    val grid: List<List<Char>>,
    val placedWords: List<PlacedWord>,
    val targetWords: List<String>,
    val gradeLevel: GradeLevel,
    val themeId: String
)

data class ThemeData(
    val id: String,
    val name: String,
    val emoji: String,
    val description: String,
    val primaryColorHex: Long,
    val secondaryColorHex: Long,
    val isCustom: Boolean = false,
    val customWords: List<String> = emptyList()
)

data class UserSettings(
    val isUppercase: Boolean = true,
    val soundEffectsEnabled: Boolean = true,
    val speechPronunciationEnabled: Boolean = true,
    val timerEnabled: Boolean = false,
    val showFirstLetterHints: Boolean = true
)

data class GameStats(
    val puzzlesSolved: Int = 0,
    val totalStars: Int = 0,
    val wordsFoundCount: Int = 0
)
