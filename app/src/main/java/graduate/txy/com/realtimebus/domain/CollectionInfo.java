package graduate.txy.com.realtimebus.domain;

/**
 * 收藏信息，用于显示和收藏
 * Created by lenovo on 2016/4/8.
 */
public class CollectionInfo {
    int id;
    private String startStation;
    private String endStation;
    private String city;
    private String routeName;

    public String getRouteName() {
        return routeName;
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getEndStation() {
        return endStation;
    }

    public void setEndStation(String endStation) {
        this.endStation = endStation;
    }

    public String getStartStation() {
        return startStation;
    }

    public void setStartStation(String startStation) {
        this.startStation = startStation;
    }

    @Override
    public String toString() {
        return "CollectionInfo{" +
                "city='" + city + '\'' +
                ", startStation='" + startStation + '\'' +
                ", endStation='" + endStation + '\'' +
                ", routeName='" + routeName + '\'' +
                '}';
    }
}
