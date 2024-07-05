package com.example.webscraper.services;

import com.example.webscraper.aplication.repositories.webContentRepository.WebContentRepository;
import com.example.webscraper.domain.entities.WaterSupplyInfo;
import com.example.webscraper.utils.Constants;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WebScraperService {
    private final WebContentRepository contentRepository;
    private final SectionCheckerService sectionChecker;
    private final ContentAnalyzerService contentAnalyzer;

    public WebScraperService() {
        this.contentRepository = new WebContentRepository();
        this.sectionChecker = new SectionCheckerService();
        this.contentAnalyzer = new ContentAnalyzerService();
    }

    public void scrapeWaterStatus() {
        try {
            Document doc = contentRepository.fetchContent(Constants.BASE_URL);
            System.out.println("Conexão bem-sucedida");

            List<WaterSupplyInfo> allInfo = new ArrayList<>();
            for (String section : Constants.SECTIONS) {
                List<String> links = sectionChecker.checkSection(doc, section);
                for (String link : links) {
                    allInfo.addAll(fetchLinkContent(link));
                }
            }

            if(allInfo.isEmpty()) {
                System.out.println("Não foi encontrado nenhum conteúdo para o bairro requisitado.");
            }

            for (WaterSupplyInfo info : allInfo) {
                System.out.println(info.getNeighborhood() + ": " + info.getDetails());
            }
        } catch (IOException e) {
            System.out.println("Erro ao conectar ou processar a página: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private List<WaterSupplyInfo> fetchLinkContent(String url) throws IOException {
        Document linkedDoc = contentRepository.fetchContent(url);
        return contentAnalyzer.analyzeContent(linkedDoc, Constants.NEIGHBORHOOD);
    }
}
