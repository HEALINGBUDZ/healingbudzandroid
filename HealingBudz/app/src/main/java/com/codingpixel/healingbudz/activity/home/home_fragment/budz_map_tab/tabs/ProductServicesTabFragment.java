package com.codingpixel.healingbudz.activity.home.home_fragment.budz_map_tab.tabs;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.codingpixel.healingbudz.DataModel.BudzMapProductDataModal;
import com.codingpixel.healingbudz.DataModel.StrainModel;
import com.codingpixel.healingbudz.R;
import com.codingpixel.healingbudz.activity.splash.Splash;
import com.codingpixel.healingbudz.adapter.BudzMapProductRecylerAdapter;
import com.codingpixel.healingbudz.customeUI.customalerts.SweetAlertDialog;
import com.codingpixel.healingbudz.custome_dialog.save_discussion.BudzProductAddAlertDialog;
import com.codingpixel.healingbudz.data_structure.APIActions;
import com.codingpixel.healingbudz.network.VollyAPICall;
import com.codingpixel.healingbudz.network.model.APIResponseListner;
import com.codingpixel.healingbudz.network.model.URL;
import com.codingpixel.healingbudz.static_function.IntentFunction;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.codingpixel.healingbudz.activity.home.home_fragment.budz_map_tab.BudzMapHomeFragment.budz_map_item_clickerd_dataModel;
import static com.codingpixel.healingbudz.activity.splash.Splash.user;
import static com.codingpixel.healingbudz.data_structure.APIActions.ApiActions.get_budz_profile;
import static com.codingpixel.healingbudz.network.model.URL.delete_budz_service;
import static com.codingpixel.healingbudz.network.model.URL.delete_product;


@SuppressLint("ValidFragment")
public class ProductServicesTabFragment extends Fragment implements APIResponseListner, BudzProductAddAlertDialog.OnDialogFragmentClickListener, BudzMapProductRecylerAdapter.ItemClickListener {

    String BudzType = "";
    public static List<StrainModel> strainModelList = new ArrayList<>();

    @SuppressLint("ValidFragment")
    public ProductServicesTabFragment(String budzType) {
        BudzType = budzType;
    }

    RecyclerView product_recyler_view;
    SwipeRefreshLayout swipe_data;
    LinearLayout Refresh;
    TextView No_Record;
    LinearLayout btns;
    RelativeLayout add_product_open, add_service_open;
    BudzMapProductRecylerAdapter recyler_adapter;
    ArrayList<BudzMapProductDataModal> dataModals = new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.budz_map_product_tab_layout, container, false);
        product_recyler_view = view.findViewById(R.id.product_recyler_view);
        btns = view.findViewById(R.id.btns);
//        swipe_data = view.findViewById(R.id.swipe_data);
//        swipe_data.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                JSONObject object = new JSONObject();
//                new VollyAPICall(view.getContext(), false, URL.get_budz_profile + "/" + budz_map_item_clickerd_dataModel.getId(), object, user.getSession_key(), Request.Method.GET, ProductServicesTabFragment.this, get_budz_profile);
//            }
//        });
        add_product_open = view.findViewById(R.id.add_product_open);
        add_service_open = view.findViewById(R.id.add_service_open);
        RecyclerView.LayoutManager layoutManager_home_saerch = new LinearLayoutManager(getContext());
        product_recyler_view.setLayoutManager(layoutManager_home_saerch);
//        if(BudzType.equalsIgnoreCase("0")){
//            BudzMapProductRecylerAdapter recyler_adapter = new BudzMapProductRecylerAdapter(getContext());
//            product_recyler_view.setAdapter(recyler_adapter);
//        }else if(BudzType.equalsIgnoreCase("1")){
//            BudzMapMedicalTypeRecylerAdapter recyler_adapter = new BudzMapMedicalTypeRecylerAdapter(getContext());
//            product_recyler_view.setAdapter(recyler_adapter);
//        }else if(BudzType.equalsIgnoreCase("2")){
//            BudzMapMenuRecylerAdapter recyler_adapter = new BudzMapMenuRecylerAdapter(getContext());
//            product_recyler_view.setAdapter(recyler_adapter);
//        }else {
//            BudzMapProductRecylerAdapter recyler_adapter = new BudzMapProductRecylerAdapter(getContext());
//            product_recyler_view.setAdapter(recyler_adapter);
//        }
        if (BudzType.equalsIgnoreCase("1")) {
            recyler_adapter = new BudzMapProductRecylerAdapter(getContext(), dataModals, false);
        } else {
            recyler_adapter = new BudzMapProductRecylerAdapter(getContext(), dataModals, false);
        }
        recyler_adapter.setClickListener(this);

        product_recyler_view.setAdapter(recyler_adapter);
