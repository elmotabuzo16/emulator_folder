package com.vitalityactive.va.eventsfeed.views.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vitalityactive.va.EmptyStateViewHolder;
import com.vitalityactive.va.R;
import com.vitalityactive.va.networking.RequestFailedEvent;

/**
 * Created by jayellos on 11/17/17.
 */

public class EmptyStateFragment extends Fragment {
    public static final String ERROR_MESSAGE = "ERROR_MESSAGE";
    public static final String ERROR_SUBTITLE = "ERROR_SUBTITLE";
    private static final String GLOBAL_TINT_COLOR = "GLOBAL_TINT_COLOR";

    @NonNull
    public static EmptyStateFragment newInstance(RequestFailedEvent.Type errorState, int globalTintColor) {
        Bundle bundle = new Bundle();
        bundle.putInt(EmptyStateFragment.GLOBAL_TINT_COLOR , globalTintColor);

        if (errorState == RequestFailedEvent.Type.CONNECTION_ERROR) {
            bundle.putInt(EmptyStateFragment.ERROR_MESSAGE, R.string.connectivity_error_alert_title_44);
            bundle.putInt(EmptyStateFragment.ERROR_SUBTITLE, R.string.connectivity_error_alert_message_45);
        } else {
            bundle.putInt(EmptyStateFragment.ERROR_MESSAGE, R.string.error_unable_to_load_title_503);
            bundle.putInt(EmptyStateFragment.ERROR_SUBTITLE, R.string.error_unable_to_load_message_504);
        }

        EmptyStateFragment emptyStateFragment = new EmptyStateFragment();
        emptyStateFragment.setArguments(bundle);

        return emptyStateFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflatedView = inflater.inflate(R.layout.empty_state, container, false);

        int messageId = getArguments().getInt(ERROR_MESSAGE);
        int subtitleId = getArguments().getInt(ERROR_SUBTITLE);
        int globalTintColor = getArguments().getInt(GLOBAL_TINT_COLOR);

        new EmptyStateViewHolder(inflatedView).setup(messageId,
                subtitleId,
                R.string.try_again_button_title_43,
                new EmptyStateViewHolder.EmptyStatusButtonClickedListener() {
                    @Override
                    public void onEmptyStateButtonClicked(EmptyStateViewHolder emptyStateViewHolder) {
                        ((EventsFeedFragment)getParentFragment()).showLoadingIndicator();
                        ((SwipeRefreshLayout.OnRefreshListener) getParentFragment()).onRefresh();
                    }
                }).setButtonColor(globalTintColor);

        return inflatedView;
    }
}