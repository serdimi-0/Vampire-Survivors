package org.milaifontanals.vampiresurvivors;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

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
    }
}