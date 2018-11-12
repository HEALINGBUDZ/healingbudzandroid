package com.codingpixel.healingbudz.DataModel;

/**
 * Created by jawadali on 9/6/17.
 */

public class FollowerJournalDialogDataModel {
    private String Name;
    private boolean isFollow;

    public FollowerJournalDialogDataModel(String name, boolean isFollow) {
        this.Name = name;
        this.isFollow = isFollow;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public boolean isFollow() {
        return isFollow;
    }

    public void setFollow(boolean follow) {
        isFollow = follow;
    }
}
