package com.hhy.game.dodgebox

import com.hhy.game.dodgebox.game.Box
import com.hhy.game.dodgebox.game.Game

internal class GameThread(private val game: Game) : Thread() {
    @Volatile
    var running = true
        private set
    private val FRAME_RATE: Int = 1000

    override fun run() {
        var lastTime: Long = System.currentTimeMillis()

        // Game loop
        while (running) {
            val now: Long = System.currentTimeMillis()
            val elapsed: Long = now - lastTime

            if (elapsed < FRAME_RATE) {
                game.update()
                game.draw()
            }

            game.clearOldBox()
            val lastBox: Box = game.getLastBox()
            if (lastBox.coordinates.y > game.getBoxDistance()) {
                game.generateBox()
            }

            lastTime = now
        }
    }

    fun shutdown() {
        running = false
    }
}