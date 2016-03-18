package graduate.txy.com.realtimebus.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import graduate.txy.com.realtimebus.R;

/**
 * Created by lenovo on 2016/3/17.
 */
public class PassFragment extends BaseFragment {


    public PassFragment() {
        title = "换乘查询";
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.pass_fragment, container, false);
        return view;
    }


}
