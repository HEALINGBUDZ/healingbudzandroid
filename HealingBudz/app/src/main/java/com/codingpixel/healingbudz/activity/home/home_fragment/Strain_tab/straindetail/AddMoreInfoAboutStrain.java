package com.codingpixel.healingbudz.activity.home.home_fragment.Strain_tab.straindetail;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.codingpixel.healingbudz.R;
import com.codingpixel.healingbudz.camera_activity.HBCameraActivity;
import com.codingpixel.healingbudz.customeUI.CustomeToast;
import com.codingpixel.healingbudz.data_structure.APIActions;
import com.codingpixel.healingbudz.network.VollyAPICall;
import com.codingpixel.healingbudz.network.model.APIResponseListner;
import com.codingpixel.healingbudz.network.model.URL;
import com.codingpixel.healingbudz.network.upload_file_with_progress.UploadFileWithProgress;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.codingpixel.healingbudz.activity.home.home_fragment.Strain_tab.straindetail.StrainDetailsActivity.strainDataModel;
import static com.codingpixel.healingbudz.activity.splash.Splash.user;
import static com.codingpixel.healingbudz.Utilities.ViewUtils.checkRotation;
import static com.codingpixel.healingbudz.customeUI.ImageHelper.getRoundedCornerBitmap;
import static com.codingpixel.healingbudz.data_structure.APIActions.ApiActions.save_user_strain;
import static com.codingpixel.healingbudz.static_function.IntentFunction.GoToHome;
import static com.codingpixel.healingbudz.static_function.UIModification.ChangeStatusBarColor;
import static com.codingpixel.healingbudz.static_function.UIModification.HideKeyboard;

//import static com.codingpixel.healingbudz.application.HealingBudApplication.getContext;

public class AddMoreInfoAboutStrain extends AppCompatActivity implements APIResponseListner {
    ImageView Back, Home;
    View Back_view_slide_bar;
    TextView Comma, type_main_Strain;
    TextView First_Percent_Text, Secnod_Percent_text;
    LinearLayout Level_Easy, Level_Mid, Level_Hard;
    TextView Yeild_Low, Yeild_High, Yeild_Medium;
    TextView Climate_inddor, Climate_outdoor, Climate_greens_house;
    LinearLayout Upload_Photo, Upload_image_photo, cross_bread_layout;
    private static final List<String> StrainKeywords = new ArrayList<String>();
    ArrayList<String> update_strain_keyword = new ArrayList<>();
    AutoCompleteTextView Cross_breed_strain_one, Cross_breed_strain_two;
    ImageView Attach_img;

    String Cross_bread_strain_one = "";
    String Cross_bread_strain_two = "";

    String Growing_dificulty_level = "moderate";
    String yeild = "low", Climate = "outdoor";
    int Indica_percentage = 50;
    int Stiva_percentage = 50;
    String genetics = "hybrid";
    String image_path = "";

    TextView Strain_Name, Strain_Type;
    Button Submit_Button;
    EditText Full_discription, Mature_Height, Following_Time, Hardness_Zone_fornheight_from, Hardness_Zone_fornheight_to, Hardness_Zone_celcious_from, Hardness_Zone_celcious_to, Notes;
    EditText min_CBD, max_CBD, min_THC, max_THC;

