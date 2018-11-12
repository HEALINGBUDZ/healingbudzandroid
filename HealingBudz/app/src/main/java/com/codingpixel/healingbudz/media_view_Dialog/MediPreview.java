package com.codingpixel.healingbudz.media_view_Dialog;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.format.Formatter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.codingpixel.healingbudz.DataModel.QuestionAnswersDataModel;
import com.codingpixel.healingbudz.R;
import com.codingpixel.healingbudz.Utilities.FileUtils;
import com.codingpixel.healingbudz.Utilities.PermissionHandler;
import com.codingpixel.healingbudz.customeUI.TouchImageView;
import com.codingpixel.healingbudz.network.model.URL;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.YouTubePlayerView;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.listeners.YouTubePlayerInitListener;

import java.util.ArrayList;

import tcking.github.com.giraffeplayer.GiraffePlayer;
import tv.danmaku.ijk.media.player.IMediaPlayer;

import static android.view.View.GONE;
import static com.codingpixel.healingbudz.network.model.URL.images_baseurl;
import static com.codingpixel.healingbudz.static_function.UIModification.ChangeStatusBarColor;

public class MediPreview extends AppCompatActivity implements ViewPager.OnPageChangeListener {
    TouchImageView Image_View;
    ImageView Back_Btn, download_btn;
    String Path;
    boolean isVideo = false;
    boolean isVideoYoutube = false;
    GiraffePlayer player;
    ProgressBar Loading_spiner;
    ArrayList<QuestionAnswersDataModel.Attachment> attachments;
    ArrayList<QuestionAnswersDataModel.Attachment> attachmentsUse;
    LinearLayout image_showing_view;
    ViewPager mViewPager;

