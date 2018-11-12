package com.codingpixel.healingbudz.activity.home.side_menu.my_journal;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.codingpixel.healingbudz.DataModel.ModeCalanderModel;
import com.codingpixel.healingbudz.R;
import com.codingpixel.healingbudz.adapter.ModeCalanderRecylerAdapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ModeCalendarFragment extends Fragment {
    ModeCalanderRecylerAdapter adapter;
    RecyclerView recyclerView;
    LinearLayout Left_Month, Right_Month;
    TextView Previous_Month, Next_month, current_month;
    TextView Date_Text_View;
    static int current;
    int maxDay;
    int cDay;
    int today_entry;
    int current_year;
    List<ModeCalanderModel> str2 = new ArrayList<ModeCalanderModel>();
    int counter = 2;
    String[] month_array = {"JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"};

    public ModeCalendarFragment() {
        super();
    }

    public ModeCalendarFragment(int today_entry) {
        this.today_entry = today_entry;
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.my_journal_mode_fragment_layout, container, false);
        recyclerView = view.findViewById(R.id.mode_calendar_recyler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 7));
        adapter = new ModeCalanderRecylerAdapter(getContext(), str2);
        recyclerView.setAdapter(adapter);
        Left_Month = view.findViewById(R.id.left_month);
        Right_Month = view.findViewById(R.id.right_month);
        Previous_Month = (TextView) view.findViewById(R.id.previous_month);
        Next_month = (TextView) view.findViewById(R.id.next_month);
        current_month = (TextView) view.findViewById(R.id.current_month);
        Date_Text_View = (TextView) view.findViewById(R.id.text_view);
        Left_Month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View button_view) {
                if (counter == 0) {
                    counter = 11;
                    current_year--;
                } else {
                    counter--;
                }
                current_month.setText(month_array[counter] + " " + current_year);
                int left_counter = counter;
                int right_counter = counter;


                if (left_counter == 0) {
                    left_counter = 11;
                } else {
                    left_counter--;
                }
                Previous_Month.setText(month_array[left_counter]);


                if (right_counter == 11) {
                    right_counter = 0;
                } else {
                    right_counter++;
                }
                Next_month.setText(month_array[right_counter]);
                dataFunction(counter + 1, left_counter);
                Animation startAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.attatch_fragment_left_to_right);
                view.startAnimation(startAnimation);
            }
        });
        Right_Month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View button_view) {
                if (counter == 11) {
                    counter = 0;
                    current_year++;
                } else {
                    counter++;
                }
                current_month.setText(month_array[counter] + " " + current_year);
                int left_counter = counter;
                int right_counter = counter;

                if (left_counter == 0) {
                    left_counter = 11;
                } else {
                    left_counter--;
                }

                Previous_Month.setText(month_array[left_counter]);


                if (right_counter == 11) {
                    right_counter = 0;
                } else {
                    right_counter++;
                }
                Next_month.setText(month_array[right_counter]);

                dataFunction(counter + 1, left_counter);

                Animation startAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.attatch_fragment_right_to_left);
                view.startAnimation(startAnimation);
            }
        });

        Calendar calendar = Calendar.getInstance();
        cDay = calendar.get(Calendar.DAY_OF_MONTH);

        current = calendar.get(calendar.MONTH) + 1;
        int cYear = calendar.get(Calendar.YEAR);
        current_year = cYear;
        counter = calendar.get(calendar.MONTH);
        current_month.setText(month_array[counter] + " " + current_year);
        int left_counter = counter;
        int right_counter = counter;


        if (left_counter == 0) {
            left_counter = 11;
        } else {
            left_counter--;
        }
        Previous_Month.setText(month_array[left_counter]);


        if (right_counter == 11) {
            right_counter = 0;
        } else {
            right_counter++;
        }
        Next_month.setText(month_array[right_counter]);
        dataFunction(current, current - 1);
        return view;
    }


    public void dataFunction(int current_month, int back_month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(current_year, current_month - 1, 1);
        maxDay = calendar.getActualMaximum(calendar.DAY_OF_MONTH);
        int previous_month;
        str2.clear();

        calendar.set(current_year, current_month, 1);
        calendar.set(current_year, back_month, 1);
        previous_month = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);


        int count = 1;
        int minus = 0;
        String first_mnth_day = getDayNameFromDate("01-" + concatinate(current_month) + "-" + current_year);
        int start_day_index = day(first_mnth_day.toUpperCase());


        for (int k = start_day_index - 1; k >= 0; k--) {

            int value = previous_month - minus++;
            str2.add(0, new ModeCalanderModel(String.valueOf(value), false, true, false));
//PREVIOUS MONTH
        }


        for (int i = 0; i < maxDay; i++) {
            if (i == 0) {
                //For giving green color to first date of every month
                str2.add(new ModeCalanderModel((i + 1) + " ", true, false, false));
            } else if ((i + 1) == cDay) {
                //For giving orange color to current date
                str2.add(new ModeCalanderModel((i + 1) + " ", false, false, true, today_entry));
            } else {
                //for giving color to calender
                str2.add(new ModeCalanderModel((i + 1) + " ", false, false, false));
            }
//CURRENT MONTH
        }

        if (str2.size() <= 35) {
            for (int i = str2.size(); i < 35; i++) {
                str2.add(new ModeCalanderModel("" + count++, false, true, false));
            }
        } else {
            for (int i = str2.size(); i < 42; i++) {
                str2.add(new ModeCalanderModel("" + count++, false, true, false));
            }
// NEXT MONTH
        }

        this.current_month.getText().toString();
        // Log.d("MONTHYEAR", this.current_month.getText().toString());
        adapter.loaded_month_year = this.current_month.getText().toString();
        Log.d("SIZE", String.valueOf(str2.size()));
        Log.d("CURRENT DATE", "" + (getDayNameFromDate("01-02-2017")));
        adapter.notifyDataSetChanged();
    }

    public static String getDayNameFromDate(String dateString) {
        SimpleDateFormat inFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date date = null;
        try {
            date = inFormat.parse(dateString);
            SimpleDateFormat outFormat = new SimpleDateFormat("EEE");
            Log.d("TAGGGG", outFormat.format(date) + "");
            return outFormat.format(date);

        } catch (ParseException e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    public int day(String dateString) {

        switch (dateString) {
            case "SUN":
                return 0;
            case "MON":
                return 1;
            case "TUE":
                return 2;
            case "WED":
                return 3;
            case "THU":
                return 4;
            case "FRI":
                return 5;
            case "SAT":
                return 6;
            default:
                return 90;
        }
    }

    String concatinate(int con_var) {
        if (con_var < 10) {

            return "0" + con_var;
        } else {

            return String.valueOf(con_var);
        }
    }
}