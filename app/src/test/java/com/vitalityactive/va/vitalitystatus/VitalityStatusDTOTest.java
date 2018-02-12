package com.vitalityactive.va.vitalitystatus;

import com.vitalityactive.va.vitalitystatus.levels.LevelStatusDTO;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class VitalityStatusDTOTest {
    @Test
    public void get_progress_works_correctly() throws Exception {
        VitalityStatusDTO vitalityStatusDTO = new VitalityStatusDTO(new VitalityStatus(),
                new LevelStatusDTO(),
                new LevelStatusDTO(),
                new LevelStatusDTO(),
                new ArrayList<LevelStatusDTO>(),
                0);

        int expectedValue = 100;
        assertEquals(expectedValue, vitalityStatusDTO.getProgress());

        vitalityStatusDTO.nextStatusLevel = new LevelStatusDTO("", 0, 100, 1000, 0, 0, 0);
        vitalityStatusDTO.totalPoints = 0;
        expectedValue = 0;
        assertEquals(expectedValue, vitalityStatusDTO.getProgress());

        vitalityStatusDTO.totalPoints = 100;
        expectedValue = 10;
        assertEquals(expectedValue, vitalityStatusDTO.getProgress());

    }

}