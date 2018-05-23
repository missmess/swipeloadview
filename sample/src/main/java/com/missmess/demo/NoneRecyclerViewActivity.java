package com.missmess.demo;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.missmess.demo.adapter.SRecyclerAdapter;
import com.missmess.demo.utils.HttpUtils;
import com.missmess.swipeloadview.LoadMoreHelper;

import java.util.ArrayList;

/**
 * @author wl
 * @since 2018/05/23 15:08
 */
public class NoneRecyclerViewActivity extends AppCompatActivity {
    private ArrayList<String> datas;
    private LoadMoreHelper loadViewHelper;
    private SRecyclerAdapter adapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_none_recycler_view);
        getSupportActionBar().setTitle("None Refresh RecyclerView sample");

        init();
    }

    private void init() {
        recyclerView = (RecyclerView) findViewById(R.id.rv);
        datas = new ArrayList<>();
        adapter = new SRecyclerAdapter(datas);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadViewHelper = new LoadMoreHelper(recyclerView);
        loadViewHelper.setAdapter(adapter);
        loadViewHelper.setOnRefreshLoadListener(new LoadMoreHelper.SimpleOnLoadListener() {
            @Override
            public void onLoad() {
                getDatas();
            }
        });
    }

    /**
     * 请求网络
     */
    private void getDatas() {
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
                if (!loadMoreData()) { //没有更多数据了
                    loadViewHelper.setHasMoreData(false);
                }
                onFinish();
            }

            private void onFinish() {
                loadViewHelper.completeLoadMore();
            }
        };
        asyncTask.execute("https://www.baidu.com");
    }

    private boolean loadMoreData() {
        //加载更多，每次请求13个，最多不超过60个
        boolean hasMore = true;
        if(datas.size() + 13 > 60) {
            hasMore = false;
        }
        for(int i = 0; i < 13 && datas.size() < 60; i++) {
            datas.add("load data");
        }
        adapter.notifyDataSetChanged();
        return hasMore;
    }
}
