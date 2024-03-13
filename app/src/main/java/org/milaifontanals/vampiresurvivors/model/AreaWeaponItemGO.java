package org.milaifontanals.vampiresurvivors.model;

import android.graphics.Point;
import android.graphics.PointF;

import org.milaifontanals.vampiresurvivors.R;
import org.milaifontanals.vampiresurvivors.view.GameSurfaceView;

public class AreaWeaponItemGO extends SpriteGO {

    PointF direction;
    CharacterGO character;
    GameSurfaceView gsv;

    public AreaWeaponItemGO(GameSurfaceView gsv, Point position, CharacterGO character) {
        super(gsv);
        sprites.put("static", new SpriteInfo(R.drawable.spain_heart_sprite, 1));
        setState("static");
        direction = new PointF(0, 0);
        this.character = character;
        this.position = position;
        this.gsv = gsv;
    }

    @Override
    public PointF getDirection() {
        return direction;
    }

    @Override
    public int getEscala() {
        return 10;
    }
    @Override
    public void update() {
        super.update();
        if (character.getHitBox().intersect(this.getHitBox())){
            gsv.setAttack2FrameCounter(1);
            this.position = new Point(-100, -100);
        }

    }
}
