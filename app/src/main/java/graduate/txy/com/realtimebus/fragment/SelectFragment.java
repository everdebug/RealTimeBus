package graduate.txy.com.realtimebus.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import graduate.txy.com.realtimebus.R;
import graduate.txy.com.realtimebus.globalApp.MyApplication;
import graduate.txy.com.realtimebus.utils.SharePreferenceUtils;

/**
 * 实时查询Fragment
 * Created by lenovo on 2016/3/17.
 */
public class SelectFragment extends BaseFragment {


    private Activity mActivity;
    private String mCity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.select_fragment, container, false);
        return view;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = getActivity();
        mCity = SharePreferenceUtils.getSPStringValue(MyApplication.getInstance(),"city","北京");
    }

    public SelectFragment() {
        title = "实时查询";
    }
}
