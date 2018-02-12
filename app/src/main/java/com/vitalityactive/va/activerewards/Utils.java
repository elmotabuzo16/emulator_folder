package com.vitalityactive.va.activerewards;

import android.text.TextUtils;

import com.vitalityactive.va.activerewards.dto.ActivityItem;
import com.vitalityactive.va.activerewards.dto.EntryMetadataDto;
import com.vitalityactive.va.activerewards.dto.GoalTrackerOutDto;
import com.vitalityactive.va.activerewards.dto.ObjectivePointsEntriesDto;
import com.vitalityactive.va.activerewards.dto.ObjectiveTrackersGoalDto;
import com.vitalityactive.va.activerewards.dto.TitleAndSubtitle;
import com.vitalityactive.va.activerewards.eventdetail.MetadataFormatter;
import com.vitalityactive.va.constants.EventMetadataType;
import com.vitalityactive.va.utilities.TextUtilities;
import com.vitalityactive.va.wellnessdevices.pointsmonitor.MeasurementContentFromResourceString;

import java.util.ArrayList;
import java.util.List;

public class Utils {
    public static List<ActivityItem> getActivityList(GoalTrackerOutDto goalTrackerOutDto) {
        return getActivityListWithUom(null, goalTrackerOutDto);
    }

    public static List<ActivityItem> getActivityListWithUom(MeasurementContentFromResourceString uomProvider,
                                                            GoalTrackerOutDto goalTrackerOutDto) {
        List<ActivityItem> result = new ArrayList<>();
        if (goalTrackerOutDto != null) {
            List<ObjectiveTrackersGoalDto> objectiveTrackers = goalTrackerOutDto.getObjectiveTrackers();
            for (ObjectiveTrackersGoalDto objectiveTracker : objectiveTrackers) {
                for (ObjectivePointsEntriesDto objectivePointsEntry : objectiveTracker.getObjectivePointsEntries()) {
                    ActivityItem activityItem = new ActivityItem(objectivePointsEntry.getPointsContributed() + "",
                            objectivePointsEntry.getTypeName(),
                            objectivePointsEntry.getEffectiveDate(),
                            getDeviceFromMetadata(objectivePointsEntry),
                            getMetadataList(uomProvider, objectivePointsEntry));
                    result.add(activityItem);
                }
            }
        }
        return result;
    }

    private static String getDeviceFromMetadata(ObjectivePointsEntriesDto objectivePointsEntry) {
        if (objectivePointsEntry.getPointsEntryMetadatas() != null && !objectivePointsEntry.getPointsEntryMetadatas().isEmpty()) {
            for (EntryMetadataDto entryMetadata : objectivePointsEntry.getPointsEntryMetadatas()) {
                if (entryMetadata.getTypeKey() == EventMetadataType._MANUFACTURER) {
                    return TextUtils.isEmpty(entryMetadata.getValue()) ? "" : TextUtilities.toProperCase(entryMetadata.getValue());
                }
            }
        }

        return "";
    }

    private static List<TitleAndSubtitle> getMetadataList(MeasurementContentFromResourceString uomProvider,
                                                          ObjectivePointsEntriesDto objectivePointsEntry) {
        ArrayList<TitleAndSubtitle> metadataList = new ArrayList<>();

        if (objectivePointsEntry.getPointsEntryMetadatas() != null && !objectivePointsEntry.getPointsEntryMetadatas().isEmpty()) {
            for (EntryMetadataDto entryMetadata : objectivePointsEntry.getPointsEntryMetadatas()) {
                metadataList.add(new TitleAndSubtitle(TextUtils.isEmpty(entryMetadata.getValue()) ? "" :
                        MetadataFormatter.getFormattedValueWithUnitOfMeasure(uomProvider, entryMetadata.getUnitOfMeasure(),
                                entryMetadata.getValue(), entryMetadata.getTypeKey()),
                        TextUtils.isEmpty(entryMetadata.getTypeName()) ? "" : entryMetadata.getTypeName()));
            }
        }

        return metadataList;
    }
}
