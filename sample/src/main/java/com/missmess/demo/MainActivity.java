package com.missmess.demo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onBtnClick(View v) {
        switch (v.getId()) {
            case R.id.button:
                startActivity(new Intent(this, SwipeListViewActivity.class));
                break;
            case R.id.button2:
                startActivity(new Intent(this, SwipeRecyclerViewActivity.class));
                break;
            case R.id.button3:
                startActivity(new Intent(this, SwipeGridViewActivity.class));
                break;
            case R.id.button4:
                startActivity(new Intent(this, SwipeExpandableListActivity.class));
                break;
            case R.id.button5:
                startActivity(new Intent(this, CustomLoadFactoryActivity.class));
                break;
        }
    }
}
