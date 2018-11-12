package com.codingpixel.healingbudz.adapter;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.codingpixel.healingbudz.R;

import java.util.ArrayList;

public class KeywordDialogRecylerAdapter extends RecyclerView.Adapter<KeywordDialogRecylerAdapter.ViewHolder> {
    private LayoutInflater mInflater;
    ArrayList<String > mData = new ArrayList<>();
    private KeywordDialogRecylerAdapter.ItemClickListener mClickListener;
    public KeywordDialogRecylerAdapter(Context context , ArrayList<String > data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data ;
    }
    @Override
    public KeywordDialogRecylerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.keyword_dialog_recyler_item, parent, false);
        KeywordDialogRecylerAdapter.ViewHolder viewHolder = new KeywordDialogRecylerAdapter.ViewHolder(view);
        return viewHolder;
//
//        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M){
//            RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams) view.getLayoutParams();
//            Log.d("heaight",parent.getMeasuredHeight() / 6 +"");
//            lp.height = parent.getMeasuredHeight() / 6;
//            int height  = parent.getMeasuredHeight() / 6;
//            if(height > 50) {
//                view.setLayoutParams(lp);
//            }
//            KeywordDialogRecylerAdapter.ViewHolder viewHolder = new KeywordDialogRecylerAdapter.ViewHolder(view);
//            return viewHolder;
//        }else {
//            RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams) view.getLayoutParams();
//            Log.d("heaight",parent.getMeasuredHeight() / 6 +"");
//            int height  = parent.getMeasuredHeight() / 6;
//            if(height == 0){
//                lp.height = 128;
//            }else {
//                lp.height = height;
//            }
//            view.setLayoutParams(lp);
//            KeywordDialogRecylerAdapter.ViewHolder viewHolder = new KeywordDialogRecylerAdapter.ViewHolder(view);
//            return viewHolder;
//        }
    }
    // binds the data to the textview in each cell
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(KeywordDialogRecylerAdapter.ViewHolder holder, final int position) {
        holder.Title.setText(mData.get(position));
    }

    // total number of cells
    @Override
    public int getItemCount() {
        return this.mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnTouchListener {
        TextView Title;
        public ViewHolder(View itemView) {
            super(itemView);
            Title = itemView.findViewById(R.id.title_keywoedd);
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }

        @Override
        public boolean onTouch(View v, MotionEvent motionEvent) {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    break;
                case MotionEvent.ACTION_CANCEL:
                case MotionEvent.ACTION_OUTSIDE:
                case MotionEvent.ACTION_UP:
                    break;
                default:
                    break;
            }
            return false;
        }
    }

    // allows clicks events to be caught
    public void setClickListener(KeywordDialogRecylerAdapter.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }
    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}