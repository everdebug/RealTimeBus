package graduate.txy.com.realtimebus.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.core.TaxiInfo;
import com.baidu.mapapi.search.core.VehicleInfo;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteLine;
import com.baidu.mapapi.search.route.TransitRoutePlanOption;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteResult;

import java.util.List;

import graduate.txy.com.realtimebus.R;
import graduate.txy.com.realtimebus.globalApp.MyApplication;

/**
 * Created by lenovo on 2016/3/17.
 */
public class PassFragment extends BaseFragment {


    public PassFragment() {
        title = "换乘查询";
    }

    private static final String TAG = "MyMapActivity";

    private RoutePlanSearch mSearch = null;
    private OnGetRoutePlanResultListener listener = null;
    private List<TransitRouteLine> transitRouteLineList = null;

    private EditText et_start;
    private EditText et_end;
    private Button bt_convert;
    private Button bt_select;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        SDKInitializer.initialize(MyApplication.getInstance());
        view = inflater.inflate(R.layout.pass_fragment, container, false);
        init();
        return view;
    }

    /*
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
            //SDKInitializer.initialize(getApplicationContext());
            SDKInitializer.initialize(MyApplication.getInstance());
            setContentView(R.layout.activity_my);
            init();
        }
    */
    //初始化
    private void init() {
        et_start = (EditText) view.findViewById(R.id.et_start);
        et_end = (EditText) view.findViewById(R.id.et_end);
        bt_convert = (Button) view.findViewById(R.id.bt_convert);
        bt_select = (Button) view.findViewById(R.id.bt_select);


        bt_convert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String start = et_end.getText().toString().trim();
                String end = et_start.getText().toString().trim();
                et_start.setText(start);
                et_end.setText(end);
                Log.i(TAG, "convert");

            }
        });
        bt_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String start = et_start.getText().toString().trim();
                String end = et_end.getText().toString().trim();
                Log.i(TAG, start + "--" + end);
                //TODO 获取城市位置
                selectRoute(start, end, "北京");


            }
        });

        mSearch = RoutePlanSearch.newInstance();
        initListener();
        mSearch.setOnGetRoutePlanResultListener(listener);
        Log.i(TAG, "监听器添加");
        bt_select.setEnabled(true);
    }

    /**
     * 查询公交换乘
     *
     * @param start 起点
     * @param end   终点
     * @param city  城市
     */
    public void selectRoute(String start, String end, String city) {
        Log.i(TAG, "select");
        PlanNode stNode = PlanNode.withCityNameAndPlaceName(city, start);
        PlanNode enNode = PlanNode.withCityNameAndPlaceName(city, end);
        //TransitRoutePlanOption可以进行策略选择
        boolean a = mSearch.transitSearch((new TransitRoutePlanOption())
                .from(stNode).policy(TransitRoutePlanOption.TransitPolicy.EBUS_WALK_FIRST)
                .city(city)
                .to(enNode));
        Log.i(TAG, String.valueOf(a));
    }

    public void initListener() {
        Log.i(TAG, "initListener");
        listener = new OnGetRoutePlanResultListener() {
            @Override
            public void onGetWalkingRouteResult(WalkingRouteResult walkingRouteResult) {
            }

            @Override
            public void onGetTransitRouteResult(TransitRouteResult result) {
                transitRouteLineList = result.getRouteLines();
                if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                    Log.i(TAG, "抱歉，未找到结果");
                    Toast.makeText(MyApplication.getInstance(), "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
                }
                if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
                    // 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
                    com.baidu.mapapi.search.route.SuggestAddrInfo si = result.getSuggestAddrInfo();
                    Log.i(TAG, "SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR");
                    return;
                }
                if (result.error == SearchResult.ERRORNO.NO_ERROR) {
                    TaxiInfo ti = result.getTaxiInfo();
                    Log.i(TAG, ti.getDesc() + "描述" + ti.getTotalPrice() + "总价格");
                    for (TransitRouteLine tr : transitRouteLineList) {//排序查询条件
                        Log.i(TAG, "---------------------------------------------------------------------------");
                        List<TransitRouteLine.TransitStep> stepList = tr.getAllStep();
                        Log.i(TAG, "-------------全程距离：" + tr.getDistance() + "----全称时间：" + tr.getDuration() + "-----------");//时间是秒，要转换成分钟，距离是米，要换算成公里
                        for (TransitRouteLine.TransitStep ts : stepList) {//计算换乘次数
                            VehicleInfo vi = ts.getVehicleInfo();
                            if (vi != null) {
                                Log.i(TAG, "经过站数：" + vi.getPassStationNum() + "----路线名称：" + vi.getTitle());
                            }
                            //获取交通工具信息
                            Log.i(TAG, "交通工具：" + ts.getInstructions() + "---------路段类型：" + ts.getStepType());
                            //distance
                            Log.i(TAG, "1:" + "distance" + ts.getDistance() + "-----duration : " + ts.getDuration());
                        }

                    }

                }

            }


            @Override
            public void onGetDrivingRouteResult(DrivingRouteResult drivingRouteResult) {
            }

            @Override
            public void onGetBikingRouteResult(BikingRouteResult bikingRouteResult) {
            }
        };
    }

    @Override
    public void onDestroyView() {
        mSearch.destroy();
        super.onDestroyView();
    }

/*
    protected void onDestroy() {
        mSearch.destroy();
        super.onDestroy();
    }

*/
}
