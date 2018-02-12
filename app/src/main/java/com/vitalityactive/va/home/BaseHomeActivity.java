package com.vitalityactive.va.home;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.vitalityactive.va.BaseActivity;
import com.vitalityactive.va.BuildConfig;
import com.vitalityactive.va.DeviceSpecificPreferences;
import com.vitalityactive.va.PartyInformationRepository;
import com.vitalityactive.va.R;
import com.vitalityactive.va.VitalityActiveApplication;
import com.vitalityactive.va.appconfig.AppConfigRepository;
import com.vitalityactive.va.databinding.ActivityHomeBinding;
import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.events.EventListener;
import com.vitalityactive.va.help.HelpFragment;
import com.vitalityactive.va.myhealth.events.MyHealthOnboardingEvent;
import com.vitalityactive.va.myhealth.landing.MyHealthLandingFragment;
import com.vitalityactive.va.pointsmonitor.PointsMonitorFragment;
import com.vitalityactive.va.profile.ProfileFragmentDev;
import com.vitalityactive.va.profile.ProfileImageAvailableEvent;
import com.vitalityactive.va.profile.ProfileImageProvider;
import com.vitalityactive.va.pushnotification.InAppPreferences;
import com.vitalityactive.va.pushnotification.InAppType;
import com.vitalityactive.va.pushnotification.MenuBadgeEvent;
import com.vitalityactive.va.pushnotification.PushNotificationData;
import com.vitalityactive.va.pushnotification.ShowScreenUtility;
import com.vitalityactive.va.settings.SettingsFragment;
import com.vitalityactive.va.utilities.ImageLoader;

import java.util.Set;

import javax.inject.Inject;

import static com.vitalityactive.va.pushnotification.InAppType.*;

public class BaseHomeActivity extends BaseActivity {

    public static final String SCREEN_TITLE = "SCREEN_TITLE";
    public static final String GLOBAL_TINT_COLOR = "GLOBAL_TINT_COLOR";
    private static final int NOT_SET = -1;
    private static final String NEXT_SCREEN = "NEXT_SCREEN";

    @Inject
    AppConfigRepository appConfigRepository;
    @Inject
    PartyInformationRepository partyInformationRepository;
    @Inject
    EventDispatcher eventDispatcher;
    @Inject
    ProfileImageProvider profileImageProvider;
    @Inject
    DeviceSpecificPreferences deviceSpecificPreferences;
    @Inject
    InAppPreferences inAppPreferences;

    private DrawerLayout navigationDrawer;
    private ActionBarDrawerToggle drawerToggle;
    private int nextScreen = NOT_SET;
    private CharSequence screenTitle;
    private int globalTintColour, globalTintDarker;

    private EventListener<ProfileImageAvailableEvent> profileImageAvailableEventListener = new EventListener<ProfileImageAvailableEvent>() {
        @Override
        public void onEvent(ProfileImageAvailableEvent event) {
            updateProfileImage(BaseHomeActivity.this.navigationView.getHeaderView(0));
        }
    };

    private EventListener<MyHealthOnboardingEvent> myHealthOnboardingEventListener = new EventListener<MyHealthOnboardingEvent>() {
        @Override
        public void onEvent(MyHealthOnboardingEvent event) {
            navigateToMyHealth();
        }
    };
    protected NavigationView navigationView;

    private TextView homePointsBadgeTextView;
    private TextView menuPointsBadgeTextView;
    private TextView myHealthPointsBadgeTextView;
    private TextView profilePointsBadgeTextView;
    private int checkedItemID;
    private int previousCheckedItemID;

