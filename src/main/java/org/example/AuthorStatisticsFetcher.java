package org.example;

import io.github.bonigarcia.wdm.WebDriverManager;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.IOException;
import java.util.*;


public class AuthorStatisticsFetcher {
    private static final Logger logger = LogManager.getLogger(AuthorStatisticsFetcher.class);
    private final Map<String, String> cookieMap = new HashMap<>();

    public Map<String, String> getCookies() {
        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver();

        try {
            driver.get(Util.BASE_URL);
            Thread.sleep(5000);

            Set<Cookie> cookies = driver.manage().getCookies();
            for (Cookie cookie : cookies) {
                cookieMap.put(cookie.getName(), cookie.getValue());
            }

            logger.info("Полученные куки: " + cookieMap);
        } catch (InterruptedException e) {
            logger.error("Ошибка при получении куков", e);
        } finally {
            driver.quit();
        }

        return cookieMap;
    }

    public AuthorStatistics fetchAuthorStatistics(String authorId) throws IOException {
        if (authorId == null || authorId.isEmpty()) {
            throw new IllegalArgumentException("ID автора не может быть пустым");
        }

        AuthorStatistics statistics = new AuthorStatistics();
        int pageNum = 1;
        boolean hasMorePages = true;

        while (hasMorePages) {
            String url = Util.BASE_URL + "/author_items.asp?authorid=" + authorId + "&pubrole=100&show_refs=1&show_option=0&pagenum=" + pageNum;

            logger.info("Выполняется запрос к URL: " + url);

            Response response = RestAssured.given()
                    .cookies(cookieMap)
                    .headers(Util.DEFAULT_HEADERS)
                    .get(url);

            if (response.statusCode() != 200) {
                logger.error("Ошибка получения данных для автора " + authorId + ", статус код: " + response.statusCode());
                throw new IOException("Ошибка HTTP: " + response.statusCode());
            }

            Document doc = Jsoup.parse(response.getBody().asString());

            // Сохранение имени автора
            if (statistics.getAuthorName() == null) {
                Element authorNameElement = doc.selectFirst(Util.NAME_SELECTOR);
                if (authorNameElement != null) {
                    statistics.setAuthorName(authorNameElement.text());
                } else {
                    throw new IOException("Имя автора не найдено для ID: " + authorId);
                }
            }

            processPage(doc, statistics);

            hasMorePages = doc.select("a[title=Следующая страница]").size() > 0;
            pageNum++;
        }

        return statistics;
    }

    private void processPage(Document doc, AuthorStatistics statistics) {
        Element table = doc.selectFirst("table#restab");

        if (table != null) {
            Elements rows = table.select("tr");
            for (Element row : rows) {
                Element titleCell = row.selectFirst("td:nth-child(2) span a b span");
                Element publicationCell = row.selectFirst("td:nth-child(2) > span > font:nth-child(5)");
                Element citationCell = row.selectFirst("td.select-tr-right");

                if (titleCell == null || publicationCell == null || citationCell == null) continue;

                statistics.incrementTotalArticles();

                String title = titleCell.text();
                String publication = publicationCell.text();
                int citations;
                try {
                    citations = Integer.parseInt(citationCell.text().trim());
                } catch (NumberFormatException e) {
                    logger.warn("Ошибка при разборе количества цитирований: " + citationCell.text());
                    continue;
                }

                if (citations == 0) {
                    statistics.incrementZeroCitationArticles();
                    statistics.addZeroCitationDetails(title, publication);
                }

                if (citations >= statistics.getTotalArticles()) {
                    statistics.incrementHIndex();
                }
            }
        } else {
            logger.warn("Таблица публикаций не найдена на странице");
        }
    }
}