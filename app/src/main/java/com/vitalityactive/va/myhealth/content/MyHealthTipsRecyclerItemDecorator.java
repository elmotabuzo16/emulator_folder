package com.vitalityactive.va.myhealth.content;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class MyHealthTipsRecyclerItemDecorator extends DividerItemDecoration {

    public MyHealthTipsRecyclerItemDecorator(Builder builder) {
        super(builder.context, builder.orientation);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view);
        if (position == 2) {
            super.getItemOffsets(outRect, view, parent, state);
        } else {
            outRect.setEmpty();
        }
    }


    public static class Builder {
        Context context;
        int orientation;


        public Builder(Context context, int orientation) {
            this.context = context;
            this.orientation = orientation;
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

        public MyHealthTipsRecyclerItemDecorator build() {
            return new MyHealthTipsRecyclerItemDecorator(this);
        }

    }
}