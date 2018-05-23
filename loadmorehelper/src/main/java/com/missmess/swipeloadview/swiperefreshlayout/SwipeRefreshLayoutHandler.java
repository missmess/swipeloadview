package com.missmess.swipeloadview.swiperefreshlayout;

import android.support.v4.widget.SwipeRefreshLayout;

import com.missmess.swipeloadview.IRefreshLayoutHandler;

/**
 * @author wl
 * @since 2018/05/23 10:25
 */
public class SwipeRefreshLayoutHandler implements IRefreshLayoutHandler<SwipeRefreshLayout> {
    @Override
    public void handleSetRefreshListener(SwipeRefreshLayout view, final Runnable runnable) {
        view.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                runnable.run();
            }
        });
    }

    @Override
    public void refresh(SwipeRefreshLayout view) {
        view.setRefreshing(true);
    }

    @Override
    public void finishRefresh(SwipeRefreshLayout view) {
        view.setRefreshing(false);
    }
}
