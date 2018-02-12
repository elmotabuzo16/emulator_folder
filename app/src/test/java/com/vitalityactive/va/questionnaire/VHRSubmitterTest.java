package com.vitalityactive.va.questionnaire;

import com.google.gson.Gson;
import com.vitalityactive.va.BaseTest;
import com.vitalityactive.va.NetworkingTestUtilities;
import com.vitalityactive.va.networking.WebServiceClient;
import com.vitalityactive.va.questionnaire.types.QuestionBasicInputValueDto;
import com.vitalityactive.va.questionnaire.types.TestQuestionFactory;
import com.vitalityactive.va.questionnaire.types.YesNoQuestionDto;
import com.vitalityactive.va.shared.questionnaire.service.QuestionnaireSubmitter;
import com.vitalityactive.va.shared.questionnaire.service.QuestionnaireSubmissionService;
import com.vitalityactive.va.shared.questionnaire.service.QuestionnaireSubmissionServiceClient;
import com.vitalityactive.va.shared.questionnaire.service.QuestionnaireSubmitRequestBody;
import com.vitalityactive.va.vhr.service.VHRSubmitterImpl;
import com.vitalityactive.va.myhealth.dto.VitalityAgeRepository;

import org.junit.Test;
import org.mockito.Mock;

import java.util.Arrays;

import okhttp3.mockwebserver.RecordedRequest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

public class VHRSubmitterTest extends BaseTest {

    @Mock
    QuestionnaireStateManager mockQuestionnaireStateManager;

    @Mock
    VitalityAgeRepository vitalityAgeRepository;

    @Override
    public void setUp() throws Exception {
        super.setUp();
    }

    @Test
    public void submits_correct_request() throws Exception {
        setUpMockWebServerWithSuccessResponse();
        QuestionnaireSubmitter submitter = new VHRSubmitterImpl(mockQuestionnaireStateManager, new QuestionnaireSubmissionServiceClient(mockAccessTokenAuthorizationProvider, NetworkingTestUtilities.getRetrofit(mockWebServer.url("").toString(), "expectedSessionId", "en_ZA").create(QuestionnaireSubmissionService.class), new WebServiceClient(sameThreadScheduler, eventDispatcher), getPartyInformationRepository()), eventDispatcher, vitalityAgeRepository);

        YesNoQuestionDto yesNoQuestion = (YesNoQuestionDto) TestQuestionFactory.cutDownOnDrinkAlcohol();
        yesNoQuestion.setValue(true);

        QuestionBasicInputValueDto basicInputQuestion = TestQuestionFactory.waistMeasurement(1);
        basicInputQuestion.setValue("123");
        basicInputQuestion.setSelectedUnit(basicInputQuestion.getUnits().get(0));

        when(mockQuestionnaireStateManager.getAllValidAnsweredQuestionsForQuestionnaire(1)).thenReturn(Arrays.asList(yesNoQuestion, basicInputQuestion));

        submitter.submit(1);

        RecordedRequest request = mockWebServer.takeRequest();
        QuestionnaireSubmitRequestBody requestBody = new Gson().fromJson(request.getBody().readUtf8(), QuestionnaireSubmitRequestBody.class);

        // Headers
        assertEquals("/vitality-manage-assessments-service-1/1.0/svc/2/captureAssessment/100002/1", request.getPath());
        assertEquals(AUTHORIZATION_HEADER, request.getHeader("Authorization"));
        assertEquals("application/json", request.getHeader("Content-Type"));
        assertEquals("expectedSessionId", request.getHeader("session-id"));
        assertEquals("en_ZA", request.getHeader("locale"));
        assertNotNull(request.getHeader("correlation-id"));

        QuestionnaireSubmitRequestBody.Assessment assessment = requestBody.assessments[0];
        assertEquals(1, assessment.typeKey);
        assertEquals(1, assessment.statusTypeKey);

        QuestionnaireSubmitRequestBody.AssessmentAnswer firstAnswer = assessment.assessmentAnswers[0];
        assertEquals(14, firstAnswer.questionIdentifier);
        assertEquals(3, firstAnswer.answerStatusTypeKey);
        assertEquals(0, firstAnswer.selectedValues[0].valueType);
        assertEquals("true", firstAnswer.selectedValues[0].value);
        assertEquals(null, firstAnswer.selectedValues[0].unitOfMeasure);

        QuestionnaireSubmitRequestBody.AssessmentAnswer secondAnswer = assessment.assessmentAnswers[1];
        assertEquals(1, secondAnswer.questionIdentifier);
        assertEquals(3, secondAnswer.answerStatusTypeKey);
        assertEquals(0, secondAnswer.selectedValues[0].valueType);
        assertEquals("123", secondAnswer.selectedValues[0].value);
        assertEquals("100111", secondAnswer.selectedValues[0].unitOfMeasure);
    }

}
