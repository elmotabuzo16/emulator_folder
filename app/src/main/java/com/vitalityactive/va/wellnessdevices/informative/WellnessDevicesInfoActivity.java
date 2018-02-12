package com.vitalityactive.va.wellnessdevices.informative;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.webkit.WebView;

import com.vitalityactive.va.BasePresentedActivity;
import com.vitalityactive.va.EmptyStateViewHolder;
import com.vitalityactive.va.R;
import com.vitalityactive.va.dependencyinjection.DependencyInjector;
import com.vitalityactive.va.utilities.FileUtilities;

import java.io.IOException;

import javax.inject.Inject;

public class WellnessDevicesInfoActivity
        extends BasePresentedActivity<WellnessDevicesInformativePresenter.UserInterface, WellnessDevicesInformativePresenter>
        implements WellnessDevicesInformativePresenter.UserInterface,
        EmptyStateViewHolder.EmptyStatusButtonClickedListener {

    public static final String KEY_BUNDLE = "KeyBundle";
    public static final String KEY_TITLE = "KeyTitle";
    public static final String KEY_ARTICLE_ID = "KeyArticleId";

    private WebView webView;
    private EmptyStateViewHolder emptyStateViewHolder;

//    @Inject
//    EventDispatcher eventDispatcher;

    @Inject
    WellnessDevicesInformativePresenter presenter;

    @Override
    protected void create(Bundle savedInstanceState) {
        super.create(savedInstanceState);
        setContentView(R.layout.activity_wd_informative);

        Bundle bundle = getIntent().getBundleExtra(KEY_BUNDLE);

        presenter.setArticleId(bundle.getString(KEY_ARTICLE_ID));
        setUpActionBarWithTitle(bundle.getString(KEY_TITLE))
                .setDisplayHomeAsUpEnabled(true);

        webView = (WebView) findViewById(R.id.webview);
        setUpEmptyState();
    }

    @Override
    protected void injectDependencies(DependencyInjector dependencyInjector) {
        dependencyInjector.inject(this);
    }

    @Override
    protected WellnessDevicesInfoActivity getUserInterface() {
        return this;
    }

    @Override
    protected WellnessDevicesInformativePresenter getPresenter() {
        return presenter;
    }

    @Override
    public void showContent(@NonNull String content) {
        if (TextUtils.isEmpty(content)) {
            showContentRequestErrorMessage();
        } else {
            emptyStateViewHolder.hideEmptyStateViewAndShowOtherView(webView);
            webView.loadData(getWebPageWithContent(content), "text/html; charset=utf-8", "utf-8");
        }
    }

    private String getWebPageWithContent(@NonNull String content) {
        try {
            String template = FileUtilities.readFile(getAssets().open("template.html"));
            return template.replace("CONTENT_CONTENT_CONTENT", content);
        } catch (IOException e) {
            return content;
        }
    }

    private void setUpEmptyState() {
        emptyStateViewHolder = new EmptyStateViewHolder(findViewById(R.id.empty_state));
        emptyStateViewHolder.setup(R.string.error_unable_to_load_title_503,
                R.string.error_unable_to_load_message_504,
                R.string.try_again_button_title_43,
                this);
    }

    private void showContentRequestErrorMessage() {
        emptyStateViewHolder.showEmptyStateViewAndHideOtherView(webView);
    }

    @Override
    public void showGenericContentRequestErrorMessage() {
        showContentRequestErrorMessage();
    }

    @Override
    public void showConnectionContentRequestErrorMessage() {
        showContentRequestErrorMessage();
    }

    @Override
    public void onEmptyStateButtonClicked(EmptyStateViewHolder emptyStateViewHolder) {
        presenter.fetchContent();
    }
}
