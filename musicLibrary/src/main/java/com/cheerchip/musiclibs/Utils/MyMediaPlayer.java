package com.cheerchip.musiclibs.Utils;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by hfc on 2016/10/18.
 */

public class MyMediaPlayer {
    public static MediaPlayer player = null;
    public final static int PLAY = 0;
    public final static int PAUSE = 1;
    public final static int STOP = 2;
    //记录当前的播放状态
    public static int CURRENT_STATE = 2;
    //当前正在播放的Music对象
    public static  Mp3Info currentMusic;
    //记录当前所播放音乐在List集合中的位置
    public static int CURRENT_POSITION = 0;
    public static   List<Mp3Info> CURRENT_PLAY_LIST;
    //当前模式的状态
    public static int CUREENTMODE=1;
    public static  int RADOMMODE=3;
    public static  int SINGLEMODE=2;
    public static int ORDERMODE=1;
    //音乐切换的监听


    private  MusicChangeLisener onmusicchanged=null;
    //音乐暂停播放的监听
    private  OnMusicStopOrPauseLisener onstoppause;
    private  Context context1;
    private static MyMediaPlayer  myMediaPlayer;

    public OnMusicStopOrPauseLisener getOnstoppause() {
        return onstoppause;
    }

    public void setOnstoppause(OnMusicStopOrPauseLisener onstoppause) {
        this.onstoppause = onstoppause;
    }

