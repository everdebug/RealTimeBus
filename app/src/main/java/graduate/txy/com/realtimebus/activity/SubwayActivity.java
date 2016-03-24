package graduate.txy.com.realtimebus.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Window;
import android.widget.TextView;
import graduate.txy.com.realtimebus.R;

/**
 * 地铁界面的显示，图片缩放拖动，自动居中
 *
 * Created by lenovo on 2016/3/20.
 */
public class SubwayActivity extends Activity {
    private TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//无title
        setContentView(R.layout.activity_subway);
        tv = (TextView)findViewById(R.id.tv_subway_title);
        tv.setText(getIntent().getStringExtra("name"));
        //TODO 从config中获取城市，如果城市存在地铁线路图，则进行相关显示，否则显示此城市没有地铁图

    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {// 按返回键时退出Activity的Activity特效动画

        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            finish();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
