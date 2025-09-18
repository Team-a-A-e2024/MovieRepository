package app.populators;

import app.daos.PersonDAO;
import app.entities.Person;

import java.util.List;

public class PersonPopulator {

    public static List<Person> populatePersons(PersonDAO personDAO) {
        Person p1 = Person.builder()
                .id(200).name("Keanu Reeves")
                .build();
        Person p2 = Person.builder()
                .id(201)
                .name("Brad Pitt")
                .build();
        Person p3 = Person.builder()
                .id(202)
                .name("Leonardo DiCaprio")
                .build();

        personDAO.create(p1);
        personDAO.create(p2);
        personDAO.create(p3);

        return List.of(p1, p2, p3);
    }
}