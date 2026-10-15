package com.example.util

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.pdf.PdfDocument
import android.os.Bundle
import android.os.CancellationSignal
import android.os.ParcelFileDescriptor
import android.print.PageRange
import android.print.PrintAttributes
import android.print.PrintDocumentAdapter
import android.print.PrintDocumentInfo
import android.print.PrintManager
import com.example.model.GradeLevel
import com.example.model.ThemeData
import com.example.model.WordSearchPuzzle
import java.io.FileOutputStream
import java.io.IOException

object PrintWorksheetHelper {

    fun printPuzzle(
        context: Context,
        puzzle: WordSearchPuzzle,
        theme: ThemeData,
        gradeLevel: GradeLevel,
        includeAnswerKey: Boolean = false
    ) {
        val printManager = context.getSystemService(Context.PRINT_SERVICE) as? PrintManager ?: return
        val jobName = "Word_Finder_Magic_${theme.name.replace(" ", "_")}"

        printManager.print(
            jobName,
            object : PrintDocumentAdapter() {
                private var pdfDocument: PdfDocument? = null

                override fun onLayout(
                    oldAttributes: PrintAttributes?,
                    newAttributes: PrintAttributes?,
                    cancellationSignal: CancellationSignal?,
                    callback: LayoutResultCallback?,
                    extras: Bundle?
                ) {
                    if (cancellationSignal?.isCanceled == true) {
                        callback?.onLayoutCancelled()
                        return
                    }

                    val pageCount = if (includeAnswerKey) 2 else 1
                    val info = PrintDocumentInfo.Builder("$jobName.pdf")
                        .setContentType(PrintDocumentInfo.CONTENT_TYPE_DOCUMENT)
                        .setPageCount(pageCount)
                        .build()

                    callback?.onLayoutFinished(info, true)
                }

                override fun onWrite(
                    pages: Array<out PageRange>?,
                    destination: ParcelFileDescriptor?,
                    cancellationSignal: CancellationSignal?,
                    callback: WriteResultCallback?
                ) {
                    pdfDocument = PdfDocument()

                    try {
                        // Page 1: Student Puzzle Worksheet
                        drawStudentPage(pdfDocument!!, 1, puzzle, theme, gradeLevel)

                        // Page 2: Answer Key (if requested)
                        if (includeAnswerKey) {
                            drawAnswerKeyPage(pdfDocument!!, 2, puzzle, theme, gradeLevel)
                        }

                        val outputStream = FileOutputStream(destination?.fileDescriptor)
                        pdfDocument?.writeTo(outputStream)
                        outputStream.close()

                        callback?.onWriteFinished(arrayOf(PageRange.ALL_PAGES))
                    } catch (e: IOException) {
                        callback?.onWriteFailed(e.message)
                    } finally {
                        pdfDocument?.close()
                        pdfDocument = null
                    }
                }
            },
            null
        )
    }

