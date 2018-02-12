package com.vitalityactive.va.search;

import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.vitalityactive.va.BasePresentedActivity;
import com.vitalityactive.va.R;
import com.vitalityactive.va.activerewards.viewholder.TitleViewHolder;
import com.vitalityactive.va.appconfig.AppConfigRepository;
import com.vitalityactive.va.dependencyinjection.DependencyInjector;
import com.vitalityactive.va.dto.ContentHelpDTO;
import com.vitalityactive.va.uicomponents.adapters.CompositeRecyclerViewAdapter;
import com.vitalityactive.va.uicomponents.adapters.GenericRecyclerViewAdapter;
import com.vitalityactive.va.utilities.FileUtilities;
import com.vitalityactive.va.utilities.TextUtilities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

public class SearchResultsActivity extends BasePresentedActivity<ContentHelpPresenter.UserInterface, ContentHelpPresenter<ContentHelpPresenter.UserInterface>>
        implements ContentHelpPresenter.UserInterface {

    @Inject
    ContentHelpPresenter presenter;

    @Inject
    AppConfigRepository appConfigRepository;

    private TextView feedbackYes;
    private TextView feedbackNo;
    private TextView feedbackHeader;
    private LinearLayout feedbackOptions;
    private RecyclerView recyclerView;
    private CardView relatedCardView;
    private int globalTintColor, globalTintDarker;
    public static final String ITEM_QUERY = "ITEM_QUERY";

    private List<Integer> viewTypes = new ArrayList<>();
    private String query;
    private String sample;

    @Override
    protected void create(@Nullable Bundle savedInstanceState) {
        super.create(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        setUpActionBar().setDisplayHomeAsUpEnabled(true);

        globalTintColor = Color.parseColor(appConfigRepository.getGlobalTintColorHex());
        globalTintDarker = Color.parseColor(appConfigRepository.getGlobalTintDarkerColorHex());

        if (savedInstanceState == null) {
            setActionBarTitle(getResources().getString(R.string.events_help));
            setCloseAsActionBarIcon();
            setStatusBarColor(globalTintDarker);
            setActionBarColor(globalTintColor);
        }

        Bundle extras = getIntent().getExtras();
        if (TextUtilities.isNullOrEmpty(query)) {
            if (extras != null) {
                query = extras.getString(ITEM_QUERY);
            }
        }

        handleIntent(getIntent());
        loadDetails();
        setHelpFeedback();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            query = intent.getStringExtra(SearchManager.QUERY);
            Toast.makeText(this, getString(R.string.generic_searching_by) + query, Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void loadDetails() {
        String tagName = "help";
        if (!TextUtils.isEmpty(query)) {
            if (query.contains("?")) {
                String tagkey = query.replaceAll("[?]", "");
                presenter.loadHelpDetails(tagkey, tagName);
            } else {
                presenter.loadHelpDetails(query, tagName);
            }
        }
    }

    @Override
    public void setDetailsView(List<ContentHelpDTO> contents) {

        for (int i = 0; i < contents.size(); i++) {
            if (contents.get(i).getQuestion().contains(query)) {
                sample = contents.get(i).getAnswer();
            }
            viewTypes.add(R.layout.menu_item_small_text);
        }

        final WebView webView = findViewById(R.id.helpDetailsWeb);

        if (!TextUtils.isEmpty(sample)) {
            webView.loadData(getWebPageWithContent(sample), "text/html; charset=utf-8", "utf-8");
            webView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    int height = webView.getContentHeight();
                    if (height != 0) {
                        webView.getViewTreeObserver().removeOnPreDrawListener(this);
                    }
                    return false;
                }
            });
        }
    }

    private String getWebPageWithContent(@NonNull String termsAndConditions) {
        try {
            String template = FileUtilities.readFile(getAssets().open("template.html"));
            return template.replace("CONTENT_CONTENT_CONTENT", termsAndConditions);
        } catch (IOException e) {
            return termsAndConditions;
        }
    }

    @Override
    public void setRelatedHelp(List<ContentHelpDTO> relatedHelp) {
        recyclerView = findViewById(R.id.contentRelatedHelp);
        relatedCardView = findViewById(R.id.relatedCardView);

        HashMap<Integer, GenericRecyclerViewAdapter> adapters = createAdapter(relatedHelp);

        CompositeRecyclerViewAdapter containersRecyclerViewAdapter =
                new CompositeRecyclerViewAdapter(adapters, createViewTypeArray());


        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(containersRecyclerViewAdapter);
    }

    private HashMap<Integer, GenericRecyclerViewAdapter> createAdapter(List<ContentHelpDTO> questions) {
        @SuppressLint("UseSparseArrays") HashMap<Integer, GenericRecyclerViewAdapter> adapter = new HashMap<>();


        adapter.put(R.layout.active_rewards_title, createHeaderAdapter());

        if (questions.size() != 0) {
            relatedCardView.setVisibility(View.VISIBLE);
            adapter.put(R.layout.menu_item_small_text,
                    createRelatedHelp(questions));
        } else {
            relatedCardView.setVisibility(View.GONE);
        }

        return adapter;
    }

    private GenericRecyclerViewAdapter createRelatedHelp(List<ContentHelpDTO> questions) {


        for (int i = 0; i < questions.size(); i++) {
            viewTypes.add(R.layout.menu_item_small_text);
        }

        return new GenericRecyclerViewAdapter<>(this,
                questions,
                R.layout.menu_item_small_text,
                new ContentHelpViewHolder.Factory());
    }

    private GenericRecyclerViewAdapter createHeaderAdapter() {
        viewTypes.add(R.layout.active_rewards_title);

        return new GenericRecyclerViewAdapter<>(this,
                Collections.singletonList(getString(R.string.help_related_help_9999)),
                R.layout.active_rewards_title,
                new TitleViewHolder.Factory());
    }

    private int[] createViewTypeArray() {
        int[] array = new int[viewTypes.size()];

        for (int i = 0; i < viewTypes.size(); i++)
            array[i] = viewTypes.get(i);

        return array;
    }

    @Override
    public void setHelpFeedback() {
        feedbackYes = findViewById(R.id.feedbackYesOption);
        feedbackNo = findViewById(R.id.feedbackNoOption);
        feedbackOptions = findViewById(R.id.feedbackOptions);
        feedbackHeader = findViewById(R.id.feedbackHeader);

        feedbackOptions.setVisibility(View.GONE);
        feedbackHeader.setVisibility(View.GONE);

        feedbackHeader.setText(getResources().getString(R.string.help_was_this_helpful_9999));

        feedbackYes.setOnClickListener(view -> onUserClickYesOption());
    }

    private void onUserClickYesOption() {
        feedbackHeader.setText(getResources().getString(R.string.help_feedback_thanks_message_9999));
        feedbackOptions.setVisibility(View.GONE);
    }

    @Override
    protected void injectDependencies(DependencyInjector dependencyInjector) {
        dependencyInjector.inject(this);
    }

    @Override
    protected ContentHelpPresenter.UserInterface getUserInterface() {
        return this;
    }

    @Override
    protected ContentHelpPresenter getPresenter() {
        return presenter;
    }

    @Override
    public void showLoadingIndicator() {
        super.showLoadingIndicator();
    }

    @Override
    public void hideLoadingIndicator() {
        super.hideLoadingIndicator();
    }

}
