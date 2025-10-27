package com.raphael.tutorialscout.ui;

import com.raphael.tutorialscout.backend.ProgramizScraper;
import com.raphael.tutorialscout.backend.w3GoogleCSEScrapper;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

/**
 * TutorialScout is a JavaFX application that allows users to search
 * for programming tutorials across multiple websites using filters for
 * language and difficulty level. Results are displayed in styled cards.
 *
 * Sources currently supported: w3Schools, Programiz
 * -- with more to be included in the future (e.g., "geeksForGeeks", etc.)
 *
 */

public class TutorialScout extends Application {
    // Private static variables

    // Keeps track of selected tag and language filters
    private static String tagPill;
    private static String langPill;

    // Booleans for managing conditional logic during search
    private static boolean tagFound = false;
    private static boolean langFound = false;
    private static boolean w3DataFound = false;
    private static boolean programizDataFound = false;

    // User input string (search query)
    private static String inputText = "";


    // Launch application
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // Load necessary fonts
        Font.loadFont(getClass().getResource("/fonts/FiraSansExtraCondensed-Regular.ttf").toExternalForm(), 12);
        Font.loadFont(getClass().getResource("/fonts/FiraSansExtraCondensed-BoldItalic.ttf").toExternalForm(), 12);
        Font.loadFont(getClass().getResource("/fonts/NewsCycle-Bold.ttf").toExternalForm(), 12);
        Font.loadFont(getClass().getResource("/fonts/NewsCycle-Regular.ttf").toExternalForm(), 12);

        // Full width top bar
        Region topBar = new Region();
        topBar.getStyleClass().add("top-bar-style");
        topBar.setMaxWidth(Double.MAX_VALUE);

        // Title
        Label titleLabel = new Label("Tutorial Scout");
        titleLabel.getStyleClass().add("title-label");

        // Textfield
        TextField inputField = new TextField();
        inputField.setPromptText("enter your text here");
        inputField.getStyleClass().add("custom-text-field");

        // Search btn
        Button searchButton = new Button("SEARCH");
        searchButton.getStyleClass().add("search-button");

        // HBox to hold search bar (textfield + search btn)
        HBox searchBar = new HBox(10, inputField, searchButton);
        searchBar.setAlignment(Pos.CENTER);

        // HBox to hold two filter cards
        HBox filtersRow = new HBox(30);

        // VBox to hold Tags and Languages Filter Card
        VBox tagsAndLang = new VBox();
        tagsAndLang.getStyleClass().addAll("filter-card", "filter-box-tags-languages");

        // HBox to hold Tags and Languages Headers
        HBox header = new HBox();
        header.getStyleClass().add("filter-card-header");
        header.setSpacing(100);
        header.setPadding(new Insets(8, 12, 8, 12));

        // Tags and Languages Headers
        Label tagsHeader = new Label("Tags");
        tagsHeader.getStyleClass().add("filter-card-header-text");
        Label langHeader = new Label("Language");
        langHeader.getStyleClass().add("filter-card-header-text");

        header.getChildren().addAll(tagsHeader, langHeader);

        // HBox to hold body of Tags and Languages
        HBox body = new HBox (30);
        body.getStyleClass().add("filter-card-body");

        // Checkboxes Of Tags (difficulty)
        CheckBox beginner = new CheckBox("Beginner");
        CheckBox moderate = new CheckBox("Moderate");
        CheckBox advanced = new CheckBox("Advanced");
        beginner.getStyleClass().add("custom-checkbox");
        moderate.getStyleClass().add("custom-checkbox");
        advanced.getStyleClass().add("custom-checkbox");

        // Group checkboxes of tags into an array
        CheckBox[] tagArr = {beginner, moderate, advanced};

        // Only allow user to select one tag
        for (CheckBox cb : tagArr) {
            cb.selectedProperty().addListener((obs, wasSelected, isNowSelected) -> {
                if (isNowSelected) {
                    for (CheckBox other : tagArr) {
                        if (other != cb) {
                            other.setSelected(false);
                        }
                    }
                }
            });
        }



        // VBox to hold tags checkboxes
        VBox tagsCol = new VBox(10);
        tagsCol.getChildren().addAll(beginner, moderate, advanced);



        // Languages checkboxes
        CheckBox Java = new CheckBox("Java");
        CheckBox Python = new CheckBox("Python");
        CheckBox JavaScript = new CheckBox("JavaScript");
        Java.getStyleClass().add("custom-checkbox");
        Python.getStyleClass().add("custom-checkbox");
        JavaScript.getStyleClass().add("custom-checkbox");

        // Group checkboxes of Languages into an array
        CheckBox[] langArr = {Java, Python, JavaScript};

        // Only allow user to select one language
        for (CheckBox cb : langArr) {
            cb.selectedProperty().addListener((obs, wasSelected, isNowSelected) -> {
                if (isNowSelected) {
                    for (CheckBox other : langArr) {
                        if (other != cb) {
                            other.setSelected(false);
                        }
                    }
                }
            });
        }


