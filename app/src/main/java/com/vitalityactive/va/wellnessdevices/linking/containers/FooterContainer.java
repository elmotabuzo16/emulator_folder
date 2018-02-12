package com.vitalityactive.va.wellnessdevices.linking.containers;

import android.view.View;
import android.widget.TextView;

import com.vitalityactive.va.R;
import com.vitalityactive.va.uicomponents.adapters.GenericRecyclerViewAdapter;

public class FooterContainer extends GenericRecyclerViewAdapter.ViewHolder<String> {
    private final TextView tvAbout;

    public FooterContainer(View itemView) {
        super(itemView);
        tvAbout = (TextView)itemView.findViewById(R.id.tvAbout);
    }

    @Override
    public void bindWith(String dataItem) {
        tvAbout.setText(itemView.getContext().getString(R.string.about_text_450, dataItem));
    }

    public static class Factory implements GenericRecyclerViewAdapter.IViewHolderFactory<String, FooterContainer> {

        @Override
        public FooterContainer createViewHolder(View itemView) {
            return new FooterContainer(itemView);
        }
    }

}
