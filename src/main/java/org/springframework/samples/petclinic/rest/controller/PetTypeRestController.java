package org.springframework.samples.petclinic.rest.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.petclinic.mapper.PetTypeMapper;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.rest.api.PettypesApi;
import org.springframework.samples.petclinic.rest.dto.PetTypeDto;
import org.springframework.samples.petclinic.service.ClinicService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.transaction.Transactional;

@RestController
@CrossOrigin(exposedHeaders = "errors, content-type")
@RequestMapping("api")
public class PetTypeRestController implements PettypesApi {

	private final ClinicService clinicService;
	private final PetTypeMapper petTypeMapper;

	public PetTypeRestController(ClinicService clinicService, PetTypeMapper petTypeMapper) {
		this.clinicService = clinicService;
		this.petTypeMapper = petTypeMapper;
	}

	@PreAuthorize("hasAnyRole(@roles.OWNER_ADMIN, @roles.VET_ADMIN)") //owner,vet管理者権限のみアクセス可能
	@Override
	public ResponseEntity<List<PetTypeDto>> listPetTypes() {
		List<PetType> petTypes = new ArrayList<>(this.clinicService.findAllPetTypes());
		if (petTypes.isEmpty())
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);//NotFound(404)のエラーを出力
		return new ResponseEntity<>(petTypeMapper.toPetTypeDtos(petTypes), HttpStatus.OK);//OK(200)のステータスを出力
	}

	@PreAuthorize("hasAnyRole(@roles.OWNER_ADMIN, @roles.VET_ADMIN)") //owner,vet管理者権限のみアクセス可能
	@Override
	public ResponseEntity<PetTypeDto> getPetType(Integer petTypeId) {
		PetType petType = this.clinicService.findPetTypeById(petTypeId);
		if (petType == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);//NotFound(404)のエラーを出力
		}
		return new ResponseEntity<>(petTypeMapper.toPetTypeDto(petType), HttpStatus.OK);//OK(200)のステータスを出力
	}

	@PreAuthorize("hasRole(@roles.VET_ADMIN)") //vet管理者権限のみアクセス可能
	@Override
	public ResponseEntity<PetTypeDto> addPetType(PetTypeDto petTypeDto) {
		HttpHeaders headers = new HttpHeaders();
		if (Objects.nonNull(petTypeDto.getId()) && !petTypeDto.getId().equals(0)) {//petidが参照できて、かつ0出ない場合
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);//BADREQUEST(400)のエラーを出力
		} else {
			//登録処理
			final PetType type = petTypeMapper.toPetType(petTypeDto);
			this.clinicService.savePetType(type);
			headers.setLocation(
					UriComponentsBuilder.newInstance().path("/api/pettypes/{id}").buildAndExpand(type.getId()).toUri());
			return new ResponseEntity<>(petTypeMapper.toPetTypeDto(type), headers, HttpStatus.CREATED);//CREATED(201)のステータスを出力
		}
	}

	@PreAuthorize("hasRole(@roles.VET_ADMIN)") //vet管理者権限のみアクセス可能
	@Override
	public ResponseEntity<PetTypeDto> updatePetType(Integer petTypeId, PetTypeDto petTypeDto) {
		PetType currentPetType = this.clinicService.findPetTypeById(petTypeId);
		if (currentPetType == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);//NotFound(404)のエラーを出力
		}
		//更新処理
		currentPetType.setName(petTypeDto.getName());
		this.clinicService.savePetType(currentPetType);
		return new ResponseEntity<>(petTypeMapper.toPetTypeDto(currentPetType), HttpStatus.NO_CONTENT);//NOCONTENT(204)のステータスを出力
	}

	@PreAuthorize("hasRole(@roles.VET_ADMIN)") //vet管理者権限のみアクセス可能
	@Transactional
	@Override
	public ResponseEntity<PetTypeDto> deletePetType(Integer petTypeId) {
		PetType petType = this.clinicService.findPetTypeById(petTypeId);
		if (petType == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);//NotFound(404)のエラーを出力
		}
		//削除処理
		this.clinicService.deletePetType(petType);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);//NOCONTENT(204)のステータスを出力
	}

}
