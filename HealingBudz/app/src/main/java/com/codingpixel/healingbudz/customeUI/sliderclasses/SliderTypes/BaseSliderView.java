package com.codingpixel.healingbudz.customeUI.sliderclasses.SliderTypes;
//
//import android.content.Context;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.ImageView;
//
////import com.daimajia.slider.library.R;
//import com.bumptech.glide.Glide;
//import com.codingpixel.healingbudz.R;
//import com.squareup.picasso.Callback;
//import com.squareup.picasso.Picasso;
//import com.squareup.picasso.RequestCreator;
////import com.squareup.picasso.Callback;
////import com.squareup.picasso.Picasso;
////import com.squareup.picasso.RequestCreator;
//
//import java.io.File;
//
///**
// * When you want to make your own slider view, you must extends from this class.
// * BaseSliderView provides some useful methods.
// * I provide two example: {@link SliderTypes.DefaultSliderView} and
// * {@link SliderTypes.TextSliderView}
// * if you want to show progressbar, you just need to set a progressbar id as @+id/loading_bar.
// */
//public abstract class BaseSliderView {
//
//    protected Context mContext;
//
//    private Bundle mBundle;
//
//    /**
//     * Error place holder image.
//     */
//    private int mErrorPlaceHolderRes;
//
//    /**
//     * Empty imageView placeholder.
//     */
//    private int mEmptyPlaceHolderRes;
//
//    private String mUrl;
//    private File mFile;
//    private int mRes;
//
//    protected OnSliderClickListener mOnSliderClickListener;
//
//    private boolean mErrorDisappear;
//
//    private ImageLoadListener mLoadListener;
//
//    private String mDescription;
//
//    private Picasso mPicasso;
//
//    /**
//     * Scale type of the image.
//     */
//    private ScaleType mScaleType = ScaleType.Fit;
//
//    public enum ScaleType{
//        CenterCrop, CenterInside, Fit, FitCenterCrop
//    }
//
//    protected BaseSliderView(Context context) {
//        mContext = context;
//    }
//
//    /**
//     * the placeholder image when loading image from url or file.
//     * @param resId Image resource id
//     * @return
//     */
//    public BaseSliderView empty(int resId){
//        mEmptyPlaceHolderRes = resId;
//        return this;
//    }
//
//    /**
//     * determine whether remove the image which failed to download or load from file
//     * @param disappear
//     * @return
//     */
//    public BaseSliderView errorDisappear(boolean disappear){
//        mErrorDisappear = disappear;
//        return this;
//    }
//
//    /**
//     * if you set errorDisappear false, this will set a error placeholder image.
//     * @param resId image resource id
//     * @return
//     */
//    public BaseSliderView error(int resId){
//        mErrorPlaceHolderRes = resId;
//        return this;
//    }
//
//    /**
//     * the description of a slider image.
//     * @param description
//     * @return
//     */
//    public BaseSliderView description(String description){
//        mDescription = description;
//        return this;
//    }
//
//    /**
//     * set a url as a image that preparing to load
//     * @param url
//     * @return
//     */
//    public BaseSliderView image(String url){
//        if(mFile != null || mRes != 0){
//            throw new IllegalStateException("Call multi image function," +
//                    "you only have permission to call it once");
//        }
//        mUrl = url;
//        return this;
//    }
//
//    /**
//     * set a file as a image that will to load
//     * @param file
//     * @return
//     */
//    public BaseSliderView image(File file){
//        if(mUrl != null || mRes != 0){
//            throw new IllegalStateException("Call multi image function," +
//                    "you only have permission to call it once");
//        }
//        mFile = file;
//        return this;
//    }
//
//    public BaseSliderView image(int res){
//        if(mUrl != null || mFile != null){
//            throw new IllegalStateException("Call multi image function," +
//                    "you only have permission to call it once");
//        }
//        mRes = res;
//        return this;
//    }
//
//    /**
//     * lets users add a bundle of additional information
//     * @param bundle
//     * @return
//     */
//    public BaseSliderView bundle(Bundle bundle){
//        mBundle = bundle;
//        return this;
//    }
//
//    public String getUrl(){
//        return mUrl;
//    }
//
//    public boolean isErrorDisappear(){
//        return mErrorDisappear;
//    }
//
//    public int getEmpty(){
//        return mEmptyPlaceHolderRes;
//    }
//
//    public int getError(){
//        return mErrorPlaceHolderRes;
//    }
//
//    public String getDescription(){
//        return mDescription;
//    }
//
//    public Context getContext(){
//        return mContext;
//    }
//
//    /**
//     * set a slider image click listener
//     * @param l
//     * @return
//     */
//    public BaseSliderView setOnSliderClickListener(OnSliderClickListener l){
//        mOnSliderClickListener = l;
//        return this;
//    }
//
//    /**
//     * When you want to implement your own slider view, please call this method in the end in `getView()` method
//     * @param v the whole view
//     * @param targetImageView where to place image
//     */
//    protected void bindEventAndShow(final View v, ImageView targetImageView){
//        final BaseSliderView me = this;
//
//        v.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//            if(mOnSliderClickListener != null){
//                mOnSliderClickListener.onSliderClick(me);
//            }
//            }
//        });
//
//        if (targetImageView == null)
//            return;
//
//        if (mLoadListener != null) {
//            mLoadListener.onStart(me);
//        }
//
//        Picasso p = (mPicasso != null) ? mPicasso : Picasso.with(mContext);
//        RequestCreator rq = null;
//        if(mUrl!=null){
//            rq = p.load(mUrl);
//        }else if(mFile != null){
//            rq = p.load(mFile);
//        }else if(mRes != 0){
//            rq = p.load(mRes);
//        }else{
//            return;
//        }
//
//        if(rq == null){
//            return;
//        }
//
//        if(getEmpty() != 0){
//            rq.placeholder(getEmpty());
//        }
//
//        if(getError() != 0){
//            rq.error(getError());
//        }
//
//        switch (mScaleType){
//            case Fit:
//                rq.fit();
//                break;
//            case CenterCrop:
//                rq.fit().centerCrop();
//                break;
//            case CenterInside:
//                rq.fit().centerInside();
//                break;
//        }
//
//        rq.into(targetImageView,new Callback() {
//            @Override
//            public void onSuccess() {
//                if(v.findViewById(R.id.loading_bar) != null){
//                    v.findViewById(R.id.loading_bar).setVisibility(View.INVISIBLE);
//                }
//            }
//
//            @Override
//            public void onError() {
//                if(mLoadListener != null){
//                    mLoadListener.onEnd(false,me);
//                }
//                if(v.findViewById(R.id.loading_bar) != null){
//                    v.findViewById(R.id.loading_bar).setVisibility(View.INVISIBLE);
//                }
//            }
//        });
//   }
//
//
//
//    public BaseSliderView setScaleType(ScaleType type){
//        mScaleType = type;
//        return this;
//    }
//
//    public ScaleType getScaleType(){
//        return mScaleType;
//    }
//
//    /**
//     * the extended class have to implement getView(), which is called by the adapter,
//     * every extended class response to render their own view.
//     * @return
//     */
//    public abstract View getView();
//
//    /**
//     * set a listener to get a message , if load error.
//     * @param l
//     */
//    public void setOnImageLoadListener(ImageLoadListener l){
//        mLoadListener = l;
//    }
//
//    public interface OnSliderClickListener {
//        public void onSliderClick(BaseSliderView slider);
//    }
//
//    /**
//     * when you have some extra information, please put it in this bundle.
//     * @return
//     */
//    public Bundle getBundle(){
//        return mBundle;
//    }
//
//    public interface ImageLoadListener{
//        public void onStart(BaseSliderView target);
//        public void onEnd(boolean result, BaseSliderView target);
//    }
//
//    /**
//     * Get the last instance set via setPicasso(), or null if no user provided instance was set
//     *
//     * @return The current user-provided Picasso instance, or null if none
//     */
//    public Picasso getPicasso() {
//        return mPicasso;
//    }
//
//    /**
//     * Provide a Picasso instance to use when loading pictures, this is useful if you have a
//     * particular HTTP cache you would like to share.
//     *
//     * @param picasso The Picasso instance to use, may be null to let the system use the default
//     *                instance
//     */
//    public void setPicasso(Picasso picasso) {
//        mPicasso = picasso;
//    }
//}


