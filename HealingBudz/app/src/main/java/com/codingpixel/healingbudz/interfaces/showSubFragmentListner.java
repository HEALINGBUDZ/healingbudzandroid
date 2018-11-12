package com.codingpixel.healingbudz.interfaces;

import com.codingpixel.healingbudz.DataModel.HomeQAfragmentDataModel;

/**
 * Created by jawadali on 12/13/17.
 */

public interface showSubFragmentListner {
    public void ShowQuestions(HomeQAfragmentDataModel dataModel , boolean isId);
    public void ShowAnswers(HomeQAfragmentDataModel dataModel , int AnswerId ,boolean isOnlyAnswer);
    public void ShowStrainSearch(HomeQAfragmentDataModel dataModel , String keyword);
}
