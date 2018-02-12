package com.vitalityactive.va.snv.history.views;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;

import com.vitalityactive.va.BasePresentedActivity;
import com.vitalityactive.va.R;
import com.vitalityactive.va.dependencyinjection.DependencyInjector;
import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.events.EventListener;
import com.vitalityactive.va.snv.dto.ListHistoryListDto;
import com.vitalityactive.va.snv.history.presenter.ScreenAndVaccinationHistoryDetailPresenter;
import com.vitalityactive.va.snv.history.repository.ScreenAndVaccinationHistoryRepository;
import com.vitalityactive.va.snv.history.views.adapters.SNVHistoryMainAdapter;
import com.vitalityactive.va.snv.history.views.model.SNVHistoryPresentableProof;
import com.vitalityactive.va.snv.history.views.model.SNVMainAdapterModel;
import com.vitalityactive.va.uicomponents.AlertDialogFragment;
import com.vitalityactive.va.utilities.ViewUtilities;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by dharel.h.rosell on 12/6/2017.
 */

public class SNVHistoryDetailActivity extends BasePresentedActivity<ScreenAndVaccinationHistoryDetailPresenter.UI,
        ScreenAndVaccinationHistoryDetailPresenter<ScreenAndVaccinationHistoryDetailPresenter.UI>>
        implements ScreenAndVaccinationHistoryDetailPresenter.UI, EventListener<AlertDialogFragment.DismissedEvent> {

    public static final String DATE_STRING = "DATE_STRING";
    public static final String DATE_MESSAGE = "DATE_MESSAGE";
    private static final String DOWNLOAD_FILE_ALERT_FAILED = "DOWNLOAD_FAILED";

    @Inject
    ScreenAndVaccinationHistoryRepository historyItemRepository;

    @Inject
    ScreenAndVaccinationHistoryDetailPresenter presenter;

    @Inject
    EventDispatcher eventDispatcher;

    private RecyclerView mainRecyclerView;

    private ListHistoryListDto historyListDto;
//    private PresentableProof snvPresentableProof;
    SNVHistoryPresentableProof snvPresentableProof;
    private List<SNVMainAdapterModel> mainAdapterModels;
    private SNVHistoryMainAdapter adapter;


    @Override
    protected void create(@Nullable Bundle savedInstanceState) {
        super.create(savedInstanceState);
        setContentView(R.layout.activity_snv_history_detail);

        setUpActionBarWithTitle(getIntent().getStringExtra(DATE_MESSAGE))
                .setDisplayHomeAsUpEnabled(true);

        setUpViews();
        setUpViewsData();
        presenter.setContext(this);
        presenter.setMetadaDTO(historyItemRepository.getEventMetaData(getIntent().getStringExtra(DATE_STRING)));

    }

    @Override
    protected void injectDependencies(DependencyInjector dependencyInjector) {
        dependencyInjector.inject(this);
    }

    @Override
    protected ScreenAndVaccinationHistoryDetailPresenter.UI getUserInterface() {
        return this;
    }

    @Override
    protected ScreenAndVaccinationHistoryDetailPresenter<ScreenAndVaccinationHistoryDetailPresenter.UI> getPresenter() {
        return presenter;
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
    protected void resume() {
        eventDispatcher.addEventListener(AlertDialogFragment.DismissedEvent.class, this);
    }

    @Override
    protected void pause() {
        eventDispatcher.removeEventListener(AlertDialogFragment.DismissedEvent.class, this);
    }

    public void setUpViews(){
        this.mainRecyclerView = findViewById(R.id.main_recycler_view);
    }

    public void setUpViewsData(){
        this.mainAdapterModels = new ArrayList<>();
        historyListDto = historyItemRepository.getHistoryListItem(getIntent().getStringExtra(DATE_STRING));
        snvPresentableProof = historyItemRepository.getSNVPresentableProof();
        if(historyListDto != null){
            if(historyListDto.getScreeningsList().size() != 0){
                mainAdapterModels.add(new SNVMainAdapterModel(getString(R.string.SV_screenings_title_1012),
                        historyListDto.getScreeningsList()));
            }

            if(historyListDto.getVaccinationList().size() != 0){
                mainAdapterModels.add(new SNVMainAdapterModel(getString(R.string.SV_vaccinations_title_1013),
                        historyListDto.getVaccinationList()));
            }
            providingListData(mainAdapterModels, mainRecyclerView, snvPresentableProof);
        }
    }

    public void providingListData(List<SNVMainAdapterModel> mapAdapterList, RecyclerView eventListView,
                                  SNVHistoryPresentableProof presentableProofParam) {
        if(mapAdapterList.size() != 0) {

            adapter = new SNVHistoryMainAdapter(mapAdapterList, getLayoutInflater(),this);
            eventListView.setAdapter(adapter);
            ViewUtilities.addDividers(this, eventListView);
        } else {
            eventListView.setVisibility(View.GONE);
        }
    }

    @Override
    public void getPresentableProof(List<String> providedPresentableProof) {
        if(providedPresentableProof.size() != 0){
            adapter.setSnvPresentableProof(providedPresentableProof);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void showAlertDialog() {
        AlertDialogFragment alert = AlertDialogFragment.create(DOWNLOAD_FILE_ALERT_FAILED, "DOWNLOAD FAILED", "Something went wrong in downloading the files", null, null, getString(R.string.ok_button_title_40));
        alert.show(getSupportFragmentManager(), DOWNLOAD_FILE_ALERT_FAILED);
    }

    @Override
    public void onEvent(AlertDialogFragment.DismissedEvent event) {
        this.presenter.setIsDialogShown(false);
    }

}
