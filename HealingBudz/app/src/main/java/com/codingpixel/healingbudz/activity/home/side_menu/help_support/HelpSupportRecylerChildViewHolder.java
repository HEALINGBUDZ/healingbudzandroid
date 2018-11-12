package com.codingpixel.healingbudz.activity.home.side_menu.help_support;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.codingpixel.healingbudz.R;
import com.codingpixel.healingbudz.customeUI.customspinner;
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;

public class HelpSupportRecylerChildViewHolder extends ChildViewHolder {
    public View childItemView;
    public Button invite_bud, submit,ask_support;
    public EditText email_edit_text, phone_edit_text, message_text;
    public TextView term_condition_link, policy_link,year_text,ver_num;
    public customspinner link_to;
    public LinearLayout Item_One, Item_Two, Item_Three, send_play_store;

    public HelpSupportRecylerChildViewHolder(View itemView) {
        super(itemView);
        childItemView = itemView;
        link_to = itemView.findViewById(R.id.link_to);
        ask_support = itemView.findViewById(R.id.ask_support);
        year_text = itemView.findViewById(R.id.year_text);
        ver_num = itemView.findViewById(R.id.ver_num);
        invite_bud = itemView.findViewById(R.id.invite_bud);
        submit = itemView.findViewById(R.id.submit);
        email_edit_text = itemView.findViewById(R.id.email_edit_text);
        phone_edit_text = itemView.findViewById(R.id.phone_edit_text);
        message_text = itemView.findViewById(R.id.message_text);
        policy_link = itemView.findViewById(R.id.policy_link);
        term_condition_link = itemView.findViewById(R.id.term_condition_link);
        send_play_store = itemView.findViewById(R.id.send_play_store);
        Item_One = itemView.findViewById(R.id.item_one);
        Item_Two = itemView.findViewById(R.id.item_two);
        Item_Three = itemView.findViewById(R.id.item_three);
    }
}
