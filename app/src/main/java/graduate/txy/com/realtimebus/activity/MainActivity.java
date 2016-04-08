package graduate.txy.com.realtimebus.activity;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.Window;
import android.widget.RadioGroup;
import android.widget.TextView;

import graduate.txy.com.realtimebus.R;
import graduate.txy.com.realtimebus.fragment.BaseFragment;
import graduate.txy.com.realtimebus.fragment.FragmentFactory;


public class MainActivity extends Activity {

    private BaseFragment mFragment;
    private FragmentManager mFManger;
    private RadioGroup tabs;
    private TextView tv_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//无title
        setContentView(R.layout.activity_main);
        init();
    }


    /**
     * 初始化一些数据
     */
    private void init() {
        tv_title = (TextView) findViewById(R.id.tv_title);
        mFManger = getFragmentManager();
        tabs = (RadioGroup) findViewById(R.id.rg_tab);
        tabs.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                //切换fragment
                FragmentTransaction transaction = mFManger.beginTransaction();
                mFragment = (BaseFragment) FragmentFactory.getInstanceByIndex(checkedId);
                tv_title.setText(mFragment.getTitle());
                transaction.replace(R.id.fl_main, mFragment);
                transaction.commit();
            }
        });

        //默认Tab
        tabs.getChildAt(0).performClick();
    }

/*
复用Fragment
    public void switchContent(Fragment from, Fragment to) {
        if (mContent != to) {
            mContent = to;
            FragmentTransaction transaction = mFragmentMan.beginTransaction().setCustomAnimations(
                    android.R.anim.fade_in, R.anim.slide_out);
            if (!to.isAdded()) {    // 先判断是否被add过
                transaction.hide(from).add(R.id.content_frame, to).commit(); // 隐藏当前的fragment，add下一个到Activity中
            } else {
                transaction.hide(from).show(to).commit(); // 隐藏当前的fragment，显示下一个
            }
        }
    }
*/
}
