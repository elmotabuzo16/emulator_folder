package com.vitalityactive.va.questionnaire;

import android.annotation.SuppressLint;

import com.vitalityactive.va.questionnaire.dependencies.DependencyChecker;
import com.vitalityactive.va.questionnaire.types.Question;
import com.vitalityactive.va.questionnaire.types.QuestionFactory;
import com.vitalityactive.va.questionnaire.types.TestQuestionFactory;
import com.vitalityactive.va.questionnaire.validations.ValidationResult;
import com.vitalityactive.va.utilities.TimeUtilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unused")
public class DummyQuestionnaireStateManager implements QuestionnaireStateManager {
    private final List<Long> sectionIds;
    @SuppressLint("UseSparseArrays")
    private final Map<Long, List<Question>> questions = new HashMap<>();
    private final QuestionnaireSetRepository repository;
    private final QuestionFactory questionFactory;
    private int currentSectionIndex;

    public DummyQuestionnaireStateManager(QuestionnaireSetRepository repository,
                                          QuestionnaireUnitOfMeasureContent content) {
        this(repository, new QuestionFactory(content));
    }

    public DummyQuestionnaireStateManager(QuestionnaireSetRepository repository, QuestionFactory questionFactory) {
        this.repository = repository;
        this.questionFactory = questionFactory;
        sectionIds = new ArrayList<>();
        buildDummyData();
        loadAnswers();
        updateCanBeAnswered();
    }

    private void loadAnswers() {
        ArrayList<Question> flatQuestionList = new ArrayList<>();
        for (List<Question> list : questions.values()) {
            flatQuestionList.addAll(list);
        }
        checkQuestionIdsAreUnique(flatQuestionList);
        Map<Long, Answer> answers = repository.loadAnswers();
        questionFactory.loadAnswers(flatQuestionList, answers);
    }

    @SuppressLint("UseSparseArrays")
    private void checkQuestionIdsAreUnique(ArrayList<Question> flatQuestionList) {
        HashMap<Long, Question> map = new HashMap<>();
        for (Question question : flatQuestionList) {
            if (question == null) {
                throw new RuntimeException("null question in list");
            }
            if (map.containsKey(question.getIdentifier())) {
                throw new RuntimeException("dummy data setup issue: multiple questions with the same id: " + question.getIdentifier());
            }
            map.put(question.getIdentifier(), question);
        }
    }

    private void buildDummyDataWithFactory() {
        List<Question> factoryCreatedQuestions = new ArrayList<>();

        factoryCreatedQuestions.add(TestQuestionFactory.labelQuestion(questionFactory));
        factoryCreatedQuestions.add(TestQuestionFactory.numberRangeTextBoxQuestion(questionFactory));
        factoryCreatedQuestions.add(TestQuestionFactory.singleSelectYesNoQuestion(questionFactory));
        factoryCreatedQuestions.add(TestQuestionFactory.dependantNumberRangeQuestion(questionFactory));
        factoryCreatedQuestions.add(TestQuestionFactory.freeTextTextBoxQuestion(questionFactory));
        factoryCreatedQuestions.add(TestQuestionFactory.singleSelectOptionQuestion(questionFactory));
        factoryCreatedQuestions.add(TestQuestionFactory.datePickerQuestion(questionFactory));
        factoryCreatedQuestions.add(TestQuestionFactory.multiSelectCheckboxQuestion(questionFactory));
        factoryCreatedQuestions.add(TestQuestionFactory.singleCheckBoxQuestion(questionFactory));

        while (true) {
            if (!factoryCreatedQuestions.remove(null))
                break;
            // null items removed from dummy data
        }

        sectionIds.add(99999L);
        this.questions.put(99999L, factoryCreatedQuestions);
    }

    private void buildDummyData() {
        questions.clear();
        buildUiSection();
        buildOverallSection();
        buildMedicalHistorySection();
        buildSectionFamilyMedicalHistory();
        buildDummyDataWithFactory();
    }

