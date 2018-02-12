package com.vitalityactive.va.shared.questionnaire.repository;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Pair;

import com.vitalityactive.va.persistence.DataStore;
import com.vitalityactive.va.persistence.Model;
import com.vitalityactive.va.questionnaire.Answer;
import com.vitalityactive.va.questionnaire.QuestionnaireSection;
import com.vitalityactive.va.questionnaire.QuestionnaireSetRepository;
import com.vitalityactive.va.questionnaire.types.Question;
import com.vitalityactive.va.questionnaire.types.QuestionFactory;
import com.vitalityactive.va.questionnaire.validations.ValidationRule;
import com.vitalityactive.va.shared.questionnaire.dto.QuestionnaireDTO;
import com.vitalityactive.va.shared.questionnaire.dto.QuestionnaireSetInformation;
import com.vitalityactive.va.shared.questionnaire.repository.model.AnswerModel;
import com.vitalityactive.va.shared.questionnaire.repository.model.PopulationValueModel;
import com.vitalityactive.va.shared.questionnaire.repository.model.QuestionModel;
import com.vitalityactive.va.shared.questionnaire.repository.model.QuestionnaireLastSeenModel;
import com.vitalityactive.va.shared.questionnaire.repository.model.QuestionnaireModel;
import com.vitalityactive.va.shared.questionnaire.repository.model.QuestionnaireSectionModel;
import com.vitalityactive.va.shared.questionnaire.repository.model.QuestionnaireSetModel;
import com.vitalityactive.va.shared.questionnaire.repository.model.ValidValueModel;
import com.vitalityactive.va.shared.questionnaire.service.QuestionnaireSetResponse;
import com.vitalityactive.va.utilities.MethodCallTrace;
import com.vitalityactive.va.utilities.TimeUtilities;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.realm.RealmList;
import io.realm.RealmQuery;
import io.realm.RealmResults;

public class QuestionnaireSetRepositoryImpl implements QuestionnaireSetRepository {
    private final DataStore dataStore;
    private final QuestionFactory questionFactory;
    private final ValidationRuleMapper validationRuleFactory;
    private final int longAgoDays;
    @SuppressLint("UseSparseArrays")
    Map<Long, List<ValidationRule>> cachedValidationRules = new HashMap<>();
    @SuppressLint("UseSparseArrays")
    Map<Long, List<Question>> cachedQuestions = new HashMap<>();
    @SuppressLint("UseSparseArrays")
    Map<Long, Long> cachedLastSeenSection = new HashMap<>();

    public QuestionnaireSetRepositoryImpl(DataStore dataStore, QuestionFactory questionFactory, ValidationRuleMapper validationRuleFactory, int longAgoDays) {
        this.dataStore = dataStore;
        this.questionFactory = questionFactory;
        this.validationRuleFactory = validationRuleFactory;
        this.longAgoDays = longAgoDays;
    }

    @Override
    public Boolean persistQuestionnaireSetResponse(QuestionnaireSetResponse response) {
        removePreviousModels();

        if (response.questionnaire == null)
            return false;

        if (!parseQuestionnaireSetResponse(response)) {
            // todo: clear all? that will remove the user entered answers
            // dataStore.clear();
            removePreviousModels();
            return false;
        }

        return true;
    }

    @Override
    public void createAnswersForPrePopulatedValues(long questionnaireTypeKey) {
        List<AnswerModel> answers = dataStore.getModelInstance(QuestionnaireModel.class, new DataStore.ModelMapper<QuestionnaireModel, List<AnswerModel>>() {
            @Override
            public List<AnswerModel> mapModel(QuestionnaireModel questionnaire) {
                ArrayList<AnswerModel> mapped = new ArrayList<>();
                for (QuestionnaireSectionModel section : questionnaire.questionnaireSection) {
                    addPrePopulatedValues(mapped, section);
                }
                return mapped;
            }

            private void addPrePopulatedValues(ArrayList<AnswerModel> mapped, QuestionnaireSectionModel section) {
                for (QuestionModel question : section.questions) {
                    addPrePopulatedValues(mapped, question);
                }
            }

            private void addPrePopulatedValues(ArrayList<AnswerModel> mapped, QuestionModel question) {
                RealmList<PopulationValueModel> answerPrePopulatedValues = question.populationValues;
                if (answerPrePopulatedValues != null && answerPrePopulatedValues.size() > 0) {
                    mapped.add(new AnswerModel(answerPrePopulatedValues));
                }
            }
        }, "typeKey", questionnaireTypeKey);

        if (answers == null || answers.size() == 0) {
            return;
        }
        List<AnswerModel> modelsToCreate = getListOfAnswersNotAlreadySaved(answers);
        dataStore.addOrUpdate(modelsToCreate);
    }

