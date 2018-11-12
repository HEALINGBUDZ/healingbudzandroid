package com.codingpixel.healingbudz.adapter;


import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.TextView;

import com.android.volley.Request;
import com.codingpixel.healingbudz.DataModel.SubUserHelpSupport;
import com.codingpixel.healingbudz.R;
import com.codingpixel.healingbudz.Utilities.CallUser;
import com.codingpixel.healingbudz.Utilities.LinksUtils;
import com.codingpixel.healingbudz.activity.home.side_menu.help_support.HelpSupportRecylerChildViewHolder;
import com.codingpixel.healingbudz.activity.home.side_menu.help_support.HelpSupportRecylerGroupViewHolder;
import com.codingpixel.healingbudz.activity.home.side_menu.help_support.PrivacyPoliccy;
import com.codingpixel.healingbudz.activity.home.side_menu.help_support.TermAndConditions;
import com.codingpixel.healingbudz.customeUI.CustomeToast;
import com.codingpixel.healingbudz.data_structure.APIActions;
import com.codingpixel.healingbudz.network.VollyAPICall;
import com.codingpixel.healingbudz.network.model.APIResponseListner;
import com.codingpixel.healingbudz.network.model.URL;
import com.freshchat.consumer.sdk.Freshchat;
import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.codingpixel.healingbudz.activity.splash.Splash.user;
import static com.codingpixel.healingbudz.application.HealingBudApplication.getContext;
import static com.codingpixel.healingbudz.static_function.IntentFunction.GoTo;
import static com.codingpixel.healingbudz.static_function.UIModification.HideKeyboard;

public class HelpAndSupportRecylerAdapter extends ExpandableRecyclerViewAdapter<HelpSupportRecylerGroupViewHolder, HelpSupportRecylerChildViewHolder> implements APIResponseListner {
    List<String> list = new ArrayList<>();
    List<SubUserHelpSupport> listSubUser = new ArrayList<>();
    int subPosition = 0;

    public HelpAndSupportRecylerAdapter(List<? extends ExpandableGroup> groups) {
        super(groups);
        setList();
    }

    public HelpAndSupportRecylerAdapter(List<? extends ExpandableGroup> groups, List<SubUserHelpSupport> listSubUser) {
        super(groups);
        setList(listSubUser);
    }

    HelpSupportRecylerChildViewHolder mainHolder;

    @Override
    public HelpSupportRecylerGroupViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.journal_details_recyler_view_item, parent, false);
        HelpSupportRecylerGroupViewHolder holder = new HelpSupportRecylerGroupViewHolder(view);
        return holder;
    }

    @Override
    public HelpSupportRecylerChildViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.help_support_recyler_item_expandable_item, parent, false);
        return new HelpSupportRecylerChildViewHolder(view);
    }

    @Override
    public void onBindChildViewHolder(final HelpSupportRecylerChildViewHolder holder, final int flatPosition, ExpandableGroup group, final int childIndex) {
        mainHolder = holder;
        holder.ask_support.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Freshchat.showConversations(v.getContext());
            }
        });
        if (group.getTitle().equalsIgnoreCase("Support Message Center")) {
            holder.Item_One.setVisibility(View.VISIBLE);
            holder.Item_Two.setVisibility(View.GONE);
            holder.Item_Three.setVisibility(View.GONE);
        } else if (group.getTitle().equalsIgnoreCase("Contact")) {
            holder.Item_One.setVisibility(View.GONE);
            holder.Item_Two.setVisibility(View.VISIBLE);
            holder.Item_Three.setVisibility(View.GONE);
        } else if (group.getTitle().equalsIgnoreCase("Legal")) {
            holder.Item_One.setVisibility(View.GONE);
            holder.Item_Two.setVisibility(View.GONE);
            holder.Item_Three.setVisibility(View.VISIBLE);
        }
        holder.policy_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoTo(view.getContext(), PrivacyPoliccy.class);
//                LinksUtils.GotoWebLink("http://139.162.37.73/healingbudz/privacy-policy", (Activity) view.getContext());
            }
        });
        holder.term_condition_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoTo(view.getContext(), TermAndConditions.class);
