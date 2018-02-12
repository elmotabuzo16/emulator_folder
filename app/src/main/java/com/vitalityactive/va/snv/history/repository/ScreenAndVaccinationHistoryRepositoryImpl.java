package com.vitalityactive.va.snv.history.repository;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.vitalityactive.va.dto.ProofItemDTO;
import com.vitalityactive.va.networking.parsing.Persister;
import com.vitalityactive.va.persistence.DataStore;
import com.vitalityactive.va.persistence.models.ProofItem;
import com.vitalityactive.va.persistence.models.screeningsandvaccinations.ScreeningVaccinationAssociatedEvents;
import com.vitalityactive.va.persistence.models.screeningsandvaccinations.ScreeningVaccinationEventByParty;
import com.vitalityactive.va.persistence.models.screeningsandvaccinations.ScreeningVaccinationEventByPartyOutbound;
import com.vitalityactive.va.persistence.models.screeningsandvaccinations.ScreeningVaccinationMetadata;
import com.vitalityactive.va.snv.dto.HistoryDetailDto;
import com.vitalityactive.va.snv.dto.ListHistoryListDto;
import com.vitalityactive.va.snv.dto.ScreeningVaccinationMetadataDto;
import com.vitalityactive.va.snv.history.interactor.ScreeningAndVaccinationsHistoryInteractor;
import com.vitalityactive.va.snv.history.views.model.SNVHistoryPresentableProof;
import com.vitalityactive.va.vhc.summary.PresentableProof;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Singleton;

import io.realm.RealmQuery;
import io.realm.Sort;

/**
 * Created by dharel.h.rosell on 12/1/2017.
 */

@Singleton
public class ScreenAndVaccinationHistoryRepositoryImpl implements ScreenAndVaccinationHistoryRepository {

    public static final int SCREENING_CODE = 23;
    public static final int VACCINATION_CODE = 24;

    private final DataStore dataStore;
    private final Persister persister;
    private ScreeningAndVaccinationsHistoryInteractor interactor;
    private List<HistoryDetailDto> historyManipulate;
    private String dateStringLabel;

    public ScreenAndVaccinationHistoryRepositoryImpl(DataStore dataStore) {
        this.dataStore = dataStore;
        persister = new Persister(dataStore);
    }

    @Override
    public boolean persisScreenAndVaccinationHistoryListResponse(ScreeningVaccinationEventByParty responseSVHistoryList) {
        return persister.addModel(responseSVHistoryList);
    }

    @Override
    public List<HistoryDetailDto> getScreeningAndVaccinationHistoryList() {
        List<HistoryDetailDto> listHistory = dataStore.getModels(ScreeningVaccinationAssociatedEvents.class,
                new DataStore.ModelListMapper<ScreeningVaccinationAssociatedEvents, HistoryDetailDto>() {
                    @Override
                    public List<HistoryDetailDto> mapModels(List<ScreeningVaccinationAssociatedEvents> models) {
                        List<HistoryDetailDto> listHistoryDto = new ArrayList<>();
                        for(ScreeningVaccinationAssociatedEvents listItem: models){
                            listHistoryDto.add(new HistoryDetailDto(listItem));
                        }
                        return listHistoryDto;
                    }
                });
        historyManipulate = new ArrayList<>();
        historyManipulate.clear();
        historyManipulate = listHistory;
        return listHistory;
    }

    @Override
    public List<ListHistoryListDto> getHistoryListDto() {

        List<ListHistoryListDto> dateString = gettingDates(this.historyManipulate);

        for(int i=0; i < dateString.size(); i++) {
            Log.d("date String parse", dateString.get(i).getDateString());

            this.dateStringLabel = dateString.get(i).getDateString();
            dateString.get(i).setScreeningsList(getScreeningRealm(dateStringLabel));
            dateString.get(i).setVaccinationList(getVaccinationRealm(dateStringLabel));
        }

        return dateString;
    }

    @Override
    public ListHistoryListDto getHistoryListItem(String date) {
        ListHistoryListDto historyListItem = new ListHistoryListDto();
        historyListItem.setDateString(date);
        historyListItem.setScreeningsList(getScreeningRealm(date));
        historyListItem.setVaccinationList(getVaccinationRealm(date));

        return historyListItem;
    }

