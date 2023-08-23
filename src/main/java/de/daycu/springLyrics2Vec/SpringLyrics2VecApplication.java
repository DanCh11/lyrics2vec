package de.daycu.springLyrics2Vec;

import de.daycu.springLyrics2Vec.services.crawlers.LyricsCrawler;
import de.daycu.springLyrics2Vec.services.crawlers.WebCrawler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

//@SpringBootApplication
public class SpringLyrics2VecApplication {

	public static void main(String[] args) throws IOException {
//		SpringApplication.run(SpringLyrics2VecApplication.class, args);

		LyricsCrawler crawler = new LyricsCrawler();

		crawler.crawlLyrics();
	}

}
