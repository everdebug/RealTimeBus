package graduate.txy.com.realtimebus.domain;

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

    public class PassItemInfo {
        String transport;
        String passMethod;
        int stationNum;


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
                    "passMethod='" + passMethod + '\'' +
                    ", transport=" + transport +
                    ", stationNum=" + stationNum +
                    '}';
        }
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
