package graduate.txy.com.realtimebus.activity;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import graduate.txy.com.realtimebus.MyView.MyTouchImageView;
import graduate.txy.com.realtimebus.R;
import graduate.txy.com.realtimebus.utils.SharePreferenceUtils;

/**
 * 地铁界面的显示，图片缩放拖动，自动居中
 * <p/>
 * Created by lenovo on 2016/3/20.
 */
public class SubwayActivity extends Activity {
    private TextView tv;
    private MyTouchImageView myiv_map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//无title
        setContentView(R.layout.activity_subway);
        tv = (TextView) findViewById(R.id.tv_subway_title);
        tv.setText(getIntent().getStringExtra("name"));
        myiv_map = (MyTouchImageView) findViewById(R.id.iv_subway);
        //获得城市，切换城市地铁图片
        String cityName = SharePreferenceUtils.getSPStringValue(SubwayActivity.this, "city", "北京");
        switchPic(cityName);
    }

    //根据城市进行切换图片
    private void switchPic(String cityName) {
        switch (cityName) {
            case "北京":
                myiv_map.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.subway_pic));
                break;
            case "哈尔滨":
                myiv_map.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.herbin_subway_map));
                break;
            default:
                myiv_map.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.subway_pic));
                break;
        }
    }


    /**
     * 摁键返回
     *
     * @param view
     */
    public void returnActivity(View view) {
        backActivity();
    }

    //返回finish和设置跳转动画
    private void backActivity() {
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {// 按返回键时退出Activity的Activity特效动画

        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            backActivity();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
