package com.vitalityactive.va.snv.learnmore;

/**
 * Created by stephen.rey.w.avila on 12/4/2017.
 */

import android.app.Activity;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vitalityactive.va.BasePresentedActivity;
import com.vitalityactive.va.R;
import com.vitalityactive.va.dependencyinjection.DependencyInjector;
import com.vitalityactive.va.snv.dto.EventTypeDto;
import com.vitalityactive.va.snv.learnmore.presenter.SnvLearnMorePresenter;
import com.vitalityactive.va.snv.shared.SnvConstants;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

public class ScreeningsActivity extends BasePresentedActivity<SnvLearnMorePresenter.UserInterface, SnvLearnMorePresenter<SnvLearnMorePresenter.UserInterface>> implements SnvLearnMorePresenter.UserInterface {

    public static final String KEY = "HEALTH_ACTIONS_ACTIVITY_KEY";
    RecyclerView recyclerView;
    private Drawable mIcon;
    private String action;
    NumberFormat numberFormat;

    @Inject
    SnvLearnMorePresenter screeningsPresenter;

    @Override
    protected void create(Bundle savedInstanceState) {
        super.create(savedInstanceState);
        setContentView(R.layout.sv_activity_snv_screenings);

        mIcon= ContextCompat.getDrawable(this, R.drawable.vhc_points_pending);
        mIcon.setColorFilter(ContextCompat.getColor(this, R.color.button_bar_gray), PorterDuff.Mode.SRC_ATOP);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView = findViewById(R.id.snv_screenings_recycler_view);
        recyclerView.setLayoutManager(linearLayoutManager);

        action = getIntent().getStringExtra(KEY);
        if (SnvConstants.HEALTH_ACTION_SCREENINGS.equalsIgnoreCase(action)) {
            setUpActionBarWithTitle(getString(R.string.SV_screenings_title_1012)).setDisplayHomeAsUpEnabled(true);
        } else if (SnvConstants.HEALTH_ACTION_VACCINATIONS.equalsIgnoreCase(action)) {
            setUpActionBarWithTitle(getString(R.string.SV_vaccinations_title_1013)).setDisplayHomeAsUpEnabled(true);
        }

        numberFormat = NumberFormat.getNumberInstance(Locale.getDefault());
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
    protected void injectDependencies(DependencyInjector dependencyInjector) {
        dependencyInjector.inject(this);
    }

    @Override
    protected SnvLearnMorePresenter.UserInterface getUserInterface() {
        return this;
    }

    @Override
    protected SnvLearnMorePresenter getPresenter() {
        return screeningsPresenter;
    }

    @Override
    public void updateListItems(final List<EventTypeDto> eventTypeDtos) {
        this.runOnUiThread(() -> {
            recyclerView = findViewById(R.id.snv_screenings_recycler_view);
            recyclerView.setAdapter(new ScreeningsAdapter(eventTypeDtos));
            recyclerView.refreshDrawableState();
            recyclerView.invalidate();
        });
    }

    private Activity getActivity() {
        return this;
    }

    @Override
    public String getAction() {
        return action;
    }

    private class ScreeningsAdapter  extends RecyclerView.Adapter<ScreeningsActivity.ScreeningsAdapter.ViewHolder> {

        private final String TAG = ScreeningsAdapter.class.getName();
        private List<ScreeningsItem> objectList;


        public ScreeningsAdapter(List<EventTypeDto> eventTypeDtos) {
            List<EventTypeDto> noEventsZeroEarnPoints = new ArrayList<EventTypeDto>();
            List<EventTypeDto> nonZeroEarnPoints = new ArrayList<EventTypeDto>();
            List<EventTypeDto> zeroEarnPoints = new ArrayList<EventTypeDto>();

            for(EventTypeDto eventTypeDto: eventTypeDtos) {
                if (eventTypeDto.getTotalPotentialPoints() > 0) {
                    if (eventTypeDto.getEvent().isEmpty() && eventTypeDto.getTotalEarnedPoints() == 0) {
                        noEventsZeroEarnPoints.add(eventTypeDto);
                    } else if (eventTypeDto.getTotalEarnedPoints() > 0) {
                        nonZeroEarnPoints.add(eventTypeDto);
                    } else if (eventTypeDto.getTotalEarnedPoints() == 0) {
                        zeroEarnPoints.add(eventTypeDto);
                    }
                }
            }
            Collections.sort(noEventsZeroEarnPoints);
            Collections.sort(nonZeroEarnPoints);
            Collections.sort(zeroEarnPoints);

            List<EventTypeDto> finalList = new ArrayList<EventTypeDto>();
            finalList.addAll(noEventsZeroEarnPoints);
            finalList.addAll(nonZeroEarnPoints);
            finalList.addAll(zeroEarnPoints);

            objectList = new ArrayList<ScreeningsItem>();
            for(EventTypeDto eventTypeDto: finalList) {
                ScreeningsItem h = new ScreeningsItem(eventTypeDto.getTypeName(), eventTypeDto.getTotalPotentialPoints());
                objectList.add(h);
            }
        }

        @Override
        public ScreeningsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view;
            view = getLayoutInflater().inflate(R.layout.sv_screenings_list_row, parent, false);

            return new ScreeningsAdapter.ViewHolder(view);
        }


        @Override
        public void onBindViewHolder(ScreeningsAdapter.ViewHolder holder, int position) {
            ScreeningsItem object = objectList.get(position);

            holder.title.setText(object.getDescription());
            holder.toolbar_title = object.getDescription();
        }

        @Override
        public int getItemCount() {
            return objectList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            private final TextView title;
            private String toolbar_title="";

            public ViewHolder(View view) {
                super(view);
                title = view.findViewById(R.id.ItemTitleTextView);
                view.setOnClickListener(view1 -> navigationCoordinator.navigateToSnvListDescription(getActivity(), toolbar_title));
            }
        }
    }
}
