package com.codingpixel.healingbudz.onesignal;

import android.content.Intent;
import android.util.Log;

import com.codingpixel.healingbudz.Utilities.eventbus.MessageEvent;
import com.codingpixel.healingbudz.activity.home.HomeActivity;
import com.codingpixel.healingbudz.activity.home.home_fragment.groups_tab.GroupsChatViewActivity;
import com.codingpixel.healingbudz.activity.home.side_menu.messages.MessagingBusinessChatViewActivity;
import com.codingpixel.healingbudz.activity.home.side_menu.messages.MessagingChatViewActivity;
import com.codingpixel.healingbudz.activity.splash.Splash;
import com.codingpixel.healingbudz.DataModel.MessagesDataModel;
import com.codingpixel.healingbudz.application.HealingBudApplication;
import com.codingpixel.healingbudz.network.model.URL;
import com.onesignal.OSNotificationAction;
import com.onesignal.OSNotificationOpenResult;
import com.onesignal.OneSignal;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import static com.codingpixel.healingbudz.activity.splash.Splash.user;
import static com.codingpixel.healingbudz.Utilities.SetUserValuesInSP.getSavedUser;

public class OneSignalNotificationOpenedHandler implements OneSignal.NotificationOpenedHandler {
    // This fires when a notification is opened by tapping on it.
    @Override
    public void notificationOpened(OSNotificationOpenResult result) {
        OSNotificationAction.ActionType actionType = result.action.type;
        JSONObject data = result.notification.payload.additionalData;
        String activityToBeOpened;
        user = getSavedUser(HealingBudApplication.getContext());
        EventBus.getDefault().post(new MessageEvent(false));
        //While sending a Push notification from OneSignal dashboard
        // you can send an addtional data named "activityToBeOpened" and retrieve the value of it and do necessary operation
        //If key is "activityToBeOpened" and value is "AnotherActivity", then when a user clicks
        //on the notification, AnotherActivity will be opened.
        //Else, if we have not set any additional data MainActivity is opened.
        if (data != null) {
            activityToBeOpened = data.optString("activityToBeOpened", null);
            if (activityToBeOpened != null && activityToBeOpened.equals("Groups")) {
                Log.i("OneSignalExample", "customkey set with value: " + activityToBeOpened);
                try {
                    Intent intent = new Intent(HealingBudApplication.getContext(), GroupsChatViewActivity.class);
                    intent.putExtra("goup_id", data.getInt("group_id"));
                    intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
                    HealingBudApplication.getContext().startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else if (activityToBeOpened != null && activityToBeOpened.equals("Chat")) {
                Log.i("OneSignalExample", "customkey set with value: " + activityToBeOpened);
                MessagesDataModel dataModel = new MessagesDataModel();
                try {
                    dataModel.setCreated_at(data.getJSONObject("message").getString("created_at"));
                    dataModel.setSender_id(data.getJSONObject("message").getInt("sender_id"));
                    dataModel.setReceiver_id(data.getJSONObject("message").getInt("receiver_id"));


                    dataModel.setSender_first_name(data.getJSONObject("message").getJSONObject("sender").getString("first_name"));
                    dataModel.setSender_image_path(data.getJSONObject("message").getJSONObject("sender").getString("image_path"));
                    dataModel.setSender_avatar(data.getJSONObject("message").getJSONObject("sender").getString("avatar"));


                    dataModel.setReceiver_first_name(data.getJSONObject("message").getJSONObject("receiver").getString("first_name"));
                    dataModel.setReceiver_image_path(data.getJSONObject("message").getJSONObject("receiver").getString("image_path"));
                    dataModel.setReceiver_avatar(data.getJSONObject("message").getJSONObject("receiver").getString("avatar"));
                    if (data.getJSONObject("message").has("budz_id")) {
                        dataModel.setBudz_id(data.getJSONObject("message").getInt("budz_id"));
                        dataModel.setId(data.getJSONObject("message").getInt("chat_id"));
                        MessagingBusinessChatViewActivity.chat_message_data_modal = dataModel;
                        Splash.image_path = URL.images_baseurl + "";
                        Splash.NameBusiness = result.notification.payload.body.replace(" sent you a private message.", "").replace(" send you a budz message.", "");
                        Splash.otherName = result.notification.payload.body.replace(" sent you a private message.", "").replace(" send you a budz message.", "");
                        Intent intent = new Intent(HealingBudApplication.getContext(), MessagingBusinessChatViewActivity.class);
                        intent.putExtra("budz_id",data.getJSONObject("message").getInt("budz_id"));
                        intent.putExtra("chat_id",data.getJSONObject("message").getInt("chat_id"));
                        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
                        HealingBudApplication.getContext().startActivity(intent);
                    } else {
                        MessagingChatViewActivity.chat_message_data_modal = dataModel;
                        Intent intent = new Intent(HealingBudApplication.getContext(), MessagingChatViewActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
                        HealingBudApplication.getContext().startActivity(intent);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else if (activityToBeOpened != null && activityToBeOpened.equals("group_invitation")) {
                Intent intent = new Intent(HealingBudApplication.getContext(), HomeActivity.class);
                intent.putExtra("activityToBeOpened", "group_invitation");
                intent.putExtra("data", String.valueOf(data));
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
                HealingBudApplication.getContext().startActivity(intent);
            } else if (data != null && activityToBeOpened != null) {
                Intent intent = new Intent(HealingBudApplication.getContext(), HomeActivity.class);
                intent.putExtra("activityToBeOpened", activityToBeOpened);
                intent.putExtra("data", String.valueOf(data));
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                HealingBudApplication.getContext().startActivity(intent);
            } else {
                Intent intent = new Intent(HealingBudApplication.getContext(), Splash.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
                HealingBudApplication.getContext().startActivity(intent);
            }
        }

//        //If we send notification with action buttons we need to specidy the button id's and retrieve it to
//        //do the necessary operation.
//        if (actionType == OSNotificationAction.ActionType.ActionTaken) {
//            Log.i("OneSignalExample", "Button pressed with id: " + result.action.actionID);
//            if (result.action.actionID.equals("ActionOne")) {
//                Toast.makeText(HealingBudApplication.getContext(), "ActionOne Button was pressed", Toast.LENGTH_LONG).show();
//            } else if (result.action.actionID.equals("ActionTwo")) {
//                Toast.makeText(HealingBudApplication.getContext(), "ActionTwo Button was pressed", Toast.LENGTH_LONG).show();
//            }
//        }
    }
}