package com.joseph.mopprimenumber.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.joseph.mopprimenumber.R;
import com.joseph.mopprimenumber.thread.SplashThread;
import com.joseph.mopprimenumber.util.ApkUtil;

import java.io.File;

/**
 * 启动界面
 * Created by Administrator on 2018/2/21.
 */

public class SplashActivity extends BaseActivity {
    private static final String TAG = "SplashActivity";
    private String apkPath;    // APK路径
    private int sleepTime;    // 跳转页面定时
    private long exitTime;    // 返回键点击计时
    private SplashThread st;

    @Override
    protected void initVariables() {
        apkPath = ApkUtil.getApkPath();
        exitTime = 0;
        sleepTime = 5000;
        st = new SplashThread(this, handler, sleepTime);
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); //设置全屏
        setContentView(R.layout.activity_splash);
        //淡入效果动画
        RelativeLayout rootLayout = (RelativeLayout) findViewById(R.id.splash_rootview);
        AlphaAnimation animation = new AlphaAnimation(0.6f, 1.0f);
        animation.setDuration(2000);
        rootLayout.startAnimation(animation);
    }

    @Override
    protected void loadData() {
        Log.i(TAG, "当前设备API级别" + android.os.Build.VERSION.SDK_INT);
        Log.i(TAG, "当前设备名称 " + android.os.Build.MODEL);
        Log.i(TAG, "系统版本" + android.os.Build.VERSION.RELEASE);
        firstLogin();
    }

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 100:
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                    finish();
                    break;
            }
        }
    };

    /**
     * 是否为首次加载
     */
    private void firstLogin() {
        File file = new File(apkPath);
        if (!file.exists()) {
            //路径不存在
            requestPermission(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0x0001);
            Log.i(TAG, "首次加载");
        } else {
            st.start();
        }
    }

    /**
     * 权限成功回调函数
     *
     * @param requestCode
     */
    @Override
    public void permissionSuccess(int requestCode) {
        super.permissionSuccess(requestCode);
        st.start();
    }

    /**
     * 权限失败回调函数
     *
     * @param requestCode
     */
    @Override
    public void permissionFail(int requestCode) {
        super.permissionFail(requestCode);
        Toast.makeText(SplashActivity.this, "未获得权限，无法运行程序", Toast.LENGTH_LONG).show();
        finish();
    }

    /**
     * 返回键处理
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
}
