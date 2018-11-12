package com.codingpixel.healingbudz.activity.home.home_fragment.Strain_tab.straindetail.tabs;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.codingpixel.healingbudz.DataModel.Budz;
import com.codingpixel.healingbudz.DataModel.StrainBudzMapDataModal;
import com.codingpixel.healingbudz.R;
import com.codingpixel.healingbudz.Utilities.Constants;
import com.codingpixel.healingbudz.Utilities.GetUserLatLng;
import com.codingpixel.healingbudz.activity.home.home_fragment.budz_map_tab.BudzMapDetailsActivity;
import com.codingpixel.healingbudz.activity.splash.Splash;
import com.codingpixel.healingbudz.adapter.StrainLocatBudTabRecylerAdapter;
import com.codingpixel.healingbudz.customeUI.CustomeToast;
import com.codingpixel.healingbudz.data_structure.APIActions;
import com.codingpixel.healingbudz.interfaces.UserLocationListner;
import com.codingpixel.healingbudz.network.VollyAPICall;
import com.codingpixel.healingbudz.network.model.APIResponseListner;
import com.codingpixel.healingbudz.network.model.URL;
import com.codingpixel.healingbudz.sharedprefrences.SharedPrefrences;
import com.codingpixel.healingbudz.static_function.IntentFunction;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

import static com.codingpixel.healingbudz.Utilities.GetUserLatLng.GetZipcode_AddressFromLatlng;
import static com.codingpixel.healingbudz.activity.home.home_fragment.Strain_tab.straindetail.StrainDetailsActivity.strainDataModel;
import static com.codingpixel.healingbudz.activity.splash.Splash.user;
import static com.codingpixel.healingbudz.data_structure.APIActions.ApiActions.get_user_strain;
import static com.codingpixel.healingbudz.network.model.URL.getLocatioZipcode_part1;
import static com.codingpixel.healingbudz.network.model.URL.getLocatioZipcode_part2;
import static com.codingpixel.healingbudz.network.model.URL.getLocatioZipcode_part3;
import static com.codingpixel.healingbudz.network.model.URL.images_baseurl;
import static com.codingpixel.healingbudz.static_function.UIModification.HideKeyboard;

@SuppressLint("ValidFragment")
public class StrainLocateThisBudTabFragment extends Fragment implements StrainLocatBudTabRecylerAdapter.ItemClickListener, APIResponseListner, LocationListener, UserLocationListner {
    RecyclerView recyclerView;
    LinearLayout Main_Layout, Refresh_layout, bud_view;
    TextView ZipCode, Address, location_text, display_text;
    ImageView user_image, user_image_topi;
    boolean isLocationGet = false;
    ArrayList<StrainBudzMapDataModal> strainBudzMapDataModals = new ArrayList<>();
    ArrayList<Budz> strainBudzMapDataModalsBudz = new ArrayList<>();
    private LocationManager locationManager = null;
    Context context;
    StrainLocatBudTabRecylerAdapter recyler_adapter;
    TextView find_my_location;

    @SuppressLint("ValidFragment")
    public StrainLocateThisBudTabFragment(Context context) {
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.strain_locate_this_bud_tab_activity, container, false);
        HideKeyboard(getActivity());
        Init(view);
        return view;
    }

    public void Init(View view) {
        location_text = view.findViewById(R.id.location_text);
        bud_view = view.findViewById(R.id.bud_view);
        bud_view.setVisibility(View.GONE);
        display_text = view.findViewById(R.id.display_text);
        user_image = view.findViewById(R.id.user_image);
        user_image_topi = view.findViewById(R.id.user_image_topi);
        user_image.setScaleType(ImageView.ScaleType.FIT_CENTER);
        user_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentFunction.GoToProfile(v.getContext(), Splash.user.getUser_id());
            }
        });
        String pathImage = "";
        if (Splash.user.getImage_path().contains("facebook.com") ||
                Splash.user.getImage_path().contains("google.com") ||
                Splash.user.getImage_path().contains("googleusercontent.com")
                || Splash.user.getImage_path().contains("http")
                || Splash.user.getImage_path().contains("https")) {
            pathImage = Splash.user.getImage_path();
        } else {
            if (Splash.user.getImage_path().length() > 5) {
                pathImage = images_baseurl + Splash.user.getImage_path();
            } else {
                pathImage = images_baseurl + Splash.user.getAvatar();
            }
        }
        com.bumptech.glide.Glide.with(context)
                .load(pathImage)
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .placeholder(R.drawable.image_plaecholder_bg).error(R.drawable.noimage)
                .fitCenter()
                .listener(new RequestListener<String, Bitmap>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        user_image.setImageBitmap(resource);
                        return false;
                    }
                })
