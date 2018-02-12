package com.vitalityactive.va.pointsmonitor;

import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.vitalityactive.va.R;
import com.vitalityactive.va.dto.PointsEntryDTO;
import com.vitalityactive.va.utilities.TextUtilities;

class PointsItemContentViewHolder extends RecyclerView.ViewHolder {
    private final TextView points;
    private final TextView reason;
    private final TextView progress;

    PointsItemContentViewHolder(View itemView) {
        super(itemView);
        points = itemView.findViewById(R.id.points);
        reason = itemView.findViewById(R.id.reason);
        progress = itemView.findViewById(R.id.progress);
    }

    @CallSuper
    void bindWith(PointsEntryDTO dataItem) {
        setVisibilityAndText(points, String.valueOf(dataItem.getPoints()));
        setVisibilityAndText(reason, dataItem.getReason());
        setVisibilityAndText(progress, getProgressString(dataItem.getProgress()));
    }

    @Nullable
    private String getProgressString(String progress) {
        if (TextUtilities.isNullOrWhitespace(progress) || progress.equals("0")) {
            return null;
        } else {
            return this.progress.getContext().getString(R.string.PM_points_earning_active_rewards_567, progress);
        }
    }

    void setVisibilityAndText(TextView textView, String text) {
        if (TextUtilities.isNullOrWhitespace(text) || text.equals("0")) {
            setViewVisibility(textView, View.GONE);
        } else {
            setViewVisibility(textView, View.VISIBLE);
            textView.setText(text);
        }
    }

    private void setViewVisibility(TextView view, int visibility) {
        view.setVisibility(visibility);
    }
}
