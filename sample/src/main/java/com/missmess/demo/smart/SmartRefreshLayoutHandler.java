package com.missmess.demo.smart;

import com.missmess.swipeloadview.IRefreshLayoutHandler;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

/**
 * @author wl
 * @since 2018/05/23 15:37
 */
public class SmartRefreshLayoutHandler implements IRefreshLayoutHandler<SmartRefreshLayout> {

    @Override
    public void handleSetRefreshListener(SmartRefreshLayout view, final Runnable runnable) {
        view.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                runnable.run();
            }
        });
    }

    @Override
    public void refresh(SmartRefreshLayout view) {
        view.autoRefresh();
    }

    @Override
    public void finishRefresh(SmartRefreshLayout view) {
        view.finishRefresh();
    }
}
