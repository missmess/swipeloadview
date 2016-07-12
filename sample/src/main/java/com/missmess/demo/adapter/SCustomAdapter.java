package com.missmess.demo.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.missmess.demo.R;

/**
 * @author wl
 * @since 2016/07/08 18:17
 */
public class SCustomAdapter extends BaseAdapter {
    @Override
    public int getCount() {
        return 10;
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
        tv.setText(String.format("Custom load view %d", position));
        return tv;
    }
}
