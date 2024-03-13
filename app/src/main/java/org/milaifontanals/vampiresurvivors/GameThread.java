package org.milaifontanals.vampiresurvivors;

import android.graphics.Canvas;

import org.milaifontanals.vampiresurvivors.view.GameSurfaceView;

public class GameThread extends Thread {

    private GameSurfaceView gsv;
    private boolean isGameOver = false;
    private static final long sleepTime = 1000000000 / 30;

    public GameThread(GameSurfaceView gameSurfaceView) {
        gsv = gameSurfaceView;
    }

    @Override
    public void run() {
        super.run();

        long startTime, afterTime, remainingTime;
        while (!isGameOver) {
            startTime = System.nanoTime();

            Canvas canvas = gsv.getHolder().lockCanvas();
            if (canvas != null) {
                gsv.paint(canvas);
                gsv.getHolder().unlockCanvasAndPost(canvas);
            }

            gsv.paint(canvas);
            gsv.update();
            afterTime = System.nanoTime();
            remainingTime = sleepTime - (afterTime - startTime);
            if (remainingTime > 0) {
                try {
                    Thread.sleep(remainingTime / 1000000);
                } catch (InterruptedException ignored) {
                }
            }
        }
    }

    public void gameOver(){
        isGameOver = true;
    }
    public boolean isGameOver(){
        return isGameOver;
    }

}
