package com.codingpixel.healingbudz.interfaces;

import com.codingpixel.healingbudz.DataModel.HomeQAfragmentDataModel;
import com.codingpixel.healingbudz.DataModel.QuestionAnswersDataModel;

public interface QAAddSubFragmentListner {
    public void AddFirstAnswerBudFragment(HomeQAfragmentDataModel dataModel);

    public void AddDiscussAnswerFragment(HomeQAfragmentDataModel dataModel);

    public void AddReplyAnswerFragment(HomeQAfragmentDataModel dataModel);

    public void EditReplyAnswerFragment(HomeQAfragmentDataModel dataModel, QuestionAnswersDataModel answersDataModel);
}

