package com.siyun.songs;

public class Song {
    private String name;
    private String artist;
    private String album;
    private String songCode;
    private boolean liked;
    private int rating;

    public Song() {
    }

    public Song(String name, String artist, String album, String songCode) {
        this.name = name;
        this.artist = artist;
        this.album = album;
        this.songCode = songCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getSongCode() {
        return songCode;
    }

    public void setSongCode(String songCode) {
        this.songCode = songCode;
    }

    public boolean isLiked() {
        return liked;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}
