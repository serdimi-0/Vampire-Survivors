package org.milaifontanals.vampiresurvivors.view;

import static androidx.core.math.MathUtils.clamp;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.MediaPlayer;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import org.milaifontanals.vampiresurvivors.GameActivity;
import org.milaifontanals.vampiresurvivors.GameThread;
import org.milaifontanals.vampiresurvivors.MainActivity;
import org.milaifontanals.vampiresurvivors.MapGenerator;
import org.milaifontanals.vampiresurvivors.R;
import org.milaifontanals.vampiresurvivors.model.AreaWeaponGO;
import org.milaifontanals.vampiresurvivors.model.AreaWeaponItemGO;
import org.milaifontanals.vampiresurvivors.model.BatGO;
import org.milaifontanals.vampiresurvivors.model.BullGO;
import org.milaifontanals.vampiresurvivors.model.CharacterGO;
import org.milaifontanals.vampiresurvivors.model.Enemy;
import org.milaifontanals.vampiresurvivors.model.GameObject;
import org.milaifontanals.vampiresurvivors.model.GrenadeGO;
import org.milaifontanals.vampiresurvivors.model.PotionGO;
import org.milaifontanals.vampiresurvivors.model.SpriteGO;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

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

    private int attackFrameCounter = 0;
    private int attack2FrameCounter = 0;

    CharacterGO character;
    GrenadeGO grenade;
    AreaWeaponGO areaWeapon;

    MediaPlayer bombSound;


    private CopyOnWriteArrayList<GameObject> gameObjects = new CopyOnWriteArrayList<>();
    public void setAttack2FrameCounter(int attack2FrameCounter) {
        this.attack2FrameCounter = attack2FrameCounter;
    }

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
        grenade = new GrenadeGO(this, new Point(-100, -100));
        grenade.setEndPosition(new Point(-100, -100));
        areaWeapon = new AreaWeaponGO(this, character);

        gameObjects.add(areaWeapon);
        gameObjects.add(character);
        gameObjects.add(grenade);

        gameObjects.add(new BatGO(this, new Point(character.getPosition().x + 1500, character.getPosition().y + 1500)));
        gameObjects.add(new BatGO(this, new Point(character.getPosition().x + 1500, character.getPosition().y - 1500)));
        gameObjects.add(new BatGO(this, new Point(character.getPosition().x - 1500, character.getPosition().y + 1500)));
        gameObjects.add(new BatGO(this, new Point(character.getPosition().x - 1500, character.getPosition().y - 1500)));

        for (int i = 0; i < 10; i++) {
            int x = (int) (Math.random() * W);
            int y = (int) (Math.random() * H);
            gameObjects.add(new PotionGO(this, new Point(x, y), character));
        }

        gameObjects.add(new AreaWeaponItemGO(this, new Point(200, 200), character));

        bombSound = MediaPlayer.create(context, R.raw.bomb);
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
            if (g instanceof AreaWeaponGO && attack2FrameCounter == 0) {
                continue;
            }
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
        attackFrameCounter++;
        if (attackFrameCounter == 30 * 3) {
            int x = character.getPosition().x;
            int y = character.getPosition().y;
            grenade.setPosition(new Point(x, y));
            SpriteGO closest = closestEnemy(new Point(x, y));
            grenade.setEndPosition(closest == null ? new Point(x, y) : closest.getPosition());
            attackFrameCounter = 0;

            gameObjects.add(new BatGO(this, new Point(x + (int) (Math.random() * 4000 - 1000), y + (int) (Math.random() * 4000 - 1000))));

            if (Math.random() < 0.2)
                gameObjects.add(new BullGO(this, new Point(x + (int) (Math.random() * 4000 - 1000), y + (int) (Math.random() * 4000 - 1000))));
            if (Math.random() < 0.2)
                gameObjects.add(new PotionGO(this, new Point(x + (int) (Math.random() * 6000 + 1000), y + (int) (Math.random() * 6000 + 1000)), character));
            if (Math.random() < 0.2)
                gameObjects.add(new AreaWeaponItemGO(this, new Point(x + (int) (Math.random() * 6000 + 1000), y + (int) (Math.random() * 6000 + 1000)), character));
        }

        if (grenade.getPosition().x != -100) {

            for (GameObject g : gameObjects) {
                if (g instanceof Enemy) {
                    SpriteGO s = (SpriteGO) g;
                    RectF grenadeHitBox = grenade.getHitBox();
                    RectF sHitBox = s.getHitBox();
                    if (grenadeHitBox != null && sHitBox != null && grenadeHitBox.intersect(sHitBox)) {
                        bombSound.start();
                        s.reduceHealth(100);
                        if (s.getHealth() <= 0) {
                            gameObjects.remove(g);
                        }
                        grenade.setPosition(new Point(-100, -100));
                        grenade.setEndPosition(new Point(-100, -100));
                    }
                }
            }
        }

        for (GameObject g : gameObjects) {
            if (g instanceof Enemy) {
                SpriteGO s = (SpriteGO) g;
                RectF areaWeaponHitBox = areaWeapon.getHitBox();
                RectF sHitBox = s.getHitBox();
                if (areaWeaponHitBox != null && sHitBox != null && areaWeaponHitBox.intersect(sHitBox)) {
                    s.reduceHealth(5);
                    if (s.getHealth() <= 0) {
                        gameObjects.remove(g);
                    }
                }
            }
        }

        if(attack2FrameCounter > 0){
            attack2FrameCounter++;
            if(attack2FrameCounter == 30 * 20){
                attack2FrameCounter = 0;
            }
        }

        if (character.getHealth() <= 0) {
            gameOver();
        }

    }

    public void gameOver() {
        gameThread.gameOver();
        goi.action();
        MediaPlayer mediaPlayer = MediaPlayer.create(getContext(), R.raw.death);
        mediaPlayer.start();
        try {
            Thread.sleep(12000);
            Intent intent = new Intent(getContext(), MainActivity.class);
            getContext().startActivity(intent);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public SpriteGO closestEnemy(Point p) {
        SpriteGO closest = null;
        float minDistance = Float.MAX_VALUE;
        for (GameObject g : gameObjects) {
            SpriteGO s = (SpriteGO) g;
            if (!(s instanceof Enemy)) {
                continue;
            }
            float distance = (float) Math.sqrt(Math.pow(p.x - s.getPosition().x, 2) + Math.pow(p.y - s.getPosition().y, 2));
            if (distance < minDistance) {
                minDistance = distance;
                closest = s;
            }
        }
        return closest;
    }
}
