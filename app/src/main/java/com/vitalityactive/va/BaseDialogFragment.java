package com.vitalityactive.va;

import android.support.v4.app.DialogFragment;

import com.vitalityactive.va.dependencyinjection.DependencyInjector;

public class BaseDialogFragment extends DialogFragment {

    protected VitalityActiveApplication getVitalityActiveApplication() {
        return (VitalityActiveApplication) getActivity().getApplication();
    }

    protected DependencyInjector getDependencyInjector() {
        return getVitalityActiveApplication().getDependencyInjector();
    }
}
