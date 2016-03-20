package graduate.txy.com.realtimebus.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import graduate.txy.com.realtimebus.domain.Category;
import graduate.txy.com.realtimebus.adapter.CategoryAdapter;
import graduate.txy.com.realtimebus.R;

/**
 *
 * MyFragment
 * Created by lenovo on 2016/3/17.
 */
public class MyFragment extends BaseFragment {


    private ListView lv_my;



    public MyFragment() {
        title = "我";
    }

    private static final String TAG = "MyFragment";
    private ArrayList<Category> categories;

    private View rootView;//缓存Fragment view

    private CategoryAdapter categoryAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        if (rootView == null) {
            Log.i(TAG, "创建View");
            rootView = inflater.inflate(R.layout.my_fragment, null);
        }
        //缓存的rootView需要判断是否已经被加过parent， 如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }
        Log.i(TAG, "oncreateview");
        init(rootView);
        return rootView;

    }

    private void init(View view) {
        Log.i(TAG, "init");

        lv_my = (ListView) view.findViewById(R.id.lv_my);
        categories = getData();
        categoryAdapter = new CategoryAdapter(view.getContext(), categories);
        lv_my.setAdapter(categoryAdapter);

        lv_my.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name = ((Category.CategoryItem)categoryAdapter.getItem(position)).getItemName();
                Log.i(TAG,"name"+name);
                Toast.makeText(getContext(),name,Toast.LENGTH_SHORT).show();
            }
        });

    }


    /*

         A a = new A();
        A.B ab = a.new B();

     */
    private ArrayList<Category> getData() {
        Log.i(TAG, "getData");

        ArrayList<Category> cs = new ArrayList<Category>();
        Category c1 = new Category("");
        c1.addItems(c1.new CategoryItem("收藏线路", R.drawable.collection));
        c1.addItems(c1.new CategoryItem("城市切换", R.drawable.city));
        Category c2 = new Category("");
        c2.addItems(c2.new CategoryItem("离线地图", R.drawable.map));
        c2.addItems(c2.new CategoryItem("地铁线路图", R.drawable.subway));
        Category c3 = new Category("");
        c3.addItems(c3.new CategoryItem("关于RTB", R.drawable.about));
        cs.add(c1);
        cs.add(c2);
        cs.add(c3);
        return cs;
    }


}
