package org.springframework.samples.petclinic.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Visit;

public interface VisitRepository {

	void save(Visit visit) throws DataAccessException;

	List<Visit> findByPetId(Integer petId);

	Visit findById(int id) throws DataAccessException;

	Collection<Visit> findAll() throws DataAccessException;

	void delete(Visit visit) throws DataAccessException;

}
