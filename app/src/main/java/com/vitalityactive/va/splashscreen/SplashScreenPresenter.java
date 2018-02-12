package com.vitalityactive.va.splashscreen;

import com.vitalityactive.va.MainThreadScheduler;
import com.vitalityactive.va.Presenter;
import com.vitalityactive.va.appconfig.AppConfigRepository;
import com.vitalityactive.va.cms.CMSContentDownloader;
import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.events.EventListener;
import com.vitalityactive.va.networking.RequestResult;

import java.util.concurrent.atomic.AtomicBoolean;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class SplashScreenPresenter implements Presenter<SplashScreenPresenter.UserInterface> {
    private final CMSContentDownloader contentDownloader;
    private final AppConfigDataUpdater appConfigDataUpdater;
    private EventDispatcher eventDispatcher;
    private MainThreadScheduler scheduler;
    private UserInterface userInterface;
    private AppConfigRepository appConfigRepository;
    private AtomicBoolean timerCompleted = new AtomicBoolean();

    private EventListener<AppConfigDataUpdatedEvent> appConfigDataUpdatedListener = new EventListener<AppConfigDataUpdatedEvent>() {
        @Override
        public void onEvent(AppConfigDataUpdatedEvent event) {
            scheduler.schedule(new Runnable() {
                @Override
                public void run() {
                    showLogoAndStartDismissTimer();
                }
            });
        }
    };
    private EventListener<AppConfigDataUpdateFailedEvent> appConfigDataUpdateFailedListener = new EventListener<AppConfigDataUpdateFailedEvent>() {
        @Override
        public void onEvent(final AppConfigDataUpdateFailedEvent event) {
            scheduler.schedule(new Runnable() {
                @Override
                public void run() {
                    if (event.getErrorType() == RequestResult.CONNECTION_ERROR){
                        userInterface.showConnectionError();
                    } else if (event.getErrorType() == RequestResult.GENERIC_ERROR){
                        userInterface.showGenericError();
                    } else {
                        userInterface.navigateOnFailedAppConfigUpdate();
                    }

                    userInterface.hideLoadingIndicator();
                }
            });
        }
    };
    private boolean skipAppConfig;

    private void showLogoAndStartDismissTimer() {
        showLogo();
        startDismissTimer();
    }

    private void startDismissTimer() {
        userInterface.startDismissTimer();
    }

    @Inject
    SplashScreenPresenter(CMSContentDownloader contentDownloader,
                          EventDispatcher eventDispatcher,
                          MainThreadScheduler scheduler,
                          AppConfigRepository appConfigRepository,
                          AppConfigDataUpdater appConfigDataUpdater) {
        this.contentDownloader = contentDownloader;
        this.eventDispatcher = eventDispatcher;
        this.scheduler = scheduler;
        this.appConfigRepository = appConfigRepository;
        this.appConfigDataUpdater = appConfigDataUpdater;
    }

    @Override
    public void onUserInterfaceCreated(boolean isNewNavigation) {
        if (contentDownloader.logoExists()) {
            showLogo();
        } else if (skipAppConfig) {
            appConfigDataUpdater.fetchLogo();
        }
        userInterface.setSplashGradient(getGradientColors());
        userInterface.setCustomPrimaryColor(appConfigRepository.getGlobalTintColorHex());
        if (!skipAppConfig) {
            appConfigDataUpdater.updateAppConfig();
        } else if (contentDownloader.logoExists()) {
            startDismissTimer();
        }
        userInterface.setStatusBarColor();
        userInterface.setLoadingIndicatorColor();
    }

    private String[] getGradientColors() {
        return new String[]{
                appConfigRepository.getGradientColor1(),
                appConfigRepository.getGradientColor2()
        };
    }

    private void showLogo() {
        if (contentDownloader.logoExists()) {
            userInterface.showLogo(contentDownloader.getLogoPath());
            userInterface.hideLoadingIndicator();
        }
    }

    void updateAppConfig() {
        userInterface.showLoadingIndicator();
        appConfigDataUpdater.updateAppConfig();
    }

    @Override
    public void onUserInterfaceAppeared() {
        eventDispatcher.addEventListener(AppConfigDataUpdatedEvent.class, appConfigDataUpdatedListener);
        eventDispatcher.addEventListener(AppConfigDataUpdateFailedEvent.class, appConfigDataUpdateFailedListener);
        if (appConfigDataUpdater.didUpdateAppConfigData()) {
            if (contentDownloader.logoExists()) {
                showLogo();
            }
            startDismissTimer();
        }
    }

    @Override
    public void onUserInterfaceDisappeared(boolean isFinishing) {
        eventDispatcher.removeEventListener(AppConfigDataUpdatedEvent.class, appConfigDataUpdatedListener);
        eventDispatcher.removeEventListener(AppConfigDataUpdateFailedEvent.class, appConfigDataUpdateFailedListener);
        scheduler.cancel();
        userInterface.pauseDismissTimer();
    }

    @Override
    public void onUserInterfaceDestroyed() {
    }

    @Override
    public void setUserInterface(UserInterface userInterface) {
        this.userInterface = userInterface;
    }

    void onDismissTimerCompleted() {
        timerCompleted.set(true);
        if (appConfigDataUpdater.didUpdateAppConfigData()) {
            userInterface.dismiss();
        } else {
            startDismissTimer();
        }
    }

    void setSizedLogoFilename(String sizedLogoFilename) {
        contentDownloader.setSizedLogoFilename(sizedLogoFilename);
    }

    void skipAppConfig(boolean skipAppConfig) {
        this.skipAppConfig = skipAppConfig;
    }

    interface UserInterface extends Presenter.UserInterface {
        void showLogo(String path);

        void dismiss();

        void pauseDismissTimer();

        void setSplashGradient(String[] gradientColorStrings);

        void startDismissTimer();

        void navigateOnFailedAppConfigUpdate();

        void setStatusBarColor();

        void showConnectionError();

        void showGenericError();

        void setLoadingIndicatorColor();

        void setCustomPrimaryColor(String globalTintColorHex);
    }
}
