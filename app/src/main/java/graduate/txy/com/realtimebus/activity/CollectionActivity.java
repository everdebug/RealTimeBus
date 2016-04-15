package graduate.txy.com.realtimebus.activity;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.lidroid.xutils.DbUtils;

import java.util.List;

import graduate.txy.com.realtimebus.R;
import graduate.txy.com.realtimebus.domain.CollectionInfo;
import graduate.txy.com.realtimebus.utils.XutilsDataBaseUtils;

/**
 * 收藏路线的显示
 * //TODO 是否进行路线的显示？数据的更新？
 * Created by lenovo on 2016/3/20.
 */
public class CollectionActivity extends Activity {
    private static final String TAG = "CollectionActivity";
    private TextView tv;
    private ListView lv_my_collection;
    private List<CollectionInfo> infos = null;
    private TextView tv_show_null;
    private DbUtils mDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_collection);
        tv = (TextView) findViewById(R.id.tv_collection_title);
        tv_show_null = (TextView) findViewById(R.id.tv_show_null);
        tv.setText(getIntent().getStringExtra("name"));
        mDB = XutilsDataBaseUtils.createDB(this);
        lv_my_collection = (ListView) findViewById(R.id.lv_my_collection);
        infos = XutilsDataBaseUtils.getCollectionInfoList(mDB);
        //TODO 问题判断表书否存在，以及是否有数据
        if (infos.size() > 0) {
            Log.i(TAG,infos.size()+"");
            lv_my_collection.setVisibility(View.VISIBLE);
            tv_show_null.setVisibility(View.INVISIBLE);
        } else {
            return;
        }
        lv_my_collection.setAdapter(new CollectionAdapter(infos));

    }

    //TODO Bug-->数据库创建，但是表如果没有-->引起程序crash
    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
    }

    /**
     * 摁键返回
     *
     * @param view
     */
    public void returnActivity(View view) {
        backActivity();
    }

    //返回finish和设置跳转动画
    private void backActivity() {
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {// 按返回键时退出Activity的Activity特效动画

        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            backActivity();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    class CollectionAdapter extends BaseAdapter {
        private List<CollectionInfo> mInfos;

        public CollectionAdapter(List<CollectionInfo> infos) {
            this.mInfos = infos;
        }

        @Override
        public int getCount() {
            return infos.size();
        }

        @Override
        public Object getItem(int position) {
            return infos.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = null;
            ViewHolder viewHolder = null;
            if (convertView != null) {
                view = convertView;
                viewHolder = (ViewHolder) view.getTag();
            } else {
                viewHolder = new ViewHolder();
                view = View.inflate(CollectionActivity.this, R.layout.item_collection, null);
                viewHolder.tv_collection_addr = (TextView) view.findViewById(R.id.tv_collection_addr);
                viewHolder.tv_collection_name = (TextView) view.findViewById(R.id.tv_collection_name);
                viewHolder.tv_collection_city = (TextView) view.findViewById(R.id.tv_collection_city);
                view.setTag(viewHolder);
            }
            viewHolder.tv_collection_addr.setText(infos.get(position).getStartStation() + "——>" + infos.get(position).getEndStation());
            viewHolder.tv_collection_name.setText(infos.get(position).getRouteName());
            viewHolder.tv_collection_city.setText(infos.get(position).getCity());

            return view;
        }

        class ViewHolder {
            TextView tv_collection_addr;
            TextView tv_collection_name;
            TextView tv_collection_city;
        }
    }


}
