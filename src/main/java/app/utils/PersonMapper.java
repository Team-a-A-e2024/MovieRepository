package app.utils;

import app.dtos.CastDTO;
import app.entities.Person;

import java.util.ArrayList;
import java.util.List;

public class PersonMapper {

    public static List<Person> CastDTOtoPersonEntity(List<CastDTO> castDTOs) {
        List<Person> persons = new ArrayList<>();

        for (CastDTO cast : castDTOs) {
            persons.add(Person.builder()
                    .id(cast.getId())
                    .name(cast.getName())
                    .build()
            );
        }
        return persons;
    }
}