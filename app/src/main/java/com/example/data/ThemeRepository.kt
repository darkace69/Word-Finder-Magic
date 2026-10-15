package com.example.data

import android.content.Context
import android.content.SharedPreferences
import com.example.model.ThemeData
import org.json.JSONArray
import org.json.JSONObject

class ThemeRepository(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("word_search_themes", Context.MODE_PRIVATE)

    companion object {
        val DEFAULT_THEMES = listOf(
            ThemeData(
                id = CurriculumWordRepository.THEME_ANIMALS,
                name = "Animal Safari",
                emoji = "🐾",
                description = "Furry, feathered, and wild creatures!",
                primaryColorHex = 0xFF10B981, // Emerald Green
                secondaryColorHex = 0xFF34D399
            ),
            ThemeData(
                id = CurriculumWordRepository.THEME_SPACE,
                name = "Cosmic Galaxy",
                emoji = "🚀",
                description = "Rockets, stars, and distant planets!",
                primaryColorHex = 0xFF6366F1, // Indigo
                secondaryColorHex = 0xFF818CF8
            ),
            ThemeData(
                id = CurriculumWordRepository.THEME_OCEAN,
                name = "Ocean Splash",
                emoji = "🌊",
                description = "Undersea reefs and sea explorers!",
                primaryColorHex = 0xFF0EA5E9, // Sky Blue
                secondaryColorHex = 0xFF38BDF8
            ),
            ThemeData(
                id = CurriculumWordRepository.THEME_MAGIC,
                name = "Magic Kingdom",
                emoji = "🏰",
                description = "Wizards, castles, and fairy spells!",
                primaryColorHex = 0xFF8B5CF6, // Purple
                secondaryColorHex = 0xFFA78BFA
            ),
            ThemeData(
                id = CurriculumWordRepository.THEME_FOOD,
                name = "Yummy Treats",
                emoji = "🍎",
                description = "Delicious snacks, fruits, and meals!",
                primaryColorHex = 0xFFF97316, // Orange
                secondaryColorHex = 0xFFFB923C
            ),
            ThemeData(
                id = CurriculumWordRepository.THEME_SPORTS,
                name = "Super Sports",
                emoji = "⚽",
                description = "Games, goals, champions, and races!",
                primaryColorHex = 0xFFEC4899, // Pink
                secondaryColorHex = 0xFFF472B6
            ),
            ThemeData(
                id = CurriculumWordRepository.THEME_NATURE,
                name = "Wild Nature",
                emoji = "🌿",
                description = "Trees, weather, mountains, and rivers!",
                primaryColorHex = 0xFF84CC16, // Lime
                secondaryColorHex = 0xFFA3E635
            ),
            ThemeData(
                id = CurriculumWordRepository.THEME_SIGHT_WORDS,
                name = "Sight Words",
                emoji = "📚",
                description = "Core reading vocabulary for every grade!",
                primaryColorHex = 0xFF3B82F6, // Blue
                secondaryColorHex = 0xFF60A5FA
            )
        )
    }

    fun getAllThemes(): List<ThemeData> {
        val customThemes = getCustomThemes()
        return DEFAULT_THEMES + customThemes
    }

    fun getThemeById(themeId: String): ThemeData {
        return getAllThemes().firstOrNull { it.id == themeId } ?: DEFAULT_THEMES.first()
    }

    fun getCustomThemes(): List<ThemeData> {
        val rawJson = prefs.getString("custom_themes_json", null) ?: return emptyList()
        val list = mutableListOf<ThemeData>()
        try {
            val jsonArray = JSONArray(rawJson)
            for (i in 0 until jsonArray.length()) {
                val obj = jsonArray.getJSONObject(i)
                val wordsArray = obj.optJSONArray("customWords")
                val wordsList = mutableListOf<String>()
                if (wordsArray != null) {
                    for (j in 0 until wordsArray.length()) {
                        wordsList.add(wordsArray.getString(j))
                    }
                }
                list.add(
                    ThemeData(
                        id = obj.getString("id"),
                        name = obj.getString("name"),
                        emoji = obj.getString("emoji"),
                        description = obj.optString("description", "Custom player theme"),
                        primaryColorHex = obj.optLong("primaryColorHex", 0xFF6366F1),
                        secondaryColorHex = obj.optLong("secondaryColorHex", 0xFF818CF8),
                        isCustom = true,
                        customWords = wordsList
                    )
                )
            }
        } catch (_: Exception) {
            // Return empty if parsing error
        }
        return list
    }

    fun saveCustomTheme(theme: ThemeData) {
        val current = getCustomThemes().toMutableList()
        val index = current.indexOfFirst { it.id == theme.id }
        if (index >= 0) {
            current[index] = theme
        } else {
            current.add(theme)
        }
        persistCustomThemes(current)
    }

    fun deleteCustomTheme(themeId: String) {
        val current = getCustomThemes().filter { it.id != themeId }
        persistCustomThemes(current)
    }

    private fun persistCustomThemes(themes: List<ThemeData>) {
        val jsonArray = JSONArray()
        for (theme in themes) {
            val obj = JSONObject().apply {
                put("id", theme.id)
                put("name", theme.name)
                put("emoji", theme.emoji)
                put("description", theme.description)
                put("primaryColorHex", theme.primaryColorHex)
                put("secondaryColorHex", theme.secondaryColorHex)
                val wordsArray = JSONArray()
                theme.customWords.forEach { wordsArray.put(it) }
                put("customWords", wordsArray)
            }
            jsonArray.put(obj)
        }
        prefs.edit().putString("custom_themes_json", jsonArray.toString()).apply()
    }
}
