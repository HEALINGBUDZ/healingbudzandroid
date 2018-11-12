package com.codingpixel.healingbudz.DataModel;

import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

public class UserProfileQATabDataModel extends ExpandableGroup<UserProfileQATabExpandAbleData> {
    public UserProfileQATabDataModel(String title, List<UserProfileQATabExpandAbleData> items) {
        super(title, items);
    }
}
