package com.codingpixel.healingbudz.DataModel;

/**
 * Created by jawadali on 8/16/17.
 */

public class SlideMenuActivityListData {
    String Title;
    String Discription ;
    int icon ;
    String Color;
    String date_str;

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    String  Type ;
    String  model ;
    int type_id;

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public SlideMenuActivityListData(int type_id,String title , String discription , int icon , String  color , String date_str ){
        this.Title = title;
        this.Discription = discription ;
        this.icon = icon ;
        this.Color = color;
        this.date_str = date_str;
        this.type_id = type_id;
    }
    public SlideMenuActivityListData(String  model,int type_id,String title , String discription , int icon , String  color , String date_str, String type){
        this.Title = title;
        this.Discription = discription ;
        this.icon = icon ;
        this.Color = color;
        this.date_str = date_str;
        this.Type = type ;
        this.type_id = type_id;
        this.model = model;
    }

    public int getType_id() {
        return type_id;
    }

    public void setType_id(int type_id) {
        this.type_id = type_id;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getDiscription() {
        return Discription;
    }

    public void setDiscription(String discription) {
        Discription = discription;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getDate_str() {
        return date_str;
    }

    public void setDate_str(String date_str) {
        this.date_str = date_str;
    }

    public String getColor() {
        return Color;
    }

    public void setColor(String color) {
        Color = color;
    }
}
