package com.codingpixel.healingbudz.interfaces;

import android.location.Location;

/**
 * Created by jawadali on 12/5/17.
 */

public interface UserLocationListner {
    public void onLocationSuccess(Location location);
    public void onLocationFailed(String Error);
}
