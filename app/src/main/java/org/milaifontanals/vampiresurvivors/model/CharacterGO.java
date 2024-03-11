package org.milaifontanals.vampiresurvivors.model;

import static androidx.core.math.MathUtils.clamp;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.os.Build;
import android.util.Log;

import org.milaifontanals.vampiresurvivors.R;
import org.milaifontanals.vampiresurvivors.view.GameSurfaceView;

public class CharacterGO extends SpriteGO {

    private float health = 100;
    Paint paintHealthBarBg = new Paint();
    Paint paintHealthBar = new Paint();

    public CharacterGO(GameSurfaceView gsv) {
        super(gsv);

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
            position.x += pJoystick.x * 28;
            position.y += pJoystick.y * 28;

            SpriteInfo s = getCurrentSprite();
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

        if(health < 0f){
            gsv.gameOver();
        }
    }

    @Override
    public void paint(Canvas canvas) {
        super.paint(canvas);

        Point screenCoordinates = gsv.getScreenCoordinates(position.x, position.y);
        Rect rect = new Rect(screenCoordinates.x - 100, screenCoordinates.y + 120, screenCoordinates.x + 100, screenCoordinates.y + 100);
        canvas.drawRect(rect, paintHealthBarBg);

        rect.right = rect.left + (int) (health/100f * rect.width());
        canvas.drawRect(screenCoordinates.x - 100, screenCoordinates.y + 120, screenCoordinates.x - 100 + health * 2, screenCoordinates.y + 100, paintHealthBar);

    }
}
