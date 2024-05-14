package mff.cuni.szczepaf;

/**
 * Common interface for all parsers that receive HTML data and turn it into a MediaEntity.
 * Currently, the implementations are ActorParser and FilmParser.
 */
public interface IParser {
    /**
     * Turn the source HTML into a Media Entity.
     * @param HTML the source HTML
     * @return an implementation of IMediaEntity
     */
    public IMediaEntity parse(String HTML);
}