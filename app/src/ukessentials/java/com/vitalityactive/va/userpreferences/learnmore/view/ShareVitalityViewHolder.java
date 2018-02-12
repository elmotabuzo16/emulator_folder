package com.vitalityactive.va.userpreferences.learnmore.view;

import android.view.View;
import android.widget.TextView;

import com.vitalityactive.va.R;
import com.vitalityactive.va.snv.learnmore.LearnMoreScreeningItem;
import com.vitalityactive.va.uicomponents.adapters.GenericRecyclerViewAdapter;

/**
 * Created by dharel.h.rosell on 1/3/2018.
 */

public class ShareVitalityViewHolder extends GenericRecyclerViewAdapter.ViewHolder<LearnMoreScreeningItem> {

    private final TextView title;
    private final TextView subTitle;

    public ShareVitalityViewHolder(View itemView) {
        super(itemView);
        title = itemView.findViewById(R.id.title);
        subTitle = itemView.findViewById(R.id.subtitle);
    }

    @Override
    public void bindWith(LearnMoreScreeningItem dataItem) {

    }
}
