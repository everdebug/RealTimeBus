package graduate.txy.com.realtimebus.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import graduate.txy.com.realtimebus.R;

/**
 * Created by lenovo on 2016/3/17.
 */
public class SelectFragment extends BaseFragment {

    public SelectFragment() {
        title = "实时查询";
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.select_fragment, container, false);
        return view;
    }


}
