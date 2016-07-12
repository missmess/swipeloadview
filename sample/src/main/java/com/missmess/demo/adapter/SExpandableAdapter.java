package com.missmess.demo.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.missmess.demo.R;

import java.util.List;

/**
 * @author wl
 * @since 2016/07/08 17:34
 */
public class SExpandableAdapter extends BaseExpandableListAdapter {
    private List<String> datas;

    public SExpandableAdapter(List<String> datas) {
        this.datas = datas;
    }

    @Override
    public int getGroupCount() {
        return datas.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 3;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return null;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return null;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        TextView tv = (TextView) View.inflate(parent.getContext(), R.layout.item_text, null);
        tv.setText(String.format("%s %d", datas.get(groupPosition), groupPosition));
        tv.setPadding(tv.getPaddingLeft() + 80, tv.getPaddingTop(), tv.getPaddingRight(), tv.getPaddingBottom());
        return tv;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        TextView tv = (TextView) View.inflate(parent.getContext(), R.layout.item_text, null);
        tv.setText(String.format("child %d", childPosition));
        return tv;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
