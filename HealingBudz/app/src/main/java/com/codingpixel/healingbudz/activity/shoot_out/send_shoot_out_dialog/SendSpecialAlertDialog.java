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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.codingpixel.healingbudz.DataModel.BudzMapHomeDataModel;
import com.codingpixel.healingbudz.DataModel.BudzMapSpecialProducts;
import com.codingpixel.healingbudz.R;
import com.codingpixel.healingbudz.Utilities.DateConverter;
import com.codingpixel.healingbudz.Utilities.GetUserLatLng;
import com.codingpixel.healingbudz.Utilities.Shareable;
import com.codingpixel.healingbudz.camera_activity.HBCameraActivity;
import com.codingpixel.healingbudz.customeUI.CustomeToast;
import com.codingpixel.healingbudz.customeUI.customalerts.SweetAlertDialog;
import com.codingpixel.healingbudz.customeUI.customspinner;
import com.codingpixel.healingbudz.data_structure.APIActions;
import com.codingpixel.healingbudz.interfaces.UserLocationListner;
import com.codingpixel.healingbudz.network.VollyAPICall;
import com.codingpixel.healingbudz.network.model.APIResponseListner;
import com.codingpixel.healingbudz.network.model.URL;

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
import static com.codingpixel.healingbudz.activity.home.home_fragment.budz_map_tab.BudzMapHomeFragment.budz_map_item_clickerd_dataModel;
import static com.codingpixel.healingbudz.activity.splash.Splash.user;
import static com.codingpixel.healingbudz.customeUI.ImageHelper.getRoundedCornerBitmap;
import static com.codingpixel.healingbudz.data_structure.APIActions.ApiActions.add_shout_out;
import static com.codingpixel.healingbudz.data_structure.APIActions.ApiActions.testAPI;
import static com.codingpixel.healingbudz.network.model.URL.getLocatioZipcode_part1;
import static com.codingpixel.healingbudz.network.model.URL.getLocatioZipcode_part2;
import static com.codingpixel.healingbudz.network.model.URL.getLocatioZipcode_part3;

public class SendSpecialAlertDialog extends BaseDialogFragment<SendSpecialAlertDialog.OnDialogFragmentClickListener> implements APIResponseListner, UserLocationListner {
    boolean isLike = true;
    boolean isShare = false;
    RadioGroup Location_radio_group;
    EditText Discription_text, Title_text;
    String ZipCode = "", Description;
    LinearLayout Send_Shoot_out, Valid_till, Add_Image;
    TextView Valid_till_date, Character_counter;
    ImageView Attached_Image;
    String Link_To = "";
    static BudzMapSpecialProducts dataModel;
    static boolean isEdit = false;
    String ImagePAth = "";
    AlertDialog dialog;
    static int pos = 0;
    public static ArrayList<BudzMapHomeDataModel> budzMapHomeDataModels;
    double latitude = 0, longitude = 0;
    String Location_name = "";
    customspinner link_to_spinner;
    ImageView twitter_share, fb_share;

    DatePickerDialog.OnDateSetListener mDateSetListener;

    static OnDialogFragmentClickListener Listener;

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
            new SweetAlertDialog(getActivity(), SweetAlertDialog.SUCCESS_TYPE)
                    .setTitleText("Special Added Successfully!")
                    .setContentText("")
                    .setConfirmText("Okay")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismissWithAnimation();

                            Listener.onSendShootOutButtonClick(SendSpecialAlertDialog.this);
                        }
                    }).show();
//            CustomeToast.ShowCustomToast(HealingBudApplication.getContext(), , Gravity.TOP);
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
        public void onSendShootOutButtonClick(SendSpecialAlertDialog dialog);

        public void onSendShootOutButtonClick(SendSpecialAlertDialog dialog, BudzMapSpecialProducts item, int pos);
    }

    public static SendSpecialAlertDialog newInstance(OnDialogFragmentClickListener listner) {
        SendSpecialAlertDialog frag = new SendSpecialAlertDialog();
        Bundle args = new Bundle();
        frag.setArguments(args);
        isEdit = false;
        Listener = listner;
        return frag;
    }

    public static SendSpecialAlertDialog newInstance(OnDialogFragmentClickListener listner, BudzMapSpecialProducts dataModel, int position) {
        SendSpecialAlertDialog frag = new SendSpecialAlertDialog();
        Bundle args = new Bundle();
        frag.setArguments(args);
        SendSpecialAlertDialog.dataModel = dataModel;
        Listener = listner;
        isEdit = true;
        pos = position;
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater factory = LayoutInflater.from(getContext());
        final View main_dialog = factory.inflate(R.layout.send_shoot_out_dialog_layout_special, null);
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
                        new VollyAPICall(getContext(), false, getLocatioZipcode_part1 + "USA" + getLocatioZipcode_part2 + user.getZip_code() + getLocatioZipcode_part3, object, null, Request.Method.POST, SendSpecialAlertDialog.this, testAPI);
                        break;
                    case R.id.current_location:
                        new GetUserLatLng().getUserLocation(getContext(), SendSpecialAlertDialog.this);
                        break;
                }
            }
        });
        Discription_text = view.findViewById(R.id.shoot_out_edit_text);
        Title_text = view.findViewById(R.id.shoot_out_edit_text_title);
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
                if (Title_text.getText().toString().trim().length() == 0) {
                    CustomeToast.ShowCustomToast(getContext(), "Enter Title...", Gravity.TOP);
                } else if (Discription_text.getText().toString().trim().length() == 0) {
                    CustomeToast.ShowCustomToast(getContext(), "Enter Description...", Gravity.TOP);
                } else if (Valid_till_date.getText().toString().contains("Valid")) {
                    CustomeToast.ShowCustomToast(getContext(), "Select Expiry Date...", Gravity.TOP);
                } else {
                    JSONObject jsonObject = new JSONObject();
                    try {
                        if (isEdit) {
                            jsonObject.put("id", dataModel.getId());
                            dataModel.setTitle(Title_text.getText().toString().trim());
                            dataModel.setMessage(Discription_text.getText().toString().trim());
                            dataModel.setValidity_date(DateConverter.validateFormat(Valid_till_date.getText().toString().trim()));
                            Listener.onSendShootOutButtonClick(SendSpecialAlertDialog.this, dataModel, pos);
                        }
                        jsonObject.put("budz_id", budz_map_item_clickerd_dataModel.getId());
                        jsonObject.put("title", Title_text.getText().toString());
                        jsonObject.put("description", Discription_text.getText().toString());
                        jsonObject.put("date", DateConverter.validateFormat(Valid_till_date.getText().toString().trim()));

                        new VollyAPICall(getContext(), true, URL.add_budz_special, jsonObject, user.getSession_key(), Request.Method.POST, SendSpecialAlertDialog.this, add_shout_out);
                    } catch (JSONException e) {
                        e.printStackTrace();
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
        final List<String> list = new ArrayList<>();
        list.add("Select One...");
        link_to_spinner.SetAdapter(list);
        if (isEdit) {
            Title_text.setText(dataModel.getTitle());
            Discription_text.setText(dataModel.getMessage());
            Valid_till_date.setText(DateConverter.getDateTime("yyyy-MM-dd", "MM/dd/yyyy", dataModel.getValidity_date()));
        }
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
}