package com.vitalityactive.va.activerewards.participatingpartners;

import android.content.Context;
import android.support.annotation.NonNull;

import com.vitalityactive.va.R;
import com.vitalityactive.va.activerewards.rewards.content.RewardPartnerContent;
import com.vitalityactive.va.activerewards.viewholder.ActiveRewardsPartnerViewHolder;
import com.vitalityactive.va.uicomponents.adapters.GenericRecyclerViewAdapter;

import java.util.Arrays;
import java.util.List;

public class ParticipatingPartnersAdapterFactory {
    private Context context;
    private GenericRecyclerViewAdapter.OnItemClickListener<RewardPartnerContent> clickListener = null;

    public ParticipatingPartnersAdapterFactory(Context context) {
        this.context = context;
    }

    public ParticipatingPartnersAdapterFactory setClickListener(GenericRecyclerViewAdapter.OnItemClickListener<RewardPartnerContent> clickListener) {
        this.clickListener = clickListener;
        return this;
    }

    @NonNull
    public GenericRecyclerViewAdapter build() {
        return new GenericRecyclerViewAdapter<>(context,
                createParticipatingPartnersListData(),
                R.layout.active_rewards_participating_partner_item,
                new ActiveRewardsPartnerViewHolder.Factory(),
                clickListener);
    }

    private List<RewardPartnerContent> createParticipatingPartnersListData() {
        return Arrays.asList(
                RewardPartnerContent.EASY_TICKETS,
                RewardPartnerContent.FOOD_PANDA
        );
    }
}
