package com.codingpixel.healingbudz.test_data;

import com.codingpixel.healingbudz.DataModel.JournalHeaderDataModel;
import com.codingpixel.healingbudz.R;

import java.util.ArrayList;

/**
 * Created by jawadali on 8/30/17.
 */

public class JournalHeaderListData {
    public static ArrayList<JournalHeaderDataModel> journal_headerData(){
        ArrayList<JournalHeaderDataModel> datas = new ArrayList<>();
        datas.add(new JournalHeaderDataModel("NEWEST","" , R.drawable.ic_journal_header_new, false));
        datas.add(new JournalHeaderDataModel("ALPHABETICAL" ,"A-Z" , 0 , false));
        datas.add(new JournalHeaderDataModel("FAVORITES","" , R.drawable.ic_journal_favorite_header, false));
        return datas;
    }
}
