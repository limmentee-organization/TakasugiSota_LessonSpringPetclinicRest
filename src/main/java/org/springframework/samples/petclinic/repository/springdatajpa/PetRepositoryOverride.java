package org.springframework.samples.petclinic.repository.springdatajpa;

import org.springframework.context.annotation.Profile;
import org.springframework.samples.petclinic.model.Pet;

@Profile("spring-data-jpa")
public interface PetRepositoryOverride {

	void delete(Pet pet);

}
