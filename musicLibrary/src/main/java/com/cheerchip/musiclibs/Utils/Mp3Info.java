package com.cheerchip.musiclibs.Utils;

import android.content.ContentUris;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.ParcelFileDescriptor;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;

/**
 * Created by hfc on 2017/3/31.
 */
public class Mp3Info {
    private long id;
    private String artist;
    private long duration;
    private long size;
    private String url;
    private String title;
    private long ALBUM_ID;


    public long getALBUM_ID() {
        return ALBUM_ID;
    }

    public void setALBUM_ID(long ALBUM_ID) {
        this.ALBUM_ID = ALBUM_ID;
    }

    @Override
    public String toString() {
        return "Mp3Info{" +
                "id=" + id +
                ", artist='" + artist + '\'' +
                ", duration=" + duration +
                ", size=" + size +
                ", url='" + url + '\'' +
                ", title='" + title + '\'' +
                '}';
    }

    public Mp3Info() {
    }

    public Mp3Info(long id, String artist, long duration, long size, String url, String title) {
        this.id = id;
        this.artist = artist;
        this.duration = duration;
        this.size = size;
        this.url = url;
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


}
