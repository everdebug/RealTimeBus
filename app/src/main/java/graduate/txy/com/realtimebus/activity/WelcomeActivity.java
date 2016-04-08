package graduate.txy.com.realtimebus.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

import graduate.txy.com.realtimebus.R;

/**
 * 欢迎界面
 * Created by lenovo on 2016/3/27.
 */
public class WelcomeActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_welcome);
    }
}
