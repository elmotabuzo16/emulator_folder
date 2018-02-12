package com.vitalityactive.va.uicomponents;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.vitalityactive.va.activerewards.dto.CardMarginSettings;
import com.vitalityactive.va.utilities.ViewUtilities;

public class GenericTitledListContainerWithAdapter<TTitledList extends GenericTitledListContainerWithAdapter.TitledListWithAdapter>
        extends BaseTitledListContainer<TTitledList> {
    public GenericTitledListContainerWithAdapter(View itemView) {
        super(itemView);
    }

    @Override
    protected void setupRecyclerView(TTitledList titledListWithAdapter) {
        recyclerView.setAdapter(titledListWithAdapter.buildAdapter(context));
        if (titledListWithAdapter.shouldAddDividers()) {
            if (!ViewUtilities.alreadyHaveAnItemDecorator(recyclerView)) {
                ViewUtilities.addDividers(context, recyclerView, titledListWithAdapter.getDividersLeftPaddingPx());
            }
        }
      //  ViewUtilities.scrollToTop(recyclerView);
    }

    public static class Factory<TTitledList extends TitledListWithAdapter>
            implements BaseTitledListContainer.Factory<TTitledList, GenericTitledListContainerWithAdapter<TTitledList>> {
        @Override
        public GenericTitledListContainerWithAdapter<TTitledList> createViewHolder(View itemView) {
            return new GenericTitledListContainerWithAdapter<>(itemView);
        }
    }

    public static abstract class TitledListWithAdapter extends TitledList {

        public TitledListWithAdapter(String title) {
            this(title, new CardMarginSettings());
        }

        public TitledListWithAdapter(String title, String footer) {
            this(title, footer, new CardMarginSettings());
        }

        public TitledListWithAdapter(String title, CardMarginSettings settings) {
            this(title, "", settings);
        }

        public TitledListWithAdapter(String title, String footer, CardMarginSettings settings) {
            super(title, footer, settings);
        }

        public abstract RecyclerView.Adapter buildAdapter(Context context);
    }
}
