package org.example;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class StatisticsSaver {
    private static final Logger logger = LogManager.getLogger(StatisticsSaver.class);

    public void saveStatistics(String outputFilePath, List<AuthorStatistics> statisticsList) throws IOException {
        logger.info("Сохранение статистики в файл: " + outputFilePath);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath))) {
            for (AuthorStatistics statistics : statisticsList) {
                writer.write("Author: " + statistics.getAuthorName());
                writer.newLine();
                writer.write("Total Articles: " + statistics.getTotalArticles());
                writer.newLine();
                writer.write("Zero Citation Articles: " + statistics.getZeroCitationArticles());
                writer.newLine();
                writer.write("H-Index: " + statistics.getHIndex());
                writer.newLine();

                writer.write("Zero Citation Articles Details:");
                writer.newLine();
                for (ArticleDetail detail : statistics.getZeroCitationDetails()) {
                    writer.write("  Title: " + detail.getTitle());
                    writer.newLine();
                    writer.write("  Publication: " + detail.getPublication());
                    writer.newLine();
                }

                writer.newLine();
                writer.newLine();
            }
        } catch (IOException e) {
            logger.error("Ошибка сохранения статистики", e);
            throw e;
        }
    }
}

