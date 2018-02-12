package com.vitalityactive.va.activerewards.history.uicontainers;

import android.view.View;
import android.widget.Button;

import com.vitalityactive.va.R;
import com.vitalityactive.va.uicomponents.adapters.GenericRecyclerViewAdapter;

/**
 * Created by dloskot on 9/1/17.
 */

public class PaginationFooterContainer extends GenericRecyclerViewAdapter.ViewHolder<String> {

    private View loading;
    private View endOfList;
    private View error;
    private Button tryAgainBtn;

    public PaginationFooterContainer(View itemView, View.OnClickListener onClickListener) {
        super(itemView);
        loading = itemView.findViewById(R.id.loading);
        endOfList = itemView.findViewById(R.id.end_of_list);
        error = itemView.findViewById(R.id.error);
        tryAgainBtn = (Button) itemView.findViewById(R.id.try_again_btn);
        tryAgainBtn.setOnClickListener(onClickListener);
    }

    public void showLoading() {
        endOfList.setVisibility(View.GONE);
        error.setVisibility(View.GONE);
        loading.setVisibility(View.VISIBLE);
    }

    public void hideFooter() {
        endOfList.setVisibility(View.GONE);
        error.setVisibility(View.VISIBLE);
        loading.setVisibility(View.GONE);
    }

    public void showEndOfList() {
        endOfList.setVisibility(View.VISIBLE);
        error.setVisibility(View.GONE);
        loading.setVisibility(View.GONE);
    }

    public void showError() {
        endOfList.setVisibility(View.GONE);
        error.setVisibility(View.VISIBLE);
        loading.setVisibility(View.GONE);
    }

    @Override
    public void bindWith(String string) {
    }

    public static class Factory implements GenericRecyclerViewAdapter.IViewHolderFactory<String, PaginationFooterContainer> {
        private final View.OnClickListener onClickListener;

        public Factory(View.OnClickListener onClickListener){
            this.onClickListener = onClickListener;
        }

        @Override
        public PaginationFooterContainer createViewHolder(View itemView) {
            return new PaginationFooterContainer(itemView, onClickListener);
        }
    }
}
