package com.missmess.swipeloadview.recyclerview;

import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.View;

import com.missmess.swipeloadview.ILoadViewHandler;
import com.missmess.swipeloadview.LoadMoreHelper.OnScrollBottomListener;

/**
 * RecyclerView处理类。给RecyclerView增加加载更多的footer
 *
 * @author wl
 * @since 2016/4/6 19:13
 */
public class RecyclerViewHandler implements ILoadViewHandler<RecyclerView> {
    private boolean isPrepared = false;

    @Override
    public void handleAddFooter(RecyclerView refreshView, @NonNull View loadMoreFooter) {
        checkAndAddFooter(refreshView, loadMoreFooter);
    }

    private void checkAndAddFooter(RecyclerView refreshView, @NonNull View loadMoreFooter) {
        Adapter adapter = refreshView.getAdapter();
        if (adapter == null) {
            if (!ViewCompat.isAttachedToWindow(refreshView)) {
                tryAddFooterOnAttached(refreshView, loadMoreFooter);
            } else {
                tryAddFooterOnChildAttached(refreshView, loadMoreFooter);
            }
        } else {
            addFooterView(refreshView, adapter, loadMoreFooter);
            isPrepared = true;
        }
    }

    private void tryAddFooterOnAttached(final RecyclerView refreshView, final View loadMoreFooter) {
        refreshView.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
            @Override
            public void onViewAttachedToWindow(View v) {
                refreshView.post(new Runnable() {
                    @Override
                    public void run() {
                        checkAndAddFooter(refreshView, loadMoreFooter);
                    }
                });
                refreshView.removeOnAttachStateChangeListener(this);
            }

            @Override
            public void onViewDetachedFromWindow(View v) {

            }
        });
    }

    private void tryAddFooterOnChildAttached(final RecyclerView refreshView, final View loadMoreFooter) {
        refreshView.addOnChildAttachStateChangeListener(new RecyclerView.OnChildAttachStateChangeListener() {
            @Override
            public void onChildViewAttachedToWindow(View view) {
                refreshView.post(new Runnable() {
                    @Override
                    public void run() {
                        checkAndAddFooter(refreshView, loadMoreFooter);
                    }
                });
                refreshView.removeOnChildAttachStateChangeListener(this);
            }

            @Override
            public void onChildViewDetachedFromWindow(View view) {

            }
        });
    }

    protected void addFooterView(RecyclerView refreshView, Adapter adapter, View loadMoreFooter) {
        if (adapter instanceof HFAdapter) {
            HFAdapter hfAdapter = (HFAdapter) adapter;
            hfAdapter.addFooter(loadMoreFooter);
        } else {
            HFRecyclerAdapter hfRecyclerAdapter = new HFRecyclerAdapter(adapter);
            hfRecyclerAdapter.addFooter(loadMoreFooter);
            refreshView.swapAdapter(hfRecyclerAdapter, false);
        }
    }

    @Override
    public void handleSetListener(RecyclerView refreshView, OnScrollBottomListener onScrollBottomListener) {
        refreshView.addOnScrollListener(new RecyclerViewOnScrollListener(onScrollBottomListener));
    }

    @Override
    public boolean isLoadViewPrepared() {
        return isPrepared;
    }

    /**
     * 滑动监听
     */
    private class RecyclerViewOnScrollListener extends RecyclerView.OnScrollListener {
        private OnScrollBottomListener onScrollBottomListener;

        public RecyclerViewOnScrollListener(OnScrollBottomListener onScrollBottomListener) {
            super();
            this.onScrollBottomListener = onScrollBottomListener;
        }

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            if (newState == RecyclerView.SCROLL_STATE_IDLE && isScollBottom(recyclerView)) {
                if (onScrollBottomListener != null) {
                    onScrollBottomListener.onScrollBottom();
                }
            }
        }

        private boolean isScollBottom(RecyclerView recyclerView) {
            return !isCanScollVertically(recyclerView);
        }

        private boolean isCanScollVertically(RecyclerView recyclerView) {
            if (android.os.Build.VERSION.SDK_INT < 14) {
                return ViewCompat.canScrollVertically(recyclerView, 1) || recyclerView.getScrollY() < recyclerView.getHeight();
            } else {
                return ViewCompat.canScrollVertically(recyclerView, 1);
            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        }

    }
}
