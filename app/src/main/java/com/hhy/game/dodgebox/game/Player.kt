package com.hhy.game.dodgebox.game

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Rect

internal class Player(private var bitmap: Bitmap, private val values: Values, isDead: Boolean) {
    private lateinit var animator: Animator
    private var width = 0
    private var height = 0

    // set coordinates
    // get coordinates
    var coordinates: VectorXY

    // true is left, false is right
    private var left = false

    // run to left
    fun moveLeft() {
        var x = coordinates.x - 10
        if (x < 0) x = 0
        coordinates[x] = coordinates.y
    }

    // run to right
    fun moveRight() {
        var x = coordinates.x + 10
        if (x > values.width - width) x = values.width - width
        coordinates[x] = coordinates.y
    }

    fun update() {}

    // draw the character
    fun draw(canvas: Canvas) {
        canvas.drawBitmap(bitmap, coordinates.x.toFloat(), coordinates.y.toFloat(), null)
    }

    // character rect angle
    val boundingRectangle: Rect
        get() = Rect(coordinates.x + width / 5, coordinates.y + height / 5,
                coordinates.x + width / 5 * 4, coordinates.y + height / 5 * 4)

    // turn left
    fun setLeft(left: Boolean) {
        this.left = left
    }

    // for run to left
    private fun rotateBitmap(bitmap: Bitmap) {
        val matrix = Matrix()
        matrix.postScale(-1f, 1f, bitmap.width / 2f, bitmap.height / 2f)
        this.bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }

    // for animating character
    fun setBitmap(bitmap: Bitmap) {
        if (left) rotateBitmap(bitmap) else this.bitmap = bitmap
    }

    // create animator
    fun createAnimator(bitmapWithFrames: Bitmap,
                       numberOfFramesHorizontally: Int,
                       numberOfFramesVertically: Int, updateTimeMillis: Int,
                       timeForOneFrameMillis: Double) {
        animator = Animator(this, bitmapWithFrames, numberOfFramesHorizontally, numberOfFramesVertically, updateTimeMillis, timeForOneFrameMillis)
    }

    // not using for now but who knows
    fun destroyAnimator() {
        animator.stopAnimation()
    }

    // start animation
    fun startAnimation() {
        if (!animator.isRunning) animator.startAnimation()
    }

    // stop animation
    fun stopAnimation() {
        if (animator.isRunning) animator.stopAnimation()
    }

    init {
        // two diffirent images for player for now
        // die images & run images
        if (isDead) {
            width = bitmap.width / 3
            height = bitmap.height / 2
        } else {
            width = bitmap.width / 4
            height = bitmap.height / 3
        }

        // starting coordinates
        coordinates = VectorXY(values.width / 2, values.groundY - height)
    }
}