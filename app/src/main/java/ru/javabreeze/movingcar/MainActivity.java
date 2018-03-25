package ru.javabreeze.movingcar;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.databinding.DataBindingUtil;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.animation.LinearInterpolator;

import ru.javabreeze.movingcar.databinding.ActivityMainBinding;
import ru.javabreeze.movingcar.model.Car;


public class MainActivity extends AppCompatActivity {

    private final static int ROTATION_TIME = 800;
    private final static int MOVEMENT_TIME = 2000;
    private final static float MAX_CAR_VELOCITY = 10000;

    private final String TAG = this.getClass().getSimpleName();

    private ActivityMainBinding binding;

    private Car car;
    float carVIWidth, carVIHeight; // car ViewImage width and height
    private PointF carCentre;
    private float angle = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        setTouchListener();
    }

    private void initCar() {
        car = new Car(MAX_CAR_VELOCITY, getLayoutCarPosition());
    }

    private void setTouchListener() {
        binding.space.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    break;
                case MotionEvent.ACTION_UP:
                    v.performClick();
                    removeTouchListener();
                    moveCarToTapPosition(new PointF(event.getX(), event.getY()));
                    Log.v(TAG, "Tap Coordinates. X: " + event.getX() + ", Y: " + event.getY());
                    break;
                default:
                    break;
            }
            return true;
        });
    }

    private void moveCarToTapPosition(PointF destination) {
        if (car == null) {
            initCar();
        }
        car.setDestination(destination);
//        ValueAnimator animation = ValueAnimator.ofInt(10000);
        ValueAnimator animation = ValueAnimator.ofObject(new CarPointFEvaluator(car), carCentre,
                destination);
        animation.setInterpolator(new LinearInterpolator());
        animation.addUpdateListener(updatedAnimation -> {
            if (carCentre.equals(destination)) {
                animation.cancel();
            } else {
                car.move();
                PointF modelCarCentre = car.getCarPosition();
                if (Math.abs(car.getAngle() - angle) > 1) {
                    binding.car.setPivotX(carCentre.x);
                    binding.car.setPivotY(carCentre.y);
                    binding.car.setRotation(car.getAngle());
                    angle = car.getAngle();
                }
                if (Math.abs(modelCarCentre.x - carCentre.x) > 1) {
                    binding.car.setTranslationX(modelCarCentre.x - carCentre.x);
                    carCentre.x = modelCarCentre.x;
                }
                if (Math.abs(modelCarCentre.y - carCentre.y) > 1) {
                    binding.car.setTranslationY(modelCarCentre.y - carCentre.y);
                    carCentre.y = modelCarCentre.y;
                }
            }
        });
        animation.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
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
        animation.start();
    }

//    private void moveCar() {
//
//        ValueAnimator animation = ValueAnimator.ofObject(new PointFEvaluator(), carCentre,
//                tapPosition);
//
//                ObjectAnimator rotate = ObjectAnimator.ofFloat(binding.car,
//                        "rotation", currentAngle, angleToRotate);
//        rotate.setDuration(ROTATION_TIME);
//        PropertyValuesHolder pvhX = PropertyValuesHolder.ofFloat("x", x1 - carVIWidth / 2, x2 - carVIWidth / 2);
//        PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat("y", y1 - carVIHeight / 2, y2 - carVIHeight / 2);
//        ObjectAnimator move = ObjectAnimator.ofPropertyValuesHolder(binding.car, pvhX, pvhY);
//        move.setDuration(MOVEMENT_TIME);
//        AnimatorSet animatorSet = new AnimatorSet();
//        animatorSet.playSequentially(rotate, move);
//        animatorSet.addListener(new Animator.AnimatorListener() {
//            @Override
//            public void onAnimationStart(Animator animation) {
//                removeTouchListener();
//            }
//
//            @Override
//            public void onAnimationEnd(Animator animation) {
//                setTouchListener();
//            }
//
//            @Override
//            public void onAnimationCancel(Animator animation) {
//                setTouchListener();
//            }
//
//            @Override
//            public void onAnimationRepeat(Animator animation) {
//            }
//        });
//        animatorSet.start();
//
//        currentAngle = angleToRotate;
//    }
//
    private void removeTouchListener() {
        binding.space.setOnTouchListener(null);
    }
//
////    private void getRotationAngle() {
////
////        angleToRotate = (float) Math.toDegrees(Math.atan((x2 - x1) / (y1 - y2)));
////        if (x2 > x1) {
////            if (y2 > y1) { // bottom-right direction
////                if (currentAngle < -90) {
////                    angleToRotate = angleToRotate - 180;
////                } else {
////                    angleToRotate = 180 + angleToRotate;
////                }
////            }
////        } else {
////            if (y2 > y1) { // bottom-left direction
////                if (currentAngle > 90) {
////                    angleToRotate = angleToRotate + 180;
////                } else {
////                    angleToRotate = angleToRotate - 180;
////                }
////            }
////        }
////    }

    private PointF getLayoutCarPosition() {
        carCentre = new PointF();
        carCentre.x = binding.car.getX() + binding.car.getWidth() / 2;
        carCentre.y = binding.car.getY() + binding.car.getHeight() / 2;
        return carCentre;
    }
}
