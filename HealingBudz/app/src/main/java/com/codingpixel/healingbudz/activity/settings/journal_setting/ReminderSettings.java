package com.codingpixel.healingbudz.activity.settings.journal_setting;

import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.android.volley.Request;
import com.codingpixel.healingbudz.R;
import com.codingpixel.healingbudz.Reminder.database.DatabaseHelper;
import com.codingpixel.healingbudz.Reminder.models.Reminder;
import com.codingpixel.healingbudz.Reminder.receivers.AlarmReceiver;
import com.codingpixel.healingbudz.Reminder.receivers.DismissReceiver;
import com.codingpixel.healingbudz.Reminder.utils.AlarmUtil;
import com.codingpixel.healingbudz.Reminder.utils.DateAndTimeUtil;
import com.codingpixel.healingbudz.customeUI.CustomeToast;
import com.codingpixel.healingbudz.data_structure.APIActions;
import com.codingpixel.healingbudz.network.VollyAPICall;
import com.codingpixel.healingbudz.network.model.APIResponseListner;
import com.codingpixel.healingbudz.network.model.URL;
import com.codingpixel.healingbudz.sharedprefrences.SharedPrefrences;

import org.json.JSONException;
import org.json.JSONObject;

import static com.codingpixel.healingbudz.activity.splash.Splash.user;
import static com.codingpixel.healingbudz.application.HealingBudApplication.getContext;
import static com.codingpixel.healingbudz.data_structure.APIActions.ApiActions.get_groups;
import static com.codingpixel.healingbudz.data_structure.APIActions.ApiActions.get_journal_settings;
import static com.codingpixel.healingbudz.static_function.IntentFunction.GoToHome;
import static com.codingpixel.healingbudz.static_function.UIModification.ChangeStatusBarColor;
import static com.codingpixel.healingbudz.static_function.UIModification.HideKeyboard;

public class ReminderSettings extends AppCompatActivity implements APIResponseListner {
    ImageView Back, Home;
    boolean isEdit = false;
    Button btn_off, btn_on;
    Button btn_off_notify, btn_on_notify;
    Button btn_off_mute, btn_on_mute;
    LinearLayout Switch_Layout;
    LinearLayout Switch_Layout_mute_sound;
    LinearLayout Switch_Layout_notify;
    boolean isEntryReminderON = true;
    boolean isQucikEntry_notify = false;
    boolean isQucikEntry_muted_sound = true;
    Button Time_Btn;
    boolean[] day_check = {false, true, true, true, true, false, false};
    TextView day_one, day_two, day_three, day_four, day_five, day_six, day_seven;


