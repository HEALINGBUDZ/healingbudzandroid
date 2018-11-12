package com.codingpixel.healingbudz.Utilities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.codingpixel.healingbudz.network.model.URL;

/**
 * Created by jawadali on 12/20/17.
 */

public class CallUser {

    public static void callUserPhone(Context context, String phone) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phone));
        context.startActivity(intent);
    }

    public static void sendSMS(String phoneNo, String msg, Context context) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + phoneNo));
        intent.putExtra("sms_body", "Check out Healing Budz for your smartphone. Download it today from " + URL.sharedBaseUrl);
        context.startActivity(intent);
//        try {
//            SmsManager smsManager = SmsManager.getDefault();
//            smsManager.sendTextMessage(phoneNo, null, msg, null, null);
//
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
    }
}
