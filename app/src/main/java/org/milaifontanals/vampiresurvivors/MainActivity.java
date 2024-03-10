package org.milaifontanals.vampiresurvivors;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity implements ViewTreeObserver.OnGlobalLayoutListener {

    private ConstraintLayout mainFrame;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainFrame = findViewById(R.id.mainFrame);
        mainFrame.getViewTreeObserver().addOnGlobalLayoutListener(this);
    }


    @Override
    public void onGlobalLayout() {
        int screenHeight = mainFrame.getHeight();
        int screenWidth = mainFrame.getWidth();

        ObjectAnimator animationSurvivors = ObjectAnimator.ofFloat(findViewById(R.id.imvSurvivors), "translationY", -screenHeight, 0f);
        animationSurvivors.setDuration(1500);

        ObjectAnimator animationVampire = ObjectAnimator.ofFloat(findViewById(R.id.imvVampire), "translationY", -screenHeight, 0f);
        animationVampire.setDuration(1500);

        ImageView[] bats = new ImageView[]{
                findViewById(R.id.bat1),
                findViewById(R.id.bat2),
                findViewById(R.id.bat3),
                findViewById(R.id.bat4),
                findViewById(R.id.bat5)
        };
        ObjectAnimator[] batMove = new ObjectAnimator[bats.length];

        int i = 0;
        for (ImageView bat : bats) {
            AnimationDrawable animation = (AnimationDrawable) bat.getDrawable();
            animation.start();
            ObjectAnimator batAnimation = ObjectAnimator.ofFloat(bat, "translationX", screenWidth, -600f);
            batAnimation.setDuration(3000);
            batAnimation.setStartDelay((long) (Math.random() * 400));
            batMove[i] = batAnimation;
            i++;
            // Vertical movement
            ObjectAnimator batAnimationY = ObjectAnimator.ofFloat(bat, "translationY", (float) (Math.random() * 300), 0);
            batAnimationY.setDuration(800);
            batAnimationY.setRepeatMode(ObjectAnimator.REVERSE);
            batAnimationY.setRepeatCount(ObjectAnimator.INFINITE);
            batAnimationY.start();
        }

        AnimatorSet batSet = new AnimatorSet();
        batSet.playTogether(batMove);

        AnimatorSet set = new AnimatorSet();
        set.playSequentially(animationSurvivors, animationVampire, batSet);
        set.start();
    }
}