package com.codingpixel.healingbudz.activity.Registration;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.codingpixel.healingbudz.activity.Registration.dialogue.RemindarDialog;
import com.codingpixel.healingbudz.R;

import static com.codingpixel.healingbudz.static_function.IntentFunction.GoToHome;
import static com.codingpixel.healingbudz.static_function.UIModification.FullScreen;

public class AddYourExpertAreasActivity extends AppCompatActivity implements RemindarDialog.OnDialogFragmentClickListener {
    Button Remind_me_later, Add_Expert_area;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_your_expert_areas);
        FullScreen(this);

        Remind_me_later = (Button) findViewById(R.id.remind_me_later);
        Add_Expert_area = (Button) findViewById(R.id.add_epert_area);

        Remind_me_later.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RemindarDialog introHowToAddQuestionDialog = RemindarDialog.newInstance(AddYourExpertAreasActivity.this);
                introHowToAddQuestionDialog.show(AddYourExpertAreasActivity.this.getSupportFragmentManager(), "pd");

            }
        });

        Add_Expert_area.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddYourExpertAreasActivity.this, AddExpertiseQuestionActivity.class);
                intent.putExtra("isEdit", false);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void onCrossClick(RemindarDialog dialog) {

    }

    @Override
    public void onDoneClick(RemindarDialog dialog) {
        GoToHome(AddYourExpertAreasActivity.this, false);
        finish();
    }
}
