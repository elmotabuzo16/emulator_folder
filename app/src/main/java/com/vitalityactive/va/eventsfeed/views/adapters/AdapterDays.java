package com.vitalityactive.va.eventsfeed.views.adapters;

import android.content.Context;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vitalityactive.va.EmptyStateViewHolder;
import com.vitalityactive.va.R;
import com.vitalityactive.va.activerewards.viewholder.TitleViewHolder;
import com.vitalityactive.va.constants.PointsEntryCategory;
import com.vitalityactive.va.eventsfeed.EventsFeedDay;
import com.vitalityactive.va.eventsfeed.EventsFeedIconProvider;
import com.vitalityactive.va.eventsfeed.presentation.EventsFeedMonthPresenter;
import com.vitalityactive.va.eventsfeed.data.dto.EventsFeedCategoryDTO;
import com.vitalityactive.va.utilities.ViewUtilities;
import com.vitalityactive.va.utilities.date.formatting.DateFormattingUtilities;

import java.util.List;

/**
 * Created by jayellos on 11/22/17.
 */

public class AdapterDays extends RecyclerView.Adapter{
    private Context context;
    @NonNull private List<EventsFeedDay> days;
    private LayoutInflater inflater;
    @ColorInt private int globalTintColor;
    private EventsFeedMonthPresenter presenter;
    private DateFormattingUtilities dateFormattingUtilities;
    private EmptyStateViewHolder.EmptyStatusButtonClickedListener emptyStateListener;
    private EventsFeedIconProvider iconProvider;


    public AdapterDays(Context context, @NonNull List<EventsFeedDay> days, LayoutInflater inflater,
                       int globalTintColor, EventsFeedMonthPresenter presenter,
                       DateFormattingUtilities dateFormattingUtilities, EmptyStateViewHolder.EmptyStatusButtonClickedListener emptyStateListener,
                       EventsFeedIconProvider iconProvider) {
        this.context = context;
        this.days = days;
        this.inflater = inflater;
        this.globalTintColor = globalTintColor;
        this.presenter = presenter;
        this.dateFormattingUtilities = dateFormattingUtilities;
        this.emptyStateListener = emptyStateListener;
        this.iconProvider = iconProvider;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(viewType, parent, false);
        if (viewType == R.layout.events_feed_day_item) {
            return new DayViewHolder(itemView, globalTintColor);
        }
        if (viewType == R.layout.events_feed_no_more_events_item) {
            return new TitleViewHolder(itemView);
        }
        return new EmptyStateViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (isLastItem(position)) {
            ((TitleViewHolder)holder).bindWith(getNoMoreItemsText());
        } else if (hasData()) {
            ((DayViewHolder) holder).bindWith(days.get(position));
        } else {
            EmptyState emptyState = getEmptyState();
            EmptyStateViewHolder emptyStateViewHolder = (EmptyStateViewHolder) holder;
            emptyStateViewHolder.setButtonColor(globalTintColor);
            emptyStateViewHolder.setup(emptyState.title, emptyState.subtitle, emptyState.buttonTitle, emptyStateListener);
        }
    }

    private String getNoMoreItemsText() {
        EventsFeedCategoryDTO selectedCategory = presenter.getSelectedCategory();
        if (isAssessmentSelected(selectedCategory)) {
            return getNoMoreItemsText(context.getString(R.string.PM_footnote_empty_state_assessment_message_545));
        } else if (isHealthyFoodSelected(selectedCategory)) {
            return getNoMoreItemsText(context.getString(R.string.PM_footnote_empty_state_nutrition_message_546));
        } else if (isFitnessSelected(selectedCategory)) {
            return getNoMoreItemsText(context.getString(R.string.PM_footnote_empty_state_fitness_message_548));
        } else if (isScreeningSelected(selectedCategory)) {
            return getNoMoreItemsText(context.getString(R.string.PM_footnote_empty_state_screening_message_547));
        } else if (isOtherSelected(selectedCategory)) {
            return getNoMoreItemsText(context.getString(R.string.PM_footnote_empty_state_other_message_551));
        }
        return getNoMoreItemsText(context.getString(R.string.PM_footnote_state_no_more_activity_message_610));
    }

