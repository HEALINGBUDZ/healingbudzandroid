package com.codingpixel.healingbudz.activity.home.side_menu.profile.dialog;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.codingpixel.healingbudz.R;
import com.codingpixel.healingbudz.application.HealingBudApplication;
import com.codingpixel.healingbudz.customeUI.CustomeToast;
import com.codingpixel.healingbudz.data_structure.APIActions;
import com.codingpixel.healingbudz.network.VollyAPICall;
import com.codingpixel.healingbudz.network.model.APIResponseListner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.codingpixel.healingbudz.activity.splash.Splash.user;
import static com.codingpixel.healingbudz.customeUI.CustomeToast.ShowCustomToast;
import static com.codingpixel.healingbudz.network.model.URL.change_password;
import static com.codingpixel.healingbudz.network.model.URL.getLocatioZipcode_part1;
import static com.codingpixel.healingbudz.network.model.URL.getLocatioZipcode_part2;
import static com.codingpixel.healingbudz.network.model.URL.getLocatioZipcode_part3;
import static com.codingpixel.healingbudz.network.model.URL.update_bio;
import static com.codingpixel.healingbudz.network.model.URL.update_email;
import static com.codingpixel.healingbudz.network.model.URL.update_name;
import static com.codingpixel.healingbudz.network.model.URL.update_zip;
import static com.codingpixel.healingbudz.static_function.UIModification.HideKeyboard;

public class EditUserProfileUpdatePersonalInfoAlertDialog extends BaseDialogFragment<EditUserProfileUpdatePersonalInfoAlertDialog.OnDialogFragmentClickListener> implements APIResponseListner {
    Button Save_Btn;
    ImageView Cross_btn;
    EditText Edit_Bio, Email, Confirm_Email, Password, Confirm_Password, Zipcode, USERNAME;
    static String Dialog_type;
    static JSONObject object;
    static OnDialogFragmentClickListener Listener;
    String api_url = "";
    JSONObject api_object;

