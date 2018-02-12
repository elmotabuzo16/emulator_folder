package com.vitalityactive.va.vitalitystatus.earningpoints;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.vitalityactive.va.NavigationCoordinator;
import com.vitalityactive.va.R;
import com.vitalityactive.va.uicomponents.adapters.GenericRecyclerViewAdapter;
import com.vitalityactive.va.utilities.ViewUtilities;
import com.vitalityactive.va.vitalitystatus.ProductPointsContent;

import java.util.List;

class ProductFeatureViewHolder extends GenericRecyclerViewAdapter.ViewHolder<List<PointsInformationDTO>> {

    private final NavigationCoordinator navigationCoordinator;
    private final ProductPointsContent content;

    private ProductFeatureViewHolder(View itemView, NavigationCoordinator navigationCoordinator, ProductPointsContent content) {
        super(itemView);
        this.navigationCoordinator = navigationCoordinator;
        this.content = content;
    }

    @Override
    public void bindWith(List<PointsInformationDTO> pointsInformationDTOs) {
        RecyclerView recyclerView = itemView.findViewById(R.id.recycler_view);

        recyclerView.setAdapter(createProductFeatureAdapter(pointsInformationDTOs, navigationCoordinator));
    }

    private RecyclerView.Adapter createProductFeatureAdapter(List<PointsInformationDTO> pointsInformationDTOs, final NavigationCoordinator navigationCoordinator) {
        return new GenericRecyclerViewAdapter<>(itemView.getContext(),
                pointsInformationDTOs,
                R.layout.vitality_status_earning_method_item,
                new GenericRecyclerViewAdapter.IViewHolderFactory<PointsInformationDTO, GenericRecyclerViewAdapter.ViewHolder<PointsInformationDTO>>() {
                    @Override
                    public GenericRecyclerViewAdapter.ViewHolder<PointsInformationDTO> createViewHolder(View itemView) {
                        return new GenericRecyclerViewAdapter.ViewHolder<PointsInformationDTO>(itemView) {
                            @Override
                            public void bindWith(PointsInformationDTO dataItem) {
                                ((TextView) itemView.findViewById(R.id.title)).setText(dataItem.getName());

                                ViewUtilities.setTextAndMakeVisibleIfPopulated(((TextView) itemView.findViewById(R.id.subtitle)),
                                        content.getSubtitleString(dataItem.getPointsEarningFlag(),
                                                dataItem.getPotentialPoints(), dataItem.isPointsLimitReached()));
                            }
                        };
                    }
                }, new GenericRecyclerViewAdapter.OnItemClickListener<PointsInformationDTO>() {
            @Override
            public void onClicked(int position, PointsInformationDTO pointsInformationDTO) {
                if (pointsInformationDTO.hasFeatures()) {
                    navigationCoordinator.navigateAfterPointsFeatureTapped((Activity) itemView.getContext(), pointsInformationDTO.getKey());
                } else {
                    navigationCoordinator.navigateToStatusHowToEarnPoints(((Activity) itemView.getContext()), pointsInformationDTO.getKey(), pointsInformationDTO.getName());
                }
            }
        }
        );
    }

    static class Factory implements GenericRecyclerViewAdapter.IViewHolderFactory<List<PointsInformationDTO>, ProductFeatureViewHolder> {
        private NavigationCoordinator navigationCoordinator;
        private ProductPointsContent content;

        public Factory(NavigationCoordinator navigationCoordinator, ProductPointsContent content) {
            this.navigationCoordinator = navigationCoordinator;
            this.content = content;
        }

        @Override
        public ProductFeatureViewHolder createViewHolder(View itemView) {
            return new ProductFeatureViewHolder(itemView, navigationCoordinator, content);
        }
    }
}
