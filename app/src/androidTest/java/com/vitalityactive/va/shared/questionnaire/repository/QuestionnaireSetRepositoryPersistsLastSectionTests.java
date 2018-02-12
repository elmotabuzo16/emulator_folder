package com.vitalityactive.va.shared.questionnaire.repository;

import com.vitalityactive.va.RepositoryTestBase;
import com.vitalityactive.va.VitalityActiveApplication;
import com.vitalityactive.va.questionnaire.QuestionnaireSetRepository;
import com.vitalityactive.va.questionnaire.types.QuestionFactory;
import com.vitalityactive.va.shared.questionnaire.service.QuestionnaireSetResponse;
import com.vitalityactive.va.testutilities.annotations.RepositoryTests;
import com.vitalityactive.va.vhr.content.QuestionnaireContentFromResourceString;

import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RepositoryTests
public class QuestionnaireSetRepositoryPersistsLastSectionTests extends RepositoryTestBase {
    private QuestionnaireSetRepository repository;

    @Override
    public void setUp() throws IOException {
        super.setUp();
        setupRepository();
        loadQuestionnaire();
    }

    private void setupRepository() {
        final QuestionnaireContentFromResourceString content = new QuestionnaireContentFromResourceString(new VitalityActiveApplication());
        QuestionFactory questionFactory = new QuestionFactory(content);
        ValidationRuleMapper validationRuleFactory = new ValidationRuleMapper();
        repository = new QuestionnaireSetRepositoryImpl(dataStore, questionFactory, validationRuleFactory, 30);
    }

    private void loadQuestionnaire() throws IOException {
        final QuestionnaireSetResponse response = getResponse(QuestionnaireSetResponse.class, "vhr/with_3_sections.json");
        assertTrue(repository.persistQuestionnaireSetResponse(response));
    }

    @Test
    public void persists_last_seen_section_key() {
        repository.setLastSeenSection(3, 17);
        repository.setLastSeenSection(1, 6);

        assertEquals(17, repository.getLastSeenSection(3).intValue());
        assertEquals(6, repository.getLastSeenSection(1).intValue());
    }

    @Test
    public void getLastSeenSection_defaults_to_null() {
        assertNull(repository.getLastSeenSection(3));
        assertNull(repository.getLastSeenSection(1));
    }

    @Test
    public void get_visible_section_keys() {
        List<Long> result = repository.getVisibleSectionKeys(3);

        assertNotNull(result);
        assertEquals(4, result.size());
    }

    @Test
    public void get_visible_section_keys_sorted() {
        List<Long> result = repository.getVisibleSectionKeys(3);

        assertEquals(15, result.get(0).longValue());
        assertEquals(16, result.get(1).longValue());
        assertEquals(17, result.get(2).longValue());
        assertEquals(18, result.get(3).longValue());
    }

    @Test
    public void getCurrentSectionTypeKey_returns_last_seen() {
        repository.setLastSeenSection(3, 17);

        Long sectionTypeKey = repository.getCurrentSectionTypeKey(3);
        assertNotNull(sectionTypeKey);
        assertEquals(17, sectionTypeKey.longValue());
    }

    @Test
    public void getCurrentSectionTypeKey_defaults_to_first() {
        Long sectionTypeKey = repository.getCurrentSectionTypeKey(1);
        assertNotNull(sectionTypeKey);
        assertEquals(3, sectionTypeKey.longValue());
    }

    @Test
    public void clearSectionProgress_removed_last_seen() {
        repository.setLastSeenSection(3, 17);
        assertEquals(17, repository.getLastSeenSection(3).intValue());

        repository.clearSectionProgress(3);
        assertNull(repository.getLastSeenSection(3));
    }
}
