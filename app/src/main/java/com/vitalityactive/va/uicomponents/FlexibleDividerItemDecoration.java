package com.vitalityactive.va.uicomponents;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

public class FlexibleDividerItemDecoration extends RecyclerView.ItemDecoration {
    private static final int HORIZONTAL = LinearLayout.HORIZONTAL;
    private static final int VERTICAL = LinearLayout.VERTICAL;

    private static final int[] ATTRS = new int[]{android.R.attr.listDivider};
    private final boolean drawLastDivider;
    private final int leftMarginPx;

    private Drawable mDivider;

    private int mOrientation;

    private final Rect mBounds = new Rect();

    private FlexibleDividerItemDecoration(Context context, int orientation, boolean drawLastDivider, int leftMarginPx) {
        this.drawLastDivider = drawLastDivider;
        this.leftMarginPx = leftMarginPx;
        final TypedArray a = context.obtainStyledAttributes(ATTRS);
        mDivider = a.getDrawable(0);
        a.recycle();
        setOrientation(orientation);
    }

    public void setOrientation(int orientation) {
        if (orientation != HORIZONTAL && orientation != VERTICAL) {
            throw new IllegalArgumentException(
                    "Invalid orientation. It should be either HORIZONTAL or VERTICAL");
        }
        mOrientation = orientation;
    }

    public void setDrawable(@NonNull Drawable drawable) {
        mDivider = drawable;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        if (parent.getLayoutManager() == null) {
            return;
        }

        if (mOrientation == VERTICAL) {
            drawVertical(c, parent);
        } else {
            drawHorizontal(c, parent);
        }
    }

    private void drawVertical(Canvas canvas, RecyclerView parent) {
        canvas.save();
        final int left;
        final int right;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && parent.getClipToPadding()) {
            left = parent.getPaddingLeft() + leftMarginPx;
            right = parent.getWidth() - parent.getPaddingRight();
            canvas.clipRect(left, parent.getPaddingTop(), right,
                    parent.getHeight() - parent.getPaddingBottom());
        } else {
            left = leftMarginPx;
            right = parent.getWidth();
        }

        final int childCount = getChildCount(parent);
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            parent.getDecoratedBoundsWithMargins(child, mBounds);
            final int bottom = mBounds.bottom + Math.round(child.getTranslationY());
            final int top = bottom - mDivider.getIntrinsicHeight();
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(canvas);
        }
        canvas.restore();
    }

    private void drawHorizontal(Canvas canvas, RecyclerView parent) {
        canvas.save();
        final int top;
        final int bottom;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && parent.getClipToPadding()) {
            top = parent.getPaddingTop();
            bottom = parent.getHeight() - parent.getPaddingBottom();
            canvas.clipRect(parent.getPaddingLeft(), top,
                    parent.getWidth() - parent.getPaddingRight(), bottom);
        } else {
            top = 0;
            bottom = parent.getHeight();
        }

        final int childCount = getChildCount(parent);
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            parent.getLayoutManager().getDecoratedBoundsWithMargins(child, mBounds);
            final int right = mBounds.right + Math.round(child.getTranslationX());
            final int left = right - mDivider.getIntrinsicWidth();
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(canvas);
        }
        canvas.restore();
    }

    private int getChildCount(RecyclerView parent) {
        return parent.getChildCount() - (drawLastDivider ? 0 : 1);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                               RecyclerView.State state) {

        if (mOrientation == VERTICAL) {
            outRect.set(0, 0, 0, mDivider.getIntrinsicHeight());
        } else {
            outRect.set(0, 0, mDivider.getIntrinsicWidth(), 0);
        }
    }

    public static class Builder {
        private final Context context;
        private int orientation = VERTICAL;
        private boolean drawLastDivider = false;
        private int leftMarginPx = 0;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setDrawLastDivider(boolean drawLastDivider) {
            this.drawLastDivider = drawLastDivider;
            return this;
        }

        public Builder setLeftMarginPx(int leftMarginDp) {
            this.leftMarginPx = leftMarginDp;
            return this;
        }

        public Builder setOrientation(int orientation) {
            this.orientation = orientation;
            return this;
        }

        public FlexibleDividerItemDecoration build() {
            return new FlexibleDividerItemDecoration(context, orientation, drawLastDivider, leftMarginPx);
        }
    }
}
