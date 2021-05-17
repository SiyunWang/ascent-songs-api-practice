package com.siyun.songs;

public class SongUpdate {
    private int rating;
    private boolean liked;

    public SongUpdate() {
    }

    public SongUpdate(int rating, boolean liked) {
        this.rating = rating;
        this.liked = liked;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public boolean isLiked() {
        return liked;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }
}
