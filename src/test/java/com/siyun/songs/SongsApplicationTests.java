package com.siyun.songs;

import org.aspectj.lang.annotation.After;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SongsApplicationTests {

	@Autowired
	SongsRepository songsRepository;

	@Autowired
	TestRestTemplate testRestTemplate;

	List<Song> songs;

	@BeforeEach
	void setup() {
		songs = new ArrayList<>();
		songs.add(new Song("Hey Jude", "The Beatles", "The Beatles Again", "HTT538"));
		songs.add(new Song("Blinding Lights", "Weekend", "After Hours", "BWA985"));
		songs.add(new Song("Lady Madonna", "The Beatles", "The Beatles Again", "LTT234"));
		songs.add(new Song("Come Together", "The Beatles", "Abbey Road", "CTT464"));
		songs.add(new Song("Your Power", "Billie Eilish", "Happier Than Ever", "YBH564"));
		songs.add(new Song("Pretty Savage", "Blackpink", "The Album", "PBT223"));
		songs.add(new Song("Sweet Melody", "Little Mix", "Confetti", "SLC887"));
		songs.add(new Song("How You Like That", "Blackpink", "The Album", "HBT177"));
		songs.add(new Song("Everything I Wanted", "Billie Eilish", "Everything I Wanted", "EBE369"));
		songs.add(new Song("Tattooed Heart", "Ariana Grande", "Yours Truly", "TAY886"));
		songsRepository.saveAll(songs);
	}

	@AfterEach
	void tearDown() {
		songsRepository.deleteAll();
	}

	@Test
	void contextLoads() {
	}

	@Test
	void getSongs_noParams_returnsSongsList_NotEmpty() {
		ResponseEntity<SongsList> response = testRestTemplate.getForEntity("/api/songs", SongsList.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody()).isNotNull();
		assertThat(response.getBody().isEmpty()).isFalse();
		assertThat(response.getBody().getSongs().size()).isEqualTo(10);
	}

	@Test
	void getSongs_noParams_returnsNoContent_IfEmpty() {
		songsRepository.deleteAll();
		ResponseEntity<SongsList> response = testRestTemplate.getForEntity("/api/songs", SongsList.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
		assertThat(response.getBody()).isNull();
	}

	@Test
	void getSongs_withArtistAndAlbum_returnsSongsList_NotEmpty() throws Exception {
		ResponseEntity<SongsList> response = testRestTemplate.getForEntity("/api/songs?artist=The Beatles&album=Abbey Road", SongsList.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody()).isNotNull();
		assertThat(response.getBody().isEmpty()).isFalse();
		assertThat(response.getBody().getSongs().size()).isEqualTo(1);
	}

	@Test
	void getSongs_withArtist_returnsSongsList_NotEmpty() throws Exception {
		ResponseEntity<SongsList> response = testRestTemplate.getForEntity("/api/songs?artist=Weekend", SongsList.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody()).isNotNull();
		assertThat(response.getBody().isEmpty()).isFalse();
		assertThat(response.getBody().getSongs().size()).isEqualTo(1);
	}

	@Test
	void getSongs_withAlbum_returnsSongsList_NotEmpty() throws Exception {
		ResponseEntity<SongsList> response = testRestTemplate.getForEntity("/api/songs?album=The Album", SongsList.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody()).isNotNull();
		assertThat(response.getBody().isEmpty()).isFalse();
		assertThat(response.getBody().getSongs().size()).isEqualTo(2);
	}

	@Test
	void getSongs_withParams_returnsNoContent_IfEmpty() throws Exception {
		ResponseEntity<SongsList> response = testRestTemplate.getForEntity("/api/songs?artist=Weekend&album=Abbey Road", SongsList.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
		assertThat(response.getBody()).isNull();
	}

}
