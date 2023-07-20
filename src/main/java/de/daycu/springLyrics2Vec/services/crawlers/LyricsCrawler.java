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
    private final WebClient webClient;

    @Autowired
    private RecordService service;
    @Autowired
    private WebCrawler crawler;

    public LyricsCrawler() {
        webClient = new WebClient();
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setJavaScriptEnabled(false);
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
        webClient.getOptions().setRedirectEnabled(true);
    }

    public void crawlLyrics() throws IOException {
        HtmlPage homepage = webClient.getPage(HOME_URL);
        List<HtmlAnchor> letterLinks = homepage.getByXPath(LETTERS_LINKS_XPATH);

        for (HtmlAnchor letterLink : letterLinks) {
            if (letterLink != null) {
                crawlArtists(letterLink);
            }
        }
    }

    public void crawlArtists(HtmlAnchor letterLink) throws IOException {
        HtmlPage letterPage = letterLink.click();
        List<HtmlAnchor> artistLists = letterPage.getByXPath(ALL_ARTISTS_XPATH);

        for (HtmlAnchor artistList : artistLists) {
            HtmlPage artistPage = artistList.click();
            List<HtmlAnchor> artists = artistPage.getByXPath(ARTIST_LINKS_XPATH);

            for (HtmlAnchor artist : artists) {
                System.out.println(artist);
                crawlSongs(artist);
            }
        }
    }

    public void crawlSongs(HtmlAnchor artist) throws IOException {
        HtmlPage artistPage = artist.click();
        List<HtmlAnchor> songs = artistPage.getByXPath(SONG_LINKS_XPATH);

        for (HtmlAnchor song : songs) {
            Record recordSet = extractRecord(song);
            System.out.println(recordSet);
        }
    }

    private Record extractRecord(HtmlAnchor song) throws IOException {
        HtmlPage songPage = song.click();
        List<String> recordXPaths = List.of(ARTIST_LINKS_XPATH, SONG_LINKS_XPATH, LYRICS_XPATH);
        String artistName = songPage.getByXPath(ARTIST_NAME_XPATH).toString();
        String songName = songPage.getByXPath(SONG_NAME_XPATH).toString();
        String lyrics = songPage.getByXPath(LYRICS_XPATH).toString();

        return service.save(
                new Record(artistName, songName, lyrics));
    }
}

