package com.codingpixel.healingbudz.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.codingpixel.healingbudz.DataModel.BudzMapHomeDataModel;
import com.codingpixel.healingbudz.R;
import com.codingpixel.healingbudz.customeUI.customalerts.SweetAlertDialog;
import com.codingpixel.healingbudz.data_structure.APIActions;
import com.codingpixel.healingbudz.network.VollyAPICall;
import com.codingpixel.healingbudz.network.model.APIResponseListner;
import com.codingpixel.healingbudz.network.model.URL;

import org.json.JSONObject;

import java.util.ArrayList;

import static com.codingpixel.healingbudz.activity.splash.Splash.user;
import static com.codingpixel.healingbudz.data_structure.APIActions.ApiActions.delete_my_save;
import static com.codingpixel.healingbudz.network.model.URL.images_baseurl;


public class BudzMapHomeSettingRecylAdapter extends RecyclerView.Adapter<BudzMapHomeSettingRecylAdapter.ViewHolder> implements APIResponseListner {
    private LayoutInflater mInflater;
    ArrayList<BudzMapHomeDataModel> mData = new ArrayList<>();
    private BudzMapHomeSettingRecylAdapter.ItemClickListener mClickListener;
    Context context;

    public BudzMapHomeSettingRecylAdapter(Context context, ArrayList<BudzMapHomeDataModel> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.context = context;
    }

