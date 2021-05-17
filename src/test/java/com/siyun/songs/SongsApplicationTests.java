package com.siyun.songs;

import org.aspectj.lang.annotation.After;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SongsApplicationTests {
	@Autowired
	TestRestTemplate testRestTemplate;
	@Autowired
	SongsRepository songsRepository;

	List<Song> songs;
	@BeforeEach
	void setup() {
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



}
