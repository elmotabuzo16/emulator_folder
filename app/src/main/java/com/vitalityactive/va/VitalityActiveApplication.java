package com.vitalityactive.va;

import android.content.Context;
import android.support.multidex.MultiDexApplication;

import com.github.ajalt.reprint.core.Reprint;
import com.jakewharton.threetenabp.AndroidThreeTen;
import com.vitalityactive.va.appconfig.AppConfigRepository;
import com.vitalityactive.va.cms.keyvaluecontent.CMSKeyValueContentRepository;
import com.vitalityactive.va.dependencyinjection.DaggerDependencyInjector;
import com.vitalityactive.va.dependencyinjection.DependencyInjector;
import com.vitalityactive.va.dependencyinjection.ModuleCollection;
import com.vitalityactive.va.networking.WebServiceClient;
import com.vitalityactive.va.utilities.DataStoreClearer;
import com.vitalityactive.va.utilities.FileUtilities;
import com.vitalityactive.va.utilities.ImageLoader;

import javax.inject.Inject;

import io.realm.Realm;

public class VitalityActiveApplication extends MultiDexApplication {
    private static Context mContext;

    private DependencyInjector dependencyInjector;

    @Inject
    CMSKeyValueContentRepository contentRepository;

    @Inject
    AppConfigRepository appConfigRepository;

    @Inject
    ExecutorServiceScheduler scheduler;

    @Inject
    WebServiceClient webServiceClient;

    @Override
    public void onCreate() {
        super.onCreate();
        AndroidThreeTen.init(this);
        Realm.init(this);
        Reprint.initialize(this);

        initialiseObjectGraph();
        registerActivityLifecycleCallbacks(new VitalityActiveActivityLifecycleCallbacks());
        mContext = getApplicationContext();
    }

    public void initialiseObjectGraph() {
        initialiseObjectGraph(new ModuleCollection(this));
    }

    public void initialiseObjectGraph(ModuleCollection moduleCollection) {
        //noinspection deprecation: flavor specific module might not have any changes for this flavor
        dependencyInjector = DaggerDependencyInjector.builder()
                .networkModule(moduleCollection.networkModule)
                .persistenceModule(moduleCollection.persistenceModule)
                .flavorSpecificModule(moduleCollection.flavorSpecificModule)
                .defaultModule(moduleCollection.defaultModule)
                .build();

        ServiceLocator.setInstance(new ServiceLocator.Builder()
                .setEventDispatcher(dependencyInjector.eventDispatcher())
                .setDeviceSpecificPreferences(dependencyInjector.preferences())
                .build());

        dependencyInjector.inject(this);
    }

    public DependencyInjector getDependencyInjector() {
        return dependencyInjector;
    }

    public CMSKeyValueContentRepository getContentRepository() {
        return contentRepository;
    }

    public void reset() {
        cancelAllRequests();
        removeResourceFiles();
        removeSplashScreenLogo();
        removeHealthCareBenefitPDF();
        clearDatabase();
        removeAllFiles();
        initialiseObjectGraph();
        scheduler.schedule(new Runnable() {
            @Override
            public void run() {
                ImageLoader.clearCache(VitalityActiveApplication.this);
            }
        });
    }

    private void cancelAllRequests() {
        webServiceClient.cancelAllRequests();
    }

    public static boolean isDebugBuild() {
        return BuildConfig.DEBUG;
    }

    public static boolean isDevDebugBuild() {
        return isDebugBuild() && BuildConfig.FLAVOR.equals("dev");
    }

    private void removeHealthCareBenefitPDF() {
        deleteFile(appConfigRepository.getHealthCareBenefitFileName());
    }

    private void removeSplashScreenLogo() {
        deleteFile("logo.png");
    }

    public void removeProfileImage() {
        deleteFile("profile.jpg");
    }

    private void removeResourceFiles() {
        for (String fileName : appConfigRepository.getResourceFileNames()) {
            deleteFile(fileName);
        }
    }

    private void removeAllFiles() {
        FileUtilities.removeRecursively(FileUtilities.getBaseApplicationDirectoryOnExternalStorage(this));
    }

    private void clearDatabase() {
        DataStoreClearer.clear(this);
    }

    public static Context getContext() {
        return mContext;
    }
}
