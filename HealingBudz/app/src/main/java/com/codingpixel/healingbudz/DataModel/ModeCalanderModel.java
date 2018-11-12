package com.codingpixel.healingbudz.DataModel;

/**
 * Created by mtalh on 05-Oct-17.
 */

public class ModeCalanderModel {
    private String date;
    private boolean Date_Helighted = false;
    private boolean Month_Dark = false;
    private boolean ToDayDate = false;
    int date_entry;

    public ModeCalanderModel(String date, boolean date_Helighted, boolean month_Dark, boolean toDayDate) {
        this.date = date;
        Date_Helighted = date_Helighted;
        Month_Dark = month_Dark;
        ToDayDate = toDayDate;
    }

    public ModeCalanderModel(String date, boolean date_Helighted, boolean month_Dark, boolean toDayDate, int entry) {
        this.date = date;
        Date_Helighted = date_Helighted;
        Month_Dark = month_Dark;
        ToDayDate = toDayDate;
        this.date_entry = entry;
    }

    public int getDate_entry() {
        return date_entry;
    }

    public void setDate_entry(int date_entry) {
        this.date_entry = date_entry;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isDate_Helighted() {
        return Date_Helighted;
    }

    public void setDate_Helighted(boolean date_Helighted) {
        Date_Helighted = date_Helighted;
    }

    public boolean isMonth_Dark() {
        return Month_Dark;
    }

    public void setMonth_Dark(boolean month_Dark) {
        Month_Dark = month_Dark;
    }

    public boolean isToDayDate() {
        return ToDayDate;
    }

    public void setToDayDate(boolean toDayDate) {
        ToDayDate = toDayDate;
    }
}
