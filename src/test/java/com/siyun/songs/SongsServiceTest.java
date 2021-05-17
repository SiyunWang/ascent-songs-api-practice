package com.siyun.songs;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SongsServiceTest {
    SongsService songsService;

    @Mock
    SongsRepository songsRepository;

    @BeforeEach
    void setup() {
        songsService = new SongsService(songsRepository);
    }

    @Test
    void getSongs_withoutParams() {
        Song song1 = new Song("Hey Jude", "The Beatles", "The Beatles Again", "HTT538");
        Song song2 = new Song("Blinding Lights", "Weekend", "After Hours", "BWA985");
        Song song3 = new Song("Lady Madonna", "The Beatles", "The Beatles Again", "LTT234");
        Song song4 = new Song("Come Together", "The Beatles", "Abbey Road", "CTT464");
        List<Song> songs = Arrays.asList(song1, song2, song3, song4);
        when(songsRepository.findAll()).thenReturn(songs);
        SongsList songsList = songsService.getSongs();
        assertThat(songsList).isNotNull();
        assertThat(songsList.getSongs().size()).isEqualTo(4);

    }

    @Test
    void getSongs_withParams() {
        Song song1 = new Song("Hey Jude", "The Beatles", "The Beatles Again", "HTT538");
        Song song3 = new Song("Lady Madonna", "The Beatles", "The Beatles Again", "LTT234");
        List<Song> songs = Arrays.asList(song1, song3);
        when(songsRepository.findByArtistContainsAndAlbumContains(anyString(), anyString())).thenReturn(songs);
        SongsList songsList = songsService.getSongs("The Beatles", "The Beatles Again");
        assertThat(songsList).isNotNull();
        assertThat(songsList.getSongs().size()).isEqualTo(2);
    }

    @Test
    void getSongsByArtist() {
        Song song1 = new Song("Hey Jude", "The Beatles", "The Beatles Again", "HTT538");
        Song song3 = new Song("Lady Madonna", "The Beatles", "The Beatles Again", "LTT234");
        Song song4 = new Song("Come Together", "The Beatles", "Abbey Road", "CTT464");
        List<Song> songs = Arrays.asList(song1, song3, song4);
        when(songsRepository.findByArtistContains(anyString())).thenReturn(songs);
        SongsList songsList = songsService.getSongsByArtist("The Beatles");
        assertThat(songsList).isNotNull();
        assertThat(songsList.getSongs().size()).isEqualTo(3);
    }

    @Test
    void getSongsByAlbum() {
        Song song2 = new Song("Blinding Lights", "Weekend", "After Hours", "BWA985");
        List<Song> songs = Arrays.asList(song2);
        when(songsRepository.findByAlbumContains(anyString())).thenReturn(songs);
        SongsList songsList = songsService.getSongsByAlbum("After Hours");
        assertThat(songsList).isNotNull();
        assertThat(songsList.getSongs().size()).isEqualTo(1);
    }

    @Test
    void addSong() {
        Song song = new Song("Lady Madonna", "The Beatles", "The Beatles Again", "LTT234");
        when(songsRepository.save(any(Song.class))).thenReturn(song);
        Song addedSong = songsService.addSong(song);
        assertThat(addedSong).isNotNull();
        assertThat(addedSong.getName()).isEqualTo(song.getName());
    }

    @Test
    void getSongBySongCode() {
        Song song = new Song("Lady Madonna", "The Beatles", "The Beatles Again", "LTT234");
        when(songsRepository.findBySongCode(anyString())).thenReturn(Optional.of(song));
        Song songBySongCode = songsService.getSongBySongCode(song.getSongCode());
        assertThat(songBySongCode).isNotNull();
        assertThat(songBySongCode.getName()).isEqualTo(song.getName());
    }

    @Test
    void updateSong() {
        Song song = new Song("Lady Madonna", "The Beatles", "The Beatles Again", "LTT234");
        SongUpdate songUpdate = new SongUpdate(4, true);
        when(songsRepository.findBySongCode(anyString())).thenReturn(Optional.of(song));
        when(songsRepository.save(any(Song.class))).thenReturn(song);
        Song updatedSong = songsService.updateSong(song.getSongCode(), songUpdate);
        assertThat(updatedSong).isNotNull();
        assertThat(updatedSong.getName()).isEqualTo(song.getName());
        assertThat(updatedSong.getRating()).isEqualTo(4);
        assertThat(updatedSong.isLiked()).isTrue();
    }

    @Test
    void deleteSong() {
    }
}