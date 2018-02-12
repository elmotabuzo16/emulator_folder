package com.vitalityactive.va.uicomponents.slotmachine;

import android.view.View;

import com.vitalityactive.va.activerewards.dto.TitleSubtitleAndIcon;

public interface SlotMachineItemInflater {
    View getView(TitleSubtitleAndIcon data);
}
