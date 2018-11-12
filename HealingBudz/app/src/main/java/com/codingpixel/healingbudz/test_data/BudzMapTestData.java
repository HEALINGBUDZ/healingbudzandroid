package com.codingpixel.healingbudz.test_data;

import com.codingpixel.healingbudz.DataModel.BudzMapDataModel;
import com.codingpixel.healingbudz.R;

import java.util.ArrayList;


public class BudzMapTestData {

    public static ArrayList<BudzMapDataModel> budz_map_test_data() {
        ArrayList<BudzMapDataModel> list = new ArrayList<>();
        list.add(new BudzMapDataModel(0,"South Miami Natural Pharmaceuticals","3.5 mi",3,5,true,R.drawable.ic_profile_blue,true,true,-31.954243, 115.810453));
        list.add(new BudzMapDataModel(1,"South Miami Natural Pharmaceuticals","3.5 mi",3,5,true,R.drawable.ic_profile_gray,true,false,-31.954243, 115.810453));
        list.add(new BudzMapDataModel(2,"South Miami Natural Pharmaceuticals","3.5 mi",3,5,false,R.drawable.ic_user_profile_green,false,false,-31.954243, 115.810453));
        list.add(new BudzMapDataModel(3,"South Miami Natural Pharmaceuticals","3.5 mi",3,5,false,R.drawable.ic_discuss_question_profile,true,true,-31.954243, 115.810453));
        list.add(new BudzMapDataModel(4,"South Miami Natural Pharmaceuticals","3.5 mi",3,5,false,R.drawable.ic_profile_blue,false,false,-31.954243, 115.810453));
        list.add(new BudzMapDataModel(1,"South Miami Natural Pharmaceuticals","3.5 mi",3,5,false,R.drawable.ic_profile_gray,false,false,-31.954243, 115.810453));
        list.add(new BudzMapDataModel(3,"South Miami Natural Pharmaceuticals","3.5 mi",3,5,false,R.drawable.ic_discuss_question_profile,true,false,-31.954243, 115.810453));
        list.add(new BudzMapDataModel(2,"South Miami Natural Pharmaceuticals","3.5 mi",3,5,false,R.drawable.ic_profile_gray,true,false,-31.954243, 115.810453));
        list.add(new BudzMapDataModel(0,"South Miami Natural Pharmaceuticals","3.5 mi",3,5,false,R.drawable.ic_profile_blue,false,true,-31.954243, 115.810453));
        list.add(new BudzMapDataModel(1,"South Miami Natural Pharmaceuticals","3.5 mi",3,5,false,R.drawable.ic_user_profile_green,false,false,-31.954243, 115.810453));
        return list ;
    }
}
