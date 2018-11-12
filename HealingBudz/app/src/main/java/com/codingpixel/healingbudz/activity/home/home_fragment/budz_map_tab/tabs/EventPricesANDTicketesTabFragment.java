package com.codingpixel.healingbudz.activity.home.home_fragment.budz_map_tab.tabs;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.codingpixel.healingbudz.DataModel.BudzMapTicketsDataModal;
import com.codingpixel.healingbudz.DataModel.PaymentMethod;
import com.codingpixel.healingbudz.R;
import com.codingpixel.healingbudz.activity.splash.Splash;
import com.codingpixel.healingbudz.adapter.AddNewPaymentMethodRecylerAdapter;
import com.codingpixel.healingbudz.adapter.BudzMapEventTypeRecylerAdapter;
import com.codingpixel.healingbudz.customeUI.customalerts.SweetAlertDialog;
import com.codingpixel.healingbudz.data_structure.APIActions;
import com.codingpixel.healingbudz.network.VollyAPICall;
import com.codingpixel.healingbudz.network.model.APIResponseListner;
import com.codingpixel.healingbudz.network.model.URL;
import com.codingpixel.healingbudz.static_function.IntentFunction;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.codingpixel.healingbudz.activity.home.home_fragment.budz_map_tab.BudzMapHomeFragment.budz_map_item_clickerd_dataModel;
import static com.codingpixel.healingbudz.activity.splash.Splash.user;
import static com.codingpixel.healingbudz.data_structure.APIActions.ApiActions.get_budz_profile;
import static com.codingpixel.healingbudz.data_structure.APIActions.ApiActions.get_languages;
import static com.codingpixel.healingbudz.data_structure.APIActions.ApiActions.save_budz_ticket_click;


@SuppressLint("ValidFragment")
public class EventPricesANDTicketesTabFragment extends Fragment implements APIResponseListner, AddNewPaymentMethodRecylerAdapter.ItemClickListener, BudzMapEventTypeRecylerAdapter.ItemClickListener {

    String BudzType = "";
    LinearLayout Refresh;
    TextView No_Record, not_found;
    ArrayList<BudzMapTicketsDataModal> dataModals = new ArrayList<>();
    AddNewPaymentMethodRecylerAdapter addNewPaymentMethodRecylerAdapter;
    List<PaymentMethod> paymentMethodList = new ArrayList<>();
    RecyclerView payment_method_recycler_view;

    @SuppressLint("ValidFragment")
    public EventPricesANDTicketesTabFragment(String budzType) {
        BudzType = budzType;
    }

