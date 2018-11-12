package com.codingpixel.healingbudz.activity.home.side_menu.profile.dialog;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.android.volley.Request;
import com.codingpixel.healingbudz.R;
import com.codingpixel.healingbudz.activity.splash.Splash;
import com.codingpixel.healingbudz.adapter.ProfilePhotoAvatreRecylerAdapter;
import com.codingpixel.healingbudz.customeUI.CircularImageView;
import com.codingpixel.healingbudz.data_structure.APIActions;
import com.codingpixel.healingbudz.network.VollyAPICall;
import com.codingpixel.healingbudz.network.model.APIResponseListner;
import com.codingpixel.healingbudz.network.model.URL;
import com.codingpixel.healingbudz.network.upload_image.UploadImageAPIcall;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.codingpixel.healingbudz.activity.splash.Splash.user;
import static com.codingpixel.healingbudz.data_structure.APIActions.ApiActions.add_media;
import static com.codingpixel.healingbudz.network.model.URL.get_defaults;
import static com.codingpixel.healingbudz.network.model.URL.update_image;

public class EditUserProfileUploadPhotoAlertDialog extends BaseDialogFragment<EditUserProfileUploadPhotoAlertDialog.OnDialogFragmentClickListener> implements ProfilePhotoAvatreRecylerAdapter.ItemClickListener, APIResponseListner {
    CircularImageView Profile_Img;
    Button Upload_Btn;
    Button Second_Upload_Btn;
    Button Save_Btn;
    ImageView Cross_btn;
    ProgressBar preogress_bar;
    ProgressBar preogress_bar_topi;
    ImageView Second_Cross_btn;

    ImageView profile_img_topi;
    LinearLayout topi;
    String selected_avatar_name = "/icons/1.png";
    ArrayList<String> avater_data = new ArrayList<>();
    ArrayList<String> avater_data_topi = new ArrayList<>();
    static boolean isCoverPhoto;
    static boolean isSpecial;
    ProfilePhotoAvatreRecylerAdapter avatreRecylerAdapter;

    String selected_avatar_name_topi = "/icons/1.png";
    ProfilePhotoAvatreRecylerAdapter avatreRecylerAdapterTopi;
    static OnDialogFragmentClickListener Listener;
    private RecyclerView Avatr_Recyler_View, Avatr_Recyler_View_topi;
    static Drawable drawableImage = null;
    static Drawable drawableImageSp = null;

    @Override
    public void onItemClick(View view, Drawable drawable, int position) {
        Profile_Img.setImageDrawable(drawable);
        selected_avatar_name = avater_data.get(position);
    }

    @Override
    public void onTopiClicked(View view, Drawable drawable, int position) {
        profile_img_topi.setImageDrawable(drawable);
        selected_avatar_name_topi = avater_data_topi.get(position);
    }

