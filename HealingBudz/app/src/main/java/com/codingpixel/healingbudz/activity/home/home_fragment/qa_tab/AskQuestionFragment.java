package com.codingpixel.healingbudz.activity.home.home_fragment.qa_tab;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.Request;
import com.codingpixel.healingbudz.DataModel.HomeQAfragmentDataModel;
import com.codingpixel.healingbudz.R;
import com.codingpixel.healingbudz.activity.home.home_fragment.groups_tab.GroupsChatViewActivity;
import com.codingpixel.healingbudz.application.HealingBudApplication;
import com.codingpixel.healingbudz.camera_activity.HBCameraActivity;
import com.codingpixel.healingbudz.customeUI.CustomEditText;
import com.codingpixel.healingbudz.customeUI.CustomeToast;
import com.codingpixel.healingbudz.customeUI.LoadingDots;
import com.codingpixel.healingbudz.customeUI.ProgressDialogVideoProcessing;
import com.codingpixel.healingbudz.customeUI.SelectableRoundedImageView;
import com.codingpixel.healingbudz.data_structure.APIActions;
import com.codingpixel.healingbudz.fragment_back_button_listner.BackButtonClickListner;
import com.codingpixel.healingbudz.network.VollyAPICall;
import com.codingpixel.healingbudz.network.model.APIResponseListner;
import com.codingpixel.healingbudz.network.upload_image.UploadImageAPIcall;
import com.codingpixel.healingbudz.network.upload_image.UploadVideoAPIcall;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static android.support.v4.content.ContextCompat.checkSelfPermission;
import static com.codingpixel.healingbudz.Utilities.ViewUtils.checkRotation;
import static com.codingpixel.healingbudz.Utilities.ViewUtils.checkRotationVideo;
import static com.codingpixel.healingbudz.activity.splash.Splash.user;
import static com.codingpixel.healingbudz.customeUI.ImageHelper.getRoundedCornerBitmap;
import static com.codingpixel.healingbudz.network.model.URL.add_image;
import static com.codingpixel.healingbudz.network.model.URL.add_question;
import static com.codingpixel.healingbudz.network.model.URL.add_video;
import static com.codingpixel.healingbudz.static_function.UIModification.HideKeyboard;


public class AskQuestionFragment extends Fragment implements BackButtonClickListner, APIResponseListner, View.OnClickListener {
    CustomEditText Question, Question_detail;
    TextView Question_counter, Question_counter_detail;
    Button Ask_your_budz;
    int x = 0;
    public Button Close_Fragment;
    View Scroll_adjust;
    ScrollView scroll_ask_question;
    int inital_index = 0;
    String final_text = "";
    boolean isKeywordChangeFunctionOpen = false;
    boolean isKeywordChangeFunctionOpenDetail = false;
    ArrayList<String> keywords = new ArrayList<>();
    ///
    Button add_attachment;
    RelativeLayout attachment_three, attachment_two, attachment_one;
    SelectableRoundedImageView attachment_three_image, attachment_two_image, attachment_one_image;
    ImageView attachment_three_video, attachment_two_video, attachment_one_video, attachment_three_cross, attachment_two_cross, attachment_one_cross;
    //
    int attachment_count = 0;
    TextView Lodaing_Dots_text;
    LoadingDots Lodaing_Dots;
    JSONObject jsonObject = new JSONObject();
    LinearLayout Dots_layout;
    boolean isUploadinginprogress = false;
    JSONArray attach_one_Array = new JSONArray();
    JSONArray attach_two_Array = new JSONArray();
    JSONArray attach_three_Array = new JSONArray();
    boolean isImageClick = false;
    boolean[] AttachmentAdded = new boolean[3];
    private ProgressDialogVideoProcessing video_processing;
    //


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_ask_question_layout, container, false);
        keywords.add("jawad");
        keywords.add("Canniban");
        keywords.add("Oil");
        keywords.add("Healing");
        keywords.add("Bud");
        keywords.add("Coding");
