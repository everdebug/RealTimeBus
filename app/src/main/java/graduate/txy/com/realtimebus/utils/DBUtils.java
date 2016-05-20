package graduate.txy.com.realtimebus.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import graduate.txy.com.realtimebus.db.DBManager;

/**
 * 一些关于数据库的工具
 * Created by lenovo on 2016/3/19.
 */
public class DBUtils {
    public static final String TAG = "DBUtils";
    public static final String PATH = "/sdcard/Android/data/graduate.txy.com.realtimebus/files/";
    public static final String DBNAME = "busdb.db";

    /**
     * 加载数据库
     *
     * @param path
     */
    public static void CopyDB(Context context, String path) {
        try { // 创建文件
            File file = new File(PATH + path);
            Log.i(TAG, "length" + file.length());
            Log.i(TAG, "path" + file.getPath());
            if (file.exists() && file.length() > 0) {
                Log.i(TAG, "数据库已经加载");
            } else {
                // 拿到文件流
                AssetManager am = context.getAssets();
                InputStream is = am.open(path);// 打开assets资源文件夹
                FileOutputStream fos = new FileOutputStream(file);
                // 模板代码
                int len = 0;
                byte[] buffer = new byte[1024];
                while ((len = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, len);
                }
                is.close();
                fos.close();
                Log.i(TAG, "DB加载完成！");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //从小到大为正向1   取大于0的，相反为逆向0，去小于0的 并且显示绝对值
    //绝对站数
    public static int getAbsoluteStationNum(String route, String nextStation, String targetStation) {
        Log.i(TAG, "nextStation" + nextStation);
        Log.i(TAG, "targetStation" + targetStation);
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(PATH + DBNAME, null);
        Cursor cursor = db.rawQuery("select Ino from L" + route + " where IstopName = \"" + nextStation + "\"", null);
        int next = 0;
        while (cursor.moveToNext()) {
            next = cursor.getInt(cursor.getColumnIndex("Ino"));
        }
        cursor = db.rawQuery("select Ino from L" + route + " where IstopName = \"" + targetStation + "\"", null);
        int target = 0;
        while (cursor.moveToNext()) {
            target = cursor.getInt(cursor.getColumnIndex("Ino"));

        }
        int derlt = target - next;
        Log.i(TAG, derlt + "-derlt");

        cursor.close();
        db.close();
        return derlt;
    }

    public static boolean getIsExist(String route, String station) {
        boolean flag = false;
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(PATH + DBNAME, null);
        Cursor cursor = db.rawQuery("select Ino from L" + route + " where IstopName = \"" + station + "\"", null);
        if (cursor.getCount() != 0) flag = true;
        cursor.close();
        db.close();
        return flag;
    }


}
