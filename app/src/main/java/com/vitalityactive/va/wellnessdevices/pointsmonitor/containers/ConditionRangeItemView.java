package com.vitalityactive.va.wellnessdevices.pointsmonitor.containers;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vitalityactive.va.R;
import com.vitalityactive.va.wellnessdevices.Utils;

public class ConditionRangeItemView extends LinearLayout {
    TextView tvConditionDescription;

    public ConditionRangeItemView(Context context, String text) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.view_wd_points_condition_list_item, this, true);
        tvConditionDescription = (TextView) findViewById(R.id.tv_condition_text);
        tvConditionDescription.setText(Utils.fromHtml(text));
    }
}
