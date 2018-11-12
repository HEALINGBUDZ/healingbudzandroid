package com.codingpixel.healingbudz.activity.home.home_fragment.budz_map_tab.tabs;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.text.util.Linkify;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.codingpixel.healingbudz.activity.home.home_fragment.budz_map_tab.BudzMapDetailsActivity;
import com.codingpixel.healingbudz.DataModel.BudzMapReviews;
import com.codingpixel.healingbudz.DataModel.PaymentMethod;
import com.codingpixel.healingbudz.DataModel.ReplyBudz;
import com.codingpixel.healingbudz.R;
import com.codingpixel.healingbudz.Utilities.CallUser;
import com.codingpixel.healingbudz.Utilities.Flags;
import com.codingpixel.healingbudz.adapter.AddNewPaymentMethodRecylerAdapter;
import com.codingpixel.healingbudz.customeUI.CustomeToast;
import com.codingpixel.healingbudz.data_structure.APIActions;
import com.codingpixel.healingbudz.interfaces.TabChangeListner;
import com.codingpixel.healingbudz.network.VollyAPICall;
import com.codingpixel.healingbudz.network.model.APIResponseListner;
import com.codingpixel.healingbudz.network.model.URL;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.codingpixel.healingbudz.activity.home.home_fragment.budz_map_tab.BudzMapHomeFragment.budz_map_item_clickerd_dataModel;
import static com.codingpixel.healingbudz.activity.home.home_fragment.budz_map_tab.BudzMapHomeFragment.getActivityForResultResponse;
import static com.codingpixel.healingbudz.activity.home.home_fragment.budz_map_tab.BudzMapHomeFragment.getCurrentDat;
import static com.codingpixel.healingbudz.activity.home.home_fragment.budz_map_tab.BudzMapHomeFragment.isSearchedText;
import static com.codingpixel.healingbudz.activity.home.home_fragment.budz_map_tab.BudzMapHomeFragment.isSearchedTextValue;
import static com.codingpixel.healingbudz.activity.splash.Splash.user;
import static com.codingpixel.healingbudz.Utilities.ClickAbleKeywordText.MakeKeywordClickableText;
import static com.codingpixel.healingbudz.data_structure.APIActions.ApiActions.get_budz_profile;
import static com.codingpixel.healingbudz.data_structure.APIActions.ApiActions.save_budz_call_click;
import static com.codingpixel.healingbudz.static_function.UIModification.HideKeyboard;

public class DetailsBusinessInfoMedicalTabFragment extends Fragment implements APIResponseListner {
    ImageView Attachment, Comment_img, Comment_img2;
    TabChangeListner listner;
    EditText Review_Edittest;
    LinearLayout Main_Layout, Refresh, show_hours;
    View view;
    TextView discription, zip_code, languages, insurance_eccepted, office_policies, pre_vist_requirments, location, contact, instagram, twitter, facebook, website, email, not_found_payment;
    Button Call_User;
    public static boolean isFresh = false;
    RecyclerView payment_method_recycler_view;


    AddNewPaymentMethodRecylerAdapter addNewPaymentMethodRecylerAdapter;
    List<PaymentMethod> paymentMethodList = new ArrayList<>();