    @Override
    public SNVHistoryPresentableProof getSNVPresentableProof() {
//        List<ProofItemDTO> proofItemUris = new ArrayList<>();
//        proofItemUris.add(new ProofItemDTO(new ProofItem("")));
//        proofItemUris.add(new ProofItemDTO(new ProofItem("")));
        List<String> paths = new ArrayList<>();
        return new SNVHistoryPresentableProof("Uploaded Proof", paths);
    }

    @Override
    public  List<ScreeningVaccinationMetadataDto> getEventMetaData(final String eventDateTime) {
        Log.d("MetaDataOut DATE", eventDateTime);
        return dataStore.getModels(ScreeningVaccinationEventByPartyOutbound.class,
                new DataStore.QueryExecutor<ScreeningVaccinationEventByPartyOutbound, RealmQuery<ScreeningVaccinationEventByPartyOutbound>>() {
                    @Override
                    public List<ScreeningVaccinationEventByPartyOutbound> executeQueries(RealmQuery<ScreeningVaccinationEventByPartyOutbound> initialQuery) {
                        return initialQuery.contains("eventDateTime", eventDateTime).findAll();
                    }
                }, new DataStore.ModelListMapper<ScreeningVaccinationEventByPartyOutbound, ScreeningVaccinationMetadataDto>() {
                    @Override
                    public List<ScreeningVaccinationMetadataDto> mapModels(List<ScreeningVaccinationEventByPartyOutbound> models) {
                        List<ScreeningVaccinationMetadataDto> listMetaDataDto = new ArrayList<>();
                        for(ScreeningVaccinationEventByPartyOutbound modelItem: models){
                            List<ScreeningVaccinationMetadata> metadateRealm = modelItem.getEventMetaDataOuts();
                            for(ScreeningVaccinationMetadata metadateRealmItem: metadateRealm){
                                listMetaDataDto.add(new ScreeningVaccinationMetadataDto(metadateRealmItem));
                            }
                        }
                        return listMetaDataDto;
                    }
                });

    }

    @Override
    public List<ScreeningVaccinationAssociatedEvents> getAssociatedEvents() {
        List<ScreeningVaccinationAssociatedEvents> listAssociatedEvents = new ArrayList<>();
        listAssociatedEvents = dataStore .getFieldValue(ScreeningVaccinationEventByPartyOutbound.class,
                new DataStore.FieldAccessor<ScreeningVaccinationEventByPartyOutbound, List<ScreeningVaccinationAssociatedEvents>>() {
                    @NonNull
                    @Override
                    public List<ScreeningVaccinationAssociatedEvents> getField(@Nullable ScreeningVaccinationEventByPartyOutbound model) {
                        return model.getAssociatedEvents();
                    }
                });

        return listAssociatedEvents;
    }


    public List<ListHistoryListDto> gettingDates(List<HistoryDetailDto> historyDetailListParam) {

          List<String> dateString = dataStore.getModels(ScreeningVaccinationEventByPartyOutbound.class,
                  new DataStore.QueryExecutor<ScreeningVaccinationEventByPartyOutbound, RealmQuery<ScreeningVaccinationEventByPartyOutbound>>() {
                      @Override
                      public List<ScreeningVaccinationEventByPartyOutbound> executeQueries(RealmQuery<ScreeningVaccinationEventByPartyOutbound> initialQuery) {
                          return initialQuery.findAllSorted("dateConverted", Sort.DESCENDING);
                      }
                  }, new DataStore.ModelListMapper<ScreeningVaccinationEventByPartyOutbound, String>() {
                      @Override
                      public List<String> mapModels(List<ScreeningVaccinationEventByPartyOutbound> models) {
                          List<String> mappedModels = new ArrayList<>();
                          for(ScreeningVaccinationEventByPartyOutbound screeningVaccinationEventByPartyOutbound: models){
                              mappedModels.add(screeningVaccinationEventByPartyOutbound.getEventDateTime());
                          }
                          return mappedModels;
                      }
                  });

        List<ListHistoryListDto> historyListDate = new ArrayList<>();
        String dateTag = dateString.get(0).substring(0,10);
        historyListDate.add(new ListHistoryListDto(dateTag, new ArrayList<HistoryDetailDto>(), new ArrayList<HistoryDetailDto>()));
        for(String item: dateString){
            String itemConvert = item.substring(0,10);
            if(!dateTag.equalsIgnoreCase(itemConvert)){
                historyListDate.add(new ListHistoryListDto(itemConvert, new ArrayList<HistoryDetailDto>(), new ArrayList<HistoryDetailDto>()));
                dateTag=itemConvert;
            }
        }

        return historyListDate;

    }

