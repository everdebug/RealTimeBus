package graduate.txy.com.realtimebus.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Window;
import android.widget.TextView;

import graduate.txy.com.realtimebus.R;
import graduate.txy.com.realtimebus.switchlayoutUtils.SwichLayoutInterFace;
import graduate.txy.com.realtimebus.switchlayoutUtils.SwitchLayout;

/**
 * Created by lenovo on 2016/3/20.
 */
public class AboutActivity extends Activity implements SwichLayoutInterFace {

    private TextView tv_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//无title
        setContentView(R.layout.activity_about);
        tv_title = (TextView) findViewById(R.id.tv_about_title);
        tv_title.setText(getIntent().getStringExtra("name"));
        setEnterSwichLayout();
    }

    @Override
    public void setEnterSwichLayout() {
        SwitchLayout.getSlideFromRight(this, false, null);
    }

    @Override
    public void setExitSwichLayout() {
        SwitchLayout.getSlideToRight(this, true, null);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {// 按返回键时退出Activity的Activity特效动画

        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            setExitSwichLayout();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
