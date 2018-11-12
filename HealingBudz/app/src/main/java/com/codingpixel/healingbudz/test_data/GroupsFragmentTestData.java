package com.codingpixel.healingbudz.test_data;

import com.codingpixel.healingbudz.DataModel.GroupsDataModel;

import java.util.ArrayList;
public class GroupsFragmentTestData {
    public static ArrayList<GroupsDataModel> groups_fragement_Data(){
        ArrayList<GroupsDataModel> dataModels = new ArrayList<>();
        dataModels.add(new GroupsDataModel("California Healing" , "122 Budz"));
        dataModels.add(new GroupsDataModel("Medical Marijuana for Dummies" , "64 Budz"));
        dataModels.add(new GroupsDataModel("Dakota Doctors" , "114 Budz"));
        dataModels.add(new GroupsDataModel("Cures for Cancer" , "82 Budz"));
        dataModels.add(new GroupsDataModel("MMJ Oillow Talk" , "151 Budz"));

        return dataModels;
    }
}
