package com.vitalityactive.va.questionnaire;

import com.vitalityactive.va.questionnaire.types.QuestionFactory;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class QuestionnaireStateManagerImplTest {
    @Mock
    QuestionnaireSetRepository repository;
    @Mock
    QuestionFactory factory;

    private QuestionnaireStateManagerImpl manager;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        manager = new QuestionnaireStateManagerImpl(repository, factory);
    }

    @Test
    public void creates_pre_populated_values_when_section_set() {
        verify(repository, times(0)).createAnswersForPrePopulatedValues(anyLong());

        manager.setQuestionnaireTypeKey(12);

        verify(repository, times(1)).createAnswersForPrePopulatedValues(anyLong());
        verify(repository, times(1)).createAnswersForPrePopulatedValues(12);
    }
}
