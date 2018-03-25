package ru.javabreeze.movingcar;

import android.animation.TypeEvaluator;

import android.animation.TypeEvaluator;
import android.graphics.PointF;

import ru.javabreeze.movingcar.model.Car;

/**
 * This evaluator can be used to perform type interpolation between <code>PointF</code> values.
 */
public class CarPointFEvaluator implements TypeEvaluator<PointF> {

    /**
     * When null, a new PointF is returned on every evaluate call. When non-null,
     * mPoint will be modified and returned on every evaluate.
     */
    private PointF mPoint;
    private Car car;

    /**
     * Construct a PointFEvaluator that returns a new PointF on every evaluate call.
     * To avoid creating an object for each evaluate call,
     * {@link android.animation.PointFEvaluator#PointFEvaluator(android.graphics.PointF)} should be used
     * whenever possible.
     */
    public CarPointFEvaluator() {
    }

    /**
     * Constructs a PointFEvaluator that modifies and returns <code>reuse</code>
     * in {@link #evaluate(float, android.graphics.PointF, android.graphics.PointF)} calls.
     * The value returned from
     * {@link #evaluate(float, android.graphics.PointF, android.graphics.PointF)} should
     * not be cached because it will change over time as the object is reused on each
     * call.
     *
     * @param reuse A PointF to be modified and returned by evaluate.
     */
    public CarPointFEvaluator(PointF reuse) {
        mPoint = reuse;
    }

    public CarPointFEvaluator(Car car) {
        this.car = car;
    }

    /**
     * This function returns the result of linearly interpolating the start and
     * end PointF values, with <code>fraction</code> representing the proportion
     * between the start and end values. The calculation is a simple parametric
     * calculation on each of the separate components in the PointF objects
     * (x, y).
     *
     * <p>If {@link #CarPointFEvaluator(android.graphics.PointF)} was used to construct
     * this PointFEvaluator, the object returned will be the <code>reuse</code>
     * passed into the constructor.</p>
     *
     * @param fraction   The fraction from the starting to the ending values
     * @param startValue The start PointF
     * @param endValue   The end PointF
     * @return A linear interpolation between the start and end values, given the
     *         <code>fraction</code> parameter.
     */
    @Override
    public PointF evaluate(float fraction, PointF startValue, PointF endValue) {
        return car.getCarPosition();
    }
}
