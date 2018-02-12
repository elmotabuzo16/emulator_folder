package com.vitalityactive.va.myhealth.vitalityage;

import android.content.Context;
import android.view.View;

import com.vitalityactive.va.uicomponents.adapters.GenericRecyclerViewAdapter;

public class MyHealthUpdatedViewHolder extends GenericRecyclerViewAdapter.ViewHolder<VitalityAgeActivity.VitalityAgeMyHealth> {

    public MyHealthUpdatedViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void bindWith(final VitalityAgeActivity.VitalityAgeMyHealth vitalityAgeMyHealth) {

    }

    public static class Factory
            implements GenericRecyclerViewAdapter.IViewHolderFactory<VitalityAgeActivity.VitalityAgeMyHealth,
            MyHealthUpdatedViewHolder> {
        Context context;

        Factory(Context context) {
            this.context = context;
        }

        @Override
        public MyHealthUpdatedViewHolder createViewHolder(View itemView) {
            return new MyHealthUpdatedViewHolder(itemView);
        }
    }


}
