package com.vitalityactive.va.activerewards.rewards.titledlists;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.vitalityactive.va.R;
import com.vitalityactive.va.activerewards.dto.CardMarginSettings;
import com.vitalityactive.va.activerewards.rewards.presenters.CurrentRewardsListPresenter;
import com.vitalityactive.va.activerewards.rewards.viewholder.ActiveRewardsPartnerViewHolder;
import com.vitalityactive.va.dto.PartnerItemDTO;
import com.vitalityactive.va.uicomponents.GenericTitledListContainerWithAdapter;
import com.vitalityactive.va.uicomponents.adapters.GenericRecyclerViewAdapter;
import com.vitalityactive.va.utilities.CMSImageLoader;

import java.util.ArrayList;
import java.util.List;

public class ParticipatingPartnersTitledList extends GenericTitledListContainerWithAdapter.TitledListWithAdapter {

    private GenericRecyclerViewAdapter<PartnerItemDTO, ActiveRewardsPartnerViewHolder> adapter;

    public ParticipatingPartnersTitledList(Context context,
                                           String title,
                                           CMSImageLoader cmsImageLoader,
                                           ActiveRewardsPartnerViewHolder.OnPartnerClickedListener clickListener,
                                           CardMarginSettings settings) {
        super(title, settings);
        addDividers = true;
        adapter = new GenericRecyclerViewAdapter<>(context,
                new ArrayList<PartnerItemDTO>(),
                R.layout.active_rewards_participating_partner_item,
                new ActiveRewardsPartnerViewHolder.Factory(cmsImageLoader, clickListener));
    }

    @Override
    public RecyclerView.Adapter buildAdapter(Context context) {
        return adapter;
    }

    public void setData(List<PartnerItemDTO> rewardPartners) {
        adapter.replaceData(rewardPartners);
    }

}
