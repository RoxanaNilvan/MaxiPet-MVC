package com.onlineshop.maxipetbackend.services;

import com.onlineshop.maxipetbackend.repositories.PersonRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.onlineshop.maxipetbackend.dtos.PersonDTO;
import com.onlineshop.maxipetbackend.entities.Person;
import com.onlineshop.maxipetbackend.dtos.mappers.PersonMapper;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PersonService {
    private static final Logger LOGGER = LoggerFactory.getLogger(PersonService.class);
    private final PersonRepository personRepository;

    public List<PersonDTO> findPersons() {
        List<Person> personList = personRepository.findAll();
        return personList.stream()
                .map(PersonMapper::toPersonDTO)
                .collect(Collectors.toList());
    }

    public PersonDTO findPersonById(UUID id) {
        Optional<Person> personOptional = personRepository.findById(id);
        if (!personOptional.isPresent()) {
            LOGGER.error("Person with id {} was not found in db", id);
        }
        return PersonMapper.toPersonDetailsDTO(personOptional.get());
    }

    public UUID insert(PersonDTO personDTO) {
        Person person = PersonMapper.toEntity(personDTO);
        person = personRepository.save(person);
        LOGGER.debug("Person with id {} was inserted in db", person.getId());
        return person.getId();
    }

}