    private void buildUiSection() {
        List<Question> questions = new ArrayList<>();
        questions.add(TestQuestionFactory.fullYesNo());
        questions.add(TestQuestionFactory.fullTypeOfUi());
        questions.add(TestQuestionFactory.fullText());
        questions.add(TestQuestionFactory.fullText2(1));
        questions.add(TestQuestionFactory.fullText2(2));
        questions.add(TestQuestionFactory.fullText2(3));
        questions.add(TestQuestionFactory.fullText3());
        questions.add(TestQuestionFactory.layoutTypes());
        questions.add(TestQuestionFactory.layoutWithAllItems());
        questions.add(TestQuestionFactory.layoutWithNoDetail());
        questions.add(TestQuestionFactory.layoutWithNoFooter());
        questions.add(TestQuestionFactory.layoutWithNoTitle());
        questions.add(TestQuestionFactory.headerLabel(9));

        sectionIds.add(0L);
        this.questions.put(0L, questions);
    }

    private void buildOverallSection() {
        List<Question> questions = TestQuestionFactory.basicList(100);
        questions.add(1, TestQuestionFactory.allergies());

        sectionIds.add(1L);
        this.questions.put(1L, questions);
    }

    private void buildMedicalHistorySection() {
        List<Question> questions = TestQuestionFactory.basicList(200);
        questions.add(1, TestQuestionFactory.doYouUseTobaccoProducts());

        sectionIds.add(2L);
        this.questions.put(2L, questions);
    }

    private void buildSectionFamilyMedicalHistory() {
        List<Question> questions = TestQuestionFactory.basicList(300);
        questions.add(1, TestQuestionFactory.drinkAlcohol());
        questions.add(2, TestQuestionFactory.cutDownOnDrinkAlcohol());
        questions.add(2, TestQuestionFactory.cutDownOnDrinkAlcoholHowMuch());

        sectionIds.add(3L);
        this.questions.put(3L, questions);
    }

    @Override
    public long getTotalSections() {
        return sectionIds.size();
    }

    @Override
    public int getCurrentSectionIndex() {
        return currentSectionIndex;
    }

    @Override
    public boolean goToNextSection() {
        if (currentSectionIndex < getTotalSections())
            currentSectionIndex++;
        if (currentSectionIndex == getTotalSections()) {
            currentSectionIndex--;
            return false;
        }
        return true;
    }

    @Override
    public boolean goToPreviousSection() {
        if (currentSectionIndex == 0) {
            // dummy state manager reloads the data on back from first page
            sectionIds.clear();
            buildDummyData();
            loadAnswers();
            updateCanBeAnswered();
            return false;
        }

        currentSectionIndex--;
        return true;
    }

    @Override
    public List<Question> getAllQuestionsInCurrentSection() {
        return questions.get(sectionIds.get(currentSectionIndex));
    }

    private List<Question> getAnswerableQuestionsInCurrentSection() {
        List<Question> list = getAllQuestionsInCurrentSection();
        ArrayList<Question> result = new ArrayList<>();
        for (Question question : list) {
            if (question.getCanBeAnswered()) {
                result.add(question);
            }
        }
        return result;
    }

    @Override
    public List<Question> getUnansweredQuestionsInCurrentSection() {
        List<Question> list = getAnswerableQuestionsInCurrentSection();
        ArrayList<Question> result = new ArrayList<>();
        for (Question question : list) {
            if (!question.isAnswered()) {
                result.add(question);
            }
        }
        return result;
    }

    @Override
    public List<Question> getInvalidAnsweredQuestionsInCurrentSection() {
        return new ArrayList<>();
    }

    @Override
    public List<Question> getAllValidAnsweredQuestionsForQuestionnaire(long questionnaireTypeKey) {
        return new ArrayList<>();
    }

    @Override
    public ValidationResult answer(Question question) {
        updateCanBeAnswered();
        repository.persistAnswer(question.getIdentifier(), question.getAnswer(), TimeUtilities.getCurrentTimestamp());
        return new ValidationResult(true);
    }

    private void updateCanBeAnswered() {
        DependencyChecker dependencyChecker = new DependencyChecker();
        for (int i = 0; i < sectionIds.size(); i++) {
            dependencyChecker.addQuestions(questions.get(sectionIds.get(i)));
        }
        dependencyChecker.update();
    }

    @Override
    public String getCurrentSectionTitle() {
        return "Test title name";
    }

    @Override
    public void setQuestionnaireTypeKey(long questionnaireTypeKey) {

    }

    @Override
    public void clearQuestionnaireProgress() {

    }

    @Override
    public Long getCurrentSectionTypeKey() {
        return null;
    }

    @Override
    public String getQuestionnaireTitle() {
        return null;
    }

    @Override
    public ValidationResult validate(Question question) {
        return null;
    }
}
