package com.example.engine

import com.example.model.Direction
import com.example.model.GradeLevel
import com.example.model.GridPoint
import com.example.model.PlacedWord
import com.example.model.WordSearchPuzzle
import kotlin.random.Random

object WordSearchGenerator {

    fun generatePuzzle(
        themeId: String,
        words: List<String>,
        gradeLevel: GradeLevel,
        random: Random = Random
    ): WordSearchPuzzle {
        val size = gradeLevel.gridSize
        val targetCount = gradeLevel.defaultWordCount

        // Clean & prepare words: uppercase, only letters, lengths within grade bounds
        val cleanedWords = words
            .map { it.uppercase().filter { char -> char in 'A'..'Z' } }
            .filter { it.length in gradeLevel.minWordLen..minOf(size, gradeLevel.maxWordLen) }
            .distinct()
            .shuffled(random)
            .take(targetCount)
            .sortedByDescending { it.length }

        val grid = Array(size) { CharArray(size) { ' ' } }
        val placedWords = mutableListOf<PlacedWord>()

        val directions = gradeLevel.allowedDirections.toList()

        for (word in cleanedWords) {
            val placed = tryPlaceWord(grid, size, word, directions, random, placedWords.size)
            if (placed != null) {
                placedWords.add(placed)
            }
        }

        // Fill remaining empty cells
        val alphabetLetters = if (gradeLevel == GradeLevel.PRE_K || gradeLevel == GradeLevel.KINDERGARTEN) {
            // Pick predominantly from letters of placed words or common child-friendly letters
            val wordLetters = placedWords.flatMap { it.word.toList() }.distinct()
            if (wordLetters.isNotEmpty()) wordLetters else ('A'..'Z').toList()
        } else {
            ('A'..'Z').toList()
        }

        for (r in 0 until size) {
            for (c in 0 until size) {
                if (grid[r][c] == ' ') {
                    grid[r][c] = alphabetLetters[random.nextInt(alphabetLetters.size)]
                }
            }
        }

        val gridList = grid.map { it.toList() }

        return WordSearchPuzzle(
            gridSize = size,
            grid = gridList,
            placedWords = placedWords,
            targetWords = placedWords.map { it.word },
            gradeLevel = gradeLevel,
            themeId = themeId
        )
    }

    private fun tryPlaceWord(
        grid: Array<CharArray>,
        size: Int,
        word: String,
        allowedDirections: List<Direction>,
        random: Random,
        colorIndex: Int
    ): PlacedWord? {
        val maxAttempts = 120
        var attempts = 0

        val shuffledDirs = allowedDirections.shuffled(random)

        while (attempts < maxAttempts) {
            attempts++
            val dir = shuffledDirs[attempts % shuffledDirs.size]
            val wordLen = word.length

            // Compute valid row and col range
            val minR = if (dir.dRow < 0) wordLen - 1 else 0
            val maxR = if (dir.dRow > 0) size - wordLen else size - 1

            val minC = if (dir.dCol < 0) wordLen - 1 else 0
            val maxC = if (dir.dCol > 0) size - wordLen else size - 1

            if (minR > maxR || minC > maxC) continue

            val r = random.nextInt(minR, maxR + 1)
            val c = random.nextInt(minC, maxC + 1)

            // Verify if word fits without conflicting with already placed characters
            var canPlace = true
            val cells = mutableListOf<GridPoint>()

            for (i in 0 until wordLen) {
                val currR = r + i * dir.dRow
                val currC = c + i * dir.dCol
                val existingChar = grid[currR][currC]

                if (existingChar != ' ' && existingChar != word[i]) {
                    canPlace = false
                    break
                }
                cells.add(GridPoint(currR, currC))
            }

            if (canPlace) {
                // Place word into grid
                for (i in 0 until wordLen) {
                    val currR = r + i * dir.dRow
                    val currC = c + i * dir.dCol
                    grid[currR][currC] = word[i]
                }

                val endR = r + (wordLen - 1) * dir.dRow
                val endC = c + (wordLen - 1) * dir.dCol

                return PlacedWord(
                    word = word,
                    start = GridPoint(r, c),
                    end = GridPoint(endR, endC),
                    cells = cells,
                    colorIndex = colorIndex
                )
            }
        }

        return null
    }
}
