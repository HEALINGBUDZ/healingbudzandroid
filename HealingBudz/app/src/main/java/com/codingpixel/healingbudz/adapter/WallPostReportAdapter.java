package com.codingpixel.healingbudz.adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.codingpixel.healingbudz.R;
import com.codingpixel.healingbudz.customeUI.HealingBudTextView;

import java.util.List;

/*
 * Created by M_Muzammil Sharif on 13-Mar-18.
 */

public class WallPostReportAdapter extends RecyclerView.Adapter<WallPostReportAdapter.WallPostReportVH> {
    private List<ReportObject> objects;

    public List<ReportObject> getObjects() {
        return objects;
    }

    public void setObjects(List<ReportObject> objects) {
        this.objects = objects;
        notifyDataSetChanged();
    }

    public WallPostReportAdapter(List<ReportObject> objects) {
        this.objects = objects;
    }

    @Override
    public WallPostReportVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vh_wall_post_report, parent, false);
//        RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams) view.getLayoutParams();
//        lp.height = parent.getMeasuredHeight() / 6;
//        view.setLayoutParams(lp);
        WallPostReportVH viewHolder = new WallPostReportVH(view);
        return viewHolder;
//        return new WallPostReportVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.vh_wall_post_report, parent, false));
    }

    @Override
    public void onBindViewHolder(WallPostReportVH holder, final int position) {
        holder.loadView(objects.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                upDateSelected(position);
            }
        });
    }

    public ReportObject getSelected() {
        for (ReportObject object : objects) {
            if (object.isSelected()) {
                return object;
            }
        }
        return null;
    }

    private void upDateSelected(int position) {
        for (int i = 0; i < objects.size(); i++) {
            objects.get(i).setSelected(i == position);
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (objects == null) {
            return 0;
        }
        return objects.size();
    }

    class WallPostReportVH extends RecyclerView.ViewHolder {
        private ImageView img;
        private View indicator;
        private HealingBudTextView textView;

        WallPostReportVH(View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.vh_wall_post_report_img);
            indicator = itemView.findViewById(R.id.vh_wall_post_report_indicator);
            textView = itemView.findViewById(R.id.vh_wall_post_report_txt);
        }

        void loadView(ReportObject object) {
            if (object.isSelected()) {
                itemView.setBackgroundColor(Color.parseColor("#403f3f"));
                indicator.setVisibility(View.VISIBLE);
                img.setImageResource(R.drawable.ic_double_circle);
            } else {
                itemView.setBackgroundColor(Color.TRANSPARENT);
                indicator.setVisibility(View.GONE);
                img.setImageResource(R.drawable.ic_white_circle);
            }
            textView.setText(object.getTxt());
        }
    }

    public static class ReportObject {
        private String txt;
        private boolean selected = false;

        public ReportObject(String txt) {
            this.txt = txt;
        }

        public ReportObject(String txt, boolean selected) {
            this.txt = txt;
            this.selected = selected;
        }

        public String getTxt() {
            return txt;
        }

        public void setTxt(String txt) {
            this.txt = txt;
        }

        public boolean isSelected() {
            return selected;
        }

        public void setSelected(boolean selected) {
            this.selected = selected;
        }
    }
}