    ImagePaggerAdapter imagePaggerAdapter;
    YouTubePlayerView youtubePlayerView;
    String display;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medi_preview);
        player = new GiraffePlayer(this);
        download_btn = (ImageView) findViewById(R.id.download_btn);
        ChangeStatusBarColor(MediPreview.this, "#171717");
        image_showing_view = (LinearLayout) findViewById(R.id.image_showing_view);
        mViewPager = (ViewPager) findViewById(R.id.pager);
        Path = getIntent().getExtras().getString("path");
        display = getIntent().getExtras().getString("display", "");
        isVideo = getIntent().getExtras().getBoolean("isvideo");
        if (getIntent().getExtras().getBoolean("isvideoyoutube", false)) {
            isVideoYoutube = true;
        } else {
            isVideoYoutube = false;
        }
        Image_View = (TouchImageView) findViewById(R.id.image_view);
        Loading_spiner = (ProgressBar) findViewById(R.id.loading_spinner);
        player.onComplete(new Runnable() {
            @Override
            public void run() {
                player.pause();
                //callback when video is finish
            }
        }).onInfo(new GiraffePlayer.OnInfoListener() {
            @Override
            public void onInfo(int what, int extra) {
                switch (what) {
                    case IMediaPlayer.MEDIA_INFO_BUFFERING_START:
                        //do something when buffering start
                        break;
                    case IMediaPlayer.MEDIA_INFO_BUFFERING_END:
                        //do something when buffering end
                        break;
                    case IMediaPlayer.MEDIA_INFO_NETWORK_BANDWIDTH:
                        //download speed
                        Log.d("speed", Formatter.formatFileSize(getApplicationContext(), extra) + "/s");
                        break;
                    case IMediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START:
                        //do something when video rendering
                        break;
                }
            }
        }).onError(new GiraffePlayer.OnErrorListener() {
            @Override
            public void onError(int what, int extra) {
            }
        });
        youtubePlayerView = findViewById(R.id.youtube_player_view);
        getLifecycle().addObserver(youtubePlayerView);
        if (isVideoYoutube) {
            Loading_spiner.setVisibility(View.VISIBLE);
            youtubePlayerView.setVisibility(View.VISIBLE);
            youtubePlayerView.initialize(new YouTubePlayerInitListener() {
                @Override
                public void onInitSuccess(@NonNull final YouTubePlayer initializedYouTubePlayer) {

                    initializedYouTubePlayer.addListener(new AbstractYouTubePlayerListener() {
                        @Override
                        public void onReady() {
                            Loading_spiner.setVisibility(View.GONE);
                            String videoId;
                            if (Path.contains("=")) {
                                videoId = Path.split("=")[1];

                            } else {
                                Uri uri = Uri.parse(Path);
                                videoId = uri.getLastPathSegment();
                            }
                            initializedYouTubePlayer.loadVideo(videoId, 0);
                        }

                    });
                }
            }, true);
            download_btn.setVisibility(GONE);
            Image_View.setVisibility(View.GONE);

            player.hide(true);
        } else {
            youtubePlayerView.setVisibility(View.GONE);
            //
            if (isVideo) {
                download_btn.setVisibility(GONE);
                Image_View.setVisibility(View.GONE);
                Loading_spiner.setVisibility(View.GONE);
                player.play(Path);
            } else {
                player.hide(true);
                download_btn.setVisibility(View.VISIBLE);
                Loading_spiner.setVisibility(View.VISIBLE);
                Image_View.setVisibility(View.VISIBLE);
                Glide.with(this)
                        .load(Path)
                        .placeholder(R.drawable.image_plaecholder_pr)
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .listener(new RequestListener<String, GlideDrawable>() {
                            @Override
                            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                Loading_spiner.setVisibility(GONE);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                Loading_spiner.setVisibility(GONE);
                                return false;
                            }
                        })
//                        .listener(new RequestListener<String, GlideDrawable>() {
//                            @Override
//                            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
//                                Loading_spiner.setVisibility(View.GONE);
//                                return false;
//                            }
//
//                            @Override
//                            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
////                                Image_View.setImageDrawable(resource);
//                                Loading_spiner.setVisibility(View.GONE);
//                                return false;
//                            }
//                        })
                        .into(Image_View);
                if (getIntent().getExtras().containsKey("Attachment_array")) {
                    QuestionAnswersDataModel.Attachment at1 = null, at2 = null, at3 = null;

                    attachments = (ArrayList<QuestionAnswersDataModel.Attachment>) getIntent().getExtras().getSerializable("Attachment_array");
                    attachmentsUse = new ArrayList<>();
                    attachmentsUse.addAll(attachments);
                    if (attachments.size() > 1) {
                        for (int i = 0; i < attachments.size(); i++) {
                            if (attachments.get(i).getUpload_path().contains(".mp4")) {
                                attachmentsUse.remove(i);
                            }
                        }
                        if (attachmentsUse.size() > 0) {
                            download_btn.setVisibility(View.VISIBLE);
                            download_btn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    PermissionHandler.checkPermission(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, v.getContext(), new PermissionHandler.CheckPermissionResponse() {
                                        @Override
                                        public void permissionGranted() {
                                            FileUtils.StartDownload(urlPath, fileName, MediPreview.this);
                                        }

                                        @Override
                                        public void showNeededPermissionDialog() {

                                        }

                                        @Override
                                        public void requestPermission() {

                                        }
                                    });

                                }
                            });
                            download_btn.setVisibility(GONE);
                            Image_View.setVisibility(View.GONE);
                            Loading_spiner.setVisibility(View.GONE);
                            image_showing_view.setVisibility(View.VISIBLE);
                            imagePaggerAdapter = new ImagePaggerAdapter(this, attachmentsUse);
                            mViewPager.addOnPageChangeListener(this);
                            mViewPager.setAdapter(imagePaggerAdapter);
                            for (int j = 0; j < attachmentsUse.size(); j++) {
                                if (display.equalsIgnoreCase(attachmentsUse.get(j).getUpload_path())) {
                                    mViewPager.setCurrentItem(j);
                                }
                            }
                        } else {
                            download_btn.setVisibility(View.VISIBLE);
                            download_btn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    PermissionHandler.checkPermission(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, v.getContext(), new PermissionHandler.CheckPermissionResponse() {
                                        @Override
                                        public void permissionGranted() {
                                            FileUtils.StartDownload(Path, Path.replace(URL.images_baseurl, "").replace("/", ""), MediPreview.this);
                                        }

                                        @Override
                                        public void showNeededPermissionDialog() {

                                        }

                                        @Override
                                        public void requestPermission() {

                                        }
                                    });

                                }
                            });
                            image_showing_view.setVisibility(View.GONE);
                            Loading_spiner.setVisibility(View.VISIBLE);
                            Image_View.setVisibility(View.VISIBLE);
                            Glide.with(this)
                                    .load(Path)
                                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                                    .listener(new RequestListener<String, GlideDrawable>() {
                                        @Override
                                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                            Loading_spiner.setVisibility(View.GONE);
                                            return false;
                                        }

                                        @Override
                                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
//                                            Image_View.setImageDrawable(resource);
                                            Loading_spiner.setVisibility(View.GONE);
                                            return false;
                                        }
                                    })
                                    .into(Image_View);
                        }
                    } else {
                        download_btn.setVisibility(View.VISIBLE);
                        download_btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                PermissionHandler.checkPermission(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, v.getContext(), new PermissionHandler.CheckPermissionResponse() {
                                    @Override
                                    public void permissionGranted() {
                                        FileUtils.StartDownload(Path, Path.replace(URL.images_baseurl, "").replace("/", ""), MediPreview.this);
                                    }

                                    @Override
                                    public void showNeededPermissionDialog() {

                                    }

                                    @Override
                                    public void requestPermission() {

                                    }
                                });

                            }
                        });
                    }

                } else {
                    download_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            PermissionHandler.checkPermission(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, v.getContext(), new PermissionHandler.CheckPermissionResponse() {
                                @Override
                                public void permissionGranted() {
                                    FileUtils.StartDownload(Path, Path.replace(URL.images_baseurl, "").replace("/", ""), MediPreview.this);
                                }

                                @Override
                                public void showNeededPermissionDialog() {

                                }

                                @Override
                                public void requestPermission() {

                                }
                            });

                        }
                    });
                }
            }
        }


        Back_Btn = (ImageView) findViewById(R.id.back_btn);

        Back_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }

    @Override
    protected void onPause() {
        super.onPause();
        if (player != null) {
            player.onPause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (player != null) {
            player.onResume();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (player != null) {
            player.onDestroy();
        }
        if (youtubePlayerView != null)
            youtubePlayerView.release();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (player != null) {
            player.onConfigurationChanged(newConfig);
        }
    }

    String urlPath = "", fileName = "";

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        download_btn.setVisibility(View.VISIBLE);
        urlPath = URL.images_baseurl + attachmentsUse.get(position).getUpload_path();
        fileName = attachmentsUse.get(position).getUpload_path();
    }

    @Override
    public void onPageSelected(int position) {
        download_btn.setVisibility(View.VISIBLE);
        urlPath = URL.images_baseurl + attachmentsUse.get(position).getUpload_path();
        fileName = attachmentsUse.get(position).getUpload_path();
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public class ImagePaggerAdapter extends PagerAdapter {

        ImageView Back_Img, Next_Img, download_image;
        TextView Image_Counter;
        Activity mContext;
        ArrayList<QuestionAnswersDataModel.Attachment> mData = new ArrayList<>();
        LayoutInflater mLayoutInflater;
        ProgressBar Loading_spiner;
        com.codingpixel.healingbudz.customeUI.TouchImageView imageView;
        public GiraffePlayer player;

        public ImagePaggerAdapter(Activity context, ArrayList<QuestionAnswersDataModel.Attachment> mData) {
            mContext = context;
            mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            this.mData = mData;
        }

        @Override
        public int getCount() {
            return mData.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return object == view;
        }

        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            View itemView = mLayoutInflater.inflate(R.layout.pager_layout_main_gallery, container, false);
            imageView = itemView.findViewById(R.id.img);
            download_image = itemView.findViewById(R.id.download_image);
            Loading_spiner = (ProgressBar) itemView.findViewById(R.id.loading_spinner);
            Loading_spiner.setVisibility(View.GONE);
            download_image.setVisibility(GONE);
            download_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FileUtils.StartDownload(images_baseurl + mData.get(position).getUpload_path(), mData.get(position).getUpload_path().replace("/", ""), MediPreview.this);
                }
            });
            final RelativeLayout rl = itemView.findViewById(R.id.app_video_box);
            imagePaggerAdapter.player = new GiraffePlayer((Activity) itemView.getContext(), itemView);
            imagePaggerAdapter.player.hide(true);
            rl.setVisibility(GONE);
            imageView.setVisibility(View.VISIBLE);
            Loading_spiner.setVisibility(View.GONE);
            imagePaggerAdapter.player.hide(true);
            rl.setVisibility(GONE);
//                vid.setVisibility(View.GONE);
            Glide.with(MediPreview.this)
                    .load(images_baseurl + mData.get(position).getUpload_path())
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .placeholder(R.drawable.image_plaecholder_bl)
                    .error(R.drawable.noimage)
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            Loading_spiner.setVisibility(GONE);
                            rl.setVisibility(GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            Log.d("ready", model);
//                            imageView.setImageDrawable(resource);
                            Loading_spiner.setVisibility(GONE);
                            rl.setVisibility(GONE);
                            return false;
                        }
                    }).into(imageView);
//                Loading_spiner.setVisibility(GONE);
            container.addView(itemView);
            return itemView;

        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }


    }

    @Override
    public void onBackPressed() {
        if (player != null && player.onBackPressed()) {
            return;
        }
        super.onBackPressed();
    }
}
