package com.codingpixel.healingbudz.DataModel;

/**
 * Created by macmini on 20/12/2017.
 */

public class RatingSumModel {
    public Integer strainId;
    public Integer total;

    public RatingSumModel() {
    }

    public Integer getStrainId() {
        return strainId;
    }

    public void setStrainId(Integer strainId) {
        this.strainId = strainId;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }
}
