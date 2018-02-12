package com.vitalityactive.va.vhc.addproof;

import com.vitalityactive.va.dto.ProofItemDTO;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


public class VHCAddProofPresenterTest {

    @Mock
    private VHCAddProofInteractor interactor;
    @Mock
    private VHCAddProofPresenter.UserInterface userInterface;
    @Mock
    private List<ProofItemDTO> proofItems;

    private VHCAddProofPresenter presenter;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        presenter = new VHCAddProofPresenterImpl(interactor);
        presenter.setUserInterface(userInterface);
    }

    @Test
    public void when_ui_appears_and_there_is_still_less_than_eleven_proof_items_then_show_add_button() throws Exception {
        int itemCount = 1;
        when(proofItems.size()).thenReturn(itemCount);
        when(interactor.getProofItemUris()).thenReturn(proofItems);

        presenter.onUserInterfaceAppeared();

        verify(userInterface).showProofItems(proofItems);
        verify(userInterface).updateProofItemCount(itemCount);
        verify(userInterface).showAddProofButton();
    }

    @Test
    public void when_ui_appears_with_eleven_proof_items_then_hide_add_button() throws Exception {
        int itemCount = 11;
        when(proofItems.size()).thenReturn(itemCount);
        when(interactor.getProofItemUris()).thenReturn(proofItems);

        presenter.onUserInterfaceAppeared();

        verify(userInterface).showProofItems(proofItems);
        verify(userInterface).updateProofItemCount(itemCount);
        verify(userInterface).hideAddProofButton();
    }

    @Test
    public void when_adding_proof_item_fails_then_return_null() throws Exception {
        when(interactor.addProofItemUri(anyString())).thenReturn(null);
        assertNull(presenter.addProofItemFromUri("proof/item/uri"));
    }

    @Test
    public void when_proof_item_added_and_there_is_still_less_than_eleven_then_show_add_button() throws Exception {
        int itemCount = 1;
        when(proofItems.size()).thenReturn(itemCount);
        when(interactor.getProofItemUris()).thenReturn(proofItems);
        ProofItemDTO proofItem = mock(ProofItemDTO.class);
        when(interactor.addProofItemUri(anyString())).thenReturn(proofItem);
        String uri = "proof/item/uri";

        assertSame(proofItem, presenter.addProofItemFromUri(uri));

        InOrder inOrder = inOrder(interactor, userInterface);
        inOrder.verify(interactor).addProofItemUri(uri);
        inOrder.verify(interactor).getProofItemUris();
        inOrder.verify(userInterface).updateProofItemCount(itemCount);
        inOrder.verify(userInterface).showAddProofButton();
    }

    @Test
    public void when_proof_item_added_and_there_is_now_eleven_then_hide_add_button() throws Exception {
        int itemCount = 11;
        when(proofItems.size()).thenReturn(itemCount);
        when(interactor.getProofItemUris()).thenReturn(proofItems);
        ProofItemDTO proofItem = mock(ProofItemDTO.class);
        when(interactor.addProofItemUri(anyString())).thenReturn(proofItem);
        String uri = "proof/item/uri";

        assertSame(proofItem, presenter.addProofItemFromUri(uri));

        InOrder inOrder = inOrder(interactor, userInterface);
        inOrder.verify(interactor).addProofItemUri(uri);
        inOrder.verify(interactor).getProofItemUris();
        inOrder.verify(userInterface).updateProofItemCount(itemCount);
        inOrder.verify(userInterface).hideAddProofButton();
    }

    @Test
    public void should_be_able_to_add_proof_item() throws Exception {
        int itemCount = 0;
        when(proofItems.size()).thenReturn(itemCount);
        when(interactor.getProofItemUris()).thenReturn(proofItems);

        presenter.onUserSelectsItem(0);

        verify(userInterface).onVHCAddProofTapped();
    }

    @Test
    public void should_be_able_to_view_proof_item_details() throws Exception {
        int itemCount = 1;
        when(proofItems.size()).thenReturn(itemCount);
        when(interactor.getProofItemUris()).thenReturn(proofItems);
        int position = 0;

        presenter.onUserSelectsItem(position);

        verify(userInterface).showProofItemDetail(position);
    }

}