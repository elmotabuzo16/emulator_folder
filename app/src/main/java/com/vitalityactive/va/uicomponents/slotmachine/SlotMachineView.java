package com.vitalityactive.va.uicomponents.slotmachine;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GestureDetectorCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.Scroller;

import com.vitalityactive.va.activerewards.rewards.content.RewardPartnerContent;
import com.vitalityactive.va.activerewards.rewards.dto.RewardSelectionDTO;
import com.vitalityactive.va.utilities.ViewUtilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class SlotMachineView extends View implements Runnable {
    private static final double VISIBLE_ITEM_COUNT = 5.0;
    private static final String TAG = "SlotMachineView";
    private Paint backgroundPaint;
    private Rect fullViewRectangle;
    private int itemHeight;
    @NonNull
    private List<RewardSelectionDTO> items = new ArrayList<>();
    private SlotMachineItemInflater inflater;
    private int scrollOffset = 0;
    private GestureDetectorCompat gestureDetector;
    private int minimumVelocityForFlingDetection;
    private FlingListener flingListener;
    private Drawable selectorHighlight;
    private int selectedItemTopOffset;
    private GestureListener gestureListener;
    private boolean isAnimatedScrollBusy;
    private SlotMachineScroller scroller;
    private FinishScrollListener finishScrollListener;

    private Camera mCamera;
    private Matrix mMatrixRotate, mMatrixDepth;
    private int mDrawnCenterX, mDrawnCenterY;
    private int mWheelCenterX, mWheelCenterY;
    private int mItemHeight, mHalfItemHeight;
    private int mHalfWheelHeight;
    private int mVisibleItemCount = 9, mDrawnItemCount;
    private int mHalfDrawnItemCount;
    private int mScrollOffsetY;
    private int mSelectedItemPosition;
    private Map<String, Bitmap> slots;
    private int mMinFlingY, mMaxFlingY;

    private final Handler mHandler = new Handler();
    private VelocityTracker mTracker;
    private Scroller mScroller;
    private int mMinimumVelocity = 50, mMaximumVelocity = 8000;
    private int mTouchSlop = 8;
    private boolean isClick;
    private boolean isTouchTriggered;
    private boolean isForceFinishScroll;
    private int mDownPointY;
    private int mLastPointY;
    private int mCurrentItemPosition;
    private int desiredItemPositionAfterRotation = 0;

    private OnItemSelectedListener mOnItemSelectedListener;
    private boolean isFlingingProgrammatically;

    public void setOnItemSelectedListener(OnItemSelectedListener onItemSelectedListener) {
        this.mOnItemSelectedListener = onItemSelectedListener;
    }

    public SlotMachineView(Context context) {
        super(context);
        initialize(context);
    }

    public SlotMachineView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context);
    }

    public SlotMachineView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(context);
    }

    private void initialize(Context context) {
        slots = new HashMap<>();
        backgroundPaint = new Paint();
        backgroundPaint.setColor(ContextCompat.getColor(getContext(), android.R.color.white));
        fullViewRectangle = new Rect();
        gestureListener = new GestureListener();
        gestureDetector = new GestureDetectorCompat(context, gestureListener);
        scroller = new SlotMachineScrollerWithBounce(this);

        mScroller = new Scroller(getContext());

        ViewConfiguration conf = ViewConfiguration.get(getContext());
        mMinimumVelocity = conf.getScaledMinimumFlingVelocity();
        mMaximumVelocity = conf.getScaledMaximumFlingVelocity();
        mTouchSlop = conf.getScaledTouchSlop();

        updateVisibleItemCount();

        mCamera = new Camera();
        mMatrixRotate = new Matrix();
        mMatrixDepth = new Matrix();

    }

    public void setItems(@NonNull List<RewardSelectionDTO> items) {
        this.items = items;

        if (mSelectedItemPosition > items.size() - 1 || mCurrentItemPosition > items.size() - 1) {
            mSelectedItemPosition = mCurrentItemPosition = items.size() - 1;
        } else {
            mSelectedItemPosition = mCurrentItemPosition;
        }
        mScrollOffsetY = 0;
        computeFlingLimitY();
        requestLayout();
        invalidate();
    }

    public void setInflater(SlotMachineItemInflater inflater) {
        this.inflater = inflater;
    }

    public void setSelectorHighlight(Drawable selectorHighlight) {
        this.selectorHighlight = selectorHighlight;
    }

    public void setFinishScrollListener(FinishScrollListener finishScrollListener) {
        this.finishScrollListener = finishScrollListener;
    }

    public void setupFlingListener(int minimumVelocityForFlingDetection, FlingListener flingListener) {
        this.minimumVelocityForFlingDetection = minimumVelocityForFlingDetection;
        this.flingListener = flingListener;
    }

    private int getCurrentItemIndex() {
        return scrollOffset / itemHeight;
    }

    public void scroll(int distance) {
        int ribbonHeight = getSlotWheelRibbonHeight();
        scrollOffset = (scrollOffset + distance) % ribbonHeight;
        while (scrollOffset < 0) {
            scrollOffset += ribbonHeight;
        }
        Log.d(TAG, String.format("scrollOffset: %d, ribbonHeight: %d", scrollOffset, ribbonHeight));
        invalidate();
    }

    public void startScroll(int distance, int milliSeconds) {
        Log.d(TAG, String.format("start scrolling %d over %d msec", distance, milliSeconds));
        isAnimatedScrollBusy = true;
        scroller.startScroll(distance, milliSeconds);
    }

    public void cancelSpin() {
        if (!isAnimatedScrollBusy)
            return;

        Log.d(TAG, "canceling active spin");
        isAnimatedScrollBusy = false;
        scroller.cancelSpin();
    }

    public void onScrollFinished() {
        isAnimatedScrollBusy = false;
        if (finishScrollListener != null) {
            finishScrollListener.onScrollFinished(this, getCurrentItemIndex());
        }
    }

    public int calculateDistanceToCompleteLoops(int loopCount) {
        return getSlotWheelRibbonHeight() * loopCount;
    }

    public int calculateDistanceToCenterItem(int itemIndex) {
        int wantedOffset = itemHeight * itemIndex;
        int distance = wantedOffset - scrollOffset;
        Log.d(TAG, String.format("scrollOffset: %d, wantedOffset: %d, distance to center: %d",
                scrollOffset, wantedOffset, distance));
        return distance;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        fullViewRectangle.right = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
        fullViewRectangle.bottom = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);
        itemHeight = (int) (fullViewRectangle.bottom / VISIBLE_ITEM_COUNT);
        selectedItemTopOffset = calculateSelectedItemTopOffset();
        setMeasuredDimension(fullViewRectangle.right, fullViewRectangle.bottom);
    }

    private int calculateSelectedItemTopOffset() {
        double halfTheOtherItems = (VISIBLE_ITEM_COUNT - 1) / 2;
        return (int) (halfTheOtherItems * itemHeight) - ViewUtilities.pxFromDp(12);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawBackground(canvas);
        if (!items.isEmpty()) {
            drawItems(canvas);
        }
        if (selectorHighlight != null) {
            drawSelector(canvas);
        }
    }

    private void drawBackground(Canvas canvas) {
        canvas.drawRect(fullViewRectangle, backgroundPaint);
    }

    private void drawSelector(Canvas canvas) {
        selectorHighlight.setBounds(0, selectedItemTopOffset, fullViewRectangle.right,
                selectedItemTopOffset + getItemHeightForRender()/* + ViewUtilities.pxFromDp(2)*/);
        selectorHighlight.draw(canvas);
    }

    private int getSlotWheelRibbonHeight() {
        if (items == null || items.size() == 0)
            return 1;       // avoid div 0 issues

        return items.size() * itemHeight;
    }

    public class GestureListener implements GestureDetector.OnGestureListener {
        public boolean isScrolling;

        @Override
        public boolean onDown(MotionEvent motionEvent) {
            return true;
        }

        @Override
        public void onShowPress(MotionEvent motionEvent) {
        }

        @Override
        public boolean onSingleTapUp(MotionEvent motionEvent) {
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float velocityY) {
            scroll((int) velocityY);
            isScrolling = true;
            return true;
        }

        @Override
        public void onLongPress(MotionEvent motionEvent) {
        }

        @Override
        public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float velocityY) {
            if (Math.abs(velocityY) < minimumVelocityForFlingDetection || flingListener == null)
                return false;

            isScrolling = false;
            Log.d(TAG, "fling " + velocityY);
            flingListener.onFling(SlotMachineView.this, velocityY);
            return true;
        }
    }

    public interface FlingListener {
        void onFling(SlotMachineView view, float velocityY);
    }

    public interface FinishScrollListener {
        void onScrollFinished(SlotMachineView slotMachineView, int selectedItemIndex);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isFlingingProgrammatically) {
            return false;
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isTouchTriggered = true;
                if (null != getParent()) {
                    getParent().requestDisallowInterceptTouchEvent(true);
                }
                if (null == mTracker) {
                    mTracker = VelocityTracker.obtain();
                } else {
                    mTracker.clear();
                }
                mTracker.addMovement(event);
                if (!mScroller.isFinished()) {
                    mScroller.abortAnimation();
                    isForceFinishScroll = true;
                }
                mDownPointY = mLastPointY = Math.round(event.getY());
                break;
            case MotionEvent.ACTION_MOVE:
                if (Math.abs(mDownPointY - event.getY()) < mTouchSlop) {
                    isClick = true;
                    break;
                }
                isClick = false;
                mTracker.addMovement(event);
                float move = event.getY() - mLastPointY;
                if (Math.abs(move) < 1) {
                    break;
                }
                mScrollOffsetY += move;
                mLastPointY = Math.round(event.getY());
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                if (null != getParent()) {
                    getParent().requestDisallowInterceptTouchEvent(false);
                }
                if (isClick && !isForceFinishScroll) {
                    break;
                }
                mTracker.addMovement(event);
                mTracker.computeCurrentVelocity(1000, mMaximumVelocity);

                // Judges the WheelPicker is scroll or fling base on current velocity
                isForceFinishScroll = false;
                int velocity = Math.round(mTracker.getYVelocity());
                if (Math.abs(velocity) > mMinimumVelocity) {
                    mScroller.fling(0, mScrollOffsetY, 0, velocity, 0, 0, mMinFlingY, mMaxFlingY);
                    mScroller.setFinalY(mScroller.getFinalY() +
                            computeDistanceToEndPoint(mScroller.getFinalY() % mItemHeight));
                } else {
                    mScroller.startScroll(0, mScrollOffsetY, 0,
                            computeDistanceToEndPoint(mScrollOffsetY % mItemHeight));
                }
                // Correct coordinates
                if (mScroller.getFinalY() > mMaxFlingY) {
                    mScroller.setFinalY(mMaxFlingY);
                } else if (mScroller.getFinalY() < mMinFlingY) {
                    mScroller.setFinalY(mMinFlingY);
                }
                mHandler.post(this);
                if (null != mTracker) {
                    mTracker.recycle();
                    mTracker = null;
                }
                break;
            case MotionEvent.ACTION_CANCEL:
                if (null != getParent()) {
                    getParent().requestDisallowInterceptTouchEvent(false);
                }
                if (null != mTracker) {
                    mTracker.recycle();
                    mTracker = null;
                }
                break;
        }
        return true;
    }

    public void setDesiredItemPositionAfterRotation(int desiredItemPositionAfterRotation){
        this.desiredItemPositionAfterRotation = desiredItemPositionAfterRotation;
    }

    public void flingProgrammatically() {
        isFlingingProgrammatically = true;
        Random rand = new Random();
        mDownPointY = -rand.nextInt(getHeight() / 3) + getHeight() / 2;
        int velocity = mMaximumVelocity / 3;
        mScroller.fling(0, 0, 0, velocity, 0, 0, mMinFlingY, mMaxFlingY);
        mScroller.setFinalY(getFinalYToHitDesiredItem(mScroller.getFinalY()));
        // Correct coordinates
        if (mScroller.getFinalY() > mMaxFlingY) {
            mScroller.setFinalY(mMaxFlingY);
        } else if (mScroller.getFinalY() < mMinFlingY) {
            mScroller.setFinalY(mMinFlingY);
        }
        mHandler.post(this);
    }

    private int getFinalYToHitDesiredItem(int originalStopPosition){
        int itemsCount = originalStopPosition % mItemHeight;
        int fullRevs = Math.max(itemsCount % items.size(), 100);
//        if(fullRevs <= 1){ // minimum 2 full revs for acceptable animation effect
//            fullRevs++;
//        }
        return (fullRevs * items.size() + (items.size() - desiredItemPositionAfterRotation)) * mItemHeight;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldW, int oldH) {
        fullViewRectangle.set(getPaddingLeft(), getPaddingTop(), getWidth() - getPaddingRight(),
                getHeight() - getPaddingBottom());
        mWheelCenterX = fullViewRectangle.centerX();
        mWheelCenterY = fullViewRectangle.centerY();

        mHalfWheelHeight = fullViewRectangle.height() / 2;

        mItemHeight = fullViewRectangle.height() / mVisibleItemCount;
        mHalfItemHeight = mItemHeight / 2;
        computeDrawnCenter();

        computeFlingLimitY();
    }

    private float rotationCorrection = 1.45f; // /
    private float depthCorrection = 1.25f; // /
    private float spaceCorrection = 1.17f; // *

    private void drawItems(Canvas canvas) {
        int drawnDataStartPos = -mScrollOffsetY / mItemHeight - mHalfDrawnItemCount;
        for (int drawnDataPos = drawnDataStartPos + mSelectedItemPosition,
             drawnOffsetPos = -mHalfDrawnItemCount;
             drawnDataPos < drawnDataStartPos + mSelectedItemPosition + mDrawnItemCount;
             drawnDataPos++, drawnOffsetPos++) {
            int actualPos = drawnDataPos % items.size();
            actualPos = actualPos < 0 ? (actualPos + items.size()) : actualPos;

            backgroundPaint.setStyle(Paint.Style.FILL);
            int mDrawnItemCenterY = mDrawnCenterY + (drawnOffsetPos * mItemHeight) +
                    mScrollOffsetY % mItemHeight;

            int distanceToCenter = 0;
            float ratio = ((float) (mDrawnCenterY - Math.abs(mDrawnCenterY - mDrawnItemCenterY) - fullViewRectangle.top)) /
                    ((float) (mDrawnCenterY - fullViewRectangle.top));

            // Correct unit
            int unit = 0;
            if (mDrawnItemCenterY > mDrawnCenterY) {
                unit = 1;
            } else if (mDrawnItemCenterY < mDrawnCenterY) {
                unit = -1;
            }

            float degree = (-(1 - ratio) * 90 * unit);
            float rotationDegree = 0;
            float spaceDegree = 0;
            float depthDegree = 0;

            if (Math.abs(drawnOffsetPos) >= 4) {
                rotationDegree = 90 * unit;
                spaceDegree = 90 * unit;
                depthDegree = 90 * unit;
            } else {
                rotationDegree = computeRotation(degree);
                spaceDegree = computeSpace(Math.round(degree));
                depthDegree = computeDepth(Math.round(degree));
            }

            if (degree < -90) degree = -90;
            if (degree > 90) degree = 90;

            distanceToCenter = (int) spaceDegree;

            if (items.get(actualPos).rewardName.equalsIgnoreCase("amazon") && Math.abs(degree) < 90) {
                Log.d(">>>", "position " + drawnOffsetPos + " degree = " + degree +
                        ", depth = " + computeDepth(Math.round(degree)) +
                        ", space = " + computeSpace(Math.round(degree)) + ", ratio " + ratio);
            }
            int transX = mWheelCenterX;
            int transY = mWheelCenterY - distanceToCenter;

            mCamera.save();
            mCamera.rotateX(rotationDegree);
            mCamera.getMatrix(mMatrixRotate);
            mCamera.restore();
            mMatrixRotate.preTranslate(-transX, -transY);
            mMatrixRotate.postTranslate(transX, transY);

            mCamera.save();
            mCamera.translate(0, computeYDepth(Math.round(degree)), depthDegree);
            mCamera.getMatrix(mMatrixDepth);
            mCamera.restore();
            mMatrixDepth.preTranslate(-transX, -transY);
            mMatrixDepth.postTranslate(transX, transY);

            mMatrixRotate.postConcat(mMatrixDepth);

            // Correct item's drawn centerY base on curved state
            int drawnCenterY = mDrawnCenterY - distanceToCenter;

            canvas.save();
            canvas.clipRect(fullViewRectangle);
            canvas.concat(mMatrixRotate);
            if (!slots.containsKey(items.get(actualPos).rewardName + items.get(actualPos).rewardDescription)) {
                WheelItemView view = generateWheelItemView(items.get(actualPos).rewardName, items.get(actualPos).rewardDescription, RewardPartnerContent.fromRewardId(items.get(actualPos).rewardId).getLogoResourceId());
                slots.put(items.get(actualPos).rewardName + items.get(actualPos).rewardDescription,
                        loadBitmapFromView(view));
            }
            if (Math.abs(degree) > 45) {
                float correction = Math.abs(degree) - 45;
                if (correction > 30) {
                    correction = 30f;
                }
                correction = correction / 600f;

                Rect rect = new Rect();
                rect.left = Math.round((float) fullViewRectangle.right * correction);
                rect.right = Math.round((float) fullViewRectangle.right * (1f - correction));
                rect.top = drawnCenterY;
                rect.bottom = drawnCenterY + getItemHeightForRender();

                canvas.drawBitmap(slots.get(items.get(actualPos).rewardName + items.get(actualPos).rewardDescription),
                        null, rect, backgroundPaint);
            } else {
                canvas.drawBitmap(slots.get(items.get(actualPos).rewardName + items.get(actualPos).rewardDescription),
                        0, drawnCenterY, backgroundPaint);
            }
            canvas.restore();
        }
    }

    private void computeDrawnCenter() {
        mDrawnCenterX = Math.round(mWheelCenterX / 2);
        mDrawnCenterY = mWheelCenterY - Math.round(getItemHeightForRender() / 2);
    }

    private void computeFlingLimitY() {
        mMinFlingY = Integer.MIN_VALUE;
        mMaxFlingY = Integer.MAX_VALUE;
    }

    private int computeDepth(int degree) {
        float unit = 0.8f;
        if (degree > 0) {
            unit *= 1;
        } else if (degree < mDrawnCenterY) {
            unit *= -1;
        }

        Double res = (double) mHalfWheelHeight -
                (Math.cos(
                        Math.toRadians(
                                (degree +
                                        0.7 * unit * (Math.max(Math.abs(degree) - 45, 0)) -
                                        0.2 * unit * (Math.max(Math.abs(degree) - 70, 0))) / depthCorrection)))
                        * (double) mHalfWheelHeight;
        return res.intValue();
    }

    private int computeYDepth(int degree) {
        float unit = 1.2f;
        if (degree > 0) {
            unit *= 1;
        } else if (degree < mDrawnCenterY) {
            unit *= -1;
        }

        Double res = Math.sin(Math.toRadians(
                unit * Math.max(Math.abs(degree) - 45, 0) +
                        0.25 * unit * Math.max(Math.abs(degree) - 60, 0))) * 120;
        return res.intValue();
    }

    private int computeRotation(float degree) {
        float unit = 1f;
        if (degree > 0) {
            unit *= 1;
        } else if (degree < mDrawnCenterY) {
            unit *= -1;
        }

        return (int) ((degree +
                0.25 * unit * (Math.max(Math.abs(degree) - 45, 0)) +
                0.17 * unit * (Math.max(Math.abs(degree) - 60, 0)) +
                0.05 * unit * (Math.max(Math.abs(degree) - 80, 0))) / rotationCorrection);
    }

    private int computeSpace(int degree) {
        float unit = 1f;
        if (degree > 0) {
            unit *= 1;
        } else if (degree < mDrawnCenterY) {
            unit *= -1;
        }

        float fixedDegree = (float) (degree -
                0.15 * unit * Math.max(Math.abs(degree) - 60, 0) -
                0.15 * unit * Math.max(Math.abs(degree) - 80, 0)) * spaceCorrection;
        Double res = (Math.sin(Math.toRadians(fixedDegree))) * (double) mHalfWheelHeight;
        return res.intValue();
    }

    private int computeDistanceToEndPoint(int remainder) {
        if (Math.abs(remainder) > mHalfItemHeight) {
            if (mScrollOffsetY < 0) {
                return -mItemHeight - remainder;
            } else {
                return mItemHeight - remainder;
            }
        } else {
            return -remainder;
        }
    }

    private void updateVisibleItemCount() {
        if (mVisibleItemCount < 2)
            throw new ArithmeticException("Wheel's visible item count can not be less than 2!");

        // Be sure count of visible item is odd number
        if (mVisibleItemCount % 2 == 0) {
            mVisibleItemCount += 1;
        }
        mDrawnItemCount = mVisibleItemCount + 2;
        mHalfDrawnItemCount = mDrawnItemCount / 2;
    }

    public WheelItemView generateWheelItemView(String title, String subtitle, int iconRes) {
        WheelItemView view = new WheelItemView(getContext());
        view.setTitle(title);
        view.setSubTitle(subtitle);
        view.setIcon(iconRes);
        view.setRootWidth(fullViewRectangle.width());
        view.setRootHeight(getItemHeightForRender());
        return view;
    }

    public Bitmap loadBitmapFromView(View v) {
        int specWidth = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        v.measure(specWidth, specWidth);

        Bitmap b = Bitmap.createBitmap(v.getMeasuredWidth(), v.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        v.layout(0, 0, fullViewRectangle.width(), v.getMeasuredHeight());
        v.draw(c);
        return b;
    }

    @Override
    public void run() {
        if (null == items || items.size() == 0) {
            return;
        }
        if (mScroller.isFinished() && !isForceFinishScroll) {
            if (mItemHeight == 0) return;
            int position = (-mScrollOffsetY / mItemHeight + mSelectedItemPosition) % items.size();
            position = position < 0 ? position + items.size() : position;
            mCurrentItemPosition = position;
            if (null != mOnItemSelectedListener && isFlingingProgrammatically) {
                mOnItemSelectedListener.onItemSelected(this, items.get(position), position);
                isFlingingProgrammatically = false;
            }
        }
        if (mScroller.computeScrollOffset()) {
            mScrollOffsetY = mScroller.getCurrY();
            postInvalidate();
            mHandler.postDelayed(this, 16);
        }
    }

    public interface OnItemSelectedListener {
        void onItemSelected(SlotMachineView picker, RewardSelectionDTO data, int position);
    }

    public int getItemHeightForRender() {
        return Math.round(mItemHeight * 2.5f);
    }
}
