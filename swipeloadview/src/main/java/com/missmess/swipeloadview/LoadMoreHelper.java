package com.missmess.swipeloadview;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.GridView;
import android.widget.ListView;

import com.missmess.swipeloadview.gridview.GridViewHandler;
import com.missmess.swipeloadview.listview.ExpandableListViewHandler;
import com.missmess.swipeloadview.listview.ListViewHandler;
import com.missmess.swipeloadview.recyclerview.RecyclerViewHandler;
import com.missmess.swipeloadview.swiperefreshlayout.SwipeRefreshLayoutHandler;

import in.srain.cube.views.GridViewWithHeaderAndFooter;

/**
 * 在SwipeRefreshLayout中使用listview，RecyclerView，GridView等，并让这些view支持上拉加载更多的helper类。
 *
 * @author wl
 * @since 2015/12/02 09:57
 */
public class LoadMoreHelper {
    private View mRefreshLayout;
    private View mRefreshView;
    private IRefreshLayoutHandler refreshHandler;
    private ILoadMoreView mLoadMoreView;
    private ILoadViewHandler loadHandler;
    private OnRefreshLoadListener onRefreshLoadListener;
    private OnScrollBottomListener onScrollBottomListener;
    private View.OnClickListener onLoadMoreBtnClickListener;
    private boolean hasInitLoadMoreView = false;
    private boolean mHasMore = true;
    private boolean hasError = false;
    private boolean isLoading = false;
    private boolean isRefreshing = false;

    public LoadMoreHelper(View mRefreshView) {
        this(null, null, mRefreshView, null, new DefaultLoadMoreView());
    }

    public LoadMoreHelper(View refreshLayout, View mRefreshView) {
        this(refreshLayout, null, mRefreshView, null, new DefaultLoadMoreView());
    }

    public LoadMoreHelper(View refreshLayout, View mRefreshView, ILoadMoreView loadMoreView) {
        this(refreshLayout, null, mRefreshView, null, loadMoreView);
    }

    public <K extends View> LoadMoreHelper(K refreshLayout, IRefreshLayoutHandler<K> refreshLayoutHandler, View refreshView) {
        this(refreshLayout, refreshLayoutHandler, refreshView, null, new DefaultLoadMoreView());
    }