    // inflates the cell layout from xml when needed
    @Override
    public BudzMapHomeSettingRecylAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.budz_map_recyler_view_item_setting_layout, parent, false);
        BudzMapHomeSettingRecylAdapter.ViewHolder viewHolder = new BudzMapHomeSettingRecylAdapter.ViewHolder(view);
        return viewHolder;

    }

    // binds the data to the textview in each cell
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(final BudzMapHomeSettingRecylAdapter.ViewHolder holder, final int position) {
        final BudzMapHomeDataModel data = mData.get(position);
        holder.business_setting.setVisibility(View.VISIBLE);
        holder.plane_type_name.setText(data.getSubScriptionName());
        holder.btn_cancel.setVisibility(View.VISIBLE);
        if (data.isCanceled()) {
            holder.btn_cancel.setVisibility(View.VISIBLE);
            holder.btn_cancel.setText("Cancelled");
            if (data.getEndTime().length() > 0) {
                holder.btn_cancel.setText(data.getEndTime());
            }
        } else {
            holder.btn_cancel.setText("CANCEL");
            holder.btn_cancel.setVisibility(View.VISIBLE);
        }

        holder.btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.btn_cancel.getText().toString().trim().equalsIgnoreCase("CANCEL")) {
                    new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Are you sure?")
                            .setContentText("You want to cancel this membership?")
                            .setConfirmText("Yes, cancel it!")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismissWithAnimation();
                                    JSONObject jsonObject = new JSONObject();
                                    new VollyAPICall(context
                                            , false
                                            , URL.delete_subscription + "/" + data.getId()
                                            , jsonObject
                                            , user.getSession_key()
                                            , Request.Method.GET, BudzMapHomeSettingRecylAdapter.this, delete_my_save);
                                    mData.get(position).setEndTime("");
                                    mData.get(position).setCanceled(true);
                                    notifyItemRemoved(position);
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
            }
        });
        if (data.getIs_featured() == 1) {
//            holder.main_layout.setBackgroundColor(Color.parseColor("#000000"));
            holder.Featured_business_layout.setVisibility(View.VISIBLE);
//            holder.rating_imgs.setImageResource(R.drawable.ic_rating_img);
        } else {
//            holder.main_layout.setBackgroundColor(Color.parseColor("#232323"));
            holder.Featured_business_layout.setVisibility(View.GONE);
//            holder.rating_imgs.setImageResource(R.drawable.ic_rating_img_2);
        }

        if (mData.get(position).getIs_featured() == 0) {
            holder.doted_line.setImageDrawable(null);
            holder.doted_line.setBackgroundColor(Color.parseColor("#3c3c3c"));
        } else {
            holder.doted_line.setImageResource(R.drawable.doted_line);
            holder.doted_line.setBackgroundColor(Color.parseColor("#00000000"));
        }

        if (data.getIs_organic() == 1) {
            holder.Organic.setVisibility(View.VISIBLE);
        } else {
            holder.Organic.setVisibility(View.GONE);
        }

        if (data.getIs_delivery() == 1) {
            holder.Delivery.setVisibility(View.VISIBLE);
        } else {
            holder.Delivery.setVisibility(View.GONE);
        }


        Glide.with(context)
                .load(images_baseurl + mData.get(position).getLogo())
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .placeholder(R.drawable.ic_budz_adn)
//                .listener(new RequestListener<String, GlideDrawable>() {
//                    @Override
//                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
//                        return false;
//                    }
//
//                    @Override
//                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
//                        Log.d("ready", model);
//                        holder.Main_Icon.setImageDrawable(resource);
//                        return false;
//                    }
//                })
                .into(holder.Main_Icon);
        switch (data.getBusiness_type_id()) {
            case 1:
                holder.type.setText("Dispensary");
                holder.BudCategory_small_icon.setImageResource(R.drawable.ic_dispancry_icon);
                break;
            case 2:
            case 6:
            case 7:
                holder.type.setText("Medical");
                holder.BudCategory_small_icon.setImageResource(R.drawable.ic_medical_icon);
                break;
            case 3:
                holder.type.setText("Cannabites");
                holder.BudCategory_small_icon.setImageResource(R.drawable.ic_cannabites_icon);
                break;
            case 4:
            case 8:
                holder.type.setText("Entertainment");
                holder.BudCategory_small_icon.setImageResource(R.drawable.ic_entertainment_icon);
                break;
            case 5:
                holder.type.setText("Events");
                holder.BudCategory_small_icon.setImageResource(R.drawable.ic_events_icon);
                break;
            case 9:
                holder.type.setText("Other");
                holder.BudCategory_small_icon.setImageResource(R.drawable.other_item);
                break;
        }


        holder.Heading_Title.setText(data.getTitle());
        @SuppressLint("DefaultLocale") String Distance = String.format("%.01f", Double.parseDouble(mData.get(position).getDistance() + ""));
        holder.distance.setText(Distance + " mi");
        if (data.getReviews() != null) {
            holder.reviewss.setText(data.getReviews().size() + " Reviews");
        }

        if (data.getRating_sum() > 0) {
            if (data.getRating_sum() == 1) {
                holder.rating_img.setImageResource(R.drawable.rating_one);
            } else if (data.getRating_sum() == 2) {
                holder.rating_img.setImageResource(R.drawable.rating_two);
            } else if (data.getRating_sum() == 3) {
                holder.rating_img.setImageResource(R.drawable.rating_three);
            } else if (data.getRating_sum() == 4) {
                holder.rating_img.setImageResource(R.drawable.rating_four);
            } else if (data.getRating_sum() == 5) {
                holder.rating_img.setImageResource(R.drawable.rating_five);
            }
        } else {
            holder.rating_img.setImageResource(R.drawable.rating_zero);
        }

    }

    // total number of cells
    @Override
    public int getItemCount() {
        return this.mData.size();
    }

    @Override
    public void onRequestSuccess(String response, APIActions.ApiActions apiActions) {
        Log.d("Tagsjj", response);
        if (mClickListener != null) mClickListener.refreschCall();
    }

    @Override
    public void onRequestError(String response, APIActions.ApiActions apiActions) {
        Log.d("Tagsjj", response);
    }


    // stores and recycles views as they are scrolled off screen
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        RelativeLayout main_layout;
        ImageView doted_line, Main_Icon;
        ImageView rating_imgs;
        LinearLayout Featured_business_layout, business_setting;
        ImageView Organic, Delivery;
        TextView btn_cancel;
        ImageView BudCategory_small_icon, rating_img;
        TextView type, Heading_Title, distance, reviewss, plane_type_name;

        public ViewHolder(View itemView) {
            super(itemView);
            main_layout = itemView.findViewById(R.id.main_layout);
            btn_cancel = itemView.findViewById(R.id.btn_cancel);
            doted_line = itemView.findViewById(R.id.top_line);
            plane_type_name = itemView.findViewById(R.id.plane_type_name);
            business_setting = itemView.findViewById(R.id.business_setting);
//            rating_imgs = itemView.findViewById(R.id.rating_img);
            Featured_business_layout = itemView.findViewById(R.id.featured_business_layout);
            Organic = itemView.findViewById(R.id.organic_img);
            Delivery = itemView.findViewById(R.id.delivery_img);
            BudCategory_small_icon = itemView.findViewById(R.id.bud_map_catagory_img);
            type = itemView.findViewById(R.id.type);
            Main_Icon = itemView.findViewById(R.id.icon_img);
            Heading_Title = itemView.findViewById(R.id.heading_title);
            distance = itemView.findViewById(R.id.distance);
            reviewss = itemView.findViewById(R.id.reviewss);
            rating_img = itemView.findViewById(R.id.rating_img);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }

    }

    // allows clicks events to be caught
    public void setClickListener(BudzMapHomeSettingRecylAdapter.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);

        void refreschCall();
    }
}