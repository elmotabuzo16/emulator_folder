package com.vitalityactive.va.userpreferences.learnmore;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.webkit.WebView;

import com.vitalityactive.va.BaseUkePresentedActivity;
import com.vitalityactive.va.R;
import com.vitalityactive.va.dependencyinjection.DependencyInjector;
import com.vitalityactive.va.userpreferences.learnmore.presenter.ShareVitalityStatusLearnMorePresenter;
import com.vitalityactive.va.utilities.FileUtilities;

import java.io.IOException;

import javax.inject.Inject;

public class ShareVitalityStatusActivity extends BaseUkePresentedActivity<ShareVitalityStatusLearnMorePresenter.UI,
        ShareVitalityStatusLearnMorePresenter<ShareVitalityStatusLearnMorePresenter.UI>>
        implements ShareVitalityStatusLearnMorePresenter.UI {

    @Inject
    ShareVitalityStatusLearnMorePresenter presenter;

    private WebView webView;

    @Override
    protected void create(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.vitality_status_learn_more_container);
        setUpActionBarWithTitle(R.string.uke_communication_pref_button_status_377)
                .setDisplayHomeAsUpEnabled(true);
        webView = findViewById(R.id.share_vitaltiy_webview);
    }

    @Override
    public void showVitalityStatusLearnMore(final @NonNull String shareVitalityStatusHtml) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                webView.loadData(getWebPageWithContent(shareVitalityStatusHtml), "text/html; charset=utf-8", "utf-8");
            }
        });
    }

    @Override
    protected void injectDependencies(DependencyInjector dependencyInjector) {
        dependencyInjector.inject(this);
    }

    @Override
    protected ShareVitalityStatusLearnMorePresenter.UI getUserInterface() {
        return this;
    }

    @Override
    protected ShareVitalityStatusLearnMorePresenter<ShareVitalityStatusLearnMorePresenter.UI> getPresenter() {
        return presenter;
    }

    private String getWebPageWithContent(@NonNull String shareVitalityStatusHtml) {
        try {
            String template = FileUtilities.readFile(getAssets().open("share_vitality_status_template.html"));
//            return template;
            return template.replace("CONTENT_CONTENT_CONTENT", shareVitalityStatusHtml);
        } catch (IOException e) {
            return shareVitalityStatusHtml;
        }
    }
}
