package org.springframework.samples.petclinic.rest.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.petclinic.mapper.VisitMapper;
import org.springframework.samples.petclinic.model.Visit;
import org.springframework.samples.petclinic.rest.api.VisitsApi;
import org.springframework.samples.petclinic.rest.dto.VisitDto;
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
public class VisitRestController implements VisitsApi {

	private final ClinicService clinicService;

	private final VisitMapper visitMapper;

	public VisitRestController(ClinicService clinicService, VisitMapper visitMapper) {
		this.clinicService = clinicService;
		this.visitMapper = visitMapper;
	}

	@PreAuthorize("hasRole(@roles.OWNER_ADMIN)")
	@Override
	public ResponseEntity<List<VisitDto>> listVisits() {
		List<Visit> visits = new ArrayList<>(this.clinicService.findAllVisits());
		if (visits.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);//NotFound(404)のエラーを出力
		}
		return new ResponseEntity<>(new ArrayList<>(visitMapper.toVisitsDto(visits)), HttpStatus.OK);//OK(200)のステータスを出力
	}

	@PreAuthorize("hasRole(@roles.OWNER_ADMIN)")
	@Override
	public ResponseEntity<VisitDto> getVisit(Integer visitId) {
		Visit visit = this.clinicService.findVisitById(visitId);
		if (visit == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);//NotFound(404)のエラーを出力
		}
		return new ResponseEntity<>(visitMapper.toVisitDto(visit), HttpStatus.OK);//OK(200)のステータスを出力
	}

	@PreAuthorize("hasRole(@roles.OWNER_ADMIN)")
	@Override
	public ResponseEntity<VisitDto> addVisit(VisitDto visitDto) {
		HttpHeaders headers = new HttpHeaders();
		//登録処理
		Visit visit = visitMapper.toVisit(visitDto);
		this.clinicService.saveVisit(visit);
		visitDto = visitMapper.toVisitDto(visit);
		headers.setLocation(
				UriComponentsBuilder.newInstance().path("/api/visits/{id}").buildAndExpand(visit.getId()).toUri());
		return new ResponseEntity<>(visitDto, headers, HttpStatus.CREATED);//CREATED(201)のステータスを出力
	}

	@PreAuthorize("hasRole(@roles.OWNER_ADMIN)")
	@Override
	public ResponseEntity<VisitDto> updateVisit(Integer visitId, VisitDto visitDto) {
		Visit currentVisit = this.clinicService.findVisitById(visitId);
		if (currentVisit == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);//NotFound(404)のエラーを出力
		}
		//更新処理
		currentVisit.setDate(visitDto.getDate());
		currentVisit.setDescription(visitDto.getDescription());
		this.clinicService.saveVisit(currentVisit);
		return new ResponseEntity<>(visitMapper.toVisitDto(currentVisit), HttpStatus.NO_CONTENT);//NOCONTENT(204)のステータスを出力
	}

	@PreAuthorize("hasRole(@roles.OWNER_ADMIN)")
	@Transactional
	@Override
	public ResponseEntity<VisitDto> deleteVisit(Integer visitId) {
		Visit visit = this.clinicService.findVisitById(visitId);
		if (visit == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);//NotFound(404)のエラーを出力
		}
		//削除処理
		this.clinicService.deleteVisit(visit);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);//NOCONTENT(204)のステータスを出力
	}

}
