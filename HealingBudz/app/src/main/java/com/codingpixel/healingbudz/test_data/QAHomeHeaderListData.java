package com.codingpixel.healingbudz.test_data;

import com.codingpixel.healingbudz.DataModel.QAHomeHeaderListDataModel;
import com.codingpixel.healingbudz.DataModel.ReportQuestionListDataModel;
import com.codingpixel.healingbudz.R;

import java.util.ArrayList;

public class QAHomeHeaderListData {
    public static ArrayList<QAHomeHeaderListDataModel> qa_home_header_data() {
        ArrayList<QAHomeHeaderListDataModel> datas = new ArrayList<>();
        datas.add(new QAHomeHeaderListDataModel("NEWEST", "", R.drawable.ic_newest_qa, false));
        datas.add(new QAHomeHeaderListDataModel("FAVORITES", "", R.drawable.ic_fevorite, false));
        datas.add(new QAHomeHeaderListDataModel("UNANSWERED", "", R.drawable.ic_unanswered_qa, false));
        datas.add(new QAHomeHeaderListDataModel("TRENDING", "", R.drawable.ic_hottest_filter, false));
        datas.add(new QAHomeHeaderListDataModel("MY QUESTIONS", "", R.drawable.ic_my_question_qa, false));
        datas.add(new QAHomeHeaderListDataModel("MY ANSWERS", "", R.drawable.ic_myanswer_qa, false));
        return datas;
    }

    public static ArrayList<ReportQuestionListDataModel> report_question_list() {
        ArrayList<ReportQuestionListDataModel> datas = new ArrayList<>();
        datas.add(new ReportQuestionListDataModel("Nudity or sexual content",R.drawable.ic_double_circle, false));
        datas.add(new ReportQuestionListDataModel("Harassment or hate speech",R.drawable.ic_double_circle, false));
        datas.add(new ReportQuestionListDataModel("Threatening, violent, or concerning", R.drawable.ic_double_circle,false));
        datas.add(new ReportQuestionListDataModel("Spam", R.drawable.ic_double_circle, false));
        datas.add(new ReportQuestionListDataModel("Answer is offensive", R.drawable.ic_double_circle, false));
        datas.add(new ReportQuestionListDataModel("Unrelated", R.drawable.ic_double_circle, false));
        return datas;
    }

    public static ArrayList<ReportQuestionListDataModel> report_main_question_list() {
        ArrayList<ReportQuestionListDataModel> datas = new ArrayList<>();
        datas.add(new ReportQuestionListDataModel("Nudity or sexual content",R.drawable.ic_double_circle, false));
        datas.add(new ReportQuestionListDataModel("Harassment or hate speech",R.drawable.ic_double_circle, false));
        datas.add(new ReportQuestionListDataModel("Threatening, violent, or concerning", R.drawable.ic_double_circle,false));
        datas.add(new ReportQuestionListDataModel("Spam", R.drawable.ic_double_circle, false));
        datas.add(new ReportQuestionListDataModel("Question is offensive", R.drawable.ic_double_circle, false));
        datas.add(new ReportQuestionListDataModel("Unrelated", R.drawable.ic_double_circle, false));
        return datas;
    }
}
