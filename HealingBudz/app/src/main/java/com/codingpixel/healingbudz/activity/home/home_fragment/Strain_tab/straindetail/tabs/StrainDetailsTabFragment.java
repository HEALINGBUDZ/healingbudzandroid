package com.codingpixel.healingbudz.activity.home.home_fragment.Strain_tab.straindetail.tabs;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.codingpixel.healingbudz.DataModel.UserStrainDetailsDataModel;
import com.codingpixel.healingbudz.R;
import com.codingpixel.healingbudz.activity.home.home_fragment.Strain_tab.straindetail.AddMoreInfoAboutStrain;
import com.codingpixel.healingbudz.activity.home.home_fragment.Strain_tab.straindetail.StrainDetailsActivity;
import com.codingpixel.healingbudz.adapter.StrainDetailsTabRecylerViewAdapter;
import com.codingpixel.healingbudz.customeUI.CustomeToast;
import com.codingpixel.healingbudz.customeUI.SimpleToolTip.SimpleTooltip;
import com.codingpixel.healingbudz.data_structure.APIActions;
import com.codingpixel.healingbudz.interfaces.ViewAllDetailsAddedByUserButtonListner;
import com.codingpixel.healingbudz.network.VollyAPICall;
import com.codingpixel.healingbudz.network.model.APIResponseListner;
import com.codingpixel.healingbudz.network.model.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.codingpixel.healingbudz.Utilities.ClickAbleKeywordText.MakeKeywordClickableText;
import static com.codingpixel.healingbudz.activity.home.home_fragment.Strain_tab.straindetail.StrainDetailsActivity.strainDataModel;
import static com.codingpixel.healingbudz.activity.home.home_fragment.Strain_tab.straindetail.StrainDetailsActivity.strainDetailsActivity_scrollView;
import static com.codingpixel.healingbudz.activity.splash.Splash.user;
import static com.codingpixel.healingbudz.data_structure.APIActions.ApiActions.get_user_strain;
import static com.codingpixel.healingbudz.static_function.IntentFunction.GoTo;
import static com.codingpixel.healingbudz.static_function.UIModification.HideKeyboard;

@SuppressLint("ValidFragment")
public class StrainDetailsTabFragment extends Fragment implements View.OnClickListener, ViewAllDetailsAddedByUserButtonListner, APIResponseListner {
    LinearLayout Add_More_Info_Strain, add_more_ifon_strain_new;
    View Back_view_slide_bar;
    RecyclerView strain_recylerview;
    ImageView Hybrid_Info_Button;
    ImageView Indica_Info_Button;
    ImageView Sativa_Info_Button;
    ImageView Edits_Info_Button, show_empty;
    RelativeLayout Hybrrid_Dialog_Layout;
    RelativeLayout Indica_Dialog_Layout;
    RelativeLayout Sativa_Dialog_Layout;
    RelativeLayout Edits_Dialog_Layout;
    RelativeLayout bottom_views;
    TextView Hybrid_Dialog_Text;
    TextView Indica_Dialog_Text;
    TextView Sativa_Dialog_Text;
    TextView Edit_Dialog_Text;
    ImageView Hybrid_Cross_button;
    ImageView Indica_Cross_button;
    ImageView Sativa_Cross_button;
    ImageView Edits_Cross_button;
    boolean isHybrid_dialog = false;
    boolean isIndica_dialog = false;
    boolean isSativa_dialog = false;
    boolean isEdit_dialog = false;
    SeekBar customSeekBar;
    TextView strain_edits;
    TextView strain_edits_show;
    ArrayList<UserStrainDetailsDataModel> userStrainDetailsDataModels = new ArrayList<>();
    ViewAllDetailsAddedByUserButtonListner Listner;
    RelativeLayout Main_Layout, in_between;
    LinearLayout Refresh_Layout, Edits_Layout, Edits_Layout1, cross_layout;
    TextView Like_Count, Discription, Indica_percentage, Stiva_percentage, Cross_Bread_one, Cross_bread_two, type_strain;
    TextView Difficulty_level_text, Mature_Height, Following_times, zone_min_forhn_temp, zone_max_forhn_temp, zone_celc_temp;
    TextView yeild, climate, notes;
    TextView min_CBD, max_CBD, min_THC, max_THC;
    ImageView Difficulty_level_icon;
    StrainDetailsTabRecylerViewAdapter adapter;
    private boolean referesh = false;
    private LinearLayout scroll_to_edit;
    public static String strainDiscription = "";

