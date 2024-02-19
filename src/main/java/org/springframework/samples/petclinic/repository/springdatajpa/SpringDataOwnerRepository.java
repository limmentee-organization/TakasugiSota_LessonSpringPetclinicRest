package org.springframework.samples.petclinic.repository.springdatajpa;

import java.util.Collection;

import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.repository.OwnerRepository;

@Profile("spring-data-jpa")
public interface SpringDataOwnerRepository extends OwnerRepository, Repository<Owner, Integer> {

	@Override
	@Query("SELECT DISTINCT owner FROM Owner owner left join fetch owner.pets WHERE owner.lastName LIKE :lastName%")
	//lastnameに引数lastNameが含まれるownerテーブルを取得
	Collection<Owner> findByLastName(@Param("lastName") String lastName);

	@Override
	@Query("SELECT owner FROM Owner owner left join fetch owner.pets WHERE owner.id =:id")
	//idが引数idと一致するownerテーブルを取得
	Owner findById(@Param("id") int id);
}
