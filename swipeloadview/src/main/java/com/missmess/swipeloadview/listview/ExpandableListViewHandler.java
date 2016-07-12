package com.missmess.swipeloadview.listview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;

import com.missmess.swipeloadview.ILoadViewFactory;
import com.missmess.swipeloadview.ILoadViewHandler;
import com.missmess.swipeloadview.SwipeLoadViewHelper;

/**
 * @author wl
 * @since 2016/07/11 17:51
 */
public class ExpandableListViewHandler implements ILoadViewHandler<ExpandableListView, ExpandableListAdapter> {
    @Override
    public boolean handleSetAdapter(ExpandableListView refreshView, ExpandableListAdapter adapter, ILoadViewFactory.ILoadMoreView loadMoreView, View.OnClickListener onClickLoadMoreListener) {
        boolean hasInit = initFooter(refreshView, loadMoreView, onClickLoadMoreListener);
        refreshView.setAdapter(adapter);
        return hasInit;
    }

    private boolean initFooter(ListView listView, ILoadViewFactory.ILoadMoreView loadMoreView, View.OnClickListener onClickLoadMoreListener) {
        boolean hasInit = false;
        if (loadMoreView != null) {
            Context context = listView.getContext().getApplicationContext();
            View footView = loadMoreView.create(LayoutInflater.from(context), onClickLoadMoreListener);
            listView.addFooterView(footView);

            hasInit = true;
        }
        return hasInit;
    }

    @Override
    public void setUpListener(ExpandableListView refreshView, SwipeLoadViewHelper.OnScrollBottomListener onScrollBottomListener) {
        refreshView.setOnScrollListener(new ListViewOnScrollListener(onScrollBottomListener));
        refreshView.setOnItemSelectedListener(new ListViewOnItemSelectedListener(onScrollBottomListener));
    }

    @Override
    public void setOnScrollListener(SwipeLoadViewHelper.OnListScrollListener<ExpandableListView> scrollListener) {

    }

    /**
     * 针对于电视 选择到了底部项的时候自动加载更多数据
     */
    private class ListViewOnItemSelectedListener implements AdapterView.OnItemSelectedListener {
        private SwipeLoadViewHelper.OnScrollBottomListener onScrollBottomListener;

        public ListViewOnItemSelectedListener(SwipeLoadViewHelper.OnScrollBottomListener onScrollBottomListener) {
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
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

    /**
     * 滚动到底部自动加载更多数据
     */
    private class ListViewOnScrollListener implements AbsListView.OnScrollListener {
        private SwipeLoadViewHelper.OnScrollBottomListener onScrollBottomListener;

        public ListViewOnScrollListener(SwipeLoadViewHelper.OnScrollBottomListener onScrollBottomListener) {
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
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        }
    }
}
