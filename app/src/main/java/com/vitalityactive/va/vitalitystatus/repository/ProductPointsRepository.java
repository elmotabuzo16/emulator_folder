package com.vitalityactive.va.vitalitystatus.repository;

import com.vitalityactive.va.home.events.ProductFeaturePointsResponse;
import com.vitalityactive.va.vitalitystatus.earningpoints.PointsInformationDTO;
import com.vitalityactive.va.vitalitystatus.earningpoints.StatusPointsItem;

import java.util.List;

public interface ProductPointsRepository {
    List<PointsInformationDTO> getFeatureList(int key);
    List<PointsInformationDTO> getSubFeatureList(int key);
    List<PointsInformationDTO> getAllPointsCategories();
    boolean hasCachedPointsInformation();
    boolean persistProductFeaturePointsResponse(ProductFeaturePointsResponse response);
    List<StatusPointsItem> getSubFeaturePointsEntries(int key);
    List<StatusPointsItem> getFeaturePointsEntries(int key);
    String getFeatureName(int key);
    String getSubFeatureName(int key);
}
