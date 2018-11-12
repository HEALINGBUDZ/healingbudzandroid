package com.codingpixel.healingbudz.activity.home.home_fragment.journal_tab;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.codingpixel.healingbudz.activity.home.home_fragment.journal_tab.dialog.FeelingJournalAlertDialog;
import com.codingpixel.healingbudz.DataModel.JournalTagsDataModal;
import com.codingpixel.healingbudz.R;
import com.codingpixel.healingbudz.adapter.AddNewJournalKeywordRecylerAdapter;
import com.codingpixel.healingbudz.adapter.AddNewJournalSelectTagRecylerAdapter;
import com.codingpixel.healingbudz.camera_activity.HBCameraActivity;
import com.codingpixel.healingbudz.customeUI.CustomeToast;
import com.codingpixel.healingbudz.customeUI.ProgressDialogVideoProcessing;
import com.codingpixel.healingbudz.data_structure.APIActions;
import com.codingpixel.healingbudz.network.VollyAPICall;
import com.codingpixel.healingbudz.network.model.APIResponseListner;
import com.codingpixel.healingbudz.network.model.URL;
import com.codingpixel.healingbudz.network.upload_image.UploadImageAPIcall;
import com.codingpixel.healingbudz.network.upload_image.UploadVideoAPIcall;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.codingpixel.healingbudz.activity.home.home_fragment.journal_tab.JournalDetailsActivity.journalFragmentDataModel;
import static com.codingpixel.healingbudz.activity.home.side_menu.messages.MessagingChatViewActivity.getVideoThumbnail;
import static com.codingpixel.healingbudz.activity.splash.Splash.user;
import static com.codingpixel.healingbudz.Utilities.DateConverter.getDateAsApiFormate;
import static com.codingpixel.healingbudz.Utilities.ViewUtils.checkRotation;
import static com.codingpixel.healingbudz.Utilities.ViewUtils.checkRotationVideo;
import static com.codingpixel.healingbudz.customeUI.ImageHelper.getRoundedCornerBitmap;
import static com.codingpixel.healingbudz.network.model.URL.add_event_image;
import static com.codingpixel.healingbudz.network.model.URL.add_event_video;
import static com.codingpixel.healingbudz.static_function.IntentFunction.GoToHome;
import static com.codingpixel.healingbudz.static_function.UIModification.ChangeStatusBarColor;
import static com.codingpixel.healingbudz.static_function.UIModification.HideKeyboard;

//import static com.codingpixel.healingbudz.application.HealingBudApplication.getContext;

public class AddNewJournalEvent extends AppCompatActivity implements FeelingJournalAlertDialog.OnDialogFragmentClickListener, View.OnClickListener, APIResponseListner, AddNewJournalSelectTagRecylerAdapter.ItemClickListener, AddNewJournalKeywordRecylerAdapter.ItemClickListener {
    RecyclerView Strains_RecylerView, Select_Tag_recylerview;
    AddNewJournalKeywordRecylerAdapter tags_strain_keyword_recyler_adapter;
    private static final List<String> StrainKeywords = new ArrayList<String>();
    private static final List<String> data = new ArrayList<String>();
    AutoCompleteTextView Search_Strains;
    ImageView Edit_DatePicker;
    LinearLayout Feeling_dialog;
    LinearLayout Upload_Media;
    EditText Entry_Title;
    Button Save_Publish;
    int tag_strain_id = -1;
    EditText Description;
    ArrayList<StrainDetail> strainDetails = new ArrayList<>();
    boolean isUploadinginprogress = false;
    ProgressDialogVideoProcessing video_processing;
    ImageView Back, Home;
    TextView Date_Text, Day_text, Month_Year_text;
    RelativeLayout Journal_event_image;
    LinearLayout Lodaing_Dots;
    boolean[] AttachmentAdded = new boolean[3];
    int attachment_count = 0;

    String image_one_url = "";
    String image_two_url = "";
    String image_three_url = "";
    TextView Feeling_text;
    ImageView Feeling_Icon;

