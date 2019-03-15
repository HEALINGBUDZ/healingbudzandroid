package com.codingpixel.healingbudz.customeUI;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.ColorInt;
import android.text.Html;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.codingpixel.healingbudz.DataModel.HomeQAfragmentDataModel;
import com.codingpixel.healingbudz.R;
import com.codingpixel.healingbudz.Utilities.eventbus.MessageEvent;
import com.codingpixel.healingbudz.data_structure.APIActions;
import com.codingpixel.healingbudz.network.BudzFeedModel.GetAllPost.Post;
import com.codingpixel.healingbudz.network.VollyAPICall;
import com.codingpixel.healingbudz.network.model.APIResponseListner;
import com.codingpixel.healingbudz.network.model.URL;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.victor.loading.rotate.RotateLoading;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

import static com.codingpixel.healingbudz.activity.home.HomeActivity.showSubFragmentListner_object;
import static com.codingpixel.healingbudz.activity.splash.Splash.user;
import static com.codingpixel.healingbudz.network.model.URL.get_question;

/**
 * Created by Alex on 11/12/2015.
 */
public class Preview extends RelativeLayout {
    private static String TAG = Preview.class.getSimpleName();
    ImageView isAnswered;
    private SelectableRoundedImageView mImgViewImage;
    private TextView mTxtViewTitle;
    private TextView mTxtViewDescription;
    LinearLayout not_found;
    private TextView mTxtViewSiteName, text_ans_count, ans_count;
    private TextView mTxtViewMessage;

    /*@ColorInt
    private int titleTextColor;
    @ColorInt
    private int descriptionTextColor;
    @ColorInt
    private int siteNameTextColor;
    @ColorInt
    private int messageTextColor;*/

    private Context mContext;
    private Handler mHandler;
    private String mTitle = null;
    private String mDescription = null;
    private String mImageLink = null;
    private String mSiteName = null;
    private String mSite;
    private String mLink;
    private RotateLoading mLoadingDialog;
    private FrameLayout mFrameLayout;
    private PreviewListener mListener;
    RelativeLayout all_view;
    LinearLayout bid_first_content, qa_view;
    private ImageView cross;
    Button first_bid_button, qa_fragment_list_discuss_button;
    private Post post;

    public Preview(Context context) {
        super(context);
        initialize(context);
    }

