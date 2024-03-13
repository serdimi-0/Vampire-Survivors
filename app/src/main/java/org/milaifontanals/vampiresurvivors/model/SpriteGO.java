package org.milaifontanals.vampiresurvivors.model;

import static androidx.core.math.MathUtils.clamp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.util.Log;

import androidx.annotation.DrawableRes;

import org.milaifontanals.vampiresurvivors.view.GameSurfaceView;

import java.util.HashMap;

public abstract class SpriteGO extends GameObject {
    protected HashMap<String, SpriteInfo> sprites;
    protected Point position = new Point(1500, 1500);
    protected String state;
    protected int fCounter = 0;
    protected int frequency = 4;
    private float lastSeenX;
    private RectF hitbox;
    protected int health;


    public int getHealth() {
        return health;
    }
    public void reduceHealth(int damage) {
        health -= damage;
    }

    public SpriteGO(GameSurfaceView gsv) {
        super(gsv);
        sprites = new HashMap<>();
    }

    public abstract PointF getDirection();

    public void setState(String state) {
        this.state = state;
    }

    public SpriteInfo getCurrentSprite() {
        return sprites.get(state);
    }

    public abstract int getEscala();

    @Override
    public void update() {
        SpriteInfo s = getCurrentSprite();
        s.nextFrame();
    }

    @Override
    public void paint(Canvas canvas) {
        Point posScreen = gsv.getScreenCoordinates(getPosition().x, getPosition().y);
        SpriteInfo s = getCurrentSprite();
        int escala = getEscala();
        canvas.save();

        if(this instanceof GrenadeGO){
            canvas.rotate((float) Math.toDegrees(Math.atan2(getDirection().y, getDirection().x))-90, posScreen.x, posScreen.y);
        }

        float x = getDirection().x;
        if (x == 0) {
            x = lastSeenX;
        }
        lastSeenX = x;
        if (x < 0) {
            canvas.scale(-1, 1, posScreen.x, posScreen.y);
        }
        hitbox = new RectF(posScreen.x - 0.5f * s.w * escala, posScreen.y - s.h * 0.5f * escala, posScreen.x + s.w * 0.5f * escala, posScreen.y + s.h * 0.5f * escala);
        canvas.drawBitmap(s.sprite,
                new Rect(s.w * (s.currentFrame), 0, s.w * (s.currentFrame + 1), s.h),
                hitbox, null);

        canvas.restore();
    }

    public RectF getHitBox() {
        return hitbox;
    }

    public Point getPosition() {
        return position;
    }

    protected class SpriteInfo {
        public Bitmap sprite;
        public int size;
        public int w, h;
        public int currentFrame = 0;

        public SpriteInfo(@DrawableRes int drawableRes, int size) {
            this.sprite = BitmapFactory.decodeResource(gsv.getResources(), drawableRes);
            this.size = size;
            this.w = sprite.getWidth() / size;
            this.h = sprite.getHeight();
        }

        public void nextFrame() {
            fCounter++;
            if (fCounter > frequency) {
                currentFrame = (currentFrame + 1) % size;
                fCounter = 0;
            }
        }

    }

    public void setPosition(Point position) {
        this.position = position;
    }

    public void setHealth(int health) {
        this.health = health;
    }
}
