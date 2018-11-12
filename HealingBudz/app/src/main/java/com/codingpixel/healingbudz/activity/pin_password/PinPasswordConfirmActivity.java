package com.codingpixel.healingbudz.activity.pin_password;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.codingpixel.healingbudz.activity.Registration.FinalStepProfileComplete;
import com.codingpixel.healingbudz.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTouch;

import static com.codingpixel.healingbudz.sharedprefrences.SharedPrefrences.setString;
import static com.codingpixel.healingbudz.static_function.IntentFunction.GoTo;
import static com.codingpixel.healingbudz.static_function.UIModification.FullScreen;

public class PinPasswordConfirmActivity extends AppCompatActivity {
   int pin_code_counter = 0 ;
    String pin_code = "";
    String  code ="";

    @BindView(R.id.headeing)
    TextView Title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin_password_confirm);
        FullScreen(PinPasswordConfirmActivity.this);
        code= getIntent().getExtras().getString("code");
        assert code != null;
        ButterKnife.bind(this);
    }
  @BindView(R.id.pin_one)
    ImageView pin_one;

    @BindView(R.id.pin_two)
    ImageView pin_two;

    @BindView(R.id.pin_three)
    ImageView pin_three;

    @BindView(R.id.pin_four)
    ImageView pin_four;

    @BindView(R.id.one)
    Button one;

    @BindView(R.id.two)
    Button two;

    @BindView(R.id.three)
    Button three;

    @BindView(R.id.four)
    Button four;

    @BindView(R.id.five)
    Button five;

    @BindView(R.id.six)
    Button six;

    @BindView(R.id.seven)
    Button seven;

    @BindView(R.id.eight)
    Button eight;

    @BindView(R.id.nine)
    Button nine;

    @BindView(R.id.zero)
    Button zero;


    @OnTouch(R.id.one)
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                one.setBackgroundResource(R.drawable.pin_password_button_bg_clicked);
                return true;
            case MotionEvent.ACTION_UP:
                one.setBackgroundResource(R.drawable.pin_password_button_bg);
                SetPin(1);
                return true;
        }
        return false;
    }

    @OnTouch(R.id.two)
    public boolean onTouchTwo(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                two.setBackgroundResource(R.drawable.pin_password_button_bg_clicked);
                return true;
            case MotionEvent.ACTION_UP:
                two.setBackgroundResource(R.drawable.pin_password_button_bg);
                SetPin(2);
                return true;
        }
        return false;
    }

    @OnTouch(R.id.three)
    public boolean onTouchThree(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                three.setBackgroundResource(R.drawable.pin_password_button_bg_clicked);
                return true;
            case MotionEvent.ACTION_UP:
                three.setBackgroundResource(R.drawable.pin_password_button_bg);
                SetPin(3);
                return true;
        }
        return false;
    }

    @OnTouch(R.id.four)
    public boolean onTouchFour(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                four.setBackgroundResource(R.drawable.pin_password_button_bg_clicked);
                return true;
            case MotionEvent.ACTION_UP:
                four.setBackgroundResource(R.drawable.pin_password_button_bg);
                SetPin(4);
                return true;
        }
        return false;
    }

    @OnTouch(R.id.five)
    public boolean onTouchFive(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                five.setBackgroundResource(R.drawable.pin_password_button_bg_clicked);
                return true;
            case MotionEvent.ACTION_UP:
                five.setBackgroundResource(R.drawable.pin_password_button_bg);
                SetPin(5);
                return true;
        }
        return false;
    }

    @OnTouch(R.id.six)
    public boolean onTouchSix(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                six.setBackgroundResource(R.drawable.pin_password_button_bg_clicked);
                return true;
            case MotionEvent.ACTION_UP:
                six.setBackgroundResource(R.drawable.pin_password_button_bg);
                SetPin(6);
                return true;
        }
        return false;
    }

    @OnTouch(R.id.seven)
    public boolean onTouchSeven(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                seven.setBackgroundResource(R.drawable.pin_password_button_bg_clicked);
                return true;
            case MotionEvent.ACTION_UP:
                seven.setBackgroundResource(R.drawable.pin_password_button_bg);
                SetPin(7);
                return true;
        }
        return false;
    }

    @OnTouch(R.id.eight)
    public boolean onTouchEight(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                eight.setBackgroundResource(R.drawable.pin_password_button_bg_clicked);
                return true;
            case MotionEvent.ACTION_UP:
                eight.setBackgroundResource(R.drawable.pin_password_button_bg);
                SetPin(8);
                return true;
        }
        return false;
    }

    @OnTouch(R.id.nine)
    public boolean onTouchNine(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                nine.setBackgroundResource(R.drawable.pin_password_button_bg_clicked);
                return true;
            case MotionEvent.ACTION_UP:
                nine.setBackgroundResource(R.drawable.pin_password_button_bg);
                SetPin(9);
                return true;
        }
        return false;
    }

    @OnTouch(R.id.zero)
    public boolean onTouchZero(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                zero.setBackgroundResource(R.drawable.pin_password_button_bg_clicked);
                return true;
            case MotionEvent.ACTION_UP:
                zero.setBackgroundResource(R.drawable.pin_password_button_bg);
                SetPin(0);
                return true;
        }
        return false;
    }


    public void SetPin(int pin){
        pin_code = pin_code+""+pin;
        if(pin_code_counter == 0){
            pin_one.setBackgroundResource(R.drawable.enter_pin_circle);
        }else if(pin_code_counter == 1){
            pin_two.setBackgroundResource(R.drawable.enter_pin_circle);
        }else if(pin_code_counter == 2){
            pin_three.setBackgroundResource(R.drawable.enter_pin_circle);
        }else if(pin_code_counter == 3){
            pin_four.setBackgroundResource(R.drawable.enter_pin_circle);
        }
        pin_code_counter++;
    }

    @OnClick(R.id.confirm)
    public void OnConfirmClick(){
        if(pin_code.equalsIgnoreCase(code)){
            // now go to home
            setString("pin_code", code ,PinPasswordConfirmActivity.this);
            GoTo(PinPasswordConfirmActivity.this , FinalStepProfileComplete.class);
            finish();
        }else {
            Toast.makeText(getApplicationContext(), "wrong pin code" , Toast.LENGTH_LONG).show();
            pin_one.setBackgroundResource(R.drawable.pin_password_button_bg);
            pin_two.setBackgroundResource(R.drawable.pin_password_button_bg);
            pin_three.setBackgroundResource(R.drawable.pin_password_button_bg);
            pin_four.setBackgroundResource(R.drawable.pin_password_button_bg);
            pin_code_counter = 0 ;
            pin_code = "" ;
            return;
        }
    }

    @OnClick(R.id.back)
    public void  OnBack(){
        Intent i = new Intent(PinPasswordConfirmActivity.this , PinPasswordActivity.class);
        i.putExtra("id" , "create" );
        startActivity(i);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
