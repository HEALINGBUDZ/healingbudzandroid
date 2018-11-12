package com.codingpixel.healingbudz.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.codingpixel.healingbudz.DataModel.StrainDataModel;
import com.codingpixel.healingbudz.R;
import com.codingpixel.healingbudz.Utilities.DateConverter;
import com.codingpixel.healingbudz.customeUI.CustomeToast;
import com.codingpixel.healingbudz.data_structure.APIActions;
import com.codingpixel.healingbudz.network.model.APIResponseListner;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.codingpixel.healingbudz.Utilities.ClickAbleKeywordText.MakeKeywordClickableText;
import static com.codingpixel.healingbudz.application.HealingBudApplication.getContext;
import static com.codingpixel.healingbudz.data_structure.APIActions.ApiActions.delete_my_save;

public class MyStrainsRecylerAdapter extends RecyclerView.Adapter<MyStrainsRecylerAdapter.ViewHolder> implements APIResponseListner {
    private LayoutInflater mInflater;
    Context context;
    ArrayList<StrainDataModel> mData = new ArrayList<>();
    private MyStrainsRecylerAdapter.ItemClickListener mClickListener;

    public MyStrainsRecylerAdapter(Context context, ArrayList<StrainDataModel> my_strains) {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.mData = my_strains;
    }

    // inflates the cell layout from xml when needed
    @Override
    public MyStrainsRecylerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.my_strains_recyler_veiew_item_layout, parent, false);
        Animation startAnimation = AnimationUtils.loadAnimation(context, R.anim.fade_in);
        view.startAnimation(startAnimation);
        MyStrainsRecylerAdapter.ViewHolder viewHolder = new MyStrainsRecylerAdapter.ViewHolder(view);
        return viewHolder;
    }

    // binds the data to the textview in each cell
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        try {
//            Date mDate = sdf.parse(mData.get(position).getUpdated_at());
//            long timeInMilliseconds = mDate.getTime();
//            String agoo = getTimeAgo(timeInMilliseconds);
//            holder.Hours_Aggo.setText(agoo);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
        holder.Hours_Aggo.setText(DateConverter.getPrettyTime(mData.get(position).getUpdated_at()));
//        holder.Title.setText(mData.get(position).getTitle());
        MakeKeywordClickableText(holder.Title.getContext(), mData.get(position).getTitle(), holder.Title);
        holder.Title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.Title.isClickable() && holder.Title.isEnabled()) {
                    if (mClickListener != null)
                        mClickListener.onItemClick(view, holder.getAdapterPosition());
                }
            }
        });

        holder.Delete_Strains.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClickListener.onDeleteItemCLick(view,position);
//                JSONObject jsonObject = new JSONObject();
//                try {
//                    jsonObject.put("user_strain_id", mData.get(position).getMy_strain_id());
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                new VollyAPICall(context, false, URL.delete_user_strain, jsonObject, user.getSession_key(), Request.Method.POST, MyStrainsRecylerAdapter.this, delete_my_save);
//                mData.remove(position);
//                notifyItemRemoved(position);
//                notifyDataSetChanged();
            }
        });

        holder.Edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClickListener.onEditClick(view, position);
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
        Log.d("resposne", response);
        if(apiActions==delete_my_save){
            CustomeToast.ShowCustomToast(getContext(),"Deleted Successfully!", Gravity.TOP);
        }
    }

    @Override
    public void onRequestError(String response, APIActions.ApiActions apiActions) {
        Log.d("resposne", response);
        try {
            JSONObject object = new JSONObject(response);
            CustomeToast.ShowCustomToast(getContext(), object.getString("errorMessage"), Gravity.TOP);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // stores and recycles views as they are scrolled off screen
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView Hours_Aggo, Title;
        ImageView Delete_Strains, Edit;

        public ViewHolder(View itemView) {
            super(itemView);
            Hours_Aggo = itemView.findViewById(R.id.hours_aggo);
            Title = itemView.findViewById(R.id.title_strain);
            Delete_Strains = itemView.findViewById(R.id.delete_strains);
            Edit = itemView.findViewById(R.id.eddit);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    public void setClickListener(MyStrainsRecylerAdapter.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);

        void onEditClick(View view, int position);
        void onDeleteItemCLick(View view,int position);
    }
}