    public   void init(Context context){
        /*//初始化时启动服务
        Intent intent = new Intent(context, MusicService.class);
        intent.putExtra("type", "5");
        context.startService(intent);*/
        if (player == null){
            player = new MediaPlayer();
        }
        context1=context;
        player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                //准备工作完成后开始播放
                if (Constent.PRESTART){
                    mp.start();
                }
            CURRENT_STATE = PLAY;
        }
      });
        initplayer();
        player.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
               // Log.e( "onError: ","errorerrorerrorerrorerror" );
                return false;
            }
        });
        if (onmusicchanged==null){
            onmusicchanged=new MusicChangeLisener() {
                @Override
                public void onmusicchanged(Mp3Info mp3Info) {

                }
            };
        }
    }
    public static MyMediaPlayer getinstance(){
        if (myMediaPlayer==null){
            myMediaPlayer=new MyMediaPlayer();
        }
        return myMediaPlayer;
    }
    //设置成准备状态进入音乐界面需要准备好。
    public void prepare(String musicPath){
        player.reset();
        try {
            if (!SDCardUtil.isLocal(currentMusic.getId() + "")) {
                player.setDataSource(musicPath);
            } else {
                player.setDataSource(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "MyMusic" + File.separator + currentMusic.getId() + ".mp3");
            }
            player.prepare();
          //  player.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //随机播放代码
    private  void suiji(){
        CUREENTMODE=RADOMMODE;
      int num=  CURRENT_PLAY_LIST.size();
        //随机播放数
      int num1= ((int) (Math.random() * num));
        //更改当前音乐及音乐名称
        currentMusic=CURRENT_PLAY_LIST.get(num1);
        CURRENT_POSITION=num1;
        //在该方法执行修改标题等
        if (onmusicchanged==null){
            return;
        }
        onmusicchanged.onmusicchanged(getCurrentMusic());
    }
    //顺序播放代码
    private  void shunxu(){
        CUREENTMODE=ORDERMODE;
        //更改当前音乐及音乐名称
        CURRENT_POSITION++;
        if (CURRENT_POSITION>=CURRENT_PLAY_LIST.size()){
            CURRENT_POSITION=0;
        }

        currentMusic=CURRENT_PLAY_LIST.get(CURRENT_POSITION);
        //在该方法执行修改标题等
        if (onmusicchanged==null){
            return;
        }
        onmusicchanged.onmusicchanged(getCurrentMusic());
    }
    //单曲循环
    private  void danquxunhuan(){
        CUREENTMODE=SINGLEMODE;
        onmusicchanged.onmusicchanged(getCurrentMusic());
    }

    public MusicChangeLisener getOnmusicchanged() {
        return onmusicchanged;
    }
    //设置播放完成的后执行的代码
    public void setOnmusicchanged(MusicChangeLisener onmusicchanged) {
        this.onmusicchanged = onmusicchanged;

    }
    //播放完成后执行的方法
    private  void initplayer() {
        MyMediaPlayer.player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                Log.e( "onCompletion: ","qqqqqq" );
                if (CUREENTMODE==RADOMMODE){
                suiji();
                     //  ((MusicActivity) MusicActivity.context).setmusictitle();
                    //player.pause();
                    startService( currentMusic, MyMediaPlayer.PLAY);
                }else if (CUREENTMODE==SINGLEMODE){
                  //  player.setLooping(true);
                    danquxunhuan();
                    mp.start();
                   // ((MusicActivity) MusicActivity.context).setmusictitle();
                }else if (CUREENTMODE==ORDERMODE){
                  //  player.setLooping(false);
                    shunxu();
                    Log.e( "pauseorplay1111: ",currentMusic.getTitle() );
                    startService(currentMusic,  MyMediaPlayer.PLAY);
                }
            //  ((MainActivity) MainActivity.context).setmusictitle();
            }
        });
    }
    //播放
    public  void play(String musicPath) {
        player.reset();
        try {
            if (!SDCardUtil.isLocal(currentMusic.getId() + "")) {
                player.setDataSource(musicPath);
            } else {
                player.setDataSource(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "MyMusic" + File.separator + currentMusic.getId() + ".mp3");
            }
            player.prepare();
            player.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //暂停
    public  void pause() {
        if (player != null && player.isPlaying()) {
            player.pause();
            CURRENT_STATE = PAUSE;

            if (onstoppause!=null)
                onstoppause.onpasueLisener();

        } else if (player != null && !player.isPlaying()) {
            player.start();
            if (onstoppause!=null)
                onstoppause.onplayLisenner();
                CURRENT_STATE = PLAY;

        }



    }
    //停止
    public  void stop() {
        if (player != null) {
            player.stop();
            player.release();
            player = null;
            CURRENT_STATE = STOP;
        }
    }
    //开启服务
    public void startService(Mp3Info musicBean, int type) {
        currentMusic = musicBean;
        Intent intent = new Intent(context1, MusicService.class);
        intent.putExtra("type", type);
        intent.putExtra("musicPath", musicBean.getUrl());
        intent.putExtra("musicName", musicBean.getTitle());
        context1.startService(intent);
    }

    //开启服务什么也不做
    public void startService() {
        Intent intent = new Intent(context1, MusicService.class);
        intent.putExtra("type", 5);
        context1.startService(intent);
    }

    private long duraction;
    //获取当前音乐播放器的长度
    public long getDuraction() {

        return   this.player.getDuration();

    }
    //获取当前音乐列表
    public List<Mp3Info> getCURRENT_PLAY_LIST() {
        return CURRENT_PLAY_LIST;
    }
    //设置当前音乐列表
    public void setCURRENT_PLAY_LIST(List<Mp3Info> CURRENT_PLAY_LIST) {
        this.CURRENT_PLAY_LIST = CURRENT_PLAY_LIST;
    }
    //获取当前音乐
    public Mp3Info getCurrentMusic() {
        return currentMusic;
    }
    //设置当前的音乐
    public void setCurrentMusic(Mp3Info currentMusic) {
        this.currentMusic = currentMusic;
    }
    //下一曲
    public  void nextmusic(){
        CURRENT_POSITION++;
        if (CURRENT_POSITION==CURRENT_PLAY_LIST.size()){
            CURRENT_POSITION=0;
        }
        if (CUREENTMODE==RADOMMODE){
            suiji();
            startService( currentMusic, MyMediaPlayer.PLAY);
        }else {
            currentMusic=CURRENT_PLAY_LIST.get(CURRENT_POSITION);
            startService( currentMusic, MyMediaPlayer.PLAY);
        }
        if (onmusicchanged!=null)
        onmusicchanged.onmusicchanged(getCurrentMusic());
        if (onstoppause!=null)
        onstoppause.onplayLisenner();
    }
    //上一曲
    public  void premusic(){
        CURRENT_POSITION--;
        if (CURRENT_POSITION<0){
            CURRENT_POSITION=CURRENT_PLAY_LIST.size()-1;
        }
        if (CUREENTMODE==RADOMMODE){
            suiji();
            startService( currentMusic, MyMediaPlayer.PLAY);
        }else {
            currentMusic = CURRENT_PLAY_LIST.get(CURRENT_POSITION);
            startService(currentMusic, MyMediaPlayer.PLAY);

        }
        if (onmusicchanged!=null)
        onmusicchanged.onmusicchanged(getCurrentMusic());
        if (onstoppause!=null)
            onstoppause.onplayLisenner();
    }
    //暂停或者播放
    public void pauseorplay(){
            Log.e( "pauseorplay: ",currentMusic.getTitle() );
        if (CURRENT_STATE!= STOP){
            //CURRENT_STATE=PAUSE;

           startService(currentMusic, MyMediaPlayer.PAUSE);
        }else {
            Log.e( "pauseorplay: ","2" );
          //  CURRENT_STATE=PLAY;
            CURRENT_STATE = PLAY;

            startService( currentMusic, MyMediaPlayer.PLAY);
        }
        if (onmusicchanged!=null)
          onmusicchanged.onmusicchanged(getCurrentMusic());
    }
    public void play(int posi){
        CURRENT_POSITION=posi;
        onmusicchanged.onmusicchanged(getCurrentMusic());
        startService( currentMusic, MyMediaPlayer.PLAY);
    }
    //跳转到相应的位置
    public void seekto(int position){
        player.seekTo(position);
    }

}