    @NonNull
    private List<AnswerModel> getListOfAnswersNotAlreadySaved(List<AnswerModel> answers) {
        Map<Long, Answer> currentlySavedAnswers = loadAnswers();
        List<AnswerModel> modelsToCreate = new ArrayList<>();
        for (AnswerModel answer : answers) {
            if (!currentlySavedAnswers.containsKey(answer.questionId)) {
                modelsToCreate.add(answer);
            }
        }
        return modelsToCreate;
    }

    private void removePreviousModels() {
        dataStore.removeAll(QuestionnaireSetModel.class);
        dataStore.removeAll(QuestionnaireModel.class);
        dataStore.removeAll(QuestionnaireSectionModel.class);
        dataStore.removeAll(QuestionModel.class);
        dataStore.removeAll(ValidValueModel.class);
    }

    @Override
    public long getNumberOfVisibleQuestionnaireSections(long questionnaireTypeKey) {
        return dataStore.getNumberOfModels(QuestionnaireSectionModel.class,
                "questionnaire.typeKey", questionnaireTypeKey,
                "isVisible", true);
    }

    @Override
    public QuestionnaireSetInformation getQuestionnairesSetTopLevelData() {
        return dataStore.getFirstModelInstance(QuestionnaireSetModel.class, new DataStore.ModelMapper<QuestionnaireSetModel, QuestionnaireSetInformation>() {
            @Override
            public QuestionnaireSetInformation mapModel(QuestionnaireSetModel model) {
                return new QuestionnaireSetInformation(model.totalQuestionnaireCompleted,
                        model.questionnaireSetCompleted,
                        model.totalPotentialPoints,
                        model.totalQuestionnaires,
                        model.footer,
                        model.description,
                        model.text,
                        model.totalEarnedPoints);
            }
        });
    }

    @Override
    public List<QuestionnaireSection> getQuestionnaireSections(final long questionnaireTypeKey) {
        return dataStore.getModels(QuestionnaireSectionModel.class, new DataStore.QueryExecutor<QuestionnaireSectionModel, RealmQuery<QuestionnaireSectionModel>>() {
            @Override
            public List<QuestionnaireSectionModel> executeQueries(RealmQuery<QuestionnaireSectionModel> initialQuery) {
                return initialQuery.equalTo("questionnaire.typeKey", questionnaireTypeKey).findAllSorted("sortOrderIndex");
            }
        }, new DataStore.ModelListMapper<QuestionnaireSectionModel, QuestionnaireSection>() {
            @Override
            public List<QuestionnaireSection> mapModels(List<QuestionnaireSectionModel> models1) {
                List<QuestionnaireSection> sectionTypeKeys = new ArrayList<>();
                for (QuestionnaireSectionModel model : models1) {
                    sectionTypeKeys.add(buildQuestionnaireSection(model));
                }
                return sectionTypeKeys;
            }
        });
    }

    @Override
    public int updateQuestionnaireSectionVisibility(List<QuestionnaireSection> sections) {
        @SuppressLint("UseSparseArrays") final Map<Long, Boolean> toUpdate = new HashMap<>();
        List<Long> ids = new ArrayList<>();
        for (QuestionnaireSection section : sections) {
            ids.add(section.typeKey);
            toUpdate.put(section.typeKey, section.isVisible);
        }
        final int[] updated = {0};

        dataStore.setFieldValue(QuestionnaireSectionModel.class, "typeKey", ids, new DataStore.FieldUpdater<QuestionnaireSectionModel>() {
            @Override
            public void updateField(@Nullable QuestionnaireSectionModel model) {
                if (model != null) {
                    Boolean newValue = toUpdate.get(model.typeKey);
                    if (model.isVisible != newValue) {
                        updated[0]++;
                        model.isVisible = newValue;
                    }
                }
            }
        });
        return updated[0];
    }

