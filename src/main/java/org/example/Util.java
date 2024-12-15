package org.example;

import java.util.Map;

public class Util {
    public static final String BASE_URL = "https://elibrary.ru";

    public static final Map<String, String> DEFAULT_HEADERS = Map.of(
            "User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/131.0.0.0 Safari/537.36",
            "Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7",
            "Accept-Encoding", "gzip, deflate, br, zstd",
            "Accept-Language", "ru-RU,ru;q=0.9,en-US;q=0.8,en;q=0.7",
            "Cache-Control", "max-age=0",
            "Connection", "keep-alive",
            "DNT", "1",
            "Referer", "https://elibrary.ru/defaultx.asp",
            "Upgrade-Insecure-Requests", "1"
    );
    public static final String NAME_SELECTOR = "#thepage > table > tbody > tr > td > table:nth-child(1) > tbody > tr > td:nth-child(2) > form > table > tbody > tr:nth-child(3) > td:nth-child(1) > table > tbody > tr > td > div:nth-child(1) > font > b";
}