    private String getNoMoreItemsText(String formatString) {
        return String.format(formatString,
                dateFormattingUtilities.formatMonthYear(days.get(0).getDate()));
    }

    private EmptyState getEmptyState() {
        EventsFeedCategoryDTO selectedCategory = presenter.getSelectedCategory();
        if (isAssessmentSelected(selectedCategory)) {
            return new EmptyState(R.string.PM_empty_state_assessment_title_528, R.string.PM_empty_state_assessment_message_529, R.string.help_button_title_141);
        } else if (isHealthyFoodSelected(selectedCategory)) {
            return new EmptyState(R.string.PM_empty_state_nutrition_title_530, R.string.PM_empty_state_nutrition_message_531, R.string.help_button_title_141);
        } else if (isFitnessSelected(selectedCategory)) {
            return new EmptyState(R.string.PM_empty_state_fitness_title_534, R.string.PM_empty_state_fitness_message_535, R.string.help_button_title_141);
        } else if (isScreeningSelected(selectedCategory)) {
            return new EmptyState(R.string.PM_empty_state_screening_title_532, R.string.PM_empty_state_screening_message_533, R.string.help_button_title_141);
        }
        return new EmptyState(R.string.Settings_events_empty_error_title_937, R.string.Settings_events_empty_error_message_938, R.string.help_button_title_141);
    }

    private boolean isScreeningSelected(EventsFeedCategoryDTO selectedCategory) {
        return isSelected(selectedCategory, PointsEntryCategory._SCREENING);
    }

    private boolean isFitnessSelected(EventsFeedCategoryDTO selectedCategory) {
        return isSelected(selectedCategory, PointsEntryCategory._FITNESS);
    }

    private boolean isHealthyFoodSelected(EventsFeedCategoryDTO selectedCategory) {
        return isSelected(selectedCategory, PointsEntryCategory._HEALTHYFOOD);
    }

    private boolean isAssessmentSelected(EventsFeedCategoryDTO selectedCategory) {
        return isSelected(selectedCategory, PointsEntryCategory._ASSESSMENT);
    }

    private boolean isOtherSelected(EventsFeedCategoryDTO selectedCategory) {
        return selectedCategory.isOther();
    }

    private boolean isSelected(EventsFeedCategoryDTO selectedCategory, int categoryTypeKey) {
        return selectedCategory.getTypeKey() == categoryTypeKey;
    }

    @Override
    public int getItemCount() {
        return Math.max(1, days.size() + 1);
    }

    @Override
    public int getItemViewType(int position) {
        if (isLastItem(position)) {
            return R.layout.events_feed_no_more_events_item;
        }
        if (hasData()) {
            return R.layout.events_feed_day_item;
        }
        return R.layout.empty_state;
    }

    private boolean isLastItem(int position) {
        return position == days.size() && hasData();
    }

    private boolean hasData() {
        return !days.isEmpty();
    }

    public void setData(@NonNull List<EventsFeedDay> days) {
        this.days = days;
    }

    private class EmptyState {
        private final int title;
        private final int subtitle;
        private final int buttonTitle;
        EmptyState(int title, int subtitle, int buttonTitle) {
            this.title = title;
            this.subtitle = subtitle;
            this.buttonTitle = buttonTitle;
        }
    }

    private class DayViewHolder extends RecyclerView.ViewHolder {
        private final RecyclerView recyclerView;
        private final int globalTintColor;
        private final TextView title;

        DayViewHolder(View itemView, int globalTintColor) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            recyclerView = itemView.findViewById(R.id.recycler_view);
            this.globalTintColor = globalTintColor;
        }

        public void bindWith(EventsFeedDay day) {
            title.setText(dateFormattingUtilities.formatDateMonthAbbreviatedYear(day.getDate()));
            recyclerView.setAdapter(new AdapterEventItems(day, globalTintColor, inflater, iconProvider));
            ViewUtilities.addDividers(context, recyclerView, context.getResources().getDimensionPixelSize(R.dimen.item_divider_inset));
        }
    }
}
