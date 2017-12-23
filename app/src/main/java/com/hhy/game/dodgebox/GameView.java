package com.hhy.game.dodgebox;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.hhy.game.dodgebox.game.Game;
import com.hhy.game.dodgebox.game.Values;

public class GameView extends SurfaceView implements SurfaceHolder.Callback, SensorEventListener {
    private Context context;
    private GameThread gameThread;
    private Game game;

    private SensorManager sensorManager;

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        SurfaceHolder holder = getHolder();
        holder.addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Values values = new Values(getWidth(), getHeight());
        game = new Game(getContext(), values, holder, getResources());
        gameThread = new GameThread(game);
        gameThread.start();

        // set up SensorManager
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);

        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if (gameThread != null) {
            gameThread.shutdown();

            while (gameThread != null) {
                try {
                    gameThread.join();
                    gameThread = null;
                } catch (InterruptedException ignored) {
                }
            }
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        game.onSensorChanged(event);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        game.onTouchEvent(event);
        performClick();
        return true;
    }

    @Override
    public boolean performClick() {
        super.performClick();
        return true;
    }

    public void pauseGame() {
        game.pauseGame();
        // stop listening to the SensorManager
        sensorManager.unregisterListener(this);
    }
}
