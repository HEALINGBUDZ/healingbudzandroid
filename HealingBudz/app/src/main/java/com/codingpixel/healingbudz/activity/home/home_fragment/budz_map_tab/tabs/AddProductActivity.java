package com.codingpixel.healingbudz.activity.home.home_fragment.budz_map_tab.tabs;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.android.volley.Request;
import com.codingpixel.healingbudz.DataModel.BudzMapProductDataModal;
import com.codingpixel.healingbudz.DataModel.StrainModel;
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
import com.codingpixel.healingbudz.customeUI.customspinner;
import com.codingpixel.healingbudz.data_structure.APIActions;
import com.codingpixel.healingbudz.interfaces.RecyclerViewItemClickListener;
import com.codingpixel.healingbudz.media_view_Dialog.MediPreview;
import com.codingpixel.healingbudz.network.VollyAPICall;
import com.codingpixel.healingbudz.network.model.APIResponseListner;
import com.codingpixel.healingbudz.network.model.URL;
import com.codingpixel.healingbudz.network.upload_image.UploadImageAPIcall;
import com.codingpixel.healingbudz.network.upload_image.UploadVideoAPIcall;
import com.rd.PageIndicatorView;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.codingpixel.healingbudz.Utilities.ViewUtils.checkRotation;
import static com.codingpixel.healingbudz.data_structure.APIActions.ApiActions.add_product;
import static com.codingpixel.healingbudz.data_structure.APIActions.ApiActions.wall_post_img_upload;

public class AddProductActivity extends AppCompatActivity implements View.OnClickListener, APIResponseListner {

