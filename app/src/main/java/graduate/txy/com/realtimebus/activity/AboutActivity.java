package graduate.txy.com.realtimebus.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import graduate.txy.com.realtimebus.R;
import graduate.txy.com.realtimebus.adapter.CategoryAdapter;
import graduate.txy.com.realtimebus.domain.Category;
import graduate.txy.com.realtimebus.globalApp.MyApplication;

/**
 * AboutActivity页面的显示
 * <p/>
 * Created by lenovo on 2016/3/20.
 */
public class AboutActivity extends Activity {

    private TextView tv_title;
    private ListView lv_my_about;
    private ArrayList<Category> mCategories;

    private CategoryAdapter mCategoryAdapter;
    private static final String TAG = "AboutActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//无title
        setContentView(R.layout.activity_about);
        init();
    }

    private void init() {
        tv_title = (TextView) findViewById(R.id.tv_about_title);
        tv_title.setText(getIntent().getStringExtra("name"));
        lv_my_about = (ListView) findViewById(R.id.lv_my_about);
        mCategories = getData();
        mCategoryAdapter = new CategoryAdapter(AboutActivity.this, mCategories);
        lv_my_about.setAdapter(mCategoryAdapter);
        lv_my_about.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Category.CategoryItem item = (Category.CategoryItem) mCategoryAdapter.getItem(position);
                String name = item.getItemName();
                Log.i(TAG, "跳转" + name);
                Intent intent = new Intent(MyApplication.getInstance(), item.getClasz());
                intent.putExtra("name", name);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
    }

    private ArrayList<Category> getData() {
        Log.i(TAG, "getData");
        ArrayList<Category> cs = new ArrayList<Category>();
        Category c1 = new Category("");
        c1.addItems(c1.new CategoryItem("收藏线路", R.drawable.collection, CollectionActivity.class));
        c1.addItems(c1.new CategoryItem("城市切", R.drawable.city, CityActivty.class));
        c1.addItems(c1.new CategoryItem("城市换", R.drawable.city, CityActivty.class));
        c1.addItems(c1.new CategoryItem("城切换", R.drawable.city, CityActivty.class));
        cs.add(c1);
        return cs;
    }


    public boolean onKeyDown(int keyCode, KeyEvent event) {// 按返回键时退出Activity的Activity特效动画

        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            finish();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
