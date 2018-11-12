package com.codingpixel.healingbudz.activity.home.home_fragment.budz_map_tab.tabs;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.android.volley.Request;
import com.codingpixel.healingbudz.DataModel.BudzMapTicketsDataModal;
import com.codingpixel.healingbudz.R;
import com.codingpixel.healingbudz.Utilities.Constants;
import com.codingpixel.healingbudz.Utilities.Flags;
import com.codingpixel.healingbudz.Utilities.Utility;
import com.codingpixel.healingbudz.activity.home.home_fragment.budz_map_tab.BudzMapHomeFragment;
import com.codingpixel.healingbudz.activity.splash.Splash;
import com.codingpixel.healingbudz.adapter.BudzFeedNewPostMediaViewPagerAdapter;
import com.codingpixel.healingbudz.camera_activity.HBCameraActivity;
import com.codingpixel.healingbudz.customeUI.CustomeToast;
import com.codingpixel.healingbudz.customeUI.HealinBudEditText;
import com.codingpixel.healingbudz.customeUI.HealingBudTextViewRegular;
import com.codingpixel.healingbudz.data_structure.APIActions;
import com.codingpixel.healingbudz.interfaces.RecyclerViewItemClickListener;
import com.codingpixel.healingbudz.media_view_Dialog.MediPreview;
import com.codingpixel.healingbudz.network.VollyAPICall;
import com.codingpixel.healingbudz.network.model.APIResponseListner;
import com.codingpixel.healingbudz.network.model.URL;
import com.codingpixel.healingbudz.network.upload_file_with_progress.UploadFileWithProgress;
import com.rd.PageIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.codingpixel.healingbudz.data_structure.APIActions.ApiActions.add_product;
import static com.codingpixel.healingbudz.data_structure.APIActions.ApiActions.wall_post_img_upload;

public class AddTicketActivity extends AppCompatActivity implements APIResponseListner {

