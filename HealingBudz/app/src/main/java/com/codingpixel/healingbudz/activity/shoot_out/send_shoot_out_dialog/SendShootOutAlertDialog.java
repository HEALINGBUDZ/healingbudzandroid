package com.codingpixel.healingbudz.activity.shoot_out.send_shoot_out_dialog;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.codingpixel.healingbudz.DataModel.BudzMapHomeDataModel;
import com.codingpixel.healingbudz.R;
import com.codingpixel.healingbudz.Utilities.DateConverter;
import com.codingpixel.healingbudz.Utilities.GetUserLatLng;
import com.codingpixel.healingbudz.Utilities.Shareable;
import com.codingpixel.healingbudz.application.HealingBudApplication;
import com.codingpixel.healingbudz.camera_activity.HBCameraActivity;
import com.codingpixel.healingbudz.customeUI.CustomeToast;
import com.codingpixel.healingbudz.customeUI.customspinner;
import com.codingpixel.healingbudz.data_structure.APIActions;
import com.codingpixel.healingbudz.interfaces.UserLocationListner;
import com.codingpixel.healingbudz.network.VollyAPICall;
import com.codingpixel.healingbudz.network.model.APIResponseListner;
import com.codingpixel.healingbudz.network.model.URL;
import com.codingpixel.healingbudz.network.upload_file_with_progress.UploadFileWithProgress;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static com.codingpixel.healingbudz.Utilities.GetUserLatLng.GetZipcode_AddressFromLatlng;
import static com.codingpixel.healingbudz.Utilities.ViewUtils.checkRotation;
import static com.codingpixel.healingbudz.activity.splash.Splash.user;
import static com.codingpixel.healingbudz.customeUI.ImageHelper.getRoundedCornerBitmap;
import static com.codingpixel.healingbudz.data_structure.APIActions.ApiActions.add_shout_out;
import static com.codingpixel.healingbudz.data_structure.APIActions.ApiActions.testAPI;
import static com.codingpixel.healingbudz.network.model.URL.getLocatioZipcode_part1;
import static com.codingpixel.healingbudz.network.model.URL.getLocatioZipcode_part2;
import static com.codingpixel.healingbudz.network.model.URL.getLocatioZipcode_part3;

public class SendShootOutAlertDialog extends BaseDialogFragment<SendShootOutAlertDialog.OnDialogFragmentClickListener> implements APIResponseListner, UserLocationListner {
    boolean isLike = true;
    boolean isShare = false;
    RadioGroup Location_radio_group;
    EditText Discription_text;
    String ZipCode = "", Description;
    LinearLayout Send_Shoot_out, Valid_till, Add_Image;
    TextView Valid_till_date, Character_counter;
    ImageView Attached_Image;
    String Link_To = "";
    String Link_To_with = "";
    String ImagePAth = "";
    AlertDialog dialog;
    public static ArrayList<BudzMapHomeDataModel> budzMapHomeDataModels;
    double latitude = 0, longitude = 0;
    String Location_name = "";
    customspinner link_to_spinner, link_to_with_spinner;
    ImageView twitter_share, fb_share;

    DatePickerDialog.OnDateSetListener mDateSetListener;

    static OnDialogFragmentClickListener Listener;
    private int indexMain;

    @Override
    public void onRequestSuccess(String response, APIActions.ApiActions apiActions) {
        Log.d("resposne", response);
        if (apiActions == testAPI) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONArray jsonArray = jsonObject.getJSONArray("results");
                if (jsonArray.length() > 0) {
                    JSONObject object = jsonArray.getJSONObject(0);
                    String Location_Name = object.getString("formatted_address");
                    JSONObject location_object = object.getJSONObject("geometry").getJSONObject("location");
                    Double latitude = location_object.getDouble("lat");
                    Double longitude = location_object.getDouble("lng");
                    this.latitude = latitude;
                    this.longitude = longitude;

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            dialog.dismiss();
            CustomeToast.ShowCustomToast(HealingBudApplication.getContext(), "Shout Out Sent Successfully!", Gravity.TOP);
            Listener.onSendShootOutButtonClick(SendShootOutAlertDialog.this);
        }
    }

