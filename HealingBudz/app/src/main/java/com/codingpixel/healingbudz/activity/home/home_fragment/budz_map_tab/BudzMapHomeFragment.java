package com.codingpixel.healingbudz.activity.home.home_fragment.budz_map_tab;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.android.volley.Request;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.codingpixel.healingbudz.DataModel.BudzMapHomeDataModel;
import com.codingpixel.healingbudz.R;
import com.codingpixel.healingbudz.Utilities.GetUserLatLng;
import com.codingpixel.healingbudz.activity.home.HomeActivity;
import com.codingpixel.healingbudz.activity.home.home_fragment.budz_map_tab.dialog.BudzFeedAlertDialog;
import com.codingpixel.healingbudz.activity.home.home_fragment.budz_map_tab.dialog.BudzMapStripeDialog;
import com.codingpixel.healingbudz.adapter.BudzMapHomeRecylAdapter;
import com.codingpixel.healingbudz.application.HealingBudApplication;
import com.codingpixel.healingbudz.custome_dialog.save_discussion.SaveDiscussionAlertDialog;
import com.codingpixel.healingbudz.data_structure.APIActions;
import com.codingpixel.healingbudz.interfaces.GetActivityForResultResponse;
import com.codingpixel.healingbudz.interfaces.UserLocationListner;
import com.codingpixel.healingbudz.network.VollyAPICall;
import com.codingpixel.healingbudz.network.model.APIResponseListner;
import com.codingpixel.healingbudz.network.model.URL;
import com.codingpixel.healingbudz.sharedprefrences.SharedPrefrences;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mancj.slideup.SlideUp;
import com.mancj.slideup.SlideUpBuilder;
import com.stripe.android.model.Token;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.codingpixel.healingbudz.Utilities.Constants.D_FORMAT_ONE;
import static com.codingpixel.healingbudz.Utilities.KeywordClickDialog.isKeywordSearch;
import static com.codingpixel.healingbudz.Utilities.KeywordClickDialog.isKeywordSearch_string;
import static com.codingpixel.healingbudz.activity.splash.Splash.user;
import static com.codingpixel.healingbudz.data_structure.APIActions.ApiActions.add_subscription;
import static com.codingpixel.healingbudz.data_structure.APIActions.ApiActions.get_budz_map;
import static com.codingpixel.healingbudz.network.model.URL.images_baseurl;
import static com.codingpixel.healingbudz.network.model.URL.sharedBaseUrl;
import static com.codingpixel.healingbudz.static_function.ShareIntent.ShareHBContent;
import static com.codingpixel.healingbudz.static_function.UIModification.HideKeyboard;

public class BudzMapHomeFragment extends Fragment implements OnMapReadyCallback
        , View.OnClickListener, LocationListener
        , BudzFeedAlertDialog.OnDialogFragmentClickListener
        , BudzMapHomeRecylAdapter.ItemClickListener
        , SaveDiscussionAlertDialog.OnDialogFragmentClickListener
        , APIResponseListner, UserLocationListner
        , BudzMapStripeDialog.OnDialogFragmentClickListener {
    RelativeLayout Home_search_filter_layout;
    LinearLayout Home_search_filter_button, distance_check;
    ImageView Home_search_filter_layout_close_btn, Share_btn;
    private SlideUp home_search_filter_slide;
    SupportMapFragment mapFragment;
    private GoogleMap mMap;
    boolean isAppiCalled = false;
    double userLAtitude, userLongitude;
    ImageView Search_btn, Cross_search;
    LinearLayout Search_layout;
    public static APIResponseListner apiResponseListner = null;
    public static Context class_context = null;
    ArrayList<BudzMapHomeDataModel> budz_map_test_data = new ArrayList<>();
    private ViewGroup infoWindow;
    private LocationManager locationManager = null;
    RecyclerView budz_map_recyler_view;
    ImageView Menu_btn;
    ImageView Search_Button;
    EditText Search_budz;
    public static BudzMapHomeDataModel budz_map_item_clickerd_dataModel;
    public static BudzMapHomeDataModel budz_map_item_clickerd_dataModel_abc;
    Drawable InfoWindowIcon = null;
    ImageView Zoom_In, Zoom_out, Current_Location;
    RelativeLayout main_content_view;
    private LatLng CurrentPosition;
    static LinearLayout Refresh, refresh_below;
    public static GetActivityForResultResponse getActivityForResultResponse;
    static String Filteration_ids = "";
    BudzMapHomeRecylAdapter recyler_adapter;
    static boolean isDistance = true;
    Switch Dispencry_Switch, MEdical_switch, Cannabites_Switch, Entertainment_switch, Event_Switch, Others_Switch, distance_switch;
    private TextView no_record_found;
    private static int pages = 0;
    static boolean isAppiCallActive = false;
    boolean isSearched = false;
    public static boolean isSearchedText = false;
    public static String isSearchedTextValue = "";
    static String lat = "";
    static String lng = "";

    @Override
    public void onResume() {
        super.onResume();
        pages = 0;
        budz_map_test_data.clear();
        Search_budz.setText("");
        Search_layout.setVisibility(View.GONE);
        recyler_adapter.notifyDataSetChanged();
        Refresh.setVisibility(View.VISIBLE);
        if (isDistance) {
            new GetUserLatLng().getUserLocation(getContext(), BudzMapHomeFragment.this, true);
        } else {
            onLocationFailed("er");
        }
    }

    SwipeRefreshLayout swipe_rf;

    @TargetApi(Build.VERSION_CODES.M)
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.budz_map_home_fragment_layout, container, false);

        lat = user.getLat() + "";
        lng = user.getLng() + "";
        view.setClickable(false);
        view.setEnabled(false);
        view.setOnClickListener(null);
        view.setOnTouchListener(null);
        apiResponseListner = BudzMapHomeFragment.this;
        class_context = getContext();
        Init(view);
        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Menu_btn = view.findViewById(R.id.menu_btn);
        Menu_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (HomeActivity.drawerLayout != null) {
                    HomeActivity.drawerLayout.openDrawer(Gravity.START);
                }
            }
        });
