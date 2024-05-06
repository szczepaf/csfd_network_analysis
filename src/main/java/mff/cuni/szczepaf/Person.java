package mff.cuni.szczepaf;

public class Person implements IMediaEntity{

    public Person(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    private String name;


    @Override
    public int hashCode() {
        // Hash the name, if it is null, return some constant, e.g. 0
        return name != null ? name.hashCode() : 0;
    }

    @Override
    public boolean equals(Object obj) {
        // Let's assume that two people are identical if they share the same name.
        // That is of course not ideal, but at the same time, CSFD is strict about having differences in names for different actors.
        // Let's rely on them to be consistent.

        if (obj == this) {
            return true;
        }

        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }

        Person person = (Person) obj;
        return name.equals(person.name);
    }
}
