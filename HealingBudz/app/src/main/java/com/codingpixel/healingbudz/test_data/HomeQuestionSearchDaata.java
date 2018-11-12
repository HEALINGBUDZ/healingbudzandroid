package com.codingpixel.healingbudz.test_data;

import com.codingpixel.healingbudz.DataModel.HomeFragmentQuestionSearchData;

import java.util.ArrayList;

/**
 * Created by codingpixel on 01/08/2017.
 */

public class HomeQuestionSearchDaata {

    public static ArrayList<HomeFragmentQuestionSearchData> home_search_questions(){
        ArrayList<HomeFragmentQuestionSearchData> datas = new ArrayList<>();
        datas.add(new HomeFragmentQuestionSearchData("Should I smoke it?" , "20"));
        datas.add(new HomeFragmentQuestionSearchData("Should I bake it at home?" , "11"));
        datas.add(new HomeFragmentQuestionSearchData("Should I use it in an infuser?" , "17"));
        datas.add(new HomeFragmentQuestionSearchData("Should I eat it with other greens?" , "102"));
        return datas;
    }
}
