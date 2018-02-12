package com.vitalityactive.va.snv.history.views;

import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.vitalityactive.va.BasePresentedActivity;
import com.vitalityactive.va.R;
import com.vitalityactive.va.appconfig.AppConfigRepository;
import com.vitalityactive.va.dependencyinjection.DependencyInjector;
import com.vitalityactive.va.snv.dto.HistoryDetailDto;
import com.vitalityactive.va.snv.dto.ListHistoryListDto;
import com.vitalityactive.va.snv.history.presenter.ScreenAndVaccinationHistoryPresenter;
import com.vitalityactive.va.snv.history.views.adapters.SNVHistoryAdapter;
import com.vitalityactive.va.utilities.ViewUtilities;
import com.vitalityactive.va.vhc.summary.PresentableProof;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by dharel.h.rosell on 12/4/2017.
 */

public class SNVHistoryActivity extends BasePresentedActivity<ScreenAndVaccinationHistoryPresenter.UserInterface,
        ScreenAndVaccinationHistoryPresenter<ScreenAndVaccinationHistoryPresenter.UserInterface>> implements ScreenAndVaccinationHistoryPresenter.UserInterface {

    @Inject
    ScreenAndVaccinationHistoryPresenter screenAndVaccinationHistoryPresenter;

    @Inject
    AppConfigRepository appConfigRepository;

    RecyclerView historyRecyclerView;
    TextView noHistoryTitle;
    TextView noHistorySubmessage;
    CardView cardView;


    @Override
    protected void create(Bundle savedInstanceState) {
        super.create(savedInstanceState);
        setContentView(R.layout.activity_snv_history);

        setUpActionBarWithTitle(R.string.history_button_140)
                .setDisplayHomeAsUpEnabled(true);

        screenAndVaccinationHistoryPresenter.setContext(this);

        setViewRecyclerView();
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

    public void setViewRecyclerView(){
            historyRecyclerView = findViewById(R.id.recycler_view);
            noHistoryTitle = findViewById(R.id.snv_no_history_title);
            noHistorySubmessage = findViewById(R.id.snv_no_history_message);
            cardView = findViewById(R.id.history_card_view);
    }

    public void setAdapterData(List<ListHistoryListDto> historyListDtos){
            SNVHistoryAdapter historyAdapter = new SNVHistoryAdapter(this, historyListDtos,
                    getLayoutInflater());
            historyAdapter.setNavigationCoordinator(navigationCoordinator);
            historyRecyclerView.setAdapter(historyAdapter);
    }

    public void hideLList(){
        this.cardView.setVisibility(View.GONE);
        this.noHistoryTitle.setVisibility(View.VISIBLE);
        this.noHistorySubmessage.setVisibility(View.VISIBLE);
    }

    @Override
    protected void injectDependencies(DependencyInjector dependencyInjector) {
        dependencyInjector.inject(this);
    }

    @Override
    protected ScreenAndVaccinationHistoryPresenter.UserInterface getUserInterface() {
        return this;
    }

    @Override
    protected ScreenAndVaccinationHistoryPresenter getPresenter() {
        return screenAndVaccinationHistoryPresenter;
    }

    @Override
    public void getHistoryListDto(List<ListHistoryListDto> historyListDtos) {
        if(historyListDtos.size() != 0){
            this.cardView.setVisibility(View.VISIBLE);
            this.noHistoryTitle.setVisibility(View.GONE);
            this.noHistorySubmessage.setVisibility(View.GONE);
            setAdapterData(historyListDtos);
        } else {
            hideLList();
        }


    }

    @Override
    public void showEmptyListMessage() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                hideLList();
            }
        });
    }

}
