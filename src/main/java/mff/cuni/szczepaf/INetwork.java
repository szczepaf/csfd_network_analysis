package mff.cuni.szczepaf;

public interface INetwork {

    Boolean loadNodes(String filename, NodeCondition condition);
    void export(String filename);

    void createEdges(EdgeCondition condition);

    void visualize();
}