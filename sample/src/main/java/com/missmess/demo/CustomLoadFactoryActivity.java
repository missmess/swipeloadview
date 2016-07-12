package com.missmess.demo;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.missmess.demo.adapter.SCustomAdapter;
import com.missmess.demo.custom.MyLoadFactory;
import com.missmess.swipeloadview.SwipeLoadViewHelper;

public class CustomLoadFactoryActivity extends AppCompatActivity {

    private SwipeRefreshLayout swipeRefreshLayout;
    private ListView listView;
    private SwipeLoadViewHelper<ListView> loadViewHelper;
    private TextView tv_refresh;
    private TextView tv_loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_load_factory);
        getSupportActionBar().setTitle("Custom LoadView");

        init();
    }

    private void init() {
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.srl);
        listView = (ListView) findViewById(R.id.lv);

        View headView = View.inflate(this, R.layout.header_view, null);
        tv_refresh = (TextView) headView.findViewById(R.id.textView2);
        tv_loading = (TextView) headView.findViewById(R.id.textView4);

        listView.addHeaderView(headView);
        SCustomAdapter adapter = new SCustomAdapter();
        swipeRefreshLayout.setProgressViewOffset(false, 40, 140);

        loadViewHelper = new SwipeLoadViewHelper<>(swipeRefreshLayout, listView, new MyLoadFactory());
        loadViewHelper.setAdapter(adapter);
        loadViewHelper.setOnRefreshLoadListener(new SwipeLoadViewHelper.OnRefreshLoadListener() {
            @Override
            public void onRefresh() {
                tv_refresh.setText(loadViewHelper.isRefreshing() ?"true" : "false");
                listView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadViewHelper.completeRefresh();
                        tv_refresh.setText(loadViewHelper.isRefreshing() ?"true" : "false");
                    }
                }, 1000);
            }

            @Override
            public void onLoad() {
                tv_loading.setText(loadViewHelper.isLoading() ?"true" : "false");
                listView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadViewHelper.completeLoadmore();
                        tv_loading.setText(loadViewHelper.isLoading() ?"true" : "false");
                    }
                }, 1000);
            }
        });
    }

    public void onBtnClick(View view) {
        switch (view.getId()) {
            case R.id.button1:
                loadViewHelper.animRefresh();
                break;
            case R.id.button2:
                loadViewHelper.endAnimRefresh();
                break;
            case R.id.button3:
                loadViewHelper.setLoadMoreError("show your custom error info");
                break;
        }
    }
}
