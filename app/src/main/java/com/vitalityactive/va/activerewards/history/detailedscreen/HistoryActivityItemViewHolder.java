package com.vitalityactive.va.activerewards.history.detailedscreen;

import android.content.Context;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vitalityactive.va.R;
import com.vitalityactive.va.activerewards.dto.GoalTrackerOutDto;
import com.vitalityactive.va.activerewards.history.uicontainers.ActivityItemViewHolder;
import com.vitalityactive.va.activerewards.utilities.IconUtilities;
import com.vitalityactive.va.constants.GoalTrackerStatus;
import com.vitalityactive.va.uicomponents.adapters.GenericRecyclerViewAdapter;
import com.vitalityactive.va.utilities.ViewUtilities;
import com.vitalityactive.va.utilities.date.formatting.DateFormattingUtilities;

public class HistoryActivityItemViewHolder extends ActivityItemViewHolder {
    protected final TextView description;
    protected final View divider;

    public HistoryActivityItemViewHolder(View itemView) {
        super(itemView);
        description = itemView.findViewById(R.id.tv_ar_description);
        divider = itemView.findViewById(R.id.divider);
        date.setVisibility(View.GONE);
    }

    @Override
    public void bindWith(GoalTrackerOutDto item) {
        super.bindWith(item);
        description.setVisibility(getStatusCode(item) == GoalTrackerStatus._NOTACHIEVED ? View.VISIBLE : View.GONE);
        divider.setVisibility(getStatusCode(item) == GoalTrackerStatus._NOTACHIEVED ? View.VISIBLE : View.GONE);
        // TODO check if we have to show any description for Canceled goals
    }

    @Override
    protected void setIcon(GoalTrackerOutDto item) {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, ViewUtilities.pxFromDp(8), 0, 0);
        icon.setLayoutParams(params);


        int color = ResourcesCompat.getColor(itemView.getContext().getResources(), IconUtilities.getIconColor(getStatusCode(item)), itemView.getContext().getTheme());
        ViewUtilities.tintDrawable(ContextCompat.getDrawable(itemView.getContext(), IconUtilities.getIconId(getStatusCode(item))), color);
        icon.setImageDrawable(ContextCompat.getDrawable(itemView.getContext(), IconUtilities.getIconId(getStatusCode(item))));
    }

    public static class Factory
            implements GenericRecyclerViewAdapter.IViewHolderFactory<GoalTrackerOutDto, HistoryActivityItemViewHolder> {

        @Override
        public HistoryActivityItemViewHolder createViewHolder(View itemView) {
            return new HistoryActivityItemViewHolder(itemView);
        }
    }

    @SuppressWarnings("deprecation")
    public void setTextAppearance(Context context, TextView textView, int styleId) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            textView.setTextAppearance(context, styleId);
        } else {
            textView.setTextAppearance(styleId);
        }
    }
}
