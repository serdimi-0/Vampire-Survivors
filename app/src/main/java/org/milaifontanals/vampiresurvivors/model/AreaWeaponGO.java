package org.milaifontanals.vampiresurvivors.model;

import android.graphics.PointF;

import org.milaifontanals.vampiresurvivors.R;
import org.milaifontanals.vampiresurvivors.view.GameSurfaceView;

public class AreaWeaponGO extends SpriteGO{


    PointF direction;
    CharacterGO character;
    public AreaWeaponGO(GameSurfaceView gsv, CharacterGO character) {
        super(gsv);
        direction = new PointF(0, 0);
        sprites.put("rotate", new SpriteInfo(R.drawable.spain_flag_spritesheet, 2));
        setState("rotate");
        this.character = character;
        this.frequency = 2;
    }

    @Override
    public PointF getDirection() {
        return direction;
    }

    @Override
    public int getEscala() {
        return 2;
    }

    @Override
    public void update() {
        super.update();
        this.position = character.position;
    }
}
