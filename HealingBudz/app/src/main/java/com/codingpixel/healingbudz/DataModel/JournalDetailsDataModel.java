package com.codingpixel.healingbudz.DataModel;

import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

public class JournalDetailsDataModel extends ExpandableGroup<JournalDetailsExpandAbleData> {
    public JournalDetailsDataModel(String title, List<JournalDetailsExpandAbleData> items) {
        super(title, items);
    }
}