    @Override
    public void onRequestSuccess(String response, APIActions.ApiActions apiActions) {
        Log.d("response", response);
        if (apiActions == APIActions.ApiActions.update_user_email) {
            SaveButtonAction();
        } else if (apiActions == APIActions.ApiActions.testAPI) {
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
                    api_object.put("lat", latitude);
                    api_object.put("lng", longitude);
                    SaveButtonAction();
                    new VollyAPICall(getContext(), false, api_url, api_object, user.getSession_key(), Request.Method.POST, EditUserProfileUpdatePersonalInfoAlertDialog.this, APIActions.ApiActions.update_profile_info);
                } else {
                    CustomeToast.ShowCustomToast(HealingBudApplication.getContext(), "Invalid Zip Code!", Gravity.TOP);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else if (apiActions == APIActions.ApiActions.update_profile_info) {
            SaveButtonAction();
        }
    }

    @Override
    public void onRequestError(String response, APIActions.ApiActions apiActions) {
        Log.d("response", response);
        if (apiActions == APIActions.ApiActions.testAPI) {
            CustomeToast.ShowCustomToast(getContext(), response, Gravity.TOP);
        }else if (apiActions == APIActions.ApiActions.update_profile_info) {
            try {
                JSONObject object = new JSONObject(response);
                CustomeToast.ShowCustomToast(getContext(), object.getString("errorMessage"), Gravity.TOP);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            try {
                JSONObject object = new JSONObject(response);
                CustomeToast.ShowCustomToast(getContext(), object.getString("errorMessage"), Gravity.TOP);
                if (apiActions == APIActions.ApiActions.update_user_email) {
//                SaveButtonAction();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    public interface OnDialogFragmentClickListener {
        void onSaveButtonClick(EditUserProfileUpdatePersonalInfoAlertDialog dialog, JSONObject data);

        void onCrossButtonClick(EditUserProfileUpdatePersonalInfoAlertDialog dialog);
    }

    public static EditUserProfileUpdatePersonalInfoAlertDialog newInstance(OnDialogFragmentClickListener listner, String dialog_type, JSONObject data_object) {
        EditUserProfileUpdatePersonalInfoAlertDialog frag = new EditUserProfileUpdatePersonalInfoAlertDialog();
        Bundle args = new Bundle();
        frag.setArguments(args);
        Listener = listner;
        Dialog_type = dialog_type;
        object = data_object;
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater factory = LayoutInflater.from(getContext());
        final View main_dialog = factory.inflate(R.layout.edit_user_profile_personalinfo_dialog_layout, null);
        final AlertDialog dialog = new AlertDialog.Builder(getContext()).create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        WindowManager.LayoutParams wmlp = dialog.getWindow().getAttributes();
        Save_Btn = main_dialog.findViewById(R.id.save_btn);
        Save_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HideKeyboard(getActivity());

                api_object = new JSONObject();
                try {

                    api_url = "";
                    if (Dialog_type.equalsIgnoreCase("BIO")) {
//                        if (Edit_Bio.getText().toString().length() == 0) {
//                            CustomeToast.ShowCustomToast(HealingBudApplication.getContext(), "Field Cannot be Empty!", Gravity.CENTER);
//                            return;
//                        }
                        api_object.put("bio", Edit_Bio.getText().toString());
                        api_url = update_bio;
                    } else if (Dialog_type.equalsIgnoreCase("Email")) {
                        api_object.put("email", Email.getText().toString().trim());
                        api_url = update_email;
                        if (!Email.getText().toString().trim().equalsIgnoreCase(Confirm_Email.getText().toString().trim())) {
                            CustomeToast.ShowCustomToast(getContext(), "Email not match!", Gravity.TOP);
                            return;
                        }
                        if (Email.getText().toString().trim().equalsIgnoreCase(object.getString("email"))) {
                            SaveButtonAction();
                            return;
                        }
                        if (Email.getText().toString().trim().length() == 0) {
                            CustomeToast.ShowCustomToast(HealingBudApplication.getContext(), "Field Cannot be Empty!", Gravity.TOP);
                            return;
                        }
                    } else if (Dialog_type.equalsIgnoreCase("Password")) {
                        api_object.put("password", Password.getText().toString());
                        api_url = change_password;
                        if (!Password.getText().toString().trim().equalsIgnoreCase(Confirm_Password.getText().toString().trim())) {
                            CustomeToast.ShowCustomToast(getContext(), "Password not match!", Gravity.TOP);
                            return;
                        }
                        if (Password.getText().toString().trim().equalsIgnoreCase(object.getString("password"))) {
                            SaveButtonAction();
                            return;
                        }
                        if (Password.getText().toString().trim().length() == 0) {
                            CustomeToast.ShowCustomToast(HealingBudApplication.getContext(), "Field Cannot be Empty!", Gravity.TOP);
                            return;
                        }
                        if (Password.getText().toString().trim().length() < 6) {
                            ShowCustomToast(HealingBudApplication.getContext(), "Enter Password must be 6 character!", Gravity.TOP);
                            return;

                        }

                    } else if (Dialog_type.equalsIgnoreCase("Zipcode")) {
                        api_object.put("zip", Zipcode.getText().toString());
                        api_url = update_zip;
                        if (Zipcode.getText().toString().trim().equalsIgnoreCase(object.getString("zipcode"))) {
                            SaveButtonAction();
                            return;
                        }
                        if (Zipcode.getText().toString().trim().length() == 0) {
                            CustomeToast.ShowCustomToast(HealingBudApplication.getContext(), "Field Cannot be Empty!", Gravity.TOP);
                            return;
                        }
                    } else if (Dialog_type.equalsIgnoreCase("NAME")) {
                        api_object.put("name", USERNAME.getText().toString().trim());
                        api_url = update_name;
                        if (USERNAME.getText().toString().trim().equalsIgnoreCase(object.getString("name"))) {
                            SaveButtonAction();
                            return;
                        }
                        if (USERNAME.getText().toString().trim().length() == 0) {
                            CustomeToast.ShowCustomToast(HealingBudApplication.getContext(), "Field Cannot be Empty!", Gravity.TOP);
                            return;
                        }
                    }
                    if (Dialog_type.equalsIgnoreCase("Email")) {
                        new VollyAPICall(getContext(), true, api_url, api_object, user.getSession_key(), Request.Method.POST, EditUserProfileUpdatePersonalInfoAlertDialog.this, APIActions.ApiActions.update_user_email);
                    } else {
                        if (Dialog_type.equalsIgnoreCase("Zipcode")) {
                            //TODO
                            JSONObject object = new JSONObject();
                            new VollyAPICall(getContext(), true, getLocatioZipcode_part1 + "USA" + getLocatioZipcode_part2 + Zipcode.getText().toString() + getLocatioZipcode_part3, object, null, Request.Method.POST, EditUserProfileUpdatePersonalInfoAlertDialog.this, APIActions.ApiActions.testAPI);
                        } else {

                            new VollyAPICall(getContext(), true, api_url, api_object, user.getSession_key(), Request.Method.POST, EditUserProfileUpdatePersonalInfoAlertDialog.this, APIActions.ApiActions.update_profile_info);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        Cross_btn = main_dialog.findViewById(R.id.cross_btn);
        Cross_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Listener.onCrossButtonClick(EditUserProfileUpdatePersonalInfoAlertDialog.this);
            }
        });

        Edit_Bio = main_dialog.findViewById(R.id.edit_bio);
        Email = main_dialog.findViewById(R.id.email);
        Confirm_Email = main_dialog.findViewById(R.id.confirm_email);
//        Confirm_Email.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                if (actionId == EditorInfo.IME_ACTION_DONE) {
//                    HideKeyboard((Activity) v.getContext());
//                    return true;
//                }
//                return false;
//            }
//        });
        Password = main_dialog.findViewById(R.id.password);
        Confirm_Password = main_dialog.findViewById(R.id.confirm_password);
        USERNAME = main_dialog.findViewById(R.id.name);
        Zipcode = main_dialog.findViewById(R.id.zipcode);
        Edit_Bio.setSelection(Edit_Bio.length());
        Zipcode.setSelection(Zipcode.length());
        Password.setSelection(Password.length());
        Password.setText("");
        Email.setText("");
        Confirm_Email.setText("");
        Confirm_Password.setText("");
        Email.setSelection(Email.length());
        LinearLayout Email_Layout, Bio_Layout, Password_Layout, Zip_Code, Name_Layout;
        Email_Layout = main_dialog.findViewById(R.id.email_layout);
        Bio_Layout = main_dialog.findViewById(R.id.bio_layout);
        Zip_Code = main_dialog.findViewById(R.id.zipcode_layout);
        Password_Layout = main_dialog.findViewById(R.id.password_layout);
        Name_Layout = main_dialog.findViewById(R.id.name_layout);
        TextView Title = main_dialog.findViewById(R.id.heading_dialog);
        if (Dialog_type.equalsIgnoreCase("BIO")) {
            Email_Layout.setVisibility(View.GONE);
            Zip_Code.setVisibility(View.GONE);
            Name_Layout.setVisibility(View.GONE);
            Password_Layout.setVisibility(View.GONE);
            Bio_Layout.setVisibility(View.VISIBLE);
            try {
                Edit_Bio.setText(object.getString("bio"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Title.setText("UPDATE BIO");
        } else if (Dialog_type.equalsIgnoreCase("Email")) {
            Email_Layout.setVisibility(View.VISIBLE);
            Bio_Layout.setVisibility(View.GONE);
            Zip_Code.setVisibility(View.GONE);
            Name_Layout.setVisibility(View.GONE);
            Password_Layout.setVisibility(View.GONE);
            try {
                Email.setHint(object.getString("email"));
//                Email.setSelection(Email.getText().length());
                Confirm_Email.setHint(object.getString("email"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Title.setText("UPDATE EMAIL");
        } else if (Dialog_type.equalsIgnoreCase("Password")) {
            Email_Layout.setVisibility(View.GONE);
            Bio_Layout.setVisibility(View.GONE);
            Zip_Code.setVisibility(View.GONE);
            Name_Layout.setVisibility(View.GONE);
            Password_Layout.setVisibility(View.VISIBLE);
            try {
                Password.setHint(object.getString("password"));
//                Password.setSelection(Password.getText().length());
                Confirm_Password.setHint(object.getString("password"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Title.setText("UPDATE PASSWORD");
        } else if (Dialog_type.equalsIgnoreCase("Zipcode")) {
            Email_Layout.setVisibility(View.GONE);
            Bio_Layout.setVisibility(View.GONE);
            Zip_Code.setVisibility(View.VISIBLE);
            Password_Layout.setVisibility(View.GONE);
            Name_Layout.setVisibility(View.GONE);
            try {
                Zipcode.setText(object.getString("zipcode"));
                Zipcode.setSelection(Zipcode.getText().length());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Title.setText("UPDATE ZIPCODE");
        } else if (Dialog_type.equalsIgnoreCase("NAME")) {
            Email_Layout.setVisibility(View.GONE);
            Bio_Layout.setVisibility(View.GONE);
            Zip_Code.setVisibility(View.GONE);
            Password_Layout.setVisibility(View.GONE);
            Name_Layout.setVisibility(View.VISIBLE);
            try {
                USERNAME.setText(object.getString("name"));
                USERNAME.setSelection(USERNAME.getText().length());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Title.setText("UPDATE NAME");
        }
        dialog.setView(main_dialog);
//        Animation startAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.zoomout);
//        ((ViewGroup)dialog.getWindow().getDecorView())
//                .getChildAt(0).startAnimation(startAnimation);
        return dialog;
    }

    public void SaveButtonAction() {
        JSONObject data = new JSONObject();
        try {
            data.put("type", Dialog_type);
            data.put("bio", Edit_Bio.getText().toString());
            data.put("email", Email.getText().toString());
            data.put("password", Password.getText().toString());
            data.put("zipcode", Zipcode.getText().toString());
            data.put("name", USERNAME.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Listener.onSaveButtonClick(EditUserProfileUpdatePersonalInfoAlertDialog.this, data);
    }
}