    private fun drawStudentPage(
        pdfDocument: PdfDocument,
        pageNum: Int,
        puzzle: WordSearchPuzzle,
        theme: ThemeData,
        gradeLevel: GradeLevel
    ) {
        // Standard US Letter dimensions (612 x 792 points at 72 dpi)
        val pageInfo = PdfDocument.PageInfo.Builder(612, 792, pageNum).create()
        val page = pdfDocument.startPage(pageInfo)
        val canvas = page.canvas

        val titlePaint = Paint().apply {
            color = Color.BLACK
            textSize = 26f
            isFakeBoldText = true
            textAlign = Paint.Align.CENTER
            isAntiAlias = true
        }

        val subtitlePaint = Paint().apply {
            color = Color.DKGRAY
            textSize = 14f
            textAlign = Paint.Align.CENTER
            isAntiAlias = true
        }

        val textPaint = Paint().apply {
            color = Color.BLACK
            textSize = 12f
            isAntiAlias = true
        }

        val boldPaint = Paint().apply {
            color = Color.BLACK
            textSize = 13f
            isFakeBoldText = true
            isAntiAlias = true
        }

        val borderPaint = Paint().apply {
            color = Color.BLACK
            style = Paint.Style.STROKE
            strokeWidth = 1.5f
            isAntiAlias = true
        }

        val gridLinePaint = Paint().apply {
            color = Color.LTGRAY
            style = Paint.Style.STROKE
            strokeWidth = 1f
            isAntiAlias = true
        }

        // Header
        canvas.drawText("WORD FINDER MAGIC", 306f, 48f, titlePaint)
        canvas.drawText("${theme.name} • ${gradeLevel.title} (${gradeLevel.gridSize}×${gradeLevel.gridSize})", 306f, 68f, subtitlePaint)

        // Student Info Lines
        canvas.drawText("Name: ___________________________________", 50f, 96f, textPaint)
        canvas.drawText("Date: _________________", 430f, 96f, textPaint)

        // Draw Puzzle Grid
        val gridSize = puzzle.gridSize
        val gridPixelSize = 340f
        val left = (612f - gridPixelSize) / 2
        val top = 115f
        val cellSize = gridPixelSize / gridSize

        // Outer border
        canvas.drawRect(left, top, left + gridPixelSize, top + gridPixelSize, borderPaint)

        // Inner grid lines
        for (i in 1 until gridSize) {
            val x = left + i * cellSize
            val y = top + i * cellSize
            canvas.drawLine(x, top, x, top + gridPixelSize, gridLinePaint)
            canvas.drawLine(left, y, left + gridPixelSize, y, gridLinePaint)
        }

        // Letters inside cells
        val letterPaint = Paint().apply {
            color = Color.BLACK
            textSize = cellSize * 0.52f
            isFakeBoldText = true
            textAlign = Paint.Align.CENTER
            isAntiAlias = true
        }
        val letterMetrics = letterPaint.fontMetrics
        val baselineOffset = (letterMetrics.descent - letterMetrics.ascent) / 2 - letterMetrics.descent

        for (r in 0 until gridSize) {
            for (c in 0 until gridSize) {
                val char = puzzle.grid[r][c].uppercaseChar().toString()
                val cx = left + (c + 0.5f) * cellSize
                val cy = top + (r + 0.5f) * cellSize + baselineOffset
                canvas.drawText(char, cx, cy, letterPaint)
            }
        }

        // Word Bank Section
        val wordBankTop = top + gridPixelSize + 30f
        canvas.drawText("WORD LIST (Find and check off all ${puzzle.targetWords.size} words):", 50f, wordBankTop, boldPaint)

        val columns = if (gridSize <= 7) 3 else 4
        val colWidth = 512f / columns
        val rowHeight = 22f

        val sortedWords = puzzle.targetWords.sorted()
        for (i in sortedWords.indices) {
            val word = sortedWords[i]
            val col = i % columns
            val row = i / columns

            val x = 50f + col * colWidth
            val y = wordBankTop + 24f + row * rowHeight

            // Checkbox
            canvas.drawRect(x, y - 11f, x + 11f, y, borderPaint)
            // Word text
            canvas.drawText(word, x + 18f, y, textPaint)
        }

        // Footer
        val footerPaint = Paint().apply {
            color = Color.GRAY
            textSize = 10f
            textAlign = Paint.Align.CENTER
            isAntiAlias = true
        }
        canvas.drawText("Created with Word Finder Magic • Kids Word Search", 306f, 765f, footerPaint)

        pdfDocument.finishPage(page)
    }

    private fun drawAnswerKeyPage(
        pdfDocument: PdfDocument,
        pageNum: Int,
        puzzle: WordSearchPuzzle,
        theme: ThemeData,
        gradeLevel: GradeLevel
    ) {
        val pageInfo = PdfDocument.PageInfo.Builder(612, 792, pageNum).create()
        val page = pdfDocument.startPage(pageInfo)
        val canvas = page.canvas

        val titlePaint = Paint().apply {
            color = Color.BLACK
            textSize = 24f
            isFakeBoldText = true
            textAlign = Paint.Align.CENTER
            isAntiAlias = true
        }

        val subtitlePaint = Paint().apply {
            color = Color.DKGRAY
            textSize = 14f
            textAlign = Paint.Align.CENTER
            isAntiAlias = true
        }

        canvas.drawText("ANSWER KEY: ${theme.name}", 306f, 50f, titlePaint)
        canvas.drawText("${gradeLevel.title} (${gradeLevel.gridSize}×${gradeLevel.gridSize})", 306f, 72f, subtitlePaint)

        val gridSize = puzzle.gridSize
        val gridPixelSize = 340f
        val left = (612f - gridPixelSize) / 2
        val top = 110f
        val cellSize = gridPixelSize / gridSize

        val borderPaint = Paint().apply {
            color = Color.BLACK
            style = Paint.Style.STROKE
            strokeWidth = 1.5f
        }
        val highlightPaint = Paint().apply {
            color = Color.argb(80, 79, 70, 229)
            style = Paint.Style.FILL
        }

        // Highlight cells containing words
        val foundCells = puzzle.placedWords.flatMap { it.cells }.toSet()
        for (cell in foundCells) {
            val cellLeft = left + cell.col * cellSize
            val cellTop = top + cell.row * cellSize
            canvas.drawRect(cellLeft, cellTop, cellLeft + cellSize, cellTop + cellSize, highlightPaint)
        }

        canvas.drawRect(left, top, left + gridPixelSize, top + gridPixelSize, borderPaint)

        val letterPaint = Paint().apply {
            color = Color.BLACK
            textSize = cellSize * 0.52f
            isFakeBoldText = true
            textAlign = Paint.Align.CENTER
            isAntiAlias = true
        }
        val letterMetrics = letterPaint.fontMetrics
        val baselineOffset = (letterMetrics.descent - letterMetrics.ascent) / 2 - letterMetrics.descent

        for (r in 0 until gridSize) {
            for (c in 0 until gridSize) {
                val char = puzzle.grid[r][c].uppercaseChar().toString()
                val cx = left + (c + 0.5f) * cellSize
                val cy = top + (r + 0.5f) * cellSize + baselineOffset
                canvas.drawText(char, cx, cy, letterPaint)
            }
        }

        pdfDocument.finishPage(page)
    }
}
