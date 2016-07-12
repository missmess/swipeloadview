package com.missmess.demo;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.Toast;

import com.missmess.demo.adapter.SListAdapter;
import com.missmess.demo.utils.HttpUtils;
import com.missmess.swipeloadview.SwipeLoadViewHelper;

import java.util.ArrayList;

public class SwipeListViewActivity extends AppCompatActivity {
    private ArrayList<String> datas;
    private SwipeLoadViewHelper<ListView> loadViewHelper;
    private SListAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe_list_view);
        getSupportActionBar().setTitle("ListView sample");

        init();
        getDatas(true);
        loadViewHelper.animRefresh();
    }

    private void init() {
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.srl);
        listView = (ListView) findViewById(R.id.lv);

        datas = new ArrayList<>();
        adapter = new SListAdapter(datas);
        swipeRefreshLayout.setProgressViewOffset(false, 40, 140);

        loadViewHelper = new SwipeLoadViewHelper<>(swipeRefreshLayout, listView);
        loadViewHelper.setAdapter(adapter);
        loadViewHelper.setOnRefreshLoadListener(new SwipeLoadViewHelper.OnRefreshLoadListener() {
            @Override
            public void onRefresh() {
                getDatas(true);
            }

            @Override
            public void onLoad() {
                getDatas(false);
            }
        });
        loadViewHelper.setOnListScrollListener(new SwipeLoadViewHelper.OnListScrollListener<ListView>() {
            boolean showed = false;
            @Override
            public void onScroll(ListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if(firstVisibleItem > 10 && !showed) {
                    Toast.makeText(SwipeListViewActivity.this, "scroll past 10th item", Toast.LENGTH_SHORT).show();
                    showed = true;
                }
                if(firstVisibleItem < 10 && showed) {
                    showed = false;
                }
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
                loadViewHelper.completeLoadmore();
            }
        };
        asyncTask.execute("http://www.baidu.com");
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
