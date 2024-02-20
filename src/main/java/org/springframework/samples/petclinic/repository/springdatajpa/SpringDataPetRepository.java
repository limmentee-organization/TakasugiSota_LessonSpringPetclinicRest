package org.springframework.samples.petclinic.repository.springdatajpa;

import java.util.List;

import org.springframework.context.annotation.Profile;
import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.repository.PetRepository;

@Profile("spring-data-jpa")
public interface SpringDataPetRepository extends PetRepository, Repository<Pet, Integer>, PetRepositoryOverride {

	@Override
	@Query("SELECT ptype FROM PetType ptype ORDER BY ptype.name")
	//pettypeテーブルをptype.nameの昇順で取得
	List<PetType> findPetTypes() throws DataAccessException;
}
