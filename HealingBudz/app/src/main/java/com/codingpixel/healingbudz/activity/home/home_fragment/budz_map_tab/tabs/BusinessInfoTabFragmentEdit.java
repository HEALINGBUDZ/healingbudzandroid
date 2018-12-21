package com.codingpixel.healingbudz.activity.home.home_fragment.budz_map_tab.tabs;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.icu.util.Calendar;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.android.volley.Request;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.codingpixel.healingbudz.DataModel.BudzMapReviews;
import com.codingpixel.healingbudz.DataModel.PaymentMethod;
import com.codingpixel.healingbudz.R;
import com.codingpixel.healingbudz.Utilities.Constants;
import com.codingpixel.healingbudz.Utilities.DateConverter;
import com.codingpixel.healingbudz.Utilities.GetUserLatLng;
import com.codingpixel.healingbudz.activity.home.home_fragment.budz_map_tab.AddNewBudzMapActivity;
import com.codingpixel.healingbudz.activity.home.home_fragment.budz_map_tab.BudzMapDetailsActivity;
import com.codingpixel.healingbudz.activity.home.home_fragment.budz_map_tab.BudzMapHomeFragment;
import com.codingpixel.healingbudz.adapter.AddNewJournalKeywordRecylerAdapter;
import com.codingpixel.healingbudz.adapter.AddNewPaymentMethodRecylerAdapter;
import com.codingpixel.healingbudz.application.HealingBudApplication;
import com.codingpixel.healingbudz.camera_activity.HBCameraActivity;
import com.codingpixel.healingbudz.customeUI.CustomeToast;
import com.codingpixel.healingbudz.customeUI.ProgressDialog;
import com.codingpixel.healingbudz.customeUI.customalerts.SweetAlertDialog;
import com.codingpixel.healingbudz.data_structure.APIActions;
import com.codingpixel.healingbudz.interfaces.FragmentToActivityComm;
import com.codingpixel.healingbudz.interfaces.UserLocationListner;
import com.codingpixel.healingbudz.map.WorkaroundMapFragment;
import com.codingpixel.healingbudz.network.VollyAPICall;
import com.codingpixel.healingbudz.network.model.APIResponseListner;
import com.codingpixel.healingbudz.network.model.URL;
import com.codingpixel.healingbudz.network.upload_image.UploadImageAPIcall;
import com.github.aakira.expandablelayout.ExpandableLayoutListener;
import com.github.aakira.expandablelayout.ExpandableRelativeLayout;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static com.codingpixel.healingbudz.Utilities.ClickAbleKeywordText.MakeKeywordClickableText;
import static com.codingpixel.healingbudz.Utilities.GetUserLatLng.GetAddressFromLatlng;
import static com.codingpixel.healingbudz.Utilities.ViewUtils.checkRotation;
import static com.codingpixel.healingbudz.activity.home.home_fragment.budz_map_tab.AddNewBudzMapActivity.add_main_scroll_view;
import static com.codingpixel.healingbudz.activity.home.home_fragment.budz_map_tab.AddNewBudzMapActivity.add_new_bud_jason_Data;
import static com.codingpixel.healingbudz.activity.home.home_fragment.budz_map_tab.BudzMapHomeFragment.budz_map_item_clickerd_dataModel;
import static com.codingpixel.healingbudz.activity.home.home_fragment.budz_map_tab.BudzMapHomeFragment.budz_map_item_clickerd_dataModel_abc;
import static com.codingpixel.healingbudz.activity.home.home_fragment.budz_map_tab.BudzMapHomeFragment.getBitmapFromVectorDrawable;
import static com.codingpixel.healingbudz.activity.splash.Splash.user;
import static com.codingpixel.healingbudz.customeUI.ImageHelper.getRoundedCornerBitmap;
import static com.codingpixel.healingbudz.data_structure.APIActions.ApiActions.get_budz_profile;
import static com.codingpixel.healingbudz.data_structure.APIActions.ApiActions.get_budz_profile_can;
import static com.codingpixel.healingbudz.data_structure.APIActions.ApiActions.get_budz_profile_event;
import static com.codingpixel.healingbudz.data_structure.APIActions.ApiActions.get_budz_profile_overall;
import static com.codingpixel.healingbudz.data_structure.APIActions.ApiActions.get_languages;
import static com.codingpixel.healingbudz.network.model.URL.getLocatioZipcode_part1;
import static com.codingpixel.healingbudz.network.model.URL.getLocatioZipcode_part2;
import static com.codingpixel.healingbudz.network.model.URL.getLocatioZipcode_part3;
import static com.codingpixel.healingbudz.static_function.StaticObjects.emailPattern;
import static com.codingpixel.healingbudz.static_function.UIModification.HideKeyboard;
import static com.google.android.gms.location.places.AutocompleteFilter.TYPE_FILTER_REGIONS;

public class BusinessInfoTabFragmentEdit extends Fragment implements OnMapReadyCallback, View.OnClickListener, UserLocationListner, APIResponseListner, AddNewJournalKeywordRecylerAdapter.ItemClickListener, AddNewPaymentMethodRecylerAdapter.ItemClickListener {
    private GoogleMap mMap;
    WorkaroundMapFragment mapFragment;
    Spinner open_time, Close_time;
    TextView Location_text;
    EditText Description, PhoneNumber, Website_url;
    double latitude, longitude;
    String business_type_id = "1";
    int marker_resoures = R.drawable.ic_dispancry_marker_icon;


    ImageView Monday_Tcik;
    LinearLayout Monday_Open;
    TextView Monday_Close;
    boolean isMondayOpen = true;
    Button Monday_start, Monday_end;


    ImageView Tuesday_Tcik;
    LinearLayout Tuesday_Open;
    TextView Tuesday_Close;
    boolean isTuesdayOpen = true;
    Button Tuesday_start, Tuesday_end;

    ImageView Wednesday_Tcik;
    LinearLayout Wednesday_Open;
    TextView Wednesday_Close;
    boolean isWednesdayOpen = true;
    Button Wednesday_start, Wednesday_end;


    ImageView Thursday_Tcik;
    LinearLayout Thursday_Open;
    TextView Thursday_Close;
    boolean isThursdayOpen = true;
    Button Thursday_start, Thursday_end;


    ImageView Friday_Tcik;
    LinearLayout Friday_Open;
    TextView Friday_Close;
    boolean isFridayOpen = true;
    Button Friday_start, Friday_end;


    ImageView Saturday_Tcik;
    LinearLayout Saturday_Open;
    TextView Saturday_Close;
    boolean isSaturdayOpen = true;
    Button Saturday_start, Saturday_end;

    ImageView Sunday_Tcik;
    LinearLayout Sunday_Open;
    TextView Sunday_Close;
    boolean isSundayOpen = true;
    Button Sunday_start, Sunday_end;

    Button Submit_data;
    LinearLayout Medical_views;
    TextView Monday, Tuesday, Wednesday, Thursday, Friday, Saturday, Sunday;


    AutoCompleteTextView Languages;
    RecyclerView Selected_Languages, payment_method_recycler_view;

    ArrayList<String> Selected_languages_data = new ArrayList<>();
    AddNewJournalKeywordRecylerAdapter languages_recyler_adapter;
    AddNewPaymentMethodRecylerAdapter addNewPaymentMethodRecylerAdapter;
    Button insurance_btn_off, insurance_btn_on;
    LinearLayout Switch_Layout;
    boolean isInsurance = true;

    EditText Office_Policcy, Pre_Visting_requirments, twitter_url, instagram_url, facebook_url, email_url, et_zip_code;
    LinearLayout Time_Oprations;
    LinearLayout Event_Layout, Payment_layout;
    RelativeLayout address_layout;
    Button Event_start, Event_end, Event_date;
    List<String> languages = new ArrayList<String>();
    ArrayList<Language> languages_main_Data = new ArrayList<>();
    List<PaymentMethod> paymentMethodList = new ArrayList<>();
    RecyclerView payment_method_selection;
    private static View viewSt;
    public static final int REQUEST_PLACE_PICKER = 12345;
    LinearLayout med_bay, enter_bay, slider_med_layout, Other_display_layout, comment_image_layout;
    boolean isMedHappen = false;
    ExpandableRelativeLayout expandableLayout, expandableLayoutTwo;
    View view;
    LinearLayout other_links;
    ImageView attachement_img, attatchment_cross;
    Button upload_comment_image;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.business_info_tab_layout, container, false);
        this.view = view;
