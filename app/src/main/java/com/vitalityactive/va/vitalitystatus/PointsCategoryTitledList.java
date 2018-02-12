package com.vitalityactive.va.vitalitystatus;

import android.app.Activity;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.vitalityactive.va.NavigationCoordinator;
import com.vitalityactive.va.R;
import com.vitalityactive.va.uicomponents.GenericTitledListContainerWithAdapter;
import com.vitalityactive.va.uicomponents.adapters.GenericRecyclerViewAdapter;
import com.vitalityactive.va.utilities.ViewUtilities;
import com.vitalityactive.va.vitalitystatus.earningpoints.PointsInformationDTO;

import java.util.Collections;
import java.util.List;

public class PointsCategoryTitledList
        extends GenericTitledListContainerWithAdapter.TitledListWithAdapter {

    private final List<PointsInformationDTO> pointsInformationDTOs;
    private final NavigationCoordinator navigationCoordinator;
    private final ProductPointsContent content;

    public PointsCategoryTitledList(String title,
                                    List<PointsInformationDTO> pointsInformationDTOs,
                                    NavigationCoordinator navigationCoordinator,
                                    ProductPointsContent content) {
        super(title);
        this.pointsInformationDTOs = pointsInformationDTOs;
        this.navigationCoordinator = navigationCoordinator;
        this.content = content;

        setDividersLeftPaddingPx(ViewUtilities.pxFromDp(16));
        setShouldAddDividers(true);
    }

    @Override
    public RecyclerView.Adapter buildAdapter(final Context context) {
        int layoutToUse = pointsInformationDTOs.isEmpty() ? R.layout.vitality_status_empty_item : R.layout.vitality_status_landing_earning_method_item;
        GenericRecyclerViewAdapter.IViewHolderFactory viewHolder = pointsInformationDTOs.isEmpty() ? new EmptyPointsCategoryViewHolder.Factory() : new PointsCategoryViewHolder.Factory(content);


        return new GenericRecyclerViewAdapter<PointsInformationDTO, PointsCategoryViewHolder>(context,
                pointsInformationDTOs.isEmpty() ? Collections.singletonList(new PointsInformationDTO()) : pointsInformationDTOs,
                layoutToUse,
                viewHolder,
                new GenericRecyclerViewAdapter.OnItemClickListener<PointsInformationDTO>() {
                    @Override
                    public void onClicked(int position, PointsInformationDTO item) {
                        if (item.hasFeatures()) {
                            navigationCoordinator.navigateAfterPointsCategoryTapped((Activity) context, item.getKey());
                        }
                    }
                });
    }
}

class PointsCategoryViewHolder extends GenericRecyclerViewAdapter.ViewHolder<PointsInformationDTO> {
    private final ImageView icon;
    private final TextView title;
    private final ImageView completed_icon;
    private final TextView limitStatus;
    private final ImageView pointsIcon;
    private final ProductPointsContent content;
    private final TextView subtitle;

    private PointsCategoryViewHolder(View itemView, ProductPointsContent content) {
        super(itemView);
        title = itemView.findViewById(R.id.title);
        subtitle = itemView.findViewById(R.id.subtitle);
        icon = itemView.findViewById(R.id.icon);
        completed_icon = itemView.findViewById(R.id.completed_icon);
        limitStatus = itemView.findViewById(R.id.limit_status);
        pointsIcon = itemView.findViewById(R.id.points_icon);
        this.content = content;
    }

    @Override
    public void bindWith(PointsInformationDTO dataItem) {
        boolean pointsLimitReached = dataItem.isPointsLimitReached();

        title.setText(dataItem.getName());
        icon.setImageResource(content.getIconResourceId(dataItem.getKey()));
        subtitle.setText(content.getSubtitleString(dataItem.getPointsEarningFlag(),
                dataItem.getPotentialPoints(),
                pointsLimitReached));

        if (pointsLimitReached) {
            ViewUtilities.tintDrawable(pointsIcon.getDrawable(), ContextCompat.getColor(itemView.getContext(), R.color.secondary_54));

            ViewUtilities.setViewVisible(completed_icon);
            ViewUtilities.setViewVisible(limitStatus);
            ViewUtilities.setViewVisible(pointsIcon);
        }
    }

    public static class Factory implements GenericRecyclerViewAdapter.IViewHolderFactory<PointsInformationDTO, PointsCategoryViewHolder> {
        private ProductPointsContent content;

        public Factory(ProductPointsContent content) {
            this.content = content;
        }

        @Override
        public PointsCategoryViewHolder createViewHolder(View itemView) {
            return new PointsCategoryViewHolder(itemView, content);
        }
    }
}

class EmptyPointsCategoryViewHolder extends GenericRecyclerViewAdapter.ViewHolder<PointsInformationDTO> {

    private EmptyPointsCategoryViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void bindWith(PointsInformationDTO dataItem) {
    }

    public static class Factory implements GenericRecyclerViewAdapter.IViewHolderFactory<PointsInformationDTO, EmptyPointsCategoryViewHolder> {

        public Factory() {
        }

        @Override
        public EmptyPointsCategoryViewHolder createViewHolder(View itemView) {
            return new EmptyPointsCategoryViewHolder(itemView);
        }
    }
}