package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class StatisticsSaverTest {
    private StatisticsSaver saver;

    @BeforeEach
    void setUp() {
        saver = new StatisticsSaver();
    }

    @Test
    void testSaveStatistics() throws IOException {
        AuthorStatistics stats = new AuthorStatistics();
        stats.setAuthorName("Test Author");
        stats.incrementTotalArticles();
        stats.incrementZeroCitationArticles();
        stats.incrementHIndex();
        stats.addZeroCitationDetails("Test Title", "Test Publication");

        String outputFilePath = "test_output.txt";
        saver.saveStatistics(outputFilePath, List.of(stats));

        File file = new File(outputFilePath);
        assertTrue(file.exists());
        assertTrue(file.length() > 0);
        file.delete(); // Clean up test file
    }
}
