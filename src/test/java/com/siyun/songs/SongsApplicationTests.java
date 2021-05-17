package com.siyun.songs;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.test.context.TestPropertySource;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations= "classpath:application-test.properties")
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

	@Test
	void addSong_returnsAddedSong_forValidRequest() throws Exception {
		Song newSong = new Song("Bad Guy", "Billie Eilish", "When We All Fall Asleep, Where Do We Go?", "BBW721");
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<Song> request = new HttpEntity<>(newSong, headers);
		ResponseEntity<Song> response = testRestTemplate.postForEntity("/api/songs", request, Song.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody()).isNotNull();
		assertThat(response.getBody().getName()).isEqualTo("Bad Guy");

	}

	@Test
	void addSong_returnsBadRequest_forInvalidRequest() throws Exception {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<Object> request = new HttpEntity<>("newSong", headers);
		ResponseEntity<Song> response = testRestTemplate.postForEntity("/api/songs", request, Song.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
	}

	@Test
	void getSongBySongCode_returnsSong_IfFound() throws Exception {
		ResponseEntity<Song> response = testRestTemplate.getForEntity("/api/songs/HTT538", Song.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody()).isNotNull();
		assertThat(response.getBody().getSongCode()).isEqualTo("HTT538");
	}

	@Test
	void getSongBySongCode_returnsNoContent_IfNotFound() throws Exception {
		ResponseEntity<Song> response = testRestTemplate.getForEntity("/api/songs/HTT539", Song.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
		assertThat(response.getBody()).isNull();
	}

	@Test
	void updateSong_returnsUpdatedSong_forSuccessfulUpdate() {
		testRestTemplate.getRestTemplate().setRequestFactory(new HttpComponentsClientHttpRequestFactory());
		SongUpdate update = new SongUpdate(4, true);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<SongUpdate> request = new HttpEntity<>(update, headers);

		ResponseEntity<Song> response = testRestTemplate.exchange("/api/songs/HTT538", HttpMethod.PATCH, request, Song.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody()).isNotNull();
		assertThat(response.getBody().getRating()).isEqualTo(4);
		assertThat(response.getBody().isLiked()).isTrue();
	}

	@Test
	void updateSong_returnsNoContent_IfTargetNotFound() {
		testRestTemplate.getRestTemplate().setRequestFactory(new HttpComponentsClientHttpRequestFactory());
		SongUpdate update = new SongUpdate(4, true);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<SongUpdate> request = new HttpEntity<>(update, headers);

		ResponseEntity<Song> response = testRestTemplate.exchange("/api/songs/HTT539", HttpMethod.PATCH, request, Song.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
		assertThat(response.getBody()).isNull();
	}

	@Test
	void deleteSong_returnsAccepted_ForSuccessfulDelete() {
		testRestTemplate.getRestTemplate().setRequestFactory(new HttpComponentsClientHttpRequestFactory());
		ResponseEntity<?> response = testRestTemplate.exchange("/api/songs/HTT538", HttpMethod.DELETE, new HttpEntity<>(""), Void.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.ACCEPTED);
		assertThat(response.getBody()).isNull();
	}

	@Test
	void deleteSong_returnsNoContent_IfTargetNotFound() {
		testRestTemplate.getRestTemplate().setRequestFactory(new HttpComponentsClientHttpRequestFactory());
		ResponseEntity<?> response = testRestTemplate.exchange("/api/songs/HTT539", HttpMethod.DELETE, new HttpEntity<>(""), Void.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
		assertThat(response.getBody()).isNull();
	}



}
