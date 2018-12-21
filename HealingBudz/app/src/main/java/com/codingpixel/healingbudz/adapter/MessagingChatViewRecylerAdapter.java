package com.codingpixel.healingbudz.adapter;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
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
import com.codingpixel.healingbudz.DataModel.MessagesChatMsgsDataModel;
import com.codingpixel.healingbudz.R;
import com.codingpixel.healingbudz.Utilities.DateConverter;
import com.codingpixel.healingbudz.Utilities.Utility;
import com.codingpixel.healingbudz.activity.splash.Splash;
import com.codingpixel.healingbudz.customeUI.Preview;
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
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.codingpixel.healingbudz.Utilities.ClickAbleKeywordText.MakeKeywordClickableText;
import static com.codingpixel.healingbudz.activity.splash.Splash.user;
import static com.codingpixel.healingbudz.network.model.URL.images_baseurl;
import static com.codingpixel.healingbudz.network.model.URL.videos_baseurl;

public class MessagingChatViewRecylerAdapter extends RecyclerView.Adapter<MessagingChatViewRecylerAdapter.ViewHolder> implements View.OnClickListener, Preview.PreviewListener {
    private LayoutInflater mInflater;
    Context context;
    Socket mSocket;
    ArrayList<MessagesChatMsgsDataModel> mData = new ArrayList<>();
    private MessagingChatViewRecylerAdapter.ItemClickListener mClickListener;

    public MessagingChatViewRecylerAdapter(Context context, Socket socket, ArrayList<MessagesChatMsgsDataModel> dataModels) {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.mData = dataModels;
        this.mSocket = socket;
    }

