package mff.cuni.szczepaf;

public class Person implements IMediaEntity{

    public Person(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    private String name;
}
