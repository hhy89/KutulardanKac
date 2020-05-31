package com.hhy.game.dodgebox.game

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.os.SystemClock.sleep
import android.view.MotionEvent
import android.view.SurfaceHolder
import com.hhy.game.dodgebox.GameOverActivity
import com.hhy.game.dodgebox.R
import kotlin.math.roundToInt

internal class Game(private val context: Context, private val values: Values, private val holder: SurfaceHolder,
                    private val resources: Resources) {
    private val options: BitmapFactory.Options = BitmapFactory.Options()
    private lateinit var background: Bitmap
    private lateinit var pauseButton: Bitmap
    private val boxes = ArrayList<Box>()
    private lateinit var player: Player
    private lateinit var scoreText: ScoreText
    private var gameRunning: Boolean = false
    private var currentScore: Int = 0
    private var recordScore: Int = 0

    // move character for accelerometer sensor
    fun onSensorChanged(event: SensorEvent) {
        if (gameRunning && event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
            // y value = event.values[1]
            when {
                event.values[1] < 0 -> {
                    player.startAnimation()
                    player.setLeft(true)
                    player.moveLeft()
                }
                event.values[1] > 0 -> {
                    player.startAnimation()
                    player.setLeft(false)
                    player.moveRight()
                }
                else -> player.stopAnimation()
            }
        }
    }

    // for pause button
    fun onTouchEvent(event: MotionEvent) {
        if (event.action == MotionEvent.ACTION_DOWN) {
            if (values.pauseButtonRect.contains(event.x.roundToInt(), event.y.roundToInt())) {
                if (gameRunning) {
                    gameRunning = false
                    player.stopAnimation()
                } else {
                    gameRunning = true
                    player.startAnimation()
                }
            }
        }

    }

    // update things
    fun update() {
        if (gameRunning) {
            var gameSpeed: Int = currentScore / 10
            if (gameSpeed < 6) gameSpeed = 6

            for (box in boxes) {
                box.update(gameSpeed)

                // if char under the box :D
                if (player.boundingRectangle.intersect(box.boundingRectangle)) {
                    finishGame()
                }
            }

            player.update()
        }
    }

    // draw them
    fun draw() {
        val canvas: Canvas? = holder.lockCanvas()
        if (canvas != null) {
            canvas.drawColor(Color.WHITE)
            this.drawGame(canvas)
            holder.unlockCanvasAndPost(canvas)
        }
    }

    private fun drawGame(canvas: Canvas) {
        // background
        canvas.drawBitmap(background, null, values.backgroundRect, null)

        // boxes
        for (box in boxes)
            box.draw(canvas)

        // player
        player.draw(canvas)

        // scoretexts
        scoreText.drawRecord(canvas, recordScore.toString())
        scoreText.drawScore(canvas, currentScore.toString())
        if (!gameRunning) scoreText.drawPause(canvas)

        // pause button
        canvas.drawBitmap(pauseButton, null, values.pauseButtonRect, null)
    }

    private fun restartGame() {
        // clear box list
        boxes.clear()
        gameRunning = true
        loadRecord()

        // chosen player
        val sharedPreferences = context.getSharedPreferences("dodgebox", MODE_PRIVATE)
        val chosenCharacter = if (sharedPreferences.getInt("character", 0) == 1)
            "girl"
        else "boy"
        val characterId: Int = resources.getIdentifier("run_${chosenCharacter}", "drawable", context.packageName)

        val playerRun: Bitmap = Bitmap.createScaledBitmap(
                BitmapFactory.decodeResource(resources, characterId, options),
                values.playerWidth * 4, values.playerHeight * 3, false)
        player = Player(playerRun, values, false)
        player.createAnimator(playerRun, 4, 3, 30, 30.0)
        player.startAnimation()

        // chosen background
        var backgroundId = sharedPreferences.getInt("background", 0) + 1
        backgroundId = resources.getIdentifier("background${backgroundId}", "drawable", context.packageName)

        background = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(resources, backgroundId, options),
                values.width, values.height, false)

        // pause button
        pauseButton = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(resources, R.drawable.pause, options),
                values.pausedButtonWidth, values.pausedButtonHeight, false)

        scoreText = ScoreText(values, resources)

        // generate first box
        generateBox()
    }

    // generate box
    fun generateBox() {
        val box = Box(values, resources)
        boxes.add(box)
    }
    // remove old box from list
    fun clearOldBox() {
        /*
        // api needs 24
        boxes.removeIf {
            it.screenOff
        }
        */
        // only this works perfectly
        val iterator = boxes.iterator()
        while (iterator.hasNext()) {
            val box = iterator.next()
            if (box.screenOff) {
                iterator.remove()

                // score+1
                currentScore++
            }
        }
    }
    fun getLastBox(): Box {
        return boxes[boxes.size - 1]
    }

    fun getBoxDistance(): Int {
        return values.boxHeight * 4
    }

    // finish game
    private fun finishGame() {
        // keep them for gameover screen
        val score: Int = currentScore
        val record: Int = recordScore
        // keep it for die animation
        val playerXY: VectorXY = player.coordinates

        // save record
        saveRecord()

        // chosen player
        val sharedPreferences = context.getSharedPreferences("dodgebox", MODE_PRIVATE)
        val chosenCharacter = if (sharedPreferences.getInt("character", 0) == 1)
            "girl"
        else "boy"
        val characterId: Int = resources.getIdentifier("die_${chosenCharacter}", "drawable", context.packageName)

        val playerDie: Bitmap = Bitmap.createScaledBitmap(
                BitmapFactory.decodeResource(resources, characterId, options),
                values.playerWidth * 3, values.playerHeight * 2, false)
        player = Player(playerDie, values, true)
        player.coordinates = playerXY
        player.createAnimator(playerDie, 3, 2, 30, 30.0)
        player.startAnimation()

        // wait the animation
        sleep(120)

        gameRunning = false

        // go to gameover screen
        val intent = Intent(context, GameOverActivity::class.java)
        intent.putExtra("score", score)
        intent.putExtra("record", record)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
    }

    // save record
    private fun saveRecord() {
        if (currentScore > recordScore) {
            val sharedPreferences = context.getSharedPreferences("dodgebox", MODE_PRIVATE)
            sharedPreferences.edit().putInt("record_key", currentScore).apply()
        }
    }

    // load record
    private fun loadRecord() {
        val sharedPreferences = context.getSharedPreferences("dodgebox", MODE_PRIVATE)
        recordScore = sharedPreferences.getInt("record_key", 0)
    }

    // pause game
    fun pauseGame() {
        gameRunning = false
        player.stopAnimation()
    }

    init {
        options.inScaled = false
        // start the game
        restartGame()
    }
}