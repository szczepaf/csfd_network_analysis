package mff.cuni.szczepaf;

public interface INetwork {
    void addNode(IMediaEntity entity);
    <ConditionFunction> void createEdges(ConditionFunction f);
    void export(String filename);
}