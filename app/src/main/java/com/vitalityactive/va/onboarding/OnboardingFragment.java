package com.vitalityactive.va.onboarding;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vitalityactive.va.R;
import com.vitalityactive.va.uicomponents.OnboardingContentConfigurator;
import com.vitalityactive.va.utilities.TextUtilities;

public class OnboardingFragment extends Fragment {
    public static final String POSITION_ARGUMENT = "position";

    public static Fragment create(int position) {
        OnboardingFragment f = new OnboardingFragment();
        Bundle args = new Bundle();
        args.putInt(POSITION_ARGUMENT, position);
        f.setArguments(args);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.onboarding_content, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getView() != null) {
            String titleText = TextUtilities.toCamelCase(getResources().getStringArray(R.array.onboarding_titles)[getArguments().getInt(POSITION_ARGUMENT)]);

            new OnboardingContentConfigurator((ViewGroup) getView())
                    .setTitleText(titleText)
                    .setSubtitleText(getResources().getStringArray(R.array.onboarding_descriptions)[getArguments().getInt(POSITION_ARGUMENT)])
                    .hideButton();
        }
    }

}