//        budz_map_test_data();
        budz_map_recyler_view = (RecyclerView) view.findViewById(R.id.budz_map_recyler_view);
        main_content_view = (RelativeLayout) view.findViewById(R.id.main_content_view);
        RecyclerView.LayoutManager layoutManager_home_saerch = new LinearLayoutManager(getContext());
        budz_map_recyler_view.setLayoutManager(layoutManager_home_saerch);
        recyler_adapter = new BudzMapHomeRecylAdapter(getContext(), budz_map_test_data);
        recyler_adapter.setClickListener(this);
        budz_map_recyler_view.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View view, int i, int i1, int i2, int i3) {
                LinearLayoutManager layoutManager = LinearLayoutManager.class.cast(budz_map_recyler_view.getLayoutManager());
                int lastVisible = layoutManager.findLastVisibleItemPosition();
                Log.d("vissible", lastVisible + "");
                Log.d("vissible_t", (lastVisible % 10) + "");
                if (lastVisible >= (pages + 1) * 9 && !isAppiCallActive) {
                    if ((lastVisible % 9) == 0) {
                        pages = pages + 1;
                        JSONObject object = new JSONObject();
                        String url = "";
                        refresh_below.setVisibility(View.VISIBLE);
                        new VollyAPICall(view.getContext(), false, URL.get_budz_map + "?skip=" + pages + "&lat=" + lat + "&lng=" + lng + "&type=" + Filteration_ids + "&query=" + Search_budz.getText().toString(), object, user.getSession_key(), Request.Method.GET, BudzMapHomeFragment.this, get_budz_map);
                    }
                }
            }
        });
        swipe_rf = (SwipeRefreshLayout) view.findViewById(R.id.swipe_rf);
        swipe_rf.setColorSchemeColors(Color.parseColor("#932a88"));
        swipe_rf.setProgressBackgroundColorSchemeColor(Color.parseColor("#252222"));
        swipe_rf.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipe_rf.setRefreshing(true);
                pages = 0;
                JSONObject object = new JSONObject();
                String url = "";

                new VollyAPICall(view.getContext(), false, URL.get_budz_map + "?skip=" + pages + "&lat=" + lat + "&lng=" + lng + "&type=" + Filteration_ids + "&query=" + Search_budz.getText().toString(), object, user.getSession_key(), Request.Method.GET, BudzMapHomeFragment.this, get_budz_map);

            }
        });
        budz_map_recyler_view.setAdapter(recyler_adapter);

        ImageView Add_new_data = view.findViewById(R.id.add_new_data);
        Add_new_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                budz_map_item_clickerd_dataModel_abc = null;
                BudzFeedAlertDialog budzFeedAlertDialog = BudzFeedAlertDialog.newInstance(BudzMapHomeFragment.this, false);
                budzFeedAlertDialog.show(getActivity().getSupportFragmentManager(), "dialog");
            }
        });


        Refresh = view.findViewById(R.id.refresh);
        refresh_below = view.findViewById(R.id.refresh_below);
        Refresh.setVisibility(View.VISIBLE);

        Dispencry_Switch = view.findViewById(R.id.switch_dispencry);
        MEdical_switch = view.findViewById(R.id.switch_medical);
        Cannabites_Switch = view.findViewById(R.id.switch_cannabites);
        Entertainment_switch = view.findViewById(R.id.switch_entertainment);
        Event_Switch = view.findViewById(R.id.switch_events);
        Others_Switch = view.findViewById(R.id.switch_others);
        distance_switch = view.findViewById(R.id.distance_switch);
        if (isDistance) {
            distance_switch.setChecked(true);
        } else {
            distance_switch.setChecked(false);
        }
        distance_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    isDistance = true;
                    new GetUserLatLng().getUserLocation(getContext(), BudzMapHomeFragment.this, true);
                } else {
                    isDistance = false;
                    onLocationFailed("er");
                }
            }
        });
        if (SharedPrefrences.getBoolWithTrueDefault("Dispencry_Switch", getContext())) {
            if (Filteration_ids.length() == 0) {
                Filteration_ids = Filteration_ids + "1";
            } else {
                Filteration_ids = Filteration_ids + ",1";
            }
            Dispencry_Switch.setChecked(true);
        } else {
            Dispencry_Switch.setChecked(false);
        }

        if (SharedPrefrences.getBoolWithTrueDefault("MEdical_switch", getContext())) {
            if (Filteration_ids.length() == 0) {
                Filteration_ids = Filteration_ids + "2";
            } else {
                Filteration_ids = Filteration_ids + ",2";
            }
            MEdical_switch.setChecked(true);
        } else {
            MEdical_switch.setChecked(false);
        }

        if (SharedPrefrences.getBoolWithTrueDefault("Cannabites_Switch", getContext())) {
            if (Filteration_ids.length() == 0) {
                Filteration_ids = Filteration_ids + "3";
            } else {
                Filteration_ids = Filteration_ids + ",3";
            }
            Cannabites_Switch.setChecked(true);
        } else {
            Cannabites_Switch.setChecked(false);
        }

        if (SharedPrefrences.getBoolWithTrueDefault("Entertainment_switch", getContext())) {
            if (Filteration_ids.length() == 0) {
                Filteration_ids = Filteration_ids + "4";
            } else {
                Filteration_ids = Filteration_ids + ",4";
            }
            Entertainment_switch.setChecked(true);
        } else {
            Entertainment_switch.setChecked(false);
        }

        if (SharedPrefrences.getBoolWithTrueDefault("Event_Switch", getContext())) {
            if (Filteration_ids.length() == 0) {
                Filteration_ids = Filteration_ids + "5";
            } else {
                Filteration_ids = Filteration_ids + ",5";
            }
            Event_Switch.setChecked(true);
        } else {
            Event_Switch.setChecked(false);
        }
        if (SharedPrefrences.getBoolWithTrueDefault("Others_Switch", getContext())) {
            if (Filteration_ids.length() == 0) {
                Filteration_ids = Filteration_ids + "9";
            } else {
                Filteration_ids = Filteration_ids + ",9";
            }
            Others_Switch.setChecked(true);
        } else {
            Others_Switch.setChecked(false);
        }
