package com.vitalityactive.va.vhc.capture;

import android.support.annotation.CallSuper;

import com.vitalityactive.va.vhc.captureresults.CaptureResultsPresenter;
import com.vitalityactive.va.vhc.captureresults.CaptureResultsPresenterImpl;
import com.vitalityactive.va.vhc.captureresults.MeasurementPersister;
import com.vitalityactive.va.vhc.captureresults.viewholder.CaptureMeasurementPropertyViewHolderUI;
import com.vitalityactive.va.vhc.content.VHCHealthAttributeContent;
import com.vitalityactive.va.vhc.dto.HealthAttributeRepository;
import com.vitalityactive.va.vhc.dto.MeasurementItemField;
import com.vitalityactive.va.vhc.dto.MeasurementProperty;
import com.vitalityactive.va.vhc.dto.UnitAbbreviationDescription;

import org.junit.Before;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.when;

class CaptureResultsPresenterBase {
    CaptureResultsPresenterImpl captureResultsPresenter;
    @Mock
    HealthAttributeRepository repository;
    @Mock
    MeasurementItemField itemField;
    @Mock
    MeasurementProperty property;
    @Mock
    MeasurementProperty secondaryProperty;
    @Mock
    CaptureMeasurementPropertyViewHolderUI viewHolderUI;
    @Mock
    CaptureMeasurementPropertyViewHolderUI secondaryViewHolderUI;
    @Mock
    CaptureResultsPresenter.UserInterface userInterface;
    @Mock
    UnitAbbreviationDescription unitAbbreviationDescription;
    @Mock
    VHCHealthAttributeContent vhcHealthAttributeContent;
    @Mock
    MeasurementPersister mockMeasurementPersister;

    @Before
    @CallSuper
    public void setup() {
        MockitoAnnotations.initMocks(this);
        captureResultsPresenter = new CaptureResultsPresenterImpl(repository, vhcHealthAttributeContent, mockMeasurementPersister);
        captureResultsPresenter.setUserInterface(userInterface);

        when(itemField.getPrimaryMeasurementProperty()).thenReturn(property);
        when(itemField.getSecondaryMeasurementProperty()).thenReturn(secondaryProperty);

        when(property.isPrimaryProperty()).thenReturn(true);
        when(secondaryProperty.isPrimaryProperty()).thenReturn(false);

        when(viewHolderUI.getSiblingProperty()).thenReturn(secondaryViewHolderUI);
        when(secondaryViewHolderUI.getSiblingProperty()).thenReturn(viewHolderUI);

        when(itemField.getTitle()).thenReturn("mock field");
        when(itemField.getFieldKey()).thenReturn("mock fieldKey");

        when(vhcHealthAttributeContent.getValidationRequiredString()).thenReturn("Required");
    }
}
