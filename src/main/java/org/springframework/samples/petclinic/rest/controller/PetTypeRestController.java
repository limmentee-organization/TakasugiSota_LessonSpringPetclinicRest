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

	@PreAuthorize("hasAnyRole(@roles.OWNER_ADMIN, @roles.VET_ADMIN)")
	@Override
	public ResponseEntity<List<PetTypeDto>> listPetTypes() {
		List<PetType> petTypes = new ArrayList<>(this.clinicService.findAllPetTypes());
		if (petTypes.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(petTypeMapper.toPetTypeDtos(petTypes), HttpStatus.OK);
	}

	@PreAuthorize("hasAnyRole(@roles.OWNER_ADMIN, @roles.VET_ADMIN)")
	@Override
	public ResponseEntity<PetTypeDto> getPetType(Integer petTypeId) {
		PetType petType = this.clinicService.findPetTypeById(petTypeId);
		if (petType == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(petTypeMapper.toPetTypeDto(petType), HttpStatus.OK);
	}

	@PreAuthorize("hasRole(@roles.VET_ADMIN)")
	@Override
	public ResponseEntity<PetTypeDto> addPetType(PetTypeDto petTypeDto) {
		HttpHeaders headers = new HttpHeaders();
		if (Objects.nonNull(petTypeDto.getId()) && !petTypeDto.getId().equals(0)) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} else {
			final PetType type = petTypeMapper.toPetType(petTypeDto);
			this.clinicService.savePetType(type);
			headers.setLocation(
					UriComponentsBuilder.newInstance().path("/api/pettypes/{id}").buildAndExpand(type.getId()).toUri());
			return new ResponseEntity<>(petTypeMapper.toPetTypeDto(type), headers, HttpStatus.CREATED);
		}
	}

	@PreAuthorize("hasRole(@roles.VET_ADMIN)")
	@Override
	public ResponseEntity<PetTypeDto> updatePetType(Integer petTypeId, PetTypeDto petTypeDto) {
		PetType currentPetType = this.clinicService.findPetTypeById(petTypeId);
		if (currentPetType == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		currentPetType.setName(petTypeDto.getName());
		this.clinicService.savePetType(currentPetType);
		return new ResponseEntity<>(petTypeMapper.toPetTypeDto(currentPetType), HttpStatus.NO_CONTENT);
	}

	@PreAuthorize("hasRole(@roles.VET_ADMIN)")
	@Transactional
	@Override
	public ResponseEntity<PetTypeDto> deletePetType(Integer petTypeId) {
		PetType petType = this.clinicService.findPetTypeById(petTypeId);
		if (petType == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		this.clinicService.deletePetType(petType);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

}
