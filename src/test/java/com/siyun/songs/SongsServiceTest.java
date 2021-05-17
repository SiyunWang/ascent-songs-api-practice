package com.siyun.songs;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
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
    }

    @Test
    void getSongsByArtist() {
    }

    @Test
    void getSongsByAlbum() {
    }

    @Test
    void addSong() {
    }

    @Test
    void getSongBySongCode() {
    }

    @Test
    void updateSong() {
    }

    @Test
    void deleteSong() {
    }
}