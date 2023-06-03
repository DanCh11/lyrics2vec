package services.crawlers;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.htmlunit.WebClient;
import org.htmlunit.html.HtmlAnchor;
import org.htmlunit.html.HtmlPage;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LyricsCrawler {
    private static final String XPATH_PROPERTIES_PATH = "src/main/resources/properties/xpath.properties";
    private static WebClient webClient;

    public LyricsCrawler() {
        webClient = getWebClient();
    }

    public void crawlLyrics() {
        try {
            final String homeURL = getConfig().getString("url.homeURL");
            final String lettersXPath = getConfig().getString("xpath.letterLinks");
            HtmlPage homepage = webClient.getPage(homeURL);
            List<HtmlAnchor> letterLinks = homepage.getByXPath(lettersXPath);

            for (HtmlAnchor letterLink : letterLinks) {
                if (letterLink != null) {
                    crawlArtists(letterLink);
                }
            }

        } catch (ConfigurationException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void crawlArtists(HtmlAnchor letterLink) {
        try {
            final String allArtistsEndpoint = getConfig().getString("xpath.endpoint.allArtists");
            final String artistLinksXPath = getConfig().getString("xpath.artistLinks");
            HtmlPage letterPage = letterLink.click();
            List<HtmlAnchor> artistLists = letterPage.getByXPath(allArtistsEndpoint);

            for (HtmlAnchor artistList : artistLists) {
                HtmlPage artistPage = artistList.click();
                List<HtmlAnchor> artists = artistPage.getByXPath(artistLinksXPath);

                for (HtmlAnchor artist : artists) {
                    crawlSongs(artist);
                }
            }

        } catch (ConfigurationException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void crawlSongs(HtmlAnchor artist) {
        try {
            final String songLinksXPath = getConfig().getString("xpath.songLinks");
            HtmlPage artistPage = artist.click();
            List<HtmlAnchor> songs = artistPage.getByXPath(songLinksXPath);

            for (HtmlAnchor song : songs) {
                Map<String, List<String>> recordSet = extractRecord(song);
                System.out.println(recordSet);
            }

        } catch (ConfigurationException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Map<String, List<String>> extractRecord(HtmlAnchor song) {
        try {
            Map<String, List<String>> recordSet = new HashMap<>();

            final String artistNameXPath = getConfig().getString("xpath.artistName");
            final String songNameXPath = getConfig().getString("xpath.songName");
            final String lyricsXPath = getConfig().getString("xpath.lyrics");

            HtmlPage songPage = song.click();
            String artistName = songPage.getByXPath(artistNameXPath).toString();
            String songName = songPage.getByXPath(songNameXPath).toString();
            String lyrics = songPage.getByXPath(lyricsXPath).toString();

            recordSet.put(artistName, List.of(songName, lyrics));

            return recordSet;

        } catch (ConfigurationException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static WebClient getWebClient() {
        if (webClient == null) {
            webClient = new WebClient();
            webClient.getOptions().setCssEnabled(false);
            webClient.getOptions().setJavaScriptEnabled(false);
            webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
            webClient.getOptions().setRedirectEnabled(true);
        }
        return webClient;
    }

    private static Configuration getConfig() throws ConfigurationException {
        Configurations config = new Configurations();

        return config.properties(XPATH_PROPERTIES_PATH);
    }
}