//        InitFragmentBackbtnListner(getActivity() , this);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Init(view);
        InitListenerForAttachment(view);


    }

    private void InitListenerForAttachment(View view) {
        attachment_one_cross.setOnClickListener(this);
        attachment_two_cross.setOnClickListener(this);
        attachment_three_cross.setOnClickListener(this);
        add_attachment.setOnClickListener(this);
    }

    public String GenrateString(String[] str_array) {
        String string = "";
        for (String aStr_array : str_array) {
            string = string + aStr_array + " ";
        }
        return string;
    }

    public void Init(View view) {
        //3
        add_attachment = view.findViewById(R.id.add_attachment);
        attachment_three = view.findViewById(R.id.attachment_three);
        attachment_three_image = view.findViewById(R.id.attachment_three_image);
        attachment_three_video = view.findViewById(R.id.attachment_three_video);
        attachment_three_cross = view.findViewById(R.id.attachment_three_cross);
        //2
        attachment_two = view.findViewById(R.id.attachment_two);
        attachment_two_image = view.findViewById(R.id.attachment_two_image);
        attachment_two_video = view.findViewById(R.id.attachment_two_video);
        attachment_two_cross = view.findViewById(R.id.attachment_two_cross);
        //1
        attachment_one = view.findViewById(R.id.attachment_one);
        attachment_one_image = view.findViewById(R.id.attachment_one_image);
        attachment_one_video = view.findViewById(R.id.attachment_one_video);
        attachment_one_cross = view.findViewById(R.id.attachment_one_cross);
        //
        Dots_layout = view.findViewById(R.id.dots_layout);
        Lodaing_Dots_text = view.findViewById(R.id.lodaing_dots_text);
        Lodaing_Dots = view.findViewById(R.id.loading_dots);
        attachment_one_image.setCornerRadiusDP(3f);
        attachment_two_image.setCornerRadiusDP(3f);
        attachment_three_image.setCornerRadiusDP(3f);
        attachment_count = 0;
        AttachmentAdded[0] = false;
        AttachmentAdded[1] = false;
        AttachmentAdded[2] = false;
        attachment_one.setVisibility(View.GONE);
        attachment_two.setVisibility(View.GONE);
        attachment_three.setVisibility(View.GONE);
        attachment_one_image.setImageDrawable(null);
        attachment_two_image.setImageDrawable(null);
        attachment_three_image.setImageDrawable(null);
        attachment_count = 0;
        //
        Scroll_adjust = view.findViewById(R.id.scrool_adjust);
        scroll_ask_question = view.findViewById(R.id.scroll_ask_question);
        Question = view.findViewById(R.id.question_text_field);
        final_text = "";
        Question.setText("");
        if (Question.getText().length() == 0) {
            Scroll_adjust.setVisibility(View.GONE);
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    scroll_ask_question.scrollTo(0, 0);
                    scroll_ask_question.fullScroll(View.FOCUS_UP);
                }
            }, 100);
        }
        Question_detail = view.findViewById(R.id.question_detail_textfiled);
        Question_detail.setImeOptions(EditorInfo.IME_ACTION_DONE);
        Question_counter = view.findViewById(R.id.question_character_counter);
        Question_counter_detail = view.findViewById(R.id.question_detail_character_counter);
        Ask_your_budz = view.findViewById(R.id.ask_your_budz);
        Scroll_adjust.setVisibility(View.VISIBLE);
        Question.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    Scroll_adjust.setVisibility(View.GONE);
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            scroll_ask_question.scrollTo(0, 0);
                            scroll_ask_question.fullScroll(View.FOCUS_UP);
                        }
                    }, 100);
                }
            }
        });
        Question_detail.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    Scroll_adjust.setVisibility(View.GONE);
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            scroll_ask_question.scrollTo(0, 0);
                            scroll_ask_question.fullScroll(View.FOCUS_UP);
                        }
                    }, 100);
                }
                return false;
            }
        });

        Question_detail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(final View view, boolean b) {
                if (b) {
                    Scroll_adjust.setVisibility(View.VISIBLE);
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            scroll_ask_question.scrollTo(0, 2000);
                            scroll_ask_question.fullScroll(View.FOCUS_DOWN);
                        }
                    }, 100);


                } else {
                    Scroll_adjust.setVisibility(View.GONE);
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            scroll_ask_question.scrollTo(0, 0);
                            scroll_ask_question.fullScroll(View.FOCUS_UP);
                        }
                    }, 100);
                }


            }
        });


        Question.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Question_counter.setText((charSequence.length()) + "/150 characters");

            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        Question_detail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Question_counter_detail.setText((charSequence.length()) + "/300 characters");

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        Ask_your_budz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HideKeyboard(getActivity());

                if (Question.getText().toString().trim().length() > 0 && Question_detail.getText().toString().trim().length() > 0) {
                    try {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("question", Question.getText().toString());
                        jsonObject.put("description", Question_detail.getText().toString());
                        JSONArray files_array = new JSONArray();
                        if (AttachmentAdded[0]) {
                            files_array.put(attach_one_Array.get(0));
                        }
                        if (AttachmentAdded[1]) {
                            files_array.put(attach_two_Array.get(0));
                        }

                        if (AttachmentAdded[2]) {
                            files_array.put(attach_three_Array.get(0));
                        }
                        jsonObject.put("file", files_array);
                        new VollyAPICall(getContext(), true, add_question, jsonObject, user.getSession_key(), Request.Method.POST, AskQuestionFragment.this, APIActions.ApiActions.ask_question);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    CustomeToast.ShowCustomToast(getActivity(), "Required fields are Empty!", Gravity.TOP);
                }


            }
        });

        Close_Fragment = view.findViewById(R.id.close);
        Close_Fragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        HideKeyboard(getActivity());
                        final_text = "";
                        attachment_count = 0;
                        AttachmentAdded[0] = false;
                        AttachmentAdded[1] = false;
                        AttachmentAdded[2] = false;
                        attachment_one.setVisibility(View.GONE);
                        attachment_two.setVisibility(View.GONE);
                        attachment_three.setVisibility(View.GONE);
                        attachment_one_image.setImageDrawable(null);
                        attachment_two_image.setImageDrawable(null);
                        attachment_three_image.setImageDrawable(null);
                        attachment_count = 0;
                        AttachmentAdded[0] = false;
                        AttachmentAdded[1] = false;
                        AttachmentAdded[2] = false;
                        attachment_one.setVisibility(View.GONE);
                        attachment_two.setVisibility(View.GONE);
                        attachment_three.setVisibility(View.GONE);
                        attachment_one_image.setImageDrawable(null);
                        attachment_two_image.setImageDrawable(null);
                        attachment_three_image.setImageDrawable(null);
                        attach_one_Array = new JSONArray();
                        attachment_count = 0;
                        Question.setText("");
                        Question_detail.setText("");
                        Question_counter_detail.setText("0/300 characters");
                        Question_counter.setText("0/300 characters");
                        FragmentManager manager = getActivity().getSupportFragmentManager();
                        FragmentTransaction transaction = manager.beginTransaction();
                        transaction.setCustomAnimations(R.anim.attatch_fragment_right_to_left, R.anim.detach_fragment);
                        transaction.remove(AskQuestionFragment.this);
                        transaction.commitAllowingStateLoss();
                    }
                });
            }
        });
        Question_detail.setKeyImeChangeListener(new CustomEditText.KeyImeChange() {

            @Override
            public void onKeyIme(int keyCode, KeyEvent event) {
                Scroll_adjust.setVisibility(View.GONE);
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        scroll_ask_question.scrollTo(0, 0);
                        scroll_ask_question.fullScroll(View.FOCUS_UP);
                    }
                }, 100);
                // All keypresses with the keyboard open will come through here!
                // You could also bubble up the true/false if you wanted
                // to disable propagation.
            }
        });

        // get Unregistrar

