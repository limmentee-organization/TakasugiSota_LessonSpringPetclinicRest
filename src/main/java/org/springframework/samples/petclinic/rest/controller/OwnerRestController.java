package org.springframework.samples.petclinic.rest.controller;

import java.util.Collection;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.petclinic.mapper.OwnerMapper;
import org.springframework.samples.petclinic.mapper.PetMapper;
import org.springframework.samples.petclinic.mapper.VisitMapper;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.model.Visit;
import org.springframework.samples.petclinic.rest.api.OwnersApi;
import org.springframework.samples.petclinic.rest.dto.OwnerDto;
import org.springframework.samples.petclinic.rest.dto.OwnerFieldsDto;
import org.springframework.samples.petclinic.rest.dto.PetDto;
import org.springframework.samples.petclinic.rest.dto.PetFieldsDto;
import org.springframework.samples.petclinic.rest.dto.VisitDto;
import org.springframework.samples.petclinic.rest.dto.VisitFieldsDto;
import org.springframework.samples.petclinic.service.ClinicService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.transaction.Transactional;

@RestController
@CrossOrigin(exposedHeaders = "errors, content-type")
@RequestMapping("/api")
public class OwnerRestController implements OwnersApi {

	private final ClinicService clinicService;

	private final OwnerMapper ownerMapper;

	private final PetMapper petMapper;

	private final VisitMapper visitMapper;

	public OwnerRestController(ClinicService clinicService,
			OwnerMapper ownerMapper,
			PetMapper petMapper,
			VisitMapper visitMapper) {
		this.clinicService = clinicService;
		this.ownerMapper = ownerMapper;
		this.petMapper = petMapper;
		this.visitMapper = visitMapper;
	}

