package com.vitalityactive.va.myhealth.shared;

import android.graphics.Typeface;
import android.widget.TextView;

import com.vitalityactive.va.myhealth.content.AttributeItem;
import com.vitalityactive.va.myhealth.content.FeedbackItem;
import com.vitalityactive.va.myhealth.content.FeedbackTip;
import com.vitalityactive.va.myhealth.content.HealthAttributeRecommendationItem;
import com.vitalityactive.va.myhealth.content.MyHealthContent;
import com.vitalityactive.va.myhealth.content.SectionItem;
import com.vitalityactive.va.myhealth.dto.AttributeDTO;
import com.vitalityactive.va.myhealth.dto.AttributeFeedbackDTO;
import com.vitalityactive.va.myhealth.dto.FeedbackTipDTO;
import com.vitalityactive.va.myhealth.dto.HealthInformationSectionDTO;
import com.vitalityactive.va.myhealth.dto.RecommendationDTO;
import com.vitalityactive.va.utilities.TextUtilities;
import com.vitalityactive.va.utilities.ViewUtilities;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MyHealthUtils {

    public static final String DISPLAY_ATTRIBUTE_CREATED_DATE_FORMAT = "dd MMMM yyyy, hh:mm";
    public static final String SOURCE_EVENT_DATE_FORMAT = "yyyy-MM-dd'T'hh:mm:ss.SSSSSSSSS'Z['z']'";

    public static String getDisplayVitalityAge(String value) {
        try {
            return String.valueOf(Double.valueOf(Math.round(Double.valueOf(value))).intValue());
        } catch (Exception e) {
            return null;
        }
    }

    public static List<FeedbackTip> toFeedbackTips(AttributeDTO attribute) {
        try {
            List<FeedbackTip> feedbackTipsList = new ArrayList<>();
            List<AttributeFeedbackDTO> feedbacks = attribute.getAttributeFeedbackDtos();
            if (feedbacks != null && feedbacks.size() > 0) {
                for (AttributeFeedbackDTO attributeFeedback : feedbacks) {
                    feedbackTipsList.addAll(toFeedbackTips(attributeFeedback));
                }
            }
            return feedbackTipsList;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<FeedbackTip> toFeedbackTips(AttributeFeedbackDTO attributeFeedbackDTO) {
        List<FeedbackTip> feedbackTipsList = new ArrayList<>();
        if (attributeFeedbackDTO != null && attributeFeedbackDTO.getFeedbackTips() != null) {
            for (FeedbackTipDTO feedbackTip : attributeFeedbackDTO.getFeedbackTips()) {
                if (!TextUtilities.isNullOrWhitespace(feedbackTip.getTypeName())) {
                    feedbackTipsList.add(new FeedbackTip.Builder()
                            .setFeedbackTipName(feedbackTip.getTypeName())
                            .setFeedbackTipNote(feedbackTip.getNote())
                            .setFeedbackTipTypeCode(feedbackTip.getTypeCode())
                            .setFeedbackTipTypeKey(feedbackTip.getTypeKey())
                            .setFeedbackName(attributeFeedbackDTO.getFeedbackTypeName())
                            .build());
                }
            }
        }
        return feedbackTipsList;
    }

    public static SectionItem toSectionItem(HealthInformationSectionDTO healthInformationSectionDTO) {
        if (healthInformationSectionDTO != null) {
            return new SectionItem.Builder()
                    .setSectionIcon(MyHealthContent.VitalityAgeTip.getIconResource(healthInformationSectionDTO.getSortOrder()))
                    .setSectionTypekey(healthInformationSectionDTO.getTypeKey())
                    .setSectionTitle(healthInformationSectionDTO.getTypeName())
                    .setSectionSortOrder(healthInformationSectionDTO.getSortOrder())
                    .setSectionTitle(healthInformationSectionDTO.getTypeName())
                    .setTintColor(MyHealthContent.VitalityAgeTip.getIconTintColor(healthInformationSectionDTO.getSortOrder()))
                    .setAttributeItems(toAttributeItems(healthInformationSectionDTO))
                    .build();
        } else {
            return null;
        }
    }

    public static List<AttributeItem> toAttributeItems(HealthInformationSectionDTO healthInformationSectionDTO) {
        List<AttributeItem> attributeItems = new ArrayList<>();
        if (healthInformationSectionDTO != null && healthInformationSectionDTO.getAttributeDTOS() != null) {
            for (AttributeDTO attributeDTO : healthInformationSectionDTO.getAttributeDTOS()) {
                attributeItems.add(toAttributeItem(attributeDTO, healthInformationSectionDTO.getTypeKey()));
            }
        }
        return attributeItems;
    }

    public static AttributeItem toAttributeItem(AttributeDTO attributeDTO, int sectionTypeKey) {
        return new AttributeItem.Builder()
                .setAttributeFriendlyValue(attributeDTO.getDisplayValue())
                .setAttributeSortOrder(attributeDTO.getSortOrder())
                .setAttributeTitle(attributeDTO.getAttributeTypeName())
                .setAttributeTypeKey(attributeDTO.getAttributeTypeKey())
                .setAttributeValue(attributeDTO.getValue())
                .setSectionTypeKey(sectionTypeKey)
                .setFeedbackItems(prioritizeFeedbackThatHasTips(toFeedback(sectionTypeKey, attributeDTO)))
                .build();
    }


    public static List<FeedbackItem> prioritizeFeedbackThatHasTips(List<FeedbackItem> feedbackTips) {
        if (feedbackTips != null) {
            Collections.sort(feedbackTips, new Comparator<FeedbackItem>() {
                @Override
                public int compare(FeedbackItem t1, FeedbackItem t2) {
                    if (hasTips(t1)) {
                        return -1;
                    } else if (hasTips(t2)) {
                        return 1;
                    }
                    return 0;
                }
            });
        }
        return feedbackTips;
    }

    private static boolean hasTips(FeedbackItem feedbackItem) {
        return feedbackItem.getFeedbackTips() != null && feedbackItem.getFeedbackTips().size() > 0;
    }

    public static boolean tipsHaveContent(List<FeedbackTip> feedbackTips) {
        if (feedbackTips != null) {
            for (FeedbackTip feedbackTip : feedbackTips) {
                if (feedbackTip.getFeedbackTipName() != null && feedbackTip.getFeedbackTipNote() != null) {
                    return true;
                }
            }
        }
        return false;
    }

    public static List<FeedbackItem> toFeedback(int sectionTypeKey, AttributeDTO attribute) {
        try {
            List<FeedbackItem> feedbackList = new ArrayList<>();
            List<AttributeFeedbackDTO> feedbacks = attribute.getAttributeFeedbackDtos();
            if (feedbacks != null) {
                for (AttributeFeedbackDTO attributeFeedback : feedbacks) {
                    feedbackList.add(new FeedbackItem.Builder()
                            .setAttributeSortOrder(attribute.getSortOrder())
                            .setAttributeTitle(attribute.getAttributeTypeName())
                            .setAttributeValue(attribute.getValue())
                            .setAttributeFriendlyValue(attribute.getDisplayValue())
                            .setAttributeTypeKey(attribute.getAttributeTypeKey())
                            .setSectionTypeKey(sectionTypeKey)
                            .setFeedbackName(attributeFeedback.getFeedbackTypeName())
                            .setWhyIsThisImportant(attributeFeedback.getWhyIsThisImportant())
                            .setFeedbackTips(toFeedbackTips(attributeFeedback))
                            .setFeedbackTypeCode(attributeFeedback.getFeedbackTypeCode())
                            .setFeedbackTypeKey(attributeFeedback.getFeedbackTypeKey())
                            .build());
                }
            }
            return feedbackList;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static HealthAttributeRecommendationItem toAttributeFeedbackRecommendationItem(int sectionTypeKey, AttributeDTO attributeDTO) {
        try {
            List<HealthAttributeRecommendationItem.Recommendation> recommendationList = new ArrayList<>();
            if (attributeDTO.getRecommendationDtos() != null) {
                for (RecommendationDTO recommendationDTO : attributeDTO.getRecommendationDtos()) {
                    recommendationList.add(new HealthAttributeRecommendationItem.Recommendation.Builder()
                            .setFromValue(recommendationDTO.getFromValue())
                            .setToValue(recommendationDTO.getToValue())
                            .setValue(recommendationDTO.getValue())
                            .setFriendlyValue(recommendationDTO.getFriendlyValue())
                            .setUnitOfMeasure(recommendationDTO.getUnitOfMeasureId())
                            .build());
                }
                return new HealthAttributeRecommendationItem.Builder()
                        .setMeasuredOn(attributeDTO.getMeasuredOn())
                        .setEventDateLogged(formatToAttributeEventDate(attributeDTO.getEventDto() != null ? attributeDTO.getEventDto().getDateLogged() : null))
                        .setRecommendations(recommendationList)
                        .setSectionTypeKey(sectionTypeKey)
                        .setSource(attributeDTO.getEventDto() != null ? attributeDTO.getEventDto().getTypeName() : null)
                        .setFeedbacks(toFeedback(sectionTypeKey, attributeDTO))
                        .build();
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String attemptRoundingUp(String value) {
        String modifiedValue = getDisplayVitalityAge(value);
        return modifiedValue != null ? modifiedValue : value;
    }

    public static void setTextOrPlaceholder(TextView textView, String content, String placeholder) {
        if (content != null && !content.trim().isEmpty()) {
            ViewUtilities.setTextOfView(textView, content);
        } else {
            ViewUtilities.setTextOfView(textView, placeholder);
            textView.setTypeface(textView.getTypeface(), Typeface.BOLD);
        }
    }

    public static String formatToAttributeEventDate(String dateAsString) {
        try {
            if (dateAsString != null) {
                SimpleDateFormat eventDateFormat = new SimpleDateFormat(SOURCE_EVENT_DATE_FORMAT);
                return new SimpleDateFormat(DISPLAY_ATTRIBUTE_CREATED_DATE_FORMAT).format(eventDateFormat.parse(dateAsString));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public static String toSentenceCase(String input) {
        try {
            if (input != null) {
                StringBuilder sentenceCase = new StringBuilder();
                char inputArray[] = input.toCharArray();
                sentenceCase.append(Character.toUpperCase(inputArray[0]));
                for (int i = 1; i < inputArray.length; i++) {
                    char c = inputArray[i];
                    if (Character.isLetter(inputArray[i])) {
                        if ((inputArray[i - 1] == '.') || (i > 2 && Character.isWhitespace(inputArray[i - 1]) && inputArray[i - 2] == '.')) {
                            c = Character.toUpperCase(inputArray[i]);
                        } else {
                            c = Character.toLowerCase(inputArray[i]);
                        }
                    }
                    sentenceCase.append(c);
                }
                return sentenceCase.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return input;
    }

    public static FeedbackItem getDisplayableFeedback(List<FeedbackItem> feedbackItems) {
        try {
            if (feedbackItems != null) {
                feedbackItems = orderByHighestTypeKey(feedbackItems);
                for (FeedbackItem feedbackItem : feedbackItems) {
                    if (hasTips(feedbackItem) && !TextUtilities.isNullOrEmpty(feedbackItem.getWhyIsThisImportant())) {
                        return feedbackItem;
                    }
                }
                for (FeedbackItem feedbackItem : feedbackItems) {
                    if (!TextUtilities.isNullOrEmpty(feedbackItem.getWhyIsThisImportant())) {
                        return feedbackItem;
                    }
                }
                for (FeedbackItem feedbackItem : feedbackItems) {
                    if (hasTips(feedbackItem)) {
                        return feedbackItem;
                    }
                }
                return !feedbackItems.isEmpty() ? feedbackItems.get(0) : null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<FeedbackItem> orderByHighestTypeKey(List<FeedbackItem> feedbackItems) {
        try {
            Collections.sort(feedbackItems, new Comparator<FeedbackItem>() {
                @Override
                public int compare(FeedbackItem t1, FeedbackItem t2) {
                    if (t1.getFeedbackTypeKey() > t2.getFeedbackTypeKey()) {
                        return -1;
                    } else if (t1.getFeedbackTypeKey() == t2.getFeedbackTypeKey()) {
                        return 0;
                    } else {
                        return 1;
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return feedbackItems;
    }
}
