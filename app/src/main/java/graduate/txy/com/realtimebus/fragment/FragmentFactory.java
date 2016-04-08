package graduate.txy.com.realtimebus.fragment;

import android.app.Fragment;

import graduate.txy.com.realtimebus.R;

/**
 * Fragment工厂类  用于选择fragment
 * Created by lenovo on 2016/3/17.
 */
public class FragmentFactory extends Fragment {
    public static Fragment getInstanceByIndex(int checkId) {
        Fragment fragment = null;
        switch (checkId) {
            case R.id.rb_select_bus: {
                fragment = new SelectFragment();
                break;
            }
            case R.id.rb_pass_bus: {
                fragment = new PassFragment();
                break;
            }
            case R.id.rb_my_bus: {
                fragment = new MyFragment();
                break;
            }
        }

        return fragment;
    }
}
