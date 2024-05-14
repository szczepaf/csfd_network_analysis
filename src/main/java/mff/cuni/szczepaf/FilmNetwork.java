package mff.cuni.szczepaf;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.stream.Stream;

import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.stream.file.FileSinkGraphML;
import org.graphstream.ui.layout.springbox.implementations.SpringBox;
import org.graphstream.ui.view.Viewer;

public class FilmNetwork implements INetwork {


    private ArrayList<Film> nodes = new ArrayList<>();
    private ArrayList<Edge> edges = new ArrayList<>();
    // Edge is a <Film,Film> tuple (see class Edge).

    public FilmNetwork(){

    }

    public FilmNetwork(ArrayList<Film> films){
        this.nodes = films;
    }

    /**
     * Based on the NodeCondition provided by the ConditionFactory, load Films~Nodes from a source filename into the graph.
     * @param filename source filename with the Film Data.
     * @param condition condition to decide on the entry of each film.
     */
    @Override
    public Boolean loadNodes(String filename, NodeCondition condition) {
        FilmParser filmParser = new FilmParser();
        try (Stream<String> stream = Files.lines(Paths.get(filename))) {
            stream.forEach(line -> {
                try {
                    String jsonPart = line.split(":", 2)[1].trim(); // example line: 341532: {"name": "Láska, soudruhu", "duration": 101, "dateCreated": 2013, "directors": ["Taru Mäkelä"], "actors": ["Kati Outinen", "Miroslav Etzler", "Elena Leeve", "Kryštof Hádek", "Esko Salminen", "Laura Birn", "Vesa Vierikko", "Tommi Korpela", "Denny Ratajský", "Petr Stach"], "rating": 55.33869}
                    Film film = filmParser.parseFilmFromDump(jsonPart);
                    if (film != null && condition.meetsCondition(film)) {
                        nodes.add(film);
                    }
                } catch (Exception e) {
                    System.err.println("Failed to parse film: " + e.getMessage());
                }
            });
        } catch (Exception e) {
            System.err.println("Failed to read file: " + e.getMessage());
            return false;
        }
        return true;
    }

    public void printNodes(){
        for (Film film : nodes){
            System.out.println(film);
        }
    }

    /**
     * Create edges between Nodes~Films based on whether they meet a BiPredicate condition.
     * @param condition Condition provided by the Condition Factory.
     */
    @Override
    public void createEdges(EdgeCondition condition) {
        edges.clear();
        for (int i = 0; i < nodes.size(); i++) {
            for (int j = i + 1; j < nodes.size(); j++) {
                Film film1 = nodes.get(i);
                Film film2 = nodes.get(j);
                if (condition.meetsCondition(film1, film2)) {
                    edges.add(new Edge(film1, film2));
                }
            }
        }
    }

    public ArrayList<Film> getNodes() {
        return nodes;
    }

    public ArrayList<Edge> getEdges() {
        return edges;
    }

    /**
     * Uses the graphstream library to visualize the graph.
     * Keep in minds that this is mainly to demonstrate that the right things are happening, for more detailed analysis or visualization I recommend moving to Python and networkx.
     * That should be easy thanks to the export function.
     * Currently, does not plot isolated vertices - as most of them would be isolated.
     */
    @Override
    public void visualize() {
        Graph graph = new SingleGraph("Film Network");
        graph.setAttribute("ui.stylesheet", styleSheet());

        // Track which nodes are connected
        HashSet<String> connectedNodes = new HashSet<>();
        SpringBox layout = new SpringBox();


        // First, determine which nodes are connected by iterating over edges
        for (Edge edge : edges) {
            String sourceId = edge.getFilm1().getName() + "_" + edge.getFilm1().getDateCreated();
            String targetId = edge.getFilm2().getName() + "_" + edge.getFilm2().getDateCreated();
            connectedNodes.add(sourceId);
            connectedNodes.add(targetId);

            // add Film names and dateCreated as labels. Lower the weight so that there is more space around each node.
            if (graph.getNode(sourceId) == null) {
                graph.addNode(sourceId).setAttribute("ui.label", edge.getFilm1().getName() + " (" + edge.getFilm1().getDateCreated() + ")");
                graph.getNode(sourceId).setAttribute("layout.weight", 1);
            }
            if (graph.getNode(targetId) == null) {
                graph.addNode(targetId).setAttribute("ui.label", edge.getFilm2().getName() + " (" + edge.getFilm2().getDateCreated() + ")");
                graph.getNode(sourceId).setAttribute("layout.weight", 1);

            }

            String edgeId = "E" + edge.getFilm1().getName() + "_" + edge.getFilm2().getName();
            if (graph.getEdge(edgeId) == null) {
                graph.addEdge(edgeId, sourceId, targetId);
            }
        }

        // Display the graph
        Viewer viewer = graph.display();
        viewer.setCloseFramePolicy(Viewer.CloseFramePolicy.HIDE_ONLY);

        // Configure the layout
        layout.setForce(0.5);  // Increase the repulsive force to spread nodes out more
        layout.setStabilizationLimit(0); // Stabilize more quickly, even if more imperfect
        viewer.enableAutoLayout(layout);

    }
    /**
     * Exports the graph to a file using the GraphML format. Always exports to the directory Exports.
     * Uses the GraphML format - so give your files the extension .graphml
     * @param filename target file for export
     */
    public void export(String filename) {
        String exportDirectory = "Exports/";
        Graph graph = new SingleGraph("Exported Film Network");
        populateGraph(graph);

        // GraphStream FileSink for exporting graph to GraphML
        FileSinkGraphML graphML = new FileSinkGraphML();

        try {
            graphML.writeAll(graph, exportDirectory + filename);
            System.out.println("Graph exported successfully to " + filename);
        } catch (IOException e) {
            System.err.println("Failed to export graph: " + e.getMessage());
        }
    }

    /**
     * Helper method to ensure all data about films is exported in the GraphML
     */
    private void populateGraph(Graph graph) {
        // Define the stylesheet and attributes
        graph.setAttribute("ui.stylesheet", styleSheet());

        // Add nodes with unique identifiers and detailed labels
        for (Film film : nodes) {
            String uniqueId = film.getName() + "_" + film.getDateCreated();
            Node node = graph.addNode(uniqueId);
            node.setAttribute("ui.label", film.getName() + " (" + film.getDateCreated() + ")");

            // Set custom attribute names according to the Film class properties
            node.setAttribute("duration", film.getDuration());
            node.setAttribute("name", film.getName());
            node.setAttribute("dateCreated", film.getDateCreated());
            node.setAttribute("directors", String.join(", ", film.getDirectorNames()));
            node.setAttribute("actors", String.join(", ", film.getActorNames()));
            node.setAttribute("rating", film.getRating().getRatingValue());
        }

        // Add edges based on existing connections
        for (Edge edge : edges) {
            String sourceId = edge.getFilm1().getName() + "_" + edge.getFilm1().getDateCreated();
            String targetId = edge.getFilm2().getName() + "_" + edge.getFilm2().getDateCreated();
            String edgeId = sourceId + "_" + targetId;
            if (graph.getEdge(edgeId) == null) {
                graph.addEdge(edgeId, sourceId, targetId);
            }
        }
    }

    private String styleSheet() {
        return "node { " +
                "   fill-color: black; " +
                "   size: 10px, 10px; " +
                "   text-alignment: under; " +
                "   text-color: darkblue; " +
                "   text-size: 20px; " +
                "   text-padding: 3px; " +
                "   text-style: bold; " +
                "} " +
                "edge { " +
                "   fill-color: grey; " +
                "   size: 2px; " +
                "}";
        }
    }

