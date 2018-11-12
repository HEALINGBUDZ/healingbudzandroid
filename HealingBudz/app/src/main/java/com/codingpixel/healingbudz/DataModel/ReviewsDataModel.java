package com.codingpixel.healingbudz.DataModel;

import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.ArrayList;

public class ReviewsDataModel extends ExpandableGroup<BudzMapReviews> {
    public ReviewsDataModel(String title, ArrayList<BudzMapReviews> items) {
        super(title, items);
    }
}
