package com.vitalityactive.va.activerewards.participatingpartners;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.vitalityactive.va.BasePresentedActivity;
import com.vitalityactive.va.R;
import com.vitalityactive.va.activerewards.participatingpartners.presenters.ParticipatingPartnerDetailPresenter;
import com.vitalityactive.va.dependencyinjection.DependencyInjector;
import com.vitalityactive.va.dto.PartnerItemDTO;
import com.vitalityactive.va.utilities.CMSImageLoader;
import com.vitalityactive.va.utilities.ViewUtilities;

import javax.inject.Inject;

public class ParticipatingPartnerDetailActivity
        extends BasePresentedActivity<ParticipatingPartnerDetailPresenter.UI, ParticipatingPartnerDetailPresenter>
        implements ParticipatingPartnerDetailPresenter.UI {
    public static final String KEY_PARTNER_ID = "ParticipatingPartnerId";

    @Inject
    ParticipatingPartnerDetailPresenter presenter;

    @Inject
    CMSImageLoader cmsImageLoader;
    private ImageView partnerLogo;
    private TextView partnerName;
    private TextView partnerDescription;

    @Override
    protected void create(@Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_participating_partner_detail);
        presenter.setPartnerId(getIntent().getLongExtra(KEY_PARTNER_ID, 0));

        setUpActionBarWithTitle(presenter.getPartnerName())
                .setDisplayHomeAsUpEnabled(true);

        partnerLogo = findViewById(R.id.partner_logo);
        partnerName = findViewById(R.id.partner_name);
        partnerDescription = findViewById(R.id.partner_description);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void injectDependencies(DependencyInjector dependencyInjector) {
        navigationCoordinator.getActiveRewardsDependencyInjector().inject(this);
    }

    @Override
    protected ParticipatingPartnerDetailPresenter.UI getUserInterface() {
        return this;
    }

    @Override
    protected ParticipatingPartnerDetailPresenter getPresenter() {
        return presenter;
    }

    @Override
    public void hideLoadingIndicator() {
        findViewById(R.id.loadingPanel).setVisibility(View.GONE);
    }

    @Override
    public void showLoadingIndicator() {
        findViewById(R.id.loadingPanel).setVisibility(View.VISIBLE);
    }

    @Override
    public void showPartnerDetails(PartnerItemDTO partnerItem) {
        hideLoadingIndicator();
        if (partnerItem == null) {
            return;
        }
        cmsImageLoader.loadImage(partnerLogo, partnerItem.logoFileName, R.drawable.img_placeholder);
        ViewUtilities.setTextAndMakeVisibleIfPopulated(partnerName, partnerItem.title);
        ViewUtilities.setTextAndMakeVisibleIfPopulated(partnerDescription, partnerItem.longDescription);
    }

}
