package app.daos;

import app.config.HibernateConfig;
import app.entities.Person;
import app.populators.PersonPopulator;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PersonDAOTest {

    private EntityManagerFactory emf;
    private PersonDAO dao;

    @BeforeEach
    void setup() {
        emf = HibernateConfig.getEntityManagerFactoryForTest();
        dao = new PersonDAO(emf);
    }

    @AfterEach
    void teardown() {
        if (emf != null && emf.isOpen()) emf.close();
    }

    // Bruges kun til createAll-testen, så vi ikke kalder populatoren to gange
    private List<Person> buildPersons() {
        return List.of(
                Person.builder().id(210).name("Helper Person 1").build(),
                Person.builder().id(211).name("Helper Person 2").build(),
                Person.builder().id(212).name("Helper Person 3").build()
        );
    }

    @Test
    void createPerson() {
        // Arrange + Act
        List<Person> persons = PersonPopulator.populatePersons(dao);

        // Assert
        assertFalse(persons.isEmpty());
        assertEquals("Keanu Reeves", persons.get(0).getName());
    }

    @Test
    void createAllPerson() {
        // Arrange
        List<Person> toCreate = buildPersons();

        // Act
        List<Person> created = dao.createAll(toCreate);

        // Assert (return-værdi)
        assertEquals(3, created.size());
        assertTrue(created.stream().anyMatch(p -> p.getName().equals("Helper Person 1")));
        assertTrue(created.stream().anyMatch(p -> p.getName().equals("Helper Person 2")));
        assertTrue(created.stream().anyMatch(p -> p.getName().equals("Helper Person 3")));

        // Assert (mod DB)
        List<Person> all = dao.getAll();
        assertTrue(all.size() >= 3);
    }

    @Test
    void getPersonById() {
        // Arrange
        List<Person> persons = PersonPopulator.populatePersons(dao);
        Integer id = persons.get(0).getId(); // fx 200

        // Act
        Optional<Person> found = dao.getById(id);

        // Assert
        assertTrue(found.isPresent());
        assertEquals("Keanu Reeves", found.get().getName());
    }

    @Test
    void getAllPersons() {
        // Arrange
        PersonPopulator.populatePersons(dao);

        // Act
        List<Person> all = dao.getAll();

        // Assert
        assertTrue(all.size() >= 3);
        assertTrue(all.stream().anyMatch(p -> "Keanu Reeves".equals(p.getName())));
        assertTrue(all.stream().anyMatch(p -> "Brad Pitt".equals(p.getName())));
        assertTrue(all.stream().anyMatch(p -> "Leonardo DiCaprio".equals(p.getName())));
    }

    @Test
    void updatePerson() {
        // Arrange
        Person p = PersonPopulator.populatePersons(dao).get(0);

        // Act
        Person updated = dao.update(
                Person.builder()
                        .id(p.getId())
                        .name("Neo")
                        .build()
        );

        // Assert
        assertEquals("Neo", updated.getName());
    }

    @Test
    void deletePerson() {
        // Arrange
        Person p = PersonPopulator.populatePersons(dao).get(1);

        // Act
        boolean deleted = dao.delete(p.getId());

        // Assert
        assertTrue(deleted);
        assertTrue(dao.getById(p.getId()).isEmpty());
    }
}