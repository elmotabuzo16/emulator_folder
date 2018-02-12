package com.vitalityactive.va.vitalitystatus;

import java.util.ArrayList;
import java.util.List;

public class PointsContent {

    public PointsContent() {

    }

    public class PointsItem {
        private final int iconResourceId;
        private final String level;
        private final String levelStatus;

        public PointsItem(int iconResourceId, String level, String levelStatus) {
            this.iconResourceId = iconResourceId;
            this.level = level;
            this.levelStatus = levelStatus;
        }

        public int getIconResourceId() {
            return iconResourceId;
        }

        public String getLevel() {
            return level;
        }

        public String getLevelStatus() {
            return levelStatus;
        }
    }
}