    public List<HistoryDetailDto> getScreeningRealm(final String dateStringLabelParam){
        Log.d("Screening get date", dateStringLabelParam);
        List<HistoryDetailDto> snvScreeningRealmResult = new ArrayList<>();
        snvScreeningRealmResult = dataStore.getModels(ScreeningVaccinationAssociatedEvents.class,
                        new DataStore.QueryExecutor<ScreeningVaccinationAssociatedEvents, RealmQuery<ScreeningVaccinationAssociatedEvents>>() {
                            @Override
                            public List<ScreeningVaccinationAssociatedEvents> executeQueries(RealmQuery<ScreeningVaccinationAssociatedEvents> initialQuery) {
                                return initialQuery
                                        .contains("eventDateTime", dateStringLabelParam)
                                        .equalTo("categoryKey", SCREENING_CODE)
                                        .findAllSorted("dateConverted", Sort.DESCENDING);
                            }
                        }, new DataStore.ModelListMapper<ScreeningVaccinationAssociatedEvents, HistoryDetailDto>() {
                            @Override
                            public List<HistoryDetailDto> mapModels(List<ScreeningVaccinationAssociatedEvents> models) {
                                List<HistoryDetailDto> mappedModels = new ArrayList<>();
                                for(ScreeningVaccinationAssociatedEvents modelItem: models){
                                    Log.d("Realm getting Screening", modelItem.getEventTypeName());
                                    if(!checkDuplicate(mappedModels, modelItem.getEventTypeName())) {
                                        mappedModels.add(new HistoryDetailDto(modelItem));
                                    }
                                }
                                return mappedModels;
                            }
                        });

        return snvScreeningRealmResult;
    }

    public List<HistoryDetailDto> getVaccinationRealm(final String dateStringLabelParam) {
        Log.d("Vaccination get date", dateStringLabelParam);
        List<HistoryDetailDto> snvVaccinationRealmResult = new ArrayList<>();
        snvVaccinationRealmResult = dataStore.getModels(ScreeningVaccinationAssociatedEvents.class,
                        new DataStore.QueryExecutor<ScreeningVaccinationAssociatedEvents, RealmQuery<ScreeningVaccinationAssociatedEvents>>() {
                            @Override
                            public List<ScreeningVaccinationAssociatedEvents> executeQueries(RealmQuery<ScreeningVaccinationAssociatedEvents> initialQuery) {
                                return initialQuery
                                        .contains("eventDateTime", dateStringLabelParam)
                                        .equalTo("categoryKey", VACCINATION_CODE)
                                        .findAllSorted("dateConverted", Sort.DESCENDING);
                            }
                        }, new DataStore.ModelListMapper<ScreeningVaccinationAssociatedEvents, HistoryDetailDto>() {
                            @Override
                            public List<HistoryDetailDto> mapModels(List<ScreeningVaccinationAssociatedEvents> models) {
                                List<HistoryDetailDto> mappedModels = new ArrayList<>();
                                for(ScreeningVaccinationAssociatedEvents modelItem: models){
                                    Log.d("Realm get Vaccination", modelItem.getEventTypeName());
                                    if(!checkDuplicate(mappedModels, modelItem.getEventTypeName())) {
                                        mappedModels.add(new HistoryDetailDto(modelItem));
                                    }
                                }
                                return mappedModels;
                            }
                        });

        return snvVaccinationRealmResult;
    }

    public boolean checkDuplicate(List<HistoryDetailDto> list, String dataToCompare) {

        if(list.size() != 0){
            for(HistoryDetailDto listItem: list) {
                if(listItem.getEventTypeName().equalsIgnoreCase(dataToCompare)){
                    return true;
                }
            }
        }

        return false;
    }

}
