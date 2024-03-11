package org.milaifontanals.vampiresurvivors.model;

import android.graphics.Point;
import android.graphics.PointF;

import org.milaifontanals.vampiresurvivors.R;
import org.milaifontanals.vampiresurvivors.view.GameSurfaceView;

public class BullGO extends SpriteGO implements Enemy {

    PointF direction;

    public BullGO(GameSurfaceView gsv, Point position) {
        super(gsv);
        sprites.put("walk", new SpriteInfo(R.drawable.bull_sheet, 3));
        setState("walk");
        this.position = position;
        direction = new PointF((float) (Math.random() * 2 - 1), (float) (Math.random() * 2 - 1));
    }

    @Override
    public float getDamage() {
        return 0.5f;
    }

    @Override
    public void update() {
        super.update();
        /*Direction can take 4 values:
        * x=1,y=1
        * x=1,y=-1
        * x=-1,y=-1
        * x=-1,y=1
        * there's a small chance the direction changes every update*/
        if (Math.random() < 0.01) {
            direction = new PointF((float) (Math.random() * 2 - 1), (float) (Math.random() * 2 - 1));
        }
        position.x += direction.x * 10;
        position.y += direction.y * 10;
        if (position.x < 300) {
            position.x = 300;
            direction.x = -direction.x;
        }
        if (position.x > gsv.getW() - 300) {
            position.x = gsv.getW() - 300;
            direction.x = -direction.x;
        }
        if (position.y < 300) {
            position.y = 300;
            direction.y = -direction.y;
        }
        if (position.y > gsv.getH() - 300) {
            position.y = gsv.getH() - 300;
            direction.y = -direction.y;
        }




    }

    @Override
    public int getEscala() {
        return 7;
    }

    @Override
    public PointF getDirection() {
        return direction;
    }
}
