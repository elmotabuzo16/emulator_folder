package com.vitalityactive.va.wellnessdevices.pointsmonitor.containers;

import android.content.Context;
import android.support.annotation.StringRes;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vitalityactive.va.R;
import com.vitalityactive.va.uicomponents.adapters.GenericRecyclerViewAdapter;
import com.vitalityactive.va.wellnessdevices.Condition;
import com.vitalityactive.va.wellnessdevices.dto.PointsConditionsDto;
import com.vitalityactive.va.wellnessdevices.pointsmonitor.MeasurementContentFromResourceString;
import com.vitalityactive.va.wellnessdevices.pointsmonitor.models.PointsEntryDetailsModel;

import java.math.BigDecimal;
import java.util.List;

import static com.vitalityactive.va.utilities.ViewUtilities.removeTrailingZeros;

class PointRangeItemViewHolder extends GenericRecyclerViewAdapter.ViewHolder<PointsEntryDetailsModel> {
    private final MeasurementContentFromResourceString uomProvider;
    private final String pointsStr;
    private final TextView tvTitle;
    private final LinearLayout llRangeContainer;
    private final View vDivider;

    private PointRangeItemViewHolder(Context context,
                                     MeasurementContentFromResourceString uomProvider,
                                     View itemView) {
        super(itemView);
        this.uomProvider = uomProvider;
        pointsStr = context.getString(R.string.potential_points_section_header_495).toLowerCase();
        llRangeContainer = itemView.findViewById(R.id.ll_range_container);
        tvTitle = itemView.findViewById(R.id.tv_points_header);
        vDivider = itemView.findViewById(R.id.v_divider);
    }

    @Override
    public void bindWith(PointsEntryDetailsModel pointsEntryDetailsModel) {
        tvTitle.setText(pointsEntryDetailsModel.getPointsEntryDetails().getPotentialPoints() + " " + pointsStr);
        fillRangeList(pointsEntryDetailsModel.getPointsEntryDetails().getConditions());
        if (pointsEntryDetailsModel.isLastItemInList()) {
            vDivider.setVisibility(View.GONE);
        }
    }

