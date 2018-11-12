package com.codingpixel.healingbudz.adapter;

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
import android.widget.TextView;

import com.android.volley.Request;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.codingpixel.healingbudz.DataModel.MySavedDataModal;
import com.codingpixel.healingbudz.R;
import com.codingpixel.healingbudz.customeUI.customalerts.SweetAlertDialog;
import com.codingpixel.healingbudz.data_structure.APIActions;
import com.codingpixel.healingbudz.network.VollyAPICall;
import com.codingpixel.healingbudz.network.model.APIResponseListner;
import com.codingpixel.healingbudz.network.model.URL;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.codingpixel.healingbudz.Utilities.ClickAbleKeywordText.MakeKeywordClickableText;
import static com.codingpixel.healingbudz.activity.splash.Splash.user;
import static com.codingpixel.healingbudz.data_structure.APIActions.ApiActions.delete_my_save;
import static com.codingpixel.healingbudz.network.model.URL.images_baseurl;

public class MySavesListRecylerAdapter extends RecyclerView.Adapter<MySavesListRecylerAdapter.ViewHolder> implements APIResponseListner {
    private LayoutInflater mInflater;
    ArrayList<MySavedDataModal> mData = new ArrayList<>();
    private MySavesListRecylerAdapter.ItemClickListener mClickListener;
    Context context;

    public MySavesListRecylerAdapter(Context context, ArrayList<MySavedDataModal> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.context = context;
    }

