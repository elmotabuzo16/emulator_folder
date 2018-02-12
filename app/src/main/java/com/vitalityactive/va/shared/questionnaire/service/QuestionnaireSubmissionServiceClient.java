package com.vitalityactive.va.shared.questionnaire.service;

import com.vitalityactive.va.PartyInformationRepository;
import com.vitalityactive.va.authentication.AccessTokenAuthorizationProvider;
import com.vitalityactive.va.networking.WebServiceClient;
import com.vitalityactive.va.networking.WebServiceResponseParser;
import com.vitalityactive.va.questionnaire.types.Question;

import java.util.List;

import retrofit2.Call;

public class QuestionnaireSubmissionServiceClient {
    private AccessTokenAuthorizationProvider accessTokenAuthorizationProvider;
    private QuestionnaireSubmissionService service;
    private WebServiceClient webServiceClient;
    private PartyInformationRepository partyInformationRepository;

    public QuestionnaireSubmissionServiceClient(AccessTokenAuthorizationProvider accessTokenAuthorizationProvider,
                                                QuestionnaireSubmissionService service,
                                                WebServiceClient webServiceClient,
                                                PartyInformationRepository partyInformationRepository) {
        this.accessTokenAuthorizationProvider = accessTokenAuthorizationProvider;
        this.service = service;
        this.webServiceClient = webServiceClient;
        this.partyInformationRepository = partyInformationRepository;
    }

    public void submitQuestionnaire(long questionnaireSetTypeKey, List<Question> questions, long questionnaireTypeKey, WebServiceResponseParser<QuestionnaireSubmitResponse> parser) {
        QuestionnaireSubmitRequestBody questionnaireSubmitRequestBody =
                new QuestionnaireSubmitRequestBody(
                        questionnaireTypeKey,
                        questions);

        Call<QuestionnaireSubmitResponse> questionnaireSetRequest = service.getQuestionnaireSubmitRequest(accessTokenAuthorizationProvider.getAuthorization(),
                partyInformationRepository.getTenantId(),
                partyInformationRepository.getPartyId(),
                questionnaireSetTypeKey,
                questionnaireSubmitRequestBody);

        webServiceClient.executeAsynchronousRequest(questionnaireSetRequest, parser);
    }
}