    public DetailsBusinessInfoMedicalTabFragment(TabChangeListner tabChangeListner) {
        this.listner = tabChangeListner;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.details_business_info_medical_tab_layout, container, false);
        Init(view);
        view.findViewById(R.id.show_pruproduct).setVisibility(View.GONE);
        not_found_payment = view.findViewById(R.id.not_found_payment);
        show_hours = view.findViewById(R.id.show_hours);
        HideKeyboard(getActivity());
        Main_Layout = view.findViewById(R.id.main_layout);
        Main_Layout.setVisibility(View.GONE);
        Refresh = view.findViewById(R.id.refresh);
        Refresh.setVisibility(View.VISIBLE);
        apiCall(true);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isFresh) {
            isFresh = false;
            apiCall(false);
            ((BudzMapDetailsActivity) view.getContext()).SetTopDataCall();
        }
    }

    boolean isKeyy = false;

    void apiCall(boolean loader) {
        if (loader) {
            Refresh.setVisibility(View.VISIBLE);
            Main_Layout.setVisibility(View.GONE);
        }
        if (isSearchedText) {
            isSearchedText = false;
            JSONObject object = new JSONObject();
            new VollyAPICall(view.getContext(), false, URL.get_budz_profile + "/" + budz_map_item_clickerd_dataModel.getId() + "?keyword=" + isSearchedTextValue, object, user.getSession_key(), Request.Method.GET, DetailsBusinessInfoMedicalTabFragment.this, get_budz_profile);
//            isSearchedTextValue = "";
            isKeyy = true;
        } else {
            if (isKeyy) {

            } else {
                isKeyy = false;
            }
            JSONObject object = new JSONObject();
            new VollyAPICall(view.getContext(), false, URL.get_budz_profile + "/" + budz_map_item_clickerd_dataModel.getId(), object, user.getSession_key(), Request.Method.GET, DetailsBusinessInfoMedicalTabFragment.this, get_budz_profile);
        }

    }

    public void Init(View view) {
        discription = view.findViewById(R.id.discription);
        languages = view.findViewById(R.id.languages);
        insurance_eccepted = view.findViewById(R.id.insurance_eccepted);
        office_policies = view.findViewById(R.id.office_policies);
        pre_vist_requirments = view.findViewById(R.id.pre_vist_requirments);
        location = view.findViewById(R.id.location);
        location.setAutoLinkMask(Linkify.MAP_ADDRESSES);
        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Flags.openMap(v.getContext(), location.getText().toString().trim());
            }
        });
        contact = view.findViewById(R.id.contact);
        instagram = view.findViewById(R.id.instagram);
        twitter = view.findViewById(R.id.twitter);
        facebook = view.findViewById(R.id.facebook);
        website = view.findViewById(R.id.website);
        email = view.findViewById(R.id.email);
        zip_code = view.findViewById(R.id.zip_code);
        website.setAutoLinkMask(Linkify.ALL);
        facebook.setAutoLinkMask(Linkify.ALL);
        twitter.setAutoLinkMask(Linkify.ALL);
        instagram.setAutoLinkMask(Linkify.ALL);
        email.setAutoLinkMask(Linkify.EMAIL_ADDRESSES);
        contact.setAutoLinkMask(Linkify.ALL);
        contact.setLinkTextColor(getContext().getResources().getColor(R.color.color_link));
        website.setLinkTextColor(getContext().getResources().getColor(R.color.color_link));
        facebook.setLinkTextColor(getContext().getResources().getColor(R.color.color_link));
        twitter.setLinkTextColor(getContext().getResources().getColor(R.color.color_link));
        instagram.setLinkTextColor(getContext().getResources().getColor(R.color.color_link));
        email.setLinkTextColor(getContext().getResources().getColor(R.color.color_link));
        Call_User = view.findViewById(R.id.call_user);
        Call_User.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!contact.getText().toString().contains("Not Available") && contact.getText().toString().trim().length() > 0) {
                    JSONObject object = new JSONObject();
                    try {
                        object.put("sub_user_id", budz_map_item_clickerd_dataModel.getId());
                        if (isKeyy)
                            object.put("keyword", isSearchedTextValue);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    new VollyAPICall(view.getContext(), false, URL.save_budz_call_click, object, user.getSession_key(), Request.Method.POST, new APIResponseListner() {
                        @Override
                        public void onRequestSuccess(String response, APIActions.ApiActions apiActions) {
                            if (apiActions == save_budz_call_click) {
                                Log.d("onRequestSuccess: ", response);
                            }
                        }

                        @Override
                        public void onRequestError(String response, APIActions.ApiActions apiActions) {
                            if (apiActions == save_budz_call_click) {
                                Log.d("onRequestSuccess: ", response);
                            }
                        }
                    }, save_budz_call_click);
                    CallUser.callUserPhone(getActivity(), contact.getText().toString());
                } else {
                    CustomeToast.ShowCustomToast(view.getContext(), "No phone number found!", Gravity.TOP);
                }
            }
        });
        payment_method_recycler_view = view.findViewById(R.id.payment_method_selection);
        addNewPaymentMethodRecylerAdapter = new AddNewPaymentMethodRecylerAdapter(view.getContext(), paymentMethodList, true);
        FlexboxLayoutManager layoutManagerPayment = new FlexboxLayoutManager(getContext());
        layoutManagerPayment.setFlexDirection(FlexDirection.ROW);
        layoutManagerPayment.setJustifyContent(JustifyContent.FLEX_START);
        payment_method_recycler_view.setLayoutManager(layoutManagerPayment);
        payment_method_recycler_view.setAdapter(addNewPaymentMethodRecylerAdapter);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        getActivityForResultResponse.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestSuccess(String response, APIActions.ApiActions apiActions) {
        Log.d("response", response);
        Refresh.setVisibility(View.GONE);
        Main_Layout.setVisibility(View.VISIBLE);
        JSONObject jsonObject = null;
        try {
            ArrayList<BudzMapReviews> reviews = new ArrayList<>();
            jsonObject = new JSONObject(response);
            if (!jsonObject.getJSONObject("successData").isNull("paymant_methods")) {
                paymentMethodList.clear();
                JSONArray jsonArray = jsonObject.getJSONObject("successData").getJSONArray("paymant_methods");
                for (int i = 0; i < jsonArray.length(); i++) {
                    PaymentMethod paymentMethod = new PaymentMethod();
                    paymentMethod.setSelected(true);
                    paymentMethod.setId(jsonArray.getJSONObject(i).getJSONObject("method_detail").getInt("id"));
                    paymentMethod.setTitle(jsonArray.getJSONObject(i).getJSONObject("method_detail").getString("title"));
                    paymentMethod.setImage(jsonArray.getJSONObject(i).getJSONObject("method_detail").getString("image"));
                    paymentMethodList.add(paymentMethod);
                }
                addNewPaymentMethodRecylerAdapter.notifyDataSetChanged();
            }
            if (paymentMethodList.size() > 0) {
                not_found_payment.setVisibility(View.GONE);
                payment_method_recycler_view.setVisibility(View.VISIBLE);
            } else {
                payment_method_recycler_view.setVisibility(View.GONE);
                not_found_payment.setVisibility(View.VISIBLE);
            }
            JSONArray reviews_array = jsonObject.getJSONObject("successData").getJSONArray("review");
            for (int x = 0; x < reviews_array.length(); x++) {
                JSONObject review_object = reviews_array.getJSONObject(x);
                BudzMapReviews review = new BudzMapReviews();
                review.setId(review_object.getInt("id"));
                if (review_object.isNull("is_flaged")) {
                    review.setIs_user_flaged_count(0);
                } else {
                    review.setIs_user_flaged_count(1);
                }
                review.setReviewed_by(review_object.getInt("reviewed_by"));
                review.setReview(review_object.getString("text"));
                review.setCreated_at(review_object.getString("created_at"));
                review.setUpdated_at(review_object.getString("updated_at"));
                review.setUser_id(review_object.getJSONObject("user").getInt("id"));
                review.setUser_first_name(review_object.getJSONObject("user").getString("first_name"));
                review.setUser_avatar(review_object.getJSONObject("user").getString("avatar"));
                review.setUser_image_path(review_object.getJSONObject("user").getString("image_path"));
                review.setSpecial_icon(review_object.getJSONObject("user").getString("special_icon"));
                review.setUser_point(review_object.getJSONObject("user").getInt("points"));
                if (review_object.getInt("is_reviewed_count") == 0) {
                    review.setLiked(false);
                } else {
                    review.setLiked(true);
                }
                if (!review_object.isNull("rating")) {
                    review.setRating(review_object.getDouble("rating"));
                } else {
                    review.setRating(0);
                }
                review.setTotal_review(review_object.getInt("likes_count"));
                if (!review_object.isNull("attachments")) {
                    if (review_object.getJSONArray("attachments").length() > 0) {
                        review.setAttatchment_type(review_object.getJSONArray("attachments").getJSONObject(0).getString("type"));
                        review.setAttatchment_poster(review_object.getJSONArray("attachments").getJSONObject(0).optString("poster"));
                        review.setAttatchment_path(review_object.getJSONArray("attachments").getJSONObject(0).getString("attachment"));

                    }
                }
                if (!review_object.isNull("reply")) {
                    ReplyBudz replyBudz = new ReplyBudz();
                    replyBudz.setCreatedAt(review_object.getJSONObject("reply").getString("created_at"));
                    replyBudz.setUpdatedAt(review_object.getJSONObject("reply").getString("updated_at"));
                    replyBudz.setReply(review_object.getJSONObject("reply").getString("reply"));
                    replyBudz.setUserId(review_object.getJSONObject("reply").getInt("user_id"));
                    replyBudz.setBusinessReviewId(review_object.getJSONObject("reply").getInt("business_review_id"));
                    replyBudz.setId(review_object.getJSONObject("reply").getInt("id"));
                    review.setReply(replyBudz);
                }
                reviews.add(review);
            }
//            get_user_review_count
            new SetReviewaAndComments().InitData(getContext(), DetailsBusinessInfoMedicalTabFragment.this, view, reviews, jsonObject.getJSONObject("successData").getInt("get_user_review_count"));

            //set other data
            JSONObject object = jsonObject.getJSONObject("successData");
//            discription.setText(object.getString("description"));
            MakeKeywordClickableText(discription.getContext(), object.getString("description"), discription);
            if (!object.isNull("insurance_accepted")) {
                if (object.getString("insurance_accepted").equalsIgnoreCase("Yes")) {
                    insurance_eccepted.setText("Yes");
                } else {
                    insurance_eccepted.setText("No");
                }
            }

            if (object.isNull("office_policies")) {
//                office_policies.setText("Not Available");
                MakeKeywordClickableText(office_policies.getContext(), "Not Available", office_policies);
            } else {
//                office_policies.setText(object.getString("office_policies"));
                if (object.getString("office_policies").replace("null", "").length() > 0) {
                    MakeKeywordClickableText(office_policies.getContext(), object.getString("office_policies").replace("null", ""), office_policies);
                } else {
                    MakeKeywordClickableText(office_policies.getContext(), "Not Available", office_policies);
                }
            }

            if (object.isNull("visit_requirements")) {
//                pre_vist_requirments.setText("Not Available");
                MakeKeywordClickableText(pre_vist_requirments.getContext(), "Not Available", pre_vist_requirments);
            } else {
//                pre_vist_requirments.setText(object.getString("visit_requirements"));
                if (object.getString("visit_requirements").replace("null", "").length() > 0) {
                    MakeKeywordClickableText(pre_vist_requirments.getContext(), object.getString("visit_requirements").replace("null", ""), pre_vist_requirments);
                } else {
                    MakeKeywordClickableText(pre_vist_requirments.getContext(), "Not Available", pre_vist_requirments);
                }
            }
            if (!object.isNull("is_flaged_count")) {
                if (object.getInt("is_flaged_count") > 0) {

                    ((BudzMapDetailsActivity) view.getContext()).setFlaggedCall(true);
                } else {
                    ((BudzMapDetailsActivity) view.getContext()).setFlaggedCall(false);

                }
            } else {

                ((BudzMapDetailsActivity) view.getContext()).setFlaggedCall(false);

            }

            location.setText(object.getString("location").replace("null", ""));


            if (object.isNull("phone")) {
                contact.setText("Not Available");
            } else {
                contact.setText(object.getString("phone").replace("null", ""));
                if (TextUtils.isEmpty(contact.getText().toString())) {
                    contact.setText("Not Available");
                }
            }

            if (object.isNull("instagram")) {
                instagram.setText("Not Available");
            } else {
                instagram.setText(object.getString("instagram").replace("null", ""));
                if (TextUtils.isEmpty(instagram.getText().toString())) {
                    instagram.setText("Not Available");
                }
            }


            if (object.isNull("twitter")) {
                twitter.setText("Not Available");
            } else {
                twitter.setText(object.getString("twitter").replace("null", ""));
                if (TextUtils.isEmpty(twitter.getText().toString())) {
                    twitter.setText("Not Available");
                }
            }


            if (object.isNull("facebook")) {
                facebook.setText("Not Available");
            } else {
                facebook.setText(object.getString("facebook").replace("null", ""));
                if (TextUtils.isEmpty(facebook.getText().toString())) {
                    facebook.setText("Not Available");
                }
            }

            if (object.isNull("web")) {
                website.setText("Not Available");
            } else {
                website.setText(object.getString("web").replace("null", ""));
                if (TextUtils.isEmpty(website.getText().toString())) {
                    website.setText("Not Available");
                }
            }
            if (object.isNull("email")) {
                email.setText("Not Available");
            } else {
                email.setText(object.getString("email").replace("null", ""));
                if (TextUtils.isEmpty(email.getText().toString())) {
                    email.setText("Not Available");
                }
            }
            if (object.isNull("zip_code")) {
                zip_code.setText("Not Available");
            } else {
                zip_code.setText(object.getString("zip_code").replace("null", ""));
                if (TextUtils.isEmpty(zip_code.getText().toString())) {
                    zip_code.setText("Not Available");
                }
            }
            String Languages = "";
            if (!jsonObject.getJSONObject("successData").isNull("languages")) {
                JSONArray lng = jsonObject.getJSONObject("successData").getJSONArray("languages");
                for (int l = 0; l < lng.length(); l++) {
                    if (!lng.getJSONObject(l).isNull("get_language")) {
                        JSONObject lng_obg = lng.getJSONObject(l).getJSONObject("get_language");
                        if (Languages.length() == 0) {
                            Languages = Languages + "" + lng_obg.getString("name");
                        } else {
                            Languages = Languages + ", " + lng_obg.getString("name");
                        }
                    }
                }
                if (Languages.length() == 0) {
                    languages.setText("No Language Found");
                } else {
                    languages.setText(Languages);
                }
            } else {
                languages.setText("No Language Found");
            }


            if (!jsonObject.getJSONObject("successData").isNull("timeing")) {
                JSONObject timings = jsonObject.getJSONObject("successData").getJSONObject("timeing");
                SetTime(view, timings);
            } else {
                SetTime(view, null);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestError(String response, APIActions.ApiActions apiActions) {
        Log.d("response", response);
    }


    public static void SetTime(View view1, JSONObject time_Obj) {
        TextView Monday, Tuesday, Wednesday, Thursday, Friday, Saturday, Sunday, add_hours;
        LinearLayout parent_house = view1.findViewById(R.id.parent_house);
        LinearLayout _1 = view1.findViewById(R.id._1), _2 = view1.findViewById(R.id._2), _3 = view1.findViewById(R.id._3), _4 = view1.findViewById(R.id._4), _5 = view1.findViewById(R.id._5), _6 = view1.findViewById(R.id._6), _7 = view1.findViewById(R.id._7);
        TextView Monday_time, Tuesday_time, Wednesday_time, Thursday_time, Friday_time, Saturday_time, Sunday_time;
        Monday = view1.findViewById(R.id.mon_d);
        add_hours = view1.findViewById(R.id.add_hours);
        Tuesday = view1.findViewById(R.id.tue_d);
        Wednesday = view1.findViewById(R.id.wed_d);
        Thursday = view1.findViewById(R.id.thu_d);
        Friday = view1.findViewById(R.id.fri_d);
        Saturday = view1.findViewById(R.id.sat_d);
        Sunday = view1.findViewById(R.id.sun_d);

        Monday_time = view1.findViewById(R.id.mon_t);
        Tuesday_time = view1.findViewById(R.id.tue_t);
        Wednesday_time = view1.findViewById(R.id.wed_t);
        Thursday_time = view1.findViewById(R.id.thu_t);
        Friday_time = view1.findViewById(R.id.fri_t);
        Saturday_time = view1.findViewById(R.id.sat_t);
        Sunday_time = view1.findViewById(R.id.sun_t);
        if (time_Obj != null) {
            try {
                int is7 = 0;
                if (time_Obj.getString("monday").contains("Closed")) {
                    Monday_time.setText("Closed");
                    _1.setVisibility(View.GONE);
                    is7++;
                } else {
                    Monday_time.setText(time_Obj.getString("monday").replace("null", "") + " - " + time_Obj.getString("mon_end").replace("null", ""));
                }

                if (time_Obj.getString("tuesday").contains("Closed")) {
                    Tuesday_time.setText("Closed");
                    _2.setVisibility(View.GONE);
                    is7++;
                } else {
                    Tuesday_time.setText(time_Obj.getString("tuesday").replace("null", "") + " - " + time_Obj.getString("tue_end").replace("null", ""));
                }

                if (time_Obj.getString("wednesday").contains("Closed")) {
                    Wednesday_time.setText("Closed");
                    _3.setVisibility(View.GONE);
                    is7++;
                } else {
                    Wednesday_time.setText(time_Obj.getString("wednesday").replace("null", "") + " - " + time_Obj.getString("wed_end").replace("null", ""));
                }

                if (time_Obj.getString("thursday").contains("Closed")) {
                    Thursday_time.setText("Closed");
                    _4.setVisibility(View.GONE);
                    is7++;
                } else {
                    Thursday_time.setText(time_Obj.getString("thursday").replace("null", "") + " - " + time_Obj.getString("thu_end").replace("null", ""));
                }


                if (time_Obj.getString("friday").contains("Closed")) {
                    Friday_time.setText("Closed");
                    _5.setVisibility(View.GONE);
                    is7++;
                } else {
                    Friday_time.setText(time_Obj.getString("friday").replace("null", "") + " - " + time_Obj.getString("fri_end").replace("null", ""));
                }

                if (time_Obj.getString("saturday").contains("Closed")) {
                    Saturday_time.setText("Closed");
                    _6.setVisibility(View.GONE);
                    is7++;
                } else {
                    Saturday_time.setText(time_Obj.getString("saturday").replace("null", "") + " - " + time_Obj.getString("sat_end").replace("null", ""));
                }

                if (time_Obj.getString("sunday").contains("Closed")) {
                    Sunday_time.setText("Closed");
                    _7.setVisibility(View.GONE);
                    is7++;
                } else {
                    Sunday_time.setText(time_Obj.getString("sunday").replace("null", "") + " - " + time_Obj.getString("sun_end").replace("null", ""));
                }
                add_hours.setVisibility(View.GONE);
                parent_house.setVisibility(View.VISIBLE);
                if (is7 == 7) {
                    parent_house.setVisibility(View.GONE);
                    if (budz_map_item_clickerd_dataModel.getUser_id() == user.getUser_id()) {
                        add_hours.setVisibility(View.VISIBLE);
                    } else {
                        view1.findViewById(R.id.show_hours).setVisibility(View.GONE);
                        add_hours.setVisibility(View.GONE);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else {
            parent_house.setVisibility(View.GONE);
            if (budz_map_item_clickerd_dataModel.getUser_id() == user.getUser_id()) {
                add_hours.setVisibility(View.VISIBLE);
            } else {
                view1.findViewById(R.id.show_hours).setVisibility(View.GONE);
                add_hours.setVisibility(View.GONE);
            }
            Monday_time.setText("Closed");
            Tuesday_time.setText("Closed");
            Wednesday_time.setText("Closed");
            Thursday_time.setText("Closed");
            Friday_time.setText("Closed");
            Saturday_time.setText("Closed");
            Sunday_time.setText("Closed");

        }


        switch (getCurrentDat()) {
            case "sunday":
                Sunday.setText("Today");
                Sunday.setTextColor(Color.parseColor("#e070e0"));
                Sunday_time.setTextColor(Color.parseColor("#e070e0"));
                break;
            case "monday":
                Monday.setText("Today");
                Monday.setTextColor(Color.parseColor("#e070e0"));
                Monday_time.setTextColor(Color.parseColor("#e070e0"));
                break;
            case "tuesday":
                Tuesday.setText("Today");
                Tuesday.setTextColor(Color.parseColor("#e070e0"));
                Tuesday_time.setTextColor(Color.parseColor("#e070e0"));
                break;
            case "wednesday":
                Wednesday.setText("Today");
                Wednesday.setTextColor(Color.parseColor("#e070e0"));
                Wednesday_time.setTextColor(Color.parseColor("#e070e0"));

                break;
            case "thursday":
                Thursday.setText("Today");
                Thursday.setTextColor(Color.parseColor("#e070e0"));
                Thursday_time.setTextColor(Color.parseColor("#e070e0"));
                break;
            case "friday":
                Friday.setText("Today");
                Friday.setTextColor(Color.parseColor("#e070e0"));
                Friday_time.setTextColor(Color.parseColor("#e070e0"));
                break;
            case "saturday":
                Saturday.setText("Today");
                Saturday.setTextColor(Color.parseColor("#e070e0"));
                Saturday_time.setTextColor(Color.parseColor("#e070e0"));
                break;
        }
    }
}
