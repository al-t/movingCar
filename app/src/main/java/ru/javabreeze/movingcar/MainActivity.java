package ru.javabreeze.movingcar;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;

import ru.javabreeze.movingcar.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity {

    private final static int ROTATION_TIME = 800;
    private final static int MOVEMENT_TIME = 2000;

    private final String TAG = this.getClass().getSimpleName();

    private ActivityMainBinding binding;

    private float x1, y1; // coordinates of car centre
    private float x2, y2; // coordinates of tap position
    private float currentAngle = 0;
    private float angleToRotate;
    private boolean coordsWereInitialised;
    float width, height;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        setTouchListener();
    }

    private void setTouchListener() {
        binding.space.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    break;
                case MotionEvent.ACTION_UP:
                    v.performClick();
                    x2 = event.getX();
                    y2 = event.getY();
                    moveCarToTapPosition();
                    break;
                default:
                    break;
            }
            return true;
        });
    }

    private void moveCarToTapPosition() {
        getCarPosition();
        getRotationAngle();
        moveCar();
    }

    private void moveCar() {
        ObjectAnimator rotate = ObjectAnimator.ofFloat(binding.car ,
                "rotation", currentAngle, angleToRotate);
        rotate.setDuration(ROTATION_TIME);
        PropertyValuesHolder pvhX = PropertyValuesHolder.ofFloat("x",  x1 - width / 2, x2 - width / 2);
        PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat("y",  y1 - height / 2, y2 - height / 2);
        ObjectAnimator move = ObjectAnimator.ofPropertyValuesHolder(binding.car, pvhX, pvhY);
        move.setDuration(MOVEMENT_TIME);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playSequentially(rotate, move);
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                removeTouchListener();
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                setTouchListener();
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                setTouchListener();
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
        animatorSet.start();

        currentAngle = angleToRotate;
    }

    private void removeTouchListener() {
        binding.space.setOnTouchListener(null);
    }

    private void getRotationAngle() {

        angleToRotate = (float) Math.toDegrees(Math.atan((x2 - x1) / (y1 - y2)));
        if (x2 > x1) {
            if (y2 > y1) { // bottom-right direction
                if (currentAngle < -90) {
                    angleToRotate = angleToRotate - 180;
                } else {
                    angleToRotate = 180 + angleToRotate;
                }
            }
        } else {
            if (y2 > y1) { // bottom-left direction
                if (currentAngle > 90) {
                    angleToRotate = angleToRotate + 180;
                } else {
                    angleToRotate = angleToRotate - 180;
                }
            }
        }
    }

    private void getCarPosition() {
            x1 = binding.car.getX() + binding.car.getWidth() / 2;
            y1 = binding.car.getY() + binding.car.getHeight() / 2;
            width = binding.car.getWidth();
            height = binding.car.getHeight();
    }
}
