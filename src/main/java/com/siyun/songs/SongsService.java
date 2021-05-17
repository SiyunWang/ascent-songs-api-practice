package com.siyun.songs;

import org.springframework.stereotype.Service;

import java.util.Optional;

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
        return songsRepository.findBySongCode(songCode).orElse(null);
    }

    public Song updateSong(String songCode, SongUpdate songUpdate) {
        Song song = songsRepository.findBySongCode(songCode).orElse(null);
        if (song != null) {
            song.setLiked(songUpdate.isLiked());
            song.setRating(songUpdate.getRating());
            songsRepository.save(song);
            return song;
        }
        return null;
    }

    public void deleteSong(String songCode) {
        return;
    }
}
