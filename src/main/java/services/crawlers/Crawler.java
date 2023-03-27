package services.crawlers;

import java.io.IOException;
import java.util.LinkedList;

public abstract class Crawler {
    public abstract LinkedList<String> extractLinks(String url, String xPath) throws IOException;
    public abstract String extractContent(String url, String xPath) throws IOException;

}
