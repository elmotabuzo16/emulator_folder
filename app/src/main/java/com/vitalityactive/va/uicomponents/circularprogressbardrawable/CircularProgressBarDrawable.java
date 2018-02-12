package com.vitalityactive.va.uicomponents.circularprogressbardrawable;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import java.util.Locale;

public class CircularProgressBarDrawable extends Drawable {
    public static final int DEFAULT_STROKE_WIDTH = 16;

    private Paint segmentBrush = new Paint();
    private int width;
    private int height;
    private int segmentsStrokeWidth;
    private int completedFillColor;
    private int incompleteFillColor;
    private int gapWidth;
    private int segmentGapColor;
    private int completedSegmentCount;
    private int totalSegmentCount;
    private final TextView progressBarTextView;
    private final int circleToRectangleScale;
    private float degreesPerSegment;
    private int baseStartingDegrees = 270;

    private CircularProgressBarDrawable(int width,
                                       int height,
                                       int segmentsStrokeWidth,
                                       int completedFillColor,
                                       int incompleteFillColor,
                                       int gapWidth,
                                       int segmentGapColor,
                                       int completedSegmentCount,
                                       int totalSegmentCount,
                                       TextView progressBarTextView,
                                       int circleToRectangleScale) {
        this.width = width;
        this.height = height;
        this.segmentsStrokeWidth = segmentsStrokeWidth;
        this.completedFillColor = completedFillColor;
        this.incompleteFillColor = incompleteFillColor;
        this.gapWidth = gapWidth;
        this.segmentGapColor = segmentGapColor;
        this.completedSegmentCount = completedSegmentCount;
        this.totalSegmentCount = totalSegmentCount;
        this.progressBarTextView = progressBarTextView;
        this.circleToRectangleScale = circleToRectangleScale;
    }

    @Override
    protected boolean onLevelChange(int level) {
        invalidateSelf();
        return true;
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        float size = Math.min(width, height);
        segmentBrush.setStrokeWidth(segmentsStrokeWidth);
        segmentBrush.setStyle(Paint.Style.STROKE);

        final RectF oval = new RectF(0, 0, width, height);
        oval.inset(size / circleToRectangleScale, size / circleToRectangleScale);

        degreesPerSegment = 360f / getTotalSegmentCount();

        drawCompletedSegments(oval, canvas);

        drawInCompletedSegments(oval, canvas);
        
        if (totalSegmentCount != 1)
            drawSegmentGaps(canvas, size);

        drawTextIfAvailable();
    }

    private void drawTextIfAvailable() {
        if (progressBarTextView != null) {
            progressBarTextView.setVisibility(View.VISIBLE);
            progressBarTextView.setTextColor(completedFillColor);
        }
    }

    private int getTotalSegmentCount() {
        return totalSegmentCount;
    }

    private void drawSegmentGaps(Canvas canvas, float size) {
        Paint gapBrush = new Paint();
        gapBrush.setStrokeWidth(gapWidth);
        gapBrush.setColor(segmentGapColor);
        gapBrush.setStyle(Paint.Style.STROKE);

        canvas.save();
        for(int i = 0; i < 360; i += degreesPerSegment){
            canvas.drawLine(size/2,size*1/4,size/2,0, gapBrush);
            canvas.rotate(degreesPerSegment,size/2,size/2);
        }
        canvas.restore();
    }

    private void drawInCompletedSegments(RectF oval, Canvas canvas) {
        segmentBrush.setColor(incompleteFillColor);

        float segmentsStartingDegrees = baseStartingDegrees + (degreesPerSegment * completedSegmentCount);
        segmentsStartingDegrees %= 360f;

        final float sweepAngle = (totalSegmentCount - completedSegmentCount) * degreesPerSegment;
        canvas.drawArc(oval, segmentsStartingDegrees, sweepAngle, false, segmentBrush);
    }

    private void drawCompletedSegments(RectF oval, Canvas canvas) {
        segmentBrush.setColor(completedFillColor);

        final float sweepAngle = degreesPerSegment * completedSegmentCount;
        canvas.drawArc(oval, baseStartingDegrees, sweepAngle, false, segmentBrush);
    }

    @Override
    public void setAlpha(int alpha) {
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    public static class Builder {
        //Builder with defaults
        private int width = 400;
        private int height = 400;
        private int fillStrokeWidth = 16;
        private int completedFillColor = Color.BLACK;
        private int incompleteFillColor = Color.LTGRAY;
        private int gapWidth = 6;
        private int segmentGapColor = Color.WHITE;
        private int totalSegmentCount = 10;
        private int completedSegmentCount = 5;
        private int circleToRectangleScale= 16;
        private TextView progressBarTextView;

        public Builder setSize(int width, int height) {
            this.width = width;
            this.height = height;

            return this;
        }

        public Builder setFillStrokeWidth(int barStrokeWidth) {
            this.fillStrokeWidth = barStrokeWidth;
            return this;
        }

        public Builder setCompletedColor(int completedFillColor) {
            this.completedFillColor = completedFillColor;
            return this;
        }

        public Builder setIncompleteColor(int incompleteFillColor) {
            this.incompleteFillColor = incompleteFillColor;
            return this;
        }

        public Builder setSegmentGapWidth(int gapWidth) {
            this.gapWidth = gapWidth;
            return this;
        }

        public Builder setSegmentGapColor(int segmentGapColor) {
            this.segmentGapColor = segmentGapColor;
            return this;
        }

        public Builder setCompletedSegmentCount(int completedSegmentCount) {
            if (completedSegmentCount > totalSegmentCount)
                this.totalSegmentCount = completedSegmentCount;

            this.completedSegmentCount = completedSegmentCount;

            return this;
        }

        public Builder setTotalSegmentCount(int totalSegmentCount) {
            if (totalSegmentCount < completedSegmentCount)
                this.totalSegmentCount = completedSegmentCount;
            else
                this.totalSegmentCount = totalSegmentCount;

            return this;
        }

        public CircularProgressBarDrawable build() {
            return new CircularProgressBarDrawable(width,
                    height,
                    fillStrokeWidth,
                    completedFillColor,
                    incompleteFillColor,
                    gapWidth,
                    segmentGapColor,
                    completedSegmentCount,
                    totalSegmentCount,
                    progressBarTextView,
                    circleToRectangleScale);
        }

        public Builder setExtraText(TextView progressBarTextView) {
            this.progressBarTextView = progressBarTextView;

            final String text = String.format(Locale.getDefault(),
                    "%d/%d",
                    completedSegmentCount,
                    totalSegmentCount);

            progressBarTextView.setText(text);

            return this;
        }
    }
}
