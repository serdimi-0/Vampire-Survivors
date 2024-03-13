package org.milaifontanals.vampiresurvivors.model;

import android.graphics.Point;
import android.graphics.PointF;

import org.milaifontanals.vampiresurvivors.R;
import org.milaifontanals.vampiresurvivors.view.GameSurfaceView;

public class GrenadeGO extends SpriteGO {
    PointF direction;
    Point endPosition;

    public GrenadeGO(GameSurfaceView gsv, Point position) {
        super(gsv);
        sprites.put("static", new SpriteInfo(R.drawable.grenade_sprite, 1));
        setState("static");
        direction = new PointF(0, 0);
        this.position = position;
    }

    @Override
    public PointF getDirection() {
        return direction;
    }

    @Override
    public int getEscala() {
        return 4;
    }

    public void setEndPosition(Point endPosition) {
        this.endPosition = endPosition;
        direction = new PointF(endPosition.x - position.x, endPosition.y - position.y);
        double distance = Math.sqrt(direction.x * direction.x + direction.y * direction.y);
        direction.x /= distance;
        direction.y /= distance;
    }

    @Override
    public void update() {
        super.update();
        if (direction.x != 0 || direction.y != 0) {
            position.x += direction.x * 40;
            position.y += direction.y * 40;
        }
    }
}
