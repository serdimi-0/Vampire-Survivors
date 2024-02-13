package org.milaifontanals.vampiresurvivors;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.PointF;
import android.os.Bundle;
import android.widget.TextView;

import org.milaifontanals.vampiresurvivors.view.GameSurfaceView;
import org.milaifontanals.vampiresurvivors.view.JoystickView;

public class MainActivity extends AppCompatActivity {

    private JoystickView joystickView;
    private GameSurfaceView gsv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        joystickView = findViewById(R.id.joystick);
        gsv = findViewById(R.id.gsv);
        gsv.setJoystick(joystickView);
    }
}