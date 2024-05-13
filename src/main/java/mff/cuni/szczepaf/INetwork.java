package mff.cuni.szczepaf;

public interface INetwork {

    void loadNodes(String filename, NodeCondition condition);
    void export(String filename);

    void createEdges(EdgeCondition condition);

    void visualize();
}