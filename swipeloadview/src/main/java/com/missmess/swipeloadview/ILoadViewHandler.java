package com.missmess.swipeloadview;

import android.view.View;

import com.missmess.swipeloadview.LoadMoreHelper.OnScrollBottomListener;

/**
 * 定义支持LoadMore的view 如何添加支持。
 *
 * @author wl
 * @since 2016/07/11 16:09
 * @param <V> refreshView的类型，如ListView
 * @param <A> refreshView的adapter，如ListAdapter
 */
public interface ILoadViewHandler<V extends View, A> {
    /**
     * 定义如何去创建foot，并添加到refreshView上。并设置adapter
     * @param refreshView 如ListView
     * @param adapter 如ListAdapter
     * @param loadMoreView 加载更多的view
     * @return 是否处理成功
     */
    boolean handleSetAdapter(V refreshView, A adapter, View loadMoreView);

    /**
     * 该方法实现为refreshView添加监听，必须要有OnScrollBottomListener
     * @param refreshView 如ListView
     * @param onScrollBottomListener 滑到底部时的监听。
     */
    void handleSetListener(V refreshView, OnScrollBottomListener onScrollBottomListener);
}
