package com.vitalityactive.va.activerewards.history.detailedscreen;

import android.view.View;
import android.widget.TextView;

import com.vitalityactive.va.R;
import com.vitalityactive.va.uicomponents.adapters.GenericRecyclerViewAdapter;

public class FooterViewHolder extends GenericRecyclerViewAdapter.ViewHolder<String> {
    private final TextView tvAbout;

    public FooterViewHolder(View itemView) {
        super(itemView);
        tvAbout = (TextView) itemView.findViewById(R.id.tvAbout);
    }

    @Override
    public void bindWith(String dataItem) {
        tvAbout.setText(itemView.getContext().getString(R.string.AR_activity_detail_activity_section_footer_text_673));
    }

    public static class Factory implements GenericRecyclerViewAdapter.IViewHolderFactory<String, FooterViewHolder> {

        @Override
        public FooterViewHolder createViewHolder(View itemView) {
            return new FooterViewHolder(itemView);
        }
    }

}