//        View view = inflater.inflate(R.layout.business_info_tab_edit_layout, container, false);
        viewSt = view;
        JSONObject object = new JSONObject();
        new VollyAPICall(view.getContext(), false, URL.get_languages, object, user.getSession_key(), Request.Method.GET, BusinessInfoTabFragmentEdit.this, get_languages);
        Location_text = view.findViewById(R.id.location_text);
        et_zip_code = view.findViewById(R.id.et_zip_code);
        med_bay = view.findViewById(R.id.med_bay);
        other_links = view.findViewById(R.id.other_links);
        enter_bay = view.findViewById(R.id.enter_bay);
        attachement_img = view.findViewById(R.id.attachement_img);
        attatchment_cross = view.findViewById(R.id.attatchment_cross);
        upload_comment_image = view.findViewById(R.id.upload_comment_image);
        expandableLayout = view.findViewById(R.id.expandableLayout);
        expandableLayoutTwo = view.findViewById(R.id.expandableLayoutTwo);
        Other_display_layout = view.findViewById(R.id.other_view_items);
        comment_image_layout = view.findViewById(R.id.comment_image_layout);
        payment_method_selection = view.findViewById(R.id.payment_method_selection);
        address_layout = view.findViewById(R.id.address_layout);
        Medical_views = view.findViewById(R.id.medical_views);
        Medical_views.setVisibility(View.GONE);
        Event_Layout = view.findViewById(R.id.event_layout);
        Payment_layout = view.findViewById(R.id.select_payment_layout);
        Event_Layout.setVisibility(View.GONE);
        Description = view.findViewById(R.id.descriptions);
        PhoneNumber = view.findViewById(R.id.phone_number);
        Website_url = view.findViewById(R.id.web_site);
        Submit_data = view.findViewById(R.id.submit_data);
        Submit_data.setText("UPDATE LISTING");
        new GetUserLatLng().getUserLocation(getContext(), BusinessInfoTabFragmentEdit.this);
        mapFragment = (WorkaroundMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        ((WorkaroundMapFragment) getChildFragmentManager().findFragmentById(R.id.map)).setListener(new WorkaroundMapFragment.OnTouchListener() {
            @Override
            public void onTouch() {
                add_main_scroll_view.requestDisallowInterceptTouchEvent(true);
            }
        });
        mapFragment.getMapAsync(this);

        HideKeyboard(getActivity());
        setTiming(view);
        Submit_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (email_url.getText().toString().trim().length() == 0) {

                    if (business_type_id.equalsIgnoreCase("2") || business_type_id.equalsIgnoreCase("7") || business_type_id.equalsIgnoreCase("6")) {
                        if (et_zip_code.getText().toString().trim().length() > 2) {
                            new VollyAPICall(getActivity()
                                    , true
                                    , getLocatioZipcode_part1 + "USA" + getLocatioZipcode_part2 + et_zip_code.getText().toString().trim() + getLocatioZipcode_part3
                                    , new JSONObject(), null
                                    , Request.Method.POST
                                    , new APIResponseListner() {
                                @Override
                                public void onRequestSuccess(String response, APIActions.ApiActions apiActions) {
                                    JSONObject jsonObject = null;
                                    try {
                                        jsonObject = new JSONObject(response);
                                        JSONArray jsonArray = jsonObject.getJSONArray("results");
                                        int stst = -1;
                                        if (jsonArray.length() > 0) {
                                            JSONObject object = jsonArray.getJSONObject(0);
                                            JSONArray array = jsonArray.getJSONObject(0).getJSONArray("address_components");
                                            for (int i = 0; i < array.length(); i++) {
                                                String name = array.getJSONObject(i).getString("long_name");
                                                for (int j = 0; j < Constants.stateList.length; j++) {
                                                    if (Constants.stateList[j].equalsIgnoreCase(name)) {

                                                        stst = 1;
                                                    }
                                                }
                                            }
                                            if (stst == 1) {
                                                new SweetAlertDialog(getActivity(), SweetAlertDialog.NORMAL_TYPE)
                                                        .setTitleText("Error!")
                                                        .setContentText("You can't create this type of business in this zip code.")
                                                        .setConfirmText("Okay!")
                                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                            @Override
                                                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                                sweetAlertDialog.dismissWithAnimation();

                                                            }
                                                        }).show();
                                            } else {
                                                submitData();
                                                //TODO SUBMIT WORK
                                            }

                                        } else {
                                            new SweetAlertDialog(getActivity(), SweetAlertDialog.NORMAL_TYPE)
                                                    .setTitleText("Error!")
                                                    .setContentText("You can't create this type of business in this zip code.")
                                                    .setConfirmText("Okay!")
                                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                        @Override
                                                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                            sweetAlertDialog.dismissWithAnimation();

                                                        }
                                                    }).show();
                                        }

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        new SweetAlertDialog(getActivity(), SweetAlertDialog.NORMAL_TYPE)
                                                .setTitleText("Error!")
                                                .setContentText("You can't create this type of business in this zip code.")
                                                .setConfirmText("Okay!")
                                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                    @Override
                                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                        sweetAlertDialog.dismissWithAnimation();

                                                    }
                                                }).show();
                                    }
                                }

                                @Override
                                public void onRequestError(String response, APIActions.ApiActions apiActions) {
                                    new SweetAlertDialog(getActivity(), SweetAlertDialog.NORMAL_TYPE)
                                            .setTitleText("Error!")
                                            .setContentText("You can't create this type of business in this zip code.")
                                            .setConfirmText("Okay!")
                                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                @Override
                                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                    sweetAlertDialog.dismissWithAnimation();

                                                }
                                            }).show();
                                }
                            }
                                    , APIActions.ApiActions.chech_state);
                        } else {
                            submitData();
                        }

                    } else {
                        submitData();
                    }
                } else {
                    if (!email_url.getText().toString().trim().matches(emailPattern)) {
                        CustomeToast.ShowCustomToast(view.getContext(), "Incorrect email format. Please provide a valid email address!", Gravity.TOP);
                    } else {
                        if (business_type_id.equalsIgnoreCase("2") || business_type_id.equalsIgnoreCase("7") || business_type_id.equalsIgnoreCase("6")) {

                            if (et_zip_code.getText().toString().trim().length() > 2) {
                                new VollyAPICall(getActivity()
                                        , true
                                        , getLocatioZipcode_part1 + "USA" + getLocatioZipcode_part2 + et_zip_code.getText().toString().trim() + getLocatioZipcode_part3
                                        , new JSONObject(), null
                                        , Request.Method.POST
                                        , new APIResponseListner() {
                                    @Override
                                    public void onRequestSuccess(String response, APIActions.ApiActions apiActions) {
                                        JSONObject jsonObject = null;
                                        try {
                                            jsonObject = new JSONObject(response);
                                            JSONArray jsonArray = jsonObject.getJSONArray("results");
                                            int stst = -1;
                                            if (jsonArray.length() > 0) {
                                                JSONObject object = jsonArray.getJSONObject(0);
                                                JSONArray array = jsonArray.getJSONObject(0).getJSONArray("address_components");
                                                for (int i = 0; i < array.length(); i++) {
                                                    String name = array.getJSONObject(i).getString("long_name");
                                                    for (int j = 0; j < Constants.stateList.length; j++) {
                                                        if (Constants.stateList[j].equalsIgnoreCase(name)) {

                                                            stst = 1;
                                                        }
                                                    }
                                                }
                                                if (stst == 1) {
                                                    new SweetAlertDialog(getActivity(), SweetAlertDialog.NORMAL_TYPE)
                                                            .setTitleText("Error!")
                                                            .setContentText("You can't create this type of business in this zip code.")
                                                            .setConfirmText("Okay!")
                                                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                                @Override
                                                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                                    sweetAlertDialog.dismissWithAnimation();

                                                                }
                                                            }).show();
                                                } else {
                                                    submitData();
                                                    //TODO SUBMIT WORK
                                                }

                                            } else {
                                                new SweetAlertDialog(getActivity(), SweetAlertDialog.NORMAL_TYPE)
                                                        .setTitleText("Error!")
                                                        .setContentText("You can't create this type of business in this zip code.")
                                                        .setConfirmText("Okay!")
                                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                            @Override
                                                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                                sweetAlertDialog.dismissWithAnimation();

                                                            }
                                                        }).show();
                                            }

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                            new SweetAlertDialog(getActivity(), SweetAlertDialog.NORMAL_TYPE)
                                                    .setTitleText("Error!")
                                                    .setContentText("You can't create this type of business in this zip code.")
                                                    .setConfirmText("Okay!")
                                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                        @Override
                                                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                            sweetAlertDialog.dismissWithAnimation();

                                                        }
                                                    }).show();
                                        }
                                    }

                                    @Override
                                    public void onRequestError(String response, APIActions.ApiActions apiActions) {
                                        new SweetAlertDialog(getActivity(), SweetAlertDialog.NORMAL_TYPE)
                                                .setTitleText("Error!")
                                                .setContentText("You can't create this type of business in this zip code.")
                                                .setConfirmText("Okay!")
                                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                    @Override
                                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                        sweetAlertDialog.dismissWithAnimation();

                                                    }
                                                }).show();
                                    }
                                }
                                        , APIActions.ApiActions.chech_state);
                            } else {
                                submitData();
                            }

                        } else {
                            submitData();
                        }

                    }

                }
            }
        });
        Selected_Languages = view.findViewById(R.id.recyler_languages);
        languages_recyler_adapter = new AddNewJournalKeywordRecylerAdapter(getContext(), Selected_languages_data, true);
        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(getContext());
        layoutManager.setFlexDirection(FlexDirection.ROW);
        layoutManager.setJustifyContent(JustifyContent.FLEX_START);
        Selected_Languages.setLayoutManager(layoutManager);
        Selected_Languages.setAdapter(languages_recyler_adapter);
        languages_recyler_adapter.setClickListener(this);
        Languages = view.findViewById(R.id.languages);
        Languages.setThreshold(0);
        Languages.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Languages.showDropDown();
                return false;
            }
        });
        Languages.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Selected_languages_data.add(adapterView.getItemAtPosition(i).toString());
                languages_recyler_adapter.notifyDataSetChanged();
//                adapterView.removeView(view);
                adapter.remove(adapterView.getItemAtPosition(i).toString());
                adapter.notifyDataSetChanged();
                Languages.setText("");
            }
        });

        insurance_btn_off = view.findViewById(R.id.btn_off);
        insurance_btn_on = view.findViewById(R.id.btn_on);
        Switch_Layout = view.findViewById(R.id.switch_layout);
        Switch_Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isInsurance) {
                    insurance_btn_on.setText("");
                    insurance_btn_off.setText("OFF");
                    insurance_btn_off.setBackgroundResource(R.drawable.seeting_toggle_btn_off);
                    insurance_btn_on.setBackground(null);
                    isInsurance = false;
                } else {
                    insurance_btn_on.setText("ON");
                    insurance_btn_off.setText("");
                    insurance_btn_on.setBackgroundResource(R.drawable.toggle_btn_public);
                    insurance_btn_off.setBackground(null);
                    isInsurance = true;
                }
            }
        });

        Office_Policcy = view.findViewById(R.id.office_pollicies);
        Pre_Visting_requirments = view.findViewById(R.id.previsting_requirments);
        facebook_url = view.findViewById(R.id.facebook_url);
        email_url = view.findViewById(R.id.et_email);
        instagram_url = view.findViewById(R.id.instagram_url);
        twitter_url = view.findViewById(R.id.twitter_url);
        Time_Oprations = view.findViewById(R.id.time_opratores);

        //
        email_url.setLinkTextColor(getContext().getResources().getColor(R.color.color_link));
        instagram_url.setLinkTextColor(getContext().getResources().getColor(R.color.color_link));
        twitter_url.setLinkTextColor(getContext().getResources().getColor(R.color.color_link));
        facebook_url.setLinkTextColor(getContext().getResources().getColor(R.color.color_link));
        Website_url.setLinkTextColor(getContext().getResources().getColor(R.color.color_link));
//        email.setLinkTextColor(getContext().getResources().getColor(R.color.color_link));
        //

        Event_date = view.findViewById(R.id.event_date);

        Event_date.setText(DateConverter.getCurrentDate());
        Event_start = view.findViewById(R.id.event_start);
        Event_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SetTime(Event_start);
            }
        });
        Event_end = view.findViewById(R.id.event_end);
        Event_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SetTime(Event_end);
            }
        });

        final DatePickerDialog.OnDateSetListener mDateSetListener =
                new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        SimpleDateFormat parseFormat = new SimpleDateFormat("MM/dd");
                        Date date = new Date(year, monthOfYear, dayOfMonth);
                        String s = parseFormat.format(date);
                        Event_date.setText(s + "/" + year);

                    }
                };
        Event_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                java.util.Calendar myCalendar = java.util.Calendar.getInstance();
                new DatePickerDialog(getContext(), mDateSetListener, myCalendar
                        .get(java.util.Calendar.YEAR), myCalendar.get(java.util.Calendar.MONTH),
                        myCalendar.get(java.util.Calendar.DAY_OF_MONTH)).show();
            }
        });
        InitTypes(view);
        if (getActivity() instanceof AddNewBudzMapActivity) {
            communicationActivity = (AddNewBudzMapActivity) getActivity();
        } else {
            communicationActivity = null;
        }
        payment_method_recycler_view = view.findViewById(R.id.payment_method_selection);
        addNewPaymentMethodRecylerAdapter = new AddNewPaymentMethodRecylerAdapter(view.getContext(), paymentMethodList);
        addNewPaymentMethodRecylerAdapter.setClickListener(this);
        FlexboxLayoutManager layoutManagerPayment = new FlexboxLayoutManager(getContext());
        layoutManagerPayment.setFlexDirection(FlexDirection.ROW);
        layoutManagerPayment.setJustifyContent(JustifyContent.FLEX_START);
        payment_method_recycler_view.setLayoutManager(layoutManagerPayment);
        payment_method_recycler_view.setAdapter(addNewPaymentMethodRecylerAdapter);
        med_bay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user.getZip_code() != null) {
                    new VollyAPICall(getActivity(), true, getLocatioZipcode_part1 + "USA" + getLocatioZipcode_part2 + user.getZip_code() + getLocatioZipcode_part3, new JSONObject(), null, Request.Method.POST, new APIResponseListner() {
                        @Override
                        public void onRequestSuccess(String response, APIActions.ApiActions apiActions) {
                            JSONObject jsonObject = null;
                            try {
                                jsonObject = new JSONObject(response);
                                JSONArray jsonArray = jsonObject.getJSONArray("results");
                                int stst = -1;
                                if (jsonArray.length() > 0) {
                                    JSONObject object = jsonArray.getJSONObject(0);
                                    JSONArray array = jsonArray.getJSONObject(0).getJSONArray("address_components");
                                    for (int i = 0; i < array.length(); i++) {
                                        String name = array.getJSONObject(i).getString("long_name");
                                        for (int j = 0; j < Constants.stateList.length; j++) {
                                            if (Constants.stateList[j].equalsIgnoreCase(name)) {

                                                stst = 1;
                                            }
                                        }
                                    }
                                    if (stst == 1) {
                                        new SweetAlertDialog(getActivity(), SweetAlertDialog.NORMAL_TYPE)
                                                .setTitleText("Error!")
                                                .setContentText("You can't create this type of business in your area.")
                                                .setConfirmText("Okay!")
                                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                    @Override
                                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                        sweetAlertDialog.dismissWithAnimation();

                                                    }
                                                }).show();
                                    } else {
                                        isMedHappen = true;

                                        expandableLayout.toggle();
                                        if (communicationActivity != null) {
                                            communicationActivity.hideOptVale(false, false);
                                        }
                                    }

                                } else {
                                    new SweetAlertDialog(getActivity(), SweetAlertDialog.NORMAL_TYPE)
                                            .setTitleText("Error!")
                                            .setContentText("You can't create this type of business in your area.")
                                            .setConfirmText("Okay!")
                                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                @Override
                                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                    sweetAlertDialog.dismissWithAnimation();

                                                }
                                            }).show();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                                new SweetAlertDialog(getActivity(), SweetAlertDialog.NORMAL_TYPE)
                                        .setTitleText("Error!")
                                        .setContentText("You can't create this type of business in your area.")
                                        .setConfirmText("Okay!")
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                sweetAlertDialog.dismissWithAnimation();

                                            }
                                        }).show();
                            }
                        }

                        @Override
                        public void onRequestError(String response, APIActions.ApiActions apiActions) {
                            new SweetAlertDialog(getActivity(), SweetAlertDialog.NORMAL_TYPE)
                                    .setTitleText("Error!")
                                    .setContentText("You can't create this type of business in your area.")
                                    .setConfirmText("Okay!")
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                                            sweetAlertDialog.dismissWithAnimation();

                                        }
                                    }).show();
                        }
                    }, APIActions.ApiActions.chech_state);

//                    if (!Arrays.asList(Constants.statZipCode).contains(Integer.parseInt(user.getZip_code()))){
//                        new SweetAlertDialog(getActivity(), SweetAlertDialog.NORMAL_TYPE)
//                                .setTitleText("Error!")
//                                .setContentText("You can't create this type of business in your area.")
//                                .setConfirmText("Okay!")
//                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
//                                    @Override
//                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
//                                        sweetAlertDialog.dismissWithAnimation();
//
//                                    }
//                                }).show();
//                    }else {