    @Override
    public void onRequestError(String response, APIActions.ApiActions apiActions) {
        Log.d("resposne", response);
        dialog.dismiss();
    }

    @Override
    public void onLocationSuccess(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        String loc = GetZipcode_AddressFromLatlng(getContext(), location.getLatitude(), location.getLongitude());
        if (loc.contains("&&&---&&&")) {
            if (loc.split("&&&---&&&").length > 0) {
                Location_name = loc.split("&&&---&&&")[0];
                ZipCode = loc.split("&&&---&&&")[1];
            }
        }
        if (ZipCode.equalsIgnoreCase("null") || ZipCode.length() < 2) {
            ZipCode = "54000";
        }
    }

    @Override
    public void onLocationFailed(String Error) {
        ZipCode = user.getZip_code();
        if (ZipCode.length() < 2) {
            ZipCode = "54000";
        }
        latitude = user.getLat();
        longitude = user.getLng();
        Location_name = user.getLocation();
    }

    public interface OnDialogFragmentClickListener {
        public void onSendShootOutButtonClick(SendShootOutAlertDialog dialog);
    }

    public static SendShootOutAlertDialog newInstance(OnDialogFragmentClickListener listner, ArrayList<BudzMapHomeDataModel> budzMapDataModels) {
        SendShootOutAlertDialog frag = new SendShootOutAlertDialog();
        Bundle args = new Bundle();
        frag.setArguments(args);
        Listener = listner;
        budzMapHomeDataModels = new ArrayList<>();
        budzMapHomeDataModels.clear();
        budzMapHomeDataModels.addAll(budzMapDataModels);
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater factory = LayoutInflater.from(getContext());
        final View main_dialog = factory.inflate(R.layout.send_shoot_out_dialog_layout, null);
        dialog = new AlertDialog.Builder(getContext()).create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        WindowManager.LayoutParams wmlp = dialog.getWindow().getAttributes();
        wmlp.gravity = Gravity.CENTER;
//        int top_y = getResources().getDisplayMetrics().heightPixels / 5;
//        wmlp.y = top_y;   //y position
        Button Learn_more = main_dialog.findViewById(R.id.learn_more_btn);
        final ImageView premium_tag = main_dialog.findViewById(R.id.premium_tag);

        ImageView Cross_btn = main_dialog.findViewById(R.id.cross_btn);
        Cross_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        InitContetn(main_dialog, dialog);
        dialog.setView(main_dialog);
        return dialog;
    }

