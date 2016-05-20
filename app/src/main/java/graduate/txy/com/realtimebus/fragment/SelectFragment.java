package graduate.txy.com.realtimebus.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.map.Text;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import graduate.txy.com.realtimebus.MyView.EditableSpinner;
import graduate.txy.com.realtimebus.R;
import graduate.txy.com.realtimebus.domain.BusInfo;
import graduate.txy.com.realtimebus.globalApp.MyApplication;
import graduate.txy.com.realtimebus.utils.DBUtils;
import graduate.txy.com.realtimebus.utils.JsonUtils;
import graduate.txy.com.realtimebus.utils.SharePreferenceUtils;

/**
 * 实时查询Fragment
 * Created by lenovo on 2016/3/17.
 */
public class SelectFragment extends BaseFragment {

    private static final String URL = "http://192.168.1.113:8080/RTBServer/servlet/ReceiveInfoServlet?route=";
    private static final String URL1 = "&direction=";
    private static final String TAG = "SelectFragment";

    private Activity mActivity;
    private String mCity;
    private Button mBtn;
    private Button mBtn_start;
    private Button mBtn_stop;
    private EditableSpinner route;
    private EditableSpinner direction;
    private EditableSpinner station;
    private ImageButton route_ib;
    private ImageButton direction_ib;
    private ImageButton station_ib;
    private List<BusInfo> infos;
    private DbUtils mdb;
    private ListView lv_buslist;
    private BusInfoAdapter bifa;
    private String stationStr;
    private String directionStr;
    private String routeStr;
    private Timer timer;
    private TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
            Log.i(TAG, "运行定时任务");
            Log.i(TAG, SystemClock.currentThreadTimeMillis() + "");
            refresh();
        }
    };
    private Handler mhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                Toast.makeText(getActivity(), "暂时还没有到这个站点的公交哦~", Toast.LENGTH_SHORT).show();
                bifa.notifyDataSetChanged();
                mBtn_start.setVisibility(View.INVISIBLE);
                mBtn_stop.setVisibility(View.INVISIBLE);
                return;
            }
            infos = (List<BusInfo>) msg.obj;
            bifa.notifyDataSetChanged();

            mBtn_start.setVisibility(View.VISIBLE);
            mBtn_stop.setVisibility(View.VISIBLE);
        }
    };
    private boolean flag = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.select_fragment, container, false);
        init(view);
        initListener();
        return view;
    }

    private void init(View view) {
        mBtn = (Button) view.findViewById(R.id.btn_real_time_select);
        mBtn_start = (Button) view.findViewById(R.id.mBtn_start);
        mBtn_stop = (Button) view.findViewById(R.id.mBtn_stop);
        route = (EditableSpinner) view.findViewById(R.id.route_spinner);
        direction = (EditableSpinner) view.findViewById(R.id.direction_spinner);
        station = (EditableSpinner) view.findViewById(R.id.station_spinner);
        lv_buslist = (ListView) view.findViewById(R.id.lv_buslist);
        route_ib = route.getImageButton();
        direction_ib = direction.getImageButton();
        station_ib = station.getImageButton();
        //tv_businfo = (TextView)view.findViewById(R.id.tv_businfo);

        mdb = DbUtils.create(mActivity, "/sdcard/Android/data/com.txy.mobliesafe/files/", "busdb.db");
        mdb.getDatabase();
        timer = new Timer(true);
        direction.setEnabled(false);
        station.setEnabled(false);
        infos = new ArrayList<BusInfo>();
        bifa = new BusInfoAdapter();
        lv_buslist.setAdapter(bifa);

        lv_buslist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
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
                routeStr = route.getEditText().getText().toString();
                directionStr = direction.getEditText().getText().toString();
                stationStr = station.getEditText().getText().toString();


                if (routeStr.equals("") || directionStr.equals("") || stationStr.equals("")) {
                    Toast.makeText(mActivity, "请输入完整的信息", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!DBUtils.getIsExist(routeStr, stationStr)) {
                    Toast.makeText(getActivity(), "请输入正确的站点名", Toast.LENGTH_SHORT).show();
                    return;
                }
                refresh();
            }
        });


        //开始刷新按钮
        mBtn_start.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.i(TAG, "Start");
                timer.schedule(timerTask, 5000, 5000);
                mBtn.setVisibility(View.INVISIBLE);
            }
        });
