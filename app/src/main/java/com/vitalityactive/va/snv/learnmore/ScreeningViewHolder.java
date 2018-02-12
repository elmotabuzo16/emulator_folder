package com.vitalityactive.va.snv.learnmore;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vitalityactive.va.R;
import com.vitalityactive.va.uicomponents.adapters.GenericRecyclerViewAdapter;
import com.vitalityactive.va.utilities.ViewUtilities;

/**
 * Created by stephen.rey.w.avila on 12/5/2017.
 */

public class ScreeningViewHolder extends GenericRecyclerViewAdapter.ViewHolder<LearnMoreScreeningItem> {
    private final ImageView icon1;
    private final TextView itemTitle1;
    private final ImageView icon2;
    private final TextView itemTitle2;
    private final LinearLayout screeningsList;
    private final LinearLayout vaccinationsList;

    Context context;

    public ScreeningViewHolder(View itemView, View.OnClickListener onClickListener,  View.OnClickListener onClickListener2) {
        super(itemView);
        context = itemView.getContext();
        icon1 = itemView.findViewById(R.id.sv_icon1);
        itemTitle1 = itemView.findViewById(R.id.sv_itemTitle1);
        screeningsList = itemView.findViewById(R.id.screenings_container);

        icon2 = itemView.findViewById(R.id.sv_icon2);
        itemTitle2 = itemView.findViewById(R.id.sv_itemTitle2);
        vaccinationsList = itemView.findViewById(R.id.vaccinations_container);

        screeningsList.setOnClickListener(onClickListener);
        vaccinationsList.setOnClickListener(onClickListener2);
    }

    @Override
    public void bindWith(LearnMoreScreeningItem dataItem) {
        final int iconResourceId1 = dataItem.getIconResourceId1();

        if(iconResourceId1!=0){
            icon1.setColorFilter(ContextCompat.getColor(itemView.getContext(), R.color.jungle_green));
        }



        itemTitle1.setText(dataItem.getTitle1());

        final int iconResourceId2 = dataItem.getIconResourceId2();

        if(iconResourceId2!=0){
            icon2.setColorFilter(ContextCompat.getColor(itemView.getContext(), R.color.jungle_green));
        }
        itemTitle2.setText(dataItem.getTitle2());

    }

    public static class Factory implements GenericRecyclerViewAdapter.IViewHolderFactory<LearnMoreScreeningItem, ScreeningViewHolder> {
        View.OnClickListener onClickListener;
        View.OnClickListener onClickListener2;

        public Factory(View.OnClickListener onClickListener) {
            this.onClickListener = onClickListener;
        }

        public Factory(View.OnClickListener onClickListener, View.OnClickListener onClickListener2) {
            this.onClickListener = onClickListener;
            this.onClickListener2 = onClickListener2;
        }

        @Override
        public ScreeningViewHolder createViewHolder(View itemView) {
            return new ScreeningViewHolder(itemView, onClickListener,onClickListener2);
        }
    }
}
