package com.raphael.tutorialscout.backend.api;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * ApyHubSummaryClient provides functionality to summarize large text blocks
 * using the ApyHub AI Summarization API.
 *
 * Note: This class is not currently active in Tutorial Scout because of the API's free usage limits.
 * It is kept for future use when switching to another plan or an alternate provider.
 */
public class ApyHubSummaryClient {

    // ApyHub API key
    public static final String API_KEY = "APY0ImlfgqTRBFk2a3G81Gu1b3FbPsbmeIerZqdeb5c2nwHvyBmG2JvpVksUqauUshXws";

    // API endpoint for text summarization
    public static final String API_URL = "https://api.apyhub.com/ai/summarize-text";

    // HTTP client instance for sending requests
    public final HttpClient httpClient;

    public ApyHubSummaryClient() {
        this.httpClient = HttpClient.newHttpClient();
    }

    /**
     * Sends a text string to the ApyHub API and returns a concise summary.
     *
     * @param inText the full text to be summarized
     * @return the summarized version of the text, as returned by the API
     * @throws IOException if the request fails or the API returns an error response
     * @throws InterruptedException if the HTTP request is interrupted during execution
     */
    public String summarizeText(String inText) throws IOException, InterruptedException {
        // Prepare the JSON with the "text" key
        JsonObject body = new JsonObject();
        body.addProperty("text", inText);

        // Build the HTTP POST request
        HttpRequest request = HttpRequest.newBuilder()
                .uri(java.net.URI.create(API_URL))
                .header("Content-Type", "application/json")
                .header("apy-token", API_KEY)  // Auth token
                .POST(HttpRequest.BodyPublishers.ofString(body.toString()))
                .build();

        // Send the request and receive the response
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        // Parse and return the summary if successful
        if (response.statusCode() == 200) {
            JsonObject responseJson = JsonParser.parseString(response.body()).getAsJsonObject();
            return responseJson.getAsJsonObject("data").get("summary").getAsString();
        } else {
            // Throw an error if summarization failed
            throw new IOException("Failed to summarize text. Status: " + response.statusCode());
        }
    }
}