    public void InitContetn(View view, final AlertDialog dialog) {

        fb_share = view.findViewById(R.id.fb_share);
        twitter_share = view.findViewById(R.id.twitter_share);
        Location_radio_group = view.findViewById(R.id.location_radio_btn);
        fb_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Shareable shareAction = new Shareable.Builder(view.getContext())
                        .message(Discription_text.getText().toString())
                        .url("http://139.162.37.73/healingbudz/")
                        .socialChannel(Shareable.Builder.FACEBOOK)
                        .build();
                shareAction.share();
            }
        });
        twitter_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Shareable shareAction = new Shareable.Builder(view.getContext())
                        .message(Discription_text.getText().toString())
                        .url("http://139.162.37.73/healingbudz/")
                        .socialChannel(Shareable.Builder.TWITTER)
                        .build();
                shareAction.share();
            }
        });
        Location_radio_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.zip_code:
                        ZipCode = user.getZip_code();
                        latitude = user.getLat();
                        longitude = user.getLng();
                        Location_name = user.getLocation();
                        JSONObject object = new JSONObject();
                        new VollyAPICall(getContext(), false, getLocatioZipcode_part1 + "USA" + getLocatioZipcode_part2 + user.getZip_code() + getLocatioZipcode_part3, object, null, Request.Method.POST, SendShootOutAlertDialog.this, testAPI);
                        break;
                    case R.id.current_location:
                        new GetUserLatLng().getUserLocation(getContext(), SendShootOutAlertDialog.this);
                        break;
                }
            }
        });
        Discription_text = view.findViewById(R.id.shoot_out_edit_text);
        Character_counter = view.findViewById(R.id.character_counter);
        Discription_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Character_counter.setText((charSequence.length()) + "/140 characters");

            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        Send_Shoot_out = view.findViewById(R.id.send_shoot_out_btn);


        Valid_till = view.findViewById(R.id.valid_till);
        Valid_till.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                DatePickerDialog abc = new DatePickerDialog(getContext(), R.style.DatePickerTheme, mDateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                abc.getDatePicker().setMinDate(calendar.getTimeInMillis());
                abc.show();

            }
        });
        Valid_till_date = view.findViewById(R.id.valid_till_date);
        Send_Shoot_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ZipCode.length() == 0) {
                    CustomeToast.ShowCustomToast(getContext(), "Select Location!", Gravity.TOP);
                } else if (Discription_text.getText().length() == 0) {
                    CustomeToast.ShowCustomToast(getContext(), "Enter Description...", Gravity.TOP);
                } else if (Valid_till_date.getText().toString().contains("Valid")) {
                    CustomeToast.ShowCustomToast(getContext(), "Select Expiry Date...", Gravity.TOP);
                } else if (Link_To.contains("Select One...")) {
                    CustomeToast.ShowCustomToast(getContext(), "Please link budz adz!", Gravity.TOP);
                } else {
                    if (ImagePAth.length() == 0) {
                        JSONObject jsonObject = new JSONObject();
                        try {
                            if (!Link_To.contains("Select One...")) {
                                jsonObject.put("sub_user_id", getLinkedBudzMapID(Link_To));
                            }
                            //budz_special_id
                            if (!Link_To_with.contains("Select Special...")) {
                                jsonObject.put("budz_special_id", getLinkedWithBudzMapID(Link_To_with, budzMapHomeDataModels.get(indexMain)));
                            }
                            jsonObject.put("title", Discription_text.getText().toString());
                            jsonObject.put("message", Discription_text.getText().toString());
                            jsonObject.put("validity_date", DateConverter.validateFormat(Valid_till_date.getText().toString().trim()));
                            jsonObject.put("lat", latitude + "");
                            jsonObject.put("lng", longitude + "");
                            jsonObject.put("public_location", Location_name);
                            new VollyAPICall(getContext(), true, URL.add_shout_out, jsonObject, user.getSession_key(), Request.Method.POST, SendShootOutAlertDialog.this, add_shout_out);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        JSONArray parameters = new JSONArray();
                        if (!Link_To.contains("Select One...")) {
                            parameters.put("sub_user_id");
                        }
                        parameters.put("title");
                        parameters.put("message");
                        parameters.put("validity_date");
                        parameters.put("lat");
                        parameters.put("lng");
                        parameters.put("public_location");
                        parameters.put("budz_special_id");

                        JSONArray parameters_values = new JSONArray();
                        if (!Link_To.contains("Select Special...")) {
                            parameters_values.put(getLinkedBudzMapID(Link_To));
                        }//budz_special_id
                        parameters_values.put(Discription_text.getText().toString());
                        parameters_values.put(Discription_text.getText().toString());
                        parameters_values.put(DateConverter.validateFormat(Valid_till_date.getText().toString().trim()));
                        parameters_values.put(latitude + "");
                        parameters_values.put(longitude + "");
                        parameters_values.put(Location_name);
                        if (!Link_To_with.contains("Select Special...")) {
                            parameters_values.put(getLinkedWithBudzMapID(Link_To_with, budzMapHomeDataModels.get(indexMain)));
                        }
                        new UploadFileWithProgress(getContext(), true, URL.add_shout_out, ImagePAth, "image", parameters_values, parameters, null, SendShootOutAlertDialog.this, add_shout_out).execute();
                    }

                }
            }
        });

        mDateSetListener =
                new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        SimpleDateFormat parseFormat = new SimpleDateFormat("MM/dd/yyyy");
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(year, monthOfYear, dayOfMonth);
                        Date date = calendar.getTime();
