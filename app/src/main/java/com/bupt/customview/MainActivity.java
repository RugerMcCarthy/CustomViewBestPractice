package com.bupt.customview;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.AnimatorSet;
import android.animation.Keyframe;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        GeometricXferView geometricXferView = findViewById(R.id.geometric);
//
//        ObjectAnimator fadeIn = ObjectAnimator.ofFloat(geometricXferView, "flipAngle", 45);
//        fadeIn.setDuration(1500);
//
//        ObjectAnimator fadeOut = ObjectAnimator.ofFloat(geometricXferView, "flipAngle", 0);
//        fadeOut.setDuration(1500);
//
//        Keyframe keyframe1 = Keyframe.ofFloat(0.3f, 0.5f * 360);
//        Keyframe keyframe2 = Keyframe.ofFloat(0.7f, 0.7f * 360);
//        Keyframe keyframe3 = Keyframe.ofFloat(1f, 360);
//        PropertyValuesHolder propertyValuesHolder = PropertyValuesHolder.ofFloat("rotateAngle", 360);
//        propertyValuesHolder.setKeyframes(keyframe1, keyframe2, keyframe3);
//
//        ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(geometricXferView, propertyValuesHolder);
//        animator.setDuration(5000);
//
//        AnimatorSet set = new AnimatorSet();
//        set.playSequentially(fadeIn, animator, fadeOut);
//        set.setStartDelay(1000);
//        set.start();
//        DashboardView dashboardView = findViewById(R.i.dash_board);
//        Button start = findViewById(R.id.start);
//        Button end = findViewById(R.id.end);
//        final Thread[] currentThread = new Thread[1];
//        start.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (currentThread[0] != null) {
//                    return;
//                }
//                currentThread[0] = new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        try {
//                            while (true) {
//                                Thread.sleep(10);
//                                dashboardView.invalidate();
//                            }
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                });
//                currentThread[0].start();
//            }
//        });
//        end.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (currentThread[0] != null) {
//                    currentThread[0].interrupt();
//                }
//                currentThread[0] = null;
//            }
//        });

    }
}