package org.springframework.samples.petclinic.repository.springdatajpa;

import org.springframework.context.annotation.Profile;
import org.springframework.data.repository.Repository;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.repository.VetRepository;

@Profile("spring-data-jpa")
public interface SpringDataVetRepository extends VetRepository, Repository<Vet, Integer> {
}