//                    }

                } else {
                    isMedHappen = true;
                    expandableLayout.toggle();
                    if (communicationActivity != null) {
                        communicationActivity.hideOptVale(false, false);
                    }
                }

            }
        });
        expandableLayout.setListener(new ExpandableLayoutListener() {
            @Override
            public void onAnimationStart() {

            }

            @Override
            public void onAnimationEnd() {

            }

            @Override
            public void onPreOpen() {

            }

            @Override
            public void onPreClose() {

            }

            @Override
            public void onOpened() {

            }

            @Override
            public void onClosed() {

            }
        });
        expandableLayoutTwo.setListener(new ExpandableLayoutListener() {
            @Override
            public void onAnimationStart() {

            }

            @Override
            public void onAnimationEnd() {

            }

            @Override
            public void onPreOpen() {

            }

            @Override
            public void onPreClose() {

            }

            @Override
            public void onOpened() {

            }

            @Override
            public void onClosed() {

            }
        });
        enter_bay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isMedHappen = false;
                expandableLayoutTwo.toggle();
                if (communicationActivity != null) {
                    communicationActivity.hideOptVale(true, false);
                }

            }
        });
        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            final String[] permissions = {
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION};
            final List<String> permissionsToRequest = new ArrayList<>();
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(getContext(), permission) != PackageManager.PERMISSION_GRANTED) {
                    permissionsToRequest.add(permission);
                }
            }
            ActivityCompat.requestPermissions(getActivity(), permissionsToRequest.toArray(new String[permissionsToRequest.size()]), 12);
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        mMap.getUiSettings().setMapToolbarEnabled(false);
        mMap.setOnInfoWindowClickListener(null);
        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {

            }

            @Override
            public void onMarkerDrag(Marker marker) {
            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                latitude = marker.getPosition().latitude;
                longitude = marker.getPosition().longitude;
                Location_text.setText(GetAddressFromLatlng(getContext(), marker.getPosition().latitude, marker.getPosition().longitude));
            }
        });


        if (latitude > 0) {
            mMap.clear();
            mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(latitude, longitude))
                    .flat(true)
                    .draggable(true)
                    .icon(BitmapDescriptorFactory.fromBitmap(getBitmapFromVectorDrawable(getContext(), marker_resoures))));
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 15);
            mMap.moveCamera(cameraUpdate);
        }
//        if (!budz_map_item_clickerd_dataModel.isPending()) {
//            setDataEdit(viewSt);
//        } else {
//            new VollyAPICall(viewSt.getContext(), false, URL.get_budz_profile + "/" + budz_map_item_clickerd_dataModel.getId(), new JSONObject(), user.getSession_key(), Request.Method.GET, BusinessInfoTabFragmentEdit.this, APIActions.ApiActions.get_budz_profile_overall);
//            onClick(viewSt.findViewById(R.id.dispencry));
//        }
    }

    LinearLayout Dispensary, Medical_Practitioner, Holistic_Medicine, Clinic, Cannabites, Lounge, Cannabis_club, Event, Other;
    ImageView Dispensary_icon, Medical_Practitioner_icon, Holistic_Medicine_icon, Clinic_icon, Cannabites_icon, Lounge_icon, Cannabis_club_icon, Event_icon, Other_icon, choose_type_image;
    TextView choose_type_name;

    public void InitTypes(View view) {

        Dispensary = view.findViewById(R.id.dispencry);
        Dispensary.setOnClickListener(this);
//        Dispensary.setVisibility(View.GONE);
        Dispensary_icon = view.findViewById(R.id.dispencry_icon);
//        Dispensary_icon.setVisibility(View.GONE);
        choose_type_name = view.findViewById(R.id.choose_type_name);
        choose_type_image = view.findViewById(R.id.choose_type_image);

        Medical_Practitioner = view.findViewById(R.id.madical_practitioner);
//        Medical_Practitioner.setVisibility(View.GONE);
        Medical_Practitioner.setOnClickListener(this);
        Medical_Practitioner_icon = view.findViewById(R.id.madical_practitioner_img_icon);
//        Medical_Practitioner_icon.setVisibility(View.GONE);


        Holistic_Medicine = view.findViewById(R.id.holistic_medicine);
//        Holistic_Medicine.setVisibility(View.GONE);
        Holistic_Medicine.setOnClickListener(this);
        Holistic_Medicine_icon = view.findViewById(R.id.holistic_medicine_img_icon);
//        Holistic_Medicine_icon.setVisibility(View.GONE);


        Clinic = view.findViewById(R.id.clinic);
//        Clinic.setVisibility(View.GONE);
        Clinic.setOnClickListener(this);
        Clinic_icon = view.findViewById(R.id.clinic_img_icon);
//        Clinic_icon.setVisibility(View.GONE);


        Cannabites = view.findViewById(R.id.cannabites);
//        Cannabites.setVisibility(View.GONE);
        Cannabites.setOnClickListener(this);
        Cannabites_icon = view.findViewById(R.id.cannabites_img_icon);
//        Cannabites_icon.setVisibility(View.GONE);


        Lounge = view.findViewById(R.id.lounge);
//        Lounge.setVisibility(View.GONE);
        Lounge.setOnClickListener(this);
        Lounge_icon = view.findViewById(R.id.lounge_img_icon);
//        Lounge_icon.setVisibility(View.GONE);


        Cannabis_club = view.findViewById(R.id.cannbis_club);
//        Cannabis_club.setVisibility(View.GONE);
        Cannabis_club.setOnClickListener(this);
        Cannabis_club_icon = view.findViewById(R.id.cannbis_club_img_icon);
//        Cannabis_club_icon.setVisibility(View.GONE);


        Event = view.findViewById(R.id.event);
//        Event.setVisibility(View.GONE);
        Event.setOnClickListener(this);
        Other = view.findViewById(R.id.other);
//        Event.setVisibility(View.GONE);
        Other.setOnClickListener(this);
        Event_icon = view.findViewById(R.id.event_img_icon);
        Other_icon = view.findViewById(R.id.other_img_icon);
//        Event_icon.setVisibility(View.GONE);


    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        upload_comment_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), HBCameraActivity.class);
                intent.putExtra("isVideoCaptureAble", false);
                BusinessInfoTabFragmentEdit.this.startActivityForResult(intent, 1224);
            }
        });
        if (add_new_bud_jason_Data.has("sub_user_id") || (budz_map_item_clickerd_dataModel != null && budz_map_item_clickerd_dataModel.getIs_featured() == 1 && budz_map_item_clickerd_dataModel.getBusiness_type_id() != 9)) {
            Other.setVisibility(View.GONE);
        }
        attatchment_cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    add_new_bud_jason_Data.put("others_image", "");
                    add_new_bud_jason_Data.put("others_image", "");