    @Override
    public List<QuestionnaireDTO> getQuestionnairesTopLevelData() {
        return dataStore.getModels(QuestionnaireModel.class, new DataStore.QueryExecutor<QuestionnaireModel, RealmQuery<QuestionnaireModel>>() {
            @Override
            public List<QuestionnaireModel> executeQueries(RealmQuery<QuestionnaireModel> initialQuery) {
                return initialQuery.findAllSorted("sortOrderIndex");
            }
        }, getModelListMapperForQuestionnaireModel());
    }

    @Override
    public List<Long> getVisibleSectionKeys(final long questionnaireKey) {
        return dataStore.getModels(QuestionnaireSectionModel.class, new DataStore.QueryExecutor<QuestionnaireSectionModel, RealmQuery<QuestionnaireSectionModel>>() {
            @Override
            public List<QuestionnaireSectionModel> executeQueries(RealmQuery<QuestionnaireSectionModel> initialQuery) {
                return initialQuery
                        .equalTo("questionnaire.typeKey", questionnaireKey)
                        .equalTo("isVisible", true)
                        .findAllSorted("sortOrderIndex");
            }
        }, new DataStore.ModelListMapper<QuestionnaireSectionModel, Long>() {
            @Override
            public List<Long> mapModels(List<QuestionnaireSectionModel> models1) {
                List<Long> sectionTypeKeys = new ArrayList<>();
                for (QuestionnaireSectionModel model : models1) {
                    sectionTypeKeys.add(model.getTypeKey());
                }
                return sectionTypeKeys;
            }
        });
    }

    @Override
    public List<QuestionnaireDTO> getUnansweredQuestionnairesTopLevelData() {
        return dataStore.getModels(QuestionnaireModel.class, new DataStore.QueryExecutor<QuestionnaireModel, RealmQuery<QuestionnaireModel>>() {
            @Override
            public List<QuestionnaireModel> executeQueries(RealmQuery<QuestionnaireModel> initialQuery) {
                return initialQuery
                        .equalTo("completionFlag", false)
                        .findAllSorted("sortOrderIndex");
            }
        }, getModelListMapperForQuestionnaireModel());
    }

    @NonNull
    private DataStore.ModelListMapper<QuestionnaireModel, QuestionnaireDTO> getModelListMapperForQuestionnaireModel() {
        return new DataStore.ModelListMapper<QuestionnaireModel, QuestionnaireDTO>() {
            @Override
            public List<QuestionnaireDTO> mapModels(List<QuestionnaireModel> models) {
                List<QuestionnaireDTO> list = new ArrayList<>();
                for (QuestionnaireModel questionnaireModel : models) {
                    list.add(new QuestionnaireDTO(questionnaireModel.typeName,
                            questionnaireModel.textDescription,
                            questionnaireModel.completionFlag,
                            getQuestionnaireIsInProgress(questionnaireModel.typeKey),
                            questionnaireModel.typeKey));
                }
                return list;
            }
        };
    }

    private boolean getQuestionnaireIsInProgress(long questionnaireTypeKey) {
        List<Question> allQuestionsInQuestionnaire = getAllQuestionsInQuestionnaire(questionnaireTypeKey);

        for (Question question : allQuestionsInQuestionnaire) {
            if (dataStore.hasModelInstance(AnswerModel.class, "questionId", question.getIdentifier())) {
                return true;
            }
        }

        return false;
    }

    @Override
    public void persistAnswer(long questionId, Answer answer, long answeredTimestamp) {
        AnswerModel answerModel = new AnswerModel(questionId, answer, answeredTimestamp);
        dataStore.addOrUpdate(Collections.singletonList(answerModel));
    }

    @Override
    public Map<Long, Answer> loadAnswers() {
        final long answeredToLongAgo = getLongAgoDate().getTime();
        List<Pair<Long, Answer>> pairs = dataStore.getModels(AnswerModel.class, new DataStore.ModelListMapper<AnswerModel, Pair<Long, Answer>>() {
            @Override
            public List<Pair<Long, Answer>> mapModels(List<AnswerModel> models) {
                List<Pair<Long, Answer>> mapped = new ArrayList<>();
                for (AnswerModel model : models) {
                    if (model.answeredTimestamp > answeredToLongAgo) {
                        mapped.add(new Pair<>(model.questionId, model.getAnswer()));
                    }
                }
                return mapped;
            }
        });

        @SuppressLint("UseSparseArrays") Map<Long, Answer> map = new HashMap<>();
        for (Pair<Long, Answer> pair : pairs) {
            map.put(pair.first, pair.second);
        }
        return map;
    }

