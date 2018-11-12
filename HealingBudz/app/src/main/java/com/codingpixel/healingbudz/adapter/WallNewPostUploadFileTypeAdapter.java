package com.codingpixel.healingbudz.adapter;
/*
 * Created by M_Muzammil Sharif on 13-Mar-18.
 */

import android.graphics.Color;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.codingpixel.healingbudz.R;
import com.codingpixel.healingbudz.Utilities.Utility;
import com.codingpixel.healingbudz.customeUI.HealingBudTextViewBold;
import com.codingpixel.healingbudz.customeUI.HealingBudTextViewRegular;
import com.codingpixel.healingbudz.interfaces.RecyclerViewItemClickListener;

import java.util.List;

public class WallNewPostUploadFileTypeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<WallNewPostUploadFileType> dataSet;
    private boolean larg;
    private RecyclerViewItemClickListener clickListener;

    public WallNewPostUploadFileTypeAdapter(@NonNull List<WallNewPostUploadFileType> dataSet, @NonNull RecyclerViewItemClickListener clickListener) {
        this.dataSet = dataSet;
        this.larg = true;
        this.clickListener = clickListener;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0) {
            return new WallNewPostUploadFileTypeVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.vh_post_upload_item_type, parent, false));
        }
        ImageView img = new ImageView(parent.getContext());
        int width = Utility.convertDpToPixel(55, parent.getContext());
        img.setLayoutParams(new RecyclerView.LayoutParams(width, width));
        width = Utility.convertDpToPixel(15, parent.getContext());
        img.setPadding(width, width, width, width);
        return new RecyclerView.ViewHolder(img) {
        };
    }

    public List<WallNewPostUploadFileType> getDataSet() {
        return dataSet;
    }

    public void setDataSet(List<WallNewPostUploadFileType> dataSet) {
        this.dataSet = dataSet;
        notifyDataSetChanged();
    }

    public boolean isLarg() {
        return larg;
    }

    public void setLarg(boolean larg) {
        this.larg = larg;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int pos) {
        final int position = pos;
        if (getItemViewType(position) == 0) {
            ((WallNewPostUploadFileTypeVH) holder).loadView(dataSet.get(position));
        } else {
            ((ImageView) holder.itemView).setImageResource(dataSet.get(position).isActive() ? dataSet.get(position).getSelectedIcon() : dataSet.get(position).getIcon());
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickListener.onItemClick(null, dataSet.get(position).getPos(), 0);
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        return isLarg() ? 0 : 1;
    }

    @Override
    public int getItemCount() {
        return dataSet == null ? 0 : dataSet.size();
    }

    class WallNewPostUploadFileTypeVH extends RecyclerView.ViewHolder {
        private ImageView icon;
        private HealingBudTextViewBold title;
        private HealingBudTextViewRegular des;

        WallNewPostUploadFileTypeVH(View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.vh_post_upload_item_type_icon);
            des = itemView.findViewById(R.id.vh_post_upload_item_type_des);
            title = itemView.findViewById(R.id.vh_post_upload_item_type_title);
        }

        void loadView(WallNewPostUploadFileType type) {
            if (type.isActive() && type.getSelectedIcon() != type.getIcon()) {
//                des.setTextColor(ContextCompat.getColor(this.itemView.getContext(), R.color.feeds_liked_blue_color));
//                title.setTextColor(ContextCompat.getColor(this.itemView.getContext(), R.color.feeds_liked_blue_color));
                des.setTextColor(Color.parseColor("#757575"));
                title.setTextColor(Color.parseColor("#b9b9b9"));
                icon.setImageResource(type.getSelectedIcon());
            } else {
                icon.setImageResource(type.getIcon());
                des.setTextColor(Color.parseColor("#757575"));
                title.setTextColor(Color.parseColor("#b9b9b9"));
            }
            des.setText(type.getDescription());
            title.setText(type.getTitle());
        }
    }

    public static class WallNewPostUploadFileType {
        @DrawableRes
        private int icon;
        private String title;
        private String description;
        private boolean active;
        @DrawableRes
        private int selectedIcon;
        private int pos;

        public int getPos() {
            return pos;
        }

        public void setPos(int pos) {
            this.pos = pos;
        }

        @DrawableRes
        public int getSelectedIcon() {
            return selectedIcon;
        }

        public void setSelectedIcon(@DrawableRes int selectedIcon) {
            this.selectedIcon = selectedIcon;
        }

        public WallNewPostUploadFileType(@DrawableRes int icon, String title, String description, int pos) {
            this.icon = icon;
            this.title = title;
            this.pos = pos;
            this.description = description;
            this.active = false;
            this.selectedIcon = icon;
        }

        public WallNewPostUploadFileType(@DrawableRes int icon, @DrawableRes int selectedIcon, String title, String description, boolean active, int pos) {
            this.icon = icon;
            this.title = title;
            this.pos = pos;
            this.description = description;
            this.active = active;
            this.selectedIcon = selectedIcon;
        }

        @DrawableRes
        public int getIcon() {
            return icon;
        }

        public void setIcon(@DrawableRes int icon) {
            this.icon = icon;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public boolean isActive() {
            return active;
        }

        public void setActive(boolean active) {
            this.active = active;
        }
    }
}
