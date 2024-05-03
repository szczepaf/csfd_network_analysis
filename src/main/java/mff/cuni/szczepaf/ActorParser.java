package mff.cuni.szczepaf;

public class ActorParser implements IParser {
    public IMediaEntity parse(String HTML) {
        return new Actor();
    }
}
