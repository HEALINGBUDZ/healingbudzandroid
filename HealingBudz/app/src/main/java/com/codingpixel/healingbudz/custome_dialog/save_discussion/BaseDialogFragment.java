package com.codingpixel.healingbudz.custome_dialog.save_discussion;


import android.app.Activity;
import android.support.v4.app.DialogFragment;

public abstract class BaseDialogFragment<T> extends DialogFragment {
    private T mActivityInstance;

    public final T getActivityInstance() {
        return mActivityInstance;
    }

//    @Override
//    public void onAttach(Context context) {
//        mActivityInstance = (T) context;
//        super.onAttach(context);
//    }

    @Override
    public void onAttach(Activity activity) {
        mActivityInstance = (T) activity;
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mActivityInstance = null;
    }
}