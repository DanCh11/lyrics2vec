package services.crawlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import models.Song;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.htmlunit.BrowserVersion;
import org.htmlunit.WebClient;
import org.htmlunit.html.HtmlAnchor;
import org.htmlunit.html.HtmlPage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LyricsCrawler {
    private final Configurations configs = new Configurations();
    private static final String XPATH_CONFIG_FILE_PATH = "src/main/resources/properties/xpath.properties";
    private static final String FILEPATH_CONFIG_FILE_PATH = "src/main/resources/properties/filepath.properties";
    private static final String HTML_ELEMENTS_CONFIG_FILE_PATH = "src/main/resources/properties/HTMLElement.properties";
    private final Configuration xpathConfig = configs.properties(new File(XPATH_CONFIG_FILE_PATH));
    private final Configuration filepathConfig = configs.properties(new File(FILEPATH_CONFIG_FILE_PATH));
    private final Configuration htmlElementsConfig = configs.properties(new File(HTML_ELEMENTS_CONFIG_FILE_PATH));
    private final String HOME_URL = xpathConfig.getString("xpath.url.homeURL");
    private final String ALPHABETICAL_LINKS_XPATH = xpathConfig.getString("xpath.alphabeticalLinks");
    private final String AUTHOR_LINKS_XPATH = xpathConfig.getString("xpath.authorLinks");
    private final String SONGS_LINKS_XPATH = xpathConfig.getString("xpath.songLinks");
    private final String ALL_ARTISTS_ENDPOINT = xpathConfig.getString("xpath.endpoint.allArtists");
    private final String SONG_NAME_TAG_ID = htmlElementsConfig.getString("HTMLElement.songName.tagID");
    private final String SONG_LYRICS_TAG_ID = htmlElementsConfig.getString("HTMLElement.songLyrics.tagID");
    private final String JSON_FILEPATH = filepathConfig.getString("filepath.songsJSON");

    private HtmlPage page;

    public LyricsCrawler() throws IOException, ConfigurationException {;
        page = getWebPage(HOME_URL);

    }

    /**
     * Address the Web Client and its parameters.
     * @param url the link that will be transformed into HTMLPage object.
     *
     * @return HTMLPage Object from given URL
     */
    private HtmlPage getWebPage(String url) throws IOException {
        try (WebClient webClient = new WebClient(BrowserVersion.EDGE)) {
            webClient.getOptions().setJavaScriptEnabled(false);
            webClient.getOptions().setCssEnabled(false);

            return webClient.getPage(url);
        }
    }

    /**
     * Extracts alphabetical links from home page.
     *
     * @return a list of extracted links
     */
    private List<String> extractAlphabeticalLinks() {
        List<String> extractedLinks = new ArrayList<>();
        List<HtmlAnchor> links = page.getByXPath(ALPHABETICAL_LINKS_XPATH);

        for (HtmlAnchor link : links) {
            if (link != null) {
                String href = link.getHrefAttribute();
                extractedLinks.add(HOME_URL + href + ALL_ARTISTS_ENDPOINT);
            }
        }
        return extractedLinks;
    }

    /**
     * Extracts links to authors' pages from alphabetical links.
     *
     * @return a list with URLs from given list.
     */
    private List<String> extractAuthorLinks() throws IOException {
        List<String> extractedLinks = new ArrayList<>();

        for (String url : extractAlphabeticalLinks()) {
            page = getWebPage(url);
            List<HtmlAnchor> links = page.getByXPath(AUTHOR_LINKS_XPATH);

            for (HtmlAnchor link : links) {
                if (link != null) {
                    String href = link.getHrefAttribute();
                    extractedLinks.add(HOME_URL + href);
                }
            }
        }
        return extractedLinks;
    }

    /**
     * Extracts links to songs' pages from urls of authors.
     *
     * @return a list with URLs from given list.
     */
    private List<String> extractSongsLinks() throws IOException {
        List<String> extractedLinks = new ArrayList<>();

        for (String url : extractAuthorLinks()) {
            page = getWebPage(url);
            List<HtmlAnchor> links = page.getByXPath(SONGS_LINKS_XPATH);

            for (HtmlAnchor link : links) {
                if (link != null) {
                    String href = link.getHrefAttribute();
                    extractedLinks.add(HOME_URL + href);
                }
            }
        }
        return extractedLinks;
    }

    public void extractContent(List<String> urls) throws IOException {

        for (String url : urls) {
            Song song = new Song();
            page = getWebPage(url);

            song.setName(page.getHtmlElementById(SONG_NAME_TAG_ID).asNormalizedText());
            song.setLyrics(page.getElementById(SONG_LYRICS_TAG_ID).asNormalizedText());

            ObjectMapper mapper = new ObjectMapper();

            mapper.writeValue(new File(JSON_FILEPATH), song);

        }
    }
}
