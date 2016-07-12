package com.missmess.demo.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.missmess.demo.R;

import java.util.List;

/**
 * @author wl
 * @since 2016/07/08 13:58
 */
public class SListAdapter extends BaseAdapter {
    private List<String> datas;

    public SListAdapter(List<String> list) {
        this.datas = list;
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView tv = (TextView) View.inflate(parent.getContext(), R.layout.item_text, null);
        tv.setText(String.format("%s %d", datas.get(position), position));
        return tv;
    }
}
