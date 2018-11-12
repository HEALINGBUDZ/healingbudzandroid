package com.codingpixel.healingbudz.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.codingpixel.healingbudz.DataModel.BudzMapSpecialProducts;
import com.codingpixel.healingbudz.R;
import com.codingpixel.healingbudz.Utilities.DateConverter;
import com.codingpixel.healingbudz.activity.shoot_out.send_shoot_out_dialog.SendSpecialAlertDialog;
import com.codingpixel.healingbudz.activity.splash.Splash;
import com.codingpixel.healingbudz.customeUI.customalerts.SweetAlertDialog;
import com.codingpixel.healingbudz.data_structure.APIActions;
import com.codingpixel.healingbudz.network.VollyAPICall;
import com.codingpixel.healingbudz.network.model.APIResponseListner;
import com.codingpixel.healingbudz.network.model.URL;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.codingpixel.healingbudz.activity.home.home_fragment.budz_map_tab.BudzMapHomeFragment.budz_map_item_clickerd_dataModel;
import static com.codingpixel.healingbudz.activity.splash.Splash.user;
import static com.codingpixel.healingbudz.network.model.URL.sharedBaseUrl;
import static com.codingpixel.healingbudz.static_function.ShareIntent.ShareHBContent;


public class BudzMapSpecialProductRecylerAdapter extends RecyclerView.Adapter<BudzMapSpecialProductRecylerAdapter.ViewHolder> implements SendSpecialAlertDialog.OnDialogFragmentClickListener {
    private LayoutInflater mInflater;
    ArrayList<BudzMapSpecialProducts> mData = new ArrayList<>();
    Context context;

    private BudzMapSpecialProductRecylerAdapter.ItemClickListener mClickListener;


    public BudzMapSpecialProductRecylerAdapter(Context context, ArrayList<BudzMapSpecialProducts> data) {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.mData = data;
    }

