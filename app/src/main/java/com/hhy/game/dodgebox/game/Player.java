package com.hhy.game.dodgebox.game;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;

class Player {
    private Animator animator;
    private Bitmap bitmap;
    private Values values;
    private int width;
    private int height;
    private VectorXY coordinates;
    // true is left, false is right
    private boolean left = false;

    Player(Bitmap image, Values values, boolean isDead) {
        this.values = values;
        bitmap = image;
        // two diffirent images for player for now
        // die images & run images
        if (isDead) {
            width = bitmap.getWidth() / 3;
            height = bitmap.getHeight() / 2;
        } else {
            width = bitmap.getWidth() / 4;
            height = bitmap.getHeight() / 3;
        }

        // starting coordinates
        coordinates = new VectorXY(values.getWidth() / 2, values.getGroundY() - height);
    }

    // run to left
    void moveLeft() {
        int X = coordinates.getX() - 10;
        if (X < 0) X = 0;
        coordinates.set(X, coordinates.getY());
    }

    // run to right
    void moveRight() {
        int X = coordinates.getX() + 10;
        if (X > values.getWidth() - width) X = values.getWidth() - width;
        coordinates.set(X, coordinates.getY());
    }

    void update() {

    }

    // draw the character
    void draw(Canvas canvas) {
        canvas.drawBitmap(bitmap, coordinates.getX(), coordinates.getY(), null);
    }

    // set coordinates
    void setCoordinates(VectorXY coordinates) {
        this.coordinates = coordinates;
    }

    // get coordinates
    VectorXY getCoordinates() {
        return coordinates;
    }

    // character rect angle
    Rect getBoundingRectangle() {
        return new Rect(coordinates.getX() + width / 5, coordinates.getY() + height / 5,
                coordinates.getX() + width / 5 * 4, coordinates.getY() + height / 5 * 4);
    }

    // turn left
    void setLeft(boolean left) {
        this.left = left;
    }

    // for run to left
    private void rotateBitmap(Bitmap bitmap) {
        Matrix matrix = new Matrix();
        matrix.postScale(-1, 1, bitmap.getWidth() / 2f, bitmap.getHeight() / 2f);
        this.bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    // for animating character
    void setBitmap(Bitmap bitmap) {
        if (left) rotateBitmap(bitmap);
        else this.bitmap = bitmap;
    }

    // create animator
    void createAnimator(Bitmap bitmapWithFrames,
                               int numberOfFramesHorizontally,
                               int numberOfFramesVertically, int updateTimeMillis,
                               double timeForOneFrameMillis) {
        if (animator == null)
            animator = new Animator(this, bitmapWithFrames, numberOfFramesHorizontally, numberOfFramesVertically, updateTimeMillis, timeForOneFrameMillis);
    }

    // not using for now but who knows
    void destroyAnimator() {
        animator.stopAnimation();
        animator = null;
    }

    // start animation
    void startAnimation() {
        if ((animator != null) && (!animator.isRunning()))
            animator.startAnimation();
    }

    // stop animation
    void stopAnimation() {
        if ((animator != null) && (animator.isRunning()))
            animator.stopAnimation();
    }
}