import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.codingpixel.healingbudz.R;
import com.codingpixel.healingbudz.customeUI.sliderclasses.SliderTypes.glideslider.glideclasses.Glide;
import com.codingpixel.healingbudz.customeUI.sliderclasses.SliderTypes.glideslider.glideclasses.RequestBuilder;
import com.codingpixel.healingbudz.customeUI.sliderclasses.SliderTypes.glideslider.glideclasses.load.DataSource;
import com.codingpixel.healingbudz.customeUI.sliderclasses.SliderTypes.glideslider.glideclasses.load.engine.GlideException;
import com.codingpixel.healingbudz.customeUI.sliderclasses.SliderTypes.glideslider.glideclasses.request.RequestListener;
import com.codingpixel.healingbudz.customeUI.sliderclasses.SliderTypes.glideslider.glideclasses.request.RequestOptions;
import com.codingpixel.healingbudz.customeUI.sliderclasses.SliderTypes.glideslider.glideclasses.request.target.Target;

import java.io.File;

//import com.glide.slider.library.R;

/**
 * When you want to make your own slider view, you must extends from this class.
 * BaseSliderView provides some useful methods.
 * I provide two example: {@link com.glide.slider.library.SliderTypes.DefaultSliderView} and
 * {@link com.glide.slider.library.SliderTypes.TextSliderView}
 */
