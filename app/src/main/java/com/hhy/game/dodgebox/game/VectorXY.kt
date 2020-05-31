package com.hhy.game.dodgebox.game

// vector class
class VectorXY internal constructor(var x: Int, var y: Int) {

    operator fun set(x: Int, y: Int) {
        this.x = x
        this.y = y
    }

}