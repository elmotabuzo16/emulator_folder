package com.vitalityactive.va.content;

import android.support.test.filters.MediumTest;
import android.support.test.runner.AndroidJUnit4;

import com.vitalityactive.va.MockJUnitRunner;
import com.vitalityactive.va.cms.keyvaluecontent.CMSKeyValueContentRepository;
import com.vitalityactive.va.testutilities.annotations.RepositoryTests;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.Assert.assertNotNull;

@RunWith(AndroidJUnit4.class)
@MediumTest
@RepositoryTests
public class CMSKeyValueContentRepositoryTests {
    private CMSKeyValueContentRepository repository;

    @Before
    public void setUp() {
        MockJUnitRunner.initialiseTestObjectGraph();
        repository = MockJUnitRunner.getInstance().getApplication().getContentRepository();
    }

    @Test
    public void reading_defaults_works() {
        assertNotNull(getContentFromCmsForEn());
        assertNotNull(getContentFromCmsForEn().getVhcContent());
        assertNotNull(getContentFromCmsForEn().getVhcContent().getBloodPressureSection1Content());

        assertNotNull(getContentFromCmsForEn().getNonSmokersDeclarationContent());
        assertNotNull(getContentFromCmsForEn().getNonSmokersDeclarationContent().getLearnMoreContent());

        assertNotNull(getContentFromCmsForEn().getVHRContent());
        assertNotNull(getContentFromCmsForEn().getVHRContent().getLearnMoreContent());

        assertNotNull(getContentFromCmsForEn().getVNAContent());
        assertNotNull(getContentFromCmsForEn().getVNAContent().getLearnMoreContent());
    }

    private CMSKeyValueContentRepository.CMSKeyValueContent getContentFromCmsForEn() {
        return repository.getContent("en");
    }
}
