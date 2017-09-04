package com.cheerchip.musiclibs;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;


import com.cheerchip.musiclibs.Utils.GetMusicUtils;
import com.cheerchip.musiclibs.Utils.Mp3Info;
import com.cheerchip.musiclibs.Utils.MyMediaPlayer;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<Mp3Info> mp3Infos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main11);

        ListView listView= ((ListView) findViewById(R.id.listview));        MyMediaPlayer.getinstance().init(this);
        mp3Infos = GetMusicUtils.getMp3Infos(this);
        MyMediaPlayer.getinstance().setCURRENT_PLAY_LIST(mp3Infos);
        final List<String>list=new ArrayList<>();
        for (int i = 0; i < mp3Infos.size() ; i++) {
            list.add(mp3Infos.get(i).getTitle());
        }
        MyMediaPlayer.getinstance().setCurrentMusic(mp3Infos.get(0));
        ArrayAdapter adapter=new ArrayAdapter(this,android.R.layout.simple_list_item_1,list);
        listView.setAdapter(adapter);
        Button bt1= ((Button) findViewById(R.id.bt1));
        Button bt2= ((Button) findViewById(R.id.bt2));
        Button bt3= ((Button) findViewById(R.id.bt3));
        Button bt4= ((Button) findViewById(R.id.bt4));
        Button bt5= ((Button) findViewById(R.id.bt5));
        Button bt6= ((Button) findViewById(R.id.bt6));
        Button bt7= ((Button) findViewById(R.id.bt7));
        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyMediaPlayer.getinstance().premusic();
            }
        });
        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyMediaPlayer.getinstance().pauseorplay();
            }
        });
        bt3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyMediaPlayer.getinstance().nextmusic();
            }
        });
    }
}
