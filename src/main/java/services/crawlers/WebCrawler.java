package services.crawlers;

import org.htmlunit.WebClient;
import org.htmlunit.html.HtmlAnchor;
import org.htmlunit.html.HtmlPage;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class WebCrawler extends Crawler {

    private static WebClient webClient;
    private static HtmlPage page;

    public WebCrawler() { }

    private static WebClient getWebClient() {
        if (webClient == null) {
            webClient = new WebClient();
            webClient.getOptions().setCssEnabled(false);
            webClient.getOptions().setJavaScriptEnabled(false);
            webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
        }
        return webClient;
    }

    @Override
    public LinkedList<String> extractLinks(String url, String xPath) throws IOException {
        LinkedList<String> extractedLinks = new LinkedList<>();
        WebClient client = getWebClient();
        page = client.getPage(url);
        List<HtmlAnchor> links = page.getByXPath(xPath);

        for (HtmlAnchor link : links) {
            if (link != null) {
                String href = link.getHrefAttribute();
                extractedLinks.add(href);
            }
        }
        return extractedLinks;
    }

    @Override
    public String extractContent(String url, String xPath) throws IOException {
        WebClient client = getWebClient();
        page = client.getPage(url);

        return page.getByXPath(xPath).toString();
    }
}
