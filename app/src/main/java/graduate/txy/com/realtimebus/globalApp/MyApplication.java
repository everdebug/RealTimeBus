package graduate.txy.com.realtimebus.globalApp;

import android.app.Application;

import graduate.txy.com.realtimebus.db.DBManager;

/**
 *
 * 全局Application
 * Created by lenovo on 2016/3/20.
 */

public class MyApplication extends Application {
    private static MyApplication instance =null;
    private DBManager dbHelper =null;
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        //导入数据库
        dbHelper = new DBManager(this);
        dbHelper.openDatabase();
    }

    public static MyApplication getInstance() {
        // 因为我们程序运行后，Application是首先初始化的，如果在这里不用判断instance是否为空
        return instance;
    }
}
