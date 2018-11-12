package com.codingpixel.healingbudz.test_data;

import com.codingpixel.healingbudz.DataModel.NotificationAndAlertSettingDataModel;
import com.codingpixel.healingbudz.sharedprefrences.SharedPrefrences;

import java.util.ArrayList;

import static com.codingpixel.healingbudz.application.HealingBudApplication.getContext;


public class Notification_Alert_Setting_testData {

    public static ArrayList<NotificationAndAlertSettingDataModel> notification_data(){
        ArrayList<NotificationAndAlertSettingDataModel> dataModels = new ArrayList<>();
       //0
        if (!SharedPrefrences.getBool("notification_setting_0", getContext())) {
            dataModels.add(new NotificationAndAlertSettingDataModel("General", "New Question Push Notification", false));
        }else {
            dataModels.add(new NotificationAndAlertSettingDataModel("General", "New Question Push Notification", true));

        }
        //1
        if (!SharedPrefrences.getBool("notification_setting_1", getContext())) {
            dataModels.add(new NotificationAndAlertSettingDataModel("Buzz Feed" , "Q&A Discussions You Follow" , false));
        }else {
            dataModels.add(new NotificationAndAlertSettingDataModel("Buzz Feed" , "Q&A Discussions You Follow" , true));

        }

        //2
//        dataModels.add(new NotificationAndAlertSettingDataModel("Group:" , "" , false));


        //3
//        if (!SharedPrefrences.getBool("notification_setting_3", getContext())) {
//            dataModels.add(new NotificationAndAlertSettingDataModel("", "Public Group You've Joined:", false));
//        }else {
//            dataModels.add(new NotificationAndAlertSettingDataModel("", "Public Group You've Joined:", true));
//        }
//
//
//        //4
//        if (!SharedPrefrences.getBool("notification_setting_4", getContext())) {
//            dataModels.add(new NotificationAndAlertSettingDataModel("", "Private Group You've Joined:", false));
//        }else {
//            dataModels.add(new NotificationAndAlertSettingDataModel("", "Private Group You've Joined:", true));
//        }


        //2
        if (!SharedPrefrences.getBool("notification_setting_5", getContext())) {

            dataModels.add(new NotificationAndAlertSettingDataModel("", "Update to Strains You Follow", false));

        }else {
            dataModels.add(new NotificationAndAlertSettingDataModel("", "Update to Strains You Follow", true));
        }




        //3
        dataModels.add(new NotificationAndAlertSettingDataModel("Budz Adz:" , "" , false));

        //4
        if (!SharedPrefrences.getBool("notification_setting_7", getContext())) {
            dataModels.add(new NotificationAndAlertSettingDataModel("", "Specials", false));
        }else {
            dataModels.add(new NotificationAndAlertSettingDataModel("", "Specials", true));
        }

        //5
        if (!SharedPrefrences.getBool("notification_setting_8", getContext())) {
            dataModels.add(new NotificationAndAlertSettingDataModel("", "Business Shout Outs", false));
        }else {
            dataModels.add(new NotificationAndAlertSettingDataModel("", "Business Shout Outs", true));
        }


        //6
        if (!SharedPrefrences.getBool("notification_setting_9", getContext())) {

            dataModels.add(new NotificationAndAlertSettingDataModel("", "Messages", false));
        }else {
            dataModels.add(new NotificationAndAlertSettingDataModel("", "Messages", true));

        }

        //7
        dataModels.add(new NotificationAndAlertSettingDataModel("Budz Following" , "" , false));

        //8
        if (!SharedPrefrences.getBool("notification_setting_11", getContext())) {

            dataModels.add(new NotificationAndAlertSettingDataModel("", "Your Profile", false));
        }else {
            dataModels.add(new NotificationAndAlertSettingDataModel("", "Your Profile", true));
        }

//        //12
//        if (!SharedPrefrences.getBool("notification_setting_12", getContext())) {
//            dataModels.add(new NotificationAndAlertSettingDataModel("", "Your Journals", false));
//        }else {
//            dataModels.add(new NotificationAndAlertSettingDataModel("", "Your Journals", true));
//        }

        //9
        if (!SharedPrefrences.getBool("notification_setting_13", getContext())) {
            dataModels.add(new NotificationAndAlertSettingDataModel("", "Your Created Strains", false));
        }else {
            dataModels.add(new NotificationAndAlertSettingDataModel("", "Your Created Strains", true));
        }


        //10
        dataModels.add(new NotificationAndAlertSettingDataModel("Budz Who Like/Dislike" , "" , false));


        //11
        if (!SharedPrefrences.getBool("notification_setting_15", getContext())) {
            dataModels.add(new NotificationAndAlertSettingDataModel("" , "Your Questions" , false));

        }else {
            dataModels.add(new NotificationAndAlertSettingDataModel("" , "Your Questions" , true));

        }

        //12
        if (!SharedPrefrences.getBool("notification_setting_16", getContext())) {
            dataModels.add(new NotificationAndAlertSettingDataModel("" , "Your Answers" , false));

        }else {
            dataModels.add(new NotificationAndAlertSettingDataModel("" , "Your Answers" , true));

        }

//        //17
//        if (!SharedPrefrences.getBool("notification_setting_17", getContext())) {
//            dataModels.add(new NotificationAndAlertSettingDataModel("" , "Your Journal Entries " , false));
//
//        }else {
//            dataModels.add(new NotificationAndAlertSettingDataModel("" , "Your Journal Entries " , true));
//
//        }
        return dataModels;
    }
}
