package com.codingpixel.healingbudz.test_data;

import com.codingpixel.healingbudz.DataModel.JournalHeaderDataModel;
import com.codingpixel.healingbudz.R;

import java.util.ArrayList;

/**
 * Created by jawadali on 8/30/17.
 */

public class GroupsHeaderListData {
    public static ArrayList<JournalHeaderDataModel> groups_headerData() {
        ArrayList<JournalHeaderDataModel> datas = new ArrayList<>();
        datas.add(new JournalHeaderDataModel("NEWEST", "", R.drawable.ic_groups_header_new, false));
        datas.add(new JournalHeaderDataModel("ALPHABETICAL", "A-Z", 0, false));
        datas.add(new JournalHeaderDataModel("JOINED", "", R.drawable.ic_groups_add, false));
        return datas;
    }

    public static ArrayList<JournalHeaderDataModel> activity_log_groups_headerData() {
        ArrayList<JournalHeaderDataModel> datas = new ArrayList<>();
//        datas.add(new JournalHeaderDataModel("QUESTIONS","" ,R.drawable.ic_question_icon , false));
        datas.add(new JournalHeaderDataModel("ANSWERS", "", R.drawable.ic_answer_icon, false));
        datas.add(new JournalHeaderDataModel("FAVORITES", "", R.drawable.ic_fevorite_act, false));
        datas.add(new JournalHeaderDataModel("LIKES", "", R.drawable.ic_thumb_licked, false));
//        datas.add(new JournalHeaderDataModel("GROUPS","" , R.drawable.ic_tab_group, false));
//        datas.add(new JournalHeaderDataModel("JOURNAL","" , R.drawable.ic_tab_journal, false));
//        datas.add(new JournalHeaderDataModel("TAGS","" , R.drawable.ic_hashtag, false));
//        datas.add(new JournalHeaderDataModel("BUDZ MAP", "", R.drawable.ic_tab_budz_map, false));
        datas.add(new JournalHeaderDataModel("STRAINS", "", R.drawable.ic_tab_strain, false));
        datas.add(new JournalHeaderDataModel("COMMENTS", "", R.drawable.comment_icon, false));
        datas.add(new JournalHeaderDataModel("POSTS", "", R.drawable.social_wall_new, false));


        return datas;
    }
}
