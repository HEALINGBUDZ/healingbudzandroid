package com.codingpixel.healingbudz.test_data;

import com.codingpixel.healingbudz.DataModel.JournalDetailsDataModel;
import com.codingpixel.healingbudz.DataModel.JournalDetailsExpandAbleData;

import java.util.ArrayList;
import java.util.List;

public class JournalDetailsTestdata {
    public static List<JournalDetailsDataModel> get_journal_Details_recyler_Data(){
        List<JournalDetailsDataModel> dataModels =  new ArrayList<>();
        List<JournalDetailsExpandAbleData> su = new ArrayList<>();
        su.add(new JournalDetailsExpandAbleData("Test", false));
        su.add(new JournalDetailsExpandAbleData("Test2", false));

        List<JournalDetailsExpandAbleData> su1 = new ArrayList<>();
        su1.add(new JournalDetailsExpandAbleData("Test", false));
        su1.add(new JournalDetailsExpandAbleData("Test2", false));
        su1.add(new JournalDetailsExpandAbleData("Test2", false));


        dataModels.add(new JournalDetailsDataModel("February,17" , su));
        dataModels.add(new JournalDetailsDataModel("January,17" , su1));
        dataModels.add(new JournalDetailsDataModel("December,16" , su));
        dataModels.add(new JournalDetailsDataModel("November,16" , su1));
        return  dataModels;
    }

    public static List<JournalDetailsDataModel> get_help_support_recyler_Data(){
        List<JournalDetailsDataModel> dataModels = new ArrayList<>();
        List<JournalDetailsExpandAbleData> su = new ArrayList<>();
        su.add(new JournalDetailsExpandAbleData("Test", false));
        dataModels.add(new JournalDetailsDataModel("Support Message Center" , su));
        dataModels.add(new JournalDetailsDataModel("Contact" , su));
        dataModels.add(new JournalDetailsDataModel("Legal" , su));
        return  dataModels;
    }

    public static List<JournalDetailsDataModel> get_user_profile_qa_recyler_Data(){
        List<JournalDetailsDataModel> dataModels = new ArrayList<>();
        List<JournalDetailsExpandAbleData> su = new ArrayList<>();
        su.add(new JournalDetailsExpandAbleData("Test", false));
        su.add(new JournalDetailsExpandAbleData("Test", false));
        su.add(new JournalDetailsExpandAbleData("Test", false));
        su.add(new JournalDetailsExpandAbleData("Test", false));
        su.add(new JournalDetailsExpandAbleData("Test", false));

        dataModels.add(new JournalDetailsDataModel("My Questions" , su));
        dataModels.add(new JournalDetailsDataModel("My Answers" , su));
        return  dataModels;
    }

    public static List<JournalDetailsDataModel> get_user_profile_journal_recyler_Data(){
        List<JournalDetailsDataModel> dataModels = new ArrayList<>();
        List<JournalDetailsExpandAbleData> su = new ArrayList<>();
        su.add(new JournalDetailsExpandAbleData("Journal Name", false));
        su.add(new JournalDetailsExpandAbleData("Journal Name", false));
        su.add(new JournalDetailsExpandAbleData("Journal Name", false));
        su.add(new JournalDetailsExpandAbleData("Journal Name", false));
        su.add(new JournalDetailsExpandAbleData("Journal Name", false));

        dataModels.add(new JournalDetailsDataModel("My Journals" , su));
        dataModels.add(new JournalDetailsDataModel("Journals I Follow" , su));
        return  dataModels;
    }



    public static List<JournalDetailsDataModel> get_user_profile_review_recyler_Data(){
        List<JournalDetailsDataModel> dataModels = new ArrayList<>();
        List<JournalDetailsExpandAbleData> su = new ArrayList<>();
        su.add(new JournalDetailsExpandAbleData("Test", false));
        su.add(new JournalDetailsExpandAbleData("Test", false));

        List<JournalDetailsExpandAbleData> su1 = new ArrayList<>();
        su1.add(new JournalDetailsExpandAbleData("Test", false));
        su1.add(new JournalDetailsExpandAbleData("Test", false));
        su1.add(new JournalDetailsExpandAbleData("Test", false));


        dataModels.add(new JournalDetailsDataModel("Budz Adz Reviews" , su));
        dataModels.add(new JournalDetailsDataModel("Strains Review" , su1));
        return  dataModels;
    }
}
