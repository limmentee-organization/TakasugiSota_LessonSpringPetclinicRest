package org.springframework.samples.petclinic.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetType;

public interface PetRepository {

	List<PetType> findPetTypes() throws DataAccessException;

	Pet findById(int id) throws DataAccessException;

	void save(Pet pet) throws DataAccessException;

	Collection<Pet> findAll() throws DataAccessException;

	void delete(Pet pet) throws DataAccessException;

}
