package graduate.txy.com.realtimebus.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;

import graduate.txy.com.realtimebus.globalApp.MyApplication;

/**
 * Created by lenovo on 2016/3/17.
 */
public class BaseFragment extends Fragment {


    private Activity activity;

    public Context getContext() {
        if (activity == null) {
            return MyApplication.getInstance();
        }
        return activity;
    }

    public String title;

    public String getTitle() {
        return title;
    }


}
