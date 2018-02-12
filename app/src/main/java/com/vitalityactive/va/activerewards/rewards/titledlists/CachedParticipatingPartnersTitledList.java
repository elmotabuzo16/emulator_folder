package com.vitalityactive.va.activerewards.rewards.titledlists;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.vitalityactive.va.activerewards.dto.CardMarginSettings;
import com.vitalityactive.va.activerewards.participatingpartners.ParticipatingPartnersAdapterFactory;
import com.vitalityactive.va.activerewards.rewards.content.RewardPartnerContent;
import com.vitalityactive.va.uicomponents.GenericTitledListContainerWithAdapter;
import com.vitalityactive.va.uicomponents.adapters.GenericRecyclerViewAdapter;

public class CachedParticipatingPartnersTitledList extends GenericTitledListContainerWithAdapter.TitledListWithAdapter
        implements GenericRecyclerViewAdapter.OnItemClickListener<RewardPartnerContent> {
    private final ClickListener clickListener;

    public CachedParticipatingPartnersTitledList(String title, ClickListener clickListener, CardMarginSettings settings) {
        super(title, settings);
        this.clickListener = clickListener;
        addDividers = true;
    }

    @Override
    public RecyclerView.Adapter buildAdapter(Context context) {
        return new ParticipatingPartnersAdapterFactory(context)
                .setClickListener(this)
                .build();
    }

    @Override
    public void onClicked(int position, RewardPartnerContent item) {
        if (clickListener != null) {
            clickListener.onPartnerClicked(item);
        }
    }

    public interface ClickListener {
        void onPartnerClicked(RewardPartnerContent item);
    }
}
