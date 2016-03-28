package graduate.txy.com.realtimebus.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import graduate.txy.com.realtimebus.R;
import graduate.txy.com.realtimebus.domain.PassInfo;

/**
 * 公交换乘显示适配器
 * Created by lenovo on 2016/3/27.
 */
public class PassInfoAdapter extends MyBaseAdapter<PassInfo, View> {
    private static final String TAG = "PassInfoAdapter";

    public PassInfoAdapter(Context ct, List<PassInfo> list) {
        super(ct, list);
    }

    public Object getItem(int position) {
        return list.get(position);
    }

    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view1;
        ViewHolder viewHolder;
        if (convertView != null) {
            view1 = convertView;
            viewHolder = (ViewHolder) view1.getTag();

        } else {
            //Log.i(TAG,"创建，调用？");
            viewHolder = new ViewHolder();
            view1 = View.inflate(context, R.layout.item_pass, null);
            viewHolder.tv_pass_title = (TextView) view1.findViewById(R.id.tv_pass_title);
            viewHolder.tv_pass_time = (TextView) view1.findViewById(R.id.tv_pass_time);
            viewHolder.tv_pass_length = (TextView) view1.findViewById(R.id.tv_pass_length);
            viewHolder.tv_pass_num = (TextView) view1.findViewById(R.id.tv_pass_num);
            view1.setTag(viewHolder);
        }

        //Log.i(TAG, (list.get(position)).getRouteName());
        viewHolder.tv_pass_title.setText((list.get(position)).getRouteName());
        viewHolder.tv_pass_time.setText("约"+String.format("%.2f", (list.get(position)).getTotalTime() / 60) + "分钟");
        viewHolder.tv_pass_length.setText((list.get(position)).getTotalLength() / 1000 + "公里");
        viewHolder.tv_pass_num.setText((list.get(position)).getTotalStationNum() + "站");
        return view1;
    }


    class ViewHolder {
        TextView tv_pass_title;
        TextView tv_pass_time;
        TextView tv_pass_length;
        TextView tv_pass_num;
    }
}
