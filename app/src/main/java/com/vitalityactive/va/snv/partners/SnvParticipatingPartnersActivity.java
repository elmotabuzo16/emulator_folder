package com.vitalityactive.va.snv.partners;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.vitalityactive.va.BasePresentedActivity;
import com.vitalityactive.va.R;
import com.vitalityactive.va.appconfig.AppConfigRepository;
import com.vitalityactive.va.dependencyinjection.DependencyInjector;
import com.vitalityactive.va.snv.partners.dto.ProductFeatureGroupDto;
import com.vitalityactive.va.snv.partners.presenter.SnvParticipatingPartnersPresenter;
import com.vitalityactive.va.snv.shared.SnvConstants;
import com.vitalityactive.va.uicomponents.AlertDialogFragment;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by kerry.e.lawagan on 12/8/2017.
 */

public class SnvParticipatingPartnersActivity extends BasePresentedActivity<SnvParticipatingPartnersPresenter.UserInterface, SnvParticipatingPartnersPresenter<SnvParticipatingPartnersPresenter.UserInterface>> implements SnvParticipatingPartnersPresenter.UserInterface {
    @Inject
    SnvParticipatingPartnersPresenter presenter;
    @Inject
    AppConfigRepository appConfigRepository;
    RecyclerView recyclerView;

    @Override
    protected void create(Bundle savedInstanceState) {
        super.create(savedInstanceState);
        setContentView(R.layout.snv_activity_participating_partners);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView = findViewById(R.id.snvPartnersRecyclerView);
        recyclerView.setLayoutManager(linearLayoutManager);

        setUpActionBarWithTitle(getString(R.string.SV_partner_title_1029)).setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void injectDependencies(DependencyInjector dependencyInjector) {
        dependencyInjector.inject(this);
    }

    @Override
    protected SnvParticipatingPartnersPresenter.UserInterface getUserInterface() {
        return this;
    }

    @Override
    protected SnvParticipatingPartnersPresenter<SnvParticipatingPartnersPresenter.UserInterface> getPresenter() {
        return presenter;
    }

    @Override
    public void updateListItems(final List<ProductFeatureGroupDto> productFeatureGroupDtos) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                recyclerView = findViewById(R.id.snvPartnersRecyclerView);
                recyclerView.setAdapter(new PartnersAdapter(productFeatureGroupDtos));
                recyclerView.refreshDrawableState();
                recyclerView.invalidate();
            }
        });
    }

    @Override
    public void showConnectionContentRequestErrorMessage() {
        getConnectionAlertDialogFragment(SnvConstants.SNV_CONNECTION_ERROR_ALERT)
                .setCustomPrimaryColor(appConfigRepository.getGlobalTintColorHex())
                .show(getSupportFragmentManager(), SnvConstants.SNV_CONNECTION_ERROR_ALERT);
    }

    @Override
    public void showGenericContentRequestErrorMessage() {
        AlertDialogFragment alert = AlertDialogFragment.create(
                SnvConstants.SNV_GENERIC_ERROR_ALERT,
                getString(R.string.alert_unknown_title_266),
                getString(R.string.SV_partners_error_message),
                getString(R.string.cancel_button_title_24),
                null,
                getString(R.string.try_again_button_title_43));
        alert.show(getSupportFragmentManager(), SnvConstants.SNV_GENERIC_ERROR_ALERT);
    }

    @Override
    public void showLoadingIndicator() {
        super.showLoadingIndicator();
    }

    @Override
    public void hideLoadingIndicator() {

        super.hideLoadingIndicator();
    }

    private AlertDialogFragment getConnectionAlertDialogFragment(String tag) {
        return AlertDialogFragment.create(
                tag,
                getString(R.string.connectivity_error_alert_title_44),
                getString(R.string.connectivity_error_alert_message_45),
                getString(R.string.cancel_button_title_24),
                null,
                getString(R.string.try_again_button_title_43));
    }

    private Activity getActivity() {
        return this;
    }

    private class PartnersAdapter extends RecyclerView.Adapter<PartnersAdapter.ViewHolder> {

        private final String TAG = PartnersAdapter.class.getName();
        private List<ProductFeatureGroupDto> objectList;

        public PartnersAdapter(List<ProductFeatureGroupDto> productFeatureGroupDtos) {
            objectList = productFeatureGroupDtos;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.snv_participating_partner_list_row, parent, false);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int itemPosition = recyclerView.indexOfChild(view);
                    ImageView imageView = view.findViewById(R.id.snvPartnerImageView);
                    if (imageView.getDrawable()!=null) {
                        SnvParticipatingPartnersImageHelper.drawable = imageView.getDrawable();
                    }
                    navigationCoordinator.navigateToSnvParticipatingPartnersDetailActivity(getActivity(), objectList.get(itemPosition));
                }
            });
            return new PartnersAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            ProductFeatureGroupDto object = objectList.get(position);

            if (position==0) {
                holder.image.setImageDrawable(getDrawable(R.drawable.clicks));
            } else if (position==1) {
                holder.image.setImageDrawable(getDrawable(R.drawable.dis_chem));
            } else if (position==2) {
                holder.image.setImageDrawable(getDrawable(R.drawable.easy_way));
            }

            holder.title.setText(object.getName());
            if (!TextUtils.isEmpty(object.getLogoFileName())) {
                // TODO: fetch individual partner logo from CMS
                //presenter.getCmsImageLoader().loadImage(holder.image, object.getLogoFileName(), R.drawable.img_placeholder);
            }
        }

        @Override
        public int getItemCount() {
            return objectList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            private final TextView title;
            private final ImageView image;

            public ViewHolder(View view) {
                super(view);
                title = view.findViewById(R.id.snvPartnerNameTextView);
                image = view.findViewById(R.id.snvPartnerImageView);
            }
        }
    }
}
