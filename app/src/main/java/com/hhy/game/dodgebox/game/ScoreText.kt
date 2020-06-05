package com.hhy.game.dodgebox.game

import android.content.Context
import android.content.res.Resources
import android.graphics.*
import androidx.core.content.ContextCompat
import com.hhy.game.dodgebox.R

internal class ScoreText(context: Context, private val values: Values, private val resources: Resources) {
    private var recordBitmap: Bitmap
    private val drawingPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val scoresPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val recordPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val pauseTextPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    // draw score
    fun drawScore(canvas: Canvas, text: String) {
        canvas.drawText(text, values.scoreTextX.toFloat(), values.scoreTextY + scoresPaint.textSize, scoresPaint)
    }

    // draw record
    fun drawRecord(canvas: Canvas, text: String) {
        canvas.drawBitmap(recordBitmap, values.recordX.toFloat(), values.recordY.toFloat(), drawingPaint)
        canvas.drawText(text, values.recordX + recordBitmap.width.toFloat(),
                values.recordY + recordPaint.textSize, recordPaint)
    }

    // draw paused text
    fun drawPause(canvas: Canvas) {
        canvas.drawText(resources.getString(R.string.pause), values.pausedTextX.toFloat(), values.pausedTextY.toFloat(), pauseTextPaint)
    }

    init {
        // score text
        scoresPaint.color = ContextCompat.getColor(context, R.color.colorText)
        scoresPaint.textSize = values.scoreTextSize.toFloat()
        scoresPaint.typeface = Typeface.SERIF
        // record text
        recordPaint.color = ContextCompat.getColor(context, R.color.colorText)
        recordPaint.textSize = values.recordTextSize.toFloat()
        recordPaint.typeface = Typeface.SERIF
        // paused text
        pauseTextPaint.color = ContextCompat.getColor(context, R.color.colorText)
        pauseTextPaint.textSize = values.pausedTextSize.toFloat()
        pauseTextPaint.typeface = Typeface.SERIF

        // record image
        recordBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(resources, R.drawable.records),
                values.recordImgWidth,
                values.recordImgHeight, false)
    }
}