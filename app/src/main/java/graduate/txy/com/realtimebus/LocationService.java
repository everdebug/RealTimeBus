package graduate.txy.com.realtimebus;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;


/**
 * 实时获取服务
 * Created by lenovo on 2016/4/10.
 */
public class LocationService extends Service {

    private volatile int discard = 1;   //Volatile修饰的成员变量在每次被线程访问时，都强迫从共享内存中重读该成员变量的值。
    private Object lock = new Object();

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public LocationService() {
        super();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
