package com.codingpixel.healingbudz.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.codingpixel.healingbudz.DataModel.MessagesDataModel;
import com.codingpixel.healingbudz.R;
import com.codingpixel.healingbudz.Utilities.DateConverter;
import com.codingpixel.healingbudz.Utilities.Utility;
import com.codingpixel.healingbudz.activity.home.side_menu.messages.MessagesActivity;
import com.codingpixel.healingbudz.activity.splash.Splash;
import com.codingpixel.healingbudz.customeUI.customalerts.SweetAlertDialog;
import com.codingpixel.healingbudz.data_structure.APIActions;
import com.codingpixel.healingbudz.network.VollyAPICall;
import com.codingpixel.healingbudz.network.model.APIResponseListner;
import com.codingpixel.healingbudz.network.model.URL;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.codingpixel.healingbudz.activity.splash.Splash.user;
import static com.codingpixel.healingbudz.data_structure.APIActions.ApiActions.delete_Chat;
import static com.codingpixel.healingbudz.network.model.URL.delete_chat;
import static com.codingpixel.healingbudz.network.model.URL.images_baseurl;

public class MessagesRecylerAdapter extends RecyclerView.Adapter<MessagesRecylerAdapter.ViewHolder> {
    private LayoutInflater mInflater;
    Context context;
    List<MessagesDataModel> mData = new ArrayList<>();
    private List<MessagesDataModel> filterData = new ArrayList<>();
    private boolean isDeleteAble;
    private final ViewBinderHelper binderHelper = new ViewBinderHelper();
    private MessagesRecylerAdapter.ItemClickListener mClickListener;

    public MessagesRecylerAdapter(Context context, List<MessagesDataModel> mData, boolean isDelete) {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.filterData = mData;
        this.mData.clear();
        this.mData.addAll(filterData);
        this.isDeleteAble = isDelete;
    }

    public void itemInserted() {

    }

    public void clearData() {
        filterData.clear();
        mData.clear();
        notifyDataSetChanged();
    }

    public void addData(MessagesDataModel dataModel) {
        filterData.add(dataModel);
        mData.add(dataModel);
        notifyDataSetChanged();
    }

    public MessagesDataModel getItem(int position) {
        return filterData.get(position);
    }

    public void deleteItem(int position) {
        mData.remove(filterData.get(position));
        filterData.remove(position);
        notifyItemRemoved(position);
        notifyDataSetChanged();
    }

