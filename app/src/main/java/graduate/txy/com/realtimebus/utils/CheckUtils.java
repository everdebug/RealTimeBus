package graduate.txy.com.realtimebus.utils;

import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * 进行一些连接检测
 *
 * Created by lenovo on 2016/3/18.
 */
public class CheckUtils {

    /**
     * 检测网络是否连接
     *
     * @return
     */
    public static boolean isNetConnected(ConnectivityManager cm) {
        if (cm != null) {
            NetworkInfo[] infos = cm.getAllNetworkInfo();
            if (infos != null) {
                for (NetworkInfo ni : infos) {
                    if (ni.isConnected()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 检测wifi是否连接
     *
     * @return
     */
    public static boolean isWifiConnected(ConnectivityManager cm) {
        if (cm != null) {
            NetworkInfo networkInfo = cm.getActiveNetworkInfo();
            if (networkInfo != null
                    && networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                return true;
            }
        }
        return false;
    }

    /**
     * 检测GPS是否打开
     *
     * @return
     */
    public static boolean isGpsEnabled(LocationManager lm) {


        if (lm.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)
                || lm.isProviderEnabled(android.location.LocationManager.NETWORK_PROVIDER)
                ) {
            return true;
        }
        return false;
    }
}
