package org.milaifontanals.vampiresurvivors.view;

import static androidx.core.math.MathUtils.clamp;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import org.milaifontanals.vampiresurvivors.GameThread;
import org.milaifontanals.vampiresurvivors.MapGenerator;
import org.milaifontanals.vampiresurvivors.R;
import org.milaifontanals.vampiresurvivors.model.BatGO;
import org.milaifontanals.vampiresurvivors.model.BullGO;
import org.milaifontanals.vampiresurvivors.model.CharacterGO;
import org.milaifontanals.vampiresurvivors.model.GameObject;

import java.util.ArrayList;
import java.util.List;

@RequiresApi(api = Build.VERSION_CODES.Q)
public class GameSurfaceView extends SurfaceView implements SurfaceHolder.Callback {

    public GameThread gameThread;

    public interface GameOverInterface {
        public void action();
    }

    public GameOverInterface goi;
    Paint paint = new Paint();
    Paint pBackground = new Paint();
    Paint pLine = new Paint();
    MapGenerator map;

    Path path = new Path();
    JoystickView joystick;
    private int w, h;
    private int W, H;

    CharacterGO character;
    private List<GameObject> gameObjects = new ArrayList<>();

    public int getW() {
        return W;
    }

    public int getH() {
        return H;
    }


    public int getw() {
        return w;
    }

    public int geth() {
        return h;
    }

    public List<GameObject> getGameObjects() {
        return gameObjects;
    }

    public GameSurfaceView(Context context) {
        this(context, null);
    }

    public GameSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.getHolder().addCallback(this);
        paint.setColor(0xFF006A4E);
        pBackground.setColor(0xFFAA0000);

        map = new MapGenerator(getResources(), context);

        W = map.getScenario().getWidth();
        H = map.getScenario().getHeight();

        character = new CharacterGO(this);

        gameObjects.add(character);
        gameObjects.add(new BatGO(this, new Point(100, 100)));
        gameObjects.add(new BatGO(this, new Point(300, 300)));
        gameObjects.add(new BatGO(this, new Point(600, 600)));
        gameObjects.add(new BatGO(this, new Point(1200, 1200)));
        gameObjects.add(new BatGO(this, new Point(1500, 1500)));
        gameObjects.add(new BullGO(this, new Point(2800, 1800)));

    }

    public Point getCharacterPosition() {
        return character.getPosition();
    }

    private Point getScreenCoordinates() {

        Point coordCorner = new Point();
        coordCorner.x = character.getPosition().x - w / 2;
        coordCorner.y = character.getPosition().y - h / 2;

        coordCorner.x = clamp(coordCorner.x, 0, W - w);
        coordCorner.y = clamp(coordCorner.y, 0, H - h);
        return coordCorner;
    }

    public Point getScreenCoordinates(int x, int y) {
        Point coordCorner = new Point();
        Point screenCorner = getScreenCoordinates();

        coordCorner.x = x - screenCorner.x;
        coordCorner.y = y - screenCorner.y;
        return coordCorner;
    }

    public void setJoystick(JoystickView joystick) {
        this.joystick = joystick;
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        w = getWidth();
        h = getHeight();
        gameThread = new GameThread(this);
        gameThread.start();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
        gameThread.gameOver();
    }

    public void paint(Canvas canvas) {
        Point screenCorner = getScreenCoordinates();

        canvas.drawBitmap(map.getScenario(), new Rect(screenCorner.x, screenCorner.y, screenCorner.x + w, screenCorner.y + h), new Rect(0, 0, w, h), null);

        for (GameObject g : gameObjects) {
            g.paint(canvas);
        }
    }

    public PointF getJoystickDirection() {
        return joystick.getDirection();
    }

    public void update() {
        if (joystick != null) {
            for (GameObject g : gameObjects) {
                g.update();
            }
        }
        /*SHow the pixel of the terrain the character is stepping on in the log*/
        Log.d("Terrain", "Terrain: " + map.getScenario().getPixel(character.getPosition().x, character.getPosition().y));
    }

    public void gameOver() {

        /*gameThread.gameOver();*/
        goi.action();
    }

    public void restart(){
    }
}
