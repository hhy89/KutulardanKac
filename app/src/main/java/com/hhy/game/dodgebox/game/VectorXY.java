package com.hhy.game.dodgebox.game;

// vector class
public class VectorXY {
    private int x;
    private int y;

    VectorXY(int x, int y) {
        this.x = x;
        this.y = y;
    }

    int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    void set(int x, int y) {
        this.x = x;
        this.y = y;
    }
}