    private Date getLongAgoDate() {
        return TimeUtilities.getAdjustedTime(Calendar.DATE, -longAgoDays);
    }

    @Override
    public void setLastSeenSection(long questionnaireKey, long sectionTypeKey) {
        QuestionnaireLastSeenModel progress = new QuestionnaireLastSeenModel(questionnaireKey, sectionTypeKey);
        dataStore.addOrUpdate(Collections.singletonList(progress));
        cachedLastSeenSection.put(questionnaireKey, sectionTypeKey);
    }

    @Override
    public Long getLastSeenSection(long questionnaireKey) {
        if (cachedLastSeenSection.containsKey(questionnaireKey)) {
            return cachedLastSeenSection.get(questionnaireKey);
        }

        Long result = dataStore.getFieldValue(QuestionnaireLastSeenModel.class,
                "questionnaireKey", questionnaireKey,
                new DataStore.FieldAccessor<QuestionnaireLastSeenModel, Long>() {
                    @NonNull
                    @Override
                    public Long getField(@Nullable QuestionnaireLastSeenModel model) {
                        return model == null ? -1 : model.getSectionTypeKey();
                    }
                });
        if (result == -1)
            return null;

        cachedLastSeenSection.put(questionnaireKey, result);
        return result;
    }

    @Override
    public boolean doesQuestionExist(final long questionTypeKey, final long questionnaireTypeKey, final long lastSeenSectionTypeKey) {
        List<Question> questions = getAllQuestionsInQuestionnaire(questionnaireTypeKey);
        int count = 0;

        for (Question question : questions) {

            //TODO: jay: are you sure if this additional contition? question.sectionTypeKey == 0
            if (question.getIdentifier() == questionTypeKey && (question.sectionTypeKey == lastSeenSectionTypeKey || question.sectionTypeKey == 0)) {
                ++count;
            }
        }
        return count > 0;
    }

    @NonNull
    @Override
    public String getQuestionnaireSectionTitle(long questionnaireTypeKey, int sectionIndex) {
        QuestionnaireSection section = getVisibleQuestionnaireSectionAtIndex(sectionIndex, questionnaireTypeKey);
        return section == null ? "" : section.title;
    }

    @Override
    public List<Question> getAllQuestionsInVisibleSectionAtIndex(final long questionnaireTypeKey, final int sectionIndex) {
        return dataStore.getModels(QuestionModel.class, new DataStore.QueryExecutor<QuestionModel, RealmQuery<QuestionModel>>() {
            @Override
            public List<QuestionModel> executeQueries(RealmQuery<QuestionModel> initialQuery) {
                long sectionTypeKey = getVisibleQuestionnaireSectionTypeKeyAtIndex(sectionIndex, questionnaireTypeKey);
                if (sectionTypeKey == -1) {
                    return new ArrayList<>();
                }
                return initialQuery.equalTo("section.typeKey", sectionTypeKey)
                        .findAllSorted("sortOrderIndex");
            }
        }, questionFactory);
    }

    @Override
    public List<ValidationRule> getQuestionValidationRules(final long questionTypeKey) {
        if (cachedValidationRules.isEmpty()) {
            loadAllValidationRulesIntoCache();
        }
        return cachedValidationRules.get(questionTypeKey);
    }

    private void loadAllValidationRulesIntoCache() {
        long trace = MethodCallTrace.enterMethod();
        cachedValidationRules.clear();
        dataStore.getModels(QuestionModel.class, new DataStore.ModelListMapper<QuestionModel, List<ValidationRule>>() {
            @Override
            public List<List<ValidationRule>> mapModels(List<QuestionModel> questionModels) {
                for (QuestionModel questionModel : questionModels) {
                    List<ValidationRule> questionRules = validationRuleFactory.mapModels(questionModel.validValues);
                    cachedValidationRules.put(questionModel.typeKey, questionRules);
                }

                return new ArrayList<>(cachedValidationRules.values());
            }
        });
        MethodCallTrace.exitMethod(trace);
    }

