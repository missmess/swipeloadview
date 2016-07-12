package com.missmess.swipeloadview.recyclerview;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;

import com.missmess.swipeloadview.ILoadViewFactory;
import com.missmess.swipeloadview.SwipeLoadViewHelper.OnScrollBottomListener;

/**
 * RecyclerView处理类。给RecyclerView增加加载更多的footer
 *
 * @author wl
 * @since 2016/4/6 19:13
 */
public class RecyclerViewHandler {
	public boolean handleSetAdapter(View contentView, RecyclerView.Adapter adapter, ILoadViewFactory.ILoadMoreView loadMoreView, OnClickListener onClickLoadMoreListener) {
		final RecyclerView recyclerView = (RecyclerView) contentView;
		boolean hasInit = false;
		Adapter<?> adapter2 = (Adapter<?>) adapter;
		if (loadMoreView != null) {
			final HFAdapter hfAdapter;
			if (adapter instanceof HFAdapter) {
				hfAdapter = (HFAdapter) adapter;
			} else {
				hfAdapter = new HFRecyclerAdapter(adapter2);
			}
			adapter2 = hfAdapter;
			final Context context = recyclerView.getContext().getApplicationContext();
			loadMoreView.init(new ILoadViewFactory.FootViewAdder() {

				@Override
				public View addFootView(int layoutId) {
					View view = LayoutInflater.from(context).inflate(layoutId, recyclerView, false);
					return addFootView(view);
				}

				@Override
				public View addFootView(View view) {
					hfAdapter.addFooter(view);
					return view;
				}
			}, onClickLoadMoreListener);
			hasInit = true;
		}
		recyclerView.setAdapter(adapter2);
		return hasInit;
	}

	public void setOnScrollBottomListener(View contentView, OnScrollBottomListener onScrollBottomListener) {
		final RecyclerView recyclerView = (RecyclerView) contentView;
		recyclerView.addOnScrollListener(new RecyclerViewOnScrollListener(onScrollBottomListener));
	}

	/**
	 * 滑动监听
	 */
	private static class RecyclerViewOnScrollListener extends RecyclerView.OnScrollListener {
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

	};
}
