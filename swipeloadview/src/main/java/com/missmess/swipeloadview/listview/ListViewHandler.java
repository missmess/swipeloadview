package com.missmess.swipeloadview.listview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.missmess.swipeloadview.SwipeLoadViewHelper;
import com.missmess.swipeloadview.SwipeLoadViewHelper.OnScrollBottomListener;
import com.missmess.swipeloadview.ILoadViewFactory.*;

/**
 * ListView处理类。给ListView增加加载更多的footer
 *
 * @author wl
 * @since 2015/12/2 16:14
 */
public class ListViewHandler {
    private SwipeLoadViewHelper.OnListScrollListener scrollListener;

    public void setOnScrollListener(SwipeLoadViewHelper.OnListScrollListener scrollListener) {
        this.scrollListener = scrollListener;
    }

    public boolean handleSetAdapter(View contentView, ListAdapter adapter, ILoadMoreView loadMoreView, OnClickListener onClickLoadMoreListener) {
		ListView listView = (ListView) contentView;
        boolean hasInit = initFooter(listView, loadMoreView, onClickLoadMoreListener);
        listView.setAdapter(adapter);
		return hasInit;
	}

    public boolean handleSetAdapter(View contentView, ExpandableListAdapter adapter, ILoadMoreView loadMoreView, OnClickListener onClickLoadMoreListener) {
		final ExpandableListView listView = (ExpandableListView) contentView;
        boolean hasInit = initFooter(listView, loadMoreView, onClickLoadMoreListener);
        listView.setAdapter(adapter);
		return hasInit;
	}

    private boolean initFooter(final ListView listView, ILoadMoreView loadMoreView, OnClickListener onClickLoadMoreListener) {
        boolean hasInit = false;
        if (loadMoreView != null) {
            final Context context = listView.getContext().getApplicationContext();
            loadMoreView.init(new FootViewAdder() {

                @Override
                public View addFootView(int layoutId) {
                    View view = LayoutInflater.from(context).inflate(layoutId, listView, false);
                    return addFootView(view);
                }

                @Override
                public View addFootView(View view) {
                    listView.addFooterView(view);
                    return view;
                }
            }, onClickLoadMoreListener);
            hasInit = true;
        }
        return hasInit;
    }

	public void setOnScrollBottomListener(View contentView, OnScrollBottomListener onScrollBottomListener) {
		ListView listView = (ListView) contentView;
		listView.setOnScrollListener(new ListViewOnScrollListener(onScrollBottomListener));
		listView.setOnItemSelectedListener(new ListViewOnItemSelectedListener(onScrollBottomListener));
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
					onScrollBottomListener.onScorllBootom();
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
					onScrollBottomListener.onScorllBootom();
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
