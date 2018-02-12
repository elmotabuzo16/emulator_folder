package com.vitalityactive.va;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnitRunner;

import com.vitalityactive.va.dependencyinjection.ModuleCollection;
import com.vitalityactive.va.testutilities.dependencyinjection.PersistenceWithInMemoryRealmModule;

public class MockJUnitRunner extends AndroidJUnitRunner {
    private VitalityActiveApplication application;

    public static MockJUnitRunner getInstance() {
        return (MockJUnitRunner) InstrumentationRegistry.getInstrumentation();
    }

    @NonNull
    public static ModuleCollection getModuleCollection() {
        ModuleCollection moduleCollection = new ModuleCollection(getInstance().getApplication());
        moduleCollection.persistenceModule = new PersistenceWithInMemoryRealmModule(getInstance().getTargetContext());
        return moduleCollection;
    }

    public static void initialiseTestObjectGraph() {
        initialiseTestObjectGraph(getModuleCollection());
    }

    public static void initialiseTestObjectGraph(ModuleCollection moduleCollection) {
        getInstance().getApplication().initialiseObjectGraph(moduleCollection);
    }

    @Override
    public Application newApplication(ClassLoader cl, String className, Context context)
            throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        application = (VitalityActiveApplication) super.newApplication(cl, VitalityActiveApplication.class.getName(), context);
        return application;
    }

    public VitalityActiveApplication getApplication() {
        return application;
    }
}
