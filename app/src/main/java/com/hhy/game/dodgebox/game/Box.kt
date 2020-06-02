package com.hhy.game.dodgebox.game

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Rect
import com.hhy.game.dodgebox.R
import kotlin.random.Random

class Box internal constructor(private val values: Values, resources: Resources) {
    val coordinates: VectorXY
    private val bitmap: Bitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(resources, R.drawable.box),
            values.boxWidth, values.boxHeight, false)

    fun update(speed: Int) {
        coordinates[coordinates.x] = coordinates.y + speed
    }

    // boxes reach the ground
    val screenOff: Boolean
        get() = coordinates.y + values.boxHeight > values.groundY

    fun draw(canvas: Canvas) {
        canvas.drawBitmap(bitmap, coordinates.x.toFloat(), coordinates.y.toFloat(), null)
    }

    val boundingRectangle: Rect
        get() = Rect(coordinates.x, coordinates.y,
                coordinates.x + values.boxWidth, coordinates.y + values.boxHeight)

    init {
        // create random
        val X = Random.nextInt(values.width - values.boxWidth)
        coordinates = VectorXY(X, 0)
    }
}