    /**
     * 创建一个SwipeLoadViewHelper，关联一个刷新控件group，并使用IRefreshLayoutHandler指定关联规则。并增加一个
     * 控件布局，这个控件通常是ListView, RecyclerView, GridView, ExpandableListView中的一个。并指定loadmore布局。
     *
     * @param refreshLayout 刷新控件，可以为null，只有指定了刷新控件，{@link OnRefreshLoadListener#onRefresh()}
     *                      方法才会被调用，同时刷新控件和加载更多功能才会有
     * @param refreshLayoutHandler 指定与刷新控件关联的规则。
     * @param refreshView ListView, RecyclerView, GridView, ExpandableListView中的一个
     * @param loadViewHandler 指定refreshView如何去增加添加adapter和添加更多界面到refreshView上
     * @param loadMoreView 定义加载更多的各种状态变更和显示策略
     * @param <K> 刷新控件view group
     * @param <V> 内容控件view
     * @param <A> 内容控件adapter
     */
    public <K extends View, V extends View, A> LoadMoreHelper(K refreshLayout, IRefreshLayoutHandler<K> refreshLayoutHandler, V refreshView, ILoadViewHandler<V, A> loadViewHandler, ILoadMoreView loadMoreView) {
        this.mRefreshLayout = refreshLayout;
        this.mRefreshView = refreshView;
        if (loadMoreView == null) {
            loadMoreView = new DefaultLoadMoreView();
        }
        this.mLoadMoreView = loadMoreView;

        IRefreshLayoutHandler rh = refreshLayoutHandler;
        if (rh == null) {
            if (refreshLayout instanceof SwipeRefreshLayout) {
                rh = new SwipeRefreshLayoutHandler();
            }
        }

        if (refreshView == null) {
            throw new NullPointerException("RefreshView cannot be null");
        }
        if (loadViewHandler != null) {
            loadHandler = loadViewHandler;
        } else {
            if (refreshView instanceof ExpandableListView) {
                loadHandler = new ExpandableListViewHandler();
            } else if (refreshView instanceof ListView) {
                loadHandler = new ListViewHandler();
            } else if (refreshView instanceof RecyclerView) {
                loadHandler = new RecyclerViewHandler();
            } else if (refreshView instanceof GridViewWithHeaderAndFooter) {
                loadHandler = new GridViewHandler();
            } else if (refreshView instanceof GridView) {
                throw new IllegalArgumentException("GridView is not supported, use GridViewWithHeaderAndFooter instead.");
            } else {
                throw new IllegalArgumentException("this view do not support Load-More function");
            }
        }

        onScrollBottomListener = new OnScrollBottomListener() {
            @Override
            public void onScrollBottom() {
                if (!hasError) {
                    judgeToLoadMore();
                }
            }
        };
        onLoadMoreBtnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                judgeToLoadMore();
            }
        };
        if (rh != null && mRefreshLayout != null) {
            refreshHandler = rh;
            rh.handleSetRefreshListener(refreshLayout, new Runnable() {
                @Override
                public void run() {
                    if (!isRefreshing() && !isLoading()) { //不在刷新,加载中
                        mHasMore = true;
                        hasError = false;
                        isRefreshing = true;
                        if (onRefreshLoadListener != null) {
                            onRefreshLoadListener.onRefresh();
                        }
                    }
                }
            });
        }
    }

    public ILoadViewHandler getLoadHandler() {
        return loadHandler;
    }

    private void judgeToLoadMore() {
        if (mHasMore && !isRefreshing() && !isLoading()) { //有数据，并且不在刷新,加载中
            hasError = false;
            isLoading = true;
            mLoadMoreView.showLoading();
            if (onRefreshLoadListener != null) {
                onRefreshLoadListener.onLoad();
            }
        }
    }

    /**
     * 设置刷新加载更多监听
     * @param onRefreshLoadListener
     */
    public void setOnRefreshLoadListener(OnRefreshLoadListener onRefreshLoadListener) {
        this.onRefreshLoadListener = onRefreshLoadListener;
    }

    /**
     * 设置适配器。需要与设置的refreshView匹配。
     *
     * @param adapter 适配器
     */
    public void setAdapter(Object adapter) {
        loadHandler.handleSetListener(mRefreshView, onScrollBottomListener);
        View loadmoreView = mLoadMoreView.create(LayoutInflater.from(mRefreshView.getContext()), onLoadMoreBtnClickListener);
        hasInitLoadMoreView = loadHandler.handleSetAdapter(mRefreshView, adapter, loadmoreView);
    }

    /**
     * 显示刷新动画。（仅显示动画）
     */
    public void setRefresh() {
        if (refreshHandler != null)
            refreshHandler.refresh(mRefreshLayout);
    }

    /**
     * 是否有更多数据
     *
     * @param hasMoreData true-还有数据；false-没有更多数据了
     */
    public void setHasMoreData(boolean hasMoreData) {
        this.mHasMore = hasMoreData;
        if (hasInitLoadMoreView && mLoadMoreView != null) {
            if (hasMoreData) {
                mLoadMoreView.showNormal();
            } else {
                mLoadMoreView.showNomore();
            }
        }
    }

    /**
     * 下拉刷新结束
     */
    public void completeRefresh() {
        isRefreshing = false;
        if (refreshHandler != null)
            refreshHandler.finishRefresh(mRefreshLayout);
    }

    /**
     * 加载更多结束
     */
    public void completeLoadMore() {
        isLoading = false;
        if (mHasMore && !hasError) //没错误，并且有更多数据
            mLoadMoreView.showNormal();
    }

    /**
     * 是否正在刷新
     *
     * @return true-是
     */
    public boolean isRefreshing() {
        return isRefreshing;
    }

    /**
     * 是否正在加载
     *
     * @return true-是
     */
    public boolean isLoading() {
        return isLoading;
    }

    /**
     * 加载更多出错了，显示错误信息
     *
     * @param msg 错误信息
     */
    public void setLoadMoreError(CharSequence msg) {
        hasError = true;
        mLoadMoreView.showFail(msg);
    }

    public static abstract class SimpleOnLoadListener implements OnRefreshLoadListener {
        @Override
        public void onRefresh() {}
    }

    public interface OnRefreshLoadListener {
        void onRefresh();
        void onLoad();
    }

    public interface OnScrollBottomListener {
        void onScrollBottom();
    }
}
