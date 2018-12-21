package com.codingpixel.healingbudz.activity.Wall;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Layout;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.codingpixel.healingbudz.DataModel.User;
import com.codingpixel.healingbudz.R;
import com.codingpixel.healingbudz.Utilities.Constants;
import com.codingpixel.healingbudz.Utilities.Dialogs;
import com.codingpixel.healingbudz.Utilities.Flags;
import com.codingpixel.healingbudz.Utilities.KeyboardUtils;
import com.codingpixel.healingbudz.Utilities.SetUserValuesInSP;
import com.codingpixel.healingbudz.Utilities.Utility;
import com.codingpixel.healingbudz.activity.splash.Splash;
import com.codingpixel.healingbudz.adapter.BudzFeedNewPostMediaViewPagerAdapter;
import com.codingpixel.healingbudz.adapter.MentionAdapter;
import com.codingpixel.healingbudz.adapter.WallNewPostUploadFileTypeAdapter;
import com.codingpixel.healingbudz.adapter.WallTagPeopleAdapter;
import com.codingpixel.healingbudz.camera_activity.HBCameraActivity;
import com.codingpixel.healingbudz.customeUI.CircularImageView;
import com.codingpixel.healingbudz.customeUI.CustomeToast;
import com.codingpixel.healingbudz.customeUI.FontCache;
import com.codingpixel.healingbudz.customeUI.HealingBudTextViewBold;
import com.codingpixel.healingbudz.customeUI.Preview;
import com.codingpixel.healingbudz.customeUI.VerticalScrollView;
import com.codingpixel.healingbudz.customeUI.customalerts.SweetAlertDialog;
import com.codingpixel.healingbudz.data_structure.APIActions;
import com.codingpixel.healingbudz.interfaces.ApiStatusCallBack;
import com.codingpixel.healingbudz.interfaces.RecyclerViewItemClickListener;
import com.codingpixel.healingbudz.media_view_Dialog.MediPreview;
import com.codingpixel.healingbudz.network.BudzFeedModel.ExtraPost;
import com.codingpixel.healingbudz.network.BudzFeedModel.FollowingUserModel.FollowingUser;
import com.codingpixel.healingbudz.network.BudzFeedModel.FollowingUserModel.FollowingUserResponse;
import com.codingpixel.healingbudz.network.BudzFeedModel.GetAllPost.GetAllPostResponse;
import com.codingpixel.healingbudz.network.BudzFeedModel.GetAllPost.Post;
import com.codingpixel.healingbudz.network.BudzFeedModel.MentionTagJsonModel;
import com.codingpixel.healingbudz.network.BudzFeedModel.SubUsers.SubUser;
import com.codingpixel.healingbudz.network.BudzFeedModel.SubUsers.SubUsersResponse;
import com.codingpixel.healingbudz.network.BudzFeedModel.TagModel.Tag;
import com.codingpixel.healingbudz.network.BudzFeedModel.TagModel.TagResponse;
import com.codingpixel.healingbudz.network.RestClient;
import com.codingpixel.healingbudz.network.model.APIResponseListner;
import com.codingpixel.healingbudz.network.model.URL;
import com.codingpixel.healingbudz.network.upload_image.UploadImageAPIcall;
import com.codingpixel.healingbudz.network.upload_image.UploadVideoAPIcall;
import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.linkedin.android.spyglass.mentions.MentionSpan;
import com.linkedin.android.spyglass.mentions.MentionsEditable;
import com.linkedin.android.spyglass.tokenization.QueryToken;
import com.linkedin.android.spyglass.tokenization.impl.WordTokenizer;
import com.linkedin.android.spyglass.tokenization.impl.WordTokenizerConfig;
import com.linkedin.android.spyglass.tokenization.interfaces.QueryTokenReceiver;
import com.linkedin.android.spyglass.ui.MentionsEditText;
import com.rd.PageIndicatorView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.codingpixel.healingbudz.Utilities.ViewUtils.checkRotation;
import static com.codingpixel.healingbudz.static_function.UIModification.HideKeyboard;

/*
 * Created by M_Muzammil Sharif on 09-Mar-18.
 */

public class WallNewPostActivity extends AppCompatActivity implements RecyclerViewItemClickListener, APIResponseListner, Callback<GetAllPostResponse>, Dialogs.DialogItemClickListener, Preview.PreviewListener {
    private CircularImageView personImg;
    private ImageView profile_img_topi;
    private HealingBudTextViewBold personName, personExtraInfo;
    private TextView tvPostAs;
    private boolean isKeyboardVisible;
    private ImageView personLeaf;
    private ViewPager pager;
    EditText activity_wall_new_repost;
    private PageIndicatorView indicator;
    private MentionsEditText editText;
    private RecyclerView recyclerView;
    private View mediaPagerParent, spinnerParent, dropDownTop;
    private WallNewPostUploadFileTypeAdapter adapter;
    private User user;
    private com.codingpixel.healingbudz.customeUI.ProgressDialog dialog;
    private BudzFeedNewPostMediaViewPagerAdapter.NewPostMedia temp;
    private RecyclerView mentionTagList;
    private MentionAdapter mentionTagAdapter;
    private Preview scrapLayout;
    private Post post = null;
    FrameLayout for_repost;
    private List<SubUser> subUsers;
    private String postAs = "";
    private int type = 0;
    private HealingBudTextViewBold title;
    private WallTagPeopleAdapter tagAdapter;
    private View tagParent;
    String jsonDataPrevious = "";
    View drop_down_ic;
    private BudzFeedNewPostMediaViewPagerAdapter mediaAdapter;
    String urlLink = null;
    LinearLayout main_tap, tap_vv;
    VerticalScrollView var_scrll;
    private MentionTagJsonModel[] preJsonData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wall_new_post);
        dialog = new com.codingpixel.healingbudz.customeUI.ProgressDialog();
        dialog.setCancelable(false);


        user = SetUserValuesInSP.getSavedUser(WallNewPostActivity.this);
        if (user == null) {
            onSessionExpire();
            return;
        }

        if (getIntent().getExtras() != null) {
            Bundle bundle = getIntent().getExtras();
            if (!bundle.containsKey("urlhas")) {
                type = getIntent().getExtras().getInt("type_int", 0);
                post = (Post) getIntent().getExtras().getSerializable(Constants.POST_EXTRA);
                jsonDataPrevious = post.getJson_Data();
                preJsonData = post.getJsonData();
            }

        } else {
            post = null;
        }

        initView();
        KeyboardUtils.addKeyboardToggleListener(WallNewPostActivity.this, new KeyboardUtils.SoftKeyboardToggleListener() {
            @Override
            public void onToggleSoftKeyboard(boolean isVisible) {

                isKeyboardVisible = isVisible;
                toggleBottomLayer(!isKeyboardVisible && mediaAdapter.getCount() == 0);
            }
        });
        setUpView();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null && bundle.containsKey("urlhas")) {
            editText.setText(bundle.getString("urlhas", ""));
        }
    }

    private void onSessionExpire() {
        Utility.finishWithResult(WallNewPostActivity.this, null, Flags.SESSION_OUT);
    }

    @SuppressLint("ClickableViewAccessibility")
    @TargetApi(Build.VERSION_CODES.M)
    private void initView() {
        getMentionUsers();
        getAllTags();
        getSubUsers();

        tagParent = findViewById(R.id.with_parent);
        for_repost = findViewById(R.id.for_repost);
        activity_wall_new_repost = findViewById(R.id.activity_wall_new_repost);
        main_tap = findViewById(R.id.main_tap);
        var_scrll = findViewById(R.id.var_scrll);
        tap_vv = findViewById(R.id.tap_vv);

        drop_down_ic = findViewById(R.id.drop_down_ic);

        title = (HealingBudTextViewBold) findViewById(R.id.wall_new_post_title);
        personImg = (CircularImageView) findViewById(R.id.activity_wall_new_post_person_img);
        personImg.setScaleType(ImageView.ScaleType.CENTER_CROP);
        profile_img_topi = findViewById(R.id.profile_img_topi);
        personLeaf = (ImageView) findViewById(R.id.activity_wall_new_post_person_leaf);
        personName = (HealingBudTextViewBold) findViewById(R.id.activity_wall_new_post_person_name);
        personExtraInfo = (HealingBudTextViewBold) findViewById(R.id.activity_wall_new_post_person_extra_info);
        tvPostAs = (TextView) findViewById(R.id.activity_wall_new_post_as_spinner);
        pager = (ViewPager) findViewById(R.id.activity_wall_new_post_imgs_pager);
        indicator = (PageIndicatorView) findViewById(R.id.activity_wall_new_post_imgs_pager_indicator);
        editText = (MentionsEditText) findViewById(R.id.activity_wall_new_post_des_edt);
        Typeface st = FontCache.getTypeface("Lato-Light.ttf", this);
        editText.setTypeface(st);
//        editText.getText().setSpan(watcher, 0, 0, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    editText.setHint("");
                } else {
                    editText.setHint("Hey Bud, what's on your mind?");
                }
            }
        });
        editText.setLinkTextColor(Color.parseColor("#6d96ad"));
