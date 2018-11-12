package com.codingpixel.healingbudz.adapter;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.codingpixel.healingbudz.DataModel.GroupsChatMsgsDataModel;
import com.codingpixel.healingbudz.R;
import com.codingpixel.healingbudz.customeUI.CustomeToast;
import com.codingpixel.healingbudz.data_structure.APIActions;
import com.codingpixel.healingbudz.network.VollyAPICall;
import com.codingpixel.healingbudz.network.model.APIResponseListner;
import com.codingpixel.healingbudz.network.model.URL;
import com.codingpixel.healingbudz.network.upload_file_with_progress.AndroidMultiPartEntity;
import com.github.nkzawa.socketio.client.Socket;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static com.codingpixel.healingbudz.activity.splash.Splash.user;
import static com.codingpixel.healingbudz.Utilities.ClickAbleKeywordText.MakeKeywordClickableText;
import static com.codingpixel.healingbudz.application.HealingBudApplication.getContext;
import static com.codingpixel.healingbudz.data_structure.APIActions.ApiActions.add_message_like;
import static com.codingpixel.healingbudz.network.model.URL.images_baseurl;
import static com.codingpixel.healingbudz.network.model.URL.videos_baseurl;

public class GroupChatViewRecylerAdapter extends RecyclerView.Adapter<GroupChatViewRecylerAdapter.ViewHolder> implements APIResponseListner {
    private LayoutInflater mInflater;
    Context context;
    Socket mSocket;
    ArrayList<GroupsChatMsgsDataModel> mData = new ArrayList<>();
    private GroupChatViewRecylerAdapter.ItemClickListener mClickListener;

    public GroupChatViewRecylerAdapter(Context context, ArrayList<GroupsChatMsgsDataModel> dataModels, Socket mSocket) {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.mData = dataModels;
        this.mSocket = mSocket;
    }

