package com.missmess.demo.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.missmess.demo.R;

import java.util.List;

/**
 * @author wl
 * @since 2016/07/08 15:48
 */
public class SRecyclerAdapter extends RecyclerView.Adapter {
    private List<String> datas;

    public SRecyclerAdapter(List<String> datas) {
        this.datas = datas;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int type) {
        View view = View.inflate(viewGroup.getContext(), R.layout.item_text, null);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder vh, int position) {
        ViewHolder holder = (ViewHolder) vh;
        holder.tv.setText(String.format("%s %d", datas.get(position), position));
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv;
        public ViewHolder(View itemView) {
            super(itemView);
            tv = (TextView) itemView;
        }
    }
}
