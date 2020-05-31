package com.hhy.game.dodgebox.game

import android.graphics.Rect

// values class
// for responsive game
// numbers are found by trial and error method :D
class Values(val width: Int, val height: Int) {

    val backgroundRect: Rect
        get() = Rect(0, 0, width, height)

    val groundY: Int
        get() = height / 9 * 8

    val pauseButtonRect: Rect
        get() {
            val left = width / 10 * 9
            val top = height / 50
            return Rect(left, top, left + pausedButtonWidth, top + pausedButtonHeight)
        }

    val pausedButtonWidth: Int
        get() = width / 11

    val pausedButtonHeight: Int
        get() = height / 11

    val playerWidth: Int
        get() = width / 7

    val playerHeight: Int
        get() = height / 6

    val scoreTextSize: Int
        get() = height / 12

    val scoreTextX: Int
        get() = width / 100

    val scoreTextY: Int
        get() = height / 100

    val recordImgWidth: Int
        get() = width / 32

    val recordImgHeight: Int
        get() = height / 18

    val recordTextSize: Int
        get() = height / 18

    val recordX: Int
        get() = width / 100

    val recordY: Int
        get() = height / 9

    val pausedTextSize: Int
        get() = height / 8

    val pausedTextX: Int
        get() = width / 10 * 3

    val pausedTextY: Int
        get() = height / 7

    val boxWidth: Int
        get() = width / 16

    val boxHeight: Int
        get() = height / 9

}