package mff.cuni.szczepaf;

public class Main {

    public static void main(String[] args) {

        // String searchParams = "rlW0rKOyVwcoZS0fVzqyoaWyVwc7VwRvBygqYPVlVwcoKFjvZlV6J10fVwDvBygqYPW0rKOyVwblsFjvo3WcM2yhVwc7VwRvBygqYPVlVwcoKFjvZlV6JmRfZGx3KFjvAPV6J10fVaE5pTHvBwA9YPW5MJSlK2Mlo20vBwR5BQxfVayyLKWsqT8vBwVjZwDfVaWuqTyhM19zpz9gVwbkYPWlLKEcozqsqT8vBwRjZPjvqTSaVwcoKFjvLJA0o3VvBygqYPWxnKWyL3EipvV6J10fVzAioKOip2IlVwcoKFjvp2AlMJIhq3WcqTIlVwcoKFjvLKI0nT9lVwcoKFjvL2yhMJ1uqT9apzSjnTIlVwcoKFjvpUWiMUIwqTyiovV6J10fVzIxnKDvBygqYPWmo3IhMPV6J10fVaAwMJ5iM3WupTu5VwcoKFjvoJSmnlV6J10fVzAip3E1oJImVwcoKFjvL29hMTy0nJ9hplV6J119";
        String sourceName = "FilmLinks/porevolucniCeskyCeskoslovenskyFilmURLs.csv";

        String targetName = "FilmData/porevolucniCeskyCeskoslovenskyFilm.csv";
        // Controller.downloadFilmsFromLinks(sourceName, targetName, 5);

        // String conditionJSONString = "{\"duration\":[\"<120\", \">50\"], \"dateCreated\": [\"=2015\"], \"rating\":[\">80.5\"], \"actors\":[{\"contains\":\"Ondřej Brzobohatý\"}, {\"contains\": \"Ivan Trojan\"}, {\"notcontains\":\"Jan Werich\"}]}";
        String conditionJSONString = "{\"actors\":[{\"contains\":\"Ondřej Gregor Brzobohatý\"}]}";

        FilmCondition fc = ConditionFactory.createConditionFromJson(conditionJSONString);
        FilmNetwork network = new FilmNetwork();

        network.loadFilms(targetName, fc);
        network.printNodes();

    }

}