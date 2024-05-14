package mff.cuni.szczepaf;

/**
 * One of the implementations of IMediaEntity. It represents any Person associated with some Media.
 * That can be an Actor, Director (as it is currently), but can be extended by other professions in the future.
 */
public class Person implements IMediaEntity{

    public Person(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    /**
     * By "name" we mean the first name and surname of a person.
     */
    private String name;

    /**
     * Let's assume that two people are identical if they share the same name.
     * CSFD is strict about having differences in names for different actors (distinguishing them e.g. by suffixes like "sr.", "jr.", etc.)
     */
    @Override
    public boolean equals(Object obj) {

        if (obj == this) return true;

        if (obj == null || obj.getClass() != this.getClass()) return false;
        Person person = (Person) obj;
        return name.equals(person.name);
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }
}
