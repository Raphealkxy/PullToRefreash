package com.example.timmy.pulltorefreash;

import android.app.Activity;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.handmark.pulltorefresh.samples.PullToRefreshGridActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class GridActivity extends Activity implements AdapterView.OnItemClickListener {


    private PullToRefreshGridView mPullRefreshGridView;
    private GridView mGridView;


    private List<Integer> booleans;
    private TableAdapter mAdapter;
    private List<String> list;
    private String content;
    private Toast mToast;
    private List<User>listUser=new ArrayList<User>();
    private String [] columnName=null;
    private List<User>listadd =new ArrayList<>();


    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid);//设置主页面

        //表的参数名称
        columnName=new String[]{"学号", "姓名", "性别", "密码","电话"};

        mPullRefreshGridView = (PullToRefreshGridView) findViewById(com.handmark.pulltorefresh.samples.R.id.pull_refresh_grid);//获取下拉刷新gridview空件
        mGridView = mPullRefreshGridView.getRefreshableView();

        // Set a listener to be invoked when the list should be refreshed.
        mPullRefreshGridView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<GridView>() {
           //上拉下拉数据添加逻辑暂时共享
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<GridView> refreshView) {
                Toast.makeText(GridActivity.this, "Pull Down!", Toast.LENGTH_SHORT).show();
                new GridActivity.GetDataTask().execute();//开始获取数据
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<GridView> refreshView) {
                Toast.makeText(GridActivity.this, "Pull Up!", Toast.LENGTH_SHORT).show();
                new GridActivity.GetDataTask().execute();//开始上拉获取数据
            }

        });

       User user1=new User();
        user1.setUserNumber("15416118");
        user1.setUsername("小明");
        user1.setSex("1");
        user1.setPassword("12345");
        user1.setTelephone("12444");

        User user2=new User();
        user2.setUserNumber("15416118");
        user2.setUsername("小明");
        user2.setSex("1");
        user2.setPassword("12345");
        user2.setTelephone("12444");

        listUser.add(user1);
        listUser.add(user2);

        list = new ArrayList<>();
        booleans = new ArrayList<>();

        //设置数据为空时的界面
        TextView tv = new TextView(this);
        tv.setGravity(Gravity.CENTER);
        tv.setText("Empty View, Pull Down/Up to Add Items");
        mPullRefreshGridView.setEmptyView(tv);


        //设置适配器
        mAdapter = new TableAdapter();
        mGridView.setAdapter(mAdapter);

        //添加消息处理
        mGridView.setOnItemClickListener(this);

        //添加表头
        addHeader();

        //添加数据测试
        addData();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(GridActivity.this, list.get(position), Toast.LENGTH_SHORT).show();

    }

    private class GetDataTask extends AsyncTask<Void, Void, List<User>> {

        @Override
        protected List<User> doInBackground(Void... params) {
            // Simulates a background job.
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
            }
            return listUser;//返回的result数据
        }

        //添加数据逻辑
        @Override
        protected void onPostExecute(List<User> result) {
            listadd.clear();
            User user1=new User();
            user1.setUserNumber("15416115");
            user1.setUsername("小明");
            user1.setSex("1");
            user1.setPassword("12345");
            user1.setTelephone("12444");
            listadd.add(user1);

            listUser.addAll(listadd);
            //listadd.clear();
            addupdate();
            mAdapter.notifyDataSetChanged();

            // Call onRefreshComplete when the list has been refreshed.
            mPullRefreshGridView.onRefreshComplete();

            super.onPostExecute(result);
        }
    }

    private void addupdate() {
        // String titles[] = {"早操", "早自习", "上课", "晚自习", "晚寝", "活动点到"};
        for (int i =(listUser.size()-listadd.size()) ; i < listUser.size(); i++) {
            for (int j = 0; j < 5; j++) {
                if (j == 0) {
                    list.add(listUser.get(i).getUserNumber());
                } else if(j==1) {
                    list.add(listUser.get(i).getUsername());
                }else if(j==2) {
                    list.add(listUser.get(i).getUsername());
                }else if(j==3) {
                    list.add(listUser.get(i).getSex());
                }else if(j==4) {
                    list.add(listUser.get(i).getTelephone());
                }
                if (i % 2 == 0) {
                    booleans.add(1);

                } else {
                    booleans.add(2);
                }
            }
        }
        mAdapter.notifyDataSetChanged(); //更新数据
    }


    //table适配器
    class TableAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = View.inflate(GridActivity.this, R.layout.grid_item, null);
                holder = new ViewHolder();
                convertView.setTag(holder);
                holder.tv_item = (TextView) convertView.findViewById(R.id.tv_item);
                holder.ll_item = (LinearLayout) convertView.findViewById(R.id.ll_item);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.tv_item.setText(list.get(position));

            if (booleans.get(position) == 0) {
                //表头颜色
                holder.tv_item.setBackgroundColor(Color.parseColor("#95c5ef"));
                holder.ll_item.setBackgroundColor(Color.WHITE);
            } else if (booleans.get(position) == 1) {
                //奇数行颜色
                holder.tv_item.setBackgroundColor(Color.parseColor("#EAEBEB"));
                holder.ll_item.setBackgroundColor(Color.parseColor("#dadada"));
            } else {
                //偶数行颜色
                holder.tv_item.setBackgroundColor(Color.parseColor("#FFFFFF"));
                holder.ll_item.setBackgroundColor(Color.parseColor("#dadada"));
            }
            return convertView;
        }

        class ViewHolder {
            private TextView tv_item;
            private LinearLayout ll_item;
        }
    }

    //目的是对其列数据
    public void addData() {

        // String titles[] = {"早操", "早自习", "上课", "晚自习", "晚寝", "活动点到"};
        for (int i = 0; i < listUser.size(); i++) {
            for (int j = 0; j < 5; j++) {
                if (j == 0) {
                    list.add(listUser.get(i).getUserNumber());
                } else if(j==1) {
                    list.add(listUser.get(i).getUsername());
                }else if(j==2) {
                    list.add(listUser.get(i).getUsername());
                }else if(j==3) {
                    list.add(listUser.get(i).getSex());
                }else if(j==4) {
                    list.add(listUser.get(i).getTelephone());
                }
                if (i % 2 == 0) {
                    booleans.add(1);

                } else {
                    booleans.add(2);
                }
            }
        }
        mAdapter.notifyDataSetChanged(); //更新数据
    }

    //清空列表
    public void RemoveAll() {
        list.clear();
       mAdapter.notifyDataSetChanged();
    }

    //设置头
    public void addHeader() {
        String items[] =columnName;
        for (String strText : items) {
            booleans.add(0);
            list.add(strText);
        }
       mAdapter.notifyDataSetChanged(); //更新数据
    }

}