    private LinearLayout mCancel;
    private HealingBudTextViewRegular mTitle;
    private LinearLayout mSave;
    private FrameLayout mViewTop;
    private ViewPager mPager;
    private PageIndicatorView mControlPager;
    private RelativeLayout mViewPager;
    private ImageView mPhotoImgUpload;
    private RelativeLayout mPhotoUpload;
    private HealinBudEditText mTicketName;
    private HealinBudEditText mChargeName;
    private HealinBudEditText mUrlName;
    private BudzFeedNewPostMediaViewPagerAdapter.NewPostMedia temp;
    private BudzFeedNewPostMediaViewPagerAdapter mediaAdapter;
    private com.codingpixel.healingbudz.customeUI.ProgressDialog dialog;
    BudzMapTicketsDataModal editObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_ticket);
        initView();
        intiListener();
        dialog = new com.codingpixel.healingbudz.customeUI.ProgressDialog();
        dialog.setCancelable(false);
        mediaAdapter = new BudzFeedNewPostMediaViewPagerAdapter(null, new BudzFeedNewPostMediaViewPagerAdapter.UpdateCallBack() {
            @Override
            public void onUpdate(int i) {
                if (mediaAdapter.getCount() == 0) {
//                    mediaPagerParent.setVisibility(View.GONE);
//                    toggleBottomLayer(isKeyboardVisible);
                } else {
//                    toggleBottomLayer(false);
//                    mediaPagerParent.setVisibility(View.VISIBLE);
                    if (i == -1) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mPager.setCurrentItem(mediaAdapter.getCount() - 1);
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
                    Utility.launchActivity(AddTicketActivity.this, MediPreview.class, false, b);
                }
            }

            @Override
            public boolean onItemLongClick(Object obj, int pos, int type) {
                return false;
            }
        });
        mPager.setAdapter(mediaAdapter);
        mControlPager.setViewPager(mPager);
        if (getIntent().hasExtra("object")) {
            editObject = (BudzMapTicketsDataModal) getIntent().getSerializableExtra("object");
            setEditView();
            mTitle.setText("Edit Ticket");
            mTitle.setAllCaps(true);
        } else {
            editObject = null;
        }
    }

    private void setEditView() {
        mChargeName.setText(editObject.getPrice());
        mTicketName.setText(editObject.getTitle());
        mUrlName.setText(editObject.getLinkee());
        if (editObject.getImage().length() > 4) {
            temp = new BudzFeedNewPostMediaViewPagerAdapter.NewPostMedia(editObject.getImage(), false);
            temp.url = editObject.getImage();
            mediaAdapter.addMedia(temp);
            temp = null;
        }
    }

    private void initView() {
        mCancel = findViewById(R.id.cancel);
        mTitle = findViewById(R.id.title);
        mSave = findViewById(R.id.save);
        mViewTop = findViewById(R.id.top_view);
        mPager = findViewById(R.id.pager);
        mControlPager = findViewById(R.id.pager_control);
        mViewPager = findViewById(R.id.pager_view);
        mPhotoImgUpload = findViewById(R.id.upload_photo_img);
        mPhotoUpload = findViewById(R.id.upload_photo);
        mTicketName = findViewById(R.id.name_ticket);
        mChargeName = findViewById(R.id.name_charge);
        mUrlName = findViewById(R.id.name_url);
    }

    private void intiListener() {
        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO FOR SAVE CALL
                if (isValid()) {
                    JSONObject object = new JSONObject();
                    try {
                        object.put("sub_user_id", BudzMapHomeFragment.budz_map_item_clickerd_dataModel.getId());
                        object.put("ticket_title", mTicketName.getText().toString().trim());
                        object.put("ticket_price", mChargeName.getText().toString().trim());
                        object.put("purchase_ticket_url", mUrlName.getText().toString().trim());
                        object.put("path", "");
                        if (editObject != null) {
                            if (editObject.getImage().length() > 4)
                                object.put("path", editObject.getImage());
                            if (mediaAdapter.getMedias().size() > 0) {
                                object.put("path", mediaAdapter.getMedias().get(0).url);
                            } else {
                                object.put("path", "");
                            }

                        }
                        if (editObject != null) {
                            object.put("id", editObject.getId());
                        }
                        StringBuilder attachments = new StringBuilder("");
                        for (BudzFeedNewPostMediaViewPagerAdapter.NewPostMedia item : mediaAdapter.getMedias()) {
                            attachments.append((item.url)).append(",");
                        }
                        if (attachments.toString().trim().length() > 0) {
                            attachments = new StringBuilder(attachments.substring(0, attachments.lastIndexOf(",")));
                        }
                        JSONArray parameter = new JSONArray();
                        parameter.put("sub_user_id");
//                        parameter.put("rating");
                        parameter.put("ticket_title");
                        parameter.put("ticket_price");
                        parameter.put("purchase_ticket_url");
                        if (editObject != null) {
                            parameter.put("id");
                        }
                        JSONArray values = new JSONArray();
                        values.put(BudzMapHomeFragment.budz_map_item_clickerd_dataModel.getId());
                        values.put(mTicketName.getText().toString().trim());
                        values.put(mChargeName.getText().toString().trim());
                        values.put(mUrlName.getText().toString().trim());
                        if (editObject != null) {
                            values.put(editObject.getId());
                        }
                        if (attachments.length() > 2 && attachments.toString().contains("com.healingbudz.android")) {
                            new UploadFileWithProgress(v.getContext(), true, URL.add_budz_ticket, attachments.toString(), "image", values, parameter, null, AddTicketActivity.this, APIActions.ApiActions.add_product).execute();
                        } else {
                            new VollyAPICall(v.getContext()
                                    , true,
                                    URL.add_budz_ticket
                                    , object
                                    , Splash.user.getSession_key()
                                    , Request.Method.POST
                                    , AddTicketActivity.this,
                                    add_product);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        });
        mPhotoUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaAdapter.getCount() < 1) {
                    Bundle b = new Bundle();
                    b.putInt(Constants.CAMERA_ONLY_VIDEO, 1);
                    Utility.launchActivityForResult(AddTicketActivity.this, HBCameraActivity.class, b, Flags.PICTURE_CAPTUREING_REQUEST);
                } else {
                    CustomeToast.ShowCustomToast(AddTicketActivity.this, "Max 1 Picture only..", Gravity.TOP);
                }
            }
        });
    }

    String path;

    public boolean isValid() {
        if (mTicketName.getText().toString().trim().length() == 0) {
            CustomeToast.ShowCustomToast(this, "Ticket Name Required!", Gravity.TOP);
            return false;
        }
//        if (mCategoryName.getText().toString().trim().length() == 0) {
//            CustomeToast.ShowCustomToast(this, "Category Name Required!", Gravity.TOP);
//            return false;
//        }
        if (mChargeName.getText().toString().trim().length() == 0) {
            CustomeToast.ShowCustomToast(this, "Charges Required!", Gravity.TOP);
            return false;
        }

//        if (mStrainName.getText().toString().trim().length() == 0) {
//            return false;
//        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null && data.getExtras() != null) {
            if (requestCode == Flags.PICTURE_CAPTUREING_REQUEST && data.getExtras().getInt("response_code_arg", 0) == HBCameraActivity.ACTION_CONFIRM) {
                path = data.getExtras().getString("file_path_arg", null);
                if (path != null || !path.trim().isEmpty()) {
                    temp = new BudzFeedNewPostMediaViewPagerAdapter.NewPostMedia(path, false);
                    temp.url = path;
                    temp.thumb = "";
                    temp.ratio = "";
                    mediaAdapter.addMedia(temp);
                    mediaAdapter.notifyDataSetChanged();
                    temp = null;
//                    uploadMedia(temp);
                } else {
                    // nothing return Picture action
                }
            }
        }
    }


    @Override
    public void onRequestSuccess(String response, APIActions.ApiActions apiActions) {
        if (apiActions == wall_post_img_upload) {
            dialog.dismiss();
            if (!response.trim().isEmpty()) {
                try {
                    JSONObject jsonObject = new JSONObject(response).getJSONObject("successData");
                    temp.url = jsonObject.getString("path");
                    temp.thumb = "";
                    temp.ratio = "";
                    mediaAdapter.addMedia(temp);
                    mediaAdapter.notifyDataSetChanged();
                    temp = null;
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            CustomeToast.ShowCustomToast(AddTicketActivity.this, "Media Uploaded!", Gravity.TOP);
        } else if (apiActions == add_product) {
            finish();
        }
    }

    @Override
    public void onRequestError(String response, APIActions.ApiActions apiActions) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
