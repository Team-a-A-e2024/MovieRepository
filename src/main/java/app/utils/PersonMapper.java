package app.utils;

import app.dtos.CastDTO;
import app.dtos.CreditsDTO;
import app.dtos.CrewDTO;
import app.entities.Person;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PersonMapper {

    public static List<Person> castDTOtoPersonEntity(List<CastDTO> castDTOs) {
        List<Person> persons = new ArrayList<>();

        for (CastDTO cast : castDTOs) {
            persons.add(Person.builder()
                    .externalId(cast.getId())
                    .name(cast.getName())
                    .build()
            );
        }
        return persons;
    }

    public static List<Person> mapCrewDTOsToPersonList(List<CrewDTO> dto) {
        List<Person> persons = new ArrayList<>();

        for (CrewDTO cast : dto) {
            persons.add(Person.builder()
                    .externalId(cast.getId())
                    .name(cast.getName())
                    .build()
            );
        }
        return persons;
    }

    public static Set<Person> mapCreditsDTOsToPersonSet(List<CreditsDTO> creditsDTOs) {
        Set<Person> persons = new HashSet<>();

        creditsDTOs.forEach(credits -> persons.addAll(castDTOtoPersonEntity(credits.getCast())));
        creditsDTOs.forEach(credits -> persons.addAll(mapCrewDTOsToPersonList(credits.getCrew())));

        return persons;
    }
}