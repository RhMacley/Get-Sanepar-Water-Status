package com.example.webscraper.services;

import com.example.webscraper.domain.entities.WaterSupplyInfo;
import com.example.webscraper.utils.Constants;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class ContentAnalyzerService {
    public List<WaterSupplyInfo> analyzeContent(Document doc, String neighborhood) {
        List<WaterSupplyInfo> infoList = new ArrayList<>();
        Element mainContent = doc.selectFirst("#content_inner");
        if (mainContent != null) {
            String contentText = mainContent.text();
            if (contentText.toLowerCase().contains(neighborhood.toLowerCase())) {
                infoList.addAll(analyzeWaterSupplyInfo(mainContent, neighborhood));
            }
        }
        return infoList;
    }

    private List<WaterSupplyInfo> analyzeWaterSupplyInfo(Element mainContent, String neighborhood) {
        List<WaterSupplyInfo> infoList = new ArrayList<>();
        Elements paragraphs = mainContent.select("p");
        List<String> keywords = Constants.KEYWORDS;

        for (Element paragraph : paragraphs) {
            String paragraphText = paragraph.text().toLowerCase();
            if (keywords.stream().anyMatch(paragraphText::contains)) {
                WaterSupplyInfo info = new WaterSupplyInfo();
                info.setNeighborhood(neighborhood);
                info.setStatus("Afetado");
                info.setDetails(paragraph.text());
                infoList.add(info);
            }
        }
        return infoList;
    }
}
