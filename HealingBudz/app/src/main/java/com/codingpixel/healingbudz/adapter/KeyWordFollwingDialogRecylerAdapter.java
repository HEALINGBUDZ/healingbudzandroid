package com.codingpixel.healingbudz.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.codingpixel.healingbudz.R;
import com.codingpixel.healingbudz.activity.home.side_menu.myrewardz.folllowkeys.KeywordModel;
import com.codingpixel.healingbudz.customeUI.CustomeToast;
import com.codingpixel.healingbudz.data_structure.APIActions;
import com.codingpixel.healingbudz.network.VollyAPICall;
import com.codingpixel.healingbudz.network.model.APIResponseListner;
import com.codingpixel.healingbudz.network.model.URL;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.codingpixel.healingbudz.activity.splash.Splash.user;
import static com.codingpixel.healingbudz.application.HealingBudApplication.getContext;

public class KeyWordFollwingDialogRecylerAdapter extends RecyclerView.Adapter<KeyWordFollwingDialogRecylerAdapter.ViewHolder> implements APIResponseListner {
    private LayoutInflater mInflater;
    Context context;
    List<KeywordModel> mData = new ArrayList<>();
    private KeyWordFollwingDialogRecylerAdapter.ItemClickListener mClickListener;

    public KeyWordFollwingDialogRecylerAdapter(Context context, List<KeywordModel> mData) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = mData;
        this.context = context;
    }

    // inflates the cell layout from xml when needed
    @Override
    public KeyWordFollwingDialogRecylerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.followe_journal_dialog_recyler_view_item_keyword, parent, false);
        RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams) view.getLayoutParams();
        KeyWordFollwingDialogRecylerAdapter.ViewHolder viewHolder = new KeyWordFollwingDialogRecylerAdapter.ViewHolder(view);
        return viewHolder;
    }

    // binds the data to the textview in each cell
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.Name.setText(mData.get(position).getTitle());

        holder.Follow_text.setText("follow");
        holder.Follow_text.setTextColor(Color.parseColor("#464645"));
        holder.F_btn.setBackgroundResource(R.drawable.add_journal_entry_btn);
        holder.Follow_icon.setImageResource(R.drawable.ic_profile_add);
        if (mData.get(position).getIs_following_count() == 1) {
            holder.Follow_text.setText("unfollow");
            holder.Follow_text.setTextColor(Color.parseColor("#464645"));
            holder.F_btn.setBackgroundResource(R.drawable.add_journal_entry_btn);
            holder.Follow_icon.setVisibility(View.GONE);
        } else {
            holder.Follow_icon.setVisibility(View.VISIBLE);
            holder.Follow_text.setText("follow");
            holder.Follow_text.setTextColor(Color.parseColor("#464645"));
            holder.F_btn.setBackgroundResource(R.drawable.add_journal_entry_btn);
            holder.Follow_icon.setImageResource(R.drawable.ic_profile_add);
        }
        holder.Follow_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mData.get(position).getIs_following_count() == 0) {
                    try {
                        new VollyAPICall(view.getContext(), true, URL.follow_keyword, new JSONObject().put("keyword", mData.get(position).getTitle()), user.getSession_key(), Request.Method.POST, new APIResponseListner() {
                            @Override
                            public void onRequestSuccess(String response, APIActions.ApiActions apiActions) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    mData.get(position).setId(jsonObject.getJSONObject("successData").optInt("id"));
                                    CustomeToast.ShowCustomToast(context, jsonObject.getString("successMessage"), Gravity.TOP);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                mData.get(position).setIs_following_count(1);

                                notifyItemChanged(position);
                                notifyDataSetChanged();
                            }

                            @Override
                            public void onRequestError(String response, APIActions.ApiActions apiActions) {
                                try {
                                    JSONObject object = new JSONObject(response);
                                    CustomeToast.ShowCustomToast(getContext(), object.getString("errorMessage"), Gravity.TOP);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, APIActions.ApiActions.get_key);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    new VollyAPICall(view.getContext(), true, URL.remove_tag + "/" + mData.get(position).getId(), new JSONObject(), user.getSession_key(), Request.Method.GET, new APIResponseListner() {
                        @Override
                        public void onRequestSuccess(String response, APIActions.ApiActions apiActions) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                CustomeToast.ShowCustomToast(context, jsonObject.getString("successMessage"), Gravity.TOP);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            mData.get(position).setIs_following_count(0);
                            notifyItemChanged(position);
                            notifyDataSetChanged();
                        }

                        @Override
                        public void onRequestError(String response, APIActions.ApiActions apiActions) {
                            try {
                                JSONObject object = new JSONObject(response);
                                CustomeToast.ShowCustomToast(getContext(), object.getString("errorMessage"), Gravity.TOP);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, APIActions.ApiActions.get_remove_tag);
                }

            }
        });
    }

    // total number of cells
    @Override
    public int getItemCount() {

        return mData.size();
    }

    @Override
    public void onRequestSuccess(String response, APIActions.ApiActions apiActions) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            CustomeToast.ShowCustomToast(context, jsonObject.getString("successMessage"), Gravity.TOP);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("respisne", response);
    }

    @Override
    public void onRequestError(String response, APIActions.ApiActions apiActions) {
        Log.d("respisne", response);
        try {
            JSONObject object = new JSONObject(response);
            CustomeToast.ShowCustomToast(getContext(), object.getString("errorMessage"), Gravity.TOP);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // stores and recycles views as they are scrolled off screen
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView Name, Follow_text;
        LinearLayout Follow_btn;
        LinearLayout F_btn;
        ImageView Follow_icon;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            Name = itemView.findViewById(R.id.name);
            Follow_text = itemView.findViewById(R.id.follow_text);
            Follow_btn = itemView.findViewById(R.id.follow_btn);
            F_btn = itemView.findViewById(R.id.f_btn);
            Follow_icon = itemView.findViewById(R.id.followe_icon);
        }


        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    public void setClickListener(KeyWordFollwingDialogRecylerAdapter.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}