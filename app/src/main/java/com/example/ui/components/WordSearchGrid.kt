package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.GridPoint
import com.example.model.PlacedWord
import com.example.ui.theme.WordSearchColors
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

@Composable
fun WordSearchGrid(
    grid: List<List<Char>>,
    foundWords: List<PlacedWord>,
    isUppercase: Boolean,
    hintPoint: GridPoint?,
    onWordSelected: (List<GridPoint>) -> Unit,
    modifier: Modifier = Modifier
) {
    val size = grid.size
    if (size == 0) return

    var dragStartPoint by remember { mutableStateOf<GridPoint?>(null) }
    var currentEndPoint by remember { mutableStateOf<GridPoint?>(null) }

    // Pulsing animation for hints
    val infiniteTransition = rememberInfiniteTransition(label = "hint_pulse")
    val hintPulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.35f,
        targetValue = 0.95f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "hint_pulse_alpha"
    )

    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .shadow(6.dp, RoundedCornerShape(24.dp))
            .clip(RoundedCornerShape(24.dp))
            .background(Color.White)
            .testTag("word_search_grid"),
        contentAlignment = Alignment.Center
    ) {
        val totalDimension = min(maxWidth.value, maxHeight.value)
        val cellSide = totalDimension / size

        val gridLetterColor = MaterialTheme.colorScheme.onSurface.toArgb()

        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(grid, size) {
                    detectDragGestures(
                        onDragStart = { offset ->
                            val cellW = this.size.width.toFloat() / size
                            val cellH = this.size.height.toFloat() / size
                            val c = (offset.x / cellW).toInt().coerceIn(0, size - 1)
                            val r = (offset.y / cellH).toInt().coerceIn(0, size - 1)
                            val startPt = GridPoint(r, c)
                            dragStartPoint = startPt
                            currentEndPoint = startPt
                        },
                        onDrag = { change, _ ->
                            change.consume()
                            val cellW = this.size.width.toFloat() / size
                            val cellH = this.size.height.toFloat() / size
                            val currC = (change.position.x / cellW).toInt().coerceIn(0, size - 1)
                            val currR = (change.position.y / cellH).toInt().coerceIn(0, size - 1)

                            dragStartPoint?.let { start ->
                                currentEndPoint = snapToStraightLine(start, GridPoint(currR, currC))
                            }
                        },
                        onDragEnd = {
                            val start = dragStartPoint
                            val end = currentEndPoint
                            if (start != null && end != null) {
                                val selectedCells = computeLinePoints(start, end)
                                onWordSelected(selectedCells)
                            }
                            dragStartPoint = null
                            currentEndPoint = null
                        },
                        onDragCancel = {
                            dragStartPoint = null
                            currentEndPoint = null
                        }
                    )
                }
        ) {
            val cellW = this.size.width / size
            val cellH = this.size.height / size

            // 1. Draw subtle grid background lines
            for (i in 1 until size) {
                drawLine(
                    color = Color(0xFFF1F5F9),
                    start = Offset(i * cellW, 0f),
                    end = Offset(i * cellW, this.size.height),
                    strokeWidth = 1.5f
                )
                drawLine(
                    color = Color(0xFFF1F5F9),
                    start = Offset(0f, i * cellH),
                    end = Offset(this.size.width, i * cellH),
                    strokeWidth = 1.5f
                )
            }

            // 2. Draw Hint if active
            hintPoint?.let { hint ->
                val center = Offset((hint.col + 0.5f) * cellW, (hint.row + 0.5f) * cellH)
                val radius = min(cellW, cellH) * 0.42f
                drawCircle(
                    color = WordSearchColors.HintHighlightColor.copy(alpha = hintPulseAlpha),
                    radius = radius,
                    center = center
                )
                drawCircle(
                    color = Color(0xFFF59E0B),
                    radius = radius,
                    center = center,
                    style = Stroke(width = 3.dp.toPx())
                )
            }

            // 3. Draw found words highlights
            for (placed in foundWords) {
                val color = WordSearchColors.HighlightPastels[placed.colorIndex % WordSearchColors.HighlightPastels.size]
                drawWordCapsule(
                    cells = placed.cells,
                    cellW = cellW,
                    cellH = cellH,
                    fillColor = color.copy(alpha = 0.45f),
                    strokeColor = color.copy(alpha = 0.9f)
                )
            }

            // 4. Draw active dragging highlight
            val start = dragStartPoint
            val end = currentEndPoint
            if (start != null && end != null) {
                val activeCells = computeLinePoints(start, end)
                if (activeCells.isNotEmpty()) {
                    drawWordCapsule(
                        cells = activeCells,
                        cellW = cellW,
                        cellH = cellH,
                        fillColor = WordSearchColors.ActiveDragColor,
                        strokeColor = Color(0xFFF59E0B)
                    )
                }
            }

            // 5. Draw Letters
            val androidPaint = android.graphics.Paint().apply {
                this.color = gridLetterColor
                this.textAlign = android.graphics.Paint.Align.CENTER
                this.isFakeBoldText = true
                this.textSize = min(cellW, cellH) * 0.54f
                this.isAntiAlias = true
            }

            val fontMetrics = androidPaint.fontMetrics
            val baselineOffset = (fontMetrics.descent - fontMetrics.ascent) / 2 - fontMetrics.descent

            for (r in 0 until size) {
                for (c in 0 until size) {
                    val rawChar = grid[r][c]
                    val displayChar = if (isUppercase) rawChar.uppercaseChar() else rawChar.lowercaseChar()
                    val cx = (c + 0.5f) * cellW
                    val cy = (r + 0.5f) * cellH + baselineOffset

                    drawContext.canvas.nativeCanvas.drawText(
                        displayChar.toString(),
                        cx,
                        cy,
                        androidPaint
                    )
                }
            }
        }
    }
}

