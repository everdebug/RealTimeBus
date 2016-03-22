package graduate.txy.com.realtimebus.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import graduate.txy.com.realtimebus.R;

/**
 * Created by lenovo on 2016/3/20.
 */
public class CityActivty extends Activity {
    private TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        tv = (TextView)findViewById(R.id.tv_text);
        tv.setText(getIntent().getStringExtra("name"));
    }
}
