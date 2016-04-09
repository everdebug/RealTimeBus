package graduate.txy.com.realtimebus.MyView;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapView;
import com.lidroid.xutils.DbUtils;

import java.util.List;

import graduate.txy.com.realtimebus.R;
import graduate.txy.com.realtimebus.adapter.PassItemInfoAdapter;
import graduate.txy.com.realtimebus.domain.CollectionInfo;
import graduate.txy.com.realtimebus.domain.PassInfo;
import graduate.txy.com.realtimebus.globalApp.MyApplication;
import graduate.txy.com.realtimebus.utils.SharePreferenceUtils;
import graduate.txy.com.realtimebus.utils.TransitRouteOverlay;
import graduate.txy.com.realtimebus.utils.XutilsDataBaseUtils;

/**
 * 自定义Dialog，显示详细换乘信息和地图显示
 * <p/>
 * Created by lenovo on 2016/4/6.
 */
public class PassRouteDialog extends Dialog {

    private PassInfo mPassInfo;//换乘信息
    private MapView mMapView = null;    // 地图View
    private BaiduMap mBaidumap = null;
    private ImageButton collectionBtn;    //收藏按钮
    private TextView tvRouteName;       //线路名
    private TextView tvWalkLength;     //走的步数
    private ListView lvPass;    //换乘方案详情
    private Button returnButton;     //返回按钮
    boolean isHaveInDB = false;//是否已经收藏
    boolean useDefaultIcon = false;
    private Context mContext;
    private DbUtils mDB;
    private Activity mActivity;
    private static final String TAG = "PassRouteDialog";

    public PassRouteDialog(Context context, PassInfo passInfo,Activity activity) {
        super(context, R.style.MyDialogTheme);
        this.mContext = context;
        this.mPassInfo = passInfo;
        this.mActivity = activity;
        this.mDB = XutilsDataBaseUtils.createDB(mActivity);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        SDKInitializer.initialize(MyApplication.getInstance());
        setContentView(R.layout.dialog_normal_layout);
        setCancelable(true);


        //初始化各种Dialog控件
        mMapView = (MapView) findViewById(R.id.map);
        tvRouteName = (TextView) findViewById(R.id.tv_dialog_route_name);
        tvWalkLength = (TextView) findViewById(R.id.tv_dialog_walk_length);
        collectionBtn = (ImageButton) findViewById(R.id.ib_collection);
        lvPass = (ListView) findViewById(R.id.lv_pass_info);
        returnButton = (Button) findViewById(R.id.returnBtn);

        mBaidumap = mMapView.getMap();
        mBaidumap.clear();
        mMapView.showZoomControls(false);


        //显示换乘路线
        TransitRouteOverlay overlay = new MyTransitRouteOverlay(mBaidumap);
        mBaidumap.setOnMarkerClickListener(overlay);
        overlay.setData(mPassInfo.getResult().getRouteLines().get(0));
        overlay.addToMap();
        overlay.zoomToSpan();


        Log.i(TAG, mPassInfo.getRouteName());
        tvRouteName.setText(mPassInfo.getRouteName());
        tvWalkLength.setText("步行" + getWalkLength() + " 米");
        lvPass.setAdapter(new PassItemInfoAdapter(context, mPassInfo.getPassItemInfoList()));

        //判断显示是否已经在数据库中
        isHaveInDB = XutilsDataBaseUtils.getInfoExist(mDB, mPassInfo.getRouteName());
        updateStar();


        final String name = mPassInfo.getRouteName();
        final CollectionInfo ci = getInfo();
        //final PassInfo pi = mPassInfo;
        //IB收藏
        collectionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "点击收藏该路线");
                //收藏功能实现
                if (isHaveInDB) {
                    XutilsDataBaseUtils.deleteInfo(mDB, name);
                    Log.i(TAG, "删除条目成功");
                    Toast.makeText(mContext, "取消成功", Toast.LENGTH_SHORT).show();
                    isHaveInDB = false;
                    updateStar();
                } else {
                    XutilsDataBaseUtils.addInfo2DB(mDB, getInfo());
                    Toast.makeText(mContext, "添加成功", Toast.LENGTH_SHORT).show();
                    isHaveInDB = true;
                    updateStar();
                }

            }
        });

        //返回
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMapView.onDestroy();
                dismiss();
            }
        });


    }

    //获得收藏信息
    private CollectionInfo getInfo() {
        CollectionInfo cii = new CollectionInfo();
        cii.setCity(SharePreferenceUtils.getSPStringValue(mContext, "city", "北京"));
        cii.setStartStation(mPassInfo.getStartStation());
        cii.setEndStation(mPassInfo.getEndStation());
        cii.setRouteName(mPassInfo.getRouteName());
        return cii;
    }

    //更新收藏按钮
    private void updateStar() {
        if (isHaveInDB) {
            collectionBtn.setImageResource(R.drawable.collection_on);
        } else {
            collectionBtn.setImageResource(R.drawable.collection_off);
        }
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }

    /**
     * 得到步行的总长度
     *
     * @return 步行长度
     */
    private int getWalkLength() {
        Log.i(TAG, "getWalkLength--S");
        int length = 0;
        List<PassInfo.PassItemInfo> infos = mPassInfo.getPassItemInfoList();
        for (PassInfo.PassItemInfo info : infos) {
            if (info.getTransport().equals("WAKLING")) {
                length += info.getItemLength();
            }
        }
        Log.i(TAG, "getWalkLength--D" + length);
        return length;
    }

    /**
     * //TODO 写注释？
     */
    private class MyTransitRouteOverlay extends TransitRouteOverlay {

        public MyTransitRouteOverlay(BaiduMap baiduMap) {
            super(baiduMap);
        }

        @Override
        public BitmapDescriptor getStartMarker() {
            if (useDefaultIcon) {
                return BitmapDescriptorFactory.fromResource(R.drawable.icon_st);
            }
            return null;
        }

        @Override
        public BitmapDescriptor getTerminalMarker() {
            if (useDefaultIcon) {
                return BitmapDescriptorFactory.fromResource(R.drawable.icon_en);
            }
            return null;
        }
    }
}


