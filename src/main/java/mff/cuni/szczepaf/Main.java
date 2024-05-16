package mff.cuni.szczepaf;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

/**
 * Main provides a console-like interface to sends commands to visualize, export, etc. the Network that is being analyzed.
 * Most methods here are just wrappers.
 * In each docstring, there is short information, but more importantly the place where you should search fore more detailed description of the given function.
 * The visualize method is called directly as a method of the Network, so see the Network class for more details (method network.visualize).
 * To list all possible commands, enter the command for help (h/help).
 */
public class Main {
    private static FilmNetwork network;
    private static Scanner scanner;

    private static String linkDirectory = "FilmLinks/";
    private static String dataDirectory = "FilmData/";

    private static final ArrayList<String> VALID_NODE_KEYS = new ArrayList<String>(Arrays.asList("rating", "actors", "directors", "dateCreated", "duration"));
    private static final ArrayList<String> VALID_EDGE_KEYS = new ArrayList<String> (Arrays.asList("commonActors", "commonDirector"));


    public static void main(String[] args) {



        System.setProperty("org.graphstream.ui", "swing"); // necessary for graph visualization
        network = new FilmNetwork();
        scanner = new Scanner(System.in);

        System.out.println("Welcome to the Film Network Console."); // TODO Make this more fancy
        printHelp();

        String command = "";
        while (true) {
            System.out.println("\nEnter a command: ");
            command = scanner.nextLine().trim().toLowerCase();

            switch (command) {
                case "load nodes", "l" ->
                    loadNodes();
                case "create edges", "c" ->
                    createEdges();
                case "visualize", "v" ->
                    network.visualize();
                case "export", "x" ->
                    exportGraph();
                case "fetch links", "f" ->
                        fetchMediaLinks();
                case "download", "d" ->
                        downloadMediaEntities();
                case "help", "h" ->
                    printHelp();
                case "exit", "e" -> {
                    System.out.println("Exiting program.");
                    scanner.close();
                    System.exit(0);
                    // This definitely is not the most graceful way to exit the program.
                    // However, the motivation is that after visualizing with the library graphstream, the program keeps running even after it should terminate in standard behaviour.

                }
                default ->
                    System.out.println("Unknown command. Type 'help' for a list of commands.");
            }
        }
    }


    /**
     * Wrapper method that scans a Node Condition, reads the source file with the Data about Nodes
     * and loads selected nodes into the network.
     */
    private static void loadNodes() {
        String conditionJSONString = getValidNodeCondition();
        if (conditionJSONString != null) {
            NodeCondition fc = ConditionFactory.createNodeConditionFromJson(conditionJSONString);
            if (fc != null) {
                String filename = getValidSourceFile(dataDirectory);
                if (filename != null) {
                    Boolean loaded = network.loadNodes(filename, fc, false);
                    if (loaded) System.out.println("Nodes loaded successfully into the Film Graph.");
                }
            }
        }
    }


    /**
     * Wrapper method that scans for an Edge Condition and created edges based on it.
     * See ConditionFactory class for more details.
     */
    private static void createEdges() {
        String conditionJSONString = getValidEdgeCondition();
        if (conditionJSONString != null) {
            EdgeCondition ec = ConditionFactory.createEdgeConditionFromJson(conditionJSONString);
            if (ec != null) {
                network.createEdges(ec);
                System.out.println("Edges created successfully.");
            }
        }
    }

    /**
     * Wrapper method for exporting the Graph in GraphML format to a target file.
     * See FilmNetwork Class for more details.
     * All exports are automatically included in the directory Exports and should have the format .graphml.
     */
    private static void exportGraph() {
        System.out.print("Enter target file name for export in GraphML format: ");
        String filename = scanner.nextLine().trim();
        network.export(filename);
    }

    /**
     * Wrapper method that fetches URL links from the search results of a detailed query at CSFD
     * (See DownloadController class for more info).
     * The target file is always in the directory FilmLinks, but the user needs not to include this prefix, it is automatically included.
     */
    private static void fetchMediaLinks() {
        System.out.print("Enter search parameters: ");
        String searchParams = scanner.nextLine().trim();

        int timeout = getValidTimeout();

        System.out.print("Enter target filename for saving film links: ");
        String filename = scanner.nextLine().trim();

        DownloadController.fetchFilmLinksFromSearchParams(searchParams, timeout, filename);
    }


