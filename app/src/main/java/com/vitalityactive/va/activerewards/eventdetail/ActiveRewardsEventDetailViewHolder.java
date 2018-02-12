package com.vitalityactive.va.activerewards.eventdetail;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vitalityactive.va.R;
import com.vitalityactive.va.activerewards.dto.ActivityItem;
import com.vitalityactive.va.activerewards.dto.TitleAndSubtitle;
import com.vitalityactive.va.activerewards.viewholder.TitleAndSubtitleViewHolder;
import com.vitalityactive.va.uicomponents.adapters.GenericRecyclerViewAdapter;
import com.vitalityactive.va.utilities.TextUtilities;
import com.vitalityactive.va.utilities.ViewUtilities;
import com.vitalityactive.va.utilities.date.Date;
import com.vitalityactive.va.utilities.date.formatting.DateFormattingUtilities;

import java.util.ArrayList;
import java.util.List;

class ActiveRewardsEventDetailViewHolder extends GenericRecyclerViewAdapter.ViewHolder<ActivityItem> {
    private final Context context;
    private final RecyclerView recyclerView;
    private final TextView points;
    private final TextView device;
    private final LinearLayout deviceContainer;

    private ActiveRewardsEventDetailViewHolder(Context context, View itemView) {
        super(itemView);
        this.context = context;
        recyclerView = itemView.findViewById(R.id.recycler_view);
        points = itemView.findViewById(R.id.tv_points);
        device = itemView.findViewById(R.id.tv_device_used);
        deviceContainer = itemView.findViewById(R.id.ll_device_container);
    }

    @Override
    public void bindWith(ActivityItem activityItem) {
        points.setText(activityItem.getTitle());
        if(TextUtilities.isNullOrWhitespace(activityItem.getDevice())){
            deviceContainer.setVisibility(View.GONE);
        } else {
            deviceContainer.setVisibility(View.VISIBLE);
            device.setText(activityItem.getDevice());
        }

        recyclerView.setAdapter(new GenericRecyclerViewAdapter<>(context,
                getEventMetadataAndDateItems(activityItem),
                R.layout.active_rewards_activity_item_metadata,
                new TitleAndSubtitleViewHolder.Factory()));
        ViewUtilities.addDividers(context, recyclerView);
    }

    private List<TitleAndSubtitle> getEventMetadataAndDateItems(ActivityItem activityItem) {
        List<TitleAndSubtitle> items = new ArrayList<>();
        items.add(getEventItem(activityItem));
        items.addAll(activityItem.getMetadata());
        items.add(getDateItem(activityItem));
        return items;
    }

    @NonNull
    private TitleAndSubtitle getDateItem(ActivityItem activityItem) {
        return new TitleAndSubtitle(DateFormattingUtilities.formatWeekdayDateMonthYear(context, new Date(activityItem.getDate())), context.getString(R.string.date_title_264));
    }

    @NonNull
    private TitleAndSubtitle getEventItem(ActivityItem activityItem) {
        return new TitleAndSubtitle(activityItem.getDescription(), context.getString(R.string.AR_points_event_detail_cell_title_684));
    }

    public static class Factory implements GenericRecyclerViewAdapter.IViewHolderFactory<ActivityItem, ActiveRewardsEventDetailViewHolder> {
        private final Context context;

        public Factory(Context context) {
            this.context = context;
        }

        @Override
        public ActiveRewardsEventDetailViewHolder createViewHolder(View itemView) {
            return new ActiveRewardsEventDetailViewHolder(context, itemView);
        }
    }
}
