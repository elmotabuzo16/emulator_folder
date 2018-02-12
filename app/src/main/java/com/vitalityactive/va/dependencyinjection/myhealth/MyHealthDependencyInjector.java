package com.vitalityactive.va.dependencyinjection.myhealth;

import com.vitalityactive.va.myhealth.disclaimer.MyHealthDisclaimerActivity;

import dagger.Subcomponent;

@MyHealthScope
@Subcomponent(modules = {MyHealthModule.class})
public interface MyHealthDependencyInjector {
    void inject(MyHealthDisclaimerActivity activity);


}
