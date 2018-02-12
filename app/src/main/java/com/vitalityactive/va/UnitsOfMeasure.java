package com.vitalityactive.va;

import com.vitalityactive.va.utilities.TextUtilities;

import static com.vitalityactive.va.constants.PointsEntryType._HEARTRATE;
import static com.vitalityactive.va.constants.PointsEntryType._STEPS;
import static com.vitalityactive.va.constants.UnitOfMeasure._BEATSPERMINUTE;
import static com.vitalityactive.va.constants.UnitOfMeasure._BMI;
import static com.vitalityactive.va.constants.UnitOfMeasure._CENTIMETER;
import static com.vitalityactive.va.constants.UnitOfMeasure._DAYS;
import static com.vitalityactive.va.constants.UnitOfMeasure._FASTINGGLUCOSEMILLIGRAMSPERDECILITRE;
import static com.vitalityactive.va.constants.UnitOfMeasure._FASTINGGLUCOSEMILLIMOLESPERLITRE;
import static com.vitalityactive.va.constants.UnitOfMeasure._FOOT;
import static com.vitalityactive.va.constants.UnitOfMeasure._FOOTINCH;
import static com.vitalityactive.va.constants.UnitOfMeasure._GRAM;
import static com.vitalityactive.va.constants.UnitOfMeasure._HDLCHOLESTROLMILLIGRAMSPERDECILITRE;
import static com.vitalityactive.va.constants.UnitOfMeasure._HDLCHOLESTROLMILLIMOLESPERLITRE;
import static com.vitalityactive.va.constants.UnitOfMeasure._HOURS;
import static com.vitalityactive.va.constants.UnitOfMeasure._INCH;
import static com.vitalityactive.va.constants.UnitOfMeasure._KILOCALORIES;
import static com.vitalityactive.va.constants.UnitOfMeasure._KILOCALORIESPERHOUR;
import static com.vitalityactive.va.constants.UnitOfMeasure._KILOGRAM;
import static com.vitalityactive.va.constants.UnitOfMeasure._KILOMETER;
import static com.vitalityactive.va.constants.UnitOfMeasure._KILOMETERSPERHOUR;
import static com.vitalityactive.va.constants.UnitOfMeasure._LDLCHOLESTROLMILLIGRAMSPERDECILITRE;
import static com.vitalityactive.va.constants.UnitOfMeasure._LDLCHOLESTROLMILLIMOLESPERLITRE;
import static com.vitalityactive.va.constants.UnitOfMeasure._METER;
import static com.vitalityactive.va.constants.UnitOfMeasure._METERSPERSECOND;
import static com.vitalityactive.va.constants.UnitOfMeasure._MILE;
import static com.vitalityactive.va.constants.UnitOfMeasure._MILLIMETEROFMERCURY;
import static com.vitalityactive.va.constants.UnitOfMeasure._MILLIMOLESPERLITRE;
import static com.vitalityactive.va.constants.UnitOfMeasure._MINUTES;
import static com.vitalityactive.va.constants.UnitOfMeasure._MINUTESPERKILOMETER;
import static com.vitalityactive.va.constants.UnitOfMeasure._OUNCE;
import static com.vitalityactive.va.constants.UnitOfMeasure._PERCENTAGE;
import static com.vitalityactive.va.constants.UnitOfMeasure._PERDAY;
import static com.vitalityactive.va.constants.UnitOfMeasure._PERWEEK;
import static com.vitalityactive.va.constants.UnitOfMeasure._POUND;
import static com.vitalityactive.va.constants.UnitOfMeasure._RANDOMGLUCOSEMILLIGRAMSPERDECILITRE;
import static com.vitalityactive.va.constants.UnitOfMeasure._RANDOMGLUCOSEMILLIMOLESPERLITRE;
import static com.vitalityactive.va.constants.UnitOfMeasure._STONE;
import static com.vitalityactive.va.constants.UnitOfMeasure._STONEPOUND;
import static com.vitalityactive.va.constants.UnitOfMeasure._SYSTOLICKILOPASCAL;
import static com.vitalityactive.va.constants.UnitOfMeasure._SYSTOLICMILLIMETEROFMERCURY;
import static com.vitalityactive.va.constants.UnitOfMeasure._TON;
import static com.vitalityactive.va.constants.UnitOfMeasure._TOTALCHOLESTROLMILLIGRAMSPERDECILITRE;
import static com.vitalityactive.va.constants.UnitOfMeasure._TRIGLYCERIDEMILLIGRAMSPERDECILITRE;
import static com.vitalityactive.va.constants.UnitOfMeasure._TRIGLYCERIDEMILLIMOLESPERLITRE;