    private String icon;
    private String colour;
    private java.util.Calendar calendar;
    private boolean[] daysOfWeek = new boolean[7];
    private int timesShown = 0;
    private int timesToShow = 1;
    private int repeatType;
    private int id;
    private int interval = 1;

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder_settings);
        ChangeStatusBarColor(ReminderSettings.this, "#171717");
        HideKeyboard(ReminderSettings.this);
        DatabaseHelper database = DatabaseHelper.getInstance(this);
        Intent intent = getIntent();
        int mReminderId = intent.getIntExtra("NOTIFICATION_ID", 0);
        if (intent.getBooleanExtra("NOTIFICATION_DISMISS", false)) {
            Intent dismissIntent = new Intent().setClass(this, DismissReceiver.class);
            dismissIntent.putExtra("NOTIFICATION_ID", mReminderId);
            sendBroadcast(dismissIntent);
        }
        calendar = java.util.Calendar.getInstance();
        icon = getString(R.string.default_icon_value);
        colour = getString(R.string.default_colour_value);
        repeatType = Reminder.SPECIFIC_DAYS;
        id = Integer.parseInt(calendar.get(Calendar.MINUTE)+calendar.get(Calendar.HOUR)+calendar.get(Calendar.DAY_OF_MONTH)+"");
        Back = (ImageView) findViewById(R.id.back_btn);
        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateData();
                finish();
            }
        });
        Home = (ImageView) findViewById(R.id.home_btn);
        Home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateData();
                GoToHome(ReminderSettings.this, true);
                finish();
            }
        });

        btn_off = (Button) findViewById(R.id.private_btn);
        btn_on = (Button) findViewById(R.id.public_btn);
        Switch_Layout = (LinearLayout) findViewById(R.id.switch_layout);
        Switch_Layout.setClickable(true);
        Switch_Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isEdit = true;
                if (isEntryReminderON) {
                    SharedPrefrences.setBool("journal_remindeer_entry", false, ReminderSettings.this);
                    btn_on.setText("");
                    btn_off.setText("OFF");
                    btn_off.setBackgroundResource(R.drawable.seeting_toggle_btn_off);
                    btn_on.setBackground(null);
                    isEntryReminderON = false;
                } else {
                    SharedPrefrences.setBool("journal_remindeer_entry", true, ReminderSettings.this);

                    btn_on.setText("ON");
                    btn_off.setText("");
                    btn_on.setBackgroundResource(R.drawable.toggle_btn_public);
                    btn_off.setBackground(null);
                    isEntryReminderON = true;
                }
            }
        });


        btn_off_notify = (Button) findViewById(R.id.btn_off_notify);
        btn_on_notify = (Button) findViewById(R.id.btn_onn_notify);
        Switch_Layout_notify = (LinearLayout) findViewById(R.id.switch_layout_notify);
        Switch_Layout_notify.setClickable(true);
        Switch_Layout_notify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isEdit = true;
                if (isQucikEntry_notify) {
                    SharedPrefrences.setBool("journal_quick_entry_notify", false, ReminderSettings.this);

                    btn_on_notify.setText("");
                    btn_off_notify.setText("OFF");
                    btn_off_notify.setBackgroundResource(R.drawable.seeting_toggle_btn_off);
                    btn_on_notify.setBackground(null);
                    isQucikEntry_notify = false;
                } else {
                    SharedPrefrences.setBool("journal_quick_entry_notify", true, ReminderSettings.this);

                    btn_on_notify.setText("ON");
                    btn_off_notify.setText("");
                    btn_on_notify.setBackgroundResource(R.drawable.toggle_btn_public);
                    btn_off_notify.setBackground(null);
                    isQucikEntry_notify = true;
                }
            }
        });


        btn_off_mute = (Button) findViewById(R.id.off_btn__mute_sound);
        btn_on_mute = (Button) findViewById(R.id.onn_btn__mute_sound);
        Switch_Layout_mute_sound = (LinearLayout) findViewById(R.id.switch_layout_mute_sound);
        Switch_Layout_mute_sound.setClickable(true);
        Switch_Layout_mute_sound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isEdit = true;
                if (isQucikEntry_muted_sound) {
                    SharedPrefrences.setBool("journal_mute_sound", false, ReminderSettings.this);

                    btn_on_mute.setText("");
                    btn_off_mute.setText("OFF");
                    btn_off_mute.setBackgroundResource(R.drawable.seeting_toggle_btn_off);
                    btn_on_mute.setBackground(null);
                    isQucikEntry_muted_sound = false;
                } else {
                    SharedPrefrences.setBool("journal_mute_sound", true, ReminderSettings.this);
                    btn_on_mute.setText("ON");
                    btn_off_mute.setText("");
                    btn_on_mute.setBackgroundResource(R.drawable.toggle_btn_public);
                    btn_off_mute.setBackground(null);
                    isQucikEntry_muted_sound = true;
                }
            }
        });


        day_one = (TextView) findViewById(R.id.day_one);
        day_two = (TextView) findViewById(R.id.day_two);
        day_three = (TextView) findViewById(R.id.day_three);
        day_four = (TextView) findViewById(R.id.day_four);
        day_five = (TextView) findViewById(R.id.day_five);
        day_six = (TextView) findViewById(R.id.day_six);
        day_seven = (TextView) findViewById(R.id.day_seven);


        day_one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isEdit = true;
                if (day_check[0]) {
                    day_one.setTextColor(Color.parseColor("#FFFFFF"));
                    day_check[0] = false;
                } else {
                    day_one.setTextColor(Color.parseColor("#7abd45"));
                    day_check[0] = true;
                }

                SharedPrefrences.setBool("journal_reminder_day_sun", day_check[0], ReminderSettings.this);
            }
        });


        day_two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isEdit = true;
                if (day_check[1]) {
                    day_two.setTextColor(Color.parseColor("#FFFFFF"));
                    day_check[1] = false;
                } else {
                    day_two.setTextColor(Color.parseColor("#7abd45"));
                    day_check[1] = true;
                }

                SharedPrefrences.setBool("journal_reminder_day_mon", day_check[1], ReminderSettings.this);

            }
        });

        day_three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isEdit = true;
                if (day_check[2]) {
                    day_three.setTextColor(Color.parseColor("#FFFFFF"));
                    day_check[2] = false;
                } else {
                    day_three.setTextColor(Color.parseColor("#7abd45"));
                    day_check[2] = true;
                }

                SharedPrefrences.setBool("journal_reminder_day_tue", day_check[2], ReminderSettings.this);

            }
        });

        day_four.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isEdit = true;
                if (day_check[3]) {
                    day_four.setTextColor(Color.parseColor("#FFFFFF"));
                    day_check[3] = false;
                } else {
                    day_four.setTextColor(Color.parseColor("#7abd45"));
                    day_check[3] = true;
                }

                SharedPrefrences.setBool("journal_reminder_day_wed", day_check[3], ReminderSettings.this);

            }
        });

        day_five.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isEdit = true;
                if (day_check[4]) {
                    day_five.setTextColor(Color.parseColor("#FFFFFF"));
                    day_check[4] = false;
                } else {
                    day_five.setTextColor(Color.parseColor("#7abd45"));
                    day_check[4] = true;
                }

                SharedPrefrences.setBool("journal_reminder_day_thu", day_check[4], ReminderSettings.this);

            }
        });

        day_six.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isEdit = true;
                if (day_check[5]) {
                    day_six.setTextColor(Color.parseColor("#FFFFFF"));
                    day_check[5] = false;
                } else {
                    day_six.setTextColor(Color.parseColor("#7abd45"));
                    day_check[5] = true;
                }
                SharedPrefrences.setBool("journal_reminder_day_fri", day_check[5], ReminderSettings.this);

            }
        });

        day_seven.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isEdit = true;
                if (day_check[6]) {
                    day_seven.setTextColor(Color.parseColor("#FFFFFF"));
                    day_check[6] = false;
                } else {
                    day_seven.setTextColor(Color.parseColor("#7abd45"));
                    day_check[6] = true;
                }
                SharedPrefrences.setBool("journal_reminder_day_sat", day_check[6], ReminderSettings.this);

            }
        });

        Time_Btn = (Button) findViewById(R.id.time_btn);
        Time_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isEdit = true;
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(ReminderSettings.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                        calendar.set(java.util.Calendar.HOUR_OF_DAY, selectedHour);
                        calendar.set(java.util.Calendar.MINUTE, selectedMinute);

                        if (selectedHour >= 12) {
                            int cl_hours = selectedHour % 12;
                            if (cl_hours == 0) {
                                cl_hours = 12;
                            }
                            Time_Btn.setText(cl_hours + ":" + selectedMinute + " PM");
                        } else {
                            int cl_hours = selectedHour;
                            if (cl_hours == 0) {
                                cl_hours = 12;
                            }
                            Time_Btn.setText(cl_hours + ":" + selectedMinute + " AM");
                        }

                        SharedPrefrences.setString("journal_Setting_Time", Time_Btn.getText().toString(), ReminderSettings.this);
                    }
                }, hour, minute, false);
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });

        JSONObject jsonObject = new JSONObject();
        new VollyAPICall(ReminderSettings.this, false, URL.get_journal_settings, jsonObject, user.getSession_key(), Request.Method.GET, ReminderSettings.this, get_journal_settings);
        SetData();
    }

    public String getDayText(int day_number) {
        switch (day_number) {
            case 0:
                return "Sun";
            case 1:
                return "Mon";

            case 2:
                return "Tue";
            case 3:
                return "Wed";
            case 4:
                return "Thu";
            case 5:
                return "Fri";
            case 6:
                return "Sat";
            default:
                return "Sun";

        }
    }

    @Override
    public void onRequestSuccess(String response, APIActions.ApiActions apiActions) {
        Log.d("Response", response);

        if (apiActions == get_journal_settings) {

            try {
                JSONObject jsonObject = new JSONObject(response).getJSONObject("successData").getJSONObject("reminder_setting");
                Log.d("Response", jsonObject.toString());
                setApidata(jsonObject);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (apiActions == get_groups) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                Log.d("Response", jsonObject.toString());

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onRequestError(String response, APIActions.ApiActions apiActions) {
        Log.d("Response", response);
        try {
            JSONObject object = new JSONObject(response);
            CustomeToast.ShowCustomToast(getContext(), object.getString("errorMessage"), Gravity.TOP);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (apiActions == get_journal_settings) {

            Log.d("Response", response);
        } else if (apiActions == get_groups) {
            Log.d("Response", response);
        }
    }

    public void updateData() {
        if (isEdit) {
            try {
                JSONObject jsonObject = new JSONObject();
                if (isEntryReminderON) {
                    jsonObject.put("is_on", 1);
                } else {
                    jsonObject.put("is_on", 0);
                }

                if (isQucikEntry_muted_sound) {
                    jsonObject.put("is_mute", 1);
                } else {
                    jsonObject.put("is_mute", 0);
                }


                if (isQucikEntry_notify) {
                    jsonObject.put("notify_if_created", 1);
                } else {
                    jsonObject.put("notify_if_created", 0);
                }

                String days = "";
                for (int x = 0; x < day_check.length; x++) {
                    if (day_check[x]) {
                        if (days.length() == 0) {
                            days = days + getDayText(x);
                        } else {
                            days = days + "," + getDayText(x);
                        }
                    }
                }
                jsonObject.put("days", days);
                jsonObject.put("time", Time_Btn.getText().toString());
                new VollyAPICall(ReminderSettings.this, false, URL.add_journal_reminder_setting, jsonObject, user.getSession_key(), Request.Method.POST, ReminderSettings.this, get_groups);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        SetReminder();
    }

    public void setApidata(JSONObject jsonObject) throws JSONException {
        if (!jsonObject.isNull("is_on"))
            if (jsonObject.getInt("is_on") == 1) {
                SharedPrefrences.setBool("journal_remindeer_entry", true, ReminderSettings.this);
            } else {
                SharedPrefrences.setBool("journal_remindeer_entry", false, ReminderSettings.this);
            }
        if (!jsonObject.isNull("notify_if_created"))
            if (jsonObject.getInt("notify_if_created") == 1) {
                SharedPrefrences.setBool("journal_quick_entry_notify", true, ReminderSettings.this);
            } else {
                SharedPrefrences.setBool("journal_quick_entry_notify", false, ReminderSettings.this);
            }
        if (!jsonObject.isNull("is_mute"))
            if (jsonObject.getInt("is_mute") == 1) {
                SharedPrefrences.setBool("journal_mute_sound", true, ReminderSettings.this);
            } else {
                SharedPrefrences.setBool("journal_mute_sound", false, ReminderSettings.this);
            }
        if (!jsonObject.isNull("time"))
            if (!jsonObject.isNull("time") && jsonObject.getString("time").length() > 0) {
                SharedPrefrences.setString("journal_Setting_Time", jsonObject.getString("time"), ReminderSettings.this);
            }
        if (!jsonObject.isNull("date")){
            if(jsonObject.getString("date").contains("Mon")){
                SharedPrefrences.setBool("journal_reminder_day_mon", true, ReminderSettings.this);
            }else{
                SharedPrefrences.setBool("journal_reminder_day_mon", false, ReminderSettings.this);
            }
            if(jsonObject.getString("date").contains("Tue")){
                SharedPrefrences.setBool("journal_reminder_day_tue", true, ReminderSettings.this);
            }else{
                SharedPrefrences.setBool("journal_reminder_day_tue", false, ReminderSettings.this);
            }
            if(jsonObject.getString("date").contains("Wed")){
                SharedPrefrences.setBool("journal_reminder_day_wed", true, ReminderSettings.this);
            }else{
                SharedPrefrences.setBool("journal_reminder_day_wed", false, ReminderSettings.this);
            }
            if(jsonObject.getString("date").contains("Thu")){
                SharedPrefrences.setBool("journal_reminder_day_thu", true, ReminderSettings.this);
            }else{
                SharedPrefrences.setBool("journal_reminder_day_thu", false, ReminderSettings.this);
            }
            if(jsonObject.getString("date").contains("Fri")){
                SharedPrefrences.setBool("journal_reminder_day_thu", true, ReminderSettings.this);
            }else{
                SharedPrefrences.setBool("journal_reminder_day_thu", false, ReminderSettings.this);
            }
            if(jsonObject.getString("date").contains("Sat")){
                SharedPrefrences.setBool("journal_reminder_day_sat", true, ReminderSettings.this);
            }else{
                SharedPrefrences.setBool("journal_reminder_day_thu", false, ReminderSettings.this);
            }
            if(jsonObject.getString("date").contains("Sun")){
                SharedPrefrences.setBool("journal_reminder_day_sun", true, ReminderSettings.this);
            }else{
                SharedPrefrences.setBool("journal_reminder_day_sun", false, ReminderSettings.this);
            }
        }
        SetData();
    }

    public void SetData() {
        if (!SharedPrefrences.getBool("journal_remindeer_entry", ReminderSettings.this)) {
            btn_on.setText("");
            btn_off.setText("OFF");
            btn_off.setBackgroundResource(R.drawable.seeting_toggle_btn_off);
            btn_on.setBackground(null);
            isEntryReminderON = false;
        } else {
            btn_on.setText("ON");
            btn_off.setText("");
            btn_on.setBackgroundResource(R.drawable.toggle_btn_public);
            btn_off.setBackground(null);
            isEntryReminderON = true;
        }


        if (!SharedPrefrences.getBool("journal_quick_entry_notify", ReminderSettings.this)) {
            btn_on_notify.setText("");
            btn_off_notify.setText("OFF");
            btn_off_notify.setBackgroundResource(R.drawable.seeting_toggle_btn_off);
            btn_on_notify.setBackground(null);
            isQucikEntry_notify = false;
        } else {
            btn_on_notify.setText("ON");
            btn_off_notify.setText("");
            btn_on_notify.setBackgroundResource(R.drawable.toggle_btn_public);
            btn_off_notify.setBackground(null);
            isQucikEntry_notify = true;
        }

        if (!SharedPrefrences.getBool("journal_mute_sound", ReminderSettings.this)) {
            btn_on_mute.setText("");
            btn_off_mute.setText("OFF");
            btn_off_mute.setBackgroundResource(R.drawable.seeting_toggle_btn_off);
            btn_on_mute.setBackground(null);
            isQucikEntry_muted_sound = false;
        } else {
            btn_on_mute.setText("ON");
            btn_off_mute.setText("");
            btn_on_mute.setBackgroundResource(R.drawable.toggle_btn_public);
            btn_off_mute.setBackground(null);
            isQucikEntry_muted_sound = true;
        }

        if (SharedPrefrences.getString("journal_Setting_Time", ReminderSettings.this).length() != 0) {
            Time_Btn.setText(SharedPrefrences.getString("journal_Setting_Time", ReminderSettings.this));
        }


        if (SharedPrefrences.getBool("journal_reminder_day_sun", ReminderSettings.this)) {
            day_one.setTextColor(Color.parseColor("#7abd45"));
            day_check[0] = true;
        } else {
            day_check[0] = false;
            day_one.setTextColor(Color.parseColor("#FFFFFF"));
        }
        if (SharedPrefrences.getBool("journal_reminder_day_mon", ReminderSettings.this)) {
            day_two.setTextColor(Color.parseColor("#7abd45"));
            day_check[1] = true;
        } else {
            day_check[1] = false;
            day_two.setTextColor(Color.parseColor("#FFFFFF"));
        }
        if (SharedPrefrences.getBool("journal_reminder_day_tue", ReminderSettings.this)) {
            day_three.setTextColor(Color.parseColor("#7abd45"));
            day_check[2] = true;
        } else {
            day_check[2] = false;
            day_three.setTextColor(Color.parseColor("#FFFFFF"));
        }
        if (SharedPrefrences.getBool("journal_reminder_day_wed", ReminderSettings.this)) {
            day_four.setTextColor(Color.parseColor("#7abd45"));
            day_check[3] = true;
        } else {
            day_check[3] = false;
            day_four.setTextColor(Color.parseColor("#FFFFFF"));
        }
        if (SharedPrefrences.getBool("journal_reminder_day_thu", ReminderSettings.this)) {
            day_five.setTextColor(Color.parseColor("#7abd45"));
            day_check[4] = true;
        } else {
            day_check[4] = false;
            day_five.setTextColor(Color.parseColor("#FFFFFF"));
        }
        if (SharedPrefrences.getBool("journal_reminder_day_fri", ReminderSettings.this)) {
            day_six.setTextColor(Color.parseColor("#7abd45"));
            day_check[5] = true;
        } else {
            day_check[5] = false;
            day_six.setTextColor(Color.parseColor("#FFFFFF"));
        }
        if (SharedPrefrences.getBool("journal_reminder_day_sat", ReminderSettings.this)) {
            day_seven.setTextColor(Color.parseColor("#7abd45"));
            day_check[6] = true;
        } else {
            day_check[6] = false;
            day_seven.setTextColor(Color.parseColor("#FFFFFF"));
        }
    }

    public  void SetReminder(){
        DatabaseHelper database = DatabaseHelper.getInstance(this);
        Reminder reminder = new Reminder()
                .setId(id)
                .setTitle("Entry Reminders")
                .setContent("Its time to write something in your Journals")
                .setDateAndTime(DateAndTimeUtil.toStringDateAndTime(calendar))
                .setRepeatType(repeatType)
                .setForeverState(Boolean.toString(true))
                .setNumberToShow(timesToShow)
                .setNumberShown(timesShown)
                .setIcon(icon)
                .setColour(colour)
                .setInterval(interval);

        database.addNotification(reminder);
        if (repeatType == Reminder.SPECIFIC_DAYS) {
            reminder.setDaysOfWeek(day_check);
            database.addDaysOfWeek(reminder);
        }
        database.close();
        Intent alarmIntent = new Intent(this, AlarmReceiver.class);
        calendar.set(java.util.Calendar.SECOND, 0);
        AlarmUtil.setAlarm(this, alarmIntent, reminder.getId(), calendar);
    }
}
