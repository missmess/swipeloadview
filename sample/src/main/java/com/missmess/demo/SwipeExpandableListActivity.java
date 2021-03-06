package com.missmess.demo;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.widget.ExpandableListView;

import com.missmess.demo.adapter.SExpandableAdapter;
import com.missmess.demo.utils.HttpUtils;
import com.missmess.swipeloadview.LoadMoreHelper;

import java.util.ArrayList;

public class SwipeExpandableListActivity extends AppCompatActivity {
    private ArrayList<String> datas;
    private LoadMoreHelper loadViewHelper;
    private SExpandableAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ExpandableListView expandableListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe_expandable_list);
        getSupportActionBar().setTitle("ExpandableListView sample");

        init();
        getDatas(true);
        loadViewHelper.setRefresh();
    }

    private void init() {
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.srl);
        expandableListView = (ExpandableListView) findViewById(R.id.elv);
        datas = new ArrayList<>();
        adapter = new SExpandableAdapter(datas);
        expandableListView.setAdapter(adapter);
        swipeRefreshLayout.setProgressViewOffset(false, 40, 140);

        loadViewHelper = new LoadMoreHelper(swipeRefreshLayout, expandableListView);
        loadViewHelper.setOnRefreshLoadListener(new LoadMoreHelper.OnRefreshLoadListener() {
            @Override
            public void onRefresh() {
                getDatas(true);
            }

            @Override
            public void onLoad() {
                getDatas(false);
            }
        });
    }

    /**
     * 请求网络
     */
    private void getDatas(final boolean refresh) {
        AsyncTask<String, Integer, String> asyncTask = new AsyncTask<String, Integer, String>() {
            @Override
            protected String doInBackground(String... params) {
                try {
                    SystemClock.sleep(800);
                    return HttpUtils.executeGet(params[0]);
                } catch (Exception e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            loadViewHelper.setLoadMoreError("连接出错，点击重试");
                            onFinish();
                        }
                    });
                }
                return null;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                if(s == null)
                    return;
                if(refresh) {
                    refreshData();
                } else {
                    if (!loadMoreData()) { //没有更多数据了
                        loadViewHelper.setHasMoreData(false);
                    }
                }
                onFinish();
            }

            private void onFinish() {
                loadViewHelper.completeRefresh();
                loadViewHelper.completeLoadMore();
            }
        };
        asyncTask.execute("https://www.baidu.com");
    }

    private void refreshData() {
        //刷新数据，每次请求15个
        datas.clear();
        for(int i = 0; i < 15; i++) {
            datas.add("refresh data");
        }
        adapter.notifyDataSetChanged();
    }

    private boolean loadMoreData() {
        //加载更多，每次请求13个，最多不超过60个
        boolean hasMore = true;
        if(datas.size() + 13 > 60) {
            hasMore = false;
        }
        for(int i = 0; i < 13 && datas.size() < 60; i++) {
            datas.add("load more data");
        }
        adapter.notifyDataSetChanged();
        return hasMore;
    }
}
