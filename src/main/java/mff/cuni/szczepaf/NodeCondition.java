package mff.cuni.szczepaf;

/**
 * A functional interface that will be used to decide whether a Node should be added to a Network.
 * Used in the ConditionFactory class.
 */
@FunctionalInterface
public interface NodeCondition {

    /**
     * Decide whether a node satisfies a condition for it to be added to a node list.
     * @param node a node that will be examined
     * @return true or false, if the condition succeeds/does not succeed
     */
    boolean meetsCondition(IMediaEntity node);
}