//        editText.
        recyclerView = (RecyclerView) findViewById(R.id.activity_wall_new_post_media_type);
        recyclerView.setLayoutManager(new LinearLayoutManager(WallNewPostActivity.this));

        scrapLayout = (Preview) findViewById(R.id.activity_wall_new_post_scrap_view);
        scrapLayout.setListener(WallNewPostActivity.this);
        scrapLayout.visbleCross();
        scrapLayout.getCross().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scrapUrl(null);
                editText.setText("");
            }
        });


        var_scrll.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
//                editText.requestFocus();
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                InputMethodManager imm = (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.toggleSoftInputFromWindow(editText.getWindowToken(), InputMethodManager.SHOW_FORCED, 0);
                                editText.requestFocus();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }, 300);
                }
                return false;
            }
        });

//        scrapLayout.setDescriptionTextColor(Color.GRAY);
//        scrapLayout.setTitleTextColor(Color.YELLOW);
//        scrapLayout.setSiteNameTextColor(Color.BLUE);

        mentionTagList = (RecyclerView) findViewById(R.id.activity_wall_new_post_mention_tag_list);
        mentionTagList.setLayoutManager(new LinearLayoutManager(WallNewPostActivity.this));
        mentionTagList.setVisibility(View.GONE);

        indicator.setViewPager(pager);

        RecyclerView tagPeople = (RecyclerView) findViewById(R.id.activity_wall_new_post_tag_people_list);
        tagPeople.setLayoutManager(ChipsLayoutManager.newBuilder(WallNewPostActivity.this).setOrientation(ChipsLayoutManager.HORIZONTAL).build());
        tagAdapter = new WallTagPeopleAdapter(null, null, true);
        tagPeople.setAdapter(tagAdapter);
        tagAdapter.notifyDataSetChanged();
        if (post != null && post.get_Tagged() != null) {
            setTags(post.get_Tagged());
        }

        mediaAdapter = new BudzFeedNewPostMediaViewPagerAdapter(null, new BudzFeedNewPostMediaViewPagerAdapter.UpdateCallBack() {
            @Override
            public void onUpdate(int i) {
                if (mediaAdapter.getCount() == 0) {
                    mediaPagerParent.setVisibility(View.GONE);
                    toggleBottomLayer(isKeyboardVisible);
                } else {
                    toggleBottomLayer(false);
                    mediaPagerParent.setVisibility(View.VISIBLE);
                    if (i == -1) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                pager.setCurrentItem(mediaAdapter.getCount() - 1);
                            }
                        }, 100);
                    }
                }
            }
        }, new RecyclerViewItemClickListener() {
            @Override
            public void onItemClick(Object obj, int pos, int type) {
                BudzFeedNewPostMediaViewPagerAdapter.NewPostMedia postMedia = ((BudzFeedNewPostMediaViewPagerAdapter.NewPostMedia) obj);
                if (postMedia.isVideo()) {
                    Bundle b = new Bundle();
                    b.putString("path", URL.videos_baseurl + postMedia.url);
                    b.putBoolean("isvideo", true);
                    Utility.launchActivity(WallNewPostActivity.this, MediPreview.class, false, b);
                }
            }

            @Override
            public boolean onItemLongClick(Object obj, int pos, int type) {
                return false;
            }
        });
        pager.setAdapter(mediaAdapter);

        findViewById(R.id.activity_wall_new_post_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HideKeyboard((AppCompatActivity) view.getContext());
                WallNewPostActivity.this.finish();
            }
        });

        findViewById(R.id.activity_wall_new_post_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HideKeyboard(WallNewPostActivity.this);
                submitPost();
            }
        });

        mediaPagerParent = findViewById(R.id.activity_wall_new_post_media_parent);
        mediaPagerParent.setVisibility(View.GONE);
        spinnerParent = findViewById(R.id.activity_wall_new_post_as_parent);
        dropDownTop = findViewById(R.id.post_as_drop_down_top);
        spinnerParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (subUsers != null && subUsers.size() > 1) {
                    Dialogs.showPostAsDropDownMenu(dropDownTop, new Dialogs.DialogItemClickListener() {
                        @Override
                        public void onDialogItemClickListener(DialogInterface dialog, Object obj, int type) {
                            if (type == 0) {
                                tvPostAs.setText(user.getFirst_name());
                                postAs = "";
                            } else {
                                selectSubUser((SubUser) obj);
                            }
                        }
                    }, subUsers);
                } else {

                    drop_down_ic.setVisibility(View.INVISIBLE);

                }
            }
        });
        spinnerParent.setVisibility(View.GONE);

        adapter = new WallNewPostUploadFileTypeAdapter(getMediaTypes(), WallNewPostActivity.this);
        recyclerView.setAdapter(adapter);

        initMentionEditText();
    }

    private void selectSubUser(SubUser obj) {
        postAs = "s_" + String.valueOf(obj.getId());
        if (subUsers == null || subUsers.size() == 1) {
            drop_down_ic.setVisibility(View.INVISIBLE);
        }
        tvPostAs.setText(obj.getTitle());
    }

    String menUser = "";
    String menHash = "";
    String selectedTagg = "";

    //
