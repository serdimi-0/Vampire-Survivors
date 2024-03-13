package org.milaifontanals.vampiresurvivors.model;

import android.graphics.Point;
import android.graphics.PointF;
import android.util.Log;

import org.milaifontanals.vampiresurvivors.R;
import org.milaifontanals.vampiresurvivors.view.GameSurfaceView;

public class PotionGO extends SpriteGO {

    PointF direction;
    CharacterGO character;

    public PotionGO(GameSurfaceView gsv, Point position, CharacterGO character) {
        super(gsv);
        sprites.put("static", new SpriteInfo(R.drawable.potion, 1));
        setState("static");
        direction = new PointF(0, 0);
        this.character = character;
        this.position = position;
    }

    @Override
    public PointF getDirection() {
        return direction;
    }

    @Override
    public int getEscala() {
        return 3;
    }

    @Override
    public void update() {
        super.update();
        if (character.getHitBox().intersect(this.getHitBox())){
            character.health += 20;
            if (character.health > 100) {
                character.health = 100;
            }
            this.position = new Point(-100, -100);
        }

    }
}