    /**
     * Wrapper method that reads the URL links to MediaEntites (e.g. Films) and dumps their data into a target file.
     * The process is gradually saved and on a re-run will continue where it left off.
     * The source file with links is always located in the directory FilmLinks, but the user does not to write this prefix, it is automatically included.
     * Likewise, it holds for the target file and the directory FilmData.
     * See DownloadController Class for more info.
     */
    private static void downloadMediaEntities() {
        String sourceFile = getValidSourceFile(linkDirectory);
        if (sourceFile != null) {
            System.out.print("Enter target filename for saving media data: ");
            String targetFile = scanner.nextLine().trim();

            int timeout = getValidTimeout();

            DownloadController.downloadMediaFromLinks(sourceFile, targetFile, timeout);
        }
    }

    private static void printHelp() {
        System.out.println("Available commands:");
        System.out.println("  load nodes/l     - Load nodes from a file with a condition on nodes");
        System.out.println("  create edges/c   - Create edges based on a condition for tuples of films");
        System.out.println("  visualize/v      - Visualize the current graph using graphstream");
        System.out.println("  export/x         - Export the graph to a GraphML file");
        System.out.println("  fetch links/f    - Fetch film links from search parameters");
        System.out.println("  download films/d - Download film data from links");
        System.out.println("  help/h           - Print this help message");
        System.out.println("  exit/e           - Exit the program");

    }

    /**
     * Method that reads a timeout value in an int.
     */
    private static int getValidTimeout() {
        int timeout = 30; // default value
        while (true) {
            System.out.print("Enter timeout between calls in seconds. (Longer is safer. Press enter for default: 30 seconds.)");
            String timeoutString = scanner.nextLine().trim();
            if (timeoutString.isEmpty()) {
                break;
            }
            try {
                timeout = Integer.parseInt(timeoutString);
                break;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
        System.out.println("Timeout is set to " + timeout);
        return timeout;
    }

    /**
     * Method that reads a source file name and checks if it exists.
     * @return The valid source file name or null if the file doesn't exist.
     */
    private static String getValidSourceFile(String directory) {
        System.out.print("Enter source filename: ");
        String sourceFile = scanner.nextLine().trim();
        if (!Files.exists(Paths.get(directory + sourceFile))) {
            System.out.println("File does not exist.");
            return null;
        }
        return sourceFile;
    }


    /**
     * Method that reads and validates a JSON string for node condition.
     * @return The valid JSON string for node condition or null if invalid.
     */
    private static String getValidNodeCondition() {
        System.out.print("Enter JSON string for node condition: ");
        String conditionJSONString = scanner.nextLine().trim();

        try {
            JSONObject conditions = new JSONObject(conditionJSONString);

            for (String key : conditions.keySet()) {
                if (!VALID_NODE_KEYS.contains(key)) {
                    System.out.println("Invalid key: " + key);
                    System.out.println("Valid keys for node condition are: " + String.join(", ", VALID_NODE_KEYS));
                    return null;
                }
            }

            for (String key : conditions.keySet()) {
                if (!(conditions.get(key) instanceof JSONArray)) {
                    System.out.println("Value for key " + key + " should be an array. Refer to the user manual.");
                    return null;
                }
            }
            return conditionJSONString;

        } catch (JSONException e) {
            System.out.println("Invalid JSON format. Please check your input.");
            return null;
        }
    }

    /**
     * Method that reads and validates a JSON string for edge condition.
     * @return The valid JSON string for edge condition or null if invalid.
     */
    private static String getValidEdgeCondition() {
        System.out.print("Enter JSON string for edge condition: ");
        String conditionJSONString = scanner.nextLine().trim();

        try {
            JSONObject conditions = new JSONObject(conditionJSONString);

            for (String key : conditions.keySet()) {
                if (!VALID_EDGE_KEYS.contains(key)) {
                    System.out.println("Invalid key: " + key);
                    System.out.println("Valid keys for edge condition are: " + String.join(", ", VALID_EDGE_KEYS));
                    return null;
                }
            }
            return conditionJSONString;

        } catch (JSONException e) {
            System.out.println("Invalid JSON format. Please check your input.");
            return null;
        }
    }

}
