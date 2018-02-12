package com.vitalityactive.va.snv.history.presenter;

import android.content.Context;
import android.util.Log;

import com.vitalityactive.va.BasePresenter;
import com.vitalityactive.va.Scheduler;
import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.events.EventListener;
import com.vitalityactive.va.persistence.DataStore;
import com.vitalityactive.va.persistence.models.screeningsandvaccinations.ScreeningVaccinationMetadata;
import com.vitalityactive.va.snv.dto.ScreeningVaccinationMetadataDto;
import com.vitalityactive.va.snv.history.utility.SNVHistoryDetailProofProvider;
import com.vitalityactive.va.snv.history.utility.UploadedProofEventFail;
import com.vitalityactive.va.snv.history.utility.UploadedProofEventSuccess;
import com.vitalityactive.va.snv.history.views.model.SNVHistoryPresentableProof;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmQuery;

/**
 * Created by dharel.h.rosell on 12/10/2017.
 */

public class ScreenAndVaccinationHistoryDetailPresenterImpl<UI extends ScreenAndVaccinationHistoryDetailPresenter.UI>
        extends BasePresenter<UI> implements ScreenAndVaccinationHistoryDetailPresenter<UI>,
        EventListener<UploadedProofEventSuccess>{

    private EventDispatcher eventDispatcher;
    private SNVHistoryDetailProofProvider snvHistoryDetailProofProvider;
    private Context context;
    private final DataStore dataStore;
    List<ScreeningVaccinationMetadataDto> metadaDTO;
    private boolean isDialogShown = false;
    private List<String> proofItemUris;
    private SNVHistoryPresentableProof presentableProof;
    private final Scheduler scheduler;

    private final EventListener<UploadedProofEventSuccess> successEvent = new EventListener<UploadedProofEventSuccess>() {
        @Override
        public void onEvent(UploadedProofEventSuccess event) {
            successGettingImage(event.getMessage());
        }
    };

    private final EventListener<UploadedProofEventFail> failEvent= new EventListener<UploadedProofEventFail>() {
        @Override
        public void onEvent(UploadedProofEventFail event) {
            errorInGettingFile();
        }
    };

    private UI userInterface;

    public ScreenAndVaccinationHistoryDetailPresenterImpl(
            EventDispatcher eventDispatcher,
            SNVHistoryDetailProofProvider snvHistoryDetailProofProvider, DataStore dataStoreParam,
            final Scheduler schedulerParam) {
        this.eventDispatcher = eventDispatcher;
        this.snvHistoryDetailProofProvider = snvHistoryDetailProofProvider;
        this.dataStore = dataStoreParam;
        this.metadaDTO = new ArrayList<>();
        this.proofItemUris = new ArrayList<>();
        this.scheduler = schedulerParam;
    }

    @Override
    public void setUserInterface(UI userInterface) {
        this.userInterface = userInterface;
    }

    @Override
    public void onUserInterfaceCreated(boolean isNewNavigation) {

        super.onUserInterfaceCreated(isNewNavigation);
        if (isNewNavigation) {
            userInterface.showLoadingIndicator();
            addEventListeners();
//            Toast.makeText(this.context, "CALLING IMAGE PROVIDER " + 411459, Toast.LENGTH_LONG).show();
//            List<Integer> referenceIdList = dataStore.getModels(ScreeningVaccinationMetadata.class,
//                    new DataStore.QueryExecutor<ScreeningVaccinationMetadata, RealmQuery<ScreeningVaccinationMetadata>>() {
//                        @Override
//                        public List<ScreeningVaccinationMetadata> executeQueries(RealmQuery<ScreeningVaccinationMetadata> initialQuery) {
//                            return initialQuery.findAll();
//                        }
//                    }, new DataStore.ModelListMapper<ScreeningVaccinationMetadata, Integer>() {
//                        @Override
//                        public List<Integer> mapModels(List<ScreeningVaccinationMetadata> models) {
//                            List<Integer> mappedModels = new ArrayList<>();
//                            for(ScreeningVaccinationMetadata modelItem: models){
//                                mappedModels.add(modelItem.getTypeKey());
//                            }
//                            return mappedModels;
//                        }
//                    });

            for(ScreeningVaccinationMetadataDto metadaDTOItem: this.metadaDTO) {
                Log.d("MetaDataOut CONTAINS", metadaDTOItem.getValue());
                snvHistoryDetailProofProvider.fetchUploadedProof(Integer.parseInt(metadaDTOItem.getValue()));
            }
//            snvHistoryDetailProofProvider.fetchUploadedProof(411459);

        }
    }

    @Override
    public void onUserInterfaceDisappeared(boolean isFinishing) {
            super.onUserInterfaceDisappeared(isFinishing);
    }

    @Override
    public void onUserInterfaceDestroyed() {
        removeEventListeners();
    }

    @Override
    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    public void setMetadaDTO(List<ScreeningVaccinationMetadataDto> metadaDTOParam) {
        this.metadaDTO = metadaDTOParam;
    }

    @Override
    public void setIsDialogShown(boolean isShown) {
        isDialogShown = isShown;
    }

    @Override
    public List<ScreeningVaccinationMetadataDto> getMetadaDTO() {
        return this.metadaDTO;
    }

    @Override
    public void onEvent(UploadedProofEventSuccess event) {
//        if(snvHistoryDetailProofProvider.isProfileImageAvailable()){
//            List<ProofItemDTO> proofItemUris = new ArrayList<>();
//            proofItemUris.add(new ProofItemDTO(new ProofItem(snvHistoryDetailProofProvider.getProfileImagePath())));
//            userInterface.getPresentableProof(new PresentableProof("Uploaded Proof", proofItemUris));
//        }
    }

    public void successGettingImage(final String fileName){
        Log.d("MetaDataOut FileName", fileName);
        scheduler.schedule(new Runnable() {
            @Override
            public void run() {
                userInterface.hideLoadingIndicator();
                if(!fileName.contains("pdf")) {
                    if (snvHistoryDetailProofProvider.isProfileImageAvailable(fileName)) {
                        String path = snvHistoryDetailProofProvider.getProfileImagePath(fileName);
                        proofItemUris.add(path);
                        presentableProof = new SNVHistoryPresentableProof("Uploaded Proof", proofItemUris);
                        userInterface.getPresentableProof(proofItemUris);
                    }
                }
            }
        });

    }

    public void errorInGettingFile(){
        scheduler.schedule(new Runnable() {
            @Override
            public void run() {
                userInterface.hideLoadingIndicator();
//                if(!isDialogShown) {
//                    userInterface.showAlertDialog();
//                    isDialogShown = true;
//                }
            }
        });
    }

    private void addEventListeners() {
        eventDispatcher.addEventListener(UploadedProofEventSuccess.class, successEvent);
        eventDispatcher.addEventListener(UploadedProofEventFail.class, failEvent);
    }

    private void removeEventListeners() {
        eventDispatcher.removeEventListener(UploadedProofEventSuccess.class, successEvent);
        eventDispatcher.removeEventListener(UploadedProofEventFail.class, failEvent);
    }
}
