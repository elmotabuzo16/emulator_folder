package com.vitalityactive.va.activerewards.landing.uicontainers;

import android.view.View;
import android.widget.TextView;

import com.vitalityactive.va.R;
import com.vitalityactive.va.uicomponents.adapters.GenericRecyclerViewAdapter;

public class NotStartedContainer extends GenericRecyclerViewAdapter.ViewHolder<String> {
        private final TextView tvHeader;

        private NotStartedContainer(View itemView) {
            super(itemView);
            tvHeader = itemView.findViewById(R.id.tv_ar_header);
        }

        @Override
        public void bindWith(String date) {
            tvHeader.setText(tvHeader.getContext().getString(R.string.AR_landing_first_goal_cell_title_677, date));
        }

        public static class Factory implements GenericRecyclerViewAdapter.IViewHolderFactory<String, NotStartedContainer> {

            @Override
            public NotStartedContainer createViewHolder(View itemView) {
                return new NotStartedContainer(itemView);
            }
        }

    }