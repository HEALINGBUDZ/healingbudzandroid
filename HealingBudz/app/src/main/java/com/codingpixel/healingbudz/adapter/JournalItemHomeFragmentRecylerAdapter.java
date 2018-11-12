package com.codingpixel.healingbudz.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.codingpixel.healingbudz.DataModel.JournalFragmentDataModel;
import com.codingpixel.healingbudz.R;

import java.util.ArrayList;

import static com.codingpixel.healingbudz.Utilities.DateConverter.convertDate_with_month_name;

public class JournalItemHomeFragmentRecylerAdapter extends RecyclerView.Adapter<JournalItemHomeFragmentRecylerAdapter.ViewHolder> {
    private LayoutInflater mInflater;
    Context context;
    ArrayList<JournalFragmentDataModel.JournalEvent> mData;
    private JournalItemHomeFragmentRecylerAdapter.ItemClickListener mClickListener;

    public JournalItemHomeFragmentRecylerAdapter(Context context, ArrayList<JournalFragmentDataModel.JournalEvent> journalEvents) {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.mData = journalEvents;
    }

    // inflates the cell layout from xml when needed
    @Override
    public JournalItemHomeFragmentRecylerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.journal_item_recyler_view_item, parent, false);
        JournalItemHomeFragmentRecylerAdapter.ViewHolder viewHolder = new JournalItemHomeFragmentRecylerAdapter.ViewHolder(view);
        return viewHolder;
    }

    // binds the data to the textview in each cell
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.Event_title.setText(mData.get(position).getEvent_Title());
        holder.Event_Date.setText(convertDate_with_month_name(mData.get(position).getEvent_Date()));
        holder.Emoji.setImageResource(GetEmojis(mData.get(position).getFeeling()));
        if (position == mData.size() - 1) {
            holder.Bottom_line.setVisibility(View.GONE);
        } else {
            holder.Bottom_line.setVisibility(View.VISIBLE);
        }
    }

    // total number of cells
    @Override
    public int getItemCount() {
        return mData.size();
    }

    // stores and recycles views as they are scrolled off screen
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView Emoji;
        View Bottom_line;
        TextView Event_title, Event_Date;

        public ViewHolder(View itemView) {
            super(itemView);
            Emoji = itemView.findViewById(R.id.emoji);
            Event_Date = itemView.findViewById(R.id.event_date);
            Event_title = itemView.findViewById(R.id.event_title);
            Bottom_line = itemView.findViewById(R.id.bottom_line);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    public void setClickListener(JournalItemHomeFragmentRecylerAdapter.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }


    public static int GetEmojis(String image_name) {
        switch (image_name) {
            case ":angry:":
                return R.drawable.angry;
            case ":anguished:":
                return R.drawable.anguished;
            case ":astonished:":
                return R.drawable.astonished;
            case ":cold_sweat:":
                return R.drawable.cold_sweat;
            case ":confounded:":
                return R.drawable.confounded;
            case ":confused:":
                return R.drawable.confused;
            case ":cry:":
                return R.drawable.cry;
            case ":dispointed_relieved:":
                return R.drawable.disappointed_relieved;
            case ":dizzy_face:":
                return R.drawable.dizzy_face;
            case ":expressionless:":
                return R.drawable.expressionless;
            case ":fearful:":
                return R.drawable.fearful;
            case ":flushed:":
                return R.drawable.flushed;
            case ":frowning:":
                return R.drawable.frowning;
            case ":grin:":
                return R.drawable.grin;
            case ":grinning:":
                return R.drawable.grinning;
            case ":innocent:":
                return R.drawable.innocent;
            case ":joy:":
                return R.drawable.joy;
            case ":joy_cat:":
                return R.drawable.joy_cat;
            case ":kissing:":
                return R.drawable.kissing;
            case ":kissing_closed_eyes:":
                return R.drawable.kissing_closed_eyes;
            case ":kissing_heart:":
                return R.drawable.kissing_heart;
            case ":laughing:":
                return R.drawable.laughing;
            case ":mask:":
                return R.drawable.mask;
            case ":neutral_face:":
                return R.drawable.neutral_face;
            case ":no_mouth:":
                return R.drawable.no_mouth;
            case ":pensive:":
                return R.drawable.pensive;
            case ":persevere:":
                return R.drawable.persevere;
            case ":rage:":
                return R.drawable.rage;
            case ":rolling_eyes:":
                return R.drawable.rolling_eyes;
            case ":scream:":
                return R.drawable.scream;
            case ":scream_cat:":
                return R.drawable.scream_cat;
            case ":sleeping:":
                return R.drawable.sleeping;
            case ":slight_frown:":
                return R.drawable.slight_frown;
            case ":slight_smile:":
                return R.drawable.slight_smile;
            case ":smile:":
                return R.drawable.smile;
            case ":smile_cat:":
                return R.drawable.smile_cat;
            case ":smiley:":
                return R.drawable.smiley;
            case ":smiling_imp:":
                return R.drawable.smiling_imp;
            case ":sweat:":
                return R.drawable.sweat;
            case ":sweat_smile:":
                return R.drawable.sweat_smile;
            case ":triumph:":
                return R.drawable.triumph;
            case ":unamused:":
                return R.drawable.unamused;
            case ":upside_down:":
                return R.drawable.upside_down;
            case ":weary:":
                return R.drawable.weary;
            case ":wink:":
                return R.drawable.wink;
            case ":happy:" :
                return R.drawable.ic_emoji;
            default:
                return R.drawable.sweat;
        }
    }
}