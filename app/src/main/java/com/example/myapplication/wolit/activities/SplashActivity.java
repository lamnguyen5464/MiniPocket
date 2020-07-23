package com.example.myapplication.wolit.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;


import com.example.myapplication.wolit.R;
import com.example.myapplication.wolit.database.RealmApdapter;
import com.example.myapplication.wolit.model.CurrentStatus;
import com.jakewharton.threetenabp.AndroidThreeTen;

import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Animation animation = AnimationUtils.loadAnimation(this,R.anim.welcome);
        ImageView imageView = (ImageView) findViewById(R.id.im1);
        TextView textView2 = (TextView) findViewById(R.id.t2);
        imageView.startAnimation(animation);
        textView2.startAnimation(animation);

        Typeface typeface = ResourcesCompat.getFont(this, R.font.math_tapping);
        textView2.setTypeface(typeface);


//        *set up
        RealmApdapter.initIntance(this);
        AndroidThreeTen.init(this);
        CurrentStatus.initSharedValue(this);
        CurrentStatus.update();

        Timer timer = new Timer();
        timer.schedule(new after_delay(),1000);


    }

    class after_delay extends TimerTask {
        public void run(){
            Intent i = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(i);
            finish();
        }
    }

}
