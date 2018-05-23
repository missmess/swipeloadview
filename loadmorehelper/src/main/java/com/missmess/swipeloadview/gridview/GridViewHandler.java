package com.missmess.swipeloadview.gridview;

import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ListAdapter;

import com.missmess.swipeloadview.ILoadViewHandler;
import com.missmess.swipeloadview.LoadMoreHelper;

import in.srain.cube.views.GridViewWithHeaderAndFooter;

/**
 * GridView处理类。给GridView增加加载更多的footer
 *
 * @author wl
 * @since 2016/4/6 19:13
 */
public class GridViewHandler implements ILoadViewHandler<GridViewWithHeaderAndFooter, ListAdapter> {
    private OnScrollListener scrollListener;
    private OnItemSelectedListener itemSelectedListener;

    @Override
    public boolean handleSetAdapter(GridViewWithHeaderAndFooter refreshView, ListAdapter adapter, View loadMoreView) {
        boolean hasInit = false;
		if (loadMoreView != null) {
            refreshView.addFooterView(loadMoreView);

			hasInit = true;
		}
		refreshView.setAdapter(adapter);
		return hasInit;
	}

    @Override
    public void handleSetListener(GridViewWithHeaderAndFooter refreshView, LoadMoreHelper.OnScrollBottomListener onScrollBottomListener) {
        refreshView.setOnScrollListener(new ListViewOnScrollListener(onScrollBottomListener));
		refreshView.setOnItemSelectedListener(new ListViewOnItemSelectedListener(onScrollBottomListener));
	}

    public void setOnScrollListener(OnScrollListener scrollListener) {
        this.scrollListener = scrollListener;
    }

    public void setOnItemSelectedListener(OnItemSelectedListener listener) {
        this.itemSelectedListener = listener;
    }

    /**
	 * 针对于电视 选择到了底部项的时候自动加载更多数据
	 */
	private class ListViewOnItemSelectedListener implements OnItemSelectedListener {
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
	private class ListViewOnScrollListener implements OnScrollListener {
		private LoadMoreHelper.OnScrollBottomListener onScrollBottomListener;

		public ListViewOnScrollListener(LoadMoreHelper.OnScrollBottomListener onScrollBottomListener) {
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