    @Override
    public void onRequestSuccess(String response, APIActions.ApiActions apiActions) {
        if (apiActions == APIActions.ApiActions.update_avator) {
            Log.d("response", response);
        } else if (apiActions == add_media) {
            Log.d("response", response);
        } else {
            try {
                JSONObject object = new JSONObject(response);
                JSONArray avatr_list = object.getJSONObject("successData").getJSONArray("icons");
                JSONArray avatr_list_topi = object.getJSONObject("successData").getJSONArray("specials_icons");
                avater_data.clear();
                avater_data_topi.clear();

                for (int x = 0; x < avatr_list.length(); x++) {
                    JSONObject jsonObject = avatr_list.getJSONObject(x);
                    avater_data.add(jsonObject.getString("name"));
                }
                for (int x = 0; x < avatr_list_topi.length(); x++) {
                    JSONObject jsonObject = avatr_list_topi.getJSONObject(x);
                    avater_data_topi.add(jsonObject.getString("name"));
                }
                preogress_bar.setVisibility(View.GONE);
                Avatr_Recyler_View.setVisibility(View.VISIBLE);
                avatreRecylerAdapter.notifyDataSetChanged();
                if (isSpecial) {
                    topi.setVisibility(View.VISIBLE);
                    profile_img_topi.setVisibility(View.VISIBLE);
                    preogress_bar_topi.setVisibility(View.GONE);
                    Avatr_Recyler_View_topi.setVisibility(View.VISIBLE);
                    avatreRecylerAdapterTopi.notifyDataSetChanged();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void onRequestError(String response, APIActions.ApiActions apiActions) {
        Log.d("response", response);
    }

    public interface OnDialogFragmentClickListener {
        void onSaveButtonClick(EditUserProfileUploadPhotoAlertDialog dialog, Drawable drawable);

        void onSaveButtonClick(EditUserProfileUploadPhotoAlertDialog dialog);

        void onTopiChanged(Drawable drawable);

        void onCrossButtonClick(EditUserProfileUploadPhotoAlertDialog dialog);

        void onUploadButtonClick(EditUserProfileUploadPhotoAlertDialog dialog);
    }

    public static EditUserProfileUploadPhotoAlertDialog newInstance(OnDialogFragmentClickListener listner, boolean ic_cover_photo) {
        EditUserProfileUploadPhotoAlertDialog frag = new EditUserProfileUploadPhotoAlertDialog();
        Bundle args = new Bundle();
        frag.setArguments(args);
        Listener = listner;
        isCoverPhoto = ic_cover_photo;
        drawableImage = null;
        isSpecial = false;
        return frag;
    }

    public static EditUserProfileUploadPhotoAlertDialog newInstance(OnDialogFragmentClickListener listner, boolean ic_cover_photo, Drawable drawable) {
        EditUserProfileUploadPhotoAlertDialog frag = new EditUserProfileUploadPhotoAlertDialog();
        Bundle args = new Bundle();
        frag.setArguments(args);
        Listener = listner;
        isCoverPhoto = ic_cover_photo;
        isSpecial = false;
        drawableImage = drawable;
        return frag;
    }

    public static EditUserProfileUploadPhotoAlertDialog newInstance(OnDialogFragmentClickListener listner, boolean ic_cover_photo, boolean isSpecialH, Drawable drawable, Drawable spet) {
        EditUserProfileUploadPhotoAlertDialog frag = new EditUserProfileUploadPhotoAlertDialog();
        Bundle args = new Bundle();
        frag.setArguments(args);
        Listener = listner;
        isCoverPhoto = ic_cover_photo;
        drawableImage = drawable;
        isSpecial = isSpecialH;
        drawableImageSp = spet;
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater factory = LayoutInflater.from(getContext());
        final View main_dialog = factory.inflate(R.layout.edit_user_profile_photoupload_dialog_layout, null);
        final AlertDialog dialog = new AlertDialog.Builder(getContext()).create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        WindowManager.LayoutParams wmlp = dialog.getWindow().getAttributes();
        profile_img_topi = main_dialog.findViewById(R.id.profile_img_topi);
        Profile_Img = (CircularImageView) main_dialog.findViewById(R.id.profile_img);
        topi = main_dialog.findViewById(R.id.topi);
        Avatr_Recyler_View = main_dialog.findViewById(R.id.avatar_recyler_view);
        Avatr_Recyler_View_topi = main_dialog.findViewById(R.id.avatar_recyler_view_topi);
        Avatr_Recyler_View.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        avatreRecylerAdapter = new ProfilePhotoAvatreRecylerAdapter(getContext(), avater_data, false);
        avatreRecylerAdapter.setClickListener(this);
        Avatr_Recyler_View.setAdapter(avatreRecylerAdapter);

        Avatr_Recyler_View_topi.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        avatreRecylerAdapterTopi = new ProfilePhotoAvatreRecylerAdapter(getContext(), avater_data_topi, true);
        avatreRecylerAdapterTopi.setClickListener(this);
        Avatr_Recyler_View_topi.setAdapter(avatreRecylerAdapterTopi);
        preogress_bar = main_dialog.findViewById(R.id.loading_spinner);
        preogress_bar_topi = main_dialog.findViewById(R.id.loading_spinner_topi);
        Upload_Btn = main_dialog.findViewById(R.id.upload_profile_img);
        Upload_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Listener.onUploadButtonClick(EditUserProfileUploadPhotoAlertDialog.this);
            }
        });

        Second_Upload_Btn = main_dialog.findViewById(R.id.second_upload_profile_img);
        Second_Upload_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Listener.onUploadButtonClick(EditUserProfileUploadPhotoAlertDialog.this);
            }
        });

        Save_Btn = main_dialog.findViewById(R.id.save_btn);
        Save_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // set avator image api call
                if (!selected_avatar_name.equalsIgnoreCase("/icons/1.png") || (isSpecial && !selected_avatar_name_topi.equalsIgnoreCase("/icons/1.png"))) {
                    JSONObject object = new JSONObject();
                    try {
                        if (!selected_avatar_name.equalsIgnoreCase("/icons/1.png")) {
                            object.put("avatar", selected_avatar_name);
                            Bitmap icon = drawableToBitmap(Profile_Img.getDrawable());
                            UploadImage(new BitmapDrawable(getResources(), icon));
                            new VollyAPICall(getContext(), false, URL.update_avatar, object, user.getSession_key(), Request.Method.POST, EditUserProfileUploadPhotoAlertDialog.this, APIActions.ApiActions.update_avator);
                            Listener.onSaveButtonClick(EditUserProfileUploadPhotoAlertDialog.this, Profile_Img.getDrawable());
                        }

                        if (isSpecial && !selected_avatar_name_topi.equalsIgnoreCase("/icons/1.png")) {
                            Listener.onTopiChanged(profile_img_topi.getDrawable());
                            object.put("special_icon", selected_avatar_name_topi);
                            //update_special_icon
                            new VollyAPICall(getContext(), false, URL.update_special_icon, object, user.getSession_key(), Request.Method.POST, EditUserProfileUploadPhotoAlertDialog.this, APIActions.ApiActions.update_avator);

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Listener.onSaveButtonClick(EditUserProfileUploadPhotoAlertDialog.this);
                }

            }
        });

        Cross_btn = main_dialog.findViewById(R.id.cross_btn);
        Cross_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Listener.onCrossButtonClick(EditUserProfileUploadPhotoAlertDialog.this);
            }
        });

        Second_Cross_btn = main_dialog.findViewById(R.id.second_cross_btn);
        Second_Cross_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Listener.onCrossButtonClick(EditUserProfileUploadPhotoAlertDialog.this);
            }
        });
        RelativeLayout Cover_Photo, Profile_Photo_Layout;

        Cover_Photo = main_dialog.findViewById(R.id.cover_photo);
        Profile_Photo_Layout = main_dialog.findViewById(R.id.profile_photo_layout);
        if (isCoverPhoto) {
            wmlp.gravity = Gravity.TOP;
            wmlp.y = getResources().getDisplayMetrics().heightPixels / 5;
            Cover_Photo.setVisibility(View.VISIBLE);
            Profile_Photo_Layout.setVisibility(View.GONE);
        } else {
            wmlp.gravity = Gravity.CENTER;
            Cover_Photo.setVisibility(View.GONE);
            Profile_Photo_Layout.setVisibility(View.VISIBLE);
            if (drawableImage != null) {
                Profile_Img.setImageDrawable(drawableImage);
            }
            if (isSpecial) {
                topi.setVisibility(View.VISIBLE);
                profile_img_topi.setVisibility(View.VISIBLE);
                profile_img_topi.setImageDrawable(drawableImageSp);
            } else {
                topi.setVisibility(View.GONE);
                profile_img_topi.setVisibility(View.GONE);
            }
        }
        dialog.setView(main_dialog);
        JSONObject object = new JSONObject();
        new VollyAPICall(getContext(), false, get_defaults + "/" + Splash.user.getEmail().trim(), object, null, Request.Method.GET, this, APIActions.ApiActions.get_defaults);
        return dialog;
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap = null;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if (bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if (drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    public void UploadImage(Drawable drawable) {
        new UploadImageAPIcall(getContext(), update_image, drawable, user.getSession_key(), this, add_media);
    }
}