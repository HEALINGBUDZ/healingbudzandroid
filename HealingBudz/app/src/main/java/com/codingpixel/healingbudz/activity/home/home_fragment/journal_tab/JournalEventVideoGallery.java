package com.codingpixel.healingbudz.activity.home.home_fragment.journal_tab;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.codingpixel.healingbudz.DataModel.HealingBudGalleryModel;
import com.codingpixel.healingbudz.R;
import com.codingpixel.healingbudz.adapter.HealingBudGalleryRecylerAdapter;
import com.codingpixel.healingbudz.media_view_Dialog.MediPreview;

import java.util.ArrayList;

import static com.codingpixel.healingbudz.network.model.URL.images_baseurl;
import static com.codingpixel.healingbudz.network.model.URL.videos_baseurl;
import static com.codingpixel.healingbudz.static_function.IntentFunction.GoToHome;
import static com.codingpixel.healingbudz.static_function.UIModification.ChangeStatusBarColor;

//import static com.codingpixel.healingbudz.application.HealingBudApplication.getContext;

@SuppressLint("Registered")
public class JournalEventVideoGallery extends AppCompatActivity implements View.OnClickListener, HealingBudGalleryRecylerAdapter.ItemClickListener {
    ImageView Back, Home;
    RecyclerView Images_Recyler_view;
    public static ArrayList<HealingBudGalleryModel> videos_path = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.healing_bud_gallery);
        ChangeStatusBarColor(JournalEventVideoGallery.this, "#171717");
        Back = (ImageView) findViewById(R.id.back_btn);
        Home = (ImageView) findViewById(R.id.home_btn);
        Back.setOnClickListener(this);
        Home.setOnClickListener(this);
        new Handler().postDelayed(new Runnable() {
            public void run() {
                Images_Recyler_view = (RecyclerView) findViewById(R.id.images_gallery_recyler_view);
                Images_Recyler_view.setVisibility(View.VISIBLE);
                Images_Recyler_view.setLayoutManager(new GridLayoutManager(JournalEventVideoGallery.this, 2));
                final HealingBudGalleryRecylerAdapter recylerAdapter = new HealingBudGalleryRecylerAdapter(JournalEventVideoGallery.this, videos_path);
                recylerAdapter.setClickListener(JournalEventVideoGallery.this);
                Images_Recyler_view.setAdapter(recylerAdapter);
                recylerAdapter.notifyDataSetChanged();
            }
        }, 500);

        ((TextView)findViewById(R.id.q_a_menu_title) ).setText("Event Videos");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_btn:
                finish();
                break;
            case R.id.home_btn:
                GoToHome(JournalEventVideoGallery.this, true);
                finish();
                break;
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        if (videos_path.get(position).getType().equalsIgnoreCase("image")) {
            Intent intent = new Intent(JournalEventVideoGallery.this, MediPreview.class);
            intent.putExtra("path", images_baseurl + videos_path.get(position).getPath());
            intent.putExtra("isvideo", false);
            JournalEventVideoGallery.this.startActivity(intent);
        } else {
            Intent intent = new Intent(JournalEventVideoGallery.this, MediPreview.class);
            intent.putExtra("path", videos_baseurl + videos_path.get(position).getPath());
            intent.putExtra("isvideo", true);
            JournalEventVideoGallery.this.startActivity(intent);
        }
    }

    @Override
    public void onItemDelete(View view, int position) {

    }
}
