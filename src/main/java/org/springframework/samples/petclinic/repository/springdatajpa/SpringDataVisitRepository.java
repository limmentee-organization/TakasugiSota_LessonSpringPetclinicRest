package org.springframework.samples.petclinic.repository.springdatajpa;

import org.springframework.context.annotation.Profile;
import org.springframework.data.repository.Repository;
import org.springframework.samples.petclinic.model.Visit;
import org.springframework.samples.petclinic.repository.VisitRepository;

@Profile("spring-data-jpa")
public interface SpringDataVisitRepository
		extends VisitRepository, Repository<Visit, Integer>, VisitRepositoryOverride {
}