//        new GetUserLatLng().getUserLocation(view.getContext(), BudzMapHomeFragment.this);
        Search_btn = view.findViewById(R.id.search_bud_button);
        Search_budz = view.findViewById(R.id.search_budz_edittext);
        Search_budz.setImeOptions(EditorInfo.IME_ACTION_DONE);
        Search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Search_budz.toString().trim().length() > 1) {
                    isSearched = true;
                    isSearchedText = true;
                    isSearchedTextValue = Search_budz.getText().toString();
                    isAppiCallActive = true;
                    HideKeyboard(getActivity());
                    JSONObject object = new JSONObject();
                    pages = 0;
                    Refresh.setVisibility(View.VISIBLE);
                    budz_map_test_data.clear();
                    recyler_adapter.notifyDataSetChanged();
                    new VollyAPICall(view.getContext(), false, URL.get_budz_map + "?skip=0&lat=" + lat + "&lng=" + lng + "&type=" + Filteration_ids + "&query=" + Search_budz.getText().toString(), object, user.getSession_key(), Request.Method.GET, BudzMapHomeFragment.this, get_budz_map);
                }
            }
        });

        Search_budz.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    if (Search_budz.toString().trim().length() > 1) {
                        isAppiCallActive = true;
                        isSearched = true;
                        isSearchedText = true;
                        isSearchedTextValue = Search_budz.getText().toString();
                        HideKeyboard(getActivity());
                        JSONObject object = new JSONObject();
                        pages = 0;
                        Refresh.setVisibility(View.VISIBLE);
                        budz_map_test_data.clear();
                        recyler_adapter.notifyDataSetChanged();
                        new VollyAPICall(view.getContext(), false, URL.get_budz_map + "?skip=0&lat=" + lat + "&lng=" + lng + "&type=" + Filteration_ids + "&query=" + Search_budz.getText().toString(), object, user.getSession_key(), Request.Method.GET, BudzMapHomeFragment.this, get_budz_map);
                    }
                    return true;
                }
                return false;
            }
        });
