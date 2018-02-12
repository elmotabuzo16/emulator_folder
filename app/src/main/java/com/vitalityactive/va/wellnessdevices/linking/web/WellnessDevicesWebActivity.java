package com.vitalityactive.va.wellnessdevices.linking.web;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.vitalityactive.va.BasePresentedActivity;
import com.vitalityactive.va.R;
import com.vitalityactive.va.dependencyinjection.DependencyInjector;

import javax.inject.Inject;

import static com.vitalityactive.va.wellnessdevices.Constants.SUCCESS_LINK_REDIRECT_URL;

public class WellnessDevicesWebActivity
        extends BasePresentedActivity<WellnessDevicesWebFlowPresenter.UI, WellnessDevicesWebFlowPresenter>
        implements WellnessDevicesWebFlowPresenter.UI {
    public static final String KEY_WEB_EXTRA = "WebExtra";
    public static final String KEY_REQUEST_TYPE = "RequestType";
    public static final String KEY_PARTNER_LINK = "PartnerLink";
    private Bundle inputBundle;
    private WebView webView;
    private WebViewClient webViewClient = new WebViewClient(){
        //keeping this method just for logging purpose
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            Log.d(">>>", "started " + url);
            super.onPageStarted(view, url, favicon);
        }

        //keeping this method just for logging purpose
        @Override
        public void onPageFinished(WebView view, String url) {
            Log.d(">>>", "finished: " + url);
        }

        // We should catch redirect here
        // actually there is another method in WebViewClient class but this one seems to be more reliable according to tests
        @SuppressWarnings("deprecation")
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return Build.VERSION.SDK_INT < Build.VERSION_CODES.N && handleUrl(url);
        }

        @RequiresApi(Build.VERSION_CODES.N)
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            return handleUrl(request.getUrl().toString());
        }

        private boolean handleUrl(String url) {
            Log.d(">>>", "shouldOverrideUrlLoading: " + url);
            if(url.toUpperCase().startsWith(SUCCESS_LINK_REDIRECT_URL.toUpperCase())){
                navigateToPreviousScreen();
                return true;
            }
            return false;
        }

        //keeping this method just for logging purpose
        @Override
        public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
            Log.d(">>>", "onReceivedHttpError " + request.getUrl() + ", Error: " + errorResponse.getReasonPhrase());
            super.onReceivedHttpError(view, request, errorResponse);
        }

        //keeping this method just for logging purpose
        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            Log.d(">>>", "onReceivedError " + request.getUrl() + ", Error: " + error.getDescription());
            super.onReceivedError(view, request, error);
        }

    };

    @Inject
    WellnessDevicesWebFlowPresenter presenter;

    @Override
    protected void create(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_wd_web);
        inputBundle = getIntent().getBundleExtra(KEY_WEB_EXTRA);
        String partnerLink = inputBundle.getString(KEY_PARTNER_LINK);
//        setUpActionBarWithTitle(partnerName)
//                .setDisplayHomeAsUpEnabled(true);

        webView = (WebView) findViewById(R.id.wvRoot);
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        webView.setWebViewClient(webViewClient);
        webView.loadUrl(partnerLink);
    }

    @Override
    protected void injectDependencies(DependencyInjector dependencyInjector) {
        dependencyInjector.inject(this);
    }

    @Override
    protected WellnessDevicesWebFlowPresenter.UI getUserInterface() {
        return this;
    }

    @Override
    protected WellnessDevicesWebFlowPresenter getPresenter() {
        return presenter;
    }

    private void navigateToPreviousScreen(){
        Intent intent = NavUtils.getParentActivityIntent(this);
        intent.putExtra(KEY_WEB_EXTRA, inputBundle);
        setResult(Activity.RESULT_OK, intent);
        NavUtils.navigateUpTo(this, intent);
    }

    private void navigateToPreviousScreenWithError(){
        Intent intent = NavUtils.getParentActivityIntent(this);
        intent.putExtra(KEY_WEB_EXTRA, inputBundle);
        setResult(Activity.RESULT_CANCELED, intent);
        NavUtils.navigateUpTo(this, intent);
    }
}
