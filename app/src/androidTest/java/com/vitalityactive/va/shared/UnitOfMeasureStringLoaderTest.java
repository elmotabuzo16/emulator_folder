package com.vitalityactive.va.shared;

import android.support.annotation.StringRes;
import android.support.test.InstrumentationRegistry;

import com.vitalityactive.va.R;
import com.vitalityactive.va.UnitsOfMeasure;
import com.vitalityactive.va.constants.UnitOfMeasure;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class UnitOfMeasureStringLoaderTest {

    private UnitOfMeasureStringLoader unitOfMeasureStringLoader;

    @Before
    public void setUp() {
        unitOfMeasureStringLoader = new UnitOfMeasureStringLoader(InstrumentationRegistry.getTargetContext());
    }

    @Test
    public void days() throws Exception {
        verifyDisplayName(R.string.assessment_unit_of_measure_days_text_642, UOMEnumFromReferenceData(UnitOfMeasure._DAYS));
        verifyAbbreviation(R.string.assessment_unit_of_measure_days_abbreviation_641, UOMEnumFromReferenceData(UnitOfMeasure._DAYS));
    }

    @Test
    public void stone_pound() throws Exception {
        verifyDisplayName(R.string.assessment_unit_of_measure_stone_pound_text_644, UOMEnumFromReferenceData(UnitOfMeasure._STONEPOUND));
        verifyAbbreviation(R.string.assessment_unit_of_measure_stone_pound_abbreviation_643, UOMEnumFromReferenceData(UnitOfMeasure._STONEPOUND));
    }

    private UnitsOfMeasure UOMEnumFromReferenceData(int unitOfMeasure) {
        return UnitsOfMeasure.fromValue(String.valueOf(unitOfMeasure));
    }

    private void verifyAbbreviation(@StringRes int stringResourceId, UnitsOfMeasure unitOfMeasure) {
        assertEquals(InstrumentationRegistry.getTargetContext().getString(stringResourceId), unitOfMeasureStringLoader.getUnitOfMeasureSymbol(unitOfMeasure));
    }

    private void verifyDisplayName(@StringRes int stringResourceId, UnitsOfMeasure unitOfMeasure) {
        assertEquals(InstrumentationRegistry.getTargetContext().getString(stringResourceId), unitOfMeasureStringLoader.getUnitOfMeasureDisplayName(unitOfMeasure));
    }

}
