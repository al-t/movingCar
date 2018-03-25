package ru.javabreeze.movingcar.model;

import android.graphics.PointF;

public class Car {

    private final static float power = 1.f;
    private final static float wheelPower = 1.f; // how quick the car's steering wheel can be
    // turned
    private PointF centre;
    private PointF destination;
    private float angle; // angle between Oy and car axis
    private float linear_velocity;
    private float maxLinearVelocity;

    public Car(float maxLinearVelocity) {
        this.maxLinearVelocity = maxLinearVelocity;
    }

    public Car(float maxLinearVelocity, PointF centre) {
        this(maxLinearVelocity);
        this.centre = centre;
    }

    public Car(float maxLinearVelocity, PointF centre, float angle) {
        this(maxLinearVelocity, centre);
        this.angle = angle;
    }

    public void setDestination(PointF destination) {
        this.destination = destination;
    }

    public void accelerate() {
        if (linear_velocity < maxLinearVelocity) {
            linear_velocity += power;
        }
    }

    public void decelerate() {
        if (linear_velocity > power) {
            linear_velocity -= power;
        } else {
            linear_velocity = 0;
        }
    }

    public void turnRight() {
        angle += wheelPower * linear_velocity;
        if (angle > 180) {
            angle = angle - 360;
        }
    }

    public void turnLeft() {
        angle -= wheelPower * linear_velocity;
        if (angle < 180) {
            angle = angle + 360;
        }
    }

    public void move() {
        navigateCar();
        centre.x += linear_velocity * Math.sin(Math.toRadians(angle));
        centre.y += linear_velocity * Math.cos(Math.toRadians(angle));
    }

    private void navigateCar() {
        float distance = getDistance();
        if ((linear_velocity / power) < (distance / linear_velocity)) {
            accelerate();
        } else {
            decelerate();
        }

        float angleDiff = getRotationAngle() - angle;
        if (angleDiff == 0) return;
        if (angleDiff > 0) {
            if (angleDiff < 180) {
                turnRight();
            } else {
                turnLeft();
            }
        } else {
            if (angleDiff > -180) {
                turnLeft();
            } else {
                turnRight();
            }
        }
    }

    public PointF getCarPosition() {
        return centre;
    }

    public float getAngle() {
        return angle;
    }

    public float getDistance() {
        return (float)Math.pow(
                Math.pow(centre.x - destination.x, 2) + Math.pow(centre.y - destination.y, 2),
                0.5);
    }

    private float getRotationAngle() {
        float angleToRotate = (float)Math.toDegrees(Math.atan(
                (destination.x - centre.x) / (centre .y - destination.y)));
        if (destination.x > centre.x) {
            if (destination.y > centre.y) { // bottom-right direction
                angleToRotate += 180;
            }
        } else {
            if (destination.y > centre.y) { // bottom-left direction
                angleToRotate -= 180;
            }
        }
        return angleToRotate;
    }
}
