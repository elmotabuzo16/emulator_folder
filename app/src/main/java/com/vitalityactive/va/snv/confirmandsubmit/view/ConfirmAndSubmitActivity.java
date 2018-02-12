package com.vitalityactive.va.snv.confirmandsubmit.view;


import android.app.DatePickerDialog;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.vitalityactive.va.BasePresentedActivity;
import com.vitalityactive.va.R;
import com.vitalityactive.va.dependencyinjection.DependencyInjector;
import com.vitalityactive.va.snv.confirmandsubmit.model.ConfirmAndSubmitItemUI;
import com.vitalityactive.va.snv.confirmandsubmit.presenter.ConfirmAndSubmitPresenter;
import com.vitalityactive.va.snv.confirmandsubmit.viewholder.ConfirmAndSubmitAdapter;
import com.vitalityactive.va.snv.dto.ConfirmAndSubmitItemDTO;
import com.vitalityactive.va.snv.dto.EventTypeDto;
import com.vitalityactive.va.uicomponents.ButtonBarConfigurator;
import com.vitalityactive.va.utilities.date.LocalDate;
import com.vitalityactive.va.utilities.date.formatting.DateFormattingUtilities;

import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;

public class ConfirmAndSubmitActivity extends BasePresentedActivity<ConfirmAndSubmitPresenter.UserInterface, ConfirmAndSubmitPresenter<ConfirmAndSubmitPresenter.UserInterface>>
        implements ButtonBarConfigurator.OnClickListener, ConfirmAndSubmitPresenter.UserInterface, ConfirmAndSubmitAdapter.Callback {

    @Inject
    ConfirmAndSubmitPresenter presenter;

    Drawable mIcon;
    private RecyclerView screeningsRecyclerView;
    private RecyclerView vaccinationsRecyclerView;
    private ConfirmAndSubmitAdapter screeningAdapter;
    private ConfirmAndSubmitAdapter vaccinationAdapter;

    @Inject
    DateFormattingUtilities dateFormattingUtilities;

    @Override
    protected void injectDependencies(DependencyInjector dependencyInjector) {
        dependencyInjector.inject(this);
    }

    @Override
    protected ConfirmAndSubmitPresenter.UserInterface getUserInterface() {
        return this;
    }

    @Override
    protected ConfirmAndSubmitPresenter getPresenter() {
        return presenter;
    }

    @Override
    protected void create(@Nullable Bundle savedInstanceState) {
        super.create(savedInstanceState);
        setContentView(R.layout.activity_snv_confirm_and_submit);

        setupRecyclerView();

        setUpActionBarWithTitle(R.string.SV_onboarding_section_2_title_1004)
                .setDisplayHomeAsUpEnabled(true);

        setupButtonBar()
                .setForwardButtonTextToNext()
                .setForwardButtonOnClick(this);

        mIcon = ContextCompat.getDrawable(this, R.drawable.screenings);
        mIcon.setColorFilter(ContextCompat.getColor(this, R.color.jungle_green), PorterDuff.Mode.SRC_ATOP);
        ImageView imageViewS = findViewById(R.id.confirm_and_submit_sicon);
        imageViewS.setImageDrawable(mIcon);

        mIcon = ContextCompat.getDrawable(this, R.drawable.vaccinations);
        mIcon.setColorFilter(ContextCompat.getColor(this, R.color.jungle_green), PorterDuff.Mode.SRC_ATOP);
        ImageView imageViewV = findViewById(R.id.confirm_and_submit_vicon);
        imageViewV.setImageDrawable(mIcon);
    }

    private void setupRecyclerView() {
        screeningsRecyclerView = findViewById(R.id.confirm_submit_recycler_screening);
        screeningsRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        screeningsRecyclerView.setNestedScrollingEnabled(false);

        vaccinationsRecyclerView = findViewById(R.id.confirm_submit_recycler_vaccination);
        vaccinationsRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        vaccinationsRecyclerView.setNestedScrollingEnabled(false);
    }

    @Override
    public void updateListItems(final List<EventTypeDto> screeningsDTO, final List<EventTypeDto> vaccinationsDTO, final List<ConfirmAndSubmitItemDTO> screeningsItemsUi, final List<ConfirmAndSubmitItemDTO> vaccinationItemsUi) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                screeningAdapter = new ConfirmAndSubmitAdapter(ConfirmAndSubmitActivity.this, screeningsDTO, screeningsItemsUi, ConfirmAndSubmitAdapter.TYPE.SCREENINGS);
                screeningsRecyclerView = findViewById(R.id.confirm_submit_recycler_screening);
                screeningsRecyclerView.setAdapter(screeningAdapter);
                vaccinationAdapter = new ConfirmAndSubmitAdapter(ConfirmAndSubmitActivity.this, vaccinationsDTO, vaccinationItemsUi, ConfirmAndSubmitAdapter.TYPE.VACCINATIONS);
                vaccinationsRecyclerView = findViewById(R.id.confirm_submit_recycler_vaccination);
                vaccinationsRecyclerView.setAdapter(vaccinationAdapter);

                adjustRecyclerViewHeight(ConfirmAndSubmitAdapter.TYPE.SCREENINGS, 0);
                adjustRecyclerViewHeight(ConfirmAndSubmitAdapter.TYPE.VACCINATIONS, 0);

                screeningsRecyclerView.refreshDrawableState();
                screeningsRecyclerView.invalidate();
                vaccinationsRecyclerView.refreshDrawableState();
                vaccinationsRecyclerView.invalidate();
            }
        });
    }

    @Override
    public void adjustRecyclerViewHeight(ConfirmAndSubmitAdapter.TYPE type, int numItemsChecked) {
        int heightOffset = numItemsChecked * getResources().getDimensionPixelSize(R.dimen.item_divider_inset) + getResources().getDimensionPixelSize(R.dimen.activity_horizontal_margin_small);

        if (type == ConfirmAndSubmitAdapter.TYPE.SCREENINGS) {
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) screeningsRecyclerView.getLayoutParams();
            params.height = screeningAdapter.getItemCount() * getResources().getDimensionPixelSize(R.dimen.active_rewards_menu_item_height) + heightOffset;
            screeningsRecyclerView.setLayoutParams(params);
        } else if (type == ConfirmAndSubmitAdapter.TYPE.VACCINATIONS) {
            LinearLayout.LayoutParams paramsVaccination = (LinearLayout.LayoutParams) vaccinationsRecyclerView.getLayoutParams();
            paramsVaccination.height = vaccinationAdapter.getItemCount() * getResources().getDimensionPixelSize(R.dimen.active_rewards_menu_item_height) + heightOffset;
            vaccinationsRecyclerView.setLayoutParams(paramsVaccination);
        }
    }

    @Override
    public void updateButtonBarStatus(boolean status){
        setupButtonBar().setForwardButtonEnabled(status);
    }

    @Override
    public String getFormattedDate(LocalDate date) {
        return  DateFormattingUtilities.formatDateMonthAbbreviatedYear(getApplicationContext(), date);
    }

    @Override
    public void navigateAway() {
        List<ConfirmAndSubmitItemUI> screeningItems = screeningAdapter.getAllConfirmAndSubmitItems();
        List<ConfirmAndSubmitItemUI> vaccinationItems = vaccinationAdapter.getAllConfirmAndSubmitItems();

        presenter.persistConfirmAndSubmitItems(screeningItems, vaccinationItems);
        navigationCoordinator.navigateAfterNextIsTappedConfirmAndSubmit(this);
    }

    public void showDatePickerDialog(final DatePickerDialog.OnDateSetListener myDateListener) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(this, R.style.AppTheme_KnowYourHealth_datePicker, myDateListener, year, month, day);
        DatePicker datePicker = dialog.getDatePicker();

        Calendar calendarToday = Calendar.getInstance();//get the current day
        datePicker.setMaxDate(calendarToday.getTimeInMillis());//set thecurrent day as the max date
        dialog.show();
    }

    @Override
    public void onButtonBarForwardClicked() {
        presenter.submit();
    }

    @Override
    public LayoutInflater getLayoutInflaterForAdapter() {
        return getLayoutInflater();
    }
}
