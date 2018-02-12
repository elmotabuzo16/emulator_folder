package com.vitalityactive.va.search;

import android.support.annotation.NonNull;

import com.vitalityactive.va.dto.ContentHelpDTO;

import java.util.List;

/**
 * Created by chelsea.b.pioquinto on 1/30/2018.
 */

public interface ContentHelpInteractor {

    void fetchDetailsData(String tagkey, String tagName);

    List<ContentHelpDTO> getContents();

    List<ContentHelpDTO> getThreeHelp();
}
