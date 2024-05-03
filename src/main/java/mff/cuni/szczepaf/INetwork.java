package mff.cuni.szczepaf;

public interface INetwork {
    void addNode(IMediaEntity entity);
    <ConditionFunction> void createEdges(ConditionFunction f); // Assuming ConditionFunction is a functional interface you define elsewhere
    void export(String filename);
}