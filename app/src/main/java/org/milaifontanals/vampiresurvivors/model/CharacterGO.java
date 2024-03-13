package org.milaifontanals.vampiresurvivors.model;

import static androidx.core.math.MathUtils.clamp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.os.Build;
import android.util.Log;

import org.milaifontanals.vampiresurvivors.R;
import org.milaifontanals.vampiresurvivors.view.GameSurfaceView;

public class CharacterGO extends SpriteGO {

    Paint paintHealthBarBg = new Paint();
    Paint paintHealthBar = new Paint();
    private Bitmap mapBitmap = BitmapFactory.decodeResource(gsv.getResources(), R.drawable.map_mini);

    public CharacterGO(GameSurfaceView gsv) {
        super(gsv);
        health = 100;
        position = new Point(2500, 2500);
        paintHealthBarBg.setARGB(105, 0, 0, 0);
        paintHealthBar.setARGB(255, 255, 0, 0);
        sprites.put("idle", new SpriteInfo(R.drawable.character_sprite_idle, 1));
        sprites.put("walk", new SpriteInfo(R.drawable.character_sprites_walk, 3));
        setState("idle");
    }

    @Override
    public PointF getDirection() {
        return gsv.getJoystickDirection();
    }

    @Override
    public int getEscala() {
        return 5;
    }

    public void update() {
        super.update();
        PointF pJoystick = gsv.getJoystickDirection();
        if (pJoystick != null) {

            SpriteInfo s = getCurrentSprite();

            if (pJoystick.x != 0 || pJoystick.y != 0) {
                PointF newPosition = new PointF();
                newPosition.x = position.x + pJoystick.x * 28;
                newPosition.y = position.y + pJoystick.y * 28;

                int left = (int) newPosition.x - s.w * 3 / 2;
                int top = (int) newPosition.y - s.h * 3 / 2;
                int right = (int) newPosition.x + s.w * 2 / 2;
                int bottom = (int) newPosition.y + s.h * 3 / 2;

                if (!mapBitmap.getColor(left / 256,  top / 256).equals(Color.valueOf(0, 0, 1)) &&
                    !mapBitmap.getColor(left / 256,  bottom / 256).equals(Color.valueOf(0, 0, 1)) &&
                    !mapBitmap.getColor(right / 256, top / 256).equals(Color.valueOf(0, 0, 1)) &&
                    !mapBitmap.getColor(right / 256, bottom / 256).equals(Color.valueOf(0, 0, 1))) {
                    position.x = (int) newPosition.x;
                    position.y = (int) newPosition.y;
                }
            }
            int escala = getEscala();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                position.x = clamp(position.x, escala * s.w / 2, gsv.getW() - s.w / 2 * escala);
                position.y = clamp(position.y, escala * s.h / 2, gsv.getH() - s.h / 2 * escala);
            }


            boolean isMovingNext = Math.abs(pJoystick.x) > 0.001 || Math.abs(pJoystick.y) > 0.001;

            if (isMovingNext) {
                setState("walk");
            } else {
                setState("idle");
            }
        }

        for (GameObject go : gsv.getGameObjects()) {
            if (go instanceof Enemy) {
                SpriteGO sgo = (SpriteGO) go;
                if (sgo.getHitBox().intersect(getHitBox())) {
                    health -= ((Enemy) go).getDamage();
                }
            }
        }

        if (health < 0f) {
            gsv.gameOver();
        }


    }

    @Override
    public void paint(Canvas canvas) {
        super.paint(canvas);

        Point screenCoordinates = gsv.getScreenCoordinates(position.x, position.y);
        Rect rect = new Rect(screenCoordinates.x - 100, screenCoordinates.y + 120, screenCoordinates.x + 100, screenCoordinates.y + 100);
        canvas.drawRect(rect, paintHealthBarBg);

        rect.right = rect.left + (int) (health / 100f * rect.width());
        canvas.drawRect(screenCoordinates.x - 100, screenCoordinates.y + 120, screenCoordinates.x - 100 + health * 2, screenCoordinates.y + 100, paintHealthBar);

    }
}