//    private void initMentionEditText() {
//        editText.setTokenizer(new WordTokenizer(new WordTokenizerConfig.Builder().
//                setExplicitChars("#@").setThreshold(1).setMaxNumKeywords(1).build()));
//        mentionTagAdapter = new MentionAdapter(null, WallNewPostActivity.this);
//        mentionTagList.setAdapter(mentionTagAdapter);
//        mentionTagList.setVisibility(View.GONE);
//
//        editText.setQueryTokenReceiver(new QueryTokenReceiver() {
//            @Override
//            public List<String> onQueryReceived(@NonNull QueryToken queryToken) {
//                if (queryToken.getExplicitChar() == '#')//|| menHash.length() > 0)
//                {
//                    menHash = queryToken.getExplicitChar() + queryToken.getKeywords().toLowerCase();
//                    menUser = "";
//                    if (queryToken.getKeywords().toLowerCase().length() > 0)//|| menHash.length() > 1)
//                    {
//                        if (queryToken.getKeywords().toLowerCase().length() > 0) {
//                            selectedTagg = "#" + queryToken.getKeywords();
//                            showHashTagList(queryToken.getKeywords().toLowerCase());
//                        }
////                        else {
////                            selectedTagg = "#" + menHash.replace("#", "") + queryToken.getTokenString();
////                            showHashTagList(menHash.replace("#", "") + queryToken.getTokenString());
////                        }
//                    } else {
//                        mentionTagList.setVisibility(View.GONE);
//                    }
//
//                } else if (queryToken.getExplicitChar() == '@' || menUser.length() > 0) {
//                    menUser = queryToken.getExplicitChar() + queryToken.getKeywords().toLowerCase();
//                    menHash = "";
//                    if (queryToken.getKeywords().toLowerCase().length() > 0
//                            || menUser.length() > 1) {
//                        if (queryToken.getKeywords().toLowerCase().length() > 0) {
//                            selectedTagg = "@" + queryToken.getKeywords();
//                            showMentionList(queryToken.getKeywords().toLowerCase());
//                        } else {
//                            selectedTagg = "@" + menUser.replace("#", "") + queryToken.getTokenString();
//                            showMentionList(menUser.replace("@", "") + queryToken.getTokenString());
//                        }
//
//                    } else {
//                        mentionTagList.setVisibility(View.GONE);
//                    }
//                } else {
//
//                    menUser = "";
//                    menHash = "";
//
//                    List<String> urls = Utility.extractURL(editText.getText().toString());
//                    if (urls != null && !urls.isEmpty()) {
//                        urlLink = urls.get(0);
//                        scrapUrl(urls.get(0));
//                    } else {
//                        urlLink = null;
//                        scrapUrl(null);
//                    }
//                    urls = null;
//                    mentionTagList.setVisibility(View.GONE);
//                }
//                return null;
//            }
//
//        });
//    }
    private void initMentionEditText() {
        editText.setTokenizer(new WordTokenizer(new WordTokenizerConfig.Builder().
                setExplicitChars("#@").setThreshold(1).setMaxNumKeywords(1).build()));
        mentionTagAdapter = new MentionAdapter(null, WallNewPostActivity.this, false);
        mentionTagList.setAdapter(mentionTagAdapter);
        mentionTagList.setVisibility(View.GONE);

        editText.setQueryTokenReceiver(new QueryTokenReceiver() {
            @Override
            public List<String> onQueryReceived(@NonNull QueryToken queryToken) {
                List<String> urls = Utility.extractURL(editText.getText().toString());
                if (urls != null && !urls.isEmpty()) {
                    urlLink = urls.get(0);
                    scrapUrl(urls.get(0));
                } else {
                    urlLink = null;
                    scrapUrl(null);
                }
                urls = null;
                if (queryToken.getExplicitChar() == '#') {
                    if (queryToken.getKeywords().toLowerCase().length() > 0) {
                        showHashTagList(queryToken.getKeywords().toLowerCase());
                    } else {
                        mentionTagList.setVisibility(View.GONE);
                    }

                } else if (queryToken.getExplicitChar() == '@') {
                    if (queryToken.getKeywords().toLowerCase().length() > 0) {
                        showMentionList(queryToken.getKeywords().toLowerCase());
                    } else {
                        mentionTagList.setVisibility(View.GONE);
                    }
                } else {

                    mentionTagList.setVisibility(View.GONE);
                }
                return null;
            }

        });
    }

    private String currentScrapUrl = null;

    private void scrapUrl(String s) {
        String temp = currentScrapUrl;
        currentScrapUrl = s;
        if (currentScrapUrl == null) {
//            scrapLayout.setData("", "", "", "");
            scrapLayout.setVisibility(View.GONE);
            scrapLayout.clear();
        } else if (currentScrapUrl.equalsIgnoreCase(temp)) {
            scrapLayout.clear();
            scrapLayout.setData(s);
            scrapLayout.setVisibility(View.VISIBLE);
        } else {
            if (s == null) {
                scrapLayout.setVisibility(View.GONE);
                scrapLayout.clear();
            } else {
                scrapLayout.clear();
                scrapLayout.setData(s);
                scrapLayout.setVisibility(View.VISIBLE);
            }

        }
        temp = null;
    }

    private void showMentionList(String keyword) {
        List<FollowingUser> users = new ArrayList<>();
        List<FollowingUser> usersDelete = new ArrayList<>();

        for (FollowingUser user : WallSinglton.getInstance().getFollowingUserList()) {
            if (user.getFirstName().replaceAll(" ", "").toLowerCase().contains(keyword.toLowerCase()) ||
                    user.getLastName().replaceAll(" ", "").toLowerCase().contains(keyword.toLowerCase())) {
                users.add(user);
            }
        }
        MentionsEditable editable = editText.getMentionsText().trim();

        for (MentionSpan span : editable.getMentionSpans()) {
            if (span.getMention() instanceof FollowingUser) {
                FollowingUser user = (FollowingUser) span.getMention();
                for (int i = 0; i < users.size(); i++) {
                    if (users.get(i).getId().equals(user.getId()) && user.isSubUser()) {
                        users.remove(i);
                    } else if (users.get(i).getId().equals(user.getId())) {
                        users.remove(i);
                    }
                }
            }
        }
        if (post != null) {

            for (int i = 0; i < users.size(); i++) {
                if (editable.toString().toLowerCase().trim().contains("@" + users.get(i).getFirstName().toLowerCase().trim())) {
//                    users.remove(i);
                    usersDelete.add(users.get(i));
                }
            }
            for (int i = 0; i < usersDelete.size(); i++) {
                users.remove(usersDelete.get(i));
            }
            usersDelete.clear();
        }
        if (!users.isEmpty()) {
            mentionTagAdapter.setMentions(users);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    showMentionTagDropDown();
                }
            }, 100);
        } else {
            mentionTagList.setVisibility(View.GONE);
        }
    }

    private int dw = 0;

    private void showMentionTagDropDown() {
        if (!editText.isEnabled()) {
            return;
        }
        mentionTagList.setVisibility(View.VISIBLE);

        final FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) mentionTagList.getLayoutParams();
        final int x = getCursorX();
        //reset all params
        params.setMargins(0, 0, 0, 0);
        params.gravity = Gravity.LEFT | Gravity.START;
        //define top bottom margin according to cursor position
        params.topMargin = (int) (getCursorY() + (editText.getTextSize() * 2));
        params.bottomMargin = 5;
        //get dowpdown list width
        mentionTagList.post(new Runnable() {
            @Override
            public void run() {
                dw = mentionTagList.getMeasuredWidth();
                if (x > (dw / 2)) {
                    if (x < (Utility.getDeviceWidth(WallNewPostActivity.this) - (dw / 2))) {
                        params.leftMargin = (x - (dw / 2));
                    } else {
                        params.gravity = Gravity.CENTER;//Gravity.END | Gravity.RIGHT;
                    }
                }
                mentionTagList.setLayoutParams(params);
            }
        });
        //defining x position of dropdown accounding to cursor x position

    }

    private void showHashTagList(String keyword) {
        List<Tag> tags = new ArrayList<>();
        for (Tag tag : WallSinglton.getInstance().getHashTagList()) {
            if (tag.getTitle().replaceAll(" ", "").toLowerCase().contains(keyword.toLowerCase())) {
                tags.add(tag);
            }
        }

        if (!tags.isEmpty()) {
            mentionTagAdapter.setTags(tags);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    showMentionTagDropDown();
                }
            }, 100);
        } else {
            mentionTagList.setVisibility(View.GONE);
        }
    }

    private void setUpView() {
        personName.setText(user.getFirst_name() + " " + user.getLast_name());
        personName.setTextColor(Color.parseColor(Utility.getBudColor(Splash.user.getPoints())));
        personLeaf.setImageResource(Utility.getBudColorDrawable(Splash.user.getPoints()));
        Glide.with(getApplicationContext()).load(getUserImage()).asBitmap().
                diskCacheStrategy(DiskCacheStrategy.SOURCE).error(R.drawable.noimage).centerCrop().
                placeholder(Utility.getProfilePlaceHolder(Splash.user.getPoints())).into(personImg);
        if (getUserImageSpecial().length() > 5) {
            profile_img_topi.setVisibility(View.VISIBLE);
            Glide.with(getApplicationContext()).load(getUserImageSpecial()).asBitmap().
                    diskCacheStrategy(DiskCacheStrategy.SOURCE).
                    placeholder(R.drawable.topi_ic).into(profile_img_topi);
        } else {
            profile_img_topi.setVisibility(View.GONE);
        }
        personExtraInfo.setText(Splash.user.getPoints() + "   |   " + Utility.getBudType(Splash.user.getPoints()));
        personExtraInfo.setTextColor(Color.parseColor(Utility.getBudColor(Splash.user.getPoints())));
        if (post != null && type == 1) {
            editText.setText(Html.fromHtml(post.getDescription()));
//            editText.insertMention();
//            if (post.getJsonData() != null) {
//                for (int i = 0; i < post.getJsonData().length; i++) {
//                    editText.insertMention(post.getJsonData()[i]);
//                }
//            }

            title.setText("The Buzz");
            mediaAdapter.setMediasFromFile(post.getFiles());
            mentionTagList.setVisibility(View.GONE);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mentionTagList.setVisibility(View.GONE);
                }
            }, 200);
        }
        if (post != null && type == 2) {
            editText.setEnabled(false);
            editText.setHint("");
            for_repost.setVisibility(View.VISIBLE);
            title.setText("Repost");
            editText.setText(Html.fromHtml(post.getDescription()));
            adapter.setDataSet(getMediaTypes());
            mediaAdapter.setMediasFromFile(post.getFiles(), false);
            Utility.hideKeyboard(WallNewPostActivity.this);
        }
    }

    private void submitPost() {
        if (editText.getText().toString().trim().length() <= 0) {
            if (mediaAdapter.getCount() > 0) {

            } else {

                CustomeToast.ShowCustomToast(WallNewPostActivity.this, "Please add some description!", Gravity.TOP);
                return;
            }
        }
        if (!Utility.isNetworkAvailable(getApplicationContext())) {
            Dialogs.showNetworkNotAvailibleDialog(WallNewPostActivity.this);
            return;
        }

        dialog.show(getSupportFragmentManager(), "sendingg");
        String video = "";
        String poster = "";
        StringBuilder tagged = new StringBuilder();
        StringBuilder images = new StringBuilder();
        StringBuilder thumbs = new StringBuilder();
        StringBuilder ratio = new StringBuilder();

        if (mediaAdapter.getCount() > 0) {
            for (BudzFeedNewPostMediaViewPagerAdapter.NewPostMedia media : mediaAdapter.getMedias()) {
                if (media.isVideo()) {
                    video = media.url;
                    poster = media.poster;
                } else {
                    images.append(media.url).append(",");
                    thumbs.append((media.thumb)).append(",");
                    ratio.append((media.ratio)).append(",");
                }
            }
            if (images.toString().trim().length() > 0) {
                images = new StringBuilder(images.substring(0, images.lastIndexOf(",")));
            }
            if (thumbs.toString().trim().length() > 0) {
                thumbs = new StringBuilder(thumbs.substring(0, thumbs.lastIndexOf(",")));
            }
            if (ratio.toString().trim().length() > 0) {
                ratio = new StringBuilder(ratio.substring(0, ratio.lastIndexOf(",")));
            }
        }
        if (tagAdapter == null || tagAdapter.getFollowingUsers() == null || tagAdapter.getFollowingUsers().isEmpty()) {
            tagged = new StringBuilder("");
        } else {
            List<FollowingUser> users = (List<FollowingUser>) tagAdapter.getFollowingUsers();
            for (FollowingUser user : users) {
                tagged.append(user.getId()).append(",");
            }
            if (tagged.toString().length() > 0) {
                tagged = new StringBuilder(tagged.subSequence(0, tagged.lastIndexOf(",")));
            }
        }

        String[] postText;
        try {
            if (editText.getMentionsText().toString().trim().length() > 0) {
                postText = getPostDescription();
            } else {
                postText = new String[]{"", ""};
            }
        } catch (StringIndexOutOfBoundsException e) {
            e.printStackTrace();
            postText = new String[]{editText.getMentionsText().toString().trim(), ""};
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
            postText = new String[]{editText.getMentionsText().toString().trim(), ""};
        }
        if (post != null && type == 1) {
            if (postText[1].length() < 5) {
                postText[1] = jsonDataPrevious;
            }
            if (urlLink == null) {
                RestClient.getInstance(WallNewPostActivity.this).getApiService().updatePost(user.getSession_key(), post.getId(), postText[0],
                        images.toString(), video, poster, tagged.toString(), postText[1], postAs,
                        adapter.getDataSet().get(0).isActive() ? 1 : 0, "", thumbs.toString(), ratio.toString()).enqueue(WallNewPostActivity.this);
            } else {
                RestClient.getInstance(WallNewPostActivity.this).getApiService().updatePost(user.getSession_key(), post.getId(), postText[0],
                        images.toString(), video, poster, tagged.toString(), postText[1], postAs,
                        adapter.getDataSet().get(0).isActive() ? 1 : 0, urlLink, thumbs.toString(), ratio.toString()).enqueue(WallNewPostActivity.this);

            }
            return;
        }
        if (post != null && type == 2) {
            RestClient.getInstance(WallNewPostActivity.this).getApiService().repost(user.getSession_key(), post.getId(), postAs, tagged.toString(), activity_wall_new_repost.getText().toString()).
                    enqueue(WallNewPostActivity.this);
            return;
        }
        if (urlLink == null) {
            RestClient.getInstance(WallNewPostActivity.this).getApiService().
                    savePost(user.getSession_key(),
                            postText[0],
                            images.toString()
                            , video
                            , poster
                            , tagged.toString()
                            , postText[1], postAs,
                            adapter.getDataSet().get(0).isActive() ? 1 : 0, "", thumbs.toString(), ratio.toString()).enqueue(WallNewPostActivity.this);
        } else {
            RestClient.getInstance(WallNewPostActivity.this).getApiService().
                    savePost(user.getSession_key(), postText[0],
                            images.toString(), video, poster, tagged.toString(), postText[1], postAs,
                            adapter.getDataSet().get(0).isActive() ? 1 : 0, urlLink, thumbs.toString(), ratio.toString()).enqueue(WallNewPostActivity.this);
        }
    }

    private void showUnknownError() {

    }

    public List<WallNewPostUploadFileTypeAdapter.WallNewPostUploadFileType> getMediaTypes() {
        List<WallNewPostUploadFileTypeAdapter.WallNewPostUploadFileType> list = new ArrayList<>();
        Boolean isActive = true;
        if (post != null) {
            if (post.getAllowRepost() == 1) {
                isActive = true;
            } else {
                isActive = false;
            }
        } else {
            isActive = true;
        }
        if (type != 2) {
            list.add(new WallNewPostUploadFileTypeAdapter.WallNewPostUploadFileType(R.drawable.wall_new_post_repost_icon, R.drawable.wall_new_post_repost_icon_selected, "Allow repost to wall", "This would allow other people to share your post as a message or share on other people's wall", isActive, 0));
            list.add(new WallNewPostUploadFileTypeAdapter.WallNewPostUploadFileType(R.drawable.ic_gallery_gray, "Photo", "You can share upto 5 photos with other budz", 1));
            list.add(new WallNewPostUploadFileTypeAdapter.WallNewPostUploadFileType(R.drawable.wall_new_post_video_cam_icon, "Video", "Make your 20 seconds count to share with other budz", 2));
        }
        list.add(new WallNewPostUploadFileTypeAdapter.WallNewPostUploadFileType(R.drawable.wall_new_post_tag_icon, "Tag People", "Share the post with other people instantly by tagging them", 3));
        return list;
    }

    @Override
    public void onItemClick(Object obj, int pos, int type) {
        switch (pos) {
            case 0: {
                adapter.getDataSet().get(0).setActive(!adapter.getDataSet().get(0).isActive());
                adapter.notifyDataSetChanged();
            }
            break;
            case 1: {
                openCamera(false);
            }
            break;
            case 2: {
                openCamera(true);
            }
            break;
            case 3: {
                if (tagAdapter == null || tagAdapter.getFollowingUsers() == null || tagAdapter.getFollowingUsers().isEmpty()) {
                    Utility.launchActivityForResult(WallNewPostActivity.this, TagPeopleActivity.class, null, Flags.ACTIVITIES_COMMUNICATION_FLAG);
                } else {
                    Bundle b = new Bundle();
                    b.putSerializable("tags", new TagPeopleActivity.TagsSerialized((List<FollowingUser>) tagAdapter.getFollowingUsers()));
                    Utility.launchActivityForResult(WallNewPostActivity.this, TagPeopleActivity.class, b, Flags.ACTIVITIES_COMMUNICATION_FLAG);
                }
            }
            break;
        }
    }

    private void openCamera(boolean isVideo) {
        if (mediaAdapter.getCount() != 0) {
            int imgCount = 0, videoCount = 0;
            for (BudzFeedNewPostMediaViewPagerAdapter.NewPostMedia media : mediaAdapter.getMedias()) {
                if (media.isVideo()) {
                    ++videoCount;
                } else {
                    ++imgCount;
                }
            }
            if (!isVideo && imgCount == 5) {
                CustomeToast.ShowCustomToast(WallNewPostActivity.this, "You already added five images!", Gravity.TOP);
                return;
            }
            if (isVideo && videoCount == 1) {
                CustomeToast.ShowCustomToast(WallNewPostActivity.this, "You already added a video!", Gravity.TOP);
                return;
            }
        }
        Bundle b = new Bundle();
        b.putInt(Constants.CAMERA_ONLY_VIDEO, isVideo ? 2 : 1);
        Utility.launchActivityForResult(WallNewPostActivity.this, HBCameraActivity.class, b, isVideo ? Flags.VIDEO_CAPTUREING_REQUEST : Flags.PICTURE_CAPTUREING_REQUEST);
    }

    private void toggleBottomLayer(Boolean larg) {
        recyclerView.setLayoutManager(new LinearLayoutManager(WallNewPostActivity.this, larg ? LinearLayout.VERTICAL : LinearLayout.HORIZONTAL, false));
        adapter.setLarg(larg);
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onItemLongClick(Object obj, int pos, int type) {
        return false;
    }

    public String getUserImageSpecial() {
        String img = user.getSpecial_icon();
        if (img != null && !img.trim().equalsIgnoreCase("null") && img.length() > 5) {

            return URL.images_baseurl + img;

        } else {
            return "";
        }

    }

    public String getUserImage() {
        String img = user.getImage_path();
        if (img != null && !img.trim().equalsIgnoreCase("null") && img.trim().length() > 5) {
            if (img.contains("http")) {
                return img;
            } else {
                return URL.images_baseurl + img;
            }
        }
        img = user.getAvatar();
        if (img != null && !img.trim().equalsIgnoreCase("null")) {
            if (img.contains("http")) {
                return img;
            } else {
                return URL.images_baseurl + img;
            }
        }
        return URL.images_baseurl + "/";
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Flags.SESSION_OUT) {
            onSessionExpire();
            return;
        }

        if (data != null && data.getExtras() != null) {
            if (requestCode == Flags.VIDEO_CAPTUREING_REQUEST && data.getExtras().getInt("response_code_arg", 0) == HBCameraActivity.ACTION_CONFIRM) {
                String path = data.getExtras().getString("file_path_arg", null);
                if (path != null || !path.trim().isEmpty()) {
                    temp = new BudzFeedNewPostMediaViewPagerAdapter.NewPostMedia(path, true);
                    uploadMedia(temp);
                } else {
                    // nothing return video action
                }
            } else if (requestCode == Flags.PICTURE_CAPTUREING_REQUEST && data.getExtras().getInt("response_code_arg", 0) == HBCameraActivity.ACTION_CONFIRM) {
                String path = data.getExtras().getString("file_path_arg", null);
                if (path != null || !path.trim().isEmpty()) {
                    temp = new BudzFeedNewPostMediaViewPagerAdapter.NewPostMedia(path, false);
                    uploadMedia(temp);
                } else {
                    // nothing return Picture action
                }
            } else if (requestCode == Flags.ACTIVITIES_COMMUNICATION_FLAG && resultCode == Flags.NOTIFY) {
                TagPeopleActivity.TagsSerialized serialized = (TagPeopleActivity.TagsSerialized) data.getExtras().getSerializable("tags");
                if (serialized == null || serialized.getTags() == null || serialized.getTags().isEmpty()) {
                    //noting come back
                } else {
                    setTags(serialized.getTags());
                }
            }
        }
    }

    private void setTags(List<FollowingUser> tags) {
        if (tags == null || tags.isEmpty()) {
            tagParent.setVisibility(View.GONE);
            return;
        }
        tagParent.setVisibility(View.VISIBLE);
        tagAdapter.setFollowingUsers(tags);
    }

    private void uploadMedia(BudzFeedNewPostMediaViewPagerAdapter.NewPostMedia media) {
        if (!media.isVideo()) {
            Bitmap bitmapOrg = BitmapFactory.decodeFile(media.getPath());
            bitmapOrg = checkRotation(bitmapOrg, media.getPath());

//            new UploadImageAPIcall(WallNewPostActivity.this, URL.addPostImageURL, new BitmapDrawable(getResources(), bitmapOrg), user.getSession_key(), WallNewPostActivity.this, APIActions.ApiActions.wall_post_img_upload);
            new UploadImageAPIcall(WallNewPostActivity.this, URL.addPostImageURL, media.getPath(), user.getSession_key(), WallNewPostActivity.this, APIActions.ApiActions.wall_post_img_upload);
        } else {

            new UploadVideoAPIcall(WallNewPostActivity.this, URL.addPostVideoURL, media.getPath(), user.getSession_key(), WallNewPostActivity.this, APIActions.ApiActions.wall_post_video_upload);
        }
        dialog.show(getSupportFragmentManager(), "sendingg");
    }

    @Override
    public void onDialogItemClickListener(DialogInterface dialog, Object obj, int type) {
        mentionTagList.setVisibility(View.GONE);

        menUser = "";
        menHash = "";
        if (type == 0) {

            editText.insertMention((FollowingUser) obj);

//            editText.insertMention((FollowingUser) obj);
        } else {

            editText.insertMention((Tag) obj);

        }
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                editText.setText(editText.getText().toString().replace(selectedTagg,""));
//                selectedTagg = "";
//            }
//        },10);
    }

    public void getMentionUsers() {
        if (Utility.isNetworkAvailable(getApplicationContext())) {
            RestClient.getInstance(getApplicationContext()).getApiService().getFollowingUsers(user.getUser_id(), user.getSession_key()).enqueue(new Callback<FollowingUserResponse>() {
                @Override
                public void onResponse(Call<FollowingUserResponse> call, final Response<FollowingUserResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Utility.getApiStatusCallBack(response.body().getStatus(), response.body().getErrorMessage(), new ApiStatusCallBack() {
                            @Override
                            public void success() {
                                WallSinglton.getInstance().setFollowingUserList(response.body().getSuccessData());
                            }

                            @Override
                            public void sessionExpire() {
                                onSessionExpire();
                            }

                            @Override
                            public void knownError(String errorMsg) {
                                CustomeToast.ShowCustomToast(WallNewPostActivity.this, errorMsg, Gravity.TOP);
                            }

                            @Override
                            public void unKnownError() {
                                showUnknownError();
                            }
                        });
                    } else {
                        showUnknownError();
                    }
                }

                @Override
                public void onFailure(Call<FollowingUserResponse> call, Throwable t) {
                    showUnknownError();
                }
            });
        } else {
            CustomeToast.ShowCustomToast(WallNewPostActivity.this, getString(R.string.network_not_connected_error), Gravity.TOP);
        }
        RestClient.getInstance(WallNewPostActivity.this).getApiService().getSubAllUsers(user.getSession_key()).enqueue(new Callback<SubUsersResponse>() {
            @Override
            public void onResponse(Call<SubUsersResponse> call, final Response<SubUsersResponse> response) {

                if (response.isSuccessful() && response.body() != null) {
                    Utility.getApiStatusCallBack(response.body().getStatus(), response.body().getErrorMessage(), new ApiStatusCallBack() {
                        @Override
                        public void success() {
                            try {
                                for (int i = 0; i < response.body().getSuccessData().getSubUsers().size(); i++) {
                                    FollowingUser e = new FollowingUser();
                                    e.setId(response.body().getSuccessData().getSubUsers().get(i).getId());
                                    e.setFirstName(response.body().getSuccessData().getSubUsers().get(i).getTitle());
                                    e.setLastName("");
                                    e.setId(response.body().getSuccessData().getSubUsers().get(i).getId());
                                    e.setSubUser(true);
                                    WallSinglton.getInstance().getFollowingUserList().add(e);
                                }

                            } catch (Exception e) {
//                                WallNewPostActivity.this.subUsers = null;
                            }


                        }

                        @Override
                        public void sessionExpire() {
                            onSessionExpire();
                        }

                        @Override
                        public void knownError(String errorMsg) {
                            CustomeToast.ShowCustomToast(WallNewPostActivity.this, errorMsg, Gravity.TOP);
                        }

                        @Override
                        public void unKnownError() {
                            onSessionExpire();
                        }
                    });
                } else {

                }
            }

            @Override
            public void onFailure(Call<SubUsersResponse> call, Throwable t) {
                onSessionExpire();
            }
        });

    }

    @Override
    public void onDataReady(final Preview preview) {
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                scrapLayout.setVisibility(View.VISIBLE);
////                scrapLayout.setMessage(preview.getLink(), ContextCompat.getColor(WallNewPostActivity.this, R.color.color_link));
//            }
//        });
    }

    public void getSubUsers() {
        if (!Utility.isNetworkAvailable(getApplicationContext())) {
            Dialogs.showNetworkNotAvailibleDialog(WallNewPostActivity.this);
            return;
        }
        final com.codingpixel.healingbudz.customeUI.ProgressDialog pd = com.codingpixel.healingbudz.customeUI.ProgressDialog.newInstance();
        pd.show(this.getSupportFragmentManager(), "pd");
        RestClient.getInstance(getApplicationContext()).getApiService().getSubUsers(user.getSession_key()).enqueue(new Callback<SubUsersResponse>() {
            @Override
            public void onResponse(Call<SubUsersResponse> call, final Response<SubUsersResponse> response) {
                pd.dismiss();
                if (response.isSuccessful() && response.body() != null) {
                    Utility.getApiStatusCallBack(response.body().getStatus(), response.body().getErrorMessage(), new ApiStatusCallBack() {
                        @Override
                        public void success() {
                            try {
                                WallNewPostActivity.this.subUsers = new ArrayList<>();
                                SubUser s = new SubUser();
                                s.setTitle(user.getFirst_name());
                                WallNewPostActivity.this.subUsers.add(s);
                                WallNewPostActivity.this.subUsers.addAll(response.body().getSuccessData().getSubUsers());
                                if (subUsers.size() == 1) {
                                    spinnerParent.setVisibility(View.GONE);
                                } else {
                                    spinnerParent.setVisibility(View.VISIBLE);
                                }
//                                for (int i = 0; i < subUsers.size(); i++) {
//                                    FollowingUser e = new FollowingUser();
//                                    e.setId(subUsers.get(i).getId());
//                                    e.setFirstName(subUsers.get(i).getTitle());
//                                    e.setLastName("");
//                                    e.setId(subUsers.get(i).getId());
//                                    e.setSubUser(true);
//                                    WallSinglton.getInstance().getFollowingUserList().add(e);
//                                }


                            } catch (Exception e) {
                                WallNewPostActivity.this.subUsers = null;
                            }

                            notifySubUserView();
                        }

                        @Override
                        public void sessionExpire() {
                            onSessionExpire();
                        }

                        @Override
                        public void knownError(String errorMsg) {
                            CustomeToast.ShowCustomToast(WallNewPostActivity.this, errorMsg, Gravity.TOP);
                        }

                        @Override
                        public void unKnownError() {
                            showUnknownError();
                        }
                    });
                } else {
                    showUnknownError();
                }
            }

            @Override
            public void onFailure(Call<SubUsersResponse> call, Throwable t) {
                pd.dismiss();
                showUnknownError();
            }
        });
    }

    private void notifySubUserView() {
        if (subUsers.size() == 1) {
            spinnerParent.setVisibility(View.GONE);
        } else {
            spinnerParent.setVisibility(View.VISIBLE);
        }
        if (subUsers == null || subUsers.size() == 1) {
            drop_down_ic.setVisibility(View.INVISIBLE);
        }
        tvPostAs.setText(user.getFirst_name());
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public static class MediaUploadResponce {
        @SerializedName("file")
        @Expose
        public String file;
        @SerializedName("thumb")
        @Expose
        public String thumb;
        @SerializedName("ratio")
        @Expose
        public String ratio;
        @SerializedName("poster")
        @Expose
        public String poster;
    }

    @Override
    public void onRequestSuccess(String response, APIActions.ApiActions apiActions) {
        try {
            if (dialog != null)
                dialog.dismiss();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        if (apiActions == APIActions.ApiActions.wall_post_img_upload) {
            if (response.contains("error")) {
                if (response.toLowerCase().contains("Session Expired".toLowerCase())) {
                    onSessionExpire();
                }
                return;
            }
            if (!response.trim().isEmpty() && response.contains("file")) {
                MediaUploadResponce responce = new Gson().fromJson(response, MediaUploadResponce.class);
                temp.url = responce.file;
                temp.thumb = responce.thumb;
                temp.ratio = responce.ratio;
                mediaAdapter.addMedia(temp);
                temp = null;
            }
            CustomeToast.ShowCustomToast(WallNewPostActivity.this, "Media Uploaded!", Gravity.TOP);
        } else if (apiActions == APIActions.ApiActions.wall_post_video_upload) {
            if (response.contains("error")) {
                if (response.toLowerCase().contains("Session Expired".toLowerCase())) {
                    onSessionExpire();
                }
                return;
            }

            if (!response.trim().isEmpty() && response.contains("poster") && response.contains("file")) {
                MediaUploadResponce responce = new Gson().fromJson(response, MediaUploadResponce.class);
                temp.url = responce.file;
                temp.poster = responce.poster;
                mediaAdapter.addMedia(temp);
                temp = null;
            }
            CustomeToast.ShowCustomToast(WallNewPostActivity.this, "Media Uploaded!", Gravity.TOP);
        }
    }

    @Override
    public void onRequestError(String response, APIActions.ApiActions apiActions) {
        try {
            if (dialog != null)
                dialog.dismiss();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        CustomeToast.ShowCustomToast(WallNewPostActivity.this, "Media Not Uploaded!", Gravity.TOP);
    }

    @Override
    public void onResponse(Call<GetAllPostResponse> call, final Response<GetAllPostResponse> response) {
        try {
            if (dialog != null)
                dialog.dismiss();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        if (response.isSuccessful() || response.body() != null) {
            Utility.getApiStatusCallBack(response.body().getStatus(), response.body().getErrorMessage(), new ApiStatusCallBack() {
                @Override
                public void success() {
                    String message = "Post created successfully";
                    if (post != null) {
                        message = "Post updated successfully";
                    }
                    new SweetAlertDialog(WallNewPostActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText(message)
                            .setContentText("")
                            .setConfirmText("Okay")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismissWithAnimation();
                                    if (response.body().getSuccessData().getPosts() == null || response.body().getSuccessData().getPosts().isEmpty()) {
                                        Utility.finishWithResult(WallNewPostActivity.this, null, Flags.ACTIVITY_COMMUNICATION_FOR_UPDATE_RESULT);
                                    } else {
                                        Bundle b = new Bundle();
                                        ExtraPost post = new ExtraPost();
                                        post.getPosts().add(response.body().getSuccessData().getPosts().get(0));
                                        b.putSerializable(Constants.POST_EXTRA, post);
                                        Utility.finishWithResult(WallNewPostActivity.this, b, Flags.NOTIFY_NEW_ELEMIENT);
                                    }
                                }
                            }).show();

                }

                @Override
                public void sessionExpire() {
                    onSessionExpire();
                }

                @Override
                public void knownError(String errorMsg) {
                    CustomeToast.ShowCustomToast(WallNewPostActivity.this, errorMsg, Gravity.TOP);
                }

                @Override
                public void unKnownError() {
                    showUnknownError();
                }
            });
        } else {
            showUnknownError();
        }
    }

    @Override
    public void onFailure(Call<GetAllPostResponse> call, Throwable t) {
        try {
            if (dialog != null)
                dialog.dismiss();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        showUnknownError();
    }

    private void getAllTags() {
        if (Utility.isNetworkAvailable(getApplicationContext())) {
            RestClient.getInstance(getApplicationContext()).getApiService().getAllTags(user.getSession_key()).enqueue(new Callback<TagResponse>() {
                @Override
                public void onResponse(Call<TagResponse> call, final Response<TagResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Utility.getApiStatusCallBack(response.body().getStatus(), response.body().getErrorMessage(), new ApiStatusCallBack() {
                            @Override
                            public void success() {
                                WallSinglton.getInstance().setHashTagList(response.body().getSuccessData());
                            }

                            @Override
                            public void sessionExpire() {
                                onSessionExpire();
                            }

                            @Override
                            public void knownError(String errorMsg) {
                                CustomeToast.ShowCustomToast(WallNewPostActivity.this, errorMsg, Gravity.TOP);
                            }

                            @Override
                            public void unKnownError() {
                                showUnknownError();
                            }
                        });
                    } else {
                        showUnknownError();
                    }
                }

                @Override
                public void onFailure(Call<TagResponse> call, Throwable t) {
                    showUnknownError();
                }
            });
        } else {
            CustomeToast.ShowCustomToast(WallNewPostActivity.this, getString(R.string.network_not_connected_error), Gravity.TOP);
        }
    }

    private int getCursorY() {
        int pos = editText.getSelectionStart();
        Layout layout = editText.getLayout();
        int line = layout.getLineForOffset(pos);
        int baseline = layout.getLineBaseline(line);
        int ascent = layout.getLineAscent(line);
        return (baseline + ascent);
    }

    private int getCursorX() {
        int pos = editText.getSelectionStart();
        Layout layout = editText.getLayout();
        if (layout != null) {
            return (int) layout.getPrimaryHorizontal(pos);
        } else {
            return 0;
        }
    }

    private String[] getPostDescription() throws StringIndexOutOfBoundsException, IndexOutOfBoundsException {
        StringBuilder builder = new StringBuilder();
        StringBuilder jsonExtra = new StringBuilder();
        jsonExtra.append("[");
        MentionsEditable editable = editText.getMentionsText().trim();
        if (editable.getMentionSpans() == null || editable.getMentionSpans().isEmpty()) {
            String[] arrayT = editText.getText().toString().trim().split(" ");
            for (String aPreJsonData : Splash.keywordList) {

                if (arrayT != null) {
                    if (arrayT.length > 0) {
                        for (int i = 0; i < arrayT.length; i++) {
                            if (arrayT[i].toLowerCase().equalsIgnoreCase(aPreJsonData.toLowerCase())) {
                                arrayT[i] = "#" + aPreJsonData;
                                jsonExtra.append(new Gson().toJson(new MentionTagJsonModel(String.valueOf(-5), "tag", aPreJsonData, "#")));
                                jsonExtra.append(",");
                            }
                        }
                    }
                }
            }

            StringBuffer addedd = new StringBuffer();
            for (int i = 0; i < arrayT.length; i++) {
                if (i == 0)
                    addedd.append(arrayT[i] + " ");
                else if (i == arrayT.length - 1) {
                    addedd.append(" " + arrayT[i]);
                } else if (i < arrayT.length) {
                    addedd.append(" " + arrayT[i] + " ");
                }
            }
            if (preJsonData != null && post != null) {
                for (MentionTagJsonModel aPreJsonData : preJsonData) {
                    if (!jsonExtra.toString().trim().toLowerCase().contains(aPreJsonData.getValue().toLowerCase().trim())) {
                        if (editText.getText().toString().toLowerCase().trim().contains(aPreJsonData.getTrigger().toLowerCase().trim() + aPreJsonData.getValue().toLowerCase().trim())) {
                            if (aPreJsonData.getTrigger().trim().equalsIgnoreCase("@")) {
                                if (aPreJsonData.getType().trim().equalsIgnoreCase("user")) {
                                    jsonExtra.append(new Gson().toJson(new MentionTagJsonModel(String.valueOf(aPreJsonData.getId()), "user", aPreJsonData.getValue(), "@")));
                                    jsonExtra.append(",");
                                } else {
                                    jsonExtra.append(new Gson().toJson(new MentionTagJsonModel(String.valueOf(aPreJsonData.getId()), "budz", aPreJsonData.getValue(), "@")));
                                    jsonExtra.append(",");
                                }
                            } else {
                                jsonExtra.append(new Gson().toJson(new MentionTagJsonModel(String.valueOf(aPreJsonData.getId()), "tag", aPreJsonData.getValue(), "#")));
                                jsonExtra.append(",");

                            }
                        }
                    }
                }
            }
            if (jsonExtra.length() > 1)
                jsonExtra = new StringBuilder(jsonExtra.substring(0, jsonExtra.lastIndexOf(",")));
            jsonExtra.append("]");
            return new String[]{addedd.toString().trim(), jsonExtra.toString()};
        }
        int start = 0;

        for (MentionSpan span : editable.getMentionSpans()) {
            int temp = editable.getSpanStart(span);
            builder.append(editable.toString().substring(start, temp));
            start = temp;
            if (span.getMention() instanceof FollowingUser) {
                FollowingUser user = (FollowingUser) span.getMention();
                String type = "user";
                if (user.isSubUser()) {
                    type = "budz";
                } else {
                    type = "user";
                }
                jsonExtra.append(new Gson().toJson(new MentionTagJsonModel(String.valueOf(user.getId()), type, user.getSuggestiblePrimaryText(), "@")));
                jsonExtra.append(",");
                builder.append("@");
            }
//            else if (span.getMention() instanceof MentionTagJsonModel) {
//                MentionTagJsonModel user = (MentionTagJsonModel) span.getMention();
//                jsonExtra.append(new Gson().toJson(new MentionTagJsonModel(String.valueOf(user.getId()), false, user.getSuggestiblePrimaryText())));
//                jsonExtra.append(",");
//                builder.append("#");
//            }
            else {
                Tag user = (Tag) span.getMention();
                jsonExtra.append(new Gson().toJson(new MentionTagJsonModel(String.valueOf(user.getId()), "tag", user.getSuggestiblePrimaryText(), "#")));
                jsonExtra.append(",");
                builder.append("#");
            }
            temp = editable.getSpanEnd(span);
            if (temp > (editable.toString().length() - 1)) {
                builder.append(editable.toString().substring(start));
            } else {
                builder.append(editable.toString().substring(start, temp));
            }
            start = temp;
        }
        if (start < editable.toString().length()) {
            builder.append(editable.toString().substring(start));
        }

        if (preJsonData != null && post != null) {
            for (MentionTagJsonModel aPreJsonData : preJsonData) {
                if (!jsonExtra.toString().trim().toLowerCase().contains(aPreJsonData.getValue().toLowerCase().trim())) {
                    if (editText.getText().toString().toLowerCase().trim().contains(aPreJsonData.getTrigger().toLowerCase().trim() + aPreJsonData.getValue().toLowerCase().trim())) {
                        if (aPreJsonData.getTrigger().trim().equalsIgnoreCase("@")) {
                            if (aPreJsonData.getType().trim().equalsIgnoreCase("user")) {
                                jsonExtra.append(new Gson().toJson(new MentionTagJsonModel(String.valueOf(aPreJsonData.getId()), "user", aPreJsonData.getValue(), "@")));
                                jsonExtra.append(",");
                            } else {
                                jsonExtra.append(new Gson().toJson(new MentionTagJsonModel(String.valueOf(aPreJsonData.getId()), "budz", aPreJsonData.getValue(), "@")));
                                jsonExtra.append(",");
                            }
                        } else {
                            jsonExtra.append(new Gson().toJson(new MentionTagJsonModel(String.valueOf(aPreJsonData.getId()), "tag", aPreJsonData.getValue(), "#")));
                            jsonExtra.append(",");

                        }
                    }
                }
            }
        }
        String[] array = builder.toString().trim().split(" ");
        for (String aPreJsonData : Splash.keywordList) {

            if (array != null) {
                if (array.length > 0) {
                    for (int i = 0; i < array.length; i++) {
                        if (array[i].toLowerCase().equalsIgnoreCase(aPreJsonData.toLowerCase())) {
                            array[i] = "#" + aPreJsonData;
                            jsonExtra.append(new Gson().toJson(new MentionTagJsonModel(String.valueOf(-5), "tag", aPreJsonData, "#")));
                            jsonExtra.append(",");
                        }
                    }
                }
            }
        }

        jsonExtra = new StringBuilder(jsonExtra.substring(0, jsonExtra.lastIndexOf(",")));
        jsonExtra.append("]");
        StringBuffer addedd = new StringBuffer();
        int ll = array.length - 1;
        for (int i = 0; i < array.length; i++) {
            if (i == 0)
                addedd.append(array[i] + " ");
            else if (i == ll) {
                addedd.append(" " + array[i]);
            } else if (i < array.length) {
                addedd.append(" " + array[i] + " ");
            }
        }
        return new String[]{addedd.toString().trim(), jsonExtra.toString()};
    }

    @Override
    protected void onDestroy() {
        HideKeyboard(WallNewPostActivity.this);
        super.onDestroy();
    }
}