public enum UnitsOfMeasure {
    GRAM(_GRAM),
    KILOGRAM(_KILOGRAM),
    METER(_METER),
    KILOMETER(_KILOMETER),
    INCH(_INCH),
    FOOT(_FOOT),
    MILE(_MILE),
    OUNCE(_OUNCE),
    POUND(_POUND),
    TON(_TON),
    CENTIMETER(_CENTIMETER),
    MILLIMETEROFMERCURY(_MILLIMETEROFMERCURY),
    TOTALCHOLESTEROL(_MILLIMOLESPERLITRE),
    PERCENTAGE(_PERCENTAGE),
    KILOPASCAL(_SYSTOLICKILOPASCAL),
    STONE(_STONE),
    FOOTINCH(_FOOTINCH),
    CHOLESTEROL_MG_PER_DL(_TOTALCHOLESTROLMILLIGRAMSPERDECILITRE),
    TRIGLYCERIDE_MG_PER_DL(_TRIGLYCERIDEMILLIGRAMSPERDECILITRE),
    FASTINGGLUCOSE_MG_PER_DL(_FASTINGGLUCOSEMILLIGRAMSPERDECILITRE),
    TRIGLYCERIDE_MM_PER_L(_TRIGLYCERIDEMILLIMOLESPERLITRE),
    FASTINGGLUCOSE_MM_PER_L(_FASTINGGLUCOSEMILLIMOLESPERLITRE),
    LDLCHOLESTEROL_MG_PER_DL(_LDLCHOLESTROLMILLIGRAMSPERDECILITRE),
    HDLCHOLESTEROL_MG_PER_DL(_HDLCHOLESTROLMILLIGRAMSPERDECILITRE),
    LDLCHOLESTEROL_MM_PER_L(_LDLCHOLESTROLMILLIMOLESPERLITRE),
    HDLCHOLESTEROL_MM_PER_L(_HDLCHOLESTROLMILLIMOLESPERLITRE),
    RANDOMGLUCOSE_MM_PER_L(_RANDOMGLUCOSEMILLIMOLESPERLITRE),
    RANDOMGLUCOSE_MG_PER_DL(_RANDOMGLUCOSEMILLIGRAMSPERDECILITRE),
    SYSTOLIC_MILLIMETER_OF_MERCURY(_SYSTOLICMILLIMETEROFMERCURY),
    DIASTOLIC_KILOPASCAL(_SYSTOLICKILOPASCAL),
    DIASTOLIC_MILLIMETER_OF_MERCURY(_MILLIMETEROFMERCURY),
    PER_DAY(_PERDAY),
    PER_WEEK(_PERWEEK),
    MINUTES(_MINUTES),
    HOURS(_HOURS),
    BMI(_BMI),
    STEPS(200001),
    AVERAGEHEARTRATE(200002),
    KILOMETERS_PER_HOUR(_KILOMETERSPERHOUR),
    DAYS(_DAYS),
    STONEPOUND(_STONEPOUND),
    KILOCALORIES(_KILOCALORIES),
    METERS_PER_SECOND(_METERSPERSECOND),
    MINUTES_PER_KILOMETER(_MINUTESPERKILOMETER),
    BEATS_PER_MINUTE(_BEATSPERMINUTE),
    KILOCALORIES_PER_HOUR(_KILOCALORIESPERHOUR),
    UNKNOWN(-1);

