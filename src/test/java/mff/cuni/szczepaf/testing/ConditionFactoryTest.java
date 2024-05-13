package mff.cuni.szczepaf.testing;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import mff.cuni.szczepaf.*;

import java.util.List;

public class ConditionFactoryTest {

    @Test
    public void testCreateConditionFromJson_actorsContains() {
        String conditionJSONString = "{\"actors\":[{\"contains\":\"Ondřej Gregor Brzobohatý\"}]}";
        NodeCondition condition = ConditionFactory.createNodeConditionFromJson(conditionJSONString);

        Film film = new Film(120, "Interstellar", 2020,
                List.of(new Director("Christopher Nolan")),
                List.of(new Actor("Ondřej Gregor Brzobohatý"), new Actor("Ivan Trojan"), new Actor("Leonardo Di Caprio")),
                new Rating(85.0F, 100));

        assertTrue(condition.meetsCondition(film), "Condition should return true for film containing the specified actor.");
    }

    @Test
    public void testCreateConditionFromJson_actorsNotContains() {
        String conditionJSONString = "{\"actors\":[{\"notcontains\":\"Jan Werich\"}]}";
        NodeCondition condition = ConditionFactory.createNodeConditionFromJson(conditionJSONString);

        Film film = new Film(120, "Interstellar", 2020,
                List.of(new Director("Christopher Nolan")),
                List.of(new Actor("Ondřej Gregor Brzobohatý"), new Actor("Ivan Trojan"), new Actor("Leonardo Di Caprio")),
                new Rating(85.0F, 100));

        assertTrue(condition.meetsCondition(film), "Condition should return true for film not containing the specified actor.");
    }

    @Test
    public void testCreateConditionFromJson_ratingLessThan() {
        String conditionJSONString = "{\"rating\":[\"<85.0\"]}";
        NodeCondition condition = ConditionFactory.createNodeConditionFromJson(conditionJSONString);

        Film film = new Film(120, "Interstellar", 2020,
                List.of(new Director("Christopher Nolan")),
                List.of(new Actor("Ondřej Gregor Brzobohatý"), new Actor("Ivan Trojan"), new Actor("Leonardo Di Caprio")),
                new Rating(52.1324F, 100));

        assertTrue(condition.meetsCondition(film), "Condition should return true for films with rating less than 85.0.");
    }

    @Test
    public void testCreateConditionFromJson_durationMoreThan() {
        String conditionJSONString = "{\"duration\":[\">120\"]}";
        NodeCondition condition = ConditionFactory.createNodeConditionFromJson(conditionJSONString);

        Film film = new Film(150, "Long Film", 2015,
                List.of(),
                List.of(),
                new Rating(0, 0));

        assertTrue(condition.meetsCondition(film), "Condition should return true for films with a duration more than 100 minutes.");
    }
    @Test
    public void testCreateConditionFromJson_createdInYear() {
        String conditionJSONString = "{\"dateCreated\":[\"=2020\"]}";
        NodeCondition condition = ConditionFactory.createNodeConditionFromJson(conditionJSONString);

        Film film = new Film(100, "Recent Film", 2020,
                List.of(),
                List.of(),
                new Rating(0, 0));

        assertTrue(condition.meetsCondition(film), "Condition should return true for films created in the year 2020.");
    }


    @Test
    public void testCreateConditionFromJson_complexConditions() {
        String conditionJSONString = "{\"duration\":[\">100\"], \"rating\":[\"<85.0\"], \"dateCreated\":[\"=2019\"], \"actors\":[{\"contains\":\"Christian Bale\"}]}";
        NodeCondition condition = ConditionFactory.createNodeConditionFromJson(conditionJSONString);

        Film film = new Film(120, "Interstellar", 2019,
                List.of(new Director("Jan Hřebejk")),
                List.of(new Actor("Christian Bale")),
                new Rating(80.0F, 150));

        assertTrue(condition.meetsCondition(film), "Complex condition not satisfied.");
    }




}
