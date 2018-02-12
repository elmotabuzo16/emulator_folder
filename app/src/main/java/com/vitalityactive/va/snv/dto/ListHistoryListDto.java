package com.vitalityactive.va.snv.dto;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by dharel.h.rosell on 12/4/2017.
 */

public class ListHistoryListDto {

    String dateString;
    List<HistoryDetailDto> screeningsList;
    List<HistoryDetailDto> vaccinationList;

    public ListHistoryListDto() {
    }

    public ListHistoryListDto(String dateString, List<HistoryDetailDto> screeningsList, List<HistoryDetailDto> vaccinationList) {
        this.dateString = dateString;
        this.screeningsList = screeningsList;
        this.vaccinationList = vaccinationList;
    }

    public void setScreeningList(HistoryDetailDto detail) {
        this.screeningsList.add(detail);
    }

    public void setVaccinationList(HistoryDetailDto detailDto){
        this.vaccinationList.add(detailDto);
    }

    public String getDateString() {
        return dateString;
    }

    public void setDateString(String dateString) {
        this.dateString = dateString;
    }

    public List<HistoryDetailDto> getScreeningsList() {
        return screeningsList;
    }

    public void setScreeningsList(List<HistoryDetailDto> screeningsList) {
        this.screeningsList = screeningsList;
    }

    public List<HistoryDetailDto> getVaccinationList() {
        return vaccinationList;
    }

    public void setVaccinationList(List<HistoryDetailDto> vaccinationList) {
        this.vaccinationList = vaccinationList;
    }

    public String getDateStringMessage() {
        return getDateMessage(dateString);
    }

    public String transforDetailedDate(String detailedDate) {
        StringBuilder dateStringBuilder = new StringBuilder("Tested on ");
        dateStringBuilder.append(detailedDate);
        return dateStringBuilder.toString();
    }

    public String getDateMessage(String stringDate) {
        String dateTime = "";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Log.d("TEST DATE TIME", "TRY");
            Date dateTrue = format.parse(stringDate);
            Log.d("TEST STRING DATE1", dateTrue.toString());
            format = new SimpleDateFormat("EEE, d MMM yyyy");
            dateTime = format.format(dateTrue);
            Log.d("TEST STRING DATE2", dateTime.toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return dateTime;
    }
}
