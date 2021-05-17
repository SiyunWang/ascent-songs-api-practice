package com.siyun.songs;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.List;

public class SongsList {
    private List<Song> songs;

    public SongsList() {
        songs = new ArrayList<>();
    }

    public SongsList(List<Song> songs) {
        this.songs = songs;
    }

    public List<Song> getSongs() {
        return songs;
    }

    public void setSongs(List<Song> songs) {
        this.songs = songs;
    }

    @JsonIgnore
    public boolean isEmpty() {
        return songs.size() == 0;
    }
}