    // inflates the cell layout from xml when needed
    @Override
    public MessagingChatViewRecylerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.messaging_chat_recyler_view_item, parent, false);
        MessagingChatViewRecylerAdapter.ViewHolder viewHolder = new MessagingChatViewRecylerAdapter.ViewHolder(view);
        return viewHolder;
    }

    // binds the data to the textview in each cell
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        String date = mData.get(position).getAdded_date();
        holder.TimeAgoText.setVisibility(View.GONE);
        if (position > 0) {
            String previous_date = mData.get(position - 1).getAdded_date();

            String[] str_splt = date.split(" ");
            String[] previous_str_splt = previous_date.split(" ");

            if (!str_splt[0].equalsIgnoreCase(previous_str_splt[0])) {
                holder.Msg_Time_layout.setVisibility(View.VISIBLE);
//                holder.TimeAgoText.setText(TimesAgo(date));
                holder.TimeAgoText.setText(DateConverter.getPrettyTime(previous_date));
            } else {
                holder.Msg_Time_layout.setVisibility(View.GONE);
            }
        } else {
            // always shown
            holder.Msg_Time_layout.setVisibility(View.VISIBLE);
//            holder.TimeAgoText.setText(TimesAgo(date));
            holder.TimeAgoText.setText(DateConverter.getPrettyTime(date));
        }
        if (mData.get(position).isReceiver()) {
            holder.Receiver_Msg_Layout.setVisibility(View.GONE);
            holder.Sender_Msg_Layout.setVisibility(View.VISIBLE);
            holder.receiver_name.setText("");
            holder.sender_name.setText(Splash.otherName);
        } else {
            holder.receiver_name.setText(MessageFormat.format("{0} {1}", Splash.user.getFirst_name(), Splash.user.getLast_name()));
            holder.sender_name.setText("");
            holder.Receiver_Msg_Layout.setVisibility(View.VISIBLE);
            holder.Sender_Msg_Layout.setVisibility(View.GONE);
        }
        holder.receiver_date.setText(DateConverter.getPrettyTime(mData.get(position).getCreateDate()));
        holder.sender_date.setText(DateConverter.getPrettyTime(mData.get(position).getCreateDate()));

        if (mData.get(position).isImageMsg()) {
            holder.Sender_video_icon.setVisibility(View.GONE);
            holder.Receiver_video_icon.setVisibility(View.GONE);
            holder.Sender_Media_Img.setVisibility(View.VISIBLE);
            holder.Receiver_Media_Img.setVisibility(View.VISIBLE);
            if (mData.get(position).isUploadinStart()) {
                holder.Media_Loading.setVisibility(View.GONE);
                holder.Media_Loading_recever.setVisibility(View.GONE);
                holder.Uploading_progress.setVisibility(View.VISIBLE);
                holder.Receiver_Media_Img.setImageDrawable(mData.get(position).getLoacal_image_drawable());
                holder.Sender_Media_Img.setImageDrawable(mData.get(position).getLoacal_image_drawable());
                new UploadFileToServer(URL.send_message, position, holder.Uploading_progress, mData.get(position), holder.Uploading_progress_bar).execute(mData.get(position).getLocal_image_path());
            } else {
                holder.Uploading_progress.setVisibility(View.GONE);
                if (mData.get(position).getImage_Path().length() > 7) {
                    if (mData.get(position).isReceiver()) {
                        holder.Media_Loading.setVisibility(View.VISIBLE);
                        holder.Media_Loading_recever.setVisibility(View.GONE);
                        ShowImage(images_baseurl + mData.get(position).getImage_Path(), holder.Receiver_Media_Img, holder.Sender_Media_Img, holder.Media_Loading);
                    } else {
                        holder.Media_Loading_recever.setVisibility(View.VISIBLE);
                        holder.Media_Loading.setVisibility(View.GONE);
                        ShowImage(images_baseurl + mData.get(position).getImage_Path(), holder.Receiver_Media_Img, holder.Sender_Media_Img, holder.Media_Loading_recever);
                    }
                } else {
                    holder.Media_Loading.setVisibility(View.GONE);
                    holder.Media_Loading_recever.setVisibility(View.GONE);
                    holder.Uploading_progress.setVisibility(View.GONE);
                    holder.Receiver_Media_Img.setImageDrawable(mData.get(position).getLoacal_image_drawable());
                    holder.Sender_Media_Img.setImageDrawable(mData.get(position).getLoacal_image_drawable());
                }
            }
        } else if (mData.get(position).isVideoMsg()) {
            holder.Sender_Media_Img.setVisibility(View.VISIBLE);
            holder.Receiver_Media_Img.setVisibility(View.VISIBLE);
            holder.Sender_video_icon.setVisibility(View.VISIBLE);
            holder.Receiver_video_icon.setVisibility(View.VISIBLE);
            if (mData.get(position).isUploadinStart()) {
                holder.Media_Loading.setVisibility(View.GONE);
                holder.Media_Loading_recever.setVisibility(View.GONE);
                holder.Uploading_progress.setVisibility(View.VISIBLE);
                holder.Receiver_Media_Img.setImageDrawable(mData.get(position).getLocal_video_thumbnil());
                holder.Sender_Media_Img.setImageDrawable(mData.get(position).getLocal_video_thumbnil());
                new UploadFileToServer(URL.send_message, position, holder.Uploading_progress, mData.get(position), holder.Uploading_progress_bar).execute(mData.get(position).getLocal_video_path());
            } else {
                holder.Uploading_progress.setVisibility(View.GONE);
                if (mData.get(position).getVideo_path().length() > 7) {
                    if (mData.get(position).isReceiver()) {
                        holder.Media_Loading.setVisibility(View.VISIBLE);
                        holder.Media_Loading_recever.setVisibility(View.GONE);
                        ShowImage(images_baseurl + mData.get(position).getVideo_thumbni(), holder.Receiver_Media_Img, holder.Sender_Media_Img, holder.Media_Loading);
                    } else {
                        holder.Media_Loading_recever.setVisibility(View.VISIBLE);
                        holder.Media_Loading.setVisibility(View.GONE);
                        ShowImage(images_baseurl + mData.get(position).getVideo_thumbni(), holder.Receiver_Media_Img, holder.Sender_Media_Img, holder.Media_Loading_recever);
                    }
                } else {
                    holder.Media_Loading.setVisibility(View.GONE);
                    holder.Media_Loading_recever.setVisibility(View.GONE);
                    holder.Uploading_progress.setVisibility(View.GONE);
                    holder.Receiver_Media_Img.setImageDrawable(mData.get(position).getLocal_video_thumbnil());
                    holder.Sender_Media_Img.setImageDrawable(mData.get(position).getLocal_video_thumbnil());
                }
            }
        } else {
            holder.Sender_Media_Img.setVisibility(View.GONE);
            holder.Receiver_Media_Img.setVisibility(View.GONE);
            holder.Sender_video_icon.setVisibility(View.GONE);
            holder.Receiver_video_icon.setVisibility(View.GONE);
            holder.Media_Loading.setVisibility(View.GONE);
        }

        if (mData.get(position).getMsg_TExt().length() > 0) {
            holder.Receiver_Text.setVisibility(View.VISIBLE);
            holder.Sender_Text.setVisibility(View.VISIBLE);
//            holder.Receiver_Text.setText(mData.get(position).getMsg_TExt());
//            holder.Sender_Text.setText(mData.get(position).getMsg_TExt());
            List<String> urls = Utility.extractURL(mData.get(position).getMsg_TExt());
            if (urls != null && urls.size() > 0) {


                holder.receiver_scraps.setVisibility(View.VISIBLE);
                holder.sender_scraps.setVisibility(View.VISIBLE);
                holder.receiver_scraps.setListener(MessagingChatViewRecylerAdapter.this);
                holder.sender_scraps.setListener(MessagingChatViewRecylerAdapter.this);
                holder.sender_scraps.setOnClickListener(MessagingChatViewRecylerAdapter.this);
                holder.receiver_scraps.setOnClickListener(MessagingChatViewRecylerAdapter.this);
                holder.sender_scraps.setData(urls.get(0));
                holder.receiver_scraps.setData(urls.get(0));
                MakeKeywordClickableText(holder.Receiver_Text.getContext(), mData.get(position).getMsg_TExt(), holder.Receiver_Text);
                MakeKeywordClickableText(holder.Sender_Text.getContext(), mData.get(position).getMsg_TExt(), holder.Sender_Text);
//                holder.Receiver_Text.setVisibility(View.GONE);
//                holder.Sender_Text.setVisibility(View.GONE);
            } else {
                holder.receiver_scraps.setVisibility(View.GONE);
                holder.sender_scraps.setVisibility(View.GONE);
                holder.Receiver_Text.setVisibility(View.VISIBLE);
                holder.Sender_Text.setVisibility(View.VISIBLE);
                MakeKeywordClickableText(holder.Receiver_Text.getContext(), mData.get(position).getMsg_TExt(), holder.Receiver_Text);
                MakeKeywordClickableText(holder.Sender_Text.getContext(), mData.get(position).getMsg_TExt(), holder.Sender_Text);
            }


        } else {
            holder.Receiver_Text.setVisibility(View.GONE);
            holder.Sender_Text.setVisibility(View.GONE);
            holder.receiver_scraps.setVisibility(View.GONE);
            holder.sender_scraps.setVisibility(View.GONE);
        }
    }

    // total number of cells
    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void ShowImage(String path, final ImageView imageView, final ImageView second_imageview, final ProgressBar progressBar) {
        Glide.with(context)
                .load(path)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        Log.d("ready", model);
//                        imageView.setImageDrawable(resource);
//                        second_imageview.setImageDrawable(resource);
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(imageView);
        Glide.with(context)
                .load(path)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        Log.d("ready", model);
//                        imageView.setImageDrawable(resource);
//                        second_imageview.setImageDrawable(resource);
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(second_imageview);
    }

    public void ShowImage(String path, final ImageView imageView, final ImageView second_imageview, final ProgressBar progressBar, ViewHolder holder) {
        Glide.with(context)
                .load(path)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        Log.d("ready", model);
                        imageView.setImageDrawable(resource);
                        second_imageview.setImageDrawable(resource);
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(1080, 1080);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sender_scraps:
            case R.id.receiver_scraps:
                Utility.launchWebUrl(view.getContext(), ((Preview) view).getUrl());
                break;
        }
    }

    @Override
    public void onDataReady(final Preview preview) {
        new Handler(preview.getContext().getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                preview.setMessage(preview.getLink(), ContextCompat.getColor(preview.getContext(), R.color.color_link));

            }
        });
    }

    // stores and recycles views as they are scrolled off screen
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView TimeAgoText, Sender_Text, Receiver_Text, sender_name, receiver_name;
        ImageView Sender_Media_Img, Receiver_Media_Img, Sender_video_icon, Receiver_video_icon;
        LinearLayout Msg_Time_layout;
        ProgressBar Media_Loading;
        ProgressBar Media_Loading_recever;
        RelativeLayout Uploading_progress;
        ProgressBar Uploading_progress_bar;
        RelativeLayout Sender_Msg_Layout, Receiver_Msg_Layout;
        Preview receiver_scraps, sender_scraps;
        TextView receiver_date,sender_date;

        public ViewHolder(View itemView) {
            super(itemView);
            sender_name = itemView.findViewById(R.id.sender_name);
            sender_date = itemView.findViewById(R.id.sender_date);
            receiver_date = itemView.findViewById(R.id.receiver_date);
            receiver_name = itemView.findViewById(R.id.receiver_name);
            Msg_Time_layout = itemView.findViewById(R.id.remaining_time);
            Sender_Msg_Layout = itemView.findViewById(R.id.sender_msgs_layout);
            Receiver_Msg_Layout = itemView.findViewById(R.id.reciever_msg_layout);
            TimeAgoText = itemView.findViewById(R.id.time_ago_text);
            Media_Loading = itemView.findViewById(R.id.loading_spinner);
            Media_Loading_recever = itemView.findViewById(R.id.loading_spinner_rcvr);
            sender_scraps = itemView.findViewById(R.id.sender_scraps);
            receiver_scraps = itemView.findViewById(R.id.receiver_scraps);

//            receiver_scraps.setDescriptionTextColor(Color.GRAY);
//            receiver_scraps.setTitleTextColor(Color.YELLOW);
//            receiver_scraps.setSiteNameTextColor(Color.BLUE);
//            receiver_scraps.getImgViewImage().setCornerRadiiDP(10, 0, 0, 0);

//            sender_scraps.setDescriptionTextColor(Color.GRAY);
//            sender_scraps.setTitleTextColor(Color.YELLOW);
//            sender_scraps.setSiteNameTextColor(Color.BLUE);
//            sender_scraps.getImgViewImage().setCornerRadiiDP(10, 0, 0, 0);
            Sender_Text = itemView.findViewById(R.id.sender_msg_text);
            Receiver_Text = itemView.findViewById(R.id.receiver_msg_text);


            Receiver_Media_Img = itemView.findViewById(R.id.receiver_msg_img);
            Sender_Media_Img = itemView.findViewById(R.id.sender_msg_img);


            Sender_video_icon = itemView.findViewById(R.id.sender_video_icon);
            Receiver_video_icon = itemView.findViewById(R.id.receiver_video_icon);
            Uploading_progress = itemView.findViewById(R.id.uploading_progress);
            Uploading_progress_bar = itemView.findViewById(R.id.uploading_progress_bar);
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }


    }

    public void setClickListener(MessagingChatViewRecylerAdapter.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    private class UploadFileToServer extends AsyncTask<String, Integer, String> {
        ProgressBar progressBar;
        int position;
        long totalSize = 0;
        RelativeLayout progress;
        String url_path;
        MessagesChatMsgsDataModel messagesChatMsgsDataModel;

        public UploadFileToServer(String url, final int position, final RelativeLayout progress, final MessagesChatMsgsDataModel messagesChatMsgsDataModel, ProgressBar uploading_progress) {
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
                reqEntity.addPart("receiver_id", new StringBody(messagesChatMsgsDataModel.getReceiver_id() + ""));
                reqEntity.addPart("message", new StringBody(messagesChatMsgsDataModel.getMsg_TExt()));
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
            Log.e("TAGGGGG", "Response from server: " + result);
            JSONObject dataObject;
            JSONObject object2 = null;
            try {
                progressBar.setProgress(100);
                progress.setVisibility(View.GONE);

                JSONObject response_object;

                JSONObject object = new JSONObject();
                try {
                    response_object = new JSONObject(result);
                    object.put("user_id", messagesChatMsgsDataModel.getReceiver_id());
                    object.put("other_id", user.getUser_id());
                    object.put("other_name", user.getFirst_name());
                    object.put("photo", "");
                    object.put("text", messagesChatMsgsDataModel.getMsg_TExt());
                    object.put("file", response_object.getJSONObject("successData").getString("file_path"));
                    object.put("file_type", response_object.getJSONObject("successData").getString("file_type"));
                    object.put("file_poster", response_object.getJSONObject("successData").getString("poster"));
                    object.put("images_base", images_baseurl);
                    object.put("video_base", videos_baseurl);
                    mSocket.emit("message_get", object);
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
                messagesChatMsgsDataModel.setUploadinStart(false);
                mData.get(position).setUploadinStart(false);

                object2 = new JSONObject(result);
                dataObject = object2.getJSONObject("successData");
                if (dataObject.getString("file_type").equalsIgnoreCase("video")) {
                    messagesChatMsgsDataModel.setImageMsg(false);
                    messagesChatMsgsDataModel.setVideoMsg(true);
                    messagesChatMsgsDataModel.setVideo_thumbni(dataObject.getString("poster"));
                    mData.get(position).setImageMsg(false);
                    mData.get(position).setVideoMsg(true);
                    mData.get(position).setVideo_thumbni(dataObject.getString("poster"));
                    messagesChatMsgsDataModel.setVideo_path(dataObject.getString("file_path"));
                    mData.get(position).setVideo_path(dataObject.getString("file_path"));
                } else {
                    messagesChatMsgsDataModel.setImageMsg(true);
                    messagesChatMsgsDataModel.setVideoMsg(false);
                    mData.get(position).setImageMsg(true);
                    mData.get(position).setVideoMsg(false);
                    messagesChatMsgsDataModel.setImage_Path(dataObject.getString("file_path"));
                    mData.get(position).setImage_Path(dataObject.getString("file_path"));
                }
                notifyItemChanged(position);
                super.onPostExecute(result);
            } catch (JSONException e) {
                e.printStackTrace();
            }

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
            Date dt = new Date(msg_year, msg_month - 1, msg_day - 1);
            String s = parseFormat.format(dt);
            return s + " " + msg_year;
        }
    }
}