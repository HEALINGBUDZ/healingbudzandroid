package com.codingpixel.healingbudz.media_view_Dialog;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.codingpixel.healingbudz.R;

public class MediaViewDialog extends BaseDialogFragment<MediaViewDialog.OnDialogFragmentClickListener> {
    public interface OnDialogFragmentClickListener {
        public void onCrossBtnClink(MediaViewDialog dialog);
    }
    public static MediaViewDialog newInstance(String Path , boolean isVideo) {
        MediaViewDialog frag = new MediaViewDialog();
        Bundle args = new Bundle();
        args.putString("path" , Path);
        args.putBoolean("isVideo" , isVideo);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String path = getArguments().getString("path");
        boolean isVideo = getArguments().getBoolean("isVideo");
        LayoutInflater factory = LayoutInflater.from(getContext());
        final View main_dialog = factory.inflate(R.layout.media_view_dialog_layout, null);
        final AlertDialog dialog = new AlertDialog.Builder(getContext()).create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        WindowManager.LayoutParams wmlp = dialog.getWindow().getAttributes();
        wmlp.gravity = Gravity.CENTER;
        dialog.setView(main_dialog);
        final  ImageView cross = main_dialog.findViewById(R.id.cross_btn);
        cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               dialog.dismiss();
            }
        });

        final ImageView Image_View = main_dialog.findViewById(R.id.image_view);
        VideoView video_View = main_dialog.findViewById(R.id.video_view);
        if(isVideo){
            video_View.setVisibility(View.VISIBLE);
            Image_View.setVisibility(View.GONE);
            video_View.setVideoURI(Uri.parse(path));
            video_View.setMediaController(new MediaController(getContext()));
            video_View.requestFocus();
            video_View.start();
        }else {
            video_View.setVisibility(View.GONE);
            Image_View.setVisibility(View.VISIBLE);
            Glide.with(getContext())
                    .load(path)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .placeholder(R.drawable.ic_user_profile)
//                    .listener(new RequestListener<String, GlideDrawable>() {
//                        @Override
//                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
//                            Log.d("ready", e.getMessage());
//                            return false;
//                        }
//
//                        @Override
//                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
//                            Log.d("ready", model);
//                            Image_View.setImageDrawable(resource);
//                            return false;
//                        }
//                    })
                    .into(Image_View);
        }
        return dialog;
    }


}