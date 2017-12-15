package com.sl.houseloan;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;

public class SplashActivity extends AppCompatActivity {
    private ImageView mImageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mImageView= (ImageView) findViewById(R.id.imageView);
        ActionBar actionBar = getActionBar();
        if (actionBar!=null){
            actionBar.hide();
        }
        android.support.v7.app.ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar!=null){
            supportActionBar.hide();
        }

        final AlphaAnimation animation = new AlphaAnimation(0.3f, 1.0f);
        animation.setDuration(2500);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        mImageView.setAnimation(animation);
        animation.start();
    }
}
