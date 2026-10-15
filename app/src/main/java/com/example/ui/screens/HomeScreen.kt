package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.CurriculumWordRepository
import com.example.model.GameStats
import com.example.model.GradeLevel
import com.example.model.ThemeData
import com.example.ui.components.CreateThemeCard
import com.example.ui.components.GradeSelector
import com.example.ui.components.ThemeCard

@Composable
fun HomeScreen(
    selectedGrade: GradeLevel,
    themes: List<ThemeData>,
    stats: GameStats,
    isPremiumUnlocked: Boolean,
    puzzlesPlayed: Int,
    onSelectGrade: (GradeLevel) -> Unit,
    onSelectTheme: (ThemeData) -> Unit,
    onQuickPlay: () -> Unit,
    onCreateCustomTheme: () -> Unit,
    onEditCustomTheme: (ThemeData) -> Unit,
    onDeleteCustomTheme: (String) -> Unit,
    onOpenSettings: () -> Unit,
    onOpenPaywall: () -> Unit,
    modifier: Modifier = Modifier
) {
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
            // Header Top Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFF6F0DF)),
                        contentAlignment = Alignment.Center
                    ) {
                        androidx.compose.foundation.Image(
                            painter = androidx.compose.ui.res.painterResource(id = com.example.R.drawable.splash_logo),
                            contentDescription = "Word Finder Magic Logo",
                            modifier = Modifier.size(42.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            text = "Word Finder Magic",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Black,
                                color = Color(0xFF0F172A)
                            )
                        )
                        Text(
                            text = "Kids Word Search",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = Color(0xFF64748B),
                                fontWeight = FontWeight.Medium
                            )
                        )
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Paywall Status Badge (VIP vs Free Count)
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(14.dp))
                            .background(if (isPremiumUnlocked) Color(0xFFD1FAE5) else Color(0xFFEFF6FF))
                            .clickable { onOpenPaywall() }
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                            .testTag("home_paywall_badge"),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (isPremiumUnlocked) "👑 VIP" else "⭐ Free $puzzlesPlayed/5",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = if (isPremiumUnlocked) Color(0xFF065F46) else Color(0xFF1E40AF)
                            )
                        )
                    }

                    Spacer(modifier = Modifier.width(6.dp))

                    // Star Counter Badge
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(14.dp))
                            .background(Color(0xFFFEF3C7))
                            .padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "Stars",
                            tint = Color(0xFFF59E0B),
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${stats.totalStars}",
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFB45309)
                            )
                        )
                    }

                    Spacer(modifier = Modifier.width(6.dp))

                    // Settings Button
                    IconButton(
                        onClick = onOpenSettings,
                        modifier = Modifier.testTag("home_settings_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Settings",
                            tint = Color(0xFF475569)
                        )
                    }
                }
            }

            // Grid Content
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 160.dp),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .testTag("home_themes_list")
            ) {
                // Grade Selector Section
                item(span = { GridItemSpan(maxLineSpan) }) {
                    GradeSelector(
                        selectedGrade = selectedGrade,
                        onGradeSelected = onSelectGrade,
                        modifier = Modifier.padding(bottom = 6.dp)
                    )
                }

                // Quick Play Banner
                item(span = { GridItemSpan(maxLineSpan) }) {
                    Button(
                        onClick = onQuickPlay,
                        shape = RoundedCornerShape(20.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF4F46E5)
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .shadow(4.dp, RoundedCornerShape(20.dp))
                            .testTag("quick_play_button")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.PlayArrow,
                                contentDescription = "Play",
                                modifier = Modifier.size(26.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Quick Play (${selectedGrade.shortName})",
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                if (!isPremiumUnlocked) {
                    item(span = { GridItemSpan(maxLineSpan) }) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color(0xFFF0FDF4))
                                .border(1.5.dp, Color(0xFFBBF7D0), RoundedCornerShape(16.dp))
                                .clickable { onOpenPaywall() }
                                .padding(horizontal = 16.dp, vertical = 12.dp)
                                .testTag("home_unlock_banner"),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(text = "👑", fontSize = 24.sp)
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = if (puzzlesPlayed >= 5) "5/5 Free Puzzles Used" else "Free Pass: ${5 - puzzlesPlayed} left",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp,
                                        color = Color(0xFF166534)
                                    )
                                    Text(
                                        text = "Unlock infinite puzzles, printables & themes",
                                        fontSize = 12.sp,
                                        color = Color(0xFF15803D)
                                    )
                                }
                            }

                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color(0xFF166534))
                                    .padding(horizontal = 10.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = "Upgrade",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }

                // Themes Header Label
                item(span = { GridItemSpan(maxLineSpan) }) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp, bottom = 2.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Choose a Theme",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF1E293B)
                            )
                        )
                        Text(
                            text = "${themes.size} Themes",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = Color(0xFF64748B)
                            )
                        )
                    }
                }

                // Theme Cards
                items(themes) { theme ->
                    val wordCount = if (theme.isCustom && theme.customWords.isNotEmpty()) {
                        theme.customWords.size
                    } else {
                        CurriculumWordRepository.getWords(theme.id, selectedGrade).size
                    }

                    ThemeCard(
                        theme = theme,
                        wordCount = wordCount,
                        onClick = { onSelectTheme(theme) },
                        onEdit = if (theme.isCustom) { { onEditCustomTheme(theme) } } else null,
                        onDelete = if (theme.isCustom) { { onDeleteCustomTheme(theme.id) } } else null
                    )
                }

                // Create Custom Theme Card
                item {
                    CreateThemeCard(onClick = onCreateCustomTheme)
                }
            }
        }
    }
}
