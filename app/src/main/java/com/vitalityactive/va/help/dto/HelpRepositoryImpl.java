package com.vitalityactive.va.help.dto;

import android.support.annotation.NonNull;

import com.vitalityactive.va.dto.HelpDTO;
import com.vitalityactive.va.dto.ContentHelpDTO;
import com.vitalityactive.va.dto.ReferenceDTO;
import com.vitalityactive.va.help.model.HelpResponse;
import com.vitalityactive.va.networking.parsing.Persister;
import com.vitalityactive.va.persistence.DataStore;
import com.vitalityactive.va.persistence.models.Help;
import com.vitalityactive.va.persistence.Model;

import java.util.ArrayList;
import java.util.List;

import com.vitalityactive.va.networking.parsing.Persister;
import com.vitalityactive.va.persistence.DataStore;
import com.vitalityactive.va.persistence.Model;
import com.vitalityactive.va.persistence.models.ContentHelp;
import com.vitalityactive.va.persistence.models.Reference;
import com.vitalityactive.va.search.ContentHelpResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by christian.j.p.capin on 11/30/2017.
 */

public class HelpRepositoryImpl implements HelpRepository {
    private final DataStore dataStore;
    private final Persister persister;

    public HelpRepositoryImpl(DataStore dataStore) {
        this.dataStore = dataStore;
        persister = new Persister(dataStore);
    }

    @Override
    public void persistHelpResponse(HelpResponse response) {
        dataStore.removeAll(Help.class);
        parseAndPersistHelp(response.sections);
    }

    private boolean parseAndPersistHelp(final List<HelpResponse.FAQ> contents) {
        return parseAndPersistModels(contents, new Persister.InstanceCreator<Model, HelpResponse.FAQ>() {
            @Override
            public Model create(HelpResponse.FAQ model) {
                return new Help(model);
            }
        });
    }

    @Override
    public List<HelpDTO> getHelp() {
        return dataStore.getModels(Help.class,
                new DataStore.ModelListMapper<Help, HelpDTO>() {
                    @Override
                    public List<HelpDTO> mapModels(List<Help> models) {
                        List<HelpDTO> mapModel = new ArrayList<>();
                        for (Help contents : models) {
                            mapModel.add(new HelpDTO(contents));
                        }
                        return mapModel;
                    }
                });
    }

    @Override
    public List<HelpDTO> getHelpFive() {
        List<HelpDTO> fiveHelp = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            fiveHelp.add(getHelp().get(i));
        }
        return fiveHelp;
    }

    @Override
    public void persistHelpContent(ContentHelpResponse response) {
        parseAndPersistContent(response.FAQ);
    }

    private boolean parseAndPersistContent(final List<ContentHelpResponse.FAQ> contents) {
        return parseAndPersistModels(contents, new Persister.InstanceCreator<Model, ContentHelpResponse.FAQ>() {
            @Override
            public Model create(ContentHelpResponse.FAQ model) {
                return new ContentHelp(model);
            }
        });
    }

    private <T extends Model, U> boolean parseAndPersistModels(List<U> models, Persister.InstanceCreator<T, U> instanceCreator) {
        return persister.addModels(models, instanceCreator);
    }

    @Override
    public List<ContentHelpDTO> getContentHelp() {
        return dataStore.getModels(ContentHelp.class, new DataStore.ModelListMapper<ContentHelp, ContentHelpDTO>() {
            @Override
            public List<ContentHelpDTO> mapModels(List<ContentHelp> models) {
                List<ContentHelpDTO> mapModel = new ArrayList<>();
                for (ContentHelp contents : models) {
                    mapModel.add(new ContentHelpDTO(contents));
                }
                return mapModel;
            }
        });
    }

    @Override
    public List<ContentHelpDTO> getThreeRelatedHelp() {
        List<ContentHelpDTO> threeRelatedHelp = new ArrayList<>();
        if (!getContentHelp().isEmpty()) {
            int sizelimit = getContentHelp().size() > 3 ? 3 : getContentHelp().size();
            for (int i = 0; i < sizelimit; i++) {
                threeRelatedHelp.add(getContentHelp().get(i));
            }
        }
        return threeRelatedHelp;
    }

}
