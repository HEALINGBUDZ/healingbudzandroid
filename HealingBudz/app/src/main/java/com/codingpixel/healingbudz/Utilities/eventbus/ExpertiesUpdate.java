package com.codingpixel.healingbudz.Utilities.eventbus;

import com.codingpixel.healingbudz.activity.Registration.AddExpertiseQuestionActivity;

import java.util.List;

public class ExpertiesUpdate {
    private List<AddExpertiseQuestionActivity.DataModelEntryExpert> list;
    private List<AddExpertiseQuestionActivity.DataModelEntryExpert> list2;

    public ExpertiesUpdate(List<AddExpertiseQuestionActivity.DataModelEntryExpert> temp, List<AddExpertiseQuestionActivity.DataModelEntryExpert> temp2) {
        list = temp;
        list2 = temp2;
    }

    public List<AddExpertiseQuestionActivity.DataModelEntryExpert> getList() {
        return list;
    }

    public List<AddExpertiseQuestionActivity.DataModelEntryExpert> getList2() {
        return list2;
    }
}
