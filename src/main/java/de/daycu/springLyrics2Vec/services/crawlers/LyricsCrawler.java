package de.daycu.springLyrics2Vec.services.crawlers;

import de.daycu.springLyrics2Vec.models.Record;
import de.daycu.springLyrics2Vec.services.RecordService;
import lombok.AllArgsConstructor;
import org.htmlunit.WebClient;
import org.htmlunit.html.HtmlAnchor;
import org.htmlunit.html.HtmlPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
@AllArgsConstructor
public class LyricsCrawler {

    private static final String HOME_URL = "https://www.lyrics.com/";

    private static final String LETTERS_LINKS_XPATH = "//*[@id='search-frm']/div[2]/a";
    private static final String ALL_ARTISTS_XPATH = "//*[@id=\"content-body\"]/div/div[2]/div[1]/div/a[13]";
    private static final String ARTIST_LINKS_XPATH = "//*[@id='content-body']/div/div[2]/table/tbody/tr/td/a";
    private static final String SONG_LINKS_XPATH = "//*[@id=\"content-body\"]/div[3]/div/table/tbody/tr/td/strong/a";
    private static final String ARTIST_NAME_XPATH = "//*[@id=\"content-body\"]/div[1]/div[1]/hgroup/h3/text()";
    private static final String SONG_NAME_XPATH = "//*[@id='lyric-title-text']/text()";
    private static final String LYRICS_XPATH = "//*[@id='lyric-body-text']/text()";

    @Autowired
    private RecordService service;
    @Autowired
    private WebCrawler crawler;

    public LyricsCrawler() {
        crawler = new WebCrawler();
    }

    public void crawlLyrics() throws IOException {
        List<String> letterLinks = crawler.extractLinks(HOME_URL, LETTERS_LINKS_XPATH);

        for (String letterLink : letterLinks) {
            System.out.println("Letter link: " + letterLink);
            if (letterLink != null) {
                crawlArtists(letterLink);
            }
        }
    }

    public void crawlArtists(String letterLink) throws IOException {
        List<String> artistLists = crawler.extractLinks(letterLink, ALL_ARTISTS_XPATH);

        for (String artistList : artistLists) {
            List<String> artists = crawler.extractLinks(artistList, ARTIST_LINKS_XPATH);

            for (String artist : artists) {
                crawlSongs(artist);
            }
        }
    }

    public void crawlSongs(String artist) throws IOException {
        List<String> songs = crawler.extractLinks(artist, SONG_LINKS_XPATH);

        for (String song : songs) {
            System.out.println("Song: " + song);
            Record recordSet = extractRecord(song);
            System.out.println(recordSet);
        }
    }

    private Record extractRecord(String song) throws IOException {
//        HtmlPage songPage = song.click();
//        List<String> recordXPaths = List.of(ARTIST_LINKS_XPATH, SONG_LINKS_XPATH, LYRICS_XPATH);
        List<HtmlAnchor> elements = crawler.extractContent(song, ARTIST_NAME_XPATH);
        System.out.println("Song link from extractRecord: " + song);
        System.out.println("Artist name: " + elements.get(0).asNormalizedText());

//        elements = crawler.extractContent(song, SONG_NAME_XPATH);
//        String songName = elements.get(1).asNormalizedText();
//
//        elements = crawler.extractContent(song, LYRICS_XPATH);
//        String lyrics = elements.get(1).asNormalizedText();

//        return service.save(
//                new Record(artistName, songName, lyrics));

        return new Record("", "", "");
    }
}