    @SuppressLint("ValidFragment")
    public StrainDetailsTabFragment(ViewAllDetailsAddedByUserButtonListner viewAllDetailsAddedByUserButtonListner) {
        this.Listner = viewAllDetailsAddedByUserButtonListner;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.strain_details_tab_layout, container, false);
        HideKeyboard(getActivity());
        Init(view);
        return view;
    }

    public void Init(View view) {
        InitView(view);

        type_strain = view.findViewById(R.id.type_strain);
        show_empty = view.findViewById(R.id.show_empty);
        add_more_ifon_strain_new = view.findViewById(R.id.add_more_ifon_strain_new);
        in_between = view.findViewById(R.id.in_between);
        show_empty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                referesh = true;
                GoTo(getContext(), AddMoreInfoAboutStrain.class);
            }
        });
        add_more_ifon_strain_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                referesh = true;
                GoTo(getContext(), AddMoreInfoAboutStrain.class);
            }
        });
        Add_More_Info_Strain = view.findViewById(R.id.add_more_ifon_strain);
        Edits_Layout = view.findViewById(R.id.view_fourteen);
        Edits_Layout1 = view.findViewById(R.id.editss);
        Add_More_Info_Strain.setOnClickListener(this);
        Add_More_Info_Strain.setVisibility(View.GONE);

        final GradientDrawable gd = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, generate_gradient(30));
        gd.setCornerRadius(0f);
        Back_view_slide_bar = view.findViewById(R.id.back_view);
        Back_view_slide_bar.setBackground(gd);
        SeekBar customSeekBar = (SeekBar) view.findViewById(R.id.customSeekBar);
        customSeekBar.setEnabled(false);
        strain_recylerview = view.findViewById(R.id.strain_details_tab_recyler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.scrollToPosition(0);
        strain_recylerview.setLayoutManager(layoutManager);
        adapter = new StrainDetailsTabRecylerViewAdapter(getContext(), this, userStrainDetailsDataModels);
        strain_recylerview.setAdapter(adapter);

        Hybrid_Dialog_Text = view.findViewById(R.id.hybrid_dialog_text);
        final String hybrid_dilog_text = "Hybrid strains are a cross-breed of Indica and Sativa strains. Due to the plethora of possible combinations, the medical benefits, effects and sensations vary greatly.<br>" +
                "<br>" +
                "Hybrids are most commonly created to target and treat specific medical conditions and illnesses.";
        Hybrid_Dialog_Text.setText(Html.fromHtml(hybrid_dilog_text));
        Hybrrid_Dialog_Layout = view.findViewById(R.id.dialog_view);
        Hybrid_Info_Button = view.findViewById(R.id.hybrid_info_button);
        Hybrid_Info_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (!isHybrid_dialog) {
//                    isHybrid_dialog = true;
//                    Hybrrid_Dialog_Layout.setVisibility(View.VISIBLE);
//                    Animation startAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.fade_in);
//                    Hybrrid_Dialog_Layout.startAnimation(startAnimation);
//                }
//                isIndica_dialog = false;
//                Indica_Dialog_Layout.setVisibility(View.GONE);
//
//                isSativa_dialog = false;
//                Sativa_Dialog_Layout.setVisibility(View.GONE);
                isEdit_dialog = false;
                Edits_Dialog_Layout.setVisibility(View.GONE);
//                strainDetailsActivity_scrollView.fullScroll(View.FOCUS_UP);

                ShowToolTipDialog(view, Gravity.TOP, R.layout.dialogue_layout_type, hybrid_dilog_text);

            }
        });

        Hybrid_Cross_button = view.findViewById(R.id.hybrid_cross_btn);
        Hybrid_Cross_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isHybrid_dialog = false;
                Hybrrid_Dialog_Layout.setVisibility(View.GONE);
                Animation startAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.fade_out);
                Hybrrid_Dialog_Layout.startAnimation(startAnimation);
            }
        });


        // Indica

        Indica_Dialog_Text = view.findViewById(R.id.indica_dialog_text);
        final String indica_dilog_text = "Indica plants typically grow short and wide which makes them better suited for indoor growing. Indica-dominant strains tend to have a strong sweet/sour odor. <br>" +
                "<br>" +
                "Indicas are very effective for overall pain relief and helpful in treating general anxiety, body pain, and sleeping disorders. It is commonly used in the evening or even right before bed due to it’s relaxing effects.<br>" +
                "<br>" +
                "<b>Most Commonly Known Benefits:</b> <br>" +
                "1.\tRelieves body pain<br>" +
                "2.\tRelaxes muscles<br>" +
                "3.\tRelieves spasms, reduces seizures<br>" +
                "4.\tRelieves headaches and migraines<br>" +
                "5.\tRelieves anxiety or stress";
        Indica_Dialog_Text.setText(Html.fromHtml(indica_dilog_text));
        Indica_Dialog_Layout = view.findViewById(R.id.indica_dialog_view);
        Indica_Info_Button = view.findViewById(R.id.indica_info_button);
        Indica_Info_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (!isIndica_dialog) {
//                    isIndica_dialog = true;
//                    Indica_Dialog_Layout.setVisibility(View.VISIBLE);
//                    Animation startAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.fade_in);
//                    Indica_Dialog_Layout.startAnimation(startAnimation);
//                }


//                isHybrid_dialog = false;
//                Hybrrid_Dialog_Layout.setVisibility(View.GONE);
//                isSativa_dialog = false;
//                Sativa_Dialog_Layout.setVisibility(View.GONE);
//                strainDetailsActivity_scrollView.fullScroll(View.FOCUS_UP);
                isEdit_dialog = false;
                Edits_Dialog_Layout.setVisibility(View.GONE);
                ShowToolTipDialog(view, Gravity.TOP, R.layout.dialogue_layout_indica, indica_dilog_text);


            }
        });
        Indica_Cross_button = view.findViewById(R.id.indica_cross_btn);
        Indica_Cross_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isIndica_dialog = false;
                Indica_Dialog_Layout.setVisibility(View.GONE);
                Animation startAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.fade_out);
                Indica_Dialog_Layout.startAnimation(startAnimation);
            }
        });


        //Sativa
        Sativa_Dialog_Text = view.findViewById(R.id.sativa_dialog_text);
        final String sativa_dilog_text = "Sativa plants grow tall and thin, making them better suited for outdoor growing- some strains can reach over 25 ft. in height. Sativa-dominant strains tend to have a more grassy-type odor.<br>" +
                "<br>" +
                "Sativa effects are known to spark creativity and produce energetic and uplifting sensations. It is commonly used in the daytime due to its cerebral stimulation.<br>" +
                "<br>" +
                "<b>Most Commonly Known Benefits:</b> <br>" +
                "1.\tProduces feelings of well-being<br>" +
                "2.\tUplifting and cerebral thoughts<br>" +
                "3.\tStimulates and energizes<br>" +
                "4.\tIncreases focus and creativity<br>" +
                "5.\tFights depression";

        Sativa_Dialog_Text.setText(Html.fromHtml(sativa_dilog_text));
        Sativa_Dialog_Layout = view.findViewById(R.id.sativa_dialog_view);
        Sativa_Info_Button = view.findViewById(R.id.sativa_info_button);
        Sativa_Info_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (!isSativa_dialog) {
//                    isSativa_dialog = true;
//                    Sativa_Dialog_Layout.setVisibility(View.VISIBLE);
//                    Animation startAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.fade_in);
//                    Sativa_Dialog_Layout.startAnimation(startAnimation);
//                }
//
//                isIndica_dialog = false;
//                Indica_Dialog_Layout.setVisibility(View.GONE);
//
//                isHybrid_dialog = false;
//                Hybrrid_Dialog_Layout.setVisibility(View.GONE);
//
//                strainDetailsActivity_scrollView.fullScroll(View.FOCUS_UP);
                isEdit_dialog = false;
                Edits_Dialog_Layout.setVisibility(View.GONE);
                ShowToolTipDialog(view, Gravity.TOP, R.layout.dialogue_layout_stiva, sativa_dilog_text);

            }
        });
        Sativa_Cross_button = view.findViewById(R.id.sativa_cross_btn);
        Sativa_Cross_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isSativa_dialog = false;
                Sativa_Dialog_Layout.setVisibility(View.GONE);
                Animation startAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.fade_out);
                Sativa_Dialog_Layout.startAnimation(startAnimation);
            }
        });


        //Edits
        Edit_Dialog_Text = view.findViewById(R.id.edits_dialog_text);
        final String edit_dilog_text = "<b>Your Vote Counts!</b> <br><br>By up-voting a user submitted description, you are endorsing that the information provided is the best among other user submissions.<br><br>The highest voted description becomes the featured description for this strain.";
        Edit_Dialog_Text.setText(Html.fromHtml(edit_dilog_text));
        Edits_Dialog_Layout = view.findViewById(R.id.edits_dialogg);
        bottom_views = view.findViewById(R.id.bottom_views);

        Edits_Info_Button = view.findViewById(R.id.edit_infoo);
        Edits_Info_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (!isEdit_dialog) {
//                    isEdit_dialog = true;
//                    Edits_Dialog_Layout.setVisibility(View.VISIBLE);
//                    Animation startAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.fade_in);
//                    Edits_Dialog_Layout.startAnimation(startAnimation);
//                }
//                isHybrid_dialog = false;
//                Hybrrid_Dialog_Layout.setVisibility(View.GONE);
//                isSativa_dialog = false;
//                Sativa_Dialog_Layout.setVisibility(View.GONE);
//                isIndica_dialog = false;
//                Indica_Dialog_Layout.setVisibility(View.GONE);
                isEdit_dialog = false;
                Edits_Dialog_Layout.setVisibility(View.GONE);
                ShowToolTipDialog(view, Gravity.TOP, R.layout.dialogue_layout_edit, edit_dilog_text);


            }
        });
        Edits_Cross_button = view.findViewById(R.id.editss_cross_btn);
        Edits_Cross_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isEdit_dialog = false;
                Edits_Dialog_Layout.setVisibility(View.GONE);
                Animation startAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.fade_out);
                Edits_Dialog_Layout.startAnimation(startAnimation);
            }
        });


        Main_Layout = view.findViewById(R.id.main_layout);
        Refresh_Layout = view.findViewById(R.id.refresh);
        Refresh_Layout.setVisibility(View.VISIBLE);
        Main_Layout.setVisibility(View.GONE);
        scroll_to_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        strainDetailsActivity_scrollView.smoothScrollTo(0, Edits_Layout1.getBottom());
                    }
                }, 50);
            }
        });
        JSONObject jsonObject = new JSONObject();
        new VollyAPICall(getContext(), false, URL.get_user_strain + "/" + strainDataModel.getId(), jsonObject, user.getSession_key(), Request.Method.GET, StrainDetailsTabFragment.this, get_user_strain);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_more_ifon_strain:
                referesh = true;
                GoTo(getContext(), AddMoreInfoAboutStrain.class);
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (referesh) {
            referesh = false;
            JSONObject jsonObject = new JSONObject();
            new VollyAPICall(getContext(), false, URL.get_user_strain + "/" + strainDataModel.getId(), jsonObject, user.getSession_key(), Request.Method.GET, StrainDetailsTabFragment.this, get_user_strain);
        }
    }

    public void InitView(View view) {
        scroll_to_edit = view.findViewById(R.id.scroll_to_edit);
        Like_Count = view.findViewById(R.id.like_count);
        cross_layout = view.findViewById(R.id.cross_layout);
        Discription = view.findViewById(R.id.view_two);
        Indica_percentage = view.findViewById(R.id.indica_percentage);
        Stiva_percentage = view.findViewById(R.id.stiva_percentage);
        Cross_Bread_one = view.findViewById(R.id.cross_bread_one);
        Cross_bread_two = view.findViewById(R.id.cross_bread_two);
        Difficulty_level_icon = view.findViewById(R.id.difficulty_level_icon);
        Difficulty_level_text = view.findViewById(R.id.difficulty_level_text);
        Mature_Height = view.findViewById(R.id.mature_height);
        Following_times = view.findViewById(R.id.following_time);
        min_CBD = view.findViewById(R.id.min_CBD);
        max_CBD = view.findViewById(R.id.max_CBD);
        min_THC = view.findViewById(R.id.min_THC);
        max_THC = view.findViewById(R.id.max_THC);
        zone_min_forhn_temp = view.findViewById(R.id.zone_min_forhn_temp);
        zone_max_forhn_temp = view.findViewById(R.id.zone_max_forhn_temp);
        zone_celc_temp = view.findViewById(R.id.zone_celc_temp);
        yeild = view.findViewById(R.id.yeild);
        climate = view.findViewById(R.id.climate);
        notes = view.findViewById(R.id.notes);
        customSeekBar = view.findViewById(R.id.customSeekBar);
        strain_edits = view.findViewById(R.id.strain_edits);
        strain_edits_show = view.findViewById(R.id.strain_edits_show);
        scroll_to_edit.setVisibility(View.GONE);
    }

    public int[] generate_gradient(int progress) {
        progress = Math.abs(100 - progress);
//        if (progress < 15) {
//            return new int[]{0xFFae59c2, 0xFFc24462, 0xFFc24462, 0xFFc24462, 0xFFc24462, 0xFFc24462, 0xFFc24462};
//        } else if (progress >= 15 && progress <= 30) {
//            return new int[]{0xFFae59c2, 0xFFae59c2, 0xFFc24462, 0xFFc24462, 0xFFc24462, 0xFFc24462, 0xFFc24462};
//        } else if (progress >= 30 && progress <= 45) {
//            return new int[]{0xFFae59c2, 0xFFae59c2, 0xFFae59c2, 0xFFc24462, 0xFFc24462, 0xFFc24462, 0xFFc24462};
//        } else if (progress >= 45 && progress <= 60) {
//            return new int[]{0xFFae59c2, 0xFFae59c2, 0xFFae59c2, 0xFFae59c2, 0xFFc24462, 0xFFc24462, 0xFFc24462};
//        } else if (progress >= 60 && progress <= 75) {
//            return new int[]{0xFFae59c2, 0xFFae59c2, 0xFFae59c2, 0xFFae59c2, 0xFFae59c2, 0xFFc24462, 0xFFc24462};
//        } else if (progress >= 75 && progress <= 90) {
//            return new int[]{0xFFae59c2, 0xFFae59c2, 0xFFae59c2, 0xFFae59c2, 0xFFae59c2, 0xFFae59c2, 0xFFc24462};
//        } else if (progress >= 90 && progress <= 100) {
//            return new int[]{0xFFae59c2, 0xFFae59c2, 0xFFae59c2, 0xFFae59c2, 0xFFae59c2, 0xFFae59c2, 0xFFc24462};
//        } else {
//            return new int[]{0xFFae59c2, 0xFFc24462};
//        }
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

    @Override
    public void viewAllEditsButtonClick(int position, UserStrainDetailsDataModel userStrainDetailsDataModel) {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                strainDetailsActivity_scrollView.fullScroll(View.FOCUS_UP);
            }
        }, 50);
        SetData(position, true);
        strain_recylerview.setVisibility(View.GONE);
        Edits_Layout.setVisibility(View.GONE);
        scroll_to_edit.setVisibility(View.GONE);
        Edits_Layout1.setVisibility(View.GONE);
        Add_More_Info_Strain.setVisibility(View.GONE);
        this.Listner.viewAllEditsButtonClick(position, userStrainDetailsDataModels.get(position));
    }

    @Override
    public void onRequestSuccess(String response, APIActions.ApiActions apiActions) {
        Log.d("response", response);
        Refresh_Layout.setVisibility(View.GONE);
        if (apiActions == get_user_strain) {
            try {
                JSONObject jsonObject = new JSONObject(response).getJSONObject("successData");
                JSONArray strain_array = jsonObject.getJSONArray("user_strains");
                userStrainDetailsDataModels.clear();
                for (int x = 0; x < strain_array.length(); x++) {
                    JSONObject obj = strain_array.getJSONObject(x);
                    UserStrainDetailsDataModel userStrainDetailsDataModel = new UserStrainDetailsDataModel();
                    userStrainDetailsDataModel.setId(obj.getInt("id"));
                    userStrainDetailsDataModel.setStrain_id(obj.getInt("strain_id"));
                    userStrainDetailsDataModel.setUser_id(obj.getInt("user_id"));
                    userStrainDetailsDataModel.setIndica(obj.getInt("indica"));
                    userStrainDetailsDataModel.setSativa(obj.getInt("sativa"));
                    userStrainDetailsDataModel.setGet_user_like_count(obj.getInt("get_user_like_count"));
                    userStrainDetailsDataModel.setGenetics(obj.getString("genetics"));
                    userStrainDetailsDataModel.setCross_breed(obj.getString("cross_breed"));
                    userStrainDetailsDataModel.setGrowing(obj.getString("growing").substring(0, 1).toUpperCase() + obj.getString("growing").substring(1));

                    userStrainDetailsDataModel.setPlant_height(obj.getInt("plant_height"));
                    userStrainDetailsDataModel.setFlowering_time(obj.getInt("flowering_time"));
                    userStrainDetailsDataModel.setMin_fahren_temp(obj.getInt("min_fahren_temp"));
                    userStrainDetailsDataModel.setMax_fahren_temp(obj.getInt("max_fahren_temp"));
                    userStrainDetailsDataModel.setMin_celsius_temp(obj.getInt("min_celsius_temp"));
                    userStrainDetailsDataModel.setMax_celsius_temp(obj.getInt("max_celsius_temp"));
                    if (!obj.isNull("min_CBD")) {
                        userStrainDetailsDataModel.setMin_CBD(String.valueOf(obj.getDouble("min_CBD")));
                    } else {
                        userStrainDetailsDataModel.setMin_CBD("0.1");
                    }

                    if (!obj.isNull("max_CBD")) {
                        userStrainDetailsDataModel.setMax_CBD(String.valueOf(obj.getDouble("max_CBD")));
                    } else {
                        userStrainDetailsDataModel.setMax_CBD("0.2");
                    }

                    if (!obj.isNull("min_THC")) {
                        userStrainDetailsDataModel.setMin_THC(String.valueOf(obj.getDouble("min_THC")));
                    } else {
                        userStrainDetailsDataModel.setMin_THC("0.1");
                    }


                    if (!obj.isNull("max_THC")) {
                        userStrainDetailsDataModel.setMax_THC(String.valueOf(obj.getDouble("max_THC")));
                    } else {
                        userStrainDetailsDataModel.setMax_THC("0.2");
                    }

                    userStrainDetailsDataModel.setYeild(obj.getString("yeild").substring(0, 1).toUpperCase() + obj.getString("yeild").substring(1));
                    userStrainDetailsDataModel.setClimate(obj.getString("climate").substring(0, 1).toUpperCase() + obj.getString("climate").substring(1));

                    userStrainDetailsDataModel.setNote(obj.getString("note"));
                    if (!obj.isNull("description")) {
                        if (obj.getString("description").trim().length() == 0) {
                            userStrainDetailsDataModel.setDescription("No description added.");
                        } else {
                            userStrainDetailsDataModel.setDescription(obj.getString("description"));
                        }

                    } else {
                        userStrainDetailsDataModel.setDescription("No description added.");
                    }
                    userStrainDetailsDataModel.setCreated_at(obj.getString("created_at"));
                    userStrainDetailsDataModel.setUpdated_at(obj.getString("updated_at"));
                    userStrainDetailsDataModel.setGet_likes_count(obj.getInt("get_likes_count"));
                    userStrainDetailsDataModel.setUser_name(obj.getJSONObject("get_user").getString("first_name"));
                    userStrainDetailsDataModel.setUser_image_path(obj.getJSONObject("get_user").getString("image_path"));
                    userStrainDetailsDataModel.setUser_point(obj.getJSONObject("get_user").getInt("points"));
                    userStrainDetailsDataModel.setAvatar(obj.getJSONObject("get_user").getString("avatar"));
                    userStrainDetailsDataModel.setSpecial_icon(obj.getJSONObject("get_user").getString("special_icon"));
                    userStrainDetailsDataModels.add(userStrainDetailsDataModel);
                }
                if (userStrainDetailsDataModels.size() > 0) {
                    SetData(0, false);
                    Main_Layout.setVisibility(View.VISIBLE);
                    Add_More_Info_Strain.setVisibility(View.VISIBLE);
                    bottom_views.setVisibility(View.VISIBLE);
                    show_empty.setVisibility(View.GONE);
                    add_more_ifon_strain_new.setVisibility(View.GONE);
                } else {
                    show_empty.setVisibility(View.VISIBLE);
                    add_more_ifon_strain_new.setVisibility(View.VISIBLE);
                    Main_Layout.setVisibility(View.GONE);
                    Add_More_Info_Strain.setVisibility(View.VISIBLE);
//                    DO WORK FOR ONLY DISPLAY DESCRIPTION
                    Main_Layout.setVisibility(View.VISIBLE);
                    bottom_views.setVisibility(View.GONE);
                    scroll_to_edit.setVisibility(View.GONE);
                    MakeKeywordClickableText(Discription.getContext(), strainDiscription, Discription);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void onRequestError(String response, APIActions.ApiActions apiActions) {
        Log.d("response", response);
        Refresh_Layout.setVisibility(View.GONE);
        Main_Layout.setVisibility(View.VISIBLE);
        try {
            JSONObject object = new JSONObject(response);
            CustomeToast.ShowCustomToast(getContext(), object.getString("errorMessage"), Gravity.TOP);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void SetData(int position, boolean isTapped) {
        UserStrainDetailsDataModel userStrainDetailsDataModel = userStrainDetailsDataModels.get(position);
//        Discription.setText(userStrainDetailsDataModel.getDescription());
        MakeKeywordClickableText(Discription.getContext(), strainDiscription, Discription);
        if (isTapped) {
            scroll_to_edit.setVisibility(View.GONE);
            in_between.setVisibility(View.VISIBLE);
            MakeKeywordClickableText(Discription.getContext(), userStrainDetailsDataModel.getDescription(), Discription);
        } else {
            if (userStrainDetailsDataModel.getGet_likes_count() > 4) {
                strain_edits_show.setVisibility(View.VISIBLE);
                scroll_to_edit.setVisibility(View.VISIBLE);
                in_between.setVisibility(View.VISIBLE);
                MakeKeywordClickableText(Discription.getContext(), userStrainDetailsDataModel.getDescription(), Discription);
            } else {
                in_between.setVisibility(View.GONE);
            }
        }
//TODO EDIT

        Like_Count.setText(userStrainDetailsDataModel.getGet_likes_count() + "");
        Stiva_percentage.setText(userStrainDetailsDataModel.getSativa() + "%");
        Indica_percentage.setText(userStrainDetailsDataModel.getIndica() + "%");
        int progress = Math.abs(100 - userStrainDetailsDataModel.getIndica());
        customSeekBar.setProgress(Math.abs(100 - userStrainDetailsDataModel.getIndica()));

        if (progress == 0) {
//            genetics = "indica";
            type_strain.setText("Indica");
            type_strain.setTextColor(Color.parseColor("#ae59c2"));
            cross_layout.setVisibility(View.GONE);
//                    type_main_Strain.setText("Sativa");
        } else if (progress == 100) {
//            genetics = "sativa";
            type_strain.setText("Sativa");
            type_strain.setTextColor(Color.parseColor("#c24462"));
            cross_layout.setVisibility(View.GONE);
//                    type_main_Strain.setText("Indica");
        } else {
//            genetics = "hybrid";
            type_strain.setText("Hybird");
            type_strain.setTextColor(Color.parseColor("#7cc244"));
            cross_layout.setVisibility(View.VISIBLE);
        }
        final GradientDrawable gd = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, generate_gradient(progress));
        gd.setCornerRadius(0f);
        Back_view_slide_bar.setBackground(gd);
        if (userStrainDetailsDataModel.getCross_breed().contains(",")) {
            if (userStrainDetailsDataModel.getCross_breed().split(",").length > 0) {
                if (userStrainDetailsDataModel.getCross_breed().split(",").length == 1) {
                    Cross_Bread_one.setText(userStrainDetailsDataModel.getCross_breed().split(",")[0]);
                    Cross_bread_two.setText("");
                } else {
                    Cross_Bread_one.setText(userStrainDetailsDataModel.getCross_breed().split(",")[0]);
                    Cross_bread_two.setText(userStrainDetailsDataModel.getCross_breed().split(",")[1]);
                }
            } else {
                Cross_Bread_one.setText("");
                Cross_bread_two.setText("");
            }
        } else {
            if (!userStrainDetailsDataModel.getCross_breed().equalsIgnoreCase("null") && userStrainDetailsDataModel.getCross_breed() != null) {
                Cross_Bread_one.setText(userStrainDetailsDataModel.getCross_breed().split(",")[0]);
                Cross_bread_two.setText("");
            }
        }
        Cross_Bread_one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Cross_Bread_one.getText().toString().trim().length() > 0) {

                    Intent strain_intetn = new Intent(v.getContext(), StrainDetailsActivity.class);
                    strain_intetn.putExtra("isKeyValuApi", true);
                    strain_intetn.putExtra("keyWordApi", Cross_Bread_one.getText().toString());
                    startActivity(strain_intetn);
                }
            }
        });
        Cross_bread_two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Cross_bread_two.getText().toString().trim().length() > 0) {
                    Intent strain_intetn = new Intent(v.getContext(), StrainDetailsActivity.class);
                    strain_intetn.putExtra("isKeyValuApi", true);
                    strain_intetn.putExtra("keyWordApi", Cross_bread_two.getText().toString());
                    startActivity(strain_intetn);
                }
            }
        });

        Difficulty_level_text.setText(userStrainDetailsDataModel.getGrowing());
        switch (userStrainDetailsDataModel.getGrowing().toLowerCase()) {
            case "easy":
                Difficulty_level_icon.setImageResource(R.drawable.ic_easy_dft);
                break;
            case "moderate":
                Difficulty_level_icon.setImageResource(R.drawable.ic_mid_dft);
                break;
            case "hard":
                Difficulty_level_icon.setImageResource(R.drawable.ic_hard_dft);
                break;
        }
        Mature_Height.setText(userStrainDetailsDataModel.getPlant_height() + "\"");
        Following_times.setText(userStrainDetailsDataModel.getFlowering_time() + "");
        zone_min_forhn_temp.setText(userStrainDetailsDataModel.getMin_fahren_temp() + "℉");
        zone_max_forhn_temp.setText(userStrainDetailsDataModel.getMax_fahren_temp() + "℉");
        zone_celc_temp.setText(userStrainDetailsDataModel.getMin_celsius_temp() + "℃\t-\t" + userStrainDetailsDataModel.getMax_celsius_temp() + "℃");
        min_CBD.setText(userStrainDetailsDataModel.getMin_CBD() + "%");
        min_THC.setText(userStrainDetailsDataModel.getMin_THC() + "%");
        max_CBD.setText(userStrainDetailsDataModel.getMax_CBD() + "%");
        max_THC.setText(userStrainDetailsDataModel.getMax_THC() + "%");
        yeild.setText(userStrainDetailsDataModel.getYeild());
        climate.setText(userStrainDetailsDataModel.getClimate());
//        notes.setText(userStrainDetailsDataModel.getNote());

        MakeKeywordClickableText(notes.getContext(), userStrainDetailsDataModel.getNote(), notes);
        adapter.notifyDataSetChanged();
        strain_edits.setText((userStrainDetailsDataModels.size()) + " Edits");
    }

    public static void ShowToolTipDialog(final View v, final int position, int layout, String text) {

        final SimpleTooltip tooltip = new SimpleTooltip.Builder(v.getContext())
                .anchorView(v)
                .focusable(true)
                .arrowDrawable(null)
                .showArrow(false)
                .text("Sample Text")
                .dismissOnOutsideTouch(true)
                .dismissOnInsideTouch(true)
                .modal(true)
                .gravity(Gravity.TOP)
                .animated(false)
                .contentView(layout)
                .focusable(true)
                .overlayMatchParent(true)
                .build();
        TextView textView = tooltip.findViewById(R.id.text);
        textView.setText(Html.fromHtml(text));
        ImageView crossBtn = tooltip.findViewById(R.id.cross_btn);
        crossBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tooltip.dismiss();
            }
        });
        tooltip.show();
    }

}