//        Search_budz.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
////                if (s.toString().isEmpty()) {
////                    isAppiCallActive = true;
////                    budz_map_test_data.clear();
////                    recyler_adapter.notifyDataSetChanged();
////                    Refresh.setVisibility(View.VISIBLE);
////                    pages = 0;
////                    new VollyAPICall(getContext(), false, URL.get_budz_map + "?skip=0&lat=" + user.getLat() + "&lng=" + user.getLng() + "&type=" + Filteration_ids, new JSONObject(), user.getSession_key(), Request.Method.GET, BudzMapHomeFragment.this, get_budz_map);
////                }
//            }
//        });
//        if (user.isShow_budz_popup()) {
//            Intent intent = new Intent(getActivity(), BudzMapPaidViewActivity.class);
//            intent.putExtra("isFromMain", true);
//            view.getContext().startActivity(intent);
//        }
        return view;
    }

    public void Init(View view) {
        no_record_found = view.findViewById(R.id.no_record_found);
        Share_btn = view.findViewById(R.id.share_btn);
        Share_btn.setOnClickListener(this);
        Home_search_filter_button = (LinearLayout) view.findViewById(R.id.home_search_filter_button_click);
        distance_check = (LinearLayout) view.findViewById(R.id.distance_check);
        Home_search_filter_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Home_search_filter_button.setVisibility(View.GONE);
                distance_check.setVisibility(View.GONE);
                home_search_filter_slide.show();
            }
        });

        Home_search_filter_layout = (RelativeLayout) view.findViewById(R.id.home_search_filter_layout);
        Home_search_filter_layout_close_btn = (ImageView) view.findViewById(R.id.close_filter);
        Home_search_filter_layout_close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                home_search_filter_slide.hide();
                pages = 0;
                Filteration_ids = "";
                if (Dispencry_Switch.isChecked()) {
                    if (Filteration_ids.length() == 0) {
                        Filteration_ids = Filteration_ids + "1";
                    } else {
                        Filteration_ids = Filteration_ids + ",1";
                    }
                    SharedPrefrences.setBool("Dispencry_Switch", true, getContext());
                } else {
                    SharedPrefrences.setBool("Dispencry_Switch", false, getContext());
                }

                if (MEdical_switch.isChecked()) {
                    if (Filteration_ids.length() == 0) {
                        Filteration_ids = Filteration_ids + "2";
                    } else {
                        Filteration_ids = Filteration_ids + ",2";
                    }
                    SharedPrefrences.setBool("MEdical_switch", true, getContext());
                } else {
                    SharedPrefrences.setBool("MEdical_switch", false, getContext());
                }

                if (Cannabites_Switch.isChecked()) {
                    if (Filteration_ids.length() == 0) {
                        Filteration_ids = Filteration_ids + "3";
                    } else {
                        Filteration_ids = Filteration_ids + ",3";
                    }
                    SharedPrefrences.setBool("Cannabites_Switch", true, getContext());
                } else {
                    SharedPrefrences.setBool("Cannabites_Switch", false, getContext());
                }

                if (Entertainment_switch.isChecked()) {
                    if (Filteration_ids.length() == 0) {
                        Filteration_ids = Filteration_ids + "4";
                    } else {
                        Filteration_ids = Filteration_ids + ",4";
                    }
                    SharedPrefrences.setBool("Entertainment_switch", true, getContext());
                } else {
                    SharedPrefrences.setBool("Entertainment_switch", false, getContext());
                }


                if (Event_Switch.isChecked()) {
                    if (Filteration_ids.length() == 0) {
                        Filteration_ids = Filteration_ids + "5";
                    } else {
                        Filteration_ids = Filteration_ids + ",5";
                    }
                    SharedPrefrences.setBool("Event_Switch", true, getContext());
                } else {
                    SharedPrefrences.setBool("Event_Switch", false, getContext());
                }
                if (Others_Switch.isChecked()) {
                    if (Filteration_ids.length() == 0) {
                        Filteration_ids = Filteration_ids + "9";
                    } else {
                        Filteration_ids = Filteration_ids + ",9";
                    }
                    SharedPrefrences.setBool("Others_Switch", true, getContext());
                } else {
                    SharedPrefrences.setBool("Others_Switch", false, getContext());
                }


                JSONObject object = new JSONObject();
                isAppiCallActive = true;
                pages = 0;
                Refresh.setVisibility(View.VISIBLE);
                budz_map_test_data.clear();
                recyler_adapter.notifyDataSetChanged();
                new VollyAPICall(getContext(), false, URL.get_budz_map + "?skip=0&lat=" + lat + "&lng=" + lng + "&type=" + Filteration_ids, object, user.getSession_key(), Request.Method.GET, BudzMapHomeFragment.this, get_budz_map);

            }
        });

        home_search_filter_slide = new SlideUpBuilder(Home_search_filter_layout)
                .withListeners(new SlideUp.Listener.Events() {
                    @Override
                    public void onSlide(float percent) {
                    }

                    @Override
                    public void onVisibilityChanged(int visibility) {
                        if (visibility == View.GONE) {
                            Home_search_filter_button.setVisibility(View.VISIBLE);
                            distance_check.setVisibility(View.GONE);
                        }
                    }
                })
                .withStartGravity(Gravity.TOP)
                .withLoggingEnabled(true)
                .withStartState(SlideUp.State.HIDDEN)
                .build();


        Zoom_In = view.findViewById(R.id.zoom_in);
        Zoom_In.setOnClickListener(this);
        Zoom_out = view.findViewById(R.id.zoom_out);
        Zoom_out.setOnClickListener(this);
        Current_Location = view.findViewById(R.id.current_location);
        Current_Location.setOnClickListener(this);

        Search_layout = view.findViewById(R.id.search_layout);
        Search_btn = view.findViewById(R.id.search_btn);
        Cross_search = view.findViewById(R.id.cross_search);

        Search_btn.setOnClickListener(this);
        Cross_search.setOnClickListener(this);
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

        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10, 0, this);
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        mMap.getUiSettings().setMapToolbarEnabled(false);
        mMap.setOnInfoWindowClickListener(null);
        setGoogleMapInfoWindow();
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(final Marker marker) {
                marker.showInfoWindow();
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(marker.getTitle());
                    if (jsonObject.getString("logo").length() < 6) {
                        InfoWindowIcon = null;
                    }
                    String url = images_baseurl + jsonObject.getString("logo");
                    Glide.with(getContext())
                            .load(url)
                            .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                            .placeholder(R.drawable.ic_add_journal)
                            .listener(new RequestListener<String, GlideDrawable>() {
                                @Override
                                public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
//                                    Log.d("ready", model);
//                                    Log.d("ready", e.getMessage());
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                    Log.d("ready", model);
                                    InfoWindowIcon = resource;
                                    marker.showInfoWindow();
                                    return false;
                                }
                            })
                            .into(300, 300);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return false;
            }
        });

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                try {
                    JSONObject jsonObject = new JSONObject(marker.getTitle());
                    budz_map_item_clickerd_dataModel = budz_map_test_data.get(jsonObject.getInt("position"));
                    budz_map_item_clickerd_dataModel_abc = budz_map_test_data.get(jsonObject.getInt("position"));
                    Intent i = new Intent(getContext(), BudzMapDetailsActivity.class);
                    i.putExtra("budz_type", jsonObject.getString("type_id"));
                    startActivity(i);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void setGoogleMapInfoWindow() {
        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            // Use default InfoWindow frame
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            // Defines the contents of the InfoWindow
            @Override
            public View getInfoContents(Marker arg0) {
                infoWindow = (ViewGroup) getActivity().getLayoutInflater().inflate(R.layout.budz_map_marker_info_window, null);
                TextView Title = infoWindow.findViewById(R.id.heading_title);
                TextView type = infoWindow.findViewById(R.id.type);
                TextView reviews = infoWindow.findViewById(R.id.reviews);
                TextView open_untill = infoWindow.findViewById(R.id.open_untill);
                final ImageView icon_img = infoWindow.findViewById(R.id.info_windowicon_img);
                final ImageView rating_img = infoWindow.findViewById(R.id.rating_img);
                try {
                    JSONObject jsonObject = new JSONObject(arg0.getTitle());
                    Title.setText(jsonObject.getString("title"));
                    type.setText(jsonObject.getString("type"));
                    reviews.setText(jsonObject.getString("reviews"));
                    open_untill.setText(jsonObject.getString("open_untill"));
                    if (InfoWindowIcon != null) {
                        icon_img.setImageDrawable(InfoWindowIcon);
                    } else {
                        icon_img.setImageResource(R.drawable.ic_budz_adn);
                    }

                    if (jsonObject.getDouble("rating") > 0) {
                        if (jsonObject.getDouble("rating") == 1) {
                            rating_img.setImageResource(R.drawable.rating_one);
                        } else if (jsonObject.getDouble("rating") == 2) {
                            rating_img.setImageResource(R.drawable.rating_two);
                        } else if (jsonObject.getDouble("rating") == 3) {
                            rating_img.setImageResource(R.drawable.rating_three);
                        } else if (jsonObject.getDouble("rating") == 4) {
                            rating_img.setImageResource(R.drawable.rating_four);
                        } else if (jsonObject.getDouble("rating") == 5) {
                            rating_img.setImageResource(R.drawable.rating_five);
                        }
                    } else {
                        rating_img.setImageResource(R.drawable.rating_zero);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                infoWindow.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                return infoWindow;
            }
        });
    }

    public static Bitmap getBitmapFromVectorDrawable(Context context, @NonNull int drawableId) {
        if (context != null) {
            Drawable drawable = ContextCompat.getDrawable(context, drawableId);
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                drawable = (DrawableCompat.wrap(drawable)).mutate();
            }
            Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                    drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
            return bitmap;
        } else {
            return null;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.zoom_in:
                mMap.animateCamera(CameraUpdateFactory.zoomIn());
                break;
            case R.id.zoom_out:
                mMap.animateCamera(CameraUpdateFactory.zoomOut());
                break;
            case R.id.current_location:
                if (CurrentPosition != null) {
                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(CurrentPosition, 15f);
                    mMap.animateCamera(cameraUpdate);
                }
                break;
            case R.id.share_btn:
                JSONObject object = new JSONObject();
                try {
//                    object.put("msg", "Hey Bud! Check out the Healing Budz app for your smart phone.” Download it today from http://139.162.37.73/healingbudz/budz-map");
                    object.put("msg", "Check out Healing Budz for your smartphone: " + sharedBaseUrl + "/budz-map");
                    object.put("content", "Budz Adz");
                    object.put("url", sharedBaseUrl + "/budz-map");
                    object.put("BudzCome", "");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                ShareHBContent(getActivity(), object);
                break;
            case R.id.search_btn:

                Search_budz.setText("");
                if (Search_budz.getText().toString().trim().length() > 0) {
                    Search_budz.setText("");
                }
                Search_layout.setVisibility(View.VISIBLE);
                Search_budz.requestFocus();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            InputMethodManager imm = (InputMethodManager) Search_budz.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.toggleSoftInputFromWindow(Search_budz.getWindowToken(), InputMethodManager.SHOW_FORCED, 0);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, 300);
                break;
            case R.id.cross_search:
                isSearchedText = false;
                isSearchedTextValue = "";
//                Search_budz.setText("");
                Search_layout.setVisibility(View.GONE);
                HideKeyboard(getActivity());
                no_record_found.setVisibility(View.GONE);
                if (Search_budz.getText().toString().trim().length() > 0) {
                    Search_budz.setText("");
                }
                if (isSearched) {
                    isSearched = false;
                    budz_map_test_data.clear();
                    recyler_adapter.notifyDataSetChanged();
                    Refresh.setVisibility(View.VISIBLE);
                    isAppiCallActive = true;
                    pages = 0;
                    new VollyAPICall(getContext(), false, URL.get_budz_map + "?skip=0&lat=" + lat + "&lng=" + lng + "&type=" + Filteration_ids, new JSONObject(), user.getSession_key(), Request.Method.GET, BudzMapHomeFragment.this, get_budz_map);
                }
                break;
        }
    }


    @Override
    public void onLocationChanged(Location location) {
        CurrentPosition = new LatLng(location.getLatitude(), location.getLongitude());
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    @Override
    public void onCountinueFreeListingBtnClink(BudzFeedAlertDialog dialog) {
        Log.d("free", "btn click");
        budz_map_item_clickerd_dataModel = null;
        budz_map_item_clickerd_dataModel_abc = null;
        Intent intent = new Intent(getContext(), AddNewBudzMapActivity.class);
        intent.putExtra("isSubcribed", false);
        intent.putExtra("sub_user_id", "");
        startActivity(intent);
    }

    @Override
    public void onSubcribeNowBtnClick(BudzFeedAlertDialog dialog) {
        Log.d("free", "btn click");
//        BudzMapStripeDialog budzFeedAlertDialog = BudzMapStripeDialog.newInstance(BudzMapHomeFragment.this, false);
//        budzFeedAlertDialog.show(getActivity().getSupportFragmentManager(), "dialog");
    }

    @Override
    public void onItemClick(View view, int position) {
        try {
            budz_map_item_clickerd_dataModel = budz_map_test_data.get(position);
            budz_map_item_clickerd_dataModel_abc = budz_map_test_data.get(position);
//            budz_map_item_clickerd_dataModel = recyler_adapter.getData(position);
//            budz_map_item_clickerd_dataModel_abc = recyler_adapter.getData(position);
            Intent i = new Intent(getContext(), BudzMapDetailsActivity.class);
            i.putExtra("budz_type", budz_map_item_clickerd_dataModel.getBusiness_type_id() + "");
            startActivity(i);
        } catch (java.lang.IndexOutOfBoundsException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onEditClick(View view, int position) {

    }

    @Override
    public void onDeleteClick(View view, int position) {

    }

    @Override
    public void onEditPendingClick(View view, int position) {

    }

    @Override
    public void onDeletePendingClick(View view, int position) {

    }

    @Override
    public void onOkClicked(SaveDiscussionAlertDialog dialog) {
        SharedPrefrences.setBool("IS_BudzMap_My_SAVE_Dialog_Shown", true, getContext());
        dialog.dismiss();
        JSONObject object = new JSONObject();
        try {
//            object.put("msg", "Hey Bud! Check out the Healing Budz app for your smart phone.” Download it today from http://139.162.37.73/healingbudz/budz-map");
            object.put("msg", "Hey Bud! Check out the Healing Budz app for your smart phone.” Download it today from http://139.162.37.73/healingbudz/");
        } catch (JSONException e) {
            e.printStackTrace();
        }
//        ShareHBContent(getActivity(), object);
    }

    @Override
    public void onCancelClicked(SaveDiscussionAlertDialog dialog) {
        dialog.dismiss();
    }

    @Override
    public void onRequestSuccess(String response, APIActions.ApiActions apiActions) {
        Log.d("response", response);
        isAppiCallActive = false;
        if (apiActions == get_budz_map) {
            refresh_below.setVisibility(View.GONE);
            swipe_rf.setRefreshing(false);
            Refresh.setVisibility(View.GONE);
            try {
                if (pages == 0) {
                    budz_map_test_data.clear();
                }
                JSONObject jsonObject = new JSONObject(response);
                JSONArray array = jsonObject.getJSONArray("successData");
                for (int x = 0; x < array.length(); x++) {
                    isAppiCalled = true;
                    JSONObject object = array.getJSONObject(x);
                    BudzMapHomeDataModel dataModel = new BudzMapHomeDataModel();
                    dataModel.setId(object.getInt("id"));
                    dataModel.setOthers_image(object.optString("others_image"));
                    dataModel.setUser_id(object.getInt("user_id"));
                    dataModel.setBusiness_type_id(object.getInt("business_type_id"));
                    dataModel.setTitle(object.getString("title"));
                    dataModel.setLogo(object.getString("logo"));
                    dataModel.setBanner(object.optString("banner"));
                    dataModel.setBanner_full(object.optString("banner_full"));
                    dataModel.setIs_organic(object.getInt("is_organic"));
                    dataModel.setIs_delivery(object.getInt("is_delivery"));
                    dataModel.setDescription(object.getString("description"));
                    dataModel.setLocation(object.getString("location"));
                    dataModel.setLat(object.getDouble("lat"));
                    dataModel.setLng(object.getDouble("lng"));
                    dataModel.setIs_featured(object.getInt("is_featured"));
                    dataModel.setPhone(object.getString("phone"));
                    dataModel.setWeb(object.getString("web"));
                    dataModel.setFacebook(object.getString("facebook"));
                    dataModel.setTwitter(object.getString("twitter"));
                    dataModel.setInstagram(object.getString("instagram"));
                    dataModel.setCreated_at(object.getString("created_at"));
                    dataModel.setUpdated_at(object.getString("updated_at"));
                    dataModel.setStripe_id(object.getString("stripe_id"));
                    dataModel.setCard_brand(object.getString("card_brand"));
                    dataModel.setCard_last_four(object.getString("card_last_four"));
                    dataModel.setTrial_ends_at(object.getString("trial_ends_at"));
                    dataModel.setDistance(object.optInt("distance"));
                    dataModel.setGet_user_save_count(object.getInt("get_user_save_count"));
                    if (!object.isNull("rating_sum")) {
//                        dataModel.setRating_sum(object.optJSONObject("rating_sum").getDouble("total"));
                        dataModel.setRating_sum(Double.valueOf(D_FORMAT_ONE.format(object.optJSONObject("rating_sum").getDouble("total"))));
                    }
                    ArrayList<BudzMapHomeDataModel.Reviews> reviews = new ArrayList<>();
                    JSONArray reviews_Array = object.getJSONArray("review");
                    for (int y = 0; y < reviews_Array.length(); y++) {
                        BudzMapHomeDataModel.Reviews reviews_model = new BudzMapHomeDataModel.Reviews();
                        JSONObject review_object = reviews_Array.getJSONObject(y);
                        reviews_model.setId(review_object.getInt("id"));
                        reviews_model.setSub_user_id(review_object.getInt("sub_user_id"));
                        reviews_model.setReviewed_by(review_object.getInt("reviewed_by"));
                        reviews_model.setText(review_object.getString("text"));
                        reviews_model.setCreated_at(review_object.getString("created_at"));
                        reviews.add(reviews_model);
                    }
                    dataModel.setReviews(reviews);
                    if (!object.isNull("timeing")) {
                        JSONObject timing_obj = object.getJSONObject("timeing");
                        BudzMapHomeDataModel.Timing timing = new BudzMapHomeDataModel.Timing();
                        timing.setId(timing_obj.getInt("id"));
                        timing.setSub_user_id(timing_obj.getInt("sub_user_id"));
                        timing.setMonday(timing_obj.optString("monday"));
                        timing.setTuesday(timing_obj.optString("tuesday"));
                        timing.setWednesday(timing_obj.optString("wednesday"));
                        timing.setThursday(timing_obj.optString("thursday"));
                        timing.setFriday(timing_obj.optString("friday"));
                        timing.setSaturday(timing_obj.optString("saturday"));
                        timing.setSunday(timing_obj.optString("sunday"));
                        timing.setMon_end(timing_obj.optString("mon_end"));
                        timing.setTue_end(timing_obj.optString("tue_end"));
                        timing.setWed_end(timing_obj.optString("wed_end"));
                        timing.setThu_end(timing_obj.optString("thu_end"));
                        timing.setFri_end(timing_obj.optString("fri_end"));
                        timing.setSat_end(timing_obj.optString("sat_end"));
                        timing.setSun_end(timing_obj.optString("sun_end"));
                        timing.setCreated_at(timing_obj.optString("created_at"));
                        dataModel.setTimings(timing);
                    }

                    JSONArray images_array = object.getJSONArray("get_images");
                    ArrayList<BudzMapHomeDataModel.Images> images = new ArrayList<>();
                    for (int y = 0; y < images_array.length(); y++) {
                        JSONObject image_object = images_array.getJSONObject(y);
                        BudzMapHomeDataModel.Images img = new BudzMapHomeDataModel.Images();
                        img.setId(image_object.getInt("id"));
                        img.setUser_id(image_object.getInt("user_id"));
                        img.setImage_path(image_object.getString("image"));
                        img.setIs_approved(0);
                        img.setIs_main(0);
                        img.setCreated_at(image_object.getString("created_at"));
                        img.setUpdated_at(image_object.getString("updated_at"));
                        images.add(img);
                    }
                    dataModel.setImages(images);
                    budz_map_test_data.add(dataModel);
                }

                if (budz_map_test_data.size() > 0) {
                    Refresh.setVisibility(View.GONE);
                    recyler_adapter.notifyDataSetChanged();
                    no_record_found.setVisibility(View.GONE);
                    if (mMap != null) {
                        SetMarkers();
                    }

                } else {
//                    Refresh.setVisibility(View.GONE);
                    recyler_adapter.notifyDataSetChanged();
                    no_record_found.setVisibility(View.VISIBLE);
                }
                if (Filteration_ids.length() == 0) {
                    Refresh.setVisibility(View.GONE);
                    recyler_adapter.notifyDataSetChanged();
                    no_record_found.setVisibility(View.GONE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else if (apiActions == add_subscription) {
            Log.d("log", response);
            try {
                JSONObject jsonObject = new JSONObject(response).getJSONObject("successData");
                Intent intent = new Intent(getContext(), AddNewBudzMapActivity.class);
                intent.putExtra("isSubcribed", true);
                intent.putExtra("sub_user_id", jsonObject.getString("id"));
                startActivity(intent);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public static void RefreshData() {
        if (class_context != null) {
            pages = 0;
            Refresh.setVisibility(View.VISIBLE);
            JSONObject object = new JSONObject();
            isAppiCallActive = true;
            new VollyAPICall(class_context, false, URL.get_budz_map + "?skip=0&lat=" + lat + "&lng=" + lng + "&type=" + Filteration_ids, object, user.getSession_key(), Request.Method.GET, apiResponseListner, get_budz_map);
        }
    }

    @Override
    public void onRequestError(String response, APIActions.ApiActions apiActions) {
        Log.d("response", response);
        isAppiCallActive = false;
        Refresh.setVisibility(View.GONE);
        swipe_rf.setRefreshing(false);
    }

    public void SetMarkers() {
        mMap.clear();
        for (int x = 0; x < budz_map_test_data.size(); x++) {
            BudzMapHomeDataModel data = budz_map_test_data.get(x);
            Double latitude = data.getLat();
            Double longitude = data.getLng();
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject();
                jsonObject.put("position", x);
                jsonObject.put("title", data.getTitle());
                jsonObject.put("logo", data.getLogo());
                jsonObject.put("reviews", data.getReviews().size() + " Reviews");
                jsonObject.put("rating", data.getRating_sum());

                if (data.getTimings() != null) {
                    switch (getCurrentDat()) {
                        case "sunday":
                            if (data.getTimings().getSunday() != null && data.getTimings().getSunday().length() > 4 && !data.getTimings().getSunday().equalsIgnoreCase("Closed")) {
                                jsonObject.put("open_untill", "open until " + data.getTimings().getSun_end());
                            } else {
                                jsonObject.put("open_untill", "closed");
                            }
                            break;
                        case "monday":
                            if (data.getTimings().getMonday() != null && data.getTimings().getMonday().length() > 4 && !data.getTimings().getMonday().equalsIgnoreCase("Closed")) {
                                jsonObject.put("open_untill", "open until " + data.getTimings().getMon_end());
                            } else {
                                jsonObject.put("open_untill", "closed");
                            }
                            break;
                        case "tuesday":
                            if (data.getTimings().getTuesday() != null && data.getTimings().getTuesday().length() > 4 && !data.getTimings().getTuesday().equalsIgnoreCase("Closed")) {
                                jsonObject.put("open_untill", "open until " + data.getTimings().getTue_end());
                            } else {
                                jsonObject.put("open_untill", "closed");
                            }
                            break;
                        case "wednesday":
                            if (data.getTimings().getWednesday() != null && data.getTimings().getWednesday().length() > 4 && !data.getTimings().getWednesday().equalsIgnoreCase("Closed")) {
                                jsonObject.put("open_untill", "open until " + data.getTimings().getWed_end());
                            } else {
                                jsonObject.put("open_untill", "closed");
                            }

                            break;
                        case "thursday":
                            if (data.getTimings().getThursday() != null && data.getTimings().getThursday().length() > 4 && !data.getTimings().getThursday().equalsIgnoreCase("Closed")) {
                                jsonObject.put("open_untill", "open until " + data.getTimings().getThu_end());
                            } else {
                                jsonObject.put("open_untill", "closed");
                            }

                            break;
                        case "friday":
                            if (data.getTimings().getFriday() != null && data.getTimings().getFriday().length() > 4 && !data.getTimings().getFriday().equalsIgnoreCase("Closed")) {
                                jsonObject.put("open_untill", "open until " + data.getTimings().getFri_end());
                            } else {
                                jsonObject.put("open_untill", "closed");
                            }

                            break;
                        case "saturday":
                            if (data.getTimings().getSaturday() != null && data.getTimings().getSaturday().length() > 4 && !data.getTimings().getSaturday().equalsIgnoreCase("Closed")) {
                                jsonObject.put("open_untill", "open until " + data.getTimings().getSat_end());
                            } else {
                                jsonObject.put("open_untill", "closed");
                            }

                            break;

                    }
                } else {
                    jsonObject.put("open_untill", "closed");
                }
                switch (data.getBusiness_type_id()) {
                    case 1:
                        jsonObject.put("type", "Dispensary");
                        jsonObject.put("type_id", 1);
                        break;
                    case 2:
                    case 6:
                    case 7:
                        jsonObject.put("type", "Medical");
                        jsonObject.put("type_id", 2);
                        break;
                    case 3:
                        jsonObject.put("type", "Cannabites");
                        jsonObject.put("type_id", 3);
                        break;
                    case 4:
                    case 8:
                        jsonObject.put("type", "Entertainment");
                        jsonObject.put("type_id", 4);
                        break;
                    case 5:
                        jsonObject.put("type", "Events");
                        jsonObject.put("type_id", 5);
                        break;
                    case 9:
                        jsonObject.put("type", "Other");
                        jsonObject.put("type_id", 9);
                        break;
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (x == 0) {
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 15);
                mMap.moveCamera(cameraUpdate);
            }
            BitmapDescriptor bitmap;
            switch (data.getBusiness_type_id()) {
                case 1:
                    bitmap = BitmapDescriptorFactory.fromBitmap(getBitmapFromVectorDrawable(HealingBudApplication.getContext(), R.drawable.ic_dispancry_marker_icon));
                    if (bitmap != null) {
                        mMap.addMarker(new MarkerOptions()
                                .position(new LatLng(latitude, longitude))
                                .flat(true)
                                .title(jsonObject.toString())
//                            .icon(BitmapDescriptorFactory.fromBitmap(getBitmapFromVectorDrawable(HealingBudApplication.getContext(), R.drawable.ic_dispancry_marker_icon))));
                                .icon(bitmap));
                    }
                    break;
                case 2:
                case 6:
                case 7:
                    bitmap = BitmapDescriptorFactory.fromBitmap(getBitmapFromVectorDrawable(HealingBudApplication.getContext(), R.drawable.ic_medical_marker_icon));
                    if (bitmap != null) {
                        mMap.addMarker(new MarkerOptions()
                                .position(new LatLng(latitude, longitude))
                                .flat(true)
                                .title(jsonObject.toString())
//                            .icon(BitmapDescriptorFactory.fromBitmap(getBitmapFromVectorDrawable(HealingBudApplication.getContext(), R.drawable.ic_medical_marker_icon))));
                                .icon(bitmap));
                    }
                    break;
                case 3:
                    bitmap = BitmapDescriptorFactory.fromBitmap(getBitmapFromVectorDrawable(HealingBudApplication.getContext(), R.drawable.ic_cannabites_marker_icon));
                    if (bitmap != null) {
                        mMap.addMarker(new MarkerOptions()
                                .position(new LatLng(latitude, longitude))
                                .flat(true)
                                .title(jsonObject.toString())
//                            .icon(BitmapDescriptorFactory.fromBitmap(getBitmapFromVectorDrawable(HealingBudApplication.getContext(), R.drawable.ic_cannabites_marker_icon))));
                                .icon(bitmap));
                    }

                    break;
                case 4:
                case 8:
                    bitmap = BitmapDescriptorFactory.fromBitmap(getBitmapFromVectorDrawable(HealingBudApplication.getContext(), R.drawable.ic_entertainment_marker_icn));
                    if (bitmap != null) {
                        mMap.addMarker(new MarkerOptions()
                                .position(new LatLng(latitude, longitude))
                                .flat(true)
                                .title(jsonObject.toString())
//                            .icon(BitmapDescriptorFactory.fromBitmap(getBitmapFromVectorDrawable(HealingBudApplication.getContext(), R.drawable.ic_entertainment_marker_icn))));
                                .icon(bitmap));
                    }

                    break;
                case 5:
                    bitmap = BitmapDescriptorFactory.fromBitmap(getBitmapFromVectorDrawable(HealingBudApplication.getContext(), R.drawable.ic_events_marker_icon));
                    if (bitmap != null) {
                        mMap.addMarker(new MarkerOptions()
                                .position(new LatLng(latitude, longitude))
                                .flat(true)
                                .title(jsonObject.toString())
//                            .icon(BitmapDescriptorFactory.fromBitmap(getBitmapFromVectorDrawable(HealingBudApplication.getContext(), R.drawable.ic_events_marker_icon))));
                                .icon(bitmap));
                    }

                    break;
                case 9:
                    bitmap = BitmapDescriptorFactory.fromBitmap(getBitmapFromVectorDrawable(HealingBudApplication.getContext(), R.drawable.ic_other_marker_icon));
                    if (bitmap != null) {
                        mMap.addMarker(new MarkerOptions()
                                .position(new LatLng(latitude, longitude))
                                .flat(true)
                                .title(jsonObject.toString())
//                            .icon(BitmapDescriptorFactory.fromBitmap(getBitmapFromVectorDrawable(HealingBudApplication.getContext(), R.drawable.ic_events_marker_icon))));
                                .icon(bitmap));
                    }

                    break;
            }

        }
    }

    public static String getCurrentDat() {
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        Date d = new Date();
        String dayOfTheWeek = sdf.format(d);
        return dayOfTheWeek.toLowerCase();
    }

    @Override
    public void onLocationSuccess(Location location) {
        isDistance = true;
        userLAtitude = location.getLatitude();
        userLongitude = location.getLongitude();
        if (distance_switch != null) {
            distance_switch.setChecked(true);
        }
        lat = String.valueOf(userLAtitude);
        lng = String.valueOf(userLongitude);
//        if (lat.length() < 4) {
//            lat = userLAtitude + "";
//            lng = userLongitude + "";
//        }
//        if (userLongitude == 0 && userLAtitude == 0) {
//            userLAtitude = user.getLat();
//            userLongitude = user.getLng();
//        }
        String path = "";
        if (isKeywordSearch) {
            isKeywordSearch = false;
            isSearchedText = true;
            isSearchedTextValue = isKeywordSearch_string.trim();
            path = URL.get_budz_map + "?skip=0&lat=" + userLAtitude + "&lng=" + userLongitude + "&type=" + Filteration_ids + "&query=" + isKeywordSearch_string;
            isKeywordSearch_string = "";
        } else {
            isSearchedText = false;
            isSearchedTextValue = "";
            path = URL.get_budz_map + "?skip=0&lat=" + userLAtitude + "&lng=" + userLongitude + "&type=" + Filteration_ids;

        }

        JSONObject object = new JSONObject();
        pages = 0;
        new VollyAPICall(getContext(), false, path, object, user.getSession_key(), Request.Method.GET, BudzMapHomeFragment.this, get_budz_map);


    }

    @Override
    public void onLocationFailed(String Error) {
        isDistance = false;
        lat = user.getLat() + "";
        lng = user.getLng() + "";
        if (lat.equalsIgnoreCase("null") || lng.equalsIgnoreCase("null")) {
            lat = SharedPrefrences.getString("lat_cur", getActivity(), "0");
            lng = SharedPrefrences.getString("lng_cur", getActivity(), "0");

        } else if (lat.length() < 4 || lng.length() < 4) {
            lat = SharedPrefrences.getString("lat_cur", getActivity(), "0");
            lng = SharedPrefrences.getString("lng_cur", getActivity(), "0");

        }
        userLAtitude = Double.parseDouble(lat);
        userLongitude = Double.parseDouble(lng);
        String path = "";
        if (isKeywordSearch) {
            isKeywordSearch = false;
            isSearchedText = true;
            isSearchedTextValue = isKeywordSearch_string.trim();
            path = URL.get_budz_map + "?skip=0&lat=" + userLAtitude + "&lng=" + userLongitude + "&type=" + Filteration_ids + "&query=" + isKeywordSearch_string;
            isKeywordSearch_string = "";
        } else {
            isSearchedText = false;
            isSearchedTextValue = "";
            path = URL.get_budz_map + "?skip=0&lat=" + userLAtitude + "&lng=" + userLongitude + "&type=" + Filteration_ids;

        }
        if (distance_switch != null) {
            distance_switch.setChecked(false);
        }
        JSONObject object = new JSONObject();
        pages = 0;
        new VollyAPICall(getContext(), false, path, object, user.getSession_key(), Request.Method.GET, BudzMapHomeFragment.this, get_budz_map);

        Log.d("onLocationFailed: ", Error);
    }

    @Override
    public void TokenGenrate(Token token) {
        Log.d("token", token.toString());
        JSONObject object = new JSONObject();
        try {
            object.put("stripe_token", token.getId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        new VollyAPICall(getContext(), true, URL.add_subscription, object, user.getSession_key(), Request.Method.POST, BudzMapHomeFragment.this, add_subscription);
    }
}