    // inflates the cell layout from xml when needed
    @Override
    public MySavesListRecylerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.my_saves_recyler_item_layout, parent, false);
        MySavesListRecylerAdapter.ViewHolder viewHolder = new MySavesListRecylerAdapter.ViewHolder(view);
        return viewHolder;

    }

    // binds the data to the textview in each cell
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(final MySavesListRecylerAdapter.ViewHolder holder, final int position) {
        final MySavedDataModal data = mData.get(position);
//        holder.Title.setText(data.getTitle());
        if (data.getType_id() != 10) {
            if (data.getType_id() == 2 || data.getType_id() == 13) {
                if (data.getType_id() == 13) {
                    MakeKeywordClickableText(holder.Title.getContext(), "Saved Business Chat with " + data.getNameUser(), holder.Title);
                } else {
                    MakeKeywordClickableText(holder.Title.getContext(), "Saved Chat with " + data.getNameUser(), holder.Title);
                }

                holder.Title.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (holder.Title.isClickable() && holder.Title.isEnabled()) {
                            if (mClickListener != null)
                                mClickListener.onItemClick(view, holder.getAdapterPosition());
                        }
                    }
                });
            } else {
                MakeKeywordClickableText(holder.Title.getContext(), data.getTitle(), holder.Title);
                holder.Title.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (holder.Title.isClickable() && holder.Title.isEnabled()) {
                            if (mClickListener != null)
                                mClickListener.onItemClick(view, holder.getAdapterPosition());
                        }
                    }
                });
            }

        } else {
            try {
                JSONObject object = new JSONObject(data.getDescription());
                MakeKeywordClickableText(holder.Title.getContext(), object.getString("search_title"), holder.Title);
                holder.Title.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (holder.Title.isClickable() && holder.Title.isEnabled()) {
                            if (mClickListener != null)
                                mClickListener.onItemClick(view, holder.getAdapterPosition());
                        }
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        SetIconAndColor(data.getType_id(), holder.icon, holder.Title, holder.userIcon, data);
        holder.Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (position < mData.size()) {
                    new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Are you sure?")
                            .setContentText("You want to delete this save?")
                            .setConfirmText("Yes, delete it!")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismissWithAnimation();
                                    JSONObject jsonObject = new JSONObject();
                                    new VollyAPICall(context
                                            , false
                                            , URL.delete_my_save + "/" + data.getId()
                                            , jsonObject
                                            , user.getSession_key()
                                            , Request.Method.GET, MySavesListRecylerAdapter.this, delete_my_save);
                                    mData.remove(position);
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
//                    JSONObject jsonObject = new JSONObject();
//                    new VollyAPICall(context, false, URL.delete_my_save + "/" + data.getId(), jsonObject, user.getSession_key(), Request.Method.GET, MySavesListRecylerAdapter.this, delete_my_save);
//                    mData.remove(position);
//                    notifyItemRemoved(position);
//                    notifyDataSetChanged();
                }
            }
        });
    }

    // total number of cells
    @Override
    public int getItemCount() {
        return this.mData.size();
    }

    @Override
    public void onRequestSuccess(String response, APIActions.ApiActions apiActions) {
        Log.d("response", response);
    }

    @Override
    public void onRequestError(String response, APIActions.ApiActions apiActions) {
        Log.d("response", response);
    }


    // stores and recycles views as they are scrolled off screen
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView icon, userIcon;
        TextView text_icon;
        TextView Title;
        ImageView Delete;

        public ViewHolder(View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.icon_img);
            userIcon = itemView.findViewById(R.id.icon_img_cir);
            text_icon = itemView.findViewById(R.id.text_icon);
            Title = itemView.findViewById(R.id.title_main);
            Delete = itemView.findViewById(R.id.delete_my_save);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }

    }

    // allows clicks events to be caught
    public void setClickListener(MySavesListRecylerAdapter.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    private void SetIconAndColor(int type, ImageView icon, TextView textView, final ImageView iconUser, MySavedDataModal data) {
        iconUser.setVisibility(View.GONE);
        icon.setVisibility(View.VISIBLE);
        String icon_path = "";
        switch (type) {
            case 3:
                icon.setImageResource(R.drawable.ic_tab_journal);
                icon.setColorFilter(0);
                textView.setTextColor(Color.parseColor("#689f3d"));
                break;
            case 2:
                icon.setImageResource(R.drawable.ic_message_green);
                icon.setVisibility(View.GONE);
                iconUser.setVisibility(View.VISIBLE);
                if (data.getImageUser().length() > 8) {
                    if (data.getImageUser().contains("facebook.com")
                            || data.getImageUser().contains("https")
                            || data.getImageUser().contains("https")
                            || data.getImageUser().contains("google.com")
                            || data.getImageUser().contains("googleusercontent.com"))
                        icon_path = data.getImageUser();
                    else {
                        icon_path = images_baseurl + data.getImageUser();
                    }
                } else {
                    icon_path = images_baseurl + data.getAvatarUser();
                }
                Glide.with(context)
                        .load(icon_path)
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .placeholder(R.drawable.ic_user_profile).error(R.drawable.noimage)
                        .centerCrop()
//                        .listener(new RequestListener<String, GlideDrawable>() {
//                            @Override
//                            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
//                                return false;
//                            }
//
//                            @Override
//                            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
//                                Log.d("ready", model);
//                                iconUser.setImageDrawable(resource);
//                                return false;
//                            }
//                        })
                        .into(iconUser);
                icon.setColorFilter(0);
                textView.setTextColor(Color.parseColor("#7ABB4B"));
                break;
            case 4:
                icon.setImageResource(R.drawable.ic_tab_qa);
                icon.setColorFilter(0);
                textView.setTextColor(Color.parseColor("#0b6596"));
                break;
            case 7:
                icon.setImageResource(R.drawable.ic_tab_strain);
                icon.setColorFilter(0);
                textView.setTextColor(Color.parseColor("#f5c52f"));
                break;
            case 8:
                icon.setImageResource(R.drawable.budzmapnewic);
                icon.setColorFilter(0);
                textView.setTextColor(Color.parseColor("#942a89"));
                break;
            case 6:
                icon.setImageResource(R.drawable.ic_tab_group);
                icon.setColorFilter(0);
                textView.setTextColor(Color.parseColor("#a06423"));
                break;
            case 10:
                icon.setImageResource(R.drawable.ic_save_icon_filter_dialog);
                icon.setColorFilter(0);
                textView.setTextColor(Color.parseColor("#7cc244"));
                break;
            case 11:
                icon.setImageResource(R.drawable.ic_save_clcik);
                icon.setColorFilter(0);
                textView.setTextColor(Color.parseColor("#942b88"));
                break;
            case 13:
                icon.setVisibility(View.GONE);
                iconUser.setVisibility(View.VISIBLE);
                icon.setImageResource(R.drawable.budzmapnewic);
                if (data.getImageUser().length() > 8) {
                    if (data.getImageUser().contains("facebook.com")
                            || data.getImageUser().contains("https")
                            || data.getImageUser().contains("https")
                            || data.getImageUser().contains("google.com")
                            || data.getImageUser().contains("googleusercontent.com"))
                        icon_path = data.getImageUser();
                    else {
                        icon_path = images_baseurl + data.getImageUser();
                    }
                } else {
                    icon_path = images_baseurl + data.getAvatarUser();
                }
                Glide.with(context)
                        .load(icon_path)
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .placeholder(R.drawable.ic_user_profile).error(R.drawable.noimage)
                        .centerCrop()
//                        .listener(new RequestListener<String, GlideDrawable>() {
//                            @Override
//                            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
//                                return false;
//                            }
//
//                            @Override
//                            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
//                                Log.d("ready", model);
//                                iconUser.setImageDrawable(resource);
//                                return false;
//                            }
//                        })
                        .into(iconUser);
                textView.setTextColor(Color.parseColor("#942b88"));
                break;
        }

    }
}