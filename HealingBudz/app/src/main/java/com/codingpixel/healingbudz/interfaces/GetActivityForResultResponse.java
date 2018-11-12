package com.codingpixel.healingbudz.interfaces;

import android.content.Intent;

/**
 * Created by jawadali on 12/6/17.
 */

public interface GetActivityForResultResponse {
    public void onActivityResult( int requestCode, int resultCode, final Intent data);
}
