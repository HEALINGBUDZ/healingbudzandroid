package com.codingpixel.healingbudz.DataModel;

/**
 * Created by codingpixel on 01/08/2017.
 */

public class HomeFragmentQuestionSearchData {
    String Questions;
    String answer_count;

    public HomeFragmentQuestionSearchData(String questions , String answer_count){
        this.Questions = questions ;
        this.answer_count = answer_count;
    }
    public String getQuestions() {
        return Questions;
    }

    public void setQuestions(String questions) {
        Questions = questions;
    }

    public String getAnswer_count() {
        return answer_count;
    }

    public void setAnswer_count(String answer_count) {
        this.answer_count = answer_count;
    }
}
