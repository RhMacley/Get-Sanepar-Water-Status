package com.example.webscraper.services;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class SectionCheckerService {
    public List<String> checkSection(Document doc, String sectionName) {
        List<String> links = new ArrayList<>();
        Elements sections = doc.select("h3:contains(" + sectionName + ")");
        for (Element section : sections) {
            Element parent = section.parent();
            Elements linkElements = parent.select("a[href]");
            for (Element link : linkElements) {
                String linkText = link.text();
                if (linkText.toLowerCase().contains("curitiba")) {
                    links.add("https://site.sanepar.com.br" + link.attr("href"));
                }
            }
        }
        return links;
    }
}