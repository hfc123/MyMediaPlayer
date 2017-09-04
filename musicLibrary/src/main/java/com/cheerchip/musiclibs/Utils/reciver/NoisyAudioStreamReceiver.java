package com.cheerchip.musiclibs.Utils.reciver;

/**
 * Created by YangJingLin on 2017/4/12.
 */

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.cheerchip.musiclibs.Utils.MyMediaPlayer;


/**
 * 来电/耳机拔出时暂停播放
 * Created by wcy on 2016/1/23.
 */
public class NoisyAudioStreamReceiver extends BroadcastReceiver {
    private static final String TAG = "NoisyAudioStreamReceive";

    private static boolean incomingFlag = false;

    private static String incoming_number = null;

    @Override
    public void onReceive(Context context, Intent intent) {
        String action=intent.getAction();
        if (action.equals(BluetoothDevice.ACTION_ACL_CONNECTED)) {
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
           Log.d("aaa", device.getName() + " ACTION_ACL_CONNECTED");
        } else if (action.equals(BluetoothDevice.ACTION_ACL_DISCONNECTED)) {
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
          Log.d("aaa", device.getName() + " ACTION_ACL_DISCONNECTED");
        }
        if (BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED
                .equals(action)) {
            int state = (Integer) intent.getExtras().get(
                    BluetoothAdapter.EXTRA_CONNECTION_STATE);
       //     mTvCurrentState.setText("链接状态改变");
            switch (state) {
                case BluetoothAdapter.STATE_CONNECTED:
                 //   mTvCurrentState.setText("已连接");
                    Log.e( "onReceive: ","1" );
                    break;
                case BluetoothAdapter.STATE_CONNECTING:
                 //   mTvCurrentState.setText("正在连接");
                    Log.e( "onReceive: ","2" );
                    break;
                case BluetoothAdapter.STATE_DISCONNECTED:
                  // mTvCurrentState.setText("已断开");
                 //   mListAdapter.notifyDataSetChanged();
                    Log.e( "onReceive: ","3" );
                    //MyMediaPlayer.getinstance().pauseorplay();
                    break;
                case BluetoothAdapter.STATE_DISCONNECTING:
                 //   mTvCurrentState.setText("正在断开...");
                    Log.e( "onReceive: ","4" );
                    break;

                default:
                    break;
            }
        }
       // PlayService.startCommand(context, Actions.ACTION_MEDIA_PLAY_PAUSE);
        //如果是拨打电话
        if (intent.getAction().equals(AudioManager.ACTION_AUDIO_BECOMING_NOISY)){
            if (MyMediaPlayer.player!=null){
                if (MyMediaPlayer.player.isPlaying()){
                    MyMediaPlayer.getinstance().pauseorplay();
                }
            }
        }
        if(intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL)){
            incomingFlag = false;
            String phoneNumber = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
            Log.i(TAG, "call OUT:"+phoneNumber);
        }else{
            //如果是来电
            TelephonyManager tm =
                    (TelephonyManager)context.getSystemService(Service.TELEPHONY_SERVICE);

            switch (tm.getCallState()) {
                case TelephonyManager.CALL_STATE_RINGING:
                    incomingFlag = true;//标识当前是来电
                    incoming_number = intent.getStringExtra("incoming_number");
                    Log.i(TAG, "RINGING :"+ incoming_number);
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:

                    if(incomingFlag){
                        Log.i(TAG, "incoming ACCEPT :"+ incoming_number);
                    }
                    break;

                case TelephonyManager.CALL_STATE_IDLE:
                    if(incomingFlag){
                        Log.i(TAG, "incoming IDLE");
                    }
                    break;
            }
        }
    }
}
