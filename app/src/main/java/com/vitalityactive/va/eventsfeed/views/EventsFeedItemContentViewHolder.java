package com.vitalityactive.va.eventsfeed.views;

import android.support.annotation.CallSuper;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.vitalityactive.va.R;
import com.vitalityactive.va.eventsfeed.data.dto.EventDTO;
import com.vitalityactive.va.utilities.TextUtilities;

public class EventsFeedItemContentViewHolder extends RecyclerView.ViewHolder {
    private final TextView eventsName;
    private final TextView reason;
    private final TextView progress;

    public EventsFeedItemContentViewHolder(View itemView) {
        super(itemView);
        eventsName = itemView.findViewById(R.id.events_name);
        reason = itemView.findViewById(R.id.reason);
        progress = itemView.findViewById(R.id.progress);
    }

    @CallSuper
    public void bindWith(EventDTO dataItem, String title) {
        setVisibilityAndText(eventsName, title);
        setVisibilityAndText(reason, dataItem.getTypeName());
    }

    public void setVisibilityAndText(TextView textView, String text) {
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
