package graduate.txy.com.realtimebus.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.platform.comapi.map.B;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import graduate.txy.com.realtimebus.R;
import graduate.txy.com.realtimebus.domain.BusInfo;
import graduate.txy.com.realtimebus.domain.CollectionInfo;
import graduate.txy.com.realtimebus.globalApp.MyApplication;
import graduate.txy.com.realtimebus.utils.JsonUtils;

/**
 * 离线存入数据类
 */
public class ServerActivity extends Activity {

    public static final String TAG = "ServerActivity";
    public static final String PACKAGE_NAME = "graduate.txy.com.realtimebus";
    public static final String DB_PATH = "/sdcard/Android/data/" + PACKAGE_NAME;
    private static final int UPDATE_TIME = 5000;
    private static int LOCATION_COUTNS = 0;
    private LocationClient locationClient = null;
    private DbUtils dbUtils;
    private TextView tv2, tv4;
    private EditText ed6, ed8, ed10, ed12, ed14, ed16, ed18;
    private BusInfo bi;
    private Button ll_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SDKInitializer.initialize(MyApplication.getInstance());//注册SDK
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_getdata);
        init();
        initLocation();
    }

    private void initLocation() {
        locationClient = new LocationClient(this);
        //设置定位条件
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);        //是否打开GPS
        option.setCoorType("bd09ll");       //设置返回值的坐标类型。
        option.setPriority(LocationClientOption.NetWorkFirst);  //设置定位优先级
        option.setProdName("LocationDemo"); //设置产品线名称。强烈建议您使用自定义的产品线名称，方便我们以后为您提供更高效准确的定位服务。
        option.setScanSpan(UPDATE_TIME);    //设置定时定位的时间间隔。单位毫秒
        locationClient.setLocOption(option);

        //注册位置监听器
        locationClient.registerLocationListener(new BDLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation location) {
                // TODO Auto-generated method stub
                if (location == null) {
                    return;
                }

                bi.setLatitude(location.getLatitude());
                bi.setLongitude(location.getLongitude());
                //location.getRadius()
                tv2.setText(location.getLongitude() + "");
                tv4.setText(location.getLatitude() + "");

                StringBuffer sb = new StringBuffer(256);
                sb.append("Time : ");
                sb.append(location.getTime());
                sb.append("\nError code : ");
                sb.append(location.getLocType());
                sb.append("\nLatitude : ");
                sb.append(location.getLatitude());
                sb.append("\nLontitude : ");
                sb.append(location.getLongitude());
                sb.append("\nRadius : ");
                sb.append(location.getRadius());
                if (location.getLocType() == BDLocation.TypeGpsLocation) {
                    sb.append("\nSpeed : ");
                    sb.append(location.getSpeed());
                    sb.append("\nSatellite : ");
                    sb.append(location.getSatelliteNumber());
                } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
                    sb.append("\nAddress : ");
                    sb.append(location.getAddrStr());
                }
                LOCATION_COUTNS++;
                sb.append("\n检查位置更新次数：");
                sb.append(String.valueOf(LOCATION_COUTNS));
                Log.i(TAG, sb.toString());
            }

        });

        ll_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (locationClient == null) {
                    return;
                }
                if (locationClient.isStarted()) {
                    ll_btn.setText("获取经纬度");
                    locationClient.stop();
                } else {
                    ll_btn.setText("停止获取");
                    locationClient.start();
                    /*
                     *当所设的整数值大于等于1000（ms）时，定位SDK内部使用定时定位模式。
                     *调用requestLocation( )后，每隔设定的时间，定位SDK就会进行一次定位。
                     *如果定位SDK根据定位依据发现位置没有发生变化，就不会发起网络请求，
                     *返回上一次定位的结果；如果发现位置改变，就进行网络请求进行定位，得到新的定位结果。
                     *定时定位时，调用一次requestLocation，会定时监听到定位结果。
                     */
                    locationClient.requestLocation();
                }
            }
        });
    }

    private void init() {
        bi = new BusInfo();
        tv2 = (TextView) findViewById(R.id.tv2);
        tv4 = (TextView) findViewById(R.id.tv2);


        ed6 = (EditText) findViewById(R.id.tv6);//route
        ed8 = (EditText) findViewById(R.id.tv8);//busname
        ed10 = (EditText) findViewById(R.id.tv10);//dis
        ed12 = (EditText) findViewById(R.id.tv12);//direction
        ed14 = (EditText) findViewById(R.id.tv14);//time
        ed16 = (EditText) findViewById(R.id.tv16);//next
        ed18 = (EditText) findViewById(R.id.tv18);//last

        ll_btn = (Button) findViewById(R.id.ll_btn);
        dbUtils = DbUtils.create(this, DB_PATH, "businfos.db");
        dbUtils.getDatabase();
    }

    String time;
    String busname;
    String route;
    String direction;
    String dis;
    String next;
    String last;

    public void savedb(View view) {
        if (!checkNull()) {
            bi.setArrive_time(Integer.parseInt(time));
            bi.setBus_name(busname);
            bi.setBus_route(route);
            bi.setDirection(Integer.parseInt(direction));
            bi.setDistance(Integer.parseInt(dis));
            bi.setNext_station(next);
            bi.setLast_station(last);
            try {
                dbUtils.save(bi);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public boolean checkNull() {
        route = ed6.getText().toString();
        busname = ed8.getText().toString();
        dis = ed10.getText().toString();
        direction = ed12.getText().toString();
        time = ed14.getText().toString();
        next = ed16.getText().toString();
        last = ed18.getText().toString();
        if (route.equals("") | busname.equals("") | dis.equals("") | direction.equals("") | time.equals("") | next.equals("") | last.equals("")) {
            Toast.makeText(this, "请输入正确信息", Toast.LENGTH_SHORT).show();
            return true;
        } else {
            return false;
        }
    }

    private static final String URL = "";

    public void upload(View view) {
        if (!checkNull()) {
            //TODO URL数据处理
            HttpUtils http = new HttpUtils();
            http.send(HttpRequest.HttpMethod.GET, URL,
                    new RequestCallBack<String>() {
                        @Override
                        public void onLoading(long total, long current, boolean isUploading) {
                            // Log.i(TAG,"开始请求");
                        }

                        @Override
                        public void onSuccess(ResponseInfo<String> responseInfo) {

                        }

                        @Override
                        public void onStart() {
                            Log.i(TAG, "开始请求");
                        }

                        @Override
                        public void onFailure(HttpException error, String msg) {
                            Toast.makeText(ServerActivity.this, "请求失败，请重试或修改", Toast.LENGTH_SHORT).show();
                            Log.i(TAG, msg);
                        }
                    });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (locationClient != null && locationClient.isStarted()) {
            locationClient.stop();
            locationClient = null;
        }
    }
}