//                        Date date = new Date(year, monthOfYear, dayOfMonth);

                        String s = parseFormat.format(date);
                        Valid_till_date.setText(s);
                    }
                };

        Add_Image = view.findViewById(R.id.attatch_image);
        Add_Image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), HBCameraActivity.class);
                intent.putExtra("isVideoCaptureAble", false);
                startActivityForResult(intent, 1200);
            }
        });
        Attached_Image = view.findViewById(R.id.image_attach);

        link_to_spinner = view.findViewById(R.id.link_to);
        link_to_with_spinner = view.findViewById(R.id.link_to_with);
        final List<String> list = new ArrayList<>(), listNext = new ArrayList<>();
        list.add("Select One...");
        for (int x = 0; x < budzMapHomeDataModels.size(); x++) {
            if (!budzMapHomeDataModels.get(x).getTitle().equalsIgnoreCase("null")) {
                list.add(budzMapHomeDataModels.get(x).getTitle());
            }
        }
        link_to_spinner.SetAdapter(list);
        if (budzMapHomeDataModels.size() == 1 || budzMapHomeDataModels.size() > 1) {
            link_to_spinner.spinner.setSelection(1);
            link_to_spinner.SetValue();
            Link_To = list.get(1);
            listNext.clear();
            listNext.add("Select Special...");
            for (int x = 0; x < budzMapHomeDataModels.get(0).getSpecialProducts().size(); x++) {
                listNext.add(budzMapHomeDataModels.get(0).getSpecialProducts().get(x).getTitle());
            }

            link_to_with_spinner.SetAdapter(listNext);
            indexMain = 0;
        }
        link_to_spinner.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                link_to_spinner.SetValue();
                Link_To = list.get(position);
                if (position > 0) {
                    indexMain = position - 1;
                    listNext.clear();
                    listNext.add("Select Special...");
                    for (int x = 0; x < budzMapHomeDataModels.get(indexMain).getSpecialProducts().size(); x++) {
                        listNext.add(budzMapHomeDataModels.get(indexMain).getSpecialProducts().get(x).getTitle());
                    }

                    link_to_with_spinner.SetAdapter(listNext);
                } else {
                    indexMain = 0;
                    listNext.clear();
                    listNext.add("Select Special...");
                    for (int x = 0; x < budzMapHomeDataModels.get(indexMain).getSpecialProducts().size(); x++) {
                        listNext.add(budzMapHomeDataModels.get(indexMain).getSpecialProducts().get(x).getTitle());
                    }

                    link_to_with_spinner.SetAdapter(listNext);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        link_to_with_spinner.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                link_to_with_spinner.SetValue();
                Link_To_with = listNext.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 1200) {
            ImagePAth = data.getExtras().getString("file_path_arg");
            Log.d("paths", data.getExtras().getString("file_path_arg"));
            Bitmap bitmapOrg = BitmapFactory.decodeFile(data.getExtras().getString("file_path_arg"));
            bitmapOrg = checkRotation(bitmapOrg, data.getExtras().getString("file_path_arg"));
            bitmapOrg = Bitmap.createScaledBitmap(bitmapOrg, 300, 300, false);
            int corner_radious = (bitmapOrg.getWidth() * 10) / 100;
            Bitmap bitmap = getRoundedCornerBitmap(bitmapOrg, corner_radious);
            Drawable drawable = new BitmapDrawable(getResources(), bitmap);
            Attached_Image.setBackground(drawable);
            Attached_Image.setImageDrawable(null);
        }
    }

    public int getLinkedBudzMapID(String title) {
        int id = -1;
        for (int x = 0; x < budzMapHomeDataModels.size(); x++) {
            if (title.equalsIgnoreCase(budzMapHomeDataModels.get(x).getTitle())) {
                id = budzMapHomeDataModels.get(x).getId();
                break;
            }
        }
        return id;
    }

    public int getLinkedWithBudzMapID(String title, BudzMapHomeDataModel model) {
        int id = -1;
        for (int x = 0; x < model.getSpecialProducts().size(); x++) {
            if (title.equalsIgnoreCase(model.getSpecialProducts().get(x).getTitle())) {
                id = model.getSpecialProducts().get(x).getId();
                break;
            }
        }
        return id;
    }
}