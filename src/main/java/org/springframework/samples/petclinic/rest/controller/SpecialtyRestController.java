package org.springframework.samples.petclinic.rest.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.petclinic.mapper.SpecialtyMapper;
import org.springframework.samples.petclinic.model.Specialty;
import org.springframework.samples.petclinic.rest.api.SpecialtiesApi;
import org.springframework.samples.petclinic.rest.dto.SpecialtyDto;
import org.springframework.samples.petclinic.service.ClinicService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@CrossOrigin(exposedHeaders = "errors, content-type")
@RequestMapping("api")
public class SpecialtyRestController implements SpecialtiesApi {

	private final ClinicService clinicService;

	private final SpecialtyMapper specialtyMapper;

	public SpecialtyRestController(ClinicService clinicService, SpecialtyMapper specialtyMapper) {
		this.clinicService = clinicService;
		this.specialtyMapper = specialtyMapper;
	}

	@PreAuthorize("hasRole(@roles.VET_ADMIN)") //vet管理者権限のみアクセス可能
	@Override
	public ResponseEntity<List<SpecialtyDto>> listSpecialties() {
		List<SpecialtyDto> specialties = new ArrayList<>();
		specialties.addAll(specialtyMapper.toSpecialtyDtos(this.clinicService.findAllSpecialties()));
		if (specialties.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);//NotFound(404)のエラーを出力
		}
		return new ResponseEntity<>(specialties, HttpStatus.OK);//OK(200)のステータスを出力
	}

	@PreAuthorize("hasRole(@roles.VET_ADMIN)") //vet管理者権限のみアクセス可能
	@Override
	public ResponseEntity<SpecialtyDto> getSpecialty(Integer specialtyId) {
		Specialty specialty = this.clinicService.findSpecialtyById(specialtyId);
		if (specialty == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);//NotFound(404)のエラーを出力
		}
		return new ResponseEntity<>(specialtyMapper.toSpecialtyDto(specialty), HttpStatus.OK);//OK(200)のステータスを出力
	}

	@PreAuthorize("hasRole(@roles.VET_ADMIN)") //vet管理者権限のみアクセス可能
	@Override
	public ResponseEntity<SpecialtyDto> addSpecialty(SpecialtyDto specialtyDto) {
		HttpHeaders headers = new HttpHeaders();
		//登録処理
		Specialty specialty = specialtyMapper.toSpecialty(specialtyDto);
		this.clinicService.saveSpecialty(specialty);
		headers.setLocation(UriComponentsBuilder.newInstance().path("/api/specialties/{id}")
				.buildAndExpand(specialty.getId()).toUri());
		return new ResponseEntity<>(specialtyMapper.toSpecialtyDto(specialty), headers, HttpStatus.CREATED);//CREATED(201)のステータスを出力
	}

	@PreAuthorize("hasRole(@roles.VET_ADMIN)") //vet管理者権限のみアクセス可能
	@Override
	public ResponseEntity<SpecialtyDto> updateSpecialty(Integer specialtyId, SpecialtyDto specialtyDto) {
		Specialty currentSpecialty = this.clinicService.findSpecialtyById(specialtyId);
		if (currentSpecialty == null)
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);//NotFound(404)のエラーを出力
		//更新処理
		currentSpecialty.setName(specialtyDto.getName());
		this.clinicService.saveSpecialty(currentSpecialty);
		return new ResponseEntity<>(specialtyMapper.toSpecialtyDto(currentSpecialty), HttpStatus.NO_CONTENT);//NOCONTENT(204)のステータスを出力
	}

	@PreAuthorize("hasRole(@roles.VET_ADMIN)") //vet管理者権限のみアクセス可能
	@Override
	public ResponseEntity<SpecialtyDto> deleteSpecialty(Integer specialtyId) {
		Specialty specialty = this.clinicService.findSpecialtyById(specialtyId);
		if (specialty == null)
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);//NotFound(404)のエラーを出力
		//削除処理
		this.clinicService.deleteSpecialty(specialty);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);//NOCONTENT(204)のステータスを出力
	}

}
