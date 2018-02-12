package com.vitalityactive.va.myhealth.content;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class MyHealthRecyclerItemDecorator extends DividerItemDecoration {

    private boolean appendToBotton = false;

    public MyHealthRecyclerItemDecorator(Builder builder) {
        super(builder.context, builder.orientation);
        this.appendToBotton = builder.appendToBotton;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view);
        if (!appendToBotton && (position == parent.getAdapter().getItemCount() - 1)) {
            outRect.setEmpty();
        } else {
            super.getItemOffsets(outRect, view, parent, state);
        }
    }


    public static class Builder {
        Context context;
        int orientation;
        boolean appendToBotton;

        public Builder(Context context, int orientation) {
            this.context = context;
            this.orientation = orientation;
        }

        public Builder(Context context, int orientation, boolean appendToBotton) {
            this(context, orientation);
            this.appendToBotton = appendToBotton;
        }

        public Builder() {

        }

        public Builder context(Context context) {
            this.context = context;
            return this;
        }

        public Builder orientation(int orientation) {
            this.orientation = orientation;
            return this;
        }

        public Builder appendToBotton(boolean appendToBotton) {
            this.appendToBotton = appendToBotton;
            return this;
        }

        public MyHealthRecyclerItemDecorator build() {
            return new MyHealthRecyclerItemDecorator(this);
        }

    }
}