    @Override
    public String getQuestionnaireTitle(final long questionnaireTypeKey) {
        return dataStore.getFieldValue(QuestionnaireModel.class,
                new DataStore.SingleModelQueryExecutor<QuestionnaireModel, RealmQuery<QuestionnaireModel>>() {
                    @Override
                    public QuestionnaireModel executeQueries(RealmQuery<QuestionnaireModel> initialQuery) {
                        return initialQuery.equalTo("typeKey", questionnaireTypeKey).findFirst();
                    }
                }, new DataStore.FieldAccessor<QuestionnaireModel, String>() {
                    @NonNull
                    @Override
                    public String getField(@Nullable QuestionnaireModel model) {
                        return model == null ? "MISSING QUESTIONNAIRE" : model.typeName;
                    }
                });
    }

    @Override
    public List<Question> getAllQuestionsInQuestionnaire(final long questionnaireTypeKey) {
        if (cachedQuestions.containsKey(questionnaireTypeKey)) {
            return cachedQuestions.get(questionnaireTypeKey);
        }

        List<Question> questions = dataStore.getModels(QuestionModel.class, new DataStore.QueryExecutor<QuestionModel, RealmQuery<QuestionModel>>() {
            @Override
            public List<QuestionModel> executeQueries(RealmQuery<QuestionModel> initialQuery) {
                return initialQuery.equalTo("section.questionnaire.typeKey", questionnaireTypeKey).findAll();
            }
        }, questionFactory);
        cachedQuestions.put(questionnaireTypeKey, questions);
        return questions;
    }

    @Override
    public void clearSectionProgress(long questionnaireKey) {
        cachedLastSeenSection.clear();
        dataStore.remove(QuestionnaireLastSeenModel.class, "questionnaireKey", questionnaireKey);
    }

    @Override
    public void clearAnswers(final long questionnaireTypeKey) {
        List<QuestionnaireSection> dtos = dataStore.getModels(QuestionnaireSectionModel.class, new DataStore.QueryExecutor<QuestionnaireSectionModel, RealmQuery<QuestionnaireSectionModel>>() {
            @Override
            public List<QuestionnaireSectionModel> executeQueries(RealmQuery<QuestionnaireSectionModel> initialQuery) {
                return initialQuery.equalTo("questionnaire.typeKey", questionnaireTypeKey).findAll();
            }
        }, new DataStore.ModelListMapper<QuestionnaireSectionModel, QuestionnaireSection>() {
            @Override
            public List<QuestionnaireSection> mapModels(List<QuestionnaireSectionModel> models) {
                List<QuestionnaireSection> dtos = new ArrayList<>();

                if (models != null) {
                    for (QuestionnaireSectionModel model : models) {
                        dtos.add(buildQuestionnaireSection(model));
                    }
                }

                return dtos;
            }
        });

        for (QuestionnaireSection dto : dtos) {
            for (Long questionTypeKey : dto.questionTypeKeys) {
                dataStore.remove(AnswerModel.class, "questionId", questionTypeKey);
            }
        }
    }

    @Override
    public String getSectionDescription(long sectionTypeKey) {
        return dataStore.getFieldValue(QuestionnaireSectionModel.class, "typeKey", sectionTypeKey, new DataStore.FieldAccessor<QuestionnaireSectionModel, String>() {
            @NonNull
            @Override
            public String getField(@Nullable QuestionnaireSectionModel model) {
                return model != null && model.textDescription != null ? model.textDescription : "";
            }
        });
    }

    @Override
    public Long getCurrentSectionTypeKey(long questionnaireTypeKey) {
        Long section = getLastSeenSection(questionnaireTypeKey);
        if (section == null) {
            List<Long> visibleSectionKeys = getVisibleSectionKeys(questionnaireTypeKey);
            section = visibleSectionKeys.isEmpty() ? 0L : visibleSectionKeys.get(0);
        }
        return section;
    }

