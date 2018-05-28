/*
Copyright 2015 shizhefei（LuckyJayce）

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */
package com.missmess.swipeloadview;

import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.missmess.swipeloadview.internal.ProgressDrawable;

/**
 * default load more view
 *
 * @author wl
 * @since 2015/12/2 16:13
 */
public class DefaultLoadMoreView implements ILoadMoreView {

    protected TextView footTextView;
    protected View.OnClickListener onClickRefreshListener;
    protected ImageView progress_wheel;

    @Override
    public View create(LayoutInflater inflater, View.OnClickListener onClickRefreshListener) {
        View footView = inflater.inflate(R.layout.view_loadmore_footer, null);
        progress_wheel = (ImageView) footView.findViewById(R.id.progress_wheel);
        footTextView = (TextView) footView.findViewById(R.id.pull_to_load_footer_hint_textview);
        this.onClickRefreshListener = onClickRefreshListener;

        ProgressDrawable progressDrawable = new ProgressDrawable();
        progressDrawable.setColor(0xFF888888);
        progress_wheel.setImageDrawable(progressDrawable);
        progress_wheel.setVisibility(View.GONE);

        return footView;
    }

    @Override
    public void showNormal() {
        progress_wheel.setVisibility(View.GONE);
        footTextView.setText("点击加载更多");
        Drawable drawable = progress_wheel.getDrawable();
        ((Animatable) drawable).stop();
        footTextView.setOnClickListener(onClickRefreshListener);
    }

    @Override
    public void showLoading() {
        progress_wheel.setVisibility(View.VISIBLE);
        footTextView.setText("正在加载中...");
        Drawable drawable = progress_wheel.getDrawable();
        ((Animatable) drawable).start();
        footTextView.setOnClickListener(null);
    }

    @Override
    public void showFail(CharSequence charSequence) {
        progress_wheel.setVisibility(View.GONE);
        footTextView.setText(charSequence);
        Drawable drawable = progress_wheel.getDrawable();
        ((Animatable) drawable).stop();
        footTextView.setOnClickListener(onClickRefreshListener);
    }

    @Override
    public void showNomore() {
        progress_wheel.setVisibility(View.GONE);
        footTextView.setText("已经到底了");
        Drawable drawable = progress_wheel.getDrawable();
        ((Animatable) drawable).stop();
        footTextView.setOnClickListener(null);
    }
}
