package com.vitalityactive.va.vhc.addproof;

import com.vitalityactive.va.dto.ProofItemDTO;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class VHCAddProofInteractorTest {

    @Mock
    private VHCProofItemRepository repository;
    @Mock
    private List<ProofItemDTO> proofItems;

    private VHCAddProofInteractor interactor;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        interactor = new VHCAddProofInteractorImpl(repository);
    }

    @Test
    public void should_add_a_proof_item() throws Exception {
        when(proofItems.size()).thenReturn(1);
        when(repository.getProofItems()).thenReturn(proofItems);
        ProofItemDTO proofItem = mock(ProofItemDTO.class);
        when(repository.persistUri(anyString())).thenReturn(proofItem);
        String uri = "proof/item/uri";

        assertSame(proofItem, interactor.addProofItemUri(uri));

        verify(repository).getProofItems();
        verify(repository).persistUri(uri);
    }

    @Test
    public void should_not_persist_more_than_eleven_proof_items() throws Exception {
        when(proofItems.size()).thenReturn(11);
        when(repository.getProofItems()).thenReturn(proofItems);
        assertNull(interactor.addProofItemUri("proof/item/uri"));
        verify(repository, never()).persistUri(anyString());
    }

    @Test
    public void should_return_proof_item_uris() throws Exception {
        interactor.getProofItemUris();
        verify(repository).getProofItems();
    }

    @Test
    public void should_remove_a_proof_item() throws Exception {
        ProofItemDTO proofItem = mock(ProofItemDTO.class);
        interactor.removeProofItem(proofItem);
        verify(repository).removeProofItem(proofItem);
    }

}