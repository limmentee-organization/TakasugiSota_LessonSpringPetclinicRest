package org.springframework.samples.petclinic.repository.springdatajpa;

import org.springframework.context.annotation.Profile;
import org.springframework.samples.petclinic.model.Specialty;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Profile("spring-data-jpa")
public class SpringDataSpecialtyRepositoryImpl implements SpecialtyRepositoryOverride {

	@PersistenceContext
	private EntityManager em;

	@Override
	public void delete(Specialty specialty) {
		this.em.remove(this.em.contains(specialty) ? specialty : this.em.merge(specialty));//引数のspecialtyがemに存在していたらそのまま削除、なければemにマージして削除
		Integer specId = specialty.getId();
		this.em.createNativeQuery("DELETE FROM vet_specialties WHERE specialty_id=" + specId).executeUpdate();//vet_pecialty削除クエリを実行するためのQueryのインスタンスを作成し実行
		this.em.createQuery("DELETE FROM Specialty specialty WHERE id=" + specId).executeUpdate();//Specialty削除クエリを実行するためのQueryのインスタンスを作成し実行
	}

}
