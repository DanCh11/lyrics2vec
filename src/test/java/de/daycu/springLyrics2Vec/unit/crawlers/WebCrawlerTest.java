package de.daycu.springLyrics2Vec.unit.crawlers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import de.daycu.springLyrics2Vec.services.crawlers.WebCrawler;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class WebCrawlerTest {
	private static final String MOCK_HOME_PAGE =
			new File("src/test/java/resources/pages/homepage.html").toURI().toString();
	private static final String MOCK_ARTIST_PAGE_LETTER_O =
			new File("src/test/java/resources/pages/artistPageLetterO.html").toURI().toString();
	private static final String MOCK_ARTIST_PAGE_LETTER_G =
			new File("src/test/java/resources/pages/artistPageLetterG.html").toURI().toString();
	private static final String MOCK_SONG_PAGE =
			new File("src/test/java/resources/pages/songPage.html").toURI().toString();
	private static final String LETTERS_LINKS_XPATH = "//*[@id='search-frm']/div[2]/a";
	private static final String ARTIST_LINKS_XPATH = "//*[@id='content-body']/div/div[2]/table/tbody/tr/td/a";
	private static final String LYRICS_XPATH = "//*[@id='lyric-body-text']/text()";
	private static final int EXTRACTED_LINKS_LIST_SIZE = 29;
	private static final String EXTRACTED_FIRST_ENDPOINT = "/artists/0";
	private static final String EXTRACTED_LAST_ENDPOINT = "/random.php";
	private static final String EXTRACTED_Q_ARTISTS_ENDPOINT = "/artists/Q";
	private static final int MULTIPLE_PAGES_EXTRACTED_SIZE = 45;
	private static final String MULTIPLE_PAGES_EXTRACTED_FIRST_ARTIST = "artist/O-A-M-Trio/512644";
	private static final String MULTIPLE_PAGES_EXTRACTED_LAST_ARTIST = "/G-Benny/2138110657";
	private static final String SONG_LYRICS = "Rosa pra se ver Pra";

	private WebCrawler crawler;


	@Before
	public void setup() {
		crawler = new WebCrawler();
	}
	
	@Test
	public void extractLinksFromOnePageTest() throws IOException {
		List<String> links = crawler.extractLinks(MOCK_HOME_PAGE, LETTERS_LINKS_XPATH);

		assertEquals(links.size(), EXTRACTED_LINKS_LIST_SIZE);
		assertTrue(links.get(0).contains(EXTRACTED_FIRST_ENDPOINT));
		assertTrue(links.get(28).contains(EXTRACTED_LAST_ENDPOINT));
		assertTrue(links.get(17).contains(EXTRACTED_Q_ARTISTS_ENDPOINT));
	}

	@Test
	public void extractLinksFromMultiplePagesTest() throws IOException {
		List<String> pages = List.of(MOCK_ARTIST_PAGE_LETTER_O, MOCK_ARTIST_PAGE_LETTER_G);
		List<String> links = crawler.extractLinksFromMultiplePages(pages, ARTIST_LINKS_XPATH);

		assertEquals(links.getClass(), ArrayList.class);
		assertEquals(links.get(0).getClass(), String.class);
		assertEquals(links.size(), MULTIPLE_PAGES_EXTRACTED_SIZE);
		assertTrue(links.get(0).contains(MULTIPLE_PAGES_EXTRACTED_FIRST_ARTIST));
		assertTrue(links.get(44).contains(MULTIPLE_PAGES_EXTRACTED_LAST_ARTIST));
	}

	@Test
	public void extractContentTest() throws IOException {
        String text = crawler.extractContent(MOCK_SONG_PAGE, LYRICS_XPATH);

        assertEquals(text.getClass(), String.class);
        assertTrue(text.contains(SONG_LYRICS));
    }
}
