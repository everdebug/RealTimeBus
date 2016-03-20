package graduate.txy.com.realtimebus.fragment;

import android.app.Fragment;
import android.content.Context;

import graduate.txy.com.realtimebus.globalApp.MyApplication;

/**
 * Created by lenovo on 2016/3/17.
 */
public class BaseFragment extends Fragment {
    public String title;

    public String getTitle() {
        return title;
    }

    public Context getContext() {
        return MyApplication.getInstance();
    }


}