public abstract class BaseSliderView {

    public Context mContext;

    private Bundle mBundle;

    private String mUrl;
    private File mFile;
    private int mRes;

    public com.codingpixel.healingbudz.customeUI.sliderclasses.SliderTypes.BaseSliderView.OnSliderClickListener mOnSliderClickListener;

    public com.codingpixel.healingbudz.customeUI.sliderclasses.SliderTypes.BaseSliderView.ImageLoadListener mLoadListener;

    private String mDescription;

    private RequestOptions mRequestOptions;

    private boolean isProgressBarVisible = false;

    private int mBackgroundColor = Color.BLACK;

    /**
     * Ctor
     *
     * @param context
     */
    public BaseSliderView(Context context) {
        mContext = context;
    }

    /**
     * the description of a slider image.
     *
     * @param description
     * @return
     */
    public com.codingpixel.healingbudz.customeUI.sliderclasses.SliderTypes.BaseSliderView description(String description) {
        mDescription = description;
        return this;
    }

    /**
     * set a url as a image that preparing to load
     *
     * @param url
     * @return
     */
    public com.codingpixel.healingbudz.customeUI.sliderclasses.SliderTypes.BaseSliderView image(String url) {
        if (mFile != null || mRes != 0) {
            throw new IllegalStateException("Call multi image function," +
                    "you only have permission to call it once");
        }
        mUrl = url;
        return this;
    }

    /**
     * set a file as a image that will to load
     *
     * @param file
     * @return
     */
    public com.codingpixel.healingbudz.customeUI.sliderclasses.SliderTypes.BaseSliderView image(File file) {
        if (mUrl != null || mRes != 0) {
            throw new IllegalStateException("Call multi image function," +
                    "you only have permission to call it once");
        }
        mFile = file;
        return this;
    }

    public com.codingpixel.healingbudz.customeUI.sliderclasses.SliderTypes.BaseSliderView image(int res) {
        if (mUrl != null || mFile != null) {
            throw new IllegalStateException("Call multi image function," +
                    "you only have permission to call it once");
        }
        mRes = res;
        return this;
    }

    /**
     * lets users add a bundle of additional information
     *
     * @param bundle
     * @return
     */
    public com.codingpixel.healingbudz.customeUI.sliderclasses.SliderTypes.BaseSliderView bundle(Bundle bundle) {
        mBundle = bundle;
        return this;
    }

    public String getUrl() {
        return mUrl;
    }

    public String getDescription() {
        return mDescription;
    }

    public Context getContext() {
        return mContext;
    }

    /**
     * set a slider image click listener
     *
     * @param l
     * @return
     */
    public com.codingpixel.healingbudz.customeUI.sliderclasses.SliderTypes.BaseSliderView setOnSliderClickListener(com.codingpixel.healingbudz.customeUI.sliderclasses.SliderTypes.BaseSliderView.OnSliderClickListener l) {
        mOnSliderClickListener = l;
        return this;
    }

