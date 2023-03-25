package services.crawlers;

import org.htmlunit.BrowserVersion;
import org.htmlunit.WebClient;
import org.htmlunit.html.HtmlAnchor;
import org.htmlunit.html.HtmlPage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LyricsCrawler {
    private static final String HOME_URL = "https://www.lyrics.com/";
    private HtmlPage page;

    public LyricsCrawler() throws IOException {
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
     * Extracts links links from a specific location of a single URL.
     *
     * @param xPath xPath to href values
     * @return a list of extracted links
     */
    public List<String> extractLinksFromSinglePage(String xPath) {
        List<String> extractedLinks = new ArrayList<>();
        List<HtmlAnchor> links = page.getByXPath(xPath);

        for (HtmlAnchor link : links) {
            if (link != null) {
                String href = link.getHrefAttribute();
                extractedLinks.add(HOME_URL + href);
            }
        }
        return extractedLinks;
    }

    /**
     * Extracts links from a specific location of multiple URLs.
     * @param urls list of URLs.
     * @param xPath XPATH to specific location where are hrefs.
     *
     * @return a list with URLs from given list.
     */
    public List<String> extractLinksFromMultiplePages(List<String> urls, String xPath) throws IOException {
        List<String> extractedLinks = new ArrayList<>();

        for (String url : urls) {
            page = getWebPage(url);
            List<HtmlAnchor> links = page.getByXPath(xPath);

            for (HtmlAnchor link : links) {
                if (link != null) {
                    String href = link.getHrefAttribute();
                    extractedLinks.add(HOME_URL + href);
                }
            }
        }
        return extractedLinks;
    }
}
