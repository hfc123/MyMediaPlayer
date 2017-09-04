package com.cheerchip.mymediaplayer;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.cheerchip.musiclibs.Utils.GetMusicUtils;
import com.cheerchip.musiclibs.Utils.Mp3Info;
import com.cheerchip.musiclibs.Utils.MusicChangeLisener;
import com.cheerchip.musiclibs.Utils.MyMediaPlayer;
import com.cheerchip.musiclibs.Utils.OnMusicStopOrPauseLisener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.cheerchip.musiclibs.Utils.MyMediaPlayer.RADOMMODE;
import static com.cheerchip.musiclibs.Utils.MyMediaPlayer.SINGLEMODE;

/**
 * Created by noname on 2017/8/24.
 */

public class MusicActivity extends BaseActivity {
    @BindView(R.id.menu_left)
    ImageView menuLeft;
    @BindView(R.id.menu_title)
    TextView menuTitle;
    @BindView(R.id.singer)
    TextView singer;
    @BindView(R.id.menu_right)
    ImageView menuRight;
    @BindView(R.id.titlelayout)
    RelativeLayout titlelayout;
    @BindView(R.id.play_seek)
    SeekBar playSeek;
    @BindView(R.id.music_duration_played)
    TextView musicDurationPlayed;
    @BindView(R.id.music_duration)
    TextView musicDuration;
    @BindView(R.id.music_single)
    ImageView musicSingle;
    @BindView(R.id.music_last)
    ImageView musicLast;
    @BindView(R.id.music_play)
    ImageView musicPlay;
    @BindView(R.id.music_next)
    ImageView musicNext;
    @BindView(R.id.music_random)
    ImageView musicRandom;
    @BindView(R.id.music_tool)
    LinearLayout musicTool;
    @BindView(R.id.volume_less)
    ImageView volumeLess;
    @BindView(R.id.play_volume)
    SeekBar playVolume;
    @BindView(R.id.volume_plus)
    ImageView volumePlus;
    @BindView(R.id.bottom_control)
    LinearLayout bottomControl;

    RelativeLayout footer;
    @BindView(R.id.roundview)
    RoundImageView roundview;
    private ObjectAnimator animator;

    /*    @BindView(R.id.lrcviewContainer)
        RelativeLayout lrcviewContainer;*/
    private SimpleDateFormat dateFormat = new SimpleDateFormat("mm:ss");

