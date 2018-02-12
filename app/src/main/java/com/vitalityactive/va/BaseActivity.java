package com.vitalityactive.va;

import android.app.Activity;
import android.content.Context;
import android.databinding.ViewDataBinding;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.SpinnerAdapter;

import com.vitalityactive.va.dependencyinjection.DependencyInjector;
import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.events.EventListener;
import com.vitalityactive.va.pushnotification.ActivitySwitchScreenEvent;
import com.vitalityactive.va.pushnotification.InAppMessageEvent;
import com.vitalityactive.va.pushnotification.InAppPreferences;
import com.vitalityactive.va.pushnotification.InAppScreenMessageEvent;
import com.vitalityactive.va.pushnotification.InAppType;
import com.vitalityactive.va.pushnotification.ShowScreenUtility;
import com.vitalityactive.va.uicomponents.ButtonBarConfigurator;

import java.util.Set;

import javax.inject.Inject;

public class
BaseActivity extends AppCompatActivity {
    @Inject
    protected NavigationCoordinator navigationCoordinator;
    @Inject
    EventDispatcher eventDispatcher;
    @Inject
    InAppPreferences inAppPreferences;

    private InappMessageSnackbarOnClickListener inappMessageSnackbarOnClickListener;
    protected ViewDataBinding binding;

    private boolean resumedNow;

    private EventListener<ActivitySwitchScreenEvent> activitySwitchScreenEventListener = new EventListener<ActivitySwitchScreenEvent>() {
        @Override
        public void onEvent(ActivitySwitchScreenEvent event) {
            switchScreen();
        }
    };

    private EventListener<InAppScreenMessageEvent> inAppMessageScreenEventListener = new EventListener<InAppScreenMessageEvent>() {

        @Override
        public void onEvent(InAppScreenMessageEvent event) {
            showInAppScreenMessages(event.inAppType);
        }
    };

    private EventListener<InAppMessageEvent> inAppMessageEventListener = new EventListener<InAppMessageEvent>() {
        @Override
        public void onEvent(InAppMessageEvent event) {
            showInAppMessage();
        }
    };

    protected void switchScreen() {

    }

    protected void showInAppScreenMessages(InAppType inAppType) {

    }

    protected void showInAppMessage() {
        Log.d("Kerry Pushwoosh", "BaseActivity.showInAppMessage");
        Set<String> messages = inAppPreferences.getInAppMessages();
        for (String message: messages) {
            showSnackbar(message);
        }
        inAppPreferences.resetInAppMessage();
    }

    protected void showSnackbar(String message) {
        if (binding != null) {
            Log.d("Kerry Pushwoosh", "showSnackbar + " + binding.getRoot().toString());
            Snackbar snackbar = Snackbar.make(binding.getRoot(), message, Snackbar.LENGTH_LONG);
            snackbar.setAction("View", inappMessageSnackbarOnClickListener);
            snackbar.show();
        }
    }

    private class InappMessageSnackbarOnClickListener implements View.OnClickListener {
        Activity activity;

        public InappMessageSnackbarOnClickListener(Activity activity) {
            this.activity = activity;
        }

        @Override
        public void onClick(View v) {
            navigationCoordinator.navigateToScreen(activity);
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getDependencyInjector().inject(this);

        if (inappMessageSnackbarOnClickListener==null) {
            inappMessageSnackbarOnClickListener = new InappMessageSnackbarOnClickListener(this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        resumedNow = true;
        eventDispatcher.addEventListener(ActivitySwitchScreenEvent.class, activitySwitchScreenEventListener);
        eventDispatcher.addEventListener(InAppMessageEvent.class, inAppMessageEventListener);
        eventDispatcher.addEventListener(InAppScreenMessageEvent.class, inAppMessageScreenEventListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        resumedNow = false;
        eventDispatcher.removeEventListener(ActivitySwitchScreenEvent.class, activitySwitchScreenEventListener);
        eventDispatcher.removeEventListener(InAppMessageEvent.class, inAppMessageEventListener);
        eventDispatcher.removeEventListener(InAppScreenMessageEvent.class, inAppMessageScreenEventListener);
    }

    public final boolean isResumedNow() {
        return resumedNow;
    }

    protected final DependencyInjector getDependencyInjector() {
        return getVitalityActiveApplication().getDependencyInjector();
    }

    protected VitalityActiveApplication getVitalityActiveApplication() {
        return (VitalityActiveApplication) getApplication();
    }

    @NonNull
    protected final ActionBar setUpActionBar() {
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        return getSupportActionBar() == null ? new NullActionBar(this.getClass().getSimpleName()) : getSupportActionBar();
    }

    @NonNull
    protected final ActionBar setUpActionBarWithTitle(int titleResourceId) {
        return setUpActionBarTitle(titleResourceId, setUpActionBar());
    }

    @NonNull
    protected final ActionBar setUpActionBarWithTitle(String titleText) {
        return setUpActionBarTitle(titleText, setUpActionBar());
    }

    protected final void setActionBarTitle(CharSequence titleText) {
        if (getSupportActionBar() != null) {
            setUpActionBarTitle(titleText, getSupportActionBar());
        }
    }

    @NonNull
    private ActionBar setUpActionBarTitle(CharSequence titleText, @NonNull ActionBar supportActionBar) {
        supportActionBar.setTitle(titleText);
        return supportActionBar;
    }

    @NonNull
    private ActionBar setUpActionBarTitle(int titleResourceId, @NonNull ActionBar supportActionBar) {
        supportActionBar.setTitle(titleResourceId);
        return supportActionBar;
    }

    protected void setCloseAsActionBarIcon() {
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.close);
        }
    }

    protected ButtonBarConfigurator setupButtonBar() {
        return setupButtonBar(R.id.button_bar);
    }

    protected ButtonBarConfigurator setupButtonBar(int id) {
        return ButtonBarConfigurator.load(findViewById(id));
    }

    protected ButtonBarConfigurator setupButtonBar(int id, int forwardButtonId, int backButtonId) {
        return ButtonBarConfigurator.load(findViewById(id), forwardButtonId, backButtonId);
    }

    protected void hideButtonBar() {
        View buttonBar = findViewById(R.id.agree_button_bar);

        if (buttonBar != null)
            buttonBar.setVisibility(View.GONE);
    }

    protected void showButtonBar() {
        View buttonBar = findViewById(R.id.agree_button_bar);

        if (buttonBar != null)
            buttonBar.setVisibility(View.VISIBLE);
    }

    public void handleButtonBarNextTapped(View view) {
        // no-op
    }

    protected void hideKeyboardAndFocusOnView(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        view.requestFocus();
    }

    public void setActionBarTitleAndDisplayHomeAsUp(String title) {
        setUpActionBarWithTitle(title)
                .setDisplayHomeAsUpEnabled(true);
    }

    @NonNull
    protected String getUnformattedString(@StringRes int resId) {
        return getString(resId, (Object) null);
    }

    protected void setActionBarColor(int globalTintColor) {
        findViewById(R.id.toolbar).setBackgroundColor(globalTintColor);
    }

    protected void setStatusBarColor(int globalTintColor) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(globalTintColor);
        }
    }

    protected void updateActionBarIcon(int icon){
        getSupportActionBar().setHomeAsUpIndicator(icon);
    }

    @SuppressWarnings("deprecation")
    private static class NullActionBar extends ActionBar {
        private final String className;

        private NullActionBar(String className) {
            this.className = className;
        }

        @Override
        public void setCustomView(View view) {
            logNoActionBarMessage();
        }

        private int logNoActionBarMessage() {
            return Log.e(className, "NO ACTIONBAR FOUND");
        }

        @Override
        public void setCustomView(View view, LayoutParams layoutParams) {
            logNoActionBarMessage();
        }

        @Override
        public void setCustomView(int resId) {
            logNoActionBarMessage();
        }

        @Override
        public void setIcon(@DrawableRes int resId) {
            logNoActionBarMessage();
        }

        @Override
        public void setIcon(Drawable icon) {
            logNoActionBarMessage();
        }

        @Override
        public void setLogo(@DrawableRes int resId) {
            logNoActionBarMessage();
        }

        @Override
        public void setLogo(Drawable logo) {
            logNoActionBarMessage();
        }

        @Override
        public void setListNavigationCallbacks(SpinnerAdapter adapter, OnNavigationListener callback) {
            logNoActionBarMessage();
        }

        @Override
        public void setSelectedNavigationItem(int position) {
            logNoActionBarMessage();
        }

        @Override
        public int getSelectedNavigationIndex() {
            logNoActionBarMessage();
            return 0;
        }

        @Override
        public int getNavigationItemCount() {
            logNoActionBarMessage();
            return 0;
        }

        @Override
        public void setTitle(CharSequence title) {
            logNoActionBarMessage();
        }

        @Override
        public void setTitle(@StringRes int resId) {
            logNoActionBarMessage();
        }

        @Override
        public void setSubtitle(CharSequence subtitle) {
            logNoActionBarMessage();
        }

        @Override
        public void setSubtitle(int resId) {
            logNoActionBarMessage();
        }

        @Override
        public void setDisplayOptions(int options) {
            logNoActionBarMessage();
        }

        @Override
        public void setDisplayOptions(int options, int mask) {
            logNoActionBarMessage();
        }

        @Override
        public void setDisplayUseLogoEnabled(boolean useLogo) {
            logNoActionBarMessage();
        }

        @Override
        public void setDisplayShowHomeEnabled(boolean showHome) {
            logNoActionBarMessage();
        }

        @Override
        public void setDisplayHomeAsUpEnabled(boolean showHomeAsUp) {
            logNoActionBarMessage();
        }

        @Override
        public void setDisplayShowTitleEnabled(boolean showTitle) {
            logNoActionBarMessage();
        }

        @Override
        public void setDisplayShowCustomEnabled(boolean showCustom) {
            logNoActionBarMessage();
        }

        @Override
        public void setBackgroundDrawable(@Nullable Drawable d) {
            logNoActionBarMessage();
        }

        @Override
        public View getCustomView() {
            logNoActionBarMessage();
            return null;
        }

        @Nullable
        @Override
        public CharSequence getTitle() {
            logNoActionBarMessage();
            return null;
        }

        @Nullable
        @Override
        public CharSequence getSubtitle() {
            logNoActionBarMessage();
            return null;
        }

        @Override
        public int getNavigationMode() {
            logNoActionBarMessage();
            return ActionBar.NAVIGATION_MODE_STANDARD;
        }

        @Override
        public void setNavigationMode(int mode) {
            logNoActionBarMessage();
        }

        @Override
        public int getDisplayOptions() {
            logNoActionBarMessage();
            return 0;
        }

        @Override
        public Tab newTab() {
            logNoActionBarMessage();
            return null;
        }

        @Override
        public void addTab(Tab tab) {
            logNoActionBarMessage();
        }

        @Override
        public void addTab(Tab tab, boolean setSelected) {
            logNoActionBarMessage();
        }

        @Override
        public void addTab(Tab tab, int position) {
            logNoActionBarMessage();
        }

        @Override
        public void addTab(Tab tab, int position, boolean setSelected) {
            logNoActionBarMessage();
        }

        @Override
        public void removeTab(Tab tab) {
            logNoActionBarMessage();
        }

        @Override
        public void removeTabAt(int position) {
            logNoActionBarMessage();
        }

        @Override
        public void removeAllTabs() {
            logNoActionBarMessage();
        }

        @Override
        public void selectTab(Tab tab) {
            logNoActionBarMessage();
        }

        @Nullable
        @Override
        public Tab getSelectedTab() {
            logNoActionBarMessage();
            return null;
        }

        @Override
        public Tab getTabAt(int index) {
            logNoActionBarMessage();
            return null;
        }

        @Override
        public int getTabCount() {
            logNoActionBarMessage();
            return 0;
        }

        @Override
        public int getHeight() {
            logNoActionBarMessage();
            return 0;
        }

        @Override
        public void show() {
            logNoActionBarMessage();
        }

        @Override
        public void hide() {
            logNoActionBarMessage();
        }

        @Override
        public boolean isShowing() {
            logNoActionBarMessage();
            return false;
        }

        @Override
        public void addOnMenuVisibilityListener(OnMenuVisibilityListener listener) {
            logNoActionBarMessage();
        }

        @Override
        public void removeOnMenuVisibilityListener(OnMenuVisibilityListener listener) {
            logNoActionBarMessage();
        }
    }
}
