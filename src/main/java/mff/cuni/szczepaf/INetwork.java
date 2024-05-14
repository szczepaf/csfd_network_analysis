package mff.cuni.szczepaf;

/**
 * Common interface for all networks. Right now, only the FilmNetwork is implemented.
 * This provides the possibility to implement SeriesNetworks, ActorNetworks, DirectorNetworks etc. in the Future.
 */
public interface INetwork {

    /**
     * Reads a source file with the Nodes (that can be Film, Actors, etc.)
     * and adds them to its node list, but only if they satisfy the Node Condition.
     * @param filename Source file
     * @param condition Condition a node has to satisfy to be added
     * @return true if all is parsed correctly, false if some problem occurs.
     */
    Boolean loadNodes(String filename, NodeCondition condition);

    /**
     * Exports the network in some legible format.
     * Currently, GraphML is the format that is used.
     * @param filename target filename to export to.
     */
    void export(String filename);

    /**
     * Creates edges between two nodes, but only if the two nodes satisfy an edge condition.
     * @param condition the edge condition two nodes have to satisfy for them to be linked.
     */
    void createEdges(EdgeCondition condition);

    /**
     * Plot the Network.
     * Currently, the graphstream library is used.
     */
    void visualize();
}