        // VBox to hold language CheckBoxes
        VBox langCol = new VBox(10);
        langCol.getChildren().addAll(Java, Python, JavaScript);

        // Add the checkboxes to the body of the filter card
        body.getChildren().addAll(tagsCol, langCol);

        // Add the header and the body HBoxes to the VBox
        // that holds the Tags and Languages FilterCard
        tagsAndLang.getChildren().addAll(header, body);

        // VBox to hold the filter card for the list of websites
        VBox filterCard = new VBox();
        filterCard.getStyleClass().addAll("filter-card", "filter-box-websites");

        // HBox to hold the header of the filter card
        HBox headerTags = new HBox();
        headerTags.getStyleClass().add("filter-card-header");

        // List Of Websites Header
        Label headerLabel = new Label("Websites");
        headerLabel.getStyleClass().add("filter-card-header-text");

        headerTags.getChildren().addAll(headerLabel);

        // VBox to hold the body of the website filter card
        VBox filterContent = new VBox(10);

        // Websites Checkboxes
        CheckBox w3 = new CheckBox("w3School.com");
        w3.getStyleClass().add("custom-checkbox");
        CheckBox Programiz = new CheckBox("Programiz.com");
        Programiz.getStyleClass().add("custom-checkbox");
        CheckBox AnotherWebsite = new CheckBox("AnotherWebsite.com");
        AnotherWebsite.getStyleClass().add("custom-checkbox");

        // Add website checkboxes to the body of the website filter card
        filterContent.getChildren().addAll(w3, Programiz, AnotherWebsite);
        filterContent.getStyleClass().add("filter-card-body");

        // Add the header and the body to the VBox
        // that holds the Websites FilterCard
        filterCard.getChildren().addAll(headerTags, filterContent);

        // Add both the website filter card and the Tags and Languages
        // To the HBox that holds all filter cards
        filtersRow.getChildren().addAll(filterCard, tagsAndLang);
        filtersRow.setAlignment(Pos.CENTER);

        // Create VBox to hold all result cards, with spacing, padding, and center alignment
        VBox resultsContainer = new VBox(15);
        resultsContainer.setPadding(new Insets(10));
        resultsContainer.setAlignment(Pos.TOP_CENTER);

        createResultCard(resultsContainer, 0, langPill, tagPill, "Welcome to Tutorial Scout.", "Find the right coding tutorials, faster. Search, filter, and learn—your way.", "");

        // Create scroll pane, which holds the resultsContainer
        ScrollPane scrollPane = new ScrollPane(resultsContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setPrefHeight(400); // Adjust height as needed
        scrollPane.getStyleClass().add("results-scroll-pane");

        // Main search logic — triggered on search button click
        // - Clears previous results
        // - Builds search query using selected filters
        // - Calls the scrapers for each selected website
        // - Dynamically generates and displays result cards
        searchButton.setOnAction(event -> {
            // Clear previous results and variables before running a new query
            resultsContainer.getChildren().clear();
            langFound = false;
            tagFound = false;
            w3DataFound = false;
            programizDataFound = false;
            inputText = "";

            // Get the search text from the search bar
            inputText = inputField.getText();

            // Iterate through the tags and languages arrays
            for (int i = 0; i < 3; i++) {
                // Get the selected tag, if any
                if(tagArr[i].isSelected()){
                    tagPill = tagArr[i].getText();
                    tagFound = true;
                }
                // Get the selected language if any
                if(langArr[i].isSelected()){
                    langPill = langArr[i].getText();
                    langFound = true;
                }

            }

            // If a language filter is found
            // then add it to the search query if it's not already included
            if(langFound){
                if(!inputText.contains(langPill)){
                    inputText = inputText + " " + langPill;
                }
            }

            // If a difficulty tag is found
            // then add it to the search query if it's not already included
            if(tagFound){
                if(!inputText.contains(tagPill)){
                    inputText = inputText + " " + tagPill;
                }
            }

            // Counter for results
            int resultCount = 1;

            // If user selected the w3Schools website
            // call scrapper and generate a results card for each result
            if(w3.isSelected()){
                w3GoogleCSEScrapper w3scrap = new w3GoogleCSEScrapper();
                try{
                    List<w3GoogleCSEScrapper.SearchResult> results = w3scrap.search(inputText);
                    if(!results.isEmpty()){
                        for(w3GoogleCSEScrapper.SearchResult result : results) {
                            createResultCard(resultsContainer, resultCount, langPill, tagPill, result.getTitle(), result.getSnippet(), result.getLink());
                            resultCount++;
                            w3DataFound = true;
                        }
                    }
                } catch (IOException e){
                    System.out.println("Error: " + e.getMessage());
                }

            }
            // If user selected the programiz website
            // call scrapper and generate a results card for each result
            if(Programiz.isSelected()){
                ProgramizScraper programizScrap = new ProgramizScraper();
                try {
                    List<ProgramizScraper.ProgramizResult> results = programizScrap.search(inputText);
                    if (!results.isEmpty()) {
                        // Limit Programiz results to the top 5 for cleaner presentation
                        for (int i = 0; i < 5; i++){
                            createResultCard(resultsContainer, resultCount, langPill, tagPill, results.get(i).getTitle(), results.get(i).getSnippet(), results.get(i).getLink());
                            resultCount++;
                            programizDataFound = true;
                        }
                    }
                } catch (IOException e) {
                    System.out.println("Error: " + e.getMessage());
                }
            }
            // Message shown when no results are found from selected sites
            if(!programizDataFound && !w3DataFound){
                createNoResultCard(resultsContainer, "We couldn't find anything matching your search. Try using a different keyword or phrase!");
            }

        });

        // VBox for stacking each element
        VBox root = new VBox();

        root.getStyleClass().add("root");
        root.setSpacing(20);
        root.setPadding(new Insets(0, 20, 20, 20));
        root.setAlignment(Pos.TOP_CENTER);
        root.getChildren().addAll(topBar, titleLabel, searchBar, filtersRow, scrollPane);

        // Create Scene
        Scene scene = new Scene(root, 720, 800);
        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());