//                .listener(new RequestListener<String, GlideDrawable>() {
//                    @Override
//                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
//                        return false;
//                    }
//
//                    @Override
//                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
//                        user_image.setImageDrawable(resource);
//                        return false;
//                    }
//                })
                .into(user_image);
        if (Splash.user.getSpecial_icon().length() > 6) {
            user_image_topi.setVisibility(View.VISIBLE);
            com.bumptech.glide.Glide.with(context)
                    .load(URL.images_baseurl + Splash.user.getSpecial_icon())
                    .asBitmap()
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)

                    .placeholder(R.drawable.image_plaecholder_bg).error(R.drawable.noimage)
                    .fitCenter()
                    .listener(new RequestListener<String, Bitmap>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            user_image_topi.setImageBitmap(resource);
                            return false;
                        }
                    })
//                    .listener(new RequestListener<String, GlideDrawable>() {
//                        @Override
//                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
//                            return false;
//                        }
//
//                        @Override
//                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
//                            user_image_topi.setImageDrawable(resource);
//                            return false;
//                        }
//                    })
                    .into(user_image_topi);
        } else {
            user_image_topi.setVisibility(View.GONE);
        }
        String loc = "within <strong>15 Miles</strong> of your Location";
        location_text.setText(Html.fromHtml(loc));
        location_text.setTextColor(Color.parseColor("#c4c4c4"));
        recyclerView = view.findViewById(R.id.locate_bud_recyler_view);
        RecyclerView.LayoutManager layoutManager_home_saerch = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager_home_saerch);
        recyler_adapter = new StrainLocatBudTabRecylerAdapter(context, strainBudzMapDataModalsBudz);
        recyclerView.setAdapter(recyler_adapter);
        recyler_adapter.setClickListener(this);
        Main_Layout = view.findViewById(R.id.main_layout);
        Main_Layout.setVisibility(View.VISIBLE);
        Refresh_layout = view.findViewById(R.id.refresh);
        Refresh_layout.setVisibility(View.GONE);
        ZipCode = view.findViewById(R.id.zip_code);
        if (user.getZip_code().length() > 2)
            ZipCode.setText(user.getZip_code() + "");
        else {
            ZipCode.setText("54000");
        }
        Address = view.findViewById(R.id.address);
        Address.setText(user.getLocation());
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10, 0, StrainLocateThisBudTabFragment.this);


        JSONObject jsonObject = new JSONObject();
        String lat = "", lng = "";
        lat = user.getLat() + "";
        lng = user.getLng() + "";
        if (lat == null || lng == null || lat.equalsIgnoreCase("NaN") || lat.length() < 3) {
            JSONObject object = new JSONObject();
            new VollyAPICall(context, true, getLocatioZipcode_part1 + "USA" + getLocatioZipcode_part2 + user.getZip_code() + getLocatioZipcode_part3, object, null, Request.Method.POST, StrainLocateThisBudTabFragment.this, APIActions.ApiActions.testAPI);
//            lat = SharedPrefrences.getString("user_laaatt", context);
//            lng = SharedPrefrences.getString("user_lnggggg", context);
//            new VollyAPICall(context, false, URL.locate_strain_budz + "?strain_id=" + strainDataModel.getId() + "&lat=" + lat + "&lng=" + lng + "&skip=0", jsonObject, user.getSession_key(), Request.Method.GET, StrainLocateThisBudTabFragment.this, get_user_strain);
        } else {
            JSONObject object = new JSONObject();
//            new VollyAPICall(context, false, getLocatioZipcode_part1 + "USA" + getLocatioZipcode_part2 + user.getZip_code() + getLocatioZipcode_part3, object, null, Request.Method.POST, StrainLocateThisBudTabFragment.this, APIActions.ApiActions.testAPI);
            lat = user.getLat() + "";
            lng = user.getLng() + "";
//            JSONObject object = new JSONObject();
            new VollyAPICall(context, true, getLocatioZipcode_part1 + "USA" + getLocatioZipcode_part2 + user.getZip_code() + getLocatioZipcode_part3, object, null, Request.Method.POST, StrainLocateThisBudTabFragment.this, APIActions.ApiActions.testAPI);

        }


        find_my_location = view.findViewById(R.id.find_my_location);
        find_my_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new GetUserLatLng().getUserLocation(context, StrainLocateThisBudTabFragment.this);
            }
        });
    }

    @Override
    public void onItemClick(View view, int position) {
//        StrainBudzMapDataModal dataModel = strainBudzMapDataModals.get(position);
//        String budz_type = dataModel.getStrain_type() + "";
        Intent i = new Intent(context, BudzMapDetailsActivity.class);
//        i.putExtra("budz_type", budz_type);
        i.putExtra("budzmap_id", strainBudzMapDataModalsBudz.get(position).getSubUserId());
        startActivity(i);
    }

    @Override
    public void onRequestSuccess(String response, APIActions.ApiActions apiActions) {
        Log.d("response", response);
        if (apiActions == APIActions.ApiActions.testAPI) {
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(response);
                JSONArray jsonArray = jsonObject.getJSONArray("results");
                if (jsonArray.length() > 0) {
                    JSONObject object = jsonArray.getJSONObject(0);
                    String Location_Name = object.getString("formatted_address");
                    JSONObject location_object = object.getJSONObject("geometry").getJSONObject("location");
                    Double latitude = location_object.getDouble("lat");
                    Double longitude = location_object.getDouble("lng");
//TODO FOR CHECK HERE
//                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response);
                        JSONArray jsonArraya = jsonObject.getJSONArray("results");
                        int stst = -1;
                        if (jsonArraya.length() > 0) {
                            JSONArray array = jsonArraya.getJSONObject(0).getJSONArray("address_components");
                            for (int i = 0; i < array.length(); i++) {
                                String name = array.getJSONObject(i).getString("long_name");
                                for (int j = 0; j < Constants.stateList.length; j++) {
                                    if (Constants.stateList[j].equalsIgnoreCase(name)) {

                                        stst = 1;
                                    }
                                }
                            }
                            if (stst == -1) {
                                //TODO FOR CHECK HERE
                                display_text.setText("Sorry we don't offer strains in illegal state");
                                location_text.setText("");
                            } else {
                                new VollyAPICall(context, true, URL.locate_strain_budz + "?strain_id=" + strainDataModel.getId() + "&lat=" + latitude + "&lng=" + longitude + "&skip=0", new JSONObject(), user.getSession_key(), Request.Method.GET, StrainLocateThisBudTabFragment.this, get_user_strain);
                            }

                        } else {
                            display_text.setText("Sorry we don't offer strains in illegal state");
                            location_text.setText("");
                            //TODO FOR CHECK HERE
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        display_text.setText("Sorry we don't offer strains in illegal state");
                        location_text.setText("");
                        //TODO FOR CHECK HERE
                    }
//                    new VollyAPICall(context, false, URL.locate_strain_budz + "?strain_id=" + strainDataModel.getId() + "&lat=" + latitude + "&lng=" + longitude + "&skip=0", new JSONObject(), user.getSession_key(), Request.Method.GET, StrainLocateThisBudTabFragment.this, get_user_strain);
                } else {
//                    new GetUserLatLng().getUserLocation(Objects.requireNonNull(context), StrainLocateThisBudTabFragment.this);
                    CustomeToast.ShowCustomToast(context, "We are not able to find your current location!", Gravity.TOP);
                    display_text.setText("We are not able to find your current location!");
                    location_text.setText("");
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else {
            Refresh_layout.setVisibility(View.GONE);
            Main_Layout.setVisibility(View.VISIBLE);
            strainBudzMapDataModals.clear();
            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONArray jsonArray = jsonObject.getJSONObject("successData").getJSONArray("budz");
                strainBudzMapDataModalsBudz.clear();
                strainBudzMapDataModalsBudz.addAll(Arrays.asList(new Gson().fromJson(jsonArray.toString(), Budz[].class)));

                if (strainBudzMapDataModalsBudz.size() > 0) {
                    bud_view.setVisibility(View.VISIBLE);
                    String loc = "within <strong>15 Miles</strong> of your Location";
                    location_text.setText(Html.fromHtml(loc));
                    location_text.setTextColor(Color.parseColor("#c4c4c4"));
                } else {
                    bud_view.setVisibility(View.GONE);
                    display_text.setText("Sorry there is no strain available in this state");
                    location_text.setText("");
                }
                recyler_adapter.notifyDataSetChanged();
                if (location != null) {
                    String loc = GetZipcode_AddressFromLatlng(context, location.getLatitude(), location.getLongitude());
                    if (loc.contains("&&&---&&&")) {
                        if (loc.split("&&&---&&&").length > 0) {
                            if (!loc.split("&&&---&&&")[1].equalsIgnoreCase("null")) {
                                ZipCode.setText(loc.split("&&&---&&&")[1]);
                                CustomeToast.ShowCustomToast(context, "Bud Found Successfully!", Gravity.TOP);
                            } else {
                                CustomeToast.ShowCustomToast(context, "We are not able to find your current location!", Gravity.TOP);
                            }
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onRequestError(String response, APIActions.ApiActions apiActions) {
        Log.d("response", response);
        try {
            JSONObject object = new JSONObject(response);
            CustomeToast.ShowCustomToast(context, object.getString("errorMessage"), Gravity.TOP);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        SharedPrefrences.setString("user_laaatt", location.getLatitude() + "", context);
        SharedPrefrences.setString("user_lnggggg", location.getLongitude() + "", context);
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

    private Location location;

    @Override
    public void onLocationSuccess(Location location) {
        this.location = location;
        JSONObject jsonObject = new JSONObject();
        String loc = GetZipcode_AddressFromLatlng(context, location.getLatitude(), location.getLongitude());
        if (loc.contains("&&&---&&&")) {
            if (loc.split("&&&---&&&").length > 0) {
                if (!loc.split("&&&---&&&")[1].equalsIgnoreCase("null")) {
                    ZipCode.setText(loc.split("&&&---&&&")[1]);
                    new VollyAPICall(context, true, getLocatioZipcode_part1 + "USA" + getLocatioZipcode_part2 + loc.split("&&&---&&&")[1] + getLocatioZipcode_part3, new JSONObject(), null, Request.Method.POST, StrainLocateThisBudTabFragment.this, APIActions.ApiActions.testAPI);
                    CustomeToast.ShowCustomToast(context, "Bud Found Successfully!", Gravity.TOP);
                } else {
                    CustomeToast.ShowCustomToast(context, "We are not able to find your current location!", Gravity.TOP);
                }
            }
        }
//        String lat = "", lng = "";
//        lat = location.getLatitude() + "";
//        lng = location.getLongitude() + "";
//        //TODO HERE FOR
//        new VollyAPICall(context, true, URL.locate_strain_budz + "?strain_id=" + strainDataModel.getId() + "&lat=" + lat + "&lng=" + lng + "&skip=0", jsonObject, user.getSession_key(), Request.Method.GET, StrainLocateThisBudTabFragment.this, get_user_strain);
    }

    @Override
    public void onLocationFailed(String Error) {

    }

//    private String getZipCode(Double latitude, Double longitude) {
//        String zipcode = null;
//        try {
//            LatLng latLng = new LatLng(latitude,longitude);
////            latLng.setLat(BigDecimal.valueOf(latitude));
////            latLng.setLng(BigDecimal.valueOf(longitude));
//            final Geocoder geocoder = new Geocoder(HealingBudApplication.context);
//            GeocoderRequest geocoderRequest = new GeocoderRequestBuilder().setLocation(latLng).getGeocoderRequest();
//            GeocodeResponse geocoderResponse = geocoder.geocode(geocoderRequest);
//            List<GeocoderResult> results = geocoderResponse.getResults();
////            logger.debug("results :  " + results); //This will print geographical information
//            List<GeocoderAddressComponent> geList = results.get(0).getAddressComponents();
//            if (geList.get(geList.size() - 1).getTypes().get(0).trim().equalsIgnoreCase("postal&#95;code")) {
//                zipcode = geList.get(geList.size() - 1).getLongName();
//            } else if (geList.get(0).getTypes().get(0).trim().equalsIgnoreCase("postal&#95;code")) {
//                zipcode = geList.get(0).getLongName();
//            }
//            Log.d("zipcode :  ", "" + zipcode);
//        } catch (Exception e) {
//            Log.d("zipcode :  ", "" + e.getLocalizedMessage());
//        }
//        return zipcode;
//    }
}
