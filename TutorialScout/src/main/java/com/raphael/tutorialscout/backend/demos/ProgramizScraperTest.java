
package com.raphael.tutorialscout.backend.demos;

import com.raphael.tutorialscout.backend.ProgramizScraper;
import com.raphael.tutorialscout.backend.api.ApyHubSummaryClient;

import java.io.IOException;
import java.util.List;

public class ProgramizScraperTest {
    public static void main(String[] args) {
        ProgramizScraper scraper = new ProgramizScraper();
        String testQuery = "java array";
        int count = 1;
        ApyHubSummaryClient apy = new ApyHubSummaryClient();
        String summary;
        try{
            List<ProgramizScraper.ProgramizResult> results = scraper.search(testQuery);
            if(!results.isEmpty()){
                System.out.println("Found " + results.size() + " results");
                for(ProgramizScraper.ProgramizResult result : results) {
                    System.out.println("Result " + count++ + ":");
                    System.out.println(result);
//                    if(result.getFullText() != null && !result.getFullText().isEmpty()){
//                        summary = apy.summarizeText(result.getFullText());
//                        System.out.println("Summary: " + summary);
//                    }
                    System.out.println("---------------------------------------");
                }
                //System.out.println(results.get(2).getFullText());

            } else{
                System.out.println("No results found");
            }

        } catch (IOException e){
            System.out.println("Error: " + e.getMessage() + "");
        }
    }
}
