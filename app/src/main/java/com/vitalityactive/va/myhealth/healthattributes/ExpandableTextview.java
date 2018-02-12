package com.vitalityactive.va.myhealth.healthattributes;


import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vitalityactive.va.R;

public class ExpandableTextview extends LinearLayout implements View.OnClickListener {
    TextView contentView;
    ImageView expandIcon;
    boolean mCollapsed = true, mRelayout = false;
    int maxLines;

    public ExpandableTextview(Context context) {
        super(context);
        init();
    }

    public ExpandableTextview(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ExpandableTextview(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ExpandableTextview(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    public void init() {
        contentView = findViewById(R.id.text_view);
        expandIcon = findViewById(R.id.expand_more);
        expandIcon.setOnClickListener(this);
        maxLines = contentView.getMaxLines();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (!mRelayout || getVisibility() == View.GONE) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            return;
        }
        mRelayout = false;
        if (contentView == null) {
            init();
        }

        expandIcon.setVisibility(View.GONE);
        contentView.setMaxLines(Integer.MAX_VALUE);
        // Measure
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (contentView.getLineCount() <= maxLines) {
            return;
        }

        if (mCollapsed) {
            contentView.setMaxLines(maxLines);
        }
        expandIcon.setVisibility(View.VISIBLE);

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void setText(String text) {
        mRelayout = true;
        if (contentView == null) {
            init();
        }
        if (text != null) {
            contentView.setText(text.trim());
            contentView.setVisibility(text.trim().isEmpty() ? GONE : VISIBLE);
            if (contentView.getLineCount() > maxLines) {
                expandIcon.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onClick(View view) {
        mCollapsed = !mCollapsed;
        contentView.setMaxLines(mCollapsed ? maxLines : Integer.MAX_VALUE);

    }
}
