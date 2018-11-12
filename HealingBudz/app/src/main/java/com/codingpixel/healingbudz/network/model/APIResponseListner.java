package com.codingpixel.healingbudz.network.model;

import com.codingpixel.healingbudz.data_structure.APIActions;

public interface APIResponseListner {
    public void onRequestSuccess(String response, APIActions.ApiActions apiActions);
    public void onRequestError(String response, APIActions.ApiActions apiActions);
}
