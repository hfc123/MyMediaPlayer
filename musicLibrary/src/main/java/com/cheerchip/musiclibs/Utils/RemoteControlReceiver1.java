package com.cheerchip.musiclibs.Utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;

import com.cheerchip.musiclibs.Utils.MyMediaPlayer;

/**
 * Created by ll on 2017/8/3.
 */

public class RemoteControlReceiver1 extends BroadcastReceiver {
    private static final String TAG = "RemoteControlReceiver1";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("5454", "onReceive: ");
        Log.e(TAG, "按了音箱按键RemoteControlReceiver:" + intent.getAction());
        Log.e(TAG, "按了音箱按键RemoteControlReceiver:" + intent.getAction());
        KeyEvent event = intent.getParcelableExtra(Intent.EXTRA_KEY_EVENT);
        if (event == null || event.getAction() != KeyEvent.ACTION_UP) {
            return;
        }

        Log.e(TAG, "按了音箱按键RemoteControlReceiver:" + intent.getAction());


        switch (event.getKeyCode()) {
            case KeyEvent.KEYCODE_MEDIA_PLAY:
            case KeyEvent.KEYCODE_MEDIA_PAUSE:
            case KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE:
                //耳机
            case KeyEvent.KEYCODE_HEADSETHOOK:
                if (MyMediaPlayer.player!=null) {
                    MyMediaPlayer.getinstance().pauseorplay();
                }
                break;
            case KeyEvent.KEYCODE_MEDIA_NEXT:
                if (MyMediaPlayer.player!=null) {
                    MyMediaPlayer.getinstance().nextmusic();
                }
                break;
            case KeyEvent.KEYCODE_MEDIA_PREVIOUS:
                if (MyMediaPlayer.player!=null) {
                    MyMediaPlayer.getinstance().premusic();
                }
                break;
        }
    }
}
