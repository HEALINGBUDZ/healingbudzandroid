package com.codingpixel.healingbudz.test_data;

import com.codingpixel.healingbudz.DataModel.HomeDrawerDataModel;
import com.codingpixel.healingbudz.R;

import java.util.ArrayList;


public class HomeDrawerData {
    public static ArrayList<HomeDrawerDataModel> home_drawer_data() {
        ArrayList<HomeDrawerDataModel> datas = new ArrayList<>();
        datas.add(new HomeDrawerDataModel(R.drawable.add_new_list, "", "Add a Business Listing"));
        datas.add(new HomeDrawerDataModel(R.drawable.ic_user_person, "", "All Budz"));
        datas.add(new HomeDrawerDataModel(R.drawable.ic_my_wall, "", "My Buzz"));
        datas.add(new HomeDrawerDataModel(R.drawable.ic_activity_log, "", "Activity Log"));
        datas.add(new HomeDrawerDataModel(R.drawable.ic_my_messages, "", "Messages"));
//        datas.add(new HomeDrawerDataModel(R.drawable.ic_my_journal, "", "My Journal"));
        datas.add(new HomeDrawerDataModel(R.drawable.ic_my_questions, "", "My Questions"));
        datas.add(new HomeDrawerDataModel(R.drawable.ic_my_answers, "", "My Answers"));
//        datas.add(new HomeDrawerDataModel(R.drawable.ic_my_groups, "", "My Groups"));
        datas.add(new HomeDrawerDataModel(R.drawable.ic_my_strains, "", "My Strains"));
        datas.add(new HomeDrawerDataModel(R.drawable.budzmapnewic, "", "My Budz Adz"));
        datas.add(new HomeDrawerDataModel(R.drawable.ic_myrewards, "", "My Rewards"));
        datas.add(new HomeDrawerDataModel(R.drawable.ic_my_heart, "", "My Saves"));
        return datas;
    }
}