    public Preview(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context);
    }

    public Preview(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initialize(context);
    }

    private void initialize(Context context) {
        mContext = context;
        inflate(context, R.layout.preview_layout, this);
//        all_view = findViewById(R.id.all_view);
        bid_first_content = findViewById(R.id.bid_first_content);
        qa_fragment_list_discuss_button = findViewById(R.id.qa_fragment_list_discuss_button);
        qa_view = findViewById(R.id.qa_view);
        first_bid_button = findViewById(R.id.first_bid_button);
        not_found = findViewById(R.id.not_found);
        mImgViewImage = findViewById(R.id.imgViewImage);
        mImgViewImage.setCornerRadiiDP(10, 10, 0, 0);
        cross = (ImageView) findViewById(R.id.cross);
        mTxtViewTitle = (TextView) findViewById(R.id.txtViewTitle);
        mTxtViewDescription = (TextView) findViewById(R.id.txtViewDescription);
        mTxtViewSiteName = (TextView) findViewById(R.id.txtViewSiteName);
        mLoadingDialog = (RotateLoading) findViewById(R.id.rotateloading);
        mTxtViewMessage = (TextView) findViewById(R.id.txtViewMessage);
        isAnswered = findViewById(R.id.is_answered);
        text_ans_count = findViewById(R.id.text_ans_count);
        ans_count = findViewById(R.id.ans_count);
        mFrameLayout = (FrameLayout) findViewById(R.id.frameLoading);
        mFrameLayout.setVisibility(GONE);
        mHandler = new Handler(mContext.getMainLooper());

        /*titleTextColor = mTxtViewTitle.getCurrentTextColor();
        descriptionTextColor = mTxtViewDescription.getCurrentTextColor();
        messageTextColor = mTxtViewMessage.getCurrentTextColor();
        siteNameTextColor = mTxtViewSiteName.getCurrentTextColor();*/
    }

    public void setListener(PreviewListener listener) {
        this.mListener = listener;
    }

    public void setData(String title, String description, String image, String site) {
//        clear();
        this.url = null;
        mTitle = title;
        mDescription = description;
        mImageLink = image;
        mSiteName = site;
        if (getTitle() != null) {
            Log.v(TAG, getTitle());
            if (getTitle().length() >= 50)
                mTitle = getTitle().substring(0, 49) + "...";
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        mTxtViewTitle.setText(Html.fromHtml(getTitle(), Html.FROM_HTML_MODE_COMPACT));
                    } else {
                        mTxtViewTitle.setText(Html.fromHtml(getTitle()));
                    }

                }
            });
        }
        if (getDescription() != null) {
            Log.v(TAG, getDescription());
            if (getDescription().length() >= 100)
                mDescription = getDescription().substring(0, 99) + "...";
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        mTxtViewDescription.setText(Html.fromHtml(getDescription(), Html.FROM_HTML_MODE_COMPACT));
                    } else {
                        mTxtViewDescription.setText(Html.fromHtml(getDescription()));
                    }

                }
            });

        }
        if (getImageLink() != null && !getImageLink().equals("")) {
            Log.v(TAG, getImageLink());
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Glide.with(mContext)
                            .load(getImageLink())
                            .error(R.drawable.noimage)
                            .centerCrop()
                            .listener(new RequestListener<String, GlideDrawable>() {
                                @Override
                                public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                    if (e != null)
                                        e.printStackTrace();
                                    mImgViewImage.setImageResource(R.drawable.noimage);
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                    return false;
                                }
                            })
                            .into(mImgViewImage);
                }
            });

        } else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Glide.with(mContext)
                            .load(R.drawable.noimage)
                            .error(R.drawable.noimage)
                            .centerCrop()
                            .listener(new RequestListener<Integer, GlideDrawable>() {
                                @Override
                                public boolean onException(Exception e, Integer model, Target<GlideDrawable> target, boolean isFirstResource) {
                                    if (e != null)
                                        e.printStackTrace();
                                    mImgViewImage.setImageResource(R.drawable.noimage);
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(GlideDrawable resource, Integer model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                    return false;
                                }
                            })
                            .into(mImgViewImage);
                }
            });
        }
        if (getSiteName() != null) {
            Log.v(TAG, getSiteName());
            if (getSiteName().length() >= 30)
                mSiteName = getSiteName().substring(0, 29) + "...";
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mTxtViewSiteName.setText(getSiteName());
                }
            });
        }
    }

    private String url;
    HomeQAfragmentDataModel modelData;

    public void setData(String url) {
        clear();
        url = url.replace("\'", "");
        if (url.contains(URL.sharedBaseUrl + "/get-question-answers/")) {
            final String urlQA = get_question + "/" + url.replace(URL.sharedBaseUrl + "/get-question-answers/", "");
            JSONObject jsonObject = new JSONObject();
            new VollyAPICall(mContext, false, urlQA, jsonObject, user.getSession_key(), com.android.volley.Request.Method.GET, new APIResponseListner() {
                @Override
                public void onRequestSuccess(String response, APIActions.ApiActions apiActions) {
                    JSONObject object = null;
                    try {
                        object = new JSONObject(response);

                        JSONArray question_array = object.getJSONArray("successData");

                        for (int x = 0; x < question_array.length(); x++) {
                            JSONObject quesiton_object = question_array.getJSONObject(x);
                            HomeQAfragmentDataModel model = new HomeQAfragmentDataModel();
                            JSONObject user_object = quesiton_object.getJSONObject("get_user");
                            model.setUser_points(user_object.getInt("points"));
                            model.setUser_name(user_object.getString("first_name"));
                            model.setUser_name_dscription("asks...");
                            String user_photo = user_object.optString("image_path");
                            if (!user_photo.equalsIgnoreCase("null") && user_photo.length() > 6) {
                                model.setUser_photo(user_photo);
                            } else {
                                model.setUser_photo(user_object.optString("avatar"));
                            }
                            model.setUser_location(user_object.optString("location"));
                            model.setSpecial_icon(user_object.optString("special_icon"));
                            model.setQuestion(quesiton_object.getString("question"));
                            model.setId(quesiton_object.getInt("id"));
                            model.setShow_ads(quesiton_object.getInt("show_ads"));
                            model.setUser_id(quesiton_object.getInt("user_id"));
                            model.setCreated_at(quesiton_object.getString("created_at"));
                            model.setUpdated_at(quesiton_object.getString("updated_at"));
                            model.setIsAnswered(quesiton_object.getInt("is_answered_count"));
                            model.setQuestion_description(quesiton_object.getString("description"));
                            model.setAnswerCount(quesiton_object.getInt("get_answers_count"));
                            model.setGet_user_flag_count(quesiton_object.getInt("get_user_flag_count"));
                            model.setGet_user_likes_count(quesiton_object.getInt("get_user_likes_count"));
                            model.setUser_notify(quesiton_object.getInt("user_notify"));
                            modelData = model;
                            qa_view.setVisibility(VISIBLE);
                            mTxtViewSiteName.setVisibility(GONE);
                            if (modelData.getIsAnswered() > 0) {
                                isAnswered.setImageResource(R.drawable.ic_answered);
                            } else {
                                isAnswered.setImageResource(R.drawable.ic_non_answer);
                            }
                            ans_count.setText(modelData.getAnswerCount() + "");
                            if (modelData.getAnswerCount() > 1) {
                                text_ans_count.setText("ANSWERS");
                            } else {
                                text_ans_count.setText("ANSWER");
                            }
//                            if (modelData.getAnswerCount() == 0) {
//                                first_bid_button.setVisibility(View.VISIBLE);
//                                bid_first_content.setVisibility(View.GONE);
//                                if (modelData.getUser_id() == user.getUser_id()) {
//                                    first_bid_button.setText("DISCUSSION");
//                                } else {
//                                    first_bid_button.setText("BE THE 1ˢᵗ BUD TO ANSWER");
//                                }
//                            } else {
//                                first_bid_button.setVisibility(View.GONE);
//                                bid_first_content.setVisibility(View.VISIBLE);
//                            }
//                            first_bid_button.setOnClickListener(new OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    HomeQAfragmentDataModel model = new HomeQAfragmentDataModel();
//                                    model.setId(modelData.getId());
//                                    showSubFragmentListner_object.ShowQuestions(model, true);
//                                }
//                            });
                            qa_fragment_list_discuss_button.setOnClickListener(new OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            EventBus.getDefault().post(new MessageEvent(true));
                                        }
                                    }, 200);
                                    HomeQAfragmentDataModel model = new HomeQAfragmentDataModel();
                                    model.setId(modelData.getId());
                                    showSubFragmentListner_object.ShowQuestions(model, true);
                                }
                            });
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onRequestError(String response, APIActions.ApiActions apiActions) {
                    qa_view.setVisibility(GONE);
                }
            }, APIActions.ApiActions.get_my_question);
            qa_view.setVisibility(VISIBLE);
            mTxtViewSiteName.setVisibility(GONE);
            qa_fragment_list_discuss_button.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            EventBus.getDefault().post(new MessageEvent(true));
                        }
                    }, 200);
                    HomeQAfragmentDataModel model = new HomeQAfragmentDataModel();
                    model.setId(Integer.parseInt(urlQA));
                    showSubFragmentListner_object.ShowQuestions(model, true);
                }
            });
        } else {
            qa_view.setVisibility(GONE);
            mTxtViewSiteName.setVisibility(VISIBLE);
        }
        if (!TextUtils.isEmpty(url)) {
            this.url = url;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    clear();
                    mFrameLayout.setVisibility(VISIBLE);
                    mLoadingDialog.start();
                }
            });
