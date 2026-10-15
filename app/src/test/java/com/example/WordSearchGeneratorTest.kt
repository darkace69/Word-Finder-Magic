package com.example

import com.example.data.CurriculumWordRepository
import com.example.engine.WordSearchGenerator
import com.example.model.Direction
import com.example.model.GradeLevel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class WordSearchGeneratorTest {

    @Test
    fun testPreKWordSearchGeneration() {
        val words = CurriculumWordRepository.getWords(CurriculumWordRepository.THEME_ANIMALS, GradeLevel.PRE_K)
        val puzzle = WordSearchGenerator.generatePuzzle(
            themeId = CurriculumWordRepository.THEME_ANIMALS,
            words = words,
            gradeLevel = GradeLevel.PRE_K
        )

        assertEquals(5, puzzle.gridSize)
        assertEquals(5, puzzle.grid.size)
        assertEquals(5, puzzle.grid[0].size)
        assertTrue(puzzle.placedWords.isNotEmpty())

        // Verify all placed words in Pre-K are horizontal forward only
        for (placed in puzzle.placedWords) {
            val dRow = placed.end.row - placed.start.row
            val dCol = placed.end.col - placed.start.col
            assertEquals(0, dRow) // Same row
            assertTrue(dCol > 0) // Forward
        }
    }

    @Test
    fun testFifthGradeWordSearchGeneration() {
        val words = CurriculumWordRepository.getWords(CurriculumWordRepository.THEME_SPACE, GradeLevel.GRADE_5)
        val puzzle = WordSearchGenerator.generatePuzzle(
            themeId = CurriculumWordRepository.THEME_SPACE,
            words = words,
            gradeLevel = GradeLevel.GRADE_5
        )

        assertEquals(11, puzzle.gridSize)
        assertEquals(11, puzzle.grid.size)
        assertTrue(puzzle.placedWords.isNotEmpty())

        // Verify placed word characters match grid characters exactly
        for (placed in puzzle.placedWords) {
            val wordCharsFromGrid = placed.cells.map { puzzle.grid[it.row][it.col] }.joinToString("")
            assertEquals(placed.word, wordCharsFromGrid)
        }
    }
}
