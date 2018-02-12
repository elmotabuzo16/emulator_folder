package com.vitalityactive.va.myhealth.tipdetail;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.MenuItem;
import android.webkit.WebView;

import com.vitalityactive.va.BasePresentedActivity;
import com.vitalityactive.va.EmptyStateViewHolder;
import com.vitalityactive.va.R;
import com.vitalityactive.va.appconfig.AppConfigRepository;
import com.vitalityactive.va.dependencyinjection.DependencyInjector;
import com.vitalityactive.va.utilities.FileUtilities;

import java.io.IOException;

import javax.inject.Inject;

public class MyHealthTipDetailActivity extends BasePresentedActivity<MyHealthTipDetailPresenter.UserInterface, MyHealthTipDetailPresenter>
        implements MyHealthTipDetailPresenter.UserInterface, EmptyStateViewHolder.EmptyStatusButtonClickedListener {

    public static final String TIP_TYPE_KEY = "TIP_TYPE_KEY";
    public static final String TIP_CODE = "TIP_CODE";

    @Inject
    MyHealthTipDetailPresenter presenter;
    @Inject
    AppConfigRepository appConfigRepository;
    private WebView webView;
    private EmptyStateViewHolder emptyStateViewHolder;

    @Override
    protected void create(@Nullable Bundle savedInstanceState) {
        super.create(savedInstanceState);
        setContentView(R.layout.activity_my_health_tip_detail);
        presenter.setTipTypeKey(getIntent().getIntExtra(TIP_TYPE_KEY, -1));
        presenter.setTipCode(getIntent().getStringExtra(TIP_CODE));
        setUpActionBarWithTitle(getString(R.string.my_health_detail_main_title_tip_1068))
                .setDisplayHomeAsUpEnabled(true);
        webView = (WebView) findViewById(R.id.webview);
        setActionBarColor();
        setStatusBarColor();
        setUpEmptyState();
    }

    protected void setActionBarColor() {
        try {
            super.setActionBarColor(globalTintColor());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void setStatusBarColor() {
        try {
            super.setStatusBarColor(globalTintColor());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected @ColorInt
    int globalTintColor() {
        return Color.parseColor(appConfigRepository.getGlobalTintColorHex());
    }


    @Override
    protected void injectDependencies(DependencyInjector dependencyInjector) {
        dependencyInjector.inject(this);
    }

    @Override
    protected MyHealthTipDetailPresenter.UserInterface getUserInterface() {
        return this;
    }

    @Override
    protected MyHealthTipDetailPresenter getPresenter() {
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


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
