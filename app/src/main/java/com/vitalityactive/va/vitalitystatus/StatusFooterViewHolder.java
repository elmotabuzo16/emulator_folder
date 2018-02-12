package com.vitalityactive.va.vitalitystatus;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.vitalityactive.va.R;
import com.vitalityactive.va.activerewards.dto.TitleSubtitleAndIcon;
import com.vitalityactive.va.uicomponents.adapters.GenericRecyclerViewAdapter;

public class StatusFooterViewHolder extends GenericRecyclerViewAdapter.ViewHolder<TitleSubtitleAndIcon> {
    private TextView containerTitle;
    private TextView title;
    private ImageView icon;
    private TextView subTitle;
    private String containerTitleText;

    private StatusFooterViewHolder(View itemView, String containerTitleText) {
        super(itemView);
        this.containerTitleText = containerTitleText;
        assignViews();
    }

    @Override
    public void bindWith(TitleSubtitleAndIcon dataItem) {
        containerTitle.setText(containerTitleText);
        title.setText(dataItem.getTitle());
        subTitle.setText(dataItem.getSubtitle());
        icon.setImageResource(dataItem.getIconResourceId());
    }

    private void assignViews() {
        containerTitle = itemView.findViewById(R.id.container_title);
        icon = itemView.findViewById(R.id.icon);
        title = itemView.findViewById(R.id.title);
        subTitle = itemView.findViewById(R.id.subtitle);
    }

    public static class Factory implements GenericRecyclerViewAdapter.IViewHolderFactory<TitleSubtitleAndIcon, StatusFooterViewHolder> {
        private String containerTitleText;

        public Factory(String containerTitleText) {
            this.containerTitleText = containerTitleText;
        }

        @Override
        public StatusFooterViewHolder createViewHolder(View itemView) {
            return new StatusFooterViewHolder(itemView, containerTitleText);
        }
    }
}
