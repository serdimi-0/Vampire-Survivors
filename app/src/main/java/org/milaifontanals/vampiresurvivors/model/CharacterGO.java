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

import androidx.annotation.DrawableRes;

import org.milaifontanals.vampiresurvivors.R;
import org.milaifontanals.vampiresurvivors.view.GameSurfaceView;

import java.util.HashMap;

public class CharacterGO extends GameObject {

    private class SpriteInfo {
        public Bitmap sprite;
        public int size;
        public int w, h;
        public SpriteInfo(@DrawableRes int drawableRes, int size) {
            this.sprite = BitmapFactory.decodeResource(gsv.getResources(), drawableRes);
            this.size = size;
            this.w = sprite.getWidth() / size;
            this.h = sprite.getHeight();
        }
    }

    Point posPersonatge = new Point(1500, 1500);

    //Bitmap pjSpriteBmp;
    private HashMap<String, SpriteInfo> sprites;
    private String state;

    private int currentFrame;
    private int escala = 5;
    private int cont = 0;
    private boolean isMoving;

    public void setState(String state) {
        this.state = state;
    }

    public SpriteInfo getCurrentSprite() {
        return sprites.get(state);
    }

    public CharacterGO(GameSurfaceView gsv) {
        super(gsv);

        sprites = new HashMap<>();
        sprites.put("idle", new SpriteInfo(R.drawable.player_sprite_idle, 1);
        sprites.put("walk", new SpriteInfo(R.drawable.player_sprite_walk, 3);
        setState("idle");
    }

    @Override
    public void update() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            PointF pJoystick = gsv.getJoystickDirection();
            if (pJoystick != null) {
                posPersonatge.x += pJoystick.x * 28;
                posPersonatge.y += pJoystick.y * 28;

                SpriteInfo s = getCurrentSprite();

                posPersonatge.x = clamp(posPersonatge.x, escala * s.w / 2, gsv.getW() - s.w / 2 * escala);
                posPersonatge.y = clamp(posPersonatge.y, escala * s.h / 2, gsv.getH() - s.h / 2 * escala);

                boolean isMovingNext = Math.abs(pJoystick.x) > 0.001 || Math.abs(pJoystick.y) > 0.001;

                if (isMovingNext && !isMoving) {
                    currentFrame = 1;
                }
                if (isMoving) {
                    cont++;
                    if (cont == 8) {
                        currentFrame++;
                        cont = 0;
                    }
                    if (currentFrame >= s.size) {
                        currentFrame = 2;
                    }
                } else {
                    currentFrame = 1;
                }
                isMoving = isMovingNext;
            }
        }
    }

    @Override
    public void paint(Canvas canvas) {
        Point posPersonatgeScreen = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
            posPersonatgeScreen = gsv.getScreenCoordinatesPersonatge();
        }
        canvas.drawBitmap(pjSpriteBmp, new Rect(SPRITE_WIDTH * (currentFrame - 1), 0, SPRITE_WIDTH * currentFrame, SPRITE_HEIGHT), new RectF(posPersonatgeScreen.x - 0.5f * SPRITE_WIDTH * escala, posPersonatgeScreen.y - SPRITE_HEIGHT * 0.5f * escala, posPersonatgeScreen.x + SPRITE_WIDTH * 0.5f * escala, posPersonatgeScreen.y + SPRITE_HEIGHT * 0.5f * escala), null);
    }

    public Point getPosPersonatge() {
        return posPersonatge;
    }
}
