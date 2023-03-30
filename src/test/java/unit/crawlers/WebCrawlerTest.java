package unit.crawlers;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.junit.Test;
import services.crawlers.WebCrawler;

import java.io.IOException;
import java.util.ArrayList;

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
        Configuration assertionConfig = getConfig(ASSERTION_PROPERTIES_PATH);
        Configuration xPathConfig = getConfig(XPATH_PROPERTIES_PATH);

        ArrayList<String> extractedLinks = crawler(MOCK_HOME_PAGE_FILEPATH).extractLinks(
                xPathConfig.getString("xpath.alphabeticalLinks"));

        assertEquals(extractedLinks.size(), 29);
        assertEquals(
                extractedLinks.get(0),
                assertionConfig.getString("assertion.firstPageSingleExtractionTest"));
        assertEquals(
                extractedLinks.get(28),
                assertionConfig.getString("assertion.lastPageSingleExtractionTest"));
        assertEquals(
                extractedLinks.get(17),
                assertionConfig.getString("assertion.pageNr17SingleExtractionTest"));
    }

    @Test
    public void extractLinksFromMultiplePagesTest() throws IOException, ConfigurationException {
        Configuration xPathConfig = getConfig(XPATH_PROPERTIES_PATH);
        Configuration assertionConfig = getConfig(ASSERTION_PROPERTIES_PATH);

        ArrayList<String> extractedLinks = new ArrayList<>();
        String[] pages = {MOCK_ARTIST_PAGE_LETTER_O, MOCK_ARTIST_PAGE_LETTER_G};

        for (String page : pages) {
            ArrayList<String> links = crawler(page).extractLinks(xPathConfig.getString("xpath.authorLinks"));

            extractedLinks.addAll(links);
        }

        assertEquals(extractedLinks.getClass(), ArrayList.class);
        assertEquals(extractedLinks.get(0).getClass(), String.class);
        assertEquals(extractedLinks.size(), 48);
        assertEquals(extractedLinks.get(0),
                assertionConfig.getString("assertion.firstPageMultipleExtractionTest"));
        assertEquals(extractedLinks.get(47),
                assertionConfig.getString("assertion.lastPageMultipleExtractionTest"));
    }

    @Test
    public void extractContentTest() throws ConfigurationException, IOException {
        Configuration xPathConfig = getConfig(XPATH_PROPERTIES_PATH);
        Configuration assertionConfig = getConfig(ASSERTION_PROPERTIES_PATH);

        String text = crawler(MOCK_LYRICS_PAGE).extractContent(xPathConfig.getString("xpath.lyricsExample"));

        assertEquals(text.getClass(), String.class);
        assertTrue(text.contains(assertionConfig.getString("assertion.containingTextContent")));

    }

    private static WebCrawler crawler(String URL) {
        return new WebCrawler(URL);
    }

    private static Configuration getConfig(String configPath) throws ConfigurationException {
        Configurations configs = new Configurations();

        return configs.properties(configPath);
    }
}
