package org.milaifontanals.vampiresurvivors;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity implements ViewTreeObserver.OnGlobalLayoutListener {

    private ConstraintLayout mainFrame;
    private MediaPlayer mediaPlayer;
    private MediaPlayer mediaPlayerPlay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainFrame = findViewById(R.id.mainFrame);
        mainFrame.getViewTreeObserver().addOnGlobalLayoutListener(this);


        findViewById(R.id.btn).setOnClickListener(e -> {
            Intent intent = new Intent(this, GameActivity.class);
            startActivity(intent);
            mediaPlayer.release();
            mediaPlayerPlay.start();
            mediaPlayer.stop();
            finish();
        });

        mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.spain);
        mediaPlayerPlay = MediaPlayer.create(MainActivity.this, R.raw.play);

    }


    @Override
    public void onGlobalLayout() {
        int screenHeight = mainFrame.getHeight();
        int screenWidth = mainFrame.getWidth();
        mediaPlayer.start();

        ObjectAnimator animationSurvivors = ObjectAnimator.ofFloat(findViewById(R.id.imvSurvivors), "translationY", 0f, 800);
        animationSurvivors.setDuration(1500);

        ObjectAnimator animationVampire = ObjectAnimator.ofFloat(findViewById(R.id.imvVampire), "translationY", 0f, 1100);
        animationVampire.setDuration(1500);

        ObjectAnimator animationVamp = ObjectAnimator.ofFloat(findViewById(R.id.imvVamp),"alpha",0f,1f);
        animationVamp.setDuration(2000);

        ObjectAnimator survivorsFadeOut = ObjectAnimator.ofFloat(findViewById(R.id.imvSurvivors),"alpha",1f,0f);
        survivorsFadeOut.setDuration(1500);
        ObjectAnimator vampiresFadeOut = ObjectAnimator.ofFloat(findViewById(R.id.imvVampire),"alpha",1f,0f);
        vampiresFadeOut.setDuration(1500);
        ObjectAnimator vampFadeOut = ObjectAnimator.ofFloat(findViewById(R.id.imvVamp),"alpha",1f,0f);
        vampFadeOut.setDuration(1500);
        ObjectAnimator buttonFadeIn = ObjectAnimator.ofFloat(findViewById(R.id.imvVamp),"alpha",0f,1f);
        buttonFadeIn.setDuration(1000);

        AnimatorSet fadeOutSet = new AnimatorSet();
        fadeOutSet.playTogether(survivorsFadeOut, vampiresFadeOut, vampFadeOut);

        ObjectAnimator fadeInBtn = ObjectAnimator.ofFloat(findViewById(R.id.btn),"alpha",0f,1f);
        fadeInBtn.setDuration(500);

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
            ObjectAnimator batAnimation = ObjectAnimator.ofFloat(bat, "translationX", screenWidth + 500, -100f);
            batAnimation.setDuration(3000);
            batAnimation.setStartDelay((long) (Math.random() * 400));
            batMove[i] = batAnimation;
            i++;
            ObjectAnimator batAnimationY = ObjectAnimator.ofFloat(bat, "translationY", (float) (Math.random() * 300), 0);
            batAnimationY.setDuration(800);
            batAnimationY.setRepeatMode(ObjectAnimator.REVERSE);
            batAnimationY.setRepeatCount(ObjectAnimator.INFINITE);
            batAnimationY.start();
        }

        AnimatorSet batSet = new AnimatorSet();
        batSet.playTogether(batMove);

        AnimatorSet set = new AnimatorSet();
        set.playSequentially(animationVamp, animationSurvivors, animationVampire, batSet, fadeOutSet, fadeInBtn);
        set.start();
    }
}