    //android:textColor="#c24462"
//    android:textColor="#ae59c2"
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_more_info_about_strain);
        InitView();
        ChangeStatusBarColor(AddMoreInfoAboutStrain.this, "#0a0a0a");
        HideKeyboard(AddMoreInfoAboutStrain.this);
        First_Percent_Text = (TextView) findViewById(R.id.first_percent_text);
        Secnod_Percent_text = (TextView) findViewById(R.id.second_percent_text);
        Attach_img = (ImageView) findViewById(R.id.attach_img);
        Back = (ImageView) findViewById(R.id.back_btn);
        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        Home = (ImageView) findViewById(R.id.home_btn);
        type_main_Strain = (TextView) findViewById(R.id.type_main_Strain);
        Home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoToHome(AddMoreInfoAboutStrain.this, true);
                finish();
            }
        });
        final GradientDrawable gd = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, generate_gradient(30));
        gd.setCornerRadius(0f);
        Back_view_slide_bar = findViewById(R.id.back_view);
        cross_bread_layout = (LinearLayout) findViewById(R.id.cross_bread_layout);
        Back_view_slide_bar.setBackground(gd);

        SeekBar customSeekBar = (SeekBar) findViewById(R.id.customSeekBar);
        // perform seek bar change listener event used for getting the progress value
        customSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressChangedValue = 0;

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressChangedValue = progress;
                gd.setColors(generate_gradient(progress));
                Back_view_slide_bar.setBackground(gd);
                Secnod_Percent_text.setText(Math.abs(progress) + "%");
                First_Percent_Text.setText(100 - progress + "%");

                Indica_percentage = (100 - progress);
                Stiva_percentage = Math.abs(progress);

                if (progress == 0) {
                    genetics = "indica";
                    type_main_Strain.setText("Indica");
                    type_main_Strain.setTextColor(Color.parseColor("#ae59c2"));
                    cross_bread_layout.setVisibility(View.GONE);
//                    type_main_Strain.setText("Sativa");
                } else if (progress == 100) {
                    genetics = "sativa";
                    type_main_Strain.setText("Sativa");
                    cross_bread_layout.setVisibility(View.GONE);
                    type_main_Strain.setTextColor(Color.parseColor("#c24462"));
//                    type_main_Strain.setText("Indica");
                } else {
                    cross_bread_layout.setVisibility(View.VISIBLE);
                    genetics = "hybrid";
                    type_main_Strain.setText("Hybrid");
                    type_main_Strain.setTextColor(Color.parseColor("#7cc244"));
                }

            }

            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                Log.d("progress", seekBar.getProgress() + "");
            }
        });

        Level_Easy = (LinearLayout) findViewById(R.id.level_easy);
        Level_Mid = (LinearLayout) findViewById(R.id.level_mid);
        Level_Hard = (LinearLayout) findViewById(R.id.level_hard);

        Level_Easy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Growing_dificulty_level = "easy";
                Level_Easy.setBackgroundResource(R.drawable.add_strain_growing_care_active_bg);
                Level_Mid.setBackground(null);
                Level_Hard.setBackground(null);
            }
        });

        Level_Mid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Growing_dificulty_level = "moderate";
                Level_Mid.setBackgroundResource(R.drawable.add_strain_growing_care_active_bg);
                Level_Easy.setBackground(null);
                Level_Hard.setBackground(null);
            }
        });

        Level_Hard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Growing_dificulty_level = "hard";
                Level_Hard.setBackgroundResource(R.drawable.add_strain_growing_care_active_bg);
                Level_Easy.setBackground(null);
                Level_Mid.setBackground(null);
            }
        });


        Yeild_Low = (TextView) findViewById(R.id.yeild_low);
        Yeild_Medium = (TextView) findViewById(R.id.yeild_medium);
        Yeild_High = (TextView) findViewById(R.id.yeild_high);


        Yeild_Low.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                yeild = "low";
                Yeild_Low.setBackgroundResource(R.drawable.add_strain_detail_level_selection_slect_bg);
                Yeild_Medium.setBackgroundResource(R.drawable.add_strain_detail_level_selection_deslect_bg);
                Yeild_High.setBackgroundResource(R.drawable.add_strain_detail_level_selection_deslect_bg);

                Yeild_Low.setTextColor(Color.parseColor("#000000"));
                Yeild_Medium.setTextColor(Color.parseColor("#c4c4c4"));
                Yeild_High.setTextColor(Color.parseColor("#c4c4c4"));

            }
        });

        Yeild_Medium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                yeild = "medium";
                Yeild_Medium.setBackgroundResource(R.drawable.add_strain_detail_level_selection_slect_bg);
                Yeild_Low.setBackgroundResource(R.drawable.add_strain_detail_level_selection_deslect_bg);
                Yeild_High.setBackgroundResource(R.drawable.add_strain_detail_level_selection_deslect_bg);

                Yeild_Medium.setTextColor(Color.parseColor("#000000"));
                Yeild_Low.setTextColor(Color.parseColor("#c4c4c4"));
                Yeild_High.setTextColor(Color.parseColor("#c4c4c4"));
            }
        });

        Yeild_High.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                yeild = "high";
                Yeild_High.setBackgroundResource(R.drawable.add_strain_detail_level_selection_slect_bg);
                Yeild_Low.setBackgroundResource(R.drawable.add_strain_detail_level_selection_deslect_bg);
                Yeild_Medium.setBackgroundResource(R.drawable.add_strain_detail_level_selection_deslect_bg);

                Yeild_High.setTextColor(Color.parseColor("#000000"));
                Yeild_Low.setTextColor(Color.parseColor("#c4c4c4"));
                Yeild_Medium.setTextColor(Color.parseColor("#c4c4c4"));
            }
        });

        Climate_inddor = (TextView) findViewById(R.id.clima_indoor);
        Climate_outdoor = (TextView) findViewById(R.id.clima_outdoor);
        Climate_greens_house = (TextView) findViewById(R.id.clima_greeen_house);


        Climate_inddor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Climate = "indoor";
                Climate_inddor.setBackgroundResource(R.drawable.add_strain_detail_level_selection_slect_bg);
                Climate_outdoor.setBackgroundResource(R.drawable.add_strain_detail_level_selection_deslect_bg);
                Climate_greens_house.setBackgroundResource(R.drawable.add_strain_detail_level_selection_deslect_bg);

                Climate_inddor.setTextColor(Color.parseColor("#000000"));
                Climate_outdoor.setTextColor(Color.parseColor("#c4c4c4"));
                Climate_greens_house.setTextColor(Color.parseColor("#c4c4c4"));

            }
        });

        Climate_outdoor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Climate = "outdoor";
                Climate_outdoor.setBackgroundResource(R.drawable.add_strain_detail_level_selection_slect_bg);
                Climate_inddor.setBackgroundResource(R.drawable.add_strain_detail_level_selection_deslect_bg);
                Climate_greens_house.setBackgroundResource(R.drawable.add_strain_detail_level_selection_deslect_bg);

                Climate_outdoor.setTextColor(Color.parseColor("#000000"));
                Climate_inddor.setTextColor(Color.parseColor("#c4c4c4"));
                Climate_greens_house.setTextColor(Color.parseColor("#c4c4c4"));
            }
        });

        Climate_greens_house.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Climate = "greenshouse";
                Climate_greens_house.setBackgroundResource(R.drawable.add_strain_detail_level_selection_slect_bg);
                Climate_inddor.setBackgroundResource(R.drawable.add_strain_detail_level_selection_deslect_bg);
                Climate_outdoor.setBackgroundResource(R.drawable.add_strain_detail_level_selection_deslect_bg);

                Climate_greens_house.setTextColor(Color.parseColor("#000000"));
                Climate_inddor.setTextColor(Color.parseColor("#c4c4c4"));
                Climate_outdoor.setTextColor(Color.parseColor("#c4c4c4"));
            }
        });

        Comma = (TextView) findViewById(R.id.coma);
        Comma.setText("\"");

        Upload_Photo = (LinearLayout) findViewById(R.id.upload_photho);
        Upload_Photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddMoreInfoAboutStrain.this, HBCameraActivity.class);
                intent.putExtra("isVideoCaptureAble", false);
                startActivityForResult(intent, 1200);
            }
        });
        Upload_image_photo = (LinearLayout) findViewById(R.id.upload_img_video);

        JSONObject jsonObject = new JSONObject();
        new VollyAPICall(AddMoreInfoAboutStrain.this, false, URL.get_strains, jsonObject, user.getSession_key(), Request.Method.GET, AddMoreInfoAboutStrain.this, APIActions.ApiActions.get_strains);

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    public int[] generate_gradient(int progress) {
        progress = Math.abs(100 - progress);
        if (progress < 15) {
            if (progress < 2) {
                return new int[]{0xFFc24462, 0xFFc24462, 0xFFc24462, 0xFFc24462, 0xFFc24462, 0xFFc24462, 0xFFc24462};
            } else {
                return new int[]{0xFFae59c2, 0xFFc24462, 0xFFc24462, 0xFFc24462, 0xFFc24462, 0xFFc24462, 0xFFc24462};
            }
        } else if (progress >= 15 && progress <= 30) {
            return new int[]{0xFFae59c2, 0xFFae59c2, 0xFFc24462, 0xFFc24462, 0xFFc24462, 0xFFc24462, 0xFFc24462};
        } else if (progress >= 30 && progress <= 45) {
            return new int[]{0xFFae59c2, 0xFFae59c2, 0xFFae59c2, 0xFFc24462, 0xFFc24462, 0xFFc24462, 0xFFc24462};
        } else if (progress >= 45 && progress <= 60) {
            return new int[]{0xFFae59c2, 0xFFae59c2, 0xFFae59c2, 0xFFae59c2, 0xFFc24462, 0xFFc24462, 0xFFc24462};
        } else if (progress >= 60 && progress <= 75) {
            return new int[]{0xFFae59c2, 0xFFae59c2, 0xFFae59c2, 0xFFae59c2, 0xFFae59c2, 0xFFc24462, 0xFFc24462};
        } else if (progress >= 75 && progress <= 90) {
            return new int[]{0xFFae59c2, 0xFFae59c2, 0xFFae59c2, 0xFFae59c2, 0xFFae59c2, 0xFFae59c2, 0xFFc24462};
        } else if (progress >= 90 && progress <= 100) {
            if (progress > 98) {
                return new int[]{0xFFae59c2, 0xFFae59c2, 0xFFae59c2, 0xFFae59c2, 0xFFae59c2, 0xFFae59c2, 0xFFae59c2};

            } else {
                return new int[]{0xFFae59c2, 0xFFae59c2, 0xFFae59c2, 0xFFae59c2, 0xFFae59c2, 0xFFae59c2, 0xFFc24462};

            }
        } else {
            return new int[]{0xFFae59c2, 0xFFc24462};
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 1200) {
            Bitmap bitmapOrg = BitmapFactory.decodeFile(data.getExtras().getString("file_path_arg"));
//            bitmapOrg = Bitmap.createScaledBitmap(bitmapOrg, Upload_image_photo.getWidth(), 300, false);
            bitmapOrg = checkRotation(bitmapOrg, data.getExtras().getString("file_path_arg"));
            Bitmap bitmap = getRoundedCornerBitmap(bitmapOrg, 0);
            Drawable drawable = new BitmapDrawable(getResources(), bitmap);
            Attach_img.setImageDrawable(drawable);
            image_path = data.getExtras().getString("file_path_arg");
        }
    }


    public void InitView() {

        Cross_breed_strain_one = (AutoCompleteTextView) findViewById(R.id.percentage_cross_breed_one);
        Cross_breed_strain_two = (AutoCompleteTextView) findViewById(R.id.percentage_cross_breed_two);
        Cross_breed_strain_one.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Cross_bread_strain_one = adapterView.getItemAtPosition(i).toString();
                update_strain_keyword.clear();
                for (int x = 0; x < StrainKeywords.size(); x++) {
                    if (!Cross_bread_strain_one.equalsIgnoreCase(StrainKeywords.get(x))) {
                        update_strain_keyword.add(StrainKeywords.get(x));
                    }
                }
                Cross_breed_strain_two.setThreshold(1);
                ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(AddMoreInfoAboutStrain.this, android.R.layout.simple_dropdown_item_1line, update_strain_keyword);
                Cross_breed_strain_two.setAdapter(adapter2);
            }
        });
        Cross_breed_strain_two.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Cross_bread_strain_two = adapterView.getItemAtPosition(i).toString();
            }
        });
        Full_discription = (EditText) findViewById(R.id.full_discription);
        Mature_Height = (EditText) findViewById(R.id.nature_height);
        Following_Time = (EditText) findViewById(R.id.following_time);
        Hardness_Zone_fornheight_from = (EditText) findViewById(R.id.hardness_zone_fornhight_from);
        Hardness_Zone_fornheight_to = (EditText) findViewById(R.id.hardness_zone_fornhight_to);
        Hardness_Zone_celcious_from = (EditText) findViewById(R.id.hardness_zone_celcious_from);
        Hardness_Zone_celcious_to = (EditText) findViewById(R.id.hardness_zone_celcious_to);
        min_CBD = (EditText) findViewById(R.id.min_CBD);
        max_CBD = (EditText) findViewById(R.id.max_CBD);
        min_THC = (EditText) findViewById(R.id.min_THC);
        max_THC = (EditText) findViewById(R.id.max_THC);
        Notes = (EditText) findViewById(R.id.notes);
        Submit_Button = (Button) findViewById(R.id.submit_button);
        Submit_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (chechForPredefined()) {
                    SubmitData();
                }
            }
        });

        Strain_Name = (TextView) findViewById(R.id.main_strain_name);
        Strain_Name.setText(strainDataModel.getTitle());
        Strain_Type = (TextView) findViewById(R.id.main_strain_type);
        Strain_Type.setText(strainDataModel.getType_title());

    }

    private boolean chechForPredefined() {
        if (Cross_breed_strain_one.getText().toString().trim().length() > 0) {
            if (!StrainKeywords.contains(Cross_breed_strain_one.getText().toString().trim())) {
                CustomeToast.ShowCustomToast(AddMoreInfoAboutStrain.this, "Cross Breed is not valid!", Gravity.TOP);
                return false;
            }
        }
        if (Cross_breed_strain_two.getText().toString().trim().length() > 0) {
            if (!update_strain_keyword.contains(Cross_breed_strain_two.getText().toString().trim())) {
                CustomeToast.ShowCustomToast(AddMoreInfoAboutStrain.this, "Cross Breed is not valid!", Gravity.TOP);
                return false;
            }
        }
//        if (Full_discription.getText().toString().trim().length() == 0) {
//            CustomeToast.ShowCustomToast(AddMoreInfoAboutStrain.this, "Description Field is Required!", Gravity.CENTER);
//            return false;
//        }
//        if (Notes.getText().toString().trim().length() == 0) {
//            CustomeToast.ShowCustomToast(AddMoreInfoAboutStrain.this, "Notes Field is Required!", Gravity.CENTER);
//            return false;
//        }
        return true;
    }

    void setEdittextValue(EditText view, String value) {

        view.setText(value);
    }

    void setEdittextValueTemp(EditText view, String value) {
        Double abc = Double.parseDouble(value);
        view.setHint(String.valueOf(abc.intValue()));
    }

    @Override
    public void onRequestSuccess(String response, APIActions.ApiActions apiActions) {
        if (apiActions == APIActions.ApiActions.get_strains) {
            Log.d("resposne", response);
            StrainKeywords.clear();
            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONArray jsonArray = jsonObject.getJSONObject("successData").getJSONArray("strains");
                for (int x = 0; x < jsonArray.length(); x++) {
                    JSONObject strain_object = jsonArray.getJSONObject(x);
                    String title = strain_object.getString("title");
                    StrainKeywords.add(title);
                    update_strain_keyword.add(title);
                }
                Cross_breed_strain_one.setThreshold(1);
                Cross_breed_strain_two.setThreshold(1);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, StrainKeywords);
                Cross_breed_strain_one.setAdapter(adapter);
                ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, update_strain_keyword);
                Cross_breed_strain_two.setAdapter(adapter2);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (apiActions == save_user_strain) {
            Log.d("resposne", response);
            finish();
            CustomeToast.ShowCustomToast(AddMoreInfoAboutStrain.this, "Strain info added successfully!", Gravity.TOP);
        }
    }

    @Override
    public void onRequestError(String response, APIActions.ApiActions apiActions) {
        Log.d("resposne", response);
        try {
            JSONObject object = new JSONObject(response);
            CustomeToast.ShowCustomToast(AddMoreInfoAboutStrain.this, object.getString("errorMessage"), Gravity.TOP);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    void setTempratureValues() {
        if (Mature_Height.getText().toString().trim().length() > 0) {
            setEdittextValue(Mature_Height, Mature_Height.getText().toString());
        } else {
            setEdittextValue(Mature_Height, Mature_Height.getHint().toString());
        }
        if (Following_Time.getText().toString().trim().length() > 0) {
            setEdittextValue(Following_Time, Following_Time.getText().toString());
        } else {
            setEdittextValue(Following_Time, Following_Time.getHint().toString());
        }
        if (Hardness_Zone_fornheight_from.getText().toString().trim().length() > 0) {
            setEdittextValue(Hardness_Zone_fornheight_from, Hardness_Zone_fornheight_from.getText().toString());
        } else {
            setEdittextValue(Hardness_Zone_fornheight_from, Hardness_Zone_fornheight_from.getHint().toString());
        }
        if (Hardness_Zone_fornheight_to.getText().toString().trim().length() > 0) {
            setEdittextValue(Hardness_Zone_fornheight_to, Hardness_Zone_fornheight_to.getText().toString());
        } else {
            setEdittextValue(Hardness_Zone_fornheight_to, Hardness_Zone_fornheight_to.getHint().toString());
        }
        if (Hardness_Zone_celcious_from.getText().toString().trim().length() > 0) {
            setEdittextValue(Hardness_Zone_celcious_from, Hardness_Zone_celcious_from.getText().toString());
        } else {
            setEdittextValue(Hardness_Zone_celcious_from, Hardness_Zone_celcious_from.getHint().toString());
        }
        if (Hardness_Zone_celcious_to.getText().toString().trim().length() > 0) {
            setEdittextValue(Hardness_Zone_celcious_to, Hardness_Zone_celcious_to.getText().toString());
        } else {
            setEdittextValue(Hardness_Zone_celcious_to, Hardness_Zone_celcious_to.getHint().toString());
        }

        ////
        if (min_CBD.getText().toString().trim().length() > 0) {
            setEdittextValue(min_CBD, min_CBD.getText().toString());
        } else {
            setEdittextValue(min_CBD, min_CBD.getHint().toString().replace("%",""));
        }
        if (min_THC.getText().toString().trim().length() > 0) {
            setEdittextValue(min_THC, min_THC.getText().toString());
        } else {
            setEdittextValue(min_THC, min_THC.getHint().toString().replace("%",""));
        }
        if (max_THC.getText().toString().trim().length() > 0) {
            setEdittextValue(max_THC, max_THC.getText().toString());
        } else {
            setEdittextValue(max_THC, max_THC.getHint().toString().replace("%",""));
        }
        if (max_CBD.getText().toString().trim().length() > 0) {
            setEdittextValue(max_CBD, max_CBD.getText().toString());
        } else {
            setEdittextValue(max_CBD, max_CBD.getHint().toString().replace("%",""));
        }
    }

    public void SubmitData() {
        setTempratureValues();
        if (image_path.length() < 6) {
            JSONObject parameters = new JSONObject();
            try {
                parameters.put("strain_id", strainDataModel.getId());
                parameters.put("indica", Indica_percentage);
                parameters.put("sativa", Stiva_percentage);
                parameters.put("genetics", genetics);
                parameters.put("growing", Growing_dificulty_level);
                parameters.put("cross_breed", Cross_bread_strain_one + "," + Cross_bread_strain_two);
                parameters.put("plant_height", Mature_Height.getText().toString());
                parameters.put("flowering_time", Following_Time.getText().toString());
                parameters.put("min_fahren_temp", Hardness_Zone_fornheight_from.getText().toString());
                parameters.put("max_fahren_temp", Hardness_Zone_fornheight_to.getText().toString());
                parameters.put("min_celsius_temp", Hardness_Zone_celcious_from.getText().toString());
                parameters.put("max_celsius_temp", Hardness_Zone_celcious_to.getText().toString());
                parameters.put("min_CBD", min_CBD.getText().toString());
                parameters.put("min_THC", min_THC.getText().toString());
                parameters.put("max_CBD", max_CBD.getText().toString());
                parameters.put("max_THC", max_THC.getText().toString());
                parameters.put("yeild", yeild);
                parameters.put("climate", Climate);
                parameters.put("note", Notes.getText().toString());
                parameters.put("description", Full_discription.getText().toString());
                new VollyAPICall(this, true, URL.save_user_strain, parameters, user.getSession_key(), Request.Method.POST, this, save_user_strain);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            JSONArray parameters = new JSONArray();
            parameters.put("strain_id");
            parameters.put("indica");
            parameters.put("sativa");
            parameters.put("genetics");
            parameters.put("growing");
            parameters.put("cross_breed");
            parameters.put("plant_height");
            parameters.put("flowering_time");
            parameters.put("min_fahren_temp");
            parameters.put("max_fahren_temp");
            parameters.put("min_celsius_temp");
            parameters.put("max_celsius_temp");
            parameters.put("yeild");
            parameters.put("climate");
            parameters.put("note");
            parameters.put("description");

            JSONArray parameters_values = new JSONArray();
            parameters_values.put(strainDataModel.getId());
            parameters_values.put(Indica_percentage);
            parameters_values.put(Stiva_percentage);
            parameters_values.put(genetics);
            parameters_values.put(Growing_dificulty_level);
            parameters_values.put(Cross_bread_strain_one + "," + Cross_bread_strain_two);
            parameters_values.put(Mature_Height.getText().toString());
            parameters_values.put(Following_Time.getText().toString());
            parameters_values.put(Hardness_Zone_fornheight_from.getText().toString());
            parameters_values.put(Hardness_Zone_fornheight_to.getText().toString());
            parameters_values.put(Hardness_Zone_celcious_from.getText().toString());
            parameters_values.put(Hardness_Zone_celcious_to.getText().toString());
            parameters_values.put(yeild);
            parameters_values.put(Climate);
            parameters_values.put(Notes.getText().toString());
            parameters_values.put(Full_discription.getText().toString());
            new UploadFileWithProgress(this, true, URL.save_user_strain, image_path, "image", parameters_values, parameters, null, AddMoreInfoAboutStrain.this, save_user_strain).execute();
        }
    }
}
