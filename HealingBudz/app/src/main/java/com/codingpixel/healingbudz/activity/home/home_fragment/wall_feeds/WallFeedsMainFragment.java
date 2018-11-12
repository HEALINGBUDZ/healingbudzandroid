package com.codingpixel.healingbudz.activity.home.home_fragment.wall_feeds;
/*
 * Created by M_Muzammil Sharif on 05-Mar-18.
 */

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.codingpixel.healingbudz.DataModel.User;
import com.codingpixel.healingbudz.R;
import com.codingpixel.healingbudz.ReportItView.WallTopDropDowns;
import com.codingpixel.healingbudz.Utilities.Constants;
import com.codingpixel.healingbudz.Utilities.Dialogs;
import com.codingpixel.healingbudz.Utilities.Flags;
import com.codingpixel.healingbudz.Utilities.KeywordClickDialog;
import com.codingpixel.healingbudz.Utilities.SetUserValuesInSP;
import com.codingpixel.healingbudz.Utilities.Utility;
import com.codingpixel.healingbudz.activity.Wall.WallNewFeedOtherActivity;
import com.codingpixel.healingbudz.activity.Wall.WallNewPostActivity;
import com.codingpixel.healingbudz.activity.Wall.WallPostDetailActivity;
import com.codingpixel.healingbudz.activity.Wall.WallPostPhotoDetailActivity;
import com.codingpixel.healingbudz.activity.Wall.WallSinglton;
import com.codingpixel.healingbudz.activity.home.HomeActivity;
import com.codingpixel.healingbudz.activity.home.buz_feed.BuzzFeedActivity;
import com.codingpixel.healingbudz.activity.home.home_fragment.HomeGlobalSearchActivity;
import com.codingpixel.healingbudz.activity.home.home_fragment.budz_map_tab.BudzMapDetailsActivity;
import com.codingpixel.healingbudz.activity.shoot_out.ShootOutActivity;
import com.codingpixel.healingbudz.activity.splash.Splash;
import com.codingpixel.healingbudz.adapter.BudzFeedMainPostAdapter;
import com.codingpixel.healingbudz.customeUI.CircularImageView;
import com.codingpixel.healingbudz.customeUI.CustomeToast;
import com.codingpixel.healingbudz.customeUI.customalerts.SweetAlertDialog;
import com.codingpixel.healingbudz.interfaces.ApiStatusCallBack;
import com.codingpixel.healingbudz.interfaces.AutoLinkTextLinkClickListener;
import com.codingpixel.healingbudz.interfaces.CounterCallback;
import com.codingpixel.healingbudz.interfaces.RecyclerViewItemClickListener;
import com.codingpixel.healingbudz.interfaces.ScrollPositionEndCallBack;
import com.codingpixel.healingbudz.interfaces.UserFollowCallBack;
import com.codingpixel.healingbudz.media_view_Dialog.MediPreview;
import com.codingpixel.healingbudz.network.BudzFeedModel.ExtraPost;
import com.codingpixel.healingbudz.network.BudzFeedModel.FollowingUserModel.FollowingUser;
import com.codingpixel.healingbudz.network.BudzFeedModel.FollowingUserModel.FollowingUserResponse;
import com.codingpixel.healingbudz.network.BudzFeedModel.GeneralResponse;
import com.codingpixel.healingbudz.network.BudzFeedModel.GetAllPost.GetAllPostResponse;
import com.codingpixel.healingbudz.network.BudzFeedModel.GetAllPost.Post;
import com.codingpixel.healingbudz.network.BudzFeedModel.GetAllPost.SharedUser;
import com.codingpixel.healingbudz.network.BudzFeedModel.MentionTagJsonModel;
import com.codingpixel.healingbudz.network.BudzFeedModel.MutePostModel.MutePostResponse;
import com.codingpixel.healingbudz.network.BudzFeedModel.ReportModel.FlagPost;
import com.codingpixel.healingbudz.network.BudzFeedModel.ReportModel.ReportPostResponse;
import com.codingpixel.healingbudz.network.BudzFeedModel.SubUsers.SubUser;
import com.codingpixel.healingbudz.network.BudzFeedModel.SubUsers.SubUsersResponse;
import com.codingpixel.healingbudz.network.BudzFeedModel.TagModel.TagResponse;
import com.codingpixel.healingbudz.network.BudzFeedModel.submitPostLike.SubmitPostLikeResponse;
import com.codingpixel.healingbudz.network.RestClient;
import com.codingpixel.healingbudz.network.model.URL;
import com.luseen.autolinklibrary.AutoLinkMode;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.codingpixel.healingbudz.activity.home.home_fragment.qa_tab.DiscussQuestionFragment.isNewScreen;
import static com.codingpixel.healingbudz.activity.home.side_menu.profile.UserProfileActivity.profiledataModel;
import static com.codingpixel.healingbudz.adapter.BudzFeedMainPostAdapter.isLikedUser;
import static com.codingpixel.healingbudz.static_function.IntentFunction.GoTo;
import static com.codingpixel.healingbudz.static_function.IntentFunction.GoToProfile;
import static com.codingpixel.healingbudz.static_function.ShareIntent.ShareCallApi;

public class WallFeedsMainFragment extends Fragment implements CounterCallback, View.OnClickListener, RecyclerViewItemClickListener, SwipeRefreshLayout.OnRefreshListener, AutoLinkTextLinkClickListener, Callback<GetAllPostResponse> {
    private BudzFeedMainPostAdapter adapter;
    private SwipeRefreshLayout refreshLayout;
    private WallTopDropDowns reportView;
    private CircularImageView imageView;
    private ImageView profile_img_topi;
    private TextView title;
    private User user;
    private int pageNo = 0;
    private boolean isOnFinalPage = false;
    private static int filter = 0;
    private Integer userId = null;
    private ProgressDialog pDialog;
    private UserFollowCallBack followCallBack = null;
    private boolean isFromProfile = false;
    LinearLayoutManager linearLayoutManager;
    TextView not_found, feed_other;
    LinearLayout not_found_main, notify_layout;
    public TextView Shout_Out_Counter, Budz_Feed_Counter;
    RecyclerView recyclerView;
    public RelativeLayout Home_Buz_Feed_btn, Home_Shoot_out_btn;
    ImageView Home_search_btn;
    private boolean isAppiCallActive = false;


