package com.cheerchip.mymediaplayer;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import com.readystatesoftware.systembartint.SystemBarTintManager;

/**
 * Created by noname on 2017/8/22.
 */

public class BaseActivity extends AppCompatActivity {
    private SystemBarTintManager tintManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initStateBar();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * 初始化沉浸式
     */
    private void initStateBar() {
        setColorId();

        if (isNeedLoadStatusBar()) {
            loadStateBar();
        }
    }

    private boolean isNeedLoadStatusBar() {
        return true;
    }

    private void loadStateBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
        }
        tintManager = new SystemBarTintManager(this);
        // 激活状态栏设置
        tintManager.setStatusBarTintEnabled(true);
        // 激活导航栏设置
        tintManager.setNavigationBarTintEnabled(true);
        // 设置一个状态栏颜色
        tintManager.setStatusBarTintResource(getColorId());
    }
    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    /**
     * 如果子类使用非默认的StatusBar,就重写此方法,传入布局的id
     */

    protected int  mColorId= R.color.blue;

    protected  void setColorId() {
        this.mColorId= R.color.transparent;//子类重写方式
    }

    protected  void setColorId(int mColorId) {
        this.mColorId=mColorId;//子类重写方式
    }


    protected int getColorId() {
        return mColorId;
    }

    public void initActivity(Activity mainActivity, Class webActivityClass, int type) {
        Intent intent=new Intent(mainActivity, webActivityClass);
        intent.putExtra("Tpye",type);
        startActivity(intent);
    }
    public void initActivity(Activity mainActivity, Class webActivityClass) {
        Intent intent=new Intent(mainActivity, webActivityClass);
        startActivity(intent);
    }
}
