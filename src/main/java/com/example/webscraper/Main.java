package com.example.webscraper;

import com.example.webscraper.services.WebScraperService;
import com.example.webscraper.utils.SSLHelper;

public class Main {
    public static void main(String[] args) {
        SSLHelper.disableSSLVerification();
        WebScraperService scraper = new WebScraperService();
        scraper.scrapeWaterStatus();
    }
}
