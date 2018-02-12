package com.vitalityactive.va.partnerjourney;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.vitalityactive.va.EmptyStateViewHolder;
import com.vitalityactive.va.R;
import com.vitalityactive.va.dependencyinjection.DependencyInjector;
import com.vitalityactive.va.menu.MenuBuilder;
import com.vitalityactive.va.menu.MenuContainerViewHolder;
import com.vitalityactive.va.menu.MenuItem;
import com.vitalityactive.va.menu.MenuItemType;
import com.vitalityactive.va.uicomponents.adapters.CompositeRecyclerViewAdapter;
import com.vitalityactive.va.uicomponents.adapters.GenericRecyclerViewAdapter;
import com.vitalityactive.va.utilities.CustomTabHelper;
import com.vitalityactive.va.utilities.FileUtilities;

import java.io.IOException;
import java.util.HashMap;

import javax.inject.Inject;

public class PartnerDetailsActivity extends BasePartnerDetailsActivity<PartnerDetailPresenter.UserInterface, PartnerDetailPresenter>
        implements PartnerDetailPresenter.UserInterface, MenuContainerViewHolder.OnMenuItemClickedListener {

    private static final String T_AND_C_SCHEME = "action://";
    private static final int SCHEME_PREFIX_LENGTH = 9;

    @Inject
    PartnerDetailPresenter presenter;
    private View emptyStateView;
    private View partnerContent;
    CustomTabHelper customTabHelper = new CustomTabHelper(this);

    @Override
    protected void create(Bundle savedInstanceState, PartnerType partnerType, long partnerId) {
        setContentView(R.layout.activity_partner_details);
        presenter.setPartner(partnerId);

        partnerContent = findViewById(R.id.partner_content);
        emptyStateView = findViewById(R.id.empty_state);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    protected void injectDependencies(DependencyInjector dependencyInjector) {
        navigationCoordinator.getPartnerJourneyDependencyInjector().inject(this);
    }

    @Override
    protected PartnerDetailPresenter.UserInterface getUserInterface() {
        return this;
    }

    @Override
    protected PartnerDetailPresenter getPresenter() {
        return presenter;
    }

    @Override
    public void setupDetails(String partnerName, String htmlContent, String url) {
        final WebView webView = findViewById(R.id.webview);
        webView.loadDataWithBaseURL("file:///android_asset/partner-templates/", getWebPageWithContent(htmlContent), "text/html; charset=utf-8", "utf-8", null);
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.startsWith("tel:")) {
                    openDialer(url);
                    return true;
                } else if (url.startsWith(T_AND_C_SCHEME)) {
                    showTermsAndConditions(url);
                    return true;
                }
                return false;
            }

            @Override
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                String url = request.getUrl().toString();
                if( url.startsWith("tel:")) {
                    openDialer(url);
                    return true;
                } else if (url.startsWith(T_AND_C_SCHEME)) {
                    showTermsAndConditions(url);
                    return true;
                }
                return super.shouldOverrideUrlLoading(view, request);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                hideLoadingIndicator();
            }
        });

        if (url == null || url.equals("")) {
            findViewById(R.id.menu).setVisibility(View.GONE);
        } else {
            buildBottomMenu();
            customTabHelper.bind(url);
        }
    }

    protected void openDialer(String telLink){
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse(telLink));
        startActivity(intent);
    }

    @Override
    protected void destroy() {
        customTabHelper.unbind();
    }

    private String getWebPageWithContent(@NonNull String partnerDetails) {
        return getHtmlTemplate().replace("CONTENT_CONTENT_CONTENT", partnerDetails);
    }

    @NonNull
    private String getHtmlTemplate() {
        return getHtmlTemplate(getPartnerType().detailsHtmlTemplateFile, "template.html");
    }

    @NonNull
    private String getHtmlTemplate(String fileName, String fallback) {
        try {
            return FileUtilities.readFile(getAssets().open(fileName));
        } catch (IOException e) {
            if (fallback != null && !fallback.equals(fileName)) {
                return getHtmlTemplate(fallback, null);
            }
            return "CONTENT_CONTENT_CONTENT";
        }
    }

    @Override
    public void showGenericError() {
        setupEmptyStateViewHolder(R.string.error_unable_to_load_title_503, R.string.error_unable_to_load_message_504);
        hideLoadingIndicator();
    }

    @Override
    public void hideLoadingIndicator() {
        findViewById(R.id.loadingPanel).setVisibility(View.GONE);
    }

    @Override
    public void showLoadingIndicator() {
        emptyStateView.setVisibility(View.GONE);
        partnerContent.setVisibility(View.VISIBLE);
        findViewById(R.id.loadingPanel).setVisibility(View.VISIBLE);
    }

    @Override
    public void showConnectionError() {
        setupEmptyStateViewHolder(R.string.connectivity_error_alert_title_44, R.string.connectivity_error_alert_message_45);
        hideLoadingIndicator();
    }

    @Override
    public void loadPartnerDetailsBrowser(String url) {
        customTabHelper.launch(url);
    }

    @Override
    public void setupActionBar(String partnerName) {
        setActionBarTitleAndDisplayHomeAsUp(partnerName);
    }

    @NonNull
    private EmptyStateViewHolder setupEmptyStateViewHolder(int titleId, int messageId) {
        EmptyStateViewHolder emptyStateViewHolder = new EmptyStateViewHolder(emptyStateView);

        emptyStateViewHolder
                .setup(titleId, messageId, R.string.try_again_button_title_43)
                .setButtonClickListener(new EmptyStateViewHolder.EmptyStatusButtonClickedListener() {
                    @Override
                    public void onEmptyStateButtonClicked(EmptyStateViewHolder emptyStateViewHolder) {
                        presenter.loadPartnerDetails();
                    }
                })
                .showEmptyStateViewAndHideOtherView(partnerContent);

        return emptyStateViewHolder;
    }

    private void buildBottomMenu() {
        MenuBuilder menuBuilder = buildMenuBuilder();
        final CompositeRecyclerViewAdapter adapter = buildCompositeRecyclerViewAdapter(menuBuilder);
        ((RecyclerView) findViewById(R.id.menu)).setAdapter(adapter);
    }

    @NonNull
    private CompositeRecyclerViewAdapter buildCompositeRecyclerViewAdapter(MenuBuilder menuBuilder) {
        @SuppressLint("UseSparseArrays") HashMap<Integer, GenericRecyclerViewAdapter> adapters = new HashMap<>();
        adapters.put(R.layout.menu_container, menuBuilder.build());
        return new CompositeRecyclerViewAdapter(adapters, new int[]{
                R.layout.menu_container,
        });
    }

    @NonNull
    private MenuBuilder buildMenuBuilder() {
        MenuBuilder menuBuilder = new MenuBuilder(this)
                .setClickListener(this);
        menuBuilder.addMenuItem(MenuItem.Builder.getStarted());
        return menuBuilder;
    }

    @Override
    public void onClicked(MenuItemType menuItemType) {
        if (menuItemType == MenuItemType.GetStarted) {
            presenter.onGetStartedTapped();
        }
    }

    private void showTermsAndConditions(String url) {
        if (url.trim().length() > SCHEME_PREFIX_LENGTH) {
            String articleId = url.replace(T_AND_C_SCHEME, "");
            navigationCoordinator.navigateToPartnerTermsAndConditions(this, articleId);
        }
    }
}