    // inflates the cell layout from xml when needed
    @Override
    public BudzMapSpecialProductRecylerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.budz_map_special_product_recyler_item, parent, false);
        BudzMapSpecialProductRecylerAdapter.ViewHolder viewHolder = new BudzMapSpecialProductRecylerAdapter.ViewHolder(view);
        return viewHolder;

    }

    // binds the data to the textview in each cell
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(final BudzMapSpecialProductRecylerAdapter.ViewHolder holder, final int position) {
//        if(position ==3 || position == 5){
//            holder.Save.setImageResource(R.drawable.ic_save_clcik);
//        }
//        holder.Save.setVisibility(View.GONE);
//        holder.Save.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                holder.Save.setImageResource(R.drawable.ic_save_clcik);
//
//            }
//        });
        if (budz_map_item_clickerd_dataModel.getUser_id() == Splash.user.getUser_id()) {
            holder.edition.setVisibility(View.VISIBLE);
        } else {
            holder.edition.setVisibility(View.GONE);
        }
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendSpecialAlertDialog sendShootOutAlertDialog = SendSpecialAlertDialog.newInstance(BudzMapSpecialProductRecylerAdapter.this, mData.get(position), position);
                sendShootOutAlertDialog.show(((AppCompatActivity) v.getContext()).getSupportFragmentManager(), "dialog");

            }
        });
        holder.Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SweetAlertDialog(v.getContext(), SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Are you sure?")
                        .setContentText("You want to delete this special?")
                        .setConfirmText("Yes, delete it!")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismissWithAnimation();
                                JSONObject object1 = new JSONObject();
                                //                        object1.put("shout_out_id", mData.get(position).getId());
//                        object1.put("business_id", budz_map_item_clickerd_dataModel.getId());
//                        object1.put("business_type_id", budz_map_item_clickerd_dataModel.getBusiness_type_id());
                                new VollyAPICall(context
                                        , false
                                        , URL.delete_special + "/" + mData.get(position).getId()
                                        , object1
                                        , user.getSession_key()
                                        , Request.Method.GET
                                        , new APIResponseListner() {
                                    @Override
                                    public void onRequestSuccess(String response, APIActions.ApiActions apiActions) {

                                    }

                                    @Override
                                    public void onRequestError(String response, APIActions.ApiActions apiActions) {

                                    }
                                }, APIActions.ApiActions.saved_shout_out);
                                mData.remove(position);//.setSaved(true);
                                notifyDataSetChanged();
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
        });
        if (mData.get(position).isSaved()) {
//            holder.Save.setImageResource(R.drawable.ic_save_clcik);
//            holder.Save.setOnClickListener(null);
        } else {
//            holder.Save.setImageResource(R.drawable.ic_save_icon);

        }
        holder.share_speacial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject object1 = new JSONObject();
                try {
//                    object1.put("msg", "http://139.162.37.73/healingbudz/budz-map");
                    object1.put("id", mData.get(position).getId());
                    object1.put("type", "Budz Special");
                    object1.put("content", budz_map_item_clickerd_dataModel.getTitle());
                    object1.put("url", sharedBaseUrl + "/get-budz?business_id=" + budz_map_item_clickerd_dataModel.getId() + "&business_type_id=" + budz_map_item_clickerd_dataModel.getBusiness_type_id());
                    object1.put("BudzCome","");
                    object1.put("msg", "Check out Healing Budz for your smartphone: " + URL.sharedBaseUrl + "/get-budz?business_id=" + budz_map_item_clickerd_dataModel.getId() + "&business_type_id=" + budz_map_item_clickerd_dataModel.getBusiness_type_id());//"/get-budz-product/" + mData.get(position).getId() + "/" + budz_map_item_clickerd_dataModel.getId());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                ShareHBContent((Activity) v.getContext(), object1);
            }
        });
        holder.title_main.setText(mData.get(position).getTitle());
        holder.detail.setText(mData.get(position).getMessage());
        if (DateConverter.convertDateForShooutOut(mData.get(position).getValidity_date()).contains("Expired")) {
            holder.Date_Titless.setText("Expired");
            holder.Date_Titless.setTextColor(Color.parseColor("#ed1c24"));
            holder.date_expire.setVisibility(View.GONE);
        } else if (DateConverter.convertDateForShooutOut(mData.get(position).getValidity_date()).contains("Expire Soon!")) {
            holder.Date_Titless.setText("Expire Soon!");
            holder.Date_Titless.setTextColor(Color.parseColor("#ed1c24"));
            holder.date_expire.setText(DateConverter.convertSpecial(mData.get(position).getValidity_date()));
            holder.date_expire.setVisibility(View.VISIBLE);
        } else {
            holder.Date_Titless.setText("Valid Untill:");
            holder.Date_Titless.setTextColor(Color.parseColor("#FFFFFF"));
            holder.date_expire.setText(DateConverter.convertSpecial(mData.get(position).getValidity_date()));
            holder.date_expire.setVisibility(View.VISIBLE);
        }

    }

    // total number of cells
    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public void onSendShootOutButtonClick(SendSpecialAlertDialog dialog) {
        dialog.dismiss();
    }

    @Override
    public void onSendShootOutButtonClick(SendSpecialAlertDialog dialog, BudzMapSpecialProducts item, int pos) {
        mData.set(pos, item);
        notifyItemChanged(pos);
//        notifyDataSetChanged();
    }


    // stores and recycles views as they are scrolled off screen
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView Save, edit, share_speacial;
        TextView title_main;
        TextView detail, Date_Titless;
        TextView date_expire;
        LinearLayout edition;


        public ViewHolder(View itemView) {
            super(itemView);
            Save = itemView.findViewById(R.id.save);
            share_speacial = itemView.findViewById(R.id.share_speacial);
            title_main = itemView.findViewById(R.id.title_main);
            edit = itemView.findViewById(R.id.edit);
            edition = itemView.findViewById(R.id.edition);
//            title_main.setAutoLinkMask(0);


            detail = itemView.findViewById(R.id.detail);
//            detail.setAutoLinkMask(0);
            Date_Titless = itemView.findViewById(R.id.date_titless);
            date_expire = itemView.findViewById(R.id.date_expire);
//            Date_Titless.setAutoLinkMask(0);
//            date_expire.setAutoLinkMask(0);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }

    }

    // allows clicks events to be caught
    public void setClickListener(BudzMapSpecialProductRecylerAdapter.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}