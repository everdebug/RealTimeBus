package graduate.txy.com.realtimebus.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.IBinder;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import graduate.txy.com.realtimebus.MyView.ClearEditText;
import graduate.txy.com.realtimebus.MyView.SideBar;
import graduate.txy.com.realtimebus.R;
import graduate.txy.com.realtimebus.adapter.MyBaseAdapter;
import graduate.txy.com.realtimebus.adapter.SortAdapter;
import graduate.txy.com.realtimebus.db.RegionDAO;
import graduate.txy.com.realtimebus.domain.RegionInfo;
import graduate.txy.com.realtimebus.domain.SortModel;
import graduate.txy.com.realtimebus.utils.CharacterParser;
import graduate.txy.com.realtimebus.utils.PinyinComparator;
import graduate.txy.com.realtimebus.utils.SharePreferenceUtils;

/**
 * 选择城市模块
 * <p/>
 * Created by lenovo on 2016/3/20.
 */
public class CityActivty extends Activity {
    private TextView tv;
    protected static final String TAG = "CityActivty";
    private List<RegionInfo> provinceList;//省份信息
    private List<RegionInfo> citysList;//城市列表
    private List<String> provinces;//地方名称
    private ListView sortListView;//显示列表
    private SideBar sideBar;
    private TextView dialog;
    private SortAdapter adapter;
    private ClearEditText mClearEditText;
    private List<RegionInfo> mHotCitys;// 热门城市列表
    private MyGridViewAdapter gvAdapter;
    private GridView mGridView;

    /**
     * 汉字转换成拼音的类
     */
    private CharacterParser characterParser;

    private List<SortModel> SourceDateList;

