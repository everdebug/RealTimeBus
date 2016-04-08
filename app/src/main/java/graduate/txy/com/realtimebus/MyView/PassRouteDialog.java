package graduate.txy.com.realtimebus.MyView;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapView;

import java.util.List;

import graduate.txy.com.realtimebus.R;
import graduate.txy.com.realtimebus.adapter.PassItemInfoAdapter;
import graduate.txy.com.realtimebus.domain.PassInfo;
import graduate.txy.com.realtimebus.globalApp.MyApplication;
import graduate.txy.com.realtimebus.utils.TransitRouteOverlay;

/**
 * 自定义Dialog，显示详细换乘信息和地图显示
 * <p/>
 * Created by lenovo on 2016/4/6.
 */
public class PassRouteDialog extends Dialog {

    private PassInfo passInfo;

    //private View contentView;
    private MapView mMapView = null;    // 地图View
    private BaiduMap mBaidumap = null;
    private ImageButton collectionBtn;    //收藏按钮
    private TextView tvRouteName;       //线路名
    private TextView tvWalkLength;     //走的步数
    private ListView lvPass;    //换乘方案详情
    private Button returnButton;     //返回按钮

    boolean useDefaultIcon = false;

    private static final String TAG = "PassRouteDialog";

    public PassRouteDialog(Context context, PassInfo passInfo) {
        super(context);
        this.passInfo = passInfo;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        SDKInitializer.initialize(MyApplication.getInstance());
        setContentView(R.layout.dialog_normal_layout);

        ContextThemeWrapper ctw = new ContextThemeWrapper(context, R.style.Base_Theme_AppCompat_Light_Dialog);

        //初始化各种Dialog控件
        mMapView = (MapView) findViewById(R.id.map);
        tvRouteName = (TextView) findViewById(R.id.tv_dialog_route_name);
        tvWalkLength = (TextView) findViewById(R.id.tv_dialog_walk_length);
        collectionBtn = (ImageButton) findViewById(R.id.ib_collection);
        lvPass = (ListView) findViewById(R.id.lv_pass_info);
        returnButton = (Button) findViewById(R.id.returnBtn);

        mBaidumap = mMapView.getMap();
        mBaidumap.clear();

        //显示换乘路线
        TransitRouteOverlay overlay = new MyTransitRouteOverlay(mBaidumap);
        mBaidumap.setOnMarkerClickListener(overlay);
        overlay.setData(passInfo.getResult().getRouteLines().get(0));

        Log.i(TAG, passInfo.getRouteName());
        tvRouteName.setText(passInfo.getRouteName());
        tvWalkLength.setText(getWalkLength() + "");
        lvPass.setAdapter(new PassItemInfoAdapter(context,passInfo.getPassItemInfoList()));

        //IB收藏
        collectionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "点击收藏该路线");
                //TODO 收藏路线到数据库？
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

    @Override
    public void dismiss() {
        super.dismiss();
    }

    /**
     * 得到步行的总长度
     * @return 步行长度
     */
    private int getWalkLength() {
        Log.i(TAG, "getWalkLength--S");
        int length = 0;
        List<PassInfo.PassItemInfo> infos = passInfo.getPassItemInfoList();
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


