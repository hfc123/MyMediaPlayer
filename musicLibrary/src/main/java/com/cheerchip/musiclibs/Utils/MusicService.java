package com.cheerchip.musiclibs.Utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.RemoteViews;

import com.cheerchip.musiclibs.R;
import com.cheerchip.musiclibs.Utils.reciver.NoisyAudioStreamReceiver;


/**
 * Created by hfc on 2016/10/18.
 */

public class MusicService extends Service implements AudioManager.OnAudioFocusChangeListener{

    private static final String TAG = "MusicService";

    //Notification播放功能暂时用不上
    private NotificationManager nfManager;
    private RemoteViews remoteViews;
    private Notification.Builder builder;
    private static final int STOPSERVICE = 3;
    private AudioManager audiomanage;
    private ComponentName mRemoteReceiver;
    private NoisyAudioStreamReceiver noisereceiver;



    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }




    @Override
    public void onCreate() {
        super.onCreate();
        audiomanage = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        registerReceiver();

       if (Constent.OPENNOTIFICATION){
           //Notification播放功能暂时用不上
           builder = new Notification.Builder(this);
           remoteViews = new RemoteViews(getPackageName(), R.layout.nf_layout);
           initpendingintent();
           builder.setContent(remoteViews).setSmallIcon(R.drawable.default1);
           startForeground(1, builder.build());
           nfManager = ((NotificationManager) getSystemService(NOTIFICATION_SERVICE));
       }

    }

    private void initpendingintent() {
        //下一曲
        Intent intent1 = new Intent(this, MusicService.class);
        intent1.putExtra("type",6);
        PendingIntent nextintent = PendingIntent.getService(this, 1, intent1,0);
        remoteViews.setOnClickPendingIntent(R.id.nf_next_btn, nextintent);
        //上一曲
        Intent intent2 = new Intent(this, MusicService.class);
        intent2.putExtra("type",7);
        PendingIntent preintent = PendingIntent.getService(this, 2, intent2, 0);
        remoteViews.setOnClickPendingIntent(R.id.nf_pre_btn, preintent);
        //暂停播放
        Intent intent3 = new Intent(this, MusicService.class);
        intent3.putExtra("type",8);
        PendingIntent playintent = PendingIntent.getService(this, 3, intent3,0);
        remoteViews.setOnClickPendingIntent(R.id.nf_pause_btn, playintent);
        //关闭
        Intent intent4 = new Intent(this, MusicService.class);
        intent4.putExtra("type",9);
        PendingIntent dele = PendingIntent.getService(this, 4, intent4, 0);
        remoteViews.setOnClickPendingIntent(R.id.nf_close_btn, dele);
    }

    private void registerReceiver() {
        audiomanage.requestAudioFocus(this, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
        mRemoteReceiver = new ComponentName(getPackageName(), RemoteControlReceiver1.class.getName());
       // mRemoteReceiver.

        audiomanage.registerMediaButtonEventReceiver(mRemoteReceiver);
        /*if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            MediaSession session=new MediaSession(this,"ss");
            PendingIntent intent = PendingIntent.getBroadcast(this,)
            session.setMediaButtonReceiver(new PendingIntent());
        }*/
        noisereceiver = new NoisyAudioStreamReceiver();

        IntentFilter filter = new IntentFilter();
        //耳机拔插
        filter.addAction(AudioManager.ACTION_AUDIO_BECOMING_NOISY);
        //来电
        filter.addAction(TelephonyManager.ACTION_PHONE_STATE_CHANGED);
        //去电
        filter.addAction(Intent.ACTION_NEW_OUTGOING_CALL);
        filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        filter.addAction(BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED);
        registerReceiver(noisereceiver, filter);

    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int type = intent.getIntExtra("type", MyMediaPlayer.STOP);
        Log.e("onStartCommand: ","" +type);
        String musicName = intent.getStringExtra("musicName");
        //Notification播放功能暂时用不上
        if (Constent.OPENNOTIFICATION) {
            remoteViews.setTextViewText(R.id.music_name, musicName);
            nfManager.notify(1, builder.build());
        }
       /* String albumpic_small = MyMediaPlayer.currentMusic.getAlbumpic_small();
        if (albumpic_small != null && !"".equals(albumpic_small)) {
            String fileName = albumpic_small.substring(albumpic_small.lastIndexOf("/") + 1);
            Bitmap bitmap = BitmapFactory.decodeFile(new File(this.getExternalCacheDir(), fileName).getAbsolutePath());
            if (bitmap!=null) {
                remoteViews.setImageViewBitmap(R.id.music_thumbnail, bitmap);
            }
        }*/




        switch (type) {
            case MyMediaPlayer.PLAY:
                MyMediaPlayer.getinstance().play( intent.getStringExtra("musicPath"));
                break;
            case MyMediaPlayer.PAUSE:
                MyMediaPlayer.getinstance().pause();

                break;
            case MyMediaPlayer.STOP:
                MyMediaPlayer.getinstance().stop();
                break;
            case STOPSERVICE:
                stopSelf();
             //   sendBroadcast(new Intent(MyMediaPlayer.STOPSERVICE_ACTION));
                break;
            case 6:
                //下一曲
                MyMediaPlayer.getinstance().nextmusic();
                break;
            case 7:
                //上一曲
             MyMediaPlayer.getinstance().premusic();
                break;
            case 8:
                //暂停
                MyMediaPlayer.getinstance().pauseorplay();
                break;
            case 9:
                //关闭
              //  notification.flags |= Notification.FLAG_AUTO_CANCEL;
                builder.setAutoCancel(true);
              //  builder.setVisibility()
              //  nfManager.cancel(1);
                break;
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        MyMediaPlayer.getinstance().stop();

        if (mRemoteReceiver != null) {
            audiomanage.unregisterMediaButtonEventReceiver(mRemoteReceiver);
        }
        audiomanage.abandonAudioFocus(this);
        unregisterReceiver(noisereceiver);
    }

    @Override
    public void onAudioFocusChange(int focusChange) {
        switch (focusChange) {
            case AudioManager.AUDIOFOCUS_LOSS:
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                if (MyMediaPlayer.player==null)
                    return;
                if (MyMediaPlayer.player.isPlaying())
                    MyMediaPlayer.getinstance().pauseorplay();
                break;
        }
    }
}