//            clear();

            OkHttpClient client = new OkHttpClient();
            try {
                Request request = new Request.Builder()
                        .url(url)
                        .build();
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Request request, IOException throwable) {

                        if (!TextUtils.isEmpty(throwable.getMessage())) {

                            Log.e(TAG, throwable.getMessage());
                        }
                    }

                    @Override
                    public void onResponse(Response response) throws IOException {
                        if (mContext != null && !((Activity) mContext).isDestroyed()) {
                            try {
                                if (!response.isSuccessful()) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (mLoadingDialog.isStart())
                                                mLoadingDialog.stop();
                                            mFrameLayout.setVisibility(GONE);
                                            mImgViewImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
                                            mImgViewImage.setImageResource(R.drawable.noimage);
                                            not_found.setVisibility(GONE);

                                        }
                                    });
                                    throw new IOException("Unexpected code " + response);
                                } else {
                                    not_found.setVisibility(VISIBLE);

                                }


                                Elements titleElements;
                                Elements descriptionElements;
                                Elements imageElements;
                                Elements siteElements;
                                Elements linkElements;
                                String site = "";
                                Document doc = null;
                                doc = Jsoup.parse(response.body().string());
                                titleElements = doc.select("title");
                                descriptionElements = doc.select("meta[name=description]");
                                if (Preview.this.url.contains("bhphotovideo")) {
                                    imageElements = doc.select("image[id=mainImage]");
                                    site = "bhphotovideo";
                                } else if (Preview.this.url.contains("www.amazon.com")) {
                                    imageElements = doc.select("img[data-old-hires]");
                                    site = "www.amazon.com";
                                } else if (Preview.this.url.contains("www.amazon.co.uk")) {
                                    imageElements = doc.select("img[data-old-hires]");
                                    site = "www.amazon.co.uk";
                                } else if (Preview.this.url.contains("www.amazon.de")) {
                                    imageElements = doc.select("img[data-old-hires]");
                                    site = "www.amazon.de";
                                } else if (Preview.this.url.contains("www.amazon.fr")) {
                                    imageElements = doc.select("img[data-old-hires]");
                                    site = "www.amazon.fr";
                                } else if (Preview.this.url.contains("www.amazon.it")) {
                                    imageElements = doc.select("img[data-old-hires]");
                                    site = "www.amazon.it";
                                } else if (Preview.this.url.contains("www.amazon.es")) {
                                    imageElements = doc.select("img[data-old-hires]");
                                    site = "www.amazon.es";
                                } else if (Preview.this.url.contains("www.amazon.ca")) {
                                    imageElements = doc.select("img[data-old-hires]");
                                    site = "www.amazon.ca";
                                } else if (Preview.this.url.contains("www.amazon.co.jp")) {
                                    imageElements = doc.select("img[data-old-hires]");
                                    site = "www.amazon.co.jp";
                                } else if (Preview.this.url.contains("m.clove.co.uk")) {
                                    imageElements = doc.select("img[id]");
                                    site = "m.clove.co.uk";
                                } else if (Preview.this.url.contains("www.clove.co.uk")) {
                                    imageElements = doc.select("li[data-thumbnail-path]");
                                    site = "www.clove.co.uk";
                                } else {
                                    site = Preview.this.url;
                                    imageElements = doc.select("meta[property=og:image]");
                                    if (imageElements == null) {
                                        imageElements = doc.select("meta[image=og:url]");
                                    }
                                }
                                mImageLink = getImageLinkFromSource(imageElements, site);
                                siteElements = doc.select("meta[property=og:site_name]");
                                linkElements = doc.select("meta[property=og:url]");

                                if (titleElements != null && titleElements.size() > 0) {
                                    mTitle = titleElements.get(0).text();
                                }
                                if (descriptionElements != null && descriptionElements.size() > 0) {
                                    mDescription = descriptionElements.get(0).attr("content");
                                }
                                if (linkElements != null && linkElements.size() > 0) {
                                    mLink = linkElements.get(0).attr("content");
                                } else {
                                    linkElements = doc.select("link[rel=canonical]");
                                    if (linkElements != null && linkElements.size() > 0) {
                                        mLink = linkElements.get(0).attr("href");
                                    }
                                }
                                if (siteElements != null && siteElements.size() > 0) {
                                    mSiteName = siteElements.get(0).attr("content");
                                }

                                if (getTitle() != null) {
                                    Log.v(TAG, getTitle());
                                    if (getTitle().length() >= 50)
                                        mTitle = getTitle().substring(0, 49) + "...";
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                                mTxtViewTitle.setText(Html.fromHtml(getTitle(), Html.FROM_HTML_MODE_COMPACT));
                                            } else {
                                                mTxtViewTitle.setText(Html.fromHtml(getTitle()));
                                            }
                                        }
                                    });
                                }
                                if (getDescription() != null) {
                                    Log.v(TAG, getDescription());
                                    if (getDescription().length() >= 100)
                                        mDescription = getDescription().substring(0, 99) + "...";
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                                mTxtViewDescription.setText(Html.fromHtml(getDescription(), Html.FROM_HTML_MODE_COMPACT));
                                            } else {
                                                mTxtViewDescription.setText(Html.fromHtml(getDescription()));
                                            }
                                        }
                                    });

                                }
                                if (getImageLink() != null && !getImageLink().equals("")) {
                                    Log.v(TAG, getImageLink());
                                    try {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                try {
                                                    Glide.with(mContext)
                                                            .load(getImageLink())
                                                            .error(R.drawable.noimage)
                                                            .listener(new RequestListener<String, GlideDrawable>() {
                                                                @Override
                                                                public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                                                    mImgViewImage.setImageResource(R.drawable.noimage);
                                                                    if (e != null)
                                                                        e.printStackTrace();
                                                                    return false;
                                                                }

                                                                @Override
                                                                public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                                                    return false;
                                                                }
                                                            })
                                                            .centerCrop()
                                                            .into(mImgViewImage);
                                                } catch (IllegalArgumentException e) {
                                                    e.printStackTrace();

                                                }
                                            }
                                        });
                                    } catch (IllegalArgumentException e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    try {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                try {
                                                    Glide.with(mContext)
                                                            .load(R.drawable.noimage)
                                                            .error(R.drawable.noimage)
                                                            .centerCrop()
                                                            .listener(new RequestListener<Integer, GlideDrawable>() {
                                                                @Override
                                                                public boolean onException(Exception e, Integer model, Target<GlideDrawable> target, boolean isFirstResource) {
                                                                    mImgViewImage.setImageResource(R.drawable.noimage);
                                                                    if (e != null)
                                                                        e.printStackTrace();
                                                                    return false;
                                                                }

                                                                @Override
                                                                public boolean onResourceReady(GlideDrawable resource, Integer model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                                                    return false;
                                                                }
                                                            })
                                                            .into(mImgViewImage);
                                                } catch (IllegalArgumentException e) {
                                                    e.printStackTrace();

                                                }
                                            }
                                        });
                                    } catch (IllegalArgumentException e) {
                                        e.printStackTrace();
                                    }
                                }
                                if (Preview.this.url.toLowerCase().contains("amazon"))
                                    if (getSiteName() == null || getSiteName().equals(""))
                                        mSiteName = "Amazon";
                                if (getSiteName() != null) {
                                    Log.v(TAG, getSiteName());
                                    if (getSiteName().length() >= 30)
                                        mSiteName = getSiteName().substring(0, 29) + "...";
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            mTxtViewSiteName.setText(getSiteName());
                                        }
                                    });
                                }

                                Log.v(TAG, "Link: " + getLink());

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (mLoadingDialog.isStart())
                                            mLoadingDialog.stop();
                                        mFrameLayout.setVisibility(GONE);
                                    }
                                });

                                mListener.onDataReady(Preview.this);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }

                });
            } catch (Exception ex) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (mLoadingDialog.isStart())
                            mLoadingDialog.stop();
                        mFrameLayout.setVisibility(GONE);
                    }
                });
            }
        } else {
            this.url = null;
        }
    }

    public String getUrl() {
        if (getLink() == null || getLink().trim().isEmpty()) {
            return url;
        }
        return getLink();
    }

    public void setMessage(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (message == null)
                    mTxtViewMessage.setVisibility(GONE);
                else
//                    mTxtViewMessage.setVisibility(VISIBLE);
                    mTxtViewMessage.setText(message);
            }
        });
    }

    public void setMessage(final String message, final int color) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (message == null)
                    mTxtViewMessage.setVisibility(GONE);
                else
