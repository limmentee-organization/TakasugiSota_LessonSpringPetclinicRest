package org.springframework.samples.petclinic.repository;

import java.util.Collection;

import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.PetType;

public interface PetTypeRepository {

	PetType findById(int id) throws DataAccessException;

	PetType findByName(String name) throws DataAccessException;

	Collection<PetType> findAll() throws DataAccessException;

	void save(PetType petType) throws DataAccessException;

	void delete(PetType petType) throws DataAccessException;

}
