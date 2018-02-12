package com.vitalityactive.va.profile;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.vitalityactive.va.BasePresentedFragment;
import com.vitalityactive.va.BuildConfig;
import com.vitalityactive.va.R;
import com.vitalityactive.va.dependencyinjection.DependencyInjector;
import com.vitalityactive.va.utilities.ImageLoader;

import javax.inject.Inject;

/**
 * Created by peter.ian.t.betos on 15/01/2018.
 */

public abstract class BaseProfileFragment extends BasePresentedFragment<ProfilePresenter.UI, ProfilePresenter>
        implements ProfilePresenter.UI ,View.OnClickListener {


    @Inject
    ProfilePresenter presenter;

    private ImageView profileImageView;
    private TextView profileInitialsView;
    private TextView profileNameView;
    private View membershipItemGridView;
    private View profileSubItemContainer;
    private ImageView membershipPassIcon;
    private ImageView eventsFeedIcon;
    private Button profileButton;

    private String globalTintColor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void activityCreated(@Nullable Bundle savedInstanceState) {
        globalTintColor = getArguments().getString(GLOBAL_TINT_COLOR);

        setUpToolbar();

        View parentView = getView();
        if (parentView == null) {
            return;
        }

        setUpProfileView(parentView);

        setUIColor(globalTintColor);

    }


    public void setButtonColor(View view, @ColorInt int color) {

    }

    private void setUpToolbar() {
        setToolbarDrawerIconColourToWhite();
    }

    private void setUpProfileView(@NonNull View parentView) {
        profileImageView = parentView.findViewById(R.id.profile_image);
        profileInitialsView = parentView.findViewById(R.id.profile_image_initials);
        profileNameView = parentView.findViewById(R.id.profile_name);
        membershipItemGridView= parentView.findViewById(R.id.membership_pass_listitem);

        profileSubItemContainer = parentView.findViewById(R.id.profile_sub_item_container);

        membershipPassIcon = parentView.findViewById(R.id.membership_pass_icon);
        eventsFeedIcon = parentView.findViewById(R.id.events_feed_icon);

        profileButton = parentView.findViewById(R.id.personal_details_button);

        profileButton.setOnClickListener(this);
        membershipItemGridView.setOnClickListener(this);

        parentView.findViewById(R.id.events_feed).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigationCoordinator.navigateProfileEventsFeedTapped(getActivity());
            }
        });

        parentView.findViewById(R.id.events_feed).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigationCoordinator.navigateProfileEventsFeedTapped(getActivity());
            }
        });

        marketUiUpdate(parentView);

    }

    @Override
    protected void injectDependencies(DependencyInjector dependencyInjector) {
        dependencyInjector.inject(this);
    }

    @Override
    protected ProfilePresenter.UI getUserInterface() {
        return this;
    }

    @Override
    protected ProfilePresenter getPresenter() {
        return presenter;
    }

    @Override
    public void showProfileName(String givenName, String familyName) {
        profileNameView.setText(getString(R.string.navigation_drawer_header_title, givenName, familyName));
    }

    @Override
    public void showProfileImage(String profileImagePath) {
        ImageLoader.loadImageFromUriRoundedAndCenterCrop(Uri.parse("file:" + profileImagePath), profileImageView);
        profileImageView.setVisibility(View.VISIBLE);
        profileInitialsView.setVisibility(View.GONE);
    }

    @Override
    public void showProfileInitials(String userInitials) {
        profileImageView.setVisibility(View.GONE);
        profileInitialsView.setVisibility(View.VISIBLE);
        profileInitialsView.setText(userInitials);
    }

    @Override
    protected void setUIColor(String globalTintColor) {
        membershipPassIcon.setColorFilter(Color.parseColor(globalTintColor));
        eventsFeedIcon.setColorFilter(Color.parseColor(globalTintColor));
        ViewCompat.setBackgroundTintList(profileButton, ColorStateList.valueOf(Color.parseColor(globalTintColor)));
    }

    protected abstract void marketUiUpdate(View parentView);

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.membership_pass_listitem:
                navigationCoordinator.navigateAfterMembershipPassItemTapped(getActivity());
                break;
            case R.id.personal_details_button:
                navigationCoordinator.navigateAfterPersonalDetailsButtonTapped(getActivity());
                break;
        }
    }
}