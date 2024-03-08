package org.springframework.samples.petclinic.repository;

import java.util.Collection;

import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Owner;

public interface OwnerRepository {

	Collection<Owner> findByLastName(String lastName) throws DataAccessException;

	Owner findById(int id) throws DataAccessException;

	void save(Owner owner) throws DataAccessException;

	Collection<Owner> findAll() throws DataAccessException;

	void delete(Owner owner) throws DataAccessException;

}
