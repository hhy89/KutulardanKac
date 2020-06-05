package com.hhy.game.dodgebox

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import com.hhy.game.dodgebox.game.Game
import com.hhy.game.dodgebox.game.Values

class GameView(context: Context, attrs: AttributeSet): SurfaceView(context, attrs), SensorEventListener, SurfaceHolder.Callback {

    private var gameThread: GameThread? = null
    private lateinit var game: Game
    private lateinit var sensorManager: SensorManager

    override fun surfaceCreated(holder: SurfaceHolder) {
        val values = Values(width, height)
        game = Game(context, values, holder, resources)
        gameThread = GameThread(game)
        gameThread!!.start()

        // set up SensorManager
        sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager

        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_GAME)
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {

    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        if (gameThread != null) {
            gameThread!!.shutdown()
            while (gameThread != null) {
                try {
                    gameThread!!.join()
                    gameThread = null
                } catch (ignored: Exception) {
                }
            }
        }
    }

    override fun onSensorChanged(event: SensorEvent) {
        game.onSensorChanged(event)
    }

    override fun onAccuracyChanged(sensor: Sensor, i: Int) {

    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        game.onTouchEvent(event)
        performClick()
        return true
    }

    override fun performClick(): Boolean {
        super.performClick()
        return true
    }

    fun pauseGame() {
        game.pauseGame()
        // stop listening to the SensorManager
        sensorManager.unregisterListener(this)
    }

    init {
        holder.addCallback(this)
    }

}