private fun sizePx(gridSize: Int, totalDimension: Float): Float = totalDimension / gridSize

private fun DrawScope.drawWordCapsule(
    cells: List<GridPoint>,
    cellW: Float,
    cellH: Float,
    fillColor: Color,
    strokeColor: Color
) {
    if (cells.isEmpty()) return
    val strokeWidth = min(cellW, cellH) * 0.82f

    if (cells.size == 1) {
        val pt = cells.first()
        val center = Offset((pt.col + 0.5f) * cellW, (pt.row + 0.5f) * cellH)
        drawCircle(color = fillColor, radius = strokeWidth / 2, center = center)
        drawCircle(color = strokeColor, radius = strokeWidth / 2, center = center, style = Stroke(width = 2.dp.toPx()))
        return
    }

    val startPt = cells.first()
    val endPt = cells.last()
    val startOffset = Offset((startPt.col + 0.5f) * cellW, (startPt.row + 0.5f) * cellH)
    val endOffset = Offset((endPt.col + 0.5f) * cellW, (endPt.row + 0.5f) * cellH)

    drawLine(
        color = fillColor,
        start = startOffset,
        end = endOffset,
        strokeWidth = strokeWidth,
        cap = StrokeCap.Round
    )

    drawLine(
        color = strokeColor,
        start = startOffset,
        end = endOffset,
        strokeWidth = strokeWidth,
        cap = StrokeCap.Round,
        alpha = 0.3f
    )
}

/**
 * Snaps touch end point to valid straight line (horizontal, vertical, or 45-degree diagonal)
 */
fun snapToStraightLine(start: GridPoint, target: GridPoint): GridPoint {
    val dRow = target.row - start.row
    val dCol = target.col - start.col

    val absRow = abs(dRow)
    val absCol = abs(dCol)

    if (absRow == 0 && absCol == 0) return start

    // Snap to pure horizontal
    if (absRow == 0 || (absCol > 2 * absRow)) {
        return GridPoint(start.row, target.col)
    }

    // Snap to pure vertical
    if (absCol == 0 || (absRow > 2 * absCol)) {
        return GridPoint(target.row, start.col)
    }

    // Snap to 45 degree diagonal
    val step = min(absRow, absCol)
    val signR = if (dRow > 0) 1 else -1
    val signC = if (dCol > 0) 1 else -1
    return GridPoint(start.row + step * signR, start.col + step * signC)
}

/**
 * Computes all GridPoints between start and end inclusive
 */
fun computeLinePoints(start: GridPoint, end: GridPoint): List<GridPoint> {
    val dRow = end.row - start.row
    val dCol = end.col - start.col

    val stepCount = max(abs(dRow), abs(dCol))
    if (stepCount == 0) return listOf(start)

    val stepR = dRow.toDouble() / stepCount
    val stepC = dCol.toDouble() / stepCount

    val points = mutableListOf<GridPoint>()
    for (i in 0..stepCount) {
        val r = (start.row + i * stepR).roundToInt()
        val c = (start.col + i * stepC).roundToInt()
        points.add(GridPoint(r, c))
    }
    return points
}
