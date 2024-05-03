package mff.cuni.szczepaf;

public class DirectorParser implements IParser {
    public IMediaEntity parse(String HTML) {
        return new Director();
    }
}