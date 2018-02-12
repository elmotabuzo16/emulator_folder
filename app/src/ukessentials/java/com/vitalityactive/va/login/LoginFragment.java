package com.vitalityactive.va.login;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;

import com.vitalityactive.va.BaseUkePresentedFragment;
import com.vitalityactive.va.BuildConfig;
import com.vitalityactive.va.R;
import com.vitalityactive.va.VitalityActiveApplication;
import com.vitalityactive.va.dependencyinjection.BaseUrl;
import com.vitalityactive.va.dependencyinjection.DependencyInjector;
import com.vitalityactive.va.login.getstarted.UKEGetStartedPresenter;
import com.vitalityactive.va.uicomponents.AlertDialogFragment;

import java.util.ArrayList;

import javax.inject.Inject;

public class LoginFragment
        extends BaseUkePresentedFragment<UKEGetStartedPresenter.UserInterface, UKEGetStartedPresenter>
        implements UKEGetStartedPresenter.UserInterface, View.OnTouchListener, AlertDialogFragment.OnItemSelectedListener {
    private static final String ALERT_DIALOG_TAG = "URL_SWITCHER";
    public static final String EXTRA_FAILURE = "EXTRA_FAILURE";
    public static final String URL_SWITCH = "URL_SWITCH";
    private static final int TAPS_TO_UNLOCK_ENVIRONMENT_SWITCHER = 3;

    @Inject
    UKEGetStartedPresenter loginPresenter;

    @Inject
    UKEURLSwitcher baseURLSwitcher;

    private long lastTouchMillis;
    private int numberOfTaps = 0;
    private String launchMessage;

    //URL will be dynamically obtained, initialization will just be for null safety
    private BaseUrl baseUrl = Enum.valueOf(BaseUrl.class, BuildConfig.UKE_INITIAL_WEB_LOGIN_ENUM_ID);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_welcome, container, false);
        view.findViewById(R.id.get_started_button).setOnClickListener(view1 -> loginPresenter.onGetStarted(getActivity(), baseUrl));
        if (VitalityActiveApplication.isDebugBuild()) {
            setupDebugOverrides(view);
        }
        view.setOnTouchListener(this);
        launchMessage = getActivity().getIntent().getStringExtra(EXTRA_FAILURE);

        String urlIdentifier = getActivity().getIntent().getStringExtra(URL_SWITCH);
        if (!TextUtils.isEmpty(urlIdentifier)) {
            baseUrl = Enum.valueOf(BaseUrl.class, urlIdentifier);
        }

        return view;
    }

    @Override
    protected void appear() {
        super.appear();
        View view = getView();
        if (launchMessage != null && view != null) {
            Snackbar.make(view, launchMessage, Snackbar.LENGTH_SHORT).show();
            launchMessage = null;
        }
    }

    private void setupDebugOverrides(View view) {
        view.findViewById(R.id.dev_uke_login).setVisibility(View.VISIBLE);
        view.findViewById(R.id.basic_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ukeNavigationCoordinator.navigateToBasicLogin(getActivity());
            }
        });
        view.findViewById(R.id.switch_url).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBaseUrlSwitch();
            }
        });
        view.findViewById(R.id.web_breakout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginPresenter.onGetStarted(getActivity(), baseUrl);
            }
        });
    }

    @Override
    protected void injectDependencies(DependencyInjector dependencyInjector) {
        dependencyInjector.inject(this);
    }

    @Override
    protected UKEGetStartedPresenter.UserInterface getUserInterface() {
        return this;
    }

    @Override
    protected UKEGetStartedPresenter getPresenter() {
        return loginPresenter;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (motionEvent.getPointerCount() != 2) {
            return true;
        }

        int action = (motionEvent.getAction() & MotionEvent.ACTION_MASK) % 5;
        long timeSinceLastTapDown = System.currentTimeMillis() - lastTouchMillis;

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                if (timeSinceLastTapDown > ViewConfiguration.getDoubleTapTimeout()) {
                    numberOfTaps = 0;
                }
                lastTouchMillis = System.currentTimeMillis();
                break;
            case MotionEvent.ACTION_UP:
                if (timeSinceLastTapDown > ViewConfiguration.getTapTimeout()) {
                    numberOfTaps = 0;
                } else {
                    if (++numberOfTaps == TAPS_TO_UNLOCK_ENVIRONMENT_SWITCHER) {
                        numberOfTaps = 0;
                        showBaseUrlSwitch();
                    }
                }
        }

        return true;
    }

    private void showBaseUrlSwitch() {

        //If switch is disabled do not show the dialog
        if (!BuildConfig.ENABLE_ENVIRONMENT_SWITCH)
            return;


        ArrayList<AlertDialogFragment.AlertDialogItem> list = new ArrayList<>();
        if (VitalityActiveApplication.isDebugBuild()) {
            list.add(BaseUrl.DEV.buildAlertDialogItem());
        }
        list.add(BaseUrl.TEST.buildAlertDialogItem());
        list.add(BaseUrl.QA.buildAlertDialogItem());
        list.add(BaseUrl.QA_FF.buildAlertDialogItem());
        list.add(BaseUrl.PROD.buildAlertDialogItem());
        list.add(new AlertDialogFragment.AlertDialogItem("Bypass UKE Web Login", 0, -2, false));
        list.add(new AlertDialogFragment.AlertDialogItem("Cancel", 0, -1, false));
        AlertDialogFragment.create(ALERT_DIALOG_TAG, "Switch environment", list)
                .setItemSelectedListener(this)
                .show(getFragmentManager(), ALERT_DIALOG_TAG);
    }

    @Override
    public void onItemSelected(AlertDialogFragment.AlertDialogItem selected) {
        switch (selected.getIdentifier()) {
            case -1:
                return;
            case -2:
                ukeNavigationCoordinator.navigateToBasicLogin(getActivity());
                return;
        }

        baseUrl = BaseUrl.values()[selected.getIdentifier()];
        switch (baseUrl) {
            case DEV:
                baseURLSwitcher.switchToDevUrl();
                break;
            case TEST:
                baseURLSwitcher.switchToTestUrl();
                break;
            case QA:
                baseURLSwitcher.switchToQaUrl();
                break;
            case QA_FF:
                baseURLSwitcher.switchToQaFfUrl();
                break;
            case PROD:
                baseURLSwitcher.switchToProdUrl();
                break;
        }

        getVitalityActiveApplication().reset();
        getDependencyInjector().inject(this);
        ukeNavigationCoordinator.showLoginGetStarted(getActivity(), "Switched environment to " + selected.getTitle(), baseUrl);
    }
}