//停止刷新按钮
        mBtn_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "stop");
                timer.cancel();
                mBtn.setVisibility(View.VISIBLE);
                mBtn_start.setVisibility(View.INVISIBLE);
                mBtn_stop.setVisibility(View.INVISIBLE);
            }
        });

    }

    public void refresh() {
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.GET, URL + routeStr + URL1 + directionStr,
                new RequestCallBack<String>() {
                    @Override
                    public void onLoading(long total, long current, boolean isUploading) {
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        infos.clear();
                        Log.i(TAG, "返回的json" + responseInfo.result.toString());
                        List<BusInfo> infoss = new ArrayList<BusInfo>();
                        infoss = JsonUtils.praseJsonL(responseInfo.result.toString());
                        int dirctionn = Integer.parseInt(directionStr);
                        Log.i(TAG, "方向" + dirctionn);
                        for (BusInfo infoo : infoss) {
                            int num = DBUtils.getAbsoluteStationNum(infoo.getBus_route(), infoo.getNext_station(), stationStr);
                            if (num > 0 && dirctionn == 1) {
                                infos.add(infoo);
                            } else if (num < 0 && dirctionn == 0) {
                                infos.add(infoo);
                            }
                        }
                        for (BusInfo infooo : infos) {
                            Log.i(TAG, infooo.toString());
                        }

                        if (infos.size() == 0) {
                            Log.i(TAG, "没有响应的数据，请重试");
                            Message msg = mhandler.obtainMessage();
                            msg.what = 1;
                            mhandler.sendMessage(msg);
                            return;
                        }

                        Message msg = mhandler.obtainMessage();
                        msg.obj = infos;
                        mhandler.sendMessage(msg);
                        //适配器更新
                        // bifa.notifyDataSetChanged();
                    }

                    @Override
                    public void onStart() {
                        Log.i(TAG, "开始请求");
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        Toast.makeText(mActivity, "请求失败，请重试或修改", Toast.LENGTH_SHORT).show();
                        Log.i(TAG, msg);
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

    class BusInfoAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return infos.size();
        }

        @Override
        public Object getItem(int position) {
            return infos.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view;
            if (convertView != null) {
                view = convertView;
            } else {
                view = View.inflate(mActivity, R.layout.busitem, null);
            }
            BusInfo info = infos.get(position);

            int dirctionn = Integer.parseInt(directionStr);
            int num = DBUtils.getAbsoluteStationNum(info.getBus_route(), info.getNext_station(), stationStr);
            if (num < 0 && dirctionn == 0) {
                num = -num;
            }
            TextView tv_busname = (TextView) view.findViewById(R.id.tv_busname);
            TextView tv_nextstop = (TextView) view.findViewById(R.id.tv_nextstop);
            TextView tv_bustime = (TextView) view.findViewById(R.id.tv_bus_time);
            TextView tv_diatance = (TextView) view.findViewById(R.id.tv_diatance);
            TextView tv_stopnum = (TextView) view.findViewById(R.id.tv_stopnum);

            BusInfo binfo = infos.get(position);
            Log.i(TAG, binfo.toString());

            tv_busname.setText(binfo.getBus_name());
            tv_nextstop.setText(binfo.getNext_station());
            tv_bustime.setText(binfo.getArrive_time()/60+"min");
            tv_diatance.setText(binfo.getDistance()+"km");
            tv_stopnum.setText(num + "");

            return view;
        }
    }
}