    RecyclerView event_recyler_adapter;
    BudzMapEventTypeRecylerAdapter recyler_adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.budz_map_event_prices_tickets_tab_layout, container, false);
        event_recyler_adapter = view.findViewById(R.id.event_recyler_adapter);
        RecyclerView.LayoutManager layoutManager_home_saerch = new LinearLayoutManager(getContext());
        event_recyler_adapter.setLayoutManager(layoutManager_home_saerch);
        recyler_adapter = new BudzMapEventTypeRecylerAdapter(getContext(), dataModals);
        recyler_adapter.setClickListener(this);
        event_recyler_adapter.setAdapter(recyler_adapter);
        event_recyler_adapter.setNestedScrollingEnabled(false);
        Refresh = view.findViewById(R.id.refresh);
        No_Record = view.findViewById(R.id.no_record);
        not_found = view.findViewById(R.id.not_found);
        Refresh.setVisibility(View.VISIBLE);
        JSONObject object = new JSONObject();
        new VollyAPICall(view.getContext(), false, URL.get_budz_profile + "/" + budz_map_item_clickerd_dataModel.getId(), object, user.getSession_key(), Request.Method.GET, EventPricesANDTicketesTabFragment.this, get_budz_profile);
        Button btn = view.findViewById(R.id.link_open);
        payment_method_recycler_view = view.findViewById(R.id.payment_method_selection);
        addNewPaymentMethodRecylerAdapter = new AddNewPaymentMethodRecylerAdapter(view.getContext(), paymentMethodList, false);
        addNewPaymentMethodRecylerAdapter.setClickListener(this);
        FlexboxLayoutManager layoutManagerPayment = new FlexboxLayoutManager(getContext());
        layoutManagerPayment.setFlexDirection(FlexDirection.ROW);
        layoutManagerPayment.setJustifyContent(JustifyContent.FLEX_START);
        payment_method_recycler_view.setLayoutManager(layoutManagerPayment);
        payment_method_recycler_view.setAdapter(addNewPaymentMethodRecylerAdapter);
        new VollyAPICall(view.getContext(), false, URL.get_languages, object, user.getSession_key(), Request.Method.GET, EventPricesANDTicketesTabFragment.this, get_languages);
        if (budz_map_item_clickerd_dataModel.getUser_id() == Splash.user.getUser_id()) {
            btn.setVisibility(View.VISIBLE);
        } else {
            btn.setVisibility(View.GONE);
        }
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONObject object = new JSONObject();
                try {
                    object.put("sub_user_id", budz_map_item_clickerd_dataModel.getId());
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
                isResume = true;
                IntentFunction.GoTo(view.getContext(), AddTicketActivity.class);
//                LinksUtils.GotoWebLink("http://139.162.37.73/healingbudz/", getActivity());

            }
        });
        return view;
    }

    boolean isResume = false;

    @Override
    public void onResume() {
        super.onResume();
        if (isResume) {
            JSONObject object = new JSONObject();
            new VollyAPICall(getContext(), true, URL.get_budz_profile + "/" + budz_map_item_clickerd_dataModel.getId(), object, user.getSession_key(), Request.Method.GET, EventPricesANDTicketesTabFragment.this, get_budz_profile);
        }
    }

    @Override
    public void onRequestSuccess(String response, APIActions.ApiActions apiActions) {
        Log.d("response", response);
        if (apiActions == get_languages) {
//            try {
//                JSONObject jsonObject = new JSONObject(response);
//                JSONArray jsonArray = jsonObject.getJSONObject("successData").getJSONArray("languages");
//                paymentMethodList.clear();
//                paymentMethodList.addAll(Arrays.asList(new Gson().fromJson(jsonObject.getJSONObject("successData").getJSONArray("payment_methods").toString(), PaymentMethod[].class)));
//
//                addNewPaymentMethodRecylerAdapter.notifyDataSetChanged();
//
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
        } else if (apiActions == APIActions.ApiActions.delete_answer) {
            isResume = true;
            this.onResume();
        } else {
            Refresh.setVisibility(View.GONE);
            JSONObject jsonObject = null;
            dataModals.clear();
            try {
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
                    not_found.setVisibility(View.GONE);
                    payment_method_recycler_view.setVisibility(View.VISIBLE);
                } else {
                    payment_method_recycler_view.setVisibility(View.GONE);
                    not_found.setVisibility(View.VISIBLE);
                }
                if (!jsonObject.getJSONObject("successData").isNull("tickets")) {
                    JSONArray specials = jsonObject.getJSONObject("successData").getJSONArray("tickets");
                    for (int x = 0; x < specials.length(); x++) {
                        JSONObject sp = specials.getJSONObject(x);
                        BudzMapTicketsDataModal ticketsDataModal = new BudzMapTicketsDataModal();
                        ticketsDataModal.setId(sp.getInt("id"));
                        ticketsDataModal.setSub_user_id(sp.getInt("sub_user_id"));
                        ticketsDataModal.setTitle(sp.getString("title"));
                        ticketsDataModal.setPrice(sp.getString("price"));
                        ticketsDataModal.setImage(sp.getString("image"));
                        ticketsDataModal.setCreated_at(sp.getString("created_at"));
                        ticketsDataModal.setUpdated_at(sp.getString("updated_at"));
                        ticketsDataModal.setLinkee(sp.getString("purchase_ticket_url"));
                        dataModals.add(ticketsDataModal);
                    }
                    if (dataModals.size() > 0) {
                        recyler_adapter.notifyDataSetChanged();
                    } else {
                        No_Record.setVisibility(View.VISIBLE);
                    }

                } else {
                    No_Record.setVisibility(View.VISIBLE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onRequestError(String response, APIActions.ApiActions apiActions) {
        Log.d("response", response);
        if (apiActions == APIActions.ApiActions.delete_answer) {
            isResume = true;
            this.onResume();
        }
    }

    @Override
    public void onPicSelctionItemClicked(View view, int position) {

    }

    @Override
    public void onItemClick(View view, int position) {

    }

    @Override
    public void onItemDelete(final View view, final int position) {
        new SweetAlertDialog(view.getContext(), SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Are you sure?")
                .setContentText("You want to delete this ticket?")
                .setConfirmText("Yes, delete it!")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismissWithAnimation();
                        JSONObject jsonObject = new JSONObject();
                        new VollyAPICall(view.getContext(), true, URL.delete_budz_ticket + dataModals.get(position).getId(), jsonObject, user.getSession_key(), Request.Method.GET, EventPricesANDTicketesTabFragment.this, APIActions.ApiActions.delete_answer);
                        dataModals.remove(position);
                        recyler_adapter.notifyDataSetChanged();
                    }
                })
                .setCancelText("Close!")
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismissWithAnimation();
                    }
                })
                .show();
    }

    @Override
    public void onItemEdit(View view, int position) {
        isResume = true;
        IntentFunction.GoToWithBundle(this.getContext(), AddTicketActivity.class, dataModals.get(position));
    }
}
