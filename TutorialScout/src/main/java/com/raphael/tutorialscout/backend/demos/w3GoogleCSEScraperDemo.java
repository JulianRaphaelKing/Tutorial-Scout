package com.raphael.tutorialscout.backend.demos;

import com.raphael.tutorialscout.backend.w3GoogleCSEScrapper;

import java.io.IOException;
import java.util.List;

public class w3GoogleCSEScraperDemo {

    public static void main(String[] args) {
        // Create an instance of w3GoogleCSEScrapper
        w3GoogleCSEScrapper scraper = new w3GoogleCSEScrapper();

        // Example search query
        String query = "python array";

        try {
            // Perform the search
            List<w3GoogleCSEScrapper.SearchResult> results = scraper.search(query);

            // Print the results
            System.out.println("Search results for: " + query);
            for (w3GoogleCSEScrapper.SearchResult result : results) {
                System.out.println("Title: " + result.title);
                System.out.println("Link: " + result.link);
                System.out.println("Snippet: " + result.snippet);
                //System.out.println("Full Text: " + result.fullText);
                System.out.println("=====================================");
            }
            System.out.println("Full Text: " + results.get(0).fullText);

        } catch (IOException e) {
            System.err.println("An error occurred while performing the search: " + e.getMessage());
        }
    }
}