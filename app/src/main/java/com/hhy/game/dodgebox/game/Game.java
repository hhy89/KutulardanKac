package com.hhy.game.dodgebox.game;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import com.hhy.game.dodgebox.GameOverActivity;
import com.hhy.game.dodgebox.R;
import java.util.ArrayList;
import static android.content.Context.MODE_PRIVATE;
import static android.os.SystemClock.sleep;

public class Game {
    private Context context;
    private SurfaceHolder holder;
    private Values values;
    private Resources resources;
    private BitmapFactory.Options options;
    private Bitmap background;
    private Bitmap pauseButton;
    private ArrayList<Box> boxes = new ArrayList<>();
    private Player player;
    private ScoreText scoreText;
    private boolean gameRunning;
    private int currentScore = 0;
    private int recordScore = 0;

    public Game(Context context, Values values, SurfaceHolder holder, Resources resources) {
        this.context = context;
        this.values = values;
        this.holder = holder;
        this.resources = resources;

        options = new BitmapFactory.Options();
        options.inScaled = false;
        // start the game
        restartGame();
    }

    // move character for accelerometer sensor
    public void onSensorChanged(SensorEvent event) {
        if (gameRunning && event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            // y value = event.values[1]
            if (event.values[1] < 0) {
                player.startAnimation();
                player.setLeft(true);
                player.moveLeft();
            } else if (event.values[1] > 0) {
                player.startAnimation();
                player.setLeft(false);
                player.moveRight();
            } else if (event.values[1] == 0)
                player.stopAnimation();
        }

    }

    // for pause button
    public void onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (values.getPauseButtonRect().contains(Math.round(event.getX()), Math.round(event.getY()))) {
                if (gameRunning) {
                    gameRunning = false;
                    player.stopAnimation();
                } else {
                    gameRunning = true;
                    player.startAnimation();
                }
            }
        }

    }

    // update things
    public void update() {
        if (gameRunning) {
            int gameSpeed = currentScore / 10;
            if (gameSpeed < 6) gameSpeed = 6;

            for (Box box : boxes) {
                box.update(gameSpeed);

                // if char under the box :D
                if (player.getBoundingRectangle().intersect(box.getBoundingRectangle())) {
                    finishGame();
                }
            }

            player.update();
        }
    }

    // draw them
    public void draw() {
        Canvas canvas = holder.lockCanvas();
        if (canvas != null) {
            canvas.drawColor(Color.WHITE);
            drawGame(canvas);
            holder.unlockCanvasAndPost(canvas);
        }
    }

    private void drawGame(Canvas canvas) {
        // background
        canvas.drawBitmap(background, null, values.getBackgroundRect(), null);

        // boxes
        for (Box box : boxes)
            box.draw(canvas);

        // player
        player.draw(canvas);

        // scoretexts
        scoreText.drawRecord(canvas, String.valueOf(recordScore));
        scoreText.drawScore(canvas, String.valueOf(currentScore));
        if (!gameRunning) scoreText.drawPause(canvas);

        // pause button
        canvas.drawBitmap(pauseButton, null, values.getPauseButtonRect(), null);
    }

    private void restartGame() {
        // clear box list
        boxes.clear();
        gameRunning = true;
        loadRecord();

        // chosen player
        String chosenCharacter;
        SharedPreferences sharedPreferences = context.getSharedPreferences("dodgebox", MODE_PRIVATE);
        if (sharedPreferences.getInt("character", 0) == 1)
            chosenCharacter = "girl";
        else chosenCharacter = "boy";
        int characterId = resources.getIdentifier("run_" + chosenCharacter, "drawable", context.getPackageName());

        Bitmap playerRun = Bitmap.createScaledBitmap(
                BitmapFactory.decodeResource(resources, characterId, options),
                values.getPlayerWidth() * 4, values.getPlayerHeight() * 3, false);
        player = new Player(playerRun, values, false);
        player.createAnimator(playerRun, 4, 3, 30, 30);
        player.startAnimation();

        // chosen background
        int backgroundId = sharedPreferences.getInt("background", 0) + 1;
        backgroundId = resources.getIdentifier("background" + backgroundId, "drawable", context.getPackageName());

        background = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(resources, backgroundId, options),
                values.getWidth(), values.getHeight(), false);

        // pause button
        pauseButton = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(resources, R.drawable.pause, options),
                values.getPausedButtonWidth(), values.getPausedButtonHeight(), false);

        scoreText = new ScoreText(values, resources);

        // generate first box
        generateBox();
    }

    // generate box
    public void generateBox() {
        Box box = new Box(values, resources);
        boxes.add(box);
    }
    // remove old box from list
    public void clearOldBox() {
        for (int i = 0; i < boxes.size(); i++) {
            if (boxes.get(i).getScreenOff()) {
                boxes.remove(i);

                // score +1
                currentScore += 1;
            }
        }
    }
    public Box getLastBox() {
        return boxes.get(boxes.size() - 1);
    }
    public int getBoxDistance() {
        return values.getBoxHeight() * 4;
    }

    // finish game
    private void finishGame() {
        // keep them for gameover screen
        int score = currentScore;
        int record = recordScore;
        // keep it for die animation
        VectorXY playerXY = player.getCoordinates();

        // save record
        saveRecord();

        // chosen player
        String chosenCharacter;
        SharedPreferences sharedPreferences = context.getSharedPreferences("dodgebox", MODE_PRIVATE);
        if (sharedPreferences.getInt("character", 0) == 1)
            chosenCharacter = "girl";
        else chosenCharacter = "boy";
        int characterId = resources.getIdentifier("die_" + chosenCharacter, "drawable", context.getPackageName());

        Bitmap playerDie = Bitmap.createScaledBitmap(
                BitmapFactory.decodeResource(resources, characterId, options),
                values.getPlayerWidth() * 3, values.getPlayerHeight() * 2, false);
        player = new Player(playerDie, values, true);
        player.setCoordinates(playerXY);
        player.createAnimator(playerDie, 3, 2, 30, 30);
        player.startAnimation();

        // wait the animation
        sleep(120);

        gameRunning = false;

        // go to gameover screen
        Intent intent = new Intent(context, GameOverActivity.class);
        intent.putExtra("score", score);
        intent.putExtra("record", record);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    // save record
    private void saveRecord() {
        if (currentScore > recordScore) {
            SharedPreferences sharedPreferences = context.getSharedPreferences("dodgebox", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("record_key", currentScore);
            editor.apply();
        }
    }

    // load record
    private void loadRecord() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("dodgebox", MODE_PRIVATE);
        recordScore = sharedPreferences.getInt("record_key", 0);
    }

    // pause game
    public void pauseGame() {
        gameRunning = false;
        player.stopAnimation();
    }
}