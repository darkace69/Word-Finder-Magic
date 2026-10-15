package com.example.ui.theme

import androidx.compose.ui.graphics.Color

object WordSearchColors {
    // Highlighter ribbons for discovered words in the grid
    val HighlightPastels = listOf(
        Color(0xFFFFD166), // Sun Yellow
        Color(0xFF06D6A0), // Emerald Mint
        Color(0xFF118AB2), // Ocean Blue
        Color(0xFFEF476F), // Strawberry Pink
        Color(0xFF8338EC), // Royal Violet
        Color(0xFFFF9F1C), // Tangerine
        Color(0xFF48CAE4), // Sky Turquoise
        Color(0xFFA7C957), // Leaf Green
        Color(0xFFF72585), // Neon Magenta
        Color(0xFF4CC9F0), // Bright Aqua
        Color(0xFFB5179E), // Berry Purple
        Color(0xFF70E000)  // Lime Punch
    )

    val ActiveDragColor = Color(0x66F59E0B) // Golden amber semi-transparent
    val HintHighlightColor = Color(0xFFFFEB3B)

    // Palette choices for Custom Theme Creator
    val CustomThemePalettes = listOf(
        Pair(0xFF3B82F6L, 0xFF60A5FAL), // Ocean Blue
        Pair(0xFF10B981L, 0xFF34D399L), // Forest Green
        Pair(0xFFEC4899L, 0xFFF472B6L), // Bubblegum Pink
        Pair(0xFF8B5CF6L, 0xFFA78BFAL), // Royal Purple
        Pair(0xFFF97316L, 0xFFFB923CL), // Sunset Orange
        Pair(0xFFEAB308L, 0xFFFDE047L), // Sunshine Yellow
        Pair(0xFF06B6D4L, 0xFF22D3EEL), // Cyan Glacier
        Pair(0xFFE11D48L, 0xFFFB7185L)  // Crimson Cherry
    )
}
