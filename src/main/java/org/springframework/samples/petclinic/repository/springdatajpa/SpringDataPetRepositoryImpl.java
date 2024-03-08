package org.springframework.samples.petclinic.repository.springdatajpa;

import org.springframework.context.annotation.Profile;
import org.springframework.samples.petclinic.model.Pet;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Profile("spring-data-jpa")
public class SpringDataPetRepositoryImpl implements PetRepositoryOverride {

	@PersistenceContext
	private EntityManager em;

	@Override
	public void delete(Pet pet) {
		String petId = pet.getId().toString();
		this.em.createQuery("DELETE FROM Visit visit WHERE pet.id=" + petId).executeUpdate();
		this.em.createQuery("DELETE FROM Pet pet WHERE id=" + petId).executeUpdate();
		if (em.contains(pet)) {
			em.remove(pet);
		}
	}

}
