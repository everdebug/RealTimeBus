package graduate.txy.com.realtimebus.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import graduate.txy.com.realtimebus.domain.BusInfo;

/**
 * 使用Gson对相关json数据的解析与封装
 */
public class JsonUtils {

    /**
     * 解析json数据
     *
     * @param result 返回的json
     * @return 返回BusInfo
     */
    public static BusInfo praseJson(String result) {
        Gson gson = new Gson();
        BusInfo businfo = gson.fromJson(result, BusInfo.class);
        return businfo;
    }

    /**
     * 对象转化成json
     *
     * @param busInfos 公交信息列表对象
     * @return json
     */
    public static String getJson(List<BusInfo> busInfos) {
        Gson gson = new Gson();
        String json = gson.toJson(busInfos);
        return json;
    }

    /**
     * 解析json数据变成List对象
     * @param result json
     * @return  List对象
     */
    public static List<BusInfo> praseJsonL(String result) {
        List<BusInfo> busInfoList = new ArrayList<BusInfo>();
        Gson gson = new Gson();
        busInfoList = gson.fromJson(result,new TypeToken<List<BusInfo>>(){}.getType());
        return  busInfoList;
    }


}