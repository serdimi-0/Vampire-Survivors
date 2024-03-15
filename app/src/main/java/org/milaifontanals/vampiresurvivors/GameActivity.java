package org.milaifontanals.vampiresurvivors;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;

import org.milaifontanals.vampiresurvivors.view.GameSurfaceView;
import org.milaifontanals.vampiresurvivors.view.JoystickView;

public class GameActivity extends AppCompatActivity {

    private JoystickView joystickView;
    private GameSurfaceView gsv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        joystickView = findViewById(R.id.joystick);
        gsv = findViewById(R.id.gsv);
        gsv.setJoystick(joystickView);


        gsv.goi = new GameSurfaceView.GameOverInterface() {
            @Override
            public void action() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ObjectAnimator anim = ObjectAnimator.ofFloat(findViewById(R.id.deadScreen), "alpha", 0f, 1f);
                        anim.setDuration(2000);
                        anim.start();
                    }
                });

            }
        };
    }
}