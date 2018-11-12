package com.codingpixel.healingbudz.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.codingpixel.healingbudz.DataModel.StrainDataModel;
import com.codingpixel.healingbudz.R;

import java.util.ArrayList;

import static com.codingpixel.healingbudz.activity.home.home_fragment.Strain_tab.StrainTabFragment.isFilter;
import static com.codingpixel.healingbudz.Utilities.Constants.D_FORMAT_ONE;
import static com.codingpixel.healingbudz.Utilities.StrainRatingImage.Strain_Rating;

public class StrainTabFragmentRecylerAdapter extends RecyclerView.Adapter<StrainTabFragmentRecylerAdapter.ViewHolder> {
    private LayoutInflater mInflater;
    Context context;
    ArrayList<StrainDataModel> mData = new ArrayList<>();
    private StrainTabFragmentRecylerAdapter.ItemClickListener mClickListener;

    public StrainTabFragmentRecylerAdapter(Context context, ArrayList<StrainDataModel> dataModels) {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.mData = dataModels;
    }

    // inflates the cell layout from xml when needed
    @Override
    public StrainTabFragmentRecylerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.strain_recyler_item, parent, false);
        StrainTabFragmentRecylerAdapter.ViewHolder viewHolder = new StrainTabFragmentRecylerAdapter.ViewHolder(view);
        return viewHolder;
    }

    // binds the data to the textview in each cell
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.Title.setText(mData.get(position).getTitle());
        holder.Rating.setText(D_FORMAT_ONE.format(mData.get(position).getRating()) + "");
        holder.Review.setText(mData.get(position).getReviews());
        if (mData.get(position).getAlphabetic_keyword().equalsIgnoreCase("i")) {
            holder.Alphatic_Keyword.setBackground(context.getDrawable(R.drawable.strain_liseted_alphabetically_border_bg_i));
            holder.Alphatic_Keyword.setText(mData.get(position).getAlphabetic_keyword());
            holder.Alphatic_Keyword.setTextColor(context.getColor(R.color.i));
        } else if (mData.get(position).getAlphabetic_keyword().equalsIgnoreCase("s")) {
            holder.Alphatic_Keyword.setBackground(context.getDrawable(R.drawable.strain_liseted_alphabetically_border_bg_s));
            holder.Alphatic_Keyword.setText(mData.get(position).getAlphabetic_keyword());
            holder.Alphatic_Keyword.setTextColor(context.getColor(R.color.s));
        } else if (mData.get(position).getAlphabetic_keyword().equalsIgnoreCase("h")) {
            holder.Alphatic_Keyword.setBackground(context.getDrawable(R.drawable.strain_liseted_alphabetically_border_bg_h));
            holder.Alphatic_Keyword.setText(mData.get(position).getAlphabetic_keyword());
            holder.Alphatic_Keyword.setTextColor(context.getColor(R.color.h));
        }

        if (isFilter) {
            holder.Matche_count.setVisibility(View.VISIBLE);
            holder.Matche_count.setText(mData.get(position).getMathces() + " of 4");
        } else {
            holder.Matche_count.setVisibility(View.GONE);
        }
        holder.Rating_img.setImageResource(Strain_Rating(mData.get(position).getRating()));
    }

    // total number of cells
    @Override
    public int getItemCount() {
        return mData.size();
    }

    // stores and recycles views as they are scrolled off screen
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView Title;
        TextView Rating, Review;
        TextView Alphatic_Keyword;
        TextView Matche_count;
        ImageView Rating_img;

        public ViewHolder(View itemView) {
            super(itemView);
            Title = itemView.findViewById(R.id.strain_title);
            Rating = itemView.findViewById(R.id.rating);
            Review = itemView.findViewById(R.id.reviews);
            Alphatic_Keyword = itemView.findViewById(R.id.alpabetic);
            Matche_count = itemView.findViewById(R.id.matche_count);
            Rating_img = itemView.findViewById(R.id.ratin_image);
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    public void setClickListener(StrainTabFragmentRecylerAdapter.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}