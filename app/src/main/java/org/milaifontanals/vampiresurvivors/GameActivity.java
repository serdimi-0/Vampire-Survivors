package org.milaifontanals.vampiresurvivors;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import org.milaifontanals.vampiresurvivors.view.GameSurfaceView;
import org.milaifontanals.vampiresurvivors.view.JoystickView;

public class GameActivity extends AppCompatActivity {

    private JoystickView joystickView;
    private GameSurfaceView gsv;
    private boolean finished = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        finished = false;

        joystickView = findViewById(R.id.joystick);
        gsv = findViewById(R.id.gsv);
        gsv.setJoystick(joystickView);

        gsv.goi = new GameSurfaceView.GameOverInterface() {
            @Override
            public void action() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ObjectAnimator anim = ObjectAnimator.ofFloat(findViewById(R.id.deadScreen),"alpha",0f,1f);
                        anim.setDuration(1500);
                        anim.start();
                    }
                });
                try {
                    if (!finished) {
                        Thread.sleep(4000);
                        Intent intent = new Intent(GameActivity.this, MainActivity.class);
                        finished = true;
                        startActivity(intent);
                    }

                } catch(Exception e){
                    Log.d("TAG", "EXCEPCION");
                }
            }
        };
    }
}