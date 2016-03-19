package graduate.txy.com.realtimebus.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 *
 *  一些关于数据库的工具
 * Created by lenovo on 2016/3/19.
 */
public class DBUtils {
public  static final String TAG = "DBUtils";
    /**
     * 加载数据库
     *
     * @param path
     */
    public void CopyDB(Context context,String path) {
        try { // 创建文件
            File file = new File(context.getFilesDir(), path);
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
}
