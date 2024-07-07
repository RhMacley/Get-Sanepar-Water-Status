package com.example.webscraper.aplication.repositories.webContentRepository;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class WebContentRepository {
    public Document fetchContent(String url) throws IOException {
        return Jsoup.connect(url)
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36")
                .header("Accept-Language", "en-US,en;q=0.5")
                .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
                .header("Connection", "keep-alive")
                .timeout(60000)
                .maxBodySize(10 * 1024 * 1024)
                .post();
    }
}
