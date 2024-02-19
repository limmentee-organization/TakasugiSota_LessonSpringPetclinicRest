package org.springframework.samples.petclinic.rest.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.petclinic.mapper.PetMapper;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.rest.api.PetsApi;
import org.springframework.samples.petclinic.rest.dto.PetDto;
import org.springframework.samples.petclinic.service.ClinicService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(exposedHeaders = "errors, content-type")
@RequestMapping("api")
public class PetRestController implements PetsApi {

	private final ClinicService clinicService;

	private final PetMapper petMapper;

	public PetRestController(ClinicService clinicService, PetMapper petMapper) {
		this.clinicService = clinicService;
		this.petMapper = petMapper;
	}

	@PreAuthorize("hasRole(@roles.OWNER_ADMIN)") //owner管理者権限のみアクセス可能
	@Override
	public ResponseEntity<PetDto> getPet(Integer petId) {
		PetDto pet = petMapper.toPetDto(this.clinicService.findPetById(petId));
		if (pet == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);//NotFound(404)のエラーを出力
		}
		return new ResponseEntity<>(pet, HttpStatus.OK);//OK(200)のステータスを出力
	}

	@PreAuthorize("hasRole(@roles.OWNER_ADMIN)") //owner管理者権限のみアクセス可能
	@Override
	public ResponseEntity<List<PetDto>> listPets() {
		List<PetDto> pets = new ArrayList<>(petMapper.toPetsDto(this.clinicService.findAllPets()));
		if (pets.isEmpty())
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);//NotFound(404)のエラーを出力
		return new ResponseEntity<>(pets, HttpStatus.OK);//OK(200)のステータスを出力
	}

	@PreAuthorize("hasRole(@roles.OWNER_ADMIN)") //owner管理者権限のみアクセス可能
	@Override
	public ResponseEntity<PetDto> updatePet(Integer petId, PetDto petDto) {
		Pet currentPet = this.clinicService.findPetById(petId);
		if (currentPet == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);//NotFound(404)のエラーを出力
		}
		currentPet.setBirthDate(petDto.getBirthDate());
		currentPet.setName(petDto.getName());
		currentPet.setType(petMapper.toPetType(petDto.getType()));
		this.clinicService.savePet(currentPet);
		return new ResponseEntity<>(petMapper.toPetDto(currentPet), HttpStatus.NO_CONTENT);//NOCONTENT(204)のステータスを出力
	}

	@PreAuthorize("hasRole(@roles.OWNER_ADMIN)") //owner管理者権限のみアクセス可能
	@Override
	public ResponseEntity<PetDto> deletePet(Integer petId) {
		Pet pet = this.clinicService.findPetById(petId);
		if (pet == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);//NotFound(404)のエラーを出力
		}
		this.clinicService.deletePet(pet);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);//NOCONTENT(204)のステータスを出力
	}

	@PreAuthorize("hasRole(@roles.OWNER_ADMIN)") //owner管理者権限のみアクセス可能
	@Override
	public ResponseEntity<PetDto> addPet(PetDto petDto) {
		this.clinicService.savePet(petMapper.toPet(petDto));
		return new ResponseEntity<>(petDto, HttpStatus.OK);//OK(200)のステータスを出力　なぜCREATEではない？
	}
}
