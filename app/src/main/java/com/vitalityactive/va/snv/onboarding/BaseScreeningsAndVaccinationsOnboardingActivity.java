package com.vitalityactive.va.snv.onboarding;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.vitalityactive.va.BasePresentedActivity;
import com.vitalityactive.va.R;
import com.vitalityactive.va.activerewards.dto.CardMarginSettings;
import com.vitalityactive.va.appconfig.AppConfigRepository;
import com.vitalityactive.va.dependencyinjection.DependencyInjector;
import com.vitalityactive.va.menu.MenuBuilder;
import com.vitalityactive.va.menu.MenuContainerViewHolder;
import com.vitalityactive.va.menu.MenuItemType;
import com.vitalityactive.va.snv.onboarding.presenter.ScreeningsAndVaccinationsOnboardingPresenter;
import com.vitalityactive.va.snv.shared.SnvConstants;
import com.vitalityactive.va.uicomponents.AlertDialogFragment;
import com.vitalityactive.va.uicomponents.adapters.ContainersRecyclerViewAdapter;
import com.vitalityactive.va.uicomponents.adapters.GenericRecyclerViewAdapter;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

public abstract class BaseScreeningsAndVaccinationsOnboardingActivity extends BasePresentedActivity<ScreeningsAndVaccinationsOnboardingPresenter.UserInterface, ScreeningsAndVaccinationsOnboardingPresenter<ScreeningsAndVaccinationsOnboardingPresenter.UserInterface>> implements ScreeningsAndVaccinationsOnboardingPresenter.UserInterface, RecyclerView.OnChildAttachStateChangeListener, MenuContainerViewHolder.OnMenuItemClickedListener {

    private static final String GENERIC_ERROR_ALERT = "GENERIC_ERROR_ALERT";
    @Inject
    ScreeningsAndVaccinationsOnboardingPresenter screeningsAndVaccinationsOnboardingPresenter;

    @Inject
    AppConfigRepository appConfigRepository;

    TextView textView3;

    TextView screeningsPointsTextView;
    TextView vaccinationsPointsTextView;
    String pointsLabel;
    NumberFormat numberFormat;
    RecyclerView snvRecyclerVIew;
    private ContainersRecyclerViewAdapter container;

    @Override
    protected void create(Bundle savedInstanceState) {
        super.create(savedInstanceState);
        setContentView(R.layout.activity_screenings_and_vaccinations_onboarding);

        setupSNVOnboardingScreen();
        marketUiUpdate();
    }

    private void setupSNVOnboardingScreen() {
        updateIcons();
        bindUi();
        addListeners();
        setupRecyclerView();

        setUpActionBarWithTitle(getString(R.string.home_card_card_section_title_365)).setDisplayHomeAsUpEnabled(true);

        numberFormat = NumberFormat.getNumberInstance(Locale.getDefault());
    }

    private void updateIcons() {
        Drawable mIcon;

        mIcon = ContextCompat.getDrawable(this, R.drawable.screenings);
        mIcon.setColorFilter(ContextCompat.getColor(this, R.color.button_bar_gray), PorterDuff.Mode.SRC_ATOP);
        ImageView imageView = findViewById(R.id.imageViewScreenings);
        imageView.setImageDrawable(mIcon);

        mIcon = ContextCompat.getDrawable(this, R.drawable.vaccinations);
        mIcon.setColorFilter(ContextCompat.getColor(this, R.color.button_bar_gray), PorterDuff.Mode.SRC_ATOP);
        imageView = findViewById(R.id.imageViewVaccinations);
        imageView.setImageDrawable(mIcon);

        mIcon = ContextCompat.getDrawable(this, R.drawable.vhc_points_pending);
        mIcon.setColorFilter(ContextCompat.getColor(this, R.color.button_bar_gray), PorterDuff.Mode.SRC_ATOP);
        imageView = findViewById(R.id.imageViewScreeningPoints);
        imageView.setImageDrawable(mIcon);

        mIcon = ContextCompat.getDrawable(this, R.drawable.vhc_points_pending);
        mIcon.setColorFilter(ContextCompat.getColor(this, R.color.button_bar_gray), PorterDuff.Mode.SRC_ATOP);
        imageView = findViewById(R.id.imageViewVaccinationPoints);
        imageView.setImageDrawable(mIcon);
    }

