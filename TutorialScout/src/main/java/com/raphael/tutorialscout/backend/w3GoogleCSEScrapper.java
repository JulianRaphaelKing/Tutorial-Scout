package com.raphael.tutorialscout.backend;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.json.JSONObject;
import org.json.JSONArray;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * This class handles searching w3Schools content using the Google Custom Search Engine (CSE) API.
 * It fetches search results for a given search query and scrapes full-page content.
 */
public class w3GoogleCSEScrapper {
    //Personal Google Custom Search API key
    private static final String API_KEY = "AIzaSyBed7pJJdx23NqmW7vpguXqCeeQzYVJ4tY";

    // Custom Search Engine ID configured to search w3Schools
    private static final String CX = "9351a81c0a6724359";

    // HTTP client used to send requests
    private final OkHttpClient client = new OkHttpClient();

    public static class SearchResult {
        public String title;
        public String link;
        public String snippet;
        public String fullText;

        public SearchResult(String title, String link, String snippet, String fullText) {
            this.title = title;
            this.link = link;
            this.snippet = snippet;
            this.fullText = fullText;
        }

        public String getTitle() {
            return title;
        }
        public String getLink() {
            return link;
        }
        public String getSnippet() {
            return snippet;
        }
        public String getFullText() {
            return fullText;
        }

        @Override
        public String toString() {
            return title + "\n" + link + "\n" + snippet + "\n" + fullText + "\n";
        }
    }

    /**
     * Performs a search on w3Schools using the CSE API and returns a list of SearchResult objects.
     *
     * @param query the user search query
     * @return a list of search results with full content text
     * @throws IOException if the request or parsing fails
     */

    public List<SearchResult> search(String query) throws IOException {
        List<SearchResult> results = new ArrayList<>();

        // Encode the search query for use in the URL
        String encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8);
        String url = "https://www.googleapis.com/customsearch/v1?q=" + encodedQuery +
                "&key=" + API_KEY + "&cx=" + CX;

        // Build and send the request
        Request request = new Request.Builder().url(url).build();

        // Execute the request
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            // Parse the JSON response
            String json = response.body().string();
            JSONObject jsonObject = new JSONObject(json);

            // If no results found — return empty list
            if (!jsonObject.has("items")) {
                return results;
            }

            // Get results from the "items" array
            JSONArray items = jsonObject.getJSONArray("items");

            // Limit the results to the top 5 for cleaner presentation
            // This help eliminate filler or non-related content
            for (int i = 0; i < 5; i++) {
                JSONObject item = items.getJSONObject(i);
                String title = item.getString("title");
                String link = item.getString("link");
                String snippet = item.optString("snippet", "");

                // Grab full page text
                String fullText = extractFullText(link);

                results.add(new SearchResult(title, link, snippet, fullText));
            }
        }

        return results;
    }

    /**
     * Connects to a link and extracts the readable full text from the main content section.
     *
     * @param url the link to scrape
     * @return the full page text (or an error message)
     */
    private String extractFullText(String url) {
        try {
            // Connect and parse HTML document
            Document doc = Jsoup.connect(url).userAgent("Mozilla/5.0").get();

            // Target the main div
            Elements content = doc.select("div#main");
            StringBuilder fullTextBuilder = new StringBuilder();

            // Append all readable text from selected elements
            for (Element el : content) {
                fullTextBuilder.append(el.text()).append("\n");
            }
            return fullTextBuilder.toString();
        } catch (IOException e) {
            return "[Could not load full content]";
        }
    }
}