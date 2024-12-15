package org.example;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    private static final Logger logger = LogManager.getLogger(Main.class);

    public static void main(String[] args) {
        String inputFilePath = "author.txt"; // Путь к файлу с authorId

        AuthorIdReader reader = new AuthorIdReader();
        AuthorStatisticsFetcher fetcher = new AuthorStatisticsFetcher();
        StatisticsSaver saver = new StatisticsSaver();

        logger.info("Инициализация получения Cookies");
        fetcher.getCookies();

        try {
            String outputFilePath = "statistics.txt";
            // Проверяем и создаем выходной файл, если он не существует
            File outputFile = new File(outputFilePath);
            if (outputFile.createNewFile()) {
                logger.info("Файл создан: " + outputFile.getName());
            } else {
                logger.warn("Файл уже существует: " + outputFile.getName());
            }

            // Чтение IDs авторов из файла
            logger.info("Чтение списка авторов из файла: " + inputFilePath);
            List<String> authorIds = reader.readAuthorIds(inputFilePath);

            List<AuthorStatistics> statisticsList = new ArrayList<>();

            // Обработка каждого автора
            for (String authorId : authorIds) {
                logger.info("Начало обработки автора с ID: " + authorId);
                try {
                    AuthorStatistics stats = fetcher.fetchAuthorStatistics(authorId);
                    statisticsList.add(stats);
                    logger.info("Успешно обработан автор с ID: " + authorId);
                } catch (IOException e) {
                    logger.error("Ошибка при обработке автора с ID: " + authorId, e);
                }
            }

            // Сохранение статистики
            logger.info("Сохранение статистики в файл: " + outputFilePath);
            saver.saveStatistics(outputFilePath, statisticsList);
            logger.info("Статистика успешно сохранена в " + outputFilePath);

        } catch (IOException e) {
            logger.fatal("Критическая ошибка при выполнении программы", e);
        }
    }
}