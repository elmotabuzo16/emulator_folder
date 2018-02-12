package com.vitalityactive.va.wellnessdevices.linking;


import android.support.annotation.StringDef;

import com.vitalityactive.va.wellnessdevices.dto.AssetsDto;
import com.vitalityactive.va.wellnessdevices.dto.PartnerDto;
import com.vitalityactive.va.wellnessdevices.dto.PotentialPointsDto;
import com.vitalityactive.va.wellnessdevices.linking.events.LinkDeviceResponseEvent;

import java.lang.annotation.Retention;
import java.util.Set;

import static java.lang.annotation.RetentionPolicy.SOURCE;

public interface WellnessDevicesLinkingInteractor {
    @Retention(SOURCE)
    @StringDef({
            WD_LINK_DEVICE,
            WD_DELINK_DEVICE
    })
    @interface RequestType {}
    String WD_LINK_DEVICE = "WD_LINK_DEVICE";
    String WD_DELINK_DEVICE = "WD_DELINK_DEVICE";

    void linkDevice(PartnerDto partner);
    LinkDeviceResponseEvent getLinkDeviceRequestResult();
    void clearLinkDeviceRequestResult();

    void delinkDevice(PartnerDto partner);

    PotentialPointsDto getPotentialPoints(int typeKey);
    AssetsDto getAssets(String device);

    Set<Integer> getDeviceAvailableActivities(String device);

    boolean isRequestRunning();
}
