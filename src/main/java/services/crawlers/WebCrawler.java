package services.crawlers;

import org.htmlunit.WebClient;
import org.htmlunit.html.HtmlAnchor;
import org.htmlunit.html.HtmlPage;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * This class uses a web client to extract necessary information from given website.
 */
public class WebCrawler {
    private static WebClient webClient;
    private static HtmlPage page;
    private final String URL;

    public WebCrawler(String URL) {
        this.URL = URL;
    }

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
     * @param xPath XPath expression to given HTML Element
     * @return a list of extracted links.
     */
    public LinkedList<String> extractLinks(String xPath) throws IOException {
        LinkedList<String> extractedLinks = new LinkedList<>();
        WebClient client = getWebClient();
        page = client.getPage(URL);
        List<HtmlAnchor> links = page.getByXPath(xPath);

        for (HtmlAnchor link : links) {
            if (link != null) {
                String href = link.getHrefAttribute();
                extractedLinks.add(href);
            }
        }
        return extractedLinks;
    }

    /**
     * Extracts the required data from given URL and XPath expression.
     * @param xPath XPath expression to given HTML Element
     *
     * @return found data from given arguments
     */
    public String extractContent(String xPath) throws IOException {
        WebClient client = getWebClient();
        page = client.getPage(URL);

        return page.getByXPath(xPath).toString();
    }
}
