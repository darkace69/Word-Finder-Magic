package com.example.model

enum class GradeLevel(
    val title: String,
    val shortName: String,
    val ageRange: String,
    val emoji: String,
    val gridSize: Int,
    val defaultWordCount: Int,
    val minWordLen: Int,
    val maxWordLen: Int,
    val allowedDirections: Set<Direction>,
    val uppercaseByDefault: Boolean
) {
    PRE_K(
        title = "Pre-Kindergarten",
        shortName = "Pre-K",
        ageRange = "Ages 3-4",
        emoji = "🐣",
        gridSize = 5,
        defaultWordCount = 4,
        minWordLen = 2,
        maxWordLen = 4,
        allowedDirections = setOf(Direction.HORIZONTAL_FORWARD),
        uppercaseByDefault = true
    ),
    KINDERGARTEN(
        title = "Kindergarten",
        shortName = "Kinder",
        ageRange = "Ages 5-6",
        emoji = "🌱",
        gridSize = 6,
        defaultWordCount = 5,
        minWordLen = 3,
        maxWordLen = 5,
        allowedDirections = setOf(Direction.HORIZONTAL_FORWARD, Direction.VERTICAL_DOWN),
        uppercaseByDefault = true
    ),
    GRADE_1(
        title = "1st Grade",
        shortName = "1st",
        ageRange = "Ages 6-7",
        emoji = "🎈",
        gridSize = 7,
        defaultWordCount = 6,
        minWordLen = 3,
        maxWordLen = 6,
        allowedDirections = setOf(Direction.HORIZONTAL_FORWARD, Direction.VERTICAL_DOWN),
        uppercaseByDefault = true
    ),
    GRADE_2(
        title = "2nd Grade",
        shortName = "2nd",
        ageRange = "Ages 7-8",
        emoji = "🚀",
        gridSize = 8,
        defaultWordCount = 7,
        minWordLen = 4,
        maxWordLen = 7,
        allowedDirections = setOf(
            Direction.HORIZONTAL_FORWARD,
            Direction.VERTICAL_DOWN,
            Direction.DIAGONAL_DOWN_RIGHT
        ),
        uppercaseByDefault = true
    ),
    GRADE_3(
        title = "3rd Grade",
        shortName = "3rd",
        ageRange = "Ages 8-9",
        emoji = "🌟",
        gridSize = 9,
        defaultWordCount = 8,
        minWordLen = 4,
        maxWordLen = 8,
        allowedDirections = setOf(
            Direction.HORIZONTAL_FORWARD,
            Direction.VERTICAL_DOWN,
            Direction.DIAGONAL_DOWN_RIGHT,
            Direction.DIAGONAL_DOWN_LEFT
        ),
        uppercaseByDefault = false
    ),
    GRADE_4(
        title = "4th Grade",
        shortName = "4th",
        ageRange = "Ages 9-10",
        emoji = "⚡",
        gridSize = 10,
        defaultWordCount = 9,
        minWordLen = 4,
        maxWordLen = 9,
        allowedDirections = setOf(
            Direction.HORIZONTAL_FORWARD,
            Direction.HORIZONTAL_BACKWARD,
            Direction.VERTICAL_DOWN,
            Direction.VERTICAL_UP,
            Direction.DIAGONAL_DOWN_RIGHT,
            Direction.DIAGONAL_DOWN_LEFT
        ),
        uppercaseByDefault = false
    ),
    GRADE_5(
        title = "5th Grade",
        shortName = "5th",
        ageRange = "Ages 10-11",
        emoji = "👑",
        gridSize = 11,
        defaultWordCount = 10,
        minWordLen = 5,
        maxWordLen = 10,
        allowedDirections = Direction.values().toSet(),
        uppercaseByDefault = false
    )
}

enum class Direction(val dRow: Int, val dCol: Int) {
    HORIZONTAL_FORWARD(0, 1),
    HORIZONTAL_BACKWARD(0, -1),
    VERTICAL_DOWN(1, 0),
    VERTICAL_UP(-1, 0),
    DIAGONAL_DOWN_RIGHT(1, 1),
    DIAGONAL_DOWN_LEFT(1, -1),
    DIAGONAL_UP_RIGHT(-1, 1),
    DIAGONAL_UP_LEFT(-1, -1)
}