    // inflates the cell layout from xml when needed
    @NonNull
    @Override
    public MessagesRecylerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.messages_recyler_veiew_item_layout, parent, false);
        view.setOnTouchListener(null);
        return new ViewHolder(view);
    }

    // binds the data to the textview in each cell
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        String icon_path = "";
        String Name = "";
        int color = 0;
        final MessagesDataModel itemData = filterData.get(position);
        if (filterData != null && 0 <= position && position < filterData.size()) {
            final String data = String.valueOf(position);

            // Use ViewBindHelper to restore and save the open/close state of the SwipeRevealView
            // put an unique string id as value, can be any string which uniquely define the data

            binderHelper.bind(holder.swipe_layout, data);
            binderHelper.setOpenOnlyOne(true);

            // Bind your data here

        }

        if (!itemData.isBudz()) {
            holder.online_indicator.setVisibility(View.VISIBLE);
//            String pathApi = URL.save_chat;
            if (Splash.user.getUser_id() == itemData.getReceiver_id()) {
                if (itemData.getIs_online_count() > 0) {
                    holder.online_indicator.setBackgroundResource(R.drawable.messages_online_notification_bg);
                } else {
                    holder.online_indicator.setBackgroundResource(R.drawable.messages_offline_notification_bg);
                }
            } else {
                if (itemData.getIs_online_count_rec() > 0) {
                    holder.online_indicator.setBackgroundResource(R.drawable.messages_online_notification_bg);
                } else {
                    holder.online_indicator.setBackgroundResource(R.drawable.messages_offline_notification_bg);
                }
            }
            if (itemData.isMain()) {
                holder.save_button.setVisibility(View.VISIBLE);
                if (getItem(position).getSave_count() == 0) {
                    holder.save_button.setText("SAVE");
                } else {
                    holder.save_button.setText("UN-SAVE");
                }
//                pathApi = URL.save_chat;
            } else {
                holder.save_button.setVisibility(View.VISIBLE);
//                pathApi = URL.save_chat_budz;
                if (itemData.isBudzPeople()) {
                    if (getItem(position).getSave_count() == 0) {
                        holder.save_button.setText("SAVE");
                    } else {
                        holder.save_button.setText("UN-SAVE");
                    }
                } else {
                    holder.save_button.setVisibility(View.GONE);
                }

            }
            holder.save_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {


                    new SweetAlertDialog(v.getContext(), SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Are you sure?")
                            .setContentText(getItem(position).getSave_count() > 0 ? "You want to un-save this chat?" : "You want to save this chat?")
                            .setConfirmText("Yes!")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismissWithAnimation();
                                    String pathAPI = URL.save_chat;
                                    if (itemData.isMain()) {
                                        pathAPI = URL.save_chat;
                                    } else {
                                        pathAPI = URL.save_chat_budz;
                                    }
                                    JSONObject object = new JSONObject();
                                    try {
                                        object.put("chat_id", getItem(position).getId());
                                        if (Splash.user.getUser_id() == getItem(position).getReceiver_id()) {
                                            object.put("other_id", getItem(position).getSender_id());
                                        } else {
                                            object.put("other_id", getItem(position).getReceiver_id());
                                        }
                                        if (getItem(position).getSave_count() == 0)
                                            object.put("save", 1);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    filterData.get(position).setSave_count(getItem(position).getSave_count() > 0 ? 0 : 1);
                                    new VollyAPICall(v.getContext(), true, pathAPI, object, user.getSession_key(), Request.Method.POST, new APIResponseListner() {
                                        @Override
                                        public void onRequestSuccess(String response, APIActions.ApiActions apiActions) {
                                            binderHelper.closeLayout(String.valueOf(position));
                                        }

                                        @Override
                                        public void onRequestError(String response, APIActions.ApiActions apiActions) {
                                            binderHelper.closeLayout(String.valueOf(position));
                                        }
                                    }, delete_Chat);
//                            messages_data.remove(recyler_adapter.getItem(position));

                                    notifyDataSetChanged();
                                }
                            })
                            .setCancelText("No!")
                            .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismissWithAnimation();
                                    binderHelper.closeLayout(String.valueOf(position));
                                }
                            })
                            .show();
                }
            });
            holder.Name.setTextColor(Color.parseColor("#FFFFFF"));
            holder.Name.setSingleLine(false);
            if (user.getUser_id() == filterData.get(position).getReceiver_id()) {
                holder.Time_Text.setVisibility(View.VISIBLE);
                if (filterData.get(position).getSender_image_path().length() > 8) {
                    if (filterData.get(position).getSender_image_path().contains("facebook.com") || filterData.get(position).getSender_image_path().contains("https") || filterData.get(position).getSender_image_path().contains("https") || filterData.get(position).getSender_image_path().contains("google.com") || filterData.get(position).getSender_image_path().contains("googleusercontent.com"))
                        icon_path = filterData.get(position).getSender_image_path();
                    else {
                        icon_path = images_baseurl + filterData.get(position).getSender_image_path();
                    }
                } else {
                    icon_path = images_baseurl + filterData.get(position).getSender_avatar();
                }
                Name = filterData.get(position).getSender_first_name();
                color = filterData.get(position).getSender_point();
            } else if (user.getUser_id() == filterData.get(position).getSender_id()) {
                holder.Time_Text.setVisibility(View.VISIBLE);
                if (filterData.get(position).getReceiver_image_path().length() > 8) {
                    if (filterData.get(position).getReceiver_image_path().contains("facebook.com") || filterData.get(position).getReceiver_image_path().contains("https") || filterData.get(position).getReceiver_image_path().contains("http") || filterData.get(position).getReceiver_image_path().contains("googleusercontent.com"))
                        icon_path = filterData.get(position).getReceiver_image_path();
                    else {
                        icon_path = images_baseurl + filterData.get(position).getReceiver_image_path();
                    }
                } else {
                    icon_path = images_baseurl + filterData.get(position).getReceiver_avatar();
                }
                Name = filterData.get(position).getReceiver_first_name();
                color = filterData.get(position).getRecev_point();
            } else {
                holder.Time_Text.setVisibility(View.INVISIBLE);
                if (filterData.get(position).getReceiver_image_path().length() > 8) {
                    if (filterData.get(position).getReceiver_image_path().contains("facebook.com") || filterData.get(position).getReceiver_image_path().contains("http") || filterData.get(position).getReceiver_image_path().contains("https") || filterData.get(position).getReceiver_image_path().contains("google.com") || filterData.get(position).getReceiver_image_path().contains("googleusercontent.com"))
                        icon_path = filterData.get(position).getReceiver_image_path();
                    else {
                        icon_path = images_baseurl + filterData.get(position).getReceiver_image_path();
                    }
                } else {
                    icon_path = images_baseurl + filterData.get(position).getReceiver_avatar();
                }
                Name = filterData.get(position).getReceiver_first_name();
                color = filterData.get(position).getRecev_point();
            }

            if (user.getUser_id() == filterData.get(position).getReceiver_id()) {
                if (filterData.get(position).getSpecial_icon_sen().length() > 6) {
                    holder.profile_img_topi.setVisibility(View.VISIBLE);

                    Glide.with(context)
                            .load(images_baseurl + filterData.get(position).getSpecial_icon_sen())
                            .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                            .placeholder(R.drawable.topi_ic).error(R.drawable.noimage)
                            .listener(new RequestListener<String, GlideDrawable>() {
                                @Override
                                public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                    Log.d("ready", model);
                                    holder.profile_img_topi.setImageDrawable(resource);
                                    return false;
                                }
                            })
                            .into(holder.profile_img_topi);
                } else {
                    holder.profile_img_topi.setVisibility(View.GONE);
                }
            } else {
                if (filterData.get(position).getSpecial_icon_rec().length() > 6) {
                    holder.profile_img_topi.setVisibility(View.VISIBLE);

                    Glide.with(context)
                            .load(images_baseurl + filterData.get(position).getSpecial_icon_rec())
                            .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                            .placeholder(R.drawable.topi_ic).error(R.drawable.noimage)
                            .listener(new RequestListener<String, GlideDrawable>() {
                                @Override
                                public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                    Log.d("ready", model);
                                    holder.profile_img_topi.setImageDrawable(resource);
                                    return false;
                                }
                            })
                            .into(holder.profile_img_topi);
                } else {
                    holder.profile_img_topi.setVisibility(View.GONE);
                }
            }
            Glide.with(context)
                    .load(icon_path)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .placeholder(R.drawable.ic_user_profile).error(R.drawable.noimage)
                    .centerCrop()
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            Log.d("ready", model);
                            holder.User_Icon.setImageDrawable(resource);
                            return false;
                        }
                    })
                    .into(holder.User_Icon);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                Date mDate = sdf.parse(filterData.get(position).getCreated_at());
                long timeInMilliseconds = mDate.getTime();
