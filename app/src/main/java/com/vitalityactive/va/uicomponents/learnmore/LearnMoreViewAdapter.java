package com.vitalityactive.va.uicomponents.learnmore;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vitalityactive.va.R;
import com.vitalityactive.va.uicomponents.adapters.GenericRecyclerViewAdapter;

import java.util.List;

public class LearnMoreViewAdapter<DataType, VH extends GenericRecyclerViewAdapter.ViewHolder<DataType>>
        extends GenericRecyclerViewAdapter<DataType, VH> {
    private final String title;
    private final String subTitle;

    public LearnMoreViewAdapter(Context context,
                                List<DataType> data,
                                IViewHolderFactory<DataType, VH> viewHolderFactory,
                                String title,
                                String subTitle,
                                @LayoutRes int resId) {
        super(context, data, resId, viewHolderFactory);
        this.title = title;
        this.subTitle = subTitle;
    }

    @Override
    protected View createHolderTemplate(ViewGroup viewGroup, int viewType) {
        View view = super.createHolderTemplate(viewGroup, viewType);

        TextView tvTitle = (TextView) view.findViewById(R.id.title);
        TextView tvSubTitle = (TextView) view.findViewById(R.id.subtitle);
        tvTitle.setText(title);
        tvSubTitle.setText(subTitle);
        return view;
    }
}
