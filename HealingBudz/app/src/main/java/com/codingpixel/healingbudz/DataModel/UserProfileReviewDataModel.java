package com.codingpixel.healingbudz.DataModel;

import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

/**
 * Created by macmini on 20/12/2017.
 */

public class UserProfileReviewDataModel extends ExpandableGroup<UserProfileQATabExpandAbleData> {
    public UserProfileReviewDataModel(String title, List<UserProfileQATabExpandAbleData> items) {
        super(title, items);
    }
}