//            String agoo = getTimeAgo(timeInMilliseconds);
                holder.Time_Text.setText(DateConverter.getPrettyTime(filterData.get(position).getUpdated_at()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            holder.Name.setText(Name);
            holder.Name.setSingleLine(false);
//            holder.Name.setTextColor(Color.parseColor(Utility.getBudColor(color)));
            if (filterData.get(position).getMessages_count() > 0) {
                holder.Notification.setVisibility(View.VISIBLE);
                holder.Notification.setText(filterData.get(position).getMessages_count() + "");
            } else {
                holder.Notification.setVisibility(View.GONE);
            }

            holder.Delete_Btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.swipe_layout.close(true);
                    holder.swipe_layout.setMinFlingVelocity(10);
                    mClickListener.onDeleteClick(v, position);
                }
            });

            if (isDeleteAble) {
                holder.swipe_layout.setLockDrag(false);
            } else {
                holder.swipe_layout.setLockDrag(true);
            }
            holder.user_name_type.setVisibility(View.GONE);
            holder.buz_image.setVisibility(View.GONE);

            holder.notification_budz.setVisibility(View.GONE);
        } else {
            holder.online_indicator.setVisibility(View.GONE);
            holder.profile_img_topi.setVisibility(View.GONE);
            switch (filterData.get(position).getBudzType()) {
                case 1:
                    holder.user_name_type.setText("Dispensary");
                    holder.buz_image.setImageResource(R.drawable.ic_dispancry_icon);
                    break;
                case 2:
                case 6:
                case 7:
                    holder.user_name_type.setText("Medical");
                    holder.buz_image.setImageResource(R.drawable.ic_medical_icon);
                    break;
                case 3:
                    holder.user_name_type.setText("Cannabites");
                    holder.buz_image.setImageResource(R.drawable.ic_cannabites_icon);
                    break;
                case 4:
                case 8:
                    holder.user_name_type.setText("Entertainment");
                    holder.buz_image.setImageResource(R.drawable.ic_entertainment_icon);
                    break;
                case 5:
                    holder.user_name_type.setText("Events");
                    holder.buz_image.setImageResource(R.drawable.ic_events_icon);
                    break;
            }
            holder.buz_image.setVisibility(View.VISIBLE);
            if (filterData.get(position).getMessages_count() > 0) {
                holder.notification_budz.setVisibility(View.VISIBLE);
                holder.Time_Text.setText(filterData.get(position).getMessages_count() + " Unread");
            } else {
                holder.notification_budz.setVisibility(View.GONE);
                if (filterData.get(position).getMember_count() < 2) {
                    holder.Time_Text.setText(filterData.get(position).getMember_count() + " Chat");
                } else {
                    holder.Time_Text.setText(filterData.get(position).getMember_count() + " Chats");

                }
            }
            holder.User_Icon.setImageResource(R.drawable.ic_budz_adn);
            holder.User_Icon.setScaleType(ImageView.ScaleType.CENTER_CROP);
            holder.Name.setText(filterData.get(position).getBudzName());
            holder.Name.setSingleLine(true);
            holder.Name.setTextColor(Color.parseColor("#931E8E"));
            holder.user_name_type.setText(filterData.get(position).getBudzBusinessName());
            holder.user_name_type.setVisibility(View.VISIBLE);
            holder.Notification.setVisibility(View.GONE);

            holder.Delete_Btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.swipe_layout.close(false);
                    holder.swipe_layout.setMinFlingVelocity(10);
                    mClickListener.onDeleteClick(v, position);
                }
            });

            if (isDeleteAble) {
                holder.swipe_layout.setLockDrag(false);
            } else {
                holder.swipe_layout.setLockDrag(true);
            }
        }
    }

    // total number of cells
    @Override
    public int getItemCount() {
        return filterData.size();
    }

    // stores and recycles views as they are scrolled off screen
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView User_Icon, buz_image, profile_img_topi;
        TextView Notification, Name, notification_budz, online_indicator;
        TextView Time_Text, user_name_type;
        Button Delete_Btn, save_button;
        LinearLayout Main_Layout;
        SwipeRevealLayout swipe_layout;

        public ViewHolder(View itemView) {
            super(itemView);
            User_Icon = itemView.findViewById(R.id.user_image);
            profile_img_topi = itemView.findViewById(R.id.profile_img_topi);
            buz_image = itemView.findViewById(R.id.buz_image);
            notification_budz = itemView.findViewById(R.id.notification_budz);
            online_indicator = itemView.findViewById(R.id.online_indicator);
            user_name_type = itemView.findViewById(R.id.user_name_type);
            Name = itemView.findViewById(R.id.user_name);
            Notification = itemView.findViewById(R.id.notification);
            Main_Layout = itemView.findViewById(R.id.main_layout);
            Time_Text = itemView.findViewById(R.id.time_text);
            Delete_Btn = itemView.findViewById(R.id.delete_button);
            save_button = itemView.findViewById(R.id.save_button);
            swipe_layout = itemView.findViewById(R.id.swipe_layout);
            Main_Layout.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    public void setClickListener(MessagesRecylerAdapter.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);

        void onDeleteClick(View view, int position);
    }


    //Filter Search
    public void filter(final String text) {

        // Searching could be complex..so we will dispatch it to a different thread...
        new Thread(new Runnable() {
            @Override
            public void run() {

                // Clear the filter list
                filterData.clear();

                // If there is no search value, then add all original list items to filter list
                if (TextUtils.isEmpty(text)) {

                    filterData.addAll(mData);

                } else {
                    // Iterate in the original List and add it to filter list...
                    for (MessagesDataModel item : mData) {
                        if (item.getReceiver_first_name().toLowerCase().contains(text.toLowerCase())
                                || item.getSender_first_name().toLowerCase().contains(text.toLowerCase())
                                ) {
                            // Adding Matched items
                            filterData.add(item);
                        }
                    }
                }

                // Set on UI Thread
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // Notify the List that the DataSet has changed...
                        notifyDataSetChanged();
                    }
                });

            }
        }).start();

    }
}