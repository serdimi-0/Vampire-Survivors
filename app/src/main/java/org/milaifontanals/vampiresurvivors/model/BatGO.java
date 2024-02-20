package org.milaifontanals.vampiresurvivors.model;

import static androidx.core.math.MathUtils.clamp;

import android.graphics.Point;
import android.graphics.PointF;
import android.os.Build;

import androidx.annotation.RequiresApi;

import org.milaifontanals.vampiresurvivors.R;
import org.milaifontanals.vampiresurvivors.view.GameSurfaceView;
import org.milaifontanals.vampiresurvivors.view.Utils;

public class BatGO extends SpriteGO {

    public BatGO(GameSurfaceView gsv, Point position) {
        super(gsv);

        sprites.put("fly", new SpriteInfo(R.drawable.bat_sheet, 9));
        setState("fly");
        this.position = position;
    }

    @Override
    public PointF getDirection() {
        Point characterPosition = gsv.getCharacterPosition();
        return new PointF(characterPosition.x - position.x, characterPosition.y - position.y);
    }

    @Override
    public int getEscala() {
        return 7;
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    public void update() {
        super.update();

        Point characterPosition = null;

        characterPosition = gsv.getCharacterPosition();
        PointF vector = new PointF(characterPosition.x - position.x, characterPosition.y - position.y);
        double distance = Utils.getDistance(position,characterPosition);

        // Normalitzem el vector
        if(distance > 0){
            vector.x /= distance;
            vector.y /= distance;
        }

        position.x += vector.x * 10;
        position.y += vector.y * 10;


        /*SpriteInfo s = getCurrentSprite();
        int escala = getEscala();
        */

    }

}
