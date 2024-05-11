package mff.cuni.szczepaf.testing;


import mff.cuni.szczepaf.PageDownloader;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
public class PageTest {

    @Test
    public void testPageDownloadingOnePager(){
        String onePage = getSearchParams();

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

        // Replace the session IDs - they are different every time.
        expectedOutput = expectedOutput.replaceAll("\r\n|\n", "").replaceAll("\"viewId\":\\d+", "\"viewId\":1111");
        onePage = onePage.replaceAll("\r\n|\n", "").replaceAll("\"viewId\":\\d+", "\"viewId\":1111");



    }

    private static String getSearchParams() {
        String searchParams = "rlW0rKOyVwcoZS0fVzqyoaWyVwc7VwRvBygqYPVlVwcoKFjvZlV6J10fVwDvBygqYPW0rKOyVwblsFjvo3WcM2yhVwc7VwRvBygqYPVlVwcoKFjvZlV6JmRfZGx3KFjvAPV6J10fVaE5pTHvBwA9YPW5MJSlK2Mlo20vBz51oTjfVayyLKWsqT8vBz51oTjfVaWuqTyhM19zpz9gVwb5BPjvpzS0nJ5aK3EiVwbkZQNfVaEuMlV6J10fVzSwqT9lVwcoKFjvMTylMJA0o3VvBygqYPWwo21jo3AypvV6J10fVaAwpzIyoaqlnKEypvV6J10fVzS1qTuipvV6J10fVzAcozIgLKEiM3WupTuypvV6J10fVaOlo2E1L3Eco24vBygqYPWyMTy0VwcoKFjvp291ozDvBygqYPWmL2Iho2qlLKObrFV6J10fVz1up2fvBygqYPWwo3A0qJ1yplV6J10fVzAiozEcqTyioaZvBygqsD";
        PageDownloader pd = new PageDownloader( 30);
        return pd.downloadByURL(searchParams);
    }


}
