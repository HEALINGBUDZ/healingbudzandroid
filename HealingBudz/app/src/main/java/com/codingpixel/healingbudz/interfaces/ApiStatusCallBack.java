package com.codingpixel.healingbudz.interfaces;
/*
 * Created by M_Muzammil Sharif on 07-Mar-18.
 */

public interface ApiStatusCallBack {

    public void success();

    public void sessionExpire();

    public void knownError(String errorMsg);

    public void unKnownError();
}