//                    mTxtViewMessage.setVisibility(VISIBLE);
                    mTxtViewMessage.setTextColor(color);
                mTxtViewMessage.setText(message);
            }
        });
    }

    private String getImageLinkFromSource(Elements elements, String site) {
        String imageLink = null;
        if (elements != null && elements.size() > 0) {
            switch (site) {
                case "m.clove.co.uk":
                case "bhphotovideo":
                    imageLink = elements.get(0).attr("src");
                    break;
                case "www.amazon.com":
                case "www.amazon.co.uk":
                case "www.amazon.de":
                case "www.amazon.fr":
                case "www.amazon.it":
                case "www.amazon.es":
                case "www.amazon.ca":
                case "www.amazon.co.jp":
                    imageLink = elements.get(0).attr("data-old-hires");
                    if (TextUtils.isEmpty(imageLink)) {
                        imageLink = elements.get(0).attr("src");
                        if (imageLink.contains("data:image/jpeg;base64,")) {
                            imageLink = elements.get(0).attr("data-a-dynamic-image");
                            if (!TextUtils.isEmpty(imageLink)) {
                                String[] array = imageLink.split(":\\[");
                                if (array.length > 1) {
                                    imageLink = array[0];
                                    if (!TextUtils.isEmpty(imageLink)) {
                                        imageLink = imageLink.replace("{\"", "");
                                        imageLink = imageLink.replace("\"", "");
                                    }
                                }
                            }
                        }
                    }
                    break;
                case "www.clove.co.uk":
                    imageLink = "https://www.clove.co.uk" + elements.get(0).attr("data-thumbnail-path");
                    break;
//                case "www.youtube.com":
//                    imageLink = site + elements.get(0).attr("content");
//                    break;
                default: {
//                    if (site.contains("www.youtube.com")) {
//                        imageLink = site + elements.get(0).attr("content");
//                    } else {
                    imageLink = elements.get(0).attr("content");
//                    }
                }
                break;
            }

        }
        return imageLink;
    }

    public void clear() {
        mImgViewImage.setImageResource(0);
        mImgViewImage.setImageDrawable(null);
        mImgViewImage.setImageBitmap(null);
        mImgViewImage.setBackgroundResource(0);
        mTxtViewTitle.setText("");
        mTxtViewDescription.setText("");
        mTxtViewSiteName.setText("");
        mTxtViewMessage.setText("");
        mTitle = null;
        mDescription = null;
        mImageLink = null;
        mSiteName = null;
        mSite = null;
        mLink = null;
    }

    public ImageView getCross() {
        return cross;
    }

    public void setCross(ImageView cross) {
        this.cross = cross;
    }

    public void visbleCross() {
        cross.setVisibility(VISIBLE);
    }

    public void goneCross() {
        cross.setVisibility(GONE);
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public Post getPost() {
        return post;
    }

    public interface PreviewListener {
        public void onDataReady(Preview preview);
    }

    private void runOnUiThread(Runnable r) {
        mHandler.post(r);
    }

    public String getTitle() {
        if (mTitle == null) {
            return "";
        }
        return mTitle;
    }

    public String getDescription() {
        if (mDescription == null) {
            return "";
        }
        return mDescription;
    }

    public String getImageLink() {
        if (mImageLink == null) {
            if (this.post.getScrappedData() != null) {
                if (this.post.getScrappedData().getImage().length() > 0) {
                    return this.post.getScrappedData().getImage();
                } else {
                    return "";
                }
            } else {
                return "";
            }
        } else if (mImageLink.length() == 0) {
            if (this.post.getScrappedData() != null) {
                if (this.post.getScrappedData().getImage().length() > 0) {
                    return this.post.getScrappedData().getImage();
                } else {
                    return "";
                }
            } else {
                return "";
            }
        }
        return mImageLink;
    }

    public String getSiteName() {
        return mSiteName;
    }

    public String getSite() {
        return mSite;
    }

    public String getLink() {
        return mLink;
    }

    /*@ColorInt
    public int getTitleTextColor() {
        return titleTextColor;
    }*/

    public void setTitleTextColor(@ColorInt int titleTextColor) {
        //this.titleTextColor = titleTextColor;
        if (mTxtViewTitle != null) {
            mTxtViewTitle.setTextColor(titleTextColor);
        }
    }

    /*@ColorInt
    public int getDescriptionTextColor() {
        return descriptionTextColor;
    }*/

    public void setDescriptionTextColor(@ColorInt int descriptionTextColor) {
        //this.descriptionTextColor = descriptionTextColor;
        if (mTxtViewDescription != null) {
            mTxtViewDescription.setTextColor(descriptionTextColor);
        }
    }

    /*@ColorInt
    public int getSiteNameTextColor() {
        return siteNameTextColor;
    }*/

    public void setSiteNameTextColor(@ColorInt int siteNameTextColor) {
        //this.siteNameTextColor = siteNameTextColor;
        if (mTxtViewSiteName != null) {
            mTxtViewSiteName.setTextColor(siteNameTextColor);
        }
    }

    /*@ColorInt
    public int getMessageTextColor() {
        return messageTextColor;
    }*/

    public void setMessageTextColor(@ColorInt int messageTextColor) {
        //this.messageTextColor = messageTextColor;
        if (mTxtViewMessage != null) {
            mTxtViewMessage.setTextColor(messageTextColor);
        }
    }

    public SelectableRoundedImageView getImgViewImage() {
        return mImgViewImage;
    }

    public TextView getTxtViewTitle() {
        return mTxtViewTitle;
    }

    public TextView getTxtViewDescription() {
        return mTxtViewDescription;
    }

    public TextView getTxtViewSiteName() {
        return mTxtViewSiteName;
    }

    public TextView getTxtViewMessage() {
        return mTxtViewMessage;
    }
}
