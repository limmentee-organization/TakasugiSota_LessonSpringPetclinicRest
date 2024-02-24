package org.springframework.samples.petclinic.repository;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Specialty;

public interface SpecialtyRepository {

	Specialty findById(int id) throws DataAccessException;

	List<Specialty> findSpecialtiesByNameIn(Set<String> names);

	Collection<Specialty> findAll() throws DataAccessException;

	void save(Specialty specialty) throws DataAccessException;

	void delete(Specialty specialty) throws DataAccessException;

}
