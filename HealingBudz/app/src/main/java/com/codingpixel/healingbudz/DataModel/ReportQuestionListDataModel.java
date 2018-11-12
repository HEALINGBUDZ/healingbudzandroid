package com.codingpixel.healingbudz.DataModel;

/**
 * Created by codingpixel on 02/08/2017.
 */

public class ReportQuestionListDataModel {

    String Title ;
    int image_resourse;
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

    public ReportQuestionListDataModel(String title , int image_resourse , boolean isSelected){
       this.Title = title ;
       this.image_resourse = image_resourse ;
        this.isSelected = isSelected ;

    }
    public ReportQuestionListDataModel(String title ,  boolean isSelected){
        this.Title = title ;
        this.isSelected = isSelected ;

    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

}
