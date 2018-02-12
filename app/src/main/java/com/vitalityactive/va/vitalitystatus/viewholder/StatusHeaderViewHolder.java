package com.vitalityactive.va.vitalitystatus.viewholder;

import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.vitalityactive.va.R;
import com.vitalityactive.va.activerewards.dto.TitleSubtitleAndIcon;
import com.vitalityactive.va.uicomponents.adapters.GenericRecyclerViewAdapter;
import com.vitalityactive.va.utilities.confetto.CommonConfetti;
import com.vitalityactive.va.utilities.confetto.ConfettiManager;

import java.util.ArrayList;
import java.util.List;

public class StatusHeaderViewHolder extends GenericRecyclerViewAdapter.ViewHolder<Object> {
    private final List<ConfettiManager> activeConfettiManagers = new ArrayList<>();
    private final int currentStatusLevelKey;
    protected ViewGroup container;
    private TextView levelStatus;
    private ImageView medalIcon;
    private TextView pointsStatusTextView;

    StatusHeaderViewHolder(View itemView, int currentStatusLevelKey) {
        super(itemView);
        this.currentStatusLevelKey = currentStatusLevelKey;

        assignViews(itemView);
    }

    protected void assignViews(View itemView) {
        medalIcon = itemView.findViewById(R.id.medal_icon);
        levelStatus = itemView.findViewById(R.id.level_status_text);
        pointsStatusTextView = itemView.findViewById(R.id.status_points_text);
    }

    @Override
    public void bindWith(Object data) {
        TitleSubtitleAndIcon headerItem = (TitleSubtitleAndIcon) data;

        this.levelStatus.setText(headerItem.getTitle());
        pointsStatusTextView.setText(headerItem.getSubtitle());
        medalIcon.setImageResource(headerItem.getIconResourceId());

        showConfettiIfEnabled(currentStatusLevelKey);
    }

    private void showConfettiIfEnabled(final int key) {
        if (shouldCreateConfetti()) {
            container = itemView.findViewById(R.id.confetti_container);

            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    activeConfettiManagers.add(generateInfinite(key));
                }
            });
        }
    }

    private ConfettiManager generateInfinite(int key) {
        int[] colors = CommonConfetti.getLevelColor(key, itemView.getContext());

        return CommonConfetti.fallingConfetti(container, colors).generate();
    }

    protected boolean shouldCreateConfetti() {
        return true;
    }

    public static class Factory
            implements GenericRecyclerViewAdapter.IViewHolderFactory<Object,
            StatusHeaderViewHolder> {


        private int currentStatusLevelKey;

        public Factory(int currentStatusLevelKey) {
            this.currentStatusLevelKey = currentStatusLevelKey;
        }

        @Override
        public StatusHeaderViewHolder createViewHolder(View itemView) {
            return new StatusHeaderViewHolder(itemView, currentStatusLevelKey);
        }
    }
}