    // inflates the cell layout from xml when needed
    @Override
    public GroupChatViewRecylerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.group_chat_recyler_view_item, parent, false);
        GroupChatViewRecylerAdapter.ViewHolder viewHolder = new GroupChatViewRecylerAdapter.ViewHolder(view);
        return viewHolder;
    }

    // binds the data to the textview in each cell
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        String date = mData.get(position).getCreated_at();

        if (position > 0) {
            String previous_date = mData.get(position - 1).getCreated_at();
            String[] str_splt = date.split(" ");
            String[] previous_str_splt = previous_date.split(" ");

            if (!str_splt[0].equalsIgnoreCase(previous_str_splt[0])) {
                holder.Msg_Time_layout.setVisibility(View.VISIBLE);
                holder.TimeAgoText.setText(TimesAgo(date));
            } else {
                holder.Msg_Time_layout.setVisibility(View.GONE);
            }
        } else {
            // always shown
            holder.Msg_Time_layout.setVisibility(View.VISIBLE);
            holder.TimeAgoText.setText(TimesAgo(date));
        }

        if (!mData.get(position).isNewMemberItem()) {
            holder.Msg_Member_Join_Layout.setVisibility(View.GONE);
            holder.Joind_Member_Text.setText("");
            holder.Msg_Content_Layout.setVisibility(View.VISIBLE);
        } else {
            holder.Msg_Content_Layout.setVisibility(View.GONE);
            holder.Msg_Member_Join_Layout.setVisibility(View.VISIBLE);
            holder.Joind_Member_Text.setText(mData.get(position).getMsg_TExt());
        }

        if (mData.get(position).isImageMsg()) {
            holder.msg_img.setVisibility(View.VISIBLE);
            holder.video_icon.setVisibility(View.GONE);
            if (mData.get(position).isUploadinStart()) {
                holder.Loading_spinner.setVisibility(View.GONE);
                holder.Uploading_progress.setVisibility(View.VISIBLE);
                holder.Uploading_progress_bar.setVisibility(View.VISIBLE);
                holder.msg_img.setImageDrawable(mData.get(position).getLoacal_image_drawable());
                new UploadFileToServer(URL.add_group_message, position, holder.Uploading_progress, mData.get(position), holder.Uploading_progress_bar).execute(mData.get(position).getLocal_image_path());
            } else {
                if (mData.get(position).getFile_path().length() > 7) {
                    holder.Loading_spinner.setVisibility(View.VISIBLE);
                    holder.Uploading_progress.setVisibility(View.GONE);
                    holder.Uploading_progress_bar.setVisibility(View.GONE);
                    ShowImage(images_baseurl + mData.get(position).getFile_path(), holder.msg_img, holder.msg_img, holder.Loading_spinner);
                } else {
                    holder.Uploading_progress.setVisibility(View.GONE);
                    holder.Uploading_progress_bar.setVisibility(View.GONE);
                    holder.Loading_spinner.setVisibility(View.GONE);
                    holder.msg_img.setImageDrawable(mData.get(position).getLoacal_image_drawable());
                }
            }
        } else if (mData.get(position).isVideoMsg()) {
            holder.msg_img.setVisibility(View.VISIBLE);
            holder.video_icon.setVisibility(View.VISIBLE);
            if (mData.get(position).isUploadinStart()) {
                holder.Loading_spinner.setVisibility(View.GONE);
                holder.Uploading_progress.setVisibility(View.VISIBLE);
                holder.Uploading_progress_bar.setVisibility(View.VISIBLE);
                holder.msg_img.setImageDrawable(mData.get(position).getLoacal_image_drawable());
                new UploadFileToServer(URL.add_group_message, position, holder.Uploading_progress, mData.get(position), holder.Uploading_progress_bar).execute(mData.get(position).getLocal_video_path());
            } else {
                if (mData.get(position).getFile_path().length() > 7) {
                    holder.Loading_spinner.setVisibility(View.VISIBLE);
                    holder.Uploading_progress.setVisibility(View.GONE);
                    holder.Uploading_progress_bar.setVisibility(View.GONE);
                    ShowImage(images_baseurl + mData.get(position).getPoster(), holder.msg_img, holder.msg_img, holder.Loading_spinner);
                } else {
                    holder.Uploading_progress.setVisibility(View.GONE);
                    holder.Uploading_progress_bar.setVisibility(View.GONE);
                    holder.Loading_spinner.setVisibility(View.GONE);
                    holder.msg_img.setImageDrawable(mData.get(position).getLoacal_image_drawable());
                }
            }
        } else {
            holder.Uploading_progress.setVisibility(View.GONE);
            holder.Uploading_progress_bar.setVisibility(View.GONE);
            holder.Loading_spinner.setVisibility(View.GONE);
            holder.Loading_spinner.setVisibility(View.GONE);
            holder.msg_img.setVisibility(View.GONE);
            holder.video_icon.setVisibility(View.GONE);
        }

        ShowImage(images_baseurl + mData.get(position).getUser_image_path(), holder.profile_img, holder.profile_img, null);
        holder.Msg_Member_Name.setText(mData.get(position).getMember_Name());
        if (mData.get(position).getMsg_TExt().length() == 0) {
            holder.Msg_Text.setVisibility(View.GONE);
        } else {
            holder.Msg_Text.setVisibility(View.VISIBLE);
//            holder.Msg_Text.setText(mData.get(position).getMsg_TExt());
            MakeKeywordClickableText(holder.Msg_Text.getContext(), mData.get(position).getMsg_TExt(), holder.Msg_Text);
        }


        if (mData.get(position).isCurrentUserLiked()) {
            holder.LikeMsg.setImageResource(R.drawable.ic_msg_like);
        } else {
            holder.LikeMsg.setImageResource(R.drawable.ic_favorite_border_white);
        }

        if (mData.get(position).getMsgLikes() == 0) {
            holder.Like_Msg_Count.setVisibility(View.INVISIBLE);
            holder.Like_Msg_Count.setText("");

        } else {
            holder.Like_Msg_Count.setVisibility(View.VISIBLE);
            holder.Like_Msg_Count.setText(mData.get(position).getMsgLikes() + "");
        }


        holder.LikeMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mData.get(position).isCurrentUserLiked()) {
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("message_id", mData.get(position).getId());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    new VollyAPICall(context, false, URL.remove_message_like, jsonObject, user.getSession_key(), Request.Method.POST, GroupChatViewRecylerAdapter.this, add_message_like);
                    mData.get(position).setCurrentUserLiked(false);
                    mData.get(position).setMsgLikes(mData.get(position).getMsgLikes() - 1);
                } else {
                    mData.get(position).setCurrentUserLiked(true);
                    mData.get(position).setMsgLikes(mData.get(position).getMsgLikes() + 1);
                    // like
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("message_id", mData.get(position).getId());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    new VollyAPICall(context, false, URL.add_message_like, jsonObject, user.getSession_key(), Request.Method.POST, GroupChatViewRecylerAdapter.this, add_message_like);

                }
                notifyItemChanged(position);
            }
        });


        Log.d("sdfa", TimesAgo("21/08/2017 3:40 PM"));
    }

    public void ShowImage(String path, final ImageView imageView, final ImageView second_imageview, final ProgressBar progressBar) {
        if (context != null) {
            Glide.with(context)
                    .load(path)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            if (progressBar != null) {
                                progressBar.setVisibility(View.GONE);
                            }
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            Log.d("ready", model);

                            if (progressBar != null) {
                                imageView.setImageDrawable(resource);
                                second_imageview.setImageDrawable(resource);
                                progressBar.setVisibility(View.GONE);
                            } else {
                                imageView.setImageDrawable(resource);
                                second_imageview.setImageDrawable(resource);
                            }
                            return false;
                        }
                    })
                    .into(480, 480);
        }
    }

    // total number of cells
    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public void onRequestSuccess(String response, APIActions.ApiActions apiActions) {
        Log.d("response", response);
        if (apiActions == add_message_like) {

        }
    }

    @Override
    public void onRequestError(String response, APIActions.ApiActions apiActions) {
        Log.d("response", response);
        try {
            JSONObject object = new JSONObject(response);
            CustomeToast.ShowCustomToast(getContext(), object.getString("errorMessage"), Gravity.TOP);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // stores and recycles views as they are scrolled off screen
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView Msg_Member_Name, Msg_Text, Like_Msg_Count, TimeAgoText, Joind_Member_Text;
        ImageView msg_img, video_icon, LikeMsg;
        ImageView profile_img;
        ProgressBar Uploading_progress_bar;
        RelativeLayout Uploading_progress;
        ProgressBar Loading_spinner;
        LinearLayout Msg_Time_layout, Msg_Content_Layout, Msg_Member_Join_Layout;

        public ViewHolder(View itemView) {
            super(itemView);
            Msg_Time_layout = itemView.findViewById(R.id.remaining_time);
            Msg_Content_Layout = itemView.findViewById(R.id.msgs_layout);
            Msg_Member_Join_Layout = itemView.findViewById(R.id.joined_membder_layout);
            Msg_Member_Name = itemView.findViewById(R.id.member_name);
            Msg_Text = itemView.findViewById(R.id.msg_text);
            msg_img = itemView.findViewById(R.id.msg_img);
            video_icon = itemView.findViewById(R.id.video_icon);
            Like_Msg_Count = itemView.findViewById(R.id.like_msg_count);
            LikeMsg = itemView.findViewById(R.id.like_msg);
            profile_img = itemView.findViewById(R.id.profile_image);
            TimeAgoText = itemView.findViewById(R.id.time_ago_text);
            Loading_spinner = itemView.findViewById(R.id.loading_spinner);
            Uploading_progress = itemView.findViewById(R.id.uploading_progress);
            Uploading_progress_bar = itemView.findViewById(R.id.uploading_progress_bar);
            Joind_Member_Text = itemView.findViewById(R.id.joind_member_text);
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    public void setClickListener(GroupChatViewRecylerAdapter.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }


    public String getFormattedDate(Context context, long smsTimeInMilis) {
        Calendar smsTime = Calendar.getInstance();
        smsTime.setTimeInMillis(smsTimeInMilis);
        Calendar now = Calendar.getInstance();
        final String timeFormatString = "h:mm aa";
        final String dateTimeFormatString = "EEEE, MMMM d, h:mm aa";
        final long HOURS = 60 * 60 * 60;
        if (now.get(Calendar.DATE) == smsTime.get(Calendar.DATE)) {
            return "Today " + DateFormat.format(timeFormatString, smsTime);
        } else if (now.get(Calendar.DATE) - smsTime.get(Calendar.DATE) == 1) {
            return "Yesterday " + DateFormat.format(timeFormatString, smsTime);
        } else if (now.get(Calendar.YEAR) == smsTime.get(Calendar.YEAR)) {
            return DateFormat.format(dateTimeFormatString, smsTime).toString();
        } else {
            return DateFormat.format("MMMM dd yyyy, h:mm aa", smsTime).toString();
        }
    }

    public String TimesAgo(String date) {
        String[] dt_splt;
        if (date.contains("/")) {
            dt_splt = date.split(" ")[0].split("/");
        } else {
            dt_splt = date.split(" ")[0].split("-");
        }
        int msg_day = Integer.parseInt(dt_splt[2]);
        int msg_month = Integer.parseInt(dt_splt[1]);
        int msg_year = Integer.parseInt(dt_splt[0]);

        Calendar calendar = Calendar.getInstance();

        int current_day = calendar.get(Calendar.DAY_OF_MONTH);
        int current_month = calendar.get(Calendar.MONTH);
        int current_year = calendar.get(Calendar.YEAR);

        if (msg_day == current_day && msg_month == current_month && msg_year == current_year) {
            return "Today";
        } else if (msg_day == current_day - 1 && msg_month == current_month && msg_year == current_year) {
            return "Yesterday";
        } else {
            SimpleDateFormat parseFormat = new SimpleDateFormat("EEEE ,MMMM");
            Date dt = new Date(msg_year, msg_month, msg_day - 1);
            String s = parseFormat.format(dt);
            return s + " " + msg_year;
        }
    }

    private class UploadFileToServer extends AsyncTask<String, Integer, String> {
        ProgressBar progressBar;
        int position;
        long totalSize = 0;
        RelativeLayout progress;
        String url_path;
        GroupsChatMsgsDataModel messagesChatMsgsDataModel;

        public UploadFileToServer(String url, final int position, final RelativeLayout progress, final GroupsChatMsgsDataModel messagesChatMsgsDataModel, ProgressBar uploading_progress) {
            this.progressBar = uploading_progress;
            this.position = position;
            this.progress = progress;
            this.url_path = url;
            this.messagesChatMsgsDataModel = messagesChatMsgsDataModel;
        }

        @Override
        protected void onPreExecute() {
            // setting progress bar to zero
            progressBar.setProgress(0);
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            // Making progress bar visible
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {
            return uploadFile(params[0]);
        }

        @SuppressWarnings("deprecation")
        private String uploadFile(String path) {
            String responseString = null;
            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost postRequest = new HttpPost(url_path);
                postRequest.setHeader("session_token", user.getSession_key());
                postRequest.setHeader("app_key", "4m9Nv1nbyLoaZAMyAhQri9BUXBxlD3yQxbAiHsn2hwQ=");
                File file = new File(path);
                AndroidMultiPartEntity reqEntity = new AndroidMultiPartEntity(
                        new AndroidMultiPartEntity.ProgressListener() {
                            @Override
                            public void transferred(long num) {
                                publishProgress(((int) ((num / (float) totalSize) * 100)));
                            }
                        });

                if (messagesChatMsgsDataModel.isImageMsg()) {
                    ContentType contentType = ContentType.create("image/jpeg");
                    FileBody cbFile = new FileBody(file, contentType.getMimeType(), file.getName());
                    reqEntity.addPart("image", cbFile);
                } else if (messagesChatMsgsDataModel.isVideoMsg()) {
                    ContentType contentType = ContentType.create("video/mp4");
                    FileBody cbFile = new FileBody(file, contentType.getMimeType(), file.getName());
                    reqEntity.addPart("video", cbFile);
                } else {
                    reqEntity.addPart("image", new StringBody(""));
                    reqEntity.addPart("video", new StringBody(""));
                }
                reqEntity.addPart("group_id", new StringBody(messagesChatMsgsDataModel.getGroup_id() + ""));
                reqEntity.addPart("text", new StringBody(messagesChatMsgsDataModel.getMsg_TExt()));
                totalSize = reqEntity.getContentLength();
                postRequest.setEntity(reqEntity);
                HttpResponse response = httpClient.execute(postRequest);
                BufferedReader reader = new BufferedReader(new InputStreamReader(
                        response.getEntity().getContent(), "UTF-8"));
                int code = response.getStatusLine().getStatusCode();
                String sResponse;
                StringBuilder s = new StringBuilder();
                while ((sResponse = reader.readLine()) != null) {
                    s = s.append(sResponse);
                }
                reader.close();
                System.out.println("Response: " + s);
                System.out.println("CODE: " + "" + code);
                return "" + s;

            } catch (Exception e) {
                // handle exception here
                Log.e(e.getClass().getName(), e.getMessage());
                return e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                Log.e("TAGGGGG", "Response from server: " + result);
                progressBar.setProgress(100);
                progress.setVisibility(View.GONE);
                messagesChatMsgsDataModel.setUploadinStart(false);
                mData.get(position).setUploadinStart(false);
                JSONObject response_object;

                JSONObject object = new JSONObject();
                try {
                    response_object = new JSONObject(result);
                    object.put("group_id", messagesChatMsgsDataModel.getGroup_id());
                    object.put("other_id", user.getUser_id());
                    object.put("other_name", user.getFirst_name());
                    object.put("photo", user.getImage_path());
                    object.put("text", messagesChatMsgsDataModel.getMsg_TExt());
                    object.put("file", response_object.getJSONObject("successData").getString("file_path"));
                    object.put("type", response_object.getJSONObject("successData").getString("type"));
                    if (response_object.getJSONObject("successData").getString("type").equalsIgnoreCase("video")) {
                        messagesChatMsgsDataModel.setImageMsg(false);
                        messagesChatMsgsDataModel.setVideoMsg(true);
                        messagesChatMsgsDataModel.setFile_path(response_object.getJSONObject("successData").getString("file_path"));
                        mData.get(position).setImageMsg(false);
                        mData.get(position).setVideoMsg(true);
                        mData.get(position).setPoster(response_object.getJSONObject("successData").getString("poster"));
                        messagesChatMsgsDataModel.setFile_path(response_object.getJSONObject("successData").getString("file_path"));
                        mData.get(position).setFile_path(response_object.getJSONObject("successData").getString("file_path"));
                    } else {
                        messagesChatMsgsDataModel.setImageMsg(true);
                        messagesChatMsgsDataModel.setVideoMsg(false);
                        mData.get(position).setImageMsg(true);
                        mData.get(position).setVideoMsg(false);
                        messagesChatMsgsDataModel.setFile_path(response_object.getJSONObject("successData").getString("file_path"));
                        mData.get(position).setFile_path(response_object.getJSONObject("successData").getString("file_path"));
                    }

                    object.put("file_poster", response_object.getJSONObject("successData").getString("poster"));
                    object.put("images_base", images_baseurl);
                    object.put("video_base", videos_baseurl);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                notifyItemChanged(position);
                mSocket.emit("group_message_get", object);
            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
            }

        }

    }
}