//                    add_new_bud_jason_Data.remove("others_image");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                comment_image_layout.setVisibility(View.GONE);
            }
        });
        Location_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    AutocompleteFilter autocompleteFilter = new AutocompleteFilter.Builder()
                            .setTypeFilter(AutocompleteFilter.TYPE_FILTER_CITIES | TYPE_FILTER_REGIONS | AutocompleteFilter.TYPE_FILTER_ADDRESS)
                            .setCountry("US")
                            .build();
                    Intent intent =
                            new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                                    .setFilter(autocompleteFilter)
                                    .build(getActivity());
                    startActivityForResult(intent, REQUEST_PLACE_PICKER);
                } catch (GooglePlayServicesRepairableException e) {
                    // TODO: Handle the error.
                } catch (GooglePlayServicesNotAvailableException e) {
                    // TODO: Handle the error.
                }


            }
        });
    }

    void setDataEdit(View view) {
//        Description.setText(budz_map_item_clickerd_dataModel.getDescription());
        if (budz_map_item_clickerd_dataModel.getDescription() != null) {
            if (!budz_map_item_clickerd_dataModel.getDescription().equalsIgnoreCase("null")) {
                MakeKeywordClickableText(Description.getContext(), budz_map_item_clickerd_dataModel.getDescription(), Description);
            }
        }
        latitude = budz_map_item_clickerd_dataModel.getLat();
        longitude = budz_map_item_clickerd_dataModel.getLng();
        PhoneNumber.setText(budz_map_item_clickerd_dataModel.getPhone().replace("null", "").replace("(", "").replace(")", "").replace("-", ""));
        Website_url.setText(budz_map_item_clickerd_dataModel.getWeb());
        facebook_url.setText(budz_map_item_clickerd_dataModel.getFacebook());
        instagram_url.setText(budz_map_item_clickerd_dataModel.getInstagram());
        twitter_url.setText(budz_map_item_clickerd_dataModel.getTwitter());
        Location_text.setText(budz_map_item_clickerd_dataModel.getLocation());
        latitude = budz_map_item_clickerd_dataModel.getLat();
        longitude = budz_map_item_clickerd_dataModel.getLng();
        switch (budz_map_item_clickerd_dataModel.getBusiness_type_id()) {
            case 1:
                new VollyAPICall(view.getContext(), true, URL.get_budz_profile + "/" + budz_map_item_clickerd_dataModel.getId(), new JSONObject(), user.getSession_key(), Request.Method.GET, BusinessInfoTabFragmentEdit.this, APIActions.ApiActions.get_budz_profile_overall);
                onClick(view.findViewById(R.id.dispencry));
                break;
            case 2:
                new VollyAPICall(view.getContext(), true, URL.get_budz_profile + "/" + budz_map_item_clickerd_dataModel.getId(), new JSONObject(), user.getSession_key(), Request.Method.GET, BusinessInfoTabFragmentEdit.this, get_budz_profile);
                onClick(view.findViewById(R.id.madical_practitioner));
                break;
            case 6:
                new VollyAPICall(view.getContext(), true, URL.get_budz_profile + "/" + budz_map_item_clickerd_dataModel.getId(), new JSONObject(), user.getSession_key(), Request.Method.GET, BusinessInfoTabFragmentEdit.this, get_budz_profile);
                onClick(view.findViewById(R.id.holistic_medicine));
                break;
            case 7:
                new VollyAPICall(view.getContext(), true, URL.get_budz_profile + "/" + budz_map_item_clickerd_dataModel.getId(), new JSONObject(), user.getSession_key(), Request.Method.GET, BusinessInfoTabFragmentEdit.this, get_budz_profile);
                onClick(view.findViewById(R.id.clinic));
                break;

            case 3:
                new VollyAPICall(view.getContext(), true, URL.get_budz_profile + "/" + budz_map_item_clickerd_dataModel.getId(), new JSONObject(), user.getSession_key(), Request.Method.GET, BusinessInfoTabFragmentEdit.this, get_budz_profile_can);
                onClick(view.findViewById(R.id.cannabites));
                break;
            case 4:
                new VollyAPICall(view.getContext(), true, URL.get_budz_profile + "/" + budz_map_item_clickerd_dataModel.getId(), new JSONObject(), user.getSession_key(), Request.Method.GET, BusinessInfoTabFragmentEdit.this, APIActions.ApiActions.get_budz_profile_overall);
                onClick(view.findViewById(R.id.lounge));
                break;
            case 8:
                new VollyAPICall(view.getContext(), true, URL.get_budz_profile + "/" + budz_map_item_clickerd_dataModel.getId(), new JSONObject(), user.getSession_key(), Request.Method.GET, BusinessInfoTabFragmentEdit.this, APIActions.ApiActions.get_budz_profile_overall);
                onClick(view.findViewById(R.id.cannbis_club));
                break;
            case 5:
                new VollyAPICall(view.getContext(), true, URL.get_budz_profile + "/" + budz_map_item_clickerd_dataModel.getId(), new JSONObject(), user.getSession_key(), Request.Method.GET, BusinessInfoTabFragmentEdit.this, get_budz_profile_event);
                onClick(view.findViewById(R.id.event));
                break;
            case 9:
                new VollyAPICall(view.getContext(), true, URL.get_budz_profile + "/" + budz_map_item_clickerd_dataModel.getId(), new JSONObject(), user.getSession_key(), Request.Method.GET, BusinessInfoTabFragmentEdit.this, APIActions.ApiActions.other);
                onClick(view.findViewById(R.id.other));
                break;

        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.dispencry:
                Time_Oprations.setVisibility(View.VISIBLE);
                business_type_id = "1";
                marker_resoures = R.drawable.ic_dispancry_marker_icon;
                Payment_layout.setVisibility(View.VISIBLE);
                Dispensary_icon.setImageResource(R.drawable.ic_tick);
                Medical_Practitioner_icon.setImageResource(R.drawable.ic_tick_uncheck);
                Holistic_Medicine_icon.setImageResource(R.drawable.ic_tick_uncheck);
                Clinic_icon.setImageResource(R.drawable.ic_tick_uncheck);
                Cannabites_icon.setImageResource(R.drawable.ic_tick_uncheck);
                Lounge_icon.setImageResource(R.drawable.ic_tick_uncheck);
                Cannabis_club_icon.setImageResource(R.drawable.ic_tick_uncheck);
                Other_display_layout.setVisibility(View.GONE);
                other_links.setVisibility(View.VISIBLE);
                Event_icon.setImageResource(R.drawable.ic_tick_uncheck);
                Other_icon.setImageResource(R.drawable.ic_tick_uncheck);
                if (communicationActivity != null) {
                    communicationActivity.hideOptVale(false, false);
                }
                isMedHappen = false;
                if (mMap != null) {
                    mMap.clear();
                }
                if (mMap != null) {
                    mMap.clear();
                    MarkerOptions marker = new MarkerOptions()
                            .position(new LatLng(latitude, longitude))
                            .flat(true)
                            .draggable(true)
                            .icon(BitmapDescriptorFactory.fromBitmap(getBitmapFromVectorDrawable(getContext(), marker_resoures)));
                    mMap.addMarker(marker);
                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 15);
                    mMap.moveCamera(cameraUpdate);
                }
                Medical_views.setVisibility(View.GONE);
                Event_Layout.setVisibility(View.GONE);
                break;
            case R.id.madical_practitioner:
                Time_Oprations.setVisibility(View.VISIBLE);
                Medical_views.setVisibility(View.VISIBLE);
                Event_Layout.setVisibility(View.GONE);
                business_type_id = "2";
                marker_resoures = R.drawable.ic_medical_marker_icon;
                Dispensary_icon.setImageResource(R.drawable.ic_tick_uncheck);
                Medical_Practitioner_icon.setImageResource(R.drawable.ic_tick);
                Holistic_Medicine_icon.setImageResource(R.drawable.ic_tick_uncheck);
                Clinic_icon.setImageResource(R.drawable.ic_tick_uncheck);
                Cannabites_icon.setImageResource(R.drawable.ic_tick_uncheck);
                Payment_layout.setVisibility(View.VISIBLE);
                Lounge_icon.setImageResource(R.drawable.ic_tick_uncheck);
                if (communicationActivity != null) {
                    communicationActivity.hideOptVale(false, false);
                }
                if (budz_map_item_clickerd_dataModel != null && !budz_map_item_clickerd_dataModel.isPending()) {
                    if (mMap != null) {
                        mMap.clear();
                        MarkerOptions marker = new MarkerOptions()
                                .position(new LatLng(latitude, longitude))
                                .flat(true)
                                .draggable(true)
                                .icon(BitmapDescriptorFactory.fromBitmap(getBitmapFromVectorDrawable(getContext(), marker_resoures)));
                        mMap.addMarker(marker);
                        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 15);
                        mMap.moveCamera(cameraUpdate);
                    }
                }
                Other_display_layout.setVisibility(View.GONE);
                other_links.setVisibility(View.VISIBLE);
                isMedHappen = true;
                Cannabis_club_icon.setImageResource(R.drawable.ic_tick_uncheck);
                Event_icon.setImageResource(R.drawable.ic_tick_uncheck);
                Other_icon.setImageResource(R.drawable.ic_tick_uncheck);
                break;

            case R.id.holistic_medicine:
                Time_Oprations.setVisibility(View.VISIBLE);
                Medical_views.setVisibility(View.VISIBLE);
                Event_Layout.setVisibility(View.GONE);
                business_type_id = "6";
                marker_resoures = R.drawable.ic_medical_marker_icon;
                Dispensary_icon.setImageResource(R.drawable.ic_tick_uncheck);
                Medical_Practitioner_icon.setImageResource(R.drawable.ic_tick_uncheck);
                Holistic_Medicine_icon.setImageResource(R.drawable.ic_tick);
                Clinic_icon.setImageResource(R.drawable.ic_tick_uncheck);
                Cannabites_icon.setImageResource(R.drawable.ic_tick_uncheck);
                Lounge_icon.setImageResource(R.drawable.ic_tick_uncheck);
                if (communicationActivity != null) {
                    communicationActivity.hideOptVale(false, false);
                }
                isMedHappen = true;
                if (budz_map_item_clickerd_dataModel != null && !budz_map_item_clickerd_dataModel.isPending()) {
                    if (mMap != null) {
                        mMap.clear();
                        MarkerOptions marker = new MarkerOptions()
                                .position(new LatLng(latitude, longitude))
                                .flat(true)
                                .draggable(true)
                                .icon(BitmapDescriptorFactory.fromBitmap(getBitmapFromVectorDrawable(getContext(), marker_resoures)));
                        mMap.addMarker(marker);
                        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 15);
                        mMap.moveCamera(cameraUpdate);
                    }
                }
                Payment_layout.setVisibility(View.VISIBLE);
                Other_display_layout.setVisibility(View.GONE);
                other_links.setVisibility(View.VISIBLE);
                Cannabis_club_icon.setImageResource(R.drawable.ic_tick_uncheck);
                Event_icon.setImageResource(R.drawable.ic_tick_uncheck);
                Other_icon.setImageResource(R.drawable.ic_tick_uncheck);
                break;

            case R.id.clinic:
                Time_Oprations.setVisibility(View.VISIBLE);
                Medical_views.setVisibility(View.VISIBLE);
                Event_Layout.setVisibility(View.GONE);
                business_type_id = "7";
                marker_resoures = R.drawable.ic_medical_marker_icon;
                Dispensary_icon.setImageResource(R.drawable.ic_tick_uncheck);
                Medical_Practitioner_icon.setImageResource(R.drawable.ic_tick_uncheck);
                Holistic_Medicine_icon.setImageResource(R.drawable.ic_tick_uncheck);
                Clinic_icon.setImageResource(R.drawable.ic_tick);
                Cannabites_icon.setImageResource(R.drawable.ic_tick_uncheck);
                if (communicationActivity != null) {
                    communicationActivity.hideOptVale(false, false);
                }
                isMedHappen = true;
                if (budz_map_item_clickerd_dataModel != null && !budz_map_item_clickerd_dataModel.isPending()) {
                    if (mMap != null) {
                        mMap.clear();
                        MarkerOptions marker = new MarkerOptions()
                                .position(new LatLng(latitude, longitude))
                                .flat(true)
                                .draggable(true)
                                .icon(BitmapDescriptorFactory.fromBitmap(getBitmapFromVectorDrawable(getContext(), marker_resoures)));
                        mMap.addMarker(marker);
                        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 15);
                        mMap.moveCamera(cameraUpdate);
                    }
                }
                Payment_layout.setVisibility(View.VISIBLE);
                Lounge_icon.setImageResource(R.drawable.ic_tick_uncheck);
                Other_display_layout.setVisibility(View.GONE);
                other_links.setVisibility(View.VISIBLE);
                Cannabis_club_icon.setImageResource(R.drawable.ic_tick_uncheck);
                Event_icon.setImageResource(R.drawable.ic_tick_uncheck);
                Other_icon.setImageResource(R.drawable.ic_tick_uncheck);
                break;

            case R.id.cannabites:
                Time_Oprations.setVisibility(View.VISIBLE);
                if (communicationActivity != null) {
                    communicationActivity.hideOptVale(false, false);
                }
                if (mMap != null) {
                    mMap.clear();
                }
                isMedHappen = false;

                Medical_views.setVisibility(View.GONE);
                Event_Layout.setVisibility(View.GONE);
                business_type_id = "3";
                marker_resoures = R.drawable.ic_cannabites_marker_icon;
                if (mMap != null) {
                    mMap.clear();
                    MarkerOptions marker = new MarkerOptions()
                            .position(new LatLng(latitude, longitude))
                            .flat(true)
                            .draggable(true)
                            .icon(BitmapDescriptorFactory.fromBitmap(getBitmapFromVectorDrawable(getContext(), marker_resoures)));
                    mMap.addMarker(marker);
                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 15);
                    mMap.moveCamera(cameraUpdate);
                }
                Dispensary_icon.setImageResource(R.drawable.ic_tick_uncheck);
                Medical_Practitioner_icon.setImageResource(R.drawable.ic_tick_uncheck);
                Holistic_Medicine_icon.setImageResource(R.drawable.ic_tick_uncheck);
                Clinic_icon.setImageResource(R.drawable.ic_tick_uncheck);
                Cannabites_icon.setImageResource(R.drawable.ic_tick);
                Payment_layout.setVisibility(View.VISIBLE);
                Lounge_icon.setImageResource(R.drawable.ic_tick_uncheck);
                Other_display_layout.setVisibility(View.GONE);
                other_links.setVisibility(View.VISIBLE);
                Cannabis_club_icon.setImageResource(R.drawable.ic_tick_uncheck);
                Event_icon.setImageResource(R.drawable.ic_tick_uncheck);
                Other_icon.setImageResource(R.drawable.ic_tick_uncheck);
                break;

            case R.id.lounge:
                Time_Oprations.setVisibility(View.VISIBLE);
                Medical_views.setVisibility(View.GONE);
                Event_Layout.setVisibility(View.GONE);
                if (communicationActivity != null) {
                    communicationActivity.hideOptVale(true, false);
                }
                isMedHappen = false;
                if (mMap != null) {
                    mMap.clear();
                }
                if (mMap != null) {
                    mMap.clear();
                    MarkerOptions marker = new MarkerOptions()
                            .position(new LatLng(latitude, longitude))
                            .flat(true)
                            .draggable(true)
                            .icon(BitmapDescriptorFactory.fromBitmap(getBitmapFromVectorDrawable(getContext(), marker_resoures)));
                    mMap.addMarker(marker);
                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 15);
                    mMap.moveCamera(cameraUpdate);
                }
                business_type_id = "4";
                marker_resoures = R.drawable.ic_entertainment_marker_icn;
                Dispensary_icon.setImageResource(R.drawable.ic_tick_uncheck);
                Medical_Practitioner_icon.setImageResource(R.drawable.ic_tick_uncheck);
                Holistic_Medicine_icon.setImageResource(R.drawable.ic_tick_uncheck);
                Clinic_icon.setImageResource(R.drawable.ic_tick_uncheck);
                Cannabites_icon.setImageResource(R.drawable.ic_tick_uncheck);
                Lounge_icon.setImageResource(R.drawable.ic_tick);
                Payment_layout.setVisibility(View.VISIBLE);
                Cannabis_club_icon.setImageResource(R.drawable.ic_tick_uncheck);
                Event_icon.setImageResource(R.drawable.ic_tick_uncheck);
                Other_display_layout.setVisibility(View.GONE);
                other_links.setVisibility(View.VISIBLE);
                Other_icon.setImageResource(R.drawable.ic_tick_uncheck);
                break;

            case R.id.cannbis_club:
                Time_Oprations.setVisibility(View.VISIBLE);
                Medical_views.setVisibility(View.GONE);
                Event_Layout.setVisibility(View.GONE);
                business_type_id = "8";
                if (communicationActivity != null) {
                    communicationActivity.hideOptVale(true, false);
                }
                isMedHappen = false;
                if (mMap != null) {
                    mMap.clear();
                }
                if (mMap != null) {
                    mMap.clear();
                    MarkerOptions marker = new MarkerOptions()
                            .position(new LatLng(latitude, longitude))
                            .flat(true)
                            .draggable(true)
                            .icon(BitmapDescriptorFactory.fromBitmap(getBitmapFromVectorDrawable(getContext(), marker_resoures)));
                    mMap.addMarker(marker);
                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 15);
                    mMap.moveCamera(cameraUpdate);
                }
                marker_resoures = R.drawable.ic_entertainment_marker_icn;
                Dispensary_icon.setImageResource(R.drawable.ic_tick_uncheck);
                Medical_Practitioner_icon.setImageResource(R.drawable.ic_tick_uncheck);
                Holistic_Medicine_icon.setImageResource(R.drawable.ic_tick_uncheck);
                Clinic_icon.setImageResource(R.drawable.ic_tick_uncheck);
                Cannabites_icon.setImageResource(R.drawable.ic_tick_uncheck);
                Lounge_icon.setImageResource(R.drawable.ic_tick_uncheck);
                Cannabis_club_icon.setImageResource(R.drawable.ic_tick);
                Event_icon.setImageResource(R.drawable.ic_tick_uncheck);
                Other_display_layout.setVisibility(View.GONE);
                other_links.setVisibility(View.VISIBLE);
                Other_icon.setImageResource(R.drawable.ic_tick_uncheck);
                Payment_layout.setVisibility(View.VISIBLE);
                break;

            case R.id.event:
                if (communicationActivity != null) {
                    communicationActivity.hideOptVale(true, false);
                }
                if (mMap != null) {
                    mMap.clear();
                }
                isMedHappen = false;

                Time_Oprations.setVisibility(View.GONE);
                Medical_views.setVisibility(View.GONE);
                Event_Layout.setVisibility(View.VISIBLE);
                business_type_id = "5";
                marker_resoures = R.drawable.ic_events_marker_icon;
                Dispensary_icon.setImageResource(R.drawable.ic_tick_uncheck);
                Medical_Practitioner_icon.setImageResource(R.drawable.ic_tick_uncheck);
                Holistic_Medicine_icon.setImageResource(R.drawable.ic_tick_uncheck);
                Clinic_icon.setImageResource(R.drawable.ic_tick_uncheck);
                Cannabites_icon.setImageResource(R.drawable.ic_tick_uncheck);
                Lounge_icon.setImageResource(R.drawable.ic_tick_uncheck);
                Cannabis_club_icon.setImageResource(R.drawable.ic_tick_uncheck);
                Event_icon.setImageResource(R.drawable.ic_tick);
                Payment_layout.setVisibility(View.VISIBLE);
                Other_display_layout.setVisibility(View.GONE);
                other_links.setVisibility(View.VISIBLE);
                if (mMap != null) {
                    mMap.clear();
                    MarkerOptions marker = new MarkerOptions()
                            .position(new LatLng(latitude, longitude))
                            .flat(true)
                            .draggable(true)
                            .icon(BitmapDescriptorFactory.fromBitmap(getBitmapFromVectorDrawable(getContext(), marker_resoures)));
                    mMap.addMarker(marker);
                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 15);
                    mMap.moveCamera(cameraUpdate);
                }
                Other_icon.setImageResource(R.drawable.ic_tick_uncheck);
                break;
            case R.id.other:
                if (communicationActivity != null) {
                    communicationActivity.hideOptVale(true, true);
                }
                if (mMap != null) {
                    mMap.clear();
                }
                isMedHappen = false;

                Time_Oprations.setVisibility(View.GONE);
                Medical_views.setVisibility(View.GONE);
                Event_Layout.setVisibility(View.GONE);
                Payment_layout.setVisibility(View.GONE);
                Other_display_layout.setVisibility(View.VISIBLE);
                other_links.setVisibility(View.GONE);
                business_type_id = "9";
                marker_resoures = R.drawable.ic_other_marker_icon;
                Dispensary_icon.setImageResource(R.drawable.ic_tick_uncheck);
                Medical_Practitioner_icon.setImageResource(R.drawable.ic_tick_uncheck);
                Holistic_Medicine_icon.setImageResource(R.drawable.ic_tick_uncheck);
                Clinic_icon.setImageResource(R.drawable.ic_tick_uncheck);
                Cannabites_icon.setImageResource(R.drawable.ic_tick_uncheck);
                Lounge_icon.setImageResource(R.drawable.ic_tick_uncheck);
                Cannabis_club_icon.setImageResource(R.drawable.ic_tick_uncheck);
                Event_icon.setImageResource(R.drawable.ic_tick_uncheck);
                Other_icon.setImageResource(R.drawable.ic_tick);
                if (mMap != null) {
                    mMap.clear();
                    MarkerOptions marker = new MarkerOptions()
                            .position(new LatLng(latitude, longitude))
                            .flat(true)
                            .draggable(true)
                            .icon(BitmapDescriptorFactory.fromBitmap(getBitmapFromVectorDrawable(getContext(), marker_resoures)));
                    mMap.addMarker(marker);
                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 15);
                    mMap.moveCamera(cameraUpdate);
                }
                break;
        }


    }

    @Override
    public void onLocationSuccess(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();

        if (mMap != null) {
            mMap.clear();
            Bitmap bitmap = getBitmapFromVectorDrawable(getContext(), marker_resoures);
            if (bitmap != null) {
                mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(latitude, longitude))
                        .flat(true)
                        .draggable(true)
                        .icon(BitmapDescriptorFactory.fromBitmap(bitmap)));
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 15);
                mMap.moveCamera(cameraUpdate);
            }
        }
        Location_text.setText(GetAddressFromLatlng(getContext(), location.getLatitude(), location.getLongitude()));
    }

    @Override
    public void onLocationFailed(String Error) {

    }

    public void submitData() {
        //Description.getText().length() > 0 &&
        if (CheckState()) {
            try {
                if (budz_map_item_clickerd_dataModel_abc != null) {
                    add_new_bud_jason_Data.put("sub_user_id", budz_map_item_clickerd_dataModel_abc.getId());
                }
                add_new_bud_jason_Data.put("location", Location_text.getText().toString().trim());
                add_new_bud_jason_Data.put("lat", latitude);
                add_new_bud_jason_Data.put("zip_code", et_zip_code.getText().toString().trim());
                add_new_bud_jason_Data.put("lng", longitude);
                add_new_bud_jason_Data.put("business_type_id", business_type_id);
                add_new_bud_jason_Data.put("description", Description.getText().toString().trim());
                add_new_bud_jason_Data.put("phone", PhoneNumber.getText().toString().trim());
                add_new_bud_jason_Data.put("web", Website_url.getText().toString().trim());
                add_new_bud_jason_Data.put("fb", facebook_url.getText().toString().trim());
                add_new_bud_jason_Data.put("twitter", twitter_url.getText().toString().trim());
                add_new_bud_jason_Data.put("instagram", instagram_url.getText().toString().trim());
                add_new_bud_jason_Data.put("email", email_url.getText().toString().trim());
                if (business_type_id.equalsIgnoreCase("4") || business_type_id.equalsIgnoreCase("8") || business_type_id.equalsIgnoreCase("5")) {

                    add_new_bud_jason_Data.put("is_organic", 0);
                    add_new_bud_jason_Data.put("is_delivery", 0);
                }
                if (business_type_id.equalsIgnoreCase("2") || business_type_id.equalsIgnoreCase("7") || business_type_id.equalsIgnoreCase("6")) {
                    if (Selected_languages_data.size() > 0) {
                        String Lang = "";
                        for (int x = 0; x < Selected_languages_data.size(); x++) {
                            if (Lang.length() == 0) {
                                Lang = Lang + getLanguageID(Selected_languages_data.get(x));
                            } else {
                                Lang = Lang + "," + getLanguageID(Selected_languages_data.get(x));
                            }
                        }
                        add_new_bud_jason_Data.put("langeages", Lang);
                    } else {
                        add_new_bud_jason_Data.put("langeages", "");
                    }

                    if (isInsurance) {
                        add_new_bud_jason_Data.put("insurance_accepted", "Yes");
                    } else {
                        add_new_bud_jason_Data.put("insurance_accepted", "No");
                    }
                    add_new_bud_jason_Data.put("office_policies", Office_Policcy.getText().toString());
                    add_new_bud_jason_Data.put("visit_requirements", Pre_Visting_requirments.getText().toString());
                }

                if (paymentMethodList.size() > 0) {
                    String payment = "";
                    for (int x = 0; x < paymentMethodList.size(); x++) {
                        if (paymentMethodList.get(x).isSelected()) {
                            if (payment.length() == 0) {
                                payment = payment + paymentMethodList.get(x).getId();
                            } else {
                                payment = payment + "," + paymentMethodList.get(x).getId();
                            }
                        }
                    }
                    add_new_bud_jason_Data.put("payments", payment);
                } else {
                    add_new_bud_jason_Data.put("payments", "");
                }

                if (business_type_id.equalsIgnoreCase("5")) {
                    Date dateeStart = DateConverter.getDateTime("hh:mm a", Event_start.getText().toString());
                    Date dateEnd = DateConverter.getDateTime("hh:mm a", Event_end.getText().toString());
                    if (checkTimeSetting(dateeStart, dateEnd, "Event")) {

                        return;
                    }
                    add_new_bud_jason_Data.put("date", Event_date.getText().toString().trim());
                    add_new_bud_jason_Data.put("from", Event_start.getText().toString());
                    add_new_bud_jason_Data.put("to", Event_end.getText().toString());
                } else {
                    if (isMondayOpen) {
                        Date dateeStart = DateConverter.getDateTime("hh:mm a", Monday_start.getText().toString());
                        Date dateEnd = DateConverter.getDateTime("hh:mm a", Monday_end.getText().toString());
                        if (checkTimeSetting(dateeStart, dateEnd, "Monday")) {

                            return;
                        }
                        add_new_bud_jason_Data.put("mon_start", Monday_start.getText().toString());
                        add_new_bud_jason_Data.put("mon_end", Monday_end.getText().toString());
                    } else {
                        add_new_bud_jason_Data.put("mon_start", "Closed");
                        add_new_bud_jason_Data.put("mon_end", "nan");
                    }

                    if (isTuesdayOpen) {
                        Date dateeStart = DateConverter.getDateTime("hh:mm a", Tuesday_start.getText().toString());
                        Date dateEnd = DateConverter.getDateTime("hh:mm a", Tuesday_end.getText().toString());
                        if (checkTimeSetting(dateeStart, dateEnd, "Tuesday")) {

                            return;
                        }
                        add_new_bud_jason_Data.put("tue_start", Tuesday_start.getText().toString());
                        add_new_bud_jason_Data.put("tue_end", Tuesday_end.getText().toString());
                    } else {
                        add_new_bud_jason_Data.put("tue_start", "Closed");
                        add_new_bud_jason_Data.put("tue_end", "nan");
                    }


                    if (isWednesdayOpen) {
                        Date dateeStart = DateConverter.getDateTime("hh:mm a", Wednesday_start.getText().toString());
                        Date dateEnd = DateConverter.getDateTime("hh:mm a", Wednesday_end.getText().toString());
                        if (checkTimeSetting(dateeStart, dateEnd, "Wednesday")) {

                            return;
                        }
                        add_new_bud_jason_Data.put("wed_start", Wednesday_start.getText().toString());
                        add_new_bud_jason_Data.put("wed_end", Wednesday_end.getText().toString());
                    } else {
                        add_new_bud_jason_Data.put("wed_start", "Closed");
                        add_new_bud_jason_Data.put("wed_end", "nan");
                    }


                    if (isThursdayOpen) {
                        Date dateeStart = DateConverter.getDateTime("hh:mm a", Thursday_start.getText().toString());
                        Date dateEnd = DateConverter.getDateTime("hh:mm a", Thursday_end.getText().toString());
                        if (checkTimeSetting(dateeStart, dateEnd, "Thursday")) {

                            return;
                        }
                        add_new_bud_jason_Data.put("thu_start", Thursday_start.getText().toString());
                        add_new_bud_jason_Data.put("thu_end", Thursday_end.getText().toString());
                    } else {
                        add_new_bud_jason_Data.put("thu_start", "Closed");
                        add_new_bud_jason_Data.put("thu_end", "nan");
                    }


                    if (isFridayOpen) {
                        Date dateeStart = DateConverter.getDateTime("hh:mm a", Friday_start.getText().toString());
                        Date dateEnd = DateConverter.getDateTime("hh:mm a", Friday_end.getText().toString());
                        if (checkTimeSetting(dateeStart, dateEnd, "Friday")) {

                            return;
                        }
                        add_new_bud_jason_Data.put("fri_start", Friday_start.getText().toString());
                        add_new_bud_jason_Data.put("fri_end", Friday_end.getText().toString());
                    } else {
                        add_new_bud_jason_Data.put("fri_start", "Closed");
                        add_new_bud_jason_Data.put("fri_end", "nan");
                    }

                    if (isSaturdayOpen) {
                        Date dateeStart = DateConverter.getDateTime("hh:mm a", Saturday_start.getText().toString());
                        Date dateEnd = DateConverter.getDateTime("hh:mm a", Saturday_end.getText().toString());
                        if (checkTimeSetting(dateeStart, dateEnd, "Saturday")) {

                            return;
                        }
                        add_new_bud_jason_Data.put("sat_start", Saturday_start.getText().toString());
                        add_new_bud_jason_Data.put("sat_end", Saturday_end.getText().toString());
                    } else {
                        add_new_bud_jason_Data.put("sat_start", "Closed");
                        add_new_bud_jason_Data.put("sat_end", "nan");
                    }


                    if (isSundayOpen) {
                        Date dateeStart = DateConverter.getDateTime("hh:mm a", Sunday_start.getText().toString());
                        Date dateEnd = DateConverter.getDateTime("hh:mm a", Sunday_end.getText().toString());
                        if (checkTimeSetting(dateeStart, dateEnd, "Sunday")) {
                            return;
                        }
                        add_new_bud_jason_Data.put("sun_start", Sunday_start.getText().toString());
                        add_new_bud_jason_Data.put("sun_end", Sunday_end.getText().toString());
                    } else {
                        add_new_bud_jason_Data.put("sun_start", "Closed");
                        add_new_bud_jason_Data.put("sun_end", "nan");
                    }
                }
                new VollyAPICall(getContext(), true, URL.add_listing, add_new_bud_jason_Data, user.getSession_key(), Request.Method.POST, BusinessInfoTabFragmentEdit.this, APIActions.ApiActions.add_listing);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
//            CustomeToast.ShowCustomToast(HealingBudApplication.getContext(), "Description and Title Fields are Required!", Gravity.CENTER);
        }
    }

    public boolean checkTimeSetting(Date start, Date end, String dayName) {
        if (start.after(end) || start.getTime() == end.getTime()) {
            CustomeToast.ShowCustomToast(getContext(), dayName + " end time must after then start time", Gravity.TOP);
            return true;
        }
        return false;
    }

    public void setTiming(View view) {

        //monday
        Monday_Tcik = view.findViewById(R.id.mon_tick);
        Monday_Open = view.findViewById(R.id.mon_open);
        Monday_Close = view.findViewById(R.id.mon_close);
        Monday_end = view.findViewById(R.id.monday_end);
        Monday_start = view.findViewById(R.id.monday_start);

        Monday_Tcik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isMondayOpen) {
                    Monday_Tcik.setImageResource(R.drawable.ic_cross_icon);
                    Monday_Open.setVisibility(View.GONE);
                    Monday_Close.setVisibility(View.VISIBLE);
                    isMondayOpen = false;
                } else {
                    Monday_Tcik.setImageResource(R.drawable.ic_tick);
                    Monday_Open.setVisibility(View.VISIBLE);
                    Monday_Close.setVisibility(View.GONE);
                    isMondayOpen = true;
                }
            }
        });

        Monday_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SetTime(Monday_start);
            }
        });


        Monday_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SetTime(Monday_end);
            }
        });


        //Tuesday
        Tuesday_Tcik = view.findViewById(R.id.tue_tick);
        Tuesday_Open = view.findViewById(R.id.tue_open);
        Tuesday_Close = view.findViewById(R.id.tue_close);
        Tuesday_end = view.findViewById(R.id.tue_end);
        Tuesday_start = view.findViewById(R.id.tue_start);

        Tuesday_Tcik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isTuesdayOpen) {
                    Tuesday_Tcik.setImageResource(R.drawable.ic_cross_icon);
                    Tuesday_Open.setVisibility(View.GONE);
                    Tuesday_Close.setVisibility(View.VISIBLE);
                    isTuesdayOpen = false;
                } else {
                    Tuesday_Tcik.setImageResource(R.drawable.ic_tick);
                    Tuesday_Open.setVisibility(View.VISIBLE);
                    Tuesday_Close.setVisibility(View.GONE);
                    isTuesdayOpen = true;
                }
            }
        });

        Tuesday_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SetTime(Tuesday_start);
            }
        });


        Tuesday_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SetTime(Tuesday_end);
            }
        });


        //Wednesday
        Wednesday_Tcik = view.findViewById(R.id.wed_tick);
        Wednesday_Open = view.findViewById(R.id.wed_open);
        Wednesday_Close = view.findViewById(R.id.wed_close);
        Wednesday_end = view.findViewById(R.id.wed_end);
        Wednesday_start = view.findViewById(R.id.wed_start);

        Wednesday_Tcik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isWednesdayOpen) {
                    Wednesday_Tcik.setImageResource(R.drawable.ic_cross_icon);
                    Wednesday_Open.setVisibility(View.GONE);
                    Wednesday_Close.setVisibility(View.VISIBLE);
                    isWednesdayOpen = false;
                } else {
                    Wednesday_Tcik.setImageResource(R.drawable.ic_tick);
                    Wednesday_Open.setVisibility(View.VISIBLE);
                    Wednesday_Close.setVisibility(View.GONE);
                    isWednesdayOpen = true;
                }
            }
        });

        Wednesday_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SetTime(Wednesday_start);
            }
        });


        Wednesday_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SetTime(Wednesday_end);
            }
        });


        //Thursday
        Thursday_Tcik = view.findViewById(R.id.thu_tick);
        Thursday_Open = view.findViewById(R.id.thu_open);
        Thursday_Close = view.findViewById(R.id.thu_close);
        Thursday_end = view.findViewById(R.id.thu_end);
        Thursday_start = view.findViewById(R.id.thu_start);

        Thursday_Tcik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isThursdayOpen) {
                    Thursday_Tcik.setImageResource(R.drawable.ic_cross_icon);
                    Thursday_Open.setVisibility(View.GONE);
                    Thursday_Close.setVisibility(View.VISIBLE);
                    isThursdayOpen = false;
                } else {
                    Thursday_Tcik.setImageResource(R.drawable.ic_tick);
                    Thursday_Open.setVisibility(View.VISIBLE);
                    Thursday_Close.setVisibility(View.GONE);
                    isThursdayOpen = true;
                }
            }
        });

        Thursday_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SetTime(Thursday_start);
            }
        });


        Thursday_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SetTime(Thursday_end);
            }
        });


        //Friday
        Friday_Tcik = view.findViewById(R.id.fri_tick);
        Friday_Open = view.findViewById(R.id.fri_open);
        Friday_Close = view.findViewById(R.id.fri_close);
        Friday_end = view.findViewById(R.id.fri_end);
        Friday_start = view.findViewById(R.id.fri_start);

        Friday_Tcik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isFridayOpen) {
                    Friday_Tcik.setImageResource(R.drawable.ic_cross_icon);
                    Friday_Open.setVisibility(View.GONE);
                    Friday_Close.setVisibility(View.VISIBLE);
                    isFridayOpen = false;
                } else {
                    Friday_Tcik.setImageResource(R.drawable.ic_tick);
                    Friday_Open.setVisibility(View.VISIBLE);
                    Friday_Close.setVisibility(View.GONE);
                    isFridayOpen = true;
                }
            }
        });

        Friday_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SetTime(Friday_start);
            }
        });


        Friday_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SetTime(Friday_end);
            }
        });


        //Saturday
        Saturday_Tcik = view.findViewById(R.id.sat_tick);
        Saturday_Open = view.findViewById(R.id.sat_open);
        Saturday_Close = view.findViewById(R.id.sat_close);
        Saturday_end = view.findViewById(R.id.sat_end);
        Saturday_start = view.findViewById(R.id.sat_start);

        Saturday_Tcik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isSaturdayOpen) {
                    Saturday_Tcik.setImageResource(R.drawable.ic_cross_icon);
                    Saturday_Open.setVisibility(View.GONE);
                    Saturday_Close.setVisibility(View.VISIBLE);
                    isSaturdayOpen = false;
                } else {
                    Saturday_Tcik.setImageResource(R.drawable.ic_tick);
                    Saturday_Open.setVisibility(View.VISIBLE);
                    Saturday_Close.setVisibility(View.GONE);
                    isSaturdayOpen = true;
                }
            }
        });

        Saturday_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SetTime(Saturday_start);
            }
        });


        Saturday_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SetTime(Saturday_end);
            }
        });


        //Sunday
        Sunday_Tcik = view.findViewById(R.id.sun_tick);
        Sunday_Open = view.findViewById(R.id.sun_open);
        Sunday_Close = view.findViewById(R.id.sun_close);
        Sunday_end = view.findViewById(R.id.sun_end);
        Sunday_start = view.findViewById(R.id.sun_start);

        Sunday_Tcik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isSundayOpen) {
                    Sunday_Tcik.setImageResource(R.drawable.ic_cross_icon);
                    Sunday_Open.setVisibility(View.GONE);
                    Sunday_Close.setVisibility(View.VISIBLE);
                    isSundayOpen = false;
                } else {
                    Sunday_Tcik.setImageResource(R.drawable.ic_tick);
                    Sunday_Open.setVisibility(View.VISIBLE);
                    Sunday_Close.setVisibility(View.GONE);
                    isSundayOpen = true;
                }
            }
        });

        Sunday_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SetTime(Sunday_start);
            }
        });


        Sunday_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SetTime(Sunday_end);
            }
        });


        Monday = view.findViewById(R.id.monday);
        Tuesday = view.findViewById(R.id.tuesday);
        Wednesday = view.findViewById(R.id.wednesday);
        Thursday = view.findViewById(R.id.thursday);
        Friday = view.findViewById(R.id.friday);
        Saturday = view.findViewById(R.id.saturday);
        Sunday = view.findViewById(R.id.sunday);

