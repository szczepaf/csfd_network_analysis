package mff.cuni.szczepaf.testing;

import mff.cuni.szczepaf.PageDownloader;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class PageTest {

        @Test
        public void testPageDownloadingOnePager() {
            String onePage = getOnePage();

            String expectedOutput = "";
            try (BufferedReader reader = new BufferedReader(new FileReader("OnePageResults.txt"))) {
                StringBuilder builder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                    builder.append(System.lineSeparator());
                }
                expectedOutput = builder.toString();
            } catch (IOException e) {
                fail("Failed to read the expected output file: " + e.getMessage());
            }

            // Parse the expected and downloaded pages to find elements with the classname "film-title-name"
            Document expectedDoc = Jsoup.parse(expectedOutput);
            Document downloadedDoc = Jsoup.parse(onePage);

            Set<String> expectedTitles = new HashSet<>();
            Set<String> downloadedTitles = new HashSet<>();

            // Extract film titles from the expected document
            Elements expectedElements = expectedDoc.select(".film-title-name");
            for (org.jsoup.nodes.Element element : expectedElements) {
                expectedTitles.add(element.text());
            }

            // Extract film titles from the downloaded document
            Elements downloadedElements = downloadedDoc.select(".film-title-name");
            for (org.jsoup.nodes.Element element : downloadedElements) {
                downloadedTitles.add(element.text());
            }

            // Check that at least one title from the expected output is present in the downloaded page
            boolean hasCommonElement = false;
            for (String title : expectedTitles) {
                if (downloadedTitles.contains(title)) {
                    hasCommonElement = true;
                    break;
                }
            }

            assertTrue(hasCommonElement, "No common film titles were found between the expected and returned string - the method for downloading pages most likely does not work.");
        }

    @Test
    public void testPageDownloadingTwoPager() {
        String onePage = getTwoPages();

        String expectedOutput = "";
        try (BufferedReader reader = new BufferedReader(new FileReader("TwoPageResults.txt"))) {
            StringBuilder builder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
                builder.append(System.lineSeparator());
            }
            expectedOutput = builder.toString();
        } catch (IOException e) {
            fail("Failed to read the expected output file: " + e.getMessage());
        }

        // Parse the expected and downloaded pages to find elements with the classname "film-title-name"
        Document expectedDoc = Jsoup.parse(expectedOutput);
        Document downloadedDoc = Jsoup.parse(onePage);

        Set<String> expectedTitles = new HashSet<>();
        Set<String> downloadedTitles = new HashSet<>();

        // Extract film titles from the expected document
        Elements expectedElements = expectedDoc.select(".film-title-name");
        for (org.jsoup.nodes.Element element : expectedElements) {
            expectedTitles.add(element.text());
        }

        // Extract film titles from the downloaded document
        Elements downloadedElements = downloadedDoc.select(".film-title-name");
        for (org.jsoup.nodes.Element element : downloadedElements) {
            downloadedTitles.add(element.text());
        }

        // Check that at least one title from the expected output is present in the downloaded page
        boolean hasCommonElement = false;
        for (String title : expectedTitles) {
            if (downloadedTitles.contains(title)) {
                hasCommonElement = true;
                break;
            }
        }

        assertTrue(hasCommonElement, "No common film titles were found between the expected and returned string - the method for downloading pages most likely does not work.");
    }

    private static String getOnePage() {
        String searchParams = "rlW0rKOyVwcoZS0fVzqyoaWyVwc7VwRvBygqYPVlVwcoKFjvZlV6J10fVwDvBygqYPW0rKOyVwblsFjvo3WcM2yhVwc7VwRvBygqYPVlVwcoKFjvZlV6JmRfZGx3KFjvAPV6J10fVaE5pTHvBwA9YPW5MJSlK2Mlo20vBz51oTjfVayyLKWsqT8vBz51oTjfVaWuqTyhM19zpz9gVwb5BPjvpzS0nJ5aK3EiVwbkZQNfVaEuMlV6J10fVzSwqT9lVwcoKFjvMTylMJA0o3VvBygqYPWwo21jo3AypvV6J10fVaAwpzIyoaqlnKEypvV6J10fVzS1qTuipvV6J10fVzAcozIgLKEiM3WupTuypvV6J10fVaOlo2E1L3Eco24vBygqYPWyMTy0VwcoKFjvp291ozDvBygqYPWmL2Iho2qlLKObrFV6J10fVz1up2fvBygqYPWwo3A0qJ1yplV6J10fVzAiozEcqTyioaZvBygqsD";
        PageDownloader pd = new PageDownloader( 30);
        return pd.downloadByURL(searchParams);
    }

    private static String getTwoPages() {
        String searchParams = "rlW0rKOyVwcoZS0fVzqyoaWyVwc7VwRvBygqYPVlVwcoKFjvZlV6J10fVwDvBygqYPW0rKOyVwblsFjvo3WcM2yhVwc7VwRvBygqYPVlVwcoKFjvZlV6JmRfZGx3KFjvAPV6J10fVaE5pTHvBwA9YPW5MJSlK2Mlo20vBz51oTjfVayyLKWsqT8vBz51oTjfVaWuqTyhM19zpz9gVwb5ZvjvpzS0nJ5aK3EiVwbkZQNfVaEuMlV6J10fVzSwqT9lVwcoKFjvMTylMJA0o3VvBygqYPWwo21jo3AypvV6J10fVaAwpzIyoaqlnKEypvV6J10fVzS1qTuipvV6J10fVzAcozIgLKEiM3WupTuypvV6J10fVaOlo2E1L3Eco24vBygqYPWyMTy0VwcoKFjvp291ozDvBygqYPWmL2Iho2qlLKObrFV6J10fVz1up2fvBygqYPWwo3A0qJ1yplV6J10fVzAiozEcqTyioaZvBygqsD";
        PageDownloader pd = new PageDownloader( 30);
        return pd.downloadByURL(searchParams);
    }
}
