package com.vitalityactive.va.uicomponents;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vitalityactive.va.R;
import com.vitalityactive.va.activerewards.dto.CardMarginSettings;
import com.vitalityactive.va.uicomponents.adapters.GenericRecyclerViewAdapter;

import java.util.Objects;

public abstract class BaseTitledListContainer<TTitledList extends BaseTitledListContainer.TitledList>
        extends GenericRecyclerViewAdapter.ViewHolder<TTitledList> {
    protected final Context context;
    protected final TextView title;
    protected final RecyclerView recyclerView;
    private final TextView footer;

    public BaseTitledListContainer(View itemView) {
        super(itemView);
        context = itemView.getContext();
        recyclerView = (RecyclerView) itemView.findViewById(R.id.recycler_view);
        title = (TextView) itemView.findViewById(R.id.title);
        footer = (TextView) itemView.findViewById(R.id.text);
    }

    @Override
    public void bindWith(TTitledList titledList) {
        setupRecyclerView(titledList);
        ViewGroup.LayoutParams layoutParams = itemView.getLayoutParams();
        if (layoutParams instanceof ViewGroup.MarginLayoutParams && !Objects.equals(titledList.title, "")) {
            setContainerMargins(titledList.settings, (ViewGroup.MarginLayoutParams) layoutParams);
            title.setText(titledList.title);
        }

        if (!Objects.equals(titledList.footer, "")) {
            footer.setText(titledList.footer);
        }
    }

    private void setContainerMargins(CardMarginSettings settings, ViewGroup.MarginLayoutParams layoutParams) {
        if (!settings.topMargin)
            layoutParams.topMargin = 0;
        if (!settings.bottomMargin)
            layoutParams.bottomMargin = 0;
        itemView.setLayoutParams(layoutParams);
    }

    protected abstract void setupRecyclerView(TTitledList titledList);

    public interface Factory<TTitledList extends TitledList, VH extends BaseTitledListContainer<TTitledList>>
            extends GenericRecyclerViewAdapter.IViewHolderFactory<TTitledList, VH> {
    }

    public static class TitledList {
        public String title;
        public String footer;
        public final CardMarginSettings settings;
        protected boolean addDividers = false;
        private int dividersLeftPaddingPx = 0;

        public TitledList(String title, String footer, CardMarginSettings settings) {

            this.title = title;
            this.footer = footer;
            this.settings = settings;
        }

        public boolean shouldAddDividers() {
            return addDividers;
        }

        public void setShouldAddDividers(boolean addDividers){
            this.addDividers = addDividers;
        }

        public int getDividersLeftPaddingPx() {
            return dividersLeftPaddingPx;
        }

        public void setDividersLeftPaddingPx(int dividersLeftPAddingPx) {
            this.dividersLeftPaddingPx = dividersLeftPAddingPx;
        }
    }
}
