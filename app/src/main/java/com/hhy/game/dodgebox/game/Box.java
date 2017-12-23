package com.hhy.game.dodgebox.game;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import com.hhy.game.dodgebox.R;
import java.util.Random;

public class Box {
    private Values values;
    private VectorXY coordinates;
    private Bitmap bitmap;

    Box(Values values, Resources resources) {
        this.values = values;

        // box bitmap
        bitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(resources, R.drawable.box),
                values.getBoxWidth(), values.getBoxHeight(), false);

        // create random
        Random random = new Random();
        int X = random.nextInt(values.getWidth() - values.getBoxWidth());

        coordinates = new VectorXY(X, 0);
    }

    public VectorXY getCoordinates() {
        return coordinates;
    }

    void update(int speed) {
        coordinates.set(coordinates.getX(), coordinates.getY() + speed);
    }

    // boxes reach the ground
    boolean getScreenOff() {
        return (coordinates.getY() + values.getBoxHeight()) > values.getGroundY();
    }

    void draw(Canvas canvas) {
        canvas.drawBitmap(bitmap, coordinates.getX(), coordinates.getY(), null);
    }

    Rect getBoundingRectangle() {
        return new Rect(coordinates.getX(), coordinates.getY(),
                coordinates.getX() + values.getBoxWidth(), coordinates.getY() + values.getBoxHeight());
    }

}
