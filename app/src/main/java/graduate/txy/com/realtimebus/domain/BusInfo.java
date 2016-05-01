package graduate.txy.com.realtimebus.domain;

/**
 * 服务器中公交车信息
 *
 * Created by lenovo on 2016/4/9.
 */
public class BusInfo {


    /**
     * bus_route : 345
     * bus_name : 黑A12345
     * distance : 122
     * arrive_time : 100000
     * longitude : 126.12312
     * latitude : 136.32145
     * next_station : 安翔桥北
     * last_station : 安翔桥南
     * direction : 1
     */

    private String bus_route;//路线名
    private String bus_name;//公交车牌号
    private int distance;//距所选公交站点的距离
    private int arrive_time;//到站时间
    private double longitude;
    private double latitude;
    private String next_station;
    private String last_station;
    private int direction;

    public String getBus_route() {
        return bus_route;
    }

    public void setBus_route(String bus_route) {
        this.bus_route = bus_route;
    }

    public String getBus_name() {
        return bus_name;
    }

    public void setBus_name(String bus_name) {
        this.bus_name = bus_name;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public int getArrive_time() {
        return arrive_time;
    }

    public void setArrive_time(int arrive_time) {
        this.arrive_time = arrive_time;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getNext_station() {
        return next_station;
    }

    public void setNext_station(String next_station) {
        this.next_station = next_station;
    }

    public String getLast_station() {
        return last_station;
    }

    public void setLast_station(String last_station) {
        this.last_station = last_station;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    @Override
    public String toString() {
        return "BusInfo{" +
                "bus_name='" + bus_name + '\'' +
                ", bus_route='" + bus_route + '\'' +
                ", distance=" + distance +
                ", arrive_time=" + arrive_time +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                ", next_station='" + next_station + '\'' +
                ", last_station='" + last_station + '\'' +
                ", direction=" + direction +
                '}';
    }
}
