package com.codingpixel.healingbudz.activity.home.home_fragment.budz_map_tab.tabs;

import android.content.Intent;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.codingpixel.healingbudz.DataModel.BudzMapReviews;
import com.codingpixel.healingbudz.DataModel.PaymentMethod;
import com.codingpixel.healingbudz.DataModel.ReplyBudz;
import com.codingpixel.healingbudz.R;
import com.codingpixel.healingbudz.Utilities.CallUser;
import com.codingpixel.healingbudz.Utilities.Flags;
import com.codingpixel.healingbudz.activity.home.home_fragment.budz_map_tab.BudzMapDetailsActivity;
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

import static com.codingpixel.healingbudz.Utilities.ClickAbleKeywordText.MakeKeywordClickableText;
import static com.codingpixel.healingbudz.activity.home.home_fragment.budz_map_tab.BudzMapDetailsActivity.main_scroll_view;
import static com.codingpixel.healingbudz.activity.home.home_fragment.budz_map_tab.BudzMapHomeFragment.budz_map_item_clickerd_dataModel;
import static com.codingpixel.healingbudz.activity.home.home_fragment.budz_map_tab.BudzMapHomeFragment.getActivityForResultResponse;
import static com.codingpixel.healingbudz.activity.home.home_fragment.budz_map_tab.BudzMapHomeFragment.isSearchedText;
import static com.codingpixel.healingbudz.activity.home.home_fragment.budz_map_tab.BudzMapHomeFragment.isSearchedTextValue;
import static com.codingpixel.healingbudz.activity.splash.Splash.user;
import static com.codingpixel.healingbudz.data_structure.APIActions.ApiActions.get_budz_profile;
import static com.codingpixel.healingbudz.data_structure.APIActions.ApiActions.save_budz_call_click;
import static com.codingpixel.healingbudz.data_structure.APIActions.ApiActions.save_budz_ticket_click;
import static com.codingpixel.healingbudz.static_function.UIModification.HideKeyboard;

public class DetailsBusinessInfoEventTabFragment extends Fragment implements APIResponseListner {
    ImageView Attachment, Comment_img, Comment_img2;
    TabChangeListner listner;
    Button Show_product;
    LinearLayout Main_Layout, Refresh;
    View view;
    Button Call_User;
    TextView location, discription, zip_code, instagram, twitter, facebook, website, contact, Event_Date, Event_Time, email, not_found_payment;
    public static boolean isFresh = false;
    RecyclerView payment_method_recycler_view;


    AddNewPaymentMethodRecylerAdapter addNewPaymentMethodRecylerAdapter;
    List<PaymentMethod> paymentMethodList = new ArrayList<>();

    public DetailsBusinessInfoEventTabFragment(TabChangeListner tabChangeListner) {
        this.listner = tabChangeListner;
        if (budz_map_item_clickerd_dataModel != null)
            IDOf = budz_map_item_clickerd_dataModel.getId();
    }

    int IDOf = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.details_business_info_event_tab_layout, container, false);
        HideKeyboard(getActivity());
        if (budz_map_item_clickerd_dataModel != null)
            IDOf = budz_map_item_clickerd_dataModel.getId();
        Init(view);
        Show_product = view.findViewById(R.id.show_pruproduct);
        not_found_payment = view.findViewById(R.id.not_found_payment);
        Show_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (budz_map_item_clickerd_dataModel.getIs_featured() == 1) {
                    JSONObject object = new JSONObject();
                    try {
                        object.put("sub_user_id", IDOf);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    new VollyAPICall(view.getContext(), false, URL.save_budz_ticket_click, object, user.getSession_key(), Request.Method.POST, new APIResponseListner() {
                        @Override
                        public void onRequestSuccess(String response, APIActions.ApiActions apiActions) {
                            if (apiActions == save_budz_ticket_click) {
                                Log.d("onRequestSuccess: ", response);
                            }
                        }

                        @Override
                        public void onRequestError(String response, APIActions.ApiActions apiActions) {
                            if (apiActions == save_budz_ticket_click) {
                                Log.d("onRequestSuccess: ", response);
                            }
                        }
                    }, save_budz_ticket_click);
                    listner.ShowProductTab();
                }
            }
        });
        if (budz_map_item_clickerd_dataModel.getIs_featured() != 1) {
            Show_product.setVisibility(View.GONE);
        }

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
            isKeyy = true;
            new VollyAPICall(view.getContext(), false, URL.get_budz_profile + "/" + IDOf + "?keyword=" + isSearchedTextValue, object, user.getSession_key(), Request.Method.GET, DetailsBusinessInfoEventTabFragment.this, get_budz_profile);
//            isSearchedTextValue = "";
        } else {
            if (isKeyy) {

            } else {
                isKeyy = false;
            }
            JSONObject object = new JSONObject();
            new VollyAPICall(view.getContext(), false, URL.get_budz_profile + "/" + IDOf, object, user.getSession_key(), Request.Method.GET, DetailsBusinessInfoEventTabFragment.this, get_budz_profile);
        }

    }

    public void Init(View view) {
        location = view.findViewById(R.id.location);
        location.setAutoLinkMask(Linkify.MAP_ADDRESSES);
        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Flags.openMap(v.getContext(), location.getText().toString().trim());
            }
        });
        discription = view.findViewById(R.id.discription);
        contact = view.findViewById(R.id.contact);
        zip_code = view.findViewById(R.id.zip_code);
        email = view.findViewById(R.id.email);
        instagram = view.findViewById(R.id.instagram);
        twitter = view.findViewById(R.id.twitter);
        facebook = view.findViewById(R.id.facebook);
        website = view.findViewById(R.id.website);
        Event_Time = view.findViewById(R.id.event_time_text);
        Event_Date = view.findViewById(R.id.event_date_text);
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
                        object.put("sub_user_id", IDOf);
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
            new SetReviewaAndComments().InitData(getContext(), DetailsBusinessInfoEventTabFragment.this, view, reviews, jsonObject.getJSONObject("successData").getInt("get_user_review_count"));


            JSONObject object = jsonObject.getJSONObject("successData");
//            discription.setText(object.getString("description"));
            MakeKeywordClickableText(discription.getContext(), object.getString("description"), discription);
            location.setText(object.getString("location"));
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
            if (!jsonObject.getJSONObject("successData").isNull("events")) {
                JSONObject review_object = jsonObject.getJSONObject("successData").getJSONArray("events").getJSONObject(0);
                Event_Time.setText(review_object.getString("from_time").replace("null", "") + " - " + review_object.getString("to_time").replace("null", ""));
                Event_Date.setText(review_object.getString("date").replace("null", ""));
            } else {
                Event_Time.setText("Not Available");
                Event_Date.setText("Not Available");
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

            main_scroll_view.fullScroll(View.FOCUS_UP);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestError(String response, APIActions.ApiActions apiActions) {
        Log.d("response", response);
    }
}
