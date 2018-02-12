package com.vitalityactive.va.activerewards;

import android.animation.TimeInterpolator;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.vitalityactive.va.R;

import java.lang.ref.WeakReference;

public class CircleGoalProgressView extends View {
    private static final int DEGREES_IN_CIRCLE = 360;
    private static final int START_ANGLE_OFFSET = -90;
    private final int barColorStandard = 0xff33D4ED;
    private float currentValue = 0;
    private float valueTo = 0;
    private float valueFrom = 0;
    private float maxValue = 300;
    private int layoutHeight = 0;
    private int layoutWidth = 0;
    private int barWidth;
    private int rimColor = 0xAA83d0c9;

    private int[] barColors = new int[2];
    private Paint.Cap barStrokeCap = Paint.Cap.ROUND;
    private Paint barPaint = new Paint();
    private Paint barPaintEnd = new Paint();
    private Paint circlePaint = new Paint();
    private Paint rimPaint = new Paint();
    private Paint imagePaint = new Paint();
    private Paint capPaintStart = new Paint();
    private Paint capPaintEnd = new Paint();
    private RectF circleBounds = new RectF();
    private RectF trophyCircleBounds = new RectF();
    private double animationDuration = 900;
    private int delayMillis = 15;
    private Handler animationHandler = new AnimationHandler(this);
    private AnimationState animationState = AnimationState.IDLE;
    private TimeInterpolator interpolator = new AccelerateDecelerateInterpolator();
    private Bitmap trophyBitmap;

