package com.missmess.swipeloadview.listview;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ExpandableListView;

import com.missmess.swipeloadview.ILoadViewHandler;
import com.missmess.swipeloadview.LoadMoreHelper;

/**
 * @author wl
 * @since 2016/07/11 17:51
 */
public class ExpandableListViewHandler implements ILoadViewHandler<ExpandableListView> {
    private AbsListView.OnScrollListener scrollListener;
    private AdapterView.OnItemSelectedListener itemSelectedListener;
    private boolean isPrepared;

    @Override
    public void handleAddFooter(ExpandableListView refreshView, @NonNull View loadMoreFooter) {
        refreshView.addFooterView(loadMoreFooter);
        isPrepared = true;
    }

    @Override
    public void handleSetListener(ExpandableListView refreshView, LoadMoreHelper.OnScrollBottomListener onScrollBottomListener) {
        refreshView.setOnScrollListener(new ListViewOnScrollListener(onScrollBottomListener));
        refreshView.setOnItemSelectedListener(new ListViewOnItemSelectedListener(onScrollBottomListener));
    }

    @Override
    public boolean isLoadViewPrepared() {
        return isPrepared;
    }

    public void setOnScrollListener(AbsListView.OnScrollListener scrollListener) {
        this.scrollListener = scrollListener;
    }

    public void setOnItemSelectedListener(AdapterView.OnItemSelectedListener listener) {
        this.itemSelectedListener = listener;
    }

    /**
     * 针对于电视 选择到了底部项的时候自动加载更多数据
     */
    private class ListViewOnItemSelectedListener implements AdapterView.OnItemSelectedListener {
        private LoadMoreHelper.OnScrollBottomListener onScrollBottomListener;

        public ListViewOnItemSelectedListener(LoadMoreHelper.OnScrollBottomListener onScrollBottomListener) {
            super();
            this.onScrollBottomListener = onScrollBottomListener;
        }

        @Override
        public void onItemSelected(AdapterView<?> listView, View view, int position, long id) {
            if (listView.getLastVisiblePosition() + 1 == listView.getCount()) {// 如果滚动到最后一行
                if (onScrollBottomListener != null) {
                    onScrollBottomListener.onScrollBottom();
                }
            }
            if (itemSelectedListener != null) {
                itemSelectedListener.onItemSelected(listView, view, position, id);
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            if (itemSelectedListener != null) {
                itemSelectedListener.onNothingSelected(parent);
            }
        }
    }

    /**
     * 滚动到底部自动加载更多数据
     */
    private class ListViewOnScrollListener implements AbsListView.OnScrollListener {
        private LoadMoreHelper.OnScrollBottomListener onScrollBottomListener;

        public ListViewOnScrollListener(LoadMoreHelper.OnScrollBottomListener onScrollBottomListener) {
            super();
            this.onScrollBottomListener = onScrollBottomListener;
        }

        @Override
        public void onScrollStateChanged(AbsListView listView, int scrollState) {
            if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && listView.getLastVisiblePosition() + 1 == listView.getCount()) {// 如果滚动到最后一行
                if (onScrollBottomListener != null) {
                    onScrollBottomListener.onScrollBottom();
                }
            }
            if(scrollListener != null) {
                scrollListener.onScrollStateChanged(listView, scrollState);
            }
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            if(scrollListener != null) {
                scrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
            }
        }
    }
}
