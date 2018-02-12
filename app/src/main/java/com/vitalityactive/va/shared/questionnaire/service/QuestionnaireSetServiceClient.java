package com.vitalityactive.va.shared.questionnaire.service;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.Gson;
import com.vitalityactive.va.PartyInformationRepository;
import com.vitalityactive.va.authentication.AccessTokenAuthorizationProvider;
import com.vitalityactive.va.cms.keyvaluecontent.FallbackContentInputStreamLoader;
import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.networking.RequestFailedEvent;
import com.vitalityactive.va.networking.WebServiceClient;
import com.vitalityactive.va.networking.WebServiceResponseParser;
import com.vitalityactive.va.questionnaire.QuestionnaireSetRepository;
import com.vitalityactive.va.utilities.FileUtilities;

import java.util.Arrays;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;

@Singleton
public class QuestionnaireSetServiceClient {
    private Call<QuestionnaireSetResponse> questionnaireSetRequest;
    private AccessTokenAuthorizationProvider accessTokenAuthorizationProvider;
    private QuestionnaireSetService service;
    private WebServiceClient webServiceClient;
    private PartyInformationRepository partyInformationRepository;
    private EventDispatcher eventDispatcher;
    private FallbackContentInputStreamLoader fallbackContentInputStreamLoader;

    @Inject
    public QuestionnaireSetServiceClient(AccessTokenAuthorizationProvider accessTokenAuthorizationProvider,
                                         QuestionnaireSetService service,
                                         WebServiceClient webServiceClient,
                                         PartyInformationRepository partyInformationRepository,
                                         EventDispatcher eventDispatcher,
                                         FallbackContentInputStreamLoader fallbackContentInputStreamLoader) {
        this.accessTokenAuthorizationProvider = accessTokenAuthorizationProvider;
        this.service = service;
        this.webServiceClient = webServiceClient;
        this.partyInformationRepository = partyInformationRepository;
        this.eventDispatcher = eventDispatcher;
        this.fallbackContentInputStreamLoader = fallbackContentInputStreamLoader;
    }

    @NonNull
    protected WebServiceResponseParser<QuestionnaireSetResponse> buildWebServiceResponseParser(final QuestionnaireSetRepository questionnaireSetRepository) {
        return new WebServiceResponseParser<QuestionnaireSetResponse>() {
            @Override
            public void parseResponse(QuestionnaireSetResponse questionnaireSetResponse) {
                Log.e("cjc","parseResponse");
                Log.e("cjc","QuestionnaireSetResponse: "+String.valueOf(questionnaireSetResponse.questionnaireSetTypeCode));
                if (questionnaireSetRepository.persistQuestionnaireSetResponse(questionnaireSetResponse)) {
                    Log.e("cjc","totalQuestionnaires: "+questionnaireSetResponse.totalQuestionnaires);
                    eventDispatcher.dispatchEvent(new QuestionnaireSetRequestSuccess());
                } else {
                    Log.e("cjc","FAILED: ");
                    eventDispatcher.dispatchEvent(new RequestFailedEvent(RequestFailedEvent.Type.GENERIC_ERROR));
                }
            }

            @Override
            public void parseErrorResponse(String errorBody, int code) {
                Log.e("cjc","parseErrorResponse"+ errorBody);
                eventDispatcher.dispatchEvent(new RequestFailedEvent(RequestFailedEvent.Type.GENERIC_ERROR));
            }

            @Override
            public void handleGenericError(Exception exception) {
                Log.e("cjc","parseErrorResponse"+ exception.getMessage());
                eventDispatcher.dispatchEvent(new RequestFailedEvent(RequestFailedEvent.Type.GENERIC_ERROR));
            }

            @Override
            public void handleConnectionError() {
                Log.e("cjc","handleConnectionError");
                eventDispatcher.dispatchEvent(new RequestFailedEvent(RequestFailedEvent.Type.CONNECTION_ERROR));
            }
        };
    }

    public void fetchQuestionnaireSets(long questionnaireSetTypeKey, Integer[] questionnaireTypeKeys, QuestionnaireSetRepository questionnaireSetRepository) {
        Log.e("cjc","QuestionnaireSetServiceClient: "+String.valueOf(accessTokenAuthorizationProvider.getAuthorization()));
        Log.e("cjc","getTenantId: "+String.valueOf(partyInformationRepository.getTenantId()));
        Log.e("cjc","getPartyId: "+String.valueOf(partyInformationRepository.getPartyId()));
        Log.e("cjc","getVitalityMembershipId: "+String.valueOf(partyInformationRepository.getVitalityMembershipId()));
        //questionnaireTypeKeys =new Integer[] {7,8,9};
        Log.e("cjc","questionnaireSetTypeKey: "+questionnaireSetTypeKey+" questionnaireTypeKeys: "+ Arrays.toString(questionnaireTypeKeys));

        questionnaireSetRequest =
                service.getQuestionnaireSetsRequest(accessTokenAuthorizationProvider.getAuthorization(),
                        partyInformationRepository.getTenantId(),
                        partyInformationRepository.getPartyId(),
                        partyInformationRepository.getVitalityMembershipId(),
                        new QuestionnaireSetRequestBody(questionnaireSetTypeKey,questionnaireTypeKeys));
        webServiceClient.executeAsynchronousRequest(questionnaireSetRequest,
                buildWebServiceResponseParser(questionnaireSetRepository));

        /*Used only for development*/
        //cjc
       //new GetMockJsonData(questionnaireSetRepository).execute();
    }

    public void fetchQuestionnaireSets(long questionnaireSetTypeKey, QuestionnaireSetRepository questionnaireSetRepository) {
        fetchQuestionnaireSets(questionnaireSetTypeKey, null, questionnaireSetRepository);
    }

    private class GetMockJsonData extends AsyncTask<Void, Void, QuestionnaireSetResponse> {
        private final QuestionnaireSetRepository repository;

        private GetMockJsonData(QuestionnaireSetRepository questionnaireSetRepository) {
            repository = questionnaireSetRepository;
        }

        @Override
        protected QuestionnaireSetResponse doInBackground(Void... voids) {
            QuestionnaireSetResponse response;

            Gson gson = new Gson();
            //cjc
           /* response = gson.fromJson(FileUtilities.readJSONFromAsset(fallbackContentInputStreamLoader.open("questionnaireProgressAndPointsTracking.json")),
                    QuestionnaireSetResponse.class);*/
            response = gson.fromJson(FileUtilities.readJSONFromAsset(fallbackContentInputStreamLoader.open("MWBquestionnaireProgressAndPointsTracking.json")),
                    QuestionnaireSetResponse.class);
            repository.persistQuestionnaireSetResponse(response);

            return response;
        }

        @Override
        protected void onPostExecute(QuestionnaireSetResponse questionnaireSetResponse) {
            super.onPostExecute(questionnaireSetResponse);
            eventDispatcher.dispatchEvent(new QuestionnaireSetRequestSuccess());
        }
    }
}
