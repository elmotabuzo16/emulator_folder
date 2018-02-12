package com.vitalityactive.va.help.dto;

import com.vitalityactive.va.dto.ContentHelpDTO;
import com.vitalityactive.va.dto.HelpDTO;
import com.vitalityactive.va.help.model.HelpResponse;
import com.vitalityactive.va.search.ContentHelpResponse;

import java.util.List;


/**
 * Created by christian.j.p.capin on 11/30/2017.
 */

public interface HelpRepository {
    void persistHelpResponse(HelpResponse response);
    List<HelpDTO> getHelp();
    List<HelpDTO> getHelpFive();



    void persistHelpContent(ContentHelpResponse response);

    List<ContentHelpDTO> getContentHelp();
    List<ContentHelpDTO> getThreeRelatedHelp();
}
