package graduate.txy.com.realtimebus;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;


/**
 * 欢迎界面
 * <p/>
 * 功能： 检查网络、GPS、WIFI是否开启、检测更新、数据库更新
 */
//不要继承ActionBarActivity
public class SplashActivity extends Activity {
    private static final String TAG = "SplashActivity";

    private static final int NEXT_ACTIVITY = 0;
    private static final int NO_NET = 1;
    private static final int NO_GPS = 2;
    private boolean IS_CHECK_OVER = false;


    private AnimationSet set;
    private ScaleAnimation scale;
    private AlphaAnimation alpha;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case NEXT_ACTIVITY: {
                    IS_CHECK_OVER = true;
                    Log.i(TAG, "handler_0");
                    break;
                }
                case NO_NET: {
                    Log.i(TAG, "handler_1");

                    break;
                }
                case NO_GPS: {
                    Log.i(TAG, "handler_2");

                    break;
                }
            }
            super.handleMessage(msg);
        }
    };

    private ConnectivityManager cm;
    private LocationManager lm;
    private RelativeLayout rl_splash;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//无title
        setContentView(R.layout.activity_splash);

        init();

        startAnimation();

    }

    private void init() {
        rl_splash = (RelativeLayout) findViewById(R.id.rl_splash);
        set = new AnimationSet(false);
//缩放动画
        scale = new ScaleAnimation(0.8f, 1, 0.9f, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
//淡入淡出动画
        alpha = new AlphaAnimation(0, 1);

        cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Log.i(TAG, "init");


    }

    private void check() {
        Log.i(TAG, "check");

        Thread t = new Thread() {
            Message m = Message.obtain();

            @Override
            public void run() {
                if (!CheckUtils.isNetConnected(cm)) {
//请连接网络
                    m.obj = "请连接网络";
                    m.what = NO_NET;
                    handler.sendMessage(m);
                    return;
                }
                Log.i(TAG, "net");


                if (!CheckUtils.isGpsEnabled(lm)) {
//请打开GPS用来进行定位
                    m.obj = "请打开GPS";
                    m.what = NO_GPS;
                    handler.sendMessage(m);
                    return;
                }

                Log.i(TAG, "gps");


                if (!CheckUtils.isWifiConnected(cm)) {
//如果打开wifi进行定位精准度会更高，设置是否存在wifi,Toast
                } else {
//设置没有wifi
                }

                //m = null;
                m.obj = "跳转Activity";
                m.what = NEXT_ACTIVITY;

                handler.sendMessage(m);
                Log.i(TAG, "send_0");

            }
        };
        t.start();

    }

    private void transition() {
        Log.i(TAG, "transition");

//跳转页面放到handler中
        startActivity(new Intent(SplashActivity.this, MainActivity.class));
        Log.i(TAG, "欢迎界面结束，进入正式界面");
        finish();
    }

    /**
     * 开始动画
     */
    private void startAnimation() {
        scale.setDuration(1000);
        scale.setFillAfter(true);

        alpha.setDuration(3000);
        alpha.setFillAfter(true);

        set.addAnimation(scale);
        set.addAnimation(alpha);
        set.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                Log.i(TAG, "start Anim");

                check();
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (IS_CHECK_OVER) {
                    transition();
                }
                Log.i(TAG, "stop Anim");
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

//设置动画
        rl_splash.startAnimation(set);


    }


}
