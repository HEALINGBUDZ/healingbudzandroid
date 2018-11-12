package com.codingpixel.healingbudz.ReportItView;
/*
 * Created by M_Muzammil Sharif on 13-Mar-18.
 */

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;

import com.codingpixel.healingbudz.DataModel.ReportQuestionListDataModel;
import com.codingpixel.healingbudz.R;
import com.codingpixel.healingbudz.Utilities.Constants;
import com.codingpixel.healingbudz.Utilities.Dialogs;
import com.codingpixel.healingbudz.adapter.WallPostReportAdapter;
import com.codingpixel.healingbudz.customeUI.HealingBudButtonBold;
import com.codingpixel.healingbudz.customeUI.HealingBudTextView;
import com.mancj.slideup.SlideUp;
import com.mancj.slideup.SlideUpBuilder;

import java.util.ArrayList;

public class WallTopDropDowns {
    private SlideUp slide;
    private RecyclerView recyclerView;
    private HealingBudButtonBold button;
    private HealingBudTextView title;

    public WallTopDropDowns(@NonNull Context context) {
        View v = LayoutInflater.from(context).inflate(R.layout.wall_post_report_layout, null);
        slide = new SlideUpBuilder(v)
                .withListeners(new SlideUp.Listener.Events() {
                    @Override
                    public void onSlide(float percent) {
                    }

                    @Override
                    public void onVisibilityChanged(int visibility) {
                        if (visibility == View.GONE) {
                        }
                    }
                })
                .withStartGravity(Gravity.TOP)
                .withLoggingEnabled(true)
                .withStartState(SlideUp.State.HIDDEN)
                .withAutoSlideDuration(600)
                .build();
        v.findViewById(R.id.indicator_close_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                slide.hide();
            }
        });

        button = v.findViewById(R.id.wall_post_report_send_button);
        title = v.findViewById(R.id.reporting_heading);
        recyclerView = v.findViewById(R.id.report_wall_post_recyler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
    }

    public View getView() {
        return slide.getSliderView();
    }

    public final void showReportPost(final int postId, @NonNull final Dialogs.DialogItemClickListener listener) {
        final WallPostReportAdapter adapter = new WallPostReportAdapter(getObjects(false, 0));
        recyclerView.setAdapter(adapter);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onDialogItemClickListener(null, adapter.getSelected().getTxt(), postId);
            }
        });
        title.setText("REASON FOR REPORTING:");
        button.setText("SEND");
        slide.show();
    }

    public final void showFilterDropdown(@NonNull final Dialogs.DialogItemClickListener listener, int currentFilter) {
        final WallPostReportAdapter adapter = new WallPostReportAdapter(getObjects(true, currentFilter));
        recyclerView.setAdapter(adapter);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onDialogItemClickListener(null, adapter.getSelected().getTxt(), -5319);
            }
        });
        title.setText("FILTER:");
        button.setText("APPLY");
        slide.show();
    }

    private ArrayList<WallPostReportAdapter.ReportObject> getObjects(boolean isFilter, int selected) {
        ArrayList<WallPostReportAdapter.ReportObject> list = new ArrayList<>();
        if (isFilter) {
            list.add(new WallPostReportAdapter.ReportObject(Constants.POST_FILTERS[0], selected == 0));
            list.add(new WallPostReportAdapter.ReportObject(Constants.POST_FILTERS[1], selected == 1));
            list.add(new WallPostReportAdapter.ReportObject(Constants.POST_FILTERS[2], selected == 2));
        } else {
            list.add(new WallPostReportAdapter.ReportObject("Nudity or sexual content", true));
            list.add(new WallPostReportAdapter.ReportObject("Harassment or hate speech", false));
            list.add(new WallPostReportAdapter.ReportObject("Threatening, violent, or concerning", false));
            list.add(new WallPostReportAdapter.ReportObject("Spam"));
            list.add(new WallPostReportAdapter.ReportObject("Post is Offensive", false));
            list.add(new WallPostReportAdapter.ReportObject("Unrelated Post"));
        }
        return list;
    }

    public void dismissSlide() {
        slide.hide();
    }
}
