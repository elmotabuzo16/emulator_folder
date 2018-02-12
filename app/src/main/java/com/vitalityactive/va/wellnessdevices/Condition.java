package com.vitalityactive.va.wellnessdevices;

import android.support.annotation.StringRes;

import com.vitalityactive.va.R;

public enum Condition {
    TOTAL_STEPS(52,
            R.string.potential_points_steps_lower_bound_only_486,
            R.string.potential_points_steps_greater_than_or_equal_bound_578,
            R.string.potential_points_steps_lower_and_upper_bound_485,
            R.string.potential_points_steps_less_than_or_equal_and_greater_than_bound_580,
            R.string.potential_points_steps_less_and_greater_than_or_equal_than_bound_581,
            R.string.potential_points_steps_less_than_or_equal_and_greater_than_or_equal_bound_579,
            R.string.potential_points_steps_upper_bound_only_508,
            R.string.potential_points_steps_less_than_or_equal_bound_577),

    DURATION(50,
            R.string.potential_points_speed_duration_lower_bound_479,
            R.string.potential_points_speed_duration_greater_or_equal_bound_582,
            R.string.potential_points_speed_duration_lower_and_upper_bound_478,
            R.string.potential_points_speed_duration_less_or_equal_and_greater_or_equal_than_bound_586,
            R.string.potential_points_speed_duration_less_and_greater_or_equal_than_bound_585,
            R.string.potential_points_speed_duration_less_or_equal_and_greater_or_equal_than_bound_586,
            R.string.potential_points_speed_duration_upper_bound_477,
            R.string.potential_points_speed_duration_less_or_equal_than_bound_583),

    ENERGY_EXPENDITURE(56,
            R.string.potential_points_calorie_count_lower_bound_490,
            R.string.potential_points_calorie_count_greater_or_equal_than_bound_593,
            R.string.potential_points_calorie_count_lower_bound_upper_bound_491,
            R.string.potential_points_calorie_count_less_or_equal_and_greater_than_bound_594,
            R.string.potential_points_calorie_count_less_and_greater_than_bound_596,
            R.string.potential_points_calorie_count_lower_bound_upper_bound_491,
            R.string.potential_points_calorie_count_upper_bound_492,
            R.string.potential_points_calorie_count_less_or_equal_than_bound_592),

    AVERAGE_SPEED(58,
            R.string.potential_points_speed_average_speed_lower_bound_487,
            R.string.potential_points_speed_average_speed_greater_or_equal_bound_597,
            R.string.potential_points_speed_average_speed_lower_and_upper_bound_488,
            R.string.potential_points_speed_average_speed_less_or_equal_and_greater_bound_598,
            R.string.potential_points_speed_average_speed_less_than_and_greater_or_equal_bound_600,
            R.string.potential_points_speed_average_speed_less_or_equal_and_greater_or_Equal_bound_599,
            R.string.potential_points_speed_average_speed_upper_bound_489,
            R.string.potential_points_speed_average_speed_less_or_equal_bound_601),

    AVERAGE_HEART_RATE(66,
            R.string.potential_points_heartrate_lower_bound_482,
            R.string.potential_points_heartrate_greater_or_equal_than_bound_588,
            R.string.potential_points_heartrate_lower_bound_upper_bound_481,
            R.string.potential_points_heartrate_less_and_greater_than_bound_591,
            R.string.potential_points_heartrate_less_and_greater_than_bound_591,
            R.string.potential_points_heartrate_less_or_equal_and_greater_or_equal_bound_590,
            R.string.potential_points_heartrate_upper_bound_480,
            R.string.potential_points_heartrate_less_or_equal_than_bound_587),

    UNDEFINED(0,
            R.string.potential_points_section_header_495,
            R.string.potential_points_section_header_495,
            R.string.potential_points_section_header_495,
            R.string.potential_points_section_header_495,
            R.string.potential_points_section_header_495,
            R.string.potential_points_section_header_495,
            R.string.potential_points_section_header_495,
            R.string.potential_points_section_header_495);

    private final int typeKey;
    private final @StringRes int textGreaterThan;
    private final @StringRes int textGreaterOrEqualThan;
    private final @StringRes int textLessAndGreaterThan;
    private final @StringRes int textLessOrEqualAndGreaterThan;
    private final @StringRes int textLessAndGreaterOrEqualThan;
    private final @StringRes int textLessOrEqualAndGreaterOrEqualThan;
    private final @StringRes int textLessThan;
    private final @StringRes int textLessOrEqualThan;

    Condition(int typeKey,
              int textGreaterThan,
              int textGreaterOrEqualThan,
              int textLessAndGreaterThan,
              int textLessOrEqualAndGreaterThan,
              int textLessAndGreaterOrEqualThan,
              int textLessOrEqualAndGreaterOrEqualThan,
              int textLessThan,
              int textLessOrEqualThan) {
        this.typeKey = typeKey;
        this.textGreaterThan = textGreaterThan;
        this.textGreaterOrEqualThan = textGreaterOrEqualThan;
        this.textLessAndGreaterThan = textLessAndGreaterThan;
        this.textLessOrEqualAndGreaterThan = textLessOrEqualAndGreaterThan;
        this.textLessAndGreaterOrEqualThan = textLessAndGreaterOrEqualThan;
        this.textLessOrEqualAndGreaterOrEqualThan = textLessOrEqualAndGreaterOrEqualThan;
        this.textLessThan = textLessThan;
        this.textLessOrEqualThan = textLessOrEqualThan;
    }

    public static Condition getConditionByKey(int typeKey) {
        for (Condition condition : Condition.values()) {
            if (condition.typeKey == typeKey) {
                return condition;
            }
        }
        return UNDEFINED;
    }

    public int getTextGreaterThan() {
        return textGreaterThan;
    }

    public int getTextLessAndGreaterThan() {
        return textLessAndGreaterThan;
    }

    public int getTextLessThan() {
        return textLessThan;
    }

    public int getTextGreaterOrEqualThan() {
        return textGreaterOrEqualThan;
    }

    public int getTextLessOrEqualAndGreaterThan() {
        return textLessOrEqualAndGreaterThan;
    }

    public int getTextLessAndGreaterOrEqualThan() {
        return textLessAndGreaterOrEqualThan;
    }

    public int getTextLessOrEqualAndGreaterOrEqualThan() {
        return textLessOrEqualAndGreaterOrEqualThan;
    }

    public int getTextLessOrEqualThan() {
        return textLessOrEqualThan;
    }
}
