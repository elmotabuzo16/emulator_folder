package com.vitalityactive.va.help.interactor;

import com.vitalityactive.va.dto.HelpDTO;
import com.vitalityactive.va.persistence.models.Help;

import java.util.List;

/**
 * Created by christian.j.p.capin on 11/30/2017.
 */

public interface HelpInteractor {
    boolean isRequestInProgress();

    boolean initialize();

    List<HelpDTO> getFiveHelp();

    List<HelpDTO> getAllHelp();

    enum HelpRequestResult {
        NONE,
        CONNECTION_ERROR,
        GENERIC_ERROR,
        SUCCESSFUL
    }
}
