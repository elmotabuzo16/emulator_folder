package com.vitalityactive.va.wellnessdevices;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.vitalityactive.va.BaseActivity;
import com.vitalityactive.va.R;

public class SimpleBrowserActivity extends BaseActivity {
    public static final String KEY_URL_EXTRA = "UrlExtra";
    private WebView webView;
    private WebViewClient webViewClient = new WebViewClient() {
        @SuppressWarnings("deprecation")
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return true; // prevent redirect to external browser
        }

        @RequiresApi(Build.VERSION_CODES.N)
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            return true;
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_wd_web);
        final String url = getIntent().getStringExtra(KEY_URL_EXTRA);

        webView = (WebView) findViewById(R.id.wvRoot);
        webView.setWebViewClient(webViewClient);
        webView.loadUrl(url);
    }
}