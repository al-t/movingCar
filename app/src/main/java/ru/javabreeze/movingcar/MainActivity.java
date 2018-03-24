package ru.javabreeze.movingcar;

import android.animation.AnimatorSet;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;

import ru.javabreeze.movingcar.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity {

    private final static int ROTATION_TIME = 800;
    private final static int MOVEMENT_TIME = 2000;

    private final String TAG = this.getClass().getSimpleName();

    private ActivityMainBinding binding;

    private float x1, y1; // coordinates of car centre
    private float x2, y2; // coordinates of tap position
    private float width, height;
    private float pivotX, pivotY; // coordinates of rotation pivot
    private float currentAngle = 0;
    private float angleToRotate;
    private boolean coordsWereInitialised;

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
        rotateCar();
//        moveCar();
    }

    private void moveCar() {
        TranslateAnimation move = new TranslateAnimation(
                Animation.ABSOLUTE, 0,
                Animation.ABSOLUTE, x2 - x1,
                Animation.ABSOLUTE, 0,
                Animation.ABSOLUTE, y2 - y1);
//        TranslateAnimation move = new TranslateAnimation(
//                Animation.RELATIVE_TO_SELF, 0,
//                Animation.RELATIVE_TO_SELF, 100,
//                Animation.RELATIVE_TO_SELF, 0,
//                Animation.RELATIVE_TO_SELF, 100);
        move.setFillEnabled(true);
        move.setFillAfter(true);
        move.setDuration(MOVEMENT_TIME);
        move.setInterpolator(new AccelerateDecelerateInterpolator());
        binding.car.startAnimation(move);
    }

    private void rotateCar() {
        Log.v(TAG, "currentAngle: " + currentAngle);
        Log.v(TAG, "angleToRotate: " + angleToRotate);
        RotateAnimation rotate = new RotateAnimation(currentAngle, angleToRotate,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
//        rotate.setFillEnabled(true);
//        rotate.setFillAfter(true);
        rotate.setDuration(ROTATION_TIME);
        rotate.setInterpolator(new AccelerateDecelerateInterpolator());
//        Toast toast = Toast.makeText(getApplicationContext(), "X: " + x2 + ", Y: " + y2 +
//                ", Angle: " + angleToRotate, Toast.LENGTH_SHORT); toast.show();
//        binding.car.startAnimation(rotate);

        TranslateAnimation move = new TranslateAnimation(
                Animation.ABSOLUTE, 0,
                Animation.ABSOLUTE, x2 - x1,
                Animation.ABSOLUTE, 0,
                Animation.ABSOLUTE, y2 - y1);
//        TranslateAnimation move = new TranslateAnimation(
//                Animation.RELATIVE_TO_SELF, 0,
//                Animation.RELATIVE_TO_SELF, 100,
//                Animation.RELATIVE_TO_SELF, 0,
//                Animation.RELATIVE_TO_SELF, 100);
//        move.setFillEnabled(true);
//        move.setFillAfter(true);
        move.setDuration(MOVEMENT_TIME);
        move.setStartOffset(ROTATION_TIME);
        move.setInterpolator(new AccelerateDecelerateInterpolator());
//        binding.car.startAnimation(move);

        AnimationSet as = new AnimationSet(true);
        as.setInterpolator(new AccelerateDecelerateInterpolator());
        as.addAnimation(rotate);
        as.addAnimation(move);
        as.setFillEnabled(true);
        as.setFillAfter(true);
        as.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
//                binding.car.setX(x1);
//                binding.car.setY(y1);
//                binding.car.layout(
//                        ((int) (x1 - binding.car.getMeasuredWidth())),
//                        ((int) (y1 - binding.car.getMeasuredHeight())),
//                        ((int) (x1)),
//                        ((int) (y1)));
                ConstraintLayout.LayoutParams par = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
                par.topMargin = (int) y2;
                par.leftMargin = (int) x2;
                binding.car.setLayoutParams(par);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        binding.car.startAnimation(as);
        as.start();

        currentAngle = angleToRotate;
        x1 = x2;
        y1 = y2;
    }

    private void obtainPivotRotation() {
        pivotX = binding.car.getWidth() / 2;
        pivotY = binding.car.getHeight() / 2;
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
        if (!coordsWereInitialised) {
            x1 = binding.car.getX() + binding.car.getWidth() / 2;
            y1 = binding.car.getY() + binding.car.getHeight() / 2;
            width = binding.car.getWidth();
            height = binding.car.getHeight();
            coordsWereInitialised = true;
        }
    }


}
