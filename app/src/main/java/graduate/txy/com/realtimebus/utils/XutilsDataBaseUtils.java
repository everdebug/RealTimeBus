package graduate.txy.com.realtimebus.utils;

import android.app.Activity;
import android.util.Log;

import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.db.sqlite.WhereBuilder;
import com.lidroid.xutils.exception.DbException;

import java.util.ArrayList;
import java.util.List;

import graduate.txy.com.realtimebus.domain.CollectionInfo;

/**
 * 根据Xutils写的数据库工具类
 * Created by lenovo on 2016/4/8.
 */
public class XutilsDataBaseUtils {

    public static final String TAG = "XutilsDataBaseUtils";

    public static final String PACKAGE_NAME = "graduate.txy.com.realtimebus";
    public static final String DB_PATH = "/sdcard/Android/data/" + PACKAGE_NAME;

    //创建/获取数据库
    public static DbUtils createDB(Activity activity) {
        DbUtils db = DbUtils.create(activity, DB_PATH, "collection.db");
        db.getDatabase();
        return db;
    }

    /**
     * 添加到数据库
     *
     * @param db
     * @param info
     */
    public static void addInfo2DB(DbUtils db, CollectionInfo info) {
        try {
            if (getInfoExist(db, info.getRouteName())) {
                return;
            }
            db.save(info);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    /**
     * 某一个线路是否存在
     *
     * @param db        数据库
     * @param routeName 要查找的路线
     * @return 返回是否存在
     */
    public static boolean getInfoExist(DbUtils db, String routeName) {
        boolean flag = false;
        CollectionInfo info = null;
        try {
            info = db.findFirst(Selector.from(CollectionInfo.class).where("routeName", "=", routeName));
        } catch (DbException e) {
            e.printStackTrace();
        }
        if (info != null) {
            flag = true;
        }
        return flag;
    }

    /**
     * 得到全部数据
     *
     * @param db
     * @return
     */
    public static List<CollectionInfo> getCollectionInfoList(DbUtils db) {
        List<CollectionInfo> list = new ArrayList<CollectionInfo>();
        try {
            list = db.findAll(Selector.from(CollectionInfo.class));
        } catch (DbException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 删除一条数据
     *
     * @param db        数据库
     * @param routeName 选择删除的名字
     */
    public static void deleteInfo(DbUtils db, String routeName) {
        try {
            Log.i(TAG, routeName + "-->DELETE");
            //db.deleteById(CollectionInfo.class, WhereBuilder.b("routeName", "=", routeName));
            CollectionInfo info = null;
            info = db.findFirst(Selector.from(CollectionInfo.class).where("routeName", "=", routeName));
            db.delete(info);
            Log.i(TAG, getInfoExist(db, routeName) + "");
        } catch (DbException e) {
            e.printStackTrace();
        }
    }
}
