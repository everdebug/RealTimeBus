package graduate.txy.com.realtimebus.domain;

import com.baidu.mapapi.search.route.TransitRouteResult;

import java.util.List;

/**
 * 换乘信息
 * Created by lenovo on 2016/3/27.
 */
public class PassInfo {
    int totalLength;
    double totalTime;
    String routeName;
    int totalStationNum;
    List<PassItemInfo> passItemInfoList;
    TransitRouteResult result;//用于显示路线

    public class PassItemInfo {
        int itemLength;
        String transport;
        String passMethod;
        int stationNum;

        public int getItemLength() {
            return itemLength;
        }

        public void setItemLength(int itemLength) {
            this.itemLength = itemLength;
        }

        public void setPassMethod(String passMethod) {
            this.passMethod = passMethod;
        }

        public void setStationNum(int stationNum) {
            this.stationNum = stationNum;
        }

        public void setTransport(String transport) {
            this.transport = transport;
        }

        public String getPassMethod() {
            return passMethod;
        }

        public int getStationNum() {
            return stationNum;
        }

        public String getTransport() {
            return transport;
        }

        @Override
        public String toString() {
            return "PassItemInfo{" +
                    "itemLength=" + itemLength +
                    ", transport='" + transport + '\'' +
                    ", passMethod='" + passMethod + '\'' +
                    ", stationNum=" + stationNum +
                    '}';
        }
    }


    public TransitRouteResult getResult() {
        return result;
    }

    public void setResult(TransitRouteResult result) {
        this.result = result;
    }

    public int getTotalStationNum() {
        return totalStationNum;
    }

    public void setTotalStationNum(int totalStationNum) {
        this.totalStationNum = totalStationNum;
    }

    public void setPassItemInfoList(List<PassItemInfo> passItemInfoList) {
        this.passItemInfoList = passItemInfoList;
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }

    public void setTotalLength(int totalLength) {
        this.totalLength = totalLength;
    }

    public void setTotalTime(double totalTime) {
        this.totalTime = totalTime;
    }

    public String getRouteName() {
        return routeName;
    }

    public int getTotalLength() {
        return totalLength;
    }

    public double getTotalTime() {
        return totalTime;
    }

    public List<PassItemInfo> getPassItemInfoList() {
        return passItemInfoList;
    }

    @Override
    public String toString() {
        return "PassInfo{" +
                "passItemInfoList=" + passItemInfoList.toString() +
                ", totalLength=" + totalLength +
                ", totalTime=" + totalTime +
                ", routeName='" + routeName + '\'' +
                ", totalStationNum=" + totalStationNum +
                '}';
    }
}
