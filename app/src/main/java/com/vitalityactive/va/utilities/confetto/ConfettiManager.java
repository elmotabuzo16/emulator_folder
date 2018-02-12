package com.vitalityactive.va.utilities.confetto;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

/**
 * A helper manager class for configuring a set of confetti and displaying them on the UI.
 */
public class ConfettiManager {
    static final long INFINITE_DURATION = Long.MAX_VALUE;

    private final Random random = new Random();
    private final ConfettoGenerator confettoGenerator;
    private final ConfettiSource confettiSource;
    private final ViewGroup parentView;
    private final ConfettiView confettiView;

    private final Queue<Confetto> recycledConfetti = new LinkedList<>();
    private final List<Confetto> confetti = new ArrayList<>(300);
    private ValueAnimator animator;
    private long lastEmittedTimestamp;

    // All of the below configured values are in milliseconds despite the setter methods take them
    // in seconds as the parameters. The parameters for the setters are in seconds to allow for
    // users to better understand/visualize the dimensions.

    // Configured attributes for the entire confetti group
    private int numInitialCount;
    private long emissionDuration;
    private float emissionRate, emissionRateInverse;
    private Rect bound;

    // Configured attributes for each confetto
    private float velocityX, velocityDeviationX;
    private float velocityY, velocityDeviationY;
    private int initialRotation, initialRotationDeviation;
    private float rotationalAcceleration, rotationalAccelerationDeviation;
    private Float targetRotationalVelocity, targetRotationalVelocityDeviation;
    private long ttl;

    ConfettiManager(Context context, ConfettoGenerator confettoGenerator,
                    ConfettiSource confettiSource, ViewGroup parentView) {
        this(confettoGenerator, confettiSource, parentView, ConfettiView.newInstance(context));
    }