    @Override
    public void setQuestionnaireCompletedFlagToTrue(long questionnaireTypeKey) {
        dataStore.setFieldValue(QuestionnaireModel.class, "typeKey", questionnaireTypeKey, new DataStore.FieldUpdater<QuestionnaireModel>() {
            @Override
            public void updateField(@Nullable QuestionnaireModel model) {
                if (model != null) {
                    model.completionFlag = true;
                }
            }
        });
    }

    @Override
    public void prepare() {
        long trace = MethodCallTrace.enterMethod();
        loadAllValidationRulesIntoCache();
        for (QuestionnaireDTO questionnaire : getQuestionnairesTopLevelData()) {
            getAllQuestionsInQuestionnaire(questionnaire.getTypeKey());
        }
        MethodCallTrace.exitMethod(trace);
    }

    private long getVisibleQuestionnaireSectionTypeKeyAtIndex(final int sectionIndex, final long questionnaireTypeKey) {
        if (sectionIndex < 0) {
            return -1;
        }

        return dataStore.getFieldValue(QuestionnaireSectionModel.class, new DataStore.SingleModelQueryExecutor<QuestionnaireSectionModel, RealmQuery<QuestionnaireSectionModel>>() {
            @Override
            public QuestionnaireSectionModel executeQueries(RealmQuery<QuestionnaireSectionModel> initialQuery) {
                RealmResults<QuestionnaireSectionModel> allSorted = initialQuery
                        .equalTo("questionnaire.typeKey", questionnaireTypeKey)
                        .equalTo("isVisible", true)
                        .findAllSorted("sortOrderIndex");
                if (sectionIndex >= allSorted.size()) {
                    return null;
                }
                return allSorted.get(sectionIndex);
            }
        }, new DataStore.FieldAccessor<QuestionnaireSectionModel, Long>() {
            @NonNull
            @Override
            public Long getField(@Nullable QuestionnaireSectionModel model) {
                if (model == null) {
                    return -1L;
                }
                return model.typeKey;
            }
        });
    }

    @Nullable
    private QuestionnaireSection getVisibleQuestionnaireSectionAtIndex(final int sectionIndex, final long questionnaireTypeKey) {
        if (sectionIndex < 0) {
            return null;
        }

        return dataStore.getModelInstance(QuestionnaireSectionModel.class, new DataStore.SingleModelQueryExecutor<QuestionnaireSectionModel, RealmQuery<QuestionnaireSectionModel>>() {
            @Override
            public QuestionnaireSectionModel executeQueries(RealmQuery<QuestionnaireSectionModel> initialQuery) {
                RealmResults<QuestionnaireSectionModel> allSorted = initialQuery
                        .equalTo("questionnaire.typeKey", questionnaireTypeKey)
                        .equalTo("isVisible", true)
                        .findAllSorted("sortOrderIndex");
                if (sectionIndex >= allSorted.size()) {
                    return null;
                }
                return allSorted.get(sectionIndex);
            }
        }, new DataStore.ModelMapper<QuestionnaireSectionModel, QuestionnaireSection>() {
            @Override
            public QuestionnaireSection mapModel(QuestionnaireSectionModel model1) {
                return buildQuestionnaireSection(model1);
            }
        });
    }

    @NonNull
    private QuestionnaireSection buildQuestionnaireSection(QuestionnaireSectionModel model) {
        boolean isVisible = model.isVisible == null ? true : model.isVisible;
        QuestionnaireSection section = new QuestionnaireSection(model.getTypeKey(), model.getTitle(), model.visibilityTagName, isVisible);
        if (model.questions != null) {
            for (QuestionModel question : model.questions) {
                section.questionTypeKeys.add(question.typeKey);
            }
        }
        return section;
    }

    private Boolean parseQuestionnaireSetResponse(QuestionnaireSetResponse response) {
        try {
            return parseModels(response, new InstanceCreator<Model, QuestionnaireSetResponse>() {
                @Override
                public Model create(QuestionnaireSetResponse model) {
                    return QuestionnaireSetModel.create(model);
                }
            });
        } catch (RuntimeException ignored) {
            return false;
        }
    }

    private <T extends Model, U> boolean parseModels(U model, InstanceCreator<T, U> instanceCreator) {
        if (model == null) {
            return false;
        }

        T instance = instanceCreator.create(model);
        return instance != null && dataStore.add(instance);

    }

    private interface InstanceCreator<T extends Model, U> {
        T create(U model);
    }
}