    private int typeKey;

    UnitsOfMeasure(int id) {
        this.typeKey = id;
    }

    public static UnitsOfMeasure fromValue(String value) {
        final Integer typeKey = TextUtilities.getIntegerFromString(value);
        switch (typeKey) {
            case _GRAM:
                return GRAM;
            case _KILOGRAM:
                return KILOGRAM;
            case _METER:
                return METER;
            case _KILOMETER:
                return KILOMETER;
            case _INCH:
                return INCH;
            case _FOOT:
                return FOOT;
            case _MILE:
                return MILE;
            case _OUNCE:
                return OUNCE;
            case _POUND:
                return POUND;
            case _TON:
                return TON;
            case _CENTIMETER:
                return CENTIMETER;
            case _MILLIMETEROFMERCURY:
                return MILLIMETEROFMERCURY;
            case _MILLIMOLESPERLITRE:
                return TOTALCHOLESTEROL;
            case _PERCENTAGE:
                return PERCENTAGE;
            case _SYSTOLICKILOPASCAL:
                return KILOPASCAL;
            case _STONE:
                return STONE;
            case _FOOTINCH:
                return FOOTINCH;
            case _TOTALCHOLESTROLMILLIGRAMSPERDECILITRE:
                return CHOLESTEROL_MG_PER_DL;
            case _TRIGLYCERIDEMILLIGRAMSPERDECILITRE:
                return TRIGLYCERIDE_MG_PER_DL;
            case _FASTINGGLUCOSEMILLIGRAMSPERDECILITRE:
                return FASTINGGLUCOSE_MG_PER_DL;
            case _TRIGLYCERIDEMILLIMOLESPERLITRE:
                return TRIGLYCERIDE_MM_PER_L;
            case _FASTINGGLUCOSEMILLIMOLESPERLITRE:
                return FASTINGGLUCOSE_MM_PER_L;
            case _LDLCHOLESTROLMILLIGRAMSPERDECILITRE:
                return LDLCHOLESTEROL_MG_PER_DL;
            case _HDLCHOLESTROLMILLIGRAMSPERDECILITRE:
                return HDLCHOLESTEROL_MG_PER_DL;
            case _LDLCHOLESTROLMILLIMOLESPERLITRE:
                return LDLCHOLESTEROL_MM_PER_L;
            case _HDLCHOLESTROLMILLIMOLESPERLITRE:
                return HDLCHOLESTEROL_MM_PER_L;
            case _RANDOMGLUCOSEMILLIMOLESPERLITRE:
                return RANDOMGLUCOSE_MM_PER_L;
            case _RANDOMGLUCOSEMILLIGRAMSPERDECILITRE:
                return RANDOMGLUCOSE_MG_PER_DL;
            case _SYSTOLICMILLIMETEROFMERCURY:
                return SYSTOLIC_MILLIMETER_OF_MERCURY;
            case _PERDAY:
                return PER_DAY;
            case _PERWEEK:
                return PER_WEEK;
            case _MINUTES:
                return MINUTES;
            case _HOURS:
                return HOURS;
            case _BMI:
                return BMI;
            case _STEPS:
                return STEPS;
            case _HEARTRATE:
                return AVERAGEHEARTRATE;
            case _KILOMETERSPERHOUR:
                return KILOMETERS_PER_HOUR;
            case _DAYS:
                return DAYS;
            case _STONEPOUND:
                return STONEPOUND;
            case _KILOCALORIES:
                return KILOCALORIES;
            case _METERSPERSECOND:
                return METERS_PER_SECOND;
            case _MINUTESPERKILOMETER:
                return MINUTES_PER_KILOMETER;
            case _BEATSPERMINUTE:
                return BEATS_PER_MINUTE;
            default:
                return UNKNOWN;
        }
    }

    public String getTypeKey() {
        return String.valueOf(typeKey);
    }
}