//
// call this method when you don't need the event listener anymore

//getActivity().getWindow().get
    }


    @Override
    public void onBackButtonClick() {

    }

    @Override
    public void onPause() {
        super.onPause();
//        final_text = "";
//        Question.setText("");

    }

    @Override
    public void onDetach() {
        super.onDetach();
        final_text = "";
        Question.setText("");
        attachment_count = 0;
        AttachmentAdded[0] = false;
        AttachmentAdded[1] = false;
        AttachmentAdded[2] = false;

        attachment_one.setVisibility(View.GONE);
        attachment_two.setVisibility(View.GONE);
        attachment_three.setVisibility(View.GONE);
        attachment_one_image.setImageDrawable(null);
        attachment_two_image.setImageDrawable(null);
        attachment_three_image.setImageDrawable(null);
    }


    public static int countWords(String s) {

        int wordCount = 0;

        boolean word = false;
        int endOfLine = s.length() - 1;

        for (int i = 0; i < s.length(); i++) {
            // if the char is a letter, word = true.
            if (Character.isLetter(s.charAt(i)) && i != endOfLine) {
                word = true;
                // if char isn't a letter and there have been letters before,
                // counter goes up.
            } else if (!Character.isLetter(s.charAt(i)) && word) {
                wordCount++;
                word = false;
                // last word of String; if it doesn't end with a non letter, it
                // wouldn't count without this.
            } else if (Character.isLetter(s.charAt(i)) && i == endOfLine) {
                wordCount++;
            }
        }
        return wordCount;
    }

    @Override
    public void onRequestSuccess(String response, APIActions.ApiActions apiActions) {
        if (apiActions == APIActions.ApiActions.add_media) {
            add_attachment.setOnClickListener(this);
            Dots_layout.setVisibility(View.GONE);
            add_attachment.setClickable(true);
            add_attachment.setEnabled(true);
            isUploadinginprogress = false;
            Ask_your_budz.setBackgroundResource(R.drawable.q_a_menu_ask_q_button);
            Ask_your_budz.setClickable(true);
            Ask_your_budz.setEnabled(true);
            JSONObject file_object = new JSONObject();
            try {
                JSONObject response_obg = new JSONObject(response);
                if (response_obg.getString("poster").length() > 4) {
                    file_object.put("media_type", "video");
                } else {
                    file_object.put("media_type", "image");
                }
                file_object.put("path", response_obg.getString("path"));
                file_object.put("poster", response_obg.getString("poster"));
                if (attachment_count == 1) {
                    attach_one_Array.put(file_object);
                } else if (attachment_count == 2) {
                    if (attach_one_Array.length() == 0) {
                        attach_one_Array.put(file_object);
                    } else
                        attach_two_Array.put(file_object);
                } else if (attachment_count == 3) {
                    if (attach_two_Array.length() == 0) {
                        attach_two_Array.put(file_object);
                    } else
                        attach_three_Array.put(file_object);
//                    attach_three_Array.put(file_object);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            JSONObject object = null;
            try {
                object = new JSONObject(response);
                CustomeToast.ShowCustomToast(getContext(), object.getString("successMessage"), Gravity.TOP);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            final JSONObject finalObject = object;
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    final_text = "";
                    Question.setText("");
                    Question_detail.setText("");
                    Question_counter_detail.setText("0/300 characters");
                    Question_counter.setText("0/300 characters");
                    attachment_count = 0;
                    AttachmentAdded[0] = false;
                    AttachmentAdded[1] = false;
                    AttachmentAdded[2] = false;
                    attachment_one.setVisibility(View.GONE);
                    attachment_two.setVisibility(View.GONE);
                    attachment_three.setVisibility(View.GONE);
                    attachment_one_image.setImageDrawable(null);
                    attachment_two_image.setImageDrawable(null);
                    attachment_three_image.setImageDrawable(null);
                    attachment_count = 0;
                    AttachmentAdded[0] = false;
                    AttachmentAdded[1] = false;
                    AttachmentAdded[2] = false;
                    attachment_one.setVisibility(View.GONE);
                    attachment_two.setVisibility(View.GONE);
                    attachment_three.setVisibility(View.GONE);
                    attachment_one_image.setImageDrawable(null);
                    attachment_two_image.setImageDrawable(null);
                    attachment_three_image.setImageDrawable(null);
                    attach_one_Array = new JSONArray();
                    attachment_count = 0;
                    HomeQAfragmentDataModel model = new HomeQAfragmentDataModel();
                    model.setUser_name(user.getFirst_name());
                    model.setUser_name_dscription("asks...");
                    String user_photo = user.getImage_path();
                    if (!user_photo.equalsIgnoreCase("null") && user_photo.length() > 6) {
                        model.setUser_photo(user_photo);
                        model.setAvatar(user.getAvatar());
                    } else {
                        model.setUser_photo(user.getAvatar());
                        model.setAvatar(user.getAvatar());
                    }
                    model.setUser_location(user.getLocation());
                    model.setQuestion(Question.getText().toString());
                    try {
                        model.setId(finalObject.getJSONObject("successData").getInt("id"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    model.setUser_id(user.getUser_id());
                    model.setQuestion_description(Question_detail.getText().toString());
                    model.setAnswerCount(0);
                    model.setGet_user_flag_count(0);
                    model.setGet_user_likes_count(0);

                    FragmentManager manager = getActivity().getSupportFragmentManager();
                    FragmentTransaction transaction = manager.beginTransaction();
                    transaction.setCustomAnimations(R.anim.attatch_fragment_right_to_left, R.anim.detach_fragment);
                    transaction.remove(AskQuestionFragment.this);
                    transaction.commitAllowingStateLoss();
                    QA_HomeTabFragment.ReloadData();
                    HideKeyboard(getActivity());
                }
            });
        }
    }

    @Override
    public void onRequestError(String response, APIActions.ApiActions apiActions) {
        Dots_layout.setVisibility(View.GONE);
        isUploadinginprogress = false;
        add_attachment.setClickable(true);
        add_attachment.setEnabled(true);
        Ask_your_budz.setBackgroundResource(R.drawable.q_a_menu_ask_q_button);
        add_attachment.setOnClickListener(this);
        try {
            JSONObject object = new JSONObject(response);
            CustomeToast.ShowCustomToast(getContext(), object.getString("errorMessage"), Gravity.TOP);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 1200) {
            Log.d("paths", data.getExtras().getString("file_path_arg"));
            if (data.getExtras().getBoolean("isVideo")) {
                video_processing = ProgressDialogVideoProcessing.newInstance();
                video_processing.show(AskQuestionFragment.this.getFragmentManager(), "pd");
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        Dots_layout.setVisibility(View.VISIBLE);
                        Lodaing_Dots_text.setText("Video Processing...");
                        String filePath = data.getExtras().getString("file_path_arg");
                        Drawable d = ContextCompat.getDrawable(getContext(), R.drawable.test_img);
                        Bitmap bitmapOrg = ((BitmapDrawable) d).getBitmap();
                        bitmapOrg = Bitmap.createScaledBitmap(bitmapOrg, 300, 300, false);
                        int corner_radious = (bitmapOrg.getWidth() * 10) / 100;
                        Bitmap bitmap = getRoundedCornerBitmap(bitmapOrg, corner_radious);
                        Drawable drawable = new BitmapDrawable(getContext().getResources(), bitmap);
                        if (!AttachmentAdded[0]) {
                            attachment_one_video.setVisibility(View.VISIBLE);
                            attachment_one.setVisibility(View.VISIBLE);
                            attachment_one_image.setImageDrawable(drawable);
                            new AskQuestionFragment.TestAsync(attachment_one_image).execute(filePath);
                            AttachmentAdded[0] = true;
                            attachment_count++;
                        } else if (!AttachmentAdded[1]) {
                            attachment_two_video.setVisibility(View.VISIBLE);
                            attachment_two.setVisibility(View.VISIBLE);
                            attachment_two_image.setImageDrawable(drawable);
                            new AskQuestionFragment.TestAsync(attachment_two_image).execute(filePath);
                            AttachmentAdded[1] = true;
                            attachment_count++;
                        } else if (!AttachmentAdded[2]) {
                            attachment_three_video.setVisibility(View.VISIBLE);
                            attachment_three.setVisibility(View.VISIBLE);
                            attachment_three_image.setImageDrawable(drawable);
                            new AskQuestionFragment.TestAsync(attachment_three_image).execute(filePath);
                            AttachmentAdded[2] = true;
                            attachment_count++;
                        }
                        isImageClick = false;
                    }
                }, 250);
            } else {
                Bitmap bitmapOrg = BitmapFactory.decodeFile(data.getExtras().getString("file_path_arg"));
                bitmapOrg = checkRotation(bitmapOrg, data.getExtras().getString("file_path_arg"));
                bitmapOrg = Bitmap.createScaledBitmap(bitmapOrg, 300, 300, false);
                int corner_radious = (bitmapOrg.getWidth() * 10) / 100;
                Bitmap bitmap = getRoundedCornerBitmap(bitmapOrg, corner_radious);
                Drawable drawable = new BitmapDrawable(getResources(), bitmap);
                if (!AttachmentAdded[0]) {
                    attachment_one.setVisibility(View.VISIBLE);
                    attachment_one_video.setVisibility(View.GONE);
                    attachment_one_image.setImageDrawable(drawable);
                    AttachmentAdded[0] = true;
                    attachment_count++;
                } else if (!AttachmentAdded[1]) {
                    attachment_two.setVisibility(View.VISIBLE);
                    attachment_two_image.setImageDrawable(drawable);
                    attachment_two_video.setVisibility(View.GONE);
                    AttachmentAdded[1] = true;
                    attachment_count++;
                } else if (!AttachmentAdded[2]) {
                    attachment_three.setVisibility(View.VISIBLE);
                    attachment_three_video.setVisibility(View.GONE);
                    attachment_three_image.setImageDrawable(drawable);
                    AttachmentAdded[2] = true;
                    attachment_count++;
                }
                Bitmap bitmapOrgAbc = BitmapFactory.decodeFile(data.getExtras().getString("file_path_arg"));
                bitmapOrgAbc = checkRotation(bitmapOrgAbc, data.getExtras().getString("file_path_arg"));
                Drawable main_drawable = new BitmapDrawable(getResources(), bitmapOrgAbc);
                UploadImage(data.getExtras().getString("file_path_arg"));
                isImageClick = false;
            }
        } else if (requestCode == 1 && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn;
            filePathColumn = new String[]{MediaStore.Images.Media.DATA};
            Cursor cursor = getActivity().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            Bitmap bitmapOrg = BitmapFactory.decodeFile(picturePath);
            int corner_radious = (bitmapOrg.getWidth() * 10) / 100;
            Bitmap bitmap = getRoundedCornerBitmap(bitmapOrg, corner_radious);
            bitmap = Bitmap.createScaledBitmap(bitmap, 300, 300, false);
            Drawable drawable = new BitmapDrawable(getResources(), bitmap);
            if (!AttachmentAdded[0]) {
                attachment_one.setVisibility(View.VISIBLE);
                attachment_one_video.setVisibility(View.GONE);
                attachment_one_image.setImageDrawable(drawable);
                AttachmentAdded[0] = true;
                attachment_count++;
            } else if (!AttachmentAdded[1]) {
                attachment_two.setVisibility(View.VISIBLE);
                attachment_two_video.setVisibility(View.GONE);
                attachment_two_image.setImageDrawable(drawable);
                AttachmentAdded[1] = true;
                attachment_count++;
            } else if (!AttachmentAdded[2]) {
                attachment_three.setVisibility(View.VISIBLE);
                attachment_three_video.setVisibility(View.GONE);
                attachment_three_image.setImageDrawable(drawable);
                AttachmentAdded[2] = true;
                attachment_count++;
            }
            isImageClick = false;

        }
    }


    public void UploadImage(String drawable) {
        isUploadinginprogress = false;
        Ask_your_budz.setBackgroundResource(R.drawable.q_a_menu_ask_q_button_inprogress);
        Ask_your_budz.setClickable(false);
        Ask_your_budz.setEnabled(false);
        add_attachment.setClickable(false);
        add_attachment.setEnabled(false);
        add_attachment.setOnClickListener(null);
        new UploadImageAPIcall(getContext(), add_image, drawable, user.getSession_key(), this, APIActions.ApiActions.add_media);
    }

    public void UploadVideo(String path) {
        isUploadinginprogress = false;
        Ask_your_budz.setBackgroundResource(R.drawable.q_a_menu_ask_q_button_inprogress);
        Ask_your_budz.setClickable(false);
        Ask_your_budz.setEnabled(false);
        add_attachment.setClickable(false);
        add_attachment.setEnabled(false);
        add_attachment.setOnClickListener(null);
        new UploadVideoAPIcall(HealingBudApplication.getContext(), add_video, path, user.getSession_key(), this, APIActions.ApiActions.add_media);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length != 0) {
            if (checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
//                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//                startActivityForResult(cameraIntent, 7677);
            } else if (requestCode == 1200) {
                if (attachment_count < 3) {

                    Intent intent = new Intent(getContext(), HBCameraActivity.class);
                    intent.putExtra("isVideoCaptureAble", true);
                    startActivityForResult(intent, 1200);
                } else {
                    Animation startAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.shaking);
                    attachment_one.startAnimation(startAnimation);
                    attachment_two.startAnimation(startAnimation);
                    attachment_three.startAnimation(startAnimation);
                }
//                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//                startActivityForResult(cameraIntent, 7677);
            } else if (requestCode == 1001) {
//                getCameraCall();
            } else if (requestCode == 1002) {
//                settingFregment.setCamera();


            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_attachment:
                if (!isUploadinginprogress) {
                    isImageClick = true;
                    if (Build.VERSION.SDK_INT > 15) {
                        final String[] permissions = {
                                Manifest.permission.CAMERA,
                                Manifest.permission.RECORD_AUDIO,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_EXTERNAL_STORAGE};
                        final List<String> permissionsToRequest = new ArrayList<>();
                        for (String permission : permissions) {
                            if (checkSelfPermission(getActivity(), permission) != PackageManager.PERMISSION_GRANTED) {
                                permissionsToRequest.add(permission);
                            }
                        }
                        if (!permissionsToRequest.isEmpty()) {
                            requestPermissions(permissionsToRequest.toArray(new String[permissionsToRequest.size()]), 1200);
//                            ActivityCompat.requestPermissions(getActivity(), permissionsToRequest.toArray(new String[permissionsToRequest.size()]), 12);
                        } else {
                            if (attachment_count < 3) {

                                Intent intent = new Intent(getContext(), HBCameraActivity.class);
                                intent.putExtra("isVideoCaptureAble", true);
                                startActivityForResult(intent, 1200);
                            } else {
                                Animation startAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.shaking);
                                attachment_one.startAnimation(startAnimation);
                                attachment_two.startAnimation(startAnimation);
                                attachment_three.startAnimation(startAnimation);
                            }
                        }
                    } else {
                        if (attachment_count < 3) {
                            Intent intent = new Intent(getContext(), HBCameraActivity.class);
                            intent.putExtra("isVideoCaptureAble", true);
                            startActivityForResult(intent, 1200);
                        } else {
                            Animation startAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.shaking);
                            attachment_one.startAnimation(startAnimation);
                            attachment_two.startAnimation(startAnimation);
                            attachment_three.startAnimation(startAnimation);
                        }
                    }
                }
                break;
            case R.id.attachment_one_cross:
                Dots_layout.setVisibility(View.GONE);
                isUploadinginprogress = false;
                Ask_your_budz.setBackgroundResource(R.drawable.q_a_menu_ask_q_button);
                Ask_your_budz.setClickable(true);
                Ask_your_budz.setEnabled(true);
                attachment_one.setVisibility(View.GONE);
                AttachmentAdded[0] = false;
                attachment_count--;
                attach_one_Array.remove(0);
                break;
            case R.id.attachment_two_cross:
                Dots_layout.setVisibility(View.GONE);
                isUploadinginprogress = false;
                Ask_your_budz.setBackgroundResource(R.drawable.q_a_menu_ask_q_button);
                Ask_your_budz.setClickable(true);
                Ask_your_budz.setEnabled(true);
                attachment_two.setVisibility(View.GONE);
                AttachmentAdded[1] = false;
                attachment_count--;
                attach_two_Array.remove(0);
                break;
            case R.id.attachment_three_cross:
                Dots_layout.setVisibility(View.GONE);
                isUploadinginprogress = false;
                Ask_your_budz.setBackgroundResource(R.drawable.q_a_menu_ask_q_button);
                Ask_your_budz.setClickable(true);
                Ask_your_budz.setEnabled(true);
                attachment_three.setVisibility(View.GONE);
                AttachmentAdded[2] = false;
                attachment_count--;
                attach_three_Array.remove(0);
                break;
        }
    }

    private class TestAsync extends AsyncTask<String, String, Bitmap> {

        String path = "";
        ImageView activity;

        TestAsync(ImageView imageView) {
            this.activity = imageView;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            Bitmap video_thumbnil = null;
            try {
                path = params[0];
                video_thumbnil = GroupsChatViewActivity.getVideoThumbnail(params[0]);
                Log.d("img", video_thumbnil.toString());
                onProgressUpdate();
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
            return video_thumbnil;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            bitmap = checkRotationVideo(bitmap, path);
            Bitmap video_thumbnil = Bitmap.createScaledBitmap(bitmap, 300, 300, false);
            int corner_radious_plc = (video_thumbnil.getWidth() * 10) / 100;
            Bitmap bitmap_plc = getRoundedCornerBitmap(video_thumbnil, corner_radious_plc);
            Drawable drawable = new BitmapDrawable(HealingBudApplication.getContext().getResources(), bitmap_plc);
            this.activity.setImageDrawable(drawable);
            video_processing.dismiss();
            Lodaing_Dots_text.setText("Uploading Video...");
            UploadVideo(path);

        }
    }


}


