package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class AuthorStatisticsFetcherTest {
    private AuthorStatisticsFetcher fetcher;

    @BeforeEach
    void setUp() {
        fetcher = new AuthorStatisticsFetcher();
    }

    @Test
    void testGetCookies() {
        Map<String, String> cookies = fetcher.getCookies();
        assertFalse(cookies.isEmpty(), "Куки не должны быть пусты");
    }

    @Test
    void testFetchAuthorStatisticsValidId() throws IOException {
        fetcher.getCookies();
        AuthorStatistics stats = fetcher.fetchAuthorStatistics("1086593");
        assertNotNull(stats);
        assertNotNull(stats.getAuthorName());
        assertTrue(stats.getTotalArticles() > 0);
    }

    @Test
    void testFetchAuthorStatisticsInvalidId() {
        fetcher.getCookies();
        assertThrows(IOException.class, () -> fetcher.fetchAuthorStatistics("invalid"));
    }
}