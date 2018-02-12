package com.vitalityactive.va.activerewards.participatingpartners;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.MenuItem;
import android.webkit.WebView;

import com.vitalityactive.va.BaseActivity;
import com.vitalityactive.va.R;
import com.vitalityactive.va.activerewards.rewards.content.RewardPartnerContent;
import com.vitalityactive.va.utilities.FileUtilities;

import java.io.IOException;

public class BaseCachedParticipatingPartnerDetailActivity extends BaseActivity {

    private static final String TAG = BaseCachedParticipatingPartnerDetailActivity.class.getSimpleName();
    public static final String KEY_PARTNER_ID = "ParticipatingPartnerId";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cached_participating_partner_detail);
        int partnerId = getIntent().getIntExtra(KEY_PARTNER_ID, 0);
        RewardPartnerContent rewardPartnerContent = RewardPartnerContent.fromRewardId(partnerId);
        setUpActionBarWithTitle(rewardPartnerContent.getPartnerName())
                .setDisplayHomeAsUpEnabled(true);
        populateWebView(rewardPartnerContent);
    }

    private void populateWebView(RewardPartnerContent rewardPartnerContent) {
        try {
            String filename = rewardPartnerContent.getDetailsFileName();
            this.<WebView>findViewById(R.id.webview).loadData(FileUtilities.readFile(getAssets().open(filename)), "text/html; charset=utf-8", "utf-8");
        } catch (IOException e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
