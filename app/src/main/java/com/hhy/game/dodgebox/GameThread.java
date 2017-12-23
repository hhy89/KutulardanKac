package com.hhy.game.dodgebox;

import com.hhy.game.dodgebox.game.Box;
import com.hhy.game.dodgebox.game.Game;

public class GameThread extends Thread {
    private Game game;
    private volatile boolean running = true;
    private final int FRAME_RATE = 1000;

    GameThread(Game game) {
        this.game = game;
    }

    @Override
    public void run() {
        long lastTime = System.currentTimeMillis();

        // Game loop
        while (running) {
            long now = System.currentTimeMillis();
            long elapsed = now - lastTime;

            if (elapsed < FRAME_RATE) {
                game.update();
                game.draw();
            }

            game.clearOldBox();
            Box lastBox = game.getLastBox();
            if (lastBox.getCoordinates().getY() > game.getBoxDistance()) {
                game.generateBox();
            }

            lastTime = now;
        }
    }

    void shutdown() {
        running = false;
    }
}
