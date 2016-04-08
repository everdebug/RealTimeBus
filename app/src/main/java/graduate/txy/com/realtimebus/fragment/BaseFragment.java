package graduate.txy.com.realtimebus.fragment;

import android.app.Fragment;

/**
 * BaseFragment
 *  为什么使用他？？？？？？
 * Created by lenovo on 2016/3/17.
 */
public class BaseFragment extends Fragment {


//    private Activity activity;
/*
    public Context getContexts() {
        if (activity == null) {
            return MyApplication.getInstance();
        }
        return activity;
    }
*/
    public String title;

    public String getTitle() {
        return title;
    }


}
