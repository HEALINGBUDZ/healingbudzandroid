package com.codingpixel.healingbudz.DataModel;

/**
 * Created by jawadali on 8/16/17.
 */

public class HomeSearchListData {
    String Title;
    String Discription ;
    int icon ;
    String Color;
    int id ;
    String type;
    String imagePath;
    boolean isPremium;
    int user_point;

    public int getUser_point() {
        return user_point;
    }

    public void setUser_point(int user_point) {
        this.user_point = user_point;
    }

    public void setPremium(boolean premium) {
        isPremium = premium;
    }

    public boolean isPremium() {
        return isPremium;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getImagePath() {
        return imagePath;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public HomeSearchListData(){
        super();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public HomeSearchListData(String title , String discription , int icon , String  color){
        this.Title = title;
        this.Discription = discription ;
        this.icon = icon ;
        this.Color = color;
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

    public String getColor() {
        return Color;
    }

    public void setColor(String color) {
        Color = color;
    }
}