    String video_one_url = "";
    String video_two_url = "";
    String video_three_url = "";
    String tag_selcetd_id = "";
    AddNewJournalSelectTagRecylerAdapter addNewJournalSelectTagRecylerAdapter;
    List<JournalTagsDataModal> tag_data = new ArrayList<>();
    Drawable[] drawables = new Drawable[3];
    ImageView attachment_one_img, attachment_two_img, attachment_three_img;
    ImageView attachment_one_video, attachment_two_video, attachment_three_video;
    ImageView attachment_one_cross, attachment_two_cross, attachment_three_cross, journel_image;
    RelativeLayout attachment_one, attachment_two, attachment_three;
    TextView Character_counter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_journal_event);
        ChangeStatusBarColor(AddNewJournalEvent.this, "#0a0a0a");
        HideKeyboard(AddNewJournalEvent.this);
        Search_Strains = (AutoCompleteTextView) findViewById(R.id.search_Strain);

        attachment_count = 0;
        AttachmentAdded[0] = false;
        AttachmentAdded[1] = false;
        AttachmentAdded[2] = false;

        drawables[0] = null;
        drawables[1] = null;
        drawables[2] = null;
        Init();
        tags_strain_keyword_recyler_adapter = new AddNewJournalKeywordRecylerAdapter(AddNewJournalEvent.this, data, true);
        Strains_RecylerView = (RecyclerView) findViewById(R.id.strain_recyler_view);
        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(AddNewJournalEvent.this);
        layoutManager.setFlexDirection(FlexDirection.ROW);
        layoutManager.setJustifyContent(JustifyContent.FLEX_START);
        Strains_RecylerView.setLayoutManager(layoutManager);
        Strains_RecylerView.setAdapter(tags_strain_keyword_recyler_adapter);
        tags_strain_keyword_recyler_adapter.setClickListener(this);

        addNewJournalSelectTagRecylerAdapter = new AddNewJournalSelectTagRecylerAdapter(AddNewJournalEvent.this, tag_data);
        Select_Tag_recylerview = (RecyclerView) findViewById(R.id.tags_recyler_view);
        FlexboxLayoutManager layoutManager_1 = new FlexboxLayoutManager(AddNewJournalEvent.this);
        layoutManager_1.setFlexDirection(FlexDirection.ROW);
        layoutManager_1.setJustifyContent(JustifyContent.FLEX_START);
        Select_Tag_recylerview.setLayoutManager(layoutManager_1);
        Select_Tag_recylerview.setAdapter(addNewJournalSelectTagRecylerAdapter);
        addNewJournalSelectTagRecylerAdapter.setClickListener(this);

        Search_Strains.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Search_Strains.setText("");
                data.add(adapterView.getItemAtPosition(i).toString());
                for (int x = 0; x < strainDetails.size(); x++) {
                    if (adapterView.getItemAtPosition(i).toString().equalsIgnoreCase(strainDetails.get(x).getTitle())) {
                        tag_strain_id = strainDetails.get(x).getId();
                        break;
                    }
                }
                tags_strain_keyword_recyler_adapter.notifyDataSetChanged();
                Search_Strains.setVisibility(View.GONE);
            }
        });

        Calendar myCalendar = Calendar.getInstance();
        SimpleDateFormat parseFormat = new SimpleDateFormat("EEEE/MMMM");
        Date date = new Date(myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH) - 1);
        String s = parseFormat.format(date);
        String[] st = s.split("/");
        Date_Text = (TextView) findViewById(R.id.date_txt);
        Day_text = (TextView) findViewById(R.id.day_text);
        Month_Year_text = (TextView) findViewById(R.id.mnth_year_text);
        Character_counter = (TextView) findViewById(R.id.character_counter);
        Date_Text.setText(myCalendar.get(Calendar.DAY_OF_MONTH) + "");
        Day_text.setText(st[0] + "");
        Month_Year_text.setText(st[1] + ", " + myCalendar.get(Calendar.YEAR));


        Edit_DatePicker = (ImageView) findViewById(R.id.date_picker_edit);
        Edit_DatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar myCalendar = Calendar.getInstance();
                new DatePickerDialog(AddNewJournalEvent.this, mDateSetListener, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        Feeling_dialog = (LinearLayout) findViewById(R.id.feeling_dialog);
        Feeling_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FeelingJournalAlertDialog feelingJournalAlertDialog = FeelingJournalAlertDialog.newInstance(AddNewJournalEvent.this);
                feelingJournalAlertDialog.show(getSupportFragmentManager(), "dialog");
            }
        });

        Upload_Media = (LinearLayout) findViewById(R.id.upload_img_video);
        Upload_Media.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (attachment_count < 3 && !isUploadinginprogress) {
                    Intent intent = new Intent(AddNewJournalEvent.this, HBCameraActivity.class);
                    intent.putExtra("isVideoCaptureAble", true);
                    startActivityForResult(intent, 1200);
                } else {
                    Animation startAnimation = AnimationUtils.loadAnimation(AddNewJournalEvent.this, R.anim.shaking);
                    attachment_one.startAnimation(startAnimation);
                    attachment_two.startAnimation(startAnimation);
                    attachment_three.startAnimation(startAnimation);
                }
            }
        });

        Back = (ImageView) findViewById(R.id.back_btn);
        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        Home = (ImageView) findViewById(R.id.home_btn);
        Home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoToHome(AddNewJournalEvent.this, true);
                finish();
            }
        });

        Entry_Title = (EditText) findViewById(R.id.entry_title);
        Entry_Title.clearFocus();
        Entry_Title.setFocusableInTouchMode(true);

        new Handler().postDelayed(new Runnable() {
            public void run() {
                if (getCurrentFocus() != null) {
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                }
            }
        }, 200);


        Description.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                int char_count = charSequence.length();
                int words = Description.getText().toString().split(" ").length;
                if (Description.getText().length() == 0) {
                    words = 0;
                }
                Character_counter.setText("Char:" + char_count + "\t\t\t\tWords:" + words);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        JSONObject jsonObject = new JSONObject();
        new VollyAPICall(AddNewJournalEvent.this, true, URL.get_tags, jsonObject, user.getSession_key(), Request.Method.GET, AddNewJournalEvent.this, APIActions.ApiActions.get_tags);
    }

    DatePickerDialog.OnDateSetListener mDateSetListener =
            new DatePickerDialog.OnDateSetListener() {
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    SimpleDateFormat parseFormat = new SimpleDateFormat("EEEE/MMMM");
                    Date date = new Date(year, monthOfYear, dayOfMonth - 1);
                    String s = parseFormat.format(date);
                    String[] st = s.split("/");
                    Date_Text.setText(dayOfMonth + "");
                    Day_text.setText(st[0] + "");
                    Month_Year_text.setText(st[1] + ", " + year);
                    Log.d("year", year + " , " + monthOfYear + ", " + dayOfMonth);
                }
            };


    @Override
    public void onCrossBtnClink(FeelingJournalAlertDialog dialog) {
        dialog.dismiss();
    }

    @Override
    public void onPickEmojiClick(FeelingJournalAlertDialog dialog, Drawable image_resoure, String feeling_Text) {
        Feeling_Icon.setImageDrawable(image_resoure);
        Feeling_text.setText(feeling_Text);
//        MakeKeywordClickableText(Feeling_text.AddNewJournalEvent.this, feeling_Text, Feeling_text);
        dialog.dismiss();
    }

    public void InitAttachments() {
        attachment_one = (RelativeLayout) findViewById(R.id.attachment_one);
        attachment_two = (RelativeLayout) findViewById(R.id.attachment_two);
        attachment_three = (RelativeLayout) findViewById(R.id.attachment_three);

        attachment_one_img = (ImageView) findViewById(R.id.attachment_one_image);
        attachment_two_img = (ImageView) findViewById(R.id.attachment_two_image);
        attachment_three_img = (ImageView) findViewById(R.id.attachment_three_image);

        attachment_one_video = (ImageView) findViewById(R.id.attachment_one_video);
        attachment_two_video = (ImageView) findViewById(R.id.attachment_two_video);
        attachment_three_video = (ImageView) findViewById(R.id.attachment_three_video);

        attachment_one_cross = (ImageView) findViewById(R.id.attachment_one_cross);
        attachment_one_cross.setOnClickListener(this);
        attachment_two_cross = (ImageView) findViewById(R.id.attachment_two_cross);
        attachment_two_cross.setOnClickListener(this);
        attachment_three_cross = (ImageView) findViewById(R.id.attachment_three_cross);
        attachment_three_cross.setOnClickListener(this);
    }

    public void Init() {
        journel_image = (ImageView) findViewById(R.id.journel_image);
        Lodaing_Dots = (LinearLayout) findViewById(R.id.dots_layout);
        Journal_event_image = (RelativeLayout) findViewById(R.id.event_image);
        Save_Publish = (Button) findViewById(R.id.save_publish);
        Feeling_text = (TextView) findViewById(R.id.feeling_text);
        Feeling_Icon = (ImageView) findViewById(R.id.feeling_iconn);
        Description = (EditText) findViewById(R.id.event_detail_textfiled);
        Save_Publish.setOnClickListener(this);
        InitAttachments();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.attachment_one_cross:
                if (!isUploadinginprogress) {
                    image_one_url = "";
                    video_one_url = "";
                    attachment_one.setVisibility(View.GONE);
                    AttachmentAdded[0] = false;
                    drawables[0] = null;
                    attachment_count--;
                    if (attachment_count == 0) {
                        Journal_event_image.setBackgroundColor(Color.parseColor("#7cc144"));
                        journel_image.setImageDrawable(null);
                    } else {
                        if (drawables[1] != null) {
                            journel_image.setImageDrawable(drawables[1]);
                        } else if (drawables[2] != null) {
                            journel_image.setImageDrawable(drawables[2]);
                        }
                    }
                }
                break;
            case R.id.attachment_two_cross:
                if (!isUploadinginprogress) {
                    image_two_url = "";
                    video_two_url = "";
                    attachment_two.setVisibility(View.GONE);
                    AttachmentAdded[1] = false;
                    drawables[1] = null;
                    attachment_count--;
                    if (attachment_count == 0) {
                        Journal_event_image.setBackgroundColor(Color.parseColor("#7cc144"));
                        journel_image.setImageDrawable(null);
                    } else {
                        if (drawables[0] != null) {
                            journel_image.setImageDrawable(drawables[0]);
                        } else if (drawables[2] != null) {
                            journel_image.setImageDrawable(drawables[2]);
                        }
                    }
                }
                break;
            case R.id.attachment_three_cross:
                if (!isUploadinginprogress) {
                    image_three_url = "";
                    video_three_url = "";
                    attachment_three.setVisibility(View.GONE);
                    AttachmentAdded[2] = false;
                    drawables[2] = null;
                    attachment_count--;
                    if (attachment_count == 0) {
                        Journal_event_image.setBackgroundColor(Color.parseColor("#7cc144"));
                        journel_image.setImageDrawable(null);
                    } else {
                        if (drawables[0] != null) {
                            journel_image.setImageDrawable(drawables[0]);
                        } else if (drawables[1] != null) {
                            journel_image.setImageDrawable(drawables[1]);
                        }
                    }
                }
                break;
            case R.id.save_publish:

                String images = "";
                if (image_one_url.length() > 0) {
                    if (images.length() > 0) {
                        images = images + "," + image_one_url;
                    } else {
                        images = images + image_one_url;
                    }

                }
                if (image_two_url.length() > 0) {
                    if (images.length() > 0) {
                        images = images + "," + image_two_url;
                    } else {
                        images = images + image_two_url;
                    }
                }

                if (image_three_url.length() > 0) {
                    if (images.length() > 0) {
                        images = images + "," + image_three_url;
                    } else {
                        images = images + image_three_url;
                    }
                }
                Log.d("images", images);


                String videos = "";
                if (video_one_url.length() > 0) {
                    if (videos.length() > 0) {
                        videos = videos + "," + video_one_url;
                    } else {
                        videos = videos + video_one_url;
                    }

                }
                if (video_two_url.length() > 0) {
                    if (videos.length() > 0) {
                        videos = videos + "," + video_two_url;
                    } else {
                        videos = videos + video_two_url;
                    }
                }

                if (video_three_url.length() > 0) {
                    if (videos.length() > 0) {
                        videos = videos + "," + video_three_url;
                    } else {
                        videos = videos + video_three_url;
                    }
                }
                Log.d("videos", videos);


                StringBuilder Ttags_id = new StringBuilder();
                for (int x = 0; x < tag_data.size(); x++) {
                    if (tag_data.get(x).isTagClicked()) {
                        if (Ttags_id.length() == 0) {
                            Ttags_id.append(tag_data.get(x).getId());
                        } else {
                            Ttags_id.append(",").append(tag_data.get(x).getId());
                        }
                    }
                }

                Log.d("tags", Ttags_id.toString());

                Calendar myCalendar = Calendar.getInstance();
//                Log.d("genrate_date", getDateAsApiFormate(Day_text.getText().toString() + " " + Date_Text.getText().toString() + "-" + Month_Year_text.getText().toString() + " " + myCalendar.get(Calendar.HOUR_OF_DAY) + ":" + myCalendar.get(Calendar.MINUTE)));

                Entry_Title.getText();

                if (Entry_Title.getText().length() > 0 && Description.getText().length() > 0) {
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("journal_id", journalFragmentDataModel.getId());
                        if (tag_strain_id != -1) {
                            jsonObject.put("strain_id", tag_strain_id);
                        }
                        String date = Day_text.getText().toString() + " " + Date_Text.getText().toString() + "-" + Month_Year_text.getText().toString() + " " + myCalendar.get(Calendar.HOUR_OF_DAY) + ":" + myCalendar.get(Calendar.MINUTE) + ":" + myCalendar.get(Calendar.SECOND);
                        String feel = ":" + Feeling_text.getText().toString() + ":";
                        Log.d("onClick: ", date);
                        jsonObject.put("date", getDateAsApiFormate(date));
                        jsonObject.put("feeling",feel );
                        jsonObject.put("description", Description.getText().toString());
                        jsonObject.put("title", Entry_Title.getText().toString());
                        jsonObject.put("image", images);
                        jsonObject.put("video", videos);
                        if (Ttags_id.toString().length() > 0) {
                            jsonObject.put("tags_id", Ttags_id.toString());
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Log.d("Json", jsonObject.toString());
                    new VollyAPICall(AddNewJournalEvent.this, true, URL.create_journal_event, jsonObject, user.getSession_key(), Request.Method.POST, AddNewJournalEvent.this, APIActions.ApiActions.create_journal_event);

                } else {
                    CustomeToast.ShowCustomToast(AddNewJournalEvent.this, "Title and Description Fields are Required!", Gravity.TOP);
                }

                break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 1200) {
            Log.d("paths", data.getExtras().getString("file_path_arg"));
            if (data.getExtras().getBoolean("isVideo")) {
                if (data.getExtras().getBoolean("camera_video")) {
                    final String filePath = data.getExtras().getString("file_path_arg");
                    video_processing = ProgressDialogVideoProcessing.newInstance();
                    video_processing.show(((FragmentActivity) this).getSupportFragmentManager(), "pd");
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            new VideoProcessing().execute(filePath);
                        }
                    }, 200);
                } else {
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            String filePath = data.getExtras().getString("file_path_arg");
                            Bitmap video_thumbnil = getVideoThumbnail(filePath);
                            UploadVideo(filePath);
                            Drawable drawable = null;
                            if (video_thumbnil != null) {
                                journel_image.setImageDrawable(new BitmapDrawable(getResources(), video_thumbnil));
                                video_thumbnil = Bitmap.createScaledBitmap(video_thumbnil, 300, 300, false);
                                int corner_radious = (video_thumbnil.getWidth() * 6) / 100;
                                Bitmap bitmap = getRoundedCornerBitmap(video_thumbnil, corner_radious);
                                drawable = new BitmapDrawable(getResources(), bitmap);

                                if (!AttachmentAdded[0]) {
                                    attachment_one_video.setVisibility(View.VISIBLE);
                                    attachment_one.setVisibility(View.VISIBLE);
                                    attachment_one_img.setBackground(drawable);
                                    AttachmentAdded[attachment_count] = true;
                                    attachment_count++;
                                    drawables[0] = new BitmapDrawable(getResources(), video_thumbnil);
                                } else if (!AttachmentAdded[1]) {
                                    attachment_two_video.setVisibility(View.VISIBLE);
                                    attachment_two.setVisibility(View.VISIBLE);
                                    attachment_two_img.setBackground(drawable);
                                    AttachmentAdded[attachment_count] = true;
                                    attachment_count++;
                                    drawables[1] = new BitmapDrawable(getResources(), video_thumbnil);
                                } else if (!AttachmentAdded[2]) {
                                    attachment_three_video.setVisibility(View.VISIBLE);
                                    attachment_three.setVisibility(View.VISIBLE);
                                    attachment_three_img.setBackground(drawable);
                                    AttachmentAdded[attachment_count] = true;
                                    attachment_count++;
                                    drawables[2] = new BitmapDrawable(getResources(), video_thumbnil);
                                }
                            } else {
                                Drawable d = ContextCompat.getDrawable(AddNewJournalEvent.this, R.drawable.test_img);
                                Bitmap bitmapOrg = ((BitmapDrawable) d).getBitmap();
                                journel_image.setImageDrawable(d);
                                bitmapOrg = Bitmap.createScaledBitmap(bitmapOrg, 300, 300, false);
                                int corner_radious = (bitmapOrg.getWidth() * 6) / 100;
                                Bitmap bitmap = getRoundedCornerBitmap(bitmapOrg, corner_radious);
                                drawable = new BitmapDrawable(AddNewJournalEvent.this.getResources(), bitmap);

                                if (!AttachmentAdded[0]) {
                                    attachment_one_video.setVisibility(View.VISIBLE);
                                    attachment_one.setVisibility(View.VISIBLE);
                                    attachment_one_img.setBackground(drawable);
                                    AttachmentAdded[attachment_count] = true;
                                    attachment_count++;
                                    drawables[0] = d;
                                } else if (!AttachmentAdded[1]) {
                                    attachment_two_video.setVisibility(View.VISIBLE);
                                    attachment_two.setVisibility(View.VISIBLE);
                                    attachment_two_img.setBackground(drawable);
                                    AttachmentAdded[attachment_count] = true;
                                    attachment_count++;
                                    drawables[1] = d;
                                } else if (!AttachmentAdded[2]) {
                                    attachment_three_video.setVisibility(View.VISIBLE);
                                    attachment_three.setVisibility(View.VISIBLE);
                                    attachment_three_img.setBackground(drawable);
                                    AttachmentAdded[attachment_count] = true;
                                    attachment_count++;
                                    drawables[2] = d;
                                }
                            }
                        }
                    }, 10);
                }
            } else {
                Bitmap bitmapOrg = BitmapFactory.decodeFile(data.getExtras().getString("file_path_arg"));
                bitmapOrg = checkRotation(bitmapOrg, data.getExtras().getString("file_path_arg"));
                journel_image.setImageDrawable(new BitmapDrawable(getResources(), bitmapOrg));
                Drawable main_drawable = new BitmapDrawable(getResources(), bitmapOrg);

                bitmapOrg = Bitmap.createScaledBitmap(bitmapOrg, 300, 300, false);
                int corner_radious = (bitmapOrg.getWidth() * 10) / 100;
                Bitmap bitmap = getRoundedCornerBitmap(bitmapOrg, corner_radious);
                Drawable drawable = new BitmapDrawable(getResources(), bitmap);

                if (!AttachmentAdded[0]) {
                    attachment_one_video.setVisibility(View.GONE);
                    attachment_one.setVisibility(View.VISIBLE);
                    attachment_one_img.setBackground(drawable);
                    AttachmentAdded[attachment_count] = true;
                    attachment_count++;
                    drawables[0] = new BitmapDrawable(getResources(), bitmapOrg);
                } else if (!AttachmentAdded[1]) {
                    attachment_two_video.setVisibility(View.GONE);
                    attachment_two.setVisibility(View.VISIBLE);
                    attachment_two_img.setBackground(drawable);
                    AttachmentAdded[attachment_count] = true;
                    attachment_count++;
                    drawables[1] = new BitmapDrawable(getResources(), bitmapOrg);
                } else if (!AttachmentAdded[2]) {
                    attachment_three_video.setVisibility(View.GONE);
                    attachment_three.setVisibility(View.VISIBLE);
                    attachment_three_img.setBackground(drawable);
                    AttachmentAdded[attachment_count] = true;
                    attachment_count++;
                    drawables[2] = new BitmapDrawable(getResources(), bitmapOrg);
                }
                UploadImage(main_drawable);

            }
        }
    }

    public void UploadImage(Drawable drawable) {
        Save_Publish.setEnabled(false);
        Save_Publish.setClickable(false);
        Save_Publish.setBackgroundResource(R.drawable.q_a_menu_ask_q_button_inprogress);
        Lodaing_Dots.setVisibility(View.VISIBLE);
        isUploadinginprogress = true;
        new UploadImageAPIcall(AddNewJournalEvent.this, add_event_image, drawable, user.getSession_key(), AddNewJournalEvent.this, APIActions.ApiActions.add_media);
    }

    public void UploadVideo(String path) {
        Save_Publish.setEnabled(false);
        Save_Publish.setClickable(false);
        Save_Publish.setBackgroundResource(R.drawable.q_a_menu_ask_q_button_inprogress);
        Lodaing_Dots.setVisibility(View.VISIBLE);
        isUploadinginprogress = true;
        new UploadVideoAPIcall(AddNewJournalEvent.this, add_event_video, path, user.getSession_key(), AddNewJournalEvent.this, APIActions.ApiActions.add_media);
    }

    @Override
    public void onRequestSuccess(String response, APIActions.ApiActions apiActions) {
        Log.d("resposne", response);
        if (apiActions == APIActions.ApiActions.add_media) {
            Save_Publish.setEnabled(true);
            Save_Publish.setClickable(true);
            Save_Publish.setBackgroundResource(R.drawable.save_and_publish_journal);
            isUploadinginprogress = false;
            Lodaing_Dots.setVisibility(View.GONE);
            if (response.contains("image")) {
                if (attachment_count == 1) {
                    image_one_url = response;
                } else if (attachment_count == 2) {
                    if (image_two_url.length() > 0) {
                        if (image_one_url.length() == 0) {
                            image_one_url = response;
                        } else if (image_three_url.length() == 0) {
                            image_three_url = response;
                        }
                    } else {
                        image_two_url = response;
                    }
                } else if (attachment_count == 3) {
                    if (image_three_url.length() > 0) {
                        if (image_one_url.length() == 0) {
                            image_one_url = response;
                        } else if (image_two_url.length() == 0) {
                            image_two_url = response;
                        }
                    } else {
                        image_three_url = response;
                    }
                }
            } else {
                String path = "";
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    path = jsonObject.getString("file") + "#" + jsonObject.getString("poster");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (attachment_count == 1) {
                    video_one_url = path;
                } else if (attachment_count == 2) {
                    if (video_two_url.length() > 0) {
                        if (video_one_url.length() == 0) {
                            video_one_url = path;
                        } else if (video_three_url.length() == 0) {
                            video_three_url = path;
                        }
                    } else {
                        video_two_url = path;
                    }
                } else if (attachment_count == 3) {
                    if (video_three_url.length() > 0) {
                        if (video_one_url.length() == 0) {
                            video_one_url = path;
                        } else if (video_two_url.length() == 0) {
                            video_two_url = path;
                        }
                    } else {
                        video_three_url = path;
                    }
                }
            }
        } else if (apiActions == APIActions.ApiActions.get_tags) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONArray jsonArray = jsonObject.getJSONArray("successData");
                tag_data.clear();
                for (int x = 0; x < jsonArray.length(); x++) {
                    JSONObject tags_object = jsonArray.getJSONObject(x);
                    JournalTagsDataModal journalTagsDataModal = new JournalTagsDataModal();
                    journalTagsDataModal.setId(tags_object.getInt("id"));
                    journalTagsDataModal.setTitle(tags_object.getString("title"));
                    journalTagsDataModal.setIs_approved(tags_object.getInt("is_approved"));
                    journalTagsDataModal.setCreated_at(tags_object.getString("created_at"));
                    journalTagsDataModal.setUpdated_at(tags_object.getString("updated_at"));
                    journalTagsDataModal.setTagClicked(false);
                    tag_data.add(journalTagsDataModal);
                }
                addNewJournalSelectTagRecylerAdapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            JSONObject jsonObject = new JSONObject();
            new VollyAPICall(AddNewJournalEvent.this, false, URL.get_strains, jsonObject, user.getSession_key(), Request.Method.GET, AddNewJournalEvent.this, APIActions.ApiActions.get_strains);

        } else if (apiActions == APIActions.ApiActions.create_journal_event) {
            Log.d("resposne", response);
            finish();
        } else if (apiActions == APIActions.ApiActions.get_strains) {
            Log.d("resposne", response);
            StrainKeywords.clear();
            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONArray jsonArray = jsonObject.getJSONObject("successData").getJSONArray("strains");
                for (int x = 0; x < jsonArray.length(); x++) {
                    JSONObject strain_object = jsonArray.getJSONObject(x);
                    int id = strain_object.getInt("id");
                    String title = strain_object.getString("title");
                    StrainDetail strainDetail = new StrainDetail();
                    strainDetail.setId(id);
                    strainDetail.setTitle(title);
                    strainDetails.add(strainDetail);
                    StrainKeywords.add(title);
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, StrainKeywords);
                Search_Strains.setAdapter(adapter);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onRequestError(String response, APIActions.ApiActions apiActions) {
        Log.d("resposne", response);
        isUploadinginprogress = false;
        Lodaing_Dots.setVisibility(View.GONE);
        Save_Publish.setEnabled(true);
        Save_Publish.setClickable(true);
        Save_Publish.setBackgroundResource(R.drawable.save_and_publish_journal);
    }

    @Override
    public void onTagsItemClick(View view, int position) {
        if (tag_data.get(position).isTagClicked()) {
            tag_data.get(position).setTagClicked(false);
        } else {
            tag_data.get(position).setTagClicked(true);
        }
        addNewJournalSelectTagRecylerAdapter.notifyItemChanged(position);

    }

    @Override
    public void onTagsCrossItemClick(View view, int position) {
        data.remove(position);
        tag_strain_id = -1;
        tags_strain_keyword_recyler_adapter.notifyItemRemoved(position);
        Search_Strains.setVisibility(View.VISIBLE);
    }

    private class VideoProcessing extends AsyncTask<String, Integer, Bitmap> {
        String path = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            Bitmap video_thumbnil = null;
            try {
                path = params[0];
                video_thumbnil = getVideoThumbnail(path);
                Log.d("img", video_thumbnil.toString());
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
            return video_thumbnil;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            if (null != bitmap) {
                video_processing.dismiss();
                bitmap = checkRotationVideo(bitmap, path);
                journel_image.setImageDrawable(new BitmapDrawable(getResources(), bitmap));
                Bitmap video_thumbnil = Bitmap.createScaledBitmap(bitmap, 300, 300, false);
                int corner_radious_plc = (video_thumbnil.getWidth() * 10) / 100;
                Bitmap bitmap_plc = getRoundedCornerBitmap(video_thumbnil, corner_radious_plc);
                Drawable drawable = new BitmapDrawable(getResources(), bitmap_plc);

                if (!AttachmentAdded[0]) {
                    attachment_one_video.setVisibility(View.VISIBLE);
                    attachment_one.setVisibility(View.VISIBLE);
                    attachment_one_img.setBackground(drawable);
                    AttachmentAdded[attachment_count] = true;
                    attachment_count++;
                    drawables[0] = new BitmapDrawable(getResources(), video_thumbnil);
                } else if (!AttachmentAdded[1]) {
                    attachment_two_video.setVisibility(View.VISIBLE);
                    attachment_two.setVisibility(View.VISIBLE);
                    attachment_two_img.setBackground(drawable);
                    AttachmentAdded[attachment_count] = true;
                    attachment_count++;
                    drawables[1] = new BitmapDrawable(getResources(), video_thumbnil);
                } else if (!AttachmentAdded[2]) {
                    attachment_three_video.setVisibility(View.VISIBLE);
                    attachment_three.setVisibility(View.VISIBLE);
                    attachment_three_img.setBackground(drawable);
                    AttachmentAdded[attachment_count] = true;
                    attachment_count++;
                    drawables[2] = new BitmapDrawable(getResources(), video_thumbnil);
                }
            }

            UploadVideo(path);
        }
    }

    public class StrainDetail {
        String title;
        int id;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }
}