        // Set the stage title, set the scene, and show
        primaryStage.setTitle("Tutorial Scout");
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/TutorialScout.png")));
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Creates and adds a styled result card to the results container.
     * Each card displays the result number, title, description, and optional filter tags.
     * The entire card is clickable and will open the provided link in the user's browser.
     *
     * @param resultsContainer the VBox to which the new result card will be added
     * @param resultCount the number to display on the card
     * @param langPill the programming language filter applied, used as a visual tag
     * @param tagPill the difficulty level filter applied, used as a visual tag
     * @param title the title of the tutorial or result
     * @param description a brief summary or snippet from the tutorial
     * @param link the URL that opens when the result card is clicked
     */
    public void createResultCard(VBox resultsContainer, int resultCount, String langPill, String tagPill , String title, String description, String link) {
        // Create a new card with the given the
        // result number, language tag, difficulty tag, title, description, and link

        // Result Counter will equal zero if it is a welcome card, instead of result

        String resultNumber;

        // If the result counter is 0, use a welcome heading
        // otherwise, make the heading the result number
        if(resultCount == 0) {
            resultNumber = "Type in the search bar above!";
        } else {
            resultNumber = "Result " + resultCount + ":";
        }


        VBox resultCard = new VBox();
        resultCard.getStyleClass().add("result-card");

        HBox topRow = new HBox();
        topRow.setSpacing(10);
        topRow.setAlignment(Pos.TOP_LEFT);

        Label resultLabel = new Label(resultNumber);
        resultLabel.getStyleClass().add("result-label");

        // Only include the link if it is not the welcome message card
        if(resultCount != 0) {
            // Make whole card clickable
            resultCard.setOnMouseClicked(event -> {
                getHostServices().showDocument(link);
            });
            resultCard.setCursor(Cursor.HAND);
        }

        HBox tagBox = new HBox(10);
        tagBox.setAlignment(Pos.TOP_RIGHT);
        if(tagFound){
            Label tagLabel = new Label(tagPill);
            tagLabel.getStyleClass().add("tag-pill");
            tagBox.getChildren().add(tagLabel);
        }

        if(langFound){
            Label langLabel = new Label(langPill);
            langLabel.getStyleClass().add("tag-pill");
            tagBox.getChildren().add(langLabel);
        }

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        topRow.getChildren().addAll(resultLabel, spacer, tagBox);

        Label titleLabel = new Label(title);
        titleLabel.getStyleClass().add("result-title");

        Label resultSummary = new Label(description);
        resultSummary.getStyleClass().add("result-summary");
        resultCard.getChildren().addAll(topRow, titleLabel, resultSummary);

        resultsContainer.getChildren().add(resultCard);
    }

    /**
     * Creates and adds a "no results found" card to the results container.
     * This card is displayed when no matching tutorials are returned from the selected sources.
     *
     * @param resultsContainer the VBox to which the no-result card will be added
     * @param title the message to display (e.g., "No results found..." or a custom tip)
     */
    public void createNoResultCard(VBox resultsContainer, String title) {
        // Create a new card with the given the
        // result number, language tag, difficulty tag, title, description, and link
        String resultNotFound = "No results found...";

        VBox resultCard = new VBox();
        resultCard.getStyleClass().add("result-card");

        HBox topRow = new HBox();
        topRow.setSpacing(10);
        topRow.setAlignment(Pos.TOP_LEFT);

        Label resultLabel = new Label(resultNotFound);
        resultLabel.getStyleClass().add("result-label");

        topRow.getChildren().addAll(resultLabel);

        Label titleLabel = new Label(title);
        titleLabel.getStyleClass().add("result-title");

        resultCard.getChildren().addAll(topRow, titleLabel);

        resultsContainer.getChildren().add(resultCard);
    }
}