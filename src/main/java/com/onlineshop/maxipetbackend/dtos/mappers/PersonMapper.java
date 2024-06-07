package com.onlineshop.maxipetbackend.dtos.mappers;

import com.onlineshop.maxipetbackend.dtos.PersonDTO;
import com.onlineshop.maxipetbackend.entities.Person;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class PersonMapper {
    public static PersonDTO toPersonDTO(Person person) {
        return new PersonDTO(person.getId(), person.getName(), person.getAddress(), person.getAge());
    }

    public static PersonDTO toPersonDetailsDTO(Person person) {
        return new PersonDTO(person.getId(), person.getName(), person.getAddress(), person.getAge());
    }

    public static Person toEntity(PersonDTO personDTO) {
        return new Person(personDTO.getId(),
                personDTO.getName(),
                personDTO.getAddress(),
                personDTO.getAge());
    }
}
