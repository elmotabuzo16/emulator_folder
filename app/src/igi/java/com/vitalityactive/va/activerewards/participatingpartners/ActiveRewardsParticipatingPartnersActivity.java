package com.vitalityactive.va.activerewards.participatingpartners;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.vitalityactive.va.BaseActivity;
import com.vitalityactive.va.R;
import com.vitalityactive.va.activerewards.rewards.content.RewardPartnerContent;
import com.vitalityactive.va.uicomponents.adapters.GenericRecyclerViewAdapter;
import com.vitalityactive.va.utilities.ViewUtilities;

public class ActiveRewardsParticipatingPartnersActivity extends BaseActivity
        implements GenericRecyclerViewAdapter.OnItemClickListener<RewardPartnerContent> {
    private static final String TAG = "ParticipatingPartnersAc";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_rewards_participating_partners);

        setUpActionBarWithTitle(R.string.AR_learn_more_participating_partners_title_696)
                .setDisplayHomeAsUpEnabled(true);

        setUpRecyclerView();
    }

    private void setUpRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.main_recyclerview);
        recyclerView.setAdapter(createAdapter());
        ViewUtilities.addDividers(this, recyclerView);
        ViewUtilities.scrollToTop(recyclerView);
    }

    private RecyclerView.Adapter createAdapter() {
        return new ParticipatingPartnersAdapterFactory(this)
                .setClickListener(this)
                .build();
    }

    @Override
    public void onClicked(int position, RewardPartnerContent item) {
        Log.d(TAG, "clicked on partner: " + item.getPartnerName());
        navigationCoordinator.navigateToCachedActiveRewardsParticipatingPartner(this, item.getRewardId());
    }
}
