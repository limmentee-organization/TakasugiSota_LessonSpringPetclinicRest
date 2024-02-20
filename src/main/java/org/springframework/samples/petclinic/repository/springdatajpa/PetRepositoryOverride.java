package org.springframework.samples.petclinic.repository.springdatajpa;

import org.springframework.context.annotation.Profile;
import org.springframework.samples.petclinic.model.Pet;

@Profile("spring-data-jpa")
//spring-data-jpa"がプロファイルされている場合、以下のインターフェースを用いる
public interface PetRepositoryOverride {

	void delete(Pet pet);

}
