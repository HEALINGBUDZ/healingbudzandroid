package com.codingpixel.healingbudz.customeUI;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.codingpixel.healingbudz.R;
import com.codingpixel.healingbudz.Utilities.ViewUtils;

import java.util.List;


public class customspinner extends RelativeLayout {
    public TextView TvShowSpinner;
    public Spinner spinner;
    public RelativeLayout menu;

    public customspinner(Context context) {
        super(context);
        initializeViews(context);
    }

    public customspinner(Context context, AttributeSet attrs) {
        super(context, attrs);
        initializeViews(context);
    }

    public customspinner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initializeViews(context);
    }

    public customspinner(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initializeViews(context);
    }

    private void initializeViews(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.customspinner, this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        // Sets the images for the previous and next buttons. Uses
        // built-in images so you don't need to add images, but in
        // a real application your images should be in the
        // application package so they are always available.

        spinner = this.findViewById(R.id.spinner);
        TvShowSpinner = this.findViewById(R.id.ShowSpinner);
        menu = this.findViewById(R.id.menu);
        menu.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                spinner.performClick();
            }
        });
        TvShowSpinner.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                spinner.performClick();
            }
        });

    }

    public void SetAdapter(List<String> list) {
        if (list.size() > 7) {
            ViewUtils.SetHeightOfSpinner(spinner);
        } else {
            ViewUtils.SetHeightOfSpinner(spinner, list.size() * 120);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), R.layout.simple_spinner_item_edittext, list);
        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item_edittext);
        spinner.setAdapter(adapter);
    }

    public String GetSelectedValue() {
        return TvShowSpinner.getText().toString();
    }

    public void SetValue() {
        TvShowSpinner.setText(spinner.getSelectedItem().toString());
    }

    public void SetValue(String Value) {
        TvShowSpinner.setText(Value);
    }
}