    private LinearLayout mCancel;
    private HealingBudTextViewRegular mTitle;
    private LinearLayout mSave;
    private FrameLayout mViewTop;
    private ViewPager mPager;
    private PageIndicatorView mControlPager;
    private RelativeLayout mViewPager;
    private ImageView mPhotoImgUpload;
    private RelativeLayout mPhotoUpload;
    private HealinBudEditText mProductName;
    private HealinBudEditText mCategoryName;
    private customspinner mToStrainLink;
    private HealinBudEditText mStrainName;
    private RelativeLayout mShowSpinner;
    private HealinBudEditText mTchName;
    private HealinBudEditText mCbdName;
    private HealinBudEditText m1Weight;
    private HealinBudEditText m1Price;
    private HealinBudEditText m2Weight;
    private HealinBudEditText m2Price;
    private HealinBudEditText m3Weight;
    private HealinBudEditText m3Price;
    private HealinBudEditText m4Weight;
    private HealinBudEditText m4Price;
    //
    private BudzFeedNewPostMediaViewPagerAdapter.NewPostMedia temp;
    private BudzFeedNewPostMediaViewPagerAdapter mediaAdapter;
    private com.codingpixel.healingbudz.customeUI.ProgressDialog dialog;
    BudzMapProductDataModal editObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        initView();
        initListener();
        dialog = new com.codingpixel.healingbudz.customeUI.ProgressDialog();
        dialog.setCancelable(false);
        mediaAdapter = new BudzFeedNewPostMediaViewPagerAdapter(null, new BudzFeedNewPostMediaViewPagerAdapter.UpdateCallBack() {
            @Override
            public void onUpdate(int i) {
                if (mediaAdapter.getCount() == 0) {
                } else {
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
                    Utility.launchActivity(AddProductActivity.this, MediPreview.class, false, b);
                }
            }

            @Override
            public boolean onItemLongClick(Object obj, int pos, int type) {
                return false;
            }
        });
        mPager.setAdapter(mediaAdapter);
        mControlPager.setViewPager(mPager);
        final List<String> list = new ArrayList<>();
        list.add("Select strain...");
        for (StrainModel item : ProductServicesTabFragment.strainModelList) {
            list.add(item.getTitle());
        }
        mToStrainLink.SetAdapter(list);
        mToStrainLink.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mToStrainLink.SetValue();
                mStrainName.setText(list.get(position));
                if (position > 0) {
                    senderPos = position - 1;
                } else {
                    senderPos = -1;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        mShowSpinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mToStrainLink.spinner.performClick();
            }
        });
        mStrainName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mToStrainLink.spinner.performClick();
            }
        });
        if (getIntent().hasExtra("object")) {
            editObject = (BudzMapProductDataModal) getIntent().getSerializableExtra("object");
            setEditView();
            mTitle.setText("Edit Product");
            mTitle.setAllCaps(true);
        } else {
            editObject = null;
        }
    }

    private void setEditView() {
        if (editObject.getCategoryModel() != null)
            mCategoryName.setText(editObject.getCategoryModel().getTitle());
        mProductName.setText(editObject.getName());
        mTchName.setText(MessageFormat.format("{0}", editObject.getThc()));
        mCbdName.setText(MessageFormat.format("{0}", editObject.getCbd()));
        if (editObject.getStrainModel() != null) {
            for (int l = 0; l < ProductServicesTabFragment.strainModelList.size(); l++) {
                if (editObject.getStrainModel().getId() == ProductServicesTabFragment.strainModelList.get(l).id) {
                    senderPos = l;
                    mToStrainLink.spinner.setSelection(senderPos + 1, true);
                    break;
                }

            }
        }
        if (editObject.getImage().size() > 0) {
            for (String item : editObject.getImage()) {
                temp = new BudzFeedNewPostMediaViewPagerAdapter.NewPostMedia(item, false);
                temp.url = item;
                mediaAdapter.addMedia(temp);
                temp = null;
            }
        }
        for (int l = 0; l < editObject.getPriceing().size(); l++) {
            switch (l) {
                case 0:
                    m1Price.setText(editObject.getPriceing().get(l).getPrice());
                    m1Weight.setText(editObject.getPriceing().get(l).getWeight());
                    break;
                case 1:
                    m2Price.setText(editObject.getPriceing().get(l).getPrice());
                    m2Weight.setText(editObject.getPriceing().get(l).getWeight());
                    break;
                case 2:
                    m3Price.setText(editObject.getPriceing().get(l).getPrice());
                    m3Weight.setText(editObject.getPriceing().get(l).getWeight());
                    break;
                case 3:
                    m4Price.setText(editObject.getPriceing().get(l).getPrice());
                    m4Weight.setText(editObject.getPriceing().get(l).getWeight());
                    break;
                default:
                    break;
            }

        }


    }

    int senderPos = -1;

    private void initListener() {
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
                        String weight = m1Weight.getText().toString().trim();// + "," + m2Weight.getText().toString().trim() + ","
                        //  + m3Weight.getText().toString().trim() + "," + m4Weight.getText().toString().trim();
                        String price = m1Price.getText().toString().trim();// + "," + m2Price.getText().toString().trim() + ","
//                                + m3Price.getText().toString().trim() + "," + m4Price.getText().toString().trim();
                        if (m2Weight.getText().toString().trim().length() > 0) {
                            if (m2Price.getText().toString().trim().length() > 0) {
                                weight = weight + "," + m2Weight.getText().toString().trim();
                                price = price + "," + m2Price.getText().toString().trim();
                            } else {
                                weight = weight + "," + m2Weight.getText().toString().trim();
                                price = price + "," + "0";
                            }
                        } else {
                            if (m2Price.getText().toString().trim().length() > 0) {
                                weight = weight + "," + "0";
                                price = price + "," + m2Price.getText().toString().trim();
                            }
                        }
                        if (m3Weight.getText().toString().trim().length() > 0) {
                            if (m3Price.getText().toString().trim().length() > 0) {
                                weight = weight + "," + m3Price.getText().toString().trim();
                                price = price + "," + m3Price.getText().toString().trim();
                            } else {
                                weight = weight + "," + m3Weight.getText().toString().trim();
                                price = price + "," + "0";
                            }

                        } else {
                            if (m3Price.getText().toString().trim().length() > 0) {
                                weight = weight + "," + "0";
                                price = price + "," + m3Price.getText().toString().trim();
                            }
                        }
                        if (m4Weight.getText().toString().trim().length() > 0) {
                            if (m4Price.getText().toString().trim().length() > 0) {
                                weight = weight + "," + m4Weight.getText().toString().trim();
                                price = price + "," + m4Price.getText().toString().trim();
                            } else {
                                weight = weight + "," + m4Weight.getText().toString().trim();
                                price = price + "," + "0";
                            }
                        } else {
                            if (m4Price.getText().toString().trim().length() > 0) {
                                weight = weight + "," + "0";
                                price = price + "," + m4Price.getText().toString().trim();
                            }
                        }
                        object.put("sub_user_id", BudzMapHomeFragment.budz_map_item_clickerd_dataModel.getId());
                        object.put("product_name", mProductName.getText().toString().trim());
                        object.put("thc", mTchName.getText().toString().trim());
                        object.put("cbd", mCbdName.getText().toString().trim());
                        object.put("weight", weight);
                        object.put("price", price);
                        if (senderPos > 0) {
                            object.put("strain_id", ProductServicesTabFragment.strainModelList.get(senderPos).getId());
                        } else {
                            object.put("strain_id", "");
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
                        object.put("cat_name", mCategoryName.getText().toString().trim());
                        object.put("attachments", attachments);

                        new VollyAPICall(v.getContext()
                                , true
                                , URL.add_budz_product
                                , object
                                , Splash.user.getSession_key()
                                , Request.Method.POST
                                , AddProductActivity.this,
                                add_product);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        });
        mPhotoUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaAdapter.getCount() < 5) {
                    Bundle b = new Bundle();
                    b.putInt(Constants.CAMERA_ONLY_VIDEO, 1);
                    Utility.launchActivityForResult(AddProductActivity.this, HBCameraActivity.class, b, Flags.PICTURE_CAPTUREING_REQUEST);
                } else {
                    CustomeToast.ShowCustomToast(AddProductActivity.this, "Maximum upload photos limit has been reached!", Gravity.TOP);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null && data.getExtras() != null) {
            if (requestCode == Flags.PICTURE_CAPTUREING_REQUEST && data.getExtras().getInt("response_code_arg", 0) == HBCameraActivity.ACTION_CONFIRM) {
                String path = data.getExtras().getString("file_path_arg", null);
                if (path != null || !path.trim().isEmpty()) {
                    temp = new BudzFeedNewPostMediaViewPagerAdapter.NewPostMedia(path, false);
                    uploadMedia(temp);
                } else {
                    // nothing return Picture action
                }
            }
        }
    }

    private void uploadMedia(BudzFeedNewPostMediaViewPagerAdapter.NewPostMedia media) {
        if (!media.isVideo()) {
            Bitmap bitmapOrg = BitmapFactory.decodeFile(media.getPath());
            bitmapOrg = checkRotation(bitmapOrg, media.getPath());

//            new UploadImageAPIcall(AddProductActivity.this, URL.add_budz_product_image, new BitmapDrawable(getResources(), bitmapOrg), Splash.user.getSession_key(), AddProductActivity.this, wall_post_img_upload);
            new UploadImageAPIcall(AddProductActivity.this, URL.add_budz_product_image, media.getPath(), Splash.user.getSession_key(), AddProductActivity.this, wall_post_img_upload);
        } else {

            new UploadVideoAPIcall(AddProductActivity.this, URL.addPostVideoURL, media.getPath(), Splash.user.getSession_key(), AddProductActivity.this, APIActions.ApiActions.wall_post_video_upload);
        }
        dialog.show(getSupportFragmentManager(), "sendingg");
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
        mProductName = findViewById(R.id.name_product);
        mCategoryName = findViewById(R.id.name_category);
        mToStrainLink = findViewById(R.id.link_to_strain);
        mStrainName = findViewById(R.id.name_strain);
        mStrainName.setOnClickListener(this);
        mShowSpinner = findViewById(R.id.spinner_show);
        mTchName = findViewById(R.id.name_tch);
        mCbdName = findViewById(R.id.name_cbd);
        m1Weight = findViewById(R.id.weight_1);
        m1Price = findViewById(R.id.price_1);

        m2Weight = findViewById(R.id.weight_2);
        m2Price = findViewById(R.id.price_2);
        m3Weight = findViewById(R.id.weight_3);
        m3Price = findViewById(R.id.price_3);
        m4Weight = findViewById(R.id.weight_4);
        m4Price = findViewById(R.id.price_4);
//        m1Price.addTextChangedListener(new DecimalInputTextWatcher(m1Price, 2));
//        m2Price.addTextChangedListener(new DecimalInputTextWatcher(m1Price, 2));
//        m3Price.addTextChangedListener(new DecimalInputTextWatcher(m1Price, 2));
//        m4Price.addTextChangedListener(new DecimalInputTextWatcher(m1Price, 2));
//        mTchName.addTextChangedListener(new DecimalInputTextWatcher(m1Price, 2));
//        mCbdName.addTextChangedListener(new DecimalInputTextWatcher(m1Price, 2));
    }

    public boolean isValid() {
        if (mProductName.getText().toString().trim().length() == 0) {
            CustomeToast.ShowCustomToast(this, "Product Name Required!", Gravity.TOP);
            return false;
        }
//        if (mCategoryName.getText().toString().trim().length() == 0) {
//            CustomeToast.ShowCustomToast(this, "Category Name Required!", Gravity.TOP);
//            return false;
//        }
        if (mTchName.getText().toString().trim().length() == 0) {
//            CustomeToast.ShowCustomToast(this, "THC Required!", Gravity.TOP);
//            return false;
        } else {
            if (mTchName.getText().toString().trim().length() > 3) {
                CustomeToast.ShowCustomToast(this, "THC value limit 1-100!", Gravity.TOP);
                return false;
            }
            try {
                if (Double.parseDouble(mTchName.getText().toString().trim()) > 100) {
                    CustomeToast.ShowCustomToast(this, "THC value limit 1-100!", Gravity.TOP);
                    return false;
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
                CustomeToast.ShowCustomToast(this, "Invalid Input!", Gravity.TOP);
                return false;
            }

        }
        if (mCbdName.getText().toString().trim().length() == 0) {
//            CustomeToast.ShowCustomToast(this, "CBD Required!", Gravity.TOP);
//            return false;
        } else {
            if (mCbdName.getText().toString().trim().length() > 3) {
                CustomeToast.ShowCustomToast(this, "CBD value limit 1-100!", Gravity.TOP);
                return false;
            }
            try {
                if (Double.parseDouble(mCbdName.getText().toString().trim()) > 100) {
                    CustomeToast.ShowCustomToast(this, "CBD value limit 1-100!", Gravity.TOP);
                    return false;
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
                CustomeToast.ShowCustomToast(this, "Invalid Input!", Gravity.TOP);
                return false;
            }
        }
        if (m1Weight.getText().toString().trim().length() == 0) {
            CustomeToast.ShowCustomToast(this, "Weight 1 Required!", Gravity.TOP);
            return false;
        } else {
            if (m1Weight.getText().toString().trim().length() > 8) {
                CustomeToast.ShowCustomToast(this, "Weight 1 limit 8 Character!", Gravity.TOP);
                return false;
            }
        }
        if (m1Price.getText().toString().trim().length() == 0) {
            CustomeToast.ShowCustomToast(this, "Price 1 Required!", Gravity.TOP);
            return false;
        } else {
            if (m1Price.getText().toString().trim().length() > 8) {
                CustomeToast.ShowCustomToast(this, "Price 1 limit 8 digit!", Gravity.TOP);
                return false;
            }
        }
        if (m2Weight.getText().toString().trim().length() > 8) {
            CustomeToast.ShowCustomToast(this, "Weight 2 limit 8 Character!", Gravity.TOP);
            return false;
        }
        if (m2Price.getText().toString().trim().length() > 8) {
            CustomeToast.ShowCustomToast(this, "Price 2 limit 8 digit!", Gravity.TOP);
            return false;
        }
        if (m3Weight.getText().toString().trim().length() > 8) {
            CustomeToast.ShowCustomToast(this, "Weight 3 limit 8 Character!", Gravity.TOP);
            return false;
        }

        if (m3Price.getText().toString().trim().length() > 8) {
            CustomeToast.ShowCustomToast(this, "Price 3 limit 8 digit!", Gravity.TOP);
            return false;
        }
        if (m4Weight.getText().toString().trim().length() > 8) {
            CustomeToast.ShowCustomToast(this, "Weight 4 limit 8 Character!", Gravity.TOP);
            return false;
        }


        if (m4Price.getText().toString().trim().length() > 8) {
            CustomeToast.ShowCustomToast(this, "Price 4 limit 8 digit!!", Gravity.TOP);
            return false;
        }
//        if (mStrainName.getText().toString().trim().length() == 0) {
//            return false;
//        }
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.name_strain:
                // TODO 18/10/03
                break;
            default:
                break;
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
            CustomeToast.ShowCustomToast(AddProductActivity.this, "Media Uploaded!", Gravity.TOP);
        } else if (apiActions == add_product) {
            finish();
        }
    }

    @Override
    public void onRequestError(String response, APIActions.ApiActions apiActions) {

    }

    public class DecimalInputTextWatcher implements TextWatcher {

        private String mPreviousValue;
        private int mCursorPosition;
        private boolean mRestoringPreviousValueFlag;
        private int mDigitsAfterZero;
        private EditText mEditText;

        public DecimalInputTextWatcher(EditText editText, int digitsAfterZero) {
            mDigitsAfterZero = digitsAfterZero;
            mEditText = editText;
            mPreviousValue = "";
            mRestoringPreviousValueFlag = false;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            if (!mRestoringPreviousValueFlag) {
                mPreviousValue = s.toString();
                mCursorPosition = mEditText.getSelectionStart();
            }
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (!mRestoringPreviousValueFlag) {

                if (!isValid(s.toString())) {
                    mRestoringPreviousValueFlag = true;
                    restorePreviousValue();
                }

            } else {
                mRestoringPreviousValueFlag = false;
            }
        }

        private void restorePreviousValue() {
            mEditText.setText(mPreviousValue);
            mEditText.setSelection(mCursorPosition);
        }

        private boolean isValid(String s) {
            Pattern patternWithDot = Pattern.compile("[0-9]*((\\.[0-9]{0," + mDigitsAfterZero + "})?)||(\\.)?");
            Pattern patternWithComma = Pattern.compile("[0-9]*((,[0-9]{0," + mDigitsAfterZero + "})?)||(,)?");

            Matcher matcherDot = patternWithDot.matcher(s);
            Matcher matcherComa = patternWithComma.matcher(s);

            return matcherDot.matches() || matcherComa.matches();
        }
    }
}