//                LinksUtils.GotoWebLink("http://139.162.37.73/healingbudz/terms-conditions", (Activity) view.getContext());
            }
        });
        holder.invite_bud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HideKeyboard((Activity) view.getContext());
                if (holder.email_edit_text.getText().toString().trim().length() > 0 && holder.phone_edit_text.getText().toString().trim().length() > 0) {
                    sendInviteEmail(view, holder.email_edit_text.getText().toString(), false);
                    CallUser.sendSMS(holder.phone_edit_text.getText().toString(), "Healing Budz", view.getContext());
                    holder.phone_edit_text.setText("");
                    holder.email_edit_text.setText("");
                } else if (holder.phone_edit_text.getText().toString().trim().length() > 0) {
                    CallUser.sendSMS(holder.phone_edit_text.getText().toString(), "Healing Budz", view.getContext());
                    holder.phone_edit_text.setText("");
                } else if(holder.email_edit_text.getText().toString().trim().length() > 0){
                    sendInviteEmail(view, holder.email_edit_text.getText().toString());
                    holder.email_edit_text.setText("");
                }else {

                }
            }
        });
        holder.phone_edit_text.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || actionId == KeyEvent.ACTION_DOWN
                        && actionId == KeyEvent.KEYCODE_ENTER) {
                    HideKeyboard((Activity) v.getContext());
//                    holder.email_edit_text.clearFocus();
//                    holder.phone_edit_text.clearFocus();
//                    holder.submit.requestFocus();
                    HideKeyboard((Activity) v.getContext());
                    return true;
                }
                return false;
            }
        });
        holder.submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.message_text.getText().length() > 0) {
                    sendMessageApi(view, holder.message_text.getText().toString(), holder.link_to.GetSelectedValue());
                    holder.message_text.setText("");
                }

            }
        });
        holder.send_play_store.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LinksUtils.PlayStore((Activity) view.getContext(), "");
            }
        });

        holder.year_text.setText("Copyright Healing Budz, Inc. " + Calendar.getInstance().get(Calendar.YEAR));
        holder.ver_num.setText("Version " + Calendar.getInstance().get(Calendar.YEAR));

        try {
            PackageInfo pInfo = holder.itemView.getContext().getPackageManager().getPackageInfo(holder.itemView.getContext().getPackageName(), 0);
            String version = pInfo.versionName;
            holder.ver_num.setText("Version " + version);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        holder.link_to.SetAdapter(list);
        holder.link_to.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                holder.link_to.SetValue();
                subPosition = position;

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void setList() {
        listSubUser.add(new SubUserHelpSupport(user.getUser_id(), user.getFirst_name()));
        list.add(user.getFirst_name());
//        list.add("Select One");
//        list.add("Select Two");
//        list.add("Select Three");
//        list.add("Select Four");
//        list.add("Select Five");
    }

    public void setList(List<SubUserHelpSupport> listSubUser) {
        this.listSubUser.clear();
        list.clear();
        this.listSubUser.add(new SubUserHelpSupport(user.getUser_id(), user.getFirst_name()));
        list.add(user.getFirst_name());
        this.listSubUser.addAll(listSubUser);
        for (int i = 0; i < listSubUser.size(); i++) {
            list.add(listSubUser.get(i).getName());
        }

//        list.add("Select One");
//        list.add("Select Two");
//        list.add("Select Three");
//        list.add("Select Four");
//        list.add("Select Five");
    }

    @Override
    public void onBindGroupViewHolder(HelpSupportRecylerGroupViewHolder holder, int flatPosition, ExpandableGroup group) {
        holder.setGenreTitle(group);
    }

    void sendMessageApi(View view, String email, String subUser) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("message", email);
            if (subPosition != 0) {
//                sub_user_id
                jsonObject.put("sub_user_id", listSubUser.get(subPosition).getId());
            } else {
                jsonObject.put("sub_user_id", "");
            }
            new VollyAPICall(view.getContext(), true, URL.baseurl + "mail_support", jsonObject, user.getSession_key(), Request.Method.POST, HelpAndSupportRecylerAdapter.this, APIActions.ApiActions.get_invite);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    void sendInviteEmail(View view, String email) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("email", email);
            new VollyAPICall(view.getContext(), true, URL.baseurl + "invite", jsonObject, user.getSession_key(), Request.Method.POST, HelpAndSupportRecylerAdapter.this, APIActions.ApiActions.get_invite);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    void sendInviteEmail(View view, String email, boolean isShow) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("email", email);
            new VollyAPICall(view.getContext(), false, URL.baseurl + "invite", jsonObject, user.getSession_key(), Request.Method.POST, HelpAndSupportRecylerAdapter.this, APIActions.ApiActions.get_invite);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestSuccess(String response, APIActions.ApiActions apiActions) {
        Log.d("onRequestSuccess: ", response);
//        mainHolder.email_edit_text.setText("");
        JSONObject object = null;
        try {
            object = new JSONObject(response);
            CustomeToast.ShowCustomToast(getContext(), object.getString("successMessage"), Gravity.TOP);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestError(String response, APIActions.ApiActions apiActions) {
        try {
            JSONObject object = new JSONObject(response);
            CustomeToast.ShowCustomToast(getContext(), object.getString("errorMessage"), Gravity.TOP);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