    private ConfettiManager(ConfettoGenerator confettoGenerator,
                            ConfettiSource confettiSource, ViewGroup parentView, ConfettiView confettiView) {
        this.confettoGenerator = confettoGenerator;
        this.confettiSource = confettiSource;
        this.parentView = parentView;
        this.confettiView = confettiView;
        this.confettiView.bind(confetti);

        this.confettiView.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
            @Override
            public void onViewAttachedToWindow(View v) {
            }

            @Override
            public void onViewDetachedFromWindow(View v) {
                terminate();
            }
        });

        // Set the defaults
        this.ttl = -1;
        this.bound = new Rect(0, 0, parentView.getWidth(), parentView.getHeight());
    }

    /**
     * The number of confetti initially emitted before any time has elapsed.
     *
     * @param numInitialCount the number of initial confetti.
     * @return the confetti manager so that the set calls can be chained.
     */
    ConfettiManager setNumInitialCount(int numInitialCount) {
        this.numInitialCount = numInitialCount;
        return this;
    }

    /**
     * Configures how long this manager will emit new confetti after the animation starts.
     *
     * @param emissionDurationInMillis how long to emit new confetti in millis. This value can be
     *   {@link #INFINITE_DURATION} for a never-ending emission.
     * @return the confetti manager so that the set calls can be chained.
     */
    ConfettiManager setEmissionDuration(long emissionDurationInMillis) {
        this.emissionDuration = emissionDurationInMillis;
        return this;
    }

    /**
     * Configures how frequently this manager will emit new confetti after the animation starts
     * if {@link #emissionDuration} is a positive value.
     *
     * @param emissionRate the rate of emission in # of confetti per second.
     * @return the confetti manager so that the set calls can be chained.
     */
    ConfettiManager setEmissionRate(float emissionRate) {
        this.emissionRate = emissionRate / 1000f;
        this.emissionRateInverse = 1f / this.emissionRate;
        return this;
    }

    /**
     * Set the velocityX used by this manager. This value defines the initial X velocity
     * for the generated confetti. The actual confetti's X velocity will be
     * (velocityX +- [0, velocityDeviationX]).
     *
     * @param velocityX the X velocity in pixels per second.
     * @param velocityDeviationX the deviation from X velocity in pixels per second.
     * @return the confetti manager so that the set calls can be chained.
     */
    ConfettiManager setVelocityX(float velocityX, float velocityDeviationX) {
        this.velocityX = velocityX / 1000f;
        this.velocityDeviationX = velocityDeviationX / 1000f;
        return this;
    }

    /**
     * Set the velocityY used by this manager. This value defines the initial Y velocity
     * for the generated confetti. The actual confetti's Y velocity will be
     * (velocityY +- [0, velocityDeviationY]). A positive Y velocity means that the velocity
     * is going down (because Y coordinate increases going down).
     *
     * @param velocityY the Y velocity in pixels per second.
     * @param velocityDeviationY the deviation from Y velocity in pixels per second.
     * @return the confetti manager so that the set calls can be chained.
     */
    ConfettiManager setVelocityY(float velocityY, float velocityDeviationY) {
        this.velocityY = velocityY / 1000f;
        this.velocityDeviationY = velocityDeviationY / 1000f;
        return this;
    }

    /**
     * Set the initialRotation used by this manager. This value defines the initial rotation in
     * degrees for the generated confetti. The actual confetti's initial rotation will be
     * (initialRotation +- [0, initialRotationDeviation]).
     *
     * @param initialRotation the initial rotation in degrees.
     * @param initialRotationDeviation the deviation from initial rotation in degrees.
     * @return the confetti manager so that the set calls can be chained.
     */
    ConfettiManager setInitialRotation(int initialRotation, int initialRotationDeviation) {
        this.initialRotation = initialRotation;
        this.initialRotationDeviation = initialRotationDeviation;
        return this;
    }

    /**
     * Set the rotationalAcceleration used by this manager. This value defines the the
     * acceleration of the rotation for the generated confetti. The actual confetti's rotational
     * acceleration will be (rotationalAcceleration +- [0, rotationalAccelerationDeviation]).
     *
     * @param rotationalAcceleration the rotational acceleration in degrees per second^2.
     * @param rotationalAccelerationDeviation the deviation from rotational acceleration in degrees
     *   per second^2.
     * @return the confetti manager so that the set calls can be chained.
     */
    ConfettiManager setRotationalAcceleration(float rotationalAcceleration,
                                              float rotationalAccelerationDeviation) {
        this.rotationalAcceleration = rotationalAcceleration / 1000000f;
        this.rotationalAccelerationDeviation = rotationalAccelerationDeviation / 1000000f;
        return this;
    }

    /**
     * @see #setTargetRotationalVelocity(float, float)
     *
     * @param targetRotationalVelocity the target rotational velocity in degrees per second.
     * @return the confetti manager so that the set calls can be chained.
     */
    ConfettiManager setTargetRotationalVelocity(float targetRotationalVelocity) {
        return setTargetRotationalVelocity(targetRotationalVelocity, 100f);
    }

    /**
     * Set the target rotational velocity that confetti can reach during the animation. The actual
     * confetti's target rotational velocity will be
     * (targetRotationalVelocity +- [0, targetRotationalVelocityDeviation]).
     *
     * @param targetRotationalVelocity the target rotational velocity in degrees per second.
     * @param targetRotationalVelocityDeviation the deviation from target rotational velocity
     *   in degrees per second.
     * @return the confetti manager so that the set calls can be chained.
     */
    private ConfettiManager setTargetRotationalVelocity(float targetRotationalVelocity,
                                                        float targetRotationalVelocityDeviation) {
        this.targetRotationalVelocity = targetRotationalVelocity / 1000f;
        this.targetRotationalVelocityDeviation = targetRotationalVelocityDeviation / 1000f;
        return this;
    }

    /**
     * Start the confetti animation configured by this manager.
     *
     * @return the confetti manager itself that just started animating.
     */
    ConfettiManager animate() {
        cleanupExistingAnimation();
        attachConfettiViewToParent();
        addNewConfetti(numInitialCount, 0);
        startNewAnimation();
        return this;
    }

    /**
     * Terminate the currently running animation if there is any.
     */
    public void terminate() {
        if (animator != null) {
            animator.cancel();
        }
        confettiView.terminate();
    }

    private void cleanupExistingAnimation() {
        if (animator != null) {
            animator.cancel();
        }

        lastEmittedTimestamp = 0;
        final Iterator<Confetto> iterator = confetti.iterator();
        while (iterator.hasNext()) {
            removeConfetto(iterator.next());
            iterator.remove();
        }
    }

    private void attachConfettiViewToParent() {
        final ViewParent currentParent = confettiView.getParent();
        if (currentParent != null) {
            if (currentParent != parentView) {
                ((ViewGroup) currentParent).removeView(confettiView);
                parentView.addView(confettiView, 0);
            }
        } else {
            parentView.addView(confettiView, 0);
        }

        confettiView.reset();
    }

    private void addNewConfetti(int numConfetti, long initialDelay) {
        for (int i = 0; i < numConfetti; i++) {
            Confetto confetto = recycledConfetti.poll();
            if (confetto == null) {
                confetto = confettoGenerator.generateConfetto(random);
            }
            configureConfetto(confetto, confettiSource, random, initialDelay);
            addConfetto(confetto);
        }
    }

    private void startNewAnimation() {
        // Never-ending animator, we will cancel once the termination condition is reached.
        animator = ValueAnimator.ofInt(0)
                .setDuration(Long.MAX_VALUE);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                final long elapsedTime = valueAnimator.getCurrentPlayTime();
                processNewEmission(elapsedTime);
                updateConfetti(elapsedTime);

                if (confetti.size() == 0 && elapsedTime >= emissionDuration) {
                    terminate();
                } else {
                    confettiView.invalidate();
                }
            }
        });

        animator.start();
    }

    private void processNewEmission(long elapsedTime) {
        if (elapsedTime < emissionDuration) {
            if (lastEmittedTimestamp == 0) {
                lastEmittedTimestamp = elapsedTime;
            } else {
                final long timeSinceLastEmission = elapsedTime - lastEmittedTimestamp;

                // Randomly determine how many confetti to emit
                final int numNewConfetti = (int)
                        (random.nextFloat() * emissionRate * timeSinceLastEmission);
                if (numNewConfetti > 0) {
                    lastEmittedTimestamp += emissionRateInverse * numNewConfetti;
                    addNewConfetti(numNewConfetti, elapsedTime);
                }
            }
        }
    }

    private void updateConfetti(long elapsedTime) {
        final Iterator<Confetto> iterator = confetti.iterator();
        while (iterator.hasNext()) {
            final Confetto confetto = iterator.next();
            if (!confetto.applyUpdate(elapsedTime)) {
                iterator.remove();
                removeConfetto(confetto);
            }
        }
    }

    private void addConfetto(Confetto confetto) {
        this.confetti.add(confetto);
    }

    private void removeConfetto(Confetto confetto) {
        recycledConfetti.add(confetto);
    }

    private void configureConfetto(Confetto confetto, ConfettiSource confettiSource,
                                   Random random, long initialDelay) {
        confetto.reset();

        confetto.setInitialDelay(initialDelay);
        confetto.setInitialX(confettiSource.getInitialX(random.nextFloat()));
        confetto.setInitialY(confettiSource.getInitialY(random.nextFloat()));
        confetto.setInitialVelocityX(getVarianceAmount(velocityX, velocityDeviationX, random));
        confetto.setInitialVelocityY(getVarianceAmount(velocityY, velocityDeviationY, random));
        confetto.setInitialRotation(
                getVarianceAmount(initialRotation, initialRotationDeviation, random));
        confetto.setRotationalAcceleration(
                getVarianceAmount(rotationalAcceleration, rotationalAccelerationDeviation, random));
        confetto.setTargetRotationalVelocity(targetRotationalVelocity == null ? null
                : getVarianceAmount(new Random().nextBoolean() ? targetRotationalVelocity : -targetRotationalVelocity, targetRotationalVelocityDeviation,
                        random));
        confetto.setTTL(ttl);
        confetto.prepare(bound);
    }

    private float getVarianceAmount(float base, float deviation, Random random) {
        // Normalize random to be [-1, 1] rather than [0, 1]
        return base + (deviation * (random.nextFloat() * 2 - 1));
    }
}