    private boolean sendable = false;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            try {
                String timeFormat = dateFormat.format(new Date(MyMediaPlayer.player.getCurrentPosition()));
                musicDurationPlayed.setText(timeFormat);
                // tvTimeBegin.setText(timeFormat);
            } catch (Exception e) {

            }
            try {
                playSeek.setProgress(MyMediaPlayer.player.getCurrentPosition());
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (sendable) {
                mHandler.sendEmptyMessageDelayed(0, 200);
            }
        }
    };
    private AudioManager audiomanage;
    private int max;
    private int currentVolume;
    private List<Mp3Info> mp3Infos;
    //  private RotateAnimation animation;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.music);
        //初始化播放器
        initPlayer();
        audiomanage = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        ButterKnife.bind(this);
        max = audiomanage.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        playVolume.setMax(max);
        Bitmap bitmap= GetMusicUtils.getArtworkFromFile(this, MyMediaPlayer.getinstance().getCurrentMusic().getId(), MyMediaPlayer.getinstance().getCurrentMusic().getALBUM_ID());
                if( bitmap==null){
                    roundview.setImageResource(R.mipmap.music_record);
                }else {
                    roundview.setImageBitmap(bitmap);
                }

        animator = ObjectAnimator.ofFloat(roundview, "rotation", 0f, 359.0f);
        animator.setDuration(3000);
        animator.setInterpolator(new LinearInterpolator());//不停顿
        animator.setRepeatCount(-1);//设置动画重复次数
        animator.setRepeatMode(ValueAnimator.RESTART);//动画重复模式
        if (MyMediaPlayer.player.isPlaying()) {
            musicPlay.setImageResource(R.mipmap.music_time_out);
            //开启动画
            startani();
        } else {
            musicPlay.setImageResource(R.mipmap.music_play);
        }
        MyMediaPlayer.getinstance().setOnmusicchanged(new MusicChangeLisener() {
            @Override
            public void onmusicchanged(Mp3Info mp3Info) {
                initplayerview();
                Bitmap bitmap= GetMusicUtils.getArtworkFromFile(MusicActivity.this, MyMediaPlayer.getinstance().getCurrentMusic().getId(), MyMediaPlayer.getinstance().getCurrentMusic().getALBUM_ID());
                if( bitmap==null){
                    roundview.setImageResource(R.mipmap.music_record);
                }else {
                    roundview.setImageBitmap(bitmap);
                }
            }
        });
        initplayerview();
        //适配
        //  shipei();
        playSeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                MyMediaPlayer.player.seekTo(seekBar.getProgress());
            }
        });
        playVolume.setProgress(audiomanage.getStreamVolume(AudioManager.STREAM_MUSIC));
        playVolume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                audiomanage.setStreamVolume(AudioManager.STREAM_MUSIC, playVolume.getProgress(), 0);
                currentVolume = audiomanage.getStreamVolume(AudioManager.STREAM_MUSIC);  //��ȡ��ǰֵ
                playVolume.setProgress(currentVolume);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(!fromUser)
                    return;
                audiomanage.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
                currentVolume = audiomanage.getStreamVolume(AudioManager.STREAM_MUSIC);  //��ȡ��ǰֵ
                playVolume.setProgress(currentVolume);
            }
        });
    }

    private void initplayerview() {

        //设置最大进度条
        playSeek.setMax((int) (MyMediaPlayer.getinstance().getCurrentMusic().getDuration()));
        //标题歌手名
        menuTitle.setText(MyMediaPlayer.getinstance().getCurrentMusic().getTitle());
        singer.setText(MyMediaPlayer.getinstance().getCurrentMusic().getArtist());
        musicDuration.setText(dateFormat.format(new Date(MyMediaPlayer.player.getDuration())));
        //动态更改进度条
        if (!sendable) {
            sendable = true;
            mHandler.sendEmptyMessage(0);
        }
    }

    /*    private void shipei() {
            RelativeLayout.LayoutParams params= (RelativeLayout.LayoutParams) lrcviewContainer.getLayoutParams();
         *//*   if (lrcviewContainer.getWidth()>lrcviewContainer.getHeight()){
            params.width=getScreenheight()/3;
            params.height=getScreenheight()/3;

        }else {
            params.width=getScreenheight()/3;
            params.height=getScreenheight()/3;
        }*//*
        params.width=getScreenheight()/3;
        params.height=getScreenheight()/3;
        Log.e( "shipei: ",params.width+","+params.height );
        lrcviewContainer.setLayoutParams(params);
    }*/
    //获取运行屏幕宽度
    public int getScreenWidth() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        //宽度 dm.widthPixels
        //高度 dm.heightPixels
        return dm.widthPixels;
    }

    //获取运行屏幕宽度
    public int getScreenheight() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        //宽度 dm.widthPixels
        //高度 dm.heightPixels
        return dm.heightPixels;
    }

    @OnClick({R.id.menu_left, R.id.music_single, R.id.music_last, R.id.music_play, R.id.music_next, R.id.music_random})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.menu_left:
                finish();
                break;
            case R.id.music_single:
                MyMediaPlayer.getinstance().CUREENTMODE = SINGLEMODE;
                break;
            case R.id.music_last:
                MyMediaPlayer.getinstance().premusic();
                musicPlay.setImageResource(R.mipmap.music_time_out);
                startani();
                break;
            case R.id.music_play:
                MyMediaPlayer.getinstance().pauseorplay();
                if (MyMediaPlayer.player.isPlaying()) {
                    musicPlay.setImageResource(R.mipmap.music_play);
                    stopani();
                } else {
                    musicPlay.setImageResource(R.mipmap.music_time_out);
                    startani();
                }
                break;
            case R.id.music_next:
                MyMediaPlayer.getinstance().nextmusic();
                musicPlay.setImageResource(R.mipmap.music_time_out);
                startani();
                break;
            case R.id.music_random:
                MyMediaPlayer.getinstance().CUREENTMODE = RADOMMODE;
                break;
        }
    }

    private void startani() {
        if (animator.isStarted()){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                animator.resume();//开始动画
            }
        }else {
            animator.start();
        }
    }
    private void stopani(){
       // roundview.anim
        animator.pause();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacks(null);
    }

    //监听声音大小
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        int volume;
        /** 现在还不能区分当前是调节媒体音量还是铃声音量,以后需要改 */
        switch (keyCode) {
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                volume = audiomanage.getStreamVolume(AudioManager.STREAM_MUSIC);
                playVolume.setProgress(--volume);
                audiomanage.setStreamVolume(AudioManager.STREAM_MUSIC,volume, 0);
                return true;
            case KeyEvent.KEYCODE_VOLUME_UP:
                volume = audiomanage.getStreamVolume(AudioManager.STREAM_MUSIC);
                playVolume.setProgress(++volume);
                audiomanage.setStreamVolume(AudioManager.STREAM_MUSIC,volume, 0);
                return true;
        }

        return super.onKeyDown(keyCode, event);
    }
    private void initPlayer() {
        MyMediaPlayer.getinstance().init(this);
        //开启服务什么也不做
        MyMediaPlayer.getinstance().startService();
        if (MyMediaPlayer.CURRENT_PLAY_LIST == null || MyMediaPlayer.CURRENT_PLAY_LIST.size() == 0) {

            //如果为空则去sd卡中获取数据并设置当前播放音乐
            mp3Infos = GetMusicUtils.getMp3Infos(this);
            MyMediaPlayer.getinstance().setCURRENT_PLAY_LIST(mp3Infos);
                MyMediaPlayer.getinstance().setCurrentMusic(mp3Infos.get(0));

            //当音乐改变时执行的方法
            MyMediaPlayer.getinstance().prepare(mp3Infos.get(MyMediaPlayer.CURRENT_POSITION).getUrl());

        } else {
            mp3Infos = MyMediaPlayer.CURRENT_PLAY_LIST;
        }

        //   adapter.setData(mp3Infos);
    }
}
