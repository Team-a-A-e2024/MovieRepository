package app.daos;

import app.config.HibernateConfig;
import app.entities.Person;
import app.populators.PersonPopulator;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Tag("IntegrationTest")
class PersonDAOTest {

    private EntityManagerFactory emf;
    private PersonDAO dao;

    @BeforeEach
    void setup() {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.createNativeQuery("TRUNCATE TABLE person RESTART IDENTITY CASCADE")
                    .executeUpdate();
            em.getTransaction().commit();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @BeforeAll
    void setupOnce() {
        emf = HibernateConfig.getEntityManagerFactoryForTest();
        dao = new PersonDAO(emf);
    }

    @AfterAll
    void teardown() {
        if (emf != null && emf.isOpen()) emf.close();
    }

    @Test
    void createPerson() {
        // Arrange
        Person expected = Person.builder()
                .id(200)
                .name("Keanu Reeves")
                .build();

        // Act
        Person actual = dao.create(expected);

        // Assert
        assertEquals(expected, actual);

        // Assert – hentet fra DB matcher
        Person fromDb = dao.getById(expected.getId()).orElseThrow();
        assertEquals(expected, fromDb);
    }

    @Test
    void getPersonById() {
        // Arrange
        List<Person> seeded = PersonPopulator.populatePersons(dao);
        Person first = seeded.get(0);
        Person expected = Person.builder()
                .id(first.getId())
                .name(first.getName())
                .build();

        // Act
        Person actual = dao.getById(first.getId()).orElseThrow();

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    void getAllPersons() {
        // Arrange
        List<Person> expected = PersonPopulator.populatePersons(dao);

        // Act
        List<Person> actual = dao.getAll();

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    void updatePerson() {
        // Arrange
        Person p = PersonPopulator.populatePersons(dao).get(0);
        Person expected = Person.builder()
                .id(p.getId())
                .name("Neo")
                .build();

        // Act
        Person updated = dao.update(expected);

        // Assert
        assertEquals(expected, updated);
    }

    @Test
    void deletePerson() {
        // Arrange
        Person p = PersonPopulator.populatePersons(dao).get(1);
        boolean expected = true;

        // Act
        boolean actual = dao.delete(p.getId());

        // Assert
        assertEquals(expected, actual);
        assertEquals(Optional.empty(), dao.getById(p.getId()));
    }
}