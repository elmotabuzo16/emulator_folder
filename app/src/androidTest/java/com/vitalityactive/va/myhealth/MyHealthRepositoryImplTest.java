package com.vitalityactive.va.myhealth;

import com.vitalityactive.va.RepositoryTestBase;
import com.vitalityactive.va.myhealth.dto.AttributeDTO;
import com.vitalityactive.va.myhealth.dto.AttributeFeedbackDTO;
import com.vitalityactive.va.myhealth.dto.FeedbackTipDTO;
import com.vitalityactive.va.myhealth.dto.HealthInformationSectionDTO;
import com.vitalityactive.va.myhealth.dto.MyHealthRepository;
import com.vitalityactive.va.myhealth.dto.MyHealthRepositoryImpl;
import com.vitalityactive.va.myhealth.dto.VitalityAgeHealthAttributeDTO;
import com.vitalityactive.va.myhealth.shared.VitalityAgeConstants;
import com.vitalityactive.va.networking.model.HealthAttributeInformationResponse;
import com.vitalityactive.va.vhc.dto.HealthAttributeFeedbackDTO;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static org.hamcrest.CoreMatchers.isA;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class MyHealthRepositoryImplTest extends RepositoryTestBase {

    private final int INDEX = 0;
    protected MyHealthRepository myHealthRepository;

    @Before
    public void setUp() throws IOException {
        super.setUp();
        myHealthRepository = new MyHealthRepositoryImpl(dataStore);
    }

    @Test
    public void persist_health_attribute_feedback() throws Exception {
        persistHealthAttributes("myhealth/health_information_response_with_vitality_age.json");
        VitalityAgeHealthAttributeDTO vitalityAgeHealthAttributeDTO = myHealthRepository.getVitalityAgeHealthAttributeFeedback();
        assertNotNull(vitalityAgeHealthAttributeDTO);
        assertNotNull(vitalityAgeHealthAttributeDTO.getValue());
        assertNotNull(vitalityAgeHealthAttributeDTO.getMeasuredOn());
        assertNotNull(vitalityAgeHealthAttributeDTO.getHealthAttributeFeedbacks());
        assertNotNull(vitalityAgeHealthAttributeDTO.getVariance());
    }

    private void persistHealthAttributes(String file) throws IOException {
        file = file != null ? file : "myhealth/health_information_response.json";
        HealthAttributeInformationResponse healthAttributeInformationResponse = healthAttributeInformationResponse = RepositoryTestBase.getResponse(HealthAttributeInformationResponse.class, file);
        myHealthRepository.persistHealthAttributeTipResponse(healthAttributeInformationResponse);
    }

    @Test
    public void persist_most_recent_healthattribute_feedback() throws Exception {
        persistHealthAttributes("myhealth/health_information_response_with_vitality_age.json");
        VitalityAgeHealthAttributeDTO vitalityAgeHealthAttributeFeedback = myHealthRepository.getVitalityAgeHealthAttributeFeedback();
        assertNotNull(vitalityAgeHealthAttributeFeedback);
        assertTrue(vitalityAgeHealthAttributeFeedback.getHealthAttributeFeedbacks().size() > 0);
    }

    @Test
    public void persist_myhealth_information_respsonse() throws Exception {
        persistHealthAttributes(null);
        List<HealthInformationSectionDTO> healthInformationSectionDtos = myHealthRepository.getHealthInformationSections();
        assertNotNull(healthInformationSectionDtos);
        assertTrue(healthInformationSectionDtos.size() > 0);
        List<AttributeDTO> attributes = healthInformationSectionDtos.get(INDEX).getAttributeDTOS();
        assertNotNull(healthInformationSectionDtos.get(INDEX).getSortOrder());
        assertNotNull(attributes);
        AttributeDTO attribute = attributes.get(INDEX);
        assertNotNull(attribute);
        assertNotNull(attribute.getAttributeTypeCode());
        assertNotNull(attribute.getAttributeTypeName());
        assertNotNull(attribute.getAttributeTypeKey());
        AttributeFeedbackDTO feedbackDto = attribute.getAttributeFeedbackDtos().get(INDEX);
        assertNotNull(feedbackDto);
        assertNotNull(feedbackDto.getFeedbackTypeCode());
        assertNotNull(feedbackDto.getFeedbackTypeKey());
        assertNotNull(feedbackDto.getFeedbackTypeName());
        FeedbackTipDTO feedbackTipDto = feedbackDto.getFeedbackTips().get(INDEX);
        assertNotNull(feedbackDto.getFeedbackTips());
        assertNotNull(feedbackTipDto);
        assertNotNull(feedbackTipDto.getTypeCode());
        assertNotNull(feedbackTipDto.getTypeKey());
        assertNotNull(feedbackTipDto.getTypeName());
        assertNotNull(feedbackTipDto.getNote());
    }

    @Test
    public void can_fetch_by_type_key() throws Exception {
        persistHealthAttributes(null);

        HealthInformationSectionDTO section = myHealthRepository.getHealthInformationSectionByTypeKey(VitalityAgeConstants.V_AGE_TYPE_KEY_RESULT_UNKNOWN);
        assertNotNull(section);

        section = myHealthRepository.getHealthInformationSectionByTypeKey(VitalityAgeConstants.V_AGE_TYPE_KEY_RESULT_BAD);
        assertNotNull(section);

        section = myHealthRepository.getHealthInformationSectionByTypeKey(VitalityAgeConstants.V_AGE_TYPE_KEY_RESULT_GOOD);
        assertNotNull(section);


        assertNotNull(section.getSortOrder());
        List<AttributeDTO> attributes = section.getAttributeDTOS();
        assertNotNull(attributes);
        AttributeDTO attribute = attributes.get(INDEX);
        assertNotNull(attribute);
        assertNotNull(attribute.getAttributeTypeCode());
        assertNotNull(attribute.getAttributeTypeName());
        assertNotNull(attribute.getAttributeTypeKey());

        AttributeFeedbackDTO feedbackDto = attribute.getAttributeFeedbackDtos().get(INDEX);
        assertNotNull(feedbackDto);
        assertNotNull(feedbackDto.getFeedbackTypeCode());
        assertNotNull(feedbackDto.getFeedbackTypeKey());
        assertNotNull(feedbackDto.getFeedbackTypeName());

        List<FeedbackTipDTO> feedbackTipDtos = feedbackDto.getFeedbackTips();
        assertNotNull(feedbackTipDtos);
        FeedbackTipDTO feedbackTipDto = feedbackTipDtos.get(INDEX);
        assertNotNull(feedbackTipDto);
        assertNotNull(feedbackTipDto.getTypeCode());
        assertNotNull(feedbackTipDto.getTypeKey());
        assertNotNull(feedbackTipDto.getTypeName());
        assertNotNull(feedbackTipDto.getNote());
    }

    @Test
    public void can_fetch_by_parent_typekey() throws Exception {
        persistHealthAttributes(null);

        List<HealthInformationSectionDTO> sections = myHealthRepository.getHealthInformationSectionByParentTypeKey(4);
        assertNotNull(sections);
    }

    @Test
    public void should_return_feedback_tips() throws Exception {
        persistHealthAttributes("myhealth/health_information_response_with_vitality_age.json");
        VitalityAgeHealthAttributeDTO vitalityAgeHealthAttributeFeedback = myHealthRepository.getVitalityAgeHealthAttributeFeedback();
        assertTrue(vitalityAgeHealthAttributeFeedback.getHealthAttributeFeedbacks().size() > 0);
        HealthAttributeFeedbackDTO vitalityAgeFeedback = vitalityAgeHealthAttributeFeedback.getHealthAttributeFeedbacks().get(INDEX);
        assertThat(vitalityAgeFeedback.getFeedbackTypeCode(), isA(String.class));
        assertThat(vitalityAgeFeedback.getFeedbackTypeKey(), isA(int.class));
        assertThat(vitalityAgeFeedback.getFeedbackTypeName(), isA(String.class));
        assertThat(vitalityAgeFeedback.getFeedbackTypeTypeCode(), isA(String.class));
        assertThat(vitalityAgeFeedback.getFeedbackTypeTypeKey(), isA(int.class));
        assertThat(vitalityAgeFeedback.getFeedbackTypeTypeName(), isA(String.class));
    }

    @Test
    public void can_persist_empty_sections() throws Exception {
        HealthAttributeInformationResponse healthAttributeInformationResponse = RepositoryTestBase.getResponse(HealthAttributeInformationResponse.class, "myhealth/health_information_empty_whattoimprove_section_response.json");
        myHealthRepository.persistHealthAttributeTipResponse(healthAttributeInformationResponse);
        HealthInformationSectionDTO section = myHealthRepository.getHealthInformationSectionByTypeKey(VitalityAgeConstants.V_AGE_TYPE_KEY_RESULT_UNKNOWN);
        assertNotNull(section);
        section = myHealthRepository.getHealthInformationSectionByTypeKey(VitalityAgeConstants.V_AGE_TYPE_KEY_RESULT_BAD);
        assertNotNull(section);
        section = myHealthRepository.getHealthInformationSectionByTypeKey(VitalityAgeConstants.V_AGE_TYPE_KEY_RESULT_GOOD);
        assertNotNull(section);
    }

    @Test
    public void can_read_health_Attribute_metadata() throws Exception{
        HealthAttributeInformationResponse healthAttributeInformationResponse = RepositoryTestBase.getResponse(HealthAttributeInformationResponse.class, "myhealth/health_information_response_withattributedetails3.json");
        myHealthRepository.persistHealthAttributeTipResponse(healthAttributeInformationResponse);
        AttributeDTO attributeDTO = myHealthRepository.getHealthAttributeBySectionTypeKeyAndAttributeTypeKey(11, 8);
        assertNotNull(attributeDTO);
        assertNotNull(attributeDTO.getFriendlyValue());
        assertEquals("80 Mg/dl",attributeDTO.getDisplayValue());
    }
}
