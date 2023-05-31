package services.crawlers;

import org.htmlunit.WebClient;
import org.htmlunit.html.HtmlAnchor;
import org.htmlunit.html.HtmlPage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * This class uses a web client to extract necessary information from given website.
 */
public class WebCrawler {
    private static WebClient webClient = getWebClient();
    private static HtmlPage page;

    private static WebClient getWebClient() {
        if (webClient == null) {
            webClient = new WebClient();
            webClient.getOptions().setCssEnabled(false);
            webClient.getOptions().setJavaScriptEnabled(false);
            webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
        }
        return webClient;
    }

    /**
     * Extracts links from given URL and XPath.
     * @param URL given URL
     * @param xPath XPath expression to given HTML Element
     * @return a list of extracted links.
     */
    public List<String> extractLinks(String URL, String xPath) throws IOException {
        List<String> extractedLinks = new ArrayList<>();
        page = webClient.getPage(URL);
        List<HtmlAnchor> links = page.getByXPath(xPath);

        for (HtmlAnchor link : links) {
            if (link != null) {
                String href = link.getHrefAttribute();
                extractedLinks.add(URL + href);
            }
        }
        return extractedLinks;
    }

    /**
     * Extracts links from multiple given URLS and XPath.
     * @param URLS given list of urls
     * @param xPath XPath expression to given HTML Element
     * @return a list of extracted links.
     */
    public List<String> extractLinksFromMultiplePages(List<String> URLS, String xPath) throws IOException {
        List<String> extractedLinks = new ArrayList<>();

        for (String URL : URLS) {
            page = webClient.getPage(URL);
            List<HtmlAnchor> links = page.getByXPath(xPath);

            for (HtmlAnchor link : links) {
                if (link != null) {
                    String href = link.getHrefAttribute();
                    extractedLinks.add(URL + href);
                }
            }
        }
        return extractedLinks;
    }

    /**
     * Extracts the required data from given URL and XPath expression.
     * @param URL given url
     * @param xPath XPath expression to given HTML Element
     *
     * @return found data from given arguments
     */
    public String extractContent(String URL, String xPath) throws IOException {
        page = webClient.getPage(URL);

        return page.getByXPath(xPath).toString();
    }
}
