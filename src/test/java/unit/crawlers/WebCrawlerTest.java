package unit.crawlers;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.junit.Test;
import services.crawlers.WebCrawler;

import java.io.IOException;
import java.util.LinkedList;

import static org.junit.Assert.*;


public class WebCrawlerTest {
    private static final String XPATH_CONFIG_FILE_PATH = "src/main/resources/properties/xpath.properties";
    private static final String MOCK_HOME_PAGE_FILEPATH =
            "file:///" + System.getProperty("user.dir") + "/src/test/resources/pages/homePage.html";
    private static final String MOCK_ARTIST_PAGE_LETTER_O =
            "file:///" + System.getProperty("user.dir") + "/src/test/resources/pages/artistsPageLetterO.html";
    private static final String MOCK_ARTIST_PAGE_LETTER_G =
            "file:///" + System.getProperty("user.dir") + "/src/test/resources/pages/artistsPageLetterG.html";
    private static final String MOCK_LYRICS_PAGE =
            "file:///" + System.getProperty("user.dir") + "/src/test/resources/pages/songPage.html";

    private static Configurations configs;

    @Test
    public void extractLinksFromOnePageTest() throws IOException, ConfigurationException {
        Configuration xPathConfig = getConfig();

        LinkedList<String> extractedLinks = crawler().extractLinks(
                MOCK_HOME_PAGE_FILEPATH,
                xPathConfig.getString("xpath.alphabeticalLinks"));

        assertEquals(extractedLinks.size(), 29);
        assertEquals(extractedLinks.getFirst(), "https://www.lyrics.com/artists/0");
        assertEquals(extractedLinks.getLast(), "https://www.lyrics.com/random.php");
        assertEquals(extractedLinks.get(17), "https://www.lyrics.com/artists/Q");
    }

    @Test
    public void extractLinksFromMultiplePagesTest() throws IOException, ConfigurationException {
        Configuration xPathConfig = getConfig();

        LinkedList<String> extractedLinks = new LinkedList<>();
        String[] pages = {MOCK_ARTIST_PAGE_LETTER_O, MOCK_ARTIST_PAGE_LETTER_G};

        for (String page : pages) {
            LinkedList<String> links = crawler().extractLinks(page, xPathConfig.getString("xpath.authorLinks"));

            extractedLinks.addAll(links);
        }

        assertEquals(extractedLinks.getClass(), LinkedList.class);
        assertEquals(extractedLinks.getFirst().getClass(), String.class);
        assertEquals(extractedLinks.size(), 48);
        assertEquals(extractedLinks.getFirst(), "https://www.lyrics.com/artist/O-A-M-Trio/512644");
        assertEquals(extractedLinks.getLast(), "https://www.lyrics.com/artist/G-Bravo/2138159325");
    }

    @Test
    public void extractContentTest() throws ConfigurationException, IOException {
        Configuration xPathConfig = getConfig();

        String text = crawler().extractContent(
                MOCK_LYRICS_PAGE,
                xPathConfig.getString("xpath.lyricsExample"));

        assertEquals(text.getClass(), String.class);
        assertTrue(text.contains("Pretty, girl Lookin in my eyes"));

    }

    private static WebCrawler crawler() {
        return new WebCrawler();
    }

    private static Configuration getConfig() throws ConfigurationException {
        Configuration config = null;
        if (configs == null) {
            configs = new Configurations();
            config = configs.properties(WebCrawlerTest.XPATH_CONFIG_FILE_PATH);
        }
        return config;
    }
}