    @SuppressLint("ValidFragment")
    public WallFeedsMainFragment(int USER_ID, boolean isFromProfile, @NonNull WallTopDropDowns dropDowns, @NonNull UserFollowCallBack callBack) {
        if (dropDowns != null) {
            reportView = dropDowns;
        }
        this.isFromProfile = isFromProfile;
        this.followCallBack = callBack;
        userId = USER_ID;

    }

    public WallFeedsMainFragment() {
        userId = null;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*if (getArguments() != null) {
            userId = getArguments().getInt("USER_ID", -5319);
        }*/
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (userId == null || !isFromProfile) {
            View view = inflater.inflate(R.layout.fragment_home_wall_main, container, false);
            not_found_main = view.findViewById(R.id.not_found);
            notify_layout = view.findViewById(R.id.notify_layout);
            Home_search_btn = (ImageView) view.findViewById(R.id.home_search);
            Home_search_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    GoTo(v.getContext(), HomeGlobalSearchActivity.class);
                }
            });
            Shout_Out_Counter = (TextView) view.findViewById(R.id.shoor_out_counter);
            Budz_Feed_Counter = (TextView) view.findViewById(R.id.budz_counter);
            Home_Buz_Feed_btn = (RelativeLayout) view.findViewById(R.id.buz_feed);
            Home_Buz_Feed_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Budz_Feed_Counter.setVisibility(View.GONE);
                    GoTo(v.getContext(), BuzzFeedActivity.class);
                }
            });

            Home_Shoot_out_btn = (RelativeLayout) view.findViewById(R.id.shoot_out);
            Home_Shoot_out_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Shout_Out_Counter.setVisibility(View.GONE);
                    GoTo(v.getContext(), ShootOutActivity.class);
                }
            });
            return view;
        } else {
            View view = inflater.inflate(R.layout.fragment_home_wall_profile, container, false);
            not_found = view.findViewById(R.id.not_found);
            feed_other = view.findViewById(R.id.feed_other);
            feed_other.setVisibility(View.GONE);
            feed_other.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (adapter.getItemCount() > 0) {
                        WallNewFeedOtherActivity.USER_FEED_ID = userId;
                        GoTo(v.getContext(), WallNewFeedOtherActivity.class);

                    }
                }
            });
            return view;
        }
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        /*if (getArguments() != null) {
            userId = getArguments().getInt("USER_ID", -5319);
        }*/
        user = SetUserValuesInSP.getSavedUser(view.getContext());
        if (user == null) {
            onSessionExpire();
            return;
        }
        pDialog = new ProgressDialog(getActivity());
        pDialog.setCancelable(false);

        title = view.findViewById(R.id.fragment_home_wall_main_title);


        recyclerView = (RecyclerView) view.findViewById(R.id.fragment_home_wall_main_post_list);

        linearLayoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        if (userId == null || !isFromProfile) {
            view.findViewById(R.id.fragment_wall_main_drawer_btn).setOnClickListener(WallFeedsMainFragment.this);

            if (userId != null) {
                view.findViewById(R.id.fragment_wall_main_add_btn).setVisibility(View.GONE);
                view.findViewById(R.id.fragment_wall_main_feed_new).setVisibility(View.GONE);
                view.findViewById(R.id.fragment_wall_main_feed_filter_icon).setVisibility(View.GONE);
                view.findViewById(R.id.feed_pic).setVisibility(View.GONE);
                title.setText("My Buzz");
                title.setVisibility(View.VISIBLE);
            } else {
                title.setText("The Buzz");
                title.setVisibility(View.VISIBLE);
                notify_layout.setVisibility(View.VISIBLE);
                view.findViewById(R.id.feed_pic).setVisibility(View.GONE);
                view.findViewById(R.id.fragment_wall_main_feed_filter_icon).setVisibility(View.VISIBLE);
                view.findViewById(R.id.fragment_wall_main_feed_filter_icon).setOnClickListener(WallFeedsMainFragment.this);

                view.findViewById(R.id.fragment_wall_main_add_btn).setVisibility(View.VISIBLE);
                view.findViewById(R.id.fragment_wall_main_feed_new).setVisibility(View.VISIBLE);
                view.findViewById(R.id.fragment_wall_main_add_btn).setOnClickListener(WallFeedsMainFragment.this);
                view.findViewById(R.id.fragment_wall_main_feed_new).setOnClickListener(WallFeedsMainFragment.this);

                imageView = view.findViewById(R.id.fragment_wall_main_feed_user_img);
                imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                profile_img_topi = view.findViewById(R.id.profile_img_topi);
                Glide.with(view.getContext()).load(getUserImage()).asBitmap().diskCacheStrategy(DiskCacheStrategy.SOURCE).fitCenter()
                        .placeholder(Utility.getProfilePlaceHolder(user.getPoints())).into(imageView);
                if (getUserImageSpecial().length() > 5) {
                    profile_img_topi.setVisibility(View.VISIBLE);
                    Glide.with(view.getContext()).load(getUserImageSpecial()).asBitmap().diskCacheStrategy(DiskCacheStrategy.SOURCE)
                            .placeholder(Utility.getProfilePlaceHolder(user.getPoints())).into(profile_img_topi);
                } else {
                    profile_img_topi.setVisibility(View.GONE);
                }
            }
            refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.fragment_home_wall_main_post_list_refresher);
            refreshLayout.setColorSchemeColors(Color.parseColor("#7CC245"));
            refreshLayout.setProgressBackgroundColorSchemeColor(Color.parseColor("#252222"));
            refreshLayout.setOnRefreshListener(WallFeedsMainFragment.this);

            reportView = new WallTopDropDowns(view.getContext());
            RelativeLayout layout = (RelativeLayout) view.findViewById(R.id.fragment_home_wall_main_layout);
            layout.addView(reportView.getView());
        } else {
            not_found = view.findViewById(R.id.not_found);
            ViewCompat.setNestedScrollingEnabled(recyclerView, false);
        }

        if (adapter == null) {
            adapter = new BudzFeedMainPostAdapter(this, user, WallFeedsMainFragment.this, WallFeedsMainFragment.this, getActivity());
            adapter.setScrll(new BudzFeedMainPostAdapter.scrollBis() {
                @Override
                public void setScrollingEnabled(boolean isEnabled) {
                    if (isEnabled) {
                        recyclerView.setNestedScrollingEnabled(false);
                        recyclerView.setLayoutFrozen(false);
//                        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//                            @Override
//                            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                                super.onScrollStateChanged(recyclerView, newState);
//
//                                if (!recyclerView.canScrollVertically(1)) {
//                                    Log.e("Muzammil", "END");
//                                    if (!isFromProfile) {
//                                        callAPI();
//                                    }
//                                } else if (!recyclerView.canScrollVertically(-1)) {
//                                    Log.e("Muzammil", "START");
//                                }
//                            }
//                        });
                    } else {
                        recyclerView.setNestedScrollingEnabled(true);
                        recyclerView.setLayoutFrozen(true);
//                        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//                            @Override
//                            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                                super.onScrollStateChanged(recyclerView, newState);
//                                recyclerView.stopScroll();
//                            }
//                        });
                    }
                }

            });
            if (userId != null && isFromProfile) {
                adapter.setCallBack(new ScrollPositionEndCallBack() {
                    @Override
                    public void onScrollPositionEnd() {
//                        linearLayoutManager
                        LinearLayoutManager layoutManager = LinearLayoutManager.class.cast(recyclerView.getLayoutManager());
                        int lastVisible = layoutManager.findLastVisibleItemPosition();
                        Log.d("vissible", lastVisible + "");
                        Log.d("vissible_t", (lastVisible % 10) + "");
                        if (lastVisible >= (pageNo + 1) * 9 && !isAppiCallActive) {
                            if ((lastVisible % 9) == 0) {
//                                if (isFromProfile) {
//                                    callAPI();
//                                }
                            }
                        }

                    }

                    @Override
                    public void onScrollPositionStart() {

                    }

                    @Override
                    public void setScrollingEnabled(boolean isEnabled) {
                        if (isEnabled) {
                            recyclerView.setNestedScrollingEnabled(true);

                            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                                @Override
                                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                                    super.onScrollStateChanged(recyclerView, newState);

                                    if (!recyclerView.canScrollVertically(1)) {
                                        Log.e("Muzammil", "END");
                                        if (!isFromProfile) {
                                            callAPI();
                                        }
                                    } else if (!recyclerView.canScrollVertically(-1)) {
                                        Log.e("Muzammil", "START");
                                    }
                                }
                            });
                        } else {
                            recyclerView.setNestedScrollingEnabled(false);

                            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                                @Override
                                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                                    super.onScrollStateChanged(recyclerView, newState);
                                    recyclerView.stopScroll();
                                }
                            });
                        }
                    }
                });
            }
            //&& profiledataModel.getIs_following_count() > 0
            if (profiledataModel != null && userId != null && isFromProfile) {
                callAPI();
            } else if (userId == null || !isFromProfile) {
                callAPI();
            } else if (isFromProfile && userId == Splash.user.getUser_id()) {
                callAPI();
            }

        }
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!recyclerView.canScrollVertically(1)) {
                    Log.e("Muzammil", "END");
                    if (!isFromProfile) {
                        callAPI();
                    }
                } else if (!recyclerView.canScrollVertically(-1)) {
                    Log.e("Muzammil", "START");
                }
            }
        });
        if (adapter.getItemCount() > 0) {
            if (feed_other != null) {
                feed_other.setVisibility(View.VISIBLE);
            }
            if (not_found != null) {
                not_found.setVisibility(View.GONE);
            }
        } else {
            if (feed_other != null) {
                feed_other.setVisibility(View.GONE);
            }
            if (not_found != null) {
                not_found.setVisibility(View.VISIBLE);
            }
        }
        getMentionUsers();
        getAllTags();
    }

    private void getAllTags() {
        if (getContext() == null) {
            //fragment die...
            return;
        }
        if (Utility.isNetworkAvailable(getContext())) {
            RestClient.getInstance(getContext()).getApiService().getAllTags(user.getSession_key()).enqueue(new Callback<TagResponse>() {
                @Override
                public void onResponse(Call<TagResponse> call, final Response<TagResponse> response) {
                    if (getContext() == null) {
                        //fragment die...
                        return;
                    }
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
                                CustomeToast.ShowCustomToast(getContext(), errorMsg, Gravity.TOP);
                            }

                            @Override
                            public void unKnownError() {
                                showunKnownSeverErrorDialog();
                            }
                        });
                    } else {
                        showunKnownSeverErrorDialog();
                    }
                }

                @Override
                public void onFailure(Call<TagResponse> call, Throwable t) {
                    showunKnownSeverErrorDialog();
                }
            });
        } else {
            CustomeToast.ShowCustomToast(getContext(), getString(R.string.network_not_connected_error), Gravity.TOP);
        }
    }

    public void getMentionUsers() {
        if (getContext() == null) {
            //fragment die...
            return;
        }
        if (Utility.isNetworkAvailable(getContext())) {
            RestClient.getInstance(getContext()).getApiService().getFollowingUsers(user.getUser_id(), user.getSession_key()).enqueue(new Callback<FollowingUserResponse>() {
                @Override
                public void onResponse(Call<FollowingUserResponse> call, final Response<FollowingUserResponse> response) {
                    if (getContext() == null) {
                        //fragment die...
                        return;
                    }
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
                                CustomeToast.ShowCustomToast(getContext(), errorMsg, Gravity.TOP);
                            }

                            @Override
                            public void unKnownError() {
                                showunKnownSeverErrorDialog();
                            }
                        });
                    } else {
                        showunKnownSeverErrorDialog();
                    }
                }

                @Override
                public void onFailure(Call<FollowingUserResponse> call, Throwable t) {
                    showunKnownSeverErrorDialog();
                }
            });
        } else {
            CustomeToast.ShowCustomToast(getContext(), getString(R.string.network_not_connected_error), Gravity.TOP);
        }
        RestClient.getInstance(getContext()).getApiService().getSubAllUsers(user.getSession_key()).enqueue(new Callback<SubUsersResponse>() {
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
                            CustomeToast.ShowCustomToast(getContext(), errorMsg, Gravity.TOP);
                        }

                        @Override
                        public void unKnownError() {

                        }
                    });
                } else {

                }
            }

            @Override
            public void onFailure(Call<SubUsersResponse> call, Throwable t) {

            }
        });

    }

    private void callAPI() {
        isAppiCallActive = true;
        if (getContext() == null) {
            // fragment die...
            return;
        }
        if (!Utility.isNetworkAvailable(getContext())) {
            Dialogs.showNetworkNotAvailibleDialog(getContext());
            return;
        }
        if (isOnFinalPage) {
            return;
        }
        if (refreshLayout != null) {
            refreshLayout.setRefreshing(true);
        }
        if (userId == null) {
            RestClient.getInstance(getContext()).getApiService().getWallFeeds(user.getSession_key(), pageNo, Constants.POST_FILTERS[filter].equalsIgnoreCase("Followers First") ? "" : Constants.POST_FILTERS[filter]).enqueue(WallFeedsMainFragment.this);
        } else {
            RestClient.getInstance(getContext()).getApiService().getUserPost(user.getSession_key(), userId, pageNo).enqueue(WallFeedsMainFragment.this);
        }
    }

    private void onSessionExpire() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fragment_wall_main_drawer_btn: {
                if (!HomeActivity.drawerLayout.isDrawerOpen(Gravity.START)) {
                    HomeActivity.drawerLayout.openDrawer(Gravity.START);
                }
            }
            break;
            case R.id.fragment_wall_main_add_btn: {
                Utility.launchActivityForResultFromFragment(WallFeedsMainFragment.this, getActivity(), WallNewPostActivity.class, null, Flags.ACTIVITIES_COMMUNICATION_FLAG);
            }
            break;
            case R.id.fragment_wall_main_feed_new: {
                Utility.launchActivityForResultFromFragment(WallFeedsMainFragment.this, getActivity(), WallNewPostActivity.class, null, Flags.ACTIVITIES_COMMUNICATION_FLAG);
            }
            break;
            case R.id.fragment_wall_main_feed_filter_icon: {
                if (reportView != null) {
                    reportView.showFilterDropdown(new Dialogs.DialogItemClickListener() {
                        @Override
                        public void onDialogItemClickListener(DialogInterface dialog, Object obj, int type) {
                            reportView.dismissSlide();
                            if (Constants.POST_FILTERS[0].equals((String) obj)) {
                                filter = 0;
                                pageNo = 0;
                                isOnFinalPage = false;
                                isAppiCallActive = true;
                                callAPI();
                            } else if (Constants.POST_FILTERS[1].equals((String) obj)) {
                                filter = 1;
                                pageNo = 0;
                                isOnFinalPage = false;
                                callAPI();
                            } else {
                                filter = 2;
                                pageNo = 0;
                                isOnFinalPage = false;
                                callAPI();
                            }
                        }
                    }, filter);
                }
            }
        }
    }

    @Override
    public void onItemClick(final Object obj, int pos, int type) {
        if (obj != null && obj instanceof Post) {
            /* type cases
             *   -1      => nomal item click
             *   -2      => file item click && POS => file pos
             *   0->20   => likes
             *   21->30  => more i.e. flag, share, etc.
             *   */
            switch (type) {
                case -1: {
                    Bundle b = new Bundle();
                    b.putSerializable(Constants.POST_EXTRA, (Post) obj);
                    Utility.launchActivityForResultFromFragment(WallFeedsMainFragment.this, getActivity(), WallPostDetailActivity.class, b, Flags.ACTIVITIES_COMMUNICATION_FLAG);
                }
                break;
                case -2: {
                    if (((Post) obj).getFiles().get(pos).getType().equalsIgnoreCase("video")) {
                        Bundle b = new Bundle();
                        b.putString("path", URL.videos_baseurl + ((Post) obj).getFiles().get(pos).getFile());
                        b.putBoolean("isvideo", true);
                        Utility.launchActivity(getActivity(), MediPreview.class, false, b);
                    } else {
                        Bundle b = new Bundle();
                        b.putSerializable(Constants.POST_EXTRA, (Post) obj);
                        b.putInt(Constants.POST_FILE_POS, pos);
                        Utility.launchActivityForResultFromFragment(WallFeedsMainFragment.this, getActivity(), WallPostPhotoDetailActivity.class, b, Flags.ACTIVITIES_COMMUNICATION_FLAG);
                    }
                }
                break;
                default: {
                    if (type >= 0 && type <= 20) {
                        submitPostLike(((Post) obj).getId(), type);
                    } else if (type >= 21 && type <= 30) {
                        switch (type - 21) {
                            case 0: {
                                JSONObject data_object = new JSONObject();
                                try {
                                    data_object.put("id", ((Post) obj).getId());
                                    data_object.put("type", "Post");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                ShareCallApi(getContext(), data_object);
                                Utility.shareString(getContext(), URL.getPostUrl(((Post) obj).getId()));
                            }
                            break;
                            case 1: {
                                flagPost((Post) obj);
                            }
                            break;
                            case 2: {
                                muteUnmutePost((Post) obj);
                            }
                            break;
                            case 3: {
                                followUnFollowBud(((Post) obj).getUser());
                            }
                            break;
                            case 4: {
                                Bundle b = new Bundle();
                                b.putSerializable(Constants.POST_EXTRA, (Post) obj);
                                b.putInt("type_int", 1);
                                Utility.launchActivityForResultFromFragment(WallFeedsMainFragment.this, getActivity(), WallNewPostActivity.class, b, Flags.ACTIVITY_COMMUNICATION_FOR_UPDATE_FLAG);
                            }
                            break;
                            case 5: {
                                new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                                        .setTitleText("Are you sure?")
                                        .setContentText("You want to delete this post?")
                                        .setConfirmText("Yes, delete it!")
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                sweetAlertDialog.dismissWithAnimation();
//                                                JSONObject jsonObject = new JSONObject();
//                                                new VollyAPICall(MyAnswersActivity.this, false, delete_answer + "/" + main_data.get(position).getId(), jsonObject, user.getSession_key(), Request.Method.GET, MyAnswersActivity.this, APIActions.ApiActions.delete_answer);
//                                                main_data.remove(position);
//                                                recyler_adapter.notifyDataSetChanged();
                                                final com.codingpixel.healingbudz.customeUI.ProgressDialog pd = com.codingpixel.healingbudz.customeUI.ProgressDialog.newInstance();
                                                pd.show(((FragmentActivity) getContext()).getSupportFragmentManager(), "pd");

                                                RestClient.getInstance(getContext()).getApiService().deletePost(user.getSession_key(), ((Post) obj).getId()).
                                                        enqueue(new Callback<GeneralResponse>() {
                                                            @Override
                                                            public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
//                                                                pDialog.dismiss();
                                                                pd.dismiss();
                                                                if (response.isSuccessful() || response.body() != null) {
                                                                    Utility.getApiStatusCallBack(response.body().getStatus(), response.body().getErrorMessage(), new ApiStatusCallBack() {
                                                                        @Override
                                                                        public void success() {
                                                                            adapter.removePost((Post) obj);
                                                                        }

                                                                        @Override
                                                                        public void sessionExpire() {
                                                                            onSessionExpire();
                                                                        }

                                                                        @Override
                                                                        public void knownError(String errorMsg) {
                                                                            CustomeToast.ShowCustomToast(getContext(), errorMsg, Gravity.TOP);
                                                                        }

                                                                        @Override
                                                                        public void unKnownError() {
                                                                            showunKnownSeverErrorDialog();
                                                                        }
                                                                    });
                                                                } else {
                                                                    showunKnownSeverErrorDialog();
                                                                }
                                                            }

                                                            @Override
                                                            public void onFailure(Call<GeneralResponse> call, Throwable t) {
                                                                showunKnownSeverErrorDialog();
                                                            }
                                                        });
                                            }
                                        })
                                        .setCancelText("Close!")
                                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                sweetAlertDialog.dismissWithAnimation();
                                            }
                                        })
                                        .show();
//                                Dialogs.showDeleteDialog(getContext(), (Post) obj, new Dialogs.DialogItemClickListener() {
//                                    @Override
//                                    public void onDialogItemClickListener(DialogInterface d, final Object o, int type) {
//                                        if (d != null) {
//                                            d.dismiss();
//                                        }
//                                        if (type == 1) {
//                                            if (getContext() == null) {
//                                                //fragment die
//                                                return;
//                                            }
//                                            pDialog.setMessage("Deleting...");
//                                            pDialog.show();
//                                            RestClient.getInstance(getContext()).getApiService().deletePost(user.getSession_key(), ((Post) o).getId()).
//                                                    enqueue(new Callback<GeneralResponse>() {
//                                                        @Override
//                                                        public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
//                                                            pDialog.dismiss();
//                                                            if (response.isSuccessful() || response.body() != null) {
//                                                                Utility.getApiStatusCallBack(response.body().getStatus(), response.body().getErrorMessage(), new ApiStatusCallBack() {
//                                                                    @Override
//                                                                    public void success() {
//                                                                        adapter.removePost((Post) o);
//                                                                    }
//
//                                                                    @Override
//                                                                    public void sessionExpire() {
//                                                                        onSessionExpire();
//                                                                    }
//
//                                                                    @Override
//                                                                    public void knownError(String errorMsg) {
//                                                                        CustomeToast.ShowCustomToast(getContext(), errorMsg, Gravity.BOTTOM);
//                                                                    }
//
//                                                                    @Override
//                                                                    public void unKnownError() {
//                                                                        showunKnownSeverErrorDialog();
//                                                                    }
//                                                                });
//                                                            } else {
//                                                                showunKnownSeverErrorDialog();
//                                                            }
//                                                        }
//
//                                                        @Override
//                                                        public void onFailure(Call<GeneralResponse> call, Throwable t) {
//                                                            showunKnownSeverErrorDialog();
//                                                        }
//                                                    });
//                                        }
//                                    }
//                                });
                            }
                            break;
                            case 6: {
                                Bundle b = new Bundle();
                                b.putSerializable(Constants.POST_EXTRA, (Post) obj);
                                b.putInt("type_int", 2);
                                Utility.launchActivityForResultFromFragment(WallFeedsMainFragment.this, getActivity(), WallNewPostActivity.class, b, Flags.ACTIVITY_COMMUNICATION_FOR_UPDATE_FLAG);
                            }
                            break;
                        }
                    }
                }
                break;
            }
        } else {
            if (type == -5 && obj != null && obj instanceof com.codingpixel.healingbudz.network.BudzFeedModel.GetAllPost.User) {
                //user click with user object
                onUserClick((com.codingpixel.healingbudz.network.BudzFeedModel.GetAllPost.User) obj);
            } else if (type == -6 && obj != null && obj instanceof SubUser) {
                //Sub User Click
                onSubUserClick((SubUser) obj);
            } else if (type == -7 && obj != null && obj instanceof SharedUser) {
                //Shared post User CLick
                onSharedPostUserClick((SharedUser) obj);
            } else if (obj instanceof FollowingUser && type == -10) {
                //tagged user clicked
                onTaggedPeopleClick((FollowingUser) obj);
            }
        }
    }

    private void onTaggedPeopleClick(FollowingUser obj) {
        if (getContext() != null) {
            isNewScreen = true;
            GoToProfile(getContext(), obj.getId());
        }
//            CustomeToast.ShowCustomToast(getContext(), "Tagged Person Clicked with name: " + obj.getFirstName() + "<&> id: " + obj.getId(), Gravity.BOTTOM);
    }

    private void onSharedPostUserClick(SharedUser obj) {
        if (getContext() != null) {
            isNewScreen = true;
            GoToProfile(getContext(), obj.getId());
//            CustomeToast.ShowCustomToast(getContext(), "Shared Post User Clicked With name: " + obj.getFirstName() + " <&> id: " + obj.getId(), Gravity.BOTTOM);
        }

    }

    private void onSubUserClick(SubUser obj) {
        if (getContext() != null) {
            //TODO FOR BUDZ MAP
//            isNewScreen = true;
//            GoToProfile(getContext(), obj.getId());
            Intent budzmap_intetn = new Intent(getActivity(), BudzMapDetailsActivity.class);
            budzmap_intetn.putExtra("budzmap_id", obj.getId());
            startActivity(budzmap_intetn);
//            CustomeToast.ShowCustomToast(getContext(), "Post Sub User Clicked With name: " + obj.getTitle() + " <&> id: " + obj.getId() + " <&> userID: " + obj.getUserId(), Gravity.BOTTOM);
        }
//
    }

    private void followUnFollowBud(com.codingpixel.healingbudz.network.BudzFeedModel.GetAllPost.User user) {
        if (getContext() == null) {
            //fragment die...
            return;
        }
        if (!Utility.isNetworkAvailable(getContext())) {
            Dialogs.showNetworkNotAvailibleDialog(getContext());
            return;
        }
        if (userId != null) {
            if (profiledataModel.getIs_following_count() > 0) {
                unfollow(user);
            } else {
                followUser(user);
            }
        } else {
            unfollow(user);
        }
    }

    private void unfollow(com.codingpixel.healingbudz.network.BudzFeedModel.GetAllPost.User user) {
        RestClient.getInstance(getContext()).getApiService().unFollowUser(WallFeedsMainFragment.this.user.getSession_key(), user.getId()).enqueue(new Callback<GeneralResponse>() {
            @Override
            public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                if (response.isSuccessful() || response.body() != null) {
                    Utility.getApiStatusCallBack(response.body().getStatus(), response.body().getErrorMessage(), new ApiStatusCallBack() {
                        @Override
                        public void success() {
                            onRefresh();
                            if (followCallBack != null && userId != null) {
                                followCallBack.onUserUnfollow(userId);
                            }
                        }

                        @Override
                        public void sessionExpire() {
                            onSessionExpire();
                        }

                        @Override
                        public void knownError(String errorMsg) {
                            CustomeToast.ShowCustomToast(getContext(), errorMsg, Gravity.TOP);
                        }

                        @Override
                        public void unKnownError() {
                            showunKnownSeverErrorDialog();
                        }
                    });
                } else {
                    showunKnownSeverErrorDialog();
                }
            }

            @Override
            public void onFailure(Call<GeneralResponse> call, Throwable t) {
                showunKnownSeverErrorDialog();
            }
        });
    }

    private void followUser(com.codingpixel.healingbudz.network.BudzFeedModel.GetAllPost.User user) {
        RestClient.getInstance(getContext()).getApiService().followUser(WallFeedsMainFragment.this.user.getSession_key(), user.getId()).enqueue(new Callback<GeneralResponse>() {
            @Override
            public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                if (response.isSuccessful() || response.body() != null) {
                    Utility.getApiStatusCallBack(response.body().getStatus(), response.body().getErrorMessage(), new ApiStatusCallBack() {
                        @Override
                        public void success() {
                            onRefresh();
                            if (followCallBack != null && userId != null) {
                                followCallBack.onUserFollow(userId);
                            }
                        }

                        @Override
                        public void sessionExpire() {
                            onSessionExpire();
                        }

                        @Override
                        public void knownError(String errorMsg) {
                            CustomeToast.ShowCustomToast(getContext(), errorMsg, Gravity.TOP);
                        }

                        @Override
                        public void unKnownError() {
                            showunKnownSeverErrorDialog();
                        }
                    });
                } else {
                    showunKnownSeverErrorDialog();
                }
            }

            @Override
            public void onFailure(Call<GeneralResponse> call, Throwable t) {
                showunKnownSeverErrorDialog();
            }
        });
    }

    private void onUserClick(com.codingpixel.healingbudz.network.BudzFeedModel.GetAllPost.User clickedUser) {
        if (getContext() != null) {
            isNewScreen = true;
            GoToProfile(getContext(), clickedUser.getId());
//            Toast.makeText(getContext(), clickedUser.getFirstName() + " " + clickedUser.getLastName() + " is clicked.", Toast.LENGTH_SHORT).show();
        }
    }

    private void flagPost(final Post post) {
        if (post.getFlags() != null && !post.getFlags().isEmpty()) {
            for (FlagPost reportPost : post.getFlags()) {
                if (user.getUser_id() == reportPost.getUserId()) {
                    return;
                }
            }
        }
        if (getContext() == null) {
            //fragment die...
            return;
        }
        if (reportView != null) {
            reportView.showReportPost(post.getId(), new Dialogs.DialogItemClickListener() {
                @Override
                public void onDialogItemClickListener(final DialogInterface dialog, Object obj, int type) {
                    if (type < 0) {
                        dialog.dismiss();
                    } else {
                        if (!Utility.isNetworkAvailable(getContext())) {
                            Dialogs.showNetworkNotAvailibleDialog(getContext());
                            return;
                        }
                        Utility.hideKeyboard(getActivity());
                        RestClient.getInstance(getContext()).getApiService().reportPost(user.getSession_key(), post.getId(), (String) obj).enqueue(new Callback<ReportPostResponse>() {
                            @Override
                            public void onResponse(Call<ReportPostResponse> call, final Response<ReportPostResponse> response) {
                                if (response.isSuccessful() && response.body() != null) {
                                    Utility.getApiStatusCallBack(response.body().getStatus(), response.body().getErrorMessage(), new ApiStatusCallBack() {
                                        @Override
                                        public void success() {
                                            reportView.dismissSlide();
                                            if (post.getFlags() == null) {
                                                post.setFlags(new ArrayList<FlagPost>());
                                            }
                                            post.getFlags().add(response.body().getSuccessData());
                                            adapter.addToDataSet(post);
                                            CustomeToast.ShowCustomToast(getContext(), "Report Submitted!", Gravity.TOP);
                                        }

                                        @Override
                                        public void sessionExpire() {
                                            reportView.dismissSlide();
                                            onSessionExpire();
                                        }

                                        @Override
                                        public void knownError(String errorMsg) {
                                            if (getContext() != null)
                                                CustomeToast.ShowCustomToast(getContext(), errorMsg, Gravity.TOP);
                                        }

                                        @Override
                                        public void unKnownError() {
                                            showunKnownSeverErrorDialog();
                                        }
                                    });
                                } else {
                                    showunKnownSeverErrorDialog();
                                }
                            }

                            @Override
                            public void onFailure(Call<ReportPostResponse> call, Throwable t) {
                                showunKnownSeverErrorDialog();
                            }
                        });
                    }

                }
            });
        }
    }

    private void muteUnmutePost(final Post post) {
        if (getContext() == null) {
            //fragment die
            return;
        }
        if (!Utility.isNetworkAvailable(getContext())) {
            Dialogs.showNetworkNotAvailibleDialog(getContext());
            return;
        }
        RestClient.getInstance(getContext()).getApiService().mutePost(user.getSession_key(),
                post.getId(), post.getMutePostByUserCount() > 0 ? 0 : 1).enqueue(new Callback<MutePostResponse>() {
            @Override
            public void onResponse(Call<MutePostResponse> call, final Response<MutePostResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Utility.getApiStatusCallBack(response.body().getStatus(), response.body().getErrorMessage(), new ApiStatusCallBack() {
                        @Override
                        public void success() {
                            post.setMutePostByUserCount(response.body().getSuccessData().getIsMute());
                        }

                        @Override
                        public void sessionExpire() {
                            onSessionExpire();
                        }

                        @Override
                        public void knownError(String errorMsg) {
                            CustomeToast.ShowCustomToast(getContext(), errorMsg, Gravity.TOP);
                        }

                        @Override
                        public void unKnownError() {
                            showunKnownSeverErrorDialog();
                        }
                    });
                } else {
                    showunKnownSeverErrorDialog();
                }
            }

            @Override
            public void onFailure(Call<MutePostResponse> call, Throwable t) {
                showunKnownSeverErrorDialog();
            }
        });
    }

    private void submitPostLike(final Integer id, int is_like) {
        if (getContext() == null) {
            //fragment die...
            return;
        }
        if (!Utility.isNetworkAvailable(getContext())) {
            Dialogs.showNetworkNotAvailibleDialog(getContext());
            return;
        }
        RestClient.getInstance(getContext()).getApiService().submitPostLike(user.getSession_key(), id, is_like).enqueue(new Callback<SubmitPostLikeResponse>() {
            @Override
            public void onResponse(Call<SubmitPostLikeResponse> call, final Response<SubmitPostLikeResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Utility.getApiStatusCallBack(response.body().getStatus(), response.body().getErrorMessage(), new ApiStatusCallBack() {
                        @Override
                        public void success() {
                            isLikedUser = false;
                            adapter.updatePostLike(id, response.body().getSuccessData());
                        }

                        @Override
                        public void sessionExpire() {
                            onSessionExpire();
                        }

                        @Override
                        public void knownError(String errorMsg) {
                            CustomeToast.ShowCustomToast(getContext(), errorMsg, Gravity.TOP);
                        }

                        @Override
                        public void unKnownError() {
                            showunKnownSeverErrorDialog();
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<SubmitPostLikeResponse> call, Throwable t) {
                showunKnownSeverErrorDialog();
            }
        });
    }

    private void showunKnownSeverErrorDialog() {

    }

    @Override
    public boolean onItemLongClick(Object obj, int pos, int type) {
        return false;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (adapter != null && adapter.cameraDelegade!=null){

            adapter.cameraDelegade.dataPassed(requestCode, resultCode, data);
        }
        if (resultCode == Flags.SESSION_OUT) {
            onSessionExpire();
            return;
        }
        if (requestCode == Flags.ACTIVITIES_COMMUNICATION_FLAG && resultCode == Flags.NOTIFY && data != null && data.getExtras() != null) {
            Post post = (Post) data.getExtras().getSerializable(Constants.POST_EXTRA);
            adapter.addToDataSet(post);
//            onRefresh();
            return;
        }
        if ((requestCode == Flags.ACTIVITIES_COMMUNICATION_FLAG || requestCode == Flags.ACTIVITY_COMMUNICATION_FOR_UPDATE_FLAG) && resultCode == Flags.NOTIFY_NEW_ELEMIENT && data != null && data.getExtras() != null) {
            ExtraPost post = (ExtraPost) data.getExtras().getSerializable(Constants.POST_EXTRA);
            if (post != null) {
                adapter.updateToDataSet(post.getPosts());
            }
            onRefresh();
        }
        if (resultCode == Flags.NOTIFY_DELETE) {
            onRefresh();
        }
    }

    @Override
    public void onRefresh() {
        pageNo = 0;
        isOnFinalPage = false;
        callAPI();
    }

    public String getUserImage() {
        String img = user.getImage_path();
        if (img != null && !img.trim().equalsIgnoreCase("null") && img.length() > 5) {
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

    public String getUserImageSpecial() {
        String img = user.getSpecial_icon();
        if (user.getSpecial_icon().length() > 5) {

            return URL.images_baseurl + img;

        } else {
            return "";
        }

    }

    @Override
    public void onLinkClick(AutoLinkMode autoLinkMode, String matchedText, MentionTagJsonModel extraJson) {
        switch (autoLinkMode) {
            case MODE_URL: {
                if (matchedText.contains("youtube.com") || matchedText.contains("youtu.be")) {
                    Intent intent = new Intent(getContext(), MediPreview.class);
                    intent.putExtra("path", matchedText);
                    intent.putExtra("isvideo", true);
                    intent.putExtra("isvideoyoutube", true);
                    getContext().startActivity(intent);
                } else
                    Utility.launchWebUrl(getContext(), matchedText);
            }
            break;
            case MODE_EMAIL: {
                Utility.launchEmailApplication(getContext(), matchedText);
            }
            break;
            case MODE_PHONE: {
                Utility.launchPhoneApplication(getContext(), matchedText);
            }
            break;
            case MODE_HASHTAG: {
                //todo: hash tag click listener
                if (extraJson != null) {
                    new KeywordClickDialog(extraJson.getValue(), getContext());
                } else {
                    Log.d("onLinkClick: ", matchedText);
                }
//                CustomeToast.ShowCustomToast(getContext(), "HashTag \"" + extraJson.getValue() + " with id: " + extraJson.getId() + "\" is clicked.", Gravity.BOTTOM);
            }
            break;
            case MODE_MENTION: {
                //todo: Mention click listener
                isNewScreen = true;
                if (extraJson != null) {
                    if (extraJson.getType().equalsIgnoreCase("budz")) {
                        Intent budzmap_intetn = new Intent(getContext(), BudzMapDetailsActivity.class);
                        budzmap_intetn.putExtra("budzmap_id", Integer.parseInt(extraJson.getId()));
                        startActivity(budzmap_intetn);
                    } else {
                        GoToProfile(getContext(), Integer.parseInt(extraJson.getId()));
                    }
                } else {
                    Log.d("onLinkClick: ", matchedText);
                }
//                CustomeToast.ShowCustomToast(getContext(), "Mention \"" + extraJson.getValue() + " with id: " + extraJson.getId() + "\" is clicked.", Gravity.BOTTOM);
            }
            break;
        }
    }

    @Override
    public void onResponse(@NonNull Call<GetAllPostResponse> call, @NonNull final Response<GetAllPostResponse> response) {
        isAppiCallActive = false;
        if (refreshLayout != null) {
            refreshLayout.setRefreshing(false);
        }

        if (response.isSuccessful() && response.body() != null) {
            Utility.getApiStatusCallBack(response.body().getStatus(), response.body().getErrorMessage(), new ApiStatusCallBack() {
                @Override
                public void success() {
                    if (response.body().getSuccessData().getPosts() == null || response.body().getSuccessData().getPosts().isEmpty()) {
                        isOnFinalPage = true;
                        if (adapter.getItemCount() > 0) {
                            if (not_found != null) {
                                not_found.setVisibility(View.GONE);
                                feed_other.setVisibility(View.VISIBLE);
                            }
                            if (not_found_main != null) {
                                not_found_main.setVisibility(View.GONE);
                            }
                        } else {
                            if (not_found != null) {
                                not_found.setVisibility(View.VISIBLE);
                                feed_other.setVisibility(View.GONE);
                            }
                            if (not_found_main != null) {
                                not_found_main.setVisibility(View.VISIBLE);
                            }
                        }
                        return;
                    }

//                    TODO FOR TAGGING OF UNKNONWN SHIT

                    if (pageNo == 0) {
                        adapter.setDataSet(new ArrayList<Post>());
                        recyclerView.scrollToPosition(0);

                    }
                    List<Post> posts = response.body().getSuccessData().getPosts();
                    adapter.addToDataSet(posts);
                    if (adapter.getItemCount() > 0 && pageNo == 0) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                recyclerView.scrollToPosition(0);
                                recyclerView.scrollTo(0, 0);
                                recyclerView.scrollBy(0, 0);
                            }
                        }, 5);

                    }
                    isOnFinalPage = false;
                    ++pageNo;
                    if (adapter.getItemCount() > 0) {
                        if (not_found != null) {
                            not_found.setVisibility(View.GONE);
                            feed_other.setVisibility(View.VISIBLE);
                        }
                        if (not_found_main != null) {
                            not_found_main.setVisibility(View.GONE);
                        }
                    } else {
                        if (not_found != null) {
                            not_found.setVisibility(View.VISIBLE);
                            feed_other.setVisibility(View.GONE);
                        }
                        if (not_found_main != null) {
                            not_found_main.setVisibility(View.VISIBLE);
                        }
                    }
                }

                @Override
                public void sessionExpire() {
                    WallFeedsMainFragment.this.onSessionExpire();
                }

                @Override
                public void knownError(String errorMsg) {
                    CustomeToast.ShowCustomToast(getContext(), errorMsg, Gravity.TOP);
                }

                @Override
                public void unKnownError() {
                    //unKnown Error
                    showunKnownSeverErrorDialog();
                }
            });
        } else {
            if (adapter.getItemCount() > 0) {
                if (not_found != null) {
                    not_found.setVisibility(View.GONE);
                    feed_other.setVisibility(View.GONE);
                }
            } else {
                if (not_found != null) {
                    not_found.setVisibility(View.VISIBLE);
                    feed_other.setVisibility(View.GONE);
                }
            }
            //unKnown Error
            showunKnownSeverErrorDialog();
        }
    }

    @Override
    public void onFailure(Call<GetAllPostResponse> call, Throwable t) {
        isAppiCallActive = false;
        if (not_found != null) {
            not_found.setVisibility(View.GONE);
            feed_other.setVisibility(View.GONE);
        }
        if (refreshLayout != null) {
            refreshLayout.setRefreshing(false);
        }
        CustomeToast.ShowCustomToast(getContext(), "Network not Available!", Gravity.TOP);
    }

    @Override
    public void setBudzFeedCount(int count, boolean isShow) {

    }

    @Override
    public void setShoutOutFeedCount(int count, boolean isShow) {

    }

    public void setImageUpdate() {
        user = Splash.user;
        Glide.with(getContext()).load(getUserImage()).asBitmap().diskCacheStrategy(DiskCacheStrategy.SOURCE).fitCenter()
                .placeholder(Utility.getProfilePlaceHolder(user.getPoints())).into(imageView);
    }
}
