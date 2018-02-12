package com.vitalityactive.va.vhc.addproof;

import android.support.test.filters.SmallTest;
import android.support.test.runner.AndroidJUnit4;

import com.vitalityactive.va.RepositoryTestBase;
import com.vitalityactive.va.dto.ProofItemDTO;
import com.vitalityactive.va.persistence.models.ProofItem;
import com.vitalityactive.va.testutilities.ModelVerifier;
import com.vitalityactive.va.testutilities.annotations.RepositoryTests;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
@SmallTest
@RepositoryTests
public class VHCProofItemRepositoryTest extends RepositoryTestBase {

    private VHCProofItemRepository repository;

    @Override
    public void setUp() throws IOException {
        super.setUp();
        repository = new VHCProofItemRepositoryImpl(dataStore);
    }

    @Test
    public void should_persist_proof_item_from_uri() throws Exception {
        final String uri = "proof/item/uri";
        repository.persistUri(uri);
        new ModelVerifier<>(ProofItem.class, getRealm(), 1, new ModelVerifier.Verifier<ProofItem>() {
            @Override
            public void verifyModel(ProofItem model, int index) throws Exception {
                assertThat(model.getUri(), is(uri));
            }
        });
    }

    @Test
    public void should_return_proof_items() throws Exception {
        final String uri = "proof/item/uri";
        persistProofItem(uri);
        persistProofItem(uri);
        List<ProofItemDTO> proofItems = repository.getProofItems();
        assertThat(proofItems.size(), is(2));
    }

    @Test
    public void should_remove_a_proof_item() throws Exception {
        final String uri = "proof/item/uri";
        ProofItemDTO proofItem = persistProofItem(uri);
        repository.removeProofItem(proofItem);
        assertTrue(repository.getProofItems().isEmpty());
    }

    @Test
    public void should_return_proof_items_that_have_not_been_submitted() throws Exception {
        final String uri = "proof/item/uri";
        persistProofItem(uri);
        ProofItemDTO proofItem = persistProofItem(uri);
        repository.setProofItemReferenceId(proofItem, "123");

        List<ProofItemDTO> proofItems = repository.getProofItemsThatHaveNotBeenSubmitted();
        assertThat(proofItems.size(), is(1));
    }

    @Test
    public void should_set_proof_item_reference_id() throws Exception {
        final String uri = "proof/item/uri";
        ProofItemDTO proofItem = persistProofItem(uri);
        String refId = "123";

        repository.setProofItemReferenceId(proofItem, refId);

        List<ProofItemDTO> proofItems = repository.getProofItems();
        assertThat(proofItems.get(0).getReferenceId(), is(refId));
    }

    @Test
    public void should_remove_all_proof_items() throws Exception {
        final String uri = "proof/item/uri";
        persistProofItem(uri);
        repository.removeAllProofItems();
        assertTrue(repository.getProofItems().isEmpty());
    }

    private ProofItemDTO persistProofItem(String uri) {
        return repository.persistUri(uri);
    }

}