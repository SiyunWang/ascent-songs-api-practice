package com.siyun.songs;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/songs")
public class SongsController {
    private SongsService songsService;

    public SongsController(SongsService songsService) {
        this.songsService = songsService;
    }

    @GetMapping
    public ResponseEntity<SongsList> getSongs(@RequestParam(required = false) String artist,
                                              @RequestParam(required = false) String album) {
        SongsList songsList;
        if (artist == null && album == null) {
            songsList = songsService.getSongs();
        } else if (artist != null && album != null) {
            songsList = songsService.getSongs(artist, album);
        } else if (artist != null) {
            songsList = songsService.getSongsByArtist(artist);
        } else {
            songsList = songsService.getSongsByAlbum(album);
        }

        return songsList.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(songsList);
    }

    @PostMapping
    public ResponseEntity<Song> addSong(@RequestBody Song song) {
        try {
            Song added = songsService.addSong(song);
           return ResponseEntity.ok(added);
        } catch (InvalidSongException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("{songCode}")
    public ResponseEntity<Song> getSongBySongCode(@PathVariable String songCode) {
        Song song = songsService.getSongBySongCode(songCode);
        return song == null ? ResponseEntity.noContent().build() : ResponseEntity.ok(song);
    }
}
