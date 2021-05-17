package com.siyun.songs;

import org.springframework.stereotype.Service;

@Service
public class SongsService {
    SongsRepository songsRepository;

    public SongsService(SongsRepository songsRepository) {
        this.songsRepository = songsRepository;
    }

    public SongsList getSongs() {
        return new SongsList(songsRepository.findAll());
    }

    public SongsList getSongs(String artist, String album) {
        return new SongsList(songsRepository.findByArtistContainsAndAlbumContains(artist, album));
    }

    public SongsList getSongsByArtist(String artist) {
        return new SongsList(songsRepository.findByArtistContains(artist));
    }

    public SongsList getSongsByAlbum(String album) {
        return new SongsList(songsRepository.findByAlbumContains(album));
    }

    public Song addSong(Song song) {
        return songsRepository.save(song);
    }

    public Song getSongBySongCode(String songCode) {
        return null;
    }

    public Song updateSong(String songCode, SongUpdate songUpdate) {
        return null;
    }

    public void deleteSong(String songCode) {
        return;
    }
}