    private void fillRangeList(List<PointsConditionsDto> conditionsList) {
        if (conditionsList != null && !conditionsList.isEmpty()) {
            for (PointsConditionsDto pointsConditionsDto : conditionsList) {
                Condition condition = Condition.getConditionByKey(pointsConditionsDto.getMetadataTypeKey());
                String conditionText;

                boolean greaterStraight = false;
                boolean lessStraight = false;
                String greaterValue = "";
                String lessValue = "";

                if (TextUtils.isEmpty(pointsConditionsDto.getGreaterThanOrEqualTo()) &&
                        !TextUtils.isEmpty(pointsConditionsDto.getGreaterThan())) {
                    greaterStraight = true;
                    greaterValue = pointsConditionsDto.getGreaterThan();
                } else if (!TextUtils.isEmpty(pointsConditionsDto.getGreaterThanOrEqualTo()) &&
                        TextUtils.isEmpty(pointsConditionsDto.getGreaterThan())) {
                    greaterStraight = false;
                    greaterValue = pointsConditionsDto.getGreaterThanOrEqualTo();
                } else if (!TextUtils.isEmpty(pointsConditionsDto.getGreaterThanOrEqualTo()) &&
                        !TextUtils.isEmpty(pointsConditionsDto.getGreaterThan())) {
                    BigDecimal greater = new BigDecimal(pointsConditionsDto.getGreaterThan());
                    BigDecimal greaterOrEqual = new BigDecimal(pointsConditionsDto.getGreaterThanOrEqualTo());
                    if (greater.compareTo(greaterOrEqual) > 0) {
                        greaterStraight = true;
                        greaterValue = pointsConditionsDto.getGreaterThan();
                    } else {
                        greaterStraight = false;
                        greaterValue = pointsConditionsDto.getGreaterThanOrEqualTo();
                    }
                }

                if (TextUtils.isEmpty(pointsConditionsDto.getLessThanOrEqualTo()) &&
                        !TextUtils.isEmpty(pointsConditionsDto.getLessThan())) {
                    lessStraight = true;
                    lessValue = pointsConditionsDto.getLessThan();
                } else if (!TextUtils.isEmpty(pointsConditionsDto.getLessThanOrEqualTo()) &&
                        TextUtils.isEmpty(pointsConditionsDto.getLessThan())) {
                    lessStraight = false;
                    lessValue = pointsConditionsDto.getLessThanOrEqualTo();
                } else if (!TextUtils.isEmpty(pointsConditionsDto.getLessThanOrEqualTo()) &&
                        !TextUtils.isEmpty(pointsConditionsDto.getLessThan())) {
                    BigDecimal less = new BigDecimal(pointsConditionsDto.getLessThan());
                    BigDecimal lessOrEqual = new BigDecimal(pointsConditionsDto.getLessThanOrEqualTo());
                    if (less.compareTo(lessOrEqual) > 0) {
                        lessStraight = true;
                        lessValue = pointsConditionsDto.getLessThan();
                    } else {
                        lessStraight = false;
                        lessValue = pointsConditionsDto.getLessThanOrEqualTo();
                    }
                }

                int uomCondition = 0;
                if (!TextUtils.isEmpty(greaterValue) && !TextUtils.isEmpty(lessValue)) {
                    if (lessStraight && greaterStraight) {
                        uomCondition = condition.getTextLessAndGreaterThan();
                    } else if (lessStraight && !greaterStraight) {
                        uomCondition = condition.getTextLessAndGreaterOrEqualThan();
                    } else if (!lessStraight && greaterStraight) {
                        uomCondition = condition.getTextLessOrEqualAndGreaterThan();
                    } else {
                        uomCondition = condition.getTextLessOrEqualAndGreaterOrEqualThan();
                    }
                    conditionText = getStringWithUom(uomCondition,
                            removeTrailingZeros(greaterValue),
                            removeTrailingZeros(lessValue),
                            getUomFromProvider(pointsConditionsDto));
                } else if (!TextUtils.isEmpty(greaterValue)) {
                    if (greaterStraight) {
                        uomCondition = condition.getTextGreaterThan();
                    } else {
                        uomCondition = condition.getTextGreaterOrEqualThan();
                    }
                    conditionText = getStringWithUom(uomCondition,
                            removeTrailingZeros(greaterValue),
                            getUomFromProvider(pointsConditionsDto));
                } else {
                    if (lessStraight) {
                        uomCondition = condition.getTextLessThan();
                    } else {
                        uomCondition = condition.getTextLessOrEqualThan();
                    }
                    conditionText = getStringWithUom(uomCondition,
                            removeTrailingZeros(lessValue),
                            getUomFromProvider(pointsConditionsDto));
                }
                llRangeContainer.addView(new ConditionRangeItemView(llRangeContainer.getContext(), conditionText));
            }
        }
    }

    private String getStringWithUom(@StringRes int stringId,
                                    String parameter,
                                    String uom) {
        return llRangeContainer.getContext().getString(stringId, getBoldString(getValueWithUom(parameter, uom)));
    }

    private String getStringWithUom(@StringRes int stringId,
                                    String parameter1,
                                    String parameter2,
                                    String uom) {
        return llRangeContainer.getContext().getString(stringId,
                getBoldString(getValueWithUom(parameter1, uom)),
                getBoldString(getValueWithUom(parameter2, uom)));
    }

    private String getValueWithUom(String value, String uom) {
        if (uom.contains("%")){
            return isUomAfterValue() ? (value + uom) : (uom + value);
        } else {
            return isUomAfterValue() ? (value + " " + uom) : (uom + " " + value);
        }

    }

    private String getUomFromProvider(PointsConditionsDto pointsConditionsDto) {
        return uomProvider.getPointsConditionUOMString(pointsConditionsDto);
    }

    private String getBoldString(String text) {
        return "<B>" + text + "</B>";
    }

    /**
     * @return true if 100%, 30 min or 1000 meters
     * false if %100, min 30, meters 1000
     */
    private boolean isUomAfterValue() {
        return !llRangeContainer.getContext().getResources().getString(R.string.current_locale).equals("ja-JP");
    }

    public static class Factory
            implements GenericRecyclerViewAdapter.IViewHolderFactory<PointsEntryDetailsModel, PointRangeItemViewHolder> {
        private Context context;
        MeasurementContentFromResourceString uomProvider;

        public Factory(Context context, MeasurementContentFromResourceString uomProvider) {
            this.context = context;
            this.uomProvider = uomProvider;
        }

        @Override
        public PointRangeItemViewHolder createViewHolder(View itemView) {
            return new PointRangeItemViewHolder(context, uomProvider, itemView);
        }
    }
}
