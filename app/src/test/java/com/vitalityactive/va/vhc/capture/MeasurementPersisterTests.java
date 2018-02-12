package com.vitalityactive.va.vhc.capture;

import android.support.annotation.NonNull;

import com.vitalityactive.va.InsurerConfigurationRepository;
import com.vitalityactive.va.UnitsOfMeasure;
import com.vitalityactive.va.constants.EventType;
import com.vitalityactive.va.constants.ProductFeature;
import com.vitalityactive.va.constants.ProductFeatureType;
import com.vitalityactive.va.persistence.models.vhc.HealthAttributeTypeValidValues;
import com.vitalityactive.va.vhc.HealthAttributeGroup;
import com.vitalityactive.va.vhc.captureresults.MeasurementPersister;
import com.vitalityactive.va.vhc.captureresults.models.CapturedGroup;
import com.vitalityactive.va.vhc.captureresults.models.GroupType;
import com.vitalityactive.va.vhc.content.VHCHealthAttributeContent;
import com.vitalityactive.va.vhc.dto.HealthAttributeRepository;
import com.vitalityactive.va.vhc.dto.MeasurementItem;
import com.vitalityactive.va.vhc.dto.MeasurementItemField;
import com.vitalityactive.va.vhc.dto.UnitAbbreviationDescription;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MeasurementPersisterTests {
    private static final double DELTA = 0.001;
    private static final long DATE_TESTED = 1490000000000L;
    private CapturedGroup bmi;
    private MeasurementPersister measurementPersister;
    private HealthAttributeGroup healthAttributeGroup;
    private HealthAttributeGroup waistCircumferenceHealthGroup;
    @Mock
    HealthAttributeRepository repository;
    @Mock
    VHCHealthAttributeContent vhcHealthAttributeContent;
    @Mock
    VHCHealthAttributeContent vhcContent;
    @Mock
    private InsurerConfigurationRepository mockInsurerConfigurationRepository;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        setupCapturedGroup();
        setupRepository();
        when(mockInsurerConfigurationRepository.getProductFeatureTypeKeyFromEventTypeKey(EventType._HEIGHTCAPTURED)).thenReturn(ProductFeature._HEIGHT);
        when(mockInsurerConfigurationRepository.getProductFeatureTypeKeyFromEventTypeKey(EventType._WEIGHTCAPTURED)).thenReturn(ProductFeature._WEIGHT);
        when(mockInsurerConfigurationRepository.getProductFeatureTypeKeyFromEventTypeKey(EventType._BMI)).thenReturn(ProductFeature._BMI);
        when(vhcHealthAttributeContent.getUnitOfMeasureDisplayName(any(UnitsOfMeasure.class))).thenReturn("mock");
        when(vhcHealthAttributeContent.getUnitOfMeasureSymbol(any(UnitsOfMeasure.class))).thenReturn("mock");
        when(vhcHealthAttributeContent.getValidationRangeBiggerString()).thenReturn("mock");
        when(vhcHealthAttributeContent.getValidationRange()).thenReturn("mock");
        when(vhcHealthAttributeContent.getValidationRangeSmallerString()).thenReturn("mock");
        measurementPersister = new MeasurementPersister(repository, vhcHealthAttributeContent, mockInsurerConfigurationRepository);
    }

    private void setupRepository() {
        setupDefaultValidValues();
        setupDefaultFields();
        setupDefaultProperties();
        setupBMIInRepository();
        setupWaistCircumferenceInRepository();
    }

    private void setupDefaultFields() {
        HashMap<String, String> fields = new HashMap<>();
        fields.put("1", "field1");
        when(repository.getGroupFields(any(HealthAttributeGroup.class))).thenReturn(fields);
    }

    private void setupDefaultProperties() {
        ArrayList<String> properties = new ArrayList<>();
        properties.add("property1");
        properties.add("property2");
        when(repository.getGroupFieldProperties(any(HealthAttributeGroup.class), anyString())).thenReturn(properties);
    }

    private void setupDefaultValidValues() {
        ArrayList<HealthAttributeTypeValidValues> defaultValues = new ArrayList<>();
        defaultValues.add(new HealthAttributeTypeValidValues(UnitsOfMeasure.CENTIMETER, 1f, 10f));
        when(repository.getValidValues(any(HealthAttributeGroup.class), anyString())).thenReturn(defaultValues);
    }

    private void setupBMIInRepository() {
        healthAttributeGroup = new HealthAttributeGroup(ProductFeatureType._VHCBMI, "Body Mass Index", "");
        when(repository.getCapturedGroup(eq(healthAttributeGroup))).thenReturn(bmi);
        when(repository.getValidValues(eq(healthAttributeGroup), eq(getHeightEventTypeKey()))).thenReturn(getHeightUnits());
        when(repository.getValidValues(eq(healthAttributeGroup), eq(getWeightEventTypeKey()))).thenReturn(getWeightUnits());
    }

    @NonNull
    private String getWeightEventTypeKey() {
        return String.valueOf(EventType._WEIGHTCAPTURED);
    }

    @NonNull
    private String getHeightEventTypeKey() {
        return String.valueOf(EventType._HEIGHTCAPTURED);
    }

    private void setupWaistCircumferenceInRepository() {
        waistCircumferenceHealthGroup = new HealthAttributeGroup(ProductFeatureType._VHCWAISTCIRCUM, "Waist Circumference", "");
        when(repository.getCapturedGroup(eq(waistCircumferenceHealthGroup))).thenReturn(null);
        when(repository.getGroupFields(waistCircumferenceHealthGroup)).thenReturn(getWaistFieldNames());
        when(repository.getValidValues(eq(waistCircumferenceHealthGroup), anyString())).thenReturn(waistValidValues());
    }

    @NonNull
    private HashMap<String, String> getWaistFieldNames() {
        HashMap<String, String> events = new HashMap<>();
        events.put("0", "");
        return events;
    }

    @NonNull
    private ArrayList<HealthAttributeTypeValidValues> waistValidValues() {
        ArrayList<HealthAttributeTypeValidValues> validValues = new ArrayList<>();
        validValues.add(new HealthAttributeTypeValidValues(UnitsOfMeasure.CENTIMETER, 0f, 30f));
        return validValues;
    }

    @NonNull
    private ArrayList<HealthAttributeTypeValidValues> getHeightUnits() {
        ArrayList<HealthAttributeTypeValidValues> values = new ArrayList<>();
        values.add(new HealthAttributeTypeValidValues(UnitsOfMeasure.CENTIMETER, 0f, 30f));
        values.add(new HealthAttributeTypeValidValues(UnitsOfMeasure.MILE, 0f, 1.2f));
        values.add(new HealthAttributeTypeValidValues(UnitsOfMeasure.FOOT, 0f, 4f));
        return values;
    }

    private ArrayList<HealthAttributeTypeValidValues> getWeightUnits() {
        ArrayList<HealthAttributeTypeValidValues> values = new ArrayList<>();
        values.add(new HealthAttributeTypeValidValues(UnitsOfMeasure.KILOGRAM, 0f, 200f));
        return values;
    }

    private void setupCapturedGroup() {
        bmi = new CapturedGroup(GroupType.BODY_MASS_INDEX, "Body Mass Index");
        bmi.addCapturedField(getHeightEventTypeKey())
                .setPrimaryValue(123f, true)
                .setSecondaryValue(456f, true)
                .setSelectedUnitOfMeasure(UnitsOfMeasure.MILE)
                .setDateTested(DATE_TESTED);
        bmi.addCapturedField(getWeightEventTypeKey());
    }

    @Test
    public void can_create_MeasurementItem_from_HealthAttributeGroup() {
        MeasurementItem item = measurementPersister.buildMeasurementItem(healthAttributeGroup);

        assertEquals(GroupType.BODY_MASS_INDEX, item.type);
    }

    @Test
    public void can_create_fields_key() {
        MeasurementItem item = measurementPersister.buildMeasurementItem(healthAttributeGroup);

        assertEquals(2, item.measurementItemFields.size());
        assertEquals(getHeightEventTypeKey(), item.measurementItemFields.get(0).getFieldKey());
        assertEquals(getWeightEventTypeKey(), item.measurementItemFields.get(1).getFieldKey());
    }

    @Test
    public void can_create_fields_names_resolved() {
        HashMap<String, String> fields = new HashMap<>();
        fields.put(getHeightEventTypeKey(), "height");
        fields.put(getWeightEventTypeKey(), "weight");
        when(repository.getGroupFields(healthAttributeGroup)).thenReturn(fields);

        MeasurementItem item = measurementPersister.buildMeasurementItem(healthAttributeGroup);

        assertEquals(2, item.measurementItemFields.size());
        assertEquals("height", item.measurementItemFields.get(0).getTitle());
        assertEquals("weight", item.measurementItemFields.get(1).getTitle());
    }

    @Test
    public void can_create_properties() {
        HealthAttributeGroup group = getCholesterolHealthAttributeGroup();
        ArrayList<String> properties = new ArrayList<>();
        properties.add("abc");
        properties.add("def");
        when(repository.getGroupFieldProperties(eq(group), anyString())).thenReturn(properties);

        MeasurementItem item = measurementPersister.buildMeasurementItem(group);

        assertEquals(1, item.measurementItemFields.size());
        assertEquals("abc", item.measurementItemFields.get(0).getPrimaryMeasurementProperty().getName());
        assertEquals("def", item.measurementItemFields.get(0).getSecondaryMeasurementProperty().getName());
    }

    @NonNull
    private HealthAttributeGroup getCholesterolHealthAttributeGroup() {
        return new HealthAttributeGroup(ProductFeatureType._VHCCHOLESTEROL, "Cholesterol", "");
    }

    @Test
    public void can_create_properties_secondary_hidden() {
        HealthAttributeGroup group = getCholesterolHealthAttributeGroup();
        ArrayList<String> properties = new ArrayList<>();
        properties.add("abc");
        when(repository.getGroupFieldProperties(eq(group), anyString())).thenReturn(properties);

        MeasurementItem item = measurementPersister.buildMeasurementItem(group);

        assertEquals(1, item.measurementItemFields.size());
        assertEquals(false, item.measurementItemFields.get(0).getSecondaryMeasurementProperty().isVisible());
    }

    @Test
    public void can_create_unit_of_measurement() {
        MeasurementItem item = measurementPersister.buildMeasurementItem(healthAttributeGroup);
        List<UnitAbbreviationDescription> heightUnits = item.measurementItemFields.get(0).getUnits();
        List<UnitAbbreviationDescription> weightUnits = item.measurementItemFields.get(1).getUnits();

        assertEquals(3, heightUnits.size());
        assertEquals(1, weightUnits.size());
    }

    @Test
    public void can_create_values() {
        MeasurementItem item = measurementPersister.buildMeasurementItem(healthAttributeGroup);
        MeasurementItemField height = item.measurementItemFields.get(0);

        assertEquals(123f, height.getPrimaryMeasurementProperty().getValue(), DELTA);
        assertEquals(456f, height.getSecondaryMeasurementProperty().getValue(), DELTA);
    }

    @Test
    public void can_load_selected_unit_of_measurement() {
        MeasurementItem item = measurementPersister.buildMeasurementItem(healthAttributeGroup);
        MeasurementItemField height = item.measurementItemFields.get(0);

        assertEquals(vhcHealthAttributeContent.getUnitOfMeasureSymbol(UnitsOfMeasure.MILE), height.getSelectedUnitOfMeasurement().getAbbreviation());
    }

    @Test
    public void can_load_date_tested() {
        MeasurementItem item = measurementPersister.buildMeasurementItem(healthAttributeGroup);
        MeasurementItemField height = item.measurementItemFields.get(0);

        assertEquals(DATE_TESTED, height.getDateTested().getTime());
    }

    @Test
    public void if_no_CapturedGroup_create_it() {
        // expect no failure
        MeasurementItem item = measurementPersister.buildMeasurementItem(waistCircumferenceHealthGroup);

        assertEquals(GroupType.WAIST_CIRCUMFERENCE, item.type);
    }

    @Test
    public void if_no_CapturedGroup_persisted() {
        measurementPersister.buildMeasurementItem(waistCircumferenceHealthGroup);

        verify(repository).persistCapturedGroup(any(CapturedGroup.class));
    }

    @Test
    public void if_no_CapturedGroup_get_field_names_from_repo() {
        measurementPersister.buildMeasurementItem(waistCircumferenceHealthGroup);

        verify(repository, atLeast(1)).getGroupFields(waistCircumferenceHealthGroup);
    }

    @Test
    public void does_not_create_a_field_for_bmi() {
        when(repository.getCapturedGroup(eq(healthAttributeGroup))).thenReturn(null);
        when(mockInsurerConfigurationRepository.getBMIEventTypeKey()).thenReturn(EventType._BMI);

        HashMap<String, String> fields = new HashMap<>();
        fields.put(String.valueOf(EventType._BMI), "bmi");
        fields.put(getHeightEventTypeKey(), "height");
        fields.put(getWeightEventTypeKey(), "weight");
        when(repository.getGroupFields(healthAttributeGroup)).thenReturn(fields);

        MeasurementItem item = measurementPersister.buildMeasurementItem(healthAttributeGroup);

        assertEquals(2, item.measurementItemFields.size());
    }
}
