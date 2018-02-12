package com.vitalityactive.va.uicomponents.learnmore;

import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.vitalityactive.va.BaseActivity;
import com.vitalityactive.va.R;
import com.vitalityactive.va.menu.MenuBuilder;
import com.vitalityactive.va.menu.MenuItem;
import com.vitalityactive.va.uicomponents.adapters.ContainersRecyclerViewAdapter;
import com.vitalityactive.va.uicomponents.adapters.GenericRecyclerViewAdapter;
import com.vitalityactive.va.utilities.ViewUtilities;

import java.util.ArrayList;
import java.util.List;

public abstract class LearnMoreActivity extends BaseActivity {
    private final static int viewResourceId = R.layout.view_learn_more_container;
    private int viewFlatResourceId = R.layout.view_learn_more_container_flat;
    protected RecyclerView recyclerView;
    private View mylinearlayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn_more);
        setUpActionBarWithTitle(getActionBarTitle()).setDisplayHomeAsUpEnabled(true);
        injectDependency();
        setupRecycler();
        setupButtons();
        ViewUtilities.scrollToTop(this.<NestedScrollView>findViewById(R.id.scrollview));
    }

    protected void setupRecycler(){
        recyclerView = findViewById(R.id.main_recyclerview);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(createAdapter());

        ViewUtilities.scrollToTop(recyclerView);
    }

    private void setupButtons() {
        ButtonConfigurations configurations = getButtonConfigurations();
        setupButton(configurations.button1, this.<Button>findViewById(R.id.learn_more_button1));
    }

    private void setupButton(ButtonConfiguration config, Button button) {
        if (config == null || !config.visible)
            return;

        button.setVisibility(View.VISIBLE);
        button.setText(config.text);
        button.setOnClickListener(config.clickListener);
    }

    protected ButtonConfigurations getButtonConfigurations() {
        return new ButtonConfigurations();
    }

    protected void injectDependency() {
        getDependencyInjector().inject(this);
    }

    private ContainersRecyclerViewAdapter createAdapter() {
        return new ContainersRecyclerViewAdapter(createChildAdapters());
    }

    private ArrayList<GenericRecyclerViewAdapter> createChildAdapters() {
        ArrayList<GenericRecyclerViewAdapter> adapters = new ArrayList<>();

        GenericRecyclerViewAdapter settingsAdapter = createSettingsAdapter();

        GenericRecyclerViewAdapter customViewAdapter = createCustomViewAdapter();

        boolean isFlatView = (settingsAdapter == null)&&(customViewAdapter==null);
        setBackgroundColor(isFlatView);

        adapters.add(createLearnMoreAdapter(isFlatView));

        if (settingsAdapter != null) {
            adapters.add(settingsAdapter);
        }

        if (customViewAdapter != null) {
            adapters.add(customViewAdapter);
        }
        return adapters;
    }

    private GenericRecyclerViewAdapter createLearnMoreAdapter(boolean isFlatView) {
        return new LearnMoreViewAdapter<>(this,
                createListData(),
                new LearnMoreContainerViewHolder.Factory(),
                getHeaderTitle(),
                getHeaderSubTitle(),
                isFlatView ? viewFlatResourceId : viewResourceId);
    }

    private void setBackgroundColor(boolean isFlatView) {
        findViewById(R.id.main_recyclerview).setBackgroundColor(ContextCompat.getColor(this,
                isFlatView ? android.R.color.white : R.color.active_rewards_divider_background));
    }

    protected GenericRecyclerViewAdapter createSettingsAdapter() {
        return null;
    }

    protected GenericRecyclerViewAdapter createCustomViewAdapter() {
        return null;
    }

    @StringRes
    protected abstract int getActionBarTitle();

    protected abstract String getHeaderTitle();

    protected abstract String getHeaderSubTitle();

    protected abstract List<List<LearnMoreItem>> createListData();

    public static class ButtonConfigurations {
        public ButtonConfiguration button1 = null;
    }

    public static class ButtonConfiguration {
        public final boolean visible;
        public final View.OnClickListener clickListener;
        public final int text;

        public ButtonConfiguration(int text, View.OnClickListener clickListener) {
            this.visible = true;
            this.clickListener = clickListener;
            this.text = text;
        }
    }

    public void setViewResourceId(int viewResourceId) {
        this.viewFlatResourceId = viewResourceId;
    }

    public void setLayout(View layout) {
        this.mylinearlayout = layout;
    }

    public View getLayout() {
        return this.mylinearlayout;
    }

    protected boolean showDisclaimer(){
        return false;
    }
}
