package graduate.txy.com.realtimebus.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import graduate.txy.com.realtimebus.R;

/**
 * Created by lenovo on 2016/3/20.
 */
public class FeedbackActivity extends Activity {
    private EditText et;
    private TextView tv;
    public static final String PATH = "http://192.168.1.113:8080/RTBServer/servlet/FeedBackServlet?feedback=";
    public static final String TAG ="FeedbackActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//无title
        setContentView(R.layout.activity_feedback);
        et = (EditText) findViewById(R.id.et_feedback);
        tv = (TextView) findViewById(R.id.tv_map_title);
        tv.setText(getIntent().getStringExtra("name"));
    }

    public void postFeedback(View view) {
        String info = et.getText().toString();
        Log.i(TAG,info);
        HttpUtils utils = new HttpUtils();
        utils.send(HttpRequest.HttpMethod.GET, PATH + info, new RequestCallBack<Object>() {
            @Override
            public void onFailure(HttpException e, String s) {

            }

            @Override
            public void onSuccess(ResponseInfo<Object> responseInfo) {
                Toast.makeText(FeedbackActivity.this, "反馈成功！", Toast.LENGTH_SHORT).show();

            }
        });
        finish();
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
