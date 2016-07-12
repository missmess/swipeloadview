package com.missmess.swipeloadview.gridview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ListAdapter;

import com.missmess.swipeloadview.ILoadViewFactory;
import com.missmess.swipeloadview.ILoadViewHandler;
import com.missmess.swipeloadview.SwipeLoadViewHelper;

import in.srain.cube.views.GridViewWithHeaderAndFooter;

/**
 * GridView处理类。给GridView增加加载更多的footer
 *
 * @author wl
 * @since 2016/4/6 19:13
 */
public class GridViewHandler implements ILoadViewHandler<GridViewWithHeaderAndFooter, ListAdapter> {

    @Override
    public boolean handleSetAdapter(GridViewWithHeaderAndFooter refreshView, ListAdapter adapter, ILoadViewFactory.ILoadMoreView loadMoreView, OnClickListener onClickLoadMoreListener) {
        boolean hasInit = false;
		if (loadMoreView != null) {
			Context context = refreshView.getContext().getApplicationContext();
            View footView = loadMoreView.create(LayoutInflater.from(context), onClickLoadMoreListener);
            refreshView.addFooterView(footView);

			hasInit = true;
		}
		refreshView.setAdapter(adapter);
		return hasInit;
	}

    @Override
    public void setUpListener(GridViewWithHeaderAndFooter refreshView, SwipeLoadViewHelper.OnScrollBottomListener onScrollBottomListener) {
        refreshView.setOnScrollListener(new ListViewOnScrollListener(onScrollBottomListener));
		refreshView.setOnItemSelectedListener(new ListViewOnItemSelectedListener(onScrollBottomListener));
	}

    @Override
    public void setOnScrollListener(SwipeLoadViewHelper.OnListScrollListener<GridViewWithHeaderAndFooter> scrollListener) {

    }

    /**
	 * 针对于电视 选择到了底部项的时候自动加载更多数据
	 */
	private class ListViewOnItemSelectedListener implements OnItemSelectedListener {
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
	private static class ListViewOnScrollListener implements OnScrollListener {
		private SwipeLoadViewHelper.OnScrollBottomListener onScrollBottomListener;

		public ListViewOnScrollListener(SwipeLoadViewHelper.OnScrollBottomListener onScrollBottomListener) {
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
		}

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

		}
	}
}
