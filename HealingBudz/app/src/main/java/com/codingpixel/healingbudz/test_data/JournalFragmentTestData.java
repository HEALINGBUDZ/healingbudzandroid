package com.codingpixel.healingbudz.test_data;

import com.codingpixel.healingbudz.DataModel.JournalFragmentDataModel;

import java.util.ArrayList;

/**
 * Created by jawadali on 8/30/17.
 */

public class JournalFragmentTestData {
    public static ArrayList<JournalFragmentDataModel> get_journal_data(){
        ArrayList<JournalFragmentDataModel> dataModels = new ArrayList<>();
        dataModels.add(new JournalFragmentDataModel(false, true));
        dataModels.add(new JournalFragmentDataModel(false, false));
        dataModels.add(new JournalFragmentDataModel(false, false));
        dataModels.add(new JournalFragmentDataModel(false, false));
        return dataModels;
    }
}
