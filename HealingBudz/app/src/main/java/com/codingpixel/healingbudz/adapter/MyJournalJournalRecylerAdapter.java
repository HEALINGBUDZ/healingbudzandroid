package com.codingpixel.healingbudz.adapter;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
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
import com.codingpixel.healingbudz.activity.home.home_fragment.journal_tab.EditJournal;
import com.codingpixel.healingbudz.activity.home.side_menu.my_journal.JournalFragment;
import com.codingpixel.healingbudz.DataModel.JournalFragmentDataModel;
import com.codingpixel.healingbudz.R;
import com.codingpixel.healingbudz.application.HealingBudApplication;
import com.codingpixel.healingbudz.customeUI.CustomeToast;
import com.codingpixel.healingbudz.customeUI.SimpleToolTip.SimpleTooltip;
import com.codingpixel.healingbudz.data_structure.APIActions;
import com.codingpixel.healingbudz.network.VollyAPICall;
import com.codingpixel.healingbudz.network.model.APIResponseListner;
import com.codingpixel.healingbudz.network.model.URL;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.codingpixel.healingbudz.activity.home.home_fragment.journal_tab.JournalDetailsActivity.journalFragmentDataModel;
import static com.codingpixel.healingbudz.activity.splash.Splash.user;
import static com.codingpixel.healingbudz.application.HealingBudApplication.getContext;
import static com.codingpixel.healingbudz.data_structure.APIActions.ApiActions.get_groups;
import static com.codingpixel.healingbudz.static_function.IntentFunction.GoTo;


public class MyJournalJournalRecylerAdapter extends RecyclerView.Adapter<MyJournalJournalRecylerAdapter.ViewHolder> implements APIResponseListner {
    private LayoutInflater mInflater;
    ArrayList<JournalFragmentDataModel> mData;
    Context context;
    private MyJournalJournalRecylerAdapter.ItemClickListener mClickListener;

    public MyJournalJournalRecylerAdapter(Context context, ArrayList<JournalFragmentDataModel> get_journal_data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = get_journal_data;
        this.context = context;
    }

    // inflates the cell layout from xml when needed
    @Override
    public MyJournalJournalRecylerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.myjournal_journal_tab_recyler_item, parent, false);
        MyJournalJournalRecylerAdapter.ViewHolder viewHolder = new MyJournalJournalRecylerAdapter.ViewHolder(view);
        return viewHolder;

    }

    // binds the data to the textview in each cell
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(final MyJournalJournalRecylerAdapter.ViewHolder holder, final int position) {

        holder.Journal_title.setText(mData.get(position).getTitle());
        holder.Entry.setText(mData.get(position).getEvents_count() + "");

        if (mData.get(position).getIs_public() == 0) {
            holder.Public_private.setImageResource(R.drawable.ic_private_icon_journal_unchecked);
        } else {
            holder.Public_private.setImageResource(R.drawable.ic_public_icon_journal);
        }
        holder.Menu_Dots.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowToolTipDialog(view, position);
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
        Log.d("response", response);
        if (apiActions == get_groups) {
            CustomeToast.ShowCustomToast(HealingBudApplication.getContext(), "Deleted Successfully!", Gravity.TOP);
        }
    }

    @Override
    public void onRequestError(String response, APIActions.ApiActions apiActions) {
        Log.d("response", response);
    }


    // stores and recycles views as they are scrolled off screen
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView Menu_Dots, Public_private;
        TextView Journal_title, Entry;
        View dmy_view;

        public ViewHolder(View itemView) {
            super(itemView);
            Menu_Dots = itemView.findViewById(R.id.menu_dots);
            dmy_view = itemView.findViewById(R.id.dummy_view);
            Journal_title = itemView.findViewById(R.id.title_journal);
            Public_private = itemView.findViewById(R.id.p_p);
            Entry = itemView.findViewById(R.id.entryies);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }

    }

    // allows clicks events to be caught
    public void setClickListener(MyJournalJournalRecylerAdapter.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    public void ShowToolTipDialog(final View v, final int position) {
        final SimpleTooltip tooltip = new SimpleTooltip.Builder(getContext())
                .anchorView(v)
                .arrowDrawable(null)
                .text("Sample Text")
                .showArrow(false)
                .gravity(Gravity.BOTTOM)
                .dismissOnOutsideTouch(false)
                .dismissOnInsideTouch(false)
                .modal(true)
                .contentView(R.layout.my_journal_list_tooltip_dialog_layout)
                .focusable(true)
                .build();
        LinearLayout delete_journal = tooltip.findViewById(R.id.delete_journal);
        delete_journal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                tooltip.dismiss();
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("journal_id", mData.get(position).getId());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                new VollyAPICall(getContext(), false, URL.delete_journal, jsonObject, user.getSession_key(), Request.Method.POST, MyJournalJournalRecylerAdapter.this, get_groups);
                mData.remove(position);
                notifyItemRemoved(position);
                notifyDataSetChanged();
            }
        });
        LinearLayout edit_journla = tooltip.findViewById(R.id.edit_journla);

        edit_journla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tooltip.dismiss();
                journalFragmentDataModel = mData.get(position);
                JournalFragment.refreshData = true;
                GoTo(getContext(), EditJournal.class);
            }
        });
        tooltip.show();
    }
}