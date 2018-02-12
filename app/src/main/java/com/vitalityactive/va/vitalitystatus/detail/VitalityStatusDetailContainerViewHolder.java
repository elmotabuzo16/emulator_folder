package com.vitalityactive.va.vitalitystatus.detail;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.vitalityactive.va.R;
import com.vitalityactive.va.activerewards.dto.TitleSubtitleAndIcon;
import com.vitalityactive.va.uicomponents.FlexibleDividerItemDecoration;
import com.vitalityactive.va.uicomponents.adapters.GenericRecyclerViewAdapter;
import com.vitalityactive.va.utilities.CMSImageLoader;
import com.vitalityactive.va.utilities.TextUtilities;
import com.vitalityactive.va.utilities.ViewUtilities;

import java.util.List;

public class VitalityStatusDetailContainerViewHolder extends GenericRecyclerViewAdapter.ViewHolder<List<TitleSubtitleAndIcon>> {

    private final CMSImageLoader cmsImageLoader;

    private VitalityStatusDetailContainerViewHolder(View itemView, String title, CMSImageLoader cmsImageLoader) {
        super(itemView);
        this.cmsImageLoader = cmsImageLoader;

        if (!TextUtilities.isNullOrWhitespace(title)) {
            ViewUtilities.setTextAndMakeVisibleIfPopulated((TextView) itemView.findViewById(R.id.container_title), title);
        }
    }

    @Override
    public void bindWith(List<TitleSubtitleAndIcon> rewardsDetails) {
        RecyclerView recyclerView = itemView.findViewById(R.id.recycler_view);
        FlexibleDividerItemDecoration divider = new FlexibleDividerItemDecoration.Builder(itemView.getContext())
                .setLeftMarginPx(ViewUtilities.pxFromDp(72))
                .build();
        recyclerView.addItemDecoration(divider);

        recyclerView.setAdapter(new GenericRecyclerViewAdapter<>(itemView.getContext(),
                rewardsDetails,
                R.layout.vitality_status_rewards_item,
                new GenericRecyclerViewAdapter.IViewHolderFactory<TitleSubtitleAndIcon, GenericRecyclerViewAdapter.ViewHolder<TitleSubtitleAndIcon>>() {
                    @Override
                    public GenericRecyclerViewAdapter.ViewHolder<TitleSubtitleAndIcon> createViewHolder(View itemView) {
                        return new GenericRecyclerViewAdapter.ViewHolder<TitleSubtitleAndIcon>(itemView) {
                            @Override
                            public void bindWith(TitleSubtitleAndIcon rewardsDetail) {
//                                ((TextView) itemView.findViewById(R.id.title)).setText(rewardsDetail.getContainerTitle());
                                ((TextView) itemView.findViewById(R.id.discount_text)).setText(rewardsDetail.getTitle());
                                ((TextView) itemView.findViewById(R.id.description_text)).setText(rewardsDetail.getSubtitle());

                                cmsImageLoader.loadImage(((ImageView) itemView.findViewById(R.id.icon)), rewardsDetail.getIconFileName(), R.drawable.img_placeholder);
                            }
                        };
                    }
                }));
    }

    public static class Factory
            implements GenericRecyclerViewAdapter.IViewHolderFactory<List<TitleSubtitleAndIcon>,
            VitalityStatusDetailContainerViewHolder> {
        private String title;
        private CMSImageLoader cmsImageLoader;

        public Factory(String title, CMSImageLoader cmsImageLoader) {
            this.title = title;
            this.cmsImageLoader = cmsImageLoader;
        }

        @Override
        public VitalityStatusDetailContainerViewHolder createViewHolder(View itemView) {
            return new VitalityStatusDetailContainerViewHolder(itemView, title, cmsImageLoader);
        }
    }
}