    /**
     * 根据拼音来排列ListView里面的数据类
     */
    private PinyinComparator pinyinComparator;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//无title
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.activity_city);
        tv = (TextView) findViewById(R.id.tv_city_title);
        tv.setText(getIntent().getStringExtra("name"));

        initData();
        initViews();
    }

    private void initData() {
        provinceList = RegionDAO.getProvencesOrCity(1);//列表中加入省份信息
        provinceList.addAll(RegionDAO.getProvencesOrCity(2));//加入市的信息

        citysList = new ArrayList<RegionInfo>();
        mHotCitys = new ArrayList<RegionInfo>();
        provinces = new ArrayList<String>();
        for (RegionInfo info : provinceList) {
            provinces.add(info.getName().trim());
        }
        //添加热门城市
        mHotCitys.add(new RegionInfo(2, 1, "北京"));
        mHotCitys.add(new RegionInfo(25, 1, "上海"));
        mHotCitys.add(new RegionInfo(76, 6, "广州"));
    }

    private void initViews() {
        View view = View.inflate(this, R.layout.head_city_list, null);
        mGridView = (GridView) view.findViewById(R.id.id_gv_remen);
        gvAdapter = new MyGridViewAdapter(this, mHotCitys);
        mGridView.setAdapter(gvAdapter);
        mGridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        // 实例化汉字转拼音类
        characterParser = CharacterParser.getInstance();

        pinyinComparator = new PinyinComparator();

        sideBar = (SideBar) findViewById(R.id.sidrbar);
        dialog = (TextView) findViewById(R.id.dialog);
        sideBar.setTextView(dialog);

        // 设置右侧触摸监听
        sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {

            @Override
            public void onTouchingLetterChanged(String s) {
                // 该字母首次出现的位置
                int position = adapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    sortListView.setSelection(position);
                }

            }
        });

        sortListView = (ListView) findViewById(R.id.country_lvcountry);
        sortListView.addHeaderView(view);
        sortListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 这里要利用adapter.getItem(position)来获取当前position所对应的对象
                String name = ((SortModel) adapter.getItem(position - 1)).getName();
                hideSoftInput(mClearEditText.getWindowToken());
                SharePreferenceUtils.setSPStringValue(CityActivty.this, "city", name);
                Log.i(TAG, "选中城市 ：" + name);
                backActivity();
                //返回显示
                /*
                Intent data = new Intent();
                data.putExtra("cityName", name);
                setResult(1110, data);
            */
            }
        });
        SourceDateList = filledData(provinceList);

        // 根据a-z进行排序源数据
        Collections.sort(SourceDateList, pinyinComparator);


        adapter = new SortAdapter(this, SourceDateList);
        sortListView.setAdapter(adapter);

        mClearEditText = (ClearEditText) findViewById(R.id.filter_edit);
        // 根据输入框输入值的改变来过滤搜索
        mClearEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // 当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
                filterData(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        //热门城市点击事件处理
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String cityName = mHotCitys.get(position).getName();
                hideSoftInput(mClearEditText.getWindowToken());
                SharePreferenceUtils.setSPStringValue(CityActivty.this, "city", cityName);
                Log.i(TAG, "选中城市 ：" + cityName);
                backActivity();
                //返回显示
                /*
                Intent data = new Intent();
                data.putExtra("cityName", cityName);
                setResult(1110, data);*/
            }
        });

    }

    /**
     * 为ListView填充数据，地区名称与响应首字母相关联
     *
     * @param date
     * @return
     */
    private List<SortModel> filledData(List<RegionInfo> date) {
        List<SortModel> mSortList = new ArrayList<SortModel>();

        for (int i = 0; i < date.size(); i++) {
            SortModel sortModel = new SortModel();
            sortModel.setName(date.get(i).getName());
            // 汉字转换成拼音
            String pinyin = characterParser.getSelling(date.get(i).getName());
            String sortString = pinyin.substring(0, 1).toUpperCase();

            // 正则表达式，判断首字母是否是英文字母
            if (sortString.matches("[A-Z]")) {
                sortModel.setSortLetters(sortString.toUpperCase());
            } else {
                sortModel.setSortLetters("#");
            }

            mSortList.add(sortModel);
        }
        return mSortList;

    }

    /**
     * 根据输入框中的值来过滤数据并更新ListView
     *
     * @param filterStr
     */
    private void filterData(String filterStr) {
        List<SortModel> filterDateList = new ArrayList<SortModel>();

        if (TextUtils.isEmpty(filterStr)) {
            filterDateList = SourceDateList;
        } else {
            if (!provinces.contains(filterStr)) {
                filterDateList.clear();
                for (SortModel sortModel : SourceDateList) {
                    String name = sortModel.getName();
                    if (name.indexOf(filterStr.toString()) != -1 || characterParser.getSelling(name).startsWith(filterStr.toString())) {
                        filterDateList.add(sortModel);
                    }
                }
            } else {
                filterDateList.clear();
                for (int i = 0; i < provinceList.size(); i++) {
                    String name = provinceList.get(i).getName();
                    if (name.equals(filterStr)) {
                        filterDateList.addAll(filledData(RegionDAO.getProvencesOrCityOnParent(provinceList.get(i).getId())));
                    }
                }
            }
        }

        // 根据a-z进行排序
        Collections.sort(filterDateList, pinyinComparator);
        adapter.updateListView(filterDateList);
    }

    private class MyGridViewAdapter extends MyBaseAdapter<RegionInfo, GridView> {
        private LayoutInflater inflater;

        public MyGridViewAdapter(Context ct, List<RegionInfo> list) {
            super(ct, list);
            inflater = LayoutInflater.from(ct);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = inflater.inflate(R.layout.item_remen_city, null);
                holder.id_tv_cityname = (TextView) convertView.findViewById(R.id.id_tv_cityname);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            RegionInfo info = mHotCitys.get(position);
            holder.id_tv_cityname.setText(info.getName());
            return convertView;
        }

        class ViewHolder {
            TextView id_tv_cityname;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    /**
     * 多种隐藏软件盘方法的其中一种
     *
     * @param token
     */
    protected void hideSoftInput(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * 摁键返回
     *
     * @param view
     */
    public void returnActivity(View view) {
        backActivity();
    }

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
}
