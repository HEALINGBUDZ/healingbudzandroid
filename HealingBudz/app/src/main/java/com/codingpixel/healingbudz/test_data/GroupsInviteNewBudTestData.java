package com.codingpixel.healingbudz.test_data;

import com.codingpixel.healingbudz.DataModel.GroupsInviteNewBudDataModel;

import java.util.ArrayList;

public class GroupsInviteNewBudTestData {

    public static ArrayList<GroupsInviteNewBudDataModel> get_invited_groups_bud_data(){
        ArrayList<GroupsInviteNewBudDataModel> dataModels = new ArrayList<>();
        dataModels.add(new GroupsInviteNewBudDataModel("Sam Doe" , false));
        dataModels.add(new GroupsInviteNewBudDataModel("Samuel Doe" , false));
        dataModels.add(new GroupsInviteNewBudDataModel("Alex Sebonge" , false));
        dataModels.add(new GroupsInviteNewBudDataModel("Shan Paul" , false));
        dataModels.add(new GroupsInviteNewBudDataModel("Alexander Joy" , false));
        dataModels.add(new GroupsInviteNewBudDataModel("Chi Najoko" , false));
        return dataModels;
    }
}
