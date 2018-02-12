package com.vitalityactive.va.dependencyinjection;

import javax.inject.Singleton;

import dagger.Component;

// add igi-only activities and fragments here.
// add main activities and fragments to DependencyInjector

@Singleton
@Component(modules = {NetworkModule.class, PersistenceModule.class, DefaultModule.class, FlavorSpecificModule.class})
public interface FlavorDependencyInjector {
}
