package graduate.txy.com.realtimebus.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.ArrayList;
import java.util.List;

import graduate.txy.com.realtimebus.MyView.EditableSpinner;
import graduate.txy.com.realtimebus.R;
import graduate.txy.com.realtimebus.domain.BusInfo;
import graduate.txy.com.realtimebus.globalApp.MyApplication;
import graduate.txy.com.realtimebus.utils.JsonUtils;
import graduate.txy.com.realtimebus.utils.SharePreferenceUtils;

/**
 * 实时查询Fragment
 * Created by lenovo on 2016/3/17.
 */
public class SelectFragment extends BaseFragment {

    //TODO 确定URL
    private static final String URL = "http://192.168.1.109:8080/RTBServer/servlet/ReceiveInfoServlet?route=";
    private static final String TAG = "SelectFragment";

    private Activity mActivity;
    private String mCity;
    private Button mBtn;
    private EditableSpinner route;
    private EditableSpinner direction;
    private EditableSpinner station;
    private ImageButton route_ib;
    private ImageButton direction_ib;
    private ImageButton station_ib;
    private List<BusInfo> infos;
    private DbUtils mdb;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.select_fragment, container, false);
        init(view);
        initListener();
        return view;
    }

    private void init(View view) {
        mBtn = (Button) view.findViewById(R.id.btn_real_time_select);
        route = (EditableSpinner) view.findViewById(R.id.route_spinner);
        direction = (EditableSpinner) view.findViewById(R.id.direction_spinner);
        station = (EditableSpinner) view.findViewById(R.id.station_spinner);

        route_ib = route.getImageButton();
        direction_ib = direction.getImageButton();
        station_ib = station.getImageButton();

        mdb = DbUtils.create(mActivity, "/sdcard/Android/data/com.txy.mobliesafe/files/", "busdb.db");
        mdb.getDatabase();

        direction.setEnabled(false);
        station.setEnabled(false);
        infos = new ArrayList<BusInfo>();
    }

    private void initListener() {
        route_ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //查询DB
            }
        });
        direction_ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //查询DB
            }
        });
        station_ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //查询DB
            }
        });


        direction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (route.getEditText().getText().toString().equals("")) {
                    Toast.makeText(mActivity, "请输入路线", Toast.LENGTH_SHORT).show();
                    return;
                }
                direction.setEnabled(true);
            }
        });

        station.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (route.getEditText().getText().toString().equals("") || direction.getEditText().getText().toString().equals("")) {
                    Toast.makeText(mActivity, "请输入路线和方向", Toast.LENGTH_SHORT).show();
                    return;
                }
                station.setEnabled(true);
            }
        });


        mBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String routeStr = route.getEditText().getText().toString();
                String directionStr = direction.getEditText().getText().toString();
                String stationStr = station.getEditText().getText().toString();

                if (routeStr.equals("") || directionStr.equals("") || stationStr.equals("")) {
                    Toast.makeText(mActivity, "请输入完整的信息", Toast.LENGTH_SHORT).show();
                }


                Log.i(TAG,"GET_URL : "+URL+routeStr);
                HttpUtils http = new HttpUtils();
                http.send(HttpRequest.HttpMethod.GET, URL+routeStr,
                        new RequestCallBack<String>() {
                            @Override
                            public void onLoading(long total, long current, boolean isUploading) {
                                // Log.i(TAG,"开始请求");
                            }

                            @Override
                            public void onSuccess(ResponseInfo<String> responseInfo) {
                                Log.i(TAG, "返回的json" + responseInfo.result.toString());
                                infos = JsonUtils.praseJsonL(responseInfo.result.toString());
                                //TODO数据加载完成，进行数据处理
                            }

                            @Override
                            public void onStart() {
                                Log.i(TAG, "开始请求");
                            }

                            @Override
                            public void onFailure(HttpException error, String msg) {
                                Toast.makeText(mActivity,"请求失败，请重试或修改", Toast.LENGTH_SHORT).show();
                                Log.i(TAG, msg);
                            }
                        });
            }
        });

    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = getActivity();
        mCity = SharePreferenceUtils.getSPStringValue(MyApplication.getInstance(), "city", "北京");
    }

    public SelectFragment() {
        title = "实时查询";
    }
}
