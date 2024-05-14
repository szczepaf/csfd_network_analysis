package mff.cuni.szczepaf;

/**
 * A functional interface that will be used to decide whether two nodes should be connected with an edge.
 * Used in the ConditionFactory class.
 */
@FunctionalInterface

public interface EdgeCondition {

    /**
     * Decide whether two nodes satisfy a condition for an edge to be added.
     * @param f node
     * @param g node
     * @return true or false, if the condition succeeds/does not succeed
     */
    boolean meetsCondition(IMediaEntity f, IMediaEntity g);

}
