package com.vitalityactive.va.networking.parsing;

import com.vitalityactive.va.networking.model.LoginServiceResponse;
import com.vitalityactive.va.persistence.DataStore;
import com.vitalityactive.va.persistence.models.Telephone;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class PersisterTest {

    @Mock
    private DataStore dataStore;
    @Mock
    private Persister.InstanceCreator<Telephone, LoginServiceResponse.Telephone> modelCreator;
    @Captor
    private ArgumentCaptor<List<Telephone>> modelCaptor;

    private Persister persister;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        persister = new Persister(dataStore);
    }

    @Test
    public void should_not_add_null_model() throws Exception {
        assertFalse(persister.addModel(null));
    }

    @Test
    public void should_add_non_null_model() throws Exception {
        when(dataStore.add(any(Telephone.class))).thenReturn(true);
        Telephone telephone = new Telephone();
        assertTrue(persister.addModel(telephone));
        verify(dataStore).add(telephone);
    }

    @Test
    public void should_not_add_null_response_objects() throws Exception {
        assertFalse(persister.addModels(null, modelCreator));
    }

    @Test
    public void should_add_non_null_response_objects() throws Exception {
        Telephone modelTel = new Telephone();
        when(modelCreator.create(any(LoginServiceResponse.Telephone.class))).thenReturn(modelTel);
        when(dataStore.add(anyListOf(Telephone.class))).thenReturn(true);
        LoginServiceResponse.Telephone telResponse = new LoginServiceResponse.Telephone();
        List<LoginServiceResponse.Telephone> responseTels = Collections.singletonList(telResponse);

        assertTrue(persister.addModels(responseTels, modelCreator));

        verify(dataStore).add(modelCaptor.capture());
        assertThat(modelCaptor.getValue().size(), is(responseTels.size()));
        verify(modelCreator, times(responseTels.size())).create(any(LoginServiceResponse.Telephone.class));
    }

    @Test
    public void should_not_add_or_update_null_response_objects() throws Exception {
        assertFalse(persister.addOrUpdateModels(null, modelCreator));
    }

    @Test
    public void should_add_or_update_non_null_response_objects() throws Exception {
        Telephone modelTel = new Telephone();
        when(modelCreator.create(any(LoginServiceResponse.Telephone.class))).thenReturn(modelTel);
        when(dataStore.addOrUpdate(anyListOf(Telephone.class))).thenReturn(true);
        LoginServiceResponse.Telephone telResponse = new LoginServiceResponse.Telephone();
        List<LoginServiceResponse.Telephone> responseTels = Collections.singletonList(telResponse);

        assertTrue(persister.addOrUpdateModels(responseTels, modelCreator));

        verify(dataStore).addOrUpdate(modelCaptor.capture());
        assertThat(modelCaptor.getValue().size(), is(responseTels.size()));
    }

}