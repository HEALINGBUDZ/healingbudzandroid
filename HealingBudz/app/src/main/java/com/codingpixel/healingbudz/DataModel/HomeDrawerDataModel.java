package com.codingpixel.healingbudz.DataModel;

/**
 * Created by codingpixel on 01/08/2017.
 */

public class HomeDrawerDataModel {
    int image_rsourse;
    String text_image;
    String title;
    public HomeDrawerDataModel(int image_rsourse , String text_image , String  title){
        this.image_rsourse = image_rsourse ;
        this.text_image = text_image ;
        this.title = title;
    }

    public int getImage_rsourse() {
        return image_rsourse;
    }

    public void setImage_rsourse(int image_rsourse) {
        this.image_rsourse = image_rsourse;
    }

    public String getText_image() {
        return text_image;
    }

    public void setText_image(String text_image) {
        this.text_image = text_image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
