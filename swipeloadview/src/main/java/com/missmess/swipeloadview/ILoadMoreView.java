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

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;

public interface ILoadMoreView {
    /**
     * 创建view
     *
     * @param onClickLoadMoreListener 加载更多的点击事件，需要点击调用加载更多的按钮都可以设置这个监听
     */
    View create(LayoutInflater inflater, OnClickListener onClickLoadMoreListener);

    /**
     * 显示普通保布局
     */
    void showNormal();

    /**
     * 显示已经加载完成，没有更多数据的布局
     */
    void showNomore();

    /**
     * 显示正在加载中的布局
     */
    void showLoading();

    /**
     * 显示加载失败的布局
     *
     * @param e 错误信息
     */
    void showFail(CharSequence e);
}
