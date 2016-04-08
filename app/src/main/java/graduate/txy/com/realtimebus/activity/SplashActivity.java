package graduate.txy.com.realtimebus.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;

import graduate.txy.com.realtimebus.R;
import graduate.txy.com.realtimebus.utils.CheckUtils;
import graduate.txy.com.realtimebus.utils.SharePreferenceUtils;


/**
 * 欢迎界面
 * <p/>
 * 功能： 检查网络、GPS、WIFI是否开启、数据库更新
 */
public class SplashActivity extends Activity {

    private static final String TAG = "SplashActivity";

    //check类型
    private static final int NEXT_ACTIVITY = 0;
    private static final int NO_NET = 1;
    private static final int NO_GPS = 2;

    private AnimationSet set;
    private ScaleAnimation scale;
    private AlphaAnimation alpha;

    private RelativeLayout rl_splash;
    private AlertDialog.Builder builder;

    private ConnectivityManager cm;
    private LocationManager lm;


    //接收各种check返回的类型，并进行相应处理
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case NEXT_ACTIVITY: {
                    Log.i(TAG, "handler_NEXT_ACTIVITY");
                    transition();
                    break;
                }
                case NO_NET: {
                    Log.i(TAG, "handler_NO_NET");
                    showDialog((String) msg.obj, Settings.ACTION_SETTINGS);//双卡手机将会无法打开网络设置，所以只能跳转到设置界面
                    break;
                }
                case NO_GPS: {
                    Log.i(TAG, "handler_NO_GPS");
                    showDialog((String) msg.obj, Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    break;
                }
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//无title
        setContentView(R.layout.activity_splash);
        init();
        startAnimation();
        check();
    }

    /**
     * 恢复界面要进行相关检查
     */
    @Override
    protected void onRestart() {
        check();
        super.onRestart();
    }

    /**
     * 初始化
     */
    private void init() {
        rl_splash = (RelativeLayout) findViewById(R.id.rl_splash);
        builder = new AlertDialog.Builder(this);

        set = new AnimationSet(false);

        scale = new ScaleAnimation(0.8f, 1, 0.9f, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);//缩放动画
        alpha = new AlphaAnimation(0, 1);//淡入淡出动画

        cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        Log.i(TAG, "init_finish");

    }

    /**
     * 检测网络、GPS、WIFI
     */
    private void check() {
        Log.i(TAG, "check");

        Thread t = new Thread() {
            Message m = Message.obtain();

            @Override
            public void run() {

                long startTime = java.lang.System.currentTimeMillis();

                if (!CheckUtils.isNetConnected(cm)) {
//请连接网络
                    m.obj = "请连接网络";
                    m.what = NO_NET;
                    handler.sendMessage(m);
                    return;
                }
                Log.i(TAG, "net可用");


                if (!CheckUtils.isGpsEnabled(lm)) {
//请打开GPS用来进行定位
                    m.obj = "请打开GPS";
                    m.what = NO_GPS;
                    handler.sendMessage(m);
                    return;
                }

                Log.i(TAG, "gps可用");
//在config文件中写入wifi信息
                if (!CheckUtils.isWifiConnected(cm)) {
                    SharePreferenceUtils.setSPValue(SplashActivity.this, "IS_CONNECT_WIFI", false);
                } else {
                    SharePreferenceUtils.setSPValue(SplashActivity.this, "IS_CONNECT_WIFI", true);
                }
//延时跳转

                long endTime = java.lang.System.currentTimeMillis();
                long costTime = endTime - startTime;
                if (costTime < 2000) {
                    try {
                        Thread.sleep(2000 - costTime);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
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
        overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
    }


    /**
     * 打开提示对话框，并进行相应界面的跳转
     *
     * @param str  提示语
     * @param PATH 跳转的系统设置界面
     */
    private void showDialog(final String str, final String PATH) {


        builder.setMessage(str).setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.i(TAG, "退出应用");
                dialog.dismiss();
                finish();
            }
        }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.i(TAG, "跳转界面" + str);
                dialog.dismiss();
                startActivity(new Intent(PATH));
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();


    }


    /**
     * 开始动画
     */
    private void startAnimation() {
        scale.setDuration(1000);
        scale.setFillAfter(true);

        alpha.setDuration(2000);
        alpha.setFillAfter(true);

        set.addAnimation(scale);
        set.addAnimation(alpha);
        set.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                Log.i(TAG, "start Anim");
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Log.i(TAG, "stop Anim");
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        rl_splash.startAnimation(set);//设置动画
    }


}