//        product_recyler_view.setNestedScrollingEnabled(true);
        Refresh = view.findViewById(R.id.refresh);
        No_Record = view.findViewById(R.id.no_record);
        Refresh.setVisibility(View.VISIBLE);
        JSONObject object = new JSONObject();
        add_product_open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isResume = true;
                IntentFunction.GoTo(view.getContext(), AddProductActivity.class);
//                BudzProductAddAlertDialog introHowToAddQuestionDialog = BudzProductAddAlertDialog.newInstance(ProductServicesTabFragment.this);
//                introHowToAddQuestionDialog.show(((AppCompatActivity) view.getContext()).getSupportFragmentManager(), "pd");
            }
        });
        add_service_open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isResume = true;
                IntentFunction.GoTo(view.getContext(), AddServiceActivity.class);
//                BudzProductAddAlertDialog introHowToAddQuestionDialog = BudzProductAddAlertDialog.newInstance(ProductServicesTabFragment.this);
//                introHowToAddQuestionDialog.show(((AppCompatActivity) view.getContext()).getSupportFragmentManager(), "pd");
            }
        });
        new VollyAPICall(view.getContext(), false, URL.get_budz_profile + "/" + budz_map_item_clickerd_dataModel.getId(), object, user.getSession_key(), Request.Method.GET, ProductServicesTabFragment.this, get_budz_profile);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isResume) {
            new VollyAPICall(getContext(), true, URL.get_budz_profile + "/" + budz_map_item_clickerd_dataModel.getId(), new JSONObject(), user.getSession_key(), Request.Method.GET, ProductServicesTabFragment.this, get_budz_profile);
        }
    }

    boolean isResume = false;

    @Override
    public void onRequestSuccess(String response, APIActions.ApiActions apiActions) {
        if (apiActions == APIActions.ApiActions.delete_answer) {
            isResume = true;
            this.onResume();
        } else {
            Log.d("response", response);
            Refresh.setVisibility(View.GONE);
            JSONObject jsonObject = null;
            dataModals.clear();
            strainModelList.clear();
            try {
                jsonObject = new JSONObject(response);
                if (jsonObject.has("strains")) {
                    for (int k = 0; k < jsonObject.getJSONArray("strains").length(); k++) {
                        StrainModel temp = new StrainModel();
                        temp.setId(jsonObject.getJSONArray("strains").getJSONObject(k).getInt("id"));
                        temp.setTitle(jsonObject.getJSONArray("strains").getJSONObject(k).getString("title"));
                        strainModelList.add(temp);
                    }
                }

                if (!jsonObject.getJSONObject("successData").isNull("products")) {
                    List<BudzMapProductDataModal> tempAray = new ArrayList<>();
                    if (!jsonObject.getJSONObject("successData").isNull("services")) {
                        setService(jsonObject);
                    }
                    JSONArray specials = jsonObject.getJSONObject("successData").getJSONArray("products");
                    for (int x = 0; x < specials.length(); x++) {
                        String type = "";
                        JSONObject sp = specials.getJSONObject(x);
//                    type = sp.getJSONObject("strain_type").getString("title");
                        BudzMapProductDataModal specialProducts = new BudzMapProductDataModal();
                        specialProducts.setId(sp.getInt("id"));
                        specialProducts.setSub_user_id(sp.optInt("sub_user_id"));
                        specialProducts.setSub_user_id(sp.optInt("type_id"));
                        specialProducts.setName(sp.optString("name"));
                        specialProducts.setThc(sp.optDouble("thc"));
                        specialProducts.setCbd(sp.optDouble("cbd"));
                        specialProducts.setCreated_at(sp.optString("created_at"));
                        specialProducts.setUpdated_at(sp.optString("updated_at"));
                        specialProducts.setStrain_id(sp.optInt("strain_id"));
                        specialProducts.setProduct(true);
                        //BudzType.equalsIgnoreCase("3")
                        if (true) {
                            if (!sp.isNull("strain_type") && !sp.isNull("category")) {//&& !BudzType.equalsIgnoreCase("1")
                                specialProducts.setStrian_type_title(sp.getJSONObject("category").getString("title"));
                                specialProducts.setIdType(sp.getJSONObject("category").getInt("id"));
                                specialProducts.setAlsoStrain(true);
                                specialProducts.setAlsoStrain(sp.getJSONObject("strain_type").getString("title"));
                                specialProducts.setStrainModel(new BudzMapProductDataModal.GenaricModel(sp.getJSONObject("strain_type").getInt("id"), sp.getJSONObject("strain_type").getString("title")));
                                specialProducts.setCategoryModel(new BudzMapProductDataModal.GenaricModel(sp.getJSONObject("category").getInt("id"), sp.getJSONObject("category").getString("title")));
                            } else if (!sp.isNull("category")) {//&& !BudzType.equalsIgnoreCase("1")
                                specialProducts.setStrian_type_title(sp.getJSONObject("category").getString("title"));
                                specialProducts.setIdType(sp.getJSONObject("category").getInt("id"));
                                specialProducts.setAlsoStrain(false);
                                specialProducts.setStrainModel(null);
                                specialProducts.setCategoryModel(new BudzMapProductDataModal.GenaricModel(sp.getJSONObject("category").getInt("id"), sp.getJSONObject("category").getString("title")));
                            } else if (!sp.isNull("strain_type")) {//&& BudzType.equalsIgnoreCase("1")
                                specialProducts.setStrian_type_title("Others");
                                specialProducts.setIdType(9999);
                                specialProducts.setAlsoStrain(true);
                                specialProducts.setStrainModel(new BudzMapProductDataModal.GenaricModel(sp.getJSONObject("strain_type").getInt("id"), sp.getJSONObject("strain_type").getString("title")));
                                specialProducts.setCategoryModel(null);
                            } else {
                                specialProducts.setAlsoStrain(false);
                                specialProducts.setStrian_type_title("Others");
                                specialProducts.setIdType(9999);
                                specialProducts.setStrainModel(null);
                                specialProducts.setCategoryModel(null);
                            }
                        } else {
                            if (!sp.isNull("strain_type") && !sp.isNull("category")) {//&& !BudzType.equalsIgnoreCase("1")
                                specialProducts.setStrian_type_title(sp.getJSONObject("category").getString("title"));
                                specialProducts.setIdType(sp.getJSONObject("category").getInt("id"));
                                specialProducts.setAlsoStrain(true);
                                specialProducts.setAlsoStrain(sp.getJSONObject("strain_type").getString("title"));
                            } else if (!sp.isNull("category")) {//&& !BudzType.equalsIgnoreCase("1")
                                specialProducts.setAlsoStrain(false);
                                specialProducts.setStrian_type_title(sp.getJSONObject("category").getString("title"));
                                specialProducts.setIdType(sp.getJSONObject("category").getInt("id"));
                            } else if (!sp.isNull("strain_type")) {//&& BudzType.equalsIgnoreCase("1")
                                specialProducts.setAlsoStrain(true);
                                specialProducts.setAlsoStrain(sp.getJSONObject("strain_type").getString("title"));
                                specialProducts.setStrian_type_title(sp.getJSONObject("strain_type").getString("title"));
                                specialProducts.setIdType(sp.getJSONObject("strain_type").getInt("id"));
                            } else {
                                specialProducts.setAlsoStrain(false);
                                specialProducts.setStrian_type_title("Others");
                                specialProducts.setIdType(9999);

                            }
                        }


                        ArrayList<String> images = new ArrayList<>();
                        JSONArray image_array = sp.getJSONArray("images");
                        for (int y = 0; y < image_array.length(); y++) {
                            JSONObject im_obj = image_array.getJSONObject(y);
                            images.add(im_obj.getString("image"));
                        }
                        specialProducts.setImage(images);
                        ArrayList<BudzMapProductDataModal.Priceing> priceings = new ArrayList<>();
                        JSONArray pricing_array = sp.getJSONArray("pricing");
                        for (int z = 0; z < pricing_array.length(); z++) {
                            JSONObject im_obj = pricing_array.getJSONObject(z);
                            BudzMapProductDataModal.Priceing priceing = new BudzMapProductDataModal.Priceing();
                            priceing.setPrice(im_obj.getString("price"));
                            priceing.setWeight(im_obj.getString("weight"));
                            priceings.add(priceing);
                        }
                        specialProducts.setPriceing(priceings);
                        tempAray.add(specialProducts);
//                    dataModals.add(specialProducts);
                    }
                    if (tempAray.size() > 0) {
                        List<String> headers = new ArrayList<>();
                        for (BudzMapProductDataModal item : tempAray) {
                            if (!headers.contains(item.getStrian_type_title())) {
                                headers.add(item.getStrian_type_title());
                            }
                        }
                        boolean vall = false;
                        if (headers.contains("Others"))
                            vall = headers.remove("Others");
                        Collections.sort(headers, new Comparator<String>() {
                            @Override
                            public int compare(String comment, String t1) {
                                return comment.trim().compareToIgnoreCase(t1.trim());
                            }
                        });
                        if (vall) {
                            headers.add("Others");
                        }
                        int count = 12;
                        for (int j = 0; j < headers.size(); j++) {
                            for (BudzMapProductDataModal item : tempAray) {
                                if (count == 12) {
                                    if (headers.get(j).equalsIgnoreCase(item.getStrian_type_title())) {
                                        item.setTitle(true);
                                        dataModals.add(item);
                                        count++;
                                    }

                                } else {
                                    if (headers.get(j).equalsIgnoreCase(item.getStrian_type_title())) {
                                        item.setTitle(false);
                                        dataModals.add(item);
                                    }
                                }


                            }
                            count = 12;
                        }

                    }

                } else if (!jsonObject.getJSONObject("successData").isNull("services")) {
                    if (!jsonObject.getJSONObject("successData").isNull("services")) {
                        setService(jsonObject);
                    }
                    add_product_open.setVisibility(View.GONE);
                } else {
                    No_Record.setVisibility(View.VISIBLE);
                    if (budz_map_item_clickerd_dataModel.getUser_id() == Splash.user.getUser_id()) {
                        add_product_open.setVisibility(View.VISIBLE);
                    } else {
                        add_product_open.setVisibility(View.GONE);
                    }
                }
                if (dataModals.size() > 0) {

                    recyler_adapter.notifyDataSetChanged();
                    add_product_open.setVisibility(View.GONE);
                } else {
                    No_Record.setVisibility(View.VISIBLE);
                    if (budz_map_item_clickerd_dataModel.getUser_id() == Splash.user.getUser_id()) {
                        add_product_open.setVisibility(View.VISIBLE);
                    } else {
                        add_product_open.setVisibility(View.GONE);
                    }
                }
                if (budz_map_item_clickerd_dataModel != null)
                    if (budz_map_item_clickerd_dataModel.getUser_id() == Splash.user.getUser_id()) {
                        add_product_open.setVisibility(View.VISIBLE);
                        btns.setVisibility(View.VISIBLE);
                        if (BudzType.equalsIgnoreCase("2") || BudzType.equalsIgnoreCase("6") || BudzType.equalsIgnoreCase("7")) {
                            add_service_open.setVisibility(View.VISIBLE);
                        } else {
                            add_service_open.setVisibility(View.GONE);
                        }
                    }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void setService(JSONObject jsonObject) throws JSONException {
        JSONArray specials = jsonObject.getJSONObject("successData").getJSONArray("services");
        for (int x = 0; x < specials.length(); x++) {
            JSONObject sp = specials.getJSONObject(x);
            BudzMapProductDataModal specialProducts = new BudzMapProductDataModal();
            specialProducts.setId(sp.getInt("id"));
            specialProducts.setSub_user_id(sp.getInt("sub_user_id"));
            specialProducts.setName(sp.getString("name"));
            specialProducts.setCreated_at(sp.getString("created_at"));
            specialProducts.setUpdated_at(sp.getString("updated_at"));
            specialProducts.setCharges(sp.getString("charges"));
            specialProducts.setImage_path(sp.getString("image"));
            specialProducts.setIdType(-1);
            specialProducts.setProduct(false);
            if (x == 0) {
                specialProducts.setTitle(true);
            } else {
                specialProducts.setTitle(false);
            }
            dataModals.add(specialProducts);
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
    public void onCrossClicked(BudzProductAddAlertDialog dialog) {

    }

    @Override
    public void onItemClick(View view, int position) {

    }

    @Override
    public void onServiceDelete(final View view, final int position) {
        new SweetAlertDialog(view.getContext(), SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Are you sure?")
                .setContentText("You want to delete this service?")
                .setConfirmText("Yes, delete it!")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismissWithAnimation();
                        JSONObject jsonObject = new JSONObject();
                        new VollyAPICall(view.getContext(), true, delete_budz_service + dataModals.get(position).getId(), jsonObject, user.getSession_key(), Request.Method.GET, ProductServicesTabFragment.this, APIActions.ApiActions.delete_answer);
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
    public void onServiceEdit(View view, int position) {
        isResume = true;
        IntentFunction.GoToWithBundle(this.getContext(), AddServiceActivity.class, dataModals.get(position));
    }

    @Override
    public void onProduceEdit(View view, int position) {
        isResume = true;
        IntentFunction.GoToWithBundle(this.getContext(), AddProductActivity.class, dataModals.get(position));
    }

    @Override
    public void onProduceDelete(final View view, final int position) {
        new SweetAlertDialog(view.getContext(), SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Are you sure?")
                .setContentText("You want to delete this product?")
                .setConfirmText("Yes, delete it!")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismissWithAnimation();
                        JSONObject jsonObject = new JSONObject();
                        new VollyAPICall(view.getContext(), true, delete_product + dataModals.get(position).getId(), jsonObject, user.getSession_key(), Request.Method.GET, ProductServicesTabFragment.this, APIActions.ApiActions.delete_answer);
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
}