    private void bindUi() {
        textView3 = findViewById(R.id.textView3);
        pointsLabel = getResources().getText(R.string.home_card_card_potential_points_message_97).toString();
    }

    private void addListeners() {
        findViewById(R.id.health_actions_screenings_container).setOnClickListener(v ->
                navigationCoordinator.navigateToHealthActions(getActivity(), SnvConstants.HEALTH_ACTION_SCREENINGS)
        );
        findViewById(R.id.health_actions_vaccinations_container).setOnClickListener(v ->
                navigationCoordinator.navigateToHealthActions(getActivity(), SnvConstants.HEALTH_ACTION_VACCINATIONS)
        );
        findViewById(R.id.confirm_and_submit_btn).setOnClickListener(v ->
                navigationCoordinator.navigateAfterConfirmAndSubmitTapped(getActivity())
        );
    }

    protected abstract void marketUiUpdate();

    private Activity getActivity() {
        return this;
    }

    @Override
    protected void injectDependencies(DependencyInjector dependencyInjector) {
        dependencyInjector.inject(this);
    }

    @Override
    protected ScreeningsAndVaccinationsOnboardingPresenter.UserInterface getUserInterface() {
        return this;
    }

    @Override
    protected ScreeningsAndVaccinationsOnboardingPresenter getPresenter() {
        return screeningsAndVaccinationsOnboardingPresenter;
    }

    @Override
    public void showLoadingIndicator() {
        super.showLoadingIndicator();
    }

    @Override
    public void hideLoadingIndicator() {
        super.hideLoadingIndicator();
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
                getString(R.string.SV_alert_unable_to_complete_message_1036),
                getString(R.string.cancel_button_title_24),
                null,
                getString(R.string.try_again_button_title_43));
        alert.show(getSupportFragmentManager(), SnvConstants.SNV_GENERIC_ERROR_ALERT);
    }

    @Override
    public void onChildViewAttachedToWindow(View view) {
    }

    @Override
    public void onChildViewDetachedFromWindow(View view) {
    }

    @Override
    public void onClicked(MenuItemType menuItemType) {
        navigationCoordinator.navigateOnMenuItemFromVitalityStatus(getActivity(), menuItemType);
    }

    @Override
    public void updateScreeningsPoints(int points) {
        final String p = numberFormat.format(points);
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                screeningsPointsTextView = findViewById(R.id.screeningsPointsTextView);
                screeningsPointsTextView.setText(pointsLabel.replaceFirst("%s", p));
            }
        });
    }

    @Override
    public void updateVaccinationsPoints(int points) {
        final String p = numberFormat.format(points);
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                vaccinationsPointsTextView = findViewById(R.id.vaccinationsPointsTextView);
                vaccinationsPointsTextView.setText(pointsLabel.replaceFirst("%s", p));
            }
        });
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

    private void setupRecyclerView() {
        snvRecyclerVIew = findViewById(R.id.snv_recycler_view);

        container = new ContainersRecyclerViewAdapter(createAdapters());
        snvRecyclerVIew.setAdapter(container);
        snvRecyclerVIew.addOnChildAttachStateChangeListener(this);
        //ViewUtilities.scrollToTop(helpRecyclerView);
    }

    @NonNull
    private List<GenericRecyclerViewAdapter> createAdapters() {
        ArrayList<GenericRecyclerViewAdapter> adapters = new ArrayList<>();
        adapters.add(getMenuAdapter());

        return adapters;
    }

    private GenericRecyclerViewAdapter<CardMarginSettings, MenuContainerViewHolder> getMenuAdapter() {
        return new MenuBuilder(getActivity())
                .addMenuItem(com.vitalityactive.va.menu.MenuItem.Builder.help(
                        Color.parseColor("#" + Integer.toHexString(
                                ContextCompat.getColor(this, R.color.jungle_green)
                        ))
                ))
                .setClickListener(this)
                .build();
    }

}
