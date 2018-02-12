package com.vitalityactive.va.uicomponents.slotmachine;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vitalityactive.va.R;
import com.vitalityactive.va.utilities.ViewUtilities;

public class WheelItemView extends RelativeLayout {
    private TextView tvTitle, tvSubTitle;
    private ImageView ivIcon;
    private RelativeLayout rlRoot;

    private int viewHeight;

    public WheelItemView(Context context) {
        this(context, null);
    }

    public WheelItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView(){
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.view_wheel_item, this, true);
        rlRoot = (RelativeLayout) findViewById(R.id.root);
        tvTitle = (TextView) view.findViewById(R.id.tvTitle);
        tvSubTitle = (TextView) view.findViewById(R.id.tvSubTitle);
        ivIcon = (ImageView) findViewById(R.id.ivIcon);
    }

    public void setTitle(String title){
        tvTitle.setText(title);
    }

    public void setSubTitle(String subtitle){
        tvSubTitle.setText(subtitle);
    }

    public void setIcon(@DrawableRes int iconResId){
        ivIcon.setImageResource(iconResId);
    }

    public void setRootWidth(int width){
        rlRoot.setMinimumWidth(width);
    }

    public void setRootHeight(int height){
        rlRoot.setMinimumHeight(height - ViewUtilities.pxFromDp(4));
        viewHeight = height - ViewUtilities.pxFromDp(4);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(viewHeight, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}