    private EventListener<MenuBadgeEvent> badgeEventEventListener = new EventListener<MenuBadgeEvent>() {

        @Override
        public void onEvent(MenuBadgeEvent event) {
            updateBadgeCount(event.inAppType);
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home);

        getDependencyInjector().inject(this);

        globalTintColour = Color.parseColor(appConfigRepository.getGlobalTintColorHex());
        globalTintDarker = Color.parseColor(appConfigRepository.getGlobalTintDarkerColorHex());

        setUpActionBarWithTitle("");

        setUpNavigationDrawer((DrawerLayout) findViewById(R.id.activity_home),
                (NavigationView) findViewById(R.id.navigation_view));

        if (savedInstanceState == null) {
            switch (ShowScreenUtility.screen) {
                case PushNotificationData.SCREEN_MENU_POINTS:
                    //navigateToPointsMonitor();
                    replaceContent(new PointsMonitorFragment());
                    selectDrawerItem(navigationView.getMenu().getItem(1));
                    break;
                case PushNotificationData.SCREEN_MENU_MY_HEALTH:
                    //navigateToMyHealth();
                    replaceContent(new MyHealthLandingFragment());
                    selectDrawerItem(navigationView.getMenu().getItem(2));
                    setActionBarTitle(getString(R.string.menu_myhealth_button_7));
                    break;
                default:
                    replaceContent(new HomeFragment());
                    setActionBarTitle("");
            }
        } else {
            nextScreen = savedInstanceState.getInt(NEXT_SCREEN);
            screenTitle = savedInstanceState.getCharSequence(SCREEN_TITLE);
        }

        setStatusBarColor(globalTintDarker);

        eventDispatcher.addEventListener(ProfileImageAvailableEvent.class, profileImageAvailableEventListener);
        eventDispatcher.addEventListener(MenuBadgeEvent.class, badgeEventEventListener);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    protected void onResume() {
        super.onResume();
        eventDispatcher.addEventListener(MyHealthOnboardingEvent.class, myHealthOnboardingEventListener);
        if (checkedItemID == R.id.navigation_item_profile) {
            checkedItemID = previousCheckedItemID;
        }
        updateBadgeCount(PROFILE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        eventDispatcher.removeEventListener(ProfileImageAvailableEvent.class, profileImageAvailableEventListener);
        eventDispatcher.removeEventListener(MenuBadgeEvent.class, badgeEventEventListener);
    }

    @Override
    protected void onPause() {
        super.onPause();

        eventDispatcher.removeEventListener(MyHealthOnboardingEvent.class, myHealthOnboardingEventListener);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(NEXT_SCREEN, nextScreen);
        outState.putCharSequence(SCREEN_TITLE, screenTitle);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return drawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    @Override
    protected void switchScreen() {
        switch (ShowScreenUtility.screen) {
            case PushNotificationData.SCREEN_MENU_POINTS:
                selectDrawerItem(navigationView.getMenu().getItem(1));
                break;
            case PushNotificationData.SCREEN_MENU_MY_HEALTH:
                selectDrawerItem(navigationView.getMenu().getItem(2));
                break;
            case PushNotificationData.SCREEN_MENU_PROFILE:
                selectDrawerItem(navigationView.getMenu().getItem(3));
            default:
                selectDrawerItem(navigationView.getMenu().getItem(0));
        }
        switchScreens();
    }

    private void replaceContent(Fragment fragment) {
        Bundle bundle = new Bundle();
        bundle.putString(GLOBAL_TINT_COLOR, appConfigRepository.getGlobalTintColorHex());
        fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.content, fragment).commit();

    }

    private void setUpNavigationDrawer(DrawerLayout navigationDrawer, NavigationView navigationView) {
        this.navigationDrawer = navigationDrawer;
        this.navigationView = navigationView;

        setUpNavigationDrawerHeader(navigationView);

        setNavDrawerColor(navigationView, globalTintColour);

        setNavigationItemSelectedListener(navigationView);
        navigationView.getMenu().setGroupVisible(R.id.dev_menu, VitalityActiveApplication.isDebugBuild());

        drawerToggle = getActionBarDrawerToggle(navigationDrawer);

        navigationDrawer.addDrawerListener(drawerToggle);

        checkedItemID = R.id.navigation_item_home;
        previousCheckedItemID = 0;
        menuPointsBadgeTextView = (TextView) navigationView.getMenu().findItem(R.id.navigation_item_points_monitor).getActionView().findViewById(R.id.menu_badge);
        homePointsBadgeTextView = (TextView) navigationView.getMenu().findItem(R.id.navigation_item_home).getActionView().findViewById(R.id.menu_badge);
        myHealthPointsBadgeTextView = (TextView) navigationView.getMenu().findItem(R.id.navigation_item_my_health).getActionView().findViewById(R.id.menu_badge);
        profilePointsBadgeTextView = (TextView) navigationView.getMenu().findItem(R.id.navigation_item_profile).getActionView().findViewById(R.id.menu_badge);
        updateBadgeCount(HOME);
        updateBadgeCount(MY_HEALTH);
        updateBadgeCount(POINTS);
        updateBadgeCount(PROFILE);
    }

    private void updateBadgeCount(final InAppType menuBadgeType) {
        Log.d("Kerry Pushwoosh", "updateBadgeCount...");

        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                int finalCount = 0;
                switch (menuBadgeType) {
                    case HOME:
                        if (checkedItemID != R.id.navigation_item_home) {
                            finalCount = inAppPreferences.currentMenuHomeBadgePreferences();
                            homePointsBadgeTextView.setText(String.valueOf(finalCount));
                            if (finalCount>0) {
                                homePointsBadgeTextView.setVisibility(View.VISIBLE);
                            } else {
                                homePointsBadgeTextView.setVisibility(View.INVISIBLE);
                            }
                        }
                        break;
                    case POINTS:
                        if (checkedItemID != R.id.navigation_item_points_monitor) {
                            finalCount = inAppPreferences.currentMenuPointsBadgePreferences();
                            menuPointsBadgeTextView.setText(String.valueOf(finalCount));
                            if (finalCount>0) {
                                menuPointsBadgeTextView.setVisibility(View.VISIBLE);
                            } else {
                                menuPointsBadgeTextView.setVisibility(View.INVISIBLE);
                            }
                        }
                        break;
                    case MY_HEALTH:
                        if (checkedItemID != R.id.navigation_item_my_health) {
                            finalCount = inAppPreferences.currentMenuMyHealthBadgePreferences();
                            myHealthPointsBadgeTextView.setText(String.valueOf(finalCount));
                            if (finalCount>0) {
                                myHealthPointsBadgeTextView.setVisibility(View.VISIBLE);
                            } else {
                                myHealthPointsBadgeTextView.setVisibility(View.INVISIBLE);
                            }
                        }
                        break;
                    case PROFILE:
                        if (checkedItemID != R.id.navigation_item_profile) {
                            finalCount = inAppPreferences.currentMenuProfileBadgePreferences();
                            profilePointsBadgeTextView.setText(String.valueOf(finalCount));
                            if (finalCount>0) {
                                profilePointsBadgeTextView.setVisibility(View.VISIBLE);
                            } else {
                                profilePointsBadgeTextView.setVisibility(View.INVISIBLE);
                            }
                        }
                        break;
                }
            }
        });

    }

    private void resetBadgeCount(InAppType menuBadgeType) {
        switch (menuBadgeType) {
            case HOME:
                inAppPreferences.resetMenuHomeBadge();
                break;
            case POINTS:
                inAppPreferences.resetMenuPointsBadge();
                break;
            case MY_HEALTH:
                inAppPreferences.resetMenuMyHealthBadge();
                break;
            case PROFILE:
                inAppPreferences.resetMenuProfileBadge();
                break;

        }
        updateBadgeCount(menuBadgeType);
    }

    private void setNavigationItemSelectedListener(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });
    }

    @NonNull
    private ActionBarDrawerToggle getActionBarDrawerToggle(final DrawerLayout navigationDrawer) {
        return new ActionBarDrawerToggle(this,
                navigationDrawer,
                (Toolbar) findViewById(R.id.toolbar),
                R.string.drawer_open_content_description,
                R.string.drawer_close_content_description) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                switchScreens();
            }
        };
    }

    private void setUpNavigationDrawerHeader(NavigationView navigationView) {
        View headerView = navigationView.getHeaderView(0);

        if (headerView == null) {
            return;
        }

        setUpUserProfileImage(headerView);
        setUpUserProfileName(headerView);

        headerView.setBackgroundColor(globalTintColour);
    }

    private void setUpUserProfileName(View headerView) {
        TextView headerTitle = headerView.findViewById(R.id.header_title);
        headerTitle.setText(getString(R.string.navigation_drawer_header_title, partyInformationRepository.getUserGivenName(), partyInformationRepository.getUserFamilyName()));
    }

    private void setUpUserProfileImage(@NonNull View headerView) {
        updateProfileImage(headerView);

        headerView.findViewById(R.id.profile_image_container).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToProfile();
                navigationView.setCheckedItem(R.id.navigation_item_profile);
                navigationDrawer.closeDrawers();
            }
        });
    }

    private void updateProfileImage(@NonNull View headerView) {
        ImageView profileImageView = headerView.findViewById(R.id.profile_image);
        TextView initials = headerView.findViewById(R.id.profile_image_initials);
        if (profileImageProvider.isProfileImageAvailable()) {
            ImageLoader.loadImageFromUriRoundedAndCenterCrop(Uri.parse("file:" + profileImageProvider.getProfileImagePath()), profileImageView);
            profileImageView.setVisibility(View.VISIBLE);
            initials.setVisibility(View.GONE);
        } else {
            profileImageProvider.fetchProfileImage();
            profileImageView.setVisibility(View.GONE);
            initials.setVisibility(View.VISIBLE);
            initials.setText(partyInformationRepository.getUserInitials());
        }
    }

    private void setNavDrawerColor(NavigationView navigationView, int globalTintColor) {
        ColorStateList defaultItemTextColors = navigationView.getItemTextColor();
        if (defaultItemTextColors != null) {
            int defaultColor = defaultItemTextColors.getDefaultColor();
            ColorStateList itemColorStateList = getGlobalTintColorStateList(globalTintColor, defaultColor);

            navigationView.setItemTextColor(itemColorStateList);
            navigationView.setItemIconTintList(itemColorStateList);
        }
    }

    @NonNull
    private ColorStateList getGlobalTintColorStateList(int globalTintColor, int defaultColor) {
        return new ColorStateList(new int[][]{new int[]{android.R.attr.state_checked},
                new int[]{android.R.attr.state_enabled},
                new int[]{android.R.attr.state_pressed},
                new int[]{android.R.attr.state_focused},
                new int[]{android.R.attr.state_pressed}},
                new int[]{globalTintColor,
                        defaultColor,
                        defaultColor,
                        defaultColor,
                        defaultColor});
    }

    private void switchScreens() {
        replaceContent();
    }

    public void selectDrawerItem(MenuItem menuItem) {
        Log.d("Kerry Pushwoosh", "replaceContent here...");
        switch (menuItem.getItemId()) {
            case R.id.navigation_item_home:
                resetBadgeCount(HOME);
                break;
            case R.id.navigation_item_points_monitor:
                resetBadgeCount(POINTS);
                break;
            case R.id.navigation_item_my_health:
                resetBadgeCount(MY_HEALTH);
                break;
            case R.id.navigation_item_profile:
                resetBadgeCount(PROFILE);
                // need to save the current selected menu since the navigationView will be place in the back st
                previousCheckedItemID = checkedItemID;
                break;
        }
        checkedItemID = menuItem.getItemId();
        navigationDrawer.closeDrawers();
        if (menuItem.isChecked()) {
            return;
        }
        nextScreen = menuItem.getItemId();
        screenTitle = menuItem.getTitle();
        menuItem.setChecked(true);
    }

    private void replaceContent() {
        switch (nextScreen) {
            case R.id.navigation_item_points_monitor:
                navigateToPointsMonitor();
                break;
            case R.id.navigation_item_home:
                replaceContent(HomeFragment.class);
                setActionBarTitle("");
                break;
            case R.id.navigation_item_profile:
                navigateToProfile();
                break;
            case R.id.navigation_item_my_health:
                navigateToMyHealth();
                break;
            case R.id.navigation_item_settings:
//                replaceContent(SettingsFragment.class);
//                setActionBarTitle(getString(R.string.settings_button_271));
                navigateToSettings();
                break;
            case R.id.navigation_item_send_feedback:
                navigateFeedback();
                break;
            case R.id.navigation_item_rate_us:
                navigateToGooglePlay();
                break;
            case R.id.navigation_item_help:
                replaceContent(HelpFragment.class);
                setActionBarTitle(getString(R.string.help_button_141));
                break;
            case R.id.navigation_item_dev:
                if (VitalityActiveApplication.isDebugBuild()) {
                    replaceContent(ProfileFragmentDev.class);
                    setActionBarTitle("dev settings screen");
                    break;
                }
        }
        if (nextScreen==R.id.navigation_item_points_monitor || nextScreen==R.id.navigation_item_home || nextScreen==R.id.navigation_item_my_health) {
            showSpecificInAppScreenMessage();
        }

        nextScreen = NOT_SET;
    }

    private void showSpecificInAppScreenMessage() {
        Log.d("Kerry Pushwoosh", "showSpecificInAppScreenMessage()");
        switch (checkedItemID) {
            case R.id.navigation_item_home:
                showInAppScreenMessages(HOME);
                break;
            case R.id.navigation_item_points_monitor:
                showInAppScreenMessages(POINTS);
                break;
            case R.id.navigation_item_my_health:
                showInAppScreenMessages(MY_HEALTH);
                break;
        }
    }

    @Override
    protected void showInAppScreenMessages(InAppType inAppType) {
        Set<String> messages = null;
        switch (inAppType) {
            case HOME:
                if (checkedItemID == R.id.navigation_item_home) {
                    messages = inAppPreferences.getHomeMessages();
                    inAppPreferences.resetHomeMessage();
                    resetBadgeCount(HOME);
                }
                break;
            case POINTS:
                if (checkedItemID == R.id.navigation_item_points_monitor) {
                    messages = inAppPreferences.getPointsMessages();
                    inAppPreferences.resetPointsMessage();
                    resetBadgeCount(POINTS);
                }
                break;
            case MY_HEALTH:
                if (checkedItemID == R.id.navigation_item_my_health) {
                    messages = inAppPreferences.getMyHealthMessages();
                    inAppPreferences.resetMyHealthMessage();
                    resetBadgeCount(MY_HEALTH);
                }
                break;
        }

        if (messages != null) {
            for (String message: messages) {
                showSnackbar(message);
            }
        }
    }

    private void navigateToPointsMonitor() {
        replaceContent(PointsMonitorFragment.class);
    }

    private void navigateToSettings() {
        navigationView.getMenu().findItem(R.id.navigation_item_settings).setChecked(false);
        navigationCoordinator.navigateToSetings(this);
    }

    private void navigateToProfile() {
        navigationView.getMenu().findItem(R.id.navigation_item_profile).setChecked(false);
        navigationCoordinator.navigateToProfile(this);
    }

    private void navigateToMyHealth() {
        if (!deviceSpecificPreferences.hasCurrentUserHasSeenMyHealthOnboarding()) {
            navigationCoordinator.navigateToMyHealthOnboarding(this);
            deviceSpecificPreferences.setCurrentUserHasSeenMyHealthOnboarding();
        } else {
            showMyHealthLandingScreen();
        }
        setActionBarTitle(getString(R.string.menu_myhealth_button_7));
    }

    private void showMyHealthLandingScreen() {
        replaceContent(MyHealthLandingFragment.class);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RequestCode.MYHEALTH_ONBOARDING && resultCode == RESULT_OK) {
            showMyHealthLandingScreen();
        } else if (requestCode == RequestCode.FEEDBACK){
//            navigationView.getMenu().findItem(R.id.navigation_item_settings).setChecked(true);
//            nextScreen = R.id.navigation_item_settings;
//            replaceContent();
            navigateToSettings();
        }
    }

    @NonNull
    private Class<?> getSettingsFragmentClass() {
        //  return VitalityActiveApplication.isDevDebugBuild() ? ProfileFragmentDev.class : SettingsFragment.class;
        return SettingsFragment.class;
    }

    private void replaceContent(Class fragmentClass) {
        try {
            replaceContent((Fragment) fragmentClass.newInstance());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void handleHomeStatusTapped(View view) {
        navigationCoordinator.navigateAfterVitalityStatusTapped(this);
    }

    public class RequestCode {
        public static final int MYHEALTH_ONBOARDING = 001;
        public static final int FEEDBACK = 002;
    }

    public void navigateFeedback(){
        navigationView.getMenu().findItem(R.id.navigation_item_send_feedback).setChecked(false);
        navigationCoordinator.navigateToFeedback(this, RequestCode.FEEDBACK);
    }

    public void navigateToGooglePlay(){

        String packageName = VitalityActiveApplication.isDevDebugBuild() ? BuildConfig.MOCK_APP_ID : getApplicationContext().getPackageName();

        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + packageName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + packageName)));
        }
        selectDrawerItem(navigationView.getMenu().getItem(0));
        switchScreens();
    }
}
