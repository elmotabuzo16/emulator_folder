package com.vitalityactive.va.snv.learnmore;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.vitalityactive.va.BasePresentedActivity;
import com.vitalityactive.va.R;
import com.vitalityactive.va.dependencyinjection.DependencyInjector;
import com.vitalityactive.va.snv.dto.EventTypeDto;
import com.vitalityactive.va.snv.learnmore.presenter.SnvLearnMorePresenter;
import com.vitalityactive.va.utilities.ViewUtilities;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by stephen.rey.w.avila on 12/6/2017.
 */

public class SnvListDescriptionActivity extends BasePresentedActivity<SnvLearnMorePresenter.UserInterface, SnvLearnMorePresenter<SnvLearnMorePresenter.UserInterface>> implements SnvLearnMorePresenter.UserInterface {


    @Inject
    SnvLearnMorePresenter screeningsPresenter;

    public static final String KEY = "HEALTH_ACTIONS_ACTIVITY_KEY";
    RecyclerView recyclerView;
    private String action;
    NumberFormat numberFormat;

    @Override
    protected void create(Bundle savedInstanceState) {
        super.create(savedInstanceState);
        setContentView(R.layout.sv_activity_snv_descriptions);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView = findViewById(R.id.snv_screenings_recycler_view);
        recyclerView.setLayoutManager(linearLayoutManager);

        action = getIntent().getStringExtra(KEY);
        setUpActionBarWithTitle(action).setDisplayHomeAsUpEnabled(true);
    }

    protected List<ScreeningListDescriptionItem> createListData() {

        List<ScreeningListDescriptionItem> items = new ArrayList<>();

        items.add(new ScreeningListDescriptionItem("What is a "+action+"?",getString(R.string.SV_descriptions_message_1_9999),R.drawable.screenings,
                null, R.color.colorBlack, true));


        items.add(new ScreeningListDescriptionItem("What do I need to know before the test?",getString(R.string.SV_descriptions_message_2_9999),R.drawable.benefit_guide,
                null, R.color.jungle_green, true));

        items.add(new ScreeningListDescriptionItem("How do I earn points for getting a "+action+" test?",getString(R.string.SV_descriptions_message_3_9999),R.drawable.points_16,
                null, R.color.jungle_green, true));

        return items;
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
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                recyclerView = findViewById(R.id.snv_screenings_recycler_view);
                recyclerView.setAdapter(new SnvListDescriptionActivity.ScreeningsAdapter(eventTypeDtos));
                recyclerView.refreshDrawableState();
                recyclerView.invalidate();
            }
        });
    }

    @Override
    public String getAction() {
        return action;
    }

    private class ScreeningsAdapter  extends RecyclerView.Adapter<SnvListDescriptionActivity.ScreeningsAdapter.ViewHolder> {

        private final String TAG = SnvListDescriptionActivity.ScreeningsAdapter.class.getName();
        private List<ScreeningListDescriptionItem> objectList;

        public ScreeningsAdapter(List<EventTypeDto> eventTypeDtos) {
            objectList =  createListData();
        }

        @Override
        public SnvListDescriptionActivity.ScreeningsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view;
            view = getLayoutInflater().inflate(R.layout.snv_description_list_item, parent, false);

            return new SnvListDescriptionActivity.ScreeningsAdapter.ViewHolder(view);
        }


        @Override
        public void onBindViewHolder(SnvListDescriptionActivity.ScreeningsAdapter.ViewHolder holder, int position) {

            ScreeningListDescriptionItem object = objectList.get(position);

            holder.title.setText(object.getTitle());
            holder.subtitle.setText(object.getSubtitle());
            if(object.getIconResourceId() != 0) {
                Drawable drawable = ViewUtilities.tintDrawable(ContextCompat.getDrawable(holder.itemView.getContext(), object.getIconResourceId()),
                        ContextCompat.getColor(holder.itemView.getContext(), object.getTintColor()));

                holder.icon.setColorFilter(ContextCompat.getColor(holder.itemView.getContext(), R.color.jungle_green));
                holder.icon.setImageDrawable(drawable);

            }


        }

        @Override
        public int getItemCount() {
            return objectList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            private final TextView title;
            private final TextView subtitle;
            private final ImageView icon;

            public ViewHolder(View view) {
                super(view);
                title = view.findViewById(R.id.title);
                subtitle = view.findViewById(R.id.subtitle);
                icon = view.findViewById(R.id.icon);
            }
        }
    }
}
