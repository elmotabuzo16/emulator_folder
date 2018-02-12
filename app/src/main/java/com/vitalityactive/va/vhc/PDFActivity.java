package com.vitalityactive.va.vhc;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.content.FileProvider;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.vitalityactive.va.BasePresentedActivity;
import com.vitalityactive.va.R;
import com.vitalityactive.va.dependencyinjection.DependencyInjector;

import java.io.File;

import javax.inject.Inject;

public class PDFActivity
        extends BasePresentedActivity<PDFPresenter.UserInterface, PDFPresenter>
        implements PDFPresenter.UserInterface {

    public static final String FILE_NAME = "FILE_NAME";
    public static final String TITLE = "TITLE";
    @Inject
    PDFPresenter presenter;
    private PDFView pdfView;
    private LinearLayout emptyState;
    private TextView titleView;
    private TextView subtitleView;
    private TextView buttonView;
    private Intent sendIntent;

    @Override
    protected void create(@Nullable Bundle savedInstanceState) {
        getDependencyInjector().inject(this);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_vhc_health_care_pdf);

        @StringRes int titleResourceId = getIntent().getIntExtra(TITLE, R.string.landing_screen_healthcare_pdf_label_248);
        setUpActionBarWithTitle(titleResourceId)
                .setDisplayHomeAsUpEnabled(true);

        assignTextViews();
        setTextOfViews();

        getPresenter().setFileName(getIntent().getStringExtra(FILE_NAME));
    }

    private void setTextOfViews() {
        buttonView.setText(R.string.try_again_button_title_43);
    }

    private void assignTextViews() {
        pdfView = findViewById(R.id.pdfView);
        emptyState = findViewById(R.id.empty_state);
        titleView = findViewById(R.id.empty_state_title);
        subtitleView = findViewById(R.id.empty_state_subtitle);
        buttonView = findViewById(R.id.empty_state_button);
    }

    @Override
    protected void injectDependencies(DependencyInjector dependencyInjector) {
        dependencyInjector.inject(this);
    }

    @Override
    protected PDFActivity getUserInterface() {
        return this;
    }

    @Override
    protected PDFPresenter getPresenter() {
        return presenter;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.share_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() != R.id.menu_item_share) {
            return super.onOptionsItemSelected(item);
        }

        if (sendIntent != null) {
            startActivity(Intent.createChooser(sendIntent, "Share"));
        }
        
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void displayFileInPDFView(File file) {
        showPopulatedState();

        pdfView.fromFile(file)
                .scrollHandle(new DefaultScrollHandle(this))
                .load();
    }

    @Override
    public void displayGenericError() {
        showErrorState();

        titleView.setText(R.string.error_unable_to_load_title_503);
        subtitleView.setText(R.string.error_unable_to_load_message_504);
    }

    private void showErrorState() {
        pdfView.setVisibility(View.GONE);
        emptyState.setVisibility(View.VISIBLE);
    }

    @Override
    public void displayConnectionError() {
        showErrorState();

        titleView.setText(R.string.connectivity_error_alert_title_44);
        subtitleView.setText(R.string.connectivity_error_alert_message_45);
    }

    @Override
    public void showPopulatedState() {
        emptyState.setVisibility(View.GONE);
        pdfView.setVisibility(View.VISIBLE);
    }

    @Override
    public void createShareIntent(File pdfFile) {
        if (pdfFile.exists()) {
            Uri uri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", pdfFile);
            sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.setType("application/pdf");
            sendIntent.putExtra(Intent.EXTRA_STREAM, uri);
        }
    }

    public void onEmptyStateButtonClicked(View view) {
        presenter.fetchPDF();
    }
}