    /**
     * set Glide RequestOption
     *
     * @param requestOption
     * @return self
     */
    public com.codingpixel.healingbudz.customeUI.sliderclasses.SliderTypes.BaseSliderView setRequestOption(RequestOptions requestOption) {
        mRequestOptions = requestOption;
        return this;
    }

    /**
     * set Progressbar visible or gone
     *
     * @param isVisible
     */
    public com.codingpixel.healingbudz.customeUI.sliderclasses.SliderTypes.BaseSliderView setProgressBarVisible(boolean isVisible) {
        isProgressBarVisible = isVisible;
        return this;
    }

    /**
     * set Background Color
     *
     * @param color
     */
    public com.codingpixel.healingbudz.customeUI.sliderclasses.SliderTypes.BaseSliderView setBackgroundColor(int color) {
        mBackgroundColor = color;
        return this;
    }

    /**
     * When you want to implement your own slider view, please call this method in the end in `getView()` method
     *
     * @param v               the whole view
     * @param targetImageView where to place image
     */
    protected void bindEventAndShow(final View v, ImageView targetImageView) {
        final com.codingpixel.healingbudz.customeUI.sliderclasses.SliderTypes.BaseSliderView me = this;

//        try {
//            v.findViewById(R.id.loading_bar).setBackgroundColor(mBackgroundColor);
//        } catch (NullPointerException e) {
//            e.printStackTrace();
//        }

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnSliderClickListener != null) {
                    mOnSliderClickListener.onSliderClick(me);
                }
            }
        });

        if (targetImageView == null)
            return;

        if (mLoadListener != null) {
            mLoadListener.onStart(me);
        }

        final ProgressBar mProgressBar = v.findViewById(R.id.loading_bar);
        if (isProgressBarVisible) {
            mProgressBar.setVisibility(View.VISIBLE);
        } else {
            mProgressBar.setVisibility(View.GONE);
        }

        Object imageToLoad = null;
        if (mUrl != null) {
            imageToLoad = mUrl;
        } else if (mFile != null) {
            imageToLoad = mFile;
        } else if (mRes != 0) {
            imageToLoad = mRes;
        }

        RequestBuilder<Drawable> requestBuilder = Glide.with(mContext).as(Drawable.class);

        if (imageToLoad != null) {
            if (mRequestOptions != null) {
                requestBuilder.load(imageToLoad)
                        .apply(mRequestOptions)
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e,
                                                        Object model,
                                                        Target<Drawable> target,
                                                        boolean isFirstResource) {
                                mProgressBar.setVisibility(View.GONE);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource,
                                                           Object model,
                                                           Target<Drawable> target,
                                                           DataSource dataSource,
                                                           boolean isFirstResource) {
                                mProgressBar.setVisibility(View.GONE);
                                return false;
                            }
                        }).into(targetImageView);
            } else {
                requestBuilder.load(imageToLoad)
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e,
                                                        Object model,
                                                        Target<Drawable> target,
                                                        boolean isFirstResource) {
                                mProgressBar.setVisibility(View.GONE);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource,
                                                           Object model,
                                                           Target<Drawable> target,
                                                           DataSource dataSource,
                                                           boolean isFirstResource) {
                                mProgressBar.setVisibility(View.GONE);
                                return false;
                            }
                        }).into(targetImageView);
            }
        }
    }

    /**
     * the extended class have to implement getView(), which is called by the adapter,
     * every extended class response to render their own view.
     *
     * @return
     */
    public abstract View getView();

    /**
     * set a listener to get a message , if load error.
     *
     * @param l
     */
    public void setOnImageLoadListener(com.codingpixel.healingbudz.customeUI.sliderclasses.SliderTypes.BaseSliderView.ImageLoadListener l) {
        mLoadListener = l;
    }

    public interface OnSliderClickListener {
        public void onSliderClick(com.codingpixel.healingbudz.customeUI.sliderclasses.SliderTypes.BaseSliderView slider);
    }

    /**
     * when you have some extra information, please put it in this bundle.
     *
     * @return
     */
    public Bundle getBundle() {
        return mBundle;
    }

    public interface ImageLoadListener {
        public void onStart(com.codingpixel.healingbudz.customeUI.sliderclasses.SliderTypes.BaseSliderView target);

        public void onEnd(boolean result, com.codingpixel.healingbudz.customeUI.sliderclasses.SliderTypes.BaseSliderView target);
    }
}