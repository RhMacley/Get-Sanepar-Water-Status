package com.example.webscraper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class WebScraper {
    public static void main(String[] args) {
        String url = "https://site.sanepar.com.br/paradas-no-abastecimento"; // Substitua pela URL do site que você quer fazer scraping

        try {
            Document doc = Jsoup.connect(url).get();

            // Exemplo: pegar todos os títulos h1 da página
            Elements h1Elements = doc.select("h1");

            for (Element h1 : h1Elements) {
                System.out.println("Título encontrado: " + h1.text());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}