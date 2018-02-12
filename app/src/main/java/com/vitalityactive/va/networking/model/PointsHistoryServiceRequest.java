package com.vitalityactive.va.networking.model;

public class PointsHistoryServiceRequest {
    public GetAllPointsHistoryRequest getAllPointsHistoryRequest;

    public PointsHistoryServiceRequest(long[] pointsPeriodOffsets) {
        getAllPointsHistoryRequest = new GetAllPointsHistoryRequest(pointsPeriodOffsets);
    }

    public static class GetAllPointsHistoryRequest {
        public PointsPeriodOffset[] pointsPeriodOffsets;

        public GetAllPointsHistoryRequest(long[] pointsPeriodOffsets) {
            this.pointsPeriodOffsets = new PointsPeriodOffset[pointsPeriodOffsets.length];
            int i = 0;
            for (long offset : pointsPeriodOffsets) {
                this.pointsPeriodOffsets[i++] = new PointsPeriodOffset(offset);
            }
        }
    }

    public static class PointsPeriodOffset {
        public long value;

        public PointsPeriodOffset(long value) {
            this.value = value;
        }
    }
}
