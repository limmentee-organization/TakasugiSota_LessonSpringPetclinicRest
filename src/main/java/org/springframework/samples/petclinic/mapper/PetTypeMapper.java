package org.springframework.samples.petclinic.mapper;

import java.util.Collection;
import java.util.List;

import org.mapstruct.Mapper;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.rest.dto.PetTypeDto;
import org.springframework.samples.petclinic.rest.dto.PetTypeFieldsDto;

@Mapper
public interface PetTypeMapper {

	PetType toPetType(PetTypeDto petTypeDto);

	PetType toPetType(PetTypeFieldsDto petTypeFieldsDto);

	PetTypeDto toPetTypeDto(PetType petType);

	List<PetTypeDto> toPetTypeDtos(Collection<PetType> petTypes);
}
