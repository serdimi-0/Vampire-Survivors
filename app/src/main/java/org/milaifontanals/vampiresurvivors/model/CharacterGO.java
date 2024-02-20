package org.milaifontanals.vampiresurvivors.model;

import static androidx.core.math.MathUtils.clamp;

import android.graphics.PointF;
import android.os.Build;

import org.milaifontanals.vampiresurvivors.R;
import org.milaifontanals.vampiresurvivors.view.GameSurfaceView;

public class CharacterGO extends SpriteGO {

    public CharacterGO(GameSurfaceView gsv) {
        super(gsv);

        sprites.put("idle", new SpriteInfo(R.drawable.player_sprite_idle, 1));
        sprites.put("walk", new SpriteInfo(R.drawable.player_sprite_walk, 3));
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
    }

}
