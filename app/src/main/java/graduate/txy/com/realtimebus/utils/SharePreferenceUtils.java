package graduate.txy.com.realtimebus.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 通过使用SharedPreferences 管理一些设置参数 工具
 * <p/>
 * Created by lenovo on 2015/8/25.
 */
public class SharePreferenceUtils {
    private static final String SHAREP_NAME = "config";

    /**
     * 取出config的值
     *
     * @param context 上下文
     * @param key     保存的key
     * @param Default 默认
     * @return
     */

    public static boolean getSPvalue(Context context, String key, boolean Default) {
        SharedPreferences sp = context.getSharedPreferences(SHAREP_NAME, Context.MODE_PRIVATE);
        boolean b = sp.getBoolean(key, Default);
        return b;
    }

    /**
     * 设置值
     *
     * @param context 上下文
     * @param key     设置的Key
     * @param value   设置的Boolean
     */
    public static void setSPValue(Context context, String key, boolean value) {
        SharedPreferences sp = context.getSharedPreferences(SHAREP_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putBoolean(key, value);
        edit.commit();
    }

    /**
     * 设置String值
     *
     * @param context
     * @param key
     * @param value
     */
    public static void setSPStringValue(Context context, String key, String value) {
        SharedPreferences sp = context.getSharedPreferences(SHAREP_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putString(key, value);
        edit.commit();
    }


    /**
     * 得到String值
     *
     * @param context
     * @param key
     * @param Default
     * @return
     */
    public static String getSPStringValue(Context context, String key, String Default) {
        SharedPreferences sp = context.getSharedPreferences(SHAREP_NAME, Context.MODE_PRIVATE);
        String b = sp.getString(key, Default);
        return b;
    }


}
