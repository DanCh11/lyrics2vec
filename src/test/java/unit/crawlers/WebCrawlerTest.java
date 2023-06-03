package unit.crawlers;

import static org.junit.Assert.assertTrue;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.junit.Test;
import services.crawlers.WebCrawler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;


public class WebCrawlerTest {
    private static final String XPATH_PROPERTIES_PATH = "src/main/resources/properties/xpath.properties";
    private static final String ASSERTION_PROPERTIES_PATH = "src/test/resources/properties/assertion.properties";
    private static final String MOCK_HOME_PAGE_FILEPATH =
            "file:///" + System.getProperty("user.dir") + "/src/test/resources/pages/homePage.html";
    private static final String MOCK_ARTIST_PAGE_LETTER_O =
            "file:///" + System.getProperty("user.dir") + "/src/test/resources/pages/artistsPageLetterO.html";
    private static final String MOCK_ARTIST_PAGE_LETTER_G =
            "file:///" + System.getProperty("user.dir") + "/src/test/resources/pages/artistsPageLetterG.html";
    private static final String MOCK_LYRICS_PAGE =
            "file:///" + System.getProperty("user.dir") + "/src/test/resources/pages/songPage.html";

    @Test
    public void extractLinksFromOnePageTest() throws IOException, ConfigurationException {
        Configuration xPathConfig = getConfig(XPATH_PROPERTIES_PATH);

        List<String> extractedLinks = crawler().extractLinks(
                xPathConfig.getString("xpath.letterLinks"));

        assertEquals(extractedLinks.size(), 29);
        assertTrue(extractedLinks.get(0).contains("/artists/0"));
        assertTrue(extractedLinks.get(28).contains("/random.php"));
        assertTrue(extractedLinks.get(17).contains("/artists/Q"));
    }

    @Test
    public void extractLinksFromMultiplePagesTest() throws IOException, ConfigurationException {
        final Configuration xPathConfig = getConfig(XPATH_PROPERTIES_PATH);
        final String authorLinksPath = xPathConfig.getString("xpath.artistLinks");

        List<String> pages = List.of(MOCK_ARTIST_PAGE_LETTER_O, MOCK_ARTIST_PAGE_LETTER_G);
        List<String> links = crawler().extractLinksFromMultiplePages(pages, authorLinksPath);

        assertEquals(links.getClass(), ArrayList.class);
        assertEquals(links.get(0).getClass(), String.class);
        assertEquals(links.size(), 48);
        assertTrue(links.get(0).contains("artist/O-A-M-Trio/512644"));
        assertTrue(links.get(47).contains("/G-Bravo/2138159325"));
    }

    @Test
    public void extractContentTest() throws ConfigurationException, IOException {
        Configuration xPathConfig = getConfig(XPATH_PROPERTIES_PATH);
        Configuration assertionConfig = getConfig(ASSERTION_PROPERTIES_PATH);

        String text = crawler().extractContent(
                MOCK_LYRICS_PAGE,
                xPathConfig.getString("xpath.lyricsExample"));

        assertEquals(text.getClass(), String.class);
        assertTrue(text.contains(assertionConfig.getString("assertion.containingTextContent")));

    }

    private static WebCrawler crawler() {
        return new WebCrawler(WebCrawlerTest.MOCK_HOME_PAGE_FILEPATH);
    }

    private static Configuration getConfig(String configPath) throws ConfigurationException {
        Configurations configs = new Configurations();

        return configs.properties(configPath);
    }
}
