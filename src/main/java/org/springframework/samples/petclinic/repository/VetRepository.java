package org.springframework.samples.petclinic.repository;

import java.util.Collection;

import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Vet;

public interface VetRepository {

	Collection<Vet> findAll() throws DataAccessException;

	Vet findById(int id) throws DataAccessException;

	void save(Vet vet) throws DataAccessException;

	void delete(Vet vet) throws DataAccessException;

}
