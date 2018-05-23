# LoadMoreHelper
  <b>原SwipeLoadView已经改名为LoadMoreHelper，支持更多刷新控件。</b>
  LoadMoreHelper可以为任意下拉刷新控件，多种滚动布局(RecyclerView, ListView, GridView, ExpandableListView等)
  增加上拉加载功能，可以作为下拉刷新库的加载功能补充。
  
---

  * [主要功能介绍](#主要功能介绍)
  * [如何添加到项目中](#如何添加到项目中)
  * [如何使用](#如何使用)
  * [示例代码](#示例代码)
  * [截图](#截图)
  * [关于作者](#关于作者)

---

### 主要功能介绍

* 可以同时支持为ListView、RecyclerView、GridView、ExpandableListView等内容组件增加pull load功能。
* load more包含上拉加载、加载中、加载失败、没有更多的功能。可以实现完全自定义。
* 支持添加各种下拉刷新组件的关联，关联后可以解决refresh和load状态的一些处理和冲突等。
* 默认实现了一套完整的功能，包含SwipeRefreshLayout+(ListView, RecyclerView, GridView, ExpandableListView)+DefaultLoadMoreView。
* 其它想要自定义的话，实现IRefreshLayoutHandler或ILoadViewHandler或ILoadMoreView（看你自己想自定义那些部分），并在构造LoadMoreHelper时传入即可。

---

### 如何添加到项目中

本library已经支持maven。Android Studio用户，只需要在项目的build.gradle中添加该depandencies：

  `
    compile 'com.missmess.swipeloadview:loadmorehelper:2.1.0'
  `

---

### 如何使用

调用非常简单，只需要几句代码即可实现为你的refresh view添加加载更多的功能。

  用法如下：
```java
// new a LoadMoreHelper
LoadMoreHelper loadViewHelper = new LoadMoreHelper(swipeRefreshLayout, listView);
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

### 示例代码
###### 0、默认增加对SwipeRefreshLayout刷新库的支持
```java
LoadMoreHelper loadViewHelper = new LoadMoreHelper(swipeRefreshLayout, listView);
```

如果需要自己指定特定的刷新库，可以实现对应的IRefreshLayoutHandler。并在构造LoadMoreHelper时，传入。以
SmartRefreshLayout为例：
```java
LoadMoreHelper loadViewHelper = new LoadMoreHelper(smartRefreshLayout, new IRefreshLayoutHandler<SmartRefreshLayout>() {
		@Override
		public void handleSetRefreshListener(SmartRefreshLayout view, final Runnable runnable) {
			view.setOnRefreshListener(new OnRefreshListener() {
				@Override
				public void onRefresh(RefreshLayout refreshLayout) {
					runnable.run();
				}
			});
		}
		@Override
		public void refresh(SmartRefreshLayout view) {
			view.autoRefresh();
		}
		@Override
		public void finishRefresh(SmartRefreshLayout view) {
			view.finishRefresh();
		}
	}, recyclerView);
```

###### 1、为不同的refresh view添加pull load支持

可以为ListView、RecyclerView、GridView、ExpandableListView添加支持。仅需要为这些不同的refresh view添加对应的泛型，和adapter即可。

* ListView
```java
LoadMoreHelper loadViewHelper = new LoadMoreHelper(swipeRefreshLayout, listView);
loadViewHelper.setAdapter(new MyListAdapter());
```

* RecyclerView
```java
LoadMoreHelper loadViewHelper = new LoadMoreHelper(swipeRefreshLayout, recyclerView);
loadViewHelper.setAdapter(new MyRecyclerAdapter());
```

* GridView

    需要使用`GridViewWithHeaderAndFooter`替代GridView。
```java
LoadMoreHelper loadViewHelper = new LoadMoreHelper(swipeRefreshLayout, gridView);
loadViewHelper.setAdapter(new MyGridAdapter());
```

* ExpandableListView
```java
LoadMoreHelper loadViewHelper = new LoadMoreHelper(swipeRefreshLayout, expandableListView);
loadViewHelper.setAdapter(new MyExpandListAdapter());
```

###### 2、通知SwipeLoadViewHelper，没有更多数据了

  没有更多数据时，需要调用setHasMoreData方法告知SwipeLoadViewHelper。
```java
if (nomoredata) { //没有更多数据了
    loadViewHelper.setHasMoreData(false);
}
```

###### 3、设置自定义加载失败信息

  加载失败时，可以自定义显示错误信息
```java
loadViewHelper.setLoadMoreError("connect failed, click to retry");
```

###### 4、自定义loadview

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
        public void showFail(CharSequence e) {
            // 错误信息通过e.getMessage()来获取
            error.setText(e);
            error.setChecked(true);
        }
    }
}
```
并在new SwipeLoadViewHelper时，传参：
```java
    LoadMoreHelper loadViewHelper = new LoadMoreHelper(swipeRefreshLayout, listView, new MyLoadFactory());
```

---

### 截图
![image](https://raw.githubusercontent.com/missmess/swipeloadview/master/raw/picc1.jpg)
![image](https://raw.githubusercontent.com/missmess/swipeloadview/master/raw/picc2.jpg)
![image](https://raw.githubusercontent.com/missmess/swipeloadview/master/raw/picc3.jpg)
![image](https://raw.githubusercontent.com/missmess/swipeloadview/master/raw/picc4.jpg)
![image](https://raw.githubusercontent.com/missmess/swipeloadview/master/raw/picc5.jpg)

---

### 关于作者
在使用中有任何问题，欢迎反馈给我，可以用以下联系方式跟我交流：

* QQ: 478271233
* 邮箱：<478271233@qq.com>
* GitHub: [@missmess](https://github.com/missmess)

---
###### CopyRight：`missmess`
