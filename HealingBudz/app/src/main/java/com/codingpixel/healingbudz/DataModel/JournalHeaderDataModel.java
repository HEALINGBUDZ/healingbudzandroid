package com.codingpixel.healingbudz.DataModel;


public class JournalHeaderDataModel {
    String Title ;
    int image_resourse;
    String  Text_image;
    boolean isSelected;

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public int getImage_resourse() {
        return image_resourse;
    }

    public void setImage_resourse(int image_resourse) {
        this.image_resourse = image_resourse;
    }

    public JournalHeaderDataModel(String title ,String text_image ,int image_resourse , boolean isSelected){
        this.Title = title ;
        this.image_resourse = image_resourse ;
        this.Text_image = text_image;
        this.isSelected = isSelected ;

    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getText_image() {
        return Text_image;
    }

    public void setText_image(String text_image) {
        Text_image = text_image;
    }

}
