package com.codingpixel.healingbudz.adapter;

import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.codingpixel.healingbudz.R;
import com.codingpixel.healingbudz.Utilities.Utility;
import com.codingpixel.healingbudz.interfaces.RecyclerViewItemClickListener;
import com.codingpixel.healingbudz.network.model.URL;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/*
 * Created by M_Muzammil Sharif on 16-Mar-18.
 */

public class BudzFeedNewPostMediaViewPagerAdapter extends PagerAdapter {

    @Override
    public Parcelable saveState() {
        return null;
    }

    public interface UpdateCallBack {
        public void onUpdate(int pos);
    }

    private RecyclerViewItemClickListener clickListener;
    private UpdateCallBack callBack;
    private List<NewPostMedia> medias;

    public BudzFeedNewPostMediaViewPagerAdapter(List<NewPostMedia> medias, UpdateCallBack callBack, RecyclerViewItemClickListener listener) {
        this.clickListener = listener;
        this.medias = medias;
        this.callBack = callBack;
    }

    public List<NewPostMedia> getMedias() {
        if (medias != null)
            return medias;
        else {
            return new ArrayList<>();
        }
    }

    public void addMedia(NewPostMedia media) {
        if (medias == null) {
            medias = new ArrayList<>();
        }
        medias.add(media);
        notifyDataSetChanged();
        callBack.onUpdate(-1);
    }

    @Override
    public int getItemPosition(Object object) {
        NewPostMedia media = (NewPostMedia) ((FrameLayout) object).getTag();
        if (media != null && getCount() != 0) {
            return medias.indexOf(media);
        } else {
            return POSITION_NONE;
        }
    }

    private void removeMedia(NewPostMedia media) {
        if (medias == null) {
            return;
        } else {
            List<NewPostMedia> media1 = medias;
            medias = null;
            notifyDataSetChanged();
            medias = media1;
        }
        medias.remove(media);
        callBack.onUpdate(-2);
        notifyDataSetChanged();
    }

    public void setMedias(List<NewPostMedia> medias) {
        this.medias = medias;
        callBack.onUpdate(-2);
        notifyDataSetChanged();
    }

    private boolean isCancelAble = true;

    public void setMediasFromFile(List<com.codingpixel.healingbudz.network.BudzFeedModel.GetAllPost.File> medias, boolean isCanclelAble) {
        setMediasFromFile(medias);
        this.isCancelAble = isCanclelAble;
    }

    public void setMediasFromFile(List<com.codingpixel.healingbudz.network.BudzFeedModel.GetAllPost.File> medias) {
        this.medias = new ArrayList<>();
        for (com.codingpixel.healingbudz.network.BudzFeedModel.GetAllPost.File file : medias) {
            NewPostMedia postMedia = new NewPostMedia(file.getFile(), file.getType().trim().equalsIgnoreCase("video"));
            postMedia.url = file.getFile();
            postMedia.poster = file.getPoster();
            this.medias.add(postMedia);
        }
        callBack.onUpdate(-2);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (medias == null) {
            return 0;
        }
        return medias.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        NewPostMedia media = medias.get(position);
        FrameLayout layout = new FrameLayout(container.getContext());
        ViewPager.LayoutParams params = new ViewPager.LayoutParams();
        params.height = ViewPager.LayoutParams.MATCH_PARENT;
        params.width = ViewPager.LayoutParams.MATCH_PARENT;
        layout.setLayoutParams(params);

        ImageView imageView = new ImageView(container.getContext());
        imageView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
//        imageView.setCornerRadiusDP(5);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        //imageView.setImageBitmap(medias.get(position).getImg());
        if(media.isVideo()){
            Glide.with(container.getContext()).load(URL.images_baseurl + (media.isVideo() ? media.poster : media.url)).
                    placeholder(R.drawable.place_holder_wall).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(imageView);
            layout.addView(imageView);
        }else {
            if(media.url.contains("com.healingbudz.android")){
                Glide.with(container.getContext()).load((media.isVideo() ? media.poster : media.url)).
                        placeholder(R.drawable.place_holder_wall).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(imageView);
                layout.addView(imageView);
            }else {
                Glide.with(container.getContext()).load(URL.images_baseurl + (media.isVideo() ? media.poster : media.url)).
                        placeholder(R.drawable.place_holder_wall).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(imageView);
                layout.addView(imageView);
            }
        }


        int w = Utility.convertDpToPixel(45, container.getContext());

        if (media.isVideo()) {
            ImageView icon = new ImageView(container.getContext());
            icon.setLayoutParams(new FrameLayout.LayoutParams(w, w, Gravity.CENTER));
            icon.setImageResource(R.drawable.ic_video_play_icon);
            layout.addView(icon);
        } /*else {
            Glide.with(container.getContext()).load(new File(medias.get(position).getPath())).into(imageView);
        }*/
        int wc = Utility.convertDpToPixel(22, container.getContext());
        if (isCancelAble) {
            ImageView cross = new ImageView(container.getContext());
            FrameLayout.LayoutParams abc = new FrameLayout.LayoutParams(wc, wc, Gravity.END | Gravity.TOP | Gravity.RIGHT);
            abc.setMargins(0
                    , Utility.convertDpToPixel(5, container.getContext())
                    , Utility.convertDpToPixel(5, container.getContext())
                    , Utility.convertDpToPixel(22, container.getContext()));
            cross.setLayoutParams(abc);

            cross.setImageResource(R.drawable.ic_close_wall);
            cross.setTag(media);
            cross.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    removeMedia((NewPostMedia) view.getTag());
                }
            });

            layout.addView(cross);
        }
        ((ViewPager) container).addView(layout);
        layout.setTag(media);

        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickListener.onItemClick(view.getTag(), 0, 0);
            }
        });

        return layout;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((FrameLayout) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    public static class NewPostMedia implements Serializable {
        public String thumb;
        public String ratio;

        public NewPostMedia(String path, boolean video) {
            this.path = path;
            this.video = video;
        }

        public String url;
        public String poster;
        private String path;
        private boolean video;

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public boolean isVideo() {
            return video;
        }

        public void setVideo(boolean video) {
            this.video = video;
        }
    }
}
