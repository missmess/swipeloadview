package com.missmess.swipeloadview;

import android.view.View;

/**
 * @author wl
 * @since 2018/05/23 10:24
 */
public interface IRefreshLayoutHandler<R extends View> {
    void handleSetRefreshListener(R view, Runnable runnable);

    void refresh(R view);

    void finishRefresh(R view);
}
