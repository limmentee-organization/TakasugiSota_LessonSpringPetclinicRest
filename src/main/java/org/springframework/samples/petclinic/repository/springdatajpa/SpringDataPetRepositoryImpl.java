package org.springframework.samples.petclinic.repository.springdatajpa;

import org.springframework.context.annotation.Profile;
import org.springframework.samples.petclinic.model.Pet;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Profile("spring-data-jpa")
public class SpringDataPetRepositoryImpl implements PetRepositoryOverride {

	@PersistenceContext //EntityManagerのリファレンスを定義するアノテーション
	private EntityManager em;//永続コンテキストへの接続

	@Override
	public void delete(Pet pet) {
		String petId = pet.getId().toString();
		this.em.createQuery("DELETE FROM Visit visit WHERE pet.id=" + petId).executeUpdate();//Visit削除クエリを実行するためのQueryのインスタンスを作成し実行
		this.em.createQuery("DELETE FROM Pet pet WHERE id=" + petId).executeUpdate();//Pet削除クエリを実行するためのQueryのインスタンスを作成し実行
		if (em.contains(pet))
			em.remove(pet);//削除対象のpetテーブルをemから削除する
	}

}