    public CircleGoalProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);

        parseAttributes(context.obtainStyledAttributes(attrs,
                R.styleable.CircleGoalProgressView));
    }

    private void parseAttributes(TypedArray attributes) {
        Resources resources = this.getResources();

        setBarWidth((int) attributes.getDimension(R.styleable.CircleGoalProgressView_barWidth,
                barWidth));

        if (attributes.hasValue(R.styleable.CircleGoalProgressView_barColorStart) && attributes.hasValue(R.styleable.CircleGoalProgressView_barColorEnd)) {
            barColors = new int[]{attributes.getColor(R.styleable.CircleGoalProgressView_barColorStart, barColorStandard), attributes.getColor(R.styleable.CircleGoalProgressView_barColorEnd, barColorStandard)};
        }

        setTrophyBitmap(BitmapFactory.decodeResource(resources,
                attributes.getResourceId(R.styleable.CircleGoalProgressView_goalCompleteDrawable,
                        R.drawable.achieved_trophy_s)));

        setRimColor(attributes.getColor(R.styleable.CircleGoalProgressView_rimColor, rimColor));

        setMaxValue(attributes.getDimension(R.styleable.CircleGoalProgressView_goalMaxValue, maxValue));

        setupBounds();
        invalidate();

        attributes.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // The first thing that happens is that we call the superclass
        // implementation of onMeasure. The reason for that is that measuring
        // can be quite a complex process and calling the super method is a
        // convenient way to get most of this complexity handled.
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        // We can’t use getWidth() or getHeight() here. During the measuring
        // pass the view has not gotten its final size yet (this happens first
        // at the start of the layout pass) so we have to use getMeasuredWidth()
        // and getMeasuredHeight().
        int size;
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        int widthWithoutPadding = width - getPaddingLeft() - getPaddingRight();
        int heightWithoutPadding = height - getPaddingTop() - getPaddingBottom();

        // Finally we have some simple logic that calculates the size of the view
        // and calls setMeasuredDimension() to set that size.
        // Before we compare the width and height of the view, we remove the padding,
        // and when we set the dimension we add it back again. Now the actual content
        // of the view will be square, but, depending on the padding, the total dimensions
        // of the view might not be.
        if (widthWithoutPadding > heightWithoutPadding) {
            size = heightWithoutPadding;
        } else {
            size = widthWithoutPadding;
        }

        // If you override onMeasure() you have to call setMeasuredDimension().
        // This is how you report back the measured size.  If you don’t call
        // setMeasuredDimension() the parent will throw an exception and your
        // application will crash.
        // We are calling the onMeasure() method of the superclass so we don’t
        // actually need to call setMeasuredDimension() since that takes care
        // of that. However, the purpose with overriding onMeasure() was to
        // change the default behaviour and to do that we need to call
        // setMeasuredDimension() with our own values.
        setMeasuredDimension(size + getPaddingLeft() + getPaddingRight(), size + getPaddingTop() + getPaddingBottom());
    }

    @Override
    protected void onSizeChanged(int width, int height, int oldWidth, int oldHeight) {
        super.onSizeChanged(width, height, oldWidth, oldHeight);

        layoutWidth = width;
        layoutHeight = height;

        setupBounds();
        setupPaints();
        invalidate();
    }

    public void setMaxValue(float _maxValue) {
        maxValue = _maxValue;
    }

    public void setBarWidth(int barWidth) {
        this.barWidth = barWidth;
    }

    public void setRimColor(int rimColor) {
        this.rimColor = rimColor;
    }

    public void setupPaints() {
        setPaintColors(barPaint, barColors, START_ANGLE_OFFSET);
        setPaintColors(barPaintEnd, barColors, -78);

        barPaint.setAntiAlias(true);
        barPaint.setStrokeCap(barStrokeCap);
        barPaint.setStyle(Style.STROKE);
        barPaint.setStrokeWidth(barWidth);

        barPaintEnd.setAntiAlias(true);
        barPaintEnd.setStrokeCap(barStrokeCap);
        barPaintEnd.setStyle(Style.STROKE);
        barPaintEnd.setStrokeWidth(barWidth);

        initPaint(rimPaint, rimColor, barWidth, Style.STROKE);
        int fillColor = 0x00000000;
        initFillPaint(circlePaint, fillColor);

        initPaint(imagePaint, Color.WHITE, 5, Style.STROKE);

        initFillPaint(capPaintStart, barColors[0]);
        initFillPaint(capPaintEnd, barColors[barColors.length - 1]);
    }

    private void initFillPaint(Paint paint, int color) {
        paint.setColor(color);
        paint.setAntiAlias(true);
        paint.setStyle(Style.FILL);
    }

    private void initPaint(Paint paint, int color, int width, Style style) {
        paint.setColor(color);
        paint.setAntiAlias(true);
        paint.setStyle(style);
        paint.setStrokeWidth(width);
    }

    private void setPaintColors(Paint paintToChange, int[] colors, int rotation) {
        float[] positions = {0, ((float) DEGREES_IN_CIRCLE / maxValue * 300) / 360f};
        paintToChange.setShader(new SweepGradient(circleBounds.centerX(), circleBounds.centerY(), colors, positions));
        Matrix matrix = new Matrix();
        paintToChange.getShader().getLocalMatrix(matrix);

        matrix.postTranslate(-circleBounds.centerX(), -circleBounds.centerY());
        matrix.postRotate(rotation);
        matrix.postTranslate(circleBounds.centerX(), circleBounds.centerY());
        paintToChange.getShader().setLocalMatrix(matrix);
    }

    /**
     * Set the bounds of the component
     */
    private void setupBounds() {
        // Width should equal to Height, find the min value to setup the circle
        int minValue = Math.min(layoutWidth, layoutHeight);

        // Calc the Offset if needed
        int xOffset = layoutWidth - minValue;
        int yOffset = layoutHeight - minValue;

        // Add the offset
        int paddingTop = this.getPaddingTop() + (yOffset / 2);
        int paddingBottom = this.getPaddingBottom() + (yOffset / 2);
        int paddingLeft = this.getPaddingLeft() + (xOffset / 2);
        int paddingRight = this.getPaddingRight() + (xOffset / 2);

        int width = getWidth();
        int height = getHeight();

        circleBounds = new RectF(paddingLeft + barWidth,
                paddingTop + barWidth,
                width - paddingRight - barWidth,
                height - paddingBottom - barWidth);

        trophyCircleBounds = new RectF(paddingLeft + barWidth + barWidth / 2,
                paddingTop + barWidth + barWidth / 2,
                width - paddingRight - barWidth - barWidth / 2,
                height - paddingBottom - barWidth - barWidth / 2);
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float degrees = ((float) DEGREES_IN_CIRCLE / maxValue * currentValue);

        //Draw the inner circle
        canvas.drawArc(circleBounds, DEGREES_IN_CIRCLE, DEGREES_IN_CIRCLE, false, circlePaint);
        //Draw the rim
        if (barWidth > 0) {
            canvas.drawArc(circleBounds, DEGREES_IN_CIRCLE, DEGREES_IN_CIRCLE, false, rimPaint);
        }

        drawCircleWithNumber(canvas, degrees);

        // Draw trophy if necessary
        if (currentValue >= maxValue && maxValue > 0.000001f) {
            canvas.drawBitmap(trophyBitmap, null, trophyCircleBounds, imagePaint);
            canvas.drawArc(trophyCircleBounds, DEGREES_IN_CIRCLE, DEGREES_IN_CIRCLE, false, imagePaint);
        }
    }

    private void drawCircleWithNumber(Canvas canvas, float _degrees) {
        if (_degrees % DEGREES_IN_CIRCLE == 0 && _degrees >= DEGREES_IN_CIRCLE) {
            _degrees = 359;
        } else {
            _degrees = _degrees % DEGREES_IN_CIRCLE;
        }

        canvas.drawArc(circleBounds, START_ANGLE_OFFSET, _degrees, false, barPaint);
        // Draw the round cap at the start manually, otherwise the left half is
        // drawn in the last color, not first
        canvas.drawCircle(circleBounds.centerX(),
                circleBounds.top,
                barWidth / 2, currentValue < maxValue ? capPaintStart : capPaintEnd);

        if (_degrees > 180) {
            canvas.drawArc(circleBounds, -1 * START_ANGLE_OFFSET, _degrees - 180, false, barPaintEnd);
        }
    }

    public void setTrophyBitmap(Bitmap trophyBitmap) {
        this.trophyBitmap = trophyBitmap;
    }

    /**
     * Set the value of the circle view without an animation.
     * Stops any currently active animations.
     */
    public void setValue(float value) {

        Message msg = new Message();
        msg.what = AnimationMsg.SET_VALUE.ordinal();
        msg.obj = new float[]{value, value};
        animationHandler.sendMessage(msg);
    }

    /**
     * Sets the value of the circle view with an animation.
     *
     * @param _valueTo           value after animation
     * @param _animationDuration the duration of the animation
     */
    public void setValueAnimated(float _valueTo, long _animationDuration) {
        setValueAnimated(0, _valueTo, _animationDuration);
    }

    public void setValueAnimated(float valueFrom, float valueTo, long animationDuration) {
        this.animationDuration = animationDuration;
        Message msg = new Message();
        msg.what = AnimationMsg.SET_VALUE_ANIMATED.ordinal();
        msg.obj = new float[]{valueFrom, valueTo};
        animationHandler.sendMessage(msg);
    }

    private enum AnimationState {
        IDLE,
        ANIMATING
    }

    private enum AnimationMsg {
        SET_VALUE,
        SET_VALUE_ANIMATED,
        TICK
    }

    private static class AnimationHandler extends Handler {
        private final WeakReference<CircleGoalProgressView> mCircleViewWeakReference;
        private long mAnimationStartTime;

        AnimationHandler(CircleGoalProgressView _circleView) {
            super(_circleView.getContext().getMainLooper());
            mCircleViewWeakReference = new WeakReference<>(_circleView);
        }

        @Override
        public void handleMessage(Message msg) {
            CircleGoalProgressView circleView = mCircleViewWeakReference.get();
            if (circleView == null) {
                return;
            }
            AnimationMsg msgType = AnimationMsg.values()[msg.what];
            if (msgType == AnimationMsg.TICK)
                // necessary to remove concurrent ticks.
                removeMessages(AnimationMsg.TICK.ordinal());

            switch (circleView.animationState) {
                case IDLE:
                    switch (msgType) {
                        case SET_VALUE:
                            setValue(msg, circleView);
                            break;
                        case SET_VALUE_ANIMATED:

                            enterSetValueAnimated(msg, circleView);
                            break;
                        case TICK:
                            // remove old ticks
                            removeMessages(AnimationMsg.TICK.ordinal());
                            //IGNORE nothing to do
                            break;
                    }
                    break;
                case ANIMATING:
                    switch (msgType) {
                        case SET_VALUE:
                            setValue(msg, circleView);
                            break;
                        case SET_VALUE_ANIMATED:
                            mAnimationStartTime = System.currentTimeMillis();
                            //restart animation from current value
                            circleView.valueFrom = circleView.currentValue;
                            circleView.valueTo = ((float[]) msg.obj)[1];

                            break;
                        case TICK:
                            if (calcNextAnimationValue(circleView)) {
                                //animation finished
                                circleView.animationState = AnimationState.IDLE;
                                circleView.currentValue = circleView.valueTo;
                            }
                            circleView.animationHandler.sendEmptyMessageDelayed(AnimationMsg.TICK.ordinal(), circleView.delayMillis);
                            circleView.invalidate();
                            break;
                    }
                    break;
            }
        }

        private void enterSetValueAnimated(Message msg, CircleGoalProgressView _circleView) {
            _circleView.valueFrom = ((float[]) msg.obj)[0];
            _circleView.valueTo = ((float[]) msg.obj)[1];
            mAnimationStartTime = System.currentTimeMillis();
            _circleView.animationState = AnimationState.ANIMATING;

            _circleView.animationHandler.sendEmptyMessageDelayed(AnimationMsg.TICK.ordinal(), _circleView.delayMillis);
        }

        /**
         * *
         *
         * @param _circleView the circle view
         * @return false if animation still running, true if animation is finished.
         */
        private boolean calcNextAnimationValue(CircleGoalProgressView _circleView) {
            float t = (float) ((System.currentTimeMillis() - mAnimationStartTime)
                    / _circleView.animationDuration);
            t = t > 1.0f ? 1.0f : t;
            float interpolatedRatio = _circleView.interpolator.getInterpolation(t);

            _circleView.currentValue = (_circleView.valueFrom + ((_circleView.valueTo - _circleView.valueFrom) * interpolatedRatio));

            return t >= 1;
        }

        private void setValue(Message msg, CircleGoalProgressView _circleView) {
            _circleView.valueFrom = _circleView.valueTo;
            _circleView.currentValue = _circleView.valueTo = ((float[]) msg.obj)[0];
            _circleView.animationState = AnimationState.IDLE;
            _circleView.invalidate();
        }
    }

}
