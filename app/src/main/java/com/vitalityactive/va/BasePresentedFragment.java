package com.vitalityactive.va;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.vitalityactive.va.dependencyinjection.DependencyInjector;

public abstract class BasePresentedFragment<UI, P extends Presenter<UI>> extends BaseFragment {
    protected P presenter;

    @Override
    public final void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        injectDependencies(getDependencyInjector());
        presenter = getPresenter();
        getPresenter().setUserInterface(getUserInterface());

        activityCreated(savedInstanceState);

        getPresenter().onUserInterfaceCreated(savedInstanceState == null);
    }

    protected void activityCreated(@Nullable Bundle savedInstanceState) {

    }

    @Override
    public final void onDestroy() {
        super.onDestroy();
        destroy();
        getPresenter().onUserInterfaceDestroyed();
        getPresenter().setUserInterface(null);
    }

    protected void destroy() {

    }

    @Override
    protected final void onAppear() {
        appear();
        getPresenter().onUserInterfaceAppeared();
    }

    @Override
    protected final void onDisappear() {
        disappear();
        getPresenter().onUserInterfaceDisappeared(getActivity().isFinishing());
    }

    protected void appear() {

    }

    protected void disappear() {

    }


    protected void setUIColor(String colorValue){
        //TODO: Make the method abstract once all devs are comfortable to use this method
        //TODO: Interim: This will be the placeholder to set the colors for each widget since they seem to be getting it on globalTintColor
        //TODO: Long term: Decide on which theme to use depending of the user's typeCode for market.
        // And make the layout widgets use android:tint="?attr/colorPrimary"
        // Note: Styles prepare first, layout with their attr next, and java code last.
        // So we need to use the styles for its potential
    }

    protected abstract void injectDependencies(DependencyInjector dependencyInjector);

    protected abstract UI getUserInterface();

    protected abstract P getPresenter();
}
