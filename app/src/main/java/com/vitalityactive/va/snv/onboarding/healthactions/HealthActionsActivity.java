package com.vitalityactive.va.snv.onboarding.healthactions;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.vitalityactive.va.BasePresentedActivity;
import com.vitalityactive.va.R;
import com.vitalityactive.va.dependencyinjection.DependencyInjector;
import com.vitalityactive.va.snv.dto.EventTypeDto;
import com.vitalityactive.va.snv.onboarding.presenter.HealthActionsPresenter;
import com.vitalityactive.va.snv.shared.SnvConstants;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

public class HealthActionsActivity extends BasePresentedActivity<HealthActionsPresenter.UserInterface, HealthActionsPresenter<HealthActionsPresenter.UserInterface>> implements HealthActionsPresenter.UserInterface {

    public static final String KEY = "HEALTH_ACTIONS_ACTIVITY_KEY";
    RecyclerView recyclerView;
    TextView textView;

    private Drawable mIcon;
    private String action;
    NumberFormat numberFormat;

    @Inject
    HealthActionsPresenter healthActionsPresenter;

    @Override
    protected void create(Bundle savedInstanceState) {
        super.create(savedInstanceState);
        setContentView(R.layout.sv_activity_health_actions);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        textView = findViewById(R.id.sv_title_details);
        recyclerView = findViewById(R.id.health_actions_recycler_view);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setBackgroundColor(ContextCompat.getColor(this, android.R.color.white));
        action = getIntent().getStringExtra(KEY);

        if (SnvConstants.HEALTH_ACTION_SCREENINGS.equalsIgnoreCase(action)) {
            setUpActionBarWithTitle(getString(R.string.SV_screenings_title_1012)).setDisplayHomeAsUpEnabled(true);
            textView.setText(R.string.SV_screenings_detail_message_1014);
        } else if (SnvConstants.HEALTH_ACTION_VACCINATIONS.equalsIgnoreCase(action)) {
            setUpActionBarWithTitle(getString(R.string.SV_vaccinations_title_1013)).setDisplayHomeAsUpEnabled(true);
            textView.setText(R.string.SV_vaccinations_detail_message_1020);
        }
        numberFormat = NumberFormat.getNumberInstance(Locale.getDefault());
    }

    @Override
    protected void injectDependencies(DependencyInjector dependencyInjector) {
        dependencyInjector.inject(this);
    }

    @Override
    protected HealthActionsPresenter.UserInterface getUserInterface() {
        return this;
    }

    @Override
    protected HealthActionsPresenter getPresenter() {
        return healthActionsPresenter;
    }

    @Override
    public void updateListItems(final List<EventTypeDto> eventTypeDtos) {
        this.runOnUiThread(() -> {
            recyclerView = findViewById(R.id.health_actions_recycler_view);
            recyclerView.setAdapter(new HealthActionsAdapter(eventTypeDtos));
            recyclerView.refreshDrawableState();
            recyclerView.invalidate();
        });
    }

    @Override
    public String getAction() {
        return action;
    }

    private class HealthActionsAdapter extends RecyclerView.Adapter<HealthActionsAdapter.ViewHolder> {

        private final String TAG = HealthActionsAdapter.class.getName();
        private List<HealthActionsItems> objectList;
        private String pointsStr;


        public HealthActionsAdapter(List<EventTypeDto> eventTypeDtos) {
            List<EventTypeDto> noEventsZeroEarnPoints = new ArrayList<EventTypeDto>();
            List<EventTypeDto> nonZeroEarnPoints = new ArrayList<EventTypeDto>();
            List<EventTypeDto> zeroEarnPoints = new ArrayList<EventTypeDto>();

            for (EventTypeDto eventTypeDto : eventTypeDtos) {
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

            Comparator<EventTypeDto> eventTypeComparator = new EventTypeComparator();
            Collections.sort(noEventsZeroEarnPoints, eventTypeComparator);
            Collections.sort(nonZeroEarnPoints, eventTypeComparator);
            Collections.sort(zeroEarnPoints, eventTypeComparator);

            List<EventTypeDto> finalList = new ArrayList<EventTypeDto>();
            finalList.addAll(noEventsZeroEarnPoints);
            finalList.addAll(nonZeroEarnPoints);
            finalList.addAll(zeroEarnPoints);

            objectList = new ArrayList<HealthActionsItems>();
            for (EventTypeDto eventTypeDto : finalList) {
                HealthActionsItems h = new HealthActionsItems(eventTypeDto.getTypeName(), eventTypeDto.getTotalPotentialPoints());
                objectList.add(h);
            }
            pointsStr = getText(R.string.home_card_card_potential_points_message_97).toString();
        }

        @Override
        public HealthActionsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view;
            view = getLayoutInflater().inflate(R.layout.sv_health_actions_list_row, parent, false);
            return new HealthActionsAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(HealthActionsAdapter.ViewHolder holder, int position) {
            HealthActionsItems object = objectList.get(position);

            holder.title.setText(object.getDescription());
            holder.points.setText(pointsStr.replaceFirst("%s", numberFormat.format(object.getPoints())));
        }

        @Override
        public int getItemCount() {
            return objectList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            private final TextView title;
            private final TextView points;
            private final ImageView imageView;

            public ViewHolder(View view) {
                super(view);
                imageView = view.findViewById(R.id.ItemPointsImageView);
                imageView.setImageDrawable(mIcon);
                title = view.findViewById(R.id.ItemTitleTextView);
                points = view.findViewById(R.id.ItemPointsTextView);
            }
        }
    }

    private static class EventTypeComparator implements Comparator<EventTypeDto> {
        @Override
        public int compare(EventTypeDto o1, EventTypeDto o2) {
            final String typeName1 = o1.getTypeName();
            final String typeName2 = o2.getTypeName();
            return typeName1.compareTo(typeName2);
        }
    }
}
