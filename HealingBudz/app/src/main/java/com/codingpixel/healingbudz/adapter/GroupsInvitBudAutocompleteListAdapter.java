package com.codingpixel.healingbudz.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codingpixel.healingbudz.DataModel.GroupsInviteNewBudDataModel;
import com.codingpixel.healingbudz.R;

import java.util.ArrayList;
import java.util.List;

public class GroupsInvitBudAutocompleteListAdapter extends ArrayAdapter<GroupsInviteNewBudDataModel> {
    private final Context mContext;
    private LayoutInflater mInflater;
    private final List<GroupsInviteNewBudDataModel> mGroupsInviteNewBudDataModels;
    private final List<GroupsInviteNewBudDataModel> mGroupsInviteNewBudDataModels_All;
    private final List<GroupsInviteNewBudDataModel> mGroupsInviteNewBudDataModels_Suggestion;
    private final int mLayoutResourceId;
    int filter_type = 0;
    private ItemClickListener onInviteClickListner;

    public interface ItemClickListener {
        void onInviteButtonClick(View view, int position, GroupsInviteNewBudDataModel groupsInviteNewBudDataModel);
    }

    public GroupsInvitBudAutocompleteListAdapter(Context context, int resource, List<GroupsInviteNewBudDataModel> GroupsInviteNewBudDataModels, ItemClickListener onInviteClickListner, int filter_type) {
        super(context, resource, GroupsInviteNewBudDataModels);
        this.mInflater = LayoutInflater.from(getContext());
        this.mContext = context;
        this.filter_type = filter_type;
        this.mLayoutResourceId = resource;
        this.mGroupsInviteNewBudDataModels = new ArrayList<>(GroupsInviteNewBudDataModels);
        this.mGroupsInviteNewBudDataModels_All = new ArrayList<>(GroupsInviteNewBudDataModels);
        this.mGroupsInviteNewBudDataModels_Suggestion = new ArrayList<>();
        this.onInviteClickListner = onInviteClickListner;
    }

    public int getCount() {
        return mGroupsInviteNewBudDataModels.size();
    }

    public GroupsInviteNewBudDataModel getItem(int position) {
        return mGroupsInviteNewBudDataModels.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        try {
            if (convertView == null) {
                convertView = this.mInflater.inflate(mLayoutResourceId, parent, false);
            }
            final GroupsInviteNewBudDataModel GroupsInviteNewBudDataModel = getItem(position);
            TextView name = convertView.findViewById(R.id.bud_name);
            if (filter_type == 0) {
                name.setText(GroupsInviteNewBudDataModel.getName());
            } else if (filter_type == 1) {
                name.setText(GroupsInviteNewBudDataModel.getEmail());
            }
            final ImageView Invite_bud_button = convertView.findViewById(R.id.add_invitation_button);

            if (GroupsInviteNewBudDataModel.isInvited()) {
                Invite_bud_button.setImageResource(R.drawable.ic_invite_group_bud_invited_btn);
                name.setTextColor(Color.parseColor("#f79124"));
            } else {
                Invite_bud_button.setImageResource(R.drawable.ic_invite_groups_bud_add_btn);
                name.setTextColor(Color.parseColor("#808080"));
            }
            name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (filter_type == 0) {
                        if (!GroupsInviteNewBudDataModel.isInvited()) {
                            GroupsInviteNewBudDataModel.setInvited(true);
                            notifyDataSetChanged();
                            onInviteClickListner.onInviteButtonClick(view, position, GroupsInviteNewBudDataModel);
                        }
                    }
                }
            });
            Invite_bud_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (filter_type == 0) {
                        if (!GroupsInviteNewBudDataModel.isInvited()) {
                            GroupsInviteNewBudDataModel.setInvited(true);
                            notifyDataSetChanged();
                            onInviteClickListner.onInviteButtonClick(view, position, GroupsInviteNewBudDataModel);
                        }
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
        return convertView;
    }


    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            public String convertResultToString(Object resultValue) {
                if (filter_type == 0) {
                    return ((GroupsInviteNewBudDataModel) resultValue).getName();
                } else {
                    return ((GroupsInviteNewBudDataModel) resultValue).getEmail();
                }
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                if (constraint != null) {
                    mGroupsInviteNewBudDataModels_Suggestion.clear();
                    for (GroupsInviteNewBudDataModel GroupsInviteNewBudDataModel : mGroupsInviteNewBudDataModels_All) {
                        switch (filter_type) {
                            case 0:
                                if (GroupsInviteNewBudDataModel.getName().toLowerCase().startsWith(constraint.toString().toLowerCase())) {
                                    mGroupsInviteNewBudDataModels_Suggestion.add(GroupsInviteNewBudDataModel);
                                }
                                break;
                            case 1:
                                if (GroupsInviteNewBudDataModel.getEmail().toLowerCase().startsWith(constraint.toString().toLowerCase())) {
                                    mGroupsInviteNewBudDataModels_Suggestion.add(GroupsInviteNewBudDataModel);
                                }
                                break;
                            case 2:
                                if (GroupsInviteNewBudDataModel.getName().toLowerCase().startsWith(constraint.toString().toLowerCase())) {
                                    mGroupsInviteNewBudDataModels_Suggestion.add(GroupsInviteNewBudDataModel);
                                }
                                break;
                        }

                    }
                    FilterResults filterResults = new FilterResults();
                    filterResults.values = mGroupsInviteNewBudDataModels_Suggestion;
                    filterResults.count = mGroupsInviteNewBudDataModels_Suggestion.size();
                    return filterResults;
                } else {
                    return new FilterResults();
                }
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                mGroupsInviteNewBudDataModels.clear();
                if (results != null && results.count > 0) {
                    // avoids unchecked cast warning when using mGroupsInviteNewBudDataModels.addAll((ArrayList<GroupsInviteNewBudDataModel>) results.values);
                    List<?> result = (List<?>) results.values;
                    for (Object object : result) {
                        if (object instanceof GroupsInviteNewBudDataModel) {
                            mGroupsInviteNewBudDataModels.add((GroupsInviteNewBudDataModel) object);
                        }
                    }
                } else if (constraint == null) {
                    // no filter, add entire original list back in
                    mGroupsInviteNewBudDataModels.addAll(mGroupsInviteNewBudDataModels_All);
                }
                notifyDataSetChanged();
            }
        };
    }
}