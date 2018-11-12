package com.codingpixel.healingbudz.DataModel;

public class NotificationAndAlertSettingDataModel {
    private String Heading;
    private  String Title;
    private  boolean isCheck;

    public String getHeading() {
        return Heading;
    }

    public void setHeading(String heading) {
        Heading = heading;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    public NotificationAndAlertSettingDataModel(String heading, String title, boolean isCheck){
        this.Heading = heading;
        this.Title = title;
        this.isCheck = isCheck;


    }
}