	@PreAuthorize("hasRole(@roles.OWNER_ADMIN)") //owner管理者権限のみアクセス可能
	@Override
	public ResponseEntity<List<OwnerDto>> listOwners(String lastName) {//ResponseEntity：HTTP応答の状態コード、ヘッダや本文を制御する方法を提供
		Collection<Owner> owners;
		if (lastName != null) {
			owners = this.clinicService.findOwnerByLastName(lastName);//lastNameで検索がかけられた場合、一致するものを取得
		} else {
			owners = this.clinicService.findAllOwners();//検索がない場合、全て取得
		}
		if (owners.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);//NotFound(404)のエラーを出力
		}
		return new ResponseEntity<>(ownerMapper.toOwnerDtoCollection(owners), HttpStatus.OK);//OK(200)のステータスを出力
	}

	@PreAuthorize("hasRole(@roles.OWNER_ADMIN)") //owner管理者権限のみアクセス可能
	@Override
	public ResponseEntity<OwnerDto> getOwner(Integer ownerId) {
		Owner owner = this.clinicService.findOwnerById(ownerId);
		if (owner == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);//NotFound(404)のエラーを出力
		}
		return new ResponseEntity<>(ownerMapper.toOwnerDto(owner), HttpStatus.OK);//OK(200)のステータスを出力
	}

	@PreAuthorize("hasRole(@roles.OWNER_ADMIN)") //owner管理者権限のみアクセス可能
	@Override
	public ResponseEntity<OwnerDto> addOwner(OwnerFieldsDto ownerFieldsDto) {
		HttpHeaders headers = new HttpHeaders();//HTTP リクエストまたはレスポンスヘッダーを表すデータ構造
		//登録処理
		Owner owner = ownerMapper.toOwner(ownerFieldsDto);
		this.clinicService.saveOwner(owner);
		OwnerDto ownerDto = ownerMapper.toOwnerDto(owner);
		//Location ヘッダーの指定に従って、リソースの（新しい）場所を設定
		headers.setLocation(UriComponentsBuilder.newInstance()
				.path("/api/owners/{id}").buildAndExpand(owner.getId()).toUri());//"/api/owners/{id}"のURIを作成
		return new ResponseEntity<>(ownerDto, headers, HttpStatus.CREATED);//CREATED(201)のステータスを出力
	}

	@PreAuthorize("hasRole(@roles.OWNER_ADMIN)") //owner管理者権限のみアクセス可能
	@Override
	public ResponseEntity<OwnerDto> updateOwner(Integer ownerId, OwnerFieldsDto ownerFieldsDto) {
		Owner currentOwner = this.clinicService.findOwnerById(ownerId);
		if (currentOwner == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		//更新処理
		currentOwner.setAddress(ownerFieldsDto.getAddress());
		currentOwner.setCity(ownerFieldsDto.getCity());
		currentOwner.setFirstName(ownerFieldsDto.getFirstName());
		currentOwner.setLastName(ownerFieldsDto.getLastName());
		currentOwner.setTelephone(ownerFieldsDto.getTelephone());
		this.clinicService.saveOwner(currentOwner);
		return new ResponseEntity<>(ownerMapper.toOwnerDto(currentOwner), HttpStatus.NO_CONTENT);//NOCONTENT(204)のステータスを出力
	}

	@PreAuthorize("hasRole(@roles.OWNER_ADMIN)") //owner管理者権限のみアクセス可能
	@Transactional
	@Override
	public ResponseEntity<OwnerDto> deleteOwner(Integer ownerId) {
		Owner owner = this.clinicService.findOwnerById(ownerId);
		if (owner == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);//NotFound(404)のエラーを出力
		}
		//削除処理
		this.clinicService.deleteOwner(owner);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);//NOCONTENT(204)のステータスを出力
	}

	@PreAuthorize("hasRole(@roles.OWNER_ADMIN)") //owner管理者権限のみアクセス可能
	@Override
	public ResponseEntity<PetDto> addPetToOwner(Integer ownerId, PetFieldsDto petFieldsDto) {
		HttpHeaders headers = new HttpHeaders();
		//petを追加
		Pet pet = petMapper.toPet(petFieldsDto);
		Owner owner = new Owner();
		owner.setId(ownerId);
		pet.setOwner(owner);
		PetType petType = this.clinicService.findPetTypeByName(pet.getType().getName());
		pet.setType(petType);
		this.clinicService.savePet(pet);
		PetDto petDto = petMapper.toPetDto(pet);
		headers.setLocation(UriComponentsBuilder.newInstance().path("/api/pets/{id}")
				.buildAndExpand(pet.getId()).toUri());//"/api/pets/{id}"のURIを作成
		return new ResponseEntity<>(petDto, headers, HttpStatus.CREATED);//CREATED(201)のステータスを出力
	}

	@PreAuthorize("hasRole(@roles.OWNER_ADMIN)") //owner管理者権限のみアクセス可能
	@Override
	public ResponseEntity<VisitDto> addVisitToOwner(Integer ownerId, Integer petId, VisitFieldsDto visitFieldsDto) {
		HttpHeaders headers = new HttpHeaders();
		//visitを追加
		Visit visit = visitMapper.toVisit(visitFieldsDto);
		Pet pet = new Pet();
		pet.setId(petId);
		visit.setPet(pet);
		this.clinicService.saveVisit(visit);
		VisitDto visitDto = visitMapper.toVisitDto(visit);
		headers.setLocation(UriComponentsBuilder.newInstance().path("/api/visits/{id}")
				.buildAndExpand(visit.getId()).toUri());//"/api/visits/{id}"のURIを作成
		return new ResponseEntity<>(visitDto, headers, HttpStatus.CREATED);//CREATED(201)のステータスを出力
	}

	@PreAuthorize("hasRole(@roles.OWNER_ADMIN)") //owner管理者権限のみアクセス可能
	@Override
	public ResponseEntity<PetDto> getOwnersPet(Integer ownerId, Integer petId) {
		Owner owner = this.clinicService.findOwnerById(ownerId);
		Pet pet = this.clinicService.findPetById(petId);
		if (owner == null || pet == null) {//存在しない場合
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);//NotFound(404)のエラーを出力
		} else {
			if (!pet.getOwner().equals(owner)) {//petテーブルのowner情報がownerに存在しない場合
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);//BADREQUEST(400)のエラーを出力
			} else {
				return new ResponseEntity<>(petMapper.toPetDto(pet), HttpStatus.OK);//OK(200)のステータスを出力
			}
		}
	}
}