// first method
//    public void OnTextAfterChange() {
//                boolean isKeywordFind = false;
//                if (editable.length() < inital_index) {
//                    inital_index = editable.length();
//                }
//                final_text = Question.getText().toString().substring(inital_index, Question.getText().toString().length());
//                for (int y = 0; y < keywords.size(); y++) {
//                    if (final_text.toLowerCase().contains(keywords.get(y).toLowerCase())) {
//                        isKeywordFind = true;
//                        getActivity().runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                String genrated_string = Question.getText().toString().toLowerCase();
//                                ;
//                                inital_index = Question.getText().length();
//                                for (int z = 0; z < keywords.size(); z++) {
//                                    if (genrated_string.toLowerCase().contains(keywords.get(z).toLowerCase())) {
//                                        String[] spl = genrated_string.split(keywords.get(z).toLowerCase());
//                                        if (spl.length == 0) {
//                                            genrated_string = "<b><font color=#6d96ad>" + keywords.get(z) + "</font></b>";
//                                        } else {
//                                            String second_genrated_string = "";
//                                            for (int x = 0; x < spl.length; x++) {
//                                                String firstText = spl[x];
//                                                String key_wrd_string = keywords.get(z).toLowerCase();
//                                                String[] temp_spl = Question.getText().toString().split(" ");
//                                                int count_wrd = countWords(key_wrd_string);
//                                                if (countWords(key_wrd_string) != spl.length
//                                                        && x == spl.length - 1
//                                                        && spl.length > 1
//                                                        && !Objects.equals(temp_spl[temp_spl.length - 1], key_wrd_string)) {
//                                                    if (!temp_spl[temp_spl.length - 1].contains(key_wrd_string)) {
//                                                        second_genrated_string = second_genrated_string + firstText + "";
//                                                    } else {
//                                                        second_genrated_string = second_genrated_string + firstText + key_wrd_string;
//                                                    }
//                                                } else {
//                                                    second_genrated_string = second_genrated_string + firstText + "<b><font color=#6d96ad>" + keywords.get(z) + "</font></b>";
//                                                }
//                                            }
//                                            genrated_string = second_genrated_string;
//                                        }
//                                    }
//                                }
//                                final_text = genrated_string;
//                            }
//                        });
//                    }
//                }
//
//                if (x == 0 && isKeywordFind) {
//                    x++;
//                    Question.setText(Html.fromHtml(final_text));
//                    Question.setSelection(Question.getText().length());
//                    Log.d("Quesion", Question.getText().toString());
//                    x = 0;
//                }
//    }



