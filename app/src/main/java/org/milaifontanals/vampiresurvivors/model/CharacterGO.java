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

import org.milaifontanals.vampiresurvivors.R;
import org.milaifontanals.vampiresurvivors.view.GameSurfaceView;

public class CharacterGO extends GameObject{

    Point posPersonatge = new Point(1500, 1500);

    Bitmap pjSpriteBmp;
    private int SPRITE_WIDTH;
    private int SPRITE_HEIGHT;
    private int SPRITE_FRAMES;
    private int currentFrame;
    private int escala = 5;
    private int cont = 0;
    private boolean isMoving;
    public CharacterGO(GameSurfaceView gsv) {
        super(gsv);
        pjSpriteBmp = BitmapFactory.decodeResource(gsv.getResources(), R.drawable.amongus_sprites);
        SPRITE_FRAMES = 4;
        SPRITE_WIDTH = pjSpriteBmp.getWidth() / SPRITE_FRAMES;
        SPRITE_HEIGHT = pjSpriteBmp.getHeight();
    }

    @Override
    public void update() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            PointF pJoystick = gsv.getJoystickDirection();
            if (pJoystick != null) {
                posPersonatge.x += pJoystick.x * 28;
                posPersonatge.y += pJoystick.y * 28;

                posPersonatge.x = clamp(posPersonatge.x, escala*SPRITE_WIDTH/2, gsv.getW() - SPRITE_WIDTH/2*escala);
                posPersonatge.y = clamp(posPersonatge.y, escala*SPRITE_HEIGHT/2, gsv.getH() - SPRITE_HEIGHT/2*escala);

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
                    if (currentFrame >= SPRITE_FRAMES) {
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
        canvas.drawBitmap(pjSpriteBmp,
                new Rect(SPRITE_WIDTH * (currentFrame - 1), 0, SPRITE_WIDTH * currentFrame, SPRITE_HEIGHT),
                new RectF(posPersonatgeScreen.x - 0.5f * SPRITE_WIDTH * escala,
                        posPersonatgeScreen.y - SPRITE_HEIGHT * 0.5f * escala,
                        posPersonatgeScreen.x + SPRITE_WIDTH * 0.5f * escala,
                        posPersonatgeScreen.y + SPRITE_HEIGHT * 0.5f * escala),
                null);
    }

    public Point getPosPersonatge() {
        return posPersonatge;
    }
}
