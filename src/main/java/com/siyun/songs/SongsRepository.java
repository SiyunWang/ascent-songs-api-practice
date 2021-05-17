package com.siyun.songs;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SongsRepository extends JpaRepository<Song, Long> {
    List<Song> findByArtistContainsAndAlbumContains(String artist, String album);
    List<Song> findByArtistContains(String artist);
    List<Song> findByAlbumContains(String album);
}
