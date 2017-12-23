package com.hhy.game.dodgebox.game;

import android.graphics.Rect;

// values class
// for responsive game
// numbers are found by trial and error method :D
public class Values {
    private int width;
    private int height;

    public Values(int width, int height) {
        this.width = width;
        this.height = height;
    }

    int getWidth() {
        return width;
    }
    int getHeight() {
        return height;
    }
    Rect getBackgroundRect() {
        return new Rect( 0, 0, width, height);
    }
    int getGroundY() {
        return height / 9 * 8;
    }
    Rect getPauseButtonRect() {
        int left = width / 10 * 9;
        int top = height / 50;
        return new Rect(left, top, left + getPausedButtonWidth(), top + getPausedButtonHeight());
    }
    int getPausedButtonWidth() {
        return width / 11;
    }
    int getPausedButtonHeight() {
        return height / 11;
    }
    int getPlayerWidth() {
        return width / 7;
    }
    int getPlayerHeight() {
        return height / 6;
    }
    int getScoreTextSize() {
        return height / 12;
    }
    int getScoreTextX() {
        return width / 100;
    }
    int getScoreTextY() {
        return height / 100;
    }
    int getRecordImgWidth() {
        return width / 32;
    }
    int getRecordImgHeight() {
        return height / 18;
    }
    int getRecordTextSize() {
        return height / 18;
    }
    int getRecordX() {
        return width / 100;
    }
    int getRecordY() {
        return height / 9;
    }
    int getPausedTextSize() {
        return height / 8;
    }
    int getPausedTextX() {
        return width / 10 * 3;
    }
    int getPausedTextY() {
        return height / 7;
    }
    int getBoxWidth() {
        return width / 16;
    }
    int getBoxHeight() {
        return height / 9;
    }
}
