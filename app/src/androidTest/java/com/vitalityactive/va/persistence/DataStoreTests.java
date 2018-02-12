package com.vitalityactive.va.persistence;

import android.support.annotation.NonNull;

import com.vitalityactive.va.RepositoryTestBase;
import com.vitalityactive.va.cucumber.utils.TestHarness;
import com.vitalityactive.va.networking.model.LoginServiceResponse;
import com.vitalityactive.va.persistence.models.User;
import com.vitalityactive.va.testutilities.annotations.RepositoryTests;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;

@RepositoryTests
public class DataStoreTests extends RepositoryTestBase {
    private ArrayList<User> list = new ArrayList<>();

    @Before
    public void setUp() throws IOException {
        super.setUp();
        dataStore.clear();
        list.add(getUser("user1", TestHarness.getPartyDetails(1L), 1L));
        list.add(getUser("user2", TestHarness.getPartyDetails(2L), 2L));
    }

    @NonNull
    private User getUser(String user2, LoginServiceResponse.PartyDetails partyDetails, long vitalityMembershipId) {
        return new User(partyDetails, vitalityMembershipId, user2);
    }

    @Test
    public void canReplaceClass() throws InterruptedException {
        Assert.assertTrue("replace all failed", dataStore.replaceAll(User.class, list));
        assertModelCount(User.class, 2);

        // when replaced again

        list.remove(0);
        list.add(getUser("user3", TestHarness.getPartyDetails(3L), 3L));
        Assert.assertTrue("replace all failed", dataStore.replaceAll(User.class, list));
        assertModelCount(User.class, 2);
    }
}
