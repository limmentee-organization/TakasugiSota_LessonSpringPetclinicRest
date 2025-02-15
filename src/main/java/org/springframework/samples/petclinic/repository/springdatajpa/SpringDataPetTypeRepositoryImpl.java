package org.springframework.samples.petclinic.repository.springdatajpa;

import java.util.List;

import org.springframework.context.annotation.Profile;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.model.Visit;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Profile("spring-data-jpa")
public class SpringDataPetTypeRepositoryImpl implements PetTypeRepositoryOverride {

	@PersistenceContext
	private EntityManager em;

	@SuppressWarnings("unchecked")
	@Override
	public void delete(PetType petType) {
		this.em.remove(this.em.contains(petType) ? petType : this.em.merge(petType));
		Integer petTypeId = petType.getId();

		List<Pet> pets = this.em.createQuery("SELECT pet FROM Pet pet WHERE type.id=" + petTypeId).getResultList();
		for (Pet pet : pets) {
			List<Visit> visits = pet.getVisits();
			for (Visit visit : visits) {
				this.em.createQuery("DELETE FROM Visit visit WHERE id=" + visit.getId()).executeUpdate();
			}
			this.em.createQuery("DELETE FROM Pet pet WHERE id=" + pet.getId()).executeUpdate();
		}
		this.em.createQuery("DELETE FROM PetType pettype WHERE id=" + petTypeId).executeUpdate();
	}

}
