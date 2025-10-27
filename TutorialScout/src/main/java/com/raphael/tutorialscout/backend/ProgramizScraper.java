package com.raphael.tutorialscout.backend;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * This class scrapes the Programiz website for tutorials related to a search query.
 * It uses Jsoup to parse the search results and extract relevant metadata including title,
 * link, snippet, and full content text from matched tutorial pages.
 */
public class ProgramizScraper {

    // Base URL used to construct the search request
    private static final String BASE_SEARCH_URL = "https://www.programiz.com/search/";
    private static final String BASE_URL = "https://www.programiz.com";

    public static class ProgramizResult {
        public String title;
        public String link;
        public String snippet;
        public String fullText;

        public ProgramizResult(String title, String link, String snippet, String fullText) {
            this.title = title;
            this.link = link;
            this.snippet = snippet;
            this.fullText = fullText;
        }

        // Getters
        public String getTitle() { return title; }
        public String getLink() { return link; }
        public String getSnippet() { return snippet; }
        public String getFullText() { return fullText; }

        @Override
        public String toString() {
            return title + "\n" + link + "\n" + snippet + "\n";
        }
    }

    /**
     * Searches Programiz, parses the result page, and returns a list of matched tutorials.
     *
     * @param query the search term entered by the user
     * @return a list of ProgramizResult objects (each containing title, link, snippet, and full text)
     * @throws IOException if the search or page loading fails
     */
    public List<ProgramizResult> search(String query) throws IOException {
        List<ProgramizResult> results = new ArrayList<>();

        // Replace spaces with "+"
        String formattedQuery = query.replace(" ", "+");
        String searchUrl = BASE_SEARCH_URL + formattedQuery;

        // Load the search results page
        Document doc = Jsoup.connect(searchUrl).get();

        // Select all search result containers
        Elements elements = doc.select("div.search-result");

        // If there is no results — return empty list
        if (elements.isEmpty()) {
            return results;
        }

        // Iterate over each result block
        for (Element element : elements) {
            Elements rows = element.select("div.search-result__row");

            for (Element row : rows) {
                // Extract title and link
                Element titleElements = row.selectFirst("a");
                Element snippetElements = row.selectFirst("p");

                if (titleElements != null && snippetElements != null) {
                    String title = titleElements.text();
                    String link = titleElements.attr("href");

                    // Confirm that all the links are valid
                    if (!link.startsWith("http")) {
                        link = BASE_URL + link;
                    }

                    // Filter out links to paid courses, masterclasses, or unrelated pro content
                    if (link.contains("learn") || link.contains("master") || link.contains("course") || link.contains("pro/learn")) {
                        continue;
                    }

                    String snippet = snippetElements.text();
                    String fullText = "";

                    // Try to extract full page content if the link is valid
                    try {
                        if (!link.contains("learn")) {
                            Document resultPage = Jsoup.connect(link).get();
                            Elements contentDivs = resultPage.select("div.content");

                            for (Element contentDiv : contentDivs) {
                                fullText = contentDiv != null ? contentDiv.text() : "[Content not found]";
                            }
                        }
                    } catch (IOException e) {
                        fullText = "[Could not load full content]";
                    }

                    // Add result to the list
                    results.add(new ProgramizResult(title, link, snippet, fullText));
                }
            }
        }

        return results;
    }
}
