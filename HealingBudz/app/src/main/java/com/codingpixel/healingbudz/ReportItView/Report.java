package com.codingpixel.healingbudz.ReportItView;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.codingpixel.healingbudz.DataModel.ReportQuestionListDataModel;
import com.codingpixel.healingbudz.GestureListner.OnSwipeTouchListner;
import com.codingpixel.healingbudz.R;
import com.codingpixel.healingbudz.activity.home.home_fragment.groups_tab.GroupsChatViewActivity;
import com.codingpixel.healingbudz.adapter.ReportQuestionHeaderLayoutRecylerAdapter;
import com.codingpixel.healingbudz.interfaces.ReportSendButtonLstner;
import com.mancj.slideup.SlideUp;
import com.mancj.slideup.SlideUpBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.codingpixel.healingbudz.static_function.UIModification.HideKeyboard;

public class Report implements ReportQuestionHeaderLayoutRecylerAdapter.ItemClickListener {
    private Context mContext;
    private View mReportitView;
    private LayoutInflater mInflater;
    private SlideUp slide;
    boolean isSlide = false;
    JSONObject object = new JSONObject();
    private Button Send_BTN;
    private View line_color_report;
    private RecyclerView Report_Question_RecylerView;
    private ImageView close_slide, indi_report;
    private int position;
    String mColor = "#0083ca";
    LinearLayout man_tp;
    String nameReport = "";
    ArrayList<ReportQuestionListDataModel> dataModels;
    private ReportSendButtonLstner Listner;

    public boolean isSlide() {
        return isSlide;
    }


    public Report(Context context, ReportSendButtonLstner listner) {
        mContext = context;
        Listner = listner;
        mInflater = LayoutInflater.from(context);
        mColor = "#0083ca";

//        mInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public Report(Context context, ReportSendButtonLstner listner, String color, String nameReport) {
        mContext = context;
        Listner = listner;
        mInflater = LayoutInflater.from(context);
        mColor = color;
        this.nameReport = nameReport;
//        mInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        mInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public View getView() {
        if (mReportitView == null) {
            mReportitView = mInflater.inflate(R.layout.reportit_layout, null);
            Report_Question_RecylerView = mReportitView.findViewById(R.id.report_question_recyler_view);
//            man_tp = mReportitView.findViewById(R.id.man_tp);


            Send_BTN = mReportitView.findViewById(R.id.send_button);
            indi_report = mReportitView.findViewById(R.id.indi_report);
            line_color_report = mReportitView.findViewById(R.id.line_color_report);
            if (nameReport.equalsIgnoreCase("strain")) {
                Send_BTN.setBackgroundResource(R.drawable.strain_flag_send_btn_bg);
                indi_report.setImageResource(R.drawable.strain_flag_close_indicator);
                indi_report.setColorFilter(Color.parseColor(mColor), android.graphics.PorterDuff.Mode.SRC_IN);
                line_color_report.setBackgroundColor(Color.parseColor(mColor));
            } else if (nameReport.equalsIgnoreCase("budz")) {
                Send_BTN.setBackgroundResource(R.drawable.budz_flag_send_btn_bg);
                indi_report.setImageResource(R.drawable.budz_flag_close_indicator);
                line_color_report.setBackgroundColor(Color.parseColor(mColor));
            } else if (nameReport.equalsIgnoreCase("gallery")) {
                Send_BTN.setBackgroundResource(R.drawable.gallery_flag_send_btn_bg);
                indi_report.setImageResource(R.drawable.strain_flag_close_indicator);
                indi_report.setColorFilter(Color.parseColor(mColor), android.graphics.PorterDuff.Mode.SRC_IN);
                line_color_report.setBackgroundColor(Color.parseColor(mColor));
            }


            RecyclerView.LayoutManager layoutManager_rq = new LinearLayoutManager(mContext);
            Report_Question_RecylerView.setLayoutManager(layoutManager_rq);
            close_slide = mReportitView.findViewById(R.id.indicator_close);
            close_slide.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (slide != null) {
                        isSlide = false;
                        slide.hide();
                    }
                }
            });

            Send_BTN.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    slide.hide();
                    isSlide = false;
                    Listner.OnSnedClicked(object, position);
                }
            });

        }
        return mReportitView;
    }

    public View getViewDisplay() {
        return slide.getSliderView();
    }

    public SlideUp InitSlide() {

        slide = new SlideUpBuilder(mReportitView)
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
                .build();
        return slide;
    }

    public void SlideDown(int Position, ArrayList<ReportQuestionListDataModel> dataModel, ReportSendButtonLstner listner) {
        dataModels = dataModel;
        isSlide = true;
        Listner = listner;
        position = Position;
        try {
            object.put("reason", dataModels.get(0).getTitle());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        dataModels.get(0).setSelected(true);
        ReportQuestionHeaderLayoutRecylerAdapter recyler_adapter = new ReportQuestionHeaderLayoutRecylerAdapter(mContext, dataModels, nameReport,mColor);
        Report_Question_RecylerView.setAdapter(recyler_adapter);
        recyler_adapter.setClickListener(this);
        if (dataModels.size() > 0) {
            slide.show();
        }
    }

    public void SlideDown(int Position, ArrayList<ReportQuestionListDataModel> dataModel, ReportSendButtonLstner listner, String name) {
        dataModels = dataModel;
        isSlide = true;
        Listner = listner;
        position = Position;
        try {
            object.put("reason", dataModels.get(0).getTitle());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        dataModels.get(0).setSelected(true);
        ReportQuestionHeaderLayoutRecylerAdapter recyler_adapter = new ReportQuestionHeaderLayoutRecylerAdapter(mContext, dataModels, name,mColor);
        Report_Question_RecylerView.setAdapter(recyler_adapter);
        recyler_adapter.setClickListener(this);
        if (dataModels.size() > 0) {
            slide.show();
        }
    }

    public void SlideUp() {
        if (slide != null) {
            isSlide = false;
            slide.hide();
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        try {
            object.put("reason", dataModels.get(position).getTitle());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
