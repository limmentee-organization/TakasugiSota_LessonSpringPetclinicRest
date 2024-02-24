package org.springframework.samples.petclinic.mapper;

import java.util.Collection;

import org.mapstruct.Mapper;
import org.springframework.samples.petclinic.model.Specialty;
import org.springframework.samples.petclinic.rest.dto.SpecialtyDto;

@Mapper
public interface SpecialtyMapper {
	Specialty toSpecialty(SpecialtyDto specialtyDto);

	SpecialtyDto toSpecialtyDto(Specialty specialty);

	Collection<SpecialtyDto> toSpecialtyDtos(Collection<Specialty> specialties);

	Collection<Specialty> toSpecialtys(Collection<SpecialtyDto> specialties);

}
