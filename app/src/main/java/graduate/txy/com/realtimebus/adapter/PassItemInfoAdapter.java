package graduate.txy.com.realtimebus.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import graduate.txy.com.realtimebus.R;
import graduate.txy.com.realtimebus.domain.PassInfo;

/**
 * 换乘Dialog中显示步骤的适配器
 * <p/>
 * Created by lenovo on 2016/4/6.
 */
public class PassItemInfoAdapter extends BaseAdapter {

    List<PassInfo.PassItemInfo> passItemInfos;
    private Context mContext;

    public PassItemInfoAdapter(Context mContext, List<PassInfo.PassItemInfo> passItemInfos) {
        this.passItemInfos = passItemInfos;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return passItemInfos.size();
    }

    @Override
    public Object getItem(int position) {
        return passItemInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view;
        ViewHolder viewHolder;
        if (convertView != null) {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        } else {
            viewHolder = new ViewHolder();
            view = View.inflate(mContext, R.layout.item_info_pass, null);
            viewHolder.iv_pass_logo = (ImageView) view.findViewById(R.id.iv_pass_logo);
            viewHolder.tv_item_pass_info = (TextView) view.findViewById(R.id.tv_item_pass_info);
            viewHolder.tv_item_station_num = (TextView) view.findViewById(R.id.tv_item_station_num);
            view.setTag(viewHolder);
        }
        int imageId;
        switch (passItemInfos.get(position).getPassMethod()) {
            case "BUSLINE":
                imageId = R.drawable.bus_logo;
                break;
            case "SUBWAY":
                imageId = R.drawable.subway_logo;
                break;
            case "WAKLING":
                imageId = R.drawable.walk_logo;
                break;
            default:
                imageId = R.drawable.icon_logo ;
        }
        viewHolder.iv_pass_logo.setImageResource(imageId);
        viewHolder.tv_item_pass_info.setText(passItemInfos.get(position).getPassMethod());
        viewHolder.tv_item_station_num.setText(passItemInfos.get(position).getStationNum() + "站");
        return view;
    }

    class ViewHolder {
        ImageView iv_pass_logo;
        TextView tv_item_pass_info;
        TextView tv_item_station_num;
    }
}
