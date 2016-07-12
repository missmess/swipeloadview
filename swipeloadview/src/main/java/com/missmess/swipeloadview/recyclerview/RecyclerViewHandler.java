package com.missmess.swipeloadview.recyclerview;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;

import com.missmess.swipeloadview.ILoadViewFactory;
import com.missmess.swipeloadview.ILoadViewHandler;
import com.missmess.swipeloadview.SwipeLoadViewHelper;
import com.missmess.swipeloadview.SwipeLoadViewHelper.OnScrollBottomListener;

/**
 * RecyclerView处理类。给RecyclerView增加加载更多的footer
 *
 * @author wl
 * @since 2016/4/6 19:13
 */
public class RecyclerViewHandler implements ILoadViewHandler<RecyclerView, Adapter> {
    @Override
    public boolean handleSetAdapter(RecyclerView refreshView, RecyclerView.Adapter adapter, ILoadViewFactory.ILoadMoreView loadMoreView, OnClickListener onClickLoadMoreListener) {
        boolean hasInit = false;
		Adapter<?> adapter2 = (Adapter<?>) adapter;
		if (loadMoreView != null) {
			HFAdapter hfAdapter;
			if (adapter instanceof HFAdapter) {
				hfAdapter = (HFAdapter) adapter;
			} else {
				hfAdapter = new HFRecyclerAdapter(adapter2);
			}
			adapter2 = hfAdapter;
			Context context = refreshView.getContext().getApplicationContext();
            View footView = loadMoreView.create(LayoutInflater.from(context), onClickLoadMoreListener);
            hfAdapter.addFooter(footView);

			hasInit = true;
		}
		refreshView.setAdapter(adapter2);
		return hasInit;
	}

    @Override
    public void setUpListener(RecyclerView refreshView, OnScrollBottomListener onScrollBottomListener) {
        refreshView.addOnScrollListener(new RecyclerViewOnScrollListener(onScrollBottomListener));
	}

    @Override
    public void setOnScrollListener(SwipeLoadViewHelper.OnListScrollListener<RecyclerView> scrollListener) {

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

	}
}
