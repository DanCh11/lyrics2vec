package services.crawlers;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;

import java.io.IOException;
import java.util.List;

public class LyricsCrawler {

    private static final String XPATH_PROPERTIES_PATH = "src/main/resources/properties/xpath.properties";
    private static final String HOMA_PAGE_URL = "https://www.lyrics.com/";
    private final WebCrawler webCrawler = new WebCrawler();

    public void crawl() throws ConfigurationException, IOException {
        List<String> alphabeticalLinks = extractAlphabeticalLinks();

        List<String> authorsPages = extractLinksFromAuthorsPages(alphabeticalLinks);
        List<String> songsPages = extractLinksFromSongsPages(authorsPages);

    }

    private List<String> extractAlphabeticalLinks() throws ConfigurationException, IOException {
        final String xpath = getXPathConfig().getString("xpath.alphabeticalLinks");

        return webCrawler.extractLinks(HOMA_PAGE_URL, xpath);
    }

    private List<String> extractLinksFromAuthorsPages(List<String> URLS) throws ConfigurationException, IOException {
        final String xpath = getXPathConfig().getString("xpath.authorLinks");

        return webCrawler.extractLinksFromMultiplePages(URLS, xpath);
    }

    private List<String> extractLinksFromSongsPages(List<String> URLS) throws ConfigurationException, IOException {
        final String xpath = getXPathConfig().getString("xpath.songLinks");

        return webCrawler.extractLinksFromMultiplePages(URLS, xpath);
    }

    private String extractAuthorName(List<String> URLS) {
        return null;
    }

    private String extractAlbumName() {
        return null;
    }

    private String extractSongName() {
        return null;
    }

    private String extractLyrics() {
        return null;
    }

    private static Configuration getXPathConfig() throws ConfigurationException {
        Configurations configs = new Configurations();

        return configs.properties(XPATH_PROPERTIES_PATH);
    }
}
