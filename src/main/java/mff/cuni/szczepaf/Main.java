package mff.cuni.szczepaf;

import java.util.Scanner;

public class Main {
    private static FilmNetwork network;
    private static Scanner scanner;

    public static void main(String[] args) {
        System.setProperty("org.graphstream.ui", "swing"); // necessary for graph visualization
        network = new FilmNetwork();
        scanner = new Scanner(System.in);

        System.out.println("Welcome to the Film Network CLI."); // TODO Make this more fancy
        // printHelp();

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
                        fetchFilmLinks();
                case "download films", "d" ->
                        downloadFilms();
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

    private static void loadNodes() {
        System.out.print("Enter JSON string for node condition: ");
        String conditionJSONString = scanner.nextLine().trim();
        NodeCondition fc = ConditionFactory.createNodeConditionFromJson(conditionJSONString);
        if (fc != null) {
            System.out.print("Enter source file name: ");
            String filename = scanner.nextLine().trim();
            Boolean loaded = network.loadNodes(filename, fc);
            if (loaded) System.out.println("Nodes loaded successfully into the Film Graph.");
        }
    }

    private static void createEdges() {
        System.out.print("Enter JSON string for edge condition: ");
        String conditionJSONString = scanner.nextLine().trim();
        EdgeCondition ec = ConditionFactory.createEdgeConditionFromJson(conditionJSONString);
        if (ec != null) {
            network.createEdges(ec);
            System.out.println("Edges created successfully.");
        }
    }

    private static void exportGraph() {
        System.out.print("Enter target file name for export in GraphML format: ");
        String filename = scanner.nextLine().trim();
        network.export(filename);
    }

    private static void fetchFilmLinks() {
        System.out.print("Enter search parameters: ");
        String searchParams = scanner.nextLine().trim();

        System.out.print("Enter timeout between calls in seconds. (Longer is safer. Press enter for default: 30 seconds.)");
        String timeoutString = scanner.nextLine().trim();
        int timeout = 30;
        if (!timeoutString.isEmpty()) timeout = Integer.parseInt(timeoutString);
        System.out.println("Timeout is set to " + timeout);

        System.out.print("Enter target filename for saving film links: ");
        String filename = scanner.nextLine().trim();

        DownloadController.fetchFilmLinksFromSearchParams(searchParams, timeout, filename);
    }

    private static void downloadFilms() {
        System.out.print("Enter source filename with film links: ");
        String sourceFile = scanner.nextLine().trim();

        System.out.print("Enter target filename for saving film data: ");
        String targetFile = scanner.nextLine().trim();

        System.out.print("Enter timeout between calls in seconds. (Longer is safer. Press enter for default: 30 seconds.)");
        String timeoutString = scanner.nextLine().trim();
        int timeout = 30;
        if (!timeoutString.isEmpty()) timeout = Integer.parseInt(timeoutString);
        System.out.println("Timeout is set to " + timeout);

        DownloadController.downloadFilmsFromLinks(sourceFile, targetFile, timeout);
    }

    private static void printHelp() {
        System.out.println("Available commands:");
        System.out.println("  load nodes/l    - Load nodes from a file with a condition on nodes");
        System.out.println("  create edges/c  - Create edges based on a condition for tuples of films");
        System.out.println("  visualize/v     - Visualize the current graph using graphstream");
        System.out.println("  export/x        - Export the graph to a GraphML file");
        System.out.println("  fetch links/f        - Fetch film links from search parameters");
        System.out.println("  download films/d     - Download film data from links");
        System.out.println("  help/h          - Print this help message");
        System.out.println("  exit/e          - Exit the program");

    }

}
