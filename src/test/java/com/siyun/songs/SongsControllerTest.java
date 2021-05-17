package com.siyun.songs;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SongsController.class)
class SongsControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    SongsService songsService;

    ObjectMapper mapper = new ObjectMapper();

    @Test
    void getSongs_noParams_returnsSongsList_NotEmpty() throws Exception {
        Song song1 = new Song("Hey Jude", "The Beatles", "The Beatles Again", "HTT538");
        Song song2 = new Song("Blinding Lights", "Weekend", "After Hours", "BWA985");
        Song song3 = new Song("Lady Madonna", "The Beatles", "The Beatles Again", "LTT234");
        Song song4 = new Song("Come Together", "The Beatles", "Abbey Road", "CTT464");
        SongsList songsList = new SongsList(Arrays.asList(song1, song2, song3, song4));
        when(songsService.getSongs()).thenReturn(songsList);
        mockMvc.perform(get("/api/songs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.songs", hasSize(4)));

    }

    @Test
    void getSongs_noParams_returnsNoContent_IfEmpty() throws Exception {
        SongsList songsList = new SongsList();
        when(songsService.getSongs()).thenReturn(songsList);
        mockMvc.perform(get("/api/songs"))
                .andExpect(status().isNoContent());
    }

    @Test
    void getSongs_withArtistAndAlbum_returnsSongsList_NotEmpty() throws Exception {
        Song song4 = new Song("Come Together", "The Beatles", "Abbey Road", "CTT464");
        SongsList songsList = new SongsList(Arrays.asList(song4));
        when(songsService.getSongs(anyString(), anyString())).thenReturn(songsList);
        mockMvc.perform(get("/api/songs?artist=The Beatles&album=Abbey Road"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.songs", hasSize(1)));

    }

    @Test
    void getSongs_withArtist_returnsSongsList_NotEmpty() throws Exception {
        Song song1 = new Song("Hey Jude", "The Beatles", "The Beatles Again", "HTT538");
        Song song2 = new Song("Blinding Lights", "Weekend", "After Hours", "BWA985");
        Song song4 = new Song("Come Together", "The Beatles", "Abbey Road", "CTT464");
        SongsList songsList = new SongsList(Arrays.asList(song1, song2, song4));
        when(songsService.getSongsByArtist(anyString())).thenReturn(songsList);
        mockMvc.perform(get("/api/songs?artist=The Beatles"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.songs", hasSize(3)));

    }

    @Test
    void getSongs_withAlbum_returnsSongsList_NotEmpty() throws Exception {
        Song song1 = new Song("Hey Jude", "The Beatles", "The Beatles Again", "HTT538");
        Song song3 = new Song("Lady Madonna", "The Beatles", "The Beatles Again", "LTT234");
        SongsList songsList = new SongsList(Arrays.asList(song1, song3));
        when(songsService.getSongsByAlbum(anyString())).thenReturn(songsList);
        mockMvc.perform(get("/api/songs?album=The Beatles Again"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.songs", hasSize(2)));

    }

    @Test
    void getSongs_withParams_returnsNoContent_IfEmpty() throws Exception {
        when(songsService.getSongs(anyString(), anyString())).thenReturn(new SongsList());
        mockMvc.perform(get("/api/songs?artist=Weekend&album=The Beatles Again"))
                .andExpect(status().isNoContent());

    }

    @Test
    void addSong_returnsAddedSong_forValidRequest() throws Exception {
        Song song = new Song("Lady Madonna", "The Beatles", "The Beatles Again", "LTT234");
        when(songsService.addSong(any(Song.class))).thenReturn(song);
        mockMvc.perform(post("/api/songs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(song)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Lady Madonna"));
    }

    @Test
    void addSong_returnsBadRequest_forInvalidRequest() throws Exception {
        Song song = new Song("Lady Madonna", "The Beatles", "The Beatles Again", "LTT234");
        when(songsService.addSong(any(Song.class))).thenThrow(InvalidSongException.class);
        mockMvc.perform(post("/api/songs")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(song)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getSongBySongCode_returnsSong_IfFound() throws Exception {
        Song song = new Song("Lady Madonna", "The Beatles", "The Beatles Again", "LTT234");
        when(songsService.getSongBySongCode(anyString())).thenReturn(song);
        mockMvc.perform(get("/api/songs/LTT234"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Lady Madonna"));
    }

    @Test
    void getSongBySongCode_returnsNoContent_IfNotFound() throws Exception {
        when(songsService.getSongBySongCode(anyString())).thenReturn(null);
        mockMvc.perform(get("/api/songs/LTT234"))
                .andExpect(status().isNoContent());
    }

    @Test
    void updateSong_returnsUpdatedSong_forSuccessfulUpdate() throws Exception {
        Song song = new Song("Lady Madonna", "The Beatles", "The Beatles Again", "LTT234");
        when(songsService.getSongBySongCode(anyString())).thenReturn(song);
        song.setRating(4);
        song.setLiked(true);
        SongUpdate songUpdate = new SongUpdate(4, true);
        when(songsService.updateSong(anyString(), any(SongUpdate.class))).thenReturn(song);
        mockMvc.perform(patch("/api/songs/LTT234")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(songUpdate)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Lady Madonna"))
                .andExpect(jsonPath("$.rating").value(4))
                .andExpect(jsonPath("$.liked").value(true));
    }

    @Test
    void updateSong_returnsNoContent_IfTargetNotFound() throws Exception {
        when(songsService.getSongBySongCode(anyString())).thenReturn(null);
        SongUpdate songUpdate = new SongUpdate(4, true);
        mockMvc.perform(patch("/api/songs/LTT235")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(songUpdate)))
                .andExpect(status().isNoContent());
    }

    @Test
    void updateSong_returnsBadRequest_forInvalidRequest() throws Exception {
        Song song = new Song("Lady Madonna", "The Beatles", "The Beatles Again", "LTT234");
        SongUpdate songUpdate = new SongUpdate(4, true);
        when(songsService.getSongBySongCode(anyString())).thenReturn(song);
        when(songsService.updateSong(anyString(), any(SongUpdate.class))).thenThrow(InvalidUpdateException.class);
        mockMvc.perform(patch("/api/songs/LTT234")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(songUpdate)))
                .andExpect(status().isBadRequest());
    }

}