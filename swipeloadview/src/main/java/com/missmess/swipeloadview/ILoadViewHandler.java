package com.missmess.swipeloadview;

import android.support.annotation.NonNull;
import android.view.View;

import com.missmess.swipeloadview.LoadMoreHelper.OnScrollBottomListener;

/**
 * 定义支持LoadMore的view 如何添加支持。
 *
 * @author wl
 * @since 2016/07/11 16:09
 * @param <V> refreshView的类型，如ListView
 */
public interface ILoadViewHandler<V extends View> {
    /**
     * 定义如何把加载更多的foot view，添加到refreshView上。
     * @param refreshView 如ListView
     * @param loadMoreFooter 加载更多的footer view
     */
    void handleAddFooter(V refreshView, @NonNull View loadMoreFooter);

    /**
     * 该方法实现为refreshView添加监听，必须要有OnScrollBottomListener
     * @param refreshView 如ListView
     * @param onScrollBottomListener 滑到底部时的监听。
     */
    void handleSetListener(V refreshView, OnScrollBottomListener onScrollBottomListener);

    /**
     * 加载更多的footer view是否已经成功设置完成。
     * @return true - 指示已经成功设置了。
     */
    boolean isLoadViewPrepared();
}
