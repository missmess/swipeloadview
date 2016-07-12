package com.missmess.swipeloadview.listview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.missmess.swipeloadview.ILoadViewFactory.ILoadMoreView;
import com.missmess.swipeloadview.ILoadViewHandler;
import com.missmess.swipeloadview.SwipeLoadViewHelper;
import com.missmess.swipeloadview.SwipeLoadViewHelper.OnScrollBottomListener;

/**
 * ListView处理类。给ListView增加加载更多的footer
 *
 * @author wl
 * @since 2015/12/2 16:14
 */
public class ListViewHandler implements ILoadViewHandler<ListView, ListAdapter> {
    private SwipeLoadViewHelper.OnListScrollListener<ListView> scrollListener;

    @Override
    public boolean handleSetAdapter(ListView refreshView, ListAdapter adapter, ILoadMoreView loadMoreView, OnClickListener onClickLoadMoreListener) {
        boolean hasInit = initFooter(refreshView, loadMoreView, onClickLoadMoreListener);
        refreshView.setAdapter(adapter);
		return hasInit;
	}

    private boolean initFooter(ListView listView, ILoadMoreView loadMoreView, OnClickListener onClickLoadMoreListener) {
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
    public void setUpListener(ListView refreshView, OnScrollBottomListener onScrollBottomListener) {
        refreshView.setOnScrollListener(new ListViewOnScrollListener(onScrollBottomListener));
		refreshView.setOnItemSelectedListener(new ListViewOnItemSelectedListener(onScrollBottomListener));
	}

    @Override
    public void setOnScrollListener(SwipeLoadViewHelper.OnListScrollListener<ListView> scrollListener) {
        this.scrollListener = scrollListener;
    }

	/**
	 * 针对于电视 选择到了底部项的时候自动加载更多数据
	 */
	private class ListViewOnItemSelectedListener implements OnItemSelectedListener {
		private OnScrollBottomListener onScrollBottomListener;

		public ListViewOnItemSelectedListener(OnScrollBottomListener onScrollBottomListener) {
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
	private class ListViewOnScrollListener implements OnScrollListener {
		private OnScrollBottomListener onScrollBottomListener;

		public ListViewOnScrollListener(OnScrollBottomListener onScrollBottomListener) {
			super();
			this.onScrollBottomListener = onScrollBottomListener;
		}

		@Override
		public void onScrollStateChanged(AbsListView listView, int scrollState) {
			if (scrollState == OnScrollListener.SCROLL_STATE_IDLE && listView.getLastVisiblePosition() + 1 == listView.getCount()) {// 如果滚动到最后一行
				if (onScrollBottomListener != null) {
					onScrollBottomListener.onScrollBottom();
				}
			}
            if(scrollListener != null) {
                scrollListener.onScrollStateChanged((ListView) listView, scrollState);
            }
		}

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            if(scrollListener != null) {
                scrollListener.onScroll((ListView) view, firstVisibleItem, visibleItemCount, totalItemCount);
            }
		}
	}
}
