package com.vitalityactive.va.vitalitystatus.viewholder;

import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.vitalityactive.va.R;
import com.vitalityactive.va.uicomponents.adapters.GenericRecyclerViewAdapter;
import com.vitalityactive.va.utilities.TextUtilities;
import com.vitalityactive.va.utilities.ViewUtilities;
import com.vitalityactive.va.vitalitystatus.VitalityStatusDTO;
import com.vitalityactive.va.vitalitystatus.levels.LevelStatusDTO;

public class StatusContentViewHolder
        extends GenericRecyclerViewAdapter.ViewHolder<VitalityStatusDTO> {

    private final TextView totalPointsText;
    private final TextView pointsStatusTextView;
    private boolean shouldShowMyRewards;

    private StatusContentViewHolder(View itemView, boolean shouldShowMyRewards) {
        super(itemView);

        totalPointsText = itemView.findViewById(R.id.total_points_text);
        pointsStatusTextView = itemView.findViewById(R.id.points_status_text);
        this.shouldShowMyRewards = shouldShowMyRewards;
    }

    @Override
    public void bindWith(VitalityStatusDTO vitalityStatusDTO) {
        totalPointsText.setText(String.valueOf(vitalityStatusDTO.getTotalPoints()));
        pointsStatusTextView.setText(getPointsStatusText(vitalityStatusDTO));

        RecyclerView recyclerView = itemView.findViewById(R.id.recycler_view);
        recyclerView.setAdapter(getPointsItemsAdapter(vitalityStatusDTO));
    }

    private String getPointsStatusText(VitalityStatusDTO vitalityStatusDTO) {
        String pointsStatus;
        String arg2;
        String arg3 = "";
        int placeHolderId;

        if (vitalityStatusDTO.getCurrentStatusLevel().getKey() == vitalityStatusDTO.getLowestStatusKey()) {
            placeHolderId = R.string.Status_landing_points_total_message_800;
            arg2 = String.valueOf(vitalityStatusDTO.getDaysRemaining());
            arg3 = vitalityStatusDTO.getHighestStatusName();
        } else if (vitalityStatusDTO.getCurrentStatusLevel().getKey() == vitalityStatusDTO.getHighestStatusKey() &&
                vitalityStatusDTO.getPointsStatusKey() == vitalityStatusDTO.getHighestStatusKey()) {
            placeHolderId = R.string.Statu_landing_total_points_final_message_825;
            arg2 = vitalityStatusDTO.getCurrentStatusLevel().getName();
        } else if (vitalityStatusDTO.getPointsToMaintainStatus() == 0) {
            placeHolderId = R.string.Status_landing_points_message_next_status_831;
            arg2 = String.valueOf(vitalityStatusDTO.getDaysRemaining());
        } else {
            placeHolderId = R.string.Status_landing_total_points_message_821;
            arg2 = String.valueOf(vitalityStatusDTO.getDaysRemaining());
        }

        pointsStatus = String.format(itemView.getContext().getString(placeHolderId),
                vitalityStatusDTO.getCurrentStatusLevel().getName(),
                arg2,
                TextUtilities.isNullOrWhitespace(arg3) ? null : arg3);

        return pointsStatus;
    }

    @NonNull
    private GenericRecyclerViewAdapter<LevelStatusDTO, GenericRecyclerViewAdapter.ViewHolder<LevelStatusDTO>> getPointsItemsAdapter(VitalityStatusDTO pointsContent) {
        return new GenericRecyclerViewAdapter<>(itemView.getContext(),
                pointsContent.getAvailableStatuses(),
                R.layout.vitality_status_landing_level_item,
                new GenericRecyclerViewAdapter.IViewHolderFactory<LevelStatusDTO, GenericRecyclerViewAdapter.ViewHolder<LevelStatusDTO>>() {
                    private ImageView icon;

                    @Override
                    public GenericRecyclerViewAdapter.ViewHolder<LevelStatusDTO> createViewHolder(View itemView) {
                        icon = itemView.findViewById(R.id.rewards_item_question_mark_icon);

                        return new GenericRecyclerViewAdapter.ViewHolder<LevelStatusDTO>(itemView) {
                            @Override
                            public void bindWith(LevelStatusDTO levelStatusDTO) {
                                String pointsStatus;
                                if (levelStatusDTO.getPointsThreshold() == 0) {
                                    itemView.findViewById(R.id.dashed_line).setVisibility(View.GONE);
                                    pointsStatus = itemView.getContext().getString(R.string.Status_landing_status_tracking_1_message_801);
                                } else {
                                    pointsStatus = String.format(itemView.getContext().getString(R.string.Status_points_progress_838), levelStatusDTO.getPointsThreshold());
                                }

                                ViewUtilities.setResourceOfView(itemView.findViewById(R.id.medal_icon), levelStatusDTO.getSmallIconResourceId());
                                ViewUtilities.setTextOfView(itemView.findViewById(R.id.title), levelStatusDTO.getName());
                                ViewUtilities.setTextOfView(itemView.findViewById(R.id.status_text), pointsStatus);

                                if (shouldShowMyRewards) {
                                    ViewUtilities.setViewVisible(icon);
                                    ViewUtilities.tintDrawable(icon.getDrawable(), ContextCompat.getColor(itemView.getContext(), R.color.vitality_orange));
                                }
                            }
                        };
                    }
                });
    }

    public static class Factory
            implements GenericRecyclerViewAdapter.IViewHolderFactory<VitalityStatusDTO, StatusContentViewHolder> {

        private boolean shouldShowMyRewards;

        public Factory(boolean shouldShowMyRewards) {
            this.shouldShowMyRewards = shouldShowMyRewards;
        }

        @Override
        public StatusContentViewHolder createViewHolder(View itemView) {
            return new StatusContentViewHolder(itemView, shouldShowMyRewards);
        }
    }
}
