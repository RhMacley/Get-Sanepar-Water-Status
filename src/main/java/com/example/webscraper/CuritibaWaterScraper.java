package com.example.webscraper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.net.ssl.*;
import java.io.IOException;
import java.security.cert.X509Certificate;

public class CuritibaWaterScraper {
    private static final String BASE_URL = "https://site.sanepar.com.br/paradas-no-abastecimento?name=Curitiba&as_sfid=AAAAAAYNYV0RaF097TGmQpXhMX1SPu3xNlJg-sSLfvu69218GC4ydtfj1t-MrZK8VtwZifdSMcBvEchz1bqcGp_0k1oNfdT1A8CnGNMkGgMOa1dLWMj4pVwc6JBztjqnkZAcSsw%3D&as_fid=af5df1b6ab8cf28d40652c176449132dfd41d12b";

    private static final String NEIGHBORHOOD = "Boqueirão";


    public static void main(String[] args) {
        disableSSLVerification();

        try {
            System.out.println("Conectando-se a: " + BASE_URL);
            Document doc = Jsoup.connect(BASE_URL)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36")
                    .timeout(30000)
                    .maxBodySize(0)
                    .get();

            System.out.println("Conexão bem-sucedida. Título da página: " + doc.title());

            checkSection(doc, "GRANDE IMPACTO");
            checkSection(doc, "EMERGENCIAL");
            checkSection(doc, "PROGRAMADA");

        } catch (IOException e) {
            System.out.println("Erro ao conectar ou processar a página: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void checkSection(Document doc, String sectionName) {
        System.out.println("Verificando seção: " + sectionName);
        Elements sections = doc.select("h3:contains(" + sectionName + ")");
        System.out.println("Seções encontradas: " + sections.size());

        for (Element section : sections) {
            Element parent = section.parent();
            Elements links = parent.select("a[href]");

            for (Element link : links) {
                String linkText = link.text();
                if (linkText.toLowerCase().contains("curitiba")) {
                    System.out.println("Encontrado 'Curitiba' em " + sectionName + ": " + link.text());
                    String linkHref = link.attr("href");
                    fetchLinkContent("https://site.sanepar.com.br" + linkHref);
                    System.out.println("Acessando conteúdo da página linkada..." + "https://site.sanepar.com.br" + linkHref);
                }
            }
        }
    }

    private static void fetchLinkContent(String url) {
        try {
            Document linkedDoc = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36")
                    .timeout(30000)
                    .get();

            // Selecionar o conteúdo principal da página usando o seletor #content_inner
            Element mainContent = linkedDoc.selectFirst("#content_inner");
            if (mainContent != null) {
                String contentText = mainContent.text();

                // Verificar a presença da palavra "Fanny"
                if (contentText.toLowerCase().contains(NEIGHBORHOOD)) {
                    System.out.println("O bairro " + NEIGHBORHOOD + " foi encontrado no conteúdo da página.");

                    // Busca parágrafos que contenham "reabastecimento" ou "normalização"
                    Elements paragraphs = mainContent.select("p");
                    System.out.println("Verificando parágrafos no conteúdo da página...");

                    for (Element paragraph : paragraphs) {
                        String paragraphText = paragraph.text();
                        if (paragraphText.toLowerCase().contains("reabastecimento") || paragraphText.toLowerCase().contains("normalização") || paragraphText.toLowerCase().contains("abastecimento")) {
                            System.out.println("INFORMAÇÃO: " + paragraphText);
                        }
                    }
                } else {
                    System.out.println("O bairro " + NEIGHBORHOOD + " não foi encontrado no conteúdo da página.");
                }
            } else {
                System.out.println("Conteúdo principal não encontrado.");
            }
        } catch (IOException e) {
            System.out.println("Erro ao conectar ou processar a página linkada: " + e.getMessage());
            e.printStackTrace();
        }
    }


    private static void disableSSLVerification() {
        try {
            TrustManager[] trustAllCerts = new TrustManager[] {
                    new X509TrustManager() {
                        public X509Certificate[] getAcceptedIssuers() { return null; }
                        public void checkClientTrusted(X509Certificate[] certs, String authType) { }
                        public void checkServerTrusted(X509Certificate[] certs, String authType) { }
                    }
            };

            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

            HostnameVerifier allHostsValid = (hostname, session) -> true;
            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
