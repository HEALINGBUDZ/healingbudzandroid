package com.codingpixel.healingbudz.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.codingpixel.healingbudz.DataModel.BuzzFeedDataModel;
import com.codingpixel.healingbudz.DataModel.HomeQAfragmentDataModel;
import com.codingpixel.healingbudz.R;
import com.codingpixel.healingbudz.Utilities.DateConverter;

import java.util.ArrayList;

import static com.codingpixel.healingbudz.Utilities.ClickAbleKeywordText.MakeKeywordClickableText;

//import static com.codingpixel.healingbudz.Utilities.TimesAgo.getTimeAgo;

public class BuzzFeedRecylerAdapter extends RecyclerView.Adapter<BuzzFeedRecylerAdapter.ViewHolder> {
    private LayoutInflater mInflater;
    ArrayList<BuzzFeedDataModel> mData = new ArrayList<>();
    private QAHomeFragmentRecylerAdapter.ItemClickListener mClickListener;

    public BuzzFeedRecylerAdapter(Context context, ArrayList<BuzzFeedDataModel> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    // inflates the cell layout from xml when needed
    @Override
    public BuzzFeedRecylerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.buzz_feed_recyler_item_layout, parent, false);
        BuzzFeedRecylerAdapter.ViewHolder viewHolder = new BuzzFeedRecylerAdapter.ViewHolder(view);
        return viewHolder;
    }

    // binds the data to the textview in each cell
    @Override
    public void onBindViewHolder(final BuzzFeedRecylerAdapter.ViewHolder holder, final int position) {

//        holder.Title.setText(mData.get(position).getNotification_text());
        MakeKeywordClickableText(holder.Title.getContext(), mData.get(position).getNotification_text(), holder.Title);
        holder.Title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.Title.isClickable() && holder.Title.isEnabled()) {
                    if (mClickListener != null)
                        mClickListener.onItemClick(view, holder.getAdapterPosition());
                }
            }
        });

        if (mData.get(position).getDescription().length() < 4 || mData.get(position).getType().equalsIgnoreCase("ShoutOut")) {
            holder.Heading.setVisibility(View.GONE);
        } else {
            holder.Heading.setVisibility(View.VISIBLE);
//            holder.Heading.setText(mData.get(position).getDescription());
            MakeKeywordClickableText(holder.Heading.getContext(), mData.get(position).getDescription(), holder.Heading);
            holder.Heading.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (holder.Heading.isClickable() && holder.Heading.isEnabled()) {
                        if (mClickListener != null)
                            mClickListener.onItemClick(view, holder.getAdapterPosition());
                    }
                }
            });
        }
        holder.Time.setText(DateConverter.getPrettyTime(mData.get(position).getCreated_at()));
        SetIcon(holder.icon, holder.text_icon, mData.get(position).getType(), holder, position);
    }

    // total number of cells
    @Override
    public int getItemCount() {
        return this.mData.size();
    }

    // stores and recycles views as they are scrolled off screen
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnTouchListener {

        ImageView icon;
        TextView text_icon;
        TextView Title, Heading, Time;

        public ViewHolder(View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.icon_img);
            text_icon = itemView.findViewById(R.id.text_icon);
            Heading = itemView.findViewById(R.id.heading_title);
            Title = itemView.findViewById(R.id.title_main);
            Time = itemView.findViewById(R.id.time);
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

    public void NotifyDataChange(ArrayList<HomeQAfragmentDataModel> data) {
        notifyDataSetChanged();
    }

    // allows clicks events to be caught
    public void setClickListener(QAHomeFragmentRecylerAdapter.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }


    public void clear() {
        notifyDataSetChanged();
    }

    public void addAll() {
        notifyDataSetChanged();
    }

    public void SetIcon(ImageView imageView, TextView textView, String type, ViewHolder holder, int pos) {

        Log.d("type=== > ", type + " \n");
        holder.Heading.setTextColor(Color.parseColor("#e4e4e5"));
        holder.Title.setTextColor(Color.parseColor("#7bc044"));
        holder.Time.setTextColor(Color.parseColor("#545454"));
        holder.Time.setVisibility(View.VISIBLE);
        if (type != null) {
            switch (type) {
                case "BudzChat":
                case "Budz":
                    holder.Heading.setTextColor(Color.parseColor("#942B88"));
                    holder.Title.setTextColor(Color.parseColor("#942B88"));
                    holder.Time.setTextColor(Color.parseColor("#942B88"));
                    imageView.setVisibility(View.VISIBLE);
                    textView.setVisibility(View.GONE);
                    imageView.setColorFilter(0);
                    imageView.setImageResource(R.drawable.budzmapnewic);
                    break;
                case "JournalEvent":
                    holder.Heading.setTextColor(Color.parseColor("#7bc044"));
                    holder.Title.setTextColor(Color.parseColor("#7bc044"));
                    holder.Time.setTextColor(Color.parseColor("#7bc044"));
                    imageView.setVisibility(View.VISIBLE);
                    textView.setVisibility(View.GONE);
                    imageView.setColorFilter(0);
                    imageView.setImageResource(R.drawable.ic_tab_journal);
                    break;
                case "Questions":
                    holder.Heading.setTextColor(Color.parseColor("#0E6DC1"));
                    holder.Title.setTextColor(Color.parseColor("#0E6DC1"));
                    holder.Time.setTextColor(Color.parseColor("#0E6DC1"));
                    imageView.setImageResource(R.drawable.ic_question_icon);
                    imageView.setVisibility(View.VISIBLE);
                    textView.setVisibility(View.GONE);
                    textView.setText("Q:");
                    imageView.setColorFilter(0);
                    break;
                case "Answers":
                    holder.Heading.setTextColor(Color.parseColor("#0E6DC1"));
                    holder.Title.setTextColor(Color.parseColor("#0E6DC1"));
                    holder.Time.setTextColor(Color.parseColor("#0E6DC1"));
                    imageView.setImageResource(R.drawable.ic_answer_icon);
                    imageView.setVisibility(View.VISIBLE);
                    textView.setVisibility(View.GONE);
                    textView.setText("A:");
                    imageView.setColorFilter(0);
                    break;
                case "UserStrain":
                    holder.Heading.setTextColor(Color.parseColor("#F4C42E"));
                    holder.Title.setTextColor(Color.parseColor("#F4C42E"));
                    holder.Time.setTextColor(Color.parseColor("#F4C42E"));
                    imageView.setVisibility(View.VISIBLE);
                    textView.setVisibility(View.GONE);
                    imageView.setColorFilter(0);
                    imageView.setImageResource(R.drawable.ic_tab_strain);
                    break;
                case "Groups":
                    holder.Heading.setTextColor(Color.parseColor("#e4e4e5"));
                    holder.Title.setTextColor(Color.parseColor("#7bc044"));
                    holder.Time.setTextColor(Color.parseColor("#545454"));
                    imageView.setVisibility(View.VISIBLE);
                    textView.setVisibility(View.GONE);
                    imageView.setColorFilter(0);
                    imageView.setImageResource(R.drawable.ic_group_green);
                    break;
                case "JournalLikeDislike":
                    holder.Heading.setTextColor(Color.parseColor("#e4e4e5"));
                    holder.Title.setTextColor(Color.parseColor("#7bc044"));
                    holder.Time.setTextColor(Color.parseColor("#545454"));
                    imageView.setVisibility(View.VISIBLE);
                    textView.setVisibility(View.GONE);
                    imageView.setColorFilter(0);
                    imageView.setImageResource(R.drawable.ic_like_green_icon);
                    break;
                case "Journal":
                    holder.Heading.setTextColor(Color.parseColor("#e4e4e5"));
                    holder.Title.setTextColor(Color.parseColor("#7bc044"));
                    holder.Time.setTextColor(Color.parseColor("#545454"));
                    imageView.setVisibility(View.VISIBLE);
                    textView.setVisibility(View.GONE);
                    imageView.setColorFilter(0);
                    imageView.setImageResource(R.drawable.ic_tab_journal);
                    break;
                case "Chat":
                    holder.Heading.setTextColor(Color.parseColor("#7bc044"));
                    holder.Title.setTextColor(Color.parseColor("#7bc044"));
                    holder.Time.setTextColor(Color.parseColor("#7bc044"));
                    imageView.setVisibility(View.VISIBLE);
                    textView.setVisibility(View.GONE);
                    imageView.setColorFilter(0);
                    imageView.setImageResource(R.drawable.ic_chat_green);
                    break;

                //
                case "Favorites":
                    holder.Heading.setTextColor(Color.parseColor("#0D6EBE"));
                    holder.Title.setTextColor(Color.parseColor("#0D6EBE"));
                    holder.Time.setTextColor(Color.parseColor("#0D6EBE"));
                    imageView.setVisibility(View.VISIBLE);
                    textView.setVisibility(View.GONE);
                    imageView.setColorFilter(0);
                    imageView.setImageResource(R.drawable.ic_fevorite_act);
                    break;
                case "Likes":
                    holder.Heading.setTextColor(Color.parseColor("#0D6EBE"));
                    holder.Title.setTextColor(Color.parseColor("#0D6EBE"));
                    holder.Time.setTextColor(Color.parseColor("#0D6EBE"));
                    imageView.setVisibility(View.VISIBLE);
                    textView.setVisibility(View.GONE);
                    imageView.setImageResource(R.drawable.ic_thumb_licked);
                    String activty_model = mData.get(pos).getModel();
                    if (activty_model.contains("Question")) {
                        holder.Heading.setTextColor(Color.parseColor("#0E6DC1"));
                        holder.Title.setTextColor(Color.parseColor("#0E6DC1"));
                        holder.Time.setTextColor(Color.parseColor("#0E6DC1"));
                        imageView.setColorFilter(Color.parseColor("#0E6DC1"), android.graphics.PorterDuff.Mode.SRC_IN);
                    } else if (activty_model.contains("Answers")) {
                        holder.Heading.setTextColor(Color.parseColor("#0E6DC1"));
                        holder.Title.setTextColor(Color.parseColor("#0E6DC1"));
                        holder.Time.setTextColor(Color.parseColor("#0E6DC1"));
                        imageView.setColorFilter(Color.parseColor("#0E6DC1"), android.graphics.PorterDuff.Mode.SRC_IN);
                    } else if (activty_model.contains("Strains") || activty_model.contains("Strain")) {
                        holder.Heading.setTextColor(Color.parseColor("#F4C42E"));
                        holder.Title.setTextColor(Color.parseColor("#F4C42E"));
                        holder.Time.setTextColor(Color.parseColor("#F4C42E"));
                        imageView.setColorFilter(Color.parseColor("#F4C42E"), android.graphics.PorterDuff.Mode.SRC_IN);
                    } else if (activty_model.contains("Groups")) {

                    } else if (activty_model.contains("Budz Map") || activty_model.contains("Budz Adz") || activty_model.contains("SubUser")) {
                        holder.Heading.setTextColor(Color.parseColor("#942B88"));
                        holder.Title.setTextColor(Color.parseColor("#942B88"));
                        holder.Time.setTextColor(Color.parseColor("#942B88"));
                        imageView.setColorFilter(Color.parseColor("#942B88"), android.graphics.PorterDuff.Mode.SRC_IN);
                    } else if (activty_model.contains("Journal")) {

                    } else if (activty_model.contains("ShoutOutLike")) {
                        holder.Heading.setTextColor(Color.parseColor("#D551D8"));
                        holder.Title.setTextColor(Color.parseColor("#D551D8"));
                        holder.Time.setTextColor(Color.parseColor("#D551D8"));
                        imageView.setColorFilter(Color.parseColor("#D551D8"), android.graphics.PorterDuff.Mode.SRC_IN);
                    } else if (activty_model.contains("UserPost")) {
                        holder.Heading.setTextColor(Color.parseColor("#80C548"));
                        holder.Title.setTextColor(Color.parseColor("#80C548"));
                        holder.Time.setTextColor(Color.parseColor("#80C548"));
                        imageView.setColorFilter(Color.parseColor("#80C548"), android.graphics.PorterDuff.Mode.SRC_IN);
                    }
                    break;
                case "Tags":
                    holder.Heading.setTextColor(Color.parseColor("#5CA71A"));
                    holder.Title.setTextColor(Color.parseColor("#5CA71A"));
                    holder.Time.setTextColor(Color.parseColor("#5CA71A"));
                    imageView.setVisibility(View.VISIBLE);
                    textView.setVisibility(View.GONE);
                    imageView.setColorFilter(0);
                    imageView.setImageResource(R.drawable.ic_hashtag);
                    break;
                case "Budz Map":
                case "Budz Adz":
                    holder.Heading.setTextColor(Color.parseColor("#942B88"));
                    holder.Title.setTextColor(Color.parseColor("#942B88"));
                    holder.Time.setTextColor(Color.parseColor("#942B88"));
                    imageView.setVisibility(View.VISIBLE);
                    textView.setVisibility(View.GONE);
                    imageView.setColorFilter(0);
                    imageView.setImageResource(R.drawable.budzmapnewic);
                    break;
                case "Strains":
                    holder.Heading.setTextColor(Color.parseColor("#F4C42E"));
                    holder.Title.setTextColor(Color.parseColor("#F4C42E"));
                    holder.Time.setTextColor(Color.parseColor("#F4C42E"));
                    imageView.setVisibility(View.VISIBLE);
                    textView.setVisibility(View.GONE);
                    imageView.setColorFilter(0);
                    imageView.setImageResource(R.drawable.ic_tab_strain);
                    break;
                case "Users":
                    holder.Heading.setTextColor(Color.parseColor("#979797"));
                    holder.Title.setTextColor(Color.parseColor("#979797"));
                    holder.Time.setTextColor(Color.parseColor("#979797"));
                    imageView.setVisibility(View.VISIBLE);
                    textView.setVisibility(View.GONE);
                    imageView.setColorFilter(0);
                    imageView.setImageResource(R.drawable.ic_user_person);
                    break;
                case "ShoutOut":
                    holder.Heading.setTextColor(Color.parseColor("#D551D8"));
                    holder.Title.setTextColor(Color.parseColor("#D551D8"));
                    holder.Time.setTextColor(Color.parseColor("#D551D8"));
                    imageView.setVisibility(View.VISIBLE);
                    textView.setVisibility(View.GONE);
                    imageView.setImageResource(R.drawable.ic_shoot_out_purpole);
                    imageView.setColorFilter(Color.parseColor("#D551D8"), android.graphics.PorterDuff.Mode.SRC_IN);
                    break;
                case "Post":
                    holder.Heading.setTextColor(Color.parseColor("#80C548"));
                    holder.Title.setTextColor(Color.parseColor("#80C548"));
                    holder.Time.setTextColor(Color.parseColor("#80C548"));
                    imageView.setVisibility(View.VISIBLE);
                    textView.setVisibility(View.GONE);
                    imageView.setImageResource(R.drawable.social_wall_new);

                    imageView.setColorFilter(Color.parseColor("#80C548"), android.graphics.PorterDuff.Mode.SRC_IN);
                    break;
                case "Comment":
                    holder.Heading.setTextColor(Color.parseColor("#80C548"));
                    holder.Title.setTextColor(Color.parseColor("#80C548"));
                    holder.Time.setTextColor(Color.parseColor("#80C548"));
                    imageView.setVisibility(View.VISIBLE);
                    textView.setVisibility(View.GONE);
                    imageView.setColorFilter(Color.parseColor("#80C548"), android.graphics.PorterDuff.Mode.SRC_IN);
                    imageView.setImageResource(R.drawable.comment_icon);
                    break;
                case "Admin":
                    holder.Heading.setTextColor(Color.parseColor("#5CA71A"));
                    holder.Title.setTextColor(Color.parseColor("#5CA71A"));
                    holder.Time.setTextColor(Color.parseColor("#5CA71A"));
                    imageView.setVisibility(View.VISIBLE);
                    textView.setVisibility(View.GONE);
                    imageView.setColorFilter(0);
                    imageView.setImageResource(R.drawable.admin);
                    break;
                default:
                    holder.Heading.setTextColor(Color.parseColor("#ff0000"));
                    holder.Title.setTextColor(Color.parseColor("#ff0000"));
                    holder.Time.setTextColor(Color.parseColor("#ff0000"));
                    imageView.setVisibility(View.VISIBLE);
                    textView.setVisibility(View.GONE);
                    imageView.setColorFilter(0);
                    imageView.setImageResource(R.drawable.ic_block_group_icon);
                    break;
            }
        }
    }
}