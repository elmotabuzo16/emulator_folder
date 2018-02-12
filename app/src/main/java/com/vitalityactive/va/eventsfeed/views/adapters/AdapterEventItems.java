package com.vitalityactive.va.eventsfeed.views.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.vitalityactive.va.R;
import com.vitalityactive.va.eventsfeed.EventsFeedDay;
import com.vitalityactive.va.eventsfeed.EventsFeedIconProvider;
import com.vitalityactive.va.eventsfeed.data.dto.EventDTO;
import com.vitalityactive.va.eventsfeed.views.EventsFeedItemContentViewHolder;

/**
 * Created by jayellos on 11/22/17.
 */

public class AdapterEventItems extends RecyclerView.Adapter{

    private EventsFeedDay day;
    private final int globalTintColor;
    private LayoutInflater layoutInflater;
    private EventsFeedIconProvider iconProvider;

    public AdapterEventItems(EventsFeedDay day, int globalTintColor, LayoutInflater layoutInflater, EventsFeedIconProvider iconProvider) {
        this.day = day;
        this.globalTintColor = globalTintColor;
        this.layoutInflater = layoutInflater;
        this.iconProvider = iconProvider;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new EventsFeedItemViewHolder(layoutInflater.inflate(viewType, parent, false), iconProvider, globalTintColor);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        EventDTO eventDTO = day.getPointsEntries().get(position);

        //TODO: change EventsFeedTitleProvider.getCategoryTitle(eventDTO.getCategoryKey()) to categoryName
        ((EventsFeedItemViewHolder) holder).bindWith(eventDTO, eventDTO.getCategoryName());
//        ((EventsFeedItemViewHolder) holder).bindWith(eventDTO, EventsFeedTitleProvider.getCategoryTitle(eventDTO.getCategoryKey()));

//            holder.itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent(getActivity(), PointsMonitorEntryDetailActivity.class);
//                    intent.putExtra(PointsMonitorEntryDetailActivity.POINTS_ENTRY_ID, day.getPointsEntries().get(holder.getAdapterPosition()).getId());
//                    intent.putExtra(PointsMonitorEntryDetailActivity.GLOBAL_TINT_COLOR, globalTintColor);
//                    getActivity().startActivity(intent);
//                }
//            });
    }

    @Override
    public int getItemCount() {
        return day.getPointsEntries().size();
    }

    @Override
    public int getItemViewType(int position) {
        return R.layout.events_feed_events_item;
    }

    private class EventsFeedItemViewHolder extends EventsFeedItemContentViewHolder {
        private final ImageView icon;
        private final TextView reason;
        private final EventsFeedIconProvider iconProvider;

        EventsFeedItemViewHolder(View itemView, EventsFeedIconProvider iconProvider, int globalTintColor) {
            super(itemView);
            this.iconProvider = iconProvider;
            icon = itemView.findViewById(R.id.icon);
            reason = itemView.findViewById(R.id.reason);
        }

        @Override
        public void bindWith(EventDTO dataItem, String title) {
            super.bindWith(dataItem, title);
            icon.setImageResource(iconProvider.getIconResourceId(dataItem.getCategoryKey()));
        }
    }
}