//        switch (getCurrentDat()) {
//            case "sunday":
//                Sunday.setText("Today");
//                Sunday.setTextColor(Color.parseColor("#FF851C7A"));
//                break;
//            case "monday":
//                Monday.setText("Today");
//                Monday.setTextColor(Color.parseColor("#FF851C7A"));
//                break;
//            case "tuesday":
//                Tuesday.setText("Today");
//                Tuesday.setTextColor(Color.parseColor("#FF851C7A"));
//                break;
//            case "wednesday":
//                Wednesday.setText("Today");
//                Wednesday.setTextColor(Color.parseColor("#FF851C7A"));
//
//                break;
//            case "thursday":
//                Thursday.setText("Today");
//                Thursday.setTextColor(Color.parseColor("#FF851C7A"));
//                break;
//            case "friday":
//                Friday.setText("Today");
//                Friday.setTextColor(Color.parseColor("#FF851C7A"));
//                break;
//            case "saturday":
//                Saturday.setText("Today");
//                Saturday.setTextColor(Color.parseColor("#FF851C7A"));
//                break;
//
//        }
    }


    public int getLanguageID(String name) {
        int id = -1;
        for (int x = 0; x < languages_main_Data.size(); x++) {
            if (languages_main_Data.get(x).getName().equalsIgnoreCase(name)) {
                id = languages_main_Data.get(x).getId();
                break;
            }
        }
        return id;
    }

    @TargetApi(Build.VERSION_CODES.N)
    public void SetTime(final Button button) {
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
//               Date datee =  DateConverter.getDateTime();
                if (selectedHour >= 12) {
                    int cl_hours = selectedHour % 12;
                    if (cl_hours == 0) {
                        cl_hours = 12;
                    }
                    if (selectedMinute <= 9) {
                        button.setText(cl_hours + ":0" + selectedMinute + " PM");
                    } else {
                        button.setText(cl_hours + ":" + selectedMinute + " PM");
                    }
                } else {
                    int cl_hours = selectedHour;
                    if (cl_hours == 0) {
                        cl_hours = 12;
                    }
                    if (selectedMinute <= 9) {
                        button.setText(cl_hours + ":0" + selectedMinute + " AM");
                    } else {
                        button.setText(cl_hours + ":" + selectedMinute + " AM");
                    }
                }
            }
        }, hour, minute, false);
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }

    ArrayAdapter<String> adapter;

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public void onRequestSuccess(String response, APIActions.ApiActions apiActions) {
        Log.d("response", response);
        //paymant_methods

        if (apiActions == get_languages) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONArray jsonArray = jsonObject.getJSONObject("successData").getJSONArray("languages");
                paymentMethodList.clear();
                paymentMethodList.addAll(Arrays.asList(new Gson().fromJson(jsonObject.getJSONObject("successData").getJSONArray("payment_methods").toString(), PaymentMethod[].class)));
                for (int x = 0; x < jsonArray.length(); x++) {
                    JSONObject object = jsonArray.getJSONObject(x);
                    Language language = new Language();
                    language.setId(object.getInt("id"));
                    language.setName(object.getString("name"));
                    languages.add(object.getString("name"));
                    languages_main_Data.add(language);
                }
                Languages.setThreshold(1);
                //TODO
                adapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_dropdown_item_1line, languages);
                Languages.setAdapter(adapter);
                addNewPaymentMethodRecylerAdapter.notifyDataSetChanged();
                if (budz_map_item_clickerd_dataModel != null) {
                    Submit_data.setText("UPDATE LISTING");
                    if (!budz_map_item_clickerd_dataModel.isPending()) {
                        setDataEdit(viewSt);
                    } else {
                        new VollyAPICall(viewSt.getContext(), true, URL.get_budz_profile + "/" + budz_map_item_clickerd_dataModel.getId(), new JSONObject(), user.getSession_key(), Request.Method.GET, BusinessInfoTabFragmentEdit.this, APIActions.ApiActions.get_budz_profile_overall);
                        onClick(viewSt.findViewById(R.id.dispencry));
                    }
                } else {
                    Submit_data.setText("ADD LISTING");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (apiActions == get_budz_profile) {
            JSONObject jsonObject = null;
            try {
                ArrayList<BudzMapReviews> reviews = new ArrayList<>();
                jsonObject = new JSONObject(response);

                JSONObject object = jsonObject.getJSONObject("successData");
                if (!object.isNull("paymant_methods")) {
                    JSONArray lng = jsonObject.getJSONObject("successData").getJSONArray("paymant_methods");
                    for (int l = 0; l < lng.length(); l++) {
                        if (!lng.getJSONObject(l).isNull("payment_id")) {
                            for (PaymentMethod item : paymentMethodList) {
                                if (item.getId() == lng.getJSONObject(l).getInt("payment_id")) {
                                    item.setSelected(true);
                                }
                            }
                        }
                    }
                }
                addNewPaymentMethodRecylerAdapter.notifyDataSetChanged();
                if (!object.isNull("insurance_accepted")) {
                    if (object.getString("insurance_accepted").equalsIgnoreCase("No")) {
                        insurance_btn_on.setText("");
                        insurance_btn_off.setText("OFF");
                        insurance_btn_off.setBackgroundResource(R.drawable.seeting_toggle_btn_off);
                        insurance_btn_on.setBackground(null);
                        isInsurance = false;
                    } else {
                        insurance_btn_on.setText("ON");
                        insurance_btn_off.setText("");
                        insurance_btn_on.setBackgroundResource(R.drawable.toggle_btn_public);
                        insurance_btn_off.setBackground(null);
                        isInsurance = true;
                    }
                }
//                office_policies
                if (object.isNull("office_policies")) {
                    Office_Policcy.setHint("Not Available");
                } else {
                    MakeKeywordClickableText(Office_Policcy.getContext(), object.getString("office_policies"), Office_Policcy);
//                    Office_Policcy.setText(object.getString("office_policies"));
                }

                if (object.isNull("visit_requirements")) {
                    Pre_Visting_requirments.setHint("Not Available");
                } else {
                    MakeKeywordClickableText(Pre_Visting_requirments.getContext(), object.getString("visit_requirements"), Pre_Visting_requirments);
//                    Pre_Visting_requirments.setText(object.getString("visit_requirements"));
                }


                setUrl(object);

                String Languages = "";
                Selected_languages_data.clear();
                if (!jsonObject.getJSONObject("successData").isNull("languages")) {
                    JSONArray lng = jsonObject.getJSONObject("successData").getJSONArray("languages");
                    for (int l = 0; l < lng.length(); l++) {
                        if (!lng.getJSONObject(l).isNull("get_language")) {
                            JSONObject lng_obg = lng.getJSONObject(l).getJSONObject("get_language");
                            if (Languages.length() == 0) {
                                Languages = Languages + "* " + lng_obg.getString("name");
                            } else {
                                Languages = Languages + "\n* " + lng_obg.getString("name");
                            }
                            Selected_languages_data.add(lng_obg.getString("name"));
                            adapter.remove(lng_obg.getString("name"));
                        }
                    }
                    if (Languages.length() == 0) {
//                        languages.setText("No Language Found");
                    } else {
//                        languages.setText(Languages);
                    }
                } else {
//                    languages.setText("No Language Found");
                }
                adapter.notifyDataSetChanged();
                languages_recyler_adapter.notifyDataSetChanged();


                if (!jsonObject.getJSONObject("successData").isNull("timeing")) {
                    JSONObject timings = jsonObject.getJSONObject("successData").getJSONObject("timeing");
                    SetTime(viewSt, timings);
                } else {
                    SetTime(viewSt, null);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (apiActions == get_budz_profile_can) {
            JSONObject jsonObject = null;
            try {
                ArrayList<BudzMapReviews> reviews = new ArrayList<>();
                jsonObject = new JSONObject(response);
                if (!jsonObject.getJSONObject("successData").isNull("timeing")) {
                    JSONObject timings = jsonObject.getJSONObject("successData").getJSONObject("timeing");
                    SetTime(viewSt, timings);
                } else {
                    SetTime(viewSt, null);
                }

                JSONObject object = jsonObject.getJSONObject("successData");
                if (!object.isNull("paymant_methods")) {
                    JSONArray lng = jsonObject.getJSONObject("successData").getJSONArray("paymant_methods");
                    for (int l = 0; l < lng.length(); l++) {
                        if (!lng.getJSONObject(l).isNull("payment_id")) {
                            for (PaymentMethod item : paymentMethodList) {
                                if (item.getId() == lng.getJSONObject(l).getInt("payment_id")) {
                                    item.setSelected(true);
                                }
                            }
                        }
                    }
                }
                addNewPaymentMethodRecylerAdapter.notifyDataSetChanged();
                setUrl(object);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (apiActions == get_budz_profile_event) {
            JSONObject jsonObject = null;
            try {
                ArrayList<BudzMapReviews> reviews = new ArrayList<>();
                jsonObject = new JSONObject(response);
                JSONObject object = jsonObject.getJSONObject("successData");
                setUrl(object);
                if (!object.isNull("paymant_methods")) {
                    JSONArray lng = jsonObject.getJSONObject("successData").getJSONArray("paymant_methods");
                    for (int l = 0; l < lng.length(); l++) {
                        if (!lng.getJSONObject(l).isNull("payment_id")) {
                            for (PaymentMethod item : paymentMethodList) {
                                if (item.getId() == lng.getJSONObject(l).getInt("payment_id")) {
                                    item.setSelected(true);
                                }
                            }
                        }
                    }
                }
                addNewPaymentMethodRecylerAdapter.notifyDataSetChanged();

                if (!jsonObject.getJSONObject("successData").isNull("events")) {
                    JSONObject review_object = jsonObject.getJSONObject("successData").getJSONArray("events").getJSONObject(0);
                    Event_end.setText(review_object.getString("to_time").replace("null", ""));
                    Event_start.setText(review_object.getString("from_time").replace("null", ""));
                    Event_date.setText(!review_object.getString("date").replace("null", "").equalsIgnoreCase("") ? DateConverter.setEventEditDate(review_object.getString("date").replace("null", "")) : DateConverter.getCurrentDate())
                    ;
                } else {
                    SetTime(Event_end);
                    SetTime(Event_start);
                    Event_date.setText("");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (apiActions == get_budz_profile_overall) {
            JSONObject jsonObject = null;
            try {
                ArrayList<BudzMapReviews> reviews = new ArrayList<>();
                jsonObject = new JSONObject(response);


                if (!jsonObject.getJSONObject("successData").isNull("timeing")) {
                    JSONObject timings = jsonObject.getJSONObject("successData").getJSONObject("timeing");
                    SetTime(viewSt, timings);
                } else {
                    SetTime(viewSt, null);
                }
                JSONObject object = jsonObject.getJSONObject("successData");
                setUrl(object);
                if (!object.isNull("paymant_methods")) {
                    JSONArray lng = jsonObject.getJSONObject("successData").getJSONArray("paymant_methods");
                    for (int l = 0; l < lng.length(); l++) {
                        if (!lng.getJSONObject(l).isNull("payment_id")) {
                            for (PaymentMethod item : paymentMethodList) {
                                if (item.getId() == lng.getJSONObject(l).getInt("payment_id")) {
                                    item.setSelected(true);
                                }
                            }
                        }
                    }
                }
                addNewPaymentMethodRecylerAdapter.notifyDataSetChanged();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (apiActions == APIActions.ApiActions.add_media) {
            progressDialog.Dismis();
            try {
                JSONObject jsonObject = new JSONObject(response);
                add_new_bud_jason_Data.put("others_image", jsonObject.getString("url"));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (apiActions == APIActions.ApiActions.other) {
            JSONObject jsonObject = null;
            try {
                ArrayList<BudzMapReviews> reviews = new ArrayList<>();
                jsonObject = new JSONObject(response);
                if (!jsonObject.getJSONObject("successData").isNull("timeing")) {
                    JSONObject timings = jsonObject.getJSONObject("successData").getJSONObject("timeing");
                    SetTime(viewSt, timings);
                } else {
                    SetTime(viewSt, null);
                }

                JSONObject object = jsonObject.getJSONObject("successData");

                setUrl(object);
                if (!object.isNull("others_image")) {
                    comment_image_layout.setVisibility(View.VISIBLE);
                    if (object.getString("others_image").length() > 3) {
                        attachement_img.setVisibility(View.VISIBLE);
                        add_new_bud_jason_Data.put("others_image", object.getString("others_image"));
                        Glide.with(this)
                                .load(URL.images_baseurl + object.getString("others_image"))
                                .placeholder(R.drawable.placeholder)
                                .error(R.drawable.noimage)
                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
//                                .centerCrop()
                                .into(attachement_img);

//                    show_image
                    } else {
                        add_new_bud_jason_Data.put("others_image", "");
                        comment_image_layout.setVisibility(View.GONE);
                    }
                } else {
                    add_new_bud_jason_Data.put("others_image", "");
                    comment_image_layout.setVisibility(View.GONE);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            add_new_bud_jason_Data = null;
            add_new_bud_jason_Data = new JSONObject();

            BudzMapHomeFragment.RefreshData();
            if (budz_map_item_clickerd_dataModel != null) {
                Intent budzmap_intetn = new Intent(getContext(), BudzMapDetailsActivity.class);
                budzmap_intetn.putExtra("budzmap_id", budz_map_item_clickerd_dataModel.getId());
                budzmap_intetn.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(budzmap_intetn);
                getActivity().finish();
                CustomeToast.ShowCustomToast(HealingBudApplication.getContext(), "List updated successfully!", Gravity.TOP);
            } else {
                try {
                    JSONObject jsonObject = new JSONObject(response).getJSONObject("successData");
                    Intent budzmap_intetn = new Intent(getContext(), BudzMapDetailsActivity.class);
                    budzmap_intetn.putExtra("budzmap_id", jsonObject.getInt("id"));
                    budzmap_intetn.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(budzmap_intetn);
                    getActivity().finish();
                    CustomeToast.ShowCustomToast(HealingBudApplication.getContext(), "List created successfully!", Gravity.TOP);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        }
    }

    public void setUrl(JSONObject object) throws JSONException {
//        Description.setText(object.getString("description"));
        if (!object.getString("description").equalsIgnoreCase("null")) {
            MakeKeywordClickableText(Description.getContext(), object.getString("description"), Description);
        }
        if (object.isNull("zip_code")) {
            et_zip_code.setHint("Not Available");
            et_zip_code.setText("");
        } else {
            if (object.getString("zip_code").replace("null", "").length() > 0) {
                et_zip_code.setText(object.getString("zip_code").replace("null", ""));
            } else {
                et_zip_code.setHint("Not Available");
            }
//                    Pre_Visting_requirments.setText(object.getString("visit_requirements"));
        }
        Location_text.setText(object.getString("location").replace("null", ""));
        if (object.isNull("phone")) {
            PhoneNumber.setHint("Not Available");
            PhoneNumber.setText("");
        } else {
            if (object.getString("phone").replace("null", "").length() > 0) {
                PhoneNumber.setText(object.getString("phone").replace("null", "").replace("(", "").replace(")", "").replace("-", ""));
            } else {
                PhoneNumber.setHint("Not Available");
            }

        }

        if (object.isNull("instagram")) {
            instagram_url.setHint("Not Available");
            instagram_url.setText("");
        } else {
            if (object.getString("instagram").replace("null", "").length() > 0) {
                instagram_url.setText(object.getString("instagram").replace("null", ""));
            } else {
                instagram_url.setHint("Not Available");
            }
        }


        if (object.isNull("twitter")) {
            twitter_url.setHint("Not Available");
            twitter_url.setText("");
        } else {
            if (object.getString("twitter").replace("null", "").length() > 0) {
                twitter_url.setText(object.getString("twitter").replace("null", ""));
            } else {
                twitter_url.setHint("Not Available");
            }
        }


        if (object.isNull("facebook")) {

            facebook_url.setText("");
        } else {
            if (object.getString("facebook").replace("null", "").length() > 0) {
                facebook_url.setText(object.getString("facebook").replace("null", ""));
            } else {
                facebook_url.setHint("Not Available");
            }
        }

        if (object.isNull("web")) {

            Website_url.setText("");
        } else {
            if (object.getString("web").replace("null", "").length() > 0) {
                Website_url.setText(object.getString("web").replace("null", ""));
            } else {
                Website_url.setHint("Not Available");
            }
        }
        if (object.isNull("email")) {

            email_url.setText("");
        } else {
            if (object.getString("email").replace("null", "").length() > 0) {
                email_url.setText(object.getString("email").replace("null", ""));
            } else {
                email_url.setHint("Not Available");
            }
        }
    }

    @Override
    public void onRequestError(String response, APIActions.ApiActions apiActions) {
        Log.d("response", response);
        try {
            JSONObject object = new JSONObject(response);
            CustomeToast.ShowCustomToast(getContext(), object.getString("errorMessage"), Gravity.TOP);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onTagsCrossItemClick(View view, int position) {
        adapter.add(Selected_languages_data.get(position));
        adapter.notifyDataSetChanged();
        Selected_languages_data.remove(position);
        languages_recyler_adapter.notifyItemRemoved(position);

    }

    private FragmentToActivityComm communicationActivity;

    public boolean CheckState() {
        if (communicationActivity != null) {
            if (!communicationActivity.checkValue()) {
                CustomeToast.ShowCustomToast(getContext(), "Title Fields is Required!", Gravity.TOP);
                return false;
            }
            if (Description.getText().toString().trim().length() == 0) {
                CustomeToast.ShowCustomToast(getContext(), "Description Fields is Required!", Gravity.TOP);
                return false;
            }

        }
        if (PhoneNumber.getText().toString().trim().length() == 0) {
            CustomeToast.ShowCustomToast(getContext(), "Phone Number field is Required!", Gravity.TOP);
            return false;
        }
        if (email_url.getText().toString().trim().length() == 0) {
            CustomeToast.ShowCustomToast(getContext(), "Email field is Required!", Gravity.TOP);
            return false;
        } else if (!email_url.getText().toString().trim().matches(emailPattern)) {
            CustomeToast.ShowCustomToast(getContext(), "Incorrect email format. Please provide a valid email address!", Gravity.TOP);
            return false;
        }
        if (business_type_id.equalsIgnoreCase("2") || business_type_id.equalsIgnoreCase("7") || business_type_id.equalsIgnoreCase("6")) {
            if (Selected_languages_data.size() == 0) {
                CustomeToast.ShowCustomToast(getContext(), "Language is mandatory!", Gravity.TOP);
                return false;
            }
            int st = -1;
            for (int j = 0; j < Constants.stateListIni.length; j++) {
                if (Location_text.getText().toString().trim().contains(Constants.stateListIni[j].toUpperCase())) {
                    st = 1;
                }
            }
            if (st == 1) {
                CustomeToast.ShowCustomToast(getContext(), "You can't create this type of business in this area!", Gravity.TOP);
                return false;
            }
        }
        return true;
    }

    @Override
    public void onPicSelctionItemClicked(View view, int position) {

    }

    public class Language {
        int id;
        String name;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public void SetTime(View view1, final JSONObject time_Obj) {
        Monday_Tcik = view1.findViewById(R.id.mon_tick);
        Monday_Open = view1.findViewById(R.id.mon_open);
        Monday_Close = view1.findViewById(R.id.mon_close);
        Monday_end = view1.findViewById(R.id.monday_end);
        Monday_start = view1.findViewById(R.id.monday_start);


        Monday_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SetTime(Monday_end);
            }
        });

//        TextView Monday, Tuesday, Wednesday, Thursday, Friday, Saturday, Sunday;
//        TextView Monday_time, Tuesday_time, Wednesday_time, Thursday_time, Friday_time, Saturday_time, Sunday_time;
//        Monday = view1.findViewById(R.id.mon_d);
//        Tuesday = view1.findViewById(R.id.tue_d);
//        Wednesday = view1.findViewById(R.id.wed_d);
//        Thursday = view1.findViewById(R.id.thu_d);
//        Friday = view1.findViewById(R.id.fri_d);
//        Saturday = view1.findViewById(R.id.sat_d);
//        Sunday = view1.findViewById(R.id.sun_d);

//        Monday_time = view1.findViewById(R.id.mon_t);
//        Tuesday_time = view1.findViewById(R.id.tue_t);
//        Wednesday_time = view1.findViewById(R.id.wed_t);
//        Thursday_time = view1.findViewById(R.id.thu_t);
//        Friday_time = view1.findViewById(R.id.fri_t);
//        Saturday_time = view1.findViewById(R.id.sat_t);
//        Sunday_time = view1.findViewById(R.id.sun_t);
        if (time_Obj != null) {
            try {

                if (time_Obj.getString("monday").contains("Closed")) {
//                    Monday_time.setText("Closed");
                    Monday_Tcik.setImageResource(R.drawable.ic_cross_icon);
                    Monday_Open.setVisibility(View.GONE);
                    Monday_Close.setVisibility(View.VISIBLE);
                    isMondayOpen = false;

                } else {
                    Monday_Tcik.setImageResource(R.drawable.ic_tick);
                    Monday_Open.setVisibility(View.VISIBLE);
                    Monday_Close.setVisibility(View.GONE);
                    isMondayOpen = true;
                    Monday_start.setText(time_Obj.getString("monday"));
                    Monday_end.setText(time_Obj.getString("mon_end"));
//                    Monday_time.setText(time_Obj.getString("monday") + " - " + time_Obj.getString("mon_end"));
                }

                if (time_Obj.getString("tuesday").contains("Closed")) {
//                    Tuesday_time.setText("Closed");
                    Tuesday_Tcik.setImageResource(R.drawable.ic_cross_icon);
                    Tuesday_Open.setVisibility(View.GONE);
                    Tuesday_Close.setVisibility(View.VISIBLE);
                    isTuesdayOpen = false;


                } else {
                    Tuesday_Tcik.setImageResource(R.drawable.ic_tick);
                    Tuesday_Open.setVisibility(View.VISIBLE);
                    Tuesday_Close.setVisibility(View.GONE);
                    isTuesdayOpen = true;
                    Tuesday_start.setText(time_Obj.getString("tuesday"));
                    Tuesday_end.setText(time_Obj.getString("tue_end"));
//                    Tuesday_time.setText(time_Obj.getString("tuesday") + " - " + time_Obj.getString("tue_end"));
                }

                if (time_Obj.getString("wednesday").contains("Closed")) {
//                    Wednesday_time.setText("Closed");
                    Wednesday_Tcik.setImageResource(R.drawable.ic_cross_icon);
                    Wednesday_Open.setVisibility(View.GONE);
                    Wednesday_Close.setVisibility(View.VISIBLE);
                    isWednesdayOpen = false;

                } else {
                    Wednesday_Tcik.setImageResource(R.drawable.ic_tick);
                    Wednesday_Open.setVisibility(View.VISIBLE);
                    Wednesday_Close.setVisibility(View.GONE);
                    isWednesdayOpen = true;
                    Wednesday_start.setText(time_Obj.getString("wednesday"));
                    Wednesday_end.setText(time_Obj.getString("wed_end"));
//                    Wednesday_time.setText(time_Obj.getString("wednesday") + " - " + time_Obj.getString("wed_end"));
                }

                if (time_Obj.getString("thursday").contains("Closed")) {
//                    Thursday_time.setText("Closed");
                    Thursday_Tcik.setImageResource(R.drawable.ic_cross_icon);
                    Thursday_Open.setVisibility(View.GONE);
                    Thursday_Close.setVisibility(View.VISIBLE);
                    isThursdayOpen = false;


                } else {
                    Thursday_Tcik.setImageResource(R.drawable.ic_tick);
                    Thursday_Open.setVisibility(View.VISIBLE);
                    Thursday_Close.setVisibility(View.GONE);
                    isThursdayOpen = true;
                    Thursday_start.setText(time_Obj.getString("thursday"));
                    Thursday_end.setText(time_Obj.getString("thu_end"));
//                    Thursday_time.setText(time_Obj.getString("thursday") + " - " + time_Obj.getString("thu_end"));
                }


                if (time_Obj.getString("friday").contains("Closed")) {
//                    Friday_time.setText("Closed");
                    Friday_Tcik.setImageResource(R.drawable.ic_cross_icon);
                    Friday_Open.setVisibility(View.GONE);
                    Friday_Close.setVisibility(View.VISIBLE);
                    isFridayOpen = false;

                } else {
                    Friday_Tcik.setImageResource(R.drawable.ic_tick);
                    Friday_Open.setVisibility(View.VISIBLE);
                    Friday_Close.setVisibility(View.GONE);
                    isFridayOpen = true;
                    Friday_start.setText(time_Obj.getString("friday"));
                    Friday_end.setText(time_Obj.getString("fri_end"));
//                    Friday_time.setText(time_Obj.getString("friday") + " - " + time_Obj.getString("fri_end"));
                }

                if (time_Obj.getString("saturday").contains("Closed")) {
//                    Saturday_time.setText("Closed");
                    Saturday_Tcik.setImageResource(R.drawable.ic_cross_icon);
                    Saturday_Open.setVisibility(View.GONE);
                    Saturday_Close.setVisibility(View.VISIBLE);
                    isSaturdayOpen = false;

                } else {
                    Saturday_Tcik.setImageResource(R.drawable.ic_tick);
                    Saturday_Open.setVisibility(View.VISIBLE);
                    Saturday_Close.setVisibility(View.GONE);
                    isSaturdayOpen = true;
                    Saturday_start.setText(time_Obj.getString("saturday"));
                    Saturday_end.setText(time_Obj.getString("sat_end"));
//                    Saturday_time.setText(time_Obj.getString("saturday") + " - " + time_Obj.getString("sat_end"));
                }

                if (time_Obj.getString("sunday").contains("Closed")) {
//                    Sunday_time.setText("Closed");
                    Sunday_Tcik.setImageResource(R.drawable.ic_cross_icon);
                    Sunday_Open.setVisibility(View.GONE);
                    Sunday_Close.setVisibility(View.VISIBLE);
                    isSundayOpen = false;

                } else {
                    Sunday_Tcik.setImageResource(R.drawable.ic_tick);
                    Sunday_Open.setVisibility(View.VISIBLE);
                    Sunday_Close.setVisibility(View.GONE);
                    isSundayOpen = true;
                    Sunday_start.setText(time_Obj.getString("sunday"));
                    Sunday_end.setText(time_Obj.getString("sun_end"));
//                    Sunday_time.setText(time_Obj.getString("sunday") + " - " + time_Obj.getString("sun_end"));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }


        } else {
            Sunday_Tcik.setImageResource(R.drawable.ic_cross_icon);
            Sunday_Open.setVisibility(View.GONE);
            Sunday_Close.setVisibility(View.VISIBLE);
            isSundayOpen = false;

            Saturday_Tcik.setImageResource(R.drawable.ic_cross_icon);
            Saturday_Open.setVisibility(View.GONE);
            Saturday_Close.setVisibility(View.VISIBLE);
            isSaturdayOpen = false;

            Monday_Tcik.setImageResource(R.drawable.ic_cross_icon);
            Monday_Open.setVisibility(View.GONE);
            Monday_Close.setVisibility(View.VISIBLE);
            isMondayOpen = false;

            Friday_Tcik.setImageResource(R.drawable.ic_cross_icon);
            Friday_Open.setVisibility(View.GONE);
            Friday_Close.setVisibility(View.VISIBLE);
            isFridayOpen = false;

            Tuesday_Tcik.setImageResource(R.drawable.ic_cross_icon);
            Tuesday_Open.setVisibility(View.GONE);
            Tuesday_Close.setVisibility(View.VISIBLE);
            isTuesdayOpen = false;
//
            Wednesday_Tcik.setImageResource(R.drawable.ic_cross_icon);
            Wednesday_Open.setVisibility(View.GONE);
            Wednesday_Close.setVisibility(View.VISIBLE);
            isWednesdayOpen = false;

            Thursday_Tcik.setImageResource(R.drawable.ic_cross_icon);
            Thursday_Open.setVisibility(View.GONE);
            Thursday_Close.setVisibility(View.VISIBLE);
            isThursdayOpen = false;


        }


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PLACE_PICKER) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(getActivity(), data);

                Log.d("onActivityResult: ", place.getAddress().toString());
                Location_text.setText(place.getAddress().toString());
                latitude = place.getLatLng().latitude;
                longitude = place.getLatLng().longitude;
                mMap.clear();
                mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(latitude, longitude))
                        .flat(true)
                        .draggable(true)
                        .icon(BitmapDescriptorFactory.fromBitmap(getBitmapFromVectorDrawable(getContext(), marker_resoures))));
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 15);
                mMap.moveCamera(cameraUpdate);
//                Log.i(TAG, "Place: " + place.getName());
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(getActivity(), data);
                // TODO: Handle the error.
//                Log.i(TAG, status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        } else if (resultCode == RESULT_OK && requestCode == 1224) {
            if (data.getExtras().getBoolean("isVideo")) {

//                comment_image_layout.setVisibility(View.VISIBLE);
//                if (data.getExtras().getBoolean("camera_video")) {
//                    final String filePath = data.getExtras().getString("file_path_arg");
//                    video_processing = ProgressDialogVideoProcessing.newInstance();
//                    video_processing.show(((FragmentActivity) MContext).getSupportFragmentManager(), "pd");
//                    new Handler().postDelayed(new Runnable() {
//                        public void run() {
//                            new SetReviewaAndComments.VideoProcessing().execute(filePath);
//                        }
//                    }, 200);
//                } else {
//                    new Handler().postDelayed(new Runnable() {
//                        public void run() {
//                            String filePath = data.getExtras().getString("file_path_arg");
//                            Bitmap video_thumbnil = getVideoThumbnail(filePath);
//                            video_thumbnil = checkRotationVideo(video_thumbnil, filePath);
//                            if (video_thumbnil != null) {
//                                video_thumbnil = Bitmap.createScaledBitmap(video_thumbnil, 300, 300, false);
//                                int corner_radious = (video_thumbnil.getWidth() * 10) / 100;
//                                Bitmap bitmap = getRoundedCornerBitmap(video_thumbnil, corner_radious);
//                                attachement_img.setBackground(new BitmapDrawable(MContext.getResources(), video_thumbnil));
//                            } else {
//                                Drawable d = ContextCompat.getDrawable(MContext, R.drawable.test_img);
//                                Bitmap bitmapOrg = ((BitmapDrawable) d).getBitmap();
//                                bitmapOrg = Bitmap.createScaledBitmap(bitmapOrg, 300, 300, false);
//                                int corner_radious = (bitmapOrg.getWidth() * 6) / 100;
//                                Bitmap bitmap = getRoundedCornerBitmap(bitmapOrg, corner_radious);
//                                attachement_img.setBackground(d);
//                            }
//                        }
//                    }, 10);
//                }
            } else {

                comment_image_layout.setVisibility(View.VISIBLE);
                Bitmap bitmapOrg = BitmapFactory.decodeFile(data.getExtras().getString("file_path_arg"));
                bitmapOrg = checkRotation(bitmapOrg, data.getExtras().getString("file_path_arg"));
                bitmapOrg = Bitmap.createScaledBitmap(bitmapOrg, 300, 300, false);
//                attachement_img.setBackground();
                Glide.with(this)
                        .load(data.getExtras().getString("file_path_arg"))
                        .placeholder(R.drawable.placeholder)
                        .error(R.drawable.noimage)
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
//                        .centerCrop()
                        .into(attachement_img);
                uploadImageOther(data.getExtras().getString("file_path_arg"));
            }
        }


    }

    ProgressDialog progressDialog;

    public void uploadImageOther(String path) {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.Show();
        new UploadImageAPIcall(getActivity(), URL.add_banner, path, user.getSession_key(), BusinessInfoTabFragmentEdit.this, APIActions.ApiActions.add_media);
    }

    public Drawable GetRounderDarbable(Bitmap bitmapOrg) {
        int corner_radious = (bitmapOrg.getWidth() * 10) / 100;
        Bitmap bitmap = getRoundedCornerBitmap(bitmapOrg, corner_radious);
        return new BitmapDrawable(getContext().getResources(), bitmap);
    }
}
