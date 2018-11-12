package com.codingpixel.healingbudz.adapter;
/*
 * Created by M_Muzammil Sharif on 06-Mar-18.
 */

import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.codingpixel.healingbudz.R;
import com.codingpixel.healingbudz.Utilities.Utility;
import com.codingpixel.healingbudz.customeUI.CircularImageView;
import com.codingpixel.healingbudz.customeUI.ZoomProblemViewPager;
import com.codingpixel.healingbudz.interfaces.RecyclerViewItemClickListener;
import com.codingpixel.healingbudz.network.BudzFeedModel.GetAllPost.File;
import com.codingpixel.healingbudz.network.model.URL;
import com.github.chrisbanes.photoview.PhotoView;

import java.util.List;

import static android.widget.ImageView.ScaleType.FIT_CENTER;

public class BudzFeedMainPostMediaPagerAdapter extends PagerAdapter {
    private List<File> files;
    private RecyclerViewItemClickListener clickListener = null;

    public BudzFeedMainPostMediaPagerAdapter(List<File> files, @NonNull RecyclerViewItemClickListener clickListener) {
        this.files = files;
        this.clickListener = clickListener;
    }

    public RecyclerViewItemClickListener getClickListener() {
        return clickListener;
    }

    public void setClickListener(RecyclerViewItemClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @Override
    public int getCount() {
        if (files == null) {
            return 0;
        }
        return files.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        switch (files.get(position).getFileType()) {
            case 0: {
                if (container instanceof ZoomProblemViewPager) {
                    ViewPager.LayoutParams params = new ViewPager.LayoutParams();
                    params.width = ViewPager.LayoutParams.MATCH_PARENT;
                    params.height = ViewPager.LayoutParams.MATCH_PARENT;
                    params.gravity = Gravity.CENTER;

                    PhotoView photoView = new PhotoView(container.getContext());
                    photoView.setLayoutParams(params);
                    photoView.getAttacher().setMinimumScale(1f);
                    photoView.getAttacher().setMediumScale(2f);
                    photoView.getAttacher().setMaximumScale(3f);
                    photoView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                    photoView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (clickListener != null) {
                                clickListener.onItemClick(files.get(position), position, 0);
                            }
                        }
                    });

                    Glide.with(container.getContext()).load(URL.images_baseurl + files.get(position).getFile()).
//                            asBitmap().
                            placeholder(R.drawable.place_holder_wall).error(R.drawable.noimage).
                            fitCenter().
                            diskCacheStrategy(DiskCacheStrategy.SOURCE).into(photoView);

                    ((ViewPager) container).addView(photoView);
                    return photoView;
                } else {
                    ViewPager.LayoutParams params = new ViewPager.LayoutParams();
                    params.width = ViewPager.LayoutParams.WRAP_CONTENT;
                    params.height = ViewPager.LayoutParams.WRAP_CONTENT;
                    ImageView imageView = new ImageView(container.getContext());
                    imageView.setLayoutParams(params);
//                    imageView.setCornerRadiusDP(0f);
                    imageView.setScaleType(FIT_CENTER);
                    Glide.with(container.getContext()).load(URL.images_baseurl + files.get(position).getFile()).
//                            asBitmap().
                            placeholder(R.drawable.place_holder_wall).error(R.drawable.noimage).
                            fitCenter().
                            diskCacheStrategy(DiskCacheStrategy.SOURCE).into(imageView);
                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (clickListener != null) {
                                clickListener.onItemClick(files.get(position), position, 0);
                            }
                        }
                    });
                    ((ViewPager) container).addView(imageView);
                    return imageView;
                }
            }
            default: {
                RelativeLayout layout = new RelativeLayout(container.getContext());
                ViewPager.LayoutParams params = new ViewPager.LayoutParams();
                params.width = ViewPager.LayoutParams.WRAP_CONTENT;
                params.height = ViewPager.LayoutParams.WRAP_CONTENT;
                layout.setLayoutParams(params);

                //view poster image
                ImageView imageView = new ImageView(container.getContext());
                imageView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                imageView.setLayoutParams(params);
//                imageView.setCornerRadiusDP(0f);
                imageView.setScaleType(FIT_CENTER);
                Glide.with(container.getContext()).load(URL.images_baseurl + files.get(position).getPoster()).
//                        asBitmap().
                        placeholder(R.drawable.place_holder_wall).error(R.drawable.noimage).
                        fitCenter().
                        diskCacheStrategy(DiskCacheStrategy.SOURCE).into(imageView);
                layout.addView(imageView);

                //view play icon
                CircularImageView icon = new CircularImageView(container.getContext());
                RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(Utility.convertDpToPixel(40f, container.getContext()), Utility.convertDpToPixel(40f, container.getContext()));
                params1.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
                params1.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
                params1.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
                icon.setLayoutParams(params1);
                icon.setImageResource(R.drawable.ic_video_play_icon);
                layout.addView(icon);

                layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (clickListener != null) {
                            clickListener.onItemClick(files.get(position), position, 0);
                        }
                    }
                });

                ((ViewPager) container).addView(layout);

                return layout;
            }
        }
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        switch (files.get(position).getFileType()) {
            case 0: {
                if (container instanceof ZoomProblemViewPager) {
                    ((ViewPager) container).removeView((PhotoView) object);
                } else {
                    ((ViewPager) container).removeView((ImageView) object);
                }
            }
            break;
            default: {
                ((ViewPager) container).removeView((RelativeLayout) object);
            }
        }
    }

    public File getCurrentItem(int pos) {
        return files.get(pos);

    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
        //super.restoreState(null, null);
    }
}
