package graduate.txy.com.realtimebus;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.Window;
import android.widget.RadioGroup;
import android.widget.TextView;

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

    private void init() {
        tv_title = (TextView) findViewById(R.id.tv_title);
        mFManger = getFragmentManager();
        tabs = (RadioGroup) findViewById(R.id.rg_tab);
        tabs.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                FragmentTransaction transaction = mFManger.beginTransaction();
                mFragment = (BaseFragment)FragmentFactory.getInstanceByIndex(checkedId);
                tv_title.setText(mFragment.getTitle());
                transaction.replace(R.id.fl_main, mFragment);
                transaction.commit();

            }
        });


        tabs.getChildAt(0).performClick();


    }


}
