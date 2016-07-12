#SwipeLoadView
  
  SwipeLoadView可以在使用SwipeRefreshLayout作为下拉刷新控件时，同时为list增加加载更多功能。
  
  SwipeLoadView is a helper library when using SwipeRefreshLayout to pull refresh, use this to add pull to load more function.
  
---

  * [主要功能介绍](#主要功能介绍)
  * [如何添加到项目中](#如何添加到项目中)
  * [如何使用](#如何使用)
  * [示例代码](#示例代码)
  * [截图](#截图)
  * [关于作者](#关于作者)

---

###主要功能介绍

* 可以同时支持为ListView、RecyclerView、GridView、ExpandableListView增加pull load功能。
* load more包含上拉加载、加载中、加载失败、没有更多的功能。
* 支持完全自定义load more的布局。
* pull refresh和pull load不会同时进行导致冲突。

---

###如何添加到项目中

本library已经支持maven。Android Studio用户，只需要在项目的build.gradle中添加该depandencies：

  `
    compile "com.missmess.swipeloadview:swipeloadview:1.0.0"
  `

---

###如何使用

调用非常简单，只需要几句代码即可实现为你的refresh view添加加载更多的功能。

  用法如下：
```java
// new a SwipeLoadViewHelper
SwipeLoadViewHelper<ListView> loadViewHelper = new SwipeLoadViewHelper<>(swipeRefreshLayout, listView);
// set adapter
loadViewHelper.setAdapter(adapter);
// set refresh and load listener
loadViewHelper.setOnRefreshLoadListener(new SwipeLoadViewHelper.OnRefreshLoadListener() {
    @Override
    public void onRefresh() {
        // do your refresh OP
    }

    @Override
    public void onLoad() {
        // do your load more OP
    }
});
```
但是不要忘记在你的刷新或加载更多操作结束后，通知SwipeLoadViewHelper。
```java
    // refresh OP finished，notify SwipeLoadViewHelper
    loadViewHelper.completeRefresh();
    // load more OP finished，notify SwipeLoadViewHelper
    loadViewHelper.completeLoadmore();
```
---

###示例代码
######1、为不同的refresh view添加pull load支持

SwipeLoadView可以为ListView、RecyclerView、GridView、ExpandableListView添加支持。仅需要为这些不同的refresh view添加对应的泛型，和adapter即可。

* ListView
```java
SwipeLoadViewHelper<ListView> loadViewHelper = new SwipeLoadViewHelper<>(swipeRefreshLayout, listView);
loadViewHelper.setAdapter(new MyListAdapter());
```

* RecyclerView
```java
SwipeLoadViewHelper<RecyclerView> loadViewHelper = new SwipeLoadViewHelper<>(swipeRefreshLayout, recyclerView);
loadViewHelper.setAdapter(new MyRecyclerAdapter());
```
使用RecyclerView时，adapter需要继承于HFAdapter：
```java
public class MyListAdapter extends HFAdapter {
    private List<String> datas;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolderHF(ViewGroup viewGroup, int type) {
        View view = View.inflate(viewGroup.getContext(), R.layout.item_text, null);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolderHF(RecyclerView.ViewHolder vh, int position) {
        ViewHolder holder = (ViewHolder) vh;
        holder.tv.setText(String.format("%s %d", datas.get(position), position));
    }

    @Override
    public int getItemCountHF() {
        return datas.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv;
        public ViewHolder(View itemView) {
            super(itemView);
            tv = (TextView) itemView;
        }
    }
}
```

* GridView

    需要使用`GridViewWithHeaderAndFooter`替代GridView。
```java
SwipeLoadViewHelper<GridViewWithHeaderAndFooter> loadViewHelper = new SwipeLoadViewHelper<>(swipeRefreshLayout, gridView);
loadViewHelper.setAdapter(new MyGridAdapter());
```

* ExpandableListView
```java
SwipeLoadViewHelper<ExpandableListView> loadViewHelper = new SwipeLoadViewHelper<>(swipeRefreshLayout, expandableListView);
loadViewHelper.setAdapter(new MyExpandListAdapter());
```

######2、通知SwipeLoadViewHelper，没有更多数据了

  没有更多数据时，需要调用setHasMoreData方法告知SwipeLoadViewHelper。
```java
if (nomoredata) { //没有更多数据了
    loadViewHelper.setHasMoreData(false);
}
```

######3、设置自定义加载失败信息

  加载失败时，可以自定义显示错误信息
```java
loadViewHelper.setLoadMoreError("connect failed, click to retry");
```

######4、自定义loadview

  可以完全自定义LoadView的布局，通过实现ILoadViewFactory：
```java
public class MyLoadFactory implements ILoadViewFactory {
    @Override
    public ILoadMoreView madeLoadMoreView() {
        return new MyLoadMoreView();
    }

    /**
     * custom load more view
     */
    class MyLoadMoreView implements ILoadMoreView {
        private RadioButton normal;
        private RadioButton loading;
        private RadioButton nodata;
        private RadioButton error;

        @Override
        public View create(LayoutInflater inflater, View.OnClickListener onClickLoadMoreListener) {
            View view = inflater.inflate(R.layout.view_custom_load, null);
            normal = (RadioButton) view.findViewById(R.id.radioButton1);
            loading = (RadioButton) view.findViewById(R.id.radioButton2);
            nodata = (RadioButton) view.findViewById(R.id.radioButton3);
            error = (RadioButton) view.findViewById(R.id.radioButton4);

            return view;
        }

        @Override
        public void showNormal() {
            normal.setChecked(true);
        }

        @Override
        public void showNomore() {
            nodata.setChecked(true);
        }

        @Override
        public void showLoading() {
            loading.setChecked(true);
        }

        @Override
        public void showFail(Exception e) {
            // 错误信息通过e.getMessage()来获取
            error.setText(e.getMessage());
            error.setChecked(true);
        }
    }
}
```
并在new SwipeLoadViewHelper时，传参：
```java
    SwipeLoadViewHelper<ListView> loadViewHelper = new SwipeLoadViewHelper<>(swipeRefreshLayout, listView, new MyLoadFactory());
```

---

###截图
![image](https://raw.githubusercontent.com/missmess/swipeloadview/master/raw/picc1.jpg)
![image](https://raw.githubusercontent.com/missmess/swipeloadview/master/raw/picc2.jpg)
![image](https://raw.githubusercontent.com/missmess/swipeloadview/master/raw/picc3.jpg)
![image](https://raw.githubusercontent.com/missmess/swipeloadview/master/raw/picc4.jpg)
![image](https://raw.githubusercontent.com/missmess/swipeloadview/master/raw/picc5.jpg)

---

###关于作者
在使用中有任何问题，欢迎反馈给我，可以用以下联系方式跟我交流：

* QQ: 478271233
* 邮箱：<478271233@qq.com>
* GitHub: [@missmess](https://github.com/missmess)

---
######CopyRight：`missmess`
